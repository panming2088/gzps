package com.augurit.am.cmpt.maintenance.view;

import android.app.ActivityManager;
import android.content.Context;
import android.text.format.Formatter;
import android.view.View;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.maintenance.memory.util.MemoryUtil;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：内存统计视图
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.stats.view
 * @createTime 创建时间 ：2017-02-24
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-24
 * @modifyMemo 修改备注：
 */
public class MemoryStatisticsView {

    private Context mContext;
    private MaintenancePresenter mPresenter;
    private long totalMem;   //总内存
    private long availMem;   //空闲内存
    private long useMem;   //本应用使用内存
    private ActivityManager.MemoryInfo memoryInfo;
    private MemoryUtil.ProgressMemoryInfo progressMemoryInfo;

    private View mView;
    private View backBtn;
    private PieChart mChart;
    private Legend mLegend;
    private int[] mColors = {ColorTemplate.MATERIAL_COLORS[0],
            ColorTemplate.LIBERTY_COLORS[1], ColorTemplate.LIBERTY_COLORS[0] }; // 图表颜色

    public MemoryStatisticsView(Context context, MaintenancePresenter presenter) {
        mContext = context;
        mPresenter = presenter;
        init(context);
    }

    private void init(Context context) {
        mView = View.inflate(context, R.layout.maintenance_memstats_view, null);
        initView(mView);
        initChart();
        setChartData();
    }

    /**
     * 初始化视图
     */
    private void initView(View view) {
        backBtn = view.findViewById(R.id.btn_back);
        mChart = (PieChart) view.findViewById(R.id.chart);
        mChart.setNoDataText("正在加载数据");
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

    /**
     * 将存储空间数据设置到图表
     */
    private void setChartData() {
        //获取整体内存信息，如总内存，空闲内存等
        memoryInfo = MemoryUtil.getMemoryInfo(mContext);
        //获取应用的内存占用信息
        progressMemoryInfo = MemoryUtil.getRunningAppProcessInfo(mContext);
        totalMem = memoryInfo.totalMem;
        availMem = memoryInfo.availMem;
        //应用占用的内存，单位为Byte
        useMem = progressMemoryInfo.getMemSize();
        // 构造数据
        PieEntry e_used = new PieEntry(useMem, "本应用"); //
        PieEntry e_app = new PieEntry(totalMem-useMem+availMem, "其他");    //
        PieEntry e_left = new PieEntry(availMem, "空闲"); //
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(e_used);
        pieEntries.add(e_app);
        pieEntries.add(e_left);
        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(mColors);
        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        // 设置图例
        String[] labels = new String[3];
        labels[0] = "本应用" + Formatter.formatFileSize(mContext, useMem);
        labels[1] = "其他" + Formatter.formatFileSize(mContext, totalMem-useMem+availMem);
        labels[2] = "空闲" + Formatter.formatFileSize(mContext, availMem);

        mLegend.setCustom(mColors, labels);
        // 设置数据到图表
        mChart.setData(data);
        mChart.highlightValue(0, 0);
    }

    /**
     * 获取视图
     * @return 视图
     */
    public View getView() {
        setChartData();
        return mView;
    }
}
