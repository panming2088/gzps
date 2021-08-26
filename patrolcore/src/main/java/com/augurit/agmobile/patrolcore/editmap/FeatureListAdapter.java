package com.augurit.agmobile.patrolcore.editmap;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AttributeListActivity;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.ListAttributeCalloutManager;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.ComponentAttributeListActivity;
import com.augurit.agmobile.patrolcore.layer.model.LayerList;
import com.augurit.agmobile.patrolcore.selectdevice.view.DeviceLayerAdapter;

import java.util.List;

/**
 * 部件列表
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/10/15
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/15
 * @modifyMemo 修改备注：
 */

public class FeatureListAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, AMFindResult> {


    private int selectedPosition = -1;

    public FeatureListAdapter(List<AMFindResult> mDataList) {
        super(mDataList);
    }

    public void notifyDataSetChanged(List<AMFindResult> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new DeviceLayersViewHolder(inflater.inflate(R.layout.map_layerdir_childholder, null));
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position, final AMFindResult data) {

        if (holder instanceof DeviceLayersViewHolder) {
            
            ((DeviceLayersViewHolder) holder).node_value.setText(data.getValue());

            /**
             * 查看详情
             */
            ((DeviceLayersViewHolder) holder).tv_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(((DeviceLayersViewHolder) holder).itemView.getContext(), ComponentAttributeListActivity.class);
                    intent.putExtra(ListAttributeCalloutManager.INTENT_FIELDNAME_KEY, data.getValue());
                    ComponentAttributeListActivity.attributes = data.getAttributes();
                    ((DeviceLayersViewHolder) holder).itemView.getContext().startActivity(intent);
                }
            });

            /**
             * 设置高亮
             */
            if (selectedPosition == position) {
                ((DeviceLayersViewHolder) holder).node_value.setTextColor(((DeviceLayersViewHolder) holder).itemView.getContext().getResources().getColor(R.color.colorAccent));
            } else {
                ((DeviceLayersViewHolder) holder).node_value.setTextColor(((DeviceLayersViewHolder) holder).itemView.getContext().getResources().getColor(R.color.dust_grey));
            }

            /**
             * 点击事件
             */
            ((DeviceLayersViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(position);
                    if (mListener != null) {
                        mListener.onItemClick(v, position, holder.getItemId());
                    }

                }
            });


        }
    }

    public void setSelected(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    private static class DeviceLayersViewHolder extends BaseRecyclerViewHolder {

        TextView node_value;
        TextView tv_detail;

        public DeviceLayersViewHolder(View itemView) {
            super(itemView);
            node_value = findView(R.id.node_value);
            tv_detail = findView(R.id.tv_detail);
        }
    }


}
