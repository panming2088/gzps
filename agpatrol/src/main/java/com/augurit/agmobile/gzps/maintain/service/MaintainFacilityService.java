package com.augurit.agmobile.gzps.maintain.service;

import android.content.Context;

import com.augurit.agmobile.gzps.BuildConfig;
import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.constant.LoginConstant;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.common.service.GzpsApi;
import com.augurit.agmobile.gzps.componentmaintenance.model.QueryFeatureSet;
import com.augurit.agmobile.gzps.componentmaintenance.service.ComponentService;
import com.augurit.agmobile.gzps.maintain.dao.MaintainFacilityApi;
import com.augurit.agmobile.gzps.maintain.model.Conserve;
import com.augurit.agmobile.gzps.maintain.model.ConserveDetail;
import com.augurit.agmobile.gzps.maintain.model.MaintainBatchInfo;
import com.augurit.agmobile.gzps.publicaffair.service.FacilityAffairService;
import com.augurit.agmobile.gzps.setting.view.myupload.Result3;
import com.augurit.agmobile.gzps.uploadfacility.dao.UploadFacilityApi2;
import com.augurit.agmobile.gzps.uploadfacility.model.CompleteTableInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
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
import com.augurit.am.fw.utils.JsonUtil;
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
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmList;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 设施新增Service
 * Created by xcl on 2017/11/14.
 */

public class MaintainFacilityService {

    public static final int LOAD_ITEM_PER_PAGE = 15;

    public static final int TIMEOUT = 60;  //网络超时时间（秒）

    private AMNetwork amNetwork;
    private MaintainFacilityApi identificationApi;
    private Context mContext;
    private List<Project> projects;
    private TableDataManager tableDataManager;
    private GzpsApi gzpsApi;
    /**
     * 上报接口跟其他接口的端口号不一样
     */
    private AMNetwork amNetwork2;
    private UploadFacilityApi2 identificationApi2;
    private GzpsApi gzpsApi2;

    public MaintainFacilityService(Context mContext) {
        this.mContext = mContext;
    }

    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null) {
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.setReadTime(TIMEOUT * 1000);
            this.amNetwork.setWriteTime(TIMEOUT * 1000);
            this.amNetwork.setConnectTime(TIMEOUT * 1000);
            this.amNetwork.build();
            this.amNetwork.registerApi(MaintainFacilityApi.class);
            this.amNetwork.registerApi(GzpsApi.class);
            this.identificationApi = (MaintainFacilityApi) this.amNetwork.getServiceApi(MaintainFacilityApi.class);
            this.gzpsApi = (GzpsApi) this.amNetwork.getServiceApi(GzpsApi.class);
        }
    }


    private void initAMNetwork2(String serverUrl) {
//        this.amNetwork2 = new AMNetwork(serverUrl);
//        this.amNetwork2.addLogPrint();
//        this.amNetwork2.addRxjavaConverterFactory();
//        this.amNetwork2.setReadTime(TIMEOUT * 1000);
//        this.amNetwork2.setWriteTime(TIMEOUT * 1000);
//        this.amNetwork2.setConnectTime(TIMEOUT * 1000);
//        this.amNetwork2.build();
//        this.amNetwork2.registerApi(UploadFacilityApi2.class);
//        this.amNetwork2.registerApi(GzpsApi.class);
//        this.identificationApi2 = (UploadFacilityApi2) this.amNetwork2.getServiceApi(UploadFacilityApi2.class);
//        this.gzpsApi2 = (GzpsApi) this.amNetwork2.getServiceApi(GzpsApi.class);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        }
        ;
        OkHttpClient client = builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS).
                readTimeout(TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(TIMEOUT, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                //使用自定义的mGsonConverterFactory
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(serverUrl)
                .build();
        identificationApi2 = retrofit.create(UploadFacilityApi2.class);
    }


    /**
     * 缺失部件标识
     *
     * @param conserve
     * @return
     */
    public Observable<ResponseBody> upload(final Conserve conserve) {

//        String port = LoginConstant.UPLOAD_PORT[new Random().nextInt(LoginConstant.UPLOAD_PORT.length)];
        String port = ":8080";

//        TableDBService dbService = new TableDBService(mContext);
//        List<DictionaryItem> dictionaryItems = dbService.getDictionaryByTypecodeInDB("A176");
//        if (!ListUtil.isEmpty(dictionaryItems)) {
//            List<String> ports = new ArrayList<>();
//            for (int i = 0; i < dictionaryItems.size(); i++) {
//                DictionaryItem dictionaryItem = dictionaryItems.get(i);
//                if (!StringUtil.isEmpty(dictionaryItem.getName())
//                        && ValidateUtils.isInteger(dictionaryItem.getName())) {
//                    ports.add(":" + dictionaryItem.getName());
//                }
//            }
//            if (!ListUtil.isEmpty(ports)) {
//                port = ports.get(new Random().nextInt(ports.size()));
//            }
//        }

        String supportUrl = LoginConstant.UPLOAD_AGSUPPORT + port + LoginConstant.UPLOAD_POSTFIX + "/";
//        ToastUtil.longToast(mContext, supportUrl);
        initAMNetwork(supportUrl);

        return getUploadObserver(conserve)
                .subscribeOn(Schedulers.io());
    }

    private Observable<ResponseBody> getUploadObserver(Conserve conserve) {

        String prefix = "";
        int i = 0;
        List<Photo> attachments = conserve.getPhotos();

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

        List<Photo> thumbnail = conserve.getThumbnailPhotos();
        if (!ListUtil.isEmpty(thumbnail)) {
            for (Photo photo : thumbnail) {
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

        String json = JsonUtil.getJson(conserve);
        final Observable<ResponseBody> observable;

        if (!MapUtils.isEmpty(requestMap)) {
            observable = identificationApi.partsNew(
                    json, conserve.getDeletedPhotoIdsStr(), requestMap);
        } else {
            observable = identificationApi.partsNew(json, conserve.getDeletedPhotoIdsStr());
        }
        return observable;
    }

//    /**
//     * 获取我的上报列表
//     *
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    public Observable<List<UploadedFacility>> getMyUploads(int page, int pageSize, String checkState) {
//
//        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
//        initAMNetwork(supportUrl);
//
//        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
//
//        return identificationApi.getPartsNew(page, pageSize, loginName, checkState)
//                //获取列表（不包含附件）
//                .map(new Func1<Result2<List<UploadedFacility>>, List<UploadedFacility>>() {
//                    @Override
//                    public List<UploadedFacility> call(Result2<List<UploadedFacility>> listResult2) {
//                        List<UploadedFacility> identifications = new ArrayList<>();
//
//                        List<UploadedFacility> data = listResult2.getData();
//                        if (!ListUtil.isEmpty(data)) {
//                            int i = 0;
//                            for (UploadedFacility modifiedFacility : data) {
//                                modifiedFacility.setOrder(i);
//                                identifications.add(modifiedFacility);
//                                i++;
//                            }
//                        }
//                        return identifications;
//                    }
//                })
//
//                .flatMap(new Func1<List<UploadedFacility>, Observable<UploadedFacility>>() {
//                    @Override
//                    public Observable<UploadedFacility> call(List<UploadedFacility> modifiedIdentifications) {
//                        return Observable.from(modifiedIdentifications);
//                    }
//                })
//                //获取附件
//                .flatMap(new Func1<UploadedFacility, Observable<UploadedFacility>>() {
//                    @Override
//                    public Observable<UploadedFacility> call(final UploadedFacility modifiedIdentification) {
//                        return getMyUploadAttachments(modifiedIdentification.getId())
//                                .map(new Func1<ServerAttachment, UploadedFacility>() {
//                                    @Override
//                                    public UploadedFacility call(ServerAttachment serverIdentificationAttachment) {
//
//
//                                        List<ServerAttachment.ServerAttachmentDataBean> data = serverIdentificationAttachment.getData();
//                                        if (!ListUtil.isEmpty(data)) {
//                                            List<Photo> photos = new ArrayList<>();
//                                            for (ServerAttachment.ServerAttachmentDataBean dataBean : data) {
//                                                Photo photo = new Photo();
//                                                photo.setId(Long.valueOf(dataBean.getId()));
//                                                photo.setPhotoPath(dataBean.getAttPath());
//                                                photo.setThumbPath(dataBean.getThumPath());
//                                                photos.add(photo);
//                                            }
//                                            modifiedIdentification.setPhotos(photos);
//                                        }
//
//                                        return modifiedIdentification;
//                                    }
//                                });
//                    }
//                })
//                .toList()
//                //进行手动排序
//                .map(new Func1<List<UploadedFacility>, List<UploadedFacility>>() {
//                    @Override
//                    public List<UploadedFacility> call(List<UploadedFacility> modifiedIdentifications) {
//                        Collections.sort(modifiedIdentifications, new Comparator<UploadedFacility>() {
//                            @Override
//                            public int compare(UploadedFacility modifiedIdentification, UploadedFacility t1) {
//                                if (modifiedIdentification.getOrder() > t1.getOrder()) {
//                                    return 1;
//                                } else {
//                                    return -1;
//                                }
//                            }
//                        });
//                        return modifiedIdentifications;
//                    }
//                })
//                .subscribeOn(Schedulers.io());
//    }

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
     * 获取附件列表
     *
     * @param id
     * @return
     */
    public Observable<Result2<ConserveDetail>> getDetailForId(Long id) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        return identificationApi.getDetailForId(id);
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

    public Observable<MaintainBatchInfo> getBatchInfo(int page, int countPerPage) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        String loginName = user.getLoginName();
        return identificationApi.getBatchInfo(loginName, page, countPerPage);
    }

    /**
     * 获取我的养护列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Observable<Result3<List<Conserve>>> getMyUploads(int page, int pageSize, String checkState, String planId) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        return identificationApi.getMaintenanceByPlanId(page, pageSize, planId, loginName, checkState);
    }

    /**
     * 删除设施
     *
     * @param markId
     * @return
     */
    public Observable<Result2<String>> delete(Long markId) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
//        String phoneBrand = PhoneUtil.getDeviceBrand() + ":" + PhoneUtil.getSystemModel();
//        LogUtil.d("okhttp", "phoneBrand:" + phoneBrand);
        return identificationApi.delete(markId, loginName)
                .subscribeOn(Schedulers.io());
    }
}
