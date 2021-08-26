package com.augurit.agmobile.mapengine.layerquery.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.utils.GeodatabaseAndShapeFileManagerFactory;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.layerquery.NoQueryableLayerException;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapView;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Field;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 查询Geodatabase
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerquery.service
 * @createTime 创建时间 ：2017-04-10
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-10
 * @modifyMemo 修改备注：
 */

public class OfflineLayerQueryService extends LayerQueryService {

    /**
     * 查询图层
     * @param context 上下文
     * @param searchKey 关键字
     * @param mapView mapview
     * @param geometry 要查询的范围，可以为Null,为null默认查询全范围
     * @param queryLayers 要查询的图层
     * @param maxCount 最多返回数目
     * @param callback 回调
     */
    @Override
    public void queryLayer(final Context context,
                           String searchKey,
                           MapView mapView,
                           Geometry geometry,
                           List<LayerInfo> queryLayers,
                           int maxCount,
                           final Callback2<List<AMFindResult>> callback) {

        queryLayer(context,searchKey,mapView.getSpatialReference(),geometry,queryLayers,maxCount,callback);
    }


    /**
     * 获取当前可见可查询图层
     * @param context
     * @return 可见可查询图层
     * @throws IOException
     */
    @Override
    public Observable<List<LayerInfo>> getQueryableLayers(final Context context) throws IOException {
        ILayersService layersService = LayerServiceFactory.provideLayerService(context);
        return layersService.getSortedLayerInfos()
                .map(new Func1<List<LayerInfo>, List<LayerInfo>>() {
            @Override
            public List<LayerInfo> call(List<LayerInfo> layerInfos) {
                List<LayerInfo> queryableLayers = new ArrayList<LayerInfo>();
                for (LayerInfo layerInfo : layerInfos){
                    if (layerInfo.isQueryable()){
                        queryableLayers.add(layerInfo);
                    }
                }
                return queryableLayers;
            }
        });
    }

    /**
     * 查询图层的特定字段
     * @param context 上下文
     * @param searchKey 查询关键字
     * @param fieldName 要查询的字段
     * @param mapView mapview
     * @param geometry 要查询的范围，可以为Null,为null默认查询全范围
     * @param queryLayers 要查询的图层
     * @param maxCount 最多返回数目
     * @param callback 回调
     */
    @Override
    public void queryLayer(Context context,
                           String searchKey,
                           String fieldName,
                           MapView mapView,
                           Geometry geometry,
                           List<LayerInfo> queryLayers,
                           int maxCount,
                           final Callback2<List<AMFindResult>> callback) {
      /*  mLocalGdb = null;
        try {
            mLocalGdb = GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getGeodatabaseManager(context).getGeodatabase();
            if (mLocalGdb == null || ValidateUtil.isListNull(mLocalGdb.getGeodatabaseTables())) {
                callback.onFail(new Exception("打开geodatabase失败，请检查是否本地是否存在"));
            }

            final List<AMFindResult> findResults = new ArrayList<>();
            //获取到geodatabase中对应的表，一张表就是一个featureLayer
            final List<GeodatabaseFeatureTable> geodatabaseTables = mLocalGdb.getGeodatabaseTables();
            final int size = geodatabaseTables.size();
            queryFinishedTables.set(0);
            for (final GeodatabaseFeatureTable gdbFeatureTable : geodatabaseTables) {
                QueryParameters queryParams = createQueryParams(geometry,fieldName, searchKey);
                GeodatabaseManager.queryFeature(gdbFeatureTable, queryParams, new Callback2<List<AMFindResult>>() {
                    @Override
                    public void onSuccess(List<AMFindResult> amFindResults) {

                        findResults.addAll(amFindResults);
                        queryFinishedTables.addAndGet(1);
                        returnResultIfFinishQueryAllTable();
                    }

                    *//**
                     * 返回结果
                     *//*
                    private void returnResultIfFinishQueryAllTable() {
                        if (queryFinishedTables.get() == size) {
                            *//* AMFindResult[] identifyResultsArray = new AMFindResult[findResults.size()];
                           for (int i = 0; i < findResults.size(); i++) {
                                identifyResultsArray[i] = findResults.get(i);
                            }*//*
                            callback.onSuccess(findResults);
                        }
                    }

                    @Override
                    public void onFail(Exception error) {
                        queryFinishedTables.addAndGet(1);
                        returnResultIfFinishQueryAllTable();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail(e);
        }*/

        GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getGeodatabaseManager(context).queryFeature(context,queryLayers,GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getGeodatabaseManager(context).createQueryParams(geometry,fieldName,searchKey,maxCount))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<AMFindResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null){
                            callback.onFail(new Exception(e));
                        }
                    }

                    @Override
                    public void onNext(List<AMFindResult> findResults) {
                        if (callback != null){
                            callback.onSuccess(findResults);
                        }
                    }
                });

      /*  for (LayerInfo layerInfo : queryLayers){
            GeodatabaseFeatureTable geodatabaseFeatureTable = GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getGeodatabaseManager(context).getGeodatabaseFeatureTable(layerInfo.getLayerTable());
            if (geodatabaseFeatureTable != null){ //如果在数据库中找到了
                GeodatabaseManager.queryFeature(geodatabaseFeatureTable, GeodatabaseManager.createQueryParams(geometry, fieldName, searchKey), new Callback2<List<AMFindResult>>() {
                    @Override
                    public void onSuccess(List<AMFindResult> amFindResults) {

                        findResults.addAll(amFindResults);
                        queryFinishedTables.addAndGet(1);
                        returnResultIfFinishQueryAllTable(size,findResults,callback);
                    }

                    @Override
                    public void onFail(Exception error) {
                        queryFinishedTables.addAndGet(1);
                        returnResultIfFinishQueryAllTable(size,findResults,callback);
                    }
                });
            }else {
                //如果在数据库中未找到
                queryFinishedTables.addAndGet(1);
                returnResultIfFinishQueryAllTable(size,findResults,callback);
            }
        }*/
    }

    /**
     * 查询图层,默认查询全字段
     * @param context 上下文
     * @param searchKey 关键字
     * @param spatialReference 空间参考系
     * @param geometry 要查询的范围，可以为Null,为null默认查询全范围
     * @param queryLayers 要查询的图层
     * @param maxCount 最多返回数目
     * @param callback 回调
     */
    @Override
    public void queryLayer(Context context,
                           String searchKey,
                           SpatialReference spatialReference,
                           Geometry geometry,
                           List<LayerInfo> queryLayers,
                           int maxCount,
                           final Callback2<List<AMFindResult>> callback) {

        if (ValidateUtil.isListNull(queryLayers)){
            callback.onFail(new NoQueryableLayerException("无可查询图层"));
            return;
        }

        GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getGeodatabaseManager(context).queryFeature(context,queryLayers,geometry,searchKey,maxCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<AMFindResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(callback != null){
                            callback.onFail(new Exception(e));
                        }
                    }

                    @Override
                    public void onNext(List<AMFindResult> findResults) {
                        if(callback != null) {
                            callback.onSuccess(findResults);
                        }
                    }
                });

          /*  for (LayerInfo layerInfo : queryLayers){
                GeodatabaseFeatureTable geodatabaseFeatureTable = GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getGeodatabaseManager(context).getGeodatabaseFeatureTable(layerInfo.getLayerTable());
                if (geodatabaseFeatureTable != null){ //如果在数据库中找到了
                    GeodatabaseManager.queryFeature(geodatabaseFeatureTable,searchKey, geometry, new Callback2<List<AMFindResult>>() {
                        @Override
                        public void onSuccess(List<AMFindResult> amFindResults) {

                            findResults.addAll(amFindResults);
                            queryFinishedTables.addAndGet(1);
                            returnResultIfFinishQueryAllTable(size,findResults,callback);
                        }



                        @Override
                        public void onFail(Exception error) {
                            queryFinishedTables.addAndGet(1);
                            returnResultIfFinishQueryAllTable(size,findResults,callback);
                        }
                    });
                }else {
                     //如果在数据库中未找到
                    queryFinishedTables.addAndGet(1);
                    returnResultIfFinishQueryAllTable(size,findResults,callback);
                }
            }*/
            //获取到geodatabase中对应的表，一张表就是一个featureLayer
         /*   final List<GeodatabaseFeatureTable> geodatabaseTables = mLocalGdb.getGeodatabaseTables();
            final int size = geodatabaseTables.size();
            queryFinishedTables.set(0);
            for (final GeodatabaseFeatureTable gdbFeatureTable : geodatabaseTables) {

                GeodatabaseManager.queryFeature(gdbFeatureTable,searchKey, geometry, new Callback2<List<AMFindResult>>() {
                    @Override
                    public void onSuccess(List<AMFindResult> amFindResults) {

                        findResults.addAll(amFindResults);
                        queryFinishedTables.addAndGet(1);
                        returnResultIfFinishQueryAllTable();
                    }

                    *//**
                     * 返回结果
                     *//*
                    private void returnResultIfFinishQueryAllTable() {
                        if (queryFinishedTables.get() == size) {
                            *//* AMFindResult[] identifyResultsArray = new AMFindResult[findResults.size()];
                           for (int i = 0; i < findResults.size(); i++) {
                                identifyResultsArray[i] = findResults.get(i);
                            }*//*
                            callback.onSuccess(findResults);
                        }
                    }

                    @Override
                    public void onFail(Exception error) {
                        queryFinishedTables.addAndGet(1);
                        returnResultIfFinishQueryAllTable();
                    }
                });
            }*/
    }


    @Override
    public List<Field> getAllFields(Context context, LayerInfo layerInfo) {
        List<Field> fields = GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getGeodatabaseManager(context).getFieldsByTableName(layerInfo.getLayerTable());
        //过滤掉不需要查询的字段
        List<Field> finalResult = new ArrayList<>();
        for (Field field : fields){
            if (!field.getName().toUpperCase().contains("OBJECTID") && !field.getName().toUpperCase().contains("SHAPE")){
                finalResult.add(field);
            }
        }
        return finalResult;
    }
}
