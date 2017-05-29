package com.nonda.dtc.ui.activities;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;

import butterknife.ButterKnife;

/**
 * Created by whaley on 2017/5/29.
 */

public class BaseActivity extends Activity {

    @CallSuper
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }
}
