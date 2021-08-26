package com.augurit.agmobile.gzps.setting.service;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.common.service.GzpsApi;
import com.augurit.agmobile.gzps.componentmaintenance.model.QueryFeatureSet;
import com.augurit.agmobile.gzps.componentmaintenance.service.ComponentService;
import com.augurit.agmobile.gzps.monitor.model.InspectionWellMonitorInfo;
import com.augurit.agmobile.gzps.monitor.model.WellMonitorInfo;
import com.augurit.agmobile.gzps.publicaffair.service.FacilityAffairService;
import com.augurit.agmobile.gzps.setting.dao.MyUploadFacilityApi;
import com.augurit.agmobile.gzps.setting.view.myupload.Result3;
import com.augurit.agmobile.gzps.uploadfacility.model.CompleteTableInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.HookBean;
import com.augurit.agmobile.gzps.uploadfacility.model.PipeBean;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.FacilityFilterCondition;
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
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;

import org.apache.commons.collections4.MapUtils;

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
 * 设施新增Service
 * Created by xcl on 2017/11/14.
 */

public class MyUploadFacilityService {

    public static final int LOAD_ITEM_PER_PAGE = 15;

    private AMNetwork amNetwork;
    private MyUploadFacilityApi identificationApi;
    private Context mContext;
    private List<Project> projects;
    private TableDataManager tableDataManager;
    private GzpsApi gzpsApi;

    public MyUploadFacilityService(Context mContext) {
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
            this.amNetwork.registerApi(MyUploadFacilityApi.class);
            this.amNetwork.registerApi(GzpsApi.class);
            this.identificationApi = (MyUploadFacilityApi) this.amNetwork.getServiceApi(MyUploadFacilityApi.class);
            this.gzpsApi = (GzpsApi) this.amNetwork.getServiceApi(GzpsApi.class);
        }
    }


    /**
     * 缺失部件标识
     *
     * @param uploadedFacility
     * @return
     */
    public Observable<ResponseBody> upload(final UploadedFacility uploadedFacility) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);


        return
                //获取服务端时间
//                gzpsApi.getServerTimestamp()
//                .flatMap(new Func1<Result2<Long>, Observable<ResponseBody>>() {
//                    @Override
//                    public Observable<ResponseBody> call(Result2<Long> longResult2) {
//                        uploadedFacility.setMarkTime(longResult2.getData());
//                        return getUploadObserver(uploadedFacility);
//                    }
//                })
                getUploadObserver(uploadedFacility)
                        .subscribeOn(Schedulers.io());
    }

    private Observable<ResponseBody> getUploadObserver(UploadedFacility uploadedFacility) {

        String prefix = "";
        int i = 0;
        List<Photo> attachments = uploadedFacility.getPhotos();

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


        final Observable<ResponseBody> observable;

        if (!MapUtils.isEmpty(requestMap)) {

            observable = identificationApi.partsNew(
                    uploadedFacility.getId(),
                    loginName,
                    uploadedFacility.getMarkPersonId(),
                    userName,
//                    uploadedFacility.getMarkTime(),
                    uploadedFacility.getDescription(),
                    uploadedFacility.getX(),
                    uploadedFacility.getY(),
                    uploadedFacility.getAddr(),
                    uploadedFacility.getComponentType(),
                    uploadedFacility.getAttrOne(),
                    uploadedFacility.getAttrTwo(),
                    uploadedFacility.getAttrThree(),
                    uploadedFacility.getAttrFour(),
                    uploadedFacility.getAttrFive(),
                    uploadedFacility.getRoad(),
                    uploadedFacility.getLayerId(),
                    uploadedFacility.getLayerName(),
                    uploadedFacility.getUsid(),
                    uploadedFacility.getIsBinding(),
                    uploadedFacility.getDeletedPhotoIdsStr(),
                    uploadedFacility.getUserLocationX(),
                    uploadedFacility.getUserLocationY(),
                    uploadedFacility.getUserAddr(),
                    uploadedFacility.getSfCzwscl(),
                     requestMap);
        } else {

            observable = identificationApi.partsNew(
                    uploadedFacility.getId(),
                    loginName,
                    uploadedFacility.getMarkPersonId(),
                    userName,
//                    uploadedFacility.getMarkTime(),
                    uploadedFacility.getDescription(),
                    uploadedFacility.getX(),
                    uploadedFacility.getY(),
                    uploadedFacility.getAddr(),
                    uploadedFacility.getComponentType(),
                    uploadedFacility.getAttrOne(),
                    uploadedFacility.getAttrTwo(),
                    uploadedFacility.getAttrThree(),
                    uploadedFacility.getAttrFour(),
                    uploadedFacility.getAttrFive(),
                    uploadedFacility.getRoad(),
                    uploadedFacility.getLayerId(),
                    uploadedFacility.getLayerName(),
                    uploadedFacility.getUsid(),
                    uploadedFacility.getIsBinding(),
                    uploadedFacility.getUserLocationX(),
                    uploadedFacility.getUserLocationY(),
                    uploadedFacility.getUserAddr(),
                    uploadedFacility.getSfCzwscl(),
                    uploadedFacility.getDeletedPhotoIdsStr());
        }
        return observable;
    }

    /**
     * 获取我的上报列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Observable<Result3<List<UploadedFacility>>> getMyUploads(int page, int pageSize, String checkState,
                                                                    FacilityFilterCondition uploadedFacilityFilterCondition) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        if (uploadedFacilityFilterCondition == null) {
            return identificationApi.getPartsNew(page, pageSize, loginName, checkState,
                    null, null, null, null, null,
                    null, null);
        } else {

            if (uploadedFacilityFilterCondition.facilityType != null && uploadedFacilityFilterCondition.facilityType.equals("全部")) {
                uploadedFacilityFilterCondition.facilityType = null;
            }

            /**
             * 只有当是窨井时，才有已挂牌编号
             */
            if (TextUtils.isEmpty(uploadedFacilityFilterCondition.attrFive)
                    || uploadedFacilityFilterCondition.facilityType == null
                    || !uploadedFacilityFilterCondition.facilityType.equals("窨井")) {
                uploadedFacilityFilterCondition.attrFive = null;
            }


            if (TextUtils.isEmpty(uploadedFacilityFilterCondition.road)) {
                uploadedFacilityFilterCondition.road = null;
            }

            if (TextUtils.isEmpty(uploadedFacilityFilterCondition.address)) {
                uploadedFacilityFilterCondition.address = null;
            }

            return identificationApi.getPartsNew(page,
                    pageSize,
                    loginName,
                    checkState,
                    uploadedFacilityFilterCondition.startTime,
                    uploadedFacilityFilterCondition.endTime,
                    uploadedFacilityFilterCondition.road,
                    uploadedFacilityFilterCondition.address,
                    uploadedFacilityFilterCondition.facilityType,
                    uploadedFacilityFilterCondition.markId,
                    uploadedFacilityFilterCondition.attrFive);
        }

    }

    /**
     * 根据上报设施的id获取详细信息
     *
     * @param markId 上报设施的id
     * @return
     */
    public Observable<UploadedFacility> getUploadFacilityById(long markId) {
        FacilityAffairService facilityAffairService = new FacilityAffairService(mContext);
        return facilityAffairService.getUploadDetail(markId);
    }

    /**
     * 获取附件列表
     *
     * @param markId 部件标识id
     * @return
     */
    public Observable<ServerAttachment> getMyUploadAttachments(long markId) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        return identificationApi.getPartsNewAttach(markId)
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
//        if (TextUtils.isEmpty(modifiedIdentification.getLayerUrl())) {
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
//        } else {
//
//            searchWithoutLayerUrl(modifiedIdentification, new Callback2<Component>() {
//                @Override
//                public void onSuccess(Component component) {
//                    loadCompleteDataAsync(component, callback2);
//                }
//
//                @Override
//                public void onFail(Exception error) {
//                    callback2.onFail(error);
//                }
//            }, point);
////            queryComponents(modifiedIdentification.getComponentId(), modifiedIdentification.getLayerUrl(), new Callback2<Component>() {
////                @Override
////                public void onSuccess(Component component) {
////                    loadCompleteDataAsync(component, callback2);
////                }
////
////                @Override
////                public void onFail(Exception error) {
////                    callback2.onFail(error);
////                }
////            });
//        }
    }

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

    /**
     * 获取管道列表（事物公开）我的上报列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Observable<Result3<List<PipeBean>>> getLinePipeByPerson(int page, int pageSize, String checkState, int type,
                                                                   FacilityFilterCondition uploadedFacilityFilterCondition) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        if (uploadedFacilityFilterCondition == null) {
            return identificationApi.getLinePipeByPerson(page, pageSize, checkState, type,
                    null, null, loginName, null, null);
        } else {

            if (uploadedFacilityFilterCondition.facilityType != null && uploadedFacilityFilterCondition.facilityType.equals("全部")) {
                uploadedFacilityFilterCondition.facilityType = null;
            }

            if (!TextUtils.isEmpty(uploadedFacilityFilterCondition.attrFive) && uploadedFacilityFilterCondition.attrFive.equals("全部")) {
                uploadedFacilityFilterCondition.attrFive = null;
            }

            return identificationApi.getLinePipeByPerson(page,
                    pageSize,
                    checkState,
                    type,
                    uploadedFacilityFilterCondition.startTime,
                    uploadedFacilityFilterCondition.endTime,
                    loginName, uploadedFacilityFilterCondition.facilityType,
                    uploadedFacilityFilterCondition.attrFive);
        }

    }
    /**
     * 获取管道列表（事物公开）我的上报列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Observable<Result3<List<HookBean>>> getPsdyJbjList(int page, int pageSize, String checkState, int type,
                                                              FacilityFilterCondition uploadedFacilityFilterCondition) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        if (uploadedFacilityFilterCondition == null) {
            return identificationApi.getPsdyJbjList(page, pageSize,
                    1, null, null,null,null,null, loginName);
        } else {

            if (uploadedFacilityFilterCondition.facilityType != null && uploadedFacilityFilterCondition.facilityType.equals("全部")) {
                uploadedFacilityFilterCondition.facilityType = null;
            }

            if (!TextUtils.isEmpty(uploadedFacilityFilterCondition.attrFive) && uploadedFacilityFilterCondition.attrFive.equals("全部")) {
                uploadedFacilityFilterCondition.attrFive = null;
            }
            if (TextUtils.isEmpty(uploadedFacilityFilterCondition.address) ) {
                uploadedFacilityFilterCondition.address = null;
            }
            if (TextUtils.isEmpty(uploadedFacilityFilterCondition.markId) ) {
                uploadedFacilityFilterCondition.markId = null;
            }

            return identificationApi.getPsdyJbjList(page,
                    pageSize,
                    1,
                    uploadedFacilityFilterCondition.address,
                    uploadedFacilityFilterCondition.facilityType,
                    uploadedFacilityFilterCondition.markId,
                    uploadedFacilityFilterCondition.startTime,
                    uploadedFacilityFilterCondition.endTime,
                    loginName);
        }

    }

    /**
     * 获取监测信息列表
     * @param page
     * @param pageSize
     * @return
     */
    public Observable<Result3<List<WellMonitorInfo>>> getJbjMonitorList(int page, int pageSize, String checkState, int type,
                                                                        FacilityFilterCondition uploadedFacilityFilterCondition) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        if (uploadedFacilityFilterCondition == null) {
            return identificationApi.getJbjMonitorList(page, pageSize, checkState, type,
                    null, null, loginName, null, null);
        } else {

            if (uploadedFacilityFilterCondition.facilityType != null && uploadedFacilityFilterCondition.facilityType.equals("全部")) {
                uploadedFacilityFilterCondition.facilityType = null;
            }

            if (!TextUtils.isEmpty(uploadedFacilityFilterCondition.attrFive) && uploadedFacilityFilterCondition.attrFive.equals("全部")) {
                uploadedFacilityFilterCondition.attrFive = null;
            }

            return identificationApi.getJbjMonitorList(page,
                    pageSize,
                    checkState,
                    type,
                    uploadedFacilityFilterCondition.startTime,
                    uploadedFacilityFilterCondition.endTime,
                    loginName, uploadedFacilityFilterCondition.facilityType,
                    uploadedFacilityFilterCondition.attrFive);
        }

    }

    /**
     * 获取监测信息列表
     * @param page
     * @param pageSize
     * @return
     */
    public Observable<Result3<List<WellMonitorInfo>>> getJbjJcCorrect(int page, int pageSize, String checkState, int type,
                                                                        FacilityFilterCondition uploadedFacilityFilterCondition) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        if (uploadedFacilityFilterCondition == null) {
            return identificationApi.getJbjJcCorrect(page, pageSize, 1, null,null,
                    null, null,null, loginName);
        } else {

            if (uploadedFacilityFilterCondition.facilityType != null && uploadedFacilityFilterCondition.facilityType.equals("全部")) {
                uploadedFacilityFilterCondition.facilityType = null;
            }

            if (!TextUtils.isEmpty(uploadedFacilityFilterCondition.attrFive) && uploadedFacilityFilterCondition.attrFive.equals("全部")) {
                uploadedFacilityFilterCondition.attrFive = null;
            }

            return identificationApi.getJbjJcCorrect(page,
                    pageSize,
                    1,
                    uploadedFacilityFilterCondition.address,
                    uploadedFacilityFilterCondition.facilityType,
                    uploadedFacilityFilterCondition.markId,
                    uploadedFacilityFilterCondition.startTime,
                    uploadedFacilityFilterCondition.endTime,
                    loginName);
        }

    }

    /**
     * 获取监测信息列表
     * @param page
     * @param pageSize
     * @return
     */
    public Observable<Result3<List<InspectionWellMonitorInfo>>> getKeyNodeJcCorrect(int page, int pageSize, String checkState, int type,
                                                                                    FacilityFilterCondition uploadedFacilityFilterCondition) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        String address = null;
        String facilityType = null;
        String objectid = null;
        Long startTime = null;
        Long endTime = null;

        if (uploadedFacilityFilterCondition != null) {

            if (uploadedFacilityFilterCondition.facilityType != null && uploadedFacilityFilterCondition.facilityType.equals("全部")) {
                uploadedFacilityFilterCondition.facilityType = null;
            }

            if (!TextUtils.isEmpty(uploadedFacilityFilterCondition.attrFive) && uploadedFacilityFilterCondition.attrFive.equals("全部")) {
                uploadedFacilityFilterCondition.attrFive = null;
            }

            address = uploadedFacilityFilterCondition.address;
            facilityType = uploadedFacilityFilterCondition.facilityType;
            objectid = uploadedFacilityFilterCondition.markId;
            startTime = uploadedFacilityFilterCondition.startTime;
            endTime = uploadedFacilityFilterCondition.endTime;
        }
        return identificationApi.getKeyNodeJcCorrect(page, pageSize, checkState,1, address, facilityType, objectid, startTime, endTime, loginName, "1");
    }
}
