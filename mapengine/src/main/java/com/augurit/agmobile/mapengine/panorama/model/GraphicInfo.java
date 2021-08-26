package com.augurit.agmobile.mapengine.panorama.model;

import com.esri.core.geometry.Geometry;
import com.esri.core.map.Graphic;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.comn.model
 * @createTime 创建时间 ：2016-12-22
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-22
 */

public class GraphicInfo {

    private Geometry mGeometry;
    private int id;

    public GraphicInfo(Geometry geometry,int id){
        this.mGeometry =geometry;
        this.id = id;
    }

    public Geometry getGeometry() {
        return mGeometry;
    }

    public void setGeometry(Geometry geometry) {
        mGeometry = geometry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
