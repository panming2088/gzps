package com.augurit.agmobile.gzps.journal.service;

import android.content.Context;

import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.journal.dao.JournalApi;
import com.augurit.agmobile.gzps.journal.model.Journal;
import com.augurit.agmobile.gzps.journal.model.JournalAttachment;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.journal.service
 * @createTime 创建时间 ：2018-07-04
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-07-04
 * @modifyMemo 修改备注：
 */

public class JournalListDetailService {

    private Context mContext;
    private AMNetwork amNetwork;
    private JournalApi journalApi;

    public JournalListDetailService(Context mContext) {
        this.mContext = mContext;
    }


    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null) {
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.build();
            this.amNetwork.registerApi(JournalApi.class);
            this.journalApi = (JournalApi) this.amNetwork.getServiceApi(JournalApi.class);
        }
    }

    public Observable<Result2> getUploadProblemList(String objectId) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        return journalApi.getUploadProblemList(objectId);
    }


    public Observable<Result2> getUploadFacilityList(String objectId) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        return journalApi.getUploadFacilityList(objectId);
    }

    public Observable<Result2> getDayPatrolList(String objectId) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        return journalApi.getDayPatrolList(objectId);
    }

    public Observable<Result2> getUploadFacilityDetail(String objectId, String itemId) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        return journalApi.getUploadFacilityDetail(objectId , itemId);
    }

    public Observable<Result2> getDayPatrolDetail(String itemId) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        return journalApi.getDayPatrolDetail(itemId );
    }

    public Observable<Result2> getUploadProblemDetail(String objectId, String itemId) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        return journalApi.getUploadProblemDetail(objectId , itemId);
    }
}
