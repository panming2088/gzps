package com.augurit.agmobile.gzps.uploadfacility.service;

import android.content.Context;

import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.uploadfacility.dao.FeedbackFacilityApi;
import com.augurit.agmobile.gzps.uploadfacility.model.FeedbackInfo;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.ListUtil;

import org.apache.commons.collections4.MapUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by lsh on 2018/3/6.
 */

public class FeedbackFacilityService {

    public static final int TIMEOUT = 60;  //网络超时时间（秒）

    private Context mContext;

    private AMNetwork amNetwork;
    private FeedbackFacilityApi mFeedbackFacilityApi;

    public FeedbackFacilityService(Context context){
        this.mContext = context;
    }

    public Observable<ResponseBody> save(FeedbackInfo feedbackInfo){
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        String loginName = user.getLoginName();
        String userName = user.getUserName();
        String prefix = "";
        int i = 0;
        List<Photo> attachments = feedbackInfo.getFiles();
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

        List<Photo> thumbnail = feedbackInfo.getThumbPhoto();
        if (!ListUtil.isEmpty(thumbnail)) {
            for (Photo photo : thumbnail) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }
        }

        if(MapUtils.isEmpty(requestMap)){
            return mFeedbackFacilityApi.save(feedbackInfo.getAid(), feedbackInfo.getTableType(),
                    feedbackInfo.getDescribe(), feedbackInfo.getSituation(),
                    userName, loginName,
                    feedbackInfo.getObjectId());
        } else {
            return mFeedbackFacilityApi.save(feedbackInfo.getAid(), feedbackInfo.getTableType(),
                    feedbackInfo.getDescribe(), feedbackInfo.getSituation(),
                    userName, loginName,
                    feedbackInfo.getObjectId(),
                    requestMap);
        }
    }

    public Observable<ResponseBody> delete(String id){
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        return mFeedbackFacilityApi.delete(id);
    }

    /**
     *
     * @param feedbackInfo
     * @param deletePicIds 要删除的图片的ID，多张图片以,号隔开
     * @return
     */
    public Observable<ResponseBody> update(FeedbackInfo feedbackInfo, String deletePicIds){
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        String loginName = user.getLoginName();
        String userName = user.getUserName();
        String prefix = "";
        int i = 0;
        List<Photo> attachments = feedbackInfo.getFiles();
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

        List<Photo> thumbnail = feedbackInfo.getThumbPhoto();
        if (!ListUtil.isEmpty(thumbnail)) {
            for (Photo photo : thumbnail) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }
        }

        if(MapUtils.isEmpty(requestMap)){
            return mFeedbackFacilityApi.update(feedbackInfo.getId() + "", feedbackInfo.getAid(), feedbackInfo.getTableType(),
                    feedbackInfo.getDescribe(), feedbackInfo.getSituation(),
                    userName, loginName,
                    feedbackInfo.getObjectId(),
                    deletePicIds);
        } else {
            return mFeedbackFacilityApi.update(feedbackInfo.getId() + "", feedbackInfo.getAid(), feedbackInfo.getTableType(),
                    feedbackInfo.getDescribe(), feedbackInfo.getSituation(),
                    userName, loginName,
                    feedbackInfo.getObjectId(),
                    deletePicIds,
                    requestMap);
        }
    }

    public Observable<Result2<List<FeedbackInfo>>> getFeedbackInfos(long aid, String tableType){
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        return mFeedbackFacilityApi.getFeedbackInfos(aid, tableType);
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
            this.amNetwork.registerApi(FeedbackFacilityApi.class);
            this.mFeedbackFacilityApi = (FeedbackFacilityApi) this.amNetwork.getServiceApi(FeedbackFacilityApi.class);
        }
    }
}
