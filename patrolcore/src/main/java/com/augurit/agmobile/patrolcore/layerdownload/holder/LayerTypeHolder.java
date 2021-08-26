package com.augurit.agmobile.patrolcore.layerdownload.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.am.cmpt.widget.treeview.model.TreeNode;

/**
 * Created by liangsh on 2016-09-19.
 */
public class LayerTypeHolder  extends TreeNode.BaseNodeViewHolder<String> {

    public LayerTypeHolder(Context context){
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, String value) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dnl_item_layer, null);
        TextView tv = (TextView)view.findViewById(R.id.dnl_tv_layername);
        View btn = view.findViewById(R.id.dnl_btn_dnllayer);
        tv.setText(value);
        btn.setVisibility(View.GONE);
        return view;
    }
}
