package com.nonda.dtc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nonda.dtc.app.AppHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by whaley on 2017/5/28.
 */

public class FaultActivity extends Activity {
    private AppHolder mAppHolder = AppHolder.getInstance();
    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, FaultActivity.class);
        activity.startActivity(intent);
    }

    @OnClick(R.id.clear)
    public void onBtnClearClicked() {
        AppHolder.getInstance().writeCmd("ATFCDTC");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.activity_fault);
        ButterKnife.bind(this);
    }
}
