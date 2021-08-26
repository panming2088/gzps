package com.augurit.agmobile.patrolcore.common.legend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.legend.model.LayerLegend;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.am.cmpt.widget.treeview.model.TreeNode;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by xcl on 2017/10/30.
 */

public class LayerLegendChildViewHolder  extends TreeNode.BaseNodeViewHolder<LayerLegendChildViewHolder.NodeValue> {



    public LayerLegendChildViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, final NodeValue value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layer_legend_child_holder, null);
        int level = node.getLevel()-1;
        view.setPadding(level * 50, view.getPaddingTop(), view.getRight(), view.getPaddingBottom());
        ImageView btn_legend_outer = (ImageView) view.findViewById(R.id.btn_legend_outer);
        btn_legend_outer.setMaxWidth(value.getLegendEntity().getWidth());
        btn_legend_outer.setMaxHeight(value.getLegendEntity().getHeight());
        String url = value.getBaseUrl()+"/"+value.getLayerId()+"/"+"/images/"+value.getLegendEntity().getUrl();
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().crossFade().into(btn_legend_outer);

        //图例名称
        String legend_name = value.getLegendEntity().getLabel();
        legend_name = legend_name.replace(",","").replace("<Null>","");
        TextView tv_legend_name = (TextView) view.findViewById(R.id.tv_legend_name);
        tv_legend_name.setText(legend_name);

        return view;
    }

    public static class NodeValue{
        private LayerLegend.LegendEntity legendEntity;
        private String baseUrl;
        private int layerId;

        public NodeValue(LayerLegend.LegendEntity legendEntity, String baseUrl, int layerId) {
            this.legendEntity = legendEntity;
            this.baseUrl = baseUrl;
            this.layerId = layerId;
        }

        public LayerLegend.LegendEntity getLegendEntity() {
            return legendEntity;
        }

        public String getBaseUrl() {
            return baseUrl;
        }


        public int getLayerId() {
            return layerId;
        }

    }
}