package com.nonda.dtc.ui.obd;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by whaley on 2017/6/17.
 */

public interface FuelViewHolder {
    TextView getFuelLevel();

    ImageView getFuelLevelIcon();

    TextView getFuelLevelLabel();

    TextView getFuelLevelUnit();
}
