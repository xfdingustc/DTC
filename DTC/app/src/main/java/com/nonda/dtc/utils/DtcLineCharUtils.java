package com.nonda.dtc.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.Chart;
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

import java.util.ArrayList;

/**
 * Created by whaley on 2017/5/29.
 */

public class DtcLineCharUtils {
    public static void setupLineChart(Context context, LineChart chart, float min, float max, LimitLine ll1) {


        chart.setPinchZoom(false);

        XAxis x = chart.getXAxis();
        x.setEnabled(false);


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.setAxisMaximum(min);
        leftAxis.setAxisMinimum(max);
        //leftAxis.setYOffset(20f);
//        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextColor(context.getResources().getColor(R.color.colorAccent));
        leftAxis.setDrawZeroLine(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        leftAxis.setDrawLimitLinesBehindData(true);

        chart.setDragEnabled(false);

//        chart.setScaleEnabled(false);

//        setData();

        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setDrawBorders(false);

        chart.animateX(2500);
    }

    public static LimitLine getLimitLine(Context context, float line , String name) {
        LimitLine ll1 = new LimitLine(line, name);
        ll1.setLineWidth(1f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        ll1.setLineColor(context.getResources().getColor(R.color.colorAccent));
        ll1.setTextColor(context.getResources().getColor(R.color.colorAccent));
        ll1.setTextSize(10f);
        return ll1;
    }

    public static void updateDataSet(Context context, Chart chart, ArrayList<Entry> values) {
        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            set1.setDrawIcons(false);

            set1.setDrawValues(false);
            set1.setDrawCircles(false);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setColor(context.getResources().getColor(R.color.colorAccent));

            set1.setDrawFilled(true);

            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_green);
                set1.setFillDrawable(drawable);
            }
            else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }

    }
}
