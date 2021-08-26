package com.augurit.agmobile.patrolcore.layerdownload;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.augurit.agmobile.mapengine.layerdownload.view.ILayerDownloadToolView;
import com.augurit.agmobile.mapengine.statistics.view.StatisticsBaseView;
import com.augurit.agmobile.patrolcore.R;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;

/**
 * Created by liangsh on 2016-11-10.
 */
public class LayerDownloadToolView extends StatisticsBaseView implements ILayerDownloadToolView, View.OnClickListener{

    private MapView mMapView;
    private LayerQueryTouchListener mOnTouchListener;  // 地图点击监听
    private MapOnTouchListener mDefaultListener;    // 默认监听
    private boolean mIsShow;    // 工具栏是否处于显示

    private ImageButton mGlobalBtn;
    private ImageButton mSquareBtn;
    private ImageButton mAreaBtn;
    private ImageButton mClearBtn;

    private GraphicsLayer mGraphicsLayer;  //用于显示框选范围的图形


    public LayerDownloadToolView(Context context, MapView mapView, ViewGroup toolContainer){
        super(context, toolContainer);
        mMapView = mapView;
    }

    @Override
    protected void init(){
        super.init();
        mView.setVisibility(View.GONE);
        mIsShow = false;
        mGraphicsLayer = new GraphicsLayer();
    }

    @Override
    protected ViewGroup getLayout() {
        return (ViewGroup) View.inflate(mContext, R.layout.dnl_areatool_view, null); // 工具按钮视图;
    }

    @Override
    protected void initView() {
        // 工具栏按钮
        mGlobalBtn = (ImageButton) mView.findViewById(R.id.dnl_btn_tool_global);
        mSquareBtn = (ImageButton) mView.findViewById(R.id.dnl_btn_tool_square);
        mAreaBtn = (ImageButton) mView.findViewById(R.id.dnl_btn_tool_area);
        mClearBtn = (ImageButton) mView.findViewById(R.id.dnl_btn_tool_clear);
    }

    @Override
    protected void initListener() {
        mGlobalBtn.setOnClickListener(this);
        mSquareBtn.setOnClickListener(this);
        mAreaBtn.setOnClickListener(this);
        mClearBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        resetButtonImage();
        if (v.getId() == R.id.dnl_btn_tool_global) {
            mOnTouchListener.setOperationState(LayerQueryTouchListener.OperationState.STATE_GLOBAL);
            mGlobalBtn.setImageResource(R.mipmap.common_ic_global_pressed);
        } else if (v.getId() == R.id.dnl_btn_tool_square){
            mOnTouchListener.setOperationState(LayerQueryTouchListener.OperationState.STATE_SQUARE);
            mSquareBtn.setImageResource(R.mipmap.common_ic_rectangle_pressed);
        } else if (v.getId() == R.id.dnl_btn_tool_area){
            mOnTouchListener.setOperationState(LayerQueryTouchListener.OperationState.STATE_CIRCLE);
            mAreaBtn.setImageResource(R.mipmap.common_ic_circle_pressed);
        } else if (v.getId() == R.id.dnl_btn_tool_clear){
            mOnTouchListener.setOperationState(LayerQueryTouchListener.OperationState.STATE_NONE);
        }
    }


    /**
     * 恢复按钮图片
     */
    protected void resetButtonImage() {
        mGlobalBtn.setImageResource(R.mipmap.common_ic_global_normal);
        mSquareBtn.setImageResource(R.mipmap.common_ic_rectangle_normal);
        mAreaBtn.setImageResource(R.mipmap.common_ic_circle_normal);
        mClearBtn.setImageResource(R.mipmap.layerq_ic_clear);
    }

    /**
     * 显示工具按钮
     */
    public void show() {
        mContainer.removeAllViews();
        mContainer.setVisibility(View.VISIBLE);
        mContainer.addView(mView);
        mView.setVisibility(View.VISIBLE);
        mIsShow = true;
        try {
            if(mGraphicsLayer != null){
                mGraphicsLayer.removeAll();
                mMapView.removeLayer(mGraphicsLayer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mGraphicsLayer = new GraphicsLayer();
        mMapView.addLayer(mGraphicsLayer);
        mOnTouchListener =
                new LayerQueryTouchListener(mContext.getApplicationContext(), mMapView, mGraphicsLayer);
        mDefaultListener = new MapOnTouchListener(mContext.getApplicationContext(), mMapView);
        mMapView.setOnTouchListener(mOnTouchListener);  // 设置地图点击监听
    }


    /**
     * 取消显示
     */
    public void dismiss() {
        mIsShow = false;
        if(mOnTouchListener != null){
            mOnTouchListener.setOperationState(LayerQueryTouchListener.OperationState.STATE_NONE);
        }
        try {
            mGraphicsLayer.removeAll();
            mMapView.removeLayer(mGraphicsLayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.setOnTouchListener(mDefaultListener);  // 恢复地图点击监听
        resetButtonImage();
        mContainer.removeAllViews();
    }

    public boolean isShow() {
        return mIsShow;
    }


    /**
     * 得到当前圈选范围
     * @return 当前圈选范围
     */
    public Geometry getCurrentGeometry(){
        if(mOnTouchListener.getOperationState()
                == LayerQueryTouchListener.OperationState.STATE_GLOBAL){
            return mMapView.getMaxExtent();
        }
        return mOnTouchListener.getGeometry();
    }

    public LayerQueryTouchListener.OperationState getCurrentOperationState(){
        return mOnTouchListener.getOperationState();
    }
}
