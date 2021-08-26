package com.augurit.agmobile.mapengine.layerquery.service;

import android.content.Context;


import com.augurit.agmobile.mapengine.common.utils.GeodatabaseAndShapeFileManagerFactory;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layerquery.NoQueryableLayerException;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.log.util.NetUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.esri.android.map.MapView;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Field;
import com.esri.core.table.FeatureTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerquery
 * @createTime 创建时间 ：2017-04-18
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-18
 * @modifyMemo 修改备注：
 */

public class CombinedOfflineAndArcgisLayerQueryService extends LayerQueryService{

    private AtomicInteger queryTime = new AtomicInteger(0); //查询次数

    @Override
    public void queryLayer(Context context, String seachKey,
                           SpatialReference spatialReference,
                           Geometry geometry,
                           List<LayerInfo> queryLayers,
                           int maxCount,
                           final Callback2<List<AMFindResult>> callback) {

        if (ValidateUtil.isListNull(queryLayers)) {
            callback.onFail(new NoQueryableLayerException("无可查询图层"));
        }
        final List<AMFindResult> finalResult = new ArrayList<>();
        List<LayerInfo> onlineLayer = new ArrayList<>(); //在线查询的图层
        List<LayerInfo> offLineLayer = new ArrayList<>(); //离线查询的图层
        for (LayerInfo layerInfo : queryLayers) {
            FeatureTable geodatabaseFeatureTable = GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getTable(context,layerInfo.getLayerTable());
            if (geodatabaseFeatureTable == null) {
                onlineLayer.add(layerInfo); //在线
            }else {
                offLineLayer.add(layerInfo); //离线
            }
        }
        queryTime.set(0);

        CombineGeodatabaseAndShapeFileLayerQueryService offlineLayerQueryService = new CombineGeodatabaseAndShapeFileLayerQueryService();
        offlineLayerQueryService.queryLayer(context, seachKey, spatialReference, geometry, offLineLayer, 100, new Callback2<List<AMFindResult>>() {
            @Override
            public void onSuccess(List<AMFindResult> findResults) {
                finalResult.addAll(findResults);
                queryTime.addAndGet(1);
                checkAndReturnResult(finalResult,callback);
            }

            @Override
            public void onFail(Exception error) {
                queryTime.addAndGet(1);
                checkAndReturnResult(finalResult,callback);
            }
        });

        if (NetUtil.isConnected(context) && !ValidateUtil.isListNull(onlineLayer)){ //如果网络可达
            LayerQueryService layerQueryService = new LayerQueryService();
            layerQueryService.queryLayer(context, seachKey, spatialReference, geometry, onlineLayer, 100, new Callback2<List<AMFindResult>>() {
                @Override
                public void onSuccess(List<AMFindResult> findResults) {
                    finalResult.addAll(findResults);
                    queryTime.addAndGet(1);
                    checkAndReturnResult(finalResult,callback);
                }

                @Override
                public void onFail(Exception error) {
                    queryTime.addAndGet(1);
                    checkAndReturnResult(finalResult,callback);
                }
            });
        }else { //如果网络不可达
            queryTime.addAndGet(1);
            checkAndReturnResult(finalResult,callback);
        }

    }

    private void checkAndReturnResult(List<AMFindResult> findResults,
                                      Callback2<List<AMFindResult>> callback) {
        if (queryTime.get() == 2){
            callback.onSuccess(findResults);
        }
    }

    @Override
    public void queryLayer(Context context, String seachKey, MapView mapView, Geometry geometry, List<LayerInfo> queryLayers, int maxCount, Callback2<List<AMFindResult>> callback) {
        queryLayer(context,seachKey,mapView.getSpatialReference(),geometry,queryLayers,maxCount,callback);
    }

    @Override
    public void queryLayer(Context context,
                           String seachKey,
                           String fieldName,
                           MapView mapView,
                           Geometry geometry,
                           List<LayerInfo> queryLayers,
                           int maxCount,
                           final Callback2<List<AMFindResult>> callback) {
        if (ValidateUtil.isListNull(queryLayers)) {
            callback.onSuccess(new ArrayList<AMFindResult>());
        }
        final List<AMFindResult> finalResult = new ArrayList<>();
        List<LayerInfo> onlineLayer = new ArrayList<>(); //在线查询的图层
        List<LayerInfo> offLineLayer = new ArrayList<>(); //离线查询的图层
        for (LayerInfo layerInfo : queryLayers) {
            GeodatabaseFeatureTable geodatabaseFeatureTable = GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getGeodatabaseManager(context).getGeodatabaseFeatureTable(layerInfo.getLayerTable());
            if (geodatabaseFeatureTable == null) {
                onlineLayer.add(layerInfo); //在线
            }else {
                offLineLayer.add(layerInfo); //离线
            }
        }
        queryTime.set(0);

        OfflineLayerQueryService offlineLayerQueryService = new OfflineLayerQueryService();
        offlineLayerQueryService.queryLayer(context, seachKey,fieldName, mapView, geometry, offLineLayer, 100, new Callback2<List<AMFindResult>>() {
            @Override
            public void onSuccess(List<AMFindResult> findResults) {
                finalResult.addAll(findResults);
                queryTime.addAndGet(1);
                checkAndReturnResult(finalResult,callback);
            }

            @Override
            public void onFail(Exception error) {
                queryTime.addAndGet(1);
                checkAndReturnResult(finalResult,callback);
            }
        });

        if (NetUtil.isConnected(context)){ //如果网络可达
            LayerQueryService layerQueryService = new LayerQueryService();
            layerQueryService.queryLayer(context, seachKey,fieldName, mapView, geometry, onlineLayer, 100, new Callback2<List<AMFindResult>>() {
                @Override
                public void onSuccess(List<AMFindResult> findResults) {
                    finalResult.addAll(findResults);
                    queryTime.addAndGet(1);
                    checkAndReturnResult(finalResult,callback);
                }

                @Override
                public void onFail(Exception error) {
                    queryTime.addAndGet(1);
                    checkAndReturnResult(finalResult,callback);
                }
            });
        }else { //如果网络不可达
            queryTime.addAndGet(1);
            checkAndReturnResult(finalResult,callback);
        }
    }

    @Override
    public List<String> getQueryHistory() {
        return new LayerQueryService().getQueryHistory();
    }

    @Override
    public Observable<List<LayerInfo>> getQueryableLayers(Context context) throws IOException {
        if (NetUtil.isConnected(context)){ //如果网络通畅
            return new LayerQueryService().getQueryableLayers(context);
        }
        return new OfflineLayerQueryService().getQueryableLayers(context);
    }

    @Override
    public List<Field> getAllFields(Context context, LayerInfo layerInfo) {
        return new OfflineLayerQueryService().getAllFields(context,layerInfo);
    }
}
