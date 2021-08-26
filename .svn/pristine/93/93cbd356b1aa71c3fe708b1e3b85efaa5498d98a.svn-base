package com.augurit.agmobile.gzps.statistic.view.uploadview;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.widget.MyGridView;
import com.augurit.agmobile.gzps.statistic.model.EchartsDataBean;
import com.augurit.agmobile.gzps.statistic.model.StatisticBean;
import com.augurit.agmobile.gzps.statistic.model.UploadStatisticBean;
import com.augurit.agmobile.gzps.statistic.model.UploadYTStatisticBean;
import com.augurit.agmobile.gzps.statistic.service.UploadStatisticService;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by liangsh on 2017/11/6.
 */

public class UploadStatsFragment extends Fragment {

    private MyGridView mGridView;
    private MyGridView mTypeGridView;
    private MyGridView mTodayGridView;
    private LinearLayout mDistrcContain;
    private List<String> areas = new ArrayList<>();
    //private String[] areas = {"市水务局", "天河", "番禺", "黄埔", "白云", "南沙", "海珠", "荔湾", "花都", "越秀","增城", "从化", "净水公司", "全市"};
    private String[] lead_yAxle = {"天河", "番禺", "黄埔", "白云", "南沙", "海珠", "荔湾", "花都", "越秀", "增城", "从化", "市排水公司"};
    private String[] patrol_yAxle = {"天河", "番禺", "黄埔", "白云", "南沙", "海珠", "荔湾", "花都", "越秀", "增城", "从化", "市排水公司","总计"};
    private List<String> patrol_type = Arrays.asList("窨井", "雨水口", "排水口", "全部");
    private ArrayList<Float> datas = new ArrayList<>();
    private MyGridViewAdapter myGridViewAdapter;
    private MyGridViewAdapter patrolypeAdapter;
    private MyGridViewAdapter todayTypeAdapter;
    private TextView all_count, install_count, no_install_count;
    //    private WebView mWebView;
    private ProgressDialog progressDialog;
    private String echartsPieJson;
    private String currentOrgName = "全市";
    private ArrayList<StatisticBean.ChildOrg> childOrgs;
    private boolean currentRoleType = false;
    private boolean isInit = true;
    private UploadStatisticService uploadStatisticService;
    private String currentType = "窨井";
    private String currentTodayType = "窨井";
    private Context mContext;
    private Calendar cal;
    private Long startDate = null;
    private Long endDate = null;
    private Long TempEndDate = null;
    private static final int START_DATE = 1;
    private static final int END_DATE = 2;
    private Button btn_start_date;
    private Button btn_end_date;
    private WebView mWebView;
    private UploadStatiscAdapter mUploadStatiscAdapter;
    private RecyclerView mRecyclerView;
    private Button btnOkTime;
    private Button btnRefresh;
    private TextView newadded_check_count;
    private TextView correct_check_count;
    private TextView all_check_count;
    private TextView newadded_question_count;
    private TextView correct_question_count;
    private TextView all_question_count;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uploadstats, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mGridView = (MyGridView) view.findViewById(R.id.gv_area);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_all_upload);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mTypeGridView = (MyGridView) view.findViewById(R.id.gv_type);
        mTodayGridView = (MyGridView) view.findViewById(R.id.gv_toyester_type);
        all_count = (TextView) view.findViewById(R.id.all_install_count);
        install_count = (TextView) view.findViewById(R.id.install_count);
        no_install_count = (TextView) view.findViewById(R.id.no_install_count);
        /**
         * 审核统计
         */
        newadded_check_count = (TextView) view.findViewById(R.id.newadded_check_count);
        correct_check_count = (TextView) view.findViewById(R.id.correct_check_count);
        all_check_count = (TextView) view.findViewById(R.id.all_check_count);

        newadded_question_count = (TextView) view.findViewById(R.id.newadded_question_count);
        correct_question_count = (TextView) view.findViewById(R.id.correct_question_count);
        all_question_count = (TextView) view.findViewById(R.id.all_question_count);

        mDistrcContain = (LinearLayout) view.findViewById(R.id.upload_statisc_distrc);
        /***
         * 日期选择
         */
        btn_start_date = (Button) view.findViewById(R.id.btn_start_date);
        cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);       //获取年月日时分秒
//        int month = cal.get(Calendar.MONTH) + 1;   //获取到的月份是从0开始计数
//        int day = cal.get(Calendar.DAY_OF_MONTH);
 //       startDate = new Date(year - 1900, month-3, day).getTime();
//        if (month == 1 || month == 2){
//            btn_start_date.setText((year - 1) + "-" + (month + 10) + "-" + day);
//        }else {
//            btn_start_date.setText(year + "-" + (month-2) + "-" + day);
//        }

        btn_end_date = (Button) view.findViewById(R.id.btn_end_date);
//        btn_end_date.setText(year + "-" + month + "-" + day);
//        endDate = new Date(year - 1900, month - 1, day+1).getTime();
//        TempEndDate = new Date(year - 1900, month - 1, day).getTime();
        setStateDate();
        setCurrentDate();
        btn_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDate == null) {
                    showDatePickerDialog(btn_start_date, cal, START_DATE);
                } else {
                    cal.setTimeInMillis(startDate);
                    showDatePickerDialog(btn_start_date, cal, START_DATE);
                }
            }
        });

        btn_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TempEndDate == null) {
                    showDatePickerDialog(btn_end_date, cal, END_DATE);
                } else {
                    cal.setTimeInMillis(TempEndDate);
                    showDatePickerDialog(btn_end_date, cal, END_DATE);
                }
            }
        });

        btnOkTime = (Button) view.findViewById(R.id.stats_time_ok);
        btnRefresh = (Button) view.findViewById(R.id.stats_refresh);
        RxView.clicks(btnRefresh)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        loadYTData(false);
                    }
                });
        RxView.clicks(btnOkTime)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (startDate > TempEndDate) {
                            ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "开始时间不能比结束时间大");
                            return;
                        }
                        loadDatas(false, currentOrgName, currentType, startDate, endDate);
                    }
                });

        mWebView = (WebView) view.findViewById(R.id.chart_webview);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JavaScriptinterface(getActivity()), "android");
        mWebView.loadUrl("file:///android_asset/uploadecharts/myechart.html");
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
//                getOrgAppInstallInfo(currentOrgName, currentRoleType,true);
                //mWebView.loadUrl("javascript:createChart('line'," + echartsPieJson + ");");
                loadDatas(false, currentOrgName, currentType, startDate, endDate);

            }
        });
        if (uploadStatisticService == null) {
            uploadStatisticService = new UploadStatisticService(mContext);
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("加载中...");
        progressDialog.setCancelable(true);
        view.findViewById(R.id.install_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), AppInstallDetailActivity.class);
//                intent.putExtra("org_name", currentOrgName);
//                intent.putExtra("roleType", currentRoleType);
//                intent.putExtra("inInstall", "true");
//                startActivity(intent);
            }
        });
        view.findViewById(R.id.no_install_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), AppInstallDetailActivity.class);
//                intent.putExtra("org_name", currentOrgName);
//                intent.putExtra("roleType", currentRoleType);
//                intent.putExtra("inInstall", "false");
//                startActivity(intent);T
            }
        });
    }

    public void setStateDate(){
        cal = Calendar.getInstance();
//        cal.add(Calendar.MONDAY,   -2);   //获取前两个月
        int year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        int month = cal.get(Calendar.MONTH) + 1;   //获取到的月份是从0开始计数
        int day = cal.get(Calendar.DAY_OF_MONTH);
        startDate = new Date(2018 - 1900, 0, 1).getTime();
        btn_start_date.setText(2018+"-" + 1 + "-" + 1);
    }

    public void setCurrentDate(){
        cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        int month = cal.get(Calendar.MONTH) + 1;   //获取到的月份是从0开始计数
        int day = cal.get(Calendar.DAY_OF_MONTH);
        btn_end_date.setText(year + "-" + month + "-" + day);
        endDate = new Date(year - 1900, month - 1, day+1).getTime();
        TempEndDate = new Date(year - 1900, month - 1, day).getTime();
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
//        if (!ListUtil.isEmpty(areas)) {
//            String distrc = areas.get(areas.size() - 1);
//            if (distrc.equals("全市")) {
//                loadDatas(false, currentOrgName, startDate, endDate);
//            } else {
//                currentOrgName = distrc;
//                loadDatas(false, distrc, startDate, endDate);
//            }
//
//        }
        loadYTData(false);

//        getOrgAppInstallInfo(currentOrgName, currentRoleType, isInit);
    }

    /**
     * 获取昨天和今天的统计数据
     */
    private void loadYTData(boolean ifShowPb) {
        if (ifShowPb) {
            progressDialog.show();
        }

        uploadStatisticService.getUploadNearTimeData(currentTodayType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadYTStatisticBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "获取数据失败");
                    }

                    @Override
                    public void onNext(UploadYTStatisticBean uploadStatisticBean) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if(uploadStatisticBean == null || !uploadStatisticBean.getSuccess()){
                            ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "获取数据失败");
                            return;
                        }
                        creatListData(uploadStatisticBean);
                    }
                });
    }

    private void creatListData(UploadYTStatisticBean uploadStatisticBean) {
        List<UploadYTStatisticBean.ToDayEntity> toDay = uploadStatisticBean.getToDay();
        List<UploadYTStatisticBean.YestDayEntity> yestDay = uploadStatisticBean.getYestDay();

        List<UploadYTStatisticBean.ToDayEntity> newToday = new ArrayList<>();
        List<UploadYTStatisticBean.YestDayEntity> newyestDay = new ArrayList<>();
        int sumYCorrect = 0;
        int sumYLack = 0;

        int sumTCorrect = 0;
        int sumTLack = 0;
        for (int i = 0; i < patrol_yAxle.length; i++) {
            UploadYTStatisticBean.ToDayEntity tempToday = new UploadYTStatisticBean.ToDayEntity();
            UploadYTStatisticBean.YestDayEntity tempYestDay = new UploadYTStatisticBean.YestDayEntity();
            String orgName = patrol_yAxle[i];
            tempToday.setName(orgName);
            tempYestDay.setName(orgName);
            if(ListUtil.isEmpty(toDay)){
                tempToday.setCorrCount(0);
                tempToday.setLackCount(0);
            }else{
                for (UploadYTStatisticBean.ToDayEntity childOrg : toDay) {
                    if (childOrg.getName().contains(orgName) || (orgName.equals("市排水公司") && childOrg.getName().contains("净水"))) {
                        tempToday.setCorrCount(childOrg.getCorrCount());
                        tempToday.setLackCount(childOrg.getLackCount());
                        sumTCorrect +=childOrg.getCorrCount();
                        sumTLack += childOrg.getLackCount();
                        break;
                    } else {
                        tempToday.setCorrCount(0);
                        tempToday.setLackCount(0);
                    }
                }
            }

            if(ListUtil.isEmpty(yestDay)){
                tempYestDay.setCorrCount(0);
                tempYestDay.setLackCount(0);
            }else{
                for (UploadYTStatisticBean.YestDayEntity childOrg : yestDay) {
                    if (childOrg.getName().contains(orgName) || (orgName.equals("市排水公司") && childOrg.getName().contains("净水"))) {
                        tempYestDay.setCorrCount(childOrg.getCorrCount());
                        tempYestDay.setLackCount(childOrg.getLackCount());
                        sumYCorrect +=childOrg.getCorrCount();
                        sumYLack += childOrg.getLackCount();
                        break;
                    } else {
                        tempYestDay.setCorrCount(0);
                        tempYestDay.setLackCount(0);
                    }
                }
            }
            if(orgName.equals("总计")){
                tempToday.setLackCount(sumTLack);
                tempToday.setCorrCount(sumTCorrect);
                tempYestDay.setLackCount(sumYLack);
                tempYestDay.setCorrCount(sumYCorrect);
            }
            newToday.add(tempToday);
            newyestDay.add(tempYestDay);
        }
        mUploadStatiscAdapter.refresh(newyestDay,newToday);
    }

    public void loadDatas(boolean ifShowPb, String distrct, String reportType, long startTime, long endTime) {
        if (ifShowPb) {
            progressDialog.show();
        }

        uploadStatisticService.getUploadStatisticForDistric(distrct, reportType, startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadStatisticBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "获取数据失败");
                    }

                    @Override
                    public void onNext(UploadStatisticBean uploadStatisticBean) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (uploadStatisticBean == null) {
                            return;
                        }
                        all_count.setText(uploadStatisticBean.getTotalStr());
                        no_install_count.setText(uploadStatisticBean.getCorrCountStr());
                        install_count.setText(uploadStatisticBean.getLackCountStr());
                        all_check_count.setText(uploadStatisticBean.getPassTotalStr());
                        correct_check_count.setText(uploadStatisticBean.getPassCorrCountStr());
                        newadded_check_count.setText(uploadStatisticBean.getPassLackCountStr());
                        all_question_count.setText(uploadStatisticBean.getDoubtTotalStr());
                        correct_question_count.setText(uploadStatisticBean.getDoubtCorrCountStr());
                        newadded_question_count.setText(uploadStatisticBean.getDoubtLackCountStr());
                        creatBarChart(uploadStatisticBean);

                    }
                });
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
                if (childOrg.getName().contains(orgName) || (orgName.equals("市排水公司") && childOrg.getName().contains("净水"))) {
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
        mUploadStatiscAdapter = new UploadStatiscAdapter(mContext);
        mRecyclerView.setAdapter(mUploadStatiscAdapter);
        mDistrcContain.setVisibility(View.VISIBLE);

        mGridView.setVisibility(View.VISIBLE);
        areas.add("天河");
        areas.add("番禺");
        areas.add("黄埔");
        areas.add("白云");
        areas.add("南沙");
        areas.add("海珠");
        areas.add("荔湾");
        areas.add("花都");
        areas.add("越秀");
        areas.add("增城");
        areas.add("从化");
        areas.add("市排水公司");
        areas.add("全市");

        //区域
        myGridViewAdapter = new MyGridViewAdapter();
        myGridViewAdapter.setPosition(areas.size() - 1);
        myGridViewAdapter.setArrayList(areas);
        myGridViewAdapter.setOnItemListener(new OnItemListener() {
            @Override
            public void onClick(int position) {
                currentOrgName = areas.get(position);
                if (startDate > TempEndDate) {
                    ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "开始时间不能比结束时间大");
                    return;
                }
                loadDatas(false, currentOrgName, currentType, startDate, endDate);
            }
        });
        mGridView.setAdapter(myGridViewAdapter);

        //设施类型
        patrolypeAdapter = new MyGridViewAdapter();
        patrolypeAdapter.setArrayList(patrol_type);
        patrolypeAdapter.setPosition(0);
        patrolypeAdapter.setOnItemListener(new OnItemListener() {
            @Override
            public void onClick(int position) {
                currentType = patrol_type.get(position);
                if (startDate > TempEndDate) {
                    ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "开始时间不能比结束时间大");
                    return;
                }
                loadDatas(false, currentOrgName, currentType, startDate, endDate);
            }
        });
        mTypeGridView.setAdapter(patrolypeAdapter);


        //今日昨日设施类型
        todayTypeAdapter = new MyGridViewAdapter();
        todayTypeAdapter.setArrayList(patrol_type);
        todayTypeAdapter.setPosition(0);
        todayTypeAdapter.setOnItemListener(new OnItemListener() {
            @Override
            public void onClick(int position) {
                currentTodayType = patrol_type.get(position);
                if(currentTodayType.equals("全部")){
                    currentTodayType = "";
                }
                loadYTData(false);
            }
        });
        mTodayGridView.setAdapter(todayTypeAdapter);
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
            View view = View.inflate(getActivity(), R.layout.gridview_item_layout, null);
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

    public void setGridViewHeight(GridView gridview) {
        // 获取gridview的adapter
        ListAdapter listAdapter = gridview.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int numColumns = gridview.getNumColumns(); //5
        int totalHeight = 0;
        // 计算每一列的高度之和
        for (int i = 0; i < listAdapter.getCount(); i += numColumns) {
            // 获取gridview的每一个item
            View listItem = listAdapter.getView(i, null, gridview);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取gridview的布局参数
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置参数
        gridview.setLayoutParams(params);
    }

    public void showDatePickerDialog(final Button btn, Calendar calendar, final int type) {
        // Calendar 需要这样来得到
        // Calendar calendar = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(mContext,
                // 绑定监听器(How the parent is notified that the date is set.)
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        display(btn, year, monthOfYear, dayOfMonth);
                        if (type == START_DATE) {
                            startDate = new Date(year - 1900, monthOfYear, dayOfMonth).getTime();
                        } else {
                            endDate = new Date(year - 1900, monthOfYear, dayOfMonth+1).getTime();
                            TempEndDate = new Date(year - 1900, monthOfYear, dayOfMonth).getTime();
                        }
                    }
                }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    /**
     * 设置日期
     */
    public void display(Button dateDisplay, int year,
                        int monthOfYear, int dayOfMonth) {
        dateDisplay.setText(new StringBuffer().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth));
    }

    public interface OnItemListener {
        void onClick(int position);
    }

}
