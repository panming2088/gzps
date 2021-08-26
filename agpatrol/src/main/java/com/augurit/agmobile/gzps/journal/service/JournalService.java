package com.augurit.agmobile.gzps.journal.service;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.gzps.BuildConfig;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.journal.dao.JournalApi2;
import com.augurit.agmobile.gzps.publicaffair.model.ModifyFacilityDetail;
import com.augurit.agmobile.gzps.publicaffair.service.FacilityAffairService;
import com.augurit.agmobile.gzps.publicaffair.util.ModifyFacilityUtil;
import com.augurit.agmobile.gzps.setting.view.myupload.Result3;
import com.augurit.agmobile.gzps.uploadfacility.dao.CorrectFacilityApi;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.FacilityFilterCondition;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.agmobile.patrolcore.common.table.util.ValidateUtils;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.PhoneUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.log.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 设施纠错Service
 * <p>
 * Created by xcl on 2017/11/13.
 */

public class JournalService {

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
    private JournalApi2 correctFacilityApi2;

    public JournalService(Context mContext) {
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
        correctFacilityApi2 = retrofit.create(JournalApi2.class);
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
        if (!ListUtil.isEmpty(dictionaryItems)) {
            List<String> ports = new ArrayList<>();
            for (int i = 0; i < dictionaryItems.size(); i++) {
                DictionaryItem dictionaryItem = dictionaryItems.get(i);
                if (!StringUtil.isEmpty(dictionaryItem.getName())
                        && ValidateUtils.isInteger(dictionaryItem.getName())) {
                    ports.add(":" + dictionaryItem.getName());
                }
            }
            if (!ListUtil.isEmpty(ports)) {
                port = ports.get(new Random().nextInt(ports.size()));
            }
        }
//        String supportUrl = LoginConstant.UPLOAD_AGSUPPORT + port + LoginConstant.UPLOAD_POSTFIX + "/";
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
//        ToastUtil.longToast(mContext, supportUrl);
        initAMNetwork2(supportUrl);

        String prefix = "";
        int i = 0;
        List<Photo> attachments = modifiedFacility.getPhotos();
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

        String videoPath = modifiedFacility.getVideoPath();
        File file = null;
        if (!TextUtils.isEmpty(videoPath)) {
            file = new File(videoPath);
            if (file.exists()) {
                requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName(), RequestBody.create(MediaType.parse("video/*"), file));
            }
        }

        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        String loginName = user.getLoginName();
        String userName = user.getUserName();

        Observable<ResponseBody> observable;

//        if (!ListUtil.isEmpty(attachments) || (file != null && file.exists())) {
        if (false) {
            observable = correctFacilityApi2.diayrNew(
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
                    modifiedFacility.getOriginAttrSix(),
                    modifiedFacility.getOriginAttrSeven(),
                    modifiedFacility.getDeletedPhotoIds(),
                    modifiedFacility.getpCode(),
                    modifiedFacility.getChildCode(),
                    modifiedFacility.getCityVillage(),
                    modifiedFacility.getTeamMember(),
                    modifiedFacility.getTqzq(),
                    modifiedFacility.getPskpszt(),
                    modifiedFacility.getGpbh(),
                    modifiedFacility.getClff(),
                    modifiedFacility.getCljg(),
                    modifiedFacility.getPh(),
                    modifiedFacility.getAdnd(),
                    modifiedFacility.getWellLength(),
                    modifiedFacility.getTrackId(),
                     requestMap);
        } else {

            observable = correctFacilityApi2.diayrNew(
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
                    modifiedFacility.getOriginAttrSix(),
                    modifiedFacility.getOriginAttrSeven(),
                    modifiedFacility.getDeletedPhotoIds(),
                    modifiedFacility.getpCode(),
                    modifiedFacility.getChildCode(),
                    modifiedFacility.getCityVillage(),
                    modifiedFacility.getTeamMember(),
                    modifiedFacility.getTqzq(),
                    modifiedFacility.getPskpszt(),
                    modifiedFacility.getGpbh(),
                    modifiedFacility.getClff(),
                    modifiedFacility.getCljg(),
                    modifiedFacility.getPh(),
                    modifiedFacility.getAdnd(),
                    modifiedFacility.getWellLength(),
                  modifiedFacility.getTrackId()
            );
        }
        return observable
                .subscribeOn(Schedulers.io());
    }

    /**
     * 修正部件标识
     *
     * @param modifiedIdentification
     * @return
     */
    public Observable<ResponseBody> editDiaryView(final ModifiedFacility modifiedIdentification) {

//        /**
//         * 再次编辑不需要校核是否有人上报过
//         */
        return editJournal(modifiedIdentification);
    }

    /**
     * 日志再次编辑
     *
     * @param modifiedFacility
     * @return
     */
    private Observable<ResponseBody> editJournal(ModifiedFacility modifiedFacility) {
        //String supportUrl = BaseInfoManager.getSupportUrl(mContext);
//        String port = LoginConstant.UPLOAD_PORT[new Random().nextInt(LoginConstant.UPLOAD_PORT.length)];
        String port = ":8080";

        TableDBService dbService = new TableDBService(mContext);
        List<DictionaryItem> dictionaryItems = dbService.getDictionaryByTypecodeInDB("A176");
        if (!ListUtil.isEmpty(dictionaryItems)) {
            List<String> ports = new ArrayList<>();
            for (int i = 0; i < dictionaryItems.size(); i++) {
                DictionaryItem dictionaryItem = dictionaryItems.get(i);
                if (!StringUtil.isEmpty(dictionaryItem.getName())
                        && ValidateUtils.isInteger(dictionaryItem.getName())) {
                    ports.add(":" + dictionaryItem.getName());
                }
            }
            if (!ListUtil.isEmpty(ports)) {
                port = ports.get(new Random().nextInt(ports.size()));
            }
        }
//        String supportUrl = LoginConstant.UPLOAD_AGSUPPORT + port + LoginConstant.UPLOAD_POSTFIX + "/";
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
//        ToastUtil.longToast(mContext, supportUrl);
        initAMNetwork2(supportUrl);

        String prefix = "";
        int i = 0;
        List<Photo> attachments = modifiedFacility.getPhotos();
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

        String videoPath = modifiedFacility.getVideoPath();
        File file = null;
        if (!TextUtils.isEmpty(videoPath)) {
            file = new File(videoPath);
            if (file.exists()) {
                requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName(), RequestBody.create(MediaType.parse("video/*"), file));
            }
        }


        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        String loginName = user.getLoginName();
        String userName = user.getUserName();

        Observable<ResponseBody> observable;

        if (!ListUtil.isEmpty(attachments)|| (file != null && file.exists())) {
            observable = correctFacilityApi2.toEditDiary(
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
                    modifiedFacility.getOriginAttrSix(),
                    modifiedFacility.getOriginAttrSeven(),
                    modifiedFacility.getDeletedPhotoIds(),
                    modifiedFacility.getpCode(),
                    modifiedFacility.getChildCode(),
                    modifiedFacility.getCityVillage(),
                    modifiedFacility.getTeamMember(),
                    modifiedFacility.getTqzq(),
                    modifiedFacility.getPskpszt(),
                    modifiedFacility.getGpbh(),
                    modifiedFacility.getClff(),
                    modifiedFacility.getCljg(),
                    modifiedFacility.getPh(),
                    modifiedFacility.getAdnd()
                    , requestMap);
        } else {

            observable = correctFacilityApi2.toEditDiary(
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
                    modifiedFacility.getOriginAttrSix(),
                    modifiedFacility.getOriginAttrSeven(),
                    modifiedFacility.getDeletedPhotoIds(),
                    modifiedFacility.getpCode(),
                    modifiedFacility.getChildCode(),
                    modifiedFacility.getCityVillage(),
                    modifiedFacility.getTeamMember()
                    , modifiedFacility.getTqzq(),
                    modifiedFacility.getPskpszt(),
                    modifiedFacility.getGpbh(),
                    modifiedFacility.getClff(),
                    modifiedFacility.getCljg(),
                    modifiedFacility.getPh(),
                    modifiedFacility.getAdnd()
            );
        }
        return observable
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
        initAMNetwork2(supportUrl);

        return correctFacilityApi2.getDiayrAttach(markId)
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
     * 获取我的修正列表
     *
     * @return
     */
    public Observable<Result3<List<ModifiedFacility>>> getMyModifications(int page, int pageSize,
                                                                          FacilityFilterCondition facilityFilterCondition) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork2(supportUrl);

        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        String userName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getUserName();
        if (facilityFilterCondition == null) {
            return correctFacilityApi2.getDiayr(page, pageSize, loginName, userName,
                    null, null, null, null, null, null, null, null);
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

            if (StringUtil.isEmpty(facilityFilterCondition.markId)) {
                facilityFilterCondition.markId = null;
            }

            return correctFacilityApi2.getDiayr(page,
                    pageSize,
                    loginName,
                    userName,
                    facilityFilterCondition.startTime,
                    facilityFilterCondition.endTime,
                    facilityFilterCondition.attrFive,
                    facilityFilterCondition.road,
                    facilityFilterCondition.address,
                    facilityFilterCondition.facilityType,
                    null,
                    facilityFilterCondition.markId);
        }
    }

    /**
     * 删除设施
     *
     * @param markId
     * @return
     */
    public Observable<Result2<String>> deleteJournal(Long markId) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork2(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        String phoneBrand = PhoneUtil.getDeviceBrand() + ":" + PhoneUtil.getSystemModel();
        LogUtil.d("okhttp", "phoneBrand:" + phoneBrand);
        return correctFacilityApi2.deleteDiary(markId, loginName, phoneBrand)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取设施纠错详情
     *
     * @param markId
     * @return
     */
    public Observable<ModifiedFacility> getModifiedDetail(long markId) {
        String url = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork2(url);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        return correctFacilityApi2.toDiaryView(markId)
                .map(new Func1<ModifyFacilityDetail, ModifiedFacility>() {
                    @Override
                    public ModifiedFacility call(ModifyFacilityDetail modifyFacilityDetail) {
                        if (modifyFacilityDetail.getCode() != 200) {
                            return null;
                        }
                        return ModifyFacilityUtil.getModifiedFacility(modifyFacilityDetail);
                    }
                })
                .subscribeOn(Schedulers.io());
    }


    /**
     *
     * @param page
     * @param pageSize
     * @param objectId
     * @param sfss
     * @return
     */
//    public Observable<Result3<List<ModifiedFacility>>> getDiayList(int page, int pageSize,String objectId ,String sfss) {
//        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
//        initAMNetwork2(supportUrl);
//        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
//        String userName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getUserName();
//
//        return correctFacilityApi2.getDiayList(page,
//                pageSize,
//                loginName,
//                objectId,
//                sfss
//        );
//    }


    /**
     * @param page
     * @param pageSize
     * @param objectId
     * @param usid
     * @param sfss
     * @return
     */
    public Observable<Result3<List<ModifiedFacility>>> getDiayList1(int page, int pageSize, String objectId, String usid, String sfss) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork2(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        return correctFacilityApi2.getDiayList1(page,
                pageSize,
                loginName,
                objectId,
                usid,
                sfss
        );
    }


    public Observable<Result3<List<ModifiedFacility>>> getDiayList(int page, int pageSize, String facilityType, String district, String startTime, String endTime, String sfss) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork2(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        return correctFacilityApi2.getDiayList(page,
                pageSize,
                loginName,
                facilityType,
                district,
                startTime,
                endTime,
                sfss
        );
    }
}
