package com.augurit.agmobile.gzps.doorno.service;

import android.content.Context;

import com.augurit.agmobile.gzps.common.util.PortSelectUtil;
import com.augurit.agmobile.gzps.doorno.dao.DoorNoHandleApi;
import com.augurit.agmobile.gzps.uploadfacility.model.DoorNoRespone;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadDoorNoBean;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.JsonUtil;

import rx.Observable;
import rx.schedulers.Schedulers;

//import com.augurit.agmobile.gzpssb.bean.AddressBean;

/**
 * Created by sdb on 2018-04-21.
 */

public class PSHUploadDoorNoService {

    private AMNetwork amNetwork;
    private DoorNoHandleApi mUploadDoorNoApi;
    private Context mContext;

    public PSHUploadDoorNoService(Context mContext) {
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
            this.amNetwork.registerApi(DoorNoHandleApi.class);
            this.mUploadDoorNoApi = (DoorNoHandleApi) this.amNetwork.getServiceApi(DoorNoHandleApi.class);
        }
    }

    /**
     * @param bean
     * @return
     */
    public Observable<DoorNoRespone> addNewDoorNo(UploadDoorNoBean bean) {
//        String url =  RequestConstant.HTTP_REQUEST + LoginConstant.GZPSH_AGSUPPORT + RequestConstant.URL_DIVIDER;
        String supportUrl = PortSelectUtil.getAgSupportPortBaseURL(mContext);
        initAMNetwork(supportUrl);
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        bean.setLoginName(loginName);
        bean.setMarkTime(System.currentTimeMillis()+"");
        return mUploadDoorNoApi.addNewDoorNo(JsonUtil.getJson(bean)).subscribeOn(Schedulers.io());
    }

    /**
     * @param
     * @return
     */
//    public Observable<AddressBean> queryAdressInfo(String id, String add_type) {
//        String supportUrl = PortSelectUtil.getAgSupportPortBaseURL(mContext);
//        initAMNetwork(supportUrl);
//        return mUploadDoorNoApi.queryAdressInfo(id,add_type);
//    }

}
