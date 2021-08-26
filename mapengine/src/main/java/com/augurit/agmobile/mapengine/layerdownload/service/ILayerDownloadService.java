package com.augurit.agmobile.mapengine.layerdownload.service;

import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ITileCacheHelper;
import com.augurit.agmobile.mapengine.layerdownload.model.Tile;
import com.augurit.agmobile.mapengine.layerdownload.view.OnLayerDownloadListener;
import com.esri.core.ags.TileInfo;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;

import java.util.ArrayList;

/**
 * 图层下载业务逻辑接口
 * @author 创建人 ：liangshenghong
 * @package 包名 ：com.augurit.agmobile.mapengine.layerdownload.service
 * @createTime 创建时间 ：2017-02-10
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-02-10
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public interface ILayerDownloadService {

    /**
     * 根据矩形范围计算所有level的瓦片
     *
     * @param tileInfo 瓦片信息
     * @param envelope 矩形范围
     * @return 计算得到的瓦片集合
     */
    ArrayList<Tile> calculateTile(TileInfo tileInfo, Envelope envelope);

    /**
     * 下载瓦片图层
     * @param serviceUrl 图层Url
     * @param extent 下载范围
     * @param cacheHelper 缓存构建
     * @param listener 下载监听
     */
    void downloadTiles(final String serviceUrl,
                              final Envelope extent,
                              final ITileCacheHelper cacheHelper,
                              final OnLayerDownloadListener listener);

    /**
     * 下载矢量图层
     * @param serviceUrl 图层Url
     * @param savePath 保存路径
     * @param layerId 图层Id
     * @param extent 下载范围
     * @param outSr SpatialReference
     * @param listener 下载监听
     */
    void downloadFeatures(String serviceUrl,
                                 String savePath,
                                 int layerId,
                                 Envelope extent,
                                 SpatialReference outSr,
                                 OnLayerDownloadListener listener);
}
