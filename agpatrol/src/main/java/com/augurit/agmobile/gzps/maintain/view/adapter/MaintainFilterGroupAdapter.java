package com.augurit.agmobile.gzps.maintain.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.maintain.model.MaintainBatchInfo;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xcl on 2017/10/23.
 */

public class MaintainFilterGroupAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, MaintainBatchInfo.RowsBean> {

    private Context mContext;

    private List<MaintainBatchInfo.RowsBean> mGroupList;

    private Set<MaintainBatchInfo.RowsBean> mSelectedGroupList;

    private OnRecyclerItemClickListener<MaintainBatchInfo.RowsBean> mOnItemClickListener;

    private OnRecyclerItemClickListener<MaintainBatchInfo.RowsBean> mOnItemShowListener;

    public MaintainFilterGroupAdapter(Context context, List<MaintainBatchInfo.RowsBean> mDataList) {
        super(mDataList);
        this.mContext = context;
        mSelectedGroupList = new HashSet<>();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new WorkNewsViewHolder(inflater.inflate(R.layout.gridview_maintain_item_layout, parent,false));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position, final MaintainBatchInfo.RowsBean item) {
        if (holder instanceof MaintainFilterGroupAdapter.WorkNewsViewHolder) {

            MaintainFilterGroupAdapter.WorkNewsViewHolder viewHolder = (MaintainFilterGroupAdapter.WorkNewsViewHolder) holder;

            viewHolder.tvName.setText(item.getName());
            viewHolder.tvContent.setText(item.getContent());
            viewHolder.tvBeginTime.setText(TimeUtil.getStringTimeYMD(new Date(item.getStartDate())));
            viewHolder.tvEndTime.setText(TimeUtil.getStringTimeYMD(new Date(item.getEndDate())));
            viewHolder.tvPipeType.setText(item.getPipeType());
            boolean isContains  = contains(mSelectedGroupList,item);
//        if(mSelectedGroupList.contains(item)){
            if(isContains){
                viewHolder.ll_container.setBackground(mContext.getResources().getDrawable(R.drawable.corner_color_primary3));
                viewHolder.tvName.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimary, null));
                viewHolder.tvContent.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimary, null));
                viewHolder.tvBeginTime.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimary, null));
                viewHolder.tvEndTime.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimary, null));
                viewHolder.tvPipeType.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimary, null));
                viewHolder.iv.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ll_container.setBackground(mContext.getResources().getDrawable(R.drawable.corner_color_write));
                viewHolder.tvName.setTextColor(Color.BLACK);
                viewHolder.tvContent.setTextColor(Color.BLACK);
                viewHolder.tvBeginTime.setTextColor(Color.BLACK);
                viewHolder.tvEndTime.setTextColor(Color.BLACK);
                viewHolder.tvPipeType.setTextColor(Color.BLACK);
                viewHolder.iv.setVisibility(View.GONE);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mSelectedGroupList.contains(item)){
                        mSelectedGroupList.remove(item);
                    } else {
                        mSelectedGroupList.add(item);
                    }
                    notifyDataSetChanged();
                    if(mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(v, position, item);
                    }
                }
            });
            viewHolder.tvMarkTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemShowListener != null){
                        mOnItemShowListener.onItemClick(v, position, item);
                    }
                }
            });
        }
    }

    private boolean contains(Set<MaintainBatchInfo.RowsBean> selectedGroupList, MaintainBatchInfo.RowsBean item) {
        for(MaintainBatchInfo.RowsBean info:selectedGroupList){
            if(info.equals(item)){
                return true;
            }
        }
        return false;
    }

    public Set<MaintainBatchInfo.RowsBean> getSelectedEventTypeList(){
        return mSelectedGroupList;
    }


    public void setSelectedGroupList(String[] eventTypeCodes){
        mSelectedGroupList.clear();
        if(ListUtil.isEmpty(mGroupList)){
            return;
        }
        for(MaintainBatchInfo.RowsBean item : mGroupList){
            for(String code : eventTypeCodes){
                if(item.getId().equals(code)){
                    mSelectedGroupList.add(item);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemShowListener(OnRecyclerItemClickListener onItemShowListener) {
        mOnItemShowListener = onItemShowListener;
    }

    public Set<MaintainBatchInfo.RowsBean> getSelectedGroupList() {
        return mSelectedGroupList;
    }

    public void setSelectedGroupList(Set<MaintainBatchInfo.RowsBean> selectedGroupList) {
        this.mSelectedGroupList.clear();
        this.mSelectedGroupList.addAll(selectedGroupList);
//        this.mSelectedGroupList = selectedGroupList;
        notifyDataSetChanged();
    }
    public void setSelectedGroupList(List<MaintainBatchInfo.RowsBean> selectedGroupList) {
        Set result = new HashSet(selectedGroupList);
        this.mSelectedGroupList.clear();
        this.mSelectedGroupList.addAll(result);
        mDataList.clear();
        mDataList.addAll(selectedGroupList);
        notifyDataSetChanged();
    }

    public static class WorkNewsViewHolder extends BaseRecyclerViewHolder {
        View ll_container;
        TextView tvName;
        TextView tvContent;
        TextView tvBeginTime;
        TextView tvEndTime;
        TextView tvPipeType;
        TextView tvMarkTime;
        ImageView iv;
        public WorkNewsViewHolder(View itemView) {
            super(itemView);
            ll_container = itemView.findViewById(R.id.ll_container);
            tvName = (TextView) itemView.findViewById(R.id.ll_name);
            tvContent = (TextView) itemView.findViewById(R.id.ll_content);
            tvBeginTime = (TextView) itemView.findViewById(R.id.ll_start_time);
            tvEndTime = (TextView) itemView.findViewById(R.id.ll_end_time);
            tvPipeType = (TextView) itemView.findViewById(R.id.ll_type);
            tvMarkTime = (TextView) itemView.findViewById(R.id.ll_mark_time);
            iv = (ImageView) itemView.findViewById(R.id.iv_selected);
        }
    }
}