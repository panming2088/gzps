package com.augurit.agmobile.patrolcore.selectlocation.service;


import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.am.cmpt.common.Callback2;
import com.esri.android.map.MapView;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;
import com.esri.core.tasks.geocode.LocatorSuggestionResult;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agcollection.selectlocation
 * @createTime 创建时间 ：2017-05-19
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-05-19
 * @modifyMemo 修改备注：
 */

public interface ISelectLocationService {

    Observable<DetailAddress> parseLocation(LatLng latLng, SpatialReference spatialReference);

    //Observable<BaiduGeocodeResult> parseLocationByBaidu(LatLng latLng);

    //void  parseLocationByArcgis(LatLng latLng, Callback2<LocatorReverseGeocodeResult> callback2);

    /**
     * 获取当前位置附近的地址
     * @param latLng
     * @param distance
     * @param callback2
     */
    void getPOIByArcgis(String address, LatLng latLng, MapView mapView, SpatialReference spatialReference, int distance,
                        Callback2<List<LocatorGeocodeResult>> callback2);

    void getNearByLocationByArcgis(Map<String, String> addressFields, LatLng latLng, MapView mapView, SpatialReference spatialReference, int distance,
                                   Callback2<List<LocatorGeocodeResult>> callback2);

    void getNearByThroughSuggest(String address, LatLng latLng, SpatialReference spatialReference, int distance,
                                 Callback2<List<LocatorSuggestionResult>> callback2);

}
