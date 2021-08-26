package com.augurit.agmobile.gzps.setting.service;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.componentmaintenance.model.QueryFeatureSet;
import com.augurit.agmobile.gzps.componentmaintenance.service.ComponentService;
import com.augurit.agmobile.gzps.publicaffair.service.FacilityAffairService;
import com.augurit.agmobile.gzps.setting.dao.MyCorrectFacilityApi;
import com.augurit.agmobile.gzps.setting.view.myupload.Result3;
import com.augurit.agmobile.gzps.uploadfacility.model.CompleteTableInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.FacilityFilterCondition;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
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
import com.augurit.agmobile.patrolcore.layer.service.EditLayerService;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.ListUtil;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.query.Query;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 设施纠错Service
 * <p>
 * Created by xcl on 2017/11/13.
 */

public class MyCorrectFacilityService {

    private AMNetwork amNetwork;
    private MyCorrectFacilityApi correctFacilityApi;
    private Context mContext;
    private List<Project> projects;
    private TableDataManager tableDataManager;

    public MyCorrectFacilityService(Context mContext) {
        this.mContext = mContext;
    }

    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null) {
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.setReadTime(20000);
            this.amNetwork.setWriteTime(20000);
            this.amNetwork.setConnectTime(20000);
            this.amNetwork.build();
            this.amNetwork.registerApi(MyCorrectFacilityApi.class);
            this.correctFacilityApi = (MyCorrectFacilityApi) this.amNetwork.getServiceApi(MyCorrectFacilityApi.class);
        }
    }


    public void initTableManager() {
        tableDataManager = new TableDataManager(mContext.getApplicationContext());
        projects = tableDataManager.getProjectFromDB();
    }

    /**
     * 修正部件标识
     *
     * @param modifiedIdentification
     * @return
     */
    public Observable<ResponseBody> upload(ModifiedFacility modifiedIdentification) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        String prefix = "";
        int i = 0;
        List<Photo> attachments = modifiedIdentification.getPhotos();
        final HashMap<String, HashMap<String, RequestBody>> map = new HashMap<>();    //
        HashMap<String, RequestBody> requestMap = new HashMap<>();
        if (!ListUtil.isEmpty(attachments)) {
            for (Photo photo : attachments) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }

        }
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        String loginName = user.getLoginName();
        String userName = user.getUserName();

        Observable<ResponseBody> observable;

        if (!ListUtil.isEmpty(attachments)) {
            observable = correctFacilityApi.partsAdd(
                    modifiedIdentification.getId(),
                    loginName,
                    modifiedIdentification.getMarkPersonId(),
                    userName,
//                    modifiedIdentification.getMarkTime(),
                    modifiedIdentification.getDescription(),
                    modifiedIdentification.getLayerName(),
                    modifiedIdentification.getUsid(),
                    modifiedIdentification.getAttrOne(),
                    modifiedIdentification.getAttrTwo(),
                    modifiedIdentification.getAttrThree(),
                    modifiedIdentification.getAttrFour(),
                    modifiedIdentification.getAttrFive(),
                    modifiedIdentification.getOriginX(),
                    modifiedIdentification.getOriginY(),
                    modifiedIdentification.getOriginAddr(),
                    modifiedIdentification.getAddr(),
                    modifiedIdentification.getX(),
                    modifiedIdentification.getY(),
                    modifiedIdentification.getCorrectType(),
                    modifiedIdentification.getLayerUrl(),
                    modifiedIdentification.getRoad(),
                    modifiedIdentification.getReportType(),
                    modifiedIdentification.getUserX(),
                    modifiedIdentification.getUserY(),
                    modifiedIdentification.getUserAddr(),
                    modifiedIdentification.getOriginRoad(),
                    modifiedIdentification.getOriginAttrOne(),
                    modifiedIdentification.getOriginAttrTwo(),
                    modifiedIdentification.getOriginAttrThree(),
                    modifiedIdentification.getOriginAttrFour(),
                    modifiedIdentification.getOriginAttrFive(),
                    modifiedIdentification.getDeletedPhotoIds()
                    , requestMap);
        } else {

            observable = correctFacilityApi.partsAdd(
                    modifiedIdentification.getId(),
                    loginName,
                    modifiedIdentification.getMarkPersonId(),
                    userName,
//                    modifiedIdentification.getMarkTime(),
                    modifiedIdentification.getDescription(),
                    modifiedIdentification.getLayerName(),
                    modifiedIdentification.getUsid(),
                    modifiedIdentification.getAttrOne(),
                    modifiedIdentification.getAttrTwo(),
                    modifiedIdentification.getAttrThree(),
                    modifiedIdentification.getAttrFour(),
                    modifiedIdentification.getAttrFive(),
                    modifiedIdentification.getOriginX(),
                    modifiedIdentification.getOriginY(),
                    modifiedIdentification.getOriginAddr(),
                    modifiedIdentification.getAddr(),
                    modifiedIdentification.getX(),
                    modifiedIdentification.getY(),
                    modifiedIdentification.getCorrectType(),
                    modifiedIdentification.getLayerUrl(),
                    modifiedIdentification.getRoad(),
                    modifiedIdentification.getReportType(),
                    modifiedIdentification.getUserX(),
                    modifiedIdentification.getUserY(),
                    modifiedIdentification.getUserAddr(),
                    modifiedIdentification.getOriginRoad(),
                    modifiedIdentification.getOriginAttrOne(),
                    modifiedIdentification.getOriginAttrTwo(),
                    modifiedIdentification.getOriginAttrThree(),
                    modifiedIdentification.getOriginAttrFour(),
                    modifiedIdentification.getOriginAttrFive(),
                    modifiedIdentification.getDeletedPhotoIds()
            );
        }


        return observable
                .subscribeOn(Schedulers.io());
    }

//    /**
//     * 新增
//     *
//     * @param modifiedIdentification
//     * @return
//     */
//    private Observable<ResponseBody> add(ModifiedFacility modifiedIdentification) {
//        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
//        initAMNetwork(supportUrl);
//
//        String prefix = "";
//        int i = 0;
//        List<Photo> attachments = modifiedIdentification.getPhotos();
//        final HashMap<String, HashMap<String, RequestBody>> map = new HashMap<>();    //
//        HashMap<String, RequestBody> requestMap = new HashMap<>();
//        if (!ListUtil.isEmpty(attachments)) {
//            for (Photo photo : attachments) {
//                if (photo.getLocalPath() != null) {
//                    File file = new File(photo.getLocalPath());
//                    requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
//                    i++;
//                }
//            }
//
//        }
//
//        String userName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
//
//        Observable<ResponseBody> observable;
//
//        if (!ListUtil.isEmpty(attachments)) {
//            observable = correctFacilityApi.partsAdd(
//                    modifiedIdentification.getId(),
//                    userName,
//                    modifiedIdentification.getMarkPersonId(),
//                    modifiedIdentification.getMarkPerson(),
//                    modifiedIdentification.getMarkTime(),
//                    modifiedIdentification.getDescription(),
//                    modifiedIdentification.getLayerName(),
//                    modifiedIdentification.getUsid(),
//                    modifiedIdentification.getAttrOne(),
//                    modifiedIdentification.getPipeType(),
//                    modifiedIdentification.getAttrThree(),
//                    modifiedIdentification.getAttrFour(),
//                    modifiedIdentification.getAttrFive(),
//                    modifiedIdentification.getOriginX(),
//                    modifiedIdentification.getOriginY(),
//                    modifiedIdentification.getOriginAddr(),
//                    modifiedIdentification.getAddr(),
//                    modifiedIdentification.getX(),
//                    modifiedIdentification.getY(),
//                    modifiedIdentification.getCorrectType(),
//                    modifiedIdentification.getLayerUrl(),
//                    modifiedIdentification.getRoad(),
//                    modifiedIdentification.getReportType(),
//                    modifiedIdentification.getUserX(),
//                    modifiedIdentification.getUserY(),
//                    modifiedIdentification.getUserAddr(),
//                    modifiedIdentification.getOriginRoad(),
//                    modifiedIdentification.getOriginAttrOne(),
//                    modifiedIdentification.getOriginAttrTwo(),
//                    modifiedIdentification.getOriginAttrThree(),
//                    modifiedIdentification.getOriginAttrFour(),
//                    modifiedIdentification.getOriginAttrFive()
//                    , requestMap);
//        } else {
//
//            observable = correctFacilityApi.partsAdd(
//                    modifiedIdentification.getId(),
//                    userName,
//                    modifiedIdentification.getMarkPersonId(),
//                    modifiedIdentification.getMarkPerson(),
//                    modifiedIdentification.getMarkTime(),
//                    modifiedIdentification.getDescription(),
//                    modifiedIdentification.getLayerName(),
//                    modifiedIdentification.getUsid(),
//                    modifiedIdentification.getAttrOne(),
//                    modifiedIdentification.getPipeType(),
//                    modifiedIdentification.getAttrThree(),
//                    modifiedIdentification.getAttrFour(),
//                    modifiedIdentification.getAttrFive(),
//                    modifiedIdentification.getOriginX(),
//                    modifiedIdentification.getOriginY(),
//                    modifiedIdentification.getOriginAddr(),
//                    modifiedIdentification.getAddr(),
//                    modifiedIdentification.getX(),
//                    modifiedIdentification.getY(),
//                    modifiedIdentification.getCorrectType(),
//                    modifiedIdentification.getLayerUrl(),
//                    modifiedIdentification.getRoad(),
//                    modifiedIdentification.getReportType(),
//                    modifiedIdentification.getUserX(),
//                    modifiedIdentification.getUserY(),
//                    modifiedIdentification.getUserAddr(),
//                    modifiedIdentification.getOriginRoad(),
//                    modifiedIdentification.getOriginAttrOne(),
//                    modifiedIdentification.getOriginAttrTwo(),
//                    modifiedIdentification.getOriginAttrThree(),
//                    modifiedIdentification.getOriginAttrFour(),
//                    modifiedIdentification.getOriginAttrFive()
//            );
//        }
//
//
//        return observable
//                .subscribeOn(Schedulers.io());
//    }
//
//    /**
//     * 修改
//     *
//     * @param modifiedIdentification
//     * @return
//     */
//    private Observable<ResponseBody> update(ModifiedFacility modifiedIdentification) {
//        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
//        initAMNetwork(supportUrl);
//
//        String prefix = "";
//        int i = 0;
//        List<Photo> attachments = modifiedIdentification.getPhotos();
//        final HashMap<String, HashMap<String, RequestBody>> map = new HashMap<>();    //
//        HashMap<String, RequestBody> requestMap = new HashMap<>();
//        if (!ListUtil.isEmpty(attachments)) {
//            for (Photo photo : attachments) {
//                if (photo.getLocalPath() != null) {
//                    File file = new File(photo.getLocalPath());
//                    requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
//                    i++;
//                }
//            }
//
//        }
//
//        String userName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
//
//        Observable<ResponseBody> observable;
//
//        if (!MapUtils.isEmpty(requestMap)) {
//            observable = correctFacilityApi.partsUpdate(
//                    modifiedIdentification.getId(),
//                    userName,
//                    modifiedIdentification.getMarkPersonId(),
//                    modifiedIdentification.getMarkPerson(),
//                    modifiedIdentification.getMarkTime(),
//                    modifiedIdentification.getDescription(),
//                    modifiedIdentification.getLayerName(),
//                    modifiedIdentification.getUsid(),
//                    modifiedIdentification.getAttrOne(),
//                    modifiedIdentification.getPipeType(),
//                    modifiedIdentification.getAttrThree(),
//                    modifiedIdentification.getAttrFour(),
//                    modifiedIdentification.getAttrFive(),
//                    modifiedIdentification.getOriginX(),
//                    modifiedIdentification.getOriginY(),
//                    modifiedIdentification.getOriginAddr(),
//                    modifiedIdentification.getAddr(),
//                    modifiedIdentification.getX(),
//                    modifiedIdentification.getY(),
//                    modifiedIdentification.getCorrectType(),
//                    modifiedIdentification.getLayerUrl(),
//                    modifiedIdentification.getRoad(),
//                    modifiedIdentification.getReportType(),
//                    modifiedIdentification.getUserX(),
//                    modifiedIdentification.getUserY(),
//                    modifiedIdentification.getUserAddr(),
//                    modifiedIdentification.getOriginRoad(),
//                    modifiedIdentification.getOriginAttrOne(),
//                    modifiedIdentification.getOriginAttrTwo(),
//                    modifiedIdentification.getOriginAttrThree(),
//                    modifiedIdentification.getOriginAttrFour(),
//                    modifiedIdentification.getOriginAttrFive()
//                    , requestMap);
//        } else {
//
//            observable = correctFacilityApi.partsUpdate(
//                    modifiedIdentification.getId(),
//                    userName,
//                    modifiedIdentification.getMarkPersonId(),
//                    modifiedIdentification.getMarkPerson(),
//                    modifiedIdentification.getMarkTime(),
//                    modifiedIdentification.getDescription(),
//                    modifiedIdentification.getLayerName(),
//                    modifiedIdentification.getUsid(),
//                    modifiedIdentification.getAttrOne(),
//                    modifiedIdentification.getPipeType(),
//                    modifiedIdentification.getAttrThree(),
//                    modifiedIdentification.getAttrFour(),
//                    modifiedIdentification.getAttrFive(),
//                    modifiedIdentification.getOriginX(),
//                    modifiedIdentification.getOriginY(),
//                    modifiedIdentification.getOriginAddr(),
//                    modifiedIdentification.getAddr(),
//                    modifiedIdentification.getX(),
//                    modifiedIdentification.getY(),
//                    modifiedIdentification.getCorrectType(),
//                    modifiedIdentification.getLayerUrl(),
//                    modifiedIdentification.getRoad(),
//                    modifiedIdentification.getReportType(),
//                    modifiedIdentification.getUserX(),
//                    modifiedIdentification.getUserY(),
//                    modifiedIdentification.getUserAddr(),
//                    modifiedIdentification.getOriginRoad(),
//                    modifiedIdentification.getOriginAttrOne(),
//                    modifiedIdentification.getOriginAttrTwo(),
//                    modifiedIdentification.getOriginAttrThree(),
//                    modifiedIdentification.getOriginAttrFour(),
//                    modifiedIdentification.getOriginAttrFive()
//            );
//        }
//
//
//        return observable
//                .subscribeOn(Schedulers.io());
//    }
//
//
//    private String getIp() {
//        String baseServerUrlWithoutRestSystem = BaseInfoManager.getBaseServerUrlWithoutRestSystem(mContext);
//        int i = baseServerUrlWithoutRestSystem.lastIndexOf("/");
//        String ip = baseServerUrlWithoutRestSystem.substring(0, i);
//        return ip;
//    }


    /**
     * 获取我的修正列表
     *
     * @return
     */
    public Observable<Result3<List<ModifiedFacility>>> getMyModifications(int page, int pageSize, String checkState,
                                                                          FacilityFilterCondition facilityFilterCondition) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        if (facilityFilterCondition == null) {
            return correctFacilityApi.getPartsCorr(page, pageSize, loginName, checkState,
                    null, null, null, null, null, null,null);
        } else {

            if (facilityFilterCondition.facilityType != null && facilityFilterCondition.facilityType.equals("全部")) {
                facilityFilterCondition.facilityType = null;
            }

            /**
             * 只有当是窨井时，才有已挂牌编号
             */
            if (TextUtils.isEmpty(facilityFilterCondition.attrFive)
                    || facilityFilterCondition.facilityType == null
                    || !facilityFilterCondition.facilityType.equals("窨井")) {
                facilityFilterCondition.attrFive = null;
            }

            if (TextUtils.isEmpty(facilityFilterCondition.road)) {
                facilityFilterCondition.road = null;
            }

            if (TextUtils.isEmpty(facilityFilterCondition.address)) {
                facilityFilterCondition.address = null;
            }

            return correctFacilityApi.getPartsCorr(page,
                    pageSize,
                    loginName,
                    checkState,
                    facilityFilterCondition.startTime,
                    facilityFilterCondition.endTime,
                    facilityFilterCondition.attrFive,
                    facilityFilterCondition.road,
                    facilityFilterCondition.address,
                    facilityFilterCondition.facilityType,
                    facilityFilterCondition.markId);
        }

    }

    /**
     * 通过设施ID获取详情信息
     *
     * @param markId 设施id
     * @return 详细信息
     */
    public Observable<ModifiedFacility> getModificationById(long markId) {
        FacilityAffairService facilityAffairService = new FacilityAffairService(mContext);
        return facilityAffairService.getModifiedDetail(markId);
    }

    /**
     * 获取附件列表
     *
     * @param markId 部件标识id
     * @return
     */
    public Observable<ServerAttachment> getMyModificationAttachments(Long markId) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        return correctFacilityApi.getPartsCorrAttach(markId)
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, ServerAttachment>() {
                    @Override
                    public ServerAttachment call(Throwable throwable) {
                        ServerAttachment serverIdentificationAttachment = new ServerAttachment();
                        serverIdentificationAttachment.setCode(500);
                        serverIdentificationAttachment.setMessage("500 - 系统内部错误");
                        return serverIdentificationAttachment;
                    }
                });
    }


    /**
     * 查询纠正设施的具体信息
     *
     * @param modifiedIdentification
     * @param callback2
     */
    public void queryComponent(final ModifiedFacility modifiedIdentification, final Callback2<CompleteTableInfo> callback2) {

        //2017.12.17 暂时屏蔽掉查询
        if (true) {
            callback2.onFail(new Exception("缺少url，无法查询具体信息"));
            return;
        }

        initTableManager();

        if (TextUtils.isEmpty(modifiedIdentification.getUsid())) {
            callback2.onFail(new Exception("缺少USID，无法查找到部件详细信息"));
            return;
        }
        /**
         * 如果缺少USID，那么用layerUrl+ x,y坐标查询
         */
        if (TextUtils.isEmpty(modifiedIdentification.getLayerUrl())) {
            queryComponentsWithLayerName(modifiedIdentification.getUsid(), modifiedIdentification.getLayerName(), new Callback2<Component>() {
                @Override
                public void onSuccess(Component component) {
                    //纠正由于地图更改（有可能对地图重新做了偏移）导致的位置不准确问题
                    //按照道理，一定是点
                    Point geometryCenter = GeometryUtil.getGeometryCenter(component.getGraphic().getGeometry());
                    modifiedIdentification.setOriginX(geometryCenter.getX());
                    modifiedIdentification.setOriginY(geometryCenter.getY());
                    loadCompleteDataAsync(component, callback2);
                }

                @Override
                public void onFail(Exception error) {
                    callback2.onFail(error);
                }
            });
        } else {
            /**
             * 用USID + LayerUrl查询
             */
            queryComponents(modifiedIdentification.getUsid(), modifiedIdentification.getLayerUrl(), new Callback2<Component>() {
                @Override
                public void onSuccess(Component component) {
                    //纠正由于地图更改（有可能对地图重新做了偏移）导致的位置不准确问题
                    //按照道理，一定是点
                    Point geometryCenter = GeometryUtil.getGeometryCenter(component.getGraphic().getGeometry());
                    modifiedIdentification.setOriginX(geometryCenter.getX());
                    modifiedIdentification.setOriginY(geometryCenter.getY());
                    loadCompleteDataAsync(component, callback2);
                }

                @Override
                public void onFail(Exception error) {
                    /**
                     * 如果查询失败了，说明地图服务可能换了图层顺序，导致原来的Url找不到当前的部件，那么用layerName + usid 查
                     */
                    queryComponentsWithLayerName(modifiedIdentification.getUsid(), modifiedIdentification.getLayerName(), new Callback2<Component>() {
                        @Override
                        public void onSuccess(Component component) {
                            //纠正由于地图更改（有可能对地图重新做了偏移）导致的位置不准确问题
                            //按照道理，一定是点
                            Point geometryCenter = GeometryUtil.getGeometryCenter(component.getGraphic().getGeometry());
                            modifiedIdentification.setOriginX(geometryCenter.getX());
                            modifiedIdentification.setOriginY(geometryCenter.getY());
                            loadCompleteDataAsync(component, callback2);
                        }

                        @Override
                        public void onFail(Exception error) {
                            callback2.onFail(error);
                        }
                    });
                }
            });
        }

        //   }
    }

    private Query getQueryByUSId(String usid) {
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        query.setWhere("USID = '" + usid + "'");
        return query;
    }


    /**
     * 位置layerUrl的情况下进行查询
     *
     * @param modifiedIdentification
     * @param callback2
     * @param point
     */
    private void searchWithoutLayerUrl(ModifiedFacility modifiedIdentification,
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


    /**
     * 已知usid和layerUrl情况下查询
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
                        if (featureSet.getGraphics() == null || featureSet.getGraphics().length == 0) {
                            subscriber.onError(new Exception("查无记录"));
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
     * 已知usid和layerName情况下查询,先用新图层查，新图层查不到用旧图层
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
                        subscriber.onNext(component);
                    }

                    @Override
                    public void onFail(Exception error) {
                        if (oldComponentLayerUrl != null) {
                            queryComponents(usid, oldComponentLayerUrl, callback);
                        } else {
                            subscriber.onError(error);
                        }

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

//        CompleteTableInfo completeTableInfo = new CompleteTableInfo();
//        completeTableInfo.setTableItems(tableItems);
//        completeTableInfo.setAttrs(graphic.getAttributes());
//        callback2.onSuccess(completeTableInfo);
//        return;
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
