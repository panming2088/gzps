package com.augurit.agmobile.patrolcore.selectlocation.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.am.cmpt.widget.treeview.model.TreeNode;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.agmobile.patrolcore.R;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agcollection.selectlocation.view
 * @createTime 创建时间 ：2017-05-19
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-05-19
 * @modifyMemo 修改备注：
 */

public class PatrolChildViewHolder extends TreeNode.BaseNodeViewHolder<LayerInfo> {
    //子节点名称
    protected TextView tv_nodeName;

    protected ViewGroup ll_iv_opacity;
    protected ImageView iv_opacity;
    protected SeekBar mSeekbar;
    protected TextView opacity_value;
    protected Button btn_close;
    protected RelativeLayout rl_container;
    protected String mGroupName;  //组名称，通过这个组名称将parent和child联系起来
    protected ViewGroup ll_child_selected;
    protected ViewGroup ll_child_unselected;
    protected ImageView iv_selected;
    protected ImageView iv_unselected;
    protected View mView;
    protected ILayerView.OnLayerItemClickListener mOnLayerItemClickListener;

    public PatrolChildViewHolder(Context context, String parentName, ILayerView.OnLayerItemClickListener onLayerItemClickListener){
        super(context);
        this.mOnLayerItemClickListener = onLayerItemClickListener;
        this.mGroupName = parentName;
    }

    @Override
    public View createNodeView(final TreeNode node, final LayerInfo value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        mView = getView(inflater);

        ll_child_selected = (ViewGroup) mView.findViewById(R.id.ll_node_selected);
        ll_child_unselected = (ViewGroup) mView.findViewById(R.id.ll_node_unselected);
        ll_child_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                node.setSelected(false);
                ll_child_selected.setVisibility(View.GONE);
                ll_child_unselected.setVisibility(View.VISIBLE);
                if (!ValidateUtil.isObjectNull(mOnLayerItemClickListener)){
                    mOnLayerItemClickListener.onClickCheckbox(value.getLayerId(), mGroupName,value,false);
                }
                for (TreeNode n : node.getChildren()) {
                    getTreeView().selectNode(n, false);
                }

            }
        });
        ll_child_unselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                node.setSelected(true);
                ll_child_unselected.setVisibility(View.GONE);
                ll_child_selected.setVisibility(View.VISIBLE);
                if (!ValidateUtil.isObjectNull(mOnLayerItemClickListener)){
                    mOnLayerItemClickListener.onClickCheckbox(value.getLayerId(), mGroupName,value,true);
                }
                for (TreeNode n : node.getChildren()) {
                    getTreeView().selectNode(n, true);
                }

            }
        });


        //图标
        iv_selected = (ImageView) mView.findViewById(R.id.iv_layer_selected);
        iv_unselected = (ImageView) mView.findViewById(R.id.iv_layer_unselected);

        tv_nodeName = (TextView) mView.findViewById(R.id.node_value);
        tv_nodeName.setText(value.getLayerName());
        if (value.getType() == LayerType.Unsupported){
            tv_nodeName.setTextColor(Color.GRAY);
        }

        ll_iv_opacity = (ViewGroup) mView.findViewById(R.id.ll_iv_opacity);
        iv_opacity = (ImageView) mView.findViewById(R.id.iv_opacity);
        if (value.getType().equals(LayerType.FeatureLayer)){
            iv_opacity.setVisibility(View.GONE);
        }
        iv_opacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekbar.setVisibility(View.VISIBLE);
                rl_container.setVisibility(View.VISIBLE);
                iv_opacity.setVisibility(View.GONE);
            }
        });
        ll_iv_opacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_opacity.performClick();
            }
        });


        opacity_value = (TextView) mView.findViewById(R.id.tv_treeview_childview_opacity_num);
        opacity_value.setText((value.getOpacity()*100)+"%");

        //当该节点的图层是FeatureLayer时,不显示调节透明度的按钮
        if(value.getType() == null || value.getType().equals(LayerType.Unsupported)){
            iv_opacity.setVisibility(View.GONE);
            ll_child_selected.setVisibility(View.GONE);
            ll_child_unselected.setVisibility(View.GONE);
        }


        mSeekbar = (SeekBar) mView.findViewById(R.id.seekbar_treeview_childview_opacity);
        mSeekbar.setProgress((int) (value.getOpacity()*100));
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                opacity_value.setText(progress+"%");
                double opacity = progress / 10 * 0.1;
                mOnLayerItemClickListener.onOpacityChange(value.getLayerId(),mGroupName,value, (float) opacity);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btn_close = (Button) mView.findViewById(R.id.btn_treeview_childview_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekbar.setVisibility(View.GONE);
                rl_container.setVisibility(View.GONE);
                iv_opacity.setVisibility(View.VISIBLE);
            }
        });

        rl_container = (RelativeLayout) mView.findViewById(R.id.rl_treeview_childview_container);

        boolean defaultVisible = value.isDefaultVisible();
        if (defaultVisible){
            ll_child_unselected.performClick();

        }else{
            ll_child_selected.performClick();
        }

        return mView;
    }

    protected View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.patrol_layer_treenview_childholder, null, false);
    }

    public void toggleSelectionMode(boolean editModeEnabled) {
        if (editModeEnabled){
            if (mNode.isSelected()){
                ll_child_unselected.performClick(); //执行全选
            }else {
                ll_child_selected.performClick(); //全不选
            }
        }
    }


}

