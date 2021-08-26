package com.augurit.agmobile.gzps.uploadfacility.service;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.model.DoorNOBean;
import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.common.util.SewerageLayerFieldKeyConstant;
import com.augurit.agmobile.gzps.componentmaintenance.model.QueryFeatureSet;
import com.augurit.agmobile.gzps.componentmaintenance.service.ComponentService;
import com.augurit.agmobile.gzps.uploadfacility.dao.UploadLayerApi;
import com.augurit.agmobile.gzps.uploadfacility.model.CompleteTableInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.util.CompleteTableInfoUtil;
import com.augurit.agmobile.gzps.uploadfacility.util.UploadLayerFieldKeyConstant;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.patrolcore.common.file.model.FileResult;
import com.augurit.agmobile.patrolcore.common.file.service.FileService;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableItems;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.agmobile.patrolcore.common.table.model.ProjectTable;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.layer.service.AgwebPatrolLayerService2;
import com.augurit.agmobile.patrolcore.layer.service.EditLayerService;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.net.util.SharedPreferencesUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnStatusChangedListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 查询数据上报的相关方法
 * Created by xcl on 2017/12/7.
 */

public class HookLayerService extends AgwebPatrolLayerService2{

    private AMNetwork amNetwork;
    private UploadLayerApi uploadLayerApi;
    private Query query;
    private CorrectFacilityService correctFacilityService;
    private UploadFacilityService uploadFacilityService;
    private TableDataManager tableDataManager;

    public HookLayerService(Context mContext) {
       super(mContext);
    }

    @Override
    public Observable<List<LayerInfo>> getLayerInfo() {
        return super.getSortedLayerInfos()
                .map(new Func1<List<LayerInfo>, List<LayerInfo>>() {
                    @Override
                    public List<LayerInfo> call(List<LayerInfo> layerInfos) {
                        //手动加多一个图层
                        List<LayerInfo> layerInfotemp = new ArrayList<>();
                        LayerInfo info = null;
                        for (LayerInfo layerInfo : layerInfos){
                            if (LayerType.TileLayer == layerInfo.getType()
                                    || LayerType.TianDiTu == layerInfo.getType()) {
                                layerInfotemp.add(layerInfo);
                            } else {
                                if (PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2.equals(layerInfo.getLayerName())) {
                                    layerInfotemp.add(layerInfo);
                                } else if (PatrolLayerPresenter.DRAINAGE_JIEBOJING.equals(layerInfo.getLayerName())) {
                                    layerInfotemp.add(layerInfo);
                                }
                            }
                        }
                        return layerInfotemp;
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

    /**
     * 点击地图时进行查询上报或者新增的信息
     *
     * @param point
     * @param spatialReference
     * @param resolution
     * @param callback2
     */
    public void queryUploadDataInfo(Point point, SpatialReference spatialReference, double resolution,
                                    final Callback2<List<UploadInfo>> callback2) {

        Geometry geometry = GeometryEngine.buffer(point, spatialReference, 60 * resolution, null);
        if (query == null) {
            callback2.onFail(new Exception("查询参数为空"));
            return;
        }
        query.setGeometry(geometry);

        String url = getUploadLayerUrl() + "/0";

        if ("/0".equals(url)) {
            callback2.onFail(new Exception("无法查询到我的上报图层的url"));
            return;
        }

        final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                url, ArcGISFeatureLayer.MODE.SNAPSHOT);
        layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
            @Override
            public void onCallback(FeatureSet featureSet) {
                LogUtil.d("部件查询点返回结果：" + featureSet.getGraphics().length);
//
                List<UploadInfo> uploadedFacilities = new ArrayList<>();
                //判断是新增还是纠错
                if (featureSet.getGraphics().length != 0) {
                    Graphic[] graphics = featureSet.getGraphics();
                    for (Graphic graphic : graphics) {
                        Object o = graphic.getAttributes().get(UploadLayerFieldKeyConstant.REPORT_TYPE);
                        Object markId = graphic.getAttributes().get(UploadLayerFieldKeyConstant.MARK_ID);
                        if (o != null) {
                            if (UploadLayerFieldKeyConstant.CORRECT_ERROR.equals(o.toString()) || UploadLayerFieldKeyConstant.CONFIRM.equals(o.toString())) {
                                //纠错
                                UploadInfo uploadInfo = new UploadInfo();
                                ModifiedFacility modifiedFacilityFromGraphic = getModifiedFacilityFromGraphic(graphic.getAttributes(),graphic.getGeometry());
                                uploadInfo.setModifiedFacilities(modifiedFacilityFromGraphic);
                                uploadInfo.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);
                                uploadInfo.setMarkId(objectToString(markId));

                                CompleteTableInfo completeTableInfo = CompleteTableInfoUtil.getCompleteTableInfo(modifiedFacilityFromGraphic);
                                uploadInfo.setCompleteTableInfo(completeTableInfo);

                                uploadedFacilities.add(uploadInfo);
                            } else {
                                //新增
                                UploadInfo uploadInfo = new UploadInfo();
                                UploadedFacility uploadedFacilityFromGraphic = getUploadedFacilityFromGraphic(graphic.getAttributes(),graphic.getGeometry());
                                uploadInfo.setReportType(o.toString());
                                uploadInfo.setMarkId(objectToString(markId));
                                uploadInfo.setUploadedFacilities(uploadedFacilityFromGraphic);
                                uploadedFacilities.add(uploadInfo);
                            }
                        }
                    }

                    if (ListUtil.isEmpty(uploadedFacilities)) {
                        callback2.onSuccess(uploadedFacilities);
                    } else {
                        queryAttachmentInfo(uploadedFacilities, callback2);
                    }

                } else {
                    callback2.onSuccess(new ArrayList<UploadInfo>());
                }

                //callback2.onSuccess(uploadedFacilities);
            }

            @Override
            public void onError(Throwable throwable) {
                callback2.onFail(new Exception(throwable));
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
    public void queryUploadDataInfos(Point point, SpatialReference spatialReference, double resolution,
                                    final Callback2<List<UploadInfo>> callback2) {

        Geometry geometry = GeometryEngine.buffer(point, spatialReference, 60 * resolution, null);
        if (query == null) {
            callback2.onFail(new Exception("查询参数为空"));
            return;
        }
        query.setGeometry(geometry);

        String url = getUploadLayerUrl() + "/0";

        if ("/0".equals(url)) {
            callback2.onFail(new Exception("无法查询到我的上报图层的url"));
            return;
        }

        final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                url, ArcGISFeatureLayer.MODE.SNAPSHOT);
        layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
            @Override
            public void onCallback(FeatureSet featureSet) {
                LogUtil.d("部件查询点返回结果：" + featureSet.getGraphics().length);
//
                List<UploadInfo> uploadedFacilities = new ArrayList<>();
                //判断是新增还是纠错
                if (featureSet.getGraphics().length != 0) {
                    Graphic[] graphics = featureSet.getGraphics();
                    for (Graphic graphic : graphics) {
                        Object o = graphic.getAttributes().get(UploadLayerFieldKeyConstant.REPORT_TYPE);
                        Object markId = graphic.getAttributes().get(UploadLayerFieldKeyConstant.MARK_ID);
                        if (o != null) {
                            if (UploadLayerFieldKeyConstant.CORRECT_ERROR.equals(o.toString()) || UploadLayerFieldKeyConstant.CONFIRM.equals(o.toString())) {
                                //纠错
                                UploadInfo uploadInfo = new UploadInfo();
                                ModifiedFacility modifiedFacilityFromGraphic = getModifiedFacilityFromGraphic(graphic.getAttributes(),graphic.getGeometry());
                                uploadInfo.setModifiedFacilities(modifiedFacilityFromGraphic);
                                uploadInfo.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);
                                uploadInfo.setMarkId(objectToString(markId));
                                CompleteTableInfo completeTableInfo = CompleteTableInfoUtil.getCompleteTableInfo(modifiedFacilityFromGraphic);
                                uploadInfo.setCompleteTableInfo(completeTableInfo);
                                uploadedFacilities.add(uploadInfo);
                            } else {
                                //新增
                                UploadInfo uploadInfo = new UploadInfo();
                                UploadedFacility uploadedFacilityFromGraphic = getUploadedFacilityFromGraphic(graphic.getAttributes(),graphic.getGeometry());
                                uploadInfo.setReportType(o.toString());
                                uploadInfo.setMarkId(objectToString(markId));
                                uploadInfo.setUploadedFacilities(uploadedFacilityFromGraphic);
                                uploadedFacilities.add(uploadInfo);
                            }
                        }
                    }

                    if (ListUtil.isEmpty(uploadedFacilities)) {
                        callback2.onSuccess(uploadedFacilities);
                    } else {
                        queryFacilityInfo(uploadedFacilities, callback2);
                    }

                } else {
                    callback2.onSuccess(new ArrayList<UploadInfo>());
                }

                //callback2.onSuccess(uploadedFacilities);
            }

            @Override
            public void onError(Throwable throwable) {
                callback2.onFail(new Exception(throwable));
            }
        });
    }

    private void queryAttachmentInfo(final List<UploadInfo> uploadedFacilities, final Callback2<List<UploadInfo>> callback2) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        Observable.from(uploadedFacilities)
                .flatMap(new Func1<UploadInfo, Observable<UploadInfo>>() {
                    //获取上报信息
                    @Override
                    public Observable<UploadInfo> call(final UploadInfo uploadInfo) {
                        //如果markId为空，无法请求附件
                        if (TextUtils.isEmpty(uploadInfo.getMarkId())) {
                            return Observable.fromCallable(new Func0<UploadInfo>() {
                                @Override
                                public UploadInfo call() {
                                    return uploadInfo;
                                }
                            });
                        } else {
                            return getAttachment(uploadInfo);
                        }

                    }
                })
                .filter(new Func1<UploadInfo, Boolean>() {
                    @Override
                    public Boolean call(UploadInfo uploadInfo) {
                        return uploadInfo.getModifiedFacilities() != null || uploadInfo.getUploadedFacilities() != null;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UploadInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback2.onSuccess(uploadedFacilities);
                    }

                    @Override
                    public void onNext(List<UploadInfo> uploadInfos) {
                        callback2.onSuccess(uploadInfos);
                    }
                });
    }

    private void queryFacilityInfo(final List<UploadInfo> uploadedFacilities, final Callback2<List<UploadInfo>> callback2) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        Observable.from(uploadedFacilities)
                .flatMap(new Func1<UploadInfo, Observable<UploadInfo>>() {
                    //获取上报信息
                    @Override
                    public Observable<UploadInfo> call(final UploadInfo uploadInfo) {
                        //如果markId为空，无法请求附件
                        if (TextUtils.isEmpty(uploadInfo.getMarkId())) {
                            return Observable.fromCallable(new Func0<UploadInfo>() {
                                @Override
                                public UploadInfo call() {
                                    return uploadInfo;
                                }
                            });
                        } else {
                            return getAttachments(uploadInfo);
                        }

                    }
                })
                .filter(new Func1<UploadInfo, Boolean>() {
                    @Override
                    public Boolean call(UploadInfo uploadInfo) {
                        return uploadInfo.getModifiedFacilities() != null || uploadInfo.getUploadedFacilities() != null;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UploadInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback2.onSuccess(uploadedFacilities);
                    }

                    @Override
                    public void onNext(List<UploadInfo> uploadInfos) {
                        callback2.onSuccess(uploadInfos);
                    }
                });
    }

    /**
     * 通过objectid进行查询上报或者新增的信息
     *
     * @param callback2
     */
    public void queryUploadInfoForObjectId(final Callback2<List<UploadInfo>> callback2) {
        if (query == null) {
            callback2.onFail(new Exception("查询参数为空"));
            return;
        }

        String url = getFeedbackLayerUrl() + "/0";

        if ("/0".equals(url)) {
            callback2.onFail(new Exception("无法查询到我的上报图层的url"));
            return;
        }

        final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                url, ArcGISFeatureLayer.MODE.SNAPSHOT);
        layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
            @Override
            public void onCallback(FeatureSet featureSet) {
                LogUtil.d("部件查询点返回结果：" + featureSet.getGraphics().length);
//
                List<UploadInfo> uploadedFacilities = new ArrayList<>();
                //判断是新增还是纠错
                if (featureSet.getGraphics().length != 0) {
                    Graphic[] graphics = featureSet.getGraphics();
                    for (Graphic graphic : graphics) {
                        Object o = graphic.getAttributes().get(UploadLayerFieldKeyConstant.REPORT_TYPE);
                        Object markId = graphic.getAttributes().get(UploadLayerFieldKeyConstant.MARK_ID);
                        if (o != null) {
                            if (UploadLayerFieldKeyConstant.CORRECT_ERROR.equals(o.toString()) || UploadLayerFieldKeyConstant.CONFIRM.equals(o.toString())) {
                                //纠错
                                UploadInfo uploadInfo = new UploadInfo();
                                ModifiedFacility modifiedFacilityFromGraphic = getModifiedFacilityFromGraphic(graphic.getAttributes(),graphic.getGeometry());
                                uploadInfo.setModifiedFacilities(modifiedFacilityFromGraphic);
                                uploadInfo.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);
                                uploadInfo.setMarkId(objectToString(markId));

                                CompleteTableInfo completeTableInfo = CompleteTableInfoUtil.getCompleteTableInfo(modifiedFacilityFromGraphic);
                                uploadInfo.setCompleteTableInfo(completeTableInfo);

                                uploadedFacilities.add(uploadInfo);
                            } else {
                                //新增
                                UploadInfo uploadInfo = new UploadInfo();
                                UploadedFacility uploadedFacilityFromGraphic = getUploadedFacilityFromGraphic(graphic.getAttributes(),graphic.getGeometry());
                                uploadInfo.setReportType(o.toString());
                                uploadInfo.setMarkId(objectToString(markId));
                                uploadInfo.setUploadedFacilities(uploadedFacilityFromGraphic);
                                uploadedFacilities.add(uploadInfo);
                            }
                        }
                    }
                    callback2.onSuccess(uploadedFacilities);
                } else {
                    callback2.onSuccess(new ArrayList<UploadInfo>());
                }

                //callback2.onSuccess(uploadedFacilities);
            }

            @Override
            public void onError(Throwable throwable) {
                callback2.onFail(new Exception(throwable));
            }
        });
    }


    /**
     * 获取我的上报图层的Url
     *
     * @return
     */
    @Nullable
    private String getUploadLayerUrl() {
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
     * 获取我的上报图层的Url
     *
     * @return
     */
    @Nullable
    private String getFeedbackLayerUrl() {
        String url = "";
        List<LayerInfo> layerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
        for (LayerInfo layerInfo : layerInfosFromLocal) {
            if (layerInfo.getLayerName().contains(PatrolLayerPresenter.FEEDBACK_LAYER_NAME)) {
                url = layerInfo.getUrl();
            }
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


    /**
     * 根据markId获取上报附件
     *
     * @param uploadInfo
     * @return
     */
    public Observable<UploadInfo> getAttachment(final UploadInfo uploadInfo) {

        /**
         * 纠错
         */
        if(UploadLayerFieldKeyConstant.CORRECT_ERROR.equals(uploadInfo.getReportType())){
            initCorrectFacilityService();
            return correctFacilityService.getMyModificationAttachments(Long.valueOf(uploadInfo.getMarkId()))
                    .map(new Func1<ServerAttachment, UploadInfo>() {
                        @Override
                        public UploadInfo call(ServerAttachment serverAttachment) {
                            if (ListUtil.isEmpty(serverAttachment.getData())) {
                                return uploadInfo;
                            }

                            List<Photo> photos = getPhotos(serverAttachment.getData());
                            if (uploadInfo.getModifiedFacilities() != null) {
                                ModifiedFacility modifiedFacilities = uploadInfo.getModifiedFacilities();
                                modifiedFacilities.setPhotos(photos);
                            } else if (uploadInfo.getUploadedFacilities() != null) {
                                UploadedFacility uploadedFacilities = uploadInfo.getUploadedFacilities();
                                uploadedFacilities.setPhotos(photos);
                            }

                            return uploadInfo;
                        }
                    });
         }else {
            initUploadFacilityService();
            return uploadFacilityService.getMyUploadAttachments(Long.valueOf(uploadInfo.getMarkId()))
                    .map(new Func1<ServerAttachment, UploadInfo>() {
                        @Override
                        public UploadInfo call(ServerAttachment serverAttachment) {
                            if (ListUtil.isEmpty(serverAttachment.getData())) {
                                return uploadInfo;
                            }

                            List<Photo> photos = getPhotos(serverAttachment.getData());
                            if (uploadInfo.getModifiedFacilities() != null) {
                                ModifiedFacility modifiedFacilities = uploadInfo.getModifiedFacilities();
                                modifiedFacilities.setPhotos(photos);
                            } else if (uploadInfo.getUploadedFacilities() != null) {
                                UploadedFacility uploadedFacilities = uploadInfo.getUploadedFacilities();
                                uploadedFacilities.setPhotos(photos);
                            }

                            return uploadInfo;
                        }
                    });
        }
    }

    /**
     * 根据markId获取上报附件
     *
     * @param uploadInfo
     * @return
     */
    public Observable<UploadInfo> getAttachments(final UploadInfo uploadInfo) {

        /**
         * 纠错
         */
        if(UploadLayerFieldKeyConstant.CORRECT_ERROR.equals(uploadInfo.getReportType())){
            initCorrectFacilityService();
            return correctFacilityService.getModificationById(Long.valueOf(uploadInfo.getMarkId()))
                    .map(new Func1<ModifiedFacility, UploadInfo>() {
                        @Override
                        public UploadInfo call(ModifiedFacility serverAttachment) {
                            if (serverAttachment == null) {
                                return uploadInfo;
                            }

                            if (uploadInfo.getModifiedFacilities() != null) {
                                ModifiedFacility modifiedFacilities = uploadInfo.getModifiedFacilities();
                                modifiedFacilities.setMpBeen(serverAttachment.getMpBeen());
                                modifiedFacilities.setPhotos(serverAttachment.getPhotos());
                                modifiedFacilities.setThumbnailPhotos(serverAttachment.getThumbnailPhotos());
                            }
                            return uploadInfo;
                        }
                    });
        }else {
            initUploadFacilityService();
            return uploadFacilityService.getUploadFacilityById(Long.valueOf(uploadInfo.getMarkId()))
                    .map(new Func1<UploadedFacility, UploadInfo>() {
                        @Override
                        public UploadInfo call(UploadedFacility serverAttachment) {
                            if (serverAttachment == null) {
                                return uploadInfo;
                            }
                            if (uploadInfo.getUploadedFacilities() != null) {
                                UploadedFacility uploadedFacilities = uploadInfo.getUploadedFacilities();
                                uploadedFacilities.setMpBeen(serverAttachment.getMpBeen());
                                uploadedFacilities.setPhotos(serverAttachment.getPhotos());
                                uploadedFacilities.setThumbnailPhotos(serverAttachment.getThumbnailPhotos());
                            }

                            return uploadInfo;
                        }
                    });
        }
    }

    private void initCorrectFacilityService() {
        if (correctFacilityService == null){
            correctFacilityService = new CorrectFacilityService(mContext);
        }
    }


    private void initUploadFacilityService() {
        if (uploadFacilityService == null){
            uploadFacilityService = new UploadFacilityService(mContext);
        }
    }

    @NonNull
    private List<Photo> getPhotos(List<ServerAttachment.ServerAttachmentDataBean> serverAttachments) {
        List<Photo> photos = new ArrayList<>();
        if (!ListUtil.isEmpty(serverAttachments)) {
            List<ServerAttachment.ServerAttachmentDataBean> data = serverAttachments;
            for (ServerAttachment.ServerAttachmentDataBean dataBean : data) {
                Photo photo = new Photo();
                photo.setId(Long.valueOf(dataBean.getId()));
                photo.setPhotoPath(dataBean.getAttPath());
                photo.setThumbPath(dataBean.getThumPath());
                photos.add(photo);
            }
        }
        return photos;
    }

    private String objectToString(Object object) {
        if (object == null) {
            return "";
        }
        return StringUtil.getNotNullString(object.toString(),"");
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

    private ModifiedFacility getModifiedFacilityFromGraphic(Map<String, Object> attribute, Geometry geometry) {
        ModifiedFacility modifiedFacility = new ModifiedFacility();
        modifiedFacility.setObjectId(objectToString(attribute.get("OBJECTID")));
        modifiedFacility.setOriginAddr(objectToString(attribute.get("ORIGIN_ADDR")));
        modifiedFacility.setAddr(objectToString(attribute.get("ADDR")));
        modifiedFacility.setRoad(objectToString(attribute.get("ROAD")));
        modifiedFacility.setAttrFive(objectToString(attribute.get("ATTR_FIVE")));
        modifiedFacility.setAttrFour(objectToString(attribute.get("ATTR_FOUR")));
        modifiedFacility.setAttrThree(objectToString(attribute.get("ATTR_THREE")));
        modifiedFacility.setAttrTwo(objectToString(attribute.get("ATTR_TWO")));
        modifiedFacility.setAttrOne(objectToString(attribute.get("ATTR_ONE")));
        modifiedFacility.setCorrectType(objectToString(attribute.get("CORRECT_TYPE")));
        modifiedFacility.setReportType(objectToString(attribute.get("REPORT_TYPE")));
        modifiedFacility.setDescription(objectToString(attribute.get("DESRIPTION")));
        modifiedFacility.setParentOrgName(objectToString(attribute.get("PARENT_ORG_NAME")));
        modifiedFacility.setDirectOrgName(objectToString(attribute.get("DIRECT_ORG_NAME")));
        modifiedFacility.setTeamOrgName(objectToString(attribute.get("TEAM_ORG_NAME")));
        modifiedFacility.setSuperviseOrgName(objectToString(attribute.get("SUPERVISE_ORG_NAME")));
        modifiedFacility.setLayerName(objectToString(attribute.get("LAYER_NAME")));
        modifiedFacility.setUsid(objectToString(attribute.get("US_ID")));
        //修改后的位置
        modifiedFacility.setX(objectToDouble(attribute.get("X")));
        modifiedFacility.setY(objectToDouble(attribute.get("Y")));
        //原设施位置
        //Point geometryCenter = GeometryUtil.getGeometryCenter(geometry);
        modifiedFacility.setOriginX(objectToDouble(attribute.get("ORGIN_X")));
        modifiedFacility.setOriginY(objectToDouble(attribute.get("ORGIN_Y")));
        modifiedFacility.setMarkPerson(objectToString(attribute.get("MARK_PERSON")));
        modifiedFacility.setMarkTime(objectToLong(attribute.get("MARK_TIME")));
        modifiedFacility.setUpdateTime(objectToLong(attribute.get("UPDATE_TIME")));
        modifiedFacility.setCheckState(objectToString(attribute.get("CHECK_STATE")));
        modifiedFacility.setId(objectToLong(attribute.get(UploadLayerFieldKeyConstant.MARK_ID)));

        //原设施信息
        modifiedFacility.setOriginRoad(objectToString(attribute.get("ORIGIN_ROAD")));
        modifiedFacility.setOriginAttrOne(objectToString(attribute.get("ORIGIN_ATTR_ONE")));
        modifiedFacility.setOriginAttrTwo(objectToString(attribute.get("ORIGIN_ATTR_TWO")));
        modifiedFacility.setOriginAttrThree(objectToString(attribute.get("ORIGIN_ATTR_THREE")));
        modifiedFacility.setOriginAttrFour(objectToString(attribute.get("ORIGIN_ATTR_FOUR")));
        modifiedFacility.setOriginAttrFive(objectToString(attribute.get("ORIGIN_ATTR_FIVE")));

        //设施问题
        modifiedFacility.setpCode(objectToString(attribute.get("PCODE")));
        modifiedFacility.setChildCode(objectToString(attribute.get("CHILD_CODE")));

        //管理状态
        modifiedFacility.setCityVillage(objectToString(attribute.get("CITY_VILLAGE")));
        modifiedFacility.setAttrSix(objectToString(attribute.get("ATTR_SIX")));
        modifiedFacility.setAttrSeven(objectToString(attribute.get("ATTR_SEVEN")));
        modifiedFacility.setRiverx(objectToDouble(attribute.get("RIVERX")));
        modifiedFacility.setRivery(objectToDouble(attribute.get("RIVERY")));
        return modifiedFacility;
    }

    private UploadedFacility getUploadedFacilityFromGraphic(Map<String, Object> attribute,Geometry geometry) {
        UploadedFacility uploadedFacility = new UploadedFacility();
        uploadedFacility.setObjectId(objectToString(attribute.get("OBJECTID")));
        uploadedFacility.setAddr(objectToString(attribute.get("ADDR")));
        uploadedFacility.setRoad(objectToString(attribute.get("ROAD")));
        uploadedFacility.setAttrFive(objectToString(attribute.get("ATTR_FIVE")));
        uploadedFacility.setAttrFour(objectToString(attribute.get("ATTR_FOUR")));
        uploadedFacility.setGpbh(objectToString(attribute.get("gpbh")));
        uploadedFacility.setAttrThree(objectToString(attribute.get("ATTR_THREE")));
        uploadedFacility.setAttrTwo(objectToString(attribute.get("ATTR_TWO")));
        uploadedFacility.setAttrOne(objectToString(attribute.get("ATTR_ONE")));
        uploadedFacility.setDescription(objectToString(attribute.get("DESRIPTION")));
        uploadedFacility.setParentOrgName(objectToString(attribute.get("PARENT_ORG_NAME")));
        uploadedFacility.setDirectOrgName(objectToString(attribute.get("DIRECT_ORG_NAME")));
        uploadedFacility.setTeamOrgName(objectToString(attribute.get("TEAM_ORG_NAME")));
        uploadedFacility.setSuperviseOrgName(objectToString(attribute.get("SUPERVISE_ORG_NAME")));
        uploadedFacility.setComponentType(objectToString(attribute.get("LAYER_NAME")));
        uploadedFacility.setLayerName(objectToString(attribute.get("LAYER_NAME")));
        Point geometryCenter = GeometryUtil.getGeometryCenter(geometry);
        uploadedFacility.setX(geometryCenter.getX());
        uploadedFacility.setY(geometryCenter.getY());
        uploadedFacility.setMarkPerson(objectToString(attribute.get("MARK_PERSON")));
        uploadedFacility.setMarkTime(objectToLong(attribute.get("MARK_TIME")));
        uploadedFacility.setUpdateTime(objectToLong(attribute.get("UPDATE_TIME")));
        uploadedFacility.setCheckState(objectToString(attribute.get("CHECK_STATE")));
        uploadedFacility.setId(objectToLong(attribute.get(UploadLayerFieldKeyConstant.MARK_ID)));

        //设施问题
        uploadedFacility.setpCode(objectToString(attribute.get("PCODE")));
        uploadedFacility.setChildCode(objectToString(attribute.get("CHILD_CODE")));
        //管理状态
        uploadedFacility.setCityVillage(objectToString(attribute.get("CITY_VILLAGE")));
        uploadedFacility.setAttrSix(objectToString(attribute.get("ATTR_SIX")));
        uploadedFacility.setAttrSeven(objectToString(attribute.get("ATTR_SEVEN")));
        uploadedFacility.setRiverx(objectToDouble(attribute.get("RIVERX")));
        uploadedFacility.setRivery(objectToDouble(attribute.get("RIVERY")));
        return uploadedFacility;
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
                        doorNOBean.setMph(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.MPWZHM)));
                        doorNOBean.setS_guid(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.S_GUID)));
                        doorNOBean.setStree(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.MPWZHM)));
                        doorNOBean.setDzdm(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.DZDM)));
                        doorNOBean.setPSDY_OID(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.PSDY_OID)));
                        doorNOBean.setISTATUE(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.ISTATUE)));
                        doorNOBean.setPSDY_NAME(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.PSDY_NAME)));
                        doorNOBean.setIsExist(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.ISEXIST)));
                        doorNOBean.setMpObjectId(objectToString(graphic.getAttributes().get(SewerageLayerFieldKeyConstant.OBJECTID)));
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
     * 查询纠正设施的具体信息
     *
     * @param modifiedIdentification
     * @param callback2
     */
    public void queryComponent(UploadedFacility modifiedIdentification, final Callback2<CompleteTableInfo> callback2) {

        if (true) {
            callback2.onFail(new Exception(""));
            return;
        }

        double x = modifiedIdentification.getX();
        double y = modifiedIdentification.getY();
        if (x == 0 || y == 0) {
            callback2.onFail(new Exception("x,y不正确"));
            return;
        }

        initTableManager();

        Point point = new Point(x, y);
        if (TextUtils.isEmpty(modifiedIdentification.getLayerUrl())) {
            searchWithoutLayerUrl(modifiedIdentification, new Callback2<Component>() {
                @Override
                public void onSuccess(Component component) {
                    loadCompleteDataAsync(component, callback2);
                }

                @Override
                public void onFail(Exception error) {
                    callback2.onFail(error);
                }
            }, point);
        } else {

            searchWithoutLayerUrl(modifiedIdentification, new Callback2<Component>() {
                @Override
                public void onSuccess(Component component) {
                    loadCompleteDataAsync(component, callback2);
                }

                @Override
                public void onFail(Exception error) {
                    callback2.onFail(error);
                }
            }, point);
//            queryComponents(modifiedIdentification.getComponentId(), modifiedIdentification.getLayerUrl(), new Callback2<Component>() {
//                @Override
//                public void onSuccess(Component component) {
//                    loadCompleteDataAsync(component, callback2);
//                }
//
//                @Override
//                public void onFail(Exception error) {
//                    callback2.onFail(error);
//                }
//            });
        }
    }

    private List<Project> projects;
    public void initTableManager() {
        tableDataManager = new TableDataManager(mContext.getApplicationContext());
        projects = tableDataManager.getProjectFromDB();
    }

    /**
     * 位置layerUrl的情况下进行查询
     *
     * @param modifiedIdentification
     * @param callback2
     * @param point
     */
    private void searchWithoutLayerUrl(UploadedFacility modifiedIdentification,
                                       final Callback2<Component> callback2, Point point) {
        final ComponentService componentMaintenanceService = new ComponentService(mContext.getApplicationContext());

        String oldLayerUrl = LayerUrlConstant.getLayerUrlByLayerName(modifiedIdentification.getLayerName());
        String currComponentUrl = LayerUrlConstant.getNewLayerUrlByLayerName(modifiedIdentification.getLayerName());

        componentMaintenanceService.queryComponents(point, oldLayerUrl, currComponentUrl, new Callback2<List<QueryFeatureSet>>() {
            @Override
            public void onSuccess(List<QueryFeatureSet> queryFeatureSetList) {
                if (ListUtil.isEmpty(queryFeatureSetList)) {
                    return;
                }
                List<Component> componentQueryResult = new ArrayList<Component>();
                for (QueryFeatureSet queryFeatureSet : queryFeatureSetList) {
                    FeatureSet featureSet = queryFeatureSet.getFeatureSet();
                    Graphic[] graphics = featureSet.getGraphics();
                    if (graphics == null
                            || graphics.length <= 0) {
                        continue;
                    }

                    for (Graphic graphic : graphics) {
                        Component component = new Component();
                        component.setLayerUrl(queryFeatureSet.getLayerUrl());
                        component.setLayerName(queryFeatureSet.getLayerName());
                        component.setDisplayFieldName(featureSet.getDisplayFieldName());
//                        component.setFieldAlias(featureSet.getFieldAliases());
//                        component.setFields(featureSet.getFields());
                        component.setGraphic(graphic);
                        Object o = graphic.getAttributes().get(featureSet.getObjectIdFieldName());
                        if (o != null && o instanceof Integer) {
                            component.setObjectId((Integer) o); //按照道理objectId一定是integer的
                        }
                        componentQueryResult.add(component);
                    }
                }
                //todo 这里应该还要进行对比objectId才对

                callback2.onSuccess(componentQueryResult.get(0));
            }

            @Override
            public void onFail(Exception error) {
                callback2.onFail(error);
            }
        });
    }

    private void loadCompleteDataAsync(final Component component, final Callback2<CompleteTableInfo> callback2) {

//        final TableDataManager tableDataManager = new TableDataManager(mContext.getApplicationContext());
//        List<Project> projects = tableDataManager.getProjectFromDB();

        Project project = null;
        for (Project p : projects) {
            if (p.getName().equals(component.getLayerName())) {
                project = p;
            }
        }
        if (project == null) {
            callback2.onFail(new Exception("加载详细信息失败！"));

            //ToastUtil.shortToast(getContext(), "加载详细信息失败！");
            return;
        }
        final String projectId = project.getId();
        String getFormStructureUrl = BaseInfoManager.getBaseServerUrl(mContext) + "rest/report/rptform";
        tableDataManager.getTableItemsFromNet(project.getId(), getFormStructureUrl, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                TableItems tmp = (TableItems) data;
                if (tmp.getResult() != null) {
                    List<TableItem> tableItems;
                    tableItems = new ArrayList<TableItem>();
                    tableItems.addAll(tmp.getResult());
                    //   tableDataManager.setTableItemsToDB(tableItems);
                    //缓存在数据库中
                    Realm realm = Realm.getDefaultInstance();
                    ProjectTable projectTable = new ProjectTable();
                    projectTable.setId(projectId);
                    realm.beginTransaction();
                    projectTable.setTableItems(new RealmList<TableItem>(tableItems.toArray(new TableItem[tableItems.size()])));
                    realm.commitTransaction();
                    tableDataManager.setProjectTableToDB(projectTable);
                    ArrayList<TableItem> completeTableItems = EditLayerService.getCompleteTableItem(component.getGraphic(), tableItems);
                    TableViewManager.isEditingFeatureLayer = true;
                    TableViewManager.graphic = component.getGraphic();
                    TableViewManager.geometry = component.getGraphic().getGeometry();
//                    TableViewManager.featueLayerUrl = component.getLayerUrl();
                    TableViewManager.featueLayerUrl = LayerUrlConstant.getNewLayerUrlByUnknownLayerUrl(component.getLayerUrl());
                    queryAttachmentInfosAsync(component.getLayerUrl(), component.getGraphic(), completeTableItems, callback2);
                } else {
                    callback2.onFail(new Exception("获取表单数据出错"));
                }
            }

            @Override
            public void onError(String msg) {
                callback2.onFail(new Exception(msg));
            }
        });
    }

    private void queryAttachmentInfosAsync(String layerUrl, final Graphic graphic,
                                           final List<TableItem> tableItems, final Callback2<CompleteTableInfo> callback2) {

        final ArcGISFeatureLayer arcGISFeatureLayer = new ArcGISFeatureLayer(layerUrl, ArcGISFeatureLayer.MODE.SNAPSHOT);
        arcGISFeatureLayer.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                final int objectid = Integer.valueOf(graphic.getAttributes().get(arcGISFeatureLayer.getObjectIdField()).toString());
                FileService fileService = new FileService(mContext);
                fileService.getList(arcGISFeatureLayer.getName(), objectid + "")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<FileResult>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable throwable) {
                                CompleteTableInfo completeTableInfo = new CompleteTableInfo();

                                completeTableInfo.setAttrs(graphic.getAttributes());
                                callback2.onSuccess(completeTableInfo);
                            }

                            @Override
                            public void onNext(List<FileResult> fileResults) {
                                if (ListUtil.isEmpty(fileResults)) {
                                    CompleteTableInfo completeTableInfo = new CompleteTableInfo();

                                    completeTableInfo.setAttrs(graphic.getAttributes());
                                    callback2.onSuccess(completeTableInfo);
                                    return;
                                }
                                Map<String, Integer> map = new HashMap<>();
                                List<Photo> photoList = new ArrayList<Photo>();
                                for (FileResult fileResult : fileResults) {
                                    if (map.containsKey(fileResult.getAttachName())) {
                                        continue;
                                    }
                                    if (!fileResult.getType().contains("image")) {
                                        continue;
                                    }
                                    Photo photo = new Photo();
                                    photo.setPhotoPath(fileResult.getFilePath());
                                    photo.setField1("photo");
                                    photo.setHasBeenUp(1);
                                    photoList.add(photo);
                                    map.put(fileResult.getAttachName(), fileResult.getId());
                                }

                                CompleteTableInfo completeTableInfo = new CompleteTableInfo();

                                completeTableInfo.setPhotos(photoList);
                                completeTableInfo.setAttrs(graphic.getAttributes());
                                callback2.onSuccess(completeTableInfo);
                            }
                        });
            }
        });
    }

}
