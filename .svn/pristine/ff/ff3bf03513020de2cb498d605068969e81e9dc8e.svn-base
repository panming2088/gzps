package com.augurit.agmobile.gzps.statistic.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.webview.WebViewConstant;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentTypeConstant;
import com.augurit.agmobile.gzps.statistic.model.StatisticResult2;
import com.augurit.agmobile.gzps.statistic.service.StatisticService2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.spinner.AMSpinner;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xcl on 2017/10/23.
 */

public class StatisticFragment extends Fragment {

    private WebView webView;
    private ProgressBar pg1;
    private AMSpinner spinner_filter_type;
    private AMSpinner spinner_filter_area;
    private StatisticService2 statisticService2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistic, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView=(WebView) view.findViewById(R.id.web_view);
        pg1=(ProgressBar) view.findViewById(R.id.progressBar1);
        view.findViewById(R.id.ll_back).setVisibility(View.GONE);
        ((TextView)view.findViewById(R.id.tv_title)).setText("统计");
        webView.setWebViewClient(new WebViewClient(){
            //覆写shouldOverrideUrlLoading实现内部显示网页
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO 自动生成的方法存根
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings seting=webView.getSettings();
        seting.setJavaScriptEnabled(true);//设置webview支持javascript脚本
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根

                if(newProgress==100){
                    pg1.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    pg1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg1.setProgress(newProgress);//设置进度值
                }

            }
        });
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {  //表示按返回键时的操作

                        webView.goBack();   //后退
                        //webview.goForward();//前进
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
        String baseLayerServerUrl = BaseInfoManager.getBaseServerUrlWithoutRestSystem(getActivity());
        String url = baseLayerServerUrl+ WebViewConstant.H5_URLS.MY_SIGN_IN_STATISTIC_URL;
        webView.loadUrl(url);
        /**
         * 筛选条件
         */
        spinner_filter_type = (AMSpinner) view.findViewById(R.id.spinner_filter_type);
        spinner_filter_area = (AMSpinner) view.findViewById(R.id.spinner_filter_area);
        statisticService2 = new StatisticService2();
        initFilterCondition();
    }

    private void initFilterCondition() {
//        Map<String, Object> filterTypes = new LinkedHashMap<>();
//        filterTypes.put("查漏补缺", ComponentTypeConstant.NEW_ADDED_COMPONENT_VALUE);
//        filterTypes.put("数据修正", ComponentTypeConstant.OLD_COMPONENT_VALUE2);
//        filterTypes.put("签到统计", null);

        spinner_filter_type.addItems("查漏补缺", ComponentTypeConstant.NEW_ADDED_COMPONENT_VALUE);
        spinner_filter_type.addItems("数据修正", ComponentTypeConstant.OLD_COMPONENT_VALUE2);
        spinner_filter_type.addItems("签到统计", null);

        spinner_filter_type.setOnItemClickListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                if (item == null) {
                    //签到统计
                } else {
                    statisticService2.statisticByDistrict((String) item)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Map<String, List<StatisticResult2>>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(Map<String, List<StatisticResult2>> stringListMap) {
                                    webView.loadUrl("file:///android_asset/statistic/leftbarchart.html");
                                    //webView.setVisibility(View.VISIBLE);
                                }
                            });
                }
            }
        });

        Map<String, Object> filterAreas = new LinkedHashMap<>();
        filterAreas.put("天河区", "天河区");
        filterAreas.put("越秀区", "越秀区");
        filterAreas.put("番禺区", "番禺区");
        filterAreas.put("白云区", "白云区");
        filterAreas.put("南沙区", "南沙区");
        spinner_filter_area.setItemsMap(filterAreas);
    }
}
