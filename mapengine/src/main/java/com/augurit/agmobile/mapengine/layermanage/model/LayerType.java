package com.augurit.agmobile.mapengine.layermanage.model;


import android.support.annotation.Keep;

/**
 * AGMobile支持添加的图层类型
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.layer.model.enums
 * @createTime 创建时间 ：2016-10-14 13:57
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2016-10-14 13:57
 */
@Keep
public enum LayerType {

    DynamicLayer("DynamicLayer"),
    TileLayer("TileLayer"),
    FeatureLayer("FeatureLayer"),
    WMTSLayer("WMTSLayer"),
    WMSLayer("WMSLayer"),
    WFSLayer("WFSLayer"),
    KmlLayer("KmlLayer"),
    RasterLayer("RasterLayer"),
    TianDiTu("TianDiTu"),
    FeatureServer("FeatureServer"),
    Unsupported("Unsupported"),
    LocalFeatureLayer("localFeatureLayer"),
    DynamicLayer_SubLayer("DynamicLayer_SubLayer");//dynmicLayer的子图层

    String name ;
    LayerType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
