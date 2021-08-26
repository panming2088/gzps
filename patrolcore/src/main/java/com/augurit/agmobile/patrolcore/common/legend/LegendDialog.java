package com.augurit.agmobile.patrolcore.common.legend;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;


import com.augurit.agmobile.mapengine.legend.model.GroupLegend;
import com.augurit.agmobile.mapengine.legend.model.LayerLegend;
import com.augurit.agmobile.mapengine.legend.model.Legend;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.cmpt.widget.treeview.model.TreeNode;
import com.augurit.am.cmpt.widget.treeview.view.AndroidTreeView;
import com.augurit.am.fw.utils.ListUtil;

import java.util.List;

/**
 * Created by xcl on 2017/10/30.
 */

public class LegendDialog extends DialogFragment {

    public static final String KEY = "data";

    private ViewGroup ll_legend_tree_container;
    private ProgressLinearLayout progressLinearlayout_legend;


    public static LegendDialog newInstance() {

        LegendDialog legendDialog = new LegendDialog();
        return legendDialog;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

      /*  Window window = getDialog().getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);*/

        View view = inflater.inflate(R.layout.legend_dialog, container);

        ll_legend_tree_container = (ViewGroup) view.findViewById(R.id.ll_legend_tree_container);

        progressLinearlayout_legend = (ProgressLinearLayout) view.findViewById(R.id.progressLinearlayout_legend);

        view.findViewById(R.id.iv_mark_closeLegend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        progressLinearlayout_legend.showLoading();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void showLoading() {

        if (progressLinearlayout_legend != null) {
            progressLinearlayout_legend.showLoading();
        }

    }

    public void showError(String message, View.OnClickListener retryListener) {

        if (progressLinearlayout_legend != null) {
            progressLinearlayout_legend.showError("~~~出错了~~~", message, "重试", retryListener);
        }

    }

    public void showEmpty(View.OnClickListener retryListener) {

        if (progressLinearlayout_legend != null) {
            progressLinearlayout_legend.setVisibility(View.VISIBLE);
            progressLinearlayout_legend.showError("当前地图显示图层没有图例", "", "再试一次", retryListener);
        }

    }

    public void hideLoading() {

        if (progressLinearlayout_legend != null) {
            progressLinearlayout_legend.showContent();
        }

    }

    /**
     * 显示图例列表
     * @param legends
     */
    public void showLegends(List<Legend> legends) {
        //移除掉之前的
        ll_legend_tree_container.removeAllViews();

        TreeNode root = TreeNode.root();
        for (Legend legend : legends) {
            List<Legend.LegendStyle> legendStyles = legend.getLegends();
            if (!ListUtil.isEmpty(legendStyles)) { //如果有图例才进行添加到treeview中
                String layerName = legend.getLayerName();
                LegendParentViewHolder parentViewHolder = getParentHolder();
                TreeNode parent = new TreeNode(layerName).setViewHolder(parentViewHolder);
                for (Legend.LegendStyle legendStyle : legendStyles) {
                    TreeNode child = new TreeNode(legendStyle).setViewHolder(getChildHolder());
                    //child.getViewHolder().getView();
                    parent.addChild(child);
                }
                root.addChild(parent);
            }
        }

        final AndroidTreeView treeView = new AndroidTreeView(getActivity(), root);
        treeView.setDefaultAnimation(false);
        treeView.setSelectionModeEnabled(true);
        //treeView.expandAll();
        treeView.setDefaultContainerStyle(R.style.AGMobile_Widget_TreeView_TreeNode);
        ll_legend_tree_container.addView(treeView.getView(), LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        treeView.expandAll();
        treeView.setUseAutoToggle(false);
    }

    /**
     * 通过地图服务获取
     * 显示图例列表
     * @param legends
     */
    public void showLayerLegends(List<GroupLegend> legends) {
        //移除掉之前的
        ll_legend_tree_container.removeAllViews();
        TreeNode root = TreeNode.root();
        for(GroupLegend groupLegend:legends) {
            String groupName = groupLegend.getParentName();
            LegendParentViewHolder gPViewHolder = getParentHolder();
            TreeNode group = new TreeNode(groupName).setViewHolder(gPViewHolder);
            List<LayerLegend> layerLegends = groupLegend.getLayerLegends();
            for (LayerLegend legend : layerLegends) {
                List<LayerLegend.LegendEntity> legend1 = legend.getLegend();
                if(layerLegends.size()>1) {
                    if (!ListUtil.isEmpty(legend1)) { //如果有图例才进行添加到treeview中
                        String layerName = legend.getLayerName();
                        LegendParentViewHolder parentViewHolder = getParentHolder();
                        TreeNode parent = new TreeNode(layerName).setViewHolder(parentViewHolder);
                        for (LayerLegend.LegendEntity legendEntity : legend.getLegend()) {
                            LayerLegendChildViewHolder.NodeValue nodeValue = new LayerLegendChildViewHolder.NodeValue(legendEntity, legend.getLayerUrl(), legend.getLayerId());
                            TreeNode child = new TreeNode(nodeValue).setViewHolder(new LayerLegendChildViewHolder(getActivity()));
                            //child.getViewHolder().getView();
                            parent.addChild(child);
                        }
                        group.addChild(parent);
                    }
                }else{
                    for (LayerLegend.LegendEntity legendEntity : legend.getLegend()) {
                        LayerLegendChildViewHolder.NodeValue nodeValue = new LayerLegendChildViewHolder.NodeValue(legendEntity, legend.getLayerUrl(), legend.getLayerId());
                        TreeNode child = new TreeNode(nodeValue).setViewHolder(new LayerLegendChildViewHolder(getActivity()));
                        group.addChild(child);
                    }
                }
            }
            root.addChild(group);
        }

        final AndroidTreeView treeView = new AndroidTreeView(getActivity(), root);
        treeView.setDefaultAnimation(false);
        treeView.setSelectionModeEnabled(true);
        treeView.setDefaultContainerStyle(R.style.AGMobile_Widget_TreeView_TreeNode);
        ll_legend_tree_container.addView(treeView.getView(), LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        treeView.expandAll();
        treeView.setUseAutoToggle(false);
    }

    @NonNull
    protected LegendChildViewHolder getChildHolder() {
        return new LegendChildViewHolder(getActivity());
    }


    @NonNull
    protected LegendParentViewHolder getParentHolder() {
        return new LegendParentViewHolder(getActivity());
    }
}
