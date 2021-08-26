package com.augurit.agmobile.patrolcore.common.device.dao;

import android.content.Context;

import com.augurit.agmobile.patrolcore.common.action.dao.remote.ActionsApi;
import com.augurit.agmobile.patrolcore.common.device.model.RegisterModel;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.DeviceIdUtils;

import rx.Observable;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.device.dao
 * @createTime 创建时间 ：2017/8/7
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/8/7
 * @modifyMemo 修改备注：
 */

public class DeviceRegisterNetLogic {
    private Context mContext;
    private AMNetwork mAMNetwork;

    public DeviceRegisterNetLogic(Context mContext, String baseUrl) {
        this.mContext = mContext;
        mAMNetwork = new AMNetwork(baseUrl);
        mAMNetwork.addLogPrint();
        mAMNetwork.build();
        mAMNetwork.registerApi(DeviceRegisterApi.class);
    }

    public Observable<RegisterModel> checkIfDeviceRegister(){
        String deviceId = DeviceIdUtils.getDeviceId(mContext);
        if(deviceId == null) return null;


        String url = BaseInfoManager.getBaseServerUrl(mContext);
        url = url +"rest/device/checkDevice?device="+deviceId;

        DeviceRegisterApi api = (DeviceRegisterApi)mAMNetwork.getServiceApi(DeviceRegisterApi.class);
        return api.checkIfDeviceRegister(url);
    }
}
