package com.augurit.agmobile.mapengine.layerdownload.service;

import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ITileCacheHelper;
import com.augurit.agmobile.mapengine.layerdownload.model.Tile;
import com.augurit.agmobile.mapengine.layerdownload.router.LayerDownloadRouter;
import com.augurit.agmobile.mapengine.layerdownload.view.OnLayerDownloadListener;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.core.ags.LOD;
import com.esri.core.ags.MapServiceInfo;
import com.esri.core.ags.TileInfo;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 图层下载业务逻辑
 * @author 创建人 ：liangshenghong
 * @package 包名 ：com.augurit.agmobile.mapengine.layerdownload.service
 * @createTime 创建时间 ：2017-02-10
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-02-10
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public class LayerDownloadService implements ILayerDownloadService {

    private LayerDownloadRouter mRouter;

    public LayerDownloadService() {
        mRouter = new LayerDownloadRouter();
    }

    /**
     * 根据矩形范围计算所有level的瓦片
     *
     * @param tileInfo 瓦片信息
     * @param envelope 矩形范围
     * @return 计算得到的瓦片集合
     */
    @Override
    public ArrayList<Tile> calculateTile(TileInfo tileInfo, Envelope envelope) {
        ArrayList<Tile> tiles = new ArrayList<>();
        //        int i = 0;
        //循环计算每个level
        for (LOD lod : tileInfo.getLODs()) {
            tiles.addAll(calculateEachLevel(tileInfo, lod, envelope));
        }
        return tiles;
    }

    /**
     * 下载瓦片图层
     * @param serviceUrl 图层Url
     * @param extent 下载范围
     * @param cacheHelper 缓存构建
     * @param listener 下载监听
     */
    @Override
    public void downloadTiles(final String serviceUrl,
                              final Envelope extent,
                              final ITileCacheHelper cacheHelper,
                              final OnLayerDownloadListener listener) {
        final double[] progress = new double[3];
        Observable.just(1)
                .map(new Func1<Integer, MapServiceInfo>() {
                    @Override
                    public MapServiceInfo call(Integer integer) {
                        // 获取图层对应MapServiceInfo
                        try {
                            return mRouter.fetchMapServiceInfo(serviceUrl);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }).flatMap(new Func1<MapServiceInfo, Observable<Tile>>() {
                    @Override
                    public Observable<Tile> call(MapServiceInfo serviceInfo) {
                        // 计算要下载的瓦片
                        ArrayList<Tile> tiles = calculateTile(serviceInfo.getTileInfo(), extent);
                        progress[0] = tiles.size();
                        return Observable.from(tiles);
                    }
                }).map(new Func1<Tile, Integer>() {
                    @Override
                    public Integer call(Tile tile) {
                        // 逐个下载
                        try {
                            return mRouter.downloadTile(serviceUrl, tile, cacheHelper);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        listener.onSuccess(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("Download Error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        progress[1] += integer;
                        listener.onDownloading(progress[0], progress[1]);
                    }
                });
    }

    /**
     * 下载FeatureLayer
     * @param serviceUrl 图层Url
     * @param savePath 保存路径
     * @param layerId 图层Id
     * @param extent 下载范围
     * @param outSr SpatialReference
     * @param listener 下载监听
     */
    @Override
    public void downloadFeatures(String serviceUrl,
                                 String savePath,
                                 int layerId,
                                 Envelope extent,
                                 SpatialReference outSr,
                                 OnLayerDownloadListener listener) {
        String cachePath = savePath+ "/feature/layer" + layerId + ".gdb";
        mRouter.downloadFeatures(serviceUrl, cachePath, extent, outSr, listener);
    }

    /**
     * 根据矩形范围计算单个level的瓦片
     *
     * 瓦片行列号换算原理
     * 假设，地图切图的原点是（x0,y0），地图的瓦片大小是tileSize，
     * 地图屏幕上1像素代表的实际距离是resolution。计算坐标点（x,y）所在的瓦片的行列号的公式是：
     * col  = floor((x0 - x)/( tileSize*resolution))
     * row = floor((y0 - y)/( tileSize*resolution))
     *
     * @param tileInfo 瓦片信息
     * @param lod      level信息
     * @param envelope 矩形范围
     * @return 计算得到的瓦片集合
     */
    private ArrayList<Tile> calculateEachLevel(TileInfo tileInfo, LOD lod, Envelope envelope) {
        ArrayList<Tile> tiles = new ArrayList<>();
        //计算
        int minRow = Double.valueOf(Math.floor(Math.abs(tileInfo.getOrigin().getY() - envelope.getYMin()) / (tileInfo.getRows() * lod.getResolution()))).intValue();
        int minCol = Double.valueOf(Math.floor(Math.abs(tileInfo.getOrigin().getX() - envelope.getXMin()) / (tileInfo.getCols() * lod.getResolution()))).intValue();
        int maxRow = Double.valueOf(Math.floor(Math.abs(tileInfo.getOrigin().getY() - envelope.getYMax()) / (tileInfo.getRows() * lod.getResolution()))).intValue();
        int maxCol = Double.valueOf(Math.floor(Math.abs(tileInfo.getOrigin().getX() - envelope.getXMax()) / (tileInfo.getCols() * lod.getResolution()))).intValue();
        int temp = 0;
        if (minRow > maxRow) {
            temp = minRow;
            minRow = maxRow;
            maxRow = temp;
        }
        if (minCol > maxCol) {
            temp = minCol;
            minRow = maxRow;
            maxCol = temp;
        }
        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minCol; j <= maxCol; j++) {
                Tile tile = new Tile(lod.getLevel(), i, j);
                tiles.add(tile);
            }
        }
        return tiles;
    }
}
