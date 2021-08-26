package com.augurit.agmobile.gzps.addcomponent.model;

import com.esri.android.map.ags.ArcGISFeatureLayer;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.map.model
 * @createTime 创建时间 ：2017-10-14
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-10-14
 * @modifyMemo 修改备注：
 */
@Deprecated
public class ComponentMap {

    private String name;
    private String url;
    private ArcGISFeatureLayer layer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArcGISFeatureLayer getLayer() {
        return layer;
    }

    public void setLayer(ArcGISFeatureLayer layer) {
        this.layer = layer;
    }
}
