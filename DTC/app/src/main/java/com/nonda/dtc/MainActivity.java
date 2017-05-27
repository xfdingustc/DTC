package com.nonda.dtc;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.example.captain_miao.grantap.ListenerPermission;
import com.example.captain_miao.grantap.listeners.PermissionListener;
import com.github.captain_miao.android.ble.BleCallback;
import com.github.captain_miao.android.ble.BleScanner;
import com.github.captain_miao.android.ble.BluetoothHelper;
import com.github.captain_miao.android.ble.ConnectCallback;
import com.github.captain_miao.android.ble.SimpleScanCallback;
import com.github.captain_miao.android.ble.constant.BleConnectState;
import com.github.captain_miao.android.ble.constant.BleScanState;
import com.github.captain_miao.android.ble.constant.ConnectError;
import com.github.captain_miao.android.ble.utils.BleUtils;
import com.github.captain_miao.android.ble.utils.HexUtil;
import com.nonda.dtc.app.AppLog;
import com.nonda.dtc.ble.AppBluetoothHelper;
import com.nonda.dtc.model.BleDevice;
import com.nonda.dtc.model.ObdData;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PermissionListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_OPEN_BLE = 1;
    private static final int REQUEST_CODE_OPEN_GPS = 2;
    private SimpleFragmentPagerAdapter mAdapter;
    private AppBluetoothHelper mBleHelper;
    private BleScanner mBleScanner;
    private BleDevice mBleDevice;

    private MaterialDialog mBleScanDialog;

    private StringBuilder mNotificationBuilder = new StringBuilder();
    private EventBus mEventBus = EventBus.getDefault();

    @BindView(R.id.view_pager)
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
        scanBleDevice();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBleScanner.stopBleScan();
    }

    private void scanBleDevice() {
        mBleScanner = new BleScanner(this, new SimpleScanCallback() {
            @Override
            public void onBleScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                Logger.t(TAG).d("onBle scan: " + device.toString() + " name : " + device.getName());
                if (!TextUtils.isEmpty(device.getName()) && device.getName().equals("OBD BLE")) {
                    mBleScanner.stopBleScan();
                    mBleScanDialog.setContent(R.string.ble_find);
                    String address = device.getAddress();
                    mBleDevice = new BleDevice(address, device.getAddress(),
                            rssi, HexUtil.encodeHexStr(scanRecord), false);

                    mBleHelper = new AppBluetoothHelper(MainActivity.this);
                    mBleHelper.setBleCallback(mBleCallback);
                    mBleHelper.bindService(new BluetoothHelper.OnBindListener() {
                        @Override
                        public void onServiceConnected() {
                            Logger.t(TAG).d("on service connect");
                            connectDevice(mBleDevice.address);
                        }
                    });
                }

            }

            @Override
            public void onBleScanFailed(BleScanState scanState) {
                Logger.t(TAG).d("scan failed: " + scanState.getMessage());

            }
        });

        Logger.t(TAG).d("start scan");
        checkPermissionAndStartScan();
        mBleScanDialog = new MaterialDialog.Builder(this)
                .progress(true, 100)
                .theme(Theme.DARK)
                .progressIndeterminateStyle(false)
                .content(R.string.ble_scanning)
                .show();

    }

    private void initViews() {
        mAdapter = new SimpleFragmentPagerAdapter(getFragmentManager());
        mAdapter.addFragment(new DeviceFragment());
        mAdapter.addFragment(new BasicFragment());
        viewPager.setAdapter(mAdapter);
    }

    private BleCallback mBleCallback = new BleCallback() {
        @Override
        public void onFailed(String msg) {
            Logger.t(TAG).i("onFailed " + msg);
        }

        @Override
        public void onDescriptorWrite(UUID uuid, int status) {
            Logger.t(TAG).i("onDescriptorWrite: " + BleUtils.getGattStatus(status));

        }

        @Override
        public void onCharacteristicRead(UUID uuid, byte[] data) {
            String values = new String(data);

            Logger.t(TAG).i("onCharacteristicRead: " + values);

        }

        @Override
        public void onCharacteristicNotification(UUID uuid, byte[] data) {
            String values = new String(data);
            if (values.startsWith("BD$")) {
                String notification = mNotificationBuilder.toString();
                Logger.t(TAG).i("onCharacteristicNotification: " + notification);
                ObdData obdData = ObdData.fromString(notification);
                if (obdData != null) {
                    mEventBus.post(obdData);
                }
                mNotificationBuilder = new StringBuilder(values);
            } else {
                mNotificationBuilder.append(values);
            }


        }

        @Override
        public void onCharacteristicWrite(UUID uuid, int status) {
            Logger.t(TAG).i("onCharacteristicWrite: " + BleUtils.getGattStatus(status));

        }

        @Override
        public void onConnectionStateChange(int status, int newStatus) {
            BleConnectState connectState = BleConnectState.getBleConnectState(newStatus);
            Logger.t(TAG).i("connect status changed " + AppBluetoothHelper.getConnectStateForShow(MainActivity.this, connectState.getCode()));

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            //服务发现成功
            if (gatt != null && status == BluetoothGatt.GATT_SUCCESS) {
                //displayGattServices(gatt.getServices());
                Logger.t(TAG).i("Discovered success");
                List<BluetoothGattService> serviceList = gatt.getServices();
                for (BluetoothGattService service : serviceList) {
                    Logger.t(TAG).d("service: " + service.getUuid());
                    List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();
                    for (BluetoothGattCharacteristic characteristic : gattCharacteristics) {
                        Logger.t(TAG).d("charater: " + characteristic.getUuid());
                        if (characteristic.getUuid().toString().startsWith("00002b11")) {
                            mBleHelper.readFromCharacteristic(service.getUuid(), characteristic.getUuid());
                        }
                    }
                }
            } else {
                Logger.t(TAG).i("Discovered fail");
            }
        }
    };


    public void connectDevice(String deviceMac) {
        Logger.t(TAG).d("connect device: " + deviceMac);
        mBleScanDialog.setContent(R.string.connecting);
        mBleHelper.connectDevice(deviceMac, new ConnectCallback() {
            @Override
            public void onConnectSuccess() {
                mBleHelper.mConnCallback = null;
                Logger.t(TAG).d("connect success");
                mBleScanDialog.setContent(R.string.connect_success);
                viewPager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBleScanDialog.dismiss();
                    }
                }, 2000);
            }

            @Override
            public void onConnectFailed(ConnectError error) {
                Logger.t(TAG).d("on connect failed");
                mBleScanDialog.setContent(R.string.connect_failed);
            }
        });
    }

    String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};

    public void checkPermissionAndStartScan() {
        checkPermissions();
    }

    private void checkPermissions() {
        ListenerPermission.from(this)
                .setPermissionListener(this)
                .setPermissions(permissions)
                .setRationaleMsg(R.string.label_request_permission_content)
                .setRationaleConfirmText("OK")
                .setDeniedMsg(R.string.label_permission_denial_content)
                .setDeniedCloseButtonText(R.string.label_ok)
                .setGotoSettingButton(false)
                .check();
    }

    @Override
    public void permissionGranted() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && !BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(mIntent, 1);
        } else if (bluetoothAdapter != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isGpsOPen(this)) {
                    // permissions is already available
                    mBleScanner.startBleScan();
                } else {
                    displayPromptForEnablingGPS();
                }
            } else {
                // permissions is already available
                mBleScanner.startBleScan();
            }
        }
    }

    @Override
    public void permissionDenied() {

    }

    public static final boolean isGpsOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

    public void displayPromptForEnablingGPS() {
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;


        new MaterialDialog.Builder(this)
                .title(R.string.label_request_open_gps_title)
                .content(R.string.label_request_open_gps_content)
                .positiveText(R.string.label_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        startActivityForResult(new Intent(action), REQUEST_CODE_OPEN_GPS);
                    }
                })
                .negativeText(R.string.label_cancel)
                .cancelable(true)
                .negativeText(R.string.label_cancel)
                .show();
    }

}

