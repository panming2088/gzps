package com.esri.android.map.ags;

import com.esri.core.ags.LayerServiceInfo;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.FeatureSet;

/**
 * Created by liangsh on 2016-09-29.
 */
public class AGFeatureLayer extends ArcGISFeatureLayer {

    public AGFeatureLayer(String url, Options layerOption){
        super(url, layerOption);
    }

    public AGFeatureLayer(String url, Options layerOption, UserCredentials credentials){
        super(url, layerOption,credentials);
    }

    public AGFeatureLayer(String url, Options layerOption, UserCredentials credentials, boolean useAdvancedSymbols){
        super(url, layerOption, credentials, useAdvancedSymbols);
    }

    public AGFeatureLayer(String url, Options layerOption, UserCredentials credentials, boolean useAdvancedSymbols, RenderingMode rmode){
        super(url, layerOption, credentials, useAdvancedSymbols, rmode);
    }

    public AGFeatureLayer(String url, MODE mode){
        super(url, mode);
    }

    public AGFeatureLayer(String url, String layerDefinition, MODE mode){
        super(url, layerDefinition, mode);
    }

    public AGFeatureLayer(String url, String layerDefinition, MODE mode, boolean initLayer){
        super(url, layerDefinition, mode, initLayer);
    }

    public AGFeatureLayer(String url, String layerDefinition, MODE mode, boolean initLayer, UserCredentials credentials){
        super(url, layerDefinition, mode, initLayer, credentials);
    }

    public AGFeatureLayer(String url, String layerDefinition, MODE mode, boolean initLayer, UserCredentials credentials, boolean useAdvancedSymbols){
        super(url, layerDefinition, mode, initLayer, credentials, useAdvancedSymbols);
    }

    public AGFeatureLayer(String url, String layerDefinition, MODE mode, boolean initLayer, UserCredentials credentials, boolean useAdvancedSymbols, RenderingMode rmode){
        super(url, layerDefinition, mode, initLayer, credentials, useAdvancedSymbols, rmode);
    }

    public AGFeatureLayer(String url, MODE mode, UserCredentials credentials){
        super(url, mode, credentials);
    }

    public AGFeatureLayer(String layerDefinition, FeatureSet featureCollection, Options layerOption){
        super(layerDefinition, featureCollection, layerOption);
    }

    public AGFeatureLayer(String layerDef, String layerDefinitionOverride, FeatureSet featureCollection, Options layerOption, boolean initLayer){
        super(layerDef, layerDefinitionOverride, featureCollection, layerOption, initLayer);
    }


    public void setLayerServiceInfo(LayerServiceInfo layerServiceInfo){
        this.f = layerServiceInfo;
    }

}
