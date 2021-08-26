package com.augurit.am.cmpt.widget.echart;

import android.content.Context;
import android.util.AttributeSet;

import com.google.gson.Gson;

/**
 * 描述：ECharts饼状图
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.amchart
 * @createTime 创建时间 ：2017-02-21
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-21
 * @modifyMemo 修改备注：
 */
public class PieEChart extends BaseEChart {

    public PieEChart(Context context) {
        super(context);
    }

    public PieEChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieEChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setChartData() {
        Gson gson = new Gson();
        String xJo = gson.toJson(mXVals);
        String yJo = gson.toJson(mYVals);
        xJo = xJo.replaceAll("\"", "\'");
        yJo = yJo.replaceAll("\"", "\'");
        String command = "javascript:createChart('pie', " +
                xJo +
                "," +
                yJo +
                ");";
        mWebView.loadUrl(command);
    }
}
