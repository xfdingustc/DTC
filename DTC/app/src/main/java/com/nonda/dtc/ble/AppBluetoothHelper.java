package com.nonda.dtc.ble;

import android.content.Context;
import android.content.Intent;


import com.nonda.dtc.R;
import com.nonda.dtc.blelib.BluetoothHelper;

/**
 * Created by whaley on 2017/5/26.
 */

public class AppBluetoothHelper extends BluetoothHelper {
    private final static String TAG = AppBluetoothHelper.class.getName();


    public AppBluetoothHelper(Context context) {
        super(context);
    }

    @Override
    public boolean bindService(OnBindListener bindListener) {
        this.mBindListener = bindListener;
        return context.getApplicationContext().bindService(
                new Intent(context, AppBleService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unbindService() {
        context.getApplicationContext().unbindService(this);
    }





    public static String getConnectStateForShow(Context context, int code) {
        int rid;
        switch (code) {
            case 0:
                rid = R.string.app_ble_status_0;
                break;
            case 1:
                rid = R.string.app_ble_status_1;
                break;
            case 2:
                rid = R.string.app_ble_status_2;
                break;
            case 3:
                rid = R.string.app_ble_status_3;
                break;
            case 4:
                rid = R.string.app_ble_status_4;
                break;
            case 5:
                rid = R.string.app_ble_status_5;
                break;
            case 6:
                rid = R.string.app_ble_status_6;
                break;
            case 7:
                rid = R.string.app_ble_status_7;
                break;
            case 8:
                rid = R.string.app_ble_status_8;
                break;
            case 9:
                rid = R.string.app_ble_status_9;
                break;
            default:
                rid = R.string.ble_unknown;
        }

        return context.getString(rid);
    }
}
