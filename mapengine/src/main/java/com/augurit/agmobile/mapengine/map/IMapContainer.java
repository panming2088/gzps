package com.augurit.agmobile.mapengine.map;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.am.intro.maps.arcgis
 * @createTime 创建时间 ：2017-01-16
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-16 10:36
 */
public interface IMapContainer {
    View getMapView();

    void setMapView(int mapViewResId);

    void setMapView(ViewGroup mapView);

    ViewGroup getSelf();

    Map getMap();

    void setMap(Map map);
}
