package com.augurit.agmobile.mapengine.common.utils;

import android.app.Activity;
import android.content.Context;
import android.transition.Scene;
import android.util.ArrayMap;

import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.AgcomLayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.agmobile.mapengine.layermanage.model.MultiPlanVectorLayerBean;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.widget.filepicker.utils.FileUtils;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Geometry;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Field;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.augurit.agmobile.mapengine.common.utils.FeatureResultUtil.transFeatureResultToAgFindResult;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.utils
 * @createTime 创建时间 ：2017-04-11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-11
 * @modifyMemo 修改备注：
 */

public class GeodatabaseManager {


    protected Geodatabase mGeodatabase;

    protected Map<String, GeodatabaseFeatureTable> tableMap = new ArrayMap<>();//key是表名

    protected static GeodatabaseManager mGeodatabaseManager;

    public static GeodatabaseManager getInstance(Context context) {
        if (mGeodatabaseManager == null) {
            synchronized (GeodatabaseManager.class) {
                if (mGeodatabaseManager == null) {
                    mGeodatabaseManager = new GeodatabaseManager(context);
                }
            }
        }
        return mGeodatabaseManager;
    }

    protected GeodatabaseManager(Context context) {
        try {
            initLocalGeodatabase(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化本地数据库
     *
     * @param context
     * @return 本地数据库
     * @throws Exception 当数据库路径为空或者找不到.geodatabase时抛出异常
     */
    private void initLocalGeodatabase(Context context) throws Exception {
        String geodatabaseSavePath = new FilePathUtil(context).getInternalGeodatabaseSavePath(); //默认geodatabase路径

        List<File> files = FileUtils.getFileListByDirPath(geodatabaseSavePath, new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.getName().contains(".geodatabase");
            }
        });

        if (ValidateUtil.isListNull(files)) {
            LogUtil.w(geodatabaseSavePath + "路径下找不到geodatabase文件");
            throw new Exception(geodatabaseSavePath + "路径下找不到geodatabase文件");
        }

        try {
            mGeodatabase = new Geodatabase(files.get(0).getAbsolutePath());
            //完善所有的表信息
            List<GeodatabaseFeatureTable> geodatabaseTables = mGeodatabase.getGeodatabaseTables();
            for (GeodatabaseFeatureTable gdbFeatureTable : geodatabaseTables) {
                tableMap.put(gdbFeatureTable.getTableName(), gdbFeatureTable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Geodatabase getGeodatabase() {
        return mGeodatabase;
    }


    private static List<AMFindResult> transToAMFindResult(FeatureResult objects, GeodatabaseFeatureTable geodatabaseFeatureTable) {
        String displayFieldName = objects.getDisplayFieldName();
        List<Field> fields = geodatabaseFeatureTable.getFields();
        String featureServiceLayerName = null;
        if (geodatabaseFeatureTable instanceof GeodatabaseFeatureTable){
            featureServiceLayerName = geodatabaseFeatureTable.getFeatureServiceLayerName();
        }else {
            featureServiceLayerName = geodatabaseFeatureTable.getTableName();
        }
        return transFeatureResultToAgFindResult(objects, displayFieldName,fields,featureServiceLayerName,geodatabaseFeatureTable.getTableName());
    }


    public  QueryParameters createQueryParams(com.esri.core.geometry.Geometry geometry, int maxCount) {
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
     * @param geometry
     * @param key
     * @return
     */
    public static QueryParameters createQueryParams(com.esri.core.geometry.Geometry geometry, String key, int maxCount, List<Field> fields) {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setGeometry(geometry);
        queryParameters.setOutFields(new String[]{"*"});
        queryParameters.setSpatialRelationship(SpatialRelationship.CONTAINS);
        queryParameters.setReturnGeometry(true);
        //  queryParameters.setText(key);
        queryParameters.setMaxFeatures(maxCount);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            if (i == fields.size() - 1) {
                builder.append(fields.get(i).getName() + " Like '%" + key + "%'");
            } else {
                builder.append(fields.get(i).getName() + " Like '%" + key + "%' Or ");
            }
        }
        queryParameters.setWhere(builder.toString());
        // queryParameters.setWhere(filedName + " Like '%" + searchKey + "%'");
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
    public  QueryParameters createQueryParams(com.esri.core.geometry.Geometry geometry, String filedName, String searchKey, int maxCount) {
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


    /**
     * 通过表名获取到FeatureLayer
     *
     * @param tableName 表名，通过{@link LayerInfo#getLayerTable()}
     * @return FeatureLayer
     */
    public FeatureLayer getFeatureLayer(String tableName) {

        GeodatabaseFeatureTable geodatabaseFeatureTable = getGeodatabaseFeatureTable(tableName);
        if (geodatabaseFeatureTable != null) {
            if (geodatabaseFeatureTable.hasGeometry()) {
                return new FeatureLayer(geodatabaseFeatureTable);
            }
        }
        return null;
    }

    public void closeGeodatabase() {

        if (mGeodatabase != null) {
            mGeodatabase.dispose();
            mGeodatabase = null;
            mGeodatabaseManager = null;
        }
    }

    /**
     * 通过表名获取到GeodatabaseFeatureTable
     *
     * @param tableName 表名，通过{@link LayerInfo#getLayerTable()}
     * @return GeodatabaseFeatureTable
     */
    public GeodatabaseFeatureTable getGeodatabaseFeatureTable(String tableName) {
       /* if (mGeodatabase != null) {
            final List<GeodatabaseFeatureTable> geodatabaseTables = mGeodatabase.getGeodatabaseTables();
            for (final GeodatabaseFeatureTable gdbFeatureTable : geodatabaseTables) {
                if (gdbFeatureTable.getTableName().equals(tableName)) {
                    return gdbFeatureTable;
                }
            }
        }
        return null;*/
        if (mGeodatabase != null) {
            return tableMap.get(tableName);
        }
        return null;
    }

    /**
     * 获取某个表（FeatureLayer）的所有字段
     *
     * @param tableName 表名，通过{@link LayerInfo#getLayerTable()}
     * @return 获取某个表（FeatureLayer）的所有字段
     */
    public List<Field> getFieldsByTableName(String tableName) {
        GeodatabaseFeatureTable geodatabaseFeatureTable = getGeodatabaseFeatureTable(tableName);
        if (geodatabaseFeatureTable != null) {
            return geodatabaseFeatureTable.getFields();
        }
        return null;
    }


    private AtomicInteger queryCount = new AtomicInteger(0); //查询次数统计
    private Map<String, List<String>> getChildLayerTableNamesByParentLayerName = new HashMap<>(); //key是图层的layerName，value是其子图层的tableName

    /**
     * 通过LayerInfo查找数据
     *
     * @param context
     * @param layerInfos
     * @param queryParameters
     * @return
     */
    public Observable<List<AMFindResult>> queryFeature(final Context context, final List<LayerInfo> layerInfos,
                                                       final QueryParameters queryParameters) {

        return Observable.create(new Observable.OnSubscribe<List<AMFindResult>>() {
            @Override
            public void call(final Subscriber<? super List<AMFindResult>> subscriber) {
                //统计需要查询的表
                List<GeodatabaseFeatureTable> queryTables = new ArrayList<GeodatabaseFeatureTable>();
                List<GeodatabaseFeatureTable> tables = getQueryTablesFromLocal(layerInfos, context);
                queryTables.addAll(tables);

                queryTable(subscriber, queryTables, queryParameters);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 通过表名查找数据
     *
     * @param queryTables
     * @param queryParameters
     * @return
     */
    public Observable<List<AMFindResult>> queryFeature(final List<GeodatabaseFeatureTable> queryTables,
                                                       final QueryParameters queryParameters) {
        return Observable.create(new Observable.OnSubscribe<List<AMFindResult>>() {
            @Override
            public void call(final Subscriber<? super List<AMFindResult>> subscriber) {
                //统计需要查询的表
             /*   List<GeodatabaseFeatureTable> queryTables = new ArrayList<GeodatabaseFeatureTable>();
                List<GeodatabaseFeatureTable> tables = getQueryTables(layerInfos, context);
                queryTables.addAll(tables);*/

                queryTable(subscriber, queryTables, queryParameters);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void queryTable(final Subscriber<? super List<AMFindResult>> subscriber, List<GeodatabaseFeatureTable> queryTables, QueryParameters queryParameters) {
        if (!ValidateUtil.isListNull(queryTables)) {
            //遍历每张表进行查询
            final int size = queryTables.size();
            queryCount.set(0);
            final List<AMFindResult> finalResults = new ArrayList<AMFindResult>();
            for (final GeodatabaseFeatureTable featureTable : queryTables) {
                featureTable.queryFeatures(queryParameters, new CallbackListener<FeatureResult>() {
                    @Override
                    public void onCallback(FeatureResult objects) {
                        if (objects == null) {
                            queryCount.addAndGet(1);
                            returnResultIfFinishQueryAllTable(size, finalResults, subscriber);
                            return;
                        }
                        List<AMFindResult> findResults = transToAMFindResult(objects, featureTable);
                        finalResults.addAll(findResults);
                        queryCount.addAndGet(1);
                        returnResultIfFinishQueryAllTable(size, findResults, subscriber);
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

    /**
     * 返回结果
     */
    private void returnResultIfFinishQueryAllTable(int size, List<AMFindResult> findResults,
                                                   Subscriber<? super List<AMFindResult>> subscriber) {
        if (queryCount.get() >= size) {
            subscriber.onNext(findResults);
            subscriber.onCompleted();
           /* if (ValidateUtil.isListNull(findResults)) {
                subscriber.onError(new Exception("查无数据"));
            } else {
                subscriber.onNext(findResults);
                subscriber.onCompleted();
            }*/
        }
    }


    /**
     * 统计需要查询的表
     *
     * @param layerInfos 要进行查询的图层
     * @param context    上下文
     * @return 需要查询的表集合
     */
    public Observable<List<GeodatabaseFeatureTable>> getQueryTables(final List<LayerInfo> layerInfos, final Context context) {
        return Observable.fromCallable(new Callable<List<GeodatabaseFeatureTable>>() {
            @Override
            public List<GeodatabaseFeatureTable> call() throws Exception {
                List<GeodatabaseFeatureTable> queryTables = new ArrayList<>();
                for (LayerInfo info : layerInfos) {
                    //先使用表名查，如果查不到，说明它不是单一图层，而是组合图层
                    GeodatabaseFeatureTable table = tableMap.get(info.getLayerTable());
                    if (table == null) { //如果查不到
                        List<String> childLayerTableNames = getChildLayerTableNamesByParentLayerName.get(info.getLayerName()); //是否之前有保存这个图层的子图层表名
                        if (ValidateUtil.isListNull(childLayerTableNames)) {
                            childLayerTableNames = searchForChildLayer(context, info);
                        }
                        if (!ValidateUtil.isListNull(childLayerTableNames)) {
                            getChildLayerTableNamesByParentLayerName.put(info.getLayerName(), childLayerTableNames);
                            for (String tableName : childLayerTableNames) {
                                if (tableMap.get(tableName) != null) {
                                    queryTables.add(tableMap.get(tableName));
                                }
                            }
                        }
                    } else {
                        //如果可以查到
                        queryTables.add(table);
                    }
                }
                return queryTables;
            }
        }).subscribeOn(Schedulers.io());
    }


    public List<GeodatabaseFeatureTable> getQueryTablesFromLocal(final List<LayerInfo> layerInfos, final Context context) {
        List<GeodatabaseFeatureTable> queryTables = new ArrayList<>();
        for (LayerInfo info : layerInfos) {
            //先使用表名查，如果查不到，说明它不是单一图层，而是组合图层
            GeodatabaseFeatureTable table = tableMap.get(info.getLayerTable());
            if (table == null) { //如果查不到
                List<String> childLayerTableNames = getChildLayerTableNamesByParentLayerName.get(info.getLayerName()); //是否之前有保存这个图层的子图层表名
                if (ValidateUtil.isListNull(childLayerTableNames)) {
                    childLayerTableNames = searchForChildLayerFromLocal(context, info);
                }
                if (!ValidateUtil.isListNull(childLayerTableNames)) {
                    getChildLayerTableNamesByParentLayerName.put(info.getLayerName(), childLayerTableNames);
                    for (String tableName : childLayerTableNames) {
                        if (tableMap.get(tableName) != null) {
                            queryTables.add(tableMap.get(tableName));
                        }
                    }
                }
            } else {
                //如果可以查到
                queryTables.add(table);
            }
        }
        return queryTables;

    }

    /**
     * 填充子图层
     *
     * @param context
     * @param info
     */
    private List<String> searchForChildLayer(Context context, LayerInfo info) {
        AgcomLayerInfo agcomLayerInfo = LayerServiceFactory.provideLayerService(context).getAgcomLayerInfos();
        List<String> childLayer = new ArrayList<>();

        if (agcomLayerInfo == null || ValidateUtil.isListNull(agcomLayerInfo.getVectorLayer())) {
            return null;
        }

        for (MultiPlanVectorLayerBean layerInfo : agcomLayerInfo.getVectorLayer()) {
            if (layerInfo.getName().equals(info.getLayerName())
                    && layerInfo.getLayerId() != info.getLayerId()) { //名称相等，但是id不同，说明是它的子图层
                childLayer.add(layerInfo.getLayer_table()); //添加图层表名称
            }
        }
        return childLayer;
    }
    private List<String> searchForChildLayerFromLocal(Context context, LayerInfo info) {
        AgcomLayerInfo agcomLayerInfo = LayerServiceFactory.provideLayerService(context).getAgcomLayerInfosFromLocal();
        List<String> childLayer = new ArrayList<>();

        if (agcomLayerInfo == null || ValidateUtil.isListNull(agcomLayerInfo.getVectorLayer())) {
            return null;
        }

        for (MultiPlanVectorLayerBean layerInfo : agcomLayerInfo.getVectorLayer()) {
            if (layerInfo.getName().equals(info.getLayerName())
                    && layerInfo.getLayerId() != info.getLayerId()) { //名称相等，但是id不同，说明是它的子图层
                childLayer.add(layerInfo.getLayer_table()); //添加图层表名称
            }
        }
        return childLayer;
    }


    public Observable<List<AMFindResult>> queryFeature(final Context context, final List<LayerInfo> layerInfos,
                                                       final Geometry geometry, final String searchKey, final int maxCount) {

        return Observable.create(new Observable.OnSubscribe<List<AMFindResult>>() {
            @Override
            public void call(final Subscriber<? super List<AMFindResult>> subscriber) {
                //统计需要查询的表
                List<GeodatabaseFeatureTable> queryTables = new ArrayList<GeodatabaseFeatureTable>();
                List<GeodatabaseFeatureTable> tables = getQueryTablesFromLocal(layerInfos, context);
                queryTables.addAll(tables);

                if (!ValidateUtil.isListNull(queryTables)) {
                    //遍历每张表进行查询
                    final int size = queryTables.size();
                    queryCount.set(0);
                    final List<AMFindResult> finalResults = new ArrayList<AMFindResult>();
                    for (final GeodatabaseFeatureTable featureTable : queryTables) {
                        featureTable.queryFeatures(createQueryParams(geometry, searchKey, maxCount, featureTable.getFields()),
                                new CallbackListener<FeatureResult>() {
                                    @Override
                                    public void onCallback(FeatureResult objects) {
                                        if (objects == null) {
                                            queryCount.addAndGet(1);
                                            returnResultIfFinishQueryAllTable(size, finalResults, subscriber);
                                            return;
                                        }
                                        List<AMFindResult> findResults = transToAMFindResult(objects, featureTable);
                                        finalResults.addAll(findResults);
                                        queryCount.addAndGet(1);
                                        returnResultIfFinishQueryAllTable(size, findResults, subscriber);
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
}
