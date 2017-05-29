package com.nonda.dtc.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nonda.dtc.R;
import com.nonda.dtc.app.AppHolder;
import com.nonda.dtc.model.ObdData;
import com.nonda.dtc.rx.Transformers;
import com.nonda.dtc.views.NumberAnimTextView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by whaley on 2017/5/29.
 */

public class ProFragment extends BaseFragment {
    private static final String TAG = ProFragment.class.getSimpleName();

    @BindView(R.id.coolant)
    TextView coolant;

    @BindView(R.id.mph)
    NumberAnimTextView mph;

    @OnClick(R.id.coolant_layout)
    public void onCoolantLayoutClicked() {

    }


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

        initViews();
        return view;
    }

    private void initViews() {
        AppHolder.getInstance().getObd()
                .compose(this.<ObdData>bindToLifecycle())
                .compose(Transformers.<ObdData>observerForUI())
                .subscribe(new Action1<ObdData>() {
                    @Override
                    public void call(ObdData obdData) {
                        onObdData(obdData);
                    }
                });
    }


}
