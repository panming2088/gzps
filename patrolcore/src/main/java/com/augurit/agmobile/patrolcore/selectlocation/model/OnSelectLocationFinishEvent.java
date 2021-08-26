package com.augurit.agmobile.patrolcore.selectlocation.model;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;

/**
 * 选择上报地点结束事件
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.map
 * @createTime 创建时间 ：2017-03-08
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-08
 * @modifyMemo 修改备注：
 */

public class OnSelectLocationFinishEvent {

    private LatLng mMapLatlng;
    private String mAddress;

    private double mScale;


    public OnSelectLocationFinishEvent(LatLng mapLatlng, double scale, String address) {
        this.mMapLatlng = mapLatlng;
        this.mAddress = address;
        this.mScale = scale;
    }

    public double getScale() {
        return mScale;
    }

    public String getAddress() {
        return mAddress;
    }

    public LatLng getMapLatlng() {
        return mMapLatlng;
    }

    public void setMapLatlng(LatLng mapLatlng) {
        mMapLatlng = mapLatlng;
    }

}
