package com.augurit.agmobile.gzps.login;

import android.content.Context;

import com.augurit.agmobile.gzps.login.dao.FunUpdateInfoNetLogic;
import com.augurit.agmobile.gzps.login.model.FunUpdateInfoModel;
import com.augurit.am.cmpt.common.base.BaseInfoManager;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：luobiao
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.login
 * @createTime 创建时间 ：2017-08-10
 * @modifyBy 修改人 ：luobiao
 * @modifyTime 修改时间 ：2017-08-10
 * @modifyMemo 修改备注：
 */

public class FunUpdateInfoService {
    private FunUpdateInfoNetLogic mFunUpdateInfoNetLogic;
    private Context mContext;

    public FunUpdateInfoService(Context context) {
        mContext = context;
        String serverUrl = BaseInfoManager.getBaseServerUrl(context);
        mFunUpdateInfoNetLogic = new FunUpdateInfoNetLogic(context,serverUrl);
    }

    /**
     * 获取同步更新信息
     * @return
     */
    public Observable<List<FunUpdateInfoModel>> getFunUpdateInfo(){
        return mFunUpdateInfoNetLogic.getFunUpdateInfo().subscribeOn(Schedulers.io());
    }
}
