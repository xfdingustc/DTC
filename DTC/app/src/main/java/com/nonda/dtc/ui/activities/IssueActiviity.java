package com.nonda.dtc.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.nonda.dtc.R;
import com.nonda.dtc.app.AppHolder;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by whaley on 2017/6/1.
 */

public class IssueActiviity extends Activity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.check_time)
    TextView checkTime;

    @BindView(R.id.issue_number)
    TextView issueNumber;

    public static void launch(Activity activity, String issueNumber) {
        Intent intent = new Intent(activity, IssueActiviity.class);
        intent.putExtra("issue", issueNumber);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.activity_issuee);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.selected));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd");
        checkTime.setText(dateFormat.format(AppHolder.getInstance().getLastCheckTime()));
        issueNumber.setText(getIntent().getStringExtra("issue"));
    }
}
