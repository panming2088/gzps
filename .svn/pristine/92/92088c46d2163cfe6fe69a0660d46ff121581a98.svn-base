package com.augurit.agmobile.mapengine.common.widget.callout.attribute;

import com.esri.core.geometry.Geometry;

import java.util.Map;

/**
 * 通用查询结果实体类
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.layerq.newarc.model
 * @createTime 创建时间 ：2016-11-30
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-30
 */

public class AMFindResult {
    long id;
    int layerId;
    String layerName;
    String displayFieldName;
    Map<String, Object> attributes;
    Geometry mGeometry;
    String mDisplayName;
    String value;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLayerId() {
        return layerId;
    }

    public void setLayerId(int layerId) {
        this.layerId = layerId;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getDisplayFieldName() {
        return displayFieldName;
    }

    public void setDisplayFieldName(String displayFieldName) {
        this.displayFieldName = displayFieldName;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Geometry getGeometry() {
        return mGeometry;
    }

    public void setGeometry(Geometry geometry) {
        mGeometry = geometry;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    @Override
    public String toString() {
        return "FindResult [_layerId=" + this.layerId + ", _layerName="
                + this.layerName + ", _displayFieldName=" +
                this.displayFieldName + ", _value=" +
                this.value + ", _attributes=" +
                this.attributes + ", _geometryType=" +
                this.mGeometry + "]";
    }
}
