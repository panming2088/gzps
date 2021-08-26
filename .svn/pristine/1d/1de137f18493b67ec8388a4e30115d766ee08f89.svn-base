package com.augurit.agmobile.gzps.uploadfacility.service;

import android.content.Context;

import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.uploadfacility.dao.DeleteFacilityApi;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.PhoneUtil;
import com.augurit.am.fw.utils.log.LogUtil;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by xucil on 2017/12/27.
 */

public class DeleteFacilityService {

    private Context mContext;

    private AMNetwork amNetwork;
    private DeleteFacilityApi uploadLayerApi;

    public DeleteFacilityService(Context mContext) {
        this.mContext = mContext;
    }


    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null) {
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.build();
            this.amNetwork.registerApi(DeleteFacilityApi.class);
            this.uploadLayerApi = (DeleteFacilityApi) this.amNetwork.getServiceApi(DeleteFacilityApi.class);
        }
    }

    /**
     * 删除设施（管井）
     *
     * @param reportType
     * @param markId
     * @return
     */
    public Observable<Result2<String>> deleteFacility(String reportType, Long markId) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        String phoneBrand = PhoneUtil.getDeviceBrand() + ":" + PhoneUtil.getSystemModel();
        LogUtil.d("okhttp", "phoneBrand:" + phoneBrand);
        return uploadLayerApi.deleteFacility(reportType, markId, loginName, phoneBrand)
                .subscribeOn(Schedulers.io());
    }
    /**
     * 删除管线上报信息
     *
     * @param id
     * @return
     */
    public Observable<Result2<String>> deleteReportLinePipe(String id) {
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        return uploadLayerApi.deleteReportLinePipe(id, loginName)
                .subscribeOn(Schedulers.io());
    }
}
