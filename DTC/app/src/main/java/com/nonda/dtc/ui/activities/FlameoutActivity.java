package com.nonda.dtc.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.nonda.dtc.R;
import com.nonda.dtc.model.Flameout;
import com.nonda.dtc.model.ObdData;
import com.nonda.dtc.utils.FloatUtils;
import com.nonda.dtc.utils.MpgUtils;
import com.nonda.dtc.utils.SpeedUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by whaley on 2017/6/17.
 */

public class FlameoutActivity extends BaseActivity {
    private static final String EXTRA_FLAME_OUT = "extra_flame_out";

    private Flameout mFlameout;

    @BindView(R.id.distance)
    TextView distance;

    @BindView(R.id.trip_time)
    TextView tripTime;

    @BindView(R.id.avg_spd)
    TextView avgSpd;

    @BindView(R.id.max_speed)
    TextView maxSpeed;

    @BindView(R.id.fuel)
    TextView fuel;

    @BindView(R.id.avg_mpg)
    TextView avgMpg;

    @OnClick(R.id.icon_back)
    public void onIconBackClicked() {
        finish();
    }

    public static void launch(Activity activity, Flameout flameout) {
        Intent intent = new Intent(activity, FlameoutActivity.class);
        intent.putExtra(EXTRA_FLAME_OUT, flameout);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFlameout = (Flameout)getIntent().getSerializableExtra(EXTRA_FLAME_OUT);
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.activity_flameout);
        distance.setText(FloatUtils.toFloatString(0, mFlameout.distance));
        tripTime.setText(FloatUtils.toFloatString(1, mFlameout.time / 3600));
        avgSpd.setText(FloatUtils.toFloatString(0, SpeedUtils.kmh2Mph(mFlameout.avgSpeed)));
        maxSpeed.setText(FloatUtils.toFloatString(0, SpeedUtils.kmh2Mph(mFlameout.maxSpeed)));
        fuel.setText(FloatUtils.toFloatString(1, MpgUtils.l2gallone(mFlameout.fuel)));
        avgMpg.setText(ObdData.getLastAverageMpg());
    }
}
