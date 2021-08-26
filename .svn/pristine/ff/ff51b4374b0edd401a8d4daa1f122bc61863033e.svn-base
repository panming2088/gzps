package com.augurit.agmobile.mapengine.project.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;


import com.augurit.agmobile.mapengine.R;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;

import java.util.List;

/**
 * 专题列表视图的适配器
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.proj.adapter
 * @createTime 创建时间 ：2016-11-02 21:04
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2016-11-02 21:04
 */

public class ProjectViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    protected Context mContext;
    protected List<ProjectInfo> mDataList;
    private int mSelectedPosition;
    protected OnItemClickListener mOnItemClickListener;

    public ProjectViewAdapter(Context context, List<ProjectInfo> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getListViewHolder();
    }

    @NonNull
    protected ListViewHolder getListViewHolder() {
        return new ListViewHolder(View.inflate(mContext, getLayout(), null));
    }

    protected int getLayout() {
        return R.layout.proj_listitem;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ListViewHolder) {
            final ListViewHolder viewHolder = (ListViewHolder) holder;
            viewHolder.tv_project_name.setText(mDataList.get(position).getProjectName());
            if (mSelectedPosition == position) {
                viewHolder.rb_check.setChecked(true);
            } else {
                viewHolder.rb_check.setChecked(false);
            }

            viewHolder.rb_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //更改选中位置
                    mSelectedPosition = position;
                    notifyDataSetChanged();
                    performClick(viewHolder, position);
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performClick(viewHolder, position);
                }
            });
            // 点击监听
            /*if (mOnItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(viewHolder.itemView, position);
                    }
                });
            }*/
        }
    }

    private void performClick(ListViewHolder viewHolder, int position) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(viewHolder.itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_project_name;
        public RadioButton rb_check;

        ListViewHolder(View itemView) {
            super(itemView);
            tv_project_name = (TextView) itemView.findViewById(R.id.tv_project_name);
            rb_check = (RadioButton) itemView.findViewById(R.id.rb_check);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
