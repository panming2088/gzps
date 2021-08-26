package com.augurit.am.cmpt.loc;

import android.location.Location;

import com.augurit.am.cmpt.coordt.model.Coordinate;

/**
 * 进行坐标转换的类。如果地图的坐标系不是WGS84坐标系，实现这个类的方法，返回正确的坐标。
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.loc
 * @createTime 创建时间 ：17/7/27
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/27
 * @modifyMemo 修改备注：
 */

public interface IReverseLocationTransform {

    Coordinate changeCurrentLocationToWGS84(double longitude, double latitude);
}
