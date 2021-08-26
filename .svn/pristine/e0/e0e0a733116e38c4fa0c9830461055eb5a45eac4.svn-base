package com.augurit.am.cmpt.maintenance.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.maintenance.cache.model.AutoClearOption;
import com.augurit.am.cmpt.maintenance.cache.util.StorageManageUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：存储空间管理View
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.stats.view
 * @createTime 创建时间 ：2017-02-24
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-24
 * @modifyMemo 修改备注：
 */
public class StorageManagerView {

    private Context mContext;
    private MaintenancePresenter mPresenter;

    private View mView;
    private PieChart mChart;
    private Button btn_clear;
    private View iv_back;
    private TextView tv_option;  // 清除选项文字
    private Legend mLegend;

    private long mTotalBytes;       // 总空间
    private long mAvailableBytes;   // 可用空间
    private long mAppBytes;         // 应用使用空间

    private int[] mColors = {ColorTemplate.MATERIAL_COLORS[0],
            ColorTemplate.LIBERTY_COLORS[1], ColorTemplate.LIBERTY_COLORS[0] }; // 图表颜色
    private TextView tv_hint;
    private long mCacheSize;

    public StorageManagerView(Context context, MaintenancePresenter presenter) {
        mContext = context;
        mPresenter = presenter;
        initView();
        initChart();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mView = View.inflate(mContext, R.layout.maintenance_storage_view, null);
        mChart = (PieChart) mView.findViewById(R.id.chart);
        btn_clear = (Button) mView.findViewById(R.id.btn_clear);
        iv_back = mView.findViewById(R.id.btn_back);
        tv_hint = (TextView) mView.findViewById(R.id.tv_cacheclear_hint);
        tv_option = (TextView) mView.findViewById(R.id.tv_option);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清除本地文件
                Context appContext = mContext.getApplicationContext();
                StorageManageUtil.deleteCache(appContext); // 缓存
                StorageManageUtil.deleteExternalCache(appContext); // 外部缓存
                StorageManageUtil.deleteExtraDir(appContext);  // 应用额外文件夹
                ToastUtil.shortToast(appContext, "清理完成");
                // 更新数据
                initData();
            }
        });
        tv_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionDialog();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showMenu();
            }
        });
        mChart.setNoDataText("正在加载数据");
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
        mLegend.setTextSize(16);
    }

    /**
     * 获取存储空间数据
     */
    private void initData() {
        StorageManageUtil.getAppTotalBytes(mContext.getApplicationContext(), new Callback2<long[]>() {
            @Override
            public void onSuccess(long[] results) {
                // 获取应用占用空间大小及可清理大小
                mAppBytes = results[0];
                mCacheSize = results[1];
                // 获取设备空间大小
                mTotalBytes = StorageManageUtil.getTotalBytes();
                mAvailableBytes = StorageManageUtil.getAvailableBytes();
                // 更新视图
                handler.obtainMessage(1).sendToTarget();
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
                mChart.setNoDataText("加载数据失败");
            }
        });
    }

    /**
     * 将存储空间数据设置到图表
     */
    private void setChartData() {
        // 构造数据
        PieEntry e_app = new PieEntry(mAppBytes, "本应用");    // 应用使用空间大小
        PieEntry e_used = new PieEntry(mTotalBytes - mAvailableBytes - mAppBytes, "其他"); // 其他使用空间大小
        PieEntry e_left = new PieEntry(mAvailableBytes, "剩余"); // 剩余空间大小
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(e_app);
        pieEntries.add(e_used);
        pieEntries.add(e_left);
        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(mColors);
        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        // 设置图例
        String[] labels = new String[3];
        labels[0] = "本应用" + Formatter.formatFileSize(mContext, mAppBytes);
        labels[1] = "其他" + Formatter.formatFileSize(mContext, mTotalBytes - mAvailableBytes - mAppBytes);
        labels[2] = "剩余" + Formatter.formatFileSize(mContext, mAvailableBytes);
        mLegend.setCustom(mColors, labels);
        // 设置数据到图表
        mChart.setData(data);
        mChart.highlightValue(0, 0);
    }

    /**
     * 显示自动清理对话框
     */
    private void showOptionDialog() {
        // 初始化视图
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View content = View.inflate(mContext, R.layout.maintenance_storage_dialog, null);
        final EditText edt_size = (EditText) content.findViewById(R.id.edt_size);
        final EditText edt_time = (EditText) content.findViewById(R.id.edt_time);
        final Switch sw_size = (Switch) content.findViewById(R.id.sw_size);
        final Switch sw_time = (Switch) content.findViewById(R.id.sw_time);
        Button btn_positive = (Button) content.findViewById(R.id.btn_positive);
        Button btn_negative = (Button) content.findViewById(R.id.btn_negative);
        // 初始化值
        final AutoClearOption optionOrigin = StorageManageUtil.readAutoClearOption(mContext);   // 读取清理选项
        edt_size.setText(String.valueOf(optionOrigin.getSize()));
        edt_time.setText(String.valueOf(optionOrigin.getTime()));
        sw_size.setChecked(optionOrigin.isSizeOn());
        sw_time.setChecked(optionOrigin.isTimeOn());
        // 初始化对话框
        final Dialog dialog = new Dialog(mContext);
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoClearOption option = new AutoClearOption();
                if (edt_size.getText().toString().equals("")
                        || Integer.parseInt(edt_size.getText().toString()) == 0) {
                    edt_size.setText("1");
                }
                if (edt_time.getText().toString().equals("")
                        || Integer.parseInt(edt_time.getText().toString()) == 0) {
                    edt_time.setText("1");
                }
                option.setSize(Integer.parseInt(edt_size.getText().toString()));
                option.setTime(Integer.parseInt(edt_time.getText().toString()));
                option.setSizeOn(sw_size.isChecked());
                option.setTimeOn(sw_time.isChecked());
                StorageManageUtil.saveAutoClearOption(mContext, option);
                StorageManageUtil.autoClear(mContext);   // 执行一次自动清理
                initData();     // 更新视图
                dialog.dismiss();
            }
        });
        btn_negative.setOnClickListener(new View.OnClickListener() {    // 取消按钮
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(content);
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.common_lyrl_dialog);  // 设置背景
    }

    /**
     * 用以在数据加载完毕时更新视图
     */
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    setChartData();
                    if (mTotalBytes* 0.1 - mAppBytes <0){
                        tv_hint.setVisibility(View.GONE); //大于10%
                    }else {
                        tv_hint.setVisibility(View.VISIBLE);
                    }
                    btn_clear.setText(mContext.getString(R.string.clear_cache,
                            Formatter.formatFileSize(mContext, mCacheSize)));
                    return true;
                default:
                    break;
            }
            return false;
        }
    });

    /**
     * 获取视图
     * @return 视图
     */
    public View getView() {
        initData();
        return mView;
    }
}
