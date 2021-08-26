package com.augurit.agmobile.gzps.statistic.view;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.statistic.model.StatisticResult;
import com.augurit.agmobile.gzps.statistic.view.conditionview.ConditionItem;
import com.augurit.agmobile.gzps.statistic.view.conditionview.ConditionView;
import com.augurit.am.cmpt.widget.spinner.AMSpinner;
import com.augurit.am.fw.common.BaseView;
import com.augurit.am.fw.utils.view.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.statistic.view
 * @createTime 创建时间 ：2017/8/15
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/8/15
 * @modifyMemo 修改备注：
 */
public class StatisticView extends BaseView<StatisticPresenter> {

    private static final int TYPE_STATISTIC = 1;
    private static final int TYPE_GROUP = 2;

    private ViewGroup mContainer;
    private AMSpinner sp_group;
    private ViewGroup statistic_condition_container;
    private ViewGroup group_condition_container;
    private TextView tv_start_time;
    private TextView tv_end_time;
    private Date mMinDate;
    private Date mMaxDate;

    private Calendar mCalendar;
    private List<ConditionItem> mStatConditions;
    private List<ConditionItem> mGroupConditions;
    private HashMap<ConditionItem, ConditionView> mStatViewMap;
    private ConditionView mGroupView;

    private LinkedHashMap<String, Object> mGroupMap;
    private int mAddedStatConditionsCount = 0;
    View btn_add;

    public StatisticView(Context context, ViewGroup container) {
        super(context);
        mContainer = container;
        mCalendar = Calendar.getInstance();
    }

    public void showConditionView(List<ConditionItem> statConditions, List<ConditionItem> groupConditions) {
        mStatConditions = statConditions;
        mGroupConditions = groupConditions;
        mStatViewMap = new HashMap<>();
        View view = View.inflate(mContext, R.layout.statistic_view_condition, null);
        statistic_condition_container = (ViewGroup) view.findViewById(R.id.statistc_conditions_container);
        group_condition_container = (ViewGroup) view.findViewById(R.id.group_conditions_container);
        sp_group = (AMSpinner) view.findViewById(R.id.sp_group);
        tv_start_time = (TextView) view.findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) view.findViewById(R.id.tv_end_time);
        btn_add = view.findViewById(R.id.btn_add);
        View btn_statistic = view.findViewById(R.id.btn_statistic);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 弹出条件选择
                showConditionsPopup();
            }
        });
        btn_statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 统计
                doStatistic();
            }
        });

        // 初始化分组Spinner
        for (ConditionItem condition : mGroupConditions) {
            sp_group.addItems(condition.getName(), condition);
        }
        sp_group.setOnItemClickListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                // 改变条件
                addCondition((ConditionItem) item, TYPE_GROUP);
            }
        });
        if (!mGroupConditions.isEmpty()) {
            sp_group.selectItem(0);
        }

        View.OnClickListener timeClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        };
        tv_start_time.setOnClickListener(timeClick);
        tv_end_time.setOnClickListener(timeClick);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        mMaxDate = new Date();
        tv_end_time.setText(format.format(mMaxDate));     // 截止日期为今天
        mCalendar.setTime(mMaxDate);
        mCalendar.add(Calendar.MONTH, -1);
        mMinDate = mCalendar.getTime();
        tv_start_time.setText(format.format(mMinDate));    // 默认统计一个月
        mCalendar.setTime(new Date());

        // TODO 只有一个条件就直接显示
        mContainer.removeAllViews();
        mContainer.addView(view);
    }

    private void showConditionsPopup() {
        if (mStatConditions.size() == 0 || mStatConditions.size() == mAddedStatConditionsCount) {
            return;
        }
        final Dialog dialog = new Dialog(mContext);
        List<String> names = new ArrayList<>();
        for (ConditionItem condition : mStatConditions) {
            names.add(condition.getName());
        }

        View view = View.inflate(mContext, R.layout.statistic_popup_condition, null);
        ListView lv_names = (ListView) view.findViewById(R.id.lv_names);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,
                R.layout.statistic_condition_name_list_item, R.id.tv_name, names);
        lv_names.setAdapter(arrayAdapter);
        lv_names.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 添加条件
                addCondition(mStatConditions.get(position), TYPE_STATISTIC);
                mAddedStatConditionsCount++;
                if (mStatConditions.size() == mAddedStatConditionsCount) btn_add.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.login_lyrl_dialog);
    }

    private void addCondition(ConditionItem conditionItem, int type) {
        ConditionView conditionView = new ConditionView(mContext);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        conditionView.setLayoutParams(layoutParams);
        conditionView.setCondition(conditionItem);
        if (type == TYPE_STATISTIC) {
            statistic_condition_container.addView(conditionView);
            mStatViewMap.put(conditionItem, conditionView);
        } else {
            group_condition_container.removeAllViews();
            group_condition_container.addView(conditionView);
            mGroupView = conditionView;
        }
    }

    private void showDatePicker(View v) {
        final TextView textView = (TextView) v;
        final boolean isStart = v.getId() == R.id.tv_start_time;
        DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(year, month, dayOfMonth);
                Date time = mCalendar.getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                textView.setText(format.format(time));
                if (isStart) {
                    mMinDate = time;
                } else {
                    mMaxDate = time;
                }
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
        DatePicker datePicker = dialog.getDatePicker();
        if (isStart) {
            if (mMaxDate != null) {
                datePicker.setMaxDate(mMaxDate.getTime());
            }
        } else {
            if (mMinDate != null) {
                datePicker.setMinDate(mMinDate.getTime());
            }
        }
    }

    private void doStatistic() {
        if (mStatViewMap.isEmpty()) {
            ToastUtil.shortToast(mContext, "请添加统计条件");
            return;
        }
        String conditionJson = "";
        try {
            // 构造统计条件
            JSONArray statisticJa = new JSONArray();
            for (Map.Entry<ConditionItem, ConditionView> entry : mStatViewMap.entrySet()) {
                LinkedHashMap<String, Object> conditions = entry.getValue().getSelectedConditions();
                if (conditions.isEmpty()) {
                    ToastUtil.shortToast(mContext, "请选择统计条件");
                    return;
                }
                // 拼接条件值
                String conditionValue = "";
                for (Object o : conditions.values()) {
                    conditionValue += o + "|";
                }
                conditionValue = conditionValue.substring(0, conditionValue.length() - 1);
                JSONObject jo = new JSONObject();
                jo.put("name", entry.getKey().getField1());
                jo.put("value", conditionValue);
                statisticJa.put(jo);
            }
            // 构造分组条件
            JSONArray groupJa = new JSONArray();
            ConditionItem groupItem = (ConditionItem) sp_group.getCurrentSelected();
            LinkedHashMap<String, Object> groupConditions = mGroupView.getSelectedConditions();
            if (groupConditions.isEmpty()) {
                ToastUtil.shortToast(mContext, "请选择分组条件");
                return;
            }
            mGroupMap = groupConditions;
            // 拼接条件值
            String groupValue = "";
            for (Object o : groupConditions.values()) {
                groupValue += o + "|";
            }
            groupValue = groupValue.substring(0, groupValue.length() - 1);
            JSONObject jo = new JSONObject();
            jo.put("name", groupItem.getField1());
            jo.put("value", groupValue);
            groupJa.put(jo);
            // 时间区间
            String start_time = tv_start_time.getText() + " 00:00:00";
            String end_time = tv_end_time.getText() + " 23:59:59";
            // 最终Json
            JSONObject conditionJo = new JSONObject();
            conditionJo.put("statistic", statisticJa);
            conditionJo.put("group", groupJa);
            conditionJo.put("start_time", start_time);
            conditionJo.put("end_time", end_time);
            conditionJson = conditionJo.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!conditionJson.isEmpty()) {
            mPresenter.statistic(conditionJson);
        }
    }

    public void showStatisticResult(List<StatisticResult> results) {
        View view = View.inflate(mContext, R.layout.statistic_view_result, null);
        View btn_back = view.findViewById(R.id.btn_back);

        BarChart barChart = (BarChart) view.findViewById(R.id.stats_barchart);

        ArrayList<String> groupValues = new ArrayList<>();  // 分组名称
        List<Map<String, Object>> series = new ArrayList<>();
        LinkedHashMap<String, List<Double>> dataMap = new LinkedHashMap<>();
        for (StatisticResult result : results) {
            for (StatisticResult.Result result1 : result.getResult()) {
                if (!dataMap.containsKey(result1.getTypeName())) {      // 存入该类数量
                    ArrayList<Double> dataList = new ArrayList<>();
                    dataList.add(result1.getCount());
                    dataMap.put(result1.getTypeName(), dataList);
                } else {
                    dataMap.get(result1.getTypeName()).add(result1.getCount());
                }
            }
            String groupName = result.getGroupValue();
            if (mGroupMap != null) {
                for (Map.Entry<String, Object> entry : mGroupMap.entrySet()) {
                    if (entry.getValue().equals(groupName)) {
                        groupName = entry.getKey();
                        break;
                    }
                }
            }
            groupValues.add(groupName);    // 记录分组名称
        }
        for (Map.Entry<String, List<Double>> entry : dataMap.entrySet()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", entry.getKey());
            map.put("type", "bar");
            map.put("data", entry.getValue().toArray(new Double[0]));
            map.put("stack", "总量");
            series.add(map);
        }
        barChart.setData(groupValues.toArray(new String[0]), series);
        barChart.initChart(mContext);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        Drawable bg = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.login_lyrl_dialog, null);
        popupWindow.setBackgroundDrawable(bg);
        popupWindow.showAtLocation(mContainer, Gravity.CENTER, 0, 0);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
}
