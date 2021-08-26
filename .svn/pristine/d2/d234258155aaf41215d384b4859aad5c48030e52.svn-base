package com.augurit.agmobile.patrolcore.survey.view;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.survey.constant.ServerTableRecordConstant;
import com.augurit.agmobile.patrolcore.survey.model.ServerTableRecord;

import java.util.List;

/**
 * 通用记录项适配器
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey
 * @createTime 创建时间 ：17/9/2
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/9/2
 * @modifyMemo 修改备注：
 */

public abstract class BaseSurveyListAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, ServerTableRecord> {


    protected OnRecyclerItemClickListener<ServerTableRecord> mOnRecyclerItemClickListener;

    public OnRecyclerItemClickListener<ServerTableRecord> getOnRecyclerItemClickListener() {
        return mOnRecyclerItemClickListener;
    }

    public void notifyDataSetChanged(List<ServerTableRecord> serverTableRecords) {
        mDataList.clear();
        mDataList.addAll(serverTableRecords);
        notifyDataSetChanged();
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener<ServerTableRecord> onRecyclerItemClickListener) {
        this.mOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public BaseSurveyListAdapter(Context context, List<ServerTableRecord> mDataList) {
        super(mDataList);

    }

    public void addData(List<ServerTableRecord> searchResults) {
        mDataList.addAll(searchResults);
    }

    public void addData(ServerTableRecord searchResults) {
        mDataList.add(0,searchResults); //在最前面插入
    }

    public void clearData() {
        mDataList.clear();
    }


    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position, final ServerTableRecord data) {

        if (holder instanceof BuildingListViewHolder) {

            ((BuildingListViewHolder) holder).maintitle.setText(data.getName());

//            if (data.getState() != null &&data.getState().equals(ServerTableRecordConstant.CHECKED)) {
//                ((BuildingListViewHolder) holder).iv_finished.setVisibility(View.VISIBLE); //已核查
//                ((BuildingListViewHolder) holder).tv_edit.setVisibility(View.GONE);
//                ((BuildingListViewHolder) holder).tv_unupload.setVisibility(View.GONE);
//
//            }else if ((data.getState() != null &&data.getState().equals(ServerTableRecordConstant.UNUPLOAD)) ){
//
//                ((BuildingListViewHolder) holder).iv_finished.setVisibility(View.GONE); //未提交
//                ((BuildingListViewHolder) holder).tv_edit.setVisibility(View.GONE);
//                ((BuildingListViewHolder) holder).tv_unupload.setVisibility(View.VISIBLE);
//                ((BuildingListViewHolder) holder).maintitle.getPaint().setFlags(0); // 取消设置的的中划线
//            }else if ((data.getState() != null &&data.getState().equals(ServerTableRecordConstant.DELETED)) ){
//
//                ((BuildingListViewHolder) holder).iv_finished.setVisibility(View.GONE); //未提交
//                ((BuildingListViewHolder) holder).tv_edit.setVisibility(View.GONE);
//                ((BuildingListViewHolder) holder).tv_unupload.setVisibility(View.GONE);
//
//                ((BuildingListViewHolder) holder).maintitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            }else {
//
//                ((BuildingListViewHolder) holder).iv_finished.setVisibility(View.GONE); //未核查
//                ((BuildingListViewHolder) holder).tv_edit.setVisibility(View.VISIBLE);
//                ((BuildingListViewHolder) holder).tv_unupload.setVisibility(View.GONE);
//                ((BuildingListViewHolder) holder).maintitle.getPaint().setFlags(0); // 取消设置的的中划线
//            }
            ((BuildingListViewHolder) holder).ll_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecyclerItemClickListener != null) {
                        mOnRecyclerItemClickListener.onItemClick(v, position, data);
                    }
                }
            });

            ((BuildingListViewHolder) holder).tv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecyclerItemClickListener != null) {
                        mOnRecyclerItemClickListener.onItemClick(v, position, data);
                    }
                }
            });

        }

    }


    public static class BuildingListViewHolder extends BaseRecyclerViewHolder {

        public TextView maintitle;
        public TextView tv_edit;
        public View ll_root;
        public View iv_finished;
        public View tv_unupload;

        public BuildingListViewHolder(View itemView) {
            super(itemView);
            maintitle = findView(R.id.maintitle);
            tv_edit = findView(R.id.tv_edit);
            ll_root = findView(R.id.ll_root);
            iv_finished = findView(R.id.iv_finished);
            tv_unupload = findView(R.id.tv_unupload);
        }
    }
}
