package com.augurit.agmobile.gzps.componentmaintenance.service;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.model.DoorNOBean;
import com.augurit.agmobile.gzps.common.util.SewerageLayerFieldKeyConstant;
import com.augurit.agmobile.gzps.componentmaintenance.model.QueryFeatureSet;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentTypeConstant;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.util.SharedPreferencesUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.identify.IdentifyTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.componentmaintenance
 * @createTime 创建时间 ：17/10/14
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/14
 * @modifyMemo 修改备注：
 */

public class ComponentService {


    private Context mContext;
    private Query query;

    public ComponentService(Context context) {
        this.mContext = context;
    }

    private List<String> layerNames = Arrays.asList("窨井", "雨水口", "排水口");
//    public static final int HUANGPU_WIKI = 3857;
//    public static final int CHENGQU_WIKI = 4490;
//    public static final int  BIG_POINT = 100000;
    /**
     * 查询所有部件
     *
     * @param componentType 部件类型，两种类型：{@link ComponentTypeConstant#NEW_ADDED}以及{@link ComponentTypeConstant#OLD_COMPONENT}
     */
    public Observable<List<QueryFeatureSet>> queryAllComponents2(final String componentType) {
        final Query query = getQueryByFlagAndUserName(componentType, new LoginService(mContext, AMDatabase.getInstance()).getUser().getUserName());
        List<Observable<QueryFeatureSet>> observableList = new ArrayList<>();
        for (String layerUrl : LayerUrlConstant.newComponentUrls) {
            Observable<QueryFeatureSet> observable = getObservable(layerUrl, query);
            observableList.add(observable);
        }
        return Observable.zip(observableList, new FuncN<List<QueryFeatureSet>>() {
            @Override
            public List<QueryFeatureSet> call(Object... objects) {
                if (objects.length == 0) {
                    return null;
                }
                List<QueryFeatureSet> queryFeatureSetList = new ArrayList<QueryFeatureSet>();
                for (Object object : objects) {
                    if (object == null) {
                        continue;
                    }
                    QueryFeatureSet queryFeatureSet = (QueryFeatureSet) object;
                    queryFeatureSetList.add(queryFeatureSet);
                }
                return queryFeatureSetList;
            }
        }).subscribeOn(Schedulers.io());
    }

//    /**
//     * 查询所有部件
//     * @param componentType 部件类型，两种类型：{@link ComponentTypeConstant#NEW_ADDED}以及{@link ComponentTypeConstant#OLD_COMPONENT}
//     */
//    public Observable<QueryFeatureSet> queryAllComponents(final String componentType) {
//        final Query query = getQueryByFlagAndUserName(componentType, new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName());
//
//        return Observable.from(LayerUrlConstant.newComponentUrls)
//                .flatMap(new Func1<String, Observable<QueryFeatureSet>>() {
//                    @Override
//                    public Observable<QueryFeatureSet> call(String s) {
//                        return getObservable(s,query);
//                    }
//                })
//                .map(new Func1<QueryFeatureSet, QueryFeatureSet>() { //根据部件类型进行过滤
//                    @Override
//                    public QueryFeatureSet call(QueryFeatureSet featureSet) {
//
//                        //todo 暂时不过滤，等条件出来加上
//                        List<Graphic> result = new ArrayList<Graphic>();
//                        if(featureSet == null){
//                            return null;
//                        }
//                        Graphic[] graphics = featureSet.getFeatureSet().getGraphics();
//                        if (graphics!= null && graphics.length >0){
//                            for (Graphic graphic : graphics){
//                                //新增部件
//                                if (ComponentTypeConstant.NEW_ADDED.equals(componentType)){
//                                    if (NEW_ADDED_COMPONENT_VALUE.equals(graphic.getAttributes().get(ComponentTypeConstant.COMPONENT_TYPE_KEY))){
//                                        result.add(graphic);
//                                    }
//                                }
//                                //原有部件
//                                else {
//                                    if (OLD_COMPONENT_VALUE2.equals(graphic.getAttributes().get(ComponentTypeConstant.COMPONENT_TYPE_KEY))){
//                                        result.add(graphic);
//                                    }
//                                }
//
//                            }
//
//                            //替代
//                            Graphic[] array = result.toArray(new Graphic[0]);
//                            featureSet.getFeatureSet().setGraphics(array);
//                        }
//                        return featureSet;
//                    }
//                })
//                .subscribeOn(Schedulers.io());
//    }

    private Query getQueryAll() {
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setWhere("1=1");
        return query;
    }

    private Query getQueryByFlag(String flag) {
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setWhere("FLAG_=" + flag);
        return query;
    }

    private Query getQueryByFlagAndUserName(String flag, String userName) {
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setWhere("FLAG_='" + flag + "' and REPAIR_COM='" + userName + "'");
        return query;
    }


    private Query getQueryByGeometry(Geometry geometry) {
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        query.setWhere("1=1");
        query.setGeometry(geometry);
        return query;
    }

    private Query getQueryByObjectId(String objectId) {
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
//        query.setWhere("OBJECTID=" + objectId +" or NAME='"+objectId+"'");
        query.setWhere("OBJECTID='" + objectId + "'");
        return query;
    }

    private Query getQueryByMenPai(String objectId) {
        User user = new LoginService(mContext.getApplicationContext(), AMDatabase.getInstance()).getUser();
        StringBuilder sb = new StringBuilder();
        sb.append("(DZQC like '%" + objectId + "%' or ");
        sb.append("MPWZHM like '%" + objectId + "%') ");
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setWhere(sb.toString());
        return query;
    }

    /**
     * 只查巡检日志
     */
    public void queryPrimaryComponentsForJournal(final Geometry geometry, final Callback2<List<Component>> callback) {
        Observable<List<Component>> yinjin = queryComponentsAfterFilter(geometry, getLayerUrlByLayName(PatrolLayerPresenter.JOURNAL_LAYER_NAME) + "/0");
        yinjin.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查窨井、雨水口、排水口
     */
    public void queryPrimaryComponentsFacility(final Geometry geometry, final Callback2<List<Component>> callback) {
        Observable<List<Component>> yinjin = queryComponentsWithLayerNameAndGeometry(geometry, "窨井");
        Observable<List<Component>> yushuikou = queryComponentsWithLayerNameAndGeometry(geometry, "雨水口");
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(yinjin);
        observables.add(yushuikou);
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查窨井、排水管道
     */
    public void queryPrimaryComponentsStream(final Geometry geometry, final Callback2<List<Component>> callback) {
        Observable<List<Component>> yinjin = queryComponentsWithLayerNameAndGeometry(geometry, "窨井");
        Observable<List<Component>> yushuikou = queryComponentsWithLayerNameAndGeometry(geometry, "排水管道");
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(yinjin);
        observables.add(yushuikou);
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查窨井、排水管道
     */
    public void queryPrimaryComponentsYinjing(final Geometry geometry, final Callback2<List<Component>> callback) {
        Observable<List<Component>> yinjin = queryComponentsWithLayerNameAndGeometry(geometry, "窨井");
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(yinjin);
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查窨井、雨水口、排水口
     */
    public void queryPrimaryComponents(final Geometry geometry, final Callback2<List<Component>> callback) {
        Observable<List<Component>> yinjin = queryComponentsWithLayerNameAndGeometry(geometry, "窨井");
        Observable<List<Component>> yushuikou = queryComponentsWithLayerNameAndGeometry(geometry, "雨水口");
        Observable<List<Component>> paifangkou = queryComponentsWithLayerNameAndGeometry(geometry, "排水口");
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(yinjin);
        observables.add(yushuikou);
        observables.add(paifangkou);
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查窨井、雨水口、排水口
     */
    public Observable<List<Component>> queryPrimaryComponentsPSK(final Geometry geometry) {
        Observable<List<Component>> yinjin = queryComponentsWithLayerNameAndGeometry(geometry, "窨井");
        Observable<List<Component>> yushuikou = queryComponentsWithLayerNameAndGeometry(geometry, "雨水口");
        Observable<List<Component>> paifangkou = queryComponentsWithLayerNameAndGeometry(geometry, "排水口");
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(yinjin);
        observables.add(yushuikou);
        observables.add(paifangkou);
        return Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 只查窨井、雨水口、排水口
     */
    public Observable<List<Component>> queryComponentsForUpload(final Geometry geometry) {
        String layerUrl = "";
        if (StringUtil.isEmpty(LayerUrlConstant.uploadServerUrl)) {
            layerUrl = getLayerUrlByLayName(PatrolLayerPresenter.UPLOAD_LAYER_NAME);
        } else {
            layerUrl = LayerUrlConstant.uploadServerUrl + "/0";
        }
        if (StringUtil.isEmpty(layerUrl)) {
            return null;
        }
        return queryComponents(geometry, layerUrl, PatrolLayerPresenter.UPLOAD_LAYER_NAME).subscribeOn(Schedulers.io());
    }

    /**
     * 只查数据上报图层
     */
    public void queryComponentsUpload(final Geometry geometry, final Callback2<List<Component>> callback) {
        queryComponentsForUpload(geometry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查排水管道
     */
    public void queryComponentsForDoubt(final Geometry geometry, final Callback2<List<Component>> callback) {
//        Observable<List<Component>> yinjin = queryComponentsForUpload(geometry);
        Observable<List<Component>> yushuikou = queryComponentsPSGD(geometry, PatrolLayerPresenter.RIVER_DOUBT_LAYER_NAME, PatrolLayerPresenter.RIVER_DOUBT_LAYER_NAME, 0);
        List<Observable<List<Component>>> observables = new ArrayList<>();
//        observables.add(yinjin);
        observables.add(yushuikou);
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查排水单元
     */
    public void queryComponentsForPSDY(final Geometry geometry, final Callback2<List<Component>> callback) {
//        Observable<List<Component>> yinjin = queryComponentsForUpload(geometry);
        Observable<List<Component>> yushuikou0 = queryComponentsPSGD(geometry, PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2, PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2, 0);
//        Observable<List<Component>> yushuikou1 = queryComponentsPSGD(geometry, PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2, PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2, 1);
//        Observable<List<Component>> yushuikou2 = queryComponentsPSGD(geometry, PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2, PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2, 2);
//        Observable<List<Component>> yushuikou3 = queryComponentsPSGD(geometry, PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2, PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2, 3);
//        Observable<List<Component>> yushuikou4 = queryComponentsPSGD(geometry, PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2, PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2, 4);
//        Observable<List<Component>> yushuikou5 = queryComponentsPSGD(geometry, PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2, PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2, 5);
        List<Observable<List<Component>>> observables = new ArrayList<>();
//        observables.add(yinjin);
        observables.add(yushuikou0);
//        observables.add(yushuikou1);
//        observables.add(yushuikou2);
//        observables.add(yushuikou3);
//        observables.add(yushuikou4);
//        observables.add(yushuikou5);
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查排水管道
     */
    public void queryComponentsForPipe(final Geometry geometry, final Callback2<List<Component>> callback) {
//        Observable<List<Component>> yinjin = queryComponentsForUpload(geometry);
        Observable<List<Component>> yushuikou = queryComponentsPSGD(geometry, "排水管道");
        Observable<List<Component>> psgdz = queryComponentsPSGD(geometry, "排水管道(中心城区)", "排水管道(中心城区)", 0);
        Observable<List<Component>> psgdz1 = queryComponentsPSGD(geometry, "排水管道(中心城区)", "排水沟渠(中心城区)", 1);
        List<Observable<List<Component>>> observables = new ArrayList<>();
//        observables.add(yinjin);
        observables.add(yushuikou);
        observables.add(psgdz);
        observables.add(psgdz1);

        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查排水管道，只查可见图层
     */
    public void queryComponentsForPipe(final Geometry geometry, final Callback2<List<Component>> callback, final List<LayerInfo> visibleLayerInfos) {
////        Observable<List<Component>> yinjin = queryComponentsForUpload(geometry);
//        Observable<List<Component>> yushuikou = queryComponentsPSGD(geometry, "排水管道");
//        Observable<List<Component>> psgdz = queryComponentsPSGD(geometry, "排水管道(中心城区)","排水管道(中心城区)",0);
//        Observable<List<Component>> psgdz1 = queryComponentsPSGD(geometry, "排水管道(中心城区)","排水沟渠(中心城区)",1);
//        List<Observable<List<Component>>> observables = new ArrayList<>();
////        observables.add(yinjin);
//        observables.add(yushuikou);
//        observables.add(psgdz);
//        observables.add(psgdz1);
        List<Observable<List<Component>>> observables = new ArrayList<>();
        for (LayerInfo visibleLayerInfo : visibleLayerInfos) {
            if ("排水管道".equals(visibleLayerInfo.getLayerName())) {
                Observable<List<Component>> yushuikou = queryComponentsPSGD(geometry, "排水管道");
                observables.add(yushuikou);
            } else if ("排水管道(中心城区)".equals(visibleLayerInfo.getLayerName())) {
                Observable<List<Component>> psgdz = queryComponentsPSGD(geometry, "排水管道(中心城区)", "排水管道(中心城区)", 0);
                Observable<List<Component>> psgdz1 = queryComponentsPSGD(geometry, "排水管道(中心城区)", "排水沟渠(中心城区)", 1);
                Observable<List<Component>> psgdz2= queryComponentsPSGD(geometry, "排水管道(黄埔管道)", "排水管道(黄埔管道)", 0);
                Observable<List<Component>> psgdz3= queryComponentsPSGD(geometry, "排水管道(黄埔管道)", "排水管道(黄埔管道)", 1);
                observables.add(psgdz);
                observables.add(psgdz1);
                observables.add(psgdz2);
                observables.add(psgdz3);
            }
        }
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查排水管井
     */
    public void queryComponentsForWell(final Geometry geometry, final Callback2<List<Component>> callback) {
//        Observable<List<Component>> yinjin = queryComponentsForUpload(geometry);
        Observable<List<Component>> yushuikou = queryComponentsPSGD(geometry, "排水管井");
        Observable<List<Component>> psgjz = queryComponentsPSGD(geometry, "排水管井(中心城区)", "窨井(中心城区)", 0);
        Observable<List<Component>> psgjz1 = queryComponentsPSGD(geometry, "排水管井(黄埔管井)", "窨井(中心城区)", 0);
        Observable<List<Component>> psgjz2 = queryComponentsPSGD(geometry, "排水管井(黄埔管井)", "雨水口(中心城区)", 1);
        Observable<List<Component>> psgjz3 = queryComponentsPSGD(geometry, "排水管井(黄埔管井)", "排水口(中心城区)", 2);
        List<Observable<List<Component>>> observables = new ArrayList<>();
//        observables.add(yinjin);
        observables.add(yushuikou);
        observables.add(psgjz);
        observables.add(psgjz1);
        observables.add(psgjz2);
        observables.add(psgjz3);
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查排水管井，只查可见图层
     */
    public void queryComponentsForWell(final Geometry geometry, final Callback2<List<Component>> callback, final List<LayerInfo> visibleLayerInfos) {
////        Observable<List<Component>> yinjin = queryComponentsForUpload(geometry);
//        Observable<List<Component>> yushuikou = queryComponentsPSGD(geometry, "排水管井");
//        Observable<List<Component>> psgjz = queryComponentsPSGD(geometry, "排水管井(中心城区)", "窨井(中心城区)", 0);
//        List<Observable<List<Component>>> observables = new ArrayList<>();
////        observables.add(yinjin);
//        observables.add(yushuikou);
//        observables.add(psgjz);
        List<Observable<List<Component>>> observables = new ArrayList<>();
        for (LayerInfo visibleLayerInfo : visibleLayerInfos) {
            if ("排水管井".equals(visibleLayerInfo.getLayerName())) {
                Observable<List<Component>> yushuikou = queryComponentsPSGD(geometry, "排水管井");
                observables.add(yushuikou);
            }else if ("排水管井(中心城区)".equals(visibleLayerInfo.getLayerName())) {
                Observable<List<Component>> psgjz = queryComponentsPSGD(geometry, "排水管井(中心城区)", "窨井(中心城区)", 0);
                Observable<List<Component>> psgjz1 = queryComponentsHP(geometry, "排水管井(黄埔管井)", "窨井(中心城区)", 0);
                Observable<List<Component>> psgjz2 = queryComponentsHP(geometry, "排水管井(黄埔管井)", "雨水口(中心城区)", 1);
                Observable<List<Component>> psgjz3 = queryComponentsHP(geometry, "排水管井(黄埔管井)", "排水口(中心城区)", 2);
                observables.add(psgjz);
                observables.add(psgjz1);
                observables.add(psgjz2);
                observables.add(psgjz3);
            }
        }
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查管道和管线
     */
    public void queryComponentsForAll(final Geometry geometry, final Callback2<List<Component>> callback) {
        Observable<List<Component>> yushuikou = queryComponentsPSGD(geometry, "排水管井");
        Observable<List<Component>> psgd = queryComponentsPSGD(geometry, "排水管道");
        Observable<List<Component>> psgjz = queryComponentsPSGD(geometry, "排水管井(中心城区)", "窨井(中心城区)", 0);
        Observable<List<Component>> psgdz = queryComponentsPSGD(geometry, "排水管道(中心城区)", "排水管道(中心城区)", 0);
        Observable<List<Component>> psgdz1 = queryComponentsPSGD(geometry, "排水管道(中心城区)", "排水沟渠(中心城区)", 1);
        Observable<List<Component>> psgdhp1 = queryComponentsPSGD(geometry, "排水管道(黄埔管道)", "排水管道(中心城区)", 0);
        Observable<List<Component>> psgdhp2 = queryComponentsPSGD(geometry, "排水管道(黄埔管道)", "排水沟渠(中心城区)", 1);
        Observable<List<Component>> psgjhp = queryComponentsPSGD(geometry, "排水管井(黄埔管井)", "窨井(中心城区)", 0);
        Observable<List<Component>> cunyimian = queryComponentsPSGD(geometry, PatrolLayerPresenter.RIVER_DOUBT_LAYER_NAME, PatrolLayerPresenter.RIVER_DOUBT_LAYER_NAME, 0);
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(yushuikou);
        observables.add(psgjz);
        observables.add(psgjhp);

        observables.add(psgd);
        observables.add(psgdz);
        observables.add(psgdz1);
        observables.add(psgdhp1);
        observables.add(psgdhp2);
        if(cunyimian != null) {
            observables.add(cunyimian);
        }
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查管道和管线,只查可见图层
     */
    public void queryComponentsForAll(final Geometry geometry, final Callback2<List<Component>> callback, final List<LayerInfo> visibleLayerInfos,boolean canSearchJb) {
//        Observable<List<Component>> yushuikou = queryComponentsPSGD(geometry, "排水管井");
//        Observable<List<Component>> psgd = queryComponentsPSGD(geometry, "排水管道");
//        Observable<List<Component>> psgjz = queryComponentsPSGD(geometry, "排水管井(中心城区)", "窨井(中心城区)", 0);
//        Observable<List<Component>> psgdz = queryComponentsPSGD(geometry, "排水管道(中心城区)", "排水管道(中心城区)", 0);
//        Observable<List<Component>> psgdz1 = queryComponentsPSGD(geometry, "排水管道(中心城区)", "排水沟渠(中心城区)", 1);
//        Observable<List<Component>> cunyimian = queryComponentsPSGD(geometry, PatrolLayerPresenter.RIVER_DOUBT_LAYER_NAME, PatrolLayerPresenter.RIVER_DOUBT_LAYER_NAME, 0);
//        List<Observable<List<Component>>> observables = new ArrayList<>();
//        observables.add(yushuikou);
//        observables.add(psgjz);
//        observables.add(psgd);
//        observables.add(psgdz);
//        observables.add(psgdz1);
//        observables.add(cunyimian);

        List<Observable<List<Component>>> observables = new ArrayList<>();
        Observable<List<Component>> jbj1 = null;
        Observable<List<Component>> jbj2 = null;
        Observable<List<Component>> jbj3 = null;
        Observable<List<Component>> yushuikou = null;
        Observable<List<Component>> psgjz = null;
        Observable<List<Component>> psgjz1 = null;
        Observable<List<Component>> psgjz2 = null;
        Observable<List<Component>> psgjz3 = null;
        Observable<List<Component>> yushuikou2 = null;
        Observable<List<Component>> psgdz = null;
        Observable<List<Component>> psgdz1 = null;
        Observable<List<Component>> psgdz2 = null;
        Observable<List<Component>> psgdz3 = null;
        Observable<List<Component>> gjjd = null;
        Observable<List<Component>> psdy = null;

        for (LayerInfo visibleLayerInfo : visibleLayerInfos) {
            if("接驳井.".equals(visibleLayerInfo.getLayerName())){
                jbj1 = queryComponentsPSGD(geometry,"接驳井.", "接驳井.", 0);
                jbj2 = queryComponentsPSGD(geometry, "接驳井.", "接驳井.", 2);
                jbj3 = queryComponentsPSGD(geometry, "接驳井.", "接驳井.", 4);
            } else if ("排水管井".equals(visibleLayerInfo.getLayerName())) {
                yushuikou = queryComponentsPSGD(geometry, "排水管井");
            } else if ("排水管井(中心城区)".equals(visibleLayerInfo.getLayerName())) {
                psgjz = queryComponentsPSGD(geometry, "排水管井(中心城区)", "窨井(中心城区)", 0);
            }  else if ("排水管井(黄埔管井)".equals(visibleLayerInfo.getLayerName())) {
                psgjz1 = queryComponentsHP(geometry, "排水管井(黄埔管井)", "窨井(中心城区)", 0);
                psgjz2 = queryComponentsHP(geometry, "排水管井(黄埔管井)", "雨水口(中心城区)", 1);
                psgjz3 = queryComponentsHP(geometry, "排水管井(黄埔管井)", "排水口(中心城区)", 2);
            } else if ("排水管道".equals(visibleLayerInfo.getLayerName())) {
                yushuikou2 = queryComponentsPSGD(geometry, "排水管道");
            } else if ("排水管道(中心城区)".equals(visibleLayerInfo.getLayerName())) {
                psgdz = queryComponentsPSGD(geometry, "排水管道(中心城区)", "排水管道(中心城区)", 0);
                psgdz1 = queryComponentsPSGD(geometry, "排水管道(中心城区)", "排水沟渠(中心城区)", 1);
            }  else if ("排水管道(黄埔管道)".equals(visibleLayerInfo.getLayerName())) {
                psgdz2 = queryComponentsPSGD(geometry, "排水管道(黄埔管道)", "排水管道(中心城区)", 0);
                psgdz3 = queryComponentsPSGD(geometry, "排水管道(黄埔管道)", "排水沟渠(中心城区)", 1);
            } else if("关键节点".equals(visibleLayerInfo.getLayerName())){
                gjjd = queryComponentsPSGD(geometry,PatrolLayerPresenter.DRAINAGE_GJJD);
            }
//            else if("广州排水单元".equals(visibleLayerInfo.getLayerName())){
//                psdy = queryComponentsPSGD(geometry, PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2, PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2, 0);
//            }
        }
        /**
         * 关键节点图层，为防止点查重复记录，进行下列特殊处理
         */
        if(yushuikou == null && gjjd != null){
            observables.add(gjjd);
        }
        if(jbj1 != null && canSearchJb) {
            observables.add(jbj1);
        }
        if(jbj2 != null && canSearchJb) {
            observables.add(jbj2);
        }
        if(jbj3 != null && canSearchJb) {
            observables.add(jbj3);
        }
//        if(psdy != null){
//            observables.add(psdy);
//        }
        if(yushuikou != null) {
            observables.add(yushuikou);
        }
        if(psgjz != null) {
            observables.add(psgjz);
        }
        if(psgjz1 != null) {
            observables.add(psgjz1);
        }
        if(psgjz2 != null) {
            observables.add(psgjz2);
        }
        if(psgjz3 != null) {
            observables.add(psgjz3);
        }
        if(yushuikou2 != null) {
            observables.add(yushuikou2);
        }
        if(psgdz != null) {
            observables.add(psgdz);
        }
        if(psgdz1 != null) {
            observables.add(psgdz1);
        }
        if(psgdz2 != null) {
            observables.add(psgdz2);
        }
        if(psgdz3 != null) {
            observables.add(psgdz3);
        }
        if(observables.isEmpty()){
            callback.onFail(new Exception(""));
            return;
        }
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查管道和管线,只查可见图层
     */
    public void queryComponentsForJBJ(final Geometry geometry, final Callback2<List<Component>> callback, final List<LayerInfo> visibleLayerInfos) {
//        Observable<List<Component>> yushuikou = queryComponentsPSGD(geometry, "排水管井");
//        Observable<List<Component>> psgd = queryComponentsPSGD(geometry, "排水管道");
//        Observable<List<Component>> psgjz = queryComponentsPSGD(geometry, "排水管井(中心城区)", "窨井(中心城区)", 0);
//        Observable<List<Component>> psgdz = queryComponentsPSGD(geometry, "排水管道(中心城区)", "排水管道(中心城区)", 0);
//        Observable<List<Component>> psgdz1 = queryComponentsPSGD(geometry, "排水管道(中心城区)", "排水沟渠(中心城区)", 1);
//        Observable<List<Component>> cunyimian = queryComponentsPSGD(geometry, PatrolLayerPresenter.RIVER_DOUBT_LAYER_NAME, PatrolLayerPresenter.RIVER_DOUBT_LAYER_NAME, 0);
//        List<Observable<List<Component>>> observables = new ArrayList<>();
//        observables.add(yushuikou);
//        observables.add(psgjz);
//        observables.add(psgd);
//        observables.add(psgdz);
//        observables.add(psgdz1);
//        observables.add(cunyimian);

        List<Observable<List<Component>>> observables = new ArrayList<>();
        for (LayerInfo visibleLayerInfo : visibleLayerInfos) {
            if("接驳井.".equals(visibleLayerInfo.getLayerName())){
                Observable<List<Component>> jbj1 = queryComponentsPSGD(geometry,"接驳井.", "接驳井.", 0);
                Observable<List<Component>> jbj2 = queryComponentsPSGD(geometry, "接驳井.", "接驳井.", 2);
                Observable<List<Component>> jbj3 = queryComponentsPSGD(geometry, "接驳井.", "接驳井.", 4);
                observables.add(jbj1);
                observables.add(jbj2);
                observables.add(jbj3);
            }
        }
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查巡检日志(搜索)
     */
    public void queryPrimaryComponentsForObjectIdJounal(final String objectId, final Callback2<List<Component>> callback) {
        Observable<List<Component>> yinjin = queryComponentsForObjectId(objectId, getLayerUrlByLayName(PatrolLayerPresenter.JOURNAL_LAYER_NAME), PatrolLayerPresenter.JOURNAL_LAYER_NAME + "/0");
//        Observable<List<Component>> yinjin = queryComponentsWithLayerNameAndObjectId(objectId, "窨井");
//        Observable<List<Component>> yushuikou = queryComponentsWithLayerNameAndObjectId(objectId, "雨水口");
//        Observable<List<Component>> paifangkou = queryComponentsWithLayerNameAndObjectId(objectId, "排水口");
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(yinjin);
//        observables.add(yushuikou);
//        observables.add(paifangkou);
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查窨井、雨水口、排水口(搜索)
     */
    public void queryPrimaryComponentsForObjectId(final String objectId, final Callback2<List<Component>> callback) {
        final String oldComponentLayerUrl = LayerUrlConstant.getLayerUrlByLayerName("窨井");
        Observable<List<Component>> yinjin = queryComponentsForObjectId(objectId, oldComponentLayerUrl, "窨井");
//        Observable<List<Component>> yinjin = queryComponentsWithLayerNameAndObjectId(objectId, "窨井");
//        Observable<List<Component>> yushuikou = queryComponentsWithLayerNameAndObjectId(objectId, "雨水口");
//        Observable<List<Component>> paifangkou = queryComponentsWithLayerNameAndObjectId(objectId, "排水口");
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(yinjin);
//        observables.add(yushuikou);
//        observables.add(paifangkou);
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查窨井、雨水口、排水口(搜索)
     */
    public void queryPrimaryPipeForObjectId(final String objectId, final Callback2<List<Component>> callback) {
        final String oldComponentLayerUrl = getLayerUrlByLayName(PatrolLayerPresenter.DRAINAGE_PIPELINE) + "/0";
        Observable<List<Component>> yinjin = queryComponentsForObjectId(objectId, oldComponentLayerUrl, "窨井");
//        Observable<List<Component>> yinjin = queryComponentsWithLayerNameAndObjectId(objectId, "窨井");
//        Observable<List<Component>> yushuikou = queryComponentsWithLayerNameAndObjectId(objectId, "雨水口");
//        Observable<List<Component>> paifangkou = queryComponentsWithLayerNameAndObjectId(objectId, "排水口");
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(yinjin);
//        observables.add(yushuikou);
//        observables.add(paifangkou);
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查窨井、雨水口、排水口(搜索)
     */
    public void queryPrimaryComponentsForObjectId2(final String objectId, final Callback2<List<Component>> callback) {
        final String oldComponentLayerUrl = getLayerUrlForName("排水管井") + "/0";
        String psgjz = getLayerUrlForName("排水管井(中心城区)") + "/0";
        String psgjhp = getLayerUrlForName("排水管井(黄埔管井)") + "/0";
        String psgd = getLayerUrlForName("排水管道") + "/0";
        String psgdz = getLayerUrlForName("排水管道(中心城区)") + "/0";
        String psgqz = getLayerUrlForName("排水管道(中心城区)") + "/1";
        String psgqhp1 = getLayerUrlForName("排水管道(黄埔管道)") + "/0";
        String psgqhp2 = getLayerUrlForName("排水管道(黄埔管道)") + "/1";
        Observable<List<Component>> yinjin = queryComponentsForObjectId2(objectId, oldComponentLayerUrl, "窨井");
        Observable<List<Component>> yinjinz = queryComponentsForObjectId2(objectId, psgjz, "窨井");
        Observable<List<Component>> yinjinhp= queryComponentsForObjectId2(objectId, psgjhp, "窨井");
        Observable<List<Component>> paisgd = queryComponentsForObjectId2(objectId, psgd, "窨井(中心城区)");
        Observable<List<Component>> paisgdz = queryComponentsForObjectId2(objectId, psgdz, "排水管道(中心城区)");
        Observable<List<Component>> paisgqz = queryComponentsForObjectId2(objectId, psgqz, "排水沟渠(中心城区)");
        Observable<List<Component>> paisghp1 = queryComponentsForObjectId2(objectId, psgqhp1, "排水管道(黄埔管道)");
        Observable<List<Component>> paisghp2 = queryComponentsForObjectId2(objectId, psgqhp2, "排水管道(黄埔管道)");
//        Observable<List<Component>> yinjin = queryComponentsWithLayerNameAndObjectId(objectId, "窨井");
//        Observable<List<Component>> yushuikou = queryComponentsWithLayerNameAndObjectId(objectId, "雨水口");
//        Observable<List<Component>> river = queryComponentsHCName(objectId, "河涌");
//        Observable<List<Component>> paifangkou = queryComponentsPSK(objectId, "排水口");
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(yinjin);
        observables.add(yinjinz);
        observables.add(yinjinhp);
        observables.add(paisgd);
        observables.add(paisgdz);
        observables.add(paisgqz);
        observables.add(paisghp1);
        observables.add(paisghp2);
//        observables.add(river);
//        observables.add(paifangkou);
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }


    /**
     * 只查窨井、雨水口、排水口(搜索)
     */
    public void queryAllRoadByKeyword(final String objectId, final Callback2<List<Component>> callback) {
        String road1 = getLayerUrlForName("道路") + "/0";
        String road2 = getLayerUrlForName("道路") + "/1";
        String road3 = getLayerUrlForName("道路") + "/2";
        Observable<List<Component>> daolu1 = queryAddressComponentsForName(objectId, road1, "道路");
        Observable<List<Component>> daolu2 = queryAddressComponentsForName(objectId, road2, "道路");
        Observable<List<Component>> daolu3 = queryAddressComponentsForName(objectId, road3, "道路");
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(daolu1);
        observables.add(daolu2);
        observables.add(daolu3);
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 只查窨井(搜索)
     */
    public Observable<List<Component>> queryPSGJForObjectId(final String objectId) {
        final String oldComponentLayerUrl = getLayerUrlForName("排水管井") + "/0";
        Observable<List<Component>> yinjin = queryComponentsForObjectId2(objectId, oldComponentLayerUrl, "窨井");
//        Observable<List<Component>> yinjin = queryComponentsWithLayerNameAndObjectId(objectId, "窨井");
//        Observable<List<Component>> yushuikou = queryComponentsWithLayerNameAndObjectId(objectId, "雨水口");
//        Observable<List<Component>> paifangkou = queryComponentsPSK(objectId, "排水口");
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(yinjin);
//        observables.add(paifangkou);
        return Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io());
    }

    /**
     * 只查窨井、雨水口、排水口(搜索)
     */
    public Observable<List<Component>> queryPrimaryComponentsPSKForObjectId(final String objectId) {
        final String oldComponentLayerUrl = LayerUrlConstant.getLayerUrlByLayerName("窨井");
        Observable<List<Component>> yinjin = queryComponentsForObjectId2(objectId, oldComponentLayerUrl, "窨井");
//        Observable<List<Component>> yinjin = queryComponentsWithLayerNameAndObjectId(objectId, "窨井");
//        Observable<List<Component>> yushuikou = queryComponentsWithLayerNameAndObjectId(objectId, "雨水口");
        Observable<List<Component>> river = queryComponentsHCName(objectId, "河涌");
        Observable<List<Component>> paifangkou = queryComponentsPSK(objectId, "排水口");
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(yinjin);
        observables.add(river);
        observables.add(paifangkou);
        return Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io());
    }

    /**
     * identify方式只查窨井、雨水口、排水口
     */
    public void identifyPrimaryComponents(final Geometry geometry, final MapView mapView, final Callback2<List<Component>> callback) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                int yinjinLayerId = LayerUrlConstant.componentIds[LayerUrlConstant.getIndexlByLayerName("窨井")];
                int yushuikouLayerId = LayerUrlConstant.componentIds[LayerUrlConstant.getIndexlByLayerName("雨水口")];
//                int paifangkouLayerId = LayerUrlConstant.componentIds[LayerUrlConstant.getIndexlByLayerName("排水口")];
                IdentifyParameters parameters = new IdentifyParameters();
                parameters.setGeometry(geometry);
                Envelope extent = new Envelope();
                mapView.getExtent().queryEnvelope(extent);
                parameters.setMapExtent(extent);
                parameters.setMapWidth(mapView.getWidth());
                parameters.setMapHeight(mapView.getHeight());
                parameters.setSpatialReference(mapView.getSpatialReference());
                parameters.setDPI(98);  // TODO 是否需要动态设置
//                parameters.setLayers(new int[]{yinjinLayerId, yushuikouLayerId, paifangkouLayerId});
                parameters.setLayers(new int[]{yinjinLayerId, yushuikouLayerId});
                parameters.setReturnGeometry(true);
                parameters.setTolerance(20);//默认20
                parameters.setLayerMode(IdentifyParameters.ALL_LAYERS);
                IdentifyTask identifyTask = new IdentifyTask(LayerUrlConstant.mapServerUrl);
                try {
                    IdentifyResult[] identifyResults = identifyTask.execute(parameters);
                    if (identifyResults != null && identifyResults.length > 0) {
                        for (IdentifyResult identifyResult : identifyResults) {
                            Component component = new Component();
                            component.setDisplayFieldName(identifyResult.getDisplayFieldName());
                            component.setLayerName(identifyResult.getLayerName());
                            String layerUrl =
                                    LayerUrlConstant.mapServerUrl
                                            + LayerUrlConstant.componentIds[
                                            LayerUrlConstant.getIndexlByLayerName(identifyResult.getLayerName())];
                            component.setLayerUrl(layerUrl);
                            Graphic graphic = new Graphic(identifyResult.getGeometry(), null, identifyResult.getAttributes());
                            component.setGraphic(graphic);
                            Object o = graphic.getAttributes().get(ComponentFieldKeyConstant.OBJECTID);
                            if (o != null && o instanceof Integer)
                                component.setObjectId((Integer) o); //按照道理objectId一定是integer的
                        }
                    }
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }


    /**
     * 只查拍门、阀门、溢流堰
     */
    public void querySecondComponents(final Geometry geometry, final Callback2<List<Component>> callback) {
        Observable<List<Component>> yinjin = queryComponentsWithLayerNameAndGeometry(geometry, "拍门");
        Observable<List<Component>> yushuikou = queryComponentsWithLayerNameAndGeometry(geometry, "阀门");
        Observable<List<Component>> paifangkou = queryComponentsWithLayerNameAndGeometry(geometry, "溢流堰");
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(yinjin);
        observables.add(yushuikou);
        observables.add(paifangkou);
        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    if (o == null) {
                        continue;
                    }
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }


    /**
     * 查全部点，不查线
     */
    public void queryAllComponentsExceptLine(final Geometry geometry, final Callback2<List<Component>> callback) {
        Observable<List<Component>> yinjin = queryComponentsWithLayerNameAndGeometry(geometry, "窨井");
        Observable<List<Component>> yushuikou = queryComponentsWithLayerNameAndGeometry(geometry, "雨水口");
//        Observable<List<Component>> paifangkou = queryComponentsWithLayerNameAndGeometry(geometry, "排水口");
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(yinjin);
        observables.add(yushuikou);
//        observables.add(paifangkou);

        Observable<List<Component>> paiment = queryComponentsWithLayerNameAndGeometry(geometry, "拍门");
        Observable<List<Component>> fament = queryComponentsWithLayerNameAndGeometry(geometry, "阀门");
        Observable<List<Component>> yiliuyan = queryComponentsWithLayerNameAndGeometry(geometry, "溢流堰");
        observables.add(paiment);
        observables.add(fament);
        observables.add(yiliuyan);

        Observable.zip(observables, new FuncN<List<Component>>() {
            @Override
            public List<Component> call(Object... args) {
                if (args.length == 0) {
                    return null;
                }
                List<Component> componentList = new ArrayList<>();
                for (Object o : args) {
                    List<Component> components = (List<Component>) o;
                    componentList.addAll(components);
                }
                return componentList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<Component> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }


    /**
     * 查询指定范围内的设施
     *
     * @param geometry 查询范围
     * @param url      旧图层URL
     * @param newUrl   新图层URL
     * @param callback 　结果回调
     */
    public void queryComponents(final Geometry geometry, final String url, final String newUrl, final Callback2<List<QueryFeatureSet>> callback) {
        final List<QueryFeatureSet> queryFeatureSetList = new ArrayList<>();
        Observable.create(new Observable.OnSubscribe<List<QueryFeatureSet>>() {
            @Override
            public void call(final Subscriber<? super List<QueryFeatureSet>> subscriber) {
                final Query query = getQueryByGeometry(geometry);
                final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                        newUrl, ArcGISFeatureLayer.MODE.SNAPSHOT);
                layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                    @Override
                    public void onCallback(FeatureSet featureSet) {
                        QueryFeatureSet queryFeatureSet = new QueryFeatureSet();
                        queryFeatureSet.setLayerUrl(newUrl);
                        queryFeatureSet.setFeatureSet(featureSet);
                        for (int i = 0; i < LayerUrlConstant.newComponentUrls.length; ++i) {
                            if (newUrl.equals(LayerUrlConstant.newComponentUrls[i])) {
                                queryFeatureSet.setLayerName(LayerUrlConstant.componentNames[i]);
                                break;
                            }
                        }
                        queryFeatureSetList.add(queryFeatureSet);

                        final ArcGISFeatureLayer newlayer = new ArcGISFeatureLayer(
                                url, ArcGISFeatureLayer.MODE.SNAPSHOT);
                        newlayer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                            @Override
                            public void onCallback(FeatureSet featureSet) {

                                LogUtil.d("部件查询点返回结果：" + featureSet.getGraphics().length);
                                QueryFeatureSet queryFeatureSet = new QueryFeatureSet();
                                queryFeatureSet.setLayerUrl(url);
                                queryFeatureSet.setFeatureSet(featureSet);
                                for (int i = 0; i < LayerUrlConstant.componentUrls.length; ++i) {
                                    if (url.equals(LayerUrlConstant.componentUrls[i])) {
                                        queryFeatureSet.setLayerName(LayerUrlConstant.componentNames[i]);
                                        break;
                                    }
                                }
                                queryFeatureSetList.add(queryFeatureSet);
                                subscriber.onNext(queryFeatureSetList);
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                subscriber.onError(throwable);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<QueryFeatureSet>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callback.onFail(new Exception(throwable));
                    }

                    @Override
                    public void onNext(List<QueryFeatureSet> queryFeatureSetList) {
                        callback.onSuccess(queryFeatureSetList);
                    }
                });


    }

    /**
     * 查询指定范围内的设施
     *
     * @param geometry 查询范围
     * @param url      图层URL
     * @param callback 　结果回调
     */
    public void queryComponents(final Geometry geometry, final String url, final Callback2<QueryFeatureSet> callback) {
        Observable.create(new Observable.OnSubscribe<QueryFeatureSet>() {
            @Override
            public void call(final Subscriber<? super QueryFeatureSet> subscriber) {
                final Query query = getQueryByGeometry(geometry);
                final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                        url, ArcGISFeatureLayer.MODE.SNAPSHOT);
                layer.queryFeatures(query, new CallbackListener<FeatureSet>() {


                    @Override
                    public void onCallback(FeatureSet featureSet) {

                        LogUtil.d("部件查询点返回结果：" + featureSet.getGraphics().length);
                        QueryFeatureSet queryFeatureSet = new QueryFeatureSet();
                        queryFeatureSet.setLayerUrl(url);
                        queryFeatureSet.setFeatureSet(featureSet);
                        for (int i = 0; i < LayerUrlConstant.componentUrls.length; ++i) {
                            if (url.equals(LayerUrlConstant.componentUrls[i])) {
                                queryFeatureSet.setLayerName(LayerUrlConstant.componentNames[i]);
                                break;
                            }
                        }
                        subscriber.onNext(queryFeatureSet);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QueryFeatureSet>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callback.onFail(new Exception(throwable));
                    }

                    @Override
                    public void onNext(QueryFeatureSet queryFeatureSet) {
                        callback.onSuccess(queryFeatureSet);
                    }
                });


    }

    /**
     * 已知layerUrl情况下查询指定范围内的设施详细信息
     * 查询设施的最后一条记录
     *
     * @param geometry
     * @param url
     */
    public Observable<List<Component>> queryComponentsAfterFilter(final Geometry geometry, final String url) {
        return Observable.create(new Observable.OnSubscribe<List<Component>>() {
            @Override
            public void call(final Subscriber<? super List<Component>> subscriber) {
                final Query query = getQueryByGeometry(geometry);
                final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                        url, ArcGISFeatureLayer.MODE.SNAPSHOT);
                layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                    @Override
                    public void onCallback(FeatureSet featureSet) {
                        if (ListUtil.isEmpty(featureSet.getGraphics())) {
                            subscriber.onNext(new ArrayList<Component>());
                            subscriber.onCompleted();
                            return;
                        }
                        List<Component> componentList = new ArrayList<>();
                        for (Graphic graphic : featureSet.getGraphics()) {
                            Component component = new Component();
                            component.setLayerUrl(url);
                            component.setLayerName(layer.getName());
                            component.setDisplayFieldName(featureSet.getDisplayFieldName());
                            component.setGraphic(graphic);
                            Object o = graphic.getAttributes().get(featureSet.getObjectIdFieldName());
                            if (o != null && o instanceof Integer) {
                                component.setObjectId((Integer) o); //按照道理objectId一定是integer的
                            }
                            componentList.add(component);
                        }
                        subscriber.onNext(componentList);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }
                });
            }
        })
                .map(new Func1<List<Component>, List<Component>>() {
                    @Override
                    public List<Component> call(List<Component> components) {
                        ArrayList<Component> resultList = new ArrayList<>();
                        HashMap<Long, Component> map = new HashMap<>();
                        Long upDate = null;
                        Long tempDate = null;
                        Collection<Component> values = map.values();
                        Component next;
                        for (Component com : components) {
                            upDate = (Long) com.getGraphic().getAttributes().get("UPDATE_TIME");
                            for (Iterator<Component> it2 = values.iterator(); it2.hasNext(); ) {
                                next = it2.next();
                                tempDate = (Long) next.getGraphic().getAttributes().get("UPDATE_TIME");
                                if (((double) next.getGraphic().getAttributes().get("X")) == ((double) com.getGraphic().getAttributes().get("X"))
                                        && ((double) next.getGraphic().getAttributes().get("Y")) == ((double) com.getGraphic().getAttributes().get("Y"))) {
                                    //同一个点取最后的结果
                                    if (tempDate < upDate) {
                                        map.remove(tempDate);
                                        map.put(upDate, com);
                                    }
                                } else {
                                    //新的点
                                    map.put(upDate, com);
                                }
                            }
                            if (map.size() == 0) {
                                map.put(upDate, com);
                            }
                        }
                        Collection<Component> finalValues = map.values();
                        for (Iterator<Component> it2 = finalValues.iterator(); it2.hasNext(); ) {
                            resultList.add(it2.next());
                        }
                        return resultList;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    /**
     * 已知layerUrl情况下查询指定范围内的设施详细信息
     *
     * @param geometry
     * @param url
     */
    public Observable<List<Component>> queryComponents(final Geometry geometry, final String url, final String layerName) {
        return Observable.create(new Observable.OnSubscribe<List<Component>>() {
            @Override
            public void call(final Subscriber<? super List<Component>> subscriber) {
                Geometry newGeometry = null;
//                if(url.indexOf("/hppsgs/hppsgsWell")!=-1 || url.indexOf("hppsgs/hppsgsPipeCanal")!=-1){
//                    newGeometry = GeometryEngine.project(geometry,SpatialReference.create(CHENGQU_WIKI),SpatialReference.create(HUANGPU_WIKI));
//            }
                final Query query = getQueryByGeometry(newGeometry == null? geometry : newGeometry);
                final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                        url, ArcGISFeatureLayer.MODE.SNAPSHOT);
                layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                    @Override
                    public void onCallback(FeatureSet featureSet) {
                        if (ListUtil.isEmpty(featureSet.getGraphics())) {
                            subscriber.onNext(new ArrayList<Component>());
                            subscriber.onCompleted();
                            return;
                        }
                        List<Component> componentList = new ArrayList<>();
                        for (Graphic graphic : featureSet.getGraphics()) {
                            Component component = new Component();
                            component.setLayerUrl(url);
                            if (!StringUtil.isEmpty(layer.getName()) && layerNames.contains(layer.getName())) {
                                component.setLayerName(layer.getName());
                            } else {
                                component.setLayerName(String.valueOf(graphic.getAttributes().get("LAYER_NAME")));
                            }
                            if (StringUtil.isEmpty(component.getLayerName()) || "null".equals(component.getLayerName())) {
                                component.setLayerName(layerName);
                            }
                            component.setDisplayFieldName(featureSet.getDisplayFieldName());
//                            component.setFieldAlias(featureSet.getFieldAliases());
//                            component.setFields(featureSet.getFields());
                            component.setGraphic(graphic);
//                            component.setFieldAlias(featureSet.getFieldAliases());
                            Object o = graphic.getAttributes().get(featureSet.getObjectIdFieldName());
                            if (o != null && o instanceof Integer) {
                                component.setObjectId((Integer) o); //按照道理objectId一定是integer的
                            }
                            componentList.add(component);
                        }
                        subscriber.onNext(componentList);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io());
    }
    private Query getQueryByNameLike(String name) {
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setWhere("NAME like '%" + name + "%'");
        return query;
    }
    /**
     * 已知layerUrl情况下查询指定范围内的设施详细信息
     *
     * @param geometry
     * @param url
     */
    public Observable<List<Component>> queryHPComponents(final Geometry geometry, final String url, final String layerName) {
        return Observable.create(new Observable.OnSubscribe<List<Component>>() {
            @Override
            public void call(final Subscriber<? super List<Component>> subscriber) {
                Geometry newGeometry = null;
//                if(url.indexOf("/hppsgs/hppsgsWell")!=-1 || url.indexOf("hppsgs/hppsgsPipeCanal")!=-1){
//                    newGeometry = GeometryEngine.project(geometry,SpatialReference.create(CHENGQU_WIKI),SpatialReference.create(HUANGPU_WIKI));
//            }
                final Query query = getQueryByGeometry(newGeometry == null? geometry : newGeometry);
                final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                        url, ArcGISFeatureLayer.MODE.SNAPSHOT);
                layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                    @Override
                    public void onCallback(FeatureSet featureSet) {
                        if (ListUtil.isEmpty(featureSet.getGraphics())) {
                            subscriber.onNext(new ArrayList<Component>());
                            subscriber.onCompleted();
                            return;
                        }
                        List<Component> componentList = new ArrayList<>();
                        for (Graphic graphic : featureSet.getGraphics()) {
                            Component component = new Component();
                            component.setLayerUrl(url);
                            if(!StringUtil.isEmpty(layerName)){
                                component.setLayerName(layerName);
                            }else if (!StringUtil.isEmpty(layer.getName()) && layerNames.contains(layer.getName())) {
                                component.setLayerName(layer.getName());
                            } else {
                                component.setLayerName(String.valueOf(graphic.getAttributes().get("LAYER_NAME")));
                            }

                            component.setDisplayFieldName(featureSet.getDisplayFieldName());
//                            component.setFieldAlias(featureSet.getFieldAliases());
//                            component.setFields(featureSet.getFields());
                            component.setGraphic(graphic);
//                            component.setFieldAlias(featureSet.getFieldAliases());
                            Object o = graphic.getAttributes().get(featureSet.getObjectIdFieldName());
                            if (o != null && o instanceof Integer) {
                                component.setObjectId((Integer) o); //按照道理objectId一定是integer的
                            }
                            componentList.add(component);
                        }
                        subscriber.onNext(componentList);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io());
    }

    private Query getQueryByName(String name) {
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setWhere("NAME='" + name + "'");
        return query;
    }
    /**
     * 已知layerUrl情况下查询指定范围内的设施详细信息
     *
     * @param name
     * @param url
     * @return
     */
    public Observable<List<Component>> queryAddressComponentsForName(final String name, final String url, final String layerName) {
        return Observable.create(new Observable.OnSubscribe<List<Component>>() {
            @Override
            public void call(final Subscriber<? super List<Component>> subscriber) {
                final Query query = getQueryByNameLike(name);
                final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                        url, ArcGISFeatureLayer.MODE.SNAPSHOT);
                layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                    @Override
                    public void onCallback(FeatureSet featureSet) {
                        if (ListUtil.isEmpty(featureSet.getGraphics())) {
                            subscriber.onNext(new ArrayList<Component>());
                            subscriber.onCompleted();
                            return;
                        }
                        List<Component> componentList = new ArrayList<>();
                        for (Graphic graphic : featureSet.getGraphics()) {
                            Component component = new Component();
                            component.setLayerUrl(url);
                            if (StringUtil.isEmpty(layer.getName())) {
                                component.setLayerName(layer.getName());
                            } else {
                                component.setLayerName(layerName);
                            }
                            if (StringUtil.isEmpty(component.getLayerName())) {
                                component.setLayerName(layerName);
                            }
                            component.setDisplayFieldName(featureSet.getDisplayFieldName());
//                            component.setFieldAlias(featureSet.getFieldAliases());
//                            component.setFields(featureSet.getFields());
                            component.setGraphic(graphic);
//                            component.setFieldAlias(featureSet.getFieldAliases());
                            Object o = graphic.getAttributes().get(featureSet.getObjectIdFieldName());
                            if (o != null && o instanceof Integer) {
                                component.setObjectId((Integer) o); //按照道理objectId一定是integer的
                            }
                            componentList.add(component);
                        }
                        subscriber.onNext(componentList);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onNext(new ArrayList<Component>());
                        subscriber.onCompleted();
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io());
    }

    /**
     * 已知layerUrl情况下查询指定范围内的设施详细信息
     *
     * @param name
     * @param url
     * @return
     */
    public Observable<List<Component>> queryHCComponentsForName(final String name, final String url, final String layerName) {
        return Observable.create(new Observable.OnSubscribe<List<Component>>() {
            @Override
            public void call(final Subscriber<? super List<Component>> subscriber) {
                final Query query = getQueryByName(name);
                final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                        url, ArcGISFeatureLayer.MODE.SNAPSHOT);
                layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                    @Override
                    public void onCallback(FeatureSet featureSet) {
                        if (ListUtil.isEmpty(featureSet.getGraphics())) {
                            subscriber.onNext(new ArrayList<Component>());
                            subscriber.onCompleted();
                            return;
                        }
                        List<Component> componentList = new ArrayList<>();
                        for (Graphic graphic : featureSet.getGraphics()) {
                            Component component = new Component();
                            component.setLayerUrl(url);
                            if (StringUtil.isEmpty(layer.getName())) {
                                component.setLayerName(layer.getName());
                            } else {
                                component.setLayerName(layerName);
                            }
                            if (StringUtil.isEmpty(component.getLayerName())) {
                                component.setLayerName(layerName);
                            }
                            component.setDisplayFieldName(featureSet.getDisplayFieldName());
//                            component.setFieldAlias(featureSet.getFieldAliases());
//                            component.setFields(featureSet.getFields());
                            component.setGraphic(graphic);
//                            component.setFieldAlias(featureSet.getFieldAliases());
                            Object o = graphic.getAttributes().get(featureSet.getObjectIdFieldName());
                            if (o != null && o instanceof Integer) {
                                component.setObjectId((Integer) o); //按照道理objectId一定是integer的
                            }
                            componentList.add(component);
                        }
                        subscriber.onNext(componentList);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onNext(new ArrayList<Component>());
                        subscriber.onCompleted();
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io());
    }

    /**
     * 已知layerUrl情况下查询指定范围内的设施详细信息
     *
     * @param objectId
     * @param url
     * @return
     */
    public Observable<List<Component>> queryComponentsForObjectId(final String objectId, final String url, final String layerName) {
        return Observable.create(new Observable.OnSubscribe<List<Component>>() {
            @Override
            public void call(final Subscriber<? super List<Component>> subscriber) {
                final Query query = getQueryByObjectId(objectId);
                final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                        url, ArcGISFeatureLayer.MODE.SNAPSHOT);
                layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                    @Override
                    public void onCallback(FeatureSet featureSet) {
                        if (ListUtil.isEmpty(featureSet.getGraphics())) {
                            subscriber.onNext(new ArrayList<Component>());
                            subscriber.onCompleted();
                            return;
                        }
                        List<Component> componentList = new ArrayList<>();
                        for (Graphic graphic : featureSet.getGraphics()) {
                            Component component = new Component();
                            component.setLayerUrl(url);
                            if (StringUtil.isEmpty(layer.getName())) {
                                component.setLayerName(layer.getName());
                            } else {
                                component.setLayerName(layerName);
                            }
                            component.setDisplayFieldName(featureSet.getDisplayFieldName());
//                            component.setFieldAlias(featureSet.getFieldAliases());
//                            component.setFields(featureSet.getFields());
                            component.setGraphic(graphic);
//                            component.setFieldAlias(featureSet.getFieldAliases());
                            Object o = graphic.getAttributes().get(featureSet.getObjectIdFieldName());
                            if (o != null && o instanceof Integer) {
                                component.setObjectId((Integer) o); //按照道理objectId一定是integer的
                            }
                            componentList.add(component);
                        }
                        subscriber.onNext(componentList);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io());
    }

    /**
     * 已知layerUrl情况下查询指定范围内的设施详细信息
     *
     * @param objectId
     * @param url
     * @return
     */
    public Observable<List<Component>> queryComponentsForObjectId2(final String objectId, final String url, final String layerName) {
        return Observable.create(new Observable.OnSubscribe<List<Component>>() {
            @Override
            public void call(final Subscriber<? super List<Component>> subscriber) {
                final Query query = getQueryByObjectId(objectId);
                final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                        url, ArcGISFeatureLayer.MODE.SNAPSHOT);
                layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                    @Override
                    public void onCallback(FeatureSet featureSet) {
                        if (ListUtil.isEmpty(featureSet.getGraphics())) {
                            subscriber.onNext(new ArrayList<Component>());
                            subscriber.onCompleted();
                            return;
                        }
                        List<Component> componentList = new ArrayList<>();
                        for (Graphic graphic : featureSet.getGraphics()) {
                            Component component = new Component();
                            component.setLayerUrl(url);
                            if (StringUtil.isEmpty(layer.getName())) {
                                component.setLayerName(layer.getName());
                            } else {
                                component.setLayerName(layerName);
                            }
                            if (StringUtil.isEmpty(component.getLayerName())) {
                                component.setLayerName(layerName);
                            }
                            component.setDisplayFieldName(featureSet.getDisplayFieldName());
//                            component.setFieldAlias(featureSet.getFieldAliases());
//                            component.setFields(featureSet.getFields());
                            component.setGraphic(graphic);
//                            component.setFieldAlias(featureSet.getFieldAliases());
                            Object o = graphic.getAttributes().get(featureSet.getObjectIdFieldName());
                            if (o != null && o instanceof Integer) {
                                component.setObjectId((Integer) o); //按照道理objectId一定是integer的
                            }
                            componentList.add(component);
                        }
                        subscriber.onNext(componentList);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable throwable) {
//                        subscriber.onError(throwable);
                        subscriber.onNext(new ArrayList<Component>());
                        subscriber.onCompleted();
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io());
    }

    private String getLayerUrlByLayName(String layerName) {
        String url = "";
        List<LayerInfo> layerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
        if (ListUtil.isEmpty(layerInfosFromLocal)) {
            return url;
        }
        for (LayerInfo layerInfo : layerInfosFromLocal) {
            if (layerInfo.getLayerName().contains(layerName)) {
                url = layerInfo.getUrl();
            }
        }
        return url;
    }


    /**
     * 已知layerName情况下查询指定范围内的设施详细信息（包括新、旧设施图层）
     *
     * @param geometry
     * @param layerName
     */
    public Observable<List<Component>> queryComponentsWithLayerNameAndGeometry(final Geometry geometry, final String layerName) {
        final String newComponentLayerUrl = LayerUrlConstant.getNewLayerUrlByLayerName(layerName);
        final String oldComponentLayerUrl = LayerUrlConstant.getLayerUrlByLayerName(layerName);
        return Observable.zip(queryComponents(geometry, newComponentLayerUrl, layerName), queryComponents(geometry, oldComponentLayerUrl, layerName), new Func2<List<Component>, List<Component>, List<Component>>() {
            @Override
            public List<Component> call(List<Component> componentList, List<Component> componentList2) {
                componentList.addAll(componentList2);
                return componentList;
            }
        });
    }

    public Observable<List<Component>> queryComponentsPSGD(final Geometry geometry, final String layerName) {
        String newComponentLayerUrl = getLayerUrlForName(layerName);
        if (StringUtil.isEmpty(newComponentLayerUrl)) {
            return null;
        }
        newComponentLayerUrl += "/0";
        return queryComponents(geometry, newComponentLayerUrl, layerName);
    }

    public Observable<List<Component>> queryComponentsPSGD(final Geometry geometry, final String layerName, final String name, int layerId) {
        String newComponentLayerUrl = getLayerUrlForName(layerName);
        if (StringUtil.isEmpty(newComponentLayerUrl)) {
            return null;
        }
        newComponentLayerUrl += "/" + layerId;
        return queryComponents(geometry, newComponentLayerUrl, name);
    }

    public Observable<List<Component>> queryComponentsHP(final Geometry geometry, final String layerName, final String name, int layerId) {
        String newComponentLayerUrl = getLayerUrlForName(layerName);
        if (StringUtil.isEmpty(newComponentLayerUrl)) {
            return null;
        }
        newComponentLayerUrl += "/" + layerId;
        return queryHPComponents(geometry, newComponentLayerUrl, name);
    }

    /**
     * 已知layerName情况下查询指定范围内的设施详细信息（包括新、旧设施图层）
     *
     * @param objectId
     * @param layerName
     * @return
     */
    public Observable<List<Component>> queryComponentsWithLayerNameAndObjectId(final String objectId, final String layerName) {
        final String newComponentLayerUrl = LayerUrlConstant.getNewLayerUrlByLayerName(layerName);
        final String oldComponentLayerUrl = LayerUrlConstant.getLayerUrlByLayerName(layerName);
        return Observable.zip(queryComponentsForObjectId(objectId, newComponentLayerUrl, layerName), queryComponentsForObjectId(objectId, oldComponentLayerUrl, layerName), new Func2<List<Component>, List<Component>, List<Component>>() {
            @Override
            public List<Component> call(List<Component> componentList, List<Component> componentList2) {
                componentList.addAll(componentList2);
                return componentList;
            }
        });
    }

    /**
     * 已知layerName情况下查询指定范围内的设施详细信息（包括新、旧设施图层）
     *
     * @param objectId
     * @param layerName
     * @return
     */
    public Observable<List<Component>> queryComponentsHCName(final String objectId, final String layerName) {
        String newComponentLayerUrl = getLayerUrlForName(layerName);
        if (StringUtil.isEmpty(newComponentLayerUrl)) {
            return null;
        }
        newComponentLayerUrl += "/0";
        return queryHCComponentsForName(objectId, newComponentLayerUrl, layerName);
    }

    /**
     * 已知layerName情况下查询指定范围内的设施详细信息（包括新、旧设施图层）
     *
     * @param objectId
     * @param layerName
     * @return
     */
    public Observable<List<Component>> queryComponentsPSK(final String objectId, final String layerName) {
        String newComponentLayerUrl = LayerUrlConstant.getLayerUrlByLayerName(layerName);
        return queryComponentsForObjectId2(objectId, newComponentLayerUrl, layerName);
    }


    /**
     * 已知usid和layerUrl情况下查询设施详细信息
     *
     * @param usId
     * @param url
     * @param callback
     */
    public void queryComponents(final String usId, final String objectId, final String url, final Callback2<Component> callback) {
        Observable.create(new Observable.OnSubscribe<Component>() {
            @Override
            public void call(final Subscriber<? super Component> subscriber) {
                Query query = getQueryByObjectId(objectId);
                if (TextUtils.isEmpty(objectId)) {
                    query = getQueryByUS_Id(usId);
                }

                final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                        url, ArcGISFeatureLayer.MODE.SNAPSHOT);
                layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                    @Override
                    public void onCallback(FeatureSet featureSet) {
                        if (ListUtil.isEmpty(featureSet.getGraphics())) {
                            subscriber.onError(new Exception("查无数据"));
                            return;
                        }
                        Component component = new Component();
                        component.setLayerUrl(url);
                        component.setLayerName(layer.getName());
                        component.setDisplayFieldName(featureSet.getDisplayFieldName());
//                        component.setFieldAlias(featureSet.getFieldAliases());
//                        component.setFields(featureSet.getFields());
                        component.setGraphic(featureSet.getGraphics()[0]);
//                        component.setFieldAlias(featureSet.getFieldAliases());
                        Object o = featureSet.getGraphics()[0].getAttributes().get(featureSet.getObjectIdFieldName());
                        if (o != null && o instanceof Integer) {
                            component.setObjectId((Integer) o); //按照道理objectId一定是integer的
                        }
                        subscriber.onNext(component);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Component>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callback.onFail(new Exception(throwable));
                    }

                    @Override
                    public void onNext(Component queryFeatureSetList) {
                        callback.onSuccess(queryFeatureSetList);
                    }
                });
    }

    /**
     * 已知usid和layerUrl情况下查询设施详细信息
     *
     * @param usId
     * @param url
     * @param callback
     */
    public void queryComponents(final String usId, final String objectId, final String url, final String layerName, final Callback2<Component> callback) {
        Observable.create(new Observable.OnSubscribe<Component>() {
            @Override
            public void call(final Subscriber<? super Component> subscriber) {
                Query query = getQueryByObjectId(objectId);
                if (TextUtils.isEmpty(objectId)) {
                    query = getQueryByUS_Id(usId);
                }

                final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                        url, ArcGISFeatureLayer.MODE.SNAPSHOT);
                layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                    @Override
                    public void onCallback(FeatureSet featureSet) {
                        if (ListUtil.isEmpty(featureSet.getGraphics())) {
                            subscriber.onError(new Exception("查无数据"));
                            return;
                        }
                        Component component = new Component();
                        component.setLayerUrl(url);
                        if (!StringUtil.isEmpty(layerName)) {
                            component.setLayerName(layerName);
                        } else {
                            component.setLayerName(layer.getName());
                        }
                        component.setDisplayFieldName(featureSet.getDisplayFieldName());
//                        component.setFieldAlias(featureSet.getFieldAliases());
//                        component.setFields(featureSet.getFields());
                        component.setGraphic(featureSet.getGraphics()[0]);
//                        component.setFieldAlias(featureSet.getFieldAliases());
                        Object o = featureSet.getGraphics()[0].getAttributes().get(featureSet.getObjectIdFieldName());
                        if (o != null && o instanceof Integer) {
                            component.setObjectId((Integer) o); //按照道理objectId一定是integer的
                        }
                        subscriber.onNext(component);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Component>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callback.onFail(new Exception(throwable));
                    }

                    @Override
                    public void onNext(Component queryFeatureSetList) {
                        callback.onSuccess(queryFeatureSetList);
                    }
                });
    }


    /**
     * 已知usid和layerUrl情况下查询设施详细信息
     *
     * @param usid
     * @param url
     * @param callback
     */
    public void queryComponents(final String usid, final String url, final Callback2<Component> callback) {
        Observable.create(new Observable.OnSubscribe<Component>() {
            @Override
            public void call(final Subscriber<? super Component> subscriber) {
                final Query query = getQueryByUSId(usid);
                final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                        url, ArcGISFeatureLayer.MODE.SNAPSHOT);
                layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                    @Override
                    public void onCallback(FeatureSet featureSet) {
                        if (ListUtil.isEmpty(featureSet.getGraphics())) {
                            subscriber.onError(new Exception("查无数据"));
                            return;
                        }
                        Component component = new Component();
                        component.setLayerUrl(url);
                        component.setLayerName(layer.getName());
                        component.setDisplayFieldName(featureSet.getDisplayFieldName());
//                        component.setFieldAlias(featureSet.getFieldAliases());
//                        component.setFields(featureSet.getFields());
                        component.setGraphic(featureSet.getGraphics()[0]);
//                        component.setFieldAlias(featureSet.getFieldAliases());
                        Object o = featureSet.getGraphics()[0].getAttributes().get(featureSet.getObjectIdFieldName());
                        if (o != null && o instanceof Integer) {
                            component.setObjectId((Integer) o); //按照道理objectId一定是integer的
                        }
                        subscriber.onNext(component);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Component>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callback.onFail(new Exception(throwable));
                    }

                    @Override
                    public void onNext(Component queryFeatureSetList) {
                        callback.onSuccess(queryFeatureSetList);
                    }
                });
    }

    private Query getQueryByUS_Id(String usid) {
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        query.setWhere("US_ID = '" + usid + "'");
        return query;
    }

    /**
     * 已知usid和url情况下查询设施详细信息
     *
     * @param usid
     * @param layerName
     * @param callback
     */
    public void queryComponentsWithLayerName(final String usid, final String objectId, final String layerName, final String url, final Callback2<Component> callback) {
        Observable.create(new Observable.OnSubscribe<Component>() {
            @Override
            public void call(final Subscriber<? super Component> subscriber) {

                queryComponents(usid, objectId, url, layerName, new Callback2<Component>() {
                    @Override
                    public void onSuccess(Component component) {
                        callback.onSuccess(component);
                    }

                    @Override
                    public void onFail(Exception error) {
                        queryComponents(usid, objectId, url, layerName, callback);
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Component>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callback.onFail(new Exception(throwable));
                    }

                    @Override
                    public void onNext(Component queryFeatureSetList) {
                        callback.onSuccess(queryFeatureSetList);
                    }
                });
    }

    /**
     * 已知usid和layerName情况下查询设施详细信息（包括新、旧设施图层）
     *
     * @param usid
     * @param layerName
     * @param callback
     */
    public void queryComponentsWithLayerName(final String usid, final String layerName, final Callback2<Component> callback) {
        Observable.create(new Observable.OnSubscribe<Component>() {
            @Override
            public void call(final Subscriber<? super Component> subscriber) {
                final Query query = getQueryByUSId(usid);
                final String newComponentLayerUrl = LayerUrlConstant.getNewLayerUrlByLayerName(layerName);
                final String oldComponentLayerUrl = LayerUrlConstant.getLayerUrlByLayerName(layerName);
                queryComponents(usid, newComponentLayerUrl, new Callback2<Component>() {
                    @Override
                    public void onSuccess(Component component) {
                        callback.onSuccess(component);
                    }

                    @Override
                    public void onFail(Exception error) {
                        queryComponents(usid, oldComponentLayerUrl, callback);
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Component>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callback.onFail(new Exception(throwable));
                    }

                    @Override
                    public void onNext(Component queryFeatureSetList) {
                        callback.onSuccess(queryFeatureSetList);
                    }
                });
    }


    private Query getQueryByUSId(String usid) {
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        query.setWhere("USID = '" + usid + "'");

        return query;
    }

    private Observable<QueryFeatureSet> getObservable(final String url, final Query query) {
        return Observable.create(new Observable.OnSubscribe<QueryFeatureSet>() {
            @Override
            public void call(final Subscriber<? super QueryFeatureSet> subscriber) {

                final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                        url, ArcGISFeatureLayer.MODE.SNAPSHOT);

//                if (!layer.isInitialized()) {
//                    LogUtil.e("部件图层服务错误");
//                    subscriber.onError(new Exception("部件图层服务不存在"));
//                    return;
//                }
                layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                    @Override
                    public void onCallback(FeatureSet featureSet) {

                        LogUtil.d("部件查询点返回结果：" + featureSet.getGraphics().length);

                        if (!TextUtils.isEmpty(LayerUrlConstant.getLayerNameByUnknownLayerUrl(url))) {
                            QueryFeatureSet queryFeatureSet = new QueryFeatureSet();
                            queryFeatureSet.setLayerUrl(url);
                            queryFeatureSet.setFeatureSet(featureSet);
                            queryFeatureSet.setLayerName(LayerUrlConstant.getLayerNameByUnknownLayerUrl(url));
                            subscriber.onNext(queryFeatureSet);
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(null);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        LogUtil.e("部件的返回结果失败");
                        subscriber.onError(throwable);
                    }
                });
            }
        }).onErrorReturn(new Func1<Throwable, QueryFeatureSet>() {
            @Override
            public QueryFeatureSet call(Throwable throwable) {
                return null;
            }
        })
                .subscribeOn(Schedulers.io());
    }


    /**
     * @param attr
     * @return
     */
    public String getErrorInfo(Map<String, Object> attr) {
        Set<Map.Entry<String, Object>> entries = attr.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            if ("errorinfo".equalsIgnoreCase(entry.getKey())) {
                return String.valueOf(entry.getValue());
            }
        }
        return null;
    }

    /**
     * 查询多个部件的上下游(2017.12.14 暂时不需要)
     *
     * @param url
     * @param components
     * @return
     */
    public Observable<List<Component>> queryUpStreamAndDownStreams(final String url, final List<Component> components) {
//        return Observable.from(components)
//                .flatMap(new Func1<Component, Observable<Component>>() {
//                    @Override
//                    public Observable<Component> call(Component component) {
//                        return queryUpStreamAndDownStream(url, component);
//                    }
//                })
//                .toList()
//                .subscribeOn(Schedulers.io());
        return Observable.fromCallable(new Callable<List<Component>>() {
            @Override
            public List<Component> call() throws Exception {
                return components;
            }
        });
    }

    private Query getQueryByUSID(String upStreamUsid, String downStreamUsid) {
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        StringBuilder stringBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(upStreamUsid)) {
            String[] split = upStreamUsid.split(";");
            for (int i = 0; i < split.length; i++) {
                if (i == split.length - 1) {
                    stringBuilder.append("USID='" + split[i].split(",")[0] + "' or " + DOWN_STREAM + " like '%" + split[i].split(",")[0] + "%'");
                } else {
                    stringBuilder.append("USID='" + split[i].split(",")[0] + "' or " + DOWN_STREAM + "like '%" + split[i].split(",")[0] + "%' or ");
                }
            }
        }

        if (!TextUtils.isEmpty(upStreamUsid) && !TextUtils.isEmpty(downStreamUsid)) {
            stringBuilder.append(" or ");
        }

        if (!TextUtils.isEmpty(downStreamUsid)) {
            String[] split = downStreamUsid.split(";");
            for (int i = 0; i < split.length; i++) {
                if (i == split.length - 1) {
                    stringBuilder.append("USID='" + split[i].split(",")[0] + "' or " + UP_STREAM + " like '%" + split[i].split(",")[0] + "%'");
                } else {
                    stringBuilder.append("USID='" + split[i].split(",")[0] + "' or " + UP_STREAM + " like '%" + split[i].split(",")[0] + "%' or ");
                }
            }
        }

        //"USID='" + upStreamUsid + "' or USID='" + downStreamUsid + "'"
        query.setWhere(stringBuilder.toString());
        return query;
    }

    private static final String UP_STREAM = "UpStream";
    private static final String DOWN_STREAM = "DownStream";


    /**
     * 查询单个部件的上下游
     *
     * @param url
     * @param component
     * @return
     */
    public Observable<Component> queryUpStreamAndDownStream(final String url, final Component component) {

        return Observable.create(new Observable.OnSubscribe<Component>() {
            @Override
            public void call(final Subscriber<? super Component> subscriber) {

                final Query query = getQueryCondition(component);

                final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                        url, ArcGISFeatureLayer.MODE.SNAPSHOT);

                layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                    @Override
                    public void onCallback(FeatureSet featureSet) {

                        LogUtil.d("部件查询点返回结果：" + featureSet.getGraphics().length);
                        QueryFeatureSet queryFeatureSet = new QueryFeatureSet();
                        queryFeatureSet.setLayerUrl(url);
                        queryFeatureSet.setFeatureSet(featureSet);
                        for (int i = 0; i < LayerUrlConstant.componentUrls.length; ++i) {
                            if (url.equals(LayerUrlConstant.componentUrls[i])) {
                                queryFeatureSet.setLayerName(LayerUrlConstant.componentNames[i]);
                                break;
                            }
                        }
                        //将FeatureSet转成component
                        List<Component> componentFromFeatureSet = getComponentFromFeatureSet(queryFeatureSet);
                        if (!ListUtil.isEmpty(componentFromFeatureSet)) {
                            //完善当前部件的上下游信息
                            completeUpStreamAndDownStreamInfo(component, componentFromFeatureSet);
                        }

                        subscriber.onNext(component);
                        subscriber.onCompleted();
                    }

                    /**
                     * 将上下游详细信息填充进原部件信息中
                     * @param component 原部件信息
                     * @param componentFromFeatureSet 上下游详细信息
                     */
                    private void completeUpStreamAndDownStreamInfo(Component component, List<Component> componentFromFeatureSet) {

                        List<Component> notDirectStreams = new ArrayList<>();
                        List<Component> upStreams = new ArrayList<>();
                        List<Component> downStreams = new ArrayList<>();
                        String upStream = getUpStream(component);
                        String downStream = getDownStream(component);
                        String[] upStreamList = upStream.split(";");
                        String[] downStreamList = downStream.split(";");

                        for (Component stream : componentFromFeatureSet) {

                            Object usid = stream.getGraphic().getAttributes().get(ComponentFieldKeyConstant.USID);
                            //判断是上游还是下游
                            if (usid != null && upStream.contains(usid.toString())) {
                                for (String up : upStreamList) {
                                    if (up.contains(usid.toString())) {
                                        String emissionType = up.split(",")[1];
                                        component.setEmissionType(emissionType);
                                        break;
                                    }
                                }
                                upStreams.add(stream);
                            } else if (usid != null && downStream.contains(usid.toString())) {
                                for (String down : downStreamList) {
                                    if (down.contains(usid.toString())) {
                                        String emissionType = down.split(",")[1];
                                        component.setEmissionType(emissionType);
                                        break;
                                    }
                                }
                                downStreams.add(stream);
                            } else {
                                notDirectStreams.add(stream);
                            }
                        }
                        component.setUpStream(upStreams);
                        component.setDownStream(downStreams);

                        completeNotDirectStream(component.getUpStream(), notDirectStreams);
                        completeNotDirectStream(component.getDownStream(), notDirectStreams);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }
                });
            }
        }).subscribeOn(Schedulers.io());
    }

    private void completeNotDirectStream(List<Component> componentUpStream, List<Component> notDirectStreams) {

        //完善上游
        //List<Component> componentUpStream = component.getUpStream();
        if (ListUtil.isEmpty(componentUpStream)) {
            return;
        }

        for (Component com : componentUpStream) {

            String up1 = getUpStream(com);
            String down1 = getDownStream(com);

            String[] upStreamList = up1.split(";");
            String[] downStreamList = down1.split(";");

            List<Component> upStreams2 = new ArrayList<>();
            List<Component> downStreams2 = new ArrayList<>();

            for (Component notDirect : notDirectStreams) {
                Object usid = notDirect.getGraphic().getAttributes().get(ComponentFieldKeyConstant.USID);
                //判断是上游还是下游
                if (usid != null && up1.contains(usid.toString())) {
                    for (String up : upStreamList) {
                        if (up.contains(usid.toString())) {
                            String emissionType = up1.split(",")[1];
                            notDirect.setEmissionType(emissionType);
                            break;
                        }
                    }
                    upStreams2.add(notDirect);
                } else if (usid != null && down1.contains(usid.toString())) {
                    for (String down : downStreamList) {
                        if (down.contains(usid.toString())) {
                            String emissionType = down.split(",")[1];
                            notDirect.setEmissionType(emissionType);
                            break;
                        }
                    }
                    downStreams2.add(notDirect);
                }
            }
            com.setUpStream(upStreams2);
            com.setDownStream(downStreams2);
        }
    }

    /**
     * 获取查询上下游的条件
     *
     * @param components
     * @return
     */
    private Query getQueryCondition(Component components) {
        String upStreamStr = getUpStream(components);
        String downStreamStr = getDownStream(components);

        return getQueryByUSID(upStreamStr, downStreamStr);
    }

    /**
     * 获取下游
     *
     * @param components
     * @return
     */
    private String getDownStream(Component components) {
        String downStreamStr = "";
        Object downStream = components.getGraphic().getAttributes().get(DOWN_STREAM);
        if (downStream != null) {
            downStreamStr = downStream.toString();
        }
        return downStreamStr;
    }

    /**
     * 获取上游
     *
     * @param components
     * @return
     */
    private String getUpStream(Component components) {
        Object upStream = components.getGraphic().getAttributes().get(UP_STREAM);

        String upStreamStr = "";


        if (upStream != null) {
            upStreamStr = upStream.toString();
        }
        return upStreamStr;
    }

    /**
     * 将QueryFeatureSet转成List<Component>
     *
     * @param queryFeatureSet
     * @return
     */
    public List<Component> getComponentFromFeatureSet(QueryFeatureSet queryFeatureSet) {
        if (queryFeatureSet == null) {
            return null;
        }
        FeatureSet featureSet = queryFeatureSet.getFeatureSet();
        Graphic[] graphics = featureSet.getGraphics();
        if (graphics == null
                || graphics.length <= 0) {
            return null;
        }

        List<Component> components = new ArrayList<>();
        for (Graphic graphic : graphics) {
            Component component = new Component();
            component.setLayerUrl(queryFeatureSet.getLayerUrl());
            component.setLayerName(queryFeatureSet.getLayerName());
            component.setDisplayFieldName(featureSet.getDisplayFieldName());
//            component.setFieldAlias(featureSet.getFieldAliases());
//            component.setFields(featureSet.getFields());
            component.setGraphic(graphic);
            Object o = graphic.getAttributes().get(featureSet.getObjectIdFieldName());
            if (o != null && o instanceof Integer) {
                component.setObjectId((Integer) o); //按照道理objectId一定是integer的
            }
            components.add(component);
        }

        return components;
    }

    /**
     * 通过图层名来获取图层URL
     *
     * @param name
     * @return
     */
    private String getLayerUrlForName(String name) {
        List<LayerInfo> layerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
        if (ListUtil.isEmpty(layerInfosFromLocal)) {
            return null;
        }
        for (LayerInfo layerInfo : layerInfosFromLocal) {
            if (layerInfo.getLayerName().equals(name)) {
                return layerInfo.getUrl();
            }
        }
        return null;
    }

    /**
     * 点击地图时进行查询上报或者新增的信息
     *
     * @param point
     * @param spatialReference
     * @param resolution
     * @param callback2
     */
    public void queryDoorDataInfo(Point point, SpatialReference spatialReference, double resolution,
                                  final Callback2<List<DoorNOBean>> callback2) {

        Geometry geometry = GeometryEngine.buffer(point, spatialReference, 60 * resolution, null);
        if (query == null) {
            callback2.onFail(new Exception("查询参数为空"));
            return;
        }
        query.setGeometry(geometry);

        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
        String url = sharedPreferencesUtil.getString(PatrolLayerPresenter.DOOR_NO_LAYER, "") + "/0";

        if ("/0".equals(url)) {
            url = getDoorNOLayerUrl() + "/0";
        }
        if ("/0".equals(url)) {
            callback2.onFail(new Exception("无法查询到我的上报图层的url"));
            return;
        }

        final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                url, ArcGISFeatureLayer.MODE.SNAPSHOT);
        layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
            @Override
            public void onCallback(FeatureSet featureSet) {
                final ArrayList<DoorNOBean> doorBeans = new ArrayList<DoorNOBean>();
                Point point = null;
                //判断是新增还是纠错
                if (featureSet.getGraphics().length != 0) {
                    Graphic[] graphics = featureSet.getGraphics();
                    DoorNOBean doorNOBean = null;
                    for (Graphic graphic : graphics) {
                        doorNOBean = new DoorNOBean();
                        doorNOBean.setAddr(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.DZQC)));
                        doorNOBean.setMpObjectId(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.OBJECTID)));
                        doorNOBean.setsGuid(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.S_GUID)));
                        doorNOBean.setMph(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.MPWZHM)));
                        doorNOBean.setDzdm(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.DZDM)));
                        doorNOBean.setPSDY_OID(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.PSDY_OID)));
                        doorNOBean.setISTATUE(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.ISTATUE)));
                        doorNOBean.setPSDY_NAME(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.PSDY_NAME)));
                        doorNOBean.setIsExist(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.ISEXIST)));
                        if (graphic.getGeometry() != null) {
                            point = (Point) graphic.getGeometry();
                            doorNOBean.setX(objectToDouble(point.getX()));
                            doorNOBean.setY(objectToDouble(point.getY()));
                        }

                        doorBeans.add(doorNOBean);
                    }

                    if (ListUtil.isEmpty(doorBeans)) {
                        callback2.onSuccess(new ArrayList<DoorNOBean>());
                    } else {
//                        callback2.onSuccess(doorBeans);
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback2.onSuccess(doorBeans);
                            }
                        });
                    }

                } else {
                    callback2.onSuccess(new ArrayList<DoorNOBean>());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                callback2.onFail(new Exception(throwable));
            }
        });
    }

    public void setQueryByWhere(String where) {
        query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        if (where != null) {
            query.setWhere(where);
        }
    }

    private String objectToString(Object object) {
        if (object == null) {
            return "";
        }
        return StringUtil.getNotNullString(object.toString(), "");
    }

    private double objectToDouble(Object object) {
        if (object == null) {
            return 0;
        }
        return Double.valueOf(object.toString());
    }

    /**
     * 获取门牌号图层的Url
     *
     * @return
     */
    @Nullable
    private String getDoorNOLayerUrl() {
        String url = "";
        try {
            List<LayerInfo> layerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
            if (null != layerInfosFromLocal) {
                for (LayerInfo layerInfo : layerInfosFromLocal) {
                    if (layerInfo.getLayerName().contains(PatrolLayerPresenter.DOOR_NO_LAYER)) {
                        url = layerInfo.getUrl();
                    }
                }
            }
        } catch (Exception e) {
            return url;
        }
        return url;
    }

    /**
     * 只查门牌号(搜索)
     */
    public void queryDoorNoComponentsForObjectId(final String keyword, boolean isSearch, final Callback2<List<DoorNOBean>> callback) {
        Observable<List<DoorNOBean>> menpai = queryComponentsForKeyword(keyword, getMenPaiLayerUrl() + "/0", PatrolLayerPresenter.DOOR_NO_LAYER, isSearch);

        menpai.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<DoorNOBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<DoorNOBean> componentList) {
                        callback.onSuccess(componentList);
                    }
                });
    }

    /**
     * 已知layerUrl情况下查询指定范围内的设施详细信息
     *
     * @param keyword
     * @param url
     * @return
     */
    public Observable<List<DoorNOBean>> queryComponentsForKeyword(final String keyword, final String url, final String layerName, final boolean isSearch) {
        return Observable.create(new Observable.OnSubscribe<List<DoorNOBean>>() {
            @Override
            public void call(final Subscriber<? super List<DoorNOBean>> subscriber) {
                if (isSearch) {
                    query = getQueryByMenPai(keyword);
                } else {
                    query = getQueryByObjectId(keyword);
                }
                final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                        url, ArcGISFeatureLayer.MODE.SNAPSHOT);
                layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
                    @Override
                    public void onCallback(FeatureSet featureSet) {
                        final ArrayList<DoorNOBean> doorBeans = new ArrayList<DoorNOBean>();
                        Point point = null;
                        //判断是新增还是纠错
                        if (featureSet.getGraphics().length != 0) {
                            Graphic[] graphics = featureSet.getGraphics();
                            DoorNOBean doorNOBean = null;
                            for (Graphic graphic : graphics) {
                                doorNOBean = new DoorNOBean();
                                doorNOBean.setAddr(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.DZQC)));
                                doorNOBean.setMph(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.MPWZHM)));
                                doorNOBean.setS_guid(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.S_GUID)));
                                doorNOBean.setStree(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.MPWZHM)));
                                doorNOBean.setDzdm(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.DZDM)));
                                doorNOBean.setPSDY_OID(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.PSDY_OID)));
                                doorNOBean.setISTATUE(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.ISTATUE)));
                                doorNOBean.setPSDY_NAME(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.PSDY_NAME)));
                                doorNOBean.setIsExist(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.ISEXIST)));
                                if (graphic.getGeometry() != null) {
                                    point = (Point) graphic.getGeometry();
                                    doorNOBean.setX(objectToDouble(point.getX()));
                                    doorNOBean.setY(objectToDouble(point.getY()));
                                }
                                doorBeans.add(doorNOBean);
                            }

                            subscriber.onNext(doorBeans);
                            subscriber.onCompleted();

                        } else {
                            subscriber.onNext(doorBeans);
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取门牌图层
     *
     * @return
     */
    @Nullable
    private String getMenPaiLayerUrl() {
        String url = "";
        List<LayerInfo> layerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
        //修改腾讯bugly上的空指针异常,目前写死
        if (ListUtil.isEmpty(layerInfosFromLocal)) {
            return "http://139.159.243.230:6080/arcgis/rest/services/PAISHUIHU/menpaihao_ms/MapServer";
        }

        for (LayerInfo layerInfo : layerInfosFromLocal) {
            if (layerInfo.getLayerName().contains(PatrolLayerPresenter.DOOR_NO_LAYER)) {
                url = layerInfo.getUrl();
            }
        }
        return url;
    }

    public static void main(String[] args) {

    }
}
