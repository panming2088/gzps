package com.augurit.agmobile.mapengine.identify.service;

import android.app.Activity;

import com.augurit.agmobile.mapengine.common.utils.GeodatabaseAndShapeFileManagerFactory;
import com.augurit.agmobile.mapengine.common.utils.GeodatabaseManager;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.esri.android.map.MapView;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Geometry;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Field;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 使用Geodatabase进行查询
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.identify.service
 * @createTime 创建时间 ：2017-04-12
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-12
 * @modifyMemo 修改备注：
 */

public class GeodatabaseIdentifyService extends IdentifyService {

    private AtomicInteger queryFinishedTables = new AtomicInteger(0);
    private Geodatabase mLocalGdb;

    @Override
    public void selectedFeature(final Activity context, final MapView mapView,List<LayerInfo> visibleQueryableLayers,
                                Geometry geometry,int tolerance, final Callback2<AMFindResult[]> callback) {
        /*String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AGMobile/shape/hd_jmd_40000.shp";
        ShapefileFeatureTable shapefileFeatureTable = null;
        try {
            shapefileFeatureTable = new ShapefileFeatureTable(filePath);
            QueryParameters queryParams = createQueryParams(geometry, mapView);
            final List<AMFindResult> finalResults = new ArrayList<>();
            shapefileFeatureTable.queryFeatures(queryParams, new CallbackListener<FeatureResult>() {
                @Override
                public void onCallback(FeatureResult objects) {
                    String displayFieldName = objects.getDisplayFieldName();
                    List<AMFindResult> findResults = FeatureResultUtil.transFeatureResultToAgFindResult(objects, displayFieldName);
                    finalResults.addAll(findResults);
                    if (ValidateUtil.isListNull(finalResults)){
                        callback.onFail(new Exception("数据为空"));
                        return;
                    }
                    //将结果传回
                    AMFindResult[] identifyResultsArray = new AMFindResult[finalResults.size()];
                    for (int i = 0; i < finalResults.size(); i++) {
                        identifyResultsArray[i] = finalResults.get(i);
                    }
                    callback.onSuccess(identifyResultsArray);
                }

                @Override
                public void onError(Throwable throwable) {
                    callback.onFail(new Exception(throwable));
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            callback.onFail(e);
        }*/

       // List<LayerInfo> visibleQueryableLayers = layersService.getVisibleQueryableLayers();
        if (ValidateUtil.isListNull(visibleQueryableLayers)){
            callback.onFail(new Exception("无可查询图层"));
        }
        selectFeature(context,mapView,geometry,tolerance,callback,visibleQueryableLayers);
       /* mLocalGdb = null;
        try {
            mLocalGdb = GeodatabaseManager.getInstance(context).getGeodatabase();
            if (mLocalGdb == null || ValidateUtil.isListNull(mLocalGdb.getGeodatabaseTables())) {
                callback.onFail(new Exception("打开geodatabase失败，请检查是否本地是否存在"));
            }

            final List<AMFindResult> findResults = new ArrayList<>();
            //获取到geodatabase中对应的表，一张表就是一个featureLayer
            final List<GeodatabaseFeatureTable> geodatabaseTables = mLocalGdb.getGeodatabaseTables();
            final int size = geodatabaseTables.size();
            queryFinishedTables.set(0);
            for (final GeodatabaseFeatureTable gdbFeatureTable : geodatabaseTables) {
                GeodatabaseManager.queryFeature(gdbFeatureTable, geometry, new Callback2<List<AMFindResult>>() {
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
                            if (ValidateUtil.isListNull(findResults)){
                                callback.onFail(new Exception("查无数据"));
                            }
                            final AMFindResult[] identifyResultsArray = new AMFindResult[findResults.size()];
                            for (int i = 0; i < findResults.size(); i++) {
                                identifyResultsArray[i] = findResults.get(i);
                            }
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(identifyResultsArray);
                                }
                            });

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
    }

    protected void selectFeature(final Activity context,
                              MapView mapView,
                              Geometry geometry,
                              int tolerance,
                              final Callback2<AMFindResult[]> callback,
                              List<LayerInfo> visibleQueryableLayers) {

        GeodatabaseManager.getInstance(context).queryFeature(context,visibleQueryableLayers, GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getGeodatabaseManager(context).createQueryParams(geometry,300))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<AMFindResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<AMFindResult> findResults) {
                        if (ValidateUtil.isListNull(findResults)){
                            callback.onFail(new Exception("点查无数据"));
                        }else {
                            final AMFindResult[] identifyResultsArray = new AMFindResult[findResults.size()];
                            for (int i = 0; i < findResults.size(); i++) {
                                identifyResultsArray[i] = findResults.get(i);
                            }
                            callback.onSuccess(identifyResultsArray);
                        }
                    }
                });

        /*final List<AMFindResult> findResults = new ArrayList<>();
        queryFinishedTables.set(0);
        final int size = visibleQueryableLayers.size();
        for (LayerInfo layerInfo : visibleQueryableLayers){
            GeodatabaseFeatureTable geodatabaseFeatureTable = GeodatabaseManager.getInstance(context).getGeodatabaseFeatureTable(layerInfo.getLayerTable());
            if (geodatabaseFeatureTable == null){ //继续下一个
                queryFinishedTables.addAndGet(1);
                returnResultIfFinishQueryAllTable(context,size,findResults,callback);
                continue;
            }
            GeodatabaseManager.queryFeature(geodatabaseFeatureTable, geometry, new Callback2<List<AMFindResult>>() {
                @Override
                public void onSuccess(List<AMFindResult> amFindResults) {
                    findResults.addAll(amFindResults);
                    queryFinishedTables.addAndGet(1);
                    returnResultIfFinishQueryAllTable(context,size,findResults,callback);
                }

                @Override
                public void onFail(Exception error) {
                    queryFinishedTables.addAndGet(1);
                    returnResultIfFinishQueryAllTable(context,size,findResults,callback);
                }
            });
        }*/
    }


    /**
     * 返回结果
     */
    private void returnResultIfFinishQueryAllTable(Activity context, int size, List<AMFindResult> findResults,
                                                   final Callback2<AMFindResult[]> callback) {
        if (queryFinishedTables.get() == size) {
            if (ValidateUtil.isListNull(findResults)){
                callback.onFail(new Exception("查无数据"));
            }
            final AMFindResult[] identifyResultsArray = new AMFindResult[findResults.size()];
            for (int i = 0; i < findResults.size(); i++) {
                identifyResultsArray[i] = findResults.get(i);
            }
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(identifyResultsArray);
                }
            });

        }
    }

}
