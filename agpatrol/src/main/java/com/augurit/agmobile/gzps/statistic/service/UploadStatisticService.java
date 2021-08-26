package com.augurit.agmobile.gzps.statistic.service;

import android.content.Context;

import com.augurit.agmobile.gzps.publicaffair.dao.IFacilityAffairApi;
import com.augurit.agmobile.gzps.statistic.dao.UploadStatisticApi;
import com.augurit.agmobile.gzps.statistic.model.UploadStatisticBean;
import com.augurit.agmobile.gzps.statistic.model.UploadYTStatisticBean;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by xcl on 2017/11/17.
 */

public class UploadStatisticService {


    public static final int LOAD_ITEM_PER_PAGE = 15;

    private AMNetwork amNetwork;
    private UploadStatisticApi uploadStatistic;
    private Context mContext;

    public UploadStatisticService(Context mContext) {
        this.mContext = mContext;
    }

    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null) {
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.build();
            this.amNetwork.registerApi(IFacilityAffairApi.class);
            this.uploadStatistic = (UploadStatisticApi) this.amNetwork.getServiceApi(UploadStatisticApi.class);
        }
    }

    /**
     * 获取所有昨天和今天的上报数据
     *
     * @return
     */
    public Observable<UploadYTStatisticBean> getUploadNearTimeData(String layerName) {
        String url = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(url);
        return uploadStatistic.getUploadNearTimeData(layerName)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取上报统计信息
     *
     * @param parentOrgName
     * @param startTime
     * @param endTime
     * @return
     */
    public Observable<UploadStatisticBean> getUploadStatisticForDistric(String parentOrgName,String reportType,long startTime,long endTime) {
        if (parentOrgName != null && parentOrgName.equals("全市")) {
            parentOrgName = null;
        }
        if (reportType != null && reportType.equals("全部")) {
            reportType = null;
        }
        String url = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(url);
        return uploadStatistic.getUploadStatisticForDistric(parentOrgName,reportType, startTime,endTime)
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
