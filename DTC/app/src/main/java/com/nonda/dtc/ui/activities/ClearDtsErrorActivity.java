package com.nonda.dtc.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nonda.dtc.R;

/**
 * Created by whaley on 2017/6/17.
 */

public class ClearDtsErrorActivity extends BaseActivity {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, ClearDtsErrorActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.activity_clear_dtc);
    }
}
