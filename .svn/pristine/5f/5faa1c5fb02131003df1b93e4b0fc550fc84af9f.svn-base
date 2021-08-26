package com.augurit.am.cmpt.widget.amchart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.augurit.am.fw.utils.DoubleUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 描述：折线图控件
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.amchart
 * @createTime 创建时间 ：2016-07-29
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2016-07-29
 * @modifyMemo 修改备注：
 */
public class AMLineChart extends AMBaseChart {

    private LineChart mLineChart;

    private int mLineColor = -1; // 线颜色

    public AMLineChart(Context context) {
        super(context);
    }

    public AMLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AMLineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initChart(Context context) {
        mLineChart = new LineChart(context);
        mLineChart.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // 初始化
        mLineChart.setExtraOffsets(5, 5, 5, 10);
        mLineChart.setDescription("");
        mLineChart.setNoDataTextDescription("没有数据");

        // X轴
        AxisValueFormatter xValueFormatter = new XAxisValueFormatter();
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(xValueFormatter);

        // 左Y轴
        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setAxisMinValue(0f);

        // 右Y轴
        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setAxisMinValue(0f);

        // MarkerView
        mLineChart.setMarkerView(new XYMarkerView(context, xValueFormatter));

        // 图例
        Legend legend = mLineChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
//        legend.setEnabled(false);

        addView(mLineChart);
    }

    @Override
    protected void setChartData() {
        mLineChart.getXAxis().setAxisMinValue(0f);
        mLineChart.getXAxis().setAxisMaxValue(mXVals.length + 0f);  // 多显示格数

        // 初始化显示值
        ArrayList<Entry> entries = new ArrayList<>();
        int xNumber = 0;    // X轴从0的位置开始显示
        for (float yVal : mYVals) {
            entries.add(new Entry(xNumber, yVal));
            xNumber++;
        }

        LineDataSet set = new LineDataSet(entries, mLabel);
        if (mLineColor != -1) {
            set.setColor(mLineColor);  // 若已调用setLineColor设置线颜色
        } else if (mColors != null) {
            super.setColors(set, mColors);  // 若已调用setColors设置颜色
        } else {
            set.setColors(ColorTemplate.MATERIAL_COLORS);   // 否则采用默认设置
        }
        set.setCircleColors(ColorTemplate.MATERIAL_COLORS);    // 点颜色

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        LineData data = new LineData(dataSets);
        data.setValueTextSize(10f);
        data.setValueFormatter(new TopValueFormatter());

        mLineChart.setData(data);
    }

    /**
     * 设置可见性，为可见时显示动画效果
     */
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        mLineChart.setVisibility(visibility);
        if (visibility == VISIBLE) {
            mLineChart.animateXY(1000, 1000);
        }
    }

    @Override
    public void setExtraOffsets(int left, int top, int right, int bottom) {
        mLineChart.setExtraOffsets(left, top, right, bottom);
    }

    @Override   // 设置一组线颜色
    public void setColors(Colors colors) {
        LineData data = mLineChart.getData();
        if (data != null && data.getDataSetCount() != 0) {
            // 已经调用setData设置了数据,则改变颜色
            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
            super.setColors(set, colors);
        }
        mColors = colors;
    }

    @Override
    public void setLegendEnabled(boolean enabled) {
        mLineChart.getLegend().setEnabled(enabled);
    }

    @Override
    public void setYUnit(String yUnit) {
        super.setYUnit(yUnit);
        if (yUnit != null && !yUnit.trim().equals("")) {  // 有单位时间隔为1, 设置数值格式化
            mLineChart.getAxisLeft().setGranularity(1f);
            mLineChart.getAxisLeft().setValueFormatter(new YAxisValueFormatter());
            mLineChart.getAxisRight().setGranularity(1f);
            mLineChart.getAxisRight().setValueFormatter(new YAxisValueFormatter());
        }
    }

    /**
     * 设置线颜色
     *
     * @param color 颜色
     */
    public void setLineColor(int color) {
        LineData data = mLineChart.getData();
        if (data != null && data.getDataSetCount() != 0) {
            // 已经调用setData设置了数据,则改变颜色
            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
            set.setColor(color);
        }
        mLineColor = color;
    }

    /**
     * 添加限制线
     *
     * @param limit 限制数值
     * @param label 描述标签
     */
    public void addLimitLine(float limit, String label) {
        LimitLine ll = new LimitLine(limit, label);
        ll.setLineWidth(2f);    // 线宽
        ll.setLineColor(ColorTemplate.MATERIAL_COLORS[2]);  // 颜色
        ll.enableDashedLine(10, 10, 0); // 点点
        ll.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);    // 标签位置
        ll.setTextColor(ColorTemplate.MATERIAL_COLORS[2]);  // 文字颜色
        ll.setTextSize(12f);    // 文字大小
        mLineChart.getAxisLeft().addLimitLine(ll);
    }

    @Override
    public void setOnChartValueSelectedListener(OnChartValueSelectedListener listener) {
        mLineChart.setOnChartValueSelectedListener(listener);
    }

    /**
     * X轴值格式化
     */
    class XAxisValueFormatter implements AxisValueFormatter {

        @Override
        public String getFormattedValue(float v, AxisBase axisBase) {
            if (v < mXVals.length) {
                // 由于从X轴1处开始显示,相应标签也从1处开始&&超出范围则不显示
                return mXVals[(int) (v)];
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
