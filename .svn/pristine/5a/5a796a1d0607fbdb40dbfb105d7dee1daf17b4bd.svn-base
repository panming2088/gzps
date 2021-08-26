package com.augurit.agmobile.mapengine.statistics.view;

import com.augurit.agmobile.mapengine.bufferanalysis.util.BufferMapOnTouchListener;
import com.augurit.agmobile.mapengine.layerimport.view.OnFeatureClickedListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;

/**
 * 描述：地图统计工具视图接口
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.statistics.view
 * @createTime 创建时间 ：2017-02-07
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-07
 * @modifyMemo 修改备注：
 */
public interface IStatisticsToolView {

    /**
     * 显示视图
     */
    void show();

    /**
     * 隐藏视图
     */
    void dismiss();

    /**
     * 是否处于显示状态
     * @return 是否显示
     */
    boolean isShow();

    /**
     * 设置Feature点击监听
     * @param onFeatureClickedListener
     */
    void setOnFeatureClickedListener(OnFeatureClickedListener onFeatureClickedListener);

    /**
     * 设置形状图层ID
     * @param layerID
     */
    void setShpLayerID(long layerID);

    /**
     * 向GraphicsLayer添加形状
     * @param geometry
     */
    void addGeometry(Geometry geometry);

    /**
     * 得到当前圈选范围
     * @return 当前圈选范围
     */
    Geometry getCurrentGeometry();

    /**
     * 生成缓冲区
     * @param radius 缓冲半径
     * @return 缓冲后的图形
     */
    Geometry buffer(double radius, SpatialReference spatialReference);

    /**
     * 获取缓冲区形状
     * @return
     */
    Geometry getBufferGeometry();

    /**
     * 获取当前操作状态
     * @return 操作状态
     */
    BufferMapOnTouchListener.OperationState getCurrentOperationState();
}
