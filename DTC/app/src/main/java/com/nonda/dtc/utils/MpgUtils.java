package com.nonda.dtc.utils;

/**
 * Created by whaley on 2017/5/29.
 */

public class MpgUtils {
    public static String kml2Mpg(float kml) {
        if (kml > 0) {
            return FloatUtils.toFloatString(1, 235.2145836f / kml);
        } else {
            return "0.0";
        }

    }

    public static float l2gallone(float l) {
        return 0.2199692f * l;
    }
}
