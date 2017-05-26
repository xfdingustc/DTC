package com.nonda.dtc;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private SimpleFragmentPagerAdapter mAdapter;

    @BindView(R.id.view_pager)
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        mAdapter = new SimpleFragmentPagerAdapter(getFragmentManager());
        mAdapter.addFragment(new DeviceFragment());
        mAdapter.addFragment(new BasicFragment());
        viewPager.setAdapter(mAdapter);
    }
}
