package com.nonda.dtc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.lang.reflect.AccessibleObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by whaley on 2017/5/28.
 */

public class GoodActivity extends Activity {

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, GoodActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.activity_good);
        ButterKnife.bind(this);

//        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }
}
