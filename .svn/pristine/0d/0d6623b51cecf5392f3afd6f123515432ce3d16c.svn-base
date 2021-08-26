package com.augurit.agmobile.mapengine.common.agmobilelayer;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.ags.LayerServiceInfo;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.FeatureSet;

import java.util.concurrent.RejectedExecutionException;

/**
 * Created by liangsh on 2016-09-29.
 */
public class AGFeatureLayer extends com.esri.android.map.ags.AGFeatureLayer {

    private int mLayerId;
    public AGFeatureLayer(int layerId,String url, ArcGISFeatureLayer.Options layerOption){
        super(url, layerOption);
        this.mLayerId = layerId;
    }

    public AGFeatureLayer(int layerId, String url, ArcGISFeatureLayer.Options layerOption, UserCredentials credentials){
        super(url, layerOption,credentials);
        this.mLayerId = layerId;
    }

    public AGFeatureLayer(int layerId, String url, ArcGISFeatureLayer.Options layerOption, UserCredentials credentials, boolean useAdvancedSymbols){
        super(url, layerOption, credentials, useAdvancedSymbols);
        this.mLayerId = layerId;
    }

    public AGFeatureLayer(int layerId, String url, ArcGISFeatureLayer.Options layerOption, UserCredentials credentials, boolean useAdvancedSymbols, GraphicsLayer.RenderingMode rmode){
        super(url, layerOption, credentials, useAdvancedSymbols, rmode);
        this.mLayerId = layerId;
    }

    public AGFeatureLayer(int layerId,String url, ArcGISFeatureLayer.MODE mode){
        super(url, mode);
        this.mLayerId = layerId;
    }

    public AGFeatureLayer(int layerId,String url, String layerDefinition, ArcGISFeatureLayer.MODE mode){
        super(url, layerDefinition, mode);
        this.mLayerId = layerId;
    }

    public AGFeatureLayer(int layerId, String url, String layerDefinition, ArcGISFeatureLayer.MODE mode, boolean initLayer){
        super(url, layerDefinition, mode, initLayer);
        this.mLayerId = layerId;
    }

    public AGFeatureLayer(int layerId, String url, String layerDefinition, ArcGISFeatureLayer.MODE mode, boolean initLayer, UserCredentials credentials){
        super(url, layerDefinition, mode, initLayer, credentials);
        this.mLayerId = layerId;
    }

    public AGFeatureLayer(int layerId, String url, String layerDefinition, ArcGISFeatureLayer.MODE mode, boolean initLayer, UserCredentials credentials, boolean useAdvancedSymbols){
        super(url, layerDefinition, mode, initLayer, credentials, useAdvancedSymbols);
        this.mLayerId = layerId;
    }

    public AGFeatureLayer(int layerId, String url, String layerDefinition, ArcGISFeatureLayer.MODE mode, boolean initLayer, UserCredentials credentials, boolean useAdvancedSymbols, GraphicsLayer.RenderingMode rmode){
        super(url, layerDefinition, mode, initLayer, credentials, useAdvancedSymbols, rmode);
        this.mLayerId = layerId;
    }

    public AGFeatureLayer(int layerId, String url, ArcGISFeatureLayer.MODE mode, UserCredentials credentials){
        super(url, mode, credentials);
        this.mLayerId = layerId;
    }

    public AGFeatureLayer(int layerId, String layerDefinition, FeatureSet featureCollection, ArcGISFeatureLayer.Options layerOption){
        super(layerDefinition, featureCollection, layerOption);
        this.mLayerId = layerId;
    }

    public AGFeatureLayer(int layerId, String layerDef, String layerDefinitionOverride, FeatureSet featureCollection, ArcGISFeatureLayer.Options layerOption, boolean initLayer){
        super(layerDef, layerDefinitionOverride, featureCollection, layerOption, initLayer);
        this.mLayerId = layerId;
    }

    public void setLayerServiceInfo(LayerServiceInfo layerServiceInfo){
        super.setLayerServiceInfo(layerServiceInfo);
    }

    public int getLayerId() {
        return mLayerId;
    }

    @Override
    public void initLayer() {
        if(this.nativeHandle == 0L) {
            this.nativeHandle = this.create();
        }

        if(this.nativeHandle != 0L) {
                try {
                    this.getServiceExecutor().submit(new Runnable() {
                        public void run() {
                            AGFeatureLayer.super.initLayer();
                        }
                    });
                } catch (RejectedExecutionException var3) {
                }
        } else {
            this.changeStatus(OnStatusChangedListener.STATUS.fromInt(-1004));
        }
    }

}
