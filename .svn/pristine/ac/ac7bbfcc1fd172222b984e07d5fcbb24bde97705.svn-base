package com.augurit.agmobile.mapengine.common.agmobilelayer;


import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ITileCacheHelper;
import com.esri.android.map.osm.OpenStreetMapLayer;


/**
 * 带有缓存功能的加载OpenStreetMap的类
 */
public class AGOpenStreetLayer extends OpenStreetMapLayer {
    /**
     * 处理缓存的类，通过实现buildOffLineCachePath方法可以自定义存储路径或者说存储规则，如果不提供BaseTileCachePathHelper，默认没有缓存功能
     */
    private ITileCacheHelper mCacheHelper;

    /*public IOffLineFileHelper getOffLineFileHelper() {
        return mOffLineFileHelper;
    }

    public void setOffLineFileHelper(IOffLineFileHelper offLineFileHelper) {
        mOffLineFileHelper = offLineFileHelper;
    }*/

    /**
     * 处理读取缓存地图图片的类，默认使用的直接保存和读取。如果想对文件进行加密后再存储，可以通过实现这个接口实现。
     */
    //    private IOffLineFileHelper mOffLineFileHelper;
    public AGOpenStreetLayer() {
        super(true);
    }

    public AGOpenStreetLayer(ITileCacheHelper cacheHelper) {
        super(true);
    }

    public AGOpenStreetLayer(boolean initLayer) {
        super(initLayer);
    }

    public AGOpenStreetLayer(boolean initLayer, ITileCacheHelper cacheHelper) {
        super(initLayer);
        this.mCacheHelper = cacheHelper;
    }

    public AGOpenStreetLayer(String[] tileServerURLs, int minZoomLevel, int maxZoomLevel, String attributionText, boolean initLayer) {
        super(tileServerURLs, minZoomLevel, maxZoomLevel, attributionText, initLayer);
    }

    public AGOpenStreetLayer(String[] tileServerURLs, int minZoomLevel, int maxZoomLevel, String attributionText, boolean initLayer, ITileCacheHelper cacheHelper) {
        super(tileServerURLs, minZoomLevel, maxZoomLevel, attributionText, initLayer);
        this.mCacheHelper = cacheHelper;
    }

    @Override
    protected byte[] getTile(int lev, int col, int row) throws Exception {
        if (this.mCacheHelper != null) {
            //构造存储路径
            /*String cachePath= mCachePathHelper.buildOffLineCachePath(lev, col, row);
            if (mOffLineFileHelper == null){
                mOffLineFileHelper = new AMFileHelper();
            }*/
            //读取缓存文件
            byte[] bytes = mCacheHelper.getOfflineCacheFile(lev, col, row);
            if (bytes == null) {
                bytes = super.getTile(lev, col, row);
                mCacheHelper.addOfflineCacheFile(bytes, lev, col, row);
            }
            return bytes;
        }
        //请求瓦片的URL地址
        // Log.d("Test",getUrl() + "/tile/" + level + "/" + col + "/" + row);
        return super.getTile(lev, col, row);
    }

    public void deleteAllCache() {
        if (mCacheHelper != null) {
            mCacheHelper.deleteAllCache();
        }
    }
}
