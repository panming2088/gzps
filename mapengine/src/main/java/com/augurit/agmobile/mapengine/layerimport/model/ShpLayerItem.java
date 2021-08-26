package com.augurit.agmobile.mapengine.layerimport.model;

import com.esri.android.map.FeatureLayer;

/**
 * Created by liangsh on 2016-11-29.
 */
public class ShpLayerItem {
    private String name;
    private FeatureLayer featureLayer;
    private boolean checked = false;

    public ShpLayerItem(){

    }

    public ShpLayerItem(String name, FeatureLayer featureLayer){
        this.name = name;
        this.featureLayer = featureLayer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FeatureLayer getFeatureLayer() {
        return featureLayer;
    }

    public void setFeatureLayer(FeatureLayer featureLayer) {
        this.featureLayer = featureLayer;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
