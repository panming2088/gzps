package com.augurit.agmobile.patrolcore.common.legend;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.augurit.agmobile.mapengine.legend.model.Legend;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.am.cmpt.widget.treeview.model.TreeNode;

/**
 * Created by xcl on 2017/10/30.
 */

public class LegendChildViewHolder extends TreeNode.BaseNodeViewHolder<Legend.LegendStyle> {



    public LegendChildViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, final Legend.LegendStyle value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.legend_child_holder, null);

        Button btn_legend_outer = (Button) view.findViewById(R.id.btn_legend_outer);

        GradientDrawable drawable =(GradientDrawable)btn_legend_outer.getBackground();
        drawable.setColor(Color.parseColor(value.getColor().replace("0x","#"))); // 设置填充色
        drawable.setStroke(1, Color.parseColor(value.getLineColor().replace("0x","#"))); // 设置边框宽度和颜色



        btn_legend_outer.setBackground(drawable);


        //图例名称
        TextView tv_legend_name = (TextView) view.findViewById(R.id.tv_legend_name);
        tv_legend_name.setText(value.getLegendName());

        return view;
    }
}