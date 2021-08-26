package com.augurit.am.cmpt.widget.echart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：ECharts柱状图
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.amchart
 * @createTime 创建时间 ：2017-02-21
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-21
 * @modifyMemo 修改备注：
 */
public class BarEChart extends BaseEChart {

    private List<Map<String, Object>> mSeries;

    public BarEChart(Context context) {
        super(context);
    }

    public BarEChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarEChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setChartData() {
        Gson gson = new Gson();
        String xJo = gson.toJson(mXVals);
        String yJo = gson.toJson(mYVals);
        xJo = xJo.replaceAll("\"", "\'");
        yJo = yJo.replaceAll("\"", "\'");
        String command = "javascript:createChart('bar', " +
                xJo +
                "," +
                yJo +
                ");";
        mWebView.loadUrl(command);
    }

    public void setData(String[] xVals, LinkedHashMap<String, double[]> yVals) {
        // 构造series
        List<Map<String, Object>> series = new ArrayList<>();
        for (Map.Entry<String, double[]> entry : yVals.entrySet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", entry.getKey());
            map.put("type", "bar");
            map.put("data", entry.getValue());
//            map.put("stack", "总量");
            series.add(map);
        }
        mXVals = xVals;
        mSeries = series;
        removeAllViews();
        initBarChart(getContext());
    }

    protected void initBarChart(Context context) {
        mWebView = new WebView(context);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/echarts/charts.html");
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mWebView);
        final ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyle);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(progressBar, params);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                if (mXVals != null && mSeries!= null) {
                    setBarChartData();
                }
                removeView(progressBar);
            }
        });
    }

    private void setBarChartData() {
        Gson gson = new Gson();
        String xJo = gson.toJson(mXVals);
        String seriesJo = gson.toJson(mSeries);
        xJo = xJo.replaceAll("\"", "\'");
        seriesJo = seriesJo.replaceAll("\"", "\'");
        String command = "javascript:createChart('group_bar', " + "\"" +
                xJo + "\"" +
                "," + "\"" +
                seriesJo + "\"" +
                ");";
        mWebView.loadUrl(command);
    }
}
