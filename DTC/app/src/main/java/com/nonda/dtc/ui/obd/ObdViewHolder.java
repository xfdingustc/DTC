package com.nonda.dtc.ui.obd;

import android.widget.TextView;

import com.nonda.dtc.views.NumberAnimTextView;

/**
 * Created by whaley on 2017/5/29.
 */

public interface ObdViewHolder {
    TextView getVoltage();

    TextView getFuleLevel();

    NumberAnimTextView getRpm();

    NumberAnimTextView getSpeed();


    TextView getCoolant();

    TextView getInstantMpg();

    NumberAnimTextView getAverageMpg();

    TextView getRange();

}
