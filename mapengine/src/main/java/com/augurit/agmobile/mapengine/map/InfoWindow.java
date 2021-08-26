package com.augurit.agmobile.mapengine.map;

import android.view.View;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;

/**
 * 在地图中显示一个信息窗口
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.map
 * @createTime 创建时间 ：2017-02-16
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-16
 * @modifyMemo 修改备注：
 */

public class InfoWindow {

    private LatLng mAnchor; //infowindow显示的位置

    private InfoWindowStyle mInfoWindowStyle ;// infowindow的样式

    private View mView; //infowindow的内容

    public LatLng getAnchor() {
        return mAnchor;
    }

    public void setAnchor(LatLng anchor) {
        mAnchor = anchor;
    }

    public InfoWindowStyle getInfoWindowStyle() {
        return mInfoWindowStyle;
    }

    public void setInfoWindowStyle(InfoWindowStyle infoWindowStyle) {
        mInfoWindowStyle = infoWindowStyle;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        mView = view;
    }
}
