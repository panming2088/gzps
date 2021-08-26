package com.augurit.agmobile.mapengine.bufferanalysis.view;

import android.content.Context;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.bufferanalysis.util.BufferMapOnTouchListener;
import com.augurit.agmobile.mapengine.layerimport.view.OnFeatureClickedListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;

/**
 * 缓冲分析工具栏接口
 * @author 创建人 ：liangshenghong
 * @package 包名 ：com.augurit.agmobile.mapengine.bufferanalysis.view
 * @createTime 创建时间 ：2017-02-07
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-02-07
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public abstract class IBufferAnlaysisToolView extends BaseView{

    public IBufferAnlaysisToolView(Context context, ViewGroup container){
        super(context, container);
    }

    /**
     * 设置当前的状态为空，即不监听地图的滑动事件
     */
    public abstract void setOperationStateNone();

    /**
     * 显示工具栏
     */
    public abstract void show();

    /**
     * 设置shape图层的要素点击事件监听器
     * @param onFeatureClickedListener
     */
    public abstract void setOnFeatureClickedListener(OnFeatureClickedListener onFeatureClickedListener);

    /**
     * 设置当前导入的shape图层的ID
     * @param layerID
     */
    public abstract void setShpLayerID(long layerID);

    /**
     * 在地图上显示一个几何图形
     * @param geometry
     */
    public abstract void addGeometry(Geometry geometry);

    /**
     * 隐藏工具栏
     */
    public abstract void dismiss();

    /**
     * 工具栏是否显示状态
     * @return
     */
    public abstract boolean isShow();

    /**
     * 获取当前待分析的几何图形，如果设置了缓冲半径，则返回缓冲后的几何图形
     * @return
     */
    public abstract Geometry getCurrentGeometry();

    /**
     * 为地图上的图形生成缓冲区
     * @param radius 缓冲半径
     * @param spatialReference 空间参考
     * @return 缓冲区
     */
    public abstract Geometry buffer(double radius, SpatialReference spatialReference);

    /**
     * 获取缓冲后的几何图形
     * @return
     */
    public abstract Geometry getBufferGeometry();

    /**
     * 获取当前工具栏的操作状态
     * @return
     */
    public abstract BufferMapOnTouchListener.OperationState getCurrentOperationState();
}
