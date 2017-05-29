package com.nonda.dtc.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.Entry;
import com.nonda.dtc.R;
import com.nonda.dtc.app.AppHolder;
import com.nonda.dtc.model.ObdData;
import com.nonda.dtc.rx.Transformers;
import com.nonda.dtc.ui.obd.ObdDataHandler;
import com.nonda.dtc.ui.obd.ObdViewHolder;
import com.nonda.dtc.utils.DtcLineCharUtils;
import com.nonda.dtc.utils.SpeedUtils;
import com.nonda.dtc.utils.TempUtils;
import com.nonda.dtc.views.NumberAnimTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by whaley on 2017/5/29.
 */

public class SpeedActivity extends BaseActivity {
    private static final String TAG = SpeedActivity.class.getSimpleName();
    private ObdDataHandler mObdDataHandler;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, SpeedActivity.class);
        activity.startActivity(intent);
    }

    @BindView(R.id.speed)
    NumberAnimTextView speed;

    @BindView(R.id.speed_chart)
    LineChart chart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.activity_speed);
        getToolbar().setNavigationIcon(R.drawable.ic_arrow_back);
        getToolbar().setTitleTextColor(Color.WHITE);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setupLineChar();

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
                return speed;
            }

            @Override
            public TextView getCoolant() {
                return null;
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
                        updateSpeedData();
                    }
                });
    }

    private void updateSpeedData() {
        setData();
        chart.invalidate();
    }

    private void setData() {
        ArrayList<Entry> values = new ArrayList<Entry>();

        List<ObdData> obdDataList = ObdData.getObdDataList();

        int interval = 1;

        for (int i = 0; i < obdDataList.size(); i += interval) {
            ObdData obdData = obdDataList.get(i);
            float val = SpeedUtils.kmh2Mph(obdData.spd);
            Log.d(TAG, "x: " + i / interval + " y: " + val);
            values.add(new Entry(i / interval, val));
        }



        DtcLineCharUtils.updateDataSet(this, chart, values);
    }

    private void setupLineChar() {
        LimitLine limitLine = DtcLineCharUtils.getLimitLine(this, 45.2f, "Average Speed");
        DtcLineCharUtils.setupLineChart(this, chart, 0, 80, limitLine);
        updateSpeedData();
    }
}
