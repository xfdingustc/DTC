package com.nonda.dtc.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.nonda.dtc.R;
import com.nonda.dtc.app.AppHolder;
import com.nonda.dtc.model.ObdData;
import com.nonda.dtc.rx.Transformers;
import com.nonda.dtc.ui.obd.ObdDataHandler;
import com.nonda.dtc.ui.obd.ObdViewHolder;
import com.nonda.dtc.utils.DtcLineCharUtils;
import com.nonda.dtc.utils.FloatUtils;
import com.nonda.dtc.utils.TempUtils;
import com.nonda.dtc.views.NumberAnimTextView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by whaley on 2017/5/29.
 */

public class CoolantActivity extends BaseActivity {
    private static final String TAG = CoolantActivity.class.getSimpleName();
    private ObdDataHandler mObdDataHandler;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, CoolantActivity.class);
        activity.startActivity(intent);
    }

    @BindView(R.id.coolant)
    TextView coolant;

    @BindView(R.id.coolant_chart)
    LineChart chart;


    @OnClick(R.id.setting)
    public void onSettingClicked() {
        SettingActivity.launch(CoolantActivity.this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }



    private void initViews() {
        setContentView(R.layout.activity_coolant);
        getToolbar().setNavigationIcon(R.drawable.icon_back);
        getToolbar().setTitleTextColor(getResources().getColor(R.color.selected));
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
                        updateCoolantData();
                    }
                });


        setupLineChart();



    }

    private void updateCoolantData() {
        setData();
        chart.invalidate();
    }

    private void setupLineChart() {
        LimitLine limitLine= DtcLineCharUtils.getLimitLine(this, 187f, "Average Coolant");
        DtcLineCharUtils.setupLineChart(this, chart, 140f, 240f, limitLine);
        updateCoolantData();
    }

    private void setData() {

        ArrayList<Entry> values = new ArrayList<Entry>();

        List<ObdData> obdDataList = ObdData.getObdDataList();

        int interval = 60;

        float coolSum = 0;
        int count = 0;

        for (int i = 0; i < obdDataList.size(); i += interval) {
            ObdData obdData = obdDataList.get(i);
            float val = TempUtils.c2p(obdData.coolant);
            coolSum += val;
            count++;
            Log.d(TAG, "x: " + i / interval + " y: " + val);
            values.add(new Entry(i / interval, val));
        }



        DtcLineCharUtils.updateDataSet(this, chart, values);
        float coolAverage = coolSum / count;
        LimitLine limitLine = DtcLineCharUtils.getLimitLine(this, coolAverage,
                "Average Coolant " + FloatUtils.toFloatString(1, coolAverage));
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(limitLine);
    }
}
