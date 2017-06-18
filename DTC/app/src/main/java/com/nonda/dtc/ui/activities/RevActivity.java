package com.nonda.dtc.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.nonda.dtc.R;
import com.nonda.dtc.app.AppHolder;
import com.nonda.dtc.model.ObdData;
import com.nonda.dtc.rx.Transformers;
import com.nonda.dtc.ui.obd.ObdDataHandler;
import com.nonda.dtc.ui.obd.ObdViewHolder;
import com.nonda.dtc.utils.DtcLineCharUtils;
import com.nonda.dtc.utils.FloatUtils;
import com.nonda.dtc.utils.SpeedUtils;
import com.nonda.dtc.views.NumberAnimTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by whaley on 2017/6/18.
 */

public class RevActivity extends BaseActivity {

    private static final String TAG = RevActivity.class.getSimpleName();
    private ObdDataHandler mObdDataHandler;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, RevActivity.class);
        activity.startActivity(intent);
    }

    @BindView(R.id.rev)
    NumberAnimTextView rev;

    @BindView(R.id.speed_chart)
    LineChart chart;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.activity_rev);
        getToolbar().setNavigationIcon(R.drawable.icon_back);
        getToolbar().setTitleTextColor(getResources().getColor(R.color.selected));

        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setupLineChar();

        mObdDataHandler = new ObdDataHandler(new ObdViewHolder() {

            @Override
            public NumberAnimTextView getRpm() {
                return rev;
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
        float speedSum = 0;
        int count = 0;

        for (int i = 0; i < obdDataList.size(); i += interval) {
            ObdData obdData = obdDataList.get(i);
            float val = obdData.rpm;
            speedSum += val;
            count++;
            Log.d(TAG, "x: " + i / interval + " y: " + val);
            values.add(new Entry(i / interval, val));
        }


        DtcLineCharUtils.updateDataSet(this, chart, values);
        float speedAverage = speedSum / count;
        LimitLine limitLine = DtcLineCharUtils.getLimitLine(this, speedAverage,
                "Average RPM " + FloatUtils.toFloatString(1, speedAverage));
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(limitLine);

    }

    private void setupLineChar() {
        LimitLine limitLine = DtcLineCharUtils.getLimitLine(this, 45.2f, "Average RPM");
        DtcLineCharUtils.setupLineChart(this, chart, 0, 8000, limitLine);
        updateSpeedData();
    }
}
