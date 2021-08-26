package com.augurit.agmobile.gzps.journal.service;

import android.content.Context;

import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.model.StringResult2;
import com.augurit.agmobile.gzps.journal.dao.JournalApi;
import com.augurit.agmobile.gzps.journal.model.Journal;
import com.augurit.agmobile.gzps.journal.model.JournalAttachment;
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
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.journal.service
 * @createTime 创建时间 ：17/11/4
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/4
 * @modifyMemo 修改备注：
 */

public class JournalsService {

    private Context mContext;
    private AMNetwork amNetwork;
    private JournalApi journalApi;

    public JournalsService(Context mContext){
        this.mContext = mContext;
    }


    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null){
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.build();
            this.amNetwork.registerApi(JournalApi.class);
            this.journalApi = (JournalApi) this.amNetwork.getServiceApi(JournalApi.class);
        }
    }


    /**
     * 添加日志
     *
     * @param journal
     * @return
     */
    public Observable<StringResult2> add(Journal journal) {


       // String supportUrl = "http://192.168.43.8:8080/agsupport_swj/" ;
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);


        String prefix = "";
        int i = 0;
        List<Photo> attachments = journal.getAttachments();
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

        Observable<StringResult2> observable;

        if (!ListUtil.isEmpty(attachments)) {

            observable = journalApi.diayrNew(user.getLoginName(),
                    user.getLoginName(),
                    journal.getWriterName(),
                    journal.getDescription(),
                    journal.getRoad(),
                    journal.getAddr(),
                    journal.getX(),
                    journal.getY(),
                    journal.getLayerUrl(),
                    journal.getLayerName(),
                    journal.getObjectId(),
                    journal.getTeamMember(),
                    journal.getWaterLevel(),
                    requestMap);
        } else {

            observable = journalApi.diayrNew(user.getLoginName(),
                    user.getLoginName(),
                    journal.getWriterName(),
                    journal.getDescription(),
                    journal.getRoad(),
                    journal.getAddr(),
                    journal.getX(),
                    journal.getY(),
                    journal.getLayerUrl(),
                    journal.getLayerName(),
                    journal.getObjectId(),
                    journal.getTeamMember(),
                    journal.getWaterLevel());
        }


        return observable
                .subscribeOn(Schedulers.io());

    }


    /**
     * 更新日志
     *
     * @param journal
     * @return
     */
    public Observable<StringResult2> update(Journal journal) {

       // String supportUrl = "http://192.168.43.8:8080/agsupport_swj/" ;
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);


        String prefix = "";
        int i = 0;
        List<Photo> attachments = journal.getAttachments();
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

        Observable<StringResult2> observable;

        if (!ListUtil.isEmpty(attachments)) {

            observable = journalApi.diayrNew(journal.getId(),
                    user.getLoginName(),
                    user.getLoginName(),
                    journal.getWriterName(),
                    journal.getDescription(),
                    journal.getRoad(),
                    journal.getAddr(),
                    journal.getX(),
                    journal.getY(),
                    journal.getLayerUrl(),
                    journal.getLayerName(),
                    journal.getObjectId(),
                    journal.getTeamMember(),
                    journal.getWaterLevel(),
                    requestMap);
        } else {

            observable = journalApi.diayrNew(
                    journal.getId(),
                    user.getLoginName(),
                    user.getLoginName(),
                    journal.getWriterName(),
                    journal.getDescription(),
                    journal.getRoad(),
                    journal.getAddr(),
                    journal.getX(),
                    journal.getY(),
                    journal.getLayerUrl(),
                    journal.getLayerName(),
                    journal.getObjectId(),
                    journal.getTeamMember(),
                    journal.getWaterLevel());
        }


        return observable
                .subscribeOn(Schedulers.io());

    }


    /**
     * 获取我的日志列表
     *
     * @return
     */
    public Observable<List<Journal>> getMyJournals(final int page, int pageSize) {
      //  String supportUrl = "http://192.168.43.8:8080/agsupport_swj/" ;
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        User user = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();

        return journalApi.getDiayr(page, pageSize, user.getLoginName(), user.getUserName())
                //获取列表（不包含附件）
                .map(new Func1<Result2<List<Journal>>, List<Journal>>() {
                    @Override
                    public List<Journal> call(Result2<List<Journal>> result2) {
                        List<Journal> data = result2.getData();
                        if (ListUtil.isEmpty(data)) {
                            return new ArrayList<>();
                        }
                        return data;
                    }
                })
                .flatMap(new Func1<List<Journal>, Observable<Journal>>() {
                    @Override
                    public Observable<Journal> call(List<Journal> journals) {
                        return Observable.from(journals);
                    }
                })
                //获取附件
                .flatMap(new Func1<Journal, Observable<Journal>>() {
                    @Override
                    public Observable<Journal> call(final Journal journal) {
                        return getJournalAttachments(journal.getId())
                                .map(new Func1<Result2<List<JournalAttachment>>, Journal>() {
                                    @Override
                                    public Journal call(Result2<List<JournalAttachment>> result2) {
                                        if (!ListUtil.isEmpty(result2.getData())) {
                                            ArrayList<JournalAttachment> data = new ArrayList<>(result2.getData());
                                            ArrayList<Photo> photoList = new ArrayList<Photo>();
                                            for (JournalAttachment journalAttachment: data){
                                                Photo photo = new Photo();
                                                photo.setPhotoPath(journalAttachment.getAttPath());
                                                photoList.add(photo);
                                            }
                                            journal.setAttachments(photoList);
                                        }
                                        return journal;
                                    }
                                });
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取附件列表
     *
     * @param journalId 日志id
     * @return
     */
    public Observable<Result2<List<JournalAttachment>>> getJournalAttachments(String journalId) {
       // String supportUrl = "http://192.168.43.8:8080/agsupport_swj/" ;
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        return journalApi.getDiayrAttach(journalId)
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, Result2<List<JournalAttachment>>>() {
                    @Override
                    public Result2<List<JournalAttachment>> call(Throwable throwable) {
                        Result2<List<JournalAttachment>> journalAttachments = new Result2<List<JournalAttachment>>();
                        journalAttachments.setCode(500);
                        journalAttachments.setMessage("500 - 系统内部错误");
                        return journalAttachments;
                    }
                });
    }

}
