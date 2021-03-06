package com.nonda.dtc.app;

import android.util.Log;

/**
 * Created by whaley on 2017/5/26.
 */

public class AppLog {
    private static boolean isPrintLog = true;

    private AppLog() {}

    public static boolean isPrintLog() {
        return isPrintLog;
    }

    public static void setPrintLog(boolean isPrintLog) {
        AppLog.isPrintLog = isPrintLog;
    }

    public static void v(String tag, String msg) {
        if (isPrintLog) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isPrintLog) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isPrintLog) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isPrintLog) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isPrintLog) {
            Log.e(tag, msg);
        }
    }
}
