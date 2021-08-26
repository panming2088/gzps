package com.augurit.agmobile.patrolcore.common.legend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.augurit.agmobile.patrolcore.R;
import com.augurit.am.cmpt.widget.treeview.model.TreeNode;

/**
 * Created by xcl on 2017/10/30.
 */

public class LegendParentViewHolder extends TreeNode.BaseNodeViewHolder<String> {


    private ImageView iv_symbol;

    public LegendParentViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, final String value) {

        final LayoutInflater inflater = LayoutInflater.from(context);
        int level = node.getLevel()-1;
        View view = inflater.inflate(R.layout.legend_parent_holder, null);
        view.setPadding(level * 50, view.getPaddingTop(), view.getRight(), view.getPaddingBottom());
        iv_symbol = (ImageView) view.findViewById(R.id.iv_symbol);

        TextView node_value = (TextView) view.findViewById(R.id.node_value);
        node_value.setText(value);


        return view;
    }

    @Override
    public void toggle(boolean active) {
        super.toggle(active);
        if (active){
            iv_symbol.setImageResource(R.mipmap.common_ic_minus);
        }else {
            iv_symbol.setImageResource(R.mipmap.common_ic_plus);
        }
    }
}
