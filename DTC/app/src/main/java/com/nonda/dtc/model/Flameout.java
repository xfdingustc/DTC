package com.nonda.dtc.model;

import java.io.Serializable;

/**
 * Created by whaley on 2017/6/17.
 */

public class Flameout implements Serializable {

    public float maxSpeed = 0.0f;
    public float avgSpeed = 0.0f;
    public float fuel = 0.0f;
    public float distance = 0.0f;
    public float time = 0.0f;

    public static Flameout fromString(String flame) {
        Flameout out = new Flameout();
        String payload = flame.substring(11);

        String[] payloadList = payload.split(";");

        for (int i = 0; i < payloadList.length; i++) {
            String onePayLoad = payloadList[i];
            if (onePayLoad.startsWith("MAXSPD")) {
                out.maxSpeed = Float.valueOf(onePayLoad.substring(7));
            } else if (onePayLoad.startsWith("AVGSPD")) {
                out.avgSpeed = Float.valueOf(onePayLoad.substring(7));
            } else if (onePayLoad.startsWith("FUEL-T")) {
                out.fuel = Float.valueOf(onePayLoad.substring(7));
            } else if (onePayLoad.startsWith("MILE-T")) {
                out.distance = Float.valueOf(onePayLoad.substring(7));
            } else if (onePayLoad.startsWith("TIMES-T")) {
                out.time = Float.valueOf(onePayLoad.substring(8));
            }
        }

        return out;
    }
}
