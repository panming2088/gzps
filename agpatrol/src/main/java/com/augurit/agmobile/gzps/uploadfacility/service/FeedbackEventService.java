package com.augurit.agmobile.gzps.uploadfacility.service;

import android.content.Context;

import com.augurit.agmobile.gzps.common.model.Result;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.componentmaintenance.service.ComponentService;
import com.augurit.agmobile.gzps.publicaffair.model.EventAffair;
import com.augurit.agmobile.gzps.uploadevent.dao.GetPersonByOrgApiData;
import com.augurit.agmobile.gzps.uploadevent.dao.UploadEventApi;
import com.augurit.agmobile.gzps.uploadevent.model.Assigneers;
import com.augurit.agmobile.gzps.uploadevent.model.EventCount;
import com.augurit.agmobile.gzps.uploadevent.model.EventDetail;
import com.augurit.agmobile.gzps.uploadevent.model.EventListItem;
import com.augurit.agmobile.gzps.uploadevent.model.EventProcess;
import com.augurit.agmobile.gzps.uploadevent.model.NextLinkOrg;
import com.augurit.agmobile.gzps.uploadevent.model.OrgItem;
import com.augurit.agmobile.gzps.uploadevent.util.TaskParamUtil;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.ListUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by xcl on 2017/11/11.
 */

public class FeedbackEventService {

    private AMNetwork amNetwork;
    private UploadEventApi mEventApi;
    private Context mContext;

    public FeedbackEventService(Context mContext) {
        this.mContext = mContext;
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
//        String supportUrl = "http://192.168.43.139:8080/agsupport_swj/";
    //      supportUrl = "http://192.168.43.130:8080/agsupport_swj/";
        initAMNetwork(supportUrl);
    }

    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null) {
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.setReadTime(20000);
            this.amNetwork.setWriteTime(20000);
            this.amNetwork.setConnectTime(20000);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.build();
            this.amNetwork.registerApi(UploadEventApi.class);
            this.mEventApi = (UploadEventApi) this.amNetwork.getServiceApi(UploadEventApi.class);
        }
    }

    public Observable<EventAffair> search(int pageNo, int pageSize, String district, String componentTypeCode, String eventTypeCode, String state){
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mEventApi.search(user.getLoginName(), pageNo, pageSize, null, district, componentTypeCode, eventTypeCode, state, null, null,null)
                .map(new Func1<Result2<EventAffair>, EventAffair>() {
                    @Override
                    public EventAffair call(Result2<EventAffair> responseBody) {
                        try {
                            if(responseBody.getCode() != 200){
                                return null;
                            }
                            return responseBody.getData();
                        } catch (Exception e) {
                            return null;
                        }
                    }
                });
    }

    public Observable<EventAffair> search(int pageNo, int pageSize, String keyword){
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mEventApi.search(user.getLoginName(), pageNo, pageSize, keyword, null, null, null, null, null, null,null)
                .map(new Func1<Result2<EventAffair>, EventAffair>() {
                    @Override
                    public EventAffair call(Result2<EventAffair> responseBody) {
                        try {
                            if(responseBody.getCode() != 200){
                                return null;
                            }
                            return responseBody.getData();
                        } catch (Exception e) {
                            return null;
                        }
                    }
                });
    }


    /**
     * 根据事件ID获取事件上报详情
     */
    public Observable<EventAffair.EventAffairBean> getEventDetail(String sjid){
        return mEventApi.getEventDetail(sjid)
                .map(new Func1<Result2<EventAffair.EventAffairBean>, EventAffair.EventAffairBean>() {
            @Override
            public EventAffair.EventAffairBean call(Result2<EventAffair.EventAffairBean> eventAffairBeanResult2) {
                return eventAffairBeanResult2.getData();
            }
        });
    }

    /**
     * 事件上报
     *
     * @return
     */
    public Observable<Boolean> uploadEvent(String isSendMessage,String json, String assignee, String assigneeName, final HashMap<String, RequestBody> requestBody) {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mEventApi.uploadEvent(isSendMessage,user.getLoginName(), json, "isMobile", assignee, assigneeName, requestBody)
                .map(new Func1<Result<String>, Boolean>() {
                    @Override
                    public Boolean call(Result<String> responseBody) {
                        try {
                            return responseBody.isSuccess();
                        } catch (Exception e) {
                            return false;
                        }
                    }
                });
    }


    /**
     * 事件问题编辑
     * @param reportId
     * @param deleteImgs
     * @param json
     * @param assignee
     * @param assigneeName
     * @param requestBody
     * @return
     */
    public Observable<Boolean>  editEvent(long reportId,String deleteImgs,
                                          String json, String assignee, String assigneeName,String isSendMessage,
                                          final HashMap<String, RequestBody> requestBody){
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();

        /*
        String url = "http://192.168.253.5:8080/agsupport_swj/";

        AMNetwork test = new AMNetwork(url);
        test.setReadTime(30000);
        test.addLogPrint();
        test.addRxjavaConverterFactory();
        test.build();
        test.registerApi(UploadEventApi.class);
        UploadEventApi testApi = (UploadEventApi) test.getServiceApi(UploadEventApi.class);
        */



        return mEventApi.editEvent(reportId,deleteImgs,user.getLoginName(), json,"isMobile",assignee, assigneeName,isSendMessage, requestBody)
                .map(new Func1<Result<String>, Boolean>() {
                    @Override
                    public Boolean call(Result<String> responseBody) {
                        try{
                            return responseBody.isSuccess();
                        }catch (Exception e){
                            return false;
                        }

                    }
                });
    }


    /**
     * 事件问题编辑
     * @param reportId
     * @param deleteImgs
     * @param json
     * @param assignee
     * @param assigneeName
     * @return
     */
    public Observable<Boolean>  editEvent(long reportId,String deleteImgs,
                                          String json, String assignee, String assigneeName,String isSendMessage){
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mEventApi.editEvent(reportId,deleteImgs,user.getLoginName(), json,"isMobile",assignee, assigneeName,isSendMessage)
                .map(new Func1<Result<String>, Boolean>() {
                    @Override
                    public Boolean call(Result<String> responseBody) {
                        try{
                            return responseBody.isSuccess();
                        }catch (Exception e){
                            return false;
                        }

                    }
                });
    }

    /**
     * 问题上报时获取下一环节处理人
     * @return
     */
    public Observable<List<Assigneers>> getTaskUserByloginName(String objectId){
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mEventApi.getTaskUserByloginName(user.getLoginName(),objectId);
    }

    /**
     * 问题上报时获取下一环节处理人
     * @return
     */
    public Observable<Result<List<Assigneers>>> getNextActivityInfo(String taskInstDbid){
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mEventApi.getNextActivityInfo(user.getLoginName(), taskInstDbid);
    }

    /**
     * 获取待领取列表
     *
     * @return
     */
    public Observable<List<EventListItem>> getDlcEventList(int pageNo, int pageSize) {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        String jsonParam = TaskParamUtil.getEventJsonParam("GX_XCYH");
        String groupBy = "sbsj";
        String groupDir = "desc";
        String pageParam = TaskParamUtil.getEventPageParam("" + pageNo, "" + pageSize);

        return mEventApi.getDlcEventList(user.getLoginName(), groupBy, groupDir, jsonParam, pageParam, 82, "isMobile")
                .map(new Func1<Result<List<EventListItem>>, List<EventListItem>>() {
                    @Override
                    public List<EventListItem> call(Result<List<EventListItem>> eventListResult) {
                        try {
                            List<EventListItem> eventListItems = new ArrayList<>();
                            if (!eventListResult.isSuccess()) {
                                return eventListItems;
                            }
                            return eventListResult.getResult();
                        } catch (Exception e) {
                            return null;
                        }
                    }
                });
    }

    /**
     * 获取处理中列表
     *
     * @return
     */
    public Observable<List<EventListItem>> getClzEventList(int pageNo, int pageSize) {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        String jsonParam = TaskParamUtil.getEventJsonParam("GX_XCYH");
        String pageParam = TaskParamUtil.getEventPageParam("" + pageNo, "" + pageSize);

        return mEventApi.getClzEventList(user.getLoginName(), jsonParam, pageParam, 82, "isMobile")
                .map(new Func1<Result<List<EventListItem>>, List<EventListItem>>() {
                    @Override
                    public List<EventListItem> call(Result<List<EventListItem>> eventListResult) {
                        try {
                            List<EventListItem> eventListItems = new ArrayList<>();
                            if (!eventListResult.isSuccess()) {
                                return eventListItems;
                            }
                            return eventListResult.getResult();
                        } catch (Exception e) {
                            return null;
                        }
                    }
                });
    }

    /**
     * 获取待在办列表
     *
     * @return
     */
    public Observable<List<EventListItem>> getDZbEventList(int pageNo, int pageSize) {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        String jsonParam = TaskParamUtil.getEventJsonParam("GX_XCYH");
        String pageParam = TaskParamUtil.getEventPageParam("" + pageNo, "" + pageSize);

        return mEventApi.getDZbSummary(user.getLoginName(), jsonParam, pageParam, 82, "isMobile")
                .map(new Func1<Result<List<EventListItem>>, List<EventListItem>>() {
                    @Override
                    public List<EventListItem> call(Result<List<EventListItem>> eventListResult) {
                        try {
                            List<EventListItem> eventListItems = new ArrayList<>();
                            if (!eventListResult.isSuccess()) {
                                return eventListItems;
                            }
                            return eventListResult.getResult();
                        } catch (Exception e) {
                            return null;
                        }
                    }
                });
    }

    /**
     * 获取已办列表
     *
     * @return
     */
    public Observable<List<EventListItem>> getYbEventList(int pageNo, int pageSize) {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        String jsonParam = TaskParamUtil.getEventJsonParam("GX_XCYH");
        String pageParam = TaskParamUtil.getEventPageParam("" + pageNo, "" + pageSize);

        return mEventApi.getYbSummary(user.getLoginName(), jsonParam, pageParam, 82, "isMobile")
                .map(new Func1<Result<List<EventListItem>>, List<EventListItem>>() {
                    @Override
                    public List<EventListItem> call(Result<List<EventListItem>> eventListResult) {
                        try {
                            List<EventListItem> eventListItems = new ArrayList<>();
                            if (!eventListResult.isSuccess()) {
                                return eventListItems;
                            }
                            return eventListResult.getResult();
                        } catch (Exception e) {
                            return null;
                        }
                    }
                });
    }

    /**
     * 获取待办结列表
     *
     * @return
     */
    public Observable<List<EventListItem>> getDbjEventList(int pageNo, int pageSize) {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        String jsonParam = TaskParamUtil.getEventJsonParam("GX_XCYH");
        String pageParam = TaskParamUtil.getEventPageParam("" + pageNo, "" + pageSize);

        return mEventApi.getDbjEventList(user.getLoginName(), jsonParam, pageParam, 82, "isMobile")
                .map(new Func1<Result<List<EventListItem>>, List<EventListItem>>() {
                    @Override
                    public List<EventListItem> call(Result<List<EventListItem>> eventListResult) {
                        try {
                            List<EventListItem> eventListItems = new ArrayList<>();
                            if (!eventListResult.isSuccess()) {
                                return eventListItems;
                            }
                            return eventListResult.getResult();
                        } catch (Exception e) {
                            return null;
                        }
                    }
                });
    }

    /**
     * 获取已办结列表
     *
     * @return
     */
    public Observable<List<EventListItem>> getYbjEventList(int pageNo, int pageSize) {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        String jsonParam = TaskParamUtil.getEventJsonParam("GX_XCYH");
        String pageParam = TaskParamUtil.getEventPageParam("" + pageNo, "" + pageSize);

        return mEventApi.getYbjEventList(user.getLoginName(), jsonParam, pageParam, 82, "isMobile")
                .map(new Func1<Result<List<EventListItem>>, List<EventListItem>>() {
                    @Override
                    public List<EventListItem> call(Result<List<EventListItem>> eventListResult) {
                        try {
                            List<EventListItem> eventListItems = new ArrayList<>();
                            if (!eventListResult.isSuccess()) {
                                return eventListItems;
                            }
                            return eventListResult.getResult();
                        } catch (Exception e) {
                            return null;
                        }
                    }
                });
    }

    /**
     * 获取上报事件详情
     *
     * @return
     */
    public Observable<Result2<EventDetail>> getDetails(String taskInstDbid, String procInstDbId, String masterEntityKey) {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mEventApi.getEventDetails(user.getLoginName(), taskInstDbid, procInstDbId, masterEntityKey);

    }

    /**
     * 领取事件
     *
     * @return
     */
    public Observable<Boolean> signEvent(String taskInstId) {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mEventApi.signEvent(user.getLoginName(),
                user.getUserName(),
                taskInstId)
                .map(new Func1<Result<String>, Boolean>() {
                    @Override
                    public Boolean call(Result<String> responseBody) {
                        return responseBody.isSuccess();
                    }
                });
    }

    /**
     * 获取事件处理情况及施工日志
     *
     * @return
     */
    public Observable<List<EventProcess>> getEventHandlesAndJournals(String sjid, int pageNo, int pageSize) {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mEventApi.getEventHandlesAndJournals(sjid, pageNo, pageSize)
                .map(new Func1<Result2<List<EventProcess>>, List<EventProcess>>() {
                    @Override
                    public List<EventProcess> call(Result2<List<EventProcess>> responseBody) {
//                        String json = JsonUtil.getJson(responseBody);
                        return responseBody.getData();
                    }
                });
    }


    public Observable<List<NextLinkOrg>> getNextLinkOrg() {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mEventApi.getEventOrg(user.getLoginName());
    }

    /**
     * 推送到一下环节
     *
     * @return
     */
    public Observable<Boolean> wfSend(String destActivityChineseName,
                                     String destActivityName,
                                     String bzCode,
                                     String assignee,
                                     String assigneeName,
                                     String taskInstId,
                                     String advice,

                                      String procInstId,  //流程ID
                                      String sjid,      //事件ID
                                      String isSendMessage,  //是否发送短信通知

                                     List<Photo> photos) {
        String prefix = "";
        int i = 0;
        HashMap<String, RequestBody> requestMap = new HashMap<>();
        if (!ListUtil.isEmpty(photos)) {
            for (Photo photo : photos) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }

        }
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mEventApi.wfSend(user.getLoginName(),
                destActivityChineseName, destActivityName,
                bzCode, assignee, assigneeName,
                taskInstId, advice,procInstId,sjid,isSendMessage)
                .map(new Func1<Result<String>, Boolean>() {
                    @Override
                    public Boolean call(Result<String> responseBody) {
                        if(responseBody == null){
                            return false;
                        }
                        return responseBody.isSuccess();
                    }
                });
    }

    public Observable<Boolean> saveJdFile(String taskInstDbid, List<Photo> photos){
        String prefix = "";
        int i = 0;
        final HashMap<String, HashMap<String, RequestBody>> map = new HashMap<>();    //
        HashMap<String, RequestBody> requestMap = new HashMap<>();
        if (!ListUtil.isEmpty(photos)) {
            for (Photo photo : photos) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }

        }
        return mEventApi.saveJdFile(taskInstDbid, requestMap)
                .map(new Func1<ResponseBody, Boolean>() {
                    @Override
                    public Boolean call(ResponseBody responseBody) {
                        try {
                            if ("true".equals(responseBody.string())) {
                                return true;
                            } else {
                                return false;
                            }
                        } catch (Exception e) {
                            return false;
                        }
                    }
                });
    }

    /**
     * 回退到上一环节
     *
     * @return
     */
    public Observable<String> wfReturn(String isSendMessage,String masterEntityKey,String taskInstId, String content) {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mEventApi.wfReturn(masterEntityKey,isSendMessage,user.getLoginName(), taskInstId, content)
                .map(new Func1<ResponseBody, String>() {
                    @Override
                    public String call(ResponseBody responseBody) {
                        return null;
                    }
                });
    }


    /**
     * 撤回
     * @param procInstId
     * @param
     * @return
     */
    public Observable<Result<String>> wfRetrive(String procInstId,String retrieveComments){
        User user = new LoginRouter(mContext,AMDatabase.getInstance()).getUser();
        return mEventApi.wfRetrive(procInstId,user.getLoginName(),retrieveComments);
    }

    /**
     * 删除
     * @param sjid
     * @param procInstId
     * @param taskInstDbid
     * @return
     */
    public Observable<Result<String>> wfDelete(String sjid,String procInstId,String taskInstDbid){
        User user = new LoginRouter(mContext,AMDatabase.getInstance()).getUser();
        return mEventApi.wfDelete(sjid,procInstId,user.getLoginName(),taskInstDbid);
    }


    /**
     * 转派
     *
     * @return
     */
    public Observable<String> wfReassign(String taskInstId, String assignee, String assigneeName,
                                         String activityChineseName, String content,String activityName,
                                         String procInstId,String sjid,String isSendMessage) {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mEventApi.wfReassign(user.getLoginName(), taskInstId, assignee, assigneeName,
                activityChineseName, content,activityName,procInstId,sjid,isSendMessage)
                .map(new Func1<ResponseBody, String>() {
                    @Override
                    public String call(ResponseBody responseBody) {
                        return null;
                    }
                });
    }

    /**
     * 上传施工日志
     *
     * @return
     */
    public Observable<Boolean> uploadJournal(String content, List<Photo> photos, String sjid) {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        String prefix = "";
        int i = 0;
        final HashMap<String, HashMap<String, RequestBody>> map = new HashMap<>();    //
        HashMap<String, RequestBody> requestMap = new HashMap<>();
        if (!ListUtil.isEmpty(photos)) {
            for (Photo photo : photos) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }

        }
        return mEventApi.uploadJournal(user.getLoginName(),
                user.getUserName(),
                content,
                sjid,
                requestMap)
                .map(new Func1<Result2<String>, Boolean>() {
                    @Override
                    public Boolean call(Result2<String> responseBody) {
                        if(responseBody.getCode() == 200){
                            return true;
                        }
                        return false;
                    }
                });
    }

    /**
     * 上传督办意见
     *
     * @return
     */
    public Observable<Boolean> uploadAdvice(String advice, String sjid) {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mEventApi.uploadAdvice(user.getLoginName(), user.getUserName(),
                advice,
                sjid)
                .map(new Func1<Result2<String>, Boolean>() {
                    @Override
                    public Boolean call(Result2<String> responseBody) {
                        if(responseBody.getCode() == 200){
                            return true;
                        } else {
                            return false;
                        }

                    }
                });
    }

    /**
     * 获取待在办列表
     *
     * @return
     */
    public Observable<EventCount> getEventCount() {
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        String jsonParam = TaskParamUtil.getEventJsonParam("GX_XCYH");

        return mEventApi.getEventCount(user.getLoginName(), jsonParam, 82)
                .map(new Func1<Result2<EventCount>, EventCount>() {
                    @Override
                    public EventCount call(Result2<EventCount> eventCountResult) {
                        try {
                            if (eventCountResult.getCode() == 200) {
                                return eventCountResult.getData();
                            } else {
                                return null;
                            }
                        } catch (Exception e) {
                            return null;
                        }
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
        new ComponentService(mContext).queryComponents(usid, url, callback);
    }


    /**
     * 已知usid和layerName情况下查询
     *
     * @param usid
     * @param layerName
     * @param callback
     */
    public void queryComponentsWithLayerName(final String usid, final String layerName, final Callback2<Component> callback) {
        new ComponentService(mContext).queryComponentsWithLayerName(usid, layerName, callback);
    }

    /**
     * 获取待在办列表
     *
     * @return
     */
    public Observable<List<OrgItem>> getOrgItemList() {
        return mEventApi.getOrgListForUploadEvent();
    }

    public Observable<GetPersonByOrgApiData> getPersonByOrgApiDataObservable(String code,String name){
        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        return mEventApi.getPersonByOrgCodeAndName(user.getLoginName(),code,name);
    }

}
