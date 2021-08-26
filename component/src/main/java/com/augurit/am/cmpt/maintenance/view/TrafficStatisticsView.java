package com.augurit.am.cmpt.maintenance.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.maintenance.traffic.dao.MySQLiteOpenHelper;
import com.augurit.am.cmpt.maintenance.traffic.model.TrafficInfo;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：流量统计View
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.stats.mView.fragment
 * @createTime 创建时间 ：2017-02-24
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-24
 * @modifyMemo 修改备注：
 */
public class TrafficStatisticsView {

    private Context mContext;
    private MaintenancePresenter mPresenter;

    private View mView;
    MySQLiteOpenHelper dbHelper;  //流量数据库操作类
    //    ArrayList<TrafficInfo> appList;
    TrafficInfo thisAppTrafficInfo;  //流量信息

    private View backBtn;
    private PieChart mChart;
    private Legend mLegend;
    private int[] mColors = {ColorTemplate.MATERIAL_COLORS[0],
            ColorTemplate.LIBERTY_COLORS[1]}; // 图表颜色

    private TextView sumTrafficTv;
    private Handler mHandler;
    private LoadThread mLoadThread;

    public TrafficStatisticsView(Context context, MaintenancePresenter presenter) {
        mContext = context;
        mPresenter = presenter;
        mHandler = new TrafficHandler(this);
        // 开始动态加载线程
        mLoadThread = new LoadThread();
        mLoadThread.start();
        initView();
        initChart();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mView = View.inflate(mContext, R.layout.maintenance_traffic_view, null);
        backBtn = mView.findViewById(R.id.btn_back);
        mChart = (PieChart) mView.findViewById(R.id.chart);
        mChart.setNoDataText("正在加载数据");
        sumTrafficTv = (TextView) mView.findViewById(R.id.traffic_tv_sum);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showMenu();
            }
        });
    }

    /**
     * 初始化图表
     */
    private void initChart() {
        mChart.setDescription("");
        mChart.setExtraOffsets(20, 0, 20 ,40);
        mChart.setTouchEnabled(false);
//        mChart.setDrawEntryLabels(false);
        mChart.setEntryLabelColor(ColorTemplate.rgb("#424242"));

        // 图例设置
        mLegend = mChart.getLegend();
        mLegend.setWordWrapEnabled(true);
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        mLegend.setOrientation(Legend.LegendOrientation.VERTICAL);
        mLegend.setYOffset(-5);
        mLegend.setXOffset(10);
        mLegend.setXEntrySpace(50);
        mLegend.setFormSize(18);
        mLegend.setTextSize(18);
    }

    public void setChartData() {
        // 构造数据
        PieEntry e_app = new PieEntry(thisAppTrafficInfo.getGPRS(), "GPRS");    //
        PieEntry e_left = new PieEntry(thisAppTrafficInfo.getWIFI(), "WIFI"); //
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(e_app);
        pieEntries.add(e_left);
        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(mColors);
        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        // 设置图例
        String[] labels = new String[2];
        labels[0] = "GPRS：" + Formatter.formatFileSize(mContext, thisAppTrafficInfo.getGPRS());
        labels[1] = "WIFI：" + Formatter.formatFileSize(mContext, thisAppTrafficInfo.getWIFI());

        mLegend.setCustom(mColors, labels);
        // 设置数据到图表
        mChart.setData(data);
        mChart.highlightValue(0, 0);

        sumTrafficTv.setText("总流量：" + Formatter.formatFileSize(mContext,
                thisAppTrafficInfo.getGPRS()+thisAppTrafficInfo.getWIFI()));
    }

    /**
     * 获取视图
     * @return 视图
     */
    public View getView() {
        if (mLoadThread == null) {
            mLoadThread = new LoadThread();
            mLoadThread.start();
        }
        return mView;
    }

    private class LoadThread extends Thread {
        @Override
        public void run() {
            dbHelper = new MySQLiteOpenHelper(mContext);
//            appList = dbHelper.queryByTime(mContext,"today");
            //统计当前应用的总流量
            thisAppTrafficInfo = dbHelper.sumCurrentAppTraffic(mContext);
            //当有一个为0的情况下，饼状图会有bug，所以在这里设为1
            if(thisAppTrafficInfo.getWIFI()<=0){
                thisAppTrafficInfo.setWIFI(1l);
            }
            if(thisAppTrafficInfo.getGPRS()<=0){
                thisAppTrafficInfo.setGPRS(1l);
            }
            mHandler.sendEmptyMessage(0); // 下载完成后发送处理消息
        }
    }

    private class TrafficHandler extends Handler{

        private final WeakReference mRefrence;

        TrafficHandler(TrafficStatisticsView view){
            mRefrence = new WeakReference(view);
        }
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:
                    TrafficStatisticsView view = (TrafficStatisticsView) mRefrence.get();
                    if(view!=null){
                        view.setChartData();
                    }
//                    LogUtil.i("Traffic applist.size="+appList.size());
//                    for(TrafficInfo trafficInfo : appList){
//                        LogUtil.i("Traffic packagename="+trafficInfo.getPackageName()
//                        + " appname=" + trafficInfo.getAppName()
//                        + " GPRS=" + trafficInfo.getGPRS()
//                        + " WIFI=" + trafficInfo.getWIFI()
//                        + " traffic=" + trafficInfo.getTraffic());
//                    }
                    mLoadThread = null;
                    break;
            }
        }
    }
}
