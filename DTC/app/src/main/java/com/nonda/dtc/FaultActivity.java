package com.nonda.dtc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.nonda.dtc.app.AppHolder;
import com.nonda.dtc.model.DTCError;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by whaley on 2017/5/28.
 */

public class FaultActivity extends Activity {
    private static final String TAG = FaultActivity.class.getSimpleName();
    private AppHolder mAppHolder = AppHolder.getInstance();

    private IssueListAdapter mAdatper;
    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, FaultActivity.class);
        activity.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDTCError(DTCError error) {
        mAdatper.setIssueList(error);
        Logger.t(TAG).d("error: " + error.toString());
        title.setText("" + error.getErrors().size() + " Issues Need to Check");
    }

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.issue_list)
    RecyclerView issueList;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.clear)
    public void onBtnClearClicked() {
        AppHolder.getInstance().writeCmd(DTCConsts.ATFCDTC);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.activity_fault);
        ButterKnife.bind(this);
        AppHolder.getInstance().writeCmd(DTCConsts.ATDTC);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        issueList.setLayoutManager(new LinearLayoutManager(this));
        mAdatper = new IssueListAdapter(this);
        issueList.setAdapter(mAdatper);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
