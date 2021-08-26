package com.augurit.agmobile.gzps.setting.service;

import android.content.Context;

import com.augurit.agmobile.gzps.publicaffair.dao.IFacilityAffairApi;
import com.augurit.agmobile.gzps.setting.dao.SignApi;
import com.augurit.agmobile.gzps.setting.model.SignBean;
import com.augurit.agmobile.gzps.setting.model.SignResultBean;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by luob on 2017/12/25.
 */

public class SignService {


    public static final int LOAD_ITEM_PER_PAGE = 15;

    private AMNetwork amNetwork;
    private SignApi signApi;
    private Context mContext;

    public SignService(Context mContext) {
        this.mContext = mContext;
    }

    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null) {
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.build();
            this.amNetwork.registerApi(IFacilityAffairApi.class);
            this.signApi = (SignApi) this.amNetwork.getServiceApi(SignApi.class);
        }
    }

    /**
     * 获取某一个月的签到情况
     *
     * @return
     */
    public Observable<SignBean> getSignInfo(String yearMonth) {
        String url = BaseInfoManager.getSupportUrl(mContext);
        String signerId = BaseInfoManager.getUserId(mContext);
        initAMNetwork(url);
        return signApi.getSignInfo(signerId,yearMonth)
                .subscribeOn(Schedulers.io());
//                .map(new Func1<ResponseBody,SignStatisticInfoBean>(){
//
//                    @Override
//                    public SignStatisticInfoBean call(ResponseBody responseBody) {
//                        SignStatisticInfoBean infos =new SignStatisticInfoBean();
//                        String respone = null;
//                        try {
//                            respone = responseBody.string();
//                            if(TextUtils.isEmpty(respone)){
//                                return new SignStatisticInfoBean();
//                            }
//                            infos = JsonUtil.getObject(respone,SignStatisticInfoBean.class);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        return infos;
//                    }
//                });
    }

    /**
     * 签到
     *
     * @param x
     * @param y
     * @param road
     * @param addr
     * @return
     */
    public Observable<SignResultBean> sign(double x,
                                           double y,
                                           String road,
                                           String addr,
                                           String appVersion) {
        String url = BaseInfoManager.getSupportUrl(mContext);
        String signerId = BaseInfoManager.getUserId(mContext);
        String signerName = BaseInfoManager.getUserName(mContext);
        String orgName = BaseInfoManager.getUserOrg(mContext);
        String orgSeq = BaseInfoManager.getOrgCode(mContext);
        initAMNetwork(url);
        return signApi.sign(signerId,signerName,x,y,road,addr,orgSeq,orgName,appVersion)
                .subscribeOn(Schedulers.io());
//        map(new Func1<ResponseBody,SignResultBean>(){
//
//                    @Override
//                    public SignResultBean call(ResponseBody responseBody) {
//                        SignResultBean infos =new SignResultBean();
//                        String respone = null;
//                        try {
//                            respone = responseBody.string();
//                            if(TextUtils.isEmpty(respone)){
//                                return new SignResultBean();
//                            }
//                            infos = JsonUtil.getObject(respone,SignResultBean.class);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        return infos;
//                    }
//                });
    }
}
