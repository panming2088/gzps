package com.augurit.agmobile.gzps.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

/**
 * 描述：ECharts图表库封装基类
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.amchart
 * @createTime 创建时间 ：2017-02-21
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-21
 * @modifyMemo 修改备注：
 */
public class HorizontalBarEChart extends RelativeLayout {
    /**
     * X轴数值
     */
    protected String[] mXVals;

    /**
     * y轴数值
     */
    protected double[] mYVals;

    /**
     * y轴数值
     */
    protected double[] mYVals2;

    protected WebView mWebView;

    public HorizontalBarEChart(Context context) {
        this(context, null);
    }

    public HorizontalBarEChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalBarEChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChart(context);
    }

    /**
     * 初始化图表
     * @param context Context
     */
    protected void initChart(Context context) {
        mWebView = new WebView(context);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/echarts/horizontal_barchart.html");
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
                if (mXVals != null && mYVals!= null) {
                    setChartData();
                }
                removeView(progressBar);
            }
        });
    }

    /**
     * 初始化图表数据
     *
     * @param xVals x轴数据
     * @param yVals y轴数据
     */
    public void setData(String[] xVals, double[] yVals, double[] yVal2) {
        mXVals = xVals;
        mYVals = yVals;
        mYVals2 = yVal2;
        setChartData();
    }

    protected void setChartData(){
        Gson gson = new Gson();
        String xJo = gson.toJson(mXVals);
        String yJo = gson.toJson(mYVals);
        String yJo2 = gson.toJson(mYVals2);
        xJo = xJo.replaceAll("\"", "\'");
        yJo = yJo.replaceAll("\"", "\'");
        String command = "javascript:initChart(\"" + xJo + "\",\"" + yJo + "\",\"" + yJo2 + "\");";
        mWebView.loadUrl(command);
    }

    @Override
    public void setVisibility(int visibility) {
//        if (visibility == VISIBLE) {
//            setChartData();
//        }
        super.setVisibility(visibility);
    }
}
