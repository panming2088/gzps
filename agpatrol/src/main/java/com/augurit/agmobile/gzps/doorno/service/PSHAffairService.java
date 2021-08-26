package com.augurit.agmobile.gzps.doorno.service;

import android.content.Context;

import com.augurit.agmobile.gzps.common.util.PortSelectUtil;
import com.augurit.agmobile.gzps.doorno.dao.PSHAffairApi;
import com.augurit.agmobile.gzps.doorno.model.PSHAffairDetail;
import com.augurit.agmobile.gzps.doorno.model.PSHEventBean;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by sdb on 2018-04-11.
 */

public class PSHAffairService {


    public static final int LOAD_ITEM_PER_PAGE = 15;

    private AMNetwork amNetwork;
    private PSHAffairApi facilityAffair;
    private Context mContext;

    public PSHAffairService(Context mContext) {
        this.mContext = mContext;
    }

    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null) {
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.setConnectTime(20000);
            this.amNetwork.setReadTime(20000);
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.build();
            this.amNetwork.registerApi(PSHAffairApi.class);
            this.facilityAffair = (PSHAffairApi) this.amNetwork.getServiceApi(PSHAffairApi.class);
        }
    }

    /**
     * 获取列表
     *
     * @param parentOrgName 区
     *                      bigType  大类
     *                      smallType 小类
     * @return
     */
    public Observable<PSHEventBean> getPSHAffairList(int page,
                                                     String parentOrgName,
                                                     String bigType,
                                                     String smallType,
                                                     Long startTime,
                                                     Long endTime) {
//        String url = BaseInfoManager.getSupportUrl(mContext);
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);;
        initAMNetwork(supportUrl);


        if (parentOrgName != null && parentOrgName.equals("全部")) {
            parentOrgName = null;
        }

//        if (parentOrgName != null && parentOrgName.equals("市水务局")) {
//            parentOrgName = null;
//        }

        /**
         * 如果是区级用户，那么不允许查看全市数据，只允许查看自己区的数据
         */
        if (!ifCurrentUserBelongToCityUser()) {
            parentOrgName = getParentOrgOfCurrentUser();
        }

        if ("市水务局".equals(parentOrgName)) {
            parentOrgName = "广州市水务局";
        }

        if (bigType != null && bigType.equals("全部")) {
            bigType = null;
        }
        if (smallType != null && smallType.equals("全部")) {
            smallType = null;
        }

        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();

        return facilityAffair.getPSHAffairList(page,
                LOAD_ITEM_PER_PAGE,
                parentOrgName,
                bigType,
                smallType,
                startTime,
                endTime,
                loginName)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取列表详情
     *
     * @return
     */
    public Observable<PSHAffairDetail> getPSHAffairDetail(int affairId) {
//        String url = BaseInfoManager.getSupportUrl(mContext);
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        return facilityAffair.getPSHAffairDetail(affairId)
                .subscribeOn(Schedulers.io());
    }

    public Observable<PSHAffairDetail> getPSHUnitDetail(int unitId) {
//        String url = BaseInfoManager.getSupportUrl(mContext);
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);;

        initAMNetwork(supportUrl);

        return facilityAffair.getPSHUnitDetail(unitId, BaseInfoManager.getLoginName(mContext))
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
}
