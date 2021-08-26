package com.augurit.agmobile.patrolcore.survey.view;

import android.content.Context;
import android.view.View;

import com.augurit.agmobile.patrolcore.survey.model.ServerTableRecord;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.view.remaning
 * @createTime 创建时间 ：17/9/5
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/9/5
 * @modifyMemo 修改备注：
 */

public class ReadOnlyRecordAdapter extends RecordsAdapter {


    public ReadOnlyRecordAdapter(Context context, List<ServerTableRecord> mDataList) {
        super(context, mDataList);
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position, final ServerTableRecord data) {

        if (holder instanceof RecordViewHolder) {

            ((RecordViewHolder) holder).maintitle.setText(data.getName());
            ((RecordViewHolder) holder).iv_finished.setVisibility(View.GONE);
            ((RecordViewHolder) holder).tv_edit.setVisibility(View.GONE);
            ((RecordViewHolder) holder).ll_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecyclerItemClickListener != null) {
                        mOnRecyclerItemClickListener.onItemClick(v, position, data);
                    }
                }
            });

            ((RecordViewHolder) holder).tv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRecyclerItemClickListener != null) {
                        mOnRecyclerItemClickListener.onItemClick(v, position, data);
                    }
                }
            });

        }

    }
}
