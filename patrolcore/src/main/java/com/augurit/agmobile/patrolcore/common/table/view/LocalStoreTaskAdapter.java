package com.augurit.agmobile.patrolcore.common.table.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.survey.model.LocalServerTableRecord2;


import java.util.List;

/**
 *
 *
 * @author 创建人 ：
 * @version 1.0
 * @package 包名 ：com.augurit.agsurvey.tasks
 * @createTime 创建时间 ：17/8/22
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/22
 * @modifyMemo 修改备注：
 */
@Deprecated
public class LocalStoreTaskAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, LocalServerTableRecord2> {


    private int selectedPosition = -1;

    private OnRecyclerItemClickListener<LocalServerTableRecord2> onStoreTaskClickListener;

    public OnRecyclerItemClickListener<LocalServerTableRecord2> getOnStoreTaskClickListener() {
        return onStoreTaskClickListener;
    }

    public void setOnStoreTaskClickListener(OnRecyclerItemClickListener<LocalServerTableRecord2> onStoreTaskClickListener) {
        this.onStoreTaskClickListener = onStoreTaskClickListener;
    }

    public LocalStoreTaskAdapter(List<LocalServerTableRecord2> mDataList) {
        super(mDataList);
    }


    public void notifyDatasetChanged(List<LocalServerTableRecord2> searchHistories) {
        mDataList.clear();
        mDataList.addAll(searchHistories);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new AddressViewHolder(inflater.inflate(R.layout.item_store_unupload_task, null));
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position, final LocalServerTableRecord2 data) {

        if (holder instanceof AddressViewHolder) {
            ((AddressViewHolder) holder).tv_name.setText("核查任务");
            if(data.getName() != null) {
                if(data.getName().equals(data.getTaskName())) {
                    ((AddressViewHolder) holder).name2.setText(data.getName());
                }else {
                    ((AddressViewHolder) holder).name2.setText(data.getTaskName() +" " +data.getName());
                }
            }else {
                ((AddressViewHolder) holder).name2.setText(data.getTaskName());
            }


           /* if (selectedPosition == position){
                ((AddressViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
                ((AddressViewHolder) holder).btn_accept.setVisibility(View.GONE);
            }*/

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onStoreTaskClickListener != null) {
                        onStoreTaskClickListener.onItemClick(v,holder.getAdapterPosition(), data);
                    }
                }
            });
        }
    }

    public class AddressViewHolder extends BaseRecyclerViewHolder {

        private TextView tv_name; //任务名字
        private View btn_accept; //签收按钮
        private ProgressBar progressBar;
        private TextView name2;//审核表名


        public AddressViewHolder(View itemView) {
            super(itemView);
            tv_name = findView(R.id.name);
            btn_accept = findView(R.id.tv_to_accept);
            progressBar = findView(R.id.pb_uploading);
            name2 = findView(R.id.name2);
        }
    }


}
