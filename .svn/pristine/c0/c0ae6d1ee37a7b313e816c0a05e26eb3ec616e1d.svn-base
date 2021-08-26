package com.augurit.agmobile.mapengine.map;


import com.augurit.agmobile.mapengine.map.graphic.PictureMarkerOverlay;
import com.esri.android.map.MapOnTouchListener;

/**
 * 这是Augur Maps Android API的主要类，是与地图相关的所有方法的入口点。
 *
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.am.intro.map
 * @createTime 创建时间 ：2017-01-11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：（1） 2017-02-28 ：加入registerMapReadyCallback、unRegisterMapReadyCallback、setDefaultMapListener方法
 */
public class Map {
    private final IMapDelegate mIAgMapDelegate;

    public Map(IMapDelegate iAgMapDelegate) {
        this.mIAgMapDelegate = iAgMapDelegate;
    }

    public float getMaxZoom() {
        return this.mIAgMapDelegate.getMaxZoom();
    }

    public void setMaxZoom(float maxZoom) {
        this.mIAgMapDelegate.setMaxZoom(maxZoom);
    }

    public float getMinZoom() {
        return this.mIAgMapDelegate.getMinZoom();
    }

    public void setMinZoom(float minZoom) {
        this.mIAgMapDelegate.setMinZoom(minZoom);
    }

    public void zoomIn() {
        this.mIAgMapDelegate.zoomIn();
    }

    public void zoomOut() {
        this.mIAgMapDelegate.zoomOut();
    }

    public <T> T addMarker(PictureMarkerOverlay markerOptions) {
        return this.mIAgMapDelegate.addMarker(markerOptions);
    }

    public void setMapClickListener(OnMapClickListener mapClickListener) {
        this.mIAgMapDelegate.setMapClickListener(mapClickListener);
    }

    public void setMapDoubleClickListener(OnMapDoubleClickListener mapDoubleClickListener) {
        this.mIAgMapDelegate.setMapDoubleClickListener(mapDoubleClickListener);
    }

    public void setMapLongClickListener(OnMapLongClickListener mapLongClickListener) {
        this.mIAgMapDelegate.setMapLongClickListener(mapLongClickListener);
    }

    public  void setMapMoveListener(OnMapMoveListener mapMoveListener) {
        this.mIAgMapDelegate.setMapMoveListener(mapMoveListener);
    }

    /**
     * 注册当地图加载完成时的回调
     * @param callback 地图加载完成时的回调
     */
    public void registerMapReadyCallback(OnMapReadyCallback callback){ //xcl 2017-02-28
        this.mIAgMapDelegate.registerMapReadyCallback(callback);
    }

    /**
     * 取消注册当地图加载完成时的回调
     * @param callback 地图加载完成时的回调
     */
    public void unRegisterMapReadyCallback(OnMapReadyCallback callback){ //xcl 2017-02-28
        this.mIAgMapDelegate.unRegisterMapReadyCallback(callback);
    }

    /**
     * 设置默认地图监听事件
     * @param mapListener 默认地图监听事件
     */
    public void setDefaultMapListener(MapOnTouchListener mapListener){
        this.mIAgMapDelegate.setDefaultMapListener(mapListener);
    }

    public void clear() {
        this.mIAgMapDelegate.clear();
    }

}
