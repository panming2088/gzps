package com.augurit.agmobile.mapengine.location;

import android.location.Location;
import android.location.LocationListener;

import com.augurit.am.cmpt.loc.ILocationTransform;
import com.esri.android.map.MapView;

/**
 * 定位管理接口
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.location
 * @createTime 创建时间 ：2017-01-19
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-19
 */

public interface ILocationManager {

    void startLocate(LocationListener locationListener);

    void stopLocate();

    void setCoordinateSystem(ILocationTransform locationTransform);

    void hideLocationSymbol();


    Location changeWGS84ToCurrentCoordinate(Location location);

    void setMapView(MapView mapView);

    MapView getMapView();

    void setUseArcGisForLocation();

}
