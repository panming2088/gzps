package com.augurit.agmobile.mapengine.layermanage.view;

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

import com.augurit.agmobile.mapengine.R;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.am.cmpt.widget.treeview.model.TreeNode;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.esri.android.map.Layer;

import java.util.Map;


public class ChildViewHolder extends TreeNode.BaseNodeViewHolder<LayerInfo> {
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

    protected ILayerView.OnHolderItemClickListener onHolderItemClickListener;

    protected Map<Integer,Layer> mAllLayer ;

    public ChildViewHolder(Context context, String parentName,
             Map<Integer, Layer> allLayer,
                           ILayerView.OnLayerItemClickListener onLayerItemClickListener) {
        super(context);
        this.mOnLayerItemClickListener = onLayerItemClickListener;
        this.mGroupName = parentName;
        this.mAllLayer = allLayer;
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
                if (ifLayerIsInactive(value)) return;
                node.setSelected(false);
                ll_child_selected.setVisibility(View.GONE);
                ll_child_unselected.setVisibility(View.VISIBLE);
                if (!ValidateUtil.isObjectNull(mOnLayerItemClickListener)) {
                    mOnLayerItemClickListener.onClickCheckbox(value.getLayerId(), mGroupName, value, false);
                }
                for (TreeNode n : node.getChildren()) {
                    getTreeView().selectNode(n, false);
                }

            }
        });
        ll_child_unselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                //当该节点的图层是不支持加载时,不显示调节透明度的按钮
//                if (value.getType() == null || value.getType().equals(LayerType.Unsupported) || (LayersService.sAllLayer != null && LayersService.sAllLayer.get(value.getLayerId()) == null)) {
//                    return;
//                }
                if (ifLayerIsInactive(value)) return;


                node.setSelected(true);
                ll_child_unselected.setVisibility(View.GONE);
                ll_child_selected.setVisibility(View.VISIBLE);
                if (!ValidateUtil.isObjectNull(mOnLayerItemClickListener)) {
                    mOnLayerItemClickListener.onClickCheckbox(value.getLayerId(), mGroupName, value, true);
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
        if (value.getType() == LayerType.Unsupported) {
            tv_nodeName.setTextColor(Color.GRAY);
        }

        ll_iv_opacity = (ViewGroup) mView.findViewById(R.id.ll_iv_opacity);
        iv_opacity = (ImageView) mView.findViewById(R.id.iv_opacity);
       /* if (value.getType().equals(LayerType.FeatureLayer)){ //featureLayer也可以进行调整透明度
            iv_opacity.setVisibility(View.GONE);
        }*/
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
        opacity_value.setText((value.getOpacity() * 100) + "%");

        //当该节点的图层是不支持加载时,不显示调节透明度的按钮
        if (ifLayerIsInactive(value)) {

            iv_opacity.setVisibility(View.GONE);
            iv_opacity.setClickable(false);

            ll_child_selected.setVisibility(View.GONE);
            ll_child_unselected.setVisibility(View.GONE);

            ll_child_selected.setClickable(false);
            ll_child_unselected.setClickable(false);

            ll_iv_opacity.setClickable(false);
            ll_iv_opacity.setVisibility(View.GONE);

            tv_nodeName.setTextColor(context.getResources().getColor(R.color.agmobile_grey_0));
        }

        if (ifLayerOpacacityIsUnChangeable(value)){
            iv_opacity.setVisibility(View.GONE);
            iv_opacity.setClickable(false);
        }

        mSeekbar = (SeekBar) mView.findViewById(R.id.seekbar_treeview_childview_opacity);
        mSeekbar.setProgress((int) (value.getOpacity() * 100));
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                opacity_value.setText(progress + "%");
                double opacity = progress / 10 * 0.1;
                mOnLayerItemClickListener.onOpacityChange(value.getLayerId(), mGroupName, value, (float) opacity);
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
        if (defaultVisible) {
            ll_child_unselected.performClick();

        } else {
            ll_child_selected.performClick();
        }


        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onHolderItemClickListener != null) {
                    onHolderItemClickListener.onItemClick(value);
                }
            }
        });


        return mView;
    }

    /**
     * 图层是否不支持加载
     * @param value
     * @return
     */
    private boolean ifLayerIsInactive(LayerInfo value) {
        if (value.getType() == null || value.getType().equals(LayerType.Unsupported)) {
            return true;
        }
        /**
         * 如果是Dynamic的子圖層，那麼加在地图上的是他的父图层
         */
        if (value.getType() == LayerType.DynamicLayer_SubLayer){
            if ((mAllLayer != null && mAllLayer.get(Integer.valueOf(value.getParentLayerId())) == null)){
                return true;
            }
        }else{
            if ((mAllLayer != null && mAllLayer.get(value.getLayerId()) == null)){
                return true;
            }
        }

        return false;
    }

    /**
     * 图层的透明度是否不可以改变， true 表示不可以改变 false表示可以改变
     * @param value
     * @return
     */
    private boolean ifLayerOpacacityIsUnChangeable(LayerInfo value) {
        if (value.getType() == null || value.getType().equals(LayerType.Unsupported)) {
            return true;
        }
        /**
         * 如果是Dynamic的子圖層，那么不可以控制透明度
         */
        if (value.getType() == LayerType.DynamicLayer_SubLayer){
           return true;
        }else{
            if ((mAllLayer != null && mAllLayer.get(value.getLayerId()) == null)){
                return true;
            }
        }

        return false;
    }

    protected View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.layer_treenview_childholder, null, false);
    }

    public void toggleSelectionMode(boolean editModeEnabled) {
        if (editModeEnabled) {
            if (mNode.isSelected()) {
                ll_child_unselected.performClick(); //执行全选
            } else {
                ll_child_selected.performClick(); //全不选
            }
        }
    }

    public void setOnHolderItemClickListener(ILayerView.OnHolderItemClickListener onHolderItemClickListener) {
        this.onHolderItemClickListener = onHolderItemClickListener;
    }
}
