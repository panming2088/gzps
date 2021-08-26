package com.augurit.agmobile.gzps.statistic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.augurit.am.fw.utils.log.LogUtil;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agwater_ss.widget
 * @createTime 创建时间 ：2017-06-08
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-06-08
 * @modifyMemo 修改备注：
 */
public class BarChart extends RelativeLayout {

    /**
     * X轴数值
     */
    protected String[] mXVals;

    /**
     * y轴数值
     */
    protected double[] mYVals;

    protected List<Map<String, Object>> mSeries;

    protected WebView mWebView;

    public BarChart(Context context) {
        this(context, null);
    }

    public BarChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initChart(context);
    }

    /**
     * 初始化图表
     * @param context Context
     */
    public void initChart(Context context) {
        mWebView = new WebView(context);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/echarts/barchart.html");
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
                    setChartData();
                }
                removeView(progressBar);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                LogUtil.d("Chart", consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });
    }

    /**
     * 初始化图表数据
     *
     */
    public void setData(String[] xVals, List<Map<String, Object>> series) {
        mXVals = xVals;
        mSeries = series;
//        setChartData();
    }

    private void setChartData() {
        Gson gson = new Gson();
        String xJo = gson.toJson(mXVals);
        String seriesJo = gson.toJson(mSeries);
        xJo = xJo.replaceAll("\"", "\'");
        seriesJo = seriesJo.replaceAll("\"", "\'");
        String command = "javascript:createChart(" + "\"" +
                xJo + "\"" +
                "," + "\"" +
                seriesJo + "\"" +
                ");";
        mWebView.loadUrl(command);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }
}
