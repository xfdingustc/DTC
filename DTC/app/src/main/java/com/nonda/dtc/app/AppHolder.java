package com.nonda.dtc.app;

import android.app.Application;
import android.content.Context;

import com.github.captain_miao.android.ble.utils.BleLog;
import com.orhanobut.logger.Logger;

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


}
