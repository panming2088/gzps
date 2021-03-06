package com.augurit.agmobile.gzps.maintain.service;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.maintain.model.MaintainBatchInfo;
import com.augurit.agmobile.gzps.uploadfacility.dao.UploadLayerApi;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.service.CorrectFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.util.UploadLayerFieldKeyConstant;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.patrolcore.layer.service.AgwebPatrolLayerService2;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.log.LogUtil;
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

import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * ?????????????????????????????????
 * Created by xcl on 2017/12/7.
 */

public class MaintainLayerService extends AgwebPatrolLayerService2{

    private AMNetwork amNetwork;
    private UploadLayerApi uploadLayerApi;
    private Query query;
    private CorrectFacilityService correctFacilityService;
    private MaintainFacilityService uploadFacilityService;

    public MaintainLayerService(Context mContext) {
       super(mContext);
    }

    @Override
    public Observable<List<LayerInfo>> getLayerInfo() {
        return super.getSortedLayerInfos()
                .map(new Func1<List<LayerInfo>, List<LayerInfo>>() {
                    @Override
                    public List<LayerInfo> call(List<LayerInfo> layerInfos) {
                        //????????????????????????
                        LayerInfo info = null;
                        List<LayerInfo> tempInfo = new ArrayList<>();
                        for (LayerInfo layerInfo : layerInfos){
                            if(layerInfo.getLayerName().contains(PatrolLayerPresenter.UPLOAD_LAYER_NAME) || layerInfo.getLayerName().contains(PatrolLayerPresenter.NW_UPLOAD_LAYER_NAME)){
                                continue;
                            }
                            tempInfo.add(layerInfo);
                        }
                        return tempInfo;
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

    public Query setQueryByObjectId(String objectId) {
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setWhere("OBJECTID="+objectId);
        return query;
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param point
     * @param spatialReference
     * @param resolution
     * @param callback2
     */
    public void queryUploadDataInfo(Point point, SpatialReference spatialReference, double resolution,
                                    final Callback2<List<MaintainBatchInfo.RowsBean>> callback2) {

        Geometry geometry = GeometryEngine.buffer(point, spatialReference, 60 * resolution, null);
        if (query == null) {
            callback2.onFail(new Exception("??????????????????"));
            return;
        }
        query.setGeometry(geometry);

        String url = getFeedbackLayerUrl() + "/0";

        if ("/0".equals(url)) {
            callback2.onFail(new Exception("????????????????????????????????????url"));
            return;
        }

        final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                url, ArcGISFeatureLayer.MODE.SNAPSHOT);
        layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
            @Override
            public void onCallback(FeatureSet featureSet) {
                LogUtil.d("??????????????????????????????" + featureSet.getGraphics().length);
//
                final List<MaintainBatchInfo.RowsBean> uploadedFacilities = new ArrayList<>();
                //???????????????????????????
                if (featureSet.getGraphics().length != 0) {
                    Graphic[] graphics = featureSet.getGraphics();
                    for (Graphic graphic : graphics) {
                        uploadedFacilities.add(getBatchInfoFromAttribute(graphic));
                    }

                    if (!ListUtil.isEmpty(uploadedFacilities)) {
                        ((Activity)mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback2.onSuccess(uploadedFacilities);
                            }
                        });
                    }
                } else {
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback2.onSuccess(new ArrayList<MaintainBatchInfo.RowsBean>());
                        }
                    });

                }

                //callback2.onSuccess(uploadedFacilities);
            }

            @Override
            public void onError(Throwable throwable) {
                callback2.onFail(new Exception(throwable));
            }
        });
    }

    private MaintainBatchInfo.RowsBean getBatchInfoFromAttribute(Graphic graphic) {
        Map<String, Object> attributes = graphic.getAttributes();
        if(MapUtils.isEmpty(attributes)){
            return null;
        }
        MaintainBatchInfo.RowsBean rowsBean = new MaintainBatchInfo.RowsBean();
        rowsBean.setObjectId(String.valueOf(attributes.get("OBJECTID")));
        rowsBean.setName(String.valueOf(attributes.get("NAME")));
        rowsBean.setContent(String.valueOf(attributes.get("CONTENT")));
        rowsBean.setStartDate(Long.valueOf(String.valueOf(attributes.get("YHKSRQ"))));
        rowsBean.setEndDate(Long.valueOf(String.valueOf(attributes.get("YHJSRQ"))));
        rowsBean.setGeometry(graphic.getGeometry());
        return rowsBean;
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param callback2
     */
    public void queryUploadDataForObjectId(final Callback2<List<MaintainBatchInfo.RowsBean>> callback2) {
        String url = getFeedbackLayerUrl() + "/0";

        if ("/0".equals(url)) {
            callback2.onFail(new Exception("????????????????????????????????????url"));
            return;
        }

        final ArcGISFeatureLayer layer = new ArcGISFeatureLayer(
                url, ArcGISFeatureLayer.MODE.SNAPSHOT);
        layer.queryFeatures(query, new CallbackListener<FeatureSet>() {
            @Override
            public void onCallback(FeatureSet featureSet) {
                LogUtil.d("??????????????????????????????" + featureSet.getGraphics().length);
                LogUtil.d("??????????????????????????????" + featureSet.getGraphics().length);
//
                final List<MaintainBatchInfo.RowsBean> uploadedFacilities = new ArrayList<>();
                //???????????????????????????
                if (featureSet.getGraphics().length != 0) {
                    Graphic[] graphics = featureSet.getGraphics();
                    for (Graphic graphic : graphics) {
                        uploadedFacilities.add(getBatchInfoFromAttribute(graphic));
                    }

                    if (!ListUtil.isEmpty(uploadedFacilities)) {
                        ((Activity)mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback2.onSuccess(uploadedFacilities);
                            }
                        });
                    }
                } else {
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback2.onSuccess(new ArrayList<MaintainBatchInfo.RowsBean>());
                        }
                    });

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
                    //??????????????????
                    @Override
                    public Observable<UploadInfo> call(final UploadInfo uploadInfo) {
                        //??????markId???????????????????????????
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


    /**
     * ???????????????????????????Url
     *
     * @return
     */
    @Nullable
    private String getFeedbackLayerUrl() {
        String url = "";
        List<LayerInfo> layerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
        if(ListUtil.isEmpty(layerInfosFromLocal)){
            return url;
        }
        for (LayerInfo layerInfo : layerInfosFromLocal) {
            if (layerInfo.getLayerName().contains(PatrolLayerPresenter.MAINTAIN_LAYER_NAME)) {
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
     * ??????markId??????????????????
     *
     * @param uploadInfo
     * @return
     */
    public Observable<UploadInfo> getAttachment(final UploadInfo uploadInfo) {

        /**
         * ??????
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
                                modifiedFacilities.setObjectId(uploadInfo.getModifiedFacilities().getObjectId());
                                modifiedFacilities.setPhotos(photos);
                            } else if (uploadInfo.getUploadedFacilities() != null) {
                                UploadedFacility uploadedFacilities = uploadInfo.getUploadedFacilities();
                                uploadedFacilities.setObjectId(uploadInfo.getUploadedFacilities().getObjectId());
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

    private void initCorrectFacilityService() {
        if (correctFacilityService == null){
            correctFacilityService = new CorrectFacilityService(mContext);
        }
    }


    private void initUploadFacilityService() {
        if (uploadFacilityService == null){
            uploadFacilityService = new MaintainFacilityService(mContext);
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
        //??????????????????
        modifiedFacility.setX(objectToDouble(attribute.get("X")));
        modifiedFacility.setY(objectToDouble(attribute.get("Y")));
        //???????????????
        //Point geometryCenter = GeometryUtil.getGeometryCenter(geometry);
        modifiedFacility.setOriginX(objectToDouble(attribute.get("ORGIN_X")));
        modifiedFacility.setOriginY(objectToDouble(attribute.get("ORGIN_Y")));
        modifiedFacility.setMarkPerson(objectToString(attribute.get("MARK_PERSON")));
        modifiedFacility.setMarkTime(objectToLong(attribute.get("MARK_TIME")));
        modifiedFacility.setUpdateTime(objectToLong(attribute.get("UPDATE_TIME")));
        modifiedFacility.setCheckState(objectToString(attribute.get("CHECK_STATE")));
        modifiedFacility.setId(objectToLong(attribute.get(UploadLayerFieldKeyConstant.MARK_ID)));

        //???????????????
        modifiedFacility.setOriginRoad(objectToString(attribute.get("ORIGIN_ROAD")));
        modifiedFacility.setOriginAttrOne(objectToString(attribute.get("ORIGIN_ATTR_ONE")));
        modifiedFacility.setOriginAttrTwo(objectToString(attribute.get("ORIGIN_ATTR_TWO")));
        modifiedFacility.setOriginAttrThree(objectToString(attribute.get("ORIGIN_ATTR_THREE")));
        modifiedFacility.setOriginAttrFour(objectToString(attribute.get("ORIGIN_ATTR_FOUR")));
        modifiedFacility.setOriginAttrFive(objectToString(attribute.get("ORIGIN_ATTR_FIVE")));

        //????????????
        modifiedFacility.setpCode(objectToString(attribute.get("PCODE")));
        modifiedFacility.setChildCode(objectToString(attribute.get("CHILD_CODE")));

        //????????????
        modifiedFacility.setCityVillage(objectToString(attribute.get("CITY_VILLAGE")));
        modifiedFacility.setQDBH(objectToString(attribute.get("QDBH")));
        modifiedFacility.setQDZT(objectToString(attribute.get("QDZT")));
        modifiedFacility.setOLDOID(objectToString(attribute.get("OLDOID")));
        return modifiedFacility;
    }

    private UploadedFacility getUploadedFacilityFromGraphic(Map<String, Object> attribute,Geometry geometry) {
        UploadedFacility uploadedFacility = new UploadedFacility();
        uploadedFacility.setObjectId(objectToString(attribute.get("OBJECTID")));
        uploadedFacility.setAddr(objectToString(attribute.get("ADDR")));
        uploadedFacility.setRoad(objectToString(attribute.get("ROAD")));
        uploadedFacility.setAttrFive(objectToString(attribute.get("ATTR_FIVE")));
        uploadedFacility.setAttrFour(objectToString(attribute.get("ATTR_FOUR")));
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

        //????????????
        uploadedFacility.setpCode(objectToString(attribute.get("PCODE")));
        uploadedFacility.setChildCode(objectToString(attribute.get("CHILD_CODE")));
        //????????????
        uploadedFacility.setCityVillage(objectToString(attribute.get("CITY_VILLAGE")));
        uploadedFacility.setQDBH(objectToString(attribute.get("QDBH")));
        uploadedFacility.setQDZT(objectToString(attribute.get("QDZT")));
        uploadedFacility.setOLDOID(objectToString(attribute.get("OLDOID")));
        return uploadedFacility;
    }
    /**
     * ??????????????????????????????
     * @param page
     * @param countPerPage
     * @return
     */
    public Observable<MaintainBatchInfo> getBatchInfo(int page, int countPerPage) {
        initUploadFacilityService();
        return    uploadFacilityService.getBatchInfo( page,  countPerPage);
    }
}
