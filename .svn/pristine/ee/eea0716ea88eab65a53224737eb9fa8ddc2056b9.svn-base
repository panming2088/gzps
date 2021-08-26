package com.augurit.agmobile.mapengine.common.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geodatabase.ShapefileFeatureTable;
import com.esri.core.geometry.Geometry;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Field;
import com.esri.core.table.FeatureTable;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.augurit.agmobile.mapengine.common.utils.FeatureResultUtil.transFeatureResultToAgFindResult;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.utils
 * @createTime 创建时间 ：2017-05-09
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-05-09
 * @modifyMemo 修改备注：
 */

public class FeatureTableUtil {

    /**
     * @param geometry
     * @param key
     * @return
     */
    public static QueryParameters createQueryParams(Geometry geometry, String key, int maxCount, List<Field> fields) {
        if (TextUtils.isEmpty(key)){
            return queryParameterWithoutSearchKey(geometry,maxCount);
        }
        return queryParameterWithSearchText(geometry, key, maxCount, fields);
    }

    /**
     * 生成用于获取结果个数的QueryParamter
     * @param geometry
     * @return
     */
    public static QueryParameters createQueryParamsForGetCount(Geometry geometry) {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setGeometry(geometry);
        queryParameters.setSpatialRelationship(SpatialRelationship.CONTAINS);
        queryParameters.setReturnGeometry(false);
        queryParameters.setReturnIdsOnly(true);
        return queryParameters;
    }

    @NonNull
    private static QueryParameters queryParameterWithSearchText(Geometry geometry, String key, int maxCount, List<Field> fields) {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setGeometry(geometry);
        queryParameters.setOutFields(new String[]{"*"});
        queryParameters.setSpatialRelationship(SpatialRelationship.CONTAINS);
        queryParameters.setReturnGeometry(true);
        queryParameters.setMaxFeatures(maxCount);
        StringBuilder builder = new StringBuilder();
        for (int i=0; i< fields.size(); i++){
            if (i == fields.size() -1){
                builder.append(fields.get(i).getName()+ " Like '%" + key + "%'");
            }else {
                builder.append(fields.get(i).getName()+ " Like '%" + key + "%' Or ");
            }
        }
        queryParameters.setWhere(builder.toString());
        return queryParameters;
    }

    @NonNull
    private static QueryParameters queryParameterWithoutSearchKey(com.esri.core.geometry.Geometry geometry,int maxCount) {
        QueryParameters queryParameters = new QueryParameters();
        if (geometry != null) {
            queryParameters.setGeometry(geometry);
        }
        queryParameters.setOutFields(new String[]{"*"});
        queryParameters.setSpatialRelationship(SpatialRelationship.CONTAINS);
        queryParameters.setReturnGeometry(true);
        queryParameters.setMaxFeatures(maxCount);

        //        queryParameters.setInSpatialReference(mapView.getSpatialReference());
        //        queryParameters.setOutSpatialReference(mapView.getSpatialReference());
        return queryParameters;
    }

    /**
     * 查询条件
     *
     * @param geometry  查询范围
     * @param filedName 查询字段
     * @param searchKey 查询关键字
     * @return 查询条件
     */
    public static QueryParameters createQueryParams(Geometry geometry, String filedName, String searchKey,int maxCount) {
        QueryParameters queryParameters = new QueryParameters();
        if (geometry != null) {
            queryParameters.setGeometry(geometry);
        }
        queryParameters.setOutFields(new String[]{"*"});
        queryParameters.setSpatialRelationship(SpatialRelationship.CONTAINS);
        queryParameters.setReturnGeometry(true);
        queryParameters.setWhere(filedName + " Like '%" + searchKey + "%'");
        queryParameters.setMaxFeatures(maxCount);
        //        queryParameters.setInSpatialReference(mapView.getSpatialReference());
        //        queryParameters.setOutSpatialReference(mapView.getSpatialReference());
        return queryParameters;
    }

    private static AtomicInteger queryCount = new AtomicInteger(0); //查询次数统计

    public static Observable<List<AMFindResult>> queryFeature(final List<FeatureTable> queryTables,
                                                       final Geometry geometry, final String searchKey, final int maxCount){
        return Observable.create(new Observable.OnSubscribe<List<AMFindResult>>() {
            @Override
            public void call(final Subscriber<? super List<AMFindResult>> subscriber) {
                if (!ValidateUtil.isListNull(queryTables)) {
                    //遍历每张表进行查询
                    final int size = queryTables.size();
                    queryCount.set(0);
                    final List<AMFindResult> finalResults = new ArrayList<AMFindResult>();
                    for (final FeatureTable featureTable : queryTables) {
                        featureTable.queryFeatures(createQueryParams(geometry, searchKey, maxCount, featureTable.getFields()), new CallbackListener<FeatureResult>() {
                            @Override
                            public void onCallback(FeatureResult objects) {
                                if (objects == null) {
                                    queryCount.addAndGet(1);
                                    returnResultIfFinishQueryAllTable(size, finalResults, maxCount,subscriber);
                                    return;
                                }
                                List<AMFindResult> findResults = null;
                                if (featureTable instanceof GeodatabaseFeatureTable){
                                    String layerName = ((GeodatabaseFeatureTable) featureTable).getFeatureServiceLayerName();
                                    findResults = transToAMFindResult(objects, featureTable,layerName);
                                }else if (featureTable instanceof ShapefileFeatureTable){
                                    String layerName = featureTable.getTableName();
                                    findResults = transToAMFindResult(objects, featureTable,layerName);
                                }
                                finalResults.addAll(findResults);
                                queryCount.addAndGet(1);
                                returnResultIfFinishQueryAllTable(size, findResults,maxCount,subscriber);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                throwable.printStackTrace();
                                queryCount.addAndGet(1);
                                returnResultIfFinishQueryAllTable(size, finalResults,maxCount, subscriber);
                            }
                        });
                    }
                } else {
                    subscriber.onError(new Exception("需要查询的图层在本地数据库中找不到对应的表"));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 自己构造查询参数进行查询
     * @param queryParameters 查询参数
     * @param queryTables 要查询的图层
     * @return 查询结果
     */
    public static Observable<List<AMFindResult>> queryFeature(final List<QueryParameters> queryParameters,
                                                              final List<FeatureTable> queryTables){
        return Observable.create(new Observable.OnSubscribe<List<AMFindResult>>() {
            @Override
            public void call(final Subscriber<? super List<AMFindResult>> subscriber) {
                if (!ValidateUtil.isListNull(queryTables)) {
                    //遍历每张表进行查询
                    final int size = queryTables.size();
                    queryCount.set(0);
                    final List<AMFindResult> finalResults = new ArrayList<AMFindResult>();
                    for (int i=0 ; i< queryTables.size(); i++) {
                        final FeatureTable featureTable = queryTables.get(i);
                        QueryParameters queryParameter = queryParameters.get(i);
                        featureTable.queryFeatures(queryParameter, new CallbackListener<FeatureResult>() {
                            @Override
                            public void onCallback(FeatureResult objects) {
                                if (objects == null) {
                                    queryCount.addAndGet(1);
                                    returnResultIfFinishQueryAllTable(size, finalResults,subscriber);
                                    return;
                                }
                                List<AMFindResult> findResults = null;
                                if (featureTable instanceof GeodatabaseFeatureTable){
                                    String layerName = ((GeodatabaseFeatureTable) featureTable).getFeatureServiceLayerName();
                                    findResults = transToAMFindResult(objects, featureTable,layerName);
                                }else if (featureTable instanceof ShapefileFeatureTable){
                                    String layerName = featureTable.getTableName();
                                    findResults = transToAMFindResult(objects, featureTable);
                                }
                                finalResults.addAll(findResults);
                                queryCount.addAndGet(1);
                                returnResultIfFinishQueryAllTable(size, findResults,subscriber);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                throwable.printStackTrace();
                                queryCount.addAndGet(1);
                                returnResultIfFinishQueryAllTable(size, finalResults, subscriber);
                            }
                        });
                    }
                } else {
                    subscriber.onError(new Exception("需要查询的图层在本地数据库中找不到对应的表"));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 返回结果
     */
    private static void returnResultIfFinishQueryAllTable(int size, List<AMFindResult> findResults,
                                                          int maxCount,
                                                   Subscriber<? super List<AMFindResult>> subscriber) {
        if (queryCount.get() >= size || findResults.size() > maxCount) {
            subscriber.onNext(findResults);
            subscriber.onCompleted();
        }
    }

    /**
     * 返回结果
     */
    private static void returnResultIfFinishQueryAllTable(int size, List<AMFindResult> findResults,
                                                   Subscriber<? super List<AMFindResult>> subscriber) {
        if (queryCount.get() >= size) {
            subscriber.onNext(findResults);
            subscriber.onCompleted();
        }
    }


    private static List<AMFindResult> transToAMFindResult(FeatureResult objects, FeatureTable geodatabaseFeatureTable) {
        String displayFieldName = objects.getDisplayFieldName();
        List<Field> fields = geodatabaseFeatureTable.getFields();
        String featureServiceLayerName = null;
        if (geodatabaseFeatureTable instanceof GeodatabaseFeatureTable){
            featureServiceLayerName = ((GeodatabaseFeatureTable) geodatabaseFeatureTable).getFeatureServiceLayerName();
        }else {
            featureServiceLayerName = geodatabaseFeatureTable.getTableName();
        }
        return transFeatureResultToAgFindResult(objects, displayFieldName,fields,featureServiceLayerName,geodatabaseFeatureTable.getTableName());
    }

    private static List<AMFindResult> transToAMFindResult(FeatureResult objects, FeatureTable geodatabaseFeatureTable,String layerName) {
        String displayFieldName = objects.getDisplayFieldName();
        List<Field> fields = geodatabaseFeatureTable.getFields();
        return transFeatureResultToAgFindResult(objects, displayFieldName,fields,layerName,geodatabaseFeatureTable.getTableName());
    }
}
