package com.nonda.dtc.model;

import com.nonda.dtc.utils.LimitList;
import com.nonda.dtc.utils.MpgUtils;
import com.nonda.dtc.utils.SpeedUtils;
import com.nonda.dtc.utils.TempUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by whaley on 2017/5/27.
 */

public class ObdData {
    private static List<ObdData> mObdHistory = new LimitList<>(600);

    public static float sumMpg = 0.0f;
    public static int instantCount = 0;

    private static float lastAverage = 0.0f;

    public float voltage = -1.0f;
    public int rpm = -1;
    public int spd = 0;
    public int coolant = 0;
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

        add2History(obdData);
        if (obdData.instantMpg > 0) {
            if (instantCount != 0) {
                lastAverage = sumMpg / instantCount;
            }
            sumMpg += obdData.instantMpg;
            instantCount++;
        }
        return obdData;
    }

    private static void add2History(ObdData obdData) {
        mObdHistory.add(obdData);
    }

    public static ObdData getLastObd() {
        if (mObdHistory.size() <= 1) {
            return null;
        }
        return mObdHistory.get(mObdHistory.size() - 2);
    }

    public static List<ObdData> getObdDataList() {
        return mObdHistory;
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

    public String getInstantMpg() {
        return MpgUtils.kml2Mpg(instantMpg);
    }

    public String getLastAverageMpeg() {
        return MpgUtils.kml2Mpg(lastAverage);
    }

    public String getCurrentAverageMpg() {
        if (instantCount == 0) {
            return "0.0";
        }
        return MpgUtils.kml2Mpg(sumMpg / instantCount);
    }

    public String getRange() {
        if (instantCount == 0) {
            return "0";
        }
        return String.valueOf((int) ((50 / (sumMpg / instantCount)) / 1.609344f));
    }
}
