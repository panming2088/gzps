package com.augurit.am.cmpt.update.dao;



import com.augurit.am.cmpt.update.model.UpdateAppInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.common.update.dao
 * @createTime 创建时间 ：17/5/10
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/5/10
 * @modifyMemo 修改备注：
 */

public interface UpdateApi {
    @GET("update")
    Observable<UpdateAppInfo> getUpdateInfo(@Query("appname") String appname, @Query("versionCode") String appVersion);


    //接口测试
    @GET("update")
    Observable<UpdateAppInfo> getUpdateInfo();

    @GET
    Observable<UpdateAppInfo> getUpdateInfo(@Url String url);
}
