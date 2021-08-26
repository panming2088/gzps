package com.augurit.agmobile.gzps.publicaffair.service;

import android.content.Context;

import com.augurit.agmobile.gzps.BuildConfig;
import com.augurit.agmobile.gzps.monitor.model.WellMonitorInfo;
import com.augurit.agmobile.gzps.publicaffair.PublicAffairActivity;
import com.augurit.agmobile.gzps.publicaffair.dao.IFacilityAffairApi;
import com.augurit.agmobile.gzps.publicaffair.model.FacilityAffairResponseBody;
import com.augurit.agmobile.gzps.publicaffair.model.ModifyFacilityDetail;
import com.augurit.agmobile.gzps.publicaffair.model.UploadFacilityDetail;
import com.augurit.agmobile.gzps.publicaffair.util.ModifyFacilityUtil;
import com.augurit.agmobile.gzps.publicaffair.util.UploadFacilityUtil;
import com.augurit.agmobile.gzps.setting.view.myupload.Result4;
import com.augurit.agmobile.gzps.uploadfacility.dao.UploadFacilityApi2;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.PipeBean;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by xcl on 2017/11/17.
 */

public class FacilityAffairService {


    public static final int LOAD_ITEM_PER_PAGE = 15;

    private AMNetwork amNetwork;
    private IFacilityAffairApi facilityAffair;
    private Context mContext;
    public static final int TIMEOUT = 60;  //网络超时时间（秒）

    public FacilityAffairService(Context mContext) {
        this.mContext = mContext;
    }


    private void initAMNetwork(String serverUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        }
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
        this.facilityAffair =retrofit.create(IFacilityAffairApi.class);
    }

    /**
     * 获取列表
     *
     * @param parentOrgName 区
     * @param layerName     设施类型（窨井，雨水口，排水口）
     * @param type          设施纠错/设施上报
     * @return
     */
    public Observable<FacilityAffairResponseBody> getFacilityAffairList(int page,
                                                                        String layerName,
                                                                        String parentOrgName,
                                                                        String type,
                                                                        Long startTime,
                                                                        Long endTime) {
        String url = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(url);

        if (layerName != null && layerName.equals("全部")) {
            layerName = null;
        }


        if (parentOrgName != null && parentOrgName.equals("全部")) {
            parentOrgName = null;
        }

//        if (parentOrgName != null && parentOrgName.equals("市水务局")) {
//            parentOrgName = null;
//        }

        /**
         * 如果是区级用户，那么不允许查看全市数据，只允许查看自己区的数据
         */

        /**
         *区级用户可以查看自己的数据
         */
//        if (!ifCurrentUserBelongToCityUser()) {
//            parentOrgName = getParentOrgOfCurrentUser();
//        }

        if ("市水务局".equals(parentOrgName)) {
            parentOrgName = "广州市水务局";
        }

        if (type != null && type.equals("全部")) {
            type = null;
        }

        if (type != null && type.equals(PublicAffairActivity.uploadTypeConditions[1])) {
            type = "lack";
        } else if (type != null && type.equals(PublicAffairActivity.uploadTypeConditions[2])) {
            type = "correct";
        } else if (type != null && type.equals(PublicAffairActivity.uploadTypeConditions[3])) {
            type = "delete";
        }

        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        return facilityAffair.getFacilityAffairList(page,
                LOAD_ITEM_PER_PAGE,
                layerName,
                parentOrgName,
                type,
                startTime,
                endTime,
                loginName)
                .subscribeOn(Schedulers.io());
    }


    /**
     * 根据objectId获取该设施的上报列表
     *
     * @param page
     * @param objectId
     * @return
     */
    public Observable<FacilityAffairResponseBody> searchCorrOrLackByOId(int page,
                                                                        String objectId) {
        String url = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(url);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        return facilityAffair.searchCorrOrLackByOId(page,
                LOAD_ITEM_PER_PAGE,
                loginName,
                objectId)
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
        initAMNetwork(url);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        return facilityAffair.getModifiedDetail(markId, loginName)
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
     * 获取广州市城市排水有限公司的设施纠错详情
     *
     * @param markId
     * @return
     */
    public Observable<ModifiedFacility> getModifiedSPDetail(long markId,String corrent,String source,String layerName) {
        String url = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(url);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        return facilityAffair.getModifiedSPDetail(markId, loginName,corrent,source,layerName)
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
     * 获取设施上报详情
     *
     * @param markId
     * @return
     */
    public Observable<UploadedFacility> getUploadDetail(long markId) {
        String url = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(url);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        return facilityAffair.getUploadDetail(markId, loginName)
                .map(new Func1<UploadFacilityDetail, UploadedFacility>() {
                    @Override
                    public UploadedFacility call(UploadFacilityDetail uploadFacilityDetail) {
                        if (uploadFacilityDetail.getCode() != 200) {
                            return null;
                        }
                        return UploadFacilityUtil.getUploadedFacility(uploadFacilityDetail);
                    }
                })
                .subscribeOn(Schedulers.io());
    }
    /**
     * 获取设施上报详情
     *
     * @param markId
     * @return
     */
    public Observable<WellMonitorInfo> getUploadDetail1(long markId) {
        String url = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(url);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        return facilityAffair.getWell(markId, loginName)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取广州市城市排水有限公司的设施上报详情
     *
     * @param markId
     * @return
     */
    public Observable<UploadedFacility> getUploadSPDetail(long markId,String corrent,String source,String layerName) {
        String url = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(url);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        return facilityAffair.getUploadSPDetail(markId, loginName,corrent,source,layerName)
                .map(new Func1<UploadFacilityDetail, UploadedFacility>() {
                    @Override
                    public UploadedFacility call(UploadFacilityDetail uploadFacilityDetail) {
                        if (uploadFacilityDetail.getCode() != 200) {
                            return null;
                        }
                        return UploadFacilityUtil.getUploadedFacility(uploadFacilityDetail);
                    }
                })
                .subscribeOn(Schedulers.io());
    }


    /**
     * 判断当前用户是否是市级用户
     *
     * @return
     */
    public boolean ifCurrentUserBelongToCityUser() {
        String userOrg = BaseInfoManager.getUserOrg(mContext);
        return userOrg.contains("市");
    }

    public String getParentOrgOfCurrentUser() {
        if (ifCurrentUserBelongToCityUser()) {
            return null;
        }
        return BaseInfoManager.getUserOrg(mContext);
    }

    /**
     * 获取列表
     *
     * @param page
     * @param type          1新增/2修改/3删除
     * @param checkState
     * @param pipeType      管线类型
     * @param direction     管线方向
     * @param parentOrgName 区
     * @param startTime
     * @param endTime
     * @return
     */
    public Observable<Result4<List<PipeBean>>> getLinePipe(int page,
                                                           String type,
                                                           String checkState,
                                                           String pipeType,
                                                           String direction,
                                                           String parentOrgName,
                                                           Long startTime,
                                                           Long endTime) {
        String url = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(url);

        if (pipeType != null && pipeType.equals("全部")) {
            pipeType = null;
        }
        if (direction != null && direction.equals("全部")) {
            direction = null;
        }

        if (type != null && type.equals("全部")) {
            type = null;
        }

        if (parentOrgName != null && parentOrgName.equals("全部")) {
            parentOrgName = null;
        }

        if (type != null && type.equals(PublicAffairActivity.uploadTypeConditions[1])) {
            type = "lack";
        } else if (type != null && type.equals(PublicAffairActivity.uploadTypeConditions[2])) {
            type = "correct";
        } else if (type != null && type.equals(PublicAffairActivity.uploadTypeConditions[3])) {
            type = "delete";
        }

//        if (parentOrgName != null && parentOrgName.equals("市水务局")) {
//            parentOrgName = null;
//        }

        /**
         * 如果是区级用户，那么不允许查看全市数据，只允许查看自己区的数据
         */

        /**
         *区级用户可以查看自己的数据
         */
//        if (!ifCurrentUserBelongToCityUser()) {
//            parentOrgName = getParentOrgOfCurrentUser();
//        }

        if ("市水务局".equals(parentOrgName)) {
            parentOrgName = "广州市水务局";
        }

        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        return facilityAffair.getLinePipe(page,
                LOAD_ITEM_PER_PAGE,
                type,
                startTime,
                endTime,
                loginName,
                pipeType,
                null,
                parentOrgName)
                .subscribeOn(Schedulers.io());
    }
}
