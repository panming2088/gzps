package com.augurit.agmobile.gzps.monitor.service;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.gzps.BuildConfig;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.journal.model.TeamMember;
import com.augurit.agmobile.gzps.monitor.dao.WellMonitorApi;
import com.augurit.agmobile.gzps.monitor.model.WellMonitorInfo;
import com.augurit.agmobile.gzps.publicaffair.service.FacilityAffairService;
import com.augurit.agmobile.gzps.setting.view.myupload.Result3;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.google.common.reflect.TypeToken;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Completable;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 设施纠错Service
 * <p>
 * Created by xcl on 2017/11/13.
 */

public class WellMonitorService {

    public static final int TIMEOUT = 60;  //网络超时时间（秒）

    private AMNetwork amNetwork;
    private WellMonitorApi wellMonitorApi;
    private Context mContext;
    /**
     * 上报端口跟其他接口不一样
     */
    private AMNetwork amNetwork2;

    public WellMonitorService(Context mContext) {
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
            this.amNetwork.registerApi(WellMonitorApi.class);
            this.wellMonitorApi = (WellMonitorApi) this.amNetwork.getServiceApi(WellMonitorApi.class);
        }
    }
    /**
     * 获取我的修正列表
     *
     * @return
     */
    public Observable<Result3<List<WellMonitorInfo>>> getWellMonitors(int page, int pageSize, String usid, String jbjObjectId) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        boolean isUsidNUll = TextUtils.isEmpty(usid);
        boolean isObjectidNUll = TextUtils.isEmpty(jbjObjectId);
        if(isUsidNUll){
            return wellMonitorApi.getWellMonitors3(page, pageSize, loginName,jbjObjectId);
        }else if(isObjectidNUll){
            return wellMonitorApi.getWellMonitors2(page, pageSize, loginName,usid);
        }else {
            return wellMonitorApi.getWellMonitors1(page, pageSize, loginName,usid,jbjObjectId);
        }
    }


    /**
     * 获取附件列表
     *
     * @param markId 部件标识id
     * @return
     */
    public Observable<ResponseBody> deleteWellMonitor(long id) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        return wellMonitorApi.deleteJbjJc(id,loginName)
                .subscribeOn(Schedulers.io());
    }





    /**
     * 新增监测信息
     */
    public Observable<ResponseBody> addWellMonitor(WellMonitorInfo wellMonitorInfo) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        wellMonitorInfo.setLoginName(loginName);
        if(ListUtil.isEmpty(wellMonitorInfo.getPhotos()) || ListUtil.isEmpty(wellMonitorInfo.getThumbnailPhotos())){
            return wellMonitorApi.addJbjJc(JsonUtil.getJson(wellMonitorInfo));
        }

        String prefix = "_jc_";
        int i = 0;
        List<Photo> attachments = wellMonitorInfo.getPhotos();
        HashMap<String, RequestBody> requestMap = new HashMap<>();
        if (!ListUtil.isEmpty(attachments)) {
            for (Photo photo : attachments) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\""  + file.getName().replaceFirst("_",prefix), RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }

        }

        String prefix1 = "_jcthumbnail_";
        List<Photo> thumbnail = wellMonitorInfo.getThumbnailPhotos();
        if (!ListUtil.isEmpty(thumbnail)) {
            for (Photo photo : thumbnail) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\""  + file.getName().replaceFirst("_",prefix1), RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }
        }
        return wellMonitorApi.addJbjJc(JsonUtil.getJson(wellMonitorInfo),requestMap);
    }

   public Observable<Result2<WellMonitorInfo>> getWellMonitorById(Long id) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        return wellMonitorApi.getWellMonitorById(id);
    }
}
