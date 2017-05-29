package com.nonda.dtc.ui.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nonda.dtc.R;
import com.nonda.dtc.model.ObdData;
import com.nonda.dtc.utls.TempUtils;
import com.nonda.dtc.views.NumberAnimTextView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by whaley on 2017/5/29.
 */

public class ProFragment extends BaseFragment {
    private static final String TAG = ProFragment.class.getSimpleName();

    @BindView(R.id.coolant)
    TextView coolant;

    @BindView(R.id.mph)
    NumberAnimTextView mph;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onObdData(ObdData obdData) {
        if (obdData == null) {
            return;
        }

        if (obdData.coolant > 0) {
            coolant.setText(obdData.getCoolant());
        }

        if (obdData.spd > 0) {
            String begin = ObdData.getLastObd() == null ? "0" : ObdData.getLastObd().getSpeed();
            Logger.t(TAG).d("begin: " + begin);
            mph.setNumberString(begin, obdData.getSpeed());
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = createFragmentView(inflater, container, R.layout.fragment_mypro, savedInstanceState);
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
