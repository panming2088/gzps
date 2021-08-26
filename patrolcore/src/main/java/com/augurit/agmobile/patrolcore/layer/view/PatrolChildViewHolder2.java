package com.augurit.agmobile.patrolcore.layer.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.patrolcore.R;
import com.esri.android.map.Layer;

import java.util.Map;

/**
 * Created by xcl on 2017/10/19.
 */

public class PatrolChildViewHolder2 {
    //子节点名称
    protected TextView tv_nodeName;

    protected ViewGroup ll_iv_opacity;
    protected ImageView iv_opacity;
    protected SeekBar mSeekbar;
    protected TextView opacity_value;
    protected Button btn_close;
    protected RelativeLayout rl_container;
    protected String mGroupName;  //组名称，通过这个组名称将parent和child联系起来

    protected View mView;
    protected ILayerView.OnLayerItemClickListener mOnLayerItemClickListener;

    protected ILayerView.OnHolderItemClickListener onHolderItemClickListener;
    protected Context mContext;
    private SwitchCompat switchCompat;
    protected Map<Integer, Layer> mAllLayer;
    protected Map<Integer,Layer> mTempLayer;

    public PatrolChildViewHolder2(Context context, String parentName,
                                  Map<Integer, Layer> allLayer,
                                  ILayerView.OnLayerItemClickListener onLayerItemClickListener) {
        this.mContext = context;
        this.mOnLayerItemClickListener = onLayerItemClickListener;
        this.mGroupName = parentName;
        initLayer();
        this.mAllLayer = allLayer;
    }

    private void initLayer() {

    }


    public View createNodeView(final LayerInfo value) {
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        mView = getView(inflater);
        tv_nodeName = (TextView) mView.findViewById(com.augurit.agmobile.mapengine.R.id.node_value);
        tv_nodeName.setText(value.getLayerName());
        if (value.getType() == LayerType.Unsupported) {
            tv_nodeName.setTextColor(Color.GRAY);
        }

        ll_iv_opacity = (ViewGroup) mView.findViewById(com.augurit.agmobile.mapengine.R.id.ll_iv_opacity);
        iv_opacity = (ImageView) mView.findViewById(com.augurit.agmobile.mapengine.R.id.iv_opacity);

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


        opacity_value = (TextView) mView.findViewById(com.augurit.agmobile.mapengine.R.id.tv_treeview_childview_opacity_num);
        int initOpacity = 100 - (int) (value.getOpacity() * 100);
        opacity_value.setText(initOpacity + "%");

        //当该节点的图层是不支持加载时,文字变灰色
        if (ifLayerIsInactive(value)) {
            tv_nodeName.setTextColor(mContext.getResources().getColor(com.augurit.agmobile.mapengine.R.color.agmobile_grey_0));
        }

        if (ifLayerOpacacityIsUnChangeable(value)) {
            ll_iv_opacity.setClickable(false);
            iv_opacity.setClickable(false);
            iv_opacity.setVisibility(View.GONE);
            ll_iv_opacity.setVisibility(View.GONE);
        }

        mSeekbar = (SeekBar) mView.findViewById(com.augurit.agmobile.mapengine.R.id.seekbar_treeview_childview_opacity);
        mSeekbar.setProgress(initOpacity);
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                opacity_value.setText(progress + "%");
                double opacity = 1.0f - progress / 10 * 0.1;
                mOnLayerItemClickListener.onOpacityChange(value.getLayerId(), mGroupName, value, (float) opacity);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btn_close = (Button) mView.findViewById(com.augurit.agmobile.mapengine.R.id.btn_treeview_childview_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekbar.setVisibility(View.GONE);
                rl_container.setVisibility(View.GONE);
                iv_opacity.setVisibility(View.VISIBLE);
            }
        });

        rl_container = (RelativeLayout) mView.findViewById(com.augurit.agmobile.mapengine.R.id.rl_treeview_childview_container);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onHolderItemClickListener != null) {
                    onHolderItemClickListener.onItemClick(value);
                }
            }
        });


        /**
         * 滑块事件
         */
        switchCompat = (SwitchCompat) mView.findViewById(R.id.switch_compat_open_or_close_layer);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mOnLayerItemClickListener != null) {
                    mOnLayerItemClickListener.onClickCheckbox(value.getLayerId(), mGroupName, value, b);
                }
            }
        });
        if (value.isDefaultVisible()) {
            switchCompat.setChecked(true);
        }

        return mView;
    }


    public void changeCheckState(boolean check) {
        switchCompat.setChecked(check);
    }

    public boolean getCheckState() {
        return switchCompat.isChecked();
    }

    /**
     * 图层是否不支持加载
     *
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
        if (value.getType() == LayerType.DynamicLayer_SubLayer) {
            if ((mAllLayer != null && mAllLayer.get(Integer.valueOf(value.getParentLayerId())) == null)) {
                return true;
            }
        } else {
            if ((mAllLayer != null && mAllLayer.get(value.getLayerId()) == null)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 图层的透明度是否不可以改变， true 表示不可以改变 false表示可以改变
     *
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
        if (value.getType() == LayerType.DynamicLayer_SubLayer) {
            return true;
        } else {
            if ((mAllLayer != null && mAllLayer.get(value.getLayerId()) == null)) {
                return true;
            }
        }

        return false;
    }

    protected View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.layer_patrol_child_holder, null, false);
    }

//    public void toggleSelectionMode(boolean editModeEnabled) {
//        if (editModeEnabled) {
//            if (mNode.isSelected()) {
//                ll_child_unselected.performClick(); //执行全选
//            } else {
//                ll_child_selected.performClick(); //全不选
//            }
//        }
//    }

    public void setOnHolderItemClickListener(ILayerView.OnHolderItemClickListener onHolderItemClickListener) {
        this.onHolderItemClickListener = onHolderItemClickListener;
    }
}
