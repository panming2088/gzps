package com.augurit.agmobile.gzps.uploadfacility.service;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.gzps.BuildConfig;
import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.constant.LoginConstant;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.publicaffair.service.FacilityAffairService;
import com.augurit.agmobile.gzps.uploadfacility.dao.CorrectFacilityApi2;
import com.augurit.agmobile.gzps.uploadfacility.model.CheckResult;
import com.augurit.agmobile.gzps.uploadfacility.model.CheckUploadRecordResult;
import com.augurit.agmobile.gzps.uploadfacility.model.PsdyJbj;
import com.augurit.agmobile.gzps.uploadfacility.util.ServerAttachmentToPhotoUtil;
import com.augurit.agmobile.gzps.uploadfacility.dao.CorrectFacilityApi;
import com.augurit.agmobile.gzps.uploadfacility.model.CompleteTableInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.patrolcore.common.file.model.FileResult;
import com.augurit.agmobile.patrolcore.common.file.service.FileService;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableItems;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.agmobile.patrolcore.common.table.model.ProjectTable;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.ValidateUtils;
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
import com.augurit.am.fw.utils.StringUtil;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.query.Query;

import org.apache.commons.collections4.MapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
 * 设施纠错Service
 * <p>
 * Created by xcl on 2017/11/13.
 */

public class CorrectFacilityService {

    public static final int TIMEOUT = 60;  //网络超时时间（秒）

    private AMNetwork amNetwork;
    private CorrectFacilityApi correctFacilityApi;
    private Context mContext;
    private List<Project> projects;
    private TableDataManager tableDataManager;
    /**
     * 上报端口跟其他接口不一样
     */
    private AMNetwork amNetwork2;
    private CorrectFacilityApi2 correctFacilityApi2;

    public CorrectFacilityService(Context mContext) {
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
            this.amNetwork.registerApi(CorrectFacilityApi.class);
            this.correctFacilityApi = (CorrectFacilityApi) this.amNetwork.getServiceApi(CorrectFacilityApi.class);
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
//        this.amNetwork2.registerApi(CorrectFacilityApi2.class);
//        this.correctFacilityApi2 = (CorrectFacilityApi2) this.amNetwork2.getServiceApi(CorrectFacilityApi2.class);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        };
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
        correctFacilityApi2 = retrofit.create(CorrectFacilityApi2.class);
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
    public Observable<ResponseBody> upload(final ModifiedFacility modifiedIdentification) {

//        if (modifiedIdentification.getId() == null || modifiedIdentification.getId() == 0 ||
//                modifiedIdentification.getId() == -1) {
//            /**
//             * 如果是初次编辑需要校核是否有人上报过
//             */
//            return checkIfTheFacilityHasUploadedBefore(modifiedIdentification.getUsid())
//                    .flatMap(new Func1<Result2<User>, Observable<ResponseBody>>() {
//                        @Override
//                        public Observable<ResponseBody> call(final Result2<User> stringResult2) {
//                            if (stringResult2.getData() == null) {
//                                return uploadModification(modifiedIdentification);
//                            }
//
//                            return Observable.fromCallable(new Callable<ResponseBody>() {
//                                @Override
//                                public ResponseBody call() throws Exception {
//                                    ResponseBody responseBody = new ResponseBody();
//                                    responseBody.setCode(500);
//                                    String checkPerson = stringResult2.getData().getPhone();
//                                    String checkPersonName =  stringResult2.getData().getLoginName();
//                                    if (checkPerson.equals(BaseInfoManager.getLoginName(mContext))){
//                                        checkPersonName = "您";
//                                    }
//                                    responseBody.setMessage("该设施已经被" + checkPersonName + "校核过，请选择其他设施上报");
//                                    return responseBody;
//                                }
//                            });
//                        }
//                    });
//        }
//        /**
//         * 再次编辑不需要校核是否有人上报过
//         */
        return uploadModification(modifiedIdentification);
    }

    private Observable<ResponseBody> uploadModification(ModifiedFacility modifiedFacility) {
        //String supportUrl = BaseInfoManager.getSupportUrl(mContext);
//        String port = LoginConstant.UPLOAD_PORT[new Random().nextInt(LoginConstant.UPLOAD_PORT.length)];
        String port = ":8080";

        TableDBService dbService = new TableDBService(mContext);
        List<DictionaryItem> dictionaryItems = dbService.getDictionaryByTypecodeInDB("A176");
        if(!ListUtil.isEmpty(dictionaryItems)){
            List<String> ports = new ArrayList<>();
            for(int i=0; i<dictionaryItems.size(); i++){
                DictionaryItem dictionaryItem = dictionaryItems.get(i);
                if(!StringUtil.isEmpty(dictionaryItem.getName())
                        && ValidateUtils.isInteger(dictionaryItem.getName())){
                    ports.add(":" + dictionaryItem.getName());
                }
            }
            if(!ListUtil.isEmpty(ports)){
                port = ports.get(new Random().nextInt(ports.size()));
            }
        }
        String supportUrl = LoginConstant.UPLOAD_AGSUPPORT + port + LoginConstant.UPLOAD_POSTFIX + "/";
//        ToastUtil.longToast(mContext, supportUrl);
        initAMNetwork2(supportUrl);

        String prefix = "well_";
        int i = 0;
        List<Photo> attachments = modifiedFacility.getPhotos();
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

        List<Photo> thumbnail = modifiedFacility.getThumbnailPhotos();
        if (!ListUtil.isEmpty(thumbnail)) {
            for (Photo photo : thumbnail) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }

        }

        String prefixWell = "prefix_";
        List<Photo> attachmentsWell = modifiedFacility.getWellPhotos();
        if (!ListUtil.isEmpty(attachmentsWell)) {
            for (Photo photo : attachmentsWell) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\"" + prefixWell + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }
        }

        List<Photo> thumbnailWell = modifiedFacility.getWellThumbnailPhotos();
        if (!ListUtil.isEmpty(thumbnailWell)) {
            for (Photo photo : thumbnailWell) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\"" + prefixWell + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }
        }

        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        String loginName = user.getLoginName();
        String userName = user.getUserName();

        Observable<ResponseBody> observable;
        String json = null;
        if(!ListUtil.isEmpty(modifiedFacility.getMpBeen())) {
            json = JsonUtil.getJson(modifiedFacility.getMpBeen());
        }
        if (!MapUtils.isEmpty(requestMap)) {
            observable = correctFacilityApi2.partsAdd(
                    modifiedFacility.getId(),
                    modifiedFacility.getObjectId(),
                    loginName,
                    modifiedFacility.getMarkPersonId(),
                    userName,
//                    modifiedIdentification.getMarkTime(),
                    modifiedFacility.getDescription(),
                    modifiedFacility.getLayerName(),
                    modifiedFacility.getUsid(),
                    modifiedFacility.getAttrOne(),
                    modifiedFacility.getAttrTwo(),
                    modifiedFacility.getAttrThree(),
                    modifiedFacility.getAttrFour(),
                    modifiedFacility.getAttrFive(),
                    modifiedFacility.getOriginX(),
                    modifiedFacility.getOriginY(),
                    modifiedFacility.getOriginAddr(),
                    modifiedFacility.getAddr(),
                    modifiedFacility.getX(),
                    modifiedFacility.getY(),
                    modifiedFacility.getCorrectType(),
                    modifiedFacility.getLayerUrl(),
                    modifiedFacility.getRoad(),
                    modifiedFacility.getReportType(),
                    modifiedFacility.getUserX(),
                    modifiedFacility.getUserY(),
                    modifiedFacility.getUserAddr(),
                    modifiedFacility.getOriginRoad(),
                    modifiedFacility.getOriginAttrOne(),
                    modifiedFacility.getOriginAttrTwo(),
                    modifiedFacility.getOriginAttrThree(),
                    modifiedFacility.getOriginAttrFour(),
                    modifiedFacility.getOriginAttrFive(),
                    modifiedFacility.getDeletedPhotoIds(),
                    modifiedFacility.getpCode(),
                    modifiedFacility.getChildCode(),
                    modifiedFacility.getCityVillage(),
                    modifiedFacility.getAttrSix(),
                    modifiedFacility.getAttrSeven(),
                    modifiedFacility.getRiverx(),
                    modifiedFacility.getRivery(),
                    modifiedFacility.getSfCzwscl(),
                    json,
                    modifiedFacility.getDeletempBeenStr(),
                    modifiedFacility.getSfgjjd(),
                    modifiedFacility.getGjjdBh(),
                    modifiedFacility.getGjjdZrr(),
                    modifiedFacility.getGjjdLxdh(),
                    modifiedFacility.getYjbh(),
                    modifiedFacility.getSfpsdyhxn()
                    , requestMap);
        } else {

            observable = correctFacilityApi2.partsAdd(
                    modifiedFacility.getId(),
                    modifiedFacility.getObjectId(),
                    loginName,
                    modifiedFacility.getMarkPersonId(),
                    userName,
//                    modifiedIdentification.getMarkTime(),
                    modifiedFacility.getDescription(),
                    modifiedFacility.getLayerName(),
                    modifiedFacility.getUsid(),
                    modifiedFacility.getAttrOne(),
                    modifiedFacility.getAttrTwo(),
                    modifiedFacility.getAttrThree(),
                    modifiedFacility.getAttrFour(),
                    modifiedFacility.getAttrFive(),
                    modifiedFacility.getOriginX(),
                    modifiedFacility.getOriginY(),
                    modifiedFacility.getOriginAddr(),
                    modifiedFacility.getAddr(),
                    modifiedFacility.getX(),
                    modifiedFacility.getY(),
                    modifiedFacility.getCorrectType(),
                    modifiedFacility.getLayerUrl(),
                    modifiedFacility.getRoad(),
                    modifiedFacility.getReportType(),
                    modifiedFacility.getUserX(),
                    modifiedFacility.getUserY(),
                    modifiedFacility.getUserAddr(),
                    modifiedFacility.getOriginRoad(),
                    modifiedFacility.getOriginAttrOne(),
                    modifiedFacility.getOriginAttrTwo(),
                    modifiedFacility.getOriginAttrThree(),
                    modifiedFacility.getOriginAttrFour(),
                    modifiedFacility.getOriginAttrFive(),
                    modifiedFacility.getDeletedPhotoIds(),
                    modifiedFacility.getpCode(),
                    modifiedFacility.getChildCode(),
                    modifiedFacility.getCityVillage(),
                    modifiedFacility.getAttrSix(),
                    modifiedFacility.getAttrSeven(),
                    modifiedFacility.getRiverx(),
                    modifiedFacility.getRivery(),
                    modifiedFacility.getSfCzwscl(),
                    json,
                    modifiedFacility.getDeletempBeenStr(),
                    modifiedFacility.getSfgjjd(),
                    modifiedFacility.getGjjdBh(),
                    modifiedFacility.getGjjdZrr(),
                    modifiedFacility.getGjjdLxdh(),
                    modifiedFacility.getYjbh(),
                    modifiedFacility.getSfpsdyhxn()
            );
        }
        return observable
                .subscribeOn(Schedulers.io());
    }


    /**
     * 获取我的修正列表
     *
     * @return
     */
    public Observable<List<ModifiedFacility>> getMyModifications(int page, int pageSize, String checkState) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        return correctFacilityApi.getPartsCorr(page, pageSize, loginName, checkState)
                //获取列表（不包含附件）
                .map(new Func1<Result2<List<ModifiedFacility>>, List<ModifiedFacility>>() {
                    @Override
                    public List<ModifiedFacility> call(Result2<List<ModifiedFacility>> modifiedFacilityResult2) {
                        List<ModifiedFacility> identifications = new ArrayList<>();

                        List<ModifiedFacility> data = modifiedFacilityResult2.getData();
                        if (!ListUtil.isEmpty(data)) {
                            int i = 0;
                            for (ModifiedFacility modifiedFacility : data) {
                                modifiedFacility.setOrder(i);
                                identifications.add(modifiedFacility);
                                i++;
                            }
                        }
                        return identifications;
                    }
                })
                .flatMap(new Func1<List<ModifiedFacility>, Observable<ModifiedFacility>>() {
                    @Override
                    public Observable<ModifiedFacility> call(List<ModifiedFacility> modifiedIdentifications) {
                        return Observable.from(modifiedIdentifications);
                    }
                })
                //获取附件
                .flatMap(new Func1<ModifiedFacility, Observable<ModifiedFacility>>() {
                    @Override
                    public Observable<ModifiedFacility> call(final ModifiedFacility modifiedIdentification) {
                        return getMyModificationAttachments(modifiedIdentification.getId())
                                .map(new Func1<ServerAttachment, ModifiedFacility>() {
                                    @Override
                                    public ModifiedFacility call(ServerAttachment serverIdentificationAttachment) {
                                        List<ServerAttachment.ServerAttachmentDataBean> data = serverIdentificationAttachment.getData();
                                        if (!ListUtil.isEmpty(data)) {
                                            List<Photo> photos = new ArrayList<>();
                                            for (ServerAttachment.ServerAttachmentDataBean dataBean : data) {
                                                Photo photo = ServerAttachmentToPhotoUtil.getPhoto(dataBean);
                                                photos.add(photo);
                                            }
                                            modifiedIdentification.setPhotos(photos);
                                        }
                                        return modifiedIdentification;
                                    }
                                });
                    }
                })
                .toList()
                //进行手动排序
                .map(new Func1<List<ModifiedFacility>, List<ModifiedFacility>>() {
                    @Override
                    public List<ModifiedFacility> call(List<ModifiedFacility> modifiedIdentifications) {
                        Collections.sort(modifiedIdentifications, new Comparator<ModifiedFacility>() {
                            @Override
                            public int compare(ModifiedFacility modifiedIdentification, ModifiedFacility t1) {
                                if (modifiedIdentification.getOrder() > t1.getOrder()) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            }
                        });
                        return modifiedIdentifications;
                    }
                })
                .subscribeOn(Schedulers.io());
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

    /**
     * 检测是否有同区的人已经上报过(校核过)
     *
     * @param usid
     * @return
     */
    public Observable<Result2<CheckUploadRecordResult>> checkIfTheFacilityHasUploadedBefore(String usid) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        return correctFacilityApi.checkIfTheFacilityHasUploadedBefore(loginName, usid)
                .onErrorReturn(new Func1<Throwable, Result2<CheckUploadRecordResult>>() {
                    @Override
                    public Result2<CheckUploadRecordResult> call(Throwable throwable) {
                        return new Result2<>();
                    }
                });
    }

    /**
     * 检测是否有同区的人已经上报过(校核过)
     *
     * @param psdyJbj
     * @return
     */
    public Observable<ResponseBody> addPsdyJbj(PsdyJbj psdyJbj) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        psdyJbj.setLoginName(loginName);
        return correctFacilityApi.addPsdyJbj(JsonUtil.getJson(psdyJbj));
    }


    /**
     * 删除接驳井和排水单元的连线信息
     */
    public  Observable<ResponseBody> deletePsdyJbj(String id) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        initAMNetwork(supportUrl);
        return correctFacilityApi.deletePsdyJbj(loginName,id);
    }

    /**
     * 获取接驳井的连线信息
     */
    public  Observable<Result2<List<PsdyJbj>>> queryPsdyJbj(String usid, String jbjObjectId, String psdyObjectId) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        return correctFacilityApi.queryPsdyJbj(usid,jbjObjectId,psdyObjectId);
    }

    /**
     * 获取关键节点窨井的连线信息
     */
    public  Observable<Result2<List<PsdyJbj>>> queryPsdyKeyNodeInspectionWell(String usid, String jbjObjectId, String psdyObjectId) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        return correctFacilityApi.queryPsdyKeyNodeInspectionWell(usid,jbjObjectId,psdyObjectId, "1");
    }




    /**
     * 检测是否有同区的人已经上报过(校核过)
     *
     * @param objectId
     * @param type
     * @return
     */
    public Observable<CheckResult> getGwyxtLastRecord(String objectId,int type) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
//        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        return correctFacilityApi.getGwyxtLastRecord(objectId, type)
                .onErrorReturn(new Func1<Throwable, CheckResult>() {
                    @Override
                    public CheckResult call(Throwable throwable) {
                        return new CheckResult();
                    }
                });
    }
}
