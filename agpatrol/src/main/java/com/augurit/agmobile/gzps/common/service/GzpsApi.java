package com.augurit.agmobile.gzps.common.service;

import com.augurit.agmobile.gzps.common.model.BannerUrl;
import com.augurit.agmobile.gzps.common.model.OUser;
import com.augurit.agmobile.gzps.common.model.Result2;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liangsh on 2017/11/10.
 */

public interface GzpsApi {

    /**
     * 获取一线人员同班组成员
     * @param loginName
     * @return
     */
    @POST("rest/parts/getTeamUser")
    Observable<Result2<List<OUser>>> getTeamMember(@Query("loginName") String loginName);

    /**
     * 获取服务器时间
     * @return
     */
    @POST("rest/parts/getNewTime")
    Observable<Result2<Long>> getServerTimestamp();

    /**
     * AES加密
     * @param content
     * @return
     */
    @GET("rest/asiWorkflow/AESEncode")
    Observable<Result2<String>> AESEncode(@Query("content") String content);

    /**
     * 专栏的某一类型设为已读
     * @return
     */
    @POST("rest/columnsUnread/readInfo")
    Observable<okhttp3.ResponseBody> columnsReadInfo(@Query("userId") String userId,
                                             @Query("infoType") String infoType);

    /**
     * 记录用户操作日志
     * @return
     */
    @POST("rest/userLog")
    Observable<okhttp3.ResponseBody> userLog(@Query("loginName") String loginName,
                                                     @Query("time") String time);

    /**
     * 主界面轮播图
     * @return
     */
    @GET("rest/appPicture/getTopImg")
    Observable<Result2<List<BannerUrl>>> banner(@Query("loginName") String loginName);
}
