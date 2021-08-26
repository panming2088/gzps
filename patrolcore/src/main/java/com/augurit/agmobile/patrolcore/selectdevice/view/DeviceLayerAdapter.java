package com.augurit.agmobile.patrolcore.selectdevice.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.patrolcore.layer.model.LayerList;
import com.augurit.agmobile.patrolcore.R;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.selectdevice
 * @createTime 创建时间 ：2017-03-29
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-29
 * @modifyMemo 修改备注：
 */

public class DeviceLayerAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder,
        LayerList.PatrolLayer> {

    public DeviceLayerAdapter(List<LayerList.PatrolLayer> mDataList) {
        super(mDataList);
    }

    @Override
    public BaseRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new DeviceLayersViewHolder(inflater.inflate(R.layout.map_layerdir_childholder,null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position, LayerList.PatrolLayer data) {

        if (holder instanceof DeviceLayersViewHolder){
            ((DeviceLayersViewHolder) holder).node_value.setText(data.getName());
        }
    }

    private static class  DeviceLayersViewHolder extends BaseRecyclerViewHolder{

        TextView node_value;

        public DeviceLayersViewHolder(View itemView) {
            super(itemView);
            node_value = findView(R.id.node_value);
        }
    }


}
