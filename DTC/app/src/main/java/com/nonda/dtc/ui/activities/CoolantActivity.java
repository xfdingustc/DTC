package com.nonda.dtc.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.nonda.dtc.R;
import com.nonda.dtc.app.AppHolder;
import com.nonda.dtc.model.ObdData;
import com.nonda.dtc.rx.Transformers;
import com.nonda.dtc.ui.obd.ObdDataHandler;
import com.nonda.dtc.ui.obd.ObdViewHolder;
import com.nonda.dtc.views.NumberAnimTextView;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by whaley on 2017/5/29.
 */

public class CoolantActivity extends BaseActivity {
    private ObdDataHandler mObdDataHandler;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, CoolantActivity.class);
        activity.startActivity(intent);
    }

    @BindView(R.id.coolant)
    TextView coolant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.activity_coolant);
        getToolbar().setNavigationIcon(R.drawable.ic_arrow_back);
        getToolbar().setTitleTextColor(Color.WHITE);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mObdDataHandler = new ObdDataHandler(new ObdViewHolder() {
            @Override
            public TextView getVoltage() {
                return null;
            }

            @Override
            public TextView getFuleLevel() {
                return null;
            }

            @Override
            public NumberAnimTextView getRpm() {
                return null;
            }

            @Override
            public NumberAnimTextView getSpeed() {
                return null;
            }

            @Override
            public TextView getCoolant() {
                return coolant;
            }

            @Override
            public TextView getInstantMpg() {
                return null;
            }

            @Override
            public NumberAnimTextView getAverageMpg() {
                return null;
            }

            @Override
            public TextView getRange() {
                return null;
            }
        });

        AppHolder.getInstance().getObd()
                .compose(this.<ObdData>bindToLifecycle())
                .compose(Transformers.<ObdData>observerForUI())
                .subscribe(new Action1<ObdData>() {
                    @Override
                    public void call(ObdData obdData) {
                        mObdDataHandler.handleObdData(obdData);
                    }
                });



    }
}
