package com.nonda.dtc.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.nonda.dtc.DTCConsts;
import com.nonda.dtc.R;
import com.nonda.dtc.app.AppHolder;
import com.nonda.dtc.model.DTCError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by whaley on 2017/5/28.
 */

public class GoodActivity extends BaseActivity {

    private MaterialDialog mLoadDialog;

    @BindView(R.id.toolbar)
    Toolbar toolbar;



    @OnClick(R.id.check)
    public void onBtnCheckClicked() {
        AppHolder.getInstance().writeCmd(DTCConsts.ATDTC);
        mLoadDialog = new MaterialDialog.Builder(this)
                .progress(true, 100)
                .theme(Theme.DARK)
                .progressIndeterminateStyle(false)
                .canceledOnTouchOutside(false)
                .content(R.string.reading_fault_info)
                .show();
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, GoodActivity.class);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();

        AppHolder.getInstance().getDtsError()
                .compose(this.<DTCError>bindToLifecycle())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DTCError>() {
                    @Override
                    public void call(DTCError error) {
                        if (mLoadDialog != null && mLoadDialog.isShowing()) {
                            mLoadDialog.dismiss();
                        }

                        if (error.getErrors().isEmpty()) {
                            Toast.makeText(GoodActivity.this, R.string.no_error, Toast.LENGTH_SHORT);
                        } else {
                            FaultActivity.launch(GoodActivity.this);
                        }
                    }
                });
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
