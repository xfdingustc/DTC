package com.nonda.dtc.ui.obd;

import com.nonda.dtc.R;
import com.nonda.dtc.model.ObdData;
import com.nonda.dtc.utils.FloatUtils;

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
        if (mViewHolder.getVoltage() != null && obdData.voltage > 0) {
            mViewHolder.getVoltage().setText(String.valueOf(obdData.voltage));
        }
        if (mViewHolder.getFuelViewHolder() != null) {
            if (obdData.flueLevel > 0) {
                mViewHolder.getFuelViewHolder().getFuelLevel().setText(String.valueOf((int) obdData.flueLevel));
                if (mViewHolder.getRangeViewHolder() != null) {
                    mViewHolder.getRangeViewHolder().getRange().setText(obdData.getRange());
                }
            } else {
                mViewHolder.getFuelViewHolder().getFuelLevelIcon().setImageResource(R.drawable.icon_distance);
                mViewHolder.getFuelViewHolder().getFuelLevelLabel().setText(R.string.distance);
                mViewHolder.getFuelViewHolder().getFuelLevel().setText(FloatUtils.toFloatString(1, obdData.getTotalDistance()));
                mViewHolder.getFuelViewHolder().getFuelLevelUnit().setText(R.string.miles);

                RangeViewHolder rangeViewHolder = mViewHolder.getRangeViewHolder();
                if (rangeViewHolder != null) {
                    rangeViewHolder.getRangeIcon().setImageResource(R.drawable.icon_trip_time);
                    rangeViewHolder.getRangeLabel().setText(R.string.trip_time);

                    int connectTime = obdData.getTotalTripTime();
                    if (connectTime < 60) {
                        rangeViewHolder.getRange().setText(String.valueOf(connectTime));
                        rangeViewHolder.getRangeUnit().setText(R.string.second);
                    } else if (connectTime < 3600) {
                        rangeViewHolder.getRange().setText(FloatUtils.toFloatString(1, connectTime / 60));
                        rangeViewHolder.getRangeUnit().setText(R.string.minute);
                    } else {
                        rangeViewHolder.getRange().setText(FloatUtils.toFloatString(1, connectTime / 3600));
                        rangeViewHolder.getRangeUnit().setText(R.string.hour);
                    }
                }
            }
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




    }
}
