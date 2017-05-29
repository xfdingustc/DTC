package com.nonda.dtc.utils;

import java.math.BigDecimal;

/**
 * Created by whaley on 2017/5/28.
 */

public class FloatUtils {
    public static String toFloatString(float value) {
        BigDecimal tmp = new BigDecimal(value);
        return String.valueOf(tmp.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
    }

    public static String toFloatString(int scale, float value) {
        BigDecimal tmp = new BigDecimal(value);
        return String.valueOf(tmp.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue());
    }
}
