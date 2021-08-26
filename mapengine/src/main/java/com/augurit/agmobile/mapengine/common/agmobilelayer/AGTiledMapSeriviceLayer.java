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

import java.util.concurrent.RejectedExecutionException;


/**
 * Created by ac on 2016-06-21.
 */
@Keep
public class AGTiledMapSeriviceLayer extends ArcGISTiledMapServiceLayer {
    /**
     * 处理缓存的类，通过实现buildOffLineCachePath方法可以自定义存储路径或者说存储规则，如果不提供BaseTileCachePathHelper，默认没有缓存功能
     */
    protected ITileCacheHelper mCacheHelper;

    protected ExternalSDCardTileCachePath mExternalSDCardTileCachePath;
   /* */
    /**
     * 处理读取缓存地图图片的类，默认使用的直接保存和读取。如果想对文件进行加密后再存储，可以通过实现这个接口实现。
     *//*
    private IOffLineFileHelper mOffLineFileHelper;*/


    protected int mLayerId;

    protected Context mContext;

    protected ArcGISCompactCache compactCache;


    /*public IOffLineFileHelper getOffLineFileHelper() {
        return mOffLineFileHelper;
    }

    public void setOffLineFileHelper(IOffLineFileHelper offLineFileHelper) {
        mOffLineFileHelper = offLineFileHelper;
    }*/

    //如果采用这种方式，那么就是采用缓存的方式进行加载图层，否则就是不需要进行缓存图层
    public AGTiledMapSeriviceLayer(int layerId, String url, ITileCacheHelper cacheHelper) {
        super(url);
        this.mCacheHelper = cacheHelper;
        this.mLayerId = layerId;
        /*String externalSDCardPath = ExternalSDCardTileCachePath.getExternalStoragePath(mContext, true);
        String bundlePath = externalSDCardPath + "/" + "VECTOR_HD01_TDLYXZ_JZD" + "/v101/Layers/_alllayers";
        compactCache = new ArcGISCompactCacheV1(bundlePath);*/
    }

    //如果采用这种方式，那么就是采用缓存的方式进行加载图层，否则就是不需要进行缓存图层
    public AGTiledMapSeriviceLayer(int layerId, String url, ITileCacheHelper cacheHelper, boolean initLayer) {
        super(url, null, initLayer);
        this.mCacheHelper = cacheHelper;
        this.mLayerId = layerId;
    }

    //如果采用这种方式，那么就是采用缓存的方式进行加载图层，否则就是不需要进行缓存图层
    public AGTiledMapSeriviceLayer(int layerId, String url, ITileCacheHelper cacheHelper, boolean initLayer, Context context) {
        super(url, null, initLayer);
        this.mCacheHelper = cacheHelper;
        this.mLayerId = layerId;
        this.mContext = context;
        /*String externalSDCardPath = ExternalSDCardTileCachePath.getExternalStoragePath(mContext, true);
        String bundlePath = externalSDCardPath + "/" + "VECTOR_HD01_TDLYXZ_JZD" + "/v101/Layers/_alllayers";
        compactCache = new ArcGISCompactCacheV1(bundlePath);*/

    }

    public AGTiledMapSeriviceLayer(int layerId, String url) {
        super(url);
        this.mLayerId = layerId;
    }

    public AGTiledMapSeriviceLayer(int layerId, String url, UserCredentials credentials) {
        super(url, credentials);
        this.mLayerId = layerId;
    }

    public AGTiledMapSeriviceLayer(int layerId, String url, UserCredentials credentials, ITileCacheHelper cacheHelper) {
        super(url, credentials);
        this.mCacheHelper = cacheHelper;
        this.mLayerId = layerId;
    }

    public AGTiledMapSeriviceLayer(int layerId, String url, UserCredentials credentials, boolean initLayer) {
        super(url, credentials, initLayer);
        this.mLayerId = layerId;
    }

    public AGTiledMapSeriviceLayer(int layerId, String url, UserCredentials credentials, boolean initLayer, ITileCacheHelper cacheHelper) {
        super(url, credentials, initLayer);
        this.mCacheHelper = cacheHelper;
        this.mLayerId = layerId;
    }

    public int getLayerId() {
        return mLayerId;
    }

    @Override
    protected byte[] getTile(int level, int col, int row) throws Exception {
        /*if(true){
            Resource resource = compactCache.getBundleFileResource(level, row, col);
            InputStream is = resource.getInputStream();
            byte[] bytes = new byte[Integer.valueOf(String.valueOf(resource.getSize()))];
            int readed = is.read(bytes);
            if(readed > 0){
                return bytes;
            }
            return super.getTile(level,col,row);
        }*/
        if (this.mCacheHelper != null && mContext != null) {
            //构造外置SD卡的瓦片存储路径
            String externalCachePath = null;
            String externalEncryptCachePath = null;
            if (mExternalSDCardTileCachePath != null) {
                externalCachePath = mExternalSDCardTileCachePath.getFilePath(level, col, row);
                externalEncryptCachePath = mExternalSDCardTileCachePath.getEncryptFilePath(level, col, row);
            }
            byte[] externalBytes;
            try {
                externalBytes = ExternalSDCardFileUtil.readBytes(mContext, externalCachePath);
            } catch (Exception e) {
                externalBytes = null;
            }
            if (externalBytes != null) {
                ExternalSDCardFileUtil.encryptFile(mContext, externalBytes, externalEncryptCachePath);
                ExternalSDCardFileUtil.deleteFile(mContext, externalCachePath);
                //                AMFileUtil.deleteFile(new File(externalCachePath));
            } else {
                externalBytes = ExternalSDCardFileUtil.decryptFileToBytes(mContext, externalEncryptCachePath);
            }
            if (externalBytes != null) {
                return externalBytes;
            }
            //构造存储路径
            /*String cachePath = mCachePathHelper.buildOffLineCachePath(level, col, row);
            if (mOffLineFileHelper == null) {
                mOffLineFileHelper = new AMFileHelper2(mContext);
            }*/
            //读取缓存文件
            byte[] bytes = mCacheHelper.getOfflineCacheFile(level, col, row);
            if (bytes == null) {
                bytes = super.getTile(level, col, row);
                mCacheHelper.addOfflineCacheFile(bytes, level, col, row);
            }
            return bytes;
        } else {
            return super.getTile(level, col, row);
        }
        //请求瓦片的URL地址
        // Log.d("Test",getUrl() + "/tile/" + level + "/" + col + "/" + row);

    }

    public void deleteAllCache() {
        if (mCacheHelper != null) {
            mCacheHelper.deleteAllCache();
        }
    }

    public void setMapServiceInfo(MapServiceInfo mapServiceInfo) {
        this.serviceInfo = mapServiceInfo;
    }

    public void setExternalSDCardTileCachePath(ExternalSDCardTileCachePath externalSDCardTileCachePath) {
        this.mExternalSDCardTileCachePath = externalSDCardTileCachePath;
    }

    @Override
    public void initLayer() {
        try {
            this.getServiceExecutor().submit(new Runnable() {
                public void run() {
                    AGTiledMapSeriviceLayer.super.initLayer();
                }
            });
        } catch (RejectedExecutionException var5) {
        }
    }

    public void callArcGISTiledMapServiceLayerFuncInitLayer(){
        AGTiledMapSeriviceLayer.super.initLayer();
    }
}
