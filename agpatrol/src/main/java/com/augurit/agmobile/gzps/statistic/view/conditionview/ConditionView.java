package com.augurit.agmobile.gzps.statistic.view.conditionview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.mapengine.common.DividerItemDecoration;

import java.util.LinkedHashMap;

/**
 * 单个统计条件控件
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.statistic.view
 * @createTime 创建时间 ：2017/8/17
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/8/17
 * @modifyMemo 修改备注：
 */

public class ConditionView extends LinearLayout {

    private TextView tv_name;
    private RecyclerView rv_condition;
    private ConditionListAdapter mAdapter;
    private ConditionItem mItem;

    public ConditionView(Context context) {
        super(context);
        initView();
    }

    public ConditionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ConditionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.statistic_widget_condition, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        rv_condition = (RecyclerView) view.findViewById(R.id.rv_condition);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_condition.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        rv_condition.addItemDecoration(decoration);
        view.findViewById(R.id.btn_condition).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 条件名称点击

            }
        });

        this.addView(view);
    }

    public void setCondition(ConditionItem condition) {
        mItem = condition;
        mAdapter = new ConditionListAdapter(getContext(), mItem.getConditionValues());
        rv_condition.setAdapter(mAdapter);
        tv_name.setText(condition.getName());
    }

    public LinkedHashMap<String, Object> getSelectedConditions() {
        return mAdapter.getSelected();
    }
}
