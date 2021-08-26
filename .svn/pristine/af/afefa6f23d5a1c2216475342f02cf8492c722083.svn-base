package com.augurit.agmobile.gzps.statistic.view.conditionview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;

import java.util.LinkedHashMap;

/**
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.statistic.view.conditionview
 * @createTime 创建时间 ：2017/8/17
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/8/17
 * @modifyMemo 修改备注：
 */

public class ConditionListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private LinkedHashMap<String, Object> mDatas;
    private SparseBooleanArray mSelectionArray;

    public ConditionListAdapter(Context context, LinkedHashMap<String, Object> datas) {
        mContext = context;
        mDatas = datas;
        initSelection();
    }

    private void initSelection() {
        if (mSelectionArray == null) {
            mSelectionArray = new SparseBooleanArray();
        }
        for (int i = 0; i < mDatas.size(); i++) {
            mSelectionArray.put(i, false);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.statistic_condition_list_item, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyHolder) {
            final MyHolder myHolder = (MyHolder) holder;
            final String[] keys = mDatas.keySet().toArray(new String[0]);
            final Object[] values = mDatas.values().toArray();
            myHolder.tv_name.setText(keys[position]);
            boolean checked = mSelectionArray.get(position);
            myHolder.cb.setChecked(checked);
            myHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mSelectionArray.put(position, isChecked);
                }
            });
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean b = mSelectionArray.get(position);
                    myHolder.cb.setChecked(!b);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setDatas(LinkedHashMap<String, Object> datas) {
        mDatas = datas;
        initSelection();
        notifyDataSetChanged();
    }

    public LinkedHashMap<String, Object> getSelected() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        final String[] keys = mDatas.keySet().toArray(new String[0]);
        for (int i = 0; i < mSelectionArray.size(); i++) {
            if (mSelectionArray.get(i)) {
                map.put(keys[i], mDatas.get(keys[i]));
            }
        }
        return map;
    }

    private class MyHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        CheckBox cb;

        MyHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            cb = (CheckBox) itemView.findViewById(R.id.cb);
        }
    }
}
