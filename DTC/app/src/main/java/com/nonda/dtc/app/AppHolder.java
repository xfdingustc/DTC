package com.nonda.dtc.app;

import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;


import com.nonda.dtc.R;
import com.nonda.dtc.ble.AppBluetoothHelper;
import com.nonda.dtc.blelib.BleCallback;
import com.nonda.dtc.blelib.BluetoothHelper;
import com.nonda.dtc.blelib.constant.BleConnectState;
import com.nonda.dtc.blelib.utils.BleLog;
import com.nonda.dtc.blelib.utils.BleUtils;
import com.nonda.dtc.model.DTCError;
import com.nonda.dtc.model.FCDTS;
import com.nonda.dtc.model.ObdData;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import rx.subjects.PublishSubject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by whaley on 2017/5/26.
 */

public class AppHolder extends Application {
    private static final String TAG = "AppHolder";

    private static AppHolder _instance;

    private BluetoothGattService mBleService;
    private BluetoothGattCharacteristic mBleChara;


    public static AppHolder getInstance() {
        return _instance;
    }

    private long lastCheckTime;

    public static Context getContext() {
        return _instance.getApplicationContext();
    }

    private StringBuilder mNotificationBuilder = new StringBuilder();
    private EventBus mEventBus = EventBus.getDefault();

    private AppBluetoothHelper mBleHelper;

    public AppBluetoothHelper getBleHelper() {
        return mBleHelper;
    }

    private PublishSubject<ObdData> mObdSubject = PublishSubject.create();

    private PublishSubject<DTCError> mDtsSubject = PublishSubject.create();

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

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-ThinItalic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
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
            parse(data);
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
//                    Logger.t(TAG).d("service: " + service.getUuid());
                    List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();
                    for (BluetoothGattCharacteristic characteristic : gattCharacteristics) {
//                        Logger.t(TAG).d("charater: " + characteristic.getUuid());
                        if (characteristic.getUuid().toString().startsWith("00002b11")) {
                            mBleHelper.readFromCharacteristic(service.getUuid(), characteristic.getUuid());
                            mBleService = service;
                            mBleChara = characteristic;
//                            String atdtc = getUTF8XMLString("ATFCDTC");
//                            mBleHelper.writeCharacteristic(service.getUuid(), characteristic.getUuid(), atdtc.getBytes());
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

    public void writeCmd(String cmd) {
        String atdtc = getUTF8XMLString(cmd);
        mBleHelper.writeCharacteristic(mBleService.getUuid(), mBleChara.getUuid(), atdtc.getBytes());
    }

    public static String getUTF8XMLString(String xml) {
        // A StringBuffer Object
        StringBuffer sb = new StringBuffer();
        sb.append(xml);
        String xmString = "";
        String xmlUTF8 = "";
        try {
            xmString = new String(sb.toString().getBytes("UTF-8"));
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
            System.out.println("utf-8 编码：" + xmlUTF8);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // return to String Formed
        return xmlUTF8;
    }

    private void parse(byte[] data) {
        String values = new String(data);

        mNotificationBuilder.append(values);
//        Log.d(TAG, mNotificationBuilder.toString());

        String notification = mNotificationBuilder.toString();
        int endOfLine = notification.indexOf("\n");
        if (endOfLine > 0) {
            String oneNotification = notification.substring(0, endOfLine);
            Log.d(TAG, oneNotification);
            mNotificationBuilder = new StringBuilder(notification.substring(endOfLine + 1));
            postOneNotification(oneNotification);
        }


    }

    private void postOneNotification(String notification) {
        if (notification.startsWith("BD$")) {
//            mEventBus.post(ObdData.fromString(notification));
            mObdSubject.onNext(ObdData.fromString(notification));
        } else if (notification.startsWith("#ATDTC$DTC")) {
            Log.d(TAG, "Get Dts Error");
            lastCheckTime = System.currentTimeMillis();
            mEventBus.post(DTCError.fromString(notification));
            mDtsSubject.onNext(DTCError.fromString(notification));
        } else if (notification.startsWith("#ATFCDTC")) {
            mEventBus.post(new FCDTS());
        }
    }

    public PublishSubject<ObdData> getObd() {
        return mObdSubject;
    }

    public PublishSubject<DTCError> getDtsError() {
        return mDtsSubject;
    }

    public long getLastCheckTime() {
        return lastCheckTime;
    }


}
