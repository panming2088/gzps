package com.augurit.am.cmpt.widget.amchart;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 饼状图控件
 * Created by xiejiexin on 2016-08-01.
 */
public class AMPieChart extends AMBaseChart {

    private PieChart mPieChart;
    private boolean mIsValueOutside = false;

    public AMPieChart(Context context) {
        super(context);
    }

    public AMPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AMPieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initChart(Context context) {
        mPieChart = new PieChart(context);
        mPieChart.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mPieChart.setExtraOffsets(5, 5, 5, 5);
        mPieChart.setDescription("");
        mPieChart.setNoDataTextDescription("没有数据");
        mPieChart.setUsePercentValues(true);    // 百分比显示值
        mPieChart.setDrawEntryLabels(true);     // 显示标签(即X轴值)
        mPieChart.setEntryLabelTextSize(10f);
//        mPieChart.setCenterText("统计结果");
//        mPieChart.setDrawCenterText(true);

        addView(mPieChart);
    }

    @Override
    protected void setChartData() {
        int[] colorTemplate;

        // 初始化显示值
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < mYVals.length; i ++) {
            entries.add(new PieEntry(mYVals[i], mXVals[i]));
        }

        PieDataSet set = new PieDataSet(entries, mLabel);
        // 设置切片样式
        if (mColors != null) {   // 已调用setColors方法设置了颜色
            super.setColors(set, mColors);
            colorTemplate = switchColor(mColors);
        } else {    // 否则采用默认设置
            set.setColors(ColorTemplate.MATERIAL_COLORS);
            colorTemplate = ColorTemplate.MATERIAL_COLORS;
        }
        set.setSliceSpace(3f);     // 切片间隔大小
        // 设置数值样式
        int valueColor = Color.WHITE;   // 数值颜色（x、y）
        if (mIsValueOutside) {
            // 数值显示在外部
            set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            set.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            set.setValueLinePart1Length(0.8f);
            valueColor = Color.BLACK;
        }
        set.setValueTextColor(valueColor);
        set.setValueFormatter(new PercentValueFormatter());
        set.setValueTextSize(10f);

        PieData data = new PieData(set);
        mPieChart.setEntryLabelColor(valueColor);

        mPieChart.setData(data);

        // 图例
        Legend legend = mPieChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        int[] colors = new int[mXVals.length];    // 初始化图例颜色，使其与X值一一对应
        int j = 0;
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorTemplate[j];
            j ++;
            if (j == colorTemplate.length) {
                j = 0;
            }
        }
        legend.setCustom(colors, mXVals);
    }

    /**
     * 设置可见性，为可见时显示动画效果
     */
    public void setVisibility(int visibility) {
        if (visibility == VISIBLE) {
            mPieChart.animateXY(1000, 1000);
        }
        super.setVisibility(visibility);
        mPieChart.setVisibility(visibility);
    }

    /**
     * 设置标签是否在显示在表外<p>
     * 默认为false
     * @param isOutside 是否在表外
     */
    public void setValueOutside(boolean isOutside) {
        mIsValueOutside = isOutside;
        if (mPieChart.getData() != null) {
            setChartData();
        }
    }

    /**
     * 设置是否显示中心圆<p>
     * 默认为true
     * @param isShow 是否显示
     */
    public void setShowCircle(boolean isShow) {
        mPieChart.setDrawHoleEnabled(isShow);
    }

    @Override // 设置切片颜色
    public void setColors(AMBaseChart.Colors colors) {
        PieData data = mPieChart.getData();
        if (data != null) {
            // 已经调用setData设置了数据,则改变颜色
            PieDataSet set = (PieDataSet) data.getDataSetByIndex(0);
            super.setColors(set, colors);
        }
        mColors = colors;
    }

    @Override
    public void setLegendEnabled(boolean enabled) {
        mPieChart.getLegend().setEnabled(enabled);
    }

    @Override
    public void setExtraOffsets(int left, int top, int right, int bottom) {
        mPieChart.setExtraOffsets(left, top, right, bottom);
    }

    @Override
    public void setOnChartValueSelectedListener(OnChartValueSelectedListener listener) {
        mPieChart.setOnChartValueSelectedListener(listener);
    }

    /**
     * 百分比数值格式化
     */
    class PercentValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // 使用entry.getY()取Y值..
            DecimalFormat format = new DecimalFormat("##.##");
            return format.format(value) + "%";
        }
    }
}
