package com.augurit.agmobile.gzps.monitor.service;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.monitor.dao.InspectionWellMonitorApi;
import com.augurit.agmobile.gzps.monitor.model.InspectionWellMonitorInfo;
import com.augurit.agmobile.gzps.setting.view.myupload.Result3;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * 窨井监测Service
 * <p>
 * Created by xcl on 2017/11/13.
 */

public class InspectionWellMonitorService {

    public static final int TIMEOUT = 60;  //网络超时时间（秒）

    private AMNetwork amNetwork;
    private InspectionWellMonitorApi inspectionWellMonitorApi;
    private Context mContext;
    /**
     * 上报端口跟其他接口不一样
     */
    private AMNetwork amNetwork2;

    public InspectionWellMonitorService(Context mContext) {
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
            this.amNetwork.registerApi(InspectionWellMonitorApi.class);
            this.inspectionWellMonitorApi = (InspectionWellMonitorApi) this.amNetwork.getServiceApi(InspectionWellMonitorApi.class);
        }
    }

    /**
     * 获取我的修正列表
     *
     * @return
     */
    public Observable<Result3<List<InspectionWellMonitorInfo>>> getInspectionWellMonitors(int page, int pageSize, String usid, String jbjObjectId) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        boolean isUsidNUll = TextUtils.isEmpty(usid);
        boolean isObjectidNUll = TextUtils.isEmpty(jbjObjectId);

        if (isUsidNUll) {
            return inspectionWellMonitorApi.getWellMonitors3(page, pageSize, loginName, jbjObjectId, "1");
        } else if (isObjectidNUll) {
            return inspectionWellMonitorApi.getWellMonitors2(page, pageSize, loginName, usid, "1");
        } else {
            return inspectionWellMonitorApi.getWellMonitors1(page, pageSize, loginName, usid, jbjObjectId,"1");
        }
    }


    /**
     * 获取附件列表
     *
     * @param id 部件标识id
     * @return
     */
    public Observable<ResponseBody> deleteInspectionWellMonitor(long id) {

        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        return inspectionWellMonitorApi.deleteJbjJc(id, loginName)
                .subscribeOn(Schedulers.io());
    }


    /**
     * 新增监测信息
     */
    public Observable<ResponseBody> addInspectionWellMonitor(InspectionWellMonitorInfo inspectionWellMonitorInfo) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        inspectionWellMonitorInfo.setLoginName(loginName);
        if (ListUtil.isEmpty(inspectionWellMonitorInfo.getPhotos()) || ListUtil.isEmpty(inspectionWellMonitorInfo.getThumbnailPhotos())) {
            return inspectionWellMonitorApi.addJbjJc(JsonUtil.getJson(inspectionWellMonitorInfo));
        }

        String prefix = "_jc_";
        int i = 0;
        List<Photo> attachments = inspectionWellMonitorInfo.getPhotos();
        HashMap<String, RequestBody> requestMap = new HashMap<>();
        if (!ListUtil.isEmpty(attachments)) {
            for (Photo photo : attachments) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\"" + file.getName().replaceFirst("_", prefix), RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }

        }

        String prefix1 = "_jcthumbnail_";
        List<Photo> thumbnail = inspectionWellMonitorInfo.getThumbnailPhotos();
        if (!ListUtil.isEmpty(thumbnail)) {
            for (Photo photo : thumbnail) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\"" + file.getName().replaceFirst("_", prefix1), RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }
        }
        String jsonData = JsonUtil.getJson(inspectionWellMonitorInfo);
        return inspectionWellMonitorApi.addJbjJc(jsonData, requestMap);
    }

    public Observable<Result2<InspectionWellMonitorInfo>> getInspectionWellMonitorById(Long id) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        return inspectionWellMonitorApi.getWellMonitorById(id, "1");
    }
}