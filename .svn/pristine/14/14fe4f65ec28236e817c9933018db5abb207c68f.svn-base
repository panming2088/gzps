package com.augurit.am.cmpt.update.utils;



import com.augurit.am.cmpt.update.dao.UpdateApi;
import com.augurit.am.cmpt.update.dao.UpdateServiceFactory;
import com.augurit.am.cmpt.update.model.UpdateAppInfo;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.common.update.utils
 * @createTime 创建时间 ：17/5/10
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/5/10
 * @modifyMemo 修改备注：
 */

public class CheckUpdateUtils {
    public static String mServerUrl = "http://139.159.243.185:8080/appFile/apk_version.json";


    /**
     * 设置更新的服务器地址
     * @param url
     */
    public static void setServerUrl(String url){
        mServerUrl = url;
    }

    /**
     * 检查更新
     */
    @SuppressWarnings("unused")
    public static void checkUpdate(String appname, String curVersion,final CheckCallBack updateCallback) {
        UpdateApi apiService=   UpdateServiceFactory.createServiceFrom(UpdateApi.class);
        apiService.getUpdateInfo(mServerUrl)//测试使用
                //   .apiService.getUpdateInfo(appname, curVersion)//开发过程中可能使用的
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdateAppInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Throwable throwable = e.fillInStackTrace();
                        String s = e.toString();
                        updateCallback.onError();
                    }

                    @Override
                    public void onNext(UpdateAppInfo updateAppInfo) {
                        if (updateAppInfo.data == null || updateAppInfo.data.updateurl == null) {
                            updateCallback.onError(); // 失败
                        } else {
                            updateCallback.onSuccess(updateAppInfo);
                        }
                    }
                });
    }


    public interface CheckCallBack{
        void onSuccess(UpdateAppInfo updateInfo);
        void onError();
    }
}

