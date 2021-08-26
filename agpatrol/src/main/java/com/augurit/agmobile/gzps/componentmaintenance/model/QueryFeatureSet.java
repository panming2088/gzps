package com.augurit.agmobile.gzps.componentmaintenance.model;

import com.esri.core.map.FeatureSet;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.componentmaintenance.model
 * @createTime 创建时间 ：2017-10-15
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-10-15
 * @modifyMemo 修改备注：
 */

public class QueryFeatureSet {

    private String layerUrl;
    private String layerName;
    private FeatureSet featureSet;


    public String getLayerUrl() {
        return layerUrl;
    }

    public void setLayerUrl(String layerUrl) {
        this.layerUrl = layerUrl;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public FeatureSet getFeatureSet() {
        return featureSet;
    }

    public void setFeatureSet(FeatureSet featureSet) {
        this.featureSet = featureSet;
    }
}
