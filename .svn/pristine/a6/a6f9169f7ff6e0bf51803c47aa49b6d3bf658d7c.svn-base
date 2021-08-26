package com.augurit.am.cmpt.widget.amchart;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

/**
 * 图表控件基类
 * Created by xiejiexin on 2016-07-29.
 */
public abstract class AMBaseChart extends LinearLayout {

    /**
     * X轴数值
     */
    protected String[] mXVals;

    /**
     * y轴数值
     */
    protected float[] mYVals;

    /**
     * 描述标签，图例名称
     */
    protected String mLabel;

    /**
     * Y轴单位
     */
    protected String mYUnit;

    /**
     * 图例是否显示
     */
    protected boolean mLegendEnabled;

    /**
     * 颜色
     */
    protected Colors mColors;

    /**
     * 颜色类型
     */
    public enum Colors {
        LIBERTY,
        JOYFUL,
        PASTEL,
        COLORFUL,
        VORDIPLOM,
        MATERIAL
    }

    public AMBaseChart(Context context) {
        this(context, null);
    }

    public AMBaseChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AMBaseChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChart(context);
    }

    /**
     * 初始化图表
     * 子类应在此方法中初始化图表View并使用addView方法将其添加到视图
     */
    protected abstract void initChart(Context context);

    /**
     * 初始化图表数据
     *
     * @param xVals x轴数据
     * @param yVals y轴数据
     */
    public void setData(String[] xVals, float[] yVals) {
        setData(xVals, yVals, "");
    }

    /**
     * 初始化图表数据
     *
     * @param xVals x轴数据
     * @param yVals y轴数据
     * @param label 描述标签，图例名称
     */
    public void setData(String[] xVals, float[] yVals, String label) {
        mXVals = xVals;
        mYVals = yVals;
        mLabel = label;
        setChartData();
    }

    /**
     * 将数据设置到图表
     * 子类应在此方法中设置图表数据
     */
    protected abstract void setChartData();

    /**
     * 设置图表的ExtraOffsets
     * 即图表距离边界的距离，类似于Padding
     * 通过此方法可控制图表大小，以解决数值显示超出范围的问题
     *
     * @param left   左边距
     * @param top    上边距
     * @param right  右边距
     * @param bottom 下边距
     */
    public abstract void setExtraOffsets(int left, int top, int right, int bottom);

    /**
     * 设置图表颜色
     *
     * @param colors 颜色类型
     */
    public abstract void setColors(Colors colors);

    /**
     * 根据颜色类型设置对应的ColorTemplate中的颜色
     *
     * @param set    数据组
     * @param colors 颜色类型
     */
    protected void setColors(DataSet set, Colors colors) {
        set.setColors(switchColor(colors));
    }

    /**
     * 返回ColorTemplate中对应颜色
     *
     * @param colors 颜色类型
     */
    protected int[] switchColor(Colors colors) {
        int[] c;
        switch (colors) {
            case COLORFUL:
                c = ColorTemplate.COLORFUL_COLORS;
                break;
            case JOYFUL:
                c = ColorTemplate.JOYFUL_COLORS;
                break;
            case LIBERTY:
                c = ColorTemplate.LIBERTY_COLORS;
                break;
            case MATERIAL:
                c = ColorTemplate.MATERIAL_COLORS;
                break;
            case PASTEL:
                c = ColorTemplate.PASTEL_COLORS;
                break;
            case VORDIPLOM:
                c = ColorTemplate.VORDIPLOM_COLORS;
                break;
            default:
                c = ColorTemplate.MATERIAL_COLORS;
                break;
        }
        return c;
    }

    /**
     * 设置Y轴数值单位
     *
     * @param yUnit 数值单位
     */
    public void setYUnit(String yUnit) {
            mYUnit = yUnit;
    }

    /**
     * 设置图例是否显示
     * @param enabled 是否显示
     */
    public abstract void setLegendEnabled(boolean enabled);

    /**
     * 设置图表值点击监听
     *
     * @param listener 监听
     */
    public abstract void setOnChartValueSelectedListener(OnChartValueSelectedListener listener);

}
