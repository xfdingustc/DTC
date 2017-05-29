package com.nonda.dtc.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.nonda.dtc.app.AppHolder;
import com.nonda.dtc.rx.Transformers;
import com.nonda.dtc.ui.activities.FaultActivity;
import com.nonda.dtc.ui.activities.GoodActivity;
import com.nonda.dtc.R;
import com.nonda.dtc.model.ObdData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by whaley on 2017/5/26.
 */

public class DeviceFragment extends BaseFragment {
    @OnClick(R.id.good_or_fault)
    public void onGoodFaultClicked() {
        if (goodOrFault.getDisplayedChild() == 1) {
            FaultActivity.launch(getActivity());
        } else {
            GoodActivity.launch(getActivity());
        }


    }

    public void onObdData(ObdData data) {
        if (data == null) {
            return;
        }

        if (data.error > 0) {
            goodOrFault.setDisplayedChild(1);
            fault.setText("" + data.error + " Faults");
        } else {
            goodOrFault.setDisplayedChild(0);
        }
    }

    @BindView(R.id.good_or_fault)
    ViewAnimator goodOrFault;

    @BindView(R.id.faults)
    TextView fault;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = createFragmentView(inflater, container, R.layout.fragment_device, savedInstanceState);
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
