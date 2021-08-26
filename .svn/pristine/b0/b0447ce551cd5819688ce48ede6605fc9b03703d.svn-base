package com.augurit.agmobile.gzps.common.facilityownership.service;

import android.content.Context;

import com.augurit.agmobile.gzps.common.facilityownership.dao.IFacilityOwnerShipUnitApi;
import com.augurit.agmobile.gzps.common.facilityownership.model.FacilityOwnerShipUnit;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * 设施权属单位网络请求
 * Created by xcl on 2017/11/27.
 */

public class FaciliyOwnerShipUnitService {


    private AMNetwork amNetwork;
    private IFacilityOwnerShipUnitApi facilityOwnerShipUnitApi;
    private Context mContext;

    public FaciliyOwnerShipUnitService(Context mContext) {
        this.mContext = mContext;
    }

    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null) {
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.build();
            this.amNetwork.registerApi(IFacilityOwnerShipUnitApi.class);
            this.facilityOwnerShipUnitApi = (IFacilityOwnerShipUnitApi) this.amNetwork.getServiceApi(IFacilityOwnerShipUnitApi.class);
        }
    }

    /**
     * 获取当前设施所属的权属单位
     * @return
     */
    public Observable<FacilityOwnerShipUnit> getFacilityOwnerShipUnit(String loginName){
        String baseServerUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(baseServerUrl);
        return facilityOwnerShipUnitApi.getFacilityOwnerShipUnit(loginName)
                .subscribeOn(Schedulers.io());
    }


}
