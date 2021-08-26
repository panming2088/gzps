package com.augurit.agmobile.patrolcore.selectdevice.service;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.selectdevice.model.Device;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;

import java.util.List;

import rx.Observable;

/**
 * 设施选择的Service
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.selectdevice.service
 * @createTime 创建时间 ：2017-03-29
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-29
 * @modifyMemo 修改备注：
 */

public interface ISelectDeviceService {

    /**
     * 获取模拟查询得到设施
     *
     * @return
     */
    Observable<List<Device>> getSuggestionDevice(MapView mapView,LatLng latLng);

    /**
     * 根据设施名称模糊搜索设施
     * @param text
     * @return
     */
    Observable<List<Device>> getSuggestionDevice(String text);

    /**
     * 根据设施的id得到设施的wkt
     * @param deviceId
     * @return
     */
    Observable<String> getWktByDeviceId(String deviceId);

    /**
     * 根据设施的id和名称找到设施
     * @param deviceId
     * @param deviceName
     * @return
     */
    Observable<Geometry> getWktByDeviceId(final String deviceId, final String deviceName);

    /**
     * 根据当前经纬度查找附近的设施，并默认返回第一个
     * @param longitude
     * @param latitude
     * @return
     */
    Observable<Device> getNearbyDevice(MapView mapView,double longitude, double latitude);

}
