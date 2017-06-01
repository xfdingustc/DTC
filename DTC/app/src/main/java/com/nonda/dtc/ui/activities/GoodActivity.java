package com.nonda.dtc.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toolbar;

import com.nonda.dtc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by whaley on 2017/5/28.
 */

public class GoodActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.check)
    public void onBtnCheckClicked() {

    }

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

        toolbar.setTitleTextColor(getResources().getColor(R.color.selected));
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
