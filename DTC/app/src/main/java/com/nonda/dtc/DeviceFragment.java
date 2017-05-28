package com.nonda.dtc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;
import android.widget.ViewSwitcher;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by whaley on 2017/5/26.
 */

public class DeviceFragment extends BaseFragment {
    @OnClick(R.id.good_or_fault)
    public void onGoodFaultClicked() {
        if (goodOrFault.getDisplayedChild() == 1) {

        }

        FaultActivity.launch(getActivity());
    }


    @BindView(R.id.good_or_fault)
    ViewAnimator goodOrFault;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = createFragmentView(inflater, container, R.layout.fragment_device, savedInstanceState);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
