package com.augurit.agmobile.mapengine.layerdownload.router;

import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ITileCacheHelper;
import com.augurit.agmobile.mapengine.layerdownload.dao.FeatureLayerDownloadDao;
import com.augurit.agmobile.mapengine.layerdownload.dao.LocalLayerDownloadSQLiteDao;
import com.augurit.agmobile.mapengine.layerdownload.dao.TiledLayerDownloadDao;
import com.augurit.agmobile.mapengine.layerdownload.model.Tile;
import com.augurit.agmobile.mapengine.layerdownload.view.OnLayerDownloadListener;
import com.esri.core.ags.MapServiceInfo;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;

/**
 * 描述：图层下载Router
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerdownload.router
 * @createTime 创建时间 ：2017-02-14
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-14
 * @modifyMemo 修改备注：
 */
public class LayerDownloadRouter {

    private LocalLayerDownloadSQLiteDao mLocalDao;
    private TiledLayerDownloadDao mTiledDownloader;
    private FeatureLayerDownloadDao mFeatureDownloader;

    public LayerDownloadRouter() {
        mLocalDao = new LocalLayerDownloadSQLiteDao();
        mTiledDownloader = new TiledLayerDownloadDao();
        mFeatureDownloader = new FeatureLayerDownloadDao();
    }

    /**
     * 获取MapServiceInfo
     * @param serviceUrl 图层Url
     * @return MapServiceInfo
     */
    public MapServiceInfo fetchMapServiceInfo(String serviceUrl) throws Exception {
        return mTiledDownloader.fetchMapServiceInfo(serviceUrl);
    }

    /**
     * 下载瓦片
     * @param serviceUrl 图层Url
     * @param tile  瓦片
     * @param cacheHelper 缓存构建
     * @return
     * @throws Exception
     */
    public int downloadTile(String serviceUrl,
                            Tile tile,
                            ITileCacheHelper cacheHelper) throws Exception {
        return mTiledDownloader.downloadTile(serviceUrl, tile, cacheHelper);
    }

    /**
     * 下载Feature
     * @param serviceUrl 图层Url
     * @param cachePath 缓存路径（包含文件名）
     * @param extent 下载区域
     * @param outSr SpatialReference
     * @param listener 下载监听
     */
    public void downloadFeatures(String serviceUrl,
                     String cachePath,
                     Envelope extent,
                     SpatialReference outSr,
                     OnLayerDownloadListener listener) {
        mFeatureDownloader.downloadFeatures(serviceUrl, cachePath, extent, outSr, listener);
    }
}
