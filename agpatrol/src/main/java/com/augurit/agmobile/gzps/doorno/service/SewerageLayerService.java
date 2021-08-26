package com.augurit.agmobile.gzps.doorno.service;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import com.augurit.agmobile.gzps.common.util.SewerageLayerFieldKeyConstant;
import com.augurit.agmobile.gzps.doorno.model.DoorNOBean;
import com.augurit.agmobile.gzps.uploadfacility.dao.UploadLayerApi;
import com.augurit.agmobile.gzps.uploadfacility.service.CorrectFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadFacilityService;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.patrolcore.layer.service.AgwebPatrolLayerService2;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.net.util.SharedPreferencesUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.query.Query;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 查询数据上报的相关方法
 * Created by xcl on 2017/12/7.
 */

public class SewerageLayerService extends AgwebPatrolLayerService2 {

    private AMNetwork amNetwork;
    private UploadLayerApi uploadLayerApi;
    private Query query;
    private CorrectFacilityService correctFacilityService;
    private UploadFacilityService uploadFacilityService;
    private Context mContext;
    private boolean isAdd = false;

    public SewerageLayerService(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public Observable<List<LayerInfo>> getLayerInfo() {
        return super.getSortedLayerInfos()
                .map(new Func1<List<LayerInfo>, List<LayerInfo>>() {
                    @Override
                    public List<LayerInfo> call(List<LayerInfo> layerInfos) {
                        //手动加多一个图层
                        LayerInfo info = null;

                        for (LayerInfo layerInfo : layerInfos) {
                            if (layerInfo.getLayerName().contains(PatrolLayerPresenter.UPLOAD_LAYER_NAME)) {
                                layerInfo.setLayerName(PatrolLayerPresenter.LOCAL_UPLOAD_LAYER_NAME);
                            }
                            if (layerInfo.getLayerName().contains(PatrolLayerPresenter.DOOR_NO_LAYER)) {
                                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
                                sharedPreferencesUtil.setString(PatrolLayerPresenter.DOOR_NO_LAYER, layerInfo.getUrl());
                                isAdd = true;
                            }
//                            if (layerInfo.getLayerName().contains(PatrolLayerPresenter.TYPICAL_DOOR_NO_LAYER)) {
//                                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
//                                sharedPreferencesUtil.setString(PatrolLayerPresenter.TYPICAL_DOOR_NO_LAYER, layerInfo.getUrl());
//                            }
                            if (layerInfo.getLayerName().contains(PatrolLayerPresenter.LOCAL_UPLOAD_LAYER_NAME)) {
                                try {
                                    info = (LayerInfo) layerInfo.clone();
                                    info.setLayerId(1116);
                                    info.setLayerName("我的上报");
                                    info.setLayerOrder(layerInfo.getLayerOrder() - 1);
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                    info = new LayerInfo();
                                    info.setLayerId(1116);
                                    info.setDefaultVisibility(layerInfo.isDefaultVisible());
                                    info.setDirTypeName("我的上报");
                                    info.setChildLayer(layerInfo.getChildLayer());
                                    info.setUrl(layerInfo.getUrl());
                                    info.setOpacity(layerInfo.getOpacity());
                                    info.setType(layerInfo.getType());
                                    info.setLayerName("我的上报");
                                    info.setLayerOrder(layerInfo.getLayerOrder() - 1);
                                }
                            }
                            if (info != null && isAdd) {
                                break;
                            }

                        }

                        if (info != null) {
                            layerInfos.add(info);
                        }
                        return layerInfos;
                    }
                });
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
                        doorNOBean.setAddress(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.DZQC)));
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

                    if (ListUtil.isEmpty(doorBeans)) {
                        callback2.onSuccess(new ArrayList<DoorNOBean>());
                    } else {
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

    /**
     * 点击地图查询典型排水户的图层信息
     *
     * @param point
     * @param spatialReference
     * @param resolution
     * @param callback2
     */
    public void queryTypicalDoorDataInfo(Point point, SpatialReference spatialReference, double resolution,
                                         final Callback2<List<DoorNOBean>> callback2) {

        Geometry geometry = GeometryEngine.buffer(point, spatialReference, 60 * resolution, null);
        if (query == null) {
            callback2.onFail(new Exception("查询参数为空"));
            return;
        }
        query.setGeometry(geometry);

        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
        String url = sharedPreferencesUtil.getString("典型排水户", "") + "/0";
//        String url = sharedPreferencesUtil.getString(PatrolLayerPresenter.TYPICAL_DOOR_NO_LAYER, "") + "/0";

        if ("/0".equals(url)) {
            url = getTypicalDoorNOLayerUrl() + "/0";
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
                        doorNOBean.setAddress(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.DZQC)));
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

                    if (ListUtil.isEmpty(doorBeans)) {
                        callback2.onSuccess(new ArrayList<DoorNOBean>());
                    } else {
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

//    private void queryAttachmentInfo(final List<DoorNOBean> doorNOBeans, final Callback2<List<DoorNOBean>> callback2) {
//        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
//        initAMNetwork(supportUrl);
//        Observable.from(doorNOBeans)
//                .flatMap(new Func1<DoorNOBean, Observable<DoorNOBean>>() {
//                    //获取上报信息
//                    @Override
//                    public Observable<DoorNOBean> call(final DoorNOBean doorNOBean) {
//                        //如果markId为空，无法请求附件
//                        if (TextUtils.isEmpty(doorNOBean.getMarkId())) {
//                            return Observable.fromCallable(new Func0<DoorNOBean>() {
//                                @Override
//                                public UploadInfo call() {
//                                    return doorNOBean;
//                                }
//                            });
//                        } else {
//                            return getAttachment(doorNOBean);
//                        }
//
//                    }
//                })
//                .filter(new Func1<DoorNOBean, Boolean>() {
//                    @Override
//                    public Boolean call(DoorNOBean doorNOBean) {
//                        return doorNOBean.getModifiedFacilities() != null || doorNOBean.getUploadedFacilities() != null;
//                    }
//                })
//                .toList()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<DoorNOBean>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        callback2.onSuccess(doorNOBeans);
//                    }
//
//                    @Override
//                    public void onNext(List<DoorNOBean> doorNOBeans) {
//                        callback2.onSuccess(doorNOBeans);
//                    }
//                });
//    }


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
     * 获取典型排水户图层的Url
     *
     * @return
     */
    @Nullable
    private String getTypicalDoorNOLayerUrl() {
        String url = "";
        try {
            List<LayerInfo> layerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
            if (null != layerInfosFromLocal) {
                for (LayerInfo layerInfo : layerInfosFromLocal) {
                    if (layerInfo.getLayerName().contains("典型排水户")) {
                        url = layerInfo.getUrl();
                    }
                }
            }
        } catch (Exception e) {
            return url;
        }
        return url;
    }


    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null) {
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.build();
            this.amNetwork.registerApi(UploadLayerApi.class);
            this.uploadLayerApi = (UploadLayerApi) this.amNetwork.getServiceApi(UploadLayerApi.class);
        }
    }

//    public Observable<UploadBean> upLoadWrongDoor(String sGuid) {
////        String url = BaseInfoManager.getSupportUrl(mContext);
//        String supportUrl = PortSelectUtil.getAgSupportPortBaseURL(mContext);
//        initAMNetwork(supportUrl);
//        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
//        return uploadLayerApi.upLoadWrongDoor(sGuid, loginName).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }

    private void initCorrectFacilityService() {
        if (correctFacilityService == null) {
            correctFacilityService = new CorrectFacilityService(mContext);
        }
    }


    private void initUploadFacilityService() {
        if (uploadFacilityService == null) {
            uploadFacilityService = new UploadFacilityService(mContext);
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

    private long objectToLong(Object object) {
        if (object == null) {
            return -1L;
        }
        return Long.valueOf(object.toString());
    }
}
