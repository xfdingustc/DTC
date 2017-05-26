package com.nonda.dtc;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SimpleFragmentPagerAdapter mAdapter;
    private AppBluetoothHelper mBleHelper;
    private BleScanner mBleScanner;
    private BleDevice mBleDevice;

    private MaterialDialog mBleScanDialog;

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

    private void scanBleDevice() {
        mBleScanner = new BleScanner(this, new SimpleScanCallback() {
            @Override
            public void onBleScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                Logger.t(TAG).d("onBle scan: " + device.toString() + " name : " + device.getName());
                if (!TextUtils.isEmpty(device.getName()) && device.getName().equals("OBD BLE")) {
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
        mBleScanner.startBleScan();
        mBleScanDialog = new MaterialDialog.Builder(this)
                .progress(true, 100)
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
            String values = HexUtil.encodeHexStr(data);

            Logger.t(TAG).i("onCharacteristicRead: " + values);

        }

        @Override
        public void onCharacteristicNotification(UUID uuid, byte[] data) {
            String values = HexUtil.encodeHexStr(data);
            Logger.t(TAG).i("onCharacteristicNotification: " + values);

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


    public void connectDevice(String deviceMac){
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

}
