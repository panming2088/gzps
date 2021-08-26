package com.augurit.agmobile.mapengine.layermanage.util;


import android.content.Context;
import android.support.annotation.NonNull;

import com.augurit.agmobile.mapengine.common.agmobilelayer.AGTiledMapSeriviceLayer;
import com.augurit.agmobile.mapengine.common.agmobilelayer.TdtLayer;
import com.augurit.agmobile.mapengine.common.agmobilelayer.TileLayer;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.XORDecryptTileCacheReader;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.agmobile.mapengine.common.utils.GeodatabaseAndShapeFileManagerFactory;
import com.augurit.agmobile.mapengine.common.utils.ShapefileManager;
import com.augurit.agmobile.mapengine.layermanage.dao.LocalLayerStorageDao;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.am.fw.log.util.NetUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.Layer;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.core.ags.MapServiceInfo;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 生成对应的layer
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.layer.util
 * @createTime 创建时间 ：2016-10-14 14:00
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2016-10-14 14:00
 */
public class LayerFactory implements ILayerFactory {


    @NonNull
    private static Layer getTdtLayer(Context context, int layerId, String url) {
        Layer layer;
        FilePathUtil savePathManager2 = new FilePathUtil(context);
        String savePath2 = savePathManager2.getSavePath();
        layer = new TdtLayer(String.valueOf(layerId), url,
                new NonEncryptTileCacheHelper(context, String.valueOf(layerId)), true);
        return layer;
    }

    private static boolean urlIsReachable(final String url) {

        FutureTask<Boolean> futureTask = FutureThreadPool.getInstance().executeTask(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return isConnect(url);
            }
        });
        try {
            return futureTask.get(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 功能：检测当前URL是否可连接或是否有效,
     * 描述：最多连接网络 3 次, 如果 3 次都不成功，视为该地址不可用
     *
     * @param urlStr 指定URL网络地址
     * @return URL
     */
    public static synchronized boolean isConnect(String urlStr) {
        int counts = 0;
        if (urlStr == null || urlStr.length() <= 0) {
            return false;
        }
        while (counts < 3) {
            //    long start = 0;
            try {
                URL url = new URL(urlStr);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                int state = con.getResponseCode();
                if (state == 200) {
                    return true;
                }
                break;
            } catch (Exception ex) {
                counts++;
                LogUtil.d("连接第 " + counts + " 次，" + urlStr + "--不可用");
                continue;
            }
        }
        return false;
    }

    /**
     * 图层URL是否可加载
     * @return
     */
    public boolean isLoadable(String url){
        if(url.contains("MapServer")
                || url.contains("FeatureServer")){
            return true;
        }
        return false;
    }

    @Override
    public Layer getLayer(Context context, LayerInfo layerInfo) {

        Layer layer = null;
        int layerId = layerInfo.getLayerId();
        String layerTable = layerInfo.getLayerTable();
        String url = layerInfo.getUrl();
        String currentProjectId = ProjectDataManager.getInstance().getCurrentProjectId(context);
        //   String localDirName = layerInfo.getLocalDirName();
        boolean ifNetworkAvailable = NetUtil.isConnected(context);
        switch (layerInfo.getType()) {
            case FeatureServer:
                String bundlePath2 = new FilePathUtil(context).getSavePath() + "/tile/" + layerInfo.getLayerName() + "/Layers";
                if (new File(bundlePath2).exists()) {
                    try {
                        XORDecryptTileCacheReader tileCacheReader = new XORDecryptTileCacheReader(bundlePath2);
                        layer = new TileLayer(bundlePath2, tileCacheReader);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (layer == null) { //featureServer可能配成geodatabase存放在本地
                    layer = GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getGeodatabaseManager(context).getFeatureLayer(layerTable); //从geodatabase查询该图层
                }

                if (layer == null) {
                    layer = ShapefileManager.getFeatureLayer(context, layerTable); //从shapeFile文件读取
                }
                boolean isCannectable = urlIsReachable(url.replace("FeatureServer", "MapServer"));
                if (layer == null && ifNetworkAvailable && isCannectable && isLoadable(url)) { //只有在线状态才加载动态图层
                    layer = new ArcGISDynamicMapServiceLayer(url.replace("FeatureServer", "MapServer"));
                }
                break;

            case DynamicLayer:
                String bundlePath = new FilePathUtil(context).getSavePath() + "/tile/" + layerInfo.getLayerName() + "/Layers";
                if (new File(bundlePath).exists()) {
                    try {
                        XORDecryptTileCacheReader tileCacheReader = new XORDecryptTileCacheReader(bundlePath);
                        layer = new TileLayer(bundlePath, tileCacheReader);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                boolean isCannectableD = urlIsReachable(url.replace("FeatureServer", "MapServer"));
                if (layer == null && ifNetworkAvailable && isCannectableD && isLoadable(url)) { //只有在线状态才加载动态图层
                    layer = new ArcGISDynamicMapServiceLayer(url.replace("FeatureServer", "MapServer"));
                }
              /*  MapServiceInfo dynamicServiceInfo = LocalLayerStorageDao.getMapServiceInfo(context, layerId, currentProjectId, url);
                if (dynamicServiceInfo != null){
                    ((AGDynamicMapServiceLayer)layer).setMapServiceInfo(dynamicServiceInfo);
                }
                ((AGDynamicMapServiceLayer)layer).initLayer();*/
                break;
            case TileLayer:
                String externalSDCardPath = ExternalSDCardTileCachePath.getExternalStoragePath(context, true);
                String tpkPath = externalSDCardPath + "/" + layerTable + ".tpk";
                String prefixTpkPath = externalSDCardPath + "/VECTOR_" + layerTable + ".tpk";
                bundlePath = externalSDCardPath + "/" + layerTable + "/v101/Layers";
                String prefixBundlePath = externalSDCardPath + "/VECTOR_" + layerTable + "/v101/Layers";
                String tilePath = null;
                if (externalSDCardPath != null) {
                    if (new File(tpkPath).exists()) {
                        tilePath = tpkPath;
                    } else if (new File(prefixTpkPath).exists()) {
                        tilePath = prefixTpkPath;
                    } else if (new File(bundlePath).exists()) {
                        tilePath = bundlePath;
                    } else if (new File(prefixBundlePath).exists()) {
                        tilePath = prefixBundlePath;
                    }
                }
                /*String extSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                String qpPath;
                if (extSDCardPath != null && !TextUtils.isEmpty(localDirName)) {
                    qpPath = extSDCardPath + File.separator + "/AGMobile/tile/" + localDirName + "/Layers";
                    if (new File(qpPath).exists()) {
                        tilePath = qpPath;
                    }
                }*/
                bundlePath = new FilePathUtil(context).getSavePath() + "/tile/" + layerInfo.getLayerName() + "/Layers";
                if (tilePath != null) {
                    ArcGISLocalTiledLayer arcGISLocalTiledLayer = new ArcGISLocalTiledLayer(tilePath);
                    layer = arcGISLocalTiledLayer;
                } else if (new File(bundlePath).exists()) {
                    try {
                        XORDecryptTileCacheReader tileCacheReader = new XORDecryptTileCacheReader(bundlePath);
                        layer = new TileLayer(bundlePath, tileCacheReader);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    boolean isConnectableTile = urlIsReachable(url.replace("FeatureServer", "MapServer"));
                    if(isConnectableTile && isLoadable(url)) {
                        AGTiledMapSeriviceLayer agTiledMapSeriviceLayer
                                = new AGTiledMapSeriviceLayer(layerId, url.replace("FeatureServer", "MapServer"),
                                new EncryptTileCacheHelper(context, String.valueOf(layerId)), false, context);
                        agTiledMapSeriviceLayer.setExternalSDCardTileCachePath(
                                new ExternalSDCardTileCachePath(context, String.valueOf(layerId), layerTable));
                        layer = agTiledMapSeriviceLayer;
                        MapServiceInfo mapServiceInfo = LocalLayerStorageDao.getMapServiceInfo(context, layerId, currentProjectId, url);
                        if (mapServiceInfo != null) {
                            ((AGTiledMapSeriviceLayer) layer).setMapServiceInfo(mapServiceInfo);
                        }
                        ((AGTiledMapSeriviceLayer) layer).initLayer();
                    }
                }
                break;

            case WFSLayer:
            case FeatureLayer:

                layer = ShapefileManager.getFeatureLayer(context, layerTable); //从shapeFile文件读取

                if (layer == null) {
                    layer = GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getGeodatabaseManager(context).getFeatureLayer(layerTable); //从geodatabase查询该图层
                }
                boolean isCannectableFeatureLayer = urlIsReachable(url);
                if (layer == null && ifNetworkAvailable && isCannectableFeatureLayer && isLoadable(url)) {
                    layer = new ArcGISFeatureLayer(url, null, ArcGISFeatureLayer.MODE.SNAPSHOT);//在线加载
                }

                break;
            case WMSLayer:
                break;
            case TianDiTu:
                /*String capabilityUrl = url + TdtLayer.REQUEST_URL;
                //首先判断本地有无文件
                String result = null;
                try {
                    result = TdtLayer.getCapabilitiesFromLocal("tianditu/" + layerId);
                    if (TextUtils.isEmpty(result)){ // //如果本地没有capability文件
                        //检查网络是否可达并且url可以访问，否则不加载
                        if (ifNetworkAvailable && urlIsReachable(url) ){
                            layer = getTdtLayer(context, layerId, url);
                        }else {
                            //打印Log提醒
                            LogUtil.d("天地图："+ url +"的配置文件无法通过"+capabilityUrl+"获取，请检查该图层类型是否配置出错");
                        }
                    }else { //如果本地有capability文件，直接加载
                        layer = getTdtLayer(context, layerId, url);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //如果读取失败，要检查网络是否可达
                    if (ifNetworkAvailable && urlIsReachable(url)){
                        layer = getTdtLayer(context, layerId, url);
                    }else {
                        //打印Log提醒
                        LogUtil.d("天地图："+ url +"的配置文件无法通过"+capabilityUrl+"获取，请检查该图层类型是否配置出错");
                    }
                }*/
                //((TdtLayer)layer).setShowLog(true);
                layer = getTdtLayer(context, layerId, url);
                break;
            case WMTSLayer:
                break;
        }
        return layer;
    }


    /**
     * 新建图层实例
     * @param context
     * @param layerInfo
     * @return
     */
   /* @Override
    public  Layer getLayer(Context context, LayerInfo layerInfo) {
        Layer layer = null;
        int layerId = layerInfo.getLayerId();
        String layerTable = layerInfo.getLayerTable();
        String url = layerInfo.getUrl();
        String currentProjectId = ProjectDataManager.getInstance().getCurrentProjectId(context);
       // String localDirName = layerInfo.getLocalDirName();
        boolean ifNetworkAvailable = NetUtil.isConnected(context);
        switch (layerInfo.getType()) {
            case FeatureServer:
                String bundlePath2 = new FilePathUtil(context).getSavePath() + "/tile/" + layerInfo.getLayerName() + "/Layers";
                if(new File(bundlePath2).exists()){
                    try {
                        XORDecryptTileCacheReader tileCacheReader = new XORDecryptTileCacheReader(bundlePath2);
                        layer = new TileLayer(bundlePath2, tileCacheReader);
                    } catch (Exception e ){
                        e.printStackTrace();
                    }
                }

                if (layer == null){ //featureServer可能配成geodatabase存放在本地
                    layer = GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getGeodatabaseManager(context).getFeatureLayer(layerTable); //从geodatabase查询该图层
                }

               if (layer == null){
                    layer = ShapefileManager.getFeatureLayer(context,layerTable); //从shapeFile文件读取
                }

                if (layer == null && ifNetworkAvailable){ //只有在线状态才加载动态图层
                    layer = new ArcGISDynamicMapServiceLayer(url.replace("FeatureServer", "MapServer"));
                }
                break;

            case DynamicLayer:
                String bundlePath = new FilePathUtil(context).getSavePath() + "/tile/" + layerInfo.getLayerName() + "/Layers";
                if(new File(bundlePath).exists()){
                    try {
                        XORDecryptTileCacheReader tileCacheReader = new XORDecryptTileCacheReader(bundlePath);
                        layer = new TileLayer(bundlePath, tileCacheReader);
                    } catch (Exception e ){
                        e.printStackTrace();
                    }
                }

                if (layer == null && ifNetworkAvailable){ //只有在线状态才加载动态图层
                    layer = new ArcGISDynamicMapServiceLayer(url.replace("FeatureServer", "MapServer"));
                    }
              *//*  MapServiceInfo dynamicServiceInfo = LocalLayerStorageDao.getMapServiceInfo(context, layerId, currentProjectId, url);
                if (dynamicServiceInfo != null){
                    ((AGDynamicMapServiceLayer)layer).setMapServiceInfo(dynamicServiceInfo);
                }
                ((AGDynamicMapServiceLayer)layer).initLayer();*//*
                break;
            case TileLayer:
                String externalSDCardPath = ExternalSDCardTileCachePath.getExternalStoragePath(context, true);
                String tpkPath = externalSDCardPath + "/" + layerTable + ".tpk";
                String prefixTpkPath = externalSDCardPath + "/VECTOR_" + layerTable + ".tpk";
                bundlePath = externalSDCardPath + "/" + layerTable + "/v101/Layers";
                String prefixBundlePath = externalSDCardPath + "/VECTOR_" + layerTable + "/v101/Layers";
                String tilePath = null;
                if(externalSDCardPath != null){
                    if(new File(tpkPath).exists()){
                        tilePath = tpkPath;
                    } else if(new File(prefixTpkPath).exists()){
                        tilePath = prefixTpkPath;
                    } else if(new File(bundlePath).exists()){
                        tilePath = bundlePath;
                    } else if(new File(prefixBundlePath).exists()){
                        tilePath = prefixBundlePath;
                    }
                }
                *//*String extSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                String qpPath;
                if (extSDCardPath != null && !TextUtils.isEmpty(localDirName)) {
                    qpPath = extSDCardPath + File.separator + "/AGMobile/tile/" + localDirName + "/Layers";
                    if (new File(qpPath).exists()) {
                        tilePath = qpPath;
                    }
                }*//*
                bundlePath = new FilePathUtil(context).getSavePath() + "/tile/" + layerInfo.getLayerName() + "/Layers";
                if (tilePath != null) {
                    ArcGISLocalTiledLayer arcGISLocalTiledLayer = new ArcGISLocalTiledLayer(tilePath);
                    layer = arcGISLocalTiledLayer;
                } else if(new File(bundlePath).exists()){
                    try {
                        XORDecryptTileCacheReader tileCacheReader = new XORDecryptTileCacheReader(bundlePath);
                        layer = new TileLayer(bundlePath, tileCacheReader);
                    } catch (Exception e ){
                        e.printStackTrace();
                    }
                } else {
                    AGTiledMapSeriviceLayer agTiledMapSeriviceLayer
                            = new AGTiledMapSeriviceLayer(layerId, url.replace("FeatureServer", "MapServer"),
                                    new EncryptTileCacheHelper(context, String.valueOf(layerId)), false, context);
                    agTiledMapSeriviceLayer.setExternalSDCardTileCachePath(
                            new ExternalSDCardTileCachePath(context, String.valueOf(layerId), layerTable));
                    layer = agTiledMapSeriviceLayer;
                    MapServiceInfo mapServiceInfo = LocalLayerStorageDao.getMapServiceInfo(context, layerId, currentProjectId, url);
                    if (mapServiceInfo != null) {
                        ((AGTiledMapSeriviceLayer) layer).setMapServiceInfo(mapServiceInfo);
                    }
                    ((AGTiledMapSeriviceLayer) layer).initLayer();
                }
                break;

            case WFSLayer:
            case FeatureLayer:
                layer = GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getGeodatabaseManager(context).getFeatureLayer(layerTable); //从geodatabase查询该图层

                if (layer == null){
                  layer = ShapefileManager.getFeatureLayer(context,layerTable); //从shapeFile文件读取
                }

                if (layer == null && ifNetworkAvailable){
                    layer = new ArcGISFeatureLayer(url, null, ArcGISFeatureLayer.MODE.SNAPSHOT);//在线加载
                }

                break;
            case WMSLayer:
                String wmsBundlePath = new FilePathUtil(context).getSavePath() + "/tile/" + layerInfo.getLayerName() + "/Layers";
                if (new File(wmsBundlePath).exists()) {
                    try {
                        XORDecryptTileCacheReader tileCacheReader = new XORDecryptTileCacheReader(wmsBundlePath);
                        layer = new TileLayer(wmsBundlePath, tileCacheReader);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (layer == null && ifNetworkAvailable) {
                    layer = new WMSLayer(url);
                }
                break;
            case TianDiTu:
                layer = getTdtLayer(context,layerId,url);
                //((TdtLayer)layer).setShowLog(true);
                break;
            case WMTSLayer:
                break;
        }
        return layer;
    }

    @NonNull
    private static Layer getTdtLayer(Context context, int layerId, String url) {
        Layer layer;
        FilePathUtil savePathManager2 = new FilePathUtil(context);
        String savePath2 = savePathManager2.getSavePath();
        layer = new TdtLayer(context, String.valueOf(layerId), url,
                "tianditu/" + layerId, false, new TiandiTuDownloader());
        return layer;
    }*/

}
