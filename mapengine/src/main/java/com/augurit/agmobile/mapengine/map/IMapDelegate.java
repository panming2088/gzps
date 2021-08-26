package com.augurit.agmobile.mapengine.map;


import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.map.graphic.Overlay;
import com.augurit.agmobile.mapengine.map.graphic.PictureMarkerOverlay;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.esri.android.map.MapOnTouchListener;

/**
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.am.intro.map
 * @createTime 创建时间 ：2017-01-11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：（1）2017-02-28 加入registerMapReadyCallback、unRegisterMapReadyCallback、setDefaultMapListener方法；
 */
public interface IMapDelegate {
    float getMaxZoom();

    void setMaxZoom(float maxZoom);

    float getMinZoom();

    void setMinZoom(float minZoom);

    void getMapAsync(final OnMapReadyCallback callback);

    void zoomIn();

    void zoomOut();

    <T> T addMarker(PictureMarkerOverlay markerOptions);

    void setMapClickListener(OnMapClickListener mapClickListener);

    void setMapDoubleClickListener(OnMapDoubleClickListener mapDoubleClickListener);

    void setMapLongClickListener(OnMapLongClickListener mapLongClickListener);

    void setMapMoveListener(OnMapMoveListener mapMoveListener);

    /**
     * 注册当地图加载完成时的回调
     * @param callback 地图加载完成时的回调
     */
    void registerMapReadyCallback(OnMapReadyCallback callback);
    /**
     * 取消注册当地图加载完成时的回调
     * @param callback 地图加载完成时的回调
     */
    void unRegisterMapReadyCallback(OnMapReadyCallback callback);
    /**
     * 清空地图所有的 Overlay 覆盖物以及 InfoWindow
     */
    void clear();

    /**
     * 更换默认的点击事件
     * @param mapListener
     */
    void setDefaultMapListener(MapOnTouchListener mapListener);

    //xcl 2017-02-15 增加以下方法
   /* void addLayer(LayerInfo layerInfo);

    void showLayerByLayerId(int layerId);

    void hideLayerByLayerId(int layerId);

    void centerAt(LatLng latLng, boolean ifAnimated);

    void setResolution(double resolution);

    void removeAllLayer();

    *//**
     * 点击地图上的覆盖物时的回调
     *//*
    //void setOnOverlayClickListener(OnOverlayClickListener listener);

    *//**
     * 设置地图缓存路径，默认缓存在SD卡的/AGMobile/Layer文件夹下
     * @param cachepath 传入相对路径，比如：/AGMobile/Layer
     *//*
    void setCachePath(String cachepath);

    void hideInfoWindow();

    void showInfoWindow(InfoWindow infoWindow);

    *//**
     * 添加一层新的绘制覆盖物的图层，相当于add一层GraphicLayer
     * @return 覆盖物的图层的id
     *//*
    int addOverlayCanvas();

    *//**
     * 在特定的图层上绘制点覆盖物
     * @param overlayCanvasId 覆盖物图层的id
     * @param overlay 要绘制的覆盖物
     *//*
    void addOverlay(int overlayCanvasId, Overlay overlay);*/
}
