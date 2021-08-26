package com.augurit.agmobile.patrolcore.common.device.dao;

import com.augurit.agmobile.patrolcore.common.action.model.ActionModel;
import com.augurit.agmobile.patrolcore.common.device.model.RegisterModel;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;
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

public interface DeviceRegisterApi {
    /**
     * 校验当前设备是否已注册
     * @return
     */
    @GET()
    Observable<RegisterModel> checkIfDeviceRegister(@Url String url);
}
