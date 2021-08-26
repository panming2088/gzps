package com.augurit.am.cmpt.loc;

import android.location.Location;
import android.support.annotation.Keep;

import com.augurit.am.cmpt.coordt.model.Coordinate;


/**
 * 进行坐标转换的类。如果地图的坐标系不是WGS84坐标系，实现这个类的方法，返回正确的坐标。
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.location.service
 * @createTime 创建时间 ：2017-02-10
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-10
 * @modifyMemo 修改备注：
 * @version 1.0
 */
@Keep
public interface ILocationTransform {

    Coordinate changeWGS84ToCurrentLocation(Location location);
    Coordinate changeWGS84ToCurrentLocation(double log, double lat);

   // Location  changeCurrentLocationToWGS84(Coordinate coordinate);
}
