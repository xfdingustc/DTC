package com.nonda.dtc.app;

import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;


import com.nonda.dtc.MainActivity;
import com.nonda.dtc.ble.AppBluetoothHelper;
import com.nonda.dtc.blelib.BleCallback;
import com.nonda.dtc.blelib.BluetoothHelper;
import com.nonda.dtc.blelib.constant.BleConnectState;
import com.nonda.dtc.blelib.utils.BleLog;
import com.nonda.dtc.blelib.utils.BleUtils;
import com.nonda.dtc.model.ObdData;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.UUID;

/**
 * Created by whaley on 2017/5/26.
 */

public class AppHolder extends Application {
    private static final String TAG = "AppHolder";

    private static AppHolder _instance;


    public static AppHolder getInstance() {
        return _instance;
    }

    public static Context getContext() {
        return _instance.getApplicationContext();
    }

    private StringBuilder mNotificationBuilder = new StringBuilder();
    private EventBus mEventBus = EventBus.getDefault();

    private AppBluetoothHelper mBleHelper;

    public AppBluetoothHelper getBleHelper() {
        return mBleHelper;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        BleLog.setPrintLog(true);
        AppLog.setPrintLog(true);

        _instance = this;

        Logger
                .init(TAG)
                .hideThreadInfo()
                .methodCount(1);
    }

    public void initBleHelper(BluetoothHelper.OnBindListener listener) {
        mBleHelper = new AppBluetoothHelper(this);
        mBleHelper.setBleCallback(AppHolder.getInstance().getBleCallBack());

        mBleHelper.bindService(listener);
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
            Logger.t(TAG).i("connect status changed " + AppBluetoothHelper.getConnectStateForShow(AppHolder.this, connectState.getCode()));

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

    public BleCallback getBleCallBack() {
        return mBleCallback;
    }


}
