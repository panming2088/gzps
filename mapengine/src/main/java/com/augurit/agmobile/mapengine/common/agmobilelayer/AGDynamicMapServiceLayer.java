package com.augurit.agmobile.mapengine.common.agmobilelayer;

import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.core.ags.MapServiceInfo;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.DrawingInfo;

import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by liangsh on 2016-10-08.
 */
public class AGDynamicMapServiceLayer extends ArcGISDynamicMapServiceLayer {

    private int mLayerId;

    public AGDynamicMapServiceLayer(int layerId,String url, int[] visiblelayers){
        super(url, visiblelayers);
        this.mLayerId = layerId;
    }

    public AGDynamicMapServiceLayer(int layerId, String url, int[] visiblelayers, UserCredentials credentials, boolean initLayer){
        super(url, visiblelayers, credentials, initLayer);
        this.mLayerId = layerId;
    }

    public AGDynamicMapServiceLayer(int layerId, String url, int[] visiblelayers, int[] invisibleLegendLayers, UserCredentials credentials, boolean initLayer){
        super(url, visiblelayers, invisibleLegendLayers, credentials, initLayer);
        this.mLayerId = layerId;
    }

    public AGDynamicMapServiceLayer(int layerId,String url){
        super(url);
        this.mLayerId = layerId;
    }


    public AGDynamicMapServiceLayer(int layerId,String url, int[] visiblelayers, UserCredentials credentials){
        super(url, visiblelayers, credentials);
        this.mLayerId = layerId;
    }

    public AGDynamicMapServiceLayer(int layerId, String url, int[] visiblelayers, Map<Integer, DrawingInfo> drawingOptions, UserCredentials credentials){
        super(url, visiblelayers, drawingOptions, credentials);
        this.mLayerId = layerId;
    }

    public void setMapServiceInfo(MapServiceInfo mapServiceInfo){
        this.serviceInfo = mapServiceInfo;
    }

    public int getLayerId() {
        return mLayerId;
    }

    @Override
    public void initLayer() {
        try {
            this.getServiceExecutor().submit(new Runnable() {
                public void run() {
                    AGDynamicMapServiceLayer.super.initLayer();
                }
            });
        } catch (RejectedExecutionException var5) {
        }
    }
}
