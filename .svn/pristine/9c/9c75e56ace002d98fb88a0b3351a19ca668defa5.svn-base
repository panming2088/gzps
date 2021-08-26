package com.augurit.agmobile.patrolcore.layerdownload.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.layerdownload.view.OnLayerClickListener;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.am.cmpt.widget.treeview.model.TreeNode;

/**
 * Created by liangsh on 2016-09-19.
 */
public class LayerItemHolder  extends TreeNode.BaseNodeViewHolder<LayerInfo> {
    private OnLayerClickListener onLayerClickListener;

    public LayerItemHolder(Context context, OnLayerClickListener onLayerClickListener){
        super(context);
        this.onLayerClickListener = onLayerClickListener;
    }

    @Override
    public View createNodeView(TreeNode node, final LayerInfo value) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dnl_item_layer, null);
        TextView tv = (TextView)view.findViewById(R.id.dnl_tv_layername);
        View btn = view.findViewById(R.id.dnl_btn_dnllayer);
        tv.setText(value.getLayerName());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLayerClickListener.onClick(v,value);
            }
        });
        return view;
    }
}
