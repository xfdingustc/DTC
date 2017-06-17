package com.nonda.dtc.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.Toolbar;


import com.nonda.dtc.R;
import com.nonda.dtc.app.AppHolder;
import com.nonda.dtc.model.Flameout;
import com.trello.rxlifecycle.components.RxActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by whaley on 2017/5/29.
 */

public class BaseActivity extends RxActivity {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppHolder.getInstance().getFlameout()
                .compose(this.<Flameout>bindToLifecycle())
                .subscribe(new Action1<Flameout>() {
                    @Override
                    public void call(Flameout flameout) {
                        FlameoutActivity.launch(BaseActivity.this, flameout);
                    }
                });
    }

    @CallSuper
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
