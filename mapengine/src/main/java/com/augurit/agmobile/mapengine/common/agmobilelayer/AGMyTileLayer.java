package com.augurit.agmobile.mapengine.common.agmobilelayer;


import android.content.Context;
import android.support.annotation.Keep;

import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ITileCacheHelper;
import com.augurit.agmobile.mapengine.layermanage.util.ExternalSDCardTileCachePath;
import com.augurit.am.fw.utils.ExternalSDCardFileUtil;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.ags.MapServiceInfo;
import com.esri.core.io.UserCredentials;

import org.geowebcache.arcgis.compact.ArcGISCompactCache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;


/**
 * Created by ac on 2016-06-21.
 */
@Keep
public class AGMyTileLayer extends AGTiledMapSeriviceLayer {

    public AGMyTileLayer(int layerId, String url, ITileCacheHelper cacheHelper, boolean initLayer){
        super(layerId, url, cacheHelper, initLayer);
    }


    @Override
    protected byte[] getTile(int level, int col, int row) throws Exception {
        if (getCurrentLevel() != level) {
            return null;
        }
//        LogUtil.d("MoreLevelTdtLayer", "getCurrentLevel()==" + getCurrentLevel() + ",level==" + level);
        byte[] originalBytes = super.getTile(level, col, row);
        if(originalBytes == null || originalBytes.length == 0){
            return null;
        }
        if (level < 12 - 1) {
            return originalBytes;
        }
        return null;

    }

    @Override
    public void initLayer() {
        try {
            this.getServiceExecutor().submit(new Runnable() {
                public void run() {
                    callArcGISTiledMapServiceLayerFuncInitLayer();
                }
            });
        } catch (RejectedExecutionException var5) {
        }
    }


    private ExecutorService serviceExecutor = null;
    protected ExecutorService getServiceExecutor() {
        if(serviceExecutor == null){
            serviceExecutor = Executors.newFixedThreadPool(2);
        }
        return serviceExecutor;
    }

    private ExecutorService poolExecutor = null;
    protected ExecutorService getPoolExecutor() {
        if(poolExecutor == null){
            poolExecutor = Executors.newFixedThreadPool(3);
        }
        return poolExecutor;
    }

}
