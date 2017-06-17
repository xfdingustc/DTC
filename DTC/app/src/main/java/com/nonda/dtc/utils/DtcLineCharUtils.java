package com.nonda.dtc.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
    public static void setupLineChart(Context context, LineChart mChart, float min, float max, LimitLine ll) {


        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);


        XAxis xAxis = mChart.getXAxis();
//        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setEnabled(false);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll);

        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        //leftAxis.setYOffset(20f);
//        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextColor(context.getResources().getColor(R.color.colorAccent));
        leftAxis.setZeroLineColor(Color.TRANSPARENT);
        leftAxis.setLabelCount(2);
        leftAxis.setDrawGridLines(false);

        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.getLegend().setEnabled(false);


//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        mChart.animateX(2500);
        mChart.invalidate();
    }

    public static LimitLine getLimitLine(Context context, float line, String name) {
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
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
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
            } else {
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
