package com.augurit.agmobile.gzps.statistic.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.widget.MyGridView;
import com.augurit.agmobile.gzps.statistic.model.EchartsDataBean;
import com.augurit.agmobile.gzps.statistic.model.SignEchartsBarBean;
import com.augurit.agmobile.gzps.statistic.model.SignEchartsPieBean;
import com.augurit.agmobile.gzps.statistic.model.SignStatisticInfoBean;
import com.augurit.agmobile.gzps.statistic.model.StatisticBean;
import com.augurit.agmobile.gzps.statistic.model.UploadStatisticBean;
import com.augurit.agmobile.gzps.statistic.service.SignStatisticService;
import com.augurit.agmobile.gzps.statistic.view.uploadview.UploadStatiscAdapter;
import com.augurit.am.fw.utils.DensityUtil;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liangsh on 2017/11/8.
 */

public class SignInStatsFragment extends Fragment {

    private MyGridView mGridView;
    private LinearLayout mDistrcContain;
    private List<String> areas = new ArrayList<>();
    //private String[] areas = {"市水务局", "天河", "番禺", "黄埔", "白云", "南沙", "海珠", "荔湾", "花都", "越秀","增城", "从化", "净水公司", "全市"};

//    private String[] lead_yAxle = {"天河", "番禺", "黄埔", "白云", "南沙", "海珠", "荔湾", "花都", "越秀", "增城", "从化", "市排水公司"};
//    private String[] patrol_yAxle = {"市水务局","天河", "番禺", "黄埔", "白云", "南沙", "海珠", "荔湾", "花都", "越秀", "增城", "从化", "市排水公司"};

    private String[] lead_yAxle = {"市水务局", "越秀", "天河", "海珠", "荔湾", "白云", "黄埔", "番禺", "南沙", "花都", "增城", "从化", "市水投集团","提质增效小组","全市"};
    private String[] patrol_yAxle = {"市排水公司", "市净水公司", "科学城排水公司", "番禺污水公司", "南沙排水公司", "花都排水公司", "增城排水公司", "从化排水公司","全市"};
    private List<String> patrol_type = Arrays.asList("窨井", "雨水口", "排水口", "全部");
    private ArrayList<Float> datas = new ArrayList<>();
    private MyGridViewAdapter myGridViewAdapter;
    private TextView all_count, install_count, no_install_count;
    //    private WebView mWebView;
    private ProgressDialog progressDialog;
    private String echartsPieJson;
    private String currentOrgName = "全市";
    private ArrayList<StatisticBean.ChildOrg> childOrgs;
    private boolean currentRoleType = false;
    private boolean isInit = true;
    private SignStatisticService signStatisticService;
    private Context mContext;
    private Long startDate = null;
    private Long endDate = null;
    private Long TempEndDate = null;
    private static final int START_DATE = 1;
    private static final int END_DATE = 2;
    private WebView mWebView;
    private UploadStatiscAdapter mUploadStatiscAdapter;
    private SignEchartsPieBean mSignEchartsPieBean;
    private SignEchartsPieBean mSignEchartsPieBean2;
    private int year;
    private int month;
    private int day;
    private SignEchartsBarBean mSignEchartsBarBean;
    private final int RE_CODE = 0x123;
    private boolean allPower = false;
    private String currentUserOrg;
    private ScrollView mScrollView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signinstats2, null);
        currentUserOrg = "市水务局";
        //市水务局角色可以看全市安装情况 其他只看看自己本区
        if (currentUserOrg.equals("市水务局")) {
            allPower = true;
            currentOrgName = "全市";
        } else {
            currentOrgName = currentUserOrg;
        }
        initView(view);
        return view;
    }

    private void initView(View view) {
        mGridView = (MyGridView) view.findViewById(R.id.gv_area);
        mScrollView = (ScrollView) view.findViewById(R.id.scrollView);
        if (currentUserOrg.equals("市水务局")) {
            mGridView.setVisibility(View.VISIBLE);
        } else {
            mGridView.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mScrollView.getLayoutParams();
            params.height = DensityUtil.dp2px(getActivity(), 850);
            mScrollView.setLayoutParams(params);
        }

        all_count = (TextView) view.findViewById(R.id.all_install_count);
        install_count = (TextView) view.findViewById(R.id.install_count);
        no_install_count = (TextView) view.findViewById(R.id.no_install_count);
        mDistrcContain = (LinearLayout) view.findViewById(R.id.upload_statisc_distrc);
        if (signStatisticService == null) {
            signStatisticService = new SignStatisticService(mContext);
        }
        mWebView = (WebView) view.findViewById(R.id.chart_webview);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JavaScriptinterface(mContext), "android");
        mWebView.loadUrl("file:///android_asset/echarts/signchart.html");
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //最好在这里调用js代码 以免网页未加载完成
//                loadDatas(false, currentOrgName, currentType, startDate, endDate);
                loadYTData(false,currentOrgName, currentRoleType);
            }
        });
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("加载中...");
        progressDialog.setCancelable(true);
        view.findViewById(R.id.install_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SignTodayAndYesActivity.class);
                if (allPower) {
                    intent.putExtra("org_name", currentOrgName);
                } else {
                    intent.putExtra("org_name", currentUserOrg);
                }
//                intent.putExtra("org_name", currentOrgName);
                intent.putExtra("type",1);
                intent.putExtra("roleType",currentRoleType);
                startActivityForResult(intent, RE_CODE);
            }
        });
        view.findViewById(R.id.no_install_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SignTodayAndYesActivity.class);
                intent.putExtra("org_name", currentOrgName);
                intent.putExtra("type",0);
                intent.putExtra("roleType",currentRoleType);
                startActivityForResult(intent, RE_CODE);
            }
        });
        initTime();
        final TextView leadTv = (TextView) view.findViewById(R.id.lead_tv1);
        final TextView patrolTv = (TextView) view.findViewById(R.id.patrol_tv1);
        leadTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                leadTv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                leadTv.setTextColor(Color.WHITE);
                patrolTv.setBackgroundColor(Color.WHITE);
                patrolTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                currentRoleType = true;
                if (areas.size() == patrol_yAxle.length) {
//                  areas.add(0, "市水务局");
                    areas.clear();

                    areas.add("市水务局");
                    areas.add("越秀");
                    areas.add( "天河");
                    areas.add("海珠");
                    areas.add("荔湾");
                    areas.add("白云");
                    areas.add("黄埔");
                    areas.add("番禺");
                    areas.add("南沙");
                    areas.add("花都");
                    areas.add("增城");
                    areas.add("从化");
                    areas.add("市水投集团");
                    areas.add("提质增效小组");
                    areas.add("全市");

                }
                if (allPower) {
                    myGridViewAdapter.setPosition(areas.size() - 1);
                    myGridViewAdapter.notifyDataSetChanged();
                    currentOrgName = "全市";
                    loadYTData(false,currentOrgName, currentRoleType);
                    //          int index = areas.indexOf(currentOrgName);
                    //                    if (index == -1) {
                    //                        myGridViewAdapter.setPosition(areas.size() - 1);
                    //                        myGridViewAdapter.notifyDataSetChanged();
                    //                        getOrgAppInstallInfo("全市", currentRoleType, false);
                    //                    } else {
                    //                        myGridViewAdapter.setPosition(index);
                    //                        myGridViewAdapter.notifyDataSetChanged();
                    //                        getOrgAppInstallInfo(currentOrgName, currentRoleType, false);
                    //                    }
                } else {
                    loadYTData(false,currentOrgName, currentRoleType);
                }

            }
        });

        patrolTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patrolTv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                patrolTv.setTextColor(Color.WHITE);
                leadTv.setBackgroundColor(Color.WHITE);
                leadTv.setTextColor(getResources().getColor(R.color.colorPrimary));

                currentRoleType = false;

                if (areas.size() == lead_yAxle.length) {
                    areas.clear();
                    areas.add("市排水公司");
                    areas.add("市净水公司");
                    areas.add("科学城排水公司");
                    areas.add("番禺污水公司");
                    areas.add("南沙排水公司");
                    areas.add("花都排水公司");
                    areas.add("增城排水公司");
                    areas.add("从化排水公司");
                    areas.add("全市");
                }
                //每次切换一线人员和管理人员将按钮重置为全市便于柱状图刷新
                if (allPower) {
                    myGridViewAdapter.setPosition(areas.size() - 1);
                    myGridViewAdapter.notifyDataSetChanged();
                    currentOrgName = "全市";
                    loadYTData(false,currentOrgName, currentRoleType);
                    //                    int index = areas.indexOf(currentOrgName);
                    //                    if (index == -1) {
                    //                        myGridViewAdapter.setPosition(areas.size() - 1);
                    //                        myGridViewAdapter.notifyDataSetChanged();
                    //                        getOrgAppInstallInfo("全市", currentRoleType, false);
                    //                    } else {
                    //                        myGridViewAdapter.setPosition(index);
                    //                        myGridViewAdapter.notifyDataSetChanged();
                    //                        getOrgAppInstallInfo(currentOrgName, currentRoleType, false);
                    //                    }

                } else {
                    loadYTData(true,currentOrgName, currentRoleType);
                }

            }
        });

    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public class JavaScriptinterface {
        Context context;

        public JavaScriptinterface(Context c) {
            context = c;
        }

        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        public void toDetailPage(String org_name) {
            //点击事件
            Intent intent = new Intent(mContext, SignDetailActivity.class);
            intent.putExtra("org_name", org_name);
            intent.putExtra("roleType",currentRoleType);
            startActivityForResult(intent, RE_CODE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
//        loadYTData(false,currentOrgName);
    }

    /**
     * 获取昨天和今天的统计数据
     */
    private void loadYTData(boolean ifShowPb,String orgName,boolean roleType) {
        if (ifShowPb) {
            progressDialog.show();
        }
        String temp_org_name = null ;
//        if ("市排水公司".equals(orgName)){
//            temp_org_name = "排水有限公司";
//        }else if("市净水公司".equals(orgName)){
//            temp_org_name = "净水有限公司";
//        }else
            if(orgName.indexOf("排水公司")!=-1){
                if("科学城排水公司".equals(orgName)||"市排水公司".equals(orgName)){
                    temp_org_name = orgName;
                }else{
                    temp_org_name = orgName.replace("排水公司","");
                }
        }else{
            temp_org_name = orgName;
        }

        signStatisticService.getUploadNearTimeData(temp_org_name,roleType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SignStatisticInfoBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "获取签到数据失败");
                    }

                    @Override
                    public void onNext(List<SignStatisticInfoBean> signStatisticInfoBeans) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if(ListUtil.isEmpty(signStatisticInfoBeans)){
                            ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "获取签到数据失败");
                            return;
                        }
                        creatListData(signStatisticInfoBeans);
                        creatBarData(signStatisticInfoBeans);
                    }
                });
    }

    /**
     * 转成柱状图的数据
     * @param signStatisticInfoBeans
     */
    private void creatBarData(List<SignStatisticInfoBean> signStatisticInfoBeans ) {
        if(currentRoleType){
            //管理层
            getMangerInfo(signStatisticInfoBeans);
        }else{
          //  一线人员统计
           getLineInfo(signStatisticInfoBeans);
        }
//        if(mSignEchartsBarBean == null){
//            mSignEchartsBarBean = new SignEchartsBarBean();
//        }
//        List<Double> yesData = new ArrayList<>();
//        List<Double> todData = new ArrayList<>();
//        List<String> distrc = new ArrayList<>();
//        String monthStr = "" + month;
//        String dayStr = "" + day;
//        if(month < 10){
//            monthStr = "0" + monthStr;
//        }
//        if(day < 10){
//            dayStr = "0" + dayStr;
//        }
//        String time = year + "" + monthStr + "" + dayStr;
//        for(SignStatisticInfoBean signStatisticInfoBean : signStatisticInfoBeans){
//            //全市、今天
//            if(signStatisticInfoBean.getOrgName().equals("全市") && signStatisticInfoBean.getSignDate().equals(time)){
//                for (int i = 0; i < patrol_yAxle.length; i++) {
//                    String orgName = patrol_yAxle[i];
//                    if (ListUtil.isEmpty(signStatisticInfoBean.getChildOrgs())) {
//                        todData.add(0.00);
//                        continue;
//                    }
//                    for (SignStatisticInfoBean.ChildOrgsEntity childOrgsEntity : signStatisticInfoBean.getChildOrgs()) {
//                        if (childOrgsEntity.getOrgName().equals(orgName.replace("排水公司",""))
//                        ||childOrgsEntity.getOrgName().equals(orgName)) {
//                            distrc.add(orgName);
//                            todData.add(childOrgsEntity.getSignPercentage());
//                            break;
//                        }else if(childOrgsEntity.getOrgName().equals("黄埔")&&orgName.equals("科学城排水公司")){
//                            distrc.add(orgName);
//                            todData.add(childOrgsEntity.getSignPercentage());
//                            break;
//                        }else if(childOrgsEntity.getOrgName().equals("番禺")&&orgName.equals("番禺污水公司")){
//                            distrc.add(orgName);
//                            todData.add(childOrgsEntity.getSignPercentage());
//                            break;
//                        }else if(childOrgsEntity.getOrgName().equals("净水公司")&&orgName.equals("市净水公司")){
//                            distrc.add(orgName);
//                            todData.add(childOrgsEntity.getSignPercentage());
//                            break;
//                        }
//                    }
//                }
//            }else if(signStatisticInfoBean.getOrgName().equals("全市") && !signStatisticInfoBean.getSignDate().equals(time)){
//                for (int i = 0; i < patrol_yAxle.length; i++) {
//                    String orgName = patrol_yAxle[i];
//                    if (ListUtil.isEmpty(signStatisticInfoBean.getChildOrgs())) {
//                        yesData.add(0.00);
//                        continue;
//                    }
//                    for (SignStatisticInfoBean.ChildOrgsEntity childOrgsEntity : signStatisticInfoBean.getChildOrgs()) {
//
//                        if (childOrgsEntity.getOrgName().equals(orgName.replace("排水公司",""))
//                                ||childOrgsEntity.getOrgName().equals(orgName)) {
//                            yesData.add(childOrgsEntity.getSignPercentage());
//                            break;
//                        }else if(childOrgsEntity.getOrgName().equals("黄埔")&&orgName.equals("科学城排水公司")){
//                            yesData.add(childOrgsEntity.getSignPercentage());
//                            break;
//                        }else if(childOrgsEntity.getOrgName().equals("番禺")&&orgName.equals("番禺污水公司")){
//                            yesData.add(childOrgsEntity.getSignPercentage());
//                            break;
//                        }else if(childOrgsEntity.getOrgName().equals("净水公司")&&orgName.equals("市净水公司")){
//                            yesData.add(childOrgsEntity.getSignPercentage());
//                            break;
//                        }
////                        if (childOrgsEntity.getOrgName().equals(orgName)) {
////                            yesData.add(childOrgsEntity.getSignPercentage());
////                            break;
////                        }
//                    }
//                }
//            }else if(!signStatisticInfoBean.getOrgName().equals("全市") && signStatisticInfoBean.getSignDate().equals(time)){
//                for (int i = 0; i < patrol_yAxle.length; i++) {
//                    String orgName = patrol_yAxle[i];
//
//                    if (signStatisticInfoBean.getOrgName().equals(orgName.replace("排水公司",""))
//                            ||signStatisticInfoBean.getOrgName().equals(orgName)) {
//                        distrc.add(orgName);
//                        todData.add(signStatisticInfoBean.getSignPercentage());
//                        break;
//                    }else if(signStatisticInfoBean.getOrgName().equals("黄埔")&&orgName.equals("科学城排水公司")){
//                        distrc.add(orgName);
//                        todData.add(signStatisticInfoBean.getSignPercentage());
//                        break;
//                    }else if(signStatisticInfoBean.getOrgName().equals("番禺")&&orgName.equals("番禺污水公司")){
//                        distrc.add(orgName);
//                        todData.add(signStatisticInfoBean.getSignPercentage());
//                        break;
//                    }else if(signStatisticInfoBean.getOrgName().equals("净水公司")&&orgName.equals("市净水公司")){
//                        distrc.add(orgName);
//                        todData.add(signStatisticInfoBean.getSignPercentage());
//                        break;
//                    }else{
//                        distrc.add(orgName);
//                        todData.add(0.0);
//                    }
//                }
//            }else if(!signStatisticInfoBean.getOrgName().equals("全市") && !signStatisticInfoBean.getSignDate().equals(time)){
//                for (int i = 0; i < patrol_yAxle.length; i++) {
//                    String orgName = patrol_yAxle[i];
//
//                    if (signStatisticInfoBean.getOrgName().equals(orgName.replace("排水公司",""))
//                            ||signStatisticInfoBean.getOrgName().equals(orgName)) {
//                        yesData.add(signStatisticInfoBean.getSignPercentage());
//                        break;
//                    }else if(signStatisticInfoBean.getOrgName().equals("黄埔")&&orgName.equals("科学城排水公司")){
//                        yesData.add(signStatisticInfoBean.getSignPercentage());
//                        break;
//                    }else if(signStatisticInfoBean.getOrgName().equals("番禺")&&orgName.equals("番禺污水公司")){
//                        yesData.add(signStatisticInfoBean.getSignPercentage());
//                        break;
//                    }else if(signStatisticInfoBean.getOrgName().equals("净水公司")&&orgName.equals("市净水公司")){
//                        yesData.add(signStatisticInfoBean.getSignPercentage());
//                        break;
//                    }else{
//                        yesData.add(0.0);
//                    }
//                }
//            }
//        }
//        Collections.reverse(distrc);
//        Collections.reverse(todData);
//        Collections.reverse(yesData);
//        mSignEchartsBarBean.setTimes(distrc);
//        List<String> todData1 = new ArrayList<>();
//        List<String> yesData1 = new ArrayList<>();
//        for(Double t : todData){
//            todData1.add(StringUtil.valueOf(t, 2));
//        }
//        for(Double y : yesData){
//            yesData1.add(StringUtil.valueOf(y, 2));
//        }
//        mSignEchartsBarBean.setTodaydata(todData1);
//        mSignEchartsBarBean.setYesterdaydata(yesData1);
//        String json = JsonUtil.getJson(mSignEchartsBarBean);
//        mWebView.loadUrl("javascript:createBarChart('bar'," + json+");");
    }

    private void getLineInfo(List<SignStatisticInfoBean> signStatisticInfoBeans) {
        if(mSignEchartsBarBean == null){
            mSignEchartsBarBean = new SignEchartsBarBean();
        }
        List<Double> yesData = new ArrayList<Double>();
        List<Double> todData = new ArrayList<Double>();
        List<String> distrc = new ArrayList<String>();
        String monthStr = "" + month;
        String dayStr = "" + day;
        if(month < 10){
            monthStr = "0" + monthStr;
        }
        if(day < 10){
            dayStr = "0" + dayStr;
        }
        String time = year + "" + monthStr + "" + dayStr;
        for(SignStatisticInfoBean signStatisticInfoBean : signStatisticInfoBeans){
            //全市、今天
            if(signStatisticInfoBean.getOrgName().equals("全市") && signStatisticInfoBean.getSignDate().equals(time)){
                for (int i = 0; i < patrol_yAxle.length; i++) {
                    String orgName = patrol_yAxle[i];
                    if (ListUtil.isEmpty(signStatisticInfoBean.getChildOrgs())) {
                        todData.add(0.00);
                        continue;
                    }
                    for (SignStatisticInfoBean.ChildOrgsEntity childOrgsEntity : signStatisticInfoBean.getChildOrgs()) {
                        if (childOrgsEntity.getOrgName().equals(orgName.replace("排水公司",""))
                                ||childOrgsEntity.getOrgName().equals(orgName)) {
                            distrc.add(orgName);
                            todData.add(childOrgsEntity.getSignPercentage());
                            // break;
                        }else if(childOrgsEntity.getOrgName().equals("黄埔")&&orgName.equals("科学城排水公司")){
                            distrc.add(orgName);
                            todData.add(childOrgsEntity.getSignPercentage());
                            // break;
                        }else if(childOrgsEntity.getOrgName().equals("番禺")&&orgName.equals("番禺污水公司")){
                            distrc.add(orgName);
                            todData.add(childOrgsEntity.getSignPercentage());
                            // break;
                        }else if(childOrgsEntity.getOrgName().equals("净水公司")&&orgName.equals("市净水公司")){
                            distrc.add(orgName);
                            todData.add(childOrgsEntity.getSignPercentage());
                            // break;
                        }
                    }
                }
            }else if(signStatisticInfoBean.getOrgName().equals("全市") && !signStatisticInfoBean.getSignDate().equals(time)){
                for (int i = 0; i < patrol_yAxle.length; i++) {
                    String orgName = patrol_yAxle[i];
                    if (ListUtil.isEmpty(signStatisticInfoBean.getChildOrgs())) {
                        yesData.add(0.00);
                        continue;
                    }
                    for (SignStatisticInfoBean.ChildOrgsEntity childOrgsEntity : signStatisticInfoBean.getChildOrgs()) {

                        if (childOrgsEntity.getOrgName().equals(orgName.replace("排水公司",""))
                                ||childOrgsEntity.getOrgName().equals(orgName)) {
                            yesData.add(childOrgsEntity.getSignPercentage());
                            // break;
                        }else if(childOrgsEntity.getOrgName().equals("黄埔")&&orgName.equals("科学城排水公司")){
                            yesData.add(childOrgsEntity.getSignPercentage());
                            // break;
                        }else if(childOrgsEntity.getOrgName().equals("番禺")&&orgName.equals("番禺污水公司")){
                            yesData.add(childOrgsEntity.getSignPercentage());
                            // break;
                        }else if(childOrgsEntity.getOrgName().equals("净水公司")&&orgName.equals("市净水公司")){
                            yesData.add(childOrgsEntity.getSignPercentage());
                            // break;
                        }
//                        if (childOrgsEntity.getOrgName().equals(orgName)) {
//                            yesData.add(childOrgsEntity.getSignPercentage());
//                            break;
//                        }
                    }
                }
            }else if(!signStatisticInfoBean.getOrgName().equals("全市") && signStatisticInfoBean.getSignDate().equals(time)){
                for (int i = 0; i < patrol_yAxle.length; i++) {
                    String orgName = patrol_yAxle[i];
                    if("全市".equals(orgName)){ continue;}
                    if (signStatisticInfoBean.getOrgName().equals(orgName.replace("排水公司",""))
                            ||signStatisticInfoBean.getOrgName().equals(orgName)) {
                        distrc.add(orgName);
                        todData.add(signStatisticInfoBean.getSignPercentage());
                        // break;
                    }else if(signStatisticInfoBean.getOrgName().equals("黄埔")&&orgName.equals("科学城排水公司")){
                        distrc.add(orgName);
                        todData.add(signStatisticInfoBean.getSignPercentage());
                        // break;
                    }else if(signStatisticInfoBean.getOrgName().equals("番禺")&&orgName.equals("番禺污水公司")){
                        distrc.add(orgName);
                        todData.add(signStatisticInfoBean.getSignPercentage());
                        // break;
                    }else if(signStatisticInfoBean.getOrgName().equals("净水公司")&&orgName.equals("市净水公司")){
                        distrc.add(orgName);
                        todData.add(signStatisticInfoBean.getSignPercentage());
                        // break;
                    }else{
                        distrc.add(orgName);
                        todData.add(0.0);
                    }
                }
            }else if(!signStatisticInfoBean.getOrgName().equals("全市") && !signStatisticInfoBean.getSignDate().equals(time)){
                for (int i = 0; i < patrol_yAxle.length; i++) {
                    String orgName = patrol_yAxle[i];
                    if("全市".equals(orgName)){ continue;}
                    if (signStatisticInfoBean.getOrgName().equals(orgName.replace("排水公司",""))
                            ||signStatisticInfoBean.getOrgName().equals(orgName)) {
                        yesData.add(signStatisticInfoBean.getSignPercentage());
                        // break;
                    }else if(signStatisticInfoBean.getOrgName().equals("黄埔")&&orgName.equals("科学城排水公司")){
                        yesData.add(signStatisticInfoBean.getSignPercentage());
                        // break;
                    }else if(signStatisticInfoBean.getOrgName().equals("番禺")&&orgName.equals("番禺污水公司")){
                        yesData.add(signStatisticInfoBean.getSignPercentage());
                        // break;
                    }else if(signStatisticInfoBean.getOrgName().equals("净水公司")&&orgName.equals("市净水公司")){
                        yesData.add(signStatisticInfoBean.getSignPercentage());
                        // break;
                    }else{
                        yesData.add(0.0);
                    }
                }
            }
        }
//        Collections.reverse(distrc);
//        Collections.reverse(todData);
//        Collections.reverse(yesData);
        int length = distrc.size();
        for(int i=0;i<length-1;i++){
                   for(int j=0;j<length-1-i;j++){
                  if(todData.get(j)>todData.get(j+1)){        //交换过程
                      double temp = todData.get(j);
//                      todData.get(j) = todData.get(j+1);
//                      todData.get(j+1) = temp;
                      todData.set(j,todData.get(j+1));
                      todData.set(j+1,temp);

                      double tempYes = yesData.get(j);
                      yesData.set(j,yesData.get(j+1));
                      yesData.set(j+1,tempYes);

                      String dis = distrc.get(j);
                      distrc.set(j,distrc.get(j+1));
                      distrc.set(j+1,dis);
                            }
                        }
                  }
        mSignEchartsBarBean.setTimes(distrc);
        List<String> todData1 = new ArrayList<>();
        List<String> yesData1 = new ArrayList<>();
        for(Double t : todData){
            todData1.add(StringUtil.valueOf(t, 2));
        }
        for(Double y : yesData){
            yesData1.add(StringUtil.valueOf(y, 2));
        }
        mSignEchartsBarBean.setTodaydata(todData1);
        mSignEchartsBarBean.setYesterdaydata(yesData1);
        String json = JsonUtil.getJson(mSignEchartsBarBean);
        mWebView.loadUrl("javascript:createBarChart('bar'," + json+");");
    }

    private void getMangerInfo(List<SignStatisticInfoBean> signStatisticInfoBeans) {
        if(mSignEchartsBarBean == null){
            mSignEchartsBarBean = new SignEchartsBarBean();
        }
        List<Double> yesData = new ArrayList<>();
        List<Double> todData = new ArrayList<>();
        List<String> distrc = new ArrayList<>();
        String monthStr = "" + month;
        String dayStr = "" + day;
        if(month < 10){
            monthStr = "0" + monthStr;
        }
        if(day < 10){
            dayStr = "0" + dayStr;
        }
        String time = year + "" + monthStr + "" + dayStr;
        for(SignStatisticInfoBean signStatisticInfoBean : signStatisticInfoBeans){
            //全市、今天
            if(signStatisticInfoBean.getOrgName().equals("全市") && signStatisticInfoBean.getSignDate().equals(time)){
                for (int i = 0; i < lead_yAxle.length; i++) {
                    String orgName = lead_yAxle[i];
                    if (ListUtil.isEmpty(signStatisticInfoBean.getChildOrgs())) {
                        todData.add(0.00);
                        continue;
                    }
                    for (SignStatisticInfoBean.ChildOrgsEntity childOrgsEntity : signStatisticInfoBean.getChildOrgs()) {
                        if (childOrgsEntity.getOrgName().equals(orgName)) {
                            distrc.add(orgName);
                            todData.add(childOrgsEntity.getSignPercentage());
                            // break;
                        }else if(childOrgsEntity.getOrgName().equals("市水投")&&orgName.equals("市水投集团")){
                            distrc.add(orgName);
                            todData.add(childOrgsEntity.getSignPercentage());
                            // break;
                        }
                    }
                }
            }else if(signStatisticInfoBean.getOrgName().equals("全市") && !signStatisticInfoBean.getSignDate().equals(time)){
                for (int i = 0; i < lead_yAxle.length; i++) {
                    String orgName = lead_yAxle[i];
                    if (ListUtil.isEmpty(signStatisticInfoBean.getChildOrgs())) {
                        yesData.add(0.00);
                        continue;
                    }
                    for (SignStatisticInfoBean.ChildOrgsEntity childOrgsEntity : signStatisticInfoBean.getChildOrgs()) {

                        if (childOrgsEntity.getOrgName().equals(orgName)){
                            yesData.add(childOrgsEntity.getSignPercentage());
                            // break;
                        }else if(childOrgsEntity.getOrgName().equals("市水投")&&orgName.equals("市水投集团")){
                            yesData.add(childOrgsEntity.getSignPercentage());
                            // break;
                        }
//                        if (childOrgsEntity.getOrgName().equals(orgName)) {
//                            yesData.add(childOrgsEntity.getSignPercentage());
//                            break;
//                        }
                    }
                }
            }else if(!signStatisticInfoBean.getOrgName().equals("全市") && signStatisticInfoBean.getSignDate().equals(time)){
                for (int i = 0; i < lead_yAxle.length; i++) {
                    String orgName = lead_yAxle[i];

                    if (signStatisticInfoBean.getOrgName().equals(orgName)) {
                        distrc.add(orgName);
                        todData.add(signStatisticInfoBean.getSignPercentage());
                        // break;
                    }else if(signStatisticInfoBean.getOrgName().equals("市水投")&&orgName.equals("市水投集团")){
                        distrc.add(orgName);
                        todData.add(signStatisticInfoBean.getSignPercentage());
                        // break;
                    }else{
                        distrc.add(orgName);
                        todData.add(0.0);
                    }
                }
            }else if(!signStatisticInfoBean.getOrgName().equals("全市") && !signStatisticInfoBean.getSignDate().equals(time)){
                for (int i = 0; i < lead_yAxle.length; i++) {
                    String orgName = lead_yAxle[i];

                    if (signStatisticInfoBean.getOrgName().equals(orgName)) {
                        yesData.add(signStatisticInfoBean.getSignPercentage());
                        // break;
                    }else if(signStatisticInfoBean.getOrgName().equals("市水投")&&orgName.equals("市水投集团")){
                        yesData.add(signStatisticInfoBean.getSignPercentage());
                        // break;
                    }else{
                        yesData.add(0.0);
                    }
                }
            }
        }
//        Collections.reverse(distrc);
//        Collections.reverse(todData);
//        Collections.reverse(yesData);

        int length = distrc.size();
        for(int i=0;i<length-1;i++){
            for(int j=0;j<length-1-i;j++){
                if(todData.get(j)>todData.get(j+1)){        //交换过程
                    double temp = todData.get(j);
//                      todData.get(j) = todData.get(j+1);
//                      todData.get(j+1) = temp;
                    todData.set(j,todData.get(j+1));
                    todData.set(j+1,temp);

                    double tempYes = yesData.get(j);
                    yesData.set(j,yesData.get(j+1));
                    yesData.set(j+1,tempYes);

                    String dis = distrc.get(j);
                    distrc.set(j,distrc.get(j+1));
                    distrc.set(j+1,dis);
                }
            }
        }

        mSignEchartsBarBean.setTimes(distrc);
        List<String> todData1 = new ArrayList<>();
        List<String> yesData1 = new ArrayList<>();
        for(Double t : todData){
            todData1.add(StringUtil.valueOf(t, 2));
        }
        for(Double y : yesData){
            yesData1.add(StringUtil.valueOf(y, 2));
        }
        mSignEchartsBarBean.setTodaydata(todData1);
        mSignEchartsBarBean.setYesterdaydata(yesData1);


        String json = JsonUtil.getJson(mSignEchartsBarBean);
        mWebView.loadUrl("javascript:createBarChart('bar'," + json+");");
    }

    private void creatListData(List<SignStatisticInfoBean> signStatisticInfoBeans) {
        int total = 0;
        int yesterday_sign = 0;
        int yesterday_not_sign = 0;
        double yesterday_percent = 0.0;
        int today_sign = 0;
        int today_not_sign = 0;
        double today_percent = 0.0;
        String monthStr = "" + month;
        String dayStr = "" + day;
        if(month < 10){
            monthStr = "0" + monthStr;
        }
        if(day < 10){
            dayStr = "0" + dayStr;
        }
        String time = year + "" + monthStr + "" + dayStr;
        mSignEchartsPieBean.setTitle("签到率统计");
        for(SignStatisticInfoBean signStatisticInfoBean:signStatisticInfoBeans){
            if(time.equals(signStatisticInfoBean.getSignDate())){
                total += signStatisticInfoBean.getTotal();
                today_sign += signStatisticInfoBean.getSignNumber();
                today_percent += signStatisticInfoBean.getSignPercentage();
            }else{
                yesterday_sign += signStatisticInfoBean.getSignNumber();
                yesterday_percent+=signStatisticInfoBean.getSignPercentage();
            }
        }
        all_count.setText(total+ "");
        no_install_count.setText(yesterday_sign + "");
        install_count.setText(today_sign + "");
        yesterday_not_sign = total - yesterday_sign;
        today_not_sign = total - today_sign;
        //模拟数据
        List<String> legends = new ArrayList<>();
        List<SignEchartsPieBean.ValueData> valueDatas = new ArrayList<>();
        SignEchartsPieBean.ValueData valueData = new SignEchartsPieBean.ValueData();
        valueData.setName("总人数:"+total+"\n"+"签到数:"+today_sign);
        valueData.setValue(today_not_sign);
        valueDatas.add(valueData);
        SignEchartsPieBean.ValueData valueData2 = new SignEchartsPieBean.ValueData();
        valueData2.setName("今日"+StringUtil.valueOf(today_percent, 2)+"%");
        valueData2.setValue(today_sign);
        valueDatas.add(valueData2);
        mSignEchartsPieBean.setValues(valueDatas);
        legends.add("今日"+StringUtil.valueOf(today_percent, 2)+"%");
        mSignEchartsPieBean2 = new SignEchartsPieBean();
        //模拟数据
        mSignEchartsPieBean2.setTitle("签到率统计");
        List<SignEchartsPieBean.ValueData> zvalueDatas = new ArrayList<>();
        SignEchartsPieBean.ValueData zvalueData = new SignEchartsPieBean.ValueData();
        zvalueData.setName("总人数:"+total+"\n"+" 签到数:"+yesterday_sign+" ");
        zvalueData.setValue(yesterday_not_sign);
        zvalueDatas.add(zvalueData);
        SignEchartsPieBean.ValueData zvalueData2 = new SignEchartsPieBean.ValueData();
        zvalueData2.setName("昨日"+ StringUtil.valueOf(yesterday_percent, 2)+"%");
        zvalueData2.setValue(yesterday_sign);
        zvalueDatas.add(zvalueData2);
        mSignEchartsPieBean2.setValues(zvalueDatas);
        legends.add("昨日"+StringUtil.valueOf(yesterday_percent, 2)+"%");
        mSignEchartsPieBean.setLegends(legends);
        String json1 = JsonUtil.getJson(mSignEchartsPieBean);
        String json2 = JsonUtil.getJson(mSignEchartsPieBean2);
        mWebView.loadUrl("javascript:createPieChart('pie'," + json1 + ","+json2+");");
    }

    private void creatBarChart(UploadStatisticBean uploadStatisticBean) {
        List<UploadStatisticBean.ChartsEntity> charts = uploadStatisticBean.getCharts();
        float[] floats = new float[lead_yAxle.length];
        for (int i = 0; i < lead_yAxle.length; i++) {
            String orgName = lead_yAxle[i];
            if (ListUtil.isEmpty(charts)) {
                floats[i] = 0;
                break;
            }
            for (UploadStatisticBean.ChartsEntity childOrg : charts) {
                if (childOrg.getName().contains(orgName) || (orgName.equals("净水公司") && childOrg.getName().contains("净水"))) {
                    floats[i] = Float.parseFloat(String.valueOf(childOrg.getTotal()));
                    break;
                } else {
                    floats[i] = 0;
                }
            }
        }
        String echartsBarJson = EchartsDataBean.getInstance().getEchartsBarJson(lead_yAxle, null, floats);
        mWebView.loadUrl("javascript:createBarChart('bar'," + echartsBarJson + ");");
    }

    private void setViewData(StatisticBean bean) {
        all_count.setText(bean.getData().getTotal() + "");
        install_count.setText(bean.getData().getInstall() + "");
        no_install_count.setText((bean.getData().getTotal()) - (bean.getData().getInstall()) + "");

    }

    private void initData() {
        mSignEchartsPieBean = new SignEchartsPieBean();
        mUploadStatiscAdapter = new UploadStatiscAdapter(mContext);
        mDistrcContain.setVisibility(View.VISIBLE);

        mGridView.setVisibility(View.VISIBLE);
        areas.add("市排水公司");
        areas.add("市净水公司");
        areas.add("科学城排水公司");
        areas.add("番禺污水公司");
        areas.add("南沙排水公司");
        areas.add("花都排水公司");
        areas.add("增城排水公司");
        areas.add("从化排水公司");
        areas.add("全市");

        //区域
        myGridViewAdapter = new MyGridViewAdapter();
        myGridViewAdapter.setPosition(areas.size() - 1);
        myGridViewAdapter.setArrayList(areas);
        myGridViewAdapter.setOnItemListener(new OnItemListener() {
            @Override
            public void onClick(int position) {
                currentOrgName = areas.get(position);
                loadYTData(false,currentOrgName, currentRoleType);
            }
        });
        mGridView.setAdapter(myGridViewAdapter);
    }

    class MyGridViewAdapter extends BaseAdapter {
        private int selectedPosition = 0;// 选中的位置
        private OnItemListener onItemListener;
        private List<String> arrayList;

        public void setOnItemListener(OnItemListener onItemListener) {
            this.onItemListener = onItemListener;
        }

        public void setArrayList(List<String> arrayList) {
            this.arrayList = arrayList;
        }

        public void setPosition(int pos) {
            this.selectedPosition = pos;
        }

        @Override
        public int getCount() {
            return arrayList.size() == 0 ? 0 : arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            if (arrayList != null && arrayList.size() > position) {
                return arrayList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (arrayList != null && arrayList.size() > position) {
                return position;
            }
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mContext, R.layout.gridview_item_layout, null);
            TextView area_name = (TextView) view.findViewById(R.id.tv_area_name);
            area_name.setText(arrayList.get(position));
            ImageView iv = (ImageView) view.findViewById(R.id.iv_selected);
            if (selectedPosition == position) {
                area_name.setBackground(getResources().getDrawable(R.drawable.corner_color_primary3));
                area_name.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                iv.setVisibility(View.VISIBLE);
            } else {
                area_name.setBackground(getResources().getDrawable(R.drawable.corner_color_write));
                area_name.setTextColor(Color.BLACK);
                iv.setVisibility(View.GONE);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPosition == position) {//点击同个按钮就不作处理
                        return;
                    }
                    selectedPosition = position;
                    if (onItemListener != null) {
                        onItemListener.onClick(selectedPosition);
                    }
                    notifyDataSetChanged();
                }
            });
            return view;
        }

    }

    public interface OnItemListener {
        void onClick(int position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadYTData(false,currentOrgName,currentRoleType);
    }
}
