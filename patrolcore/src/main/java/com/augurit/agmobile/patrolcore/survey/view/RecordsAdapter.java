package com.augurit.agmobile.patrolcore.survey.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.survey.constant.ServerTableRecordConstant;
import com.augurit.agmobile.patrolcore.survey.model.ServerTableRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey
 * @createTime 创建时间 ：17/8/26
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/26
 * @modifyMemo 修改备注：
 */

public class RecordsAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, ServerTableRecord> {


    public OnRecyclerItemClickListener<ServerTableRecord> mOnRecyclerItemClickListener;

    public OnRecyclerItemClickListener<ServerTableRecord> getOnRecyclerItemClickListener() {
        return mOnRecyclerItemClickListener;
    }

    public void notifyDataSetChanged(List<ServerTableRecord> serverTableRecords){
        mDataList.clear();
        mDataList.addAll(serverTableRecords);
        notifyDataSetChanged();
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener<ServerTableRecord> onRecyclerItemClickListener) {
        this.mOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public RecordsAdapter(Context context, List<ServerTableRecord> mDataList) {
        super(mDataList);

    }

    public void addData(List<ServerTableRecord> searchResults) {
        mDataList.addAll(searchResults);
    }

    public void addData(ServerTableRecord searchResults) {
        mDataList.add(0,searchResults);
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new RecordViewHolder(inflater.inflate(R.layout.item_records, null));
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position, final ServerTableRecord data) {

        if (holder instanceof RecordViewHolder) {

            ((RecordViewHolder) holder).maintitle.setText(data.getName());
            if (data.getState().equals(ServerTableRecordConstant.CHECKED)) {

                ((RecordViewHolder) holder).iv_finished.setVisibility(View.VISIBLE); //已核查
                ((RecordViewHolder) holder).tv_edit.setVisibility(View.GONE);
                ((RecordViewHolder) holder).tv_unupload.setVisibility(View.GONE);

            }else if ((data.getState().equals(ServerTableRecordConstant.UNUPLOAD)) ){

                ((RecordViewHolder) holder).iv_finished.setVisibility(View.GONE); //未提交
                ((RecordViewHolder) holder).tv_edit.setVisibility(View.GONE);
                ((RecordViewHolder) holder).tv_unupload.setVisibility(View.VISIBLE);
            }else {

                ((RecordViewHolder) holder).iv_finished.setVisibility(View.GONE); //未核查
                ((RecordViewHolder) holder).tv_edit.setVisibility(View.VISIBLE);
                ((RecordViewHolder) holder).tv_unupload.setVisibility(View.GONE);
            }

            ((RecordViewHolder) holder).ll_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecyclerItemClickListener != null){
                        mOnRecyclerItemClickListener.onItemClick(v,position,data);
                    }
                }
            });

            ((RecordViewHolder) holder).tv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecyclerItemClickListener != null){
                        mOnRecyclerItemClickListener.onItemClick(v,position,data);
                    }
                }
            });

            // 设置最后修改时间
            setLastModifyTime(((RecordViewHolder) holder).tv_lastmodify, data);
        }

    }

    /**
     * 设置最后修改时间
     * @param textView
     * @param value
     */
    private void setLastModifyTime(TextView textView, ServerTableRecord value) {
        // 设置最后修改时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     // 目前提交表单的时间格式
        // 提交时间
        Date uploadTime = null;
        for (TableItem tableItem : value.getItems()) {
            if (tableItem.getField1().equals("fill_time") ||
                    tableItem.getField1().equals("fill_date")) {
                try {
//                    long timeValue = Long.parseLong(tableItem.getValue());  // 目前没有时间戳格式
//                    uploadTime = new Date(timeValue);
                    uploadTime = format1.parse(tableItem.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        // 本地修改时间
        Date localTime = null;
        if (!TextUtils.isEmpty(value.getLastModifyTime())){
            String modifyTime = value.getLastModifyTime();
            try {
                localTime = format.parse(modifyTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String lastModifyTime = null;
        if (uploadTime != null && localTime != null) {
            // 对比提交及本地修改时间
            if (uploadTime.getTime() > localTime.getTime()) {
                lastModifyTime = format.format(uploadTime);
            } else {
                lastModifyTime = format.format(localTime);
            }
        } else if (uploadTime != null) {
            lastModifyTime = format.format(uploadTime);
        } else if (localTime != null) {
            lastModifyTime = format.format(localTime);
        }
        if (!TextUtils.isEmpty(lastModifyTime)) {
            textView.setText("上次修改：" + lastModifyTime);
            textView.setVisibility(View.VISIBLE);
        }
    }

    public static class RecordViewHolder extends BaseRecyclerViewHolder {

        public TextView maintitle;
        public TextView tv_edit;
        public View ll_root;
        public View iv_finished;
        public View tv_unupload;
        public TextView tv_verified;
        public TextView tv_lastmodify;

        public RecordViewHolder(View itemView) {
            super(itemView);
            maintitle = findView(R.id.maintitle);
            tv_edit = findView(R.id.tv_edit);
            ll_root = findView(R.id.ll_root);
            iv_finished = findView(R.id.iv_finished);
            tv_unupload = findView(R.id.tv_unupload);
            tv_verified = findView(R.id.tv_verified);
            tv_lastmodify = findView(R.id.tv_lastmodify);
        }
    }

}
