package com.augurit.am.cmpt.widget.amchart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.am.fw.utils.DoubleUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 描述：柱状图控件
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.amchart
 * @createTime 创建时间 ：2016-07-29
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2016-07-29
 * @modifyMemo 修改备注：
 */
public class AMHorizontalBarChart extends AMBaseChart {

    private HorizontalBarChart mBarChart;

    public AMHorizontalBarChart(Context context) {
        super(context, null);
    }

    public AMHorizontalBarChart(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public AMHorizontalBarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initChart(Context context) {
        mBarChart = new HorizontalBarChart(context);
        mBarChart.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        // 初始化
        mBarChart.setExtraOffsets(5, 5, 5, 10);
        mBarChart.setDrawValueAboveBar(true);   // 在柱顶显示数值
        mBarChart.setDescription("");     // 不显示描述
        mBarChart.setNoDataTextDescription("没有数据");   // 无数据时的显示文字
        mBarChart.setDrawGridBackground(false);   // 不显示网格背景
        mBarChart.setScaleEnabled(true);  // 允许缩放
        mBarChart.setPinchZoom(true);     // 允许XY同时缩放

        // X轴初始化
        XAxisValueFormatter valueFormatter = new XAxisValueFormatter();
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);   // 间隔显示标签,0为不间隔显示,但标签过长时仍会间隔显示
        xAxis.setValueFormatter(valueFormatter);   // X轴值格式化
        xAxis.setAxisLineWidth(0.2f);//设置x轴宽度, ...其他样式

        // 左Y轴初始化
        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisMinValue(0f);

        // 右Y轴初始化
        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinValue(0f);

        // MarkerView
        mBarChart.setMarkerView(new XYMarkerView(context, valueFormatter));

        // 图例
        Legend legend = mBarChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
//        legend.setEnabled(false);

        // 添加视图
        addView(mBarChart);
    }

    @Override
    protected void setChartData() {
        if (mXVals == null || mYVals == null) {
            return;
        }
        mBarChart.getXAxis().setAxisMinValue(0f);
        mBarChart.getXAxis().setAxisMaxValue(mXVals.length + 1);

        // 初始化显示值
        ArrayList<BarEntry> entries = new ArrayList<>();
        int xNumber = 1;    // X轴从1的位置开始显示
        for (float yVal : mYVals) {
            entries.add(new BarEntry(xNumber, yVal));
            xNumber++;
        }

        BarDataSet set;
        set = new BarDataSet(entries, mLabel);
        if (mColors != null) {  // 已调用setColors方法设置了颜色
            super.setColors(set, mColors);
        } else {   // 否则采用默认设置
            set.setColors(ColorTemplate.MATERIAL_COLORS);
        }

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);
        data.setValueFormatter(new TopValueFormatter());  // 顶部数值格式化

        mBarChart.setData(data);
    }

    /**
     * 设置可见性，为可见时显示动画效果
     */
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        mBarChart.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            mBarChart.animateXY(1000, 1000);
        }
    }

    @Override // 设置柱体颜色
    public void setColors(Colors colors) {
        BarData data = mBarChart.getData();
        if (data != null && data.getDataSetCount() != 0) {
            // 已经调用setData设置了数据,则改变颜色
            BarDataSet set = (BarDataSet) data.getDataSetByIndex(0);
            super.setColors(set, colors);
        }
        mColors = colors;
    }

    @Override
    public void setYUnit(String yUnit) {
        super.setYUnit(yUnit);
        if (yUnit != null && !yUnit.trim().equals("")) {  // 有单位时间隔为1,设置格式化
            mBarChart.getAxisLeft().setGranularity(1f);
            mBarChart.getAxisLeft().setValueFormatter(new YAxisValueFormatter());
            mBarChart.getAxisRight().setGranularity(1f);
            mBarChart.getAxisRight().setValueFormatter(new YAxisValueFormatter());
        }
    }

    @Override
    public void setLegendEnabled(boolean enabled) {
        mBarChart.getLegend().setEnabled(enabled);
    }

    @Override
    public void setOnChartValueSelectedListener(OnChartValueSelectedListener listener) {
        mBarChart.setOnChartValueSelectedListener(listener);
    }

    @Override
    public void setExtraOffsets(int left, int top, int right, int bottom) {
        mBarChart.setExtraOffsets(left, top, right, bottom);
    }

    /**
     * X轴值格式化
     */
    class XAxisValueFormatter implements AxisValueFormatter {

        @Override
        public String getFormattedValue(float v, AxisBase axisBase) {
            if (0 < v && v <= mXVals.length) {
                // 由于从X轴1处开始显示,相应标签也从1处开始&&超出范围则不显示
                return mXVals[(int) (v - 1)];
            }
            return "";
        }

        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }

    /**
     * Y轴值格式化
     */
    class YAxisValueFormatter implements AxisValueFormatter {

        @Override
        public String getFormattedValue(float v, AxisBase axisBase) {
            DecimalFormat format = new DecimalFormat("####");
            return format.format(v) + mYUnit;
        }

        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }

    /**
     * 顶部数值格式化
     */
    class TopValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return DoubleUtil.formatDecimal(value);
        }
    }
}
