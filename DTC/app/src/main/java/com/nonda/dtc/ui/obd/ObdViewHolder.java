package com.nonda.dtc.ui.obd;

import android.widget.ImageView;
import android.widget.TextView;

import com.nonda.dtc.views.NumberAnimTextView;

/**
 * Created by whaley on 2017/5/29.
 */

public abstract class ObdViewHolder {
    public TextView getVoltage() {
        return null;
    }

    public FuelViewHolder getFuelViewHolder() {
        return null;
    }

    public NumberAnimTextView getRpm() {
        return null;
    }

    public NumberAnimTextView getSpeed() {
        return null;
    }


    public TextView getCoolant() {
        return null;
    }

    public TextView getInstantMpg() {
        return null;
    }

    public NumberAnimTextView getAverageMpg() {
        return null;
    }

    public RangeViewHolder getRangeViewHolder() {
        return null;
    }

    public TextView getLoad() {
        return null;
    }

    public TextView getThrottle() {
        return null;
    }

}
