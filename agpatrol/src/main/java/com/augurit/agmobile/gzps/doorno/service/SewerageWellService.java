package com.augurit.agmobile.gzps.doorno.service;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.util.PortSelectUtil;
import com.augurit.agmobile.gzps.componentmaintenance.model.QueryFeatureSet;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentTypeConstant;
import com.augurit.agmobile.gzps.doorno.dao.NWSignStatisticApi;
import com.augurit.agmobile.gzps.doorno.dao.SewerageWellApi;
import com.augurit.agmobile.gzps.doorno.model.WellPhoto;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.identify.IdentifyTask;

import java.util.ArrayList;
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
 * @author 创建人 ：luobiao
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.componentmaintenance
 * @createTime 创建时间 ：18/4/10
 * @modifyBy 修改人 ：luobiao
 * @modifyTime 修改时间 ：18/4/10
 * @modifyMemo 修改备注：
 */

public class SewerageWellService {


    private Context mContext;

    private AMNetwork amNetwork;
    private SewerageWellApi sewerageWellApi;

    public SewerageWellService(Context context) {
        this.mContext = context;
    }

    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null) {
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.build();
            this.amNetwork.registerApi(NWSignStatisticApi.class);
            this.sewerageWellApi = (SewerageWellApi) this.amNetwork.getServiceApi(SewerageWellApi.class);
        }
    }

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
//        final Query query = getQueryByFlagAndUserName(componentType, new LoginService(mContext, AMDatabase.getInstance()).getUser().getUserName());
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
        query.setGeometry(geometry);
        return query;
    }

    private Query getQueryByID(String id) {
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setWhere("OBJECTID='" + id + "'");
        return query;
    }

    private Query getQueryMenPaiByID(String id) {
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setWhere("S_GUID='" + id + "'");
        return query;
    }


    /**
     * 只查窨井、雨水口、排放口
     */
    public void queryPrimaryComponents(final Geometry geometry, final Callback2<List<Component>> callback) {
        Observable<List<Component>> yinjin = queryComponentsWithLayerNameAndGeometry(geometry, "窨井");
        Observable<List<Component>> yushuikou = queryComponentsWithLayerNameAndGeometry(geometry, "雨水口");
        Observable<List<Component>> paifangkou = queryComponentsWithLayerNameAndGeometry(geometry, "排放口");
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
     * 只查窨井、雨水口、排放口
     */
    public void queryUnitComponents(final Geometry geometry, final Callback2<List<Component>> callback) {
        String url = getWellLayerUrl() + "/0";
        queryComponents(geometry, url)
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
     * 只查窨井、雨水口、排放口
     */
    public void queryWellComponents(final String id, final Callback2<List<Component>> callback) {
        String url = getWellLayerUrl() + "/0";
        queryComponents(id, url)
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
     * 只查窨井、雨水口、排放口
     */
    public void queryWellComponent(final String id, final String name, final Callback2<List<Component>> callback) {
        String url = getWellLayerUrl() + "/0";
        queryComponents(id, url)
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
     * 查门牌坐标
     */
    public void queryMenPaiComponents(final String id, final Callback2<List<Component>> callback) {
        String url = getMenPaiLayerUrl() + "/0";
        queryMenPaiComponents(id, url)
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


    private String getWellLayerUrl() {
        String url = "";
        List<LayerInfo> layerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
        for (LayerInfo layerInfo : layerInfosFromLocal) {
            if (layerInfo.getLayerName().contains(PatrolLayerPresenter.DRAINAGE_WELL)) {
                url = layerInfo.getUrl();
            }
        }
        return url;
    }

    /**
     * 获取门牌号坐标
     *
     * @return
     */
    private String getMenPaiLayerUrl() {
        String url = "";
        List<LayerInfo> layerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
        for (LayerInfo layerInfo : layerInfosFromLocal) {
            if (layerInfo.getLayerName().contains(PatrolLayerPresenter.DOOR_NO_LAYER)) {
                url = layerInfo.getUrl();
            }
        }
        return url;
    }

    /**
     * identify方式只查窨井、雨水口、排放口
     */
    public void identifyPrimaryComponents(final Geometry geometry, final MapView mapView, final Callback2<List<Component>> callback) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                int yinjinLayerId = LayerUrlConstant.componentIds[LayerUrlConstant.getIndexlByLayerName("窨井")];
                int yushuikouLayerId = LayerUrlConstant.componentIds[LayerUrlConstant.getIndexlByLayerName("雨水口")];
                int paifangkouLayerId = LayerUrlConstant.componentIds[LayerUrlConstant.getIndexlByLayerName("排放口")];
                IdentifyParameters parameters = new IdentifyParameters();
                parameters.setGeometry(geometry);
                Envelope extent = new Envelope();
                mapView.getExtent().queryEnvelope(extent);
                parameters.setMapExtent(extent);
                parameters.setMapWidth(mapView.getWidth());
                parameters.setMapHeight(mapView.getHeight());
                parameters.setSpatialReference(mapView.getSpatialReference());
                parameters.setDPI(98);  // TODO 是否需要动态设置
                parameters.setLayers(new int[]{yinjinLayerId, yushuikouLayerId, paifangkouLayerId});
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
        Observable<List<Component>> paifangkou = queryComponentsWithLayerNameAndGeometry(geometry, "排放口");
        List<Observable<List<Component>>> observables = new ArrayList<>();
        observables.add(yinjin);
        observables.add(yushuikou);
        observables.add(paifangkou);

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
     *
     * @param geometry
     * @param url
     */
    public Observable<List<Component>> queryComponents(final Geometry geometry, final String url) {
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
     * @param id
     * @param url
     * @return
     */
    public Observable<List<Component>> queryComponents(final String id, final String url) {
        return Observable.create(new Observable.OnSubscribe<List<Component>>() {
            @Override
            public void call(final Subscriber<? super List<Component>> subscriber) {
                final Query query = getQueryByID(id);
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
     * @param id
     * @param url
     * @return
     */
    public Observable<List<Component>> queryMenPaiComponents(final String id, final String url) {
        return Observable.create(new Observable.OnSubscribe<List<Component>>() {
            @Override
            public void call(final Subscriber<? super List<Component>> subscriber) {
                final Query query = getQueryMenPaiByID(id);
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
     * 已知layerName情况下查询指定范围内的设施详细信息（包括新、旧设施图层）
     *
     * @param geometry
     * @param layerName
     */
    public Observable<List<Component>> queryComponentsWithLayerNameAndGeometry(final Geometry geometry, final String layerName) {
        final String newComponentLayerUrl = LayerUrlConstant.getNewLayerUrlByLayerName(layerName);
        final String oldComponentLayerUrl = LayerUrlConstant.getLayerUrlByLayerName(layerName);
        return Observable.zip(queryComponents(geometry, newComponentLayerUrl), queryComponents(geometry, oldComponentLayerUrl), new Func2<List<Component>, List<Component>, List<Component>>() {
            @Override
            public List<Component> call(List<Component> componentList, List<Component> componentList2) {
                componentList.addAll(componentList2);
                return componentList;
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

    public Observable<WellPhoto> getWellPhotos(String objectid) {
//        String url = BaseInfoManager.getSupportUrl(mContext);
        String supportUrl = PortSelectUtil.getAgSupportPortBaseURL(mContext);
        initAMNetwork(supportUrl);
        return sewerageWellApi.getWellPhotos(objectid);
    }


}
