package com.augurit.agmobile.patrolcore.search.view.filterview;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.patrolcore.R;

import java.util.LinkedHashMap;

/**
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.search.view.filterview
 * @createTime 创建时间 ：2017/7/12
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/7/12
 * @modifyMemo 修改备注：
 */

class FilterSpinnerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private LinkedHashMap<String, Object> mItems;
    private OnItemClickListener mOnItemClickListener;
    private boolean mMultiSelect;
    private SparseBooleanArray mSelectionArray;

    FilterSpinnerAdapter(Context context, LinkedHashMap<String, Object> spinnerItems) {
        this(context, spinnerItems, false);
    }

    FilterSpinnerAdapter(Context context, LinkedHashMap<String, Object> spinnerItems, boolean isMultiSelect) {
        mContext = context;
        mItems = spinnerItems;
        mMultiSelect = isMultiSelect;
        initSelection();
    }

    private void initSelection() {
        if (mSelectionArray == null) {
            mSelectionArray = new SparseBooleanArray();
        }
        for (int i = 0; i < mItems.size(); i++) {
            mSelectionArray.put(i, false);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.filter_list_item, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder myHolder = (ItemHolder) holder;
            final String[] keys = mItems.keySet().toArray(new String[0]);
            final Object[] values = mItems.values().toArray();
            myHolder.tv_name.setText(keys[position]);
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean b = mSelectionArray.get(position);
                    if (!mMultiSelect) {
                        initSelection();
                    }
                    mSelectionArray.put(position, !b);
                    notifyDataSetChanged();
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position, !b, keys[position], values[position]);
                    }
                }
            });
            if (mSelectionArray.get(position)) {
                myHolder.tv_name.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.agpatrol_blue, null));
            } else {
                myHolder.tv_name.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.black, null));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder{

        TextView tv_name;

        ItemHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position, boolean isChecked, String key, Object value);
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 清空当前选择状态
     */
    public void clearSelection() {
        initSelection();
        notifyDataSetChanged();
    }
}
