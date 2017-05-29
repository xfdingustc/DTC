package com.nonda.dtc.model;

import com.nonda.dtc.utls.SpeedUtils;
import com.nonda.dtc.utls.TempUtils;

import java.util.List;

/**
 * Created by whaley on 2017/5/27.
 */

public class ObdData {
    private static ObdData mLastObdData = null;

    public float voltage = -1.0f;
    public int rpm = -1;
    public int spd = -1;
    public int coolant = -1;
    public float flueLevel = -1.0f;
    public float instantMpg = -1.0f;
    public int error = 0;

    public static ObdData fromString(String obd) {
        if (!obd.startsWith("BD$")) {
            return null;
        }
        ObdData obdData = new ObdData();
        String payload = obd.substring(3);

        String[] payloadList = payload.split(";");

        for (int i = 0; i < payloadList.length; i++) {
            String onePayLoad = payloadList[i];
            if (onePayLoad.startsWith("V")) {
                obdData.voltage = Float.valueOf(onePayLoad.substring(1));
            } else if (onePayLoad.startsWith("R")){
                obdData.rpm = Integer.valueOf(onePayLoad.substring(1));
            } else if (onePayLoad.startsWith("S")) {
                obdData.spd = Integer.valueOf(onePayLoad.substring(1));
            } else if (onePayLoad.startsWith("C")) {
                obdData.coolant = Integer.valueOf(onePayLoad.substring(1));
            } else if (onePayLoad.startsWith("L")) {
                obdData.flueLevel = Float.valueOf(onePayLoad.substring(1));
            } else if (onePayLoad.startsWith("O")) {

            } else if (onePayLoad.startsWith("XM")) {
                obdData.instantMpg = Float.valueOf(onePayLoad.substring(2));
            } else if (onePayLoad.startsWith("D")) {
                try {
                    obdData.error = Integer.valueOf(onePayLoad.substring(1));
                } catch (NumberFormatException e) {
                    obdData.error = -1;
                }
            }
        }

        mLastObdData = obdData;
        return obdData;
    }

    public static ObdData getLastObd() {
        return mLastObdData;
    }

    public String getRpm() {
        return String.valueOf(rpm);
    }

    public String getSpeed() {
        return String.valueOf((int)(SpeedUtils.kmh2Mph(spd)));
    }

    public String getCoolant() {
        return String.valueOf((int) TempUtils.c2p(coolant));
    }
}
