package com.augurit.agmobile.mapengine.map;


import com.augurit.agmobile.mapengine.map.geometry.LatLng;

/**
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.am.intro.maps
 * @createTime 创建时间 ：2017-01-22
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-22 11:20
 */
public interface OnMapClickListener {
    boolean onMapClick(float screenX,float screenY);
    //boolean onMapClick(LatLng point);
}
