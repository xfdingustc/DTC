package com.nonda.dtc.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nonda.dtc.R;
import com.nonda.dtc.app.AppHolder;
import com.nonda.dtc.model.ObdData;
import com.nonda.dtc.rx.Transformers;
import com.nonda.dtc.ui.activities.CoolantActivity;
import com.nonda.dtc.ui.activities.SpeedActivity;
import com.nonda.dtc.ui.obd.FuelViewHolder;
import com.nonda.dtc.ui.obd.ObdDataHandler;
import com.nonda.dtc.ui.obd.ObdViewHolder;
import com.nonda.dtc.views.NumberAnimTextView;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by whaley on 2017/5/29.
 */

public class SportFragment extends BaseFragment {
    private static final String TAG = SportFragment.class.getSimpleName();
    private ObdDataHandler mObdDataHandler;

    @BindView(R.id.coolant)
    TextView coolant;

    @BindView(R.id.mph)
    NumberAnimTextView mph;

    @OnClick(R.id.coolant_layout)
    public void onCoolantLayoutClicked() {
        CoolantActivity.launch(getActivity());
    }

    @OnClick(R.id.speed_layout)
    public void onSpeedLayoutClicked() {
        SpeedActivity.launch(getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = createFragmentView(inflater, container, R.layout.fragment_sport, savedInstanceState);

        initViews();
        return view;
    }

    private void initViews() {
        mObdDataHandler = new ObdDataHandler(new ObdViewHolder() {
            @Override
            public TextView getVoltage() {
                return null;
            }

            @Override
            public FuelViewHolder getFuelViewHolder() {
                return null;
            }

            @Override
            public NumberAnimTextView getRpm() {
                return null;
            }

            @Override
            public NumberAnimTextView getSpeed() {
                return mph;
            }

            @Override
            public TextView getCoolant() {
                return coolant;
            }

            @Override
            public TextView getInstantMpg() {
                return null;
            }

            @Override
            public NumberAnimTextView getAverageMpg() {
                return null;
            }

            @Override
            public TextView getRange() {
                return null;
            }
        });
        AppHolder.getInstance().getObd()
                .compose(this.<ObdData>bindToLifecycle())
                .compose(Transformers.<ObdData>observerForUI())
                .subscribe(new Action1<ObdData>() {
                    @Override
                    public void call(ObdData obdData) {
                        mObdDataHandler.handleObdData(obdData);
                    }
                });
    }


}
