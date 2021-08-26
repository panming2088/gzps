package com.augurit.agmobile.gzps.login.dao;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.gzps.login.model.FunUpdateInfoModel;
import com.augurit.agmobile.patrolcore.common.device.dao.DeviceRegisterApi;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.JsonUtil;
import com.google.common.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * 描述：
 *
 * @author 创建人 ：luobiao
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.login.dao
 * @createTime 创建时间 ：2017/8/10
 * @modifyBy 修改人 ：luobiao
 * @modifyTime 修改时间 ：2017/8/10
 * @modifyMemo 修改备注：
 */

public class FunUpdateInfoNetLogic {
    private Context mContext;
    private AMNetwork mAMNetwork;

    public FunUpdateInfoNetLogic(Context mContext, String baseUrl) {
        this.mContext = mContext;
        mAMNetwork = new AMNetwork(baseUrl);
        mAMNetwork.addLogPrint();
        mAMNetwork.build();
        mAMNetwork.registerApi(DeviceRegisterApi.class);
    }

    /**
     * 获取同步更新信息
     * @return
     */
    public Observable<List<FunUpdateInfoModel>> getFunUpdateInfo(){
        FunUpdateInfoApi api = (FunUpdateInfoApi)mAMNetwork.getServiceApi(FunUpdateInfoApi.class);
        return api.getFunUpdateInfo().map(new Func1<ResponseBody, List<FunUpdateInfoModel>>() {

            @Override
            public List<FunUpdateInfoModel> call(ResponseBody responseBody) {
                List<FunUpdateInfoModel> infos =new ArrayList<FunUpdateInfoModel>();
                String respone = null;
                try {
                    respone = responseBody.string();
                    if(TextUtils.isEmpty(respone) ||responseBody.contentType().subtype().equals("html")){
                        return new ArrayList<FunUpdateInfoModel>();
                    }
                    infos = JsonUtil.getObject(respone,new TypeToken<List<FunUpdateInfoModel>>() {
                    }.getType());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return infos;
            }
        });
    }
}
