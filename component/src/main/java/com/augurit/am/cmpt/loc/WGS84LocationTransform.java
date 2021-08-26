package com.augurit.am.cmpt.loc;

import android.location.Location;

import com.augurit.am.cmpt.coordt.model.Coordinate;

/**
 * 直接返回WGS84坐标
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.loc.impl
 * @createTime 创建时间 ：2016-11-17
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-17
 */

public class WGS84LocationTransform implements ILocationTransform {
    @Override
    public Coordinate changeWGS84ToCurrentLocation(Location location) {
        Coordinate coordinate = new Coordinate(location.getLongitude(),location.getLatitude());
        return coordinate;
    }

    @Override
    public Coordinate changeWGS84ToCurrentLocation(double log, double lat) {
        Coordinate coordinate = new Coordinate(log,lat);
        return coordinate;
    }
}
