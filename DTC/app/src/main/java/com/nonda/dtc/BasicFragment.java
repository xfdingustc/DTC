package com.nonda.dtc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nonda.dtc.model.ObdData;
import com.nonda.dtc.utls.FloatUtils;
import com.nonda.dtc.utls.SpeedUtils;
import com.nonda.dtc.utls.TempUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

import butterknife.BindView;

/**
 * Created by whaley on 2017/5/26.
 */

public class BasicFragment extends BaseFragment {

    public static float sumMpg = 0.0f;
    public static int instantCount = 0;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onObdData(ObdData obdData) {

        if (obdData == null) {
            return;
        }
        if (obdData.voltage > 0) {
            voltage.setText(String.valueOf(obdData.voltage));
        }
        if (obdData.flueLevel > 0) {
            fuleLevel.setText(String.valueOf(obdData.flueLevel));
        }

        if (obdData.rpm > 0) {
            rpm.setText(String.valueOf(obdData.rpm));
        }
        if (obdData.spd > 0) {
            speed.setText(FloatUtils.toFloatString(SpeedUtils.kmh2Mph(obdData.spd)));
        }

        if (obdData.coolant > 0) {
            coolant.setText(String.valueOf(TempUtils.c2p(obdData.coolant)));
        }

        if (obdData.instantMpg > 0) {
            instantMpg.setText((FloatUtils.toFloatString(235.2145836f / obdData.instantMpg)));
            sumMpg += obdData.instantMpg;
            instantCount++;
            float average = sumMpg / instantCount;
            averageMpg.setText((FloatUtils.toFloatString(235.2145836f / average)));
            range.setText((FloatUtils.toFloatString((50 / average) / 1.609344f)));

        }

    }

    @BindView(R.id.voltage)
    TextView voltage;

    @BindView(R.id.rmp)
    TextView rpm;

    @BindView(R.id.speed)
    TextView speed;

    @BindView(R.id.coolant)
    TextView coolant;

    @BindView(R.id.fuleLeve)
    TextView fuleLevel;

    @BindView(R.id.instantMpg)
    TextView instantMpg;

    @BindView(R.id.averageMpg)
    TextView averageMpg;

    @BindView(R.id.range)
    TextView range;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = createFragmentView(inflater, container, R.layout.fragment_basic, savedInstanceState);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
