package com.nonda.dtc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nonda.dtc.model.ObdData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
            speed.setText(String.valueOf(obdData.spd));
        }

        if (obdData.coolant > 0) {
            coolant.setText(String.valueOf(obdData.coolant));
        }

        if (obdData.instantMpg > 0) {
            instantMpg.setText(String.valueOf(obdData.instantMpg));
            sumMpg += obdData.instantMpg;
            instantCount++;
            averageMpg.setText(String.valueOf(sumMpg / instantCount));

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
