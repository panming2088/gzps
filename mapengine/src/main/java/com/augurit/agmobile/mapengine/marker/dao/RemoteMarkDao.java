package com.augurit.agmobile.mapengine.marker.dao;

import android.content.Context;

import com.augurit.agmobile.mapengine.layermanage.dao.LocalLayerStorageDao;
import com.augurit.agmobile.mapengine.marker.model.Mark;
import com.augurit.agmobile.mapengine.marker.util.MarkCompator;
import com.augurit.agmobile.mapengine.marker.util.MarkUtil;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureSet;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

/**
 * 描述：从网络获取标注信息，比如：
 * （1）从服务端获取所有的标注
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.marker.dao
 * @createTime 创建时间 ：2017-02-06
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-02-06
 */

public class RemoteMarkDao {

    private Subscription getAllMarkSubscription;

    /**
     * 从服务端获取所有的标注
     * @param mapView
     * @param listener  查询监听
     */
    public void queryAllMark(Context context, MapView mapView, final Callback2<List<Mark>> listener) {
        Query query = getQueryAll();
        //检查，如果id为null的，一律不添加到列表中
    //对这些标注按照时间进行排序
        getAllMarkSubscription = Observable
                .zip(
                        getPointQueryObservable2(context, query, mapView),
                        getLineQueryObservable(context, query, mapView),
                        getPolygonObservable(context, query, mapView),
                        new Func3<List<Mark>, List<Mark>, List<Mark>, List<Mark>>() {
                            @Override
                            public List<Mark> call(List<Mark> marks, List<Mark> marks2, List<Mark> marks3) {
                                marks.addAll(marks2);
                                marks.addAll(marks3);
                                return marks;
                            }
                        })
                .map(new Func1<List<Mark>, List<Mark>>() {
                    @Override
                    public List<Mark> call(List<Mark> marks) {
                        List<Mark> marks1 = new ArrayList<Mark>();
                        for (Mark mark : marks) {
                            if (mark.getSymbol() != null && mark.getGeometry() != null) {
                                marks1.add(mark);
                            }
                        }
                        return marks1;
                    }
                }).map(new Func1<List<Mark>, List<Mark>>() {
                    @Override
                    public List<Mark> call(List<Mark> marks) {
                        List<Mark> finalResult = new ArrayList<Mark>();
                        for (Mark mark : marks) {
                            //检查，如果id为null的，一律不添加到列表中
                            if (mark.getId() != null) {
                                finalResult.add(mark);
                            }
                        }
                        return finalResult;
                    }
                }).map(new Func1<List<Mark>, List<Mark>>() {
                    @Override
                    public List<Mark> call(List<Mark> marks) {
                        //对这些标注按照时间进行排序
                        Collections.sort(marks, new MarkCompator());
                        return marks;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Mark>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("获取所有标注失败，原因是：" + e.getMessage());
                        listener.onFail(new Exception(e.getLocalizedMessage()));
                    }

                    @Override
                    public void onNext(List<Mark> marks) {
                        LogUtil.d("获取所有标注成功，标注个数是" + marks.size());
                        listener.onSuccess(marks);
                    }
                });

    }

    private Query getQueryAll() {
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        //TODO 这里以后要变成分享的条件
        query.setWhere("1=1");
        return query;
    }

    private Observable<List<Mark>>  getPointQueryObservable2(final Context context, final Query query, final MapView mapView){
        return Observable.create(new Observable.OnSubscribe<List<Mark>>() {
            @Override
            public void call(final Subscriber<? super List<Mark>> subscriber) {
                //  List<Mark> marks = new ArrayList<Mark>();
                LocalLayerStorageDao layerDataManager = new LocalLayerStorageDao();
                if (layerDataManager.getMarkPointFeatureLayerUrl(context)!= null){
                    final ArcGISFeatureLayer pointLayer = new ArcGISFeatureLayer(
                            layerDataManager.getMarkPointFeatureLayerUrl(context), ArcGISFeatureLayer.MODE.SNAPSHOT);
                    // Query onGraphicSelected = getQuery(clickPoint);
                    if(!pointLayer.isInitialized()){
                        LogUtil.e("标注图层服务错误");
                        subscriber.onError(new Exception("标注图层服务不存在"));
                        return;
                    }
                    pointLayer.setOnStatusChangedListener(new OnStatusChangedListener() {
                        @Override
                        public void onStatusChanged(Object o, STATUS status) {
                            if (status == STATUS.INITIALIZED){
                                query.setInSpatialReference(mapView.getSpatialReference());
                                pointLayer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                                    @Override
                                    public void onCallback(FeatureSet featureSet) {

                                        LogUtil.d("标注查询点返回结果："+ featureSet.getGraphics().length);

                                        List<Mark> marks = MarkUtil.completeMarkInfo(context, featureSet, pointLayer);
                                        subscriber.onNext(marks);
                                        subscriber.onCompleted();
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        LogUtil.e("标注点的返回结果失败");
                                        subscriber.onError(throwable);
                                    }
                                });
                            }

                        }
                    });
                }
            }
        }).subscribeOn(Schedulers.io());
//                .onErrorReturn(new Func1<Throwable, List<Mark>>() {
//                    @Override
//                    public List<Mark> call(Throwable throwable) {
//                        return new ArrayList<Mark>();
//                    }
//                });
    }

    /**
     * 线
     * @param mapView
     * @return
     */
    private Observable<List<Mark>>  getLineQueryObservable(final Context context, final Query query, final MapView mapView){
        return Observable.create(new Observable.OnSubscribe<List<Mark>>() {
            @Override
            public void call(final Subscriber<? super List<Mark>> subscriber) {
                //  List<Mark> marks = new ArrayList<Mark>();
                LocalLayerStorageDao layerDataManager = new LocalLayerStorageDao();
                if (layerDataManager.getMarkLineFeatureLayerUrl(context)!= null){
                    final ArcGISFeatureLayer pointLayer = new ArcGISFeatureLayer(layerDataManager.getMarkLineFeatureLayerUrl(context), ArcGISFeatureLayer.MODE.SNAPSHOT);
                    // Query onGraphicSelected = getQuery(clickPoint);
                    if(!pointLayer.isInitialized()){
                        LogUtil.e("标注图层服务错误");
                        subscriber.onError(new Exception("标注图层服务不存在"));
                        return;
                    }
                    pointLayer.setOnStatusChangedListener(new OnStatusChangedListener() {
                        @Override
                        public void onStatusChanged(Object o, STATUS status) {
                            query.setInSpatialReference(mapView.getSpatialReference());
                            pointLayer.selectFeatures(query, ArcGISFeatureLayer.SELECTION_METHOD.NEW, new CallbackListener<FeatureSet>(){

                                @Override
                                public void onCallback(FeatureSet featureSet) {
                                    LogUtil.d("标注查询线返回结果："+ featureSet.getGraphics().length);
                                    List<Mark> marks = MarkUtil.completeMarkInfo(context, featureSet, pointLayer);
                                    subscriber.onNext(marks);
                                    subscriber.onCompleted();
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    subscriber.onError(throwable);
                                    subscriber.onError(throwable);
                                }
                            });
                        }
                    });
                }

            }
        }).subscribeOn(Schedulers.io());
//                .onErrorReturn(new Func1<Throwable, List<Mark>>() {
//                    @Override
//                    public List<Mark> call(Throwable throwable) {
//                        return new ArrayList<Mark>();
//                    }
//                });
    }

    /**
     * 面
     * @param query
     * @param mapView
     * @return
     */
    private Observable<List<Mark>>  getPolygonObservable(final Context context, final Query query, final MapView mapView){
        return Observable.create(new Observable.OnSubscribe<List<Mark>>() {
            @Override
            public void call(final Subscriber<? super List<Mark>> subscriber) {
                //  List<Mark> marks = new ArrayList<Mark>();
                LocalLayerStorageDao layerDataManager = new LocalLayerStorageDao();
                if (layerDataManager.getMarkPolygonFeatureLayerUrl(context)!= null){
                    final ArcGISFeatureLayer pointLayer = new ArcGISFeatureLayer(layerDataManager.getMarkPolygonFeatureLayerUrl(context), ArcGISFeatureLayer.MODE.SNAPSHOT);
                    //Query onGraphicSelected = getQuery(clickPoint);
                    if(!pointLayer.isInitialized()){
                        LogUtil.e("标注图层服务错误");
                        subscriber.onError(new Exception("标注图层服务不存在"));
                        return;
                    }
                    pointLayer.setOnStatusChangedListener(new OnStatusChangedListener() {
                        @Override
                        public void onStatusChanged(Object o, STATUS status) {
                            query.setInSpatialReference(mapView.getSpatialReference());
                            pointLayer.selectFeatures(query, ArcGISFeatureLayer.SELECTION_METHOD.NEW, new CallbackListener<FeatureSet>(){

                                @Override
                                public void onCallback(FeatureSet featureSet) {

                                    LogUtil.d("标注查询面返回结果："+ featureSet.getGraphics().length);
                                    //面的返回结果
                                    List<Mark> marks = MarkUtil.completeMarkInfo(context, featureSet, pointLayer);
                                    subscriber.onNext(marks);
                                    subscriber.onCompleted();
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    subscriber.onError(throwable);
                                }
                            });
                        }
                    });
                }
            }
        }).subscribeOn(Schedulers.io());
//                .onErrorReturn(new Func1<Throwable, List<Mark>>() {
//                    @Override
//                    public List<Mark> call(Throwable throwable) {
//                        return new ArrayList<Mark>();
//                    }
//                });
    }

    public void closeAllMarkRequest(){
        if (getAllMarkSubscription != null && getAllMarkSubscription.isUnsubscribed()){
            getAllMarkSubscription.unsubscribe();
        }
    }

}
