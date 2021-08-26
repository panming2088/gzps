package com.augurit.agmobile.gzps.doorno.service;

import android.content.Context;

import com.augurit.agmobile.gzps.common.constant.LoginConstant;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.util.PortSelectUtil;
import com.augurit.agmobile.gzps.doorno.dao.PSHMyUploadDoorNoApi;
import com.augurit.agmobile.gzps.doorno.model.PSHApprovalOpinion;
import com.augurit.agmobile.gzps.doorno.model.QueryIdBeanResult;
import com.augurit.agmobile.gzps.setting.view.myupload.Result3;
import com.augurit.agmobile.gzps.uploadfacility.model.ApprovalOpinion;
import com.augurit.agmobile.gzps.uploadfacility.model.DoorNoFilterCondition;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadDoorNoDetailBean;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.am.cmpt.common.base.RequestConstant;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 设施新增Service
 * Created by xcl on 2017/11/14.
 */

public class PSHMyUploadDoorNoService {

    public static final int LOAD_ITEM_PER_PAGE = 15;

    private AMNetwork amNetwork;
    private PSHMyUploadDoorNoApi mPSHMyUploadDoorNoApi;
    private Context mContext;
    private List<Project> projects;
    private TableDataManager tableDataManager;

    public PSHMyUploadDoorNoService(Context mContext) {
        this.mContext = mContext;
    }

    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null) {
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.setReadTime(20000);
            this.amNetwork.setWriteTime(20000);
            this.amNetwork.setConnectTime(20000);
            this.amNetwork.build();
            this.amNetwork.registerApi(PSHMyUploadDoorNoApi.class);
            this.mPSHMyUploadDoorNoApi = (PSHMyUploadDoorNoApi) this.amNetwork.getServiceApi(PSHMyUploadDoorNoApi.class);
        }
    }

    /**
     * 获取我的门牌上报列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Observable<Result3<List<UploadDoorNoDetailBean>>> getMyUploads(int page, int pageSize, String dzqc, String checkState,
                                                                          DoorNoFilterCondition doorNoFilterCondition) {
//        String supportUrl = RequestConstant.HTTP_REQUEST + LoginConstant.GZPSH_AGSUPPORT + RequestConstant.URL_DIVIDER;;
        String supportUrl = PortSelectUtil.getAgSupportPortBaseURL(mContext);
        initAMNetwork(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        if (doorNoFilterCondition == null) {
        } else {
            dzqc = doorNoFilterCondition.doorNo;
        }
        return mPSHMyUploadDoorNoApi.getUploadDoorNoList(loginName,dzqc,checkState,page,pageSize);
    }


    /**
     * 根据guid删除门牌
     * @param sgid
     * @return
     */
    public Observable<ResponseBody> deleteDoorNo(String sgid) {
//        String supportUrl = RequestConstant.HTTP_REQUEST + LoginConstant.GZPSH_AGSUPPORT + RequestConstant.URL_DIVIDER;
        String supportUrl = PortSelectUtil.getAgSupportPortBaseURL(mContext);
        initAMNetwork(supportUrl);

        return mPSHMyUploadDoorNoApi.deleteDoorNoBySGid(sgid);
    }

    /**
     * 获取审核意见列表
     * @param markId
     * @return
     */
    public Observable<List<ApprovalOpinion>> getOpinions(String markId) {

//        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        String supportUrl = PortSelectUtil.getAgSupportPortBaseURL(mContext);
        initAMNetwork(supportUrl);

        return mPSHMyUploadDoorNoApi.getOpinion(markId)
                .map(new Func1<Result2<List<ApprovalOpinion>>, List<ApprovalOpinion>>() {
                    @Override
                    public List<ApprovalOpinion> call(Result2<List<ApprovalOpinion>> listResult2) {
                        return listResult2.getData();
                    }
                }).subscribeOn(Schedulers.io());
//                .onErrorReturn(new Func1<Throwable, List<ApprovalOpinion>>() {
//                    @Override
//                    public List<ApprovalOpinion> call(Throwable throwable) {
//                        return new ArrayList<>();
//                    }
//                });
    }


    /**
     * 根据关键字搜索门牌的objectid
     * @param keyword 用户输入的关键字
     * @return
     */
    public Observable<QueryIdBeanResult> queryObjectIdByKeyword(String keyword) {
        String supportUrl = RequestConstant.HTTP_REQUEST + LoginConstant.GZPS_AGSUPPORT + RequestConstant.URL_DIVIDER;;
//        String supportUrl = PortSelectUtil.getAgSupportPortBaseURL(mContext);
        initAMNetwork(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        return mPSHMyUploadDoorNoApi.queryObjectIdByKeyword(keyword);
    }

    /**
     * 根据关键字搜索排水单元的objectid
     * @param keyword 用户输入的关键字
     * @return
     */
    public Observable<QueryIdBeanResult> queryPshUnitObjectIdByKeyword(String keyword) {
        String supportUrl = RequestConstant.HTTP_REQUEST + LoginConstant.GZPS_AGSUPPORT + RequestConstant.URL_DIVIDER;
        initAMNetwork(supportUrl);
        return mPSHMyUploadDoorNoApi.queryPshUnitObjectIdByKeyword(keyword);
    }
}
