package com.augurit.agmobile.mapengine.location.service;

import android.location.Location;

import com.augurit.am.cmpt.coordt.mgr.CoordTransformManager;
import com.augurit.am.cmpt.coordt.mgr.Xian1980ParmSevenManager;
import com.augurit.am.cmpt.coordt.model.Coordinate;
import com.augurit.am.cmpt.coordt.model.ParameterSeven;
import com.augurit.am.cmpt.loc.ILocationTransform;

/**
 * 将WGS84坐标转成西安1980坐标
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.loc.impl
 * @createTime 创建时间 ：2016-10-19 14:20
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2016-10-19 14:20
 */

public class Xian1980LocationTransform implements ILocationTransform {
    @Override
    public Coordinate changeWGS84ToCurrentLocation(Location location) {

        Coordinate coordinate = new Coordinate(location.getLongitude(), location.getLatitude());

        //西安80的七参数
        ParameterSeven xian80ParameterSeven = Xian1980ParmSevenManager.getXIAN80ParameterSeven();
        Coordinate coordinate1 = CoordTransformManager.transFromWGS84ToXian80(coordinate, xian80ParameterSeven, Xian1980ParmSevenManager.LZ);
        return coordinate1;
    }

    @Override
    public Coordinate changeWGS84ToCurrentLocation(double log, double lat) {
        Coordinate coordinate = new Coordinate(log, lat);

        //西安80的七参数
        ParameterSeven xian80ParameterSeven = Xian1980ParmSevenManager.getXIAN80ParameterSeven();
        Coordinate coordinate1 = CoordTransformManager.transFromWGS84ToXian80(coordinate, xian80ParameterSeven, Xian1980ParmSevenManager.LZ);
        return coordinate1;
    }
}
