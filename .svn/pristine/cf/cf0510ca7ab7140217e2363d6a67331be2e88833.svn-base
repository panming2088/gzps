package com.augurit.agmobile.patrolcore.common.opinion.service;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.patrolcore.common.opinion.dao.LocalDatabaseOpinionDao;
import com.augurit.agmobile.patrolcore.common.opinion.dao.RemoteOpinionRestDao;
import com.augurit.agmobile.patrolcore.common.opinion.model.OpinionTemplate;
import com.augurit.agmobile.patrolcore.common.opinion.util.OpinionConstant;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.log.util.NetUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.service
 * @createTime 创建时间 ：2017-07-07
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-07
 * @modifyMemo 修改备注：
 */

public class OpinionService {

    private Context mContext;
    private RemoteOpinionRestDao mRemoteOpinionRestDao;
    private LocalDatabaseOpinionDao mLocalDatabaseOpinionDao;

    public OpinionService(Context context) {
        this.mContext = context;
        mRemoteOpinionRestDao = new RemoteOpinionRestDao(context);
        mLocalDatabaseOpinionDao = new LocalDatabaseOpinionDao();
    }


    /**
     * 处理固定类型的模板规则
     *
     * @param template
     * @return
     */
    public String handleTemplateExceptField(String template) {
        if (template.contains(OpinionConstant.USERNAME)) {
            String username = new LoginService(mContext, AMDatabase.getInstance()).getUser().getUserName();
            template = template.replace(OpinionConstant.USERNAME, username);
        }
        if (template.contains(OpinionConstant.DATE)) {
            String date = TimeUtil.getStringTimeYMDChines(new Date());
            template = template.replace(OpinionConstant.DATE, date);
        }
        if (template.contains(OpinionConstant.TIME)) {
            String time = TimeUtil.getStringTimeHHmmChinese(new Date());
            template = template.replace(OpinionConstant.TIME, time);
        }
        return template;
    }

    /**
     * 根据数据字典编码，从本地数据库中查找对应的显示值
     *
     * @param code
     * @return
     */
    public String getSpinnerValueByCode(String code) {
        List<DictionaryItem> dictionaryItems = new TableDBService(mContext).getDictionaryByCode(code);
        if (ListUtil.isEmpty(dictionaryItems)) {
            return "";
        }
        DictionaryItem dictionaryItem = dictionaryItems.get(0);
        return dictionaryItem.getName();
    }

    public String getUUID() {
        return UUID.randomUUID().toString();  // 目前是生成一个UUID
    }


    public Observable<String> saveOpinionToServer(String id,
                                                  String name,
                                                  String content,
                                                  String projectId,
                                                  String link,
                                                  String authorization) {
        String userId = BaseInfoManager.getUserId(mContext);
        if (TextUtils.isEmpty(id)) {
            id = getUUID();
        }
        return mRemoteOpinionRestDao.saveOpinionTemplate(id, name, content, projectId, link, userId, authorization)
                .subscribeOn(Schedulers.io());
    }

    public Observable<String> deleteOpinionAtServer(String id) {
        return mRemoteOpinionRestDao.deleteOpinionTemplate(id)
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<OpinionTemplate>> getOpinionsFromServer(String name,
                                                                   String projectId,
                                                                   String link,
                                                                   int pageNo,
                                                                   int pageSize) {
        String userId = BaseInfoManager.getUserId(mContext);
        return mRemoteOpinionRestDao.getOpinionTemplates(userId, name, projectId, link, pageNo, pageSize)
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<OpinionTemplate>> getOpinion(String name,
                                                        String projectId,
                                                        String link,
                                                        int pageNo,
                                                        int pageSize) {
        if (NetUtil.isConnected(mContext)) {
            return getOpinionsFromServer(name, projectId, link, pageNo, pageSize);
        } else {
            return getOpinionsFromLocal(name, projectId, link, pageNo, pageSize);
        }
    }

    public void saveOpinionToLocal(String id,
                                   String name,
                                   String content,
                                   String projectId,
                                   String link,
                                   String authorization,
                                   boolean isUpload) {
        String userId = BaseInfoManager.getUserId(mContext);
        OpinionTemplate opinionTemplate = new OpinionTemplate();
        if (TextUtils.isEmpty(id)) {
            id = getUUID();
        }
        opinionTemplate.setId(id);
        opinionTemplate.setName(name);
        opinionTemplate.setContent(content);
        opinionTemplate.setUserId(userId);
        opinionTemplate.setProjectId(projectId);
        opinionTemplate.setLink(link);
        opinionTemplate.setAuthorization(authorization);
        opinionTemplate.setUpload(isUpload);
        mLocalDatabaseOpinionDao.saveOpinionTemplate(opinionTemplate);
    }

    public void saveOpinionToLocal(OpinionTemplate opinionTemplate) {
        String userId = BaseInfoManager.getUserId(mContext);
        opinionTemplate.setUserId(userId);
        mLocalDatabaseOpinionDao.saveOpinionTemplate(opinionTemplate);
    }

    public void deleteOpinionAtLocal(String id) {
        mLocalDatabaseOpinionDao.deleteOpinionTemplate(id);
    }

    public Observable<List<OpinionTemplate>> getOpinionsFromLocal(final String name, final String projectId, final String link) {
        return Observable.create(new Observable.OnSubscribe<List<OpinionTemplate>>() {
            @Override
            public void call(Subscriber<? super List<OpinionTemplate>> subscriber) {
                String userId = BaseInfoManager.getUserId(mContext);
                subscriber.onNext(mLocalDatabaseOpinionDao.getOpinionTemplates(userId, name, projectId, link));
            }
        })
                .subscribeOn(Schedulers.io());
    }

    public List<OpinionTemplate> getNotUploadOpinions() {
        String userId = BaseInfoManager.getUserId(mContext);
        return mLocalDatabaseOpinionDao.getNotUploadOpinions(userId);
//        return mLocalDatabaseOpinionDao.queryAll();
    }

    /**
     * @param name
     * @param link
     * @param pageNo   从1开始算
     * @param pageSize
     * @return
     */
    public Observable<List<OpinionTemplate>> getOpinionsFromLocal(final String name, final String projectId,
                                                                  final String link, final int pageNo,
                                                                  final int pageSize) {
        return Observable.create(new Observable.OnSubscribe<List<OpinionTemplate>>() {
            @Override
            public void call(Subscriber<? super List<OpinionTemplate>> subscriber) {
                String userId = BaseInfoManager.getUserId(mContext);
                int start = 0;
                if (pageNo <= 1) {
                    start = 0;
                } else {
                    start = (pageNo - 1) * pageSize;
                }
                subscriber.onNext(mLocalDatabaseOpinionDao.getOpinionTemplates(userId, name, projectId, link, start, pageSize));
            }
        }).subscribeOn(Schedulers.io());
    }

}
