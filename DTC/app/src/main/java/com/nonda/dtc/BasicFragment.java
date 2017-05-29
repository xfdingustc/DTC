package com.nonda.dtc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nonda.dtc.model.ObdData;
import com.nonda.dtc.utls.FloatUtils;
import com.nonda.dtc.utls.MpgUtils;
import com.nonda.dtc.utls.SpeedUtils;
import com.nonda.dtc.utls.TempUtils;
import com.nonda.dtc.views.NumberAnimTextView;

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

    public static ObdData mLastObdData = null;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onObdData(ObdData obdData) {

        if (obdData == null) {
            return;
        }
        if (obdData.voltage > 0) {
            voltage.setText(String.valueOf(obdData.voltage));
        }
        if (obdData.flueLevel > 0) {
            fuleLevel.setText(String.valueOf((int)obdData.flueLevel));
        }

        if (obdData.rpm > 0) {
            String begin = mLastObdData == null ? "0" : mLastObdData.getRpm();
            rpm.setNumberString(begin, obdData.getRpm());
        }
        if (obdData.spd > 0) {
            String begin = mLastObdData == null ? "0" : mLastObdData.getSpeed();
            speed.setNumberString(begin, obdData.getSpeed());
        }

        if (obdData.coolant > 0) {
            coolant.setText(String.valueOf((int)TempUtils.c2p(obdData.coolant)));
        }

        if (obdData.instantMpg > 0) {

            instantMpg.setText(MpgUtils.kml2Mpg(obdData.instantMpg));

            String begin = "0.0";
            if (instantCount != 0) {
                begin = MpgUtils.kml2Mpg(sumMpg / instantCount);
            }

            sumMpg += obdData.instantMpg;
            instantCount++;
            float average = sumMpg / instantCount;
            averageMpg.setNumberString(begin, MpgUtils.kml2Mpg(sumMpg / instantCount));
            range.setText(String.valueOf((int) ((50 / average) / 1.609344f)));



        }

        mLastObdData = obdData;

    }

    @BindView(R.id.voltage)
    TextView voltage;

    @BindView(R.id.rmp)
    NumberAnimTextView rpm;

    @BindView(R.id.speed)
    NumberAnimTextView speed;

    @BindView(R.id.coolant)
    TextView coolant;

    @BindView(R.id.fuleLeve)
    TextView fuleLevel;

    @BindView(R.id.instantMpg)
    TextView instantMpg;

    @BindView(R.id.averageMpg)
    NumberAnimTextView averageMpg;

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
