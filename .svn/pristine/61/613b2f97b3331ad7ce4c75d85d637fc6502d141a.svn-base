package com.augurit.agmobile.mapengine.common;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;

/**
 * 获取地图的地理基本信息
 * Created by liangsh on 2016-11-10.
 */
public class GeographyInfoManager {
    private static GeographyInfoManager instance;
//    private WeakReference<MapView> mMapView;
    private MapView mapView;
    private Polygon envelope;

    private GeographyInfoManager(MapView mapView){
//        this.mMapView = new WeakReference<MapView>(mapView);
        this.mapView = mapView;
    }

    public static void init(MapView mapView){
        if(instance==null){
            instance = new GeographyInfoManager(mapView);
        }
    }

    public static GeographyInfoManager getInstance(){
        return instance;
    }

    public MapView getMapView(){
//        return mMapView.get();
        return mapView;
    }

    public Envelope getMaxExtent(){
//        if(mMapView.get()==null){
//            return null;
//        }
//        return mMapView.get().getMaxExtent();
        return mapView.getMaxExtent();
    }

    public SpatialReference getSpatialReference(){
//        if(mMapView.get()==null){
//            return null;
//        }
//        return mMapView.get().getSpatialReference();
        return mapView.getSpatialReference();
    }

//xcl 2017-02-22 增加以下方法
    public static void clearInstance(){
        instance = null;
    }

    public void setExtent(Polygon extent){
        envelope = extent;
    }

    public Polygon getEnvelope() {
        return envelope;
    }
}
