package com.nonda.dtc.ui.obd;

import com.nonda.dtc.model.ObdData;
import com.nonda.dtc.utils.MpgUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by whaley on 2017/5/29.
 */

public class ObdDataHandler {
    private static final String TAG = ObdDataHandler.class.getSimpleName();

    private final ObdViewHolder mViewHolder;

    public ObdDataHandler(ObdViewHolder viewHolder) {
        this.mViewHolder = viewHolder;

    }
    public void handleObdData(ObdData obdData) {
        if (obdData == null) {
            return;
        }
        if (mViewHolder.getVoltage() != null &&obdData.voltage > 0) {
            mViewHolder.getVoltage().setText(String.valueOf(obdData.voltage));
        }
        if (mViewHolder.getFuleLevel() != null && obdData.flueLevel > 0) {
            mViewHolder.getFuleLevel().setText(String.valueOf((int)obdData.flueLevel));
        }

        if (mViewHolder.getRpm() != null && obdData.rpm > 0) {
            String begin = ObdData.getLastObd() == null ? "0" : ObdData.getLastObd().getRpm();
            mViewHolder.getRpm().setNumberString(begin, obdData.getRpm());
        }
//        Logger.t(TAG).d("speed: " + obdData.spd);
        if (mViewHolder.getSpeed() != null && obdData.spd >= 0) {
            String begin = ObdData.getLastObd() == null ? "0" : ObdData.getLastObd().getSpeed();
            mViewHolder.getSpeed().setNumberString(begin, obdData.getSpeed());
        }

        if (mViewHolder.getCoolant() != null) {
            mViewHolder.getCoolant().setText(obdData.getCoolant());
        }

        if (mViewHolder.getInstantMpg() != null && obdData.instantMpg > 0) {

            mViewHolder.getInstantMpg().setText(obdData.getInstantMpg());

        }

        if (mViewHolder.getAverageMpg() != null) {
            mViewHolder.getAverageMpg().setNumberString(obdData.getLastAverageMpeg(), obdData.getCurrentAverageMpg());
        }

        if (mViewHolder.getRange() != null) {
            mViewHolder.getRange().setText(obdData.getRange());
        }


    }
}
