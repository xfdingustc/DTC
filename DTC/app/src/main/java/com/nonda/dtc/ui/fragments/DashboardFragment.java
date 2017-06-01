package com.nonda.dtc.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.nonda.dtc.R;

import butterknife.BindView;

/**
 * Created by whaley on 2017/5/29.
 */

public class DashboardFragment extends BaseFragment {
    private static final String TAG = DashboardFragment.class.getSimpleName();
    private String[] mTitles = {"Eco", "Normal", "Sport"};

    @BindView(R.id.tablayout)
    SegmentTabLayout tabLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = createFragmentView(inflater, container, R.layout.fragment_dashboard, savedInstanceState);
        initViews();
        return view;
    }

    private void initViews() {
        tabLayout.setTabData(mTitles);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Fragment fragment = new NormalFragment();
                switch (position) {
                    case 0:
                        fragment = new EcoFragment();
                        break;
                    case 1:
                        fragment = new NormalFragment();
                        break;
                    case 2:
                        fragment = new ProFragment();
                        break;
                }
                getChildFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        tabLayout.setCurrentTab(1);
        Fragment fragment = new NormalFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}
