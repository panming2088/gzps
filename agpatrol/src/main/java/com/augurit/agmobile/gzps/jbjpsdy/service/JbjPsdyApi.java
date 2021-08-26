package com.augurit.agmobile.gzps.jbjpsdy.service;

import okhttp3.ResponseBody;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 接驳井-排水单元 相关接口
 *
 * @PROJECT GZPS
 * @USER Augurit
 * @CREATE 2020/11/10 11:02
 */
public interface JbjPsdyApi {
    @POST("rest/psdyJbjRest/getPsdyPshList")
    Observable<ResponseBody> getAllSewerageUsers(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("psdy") String psdy);

    @POST("rest/psdyJbjRest/getPsdyPshList")
    Observable<ResponseBody> getSewerageUsersByType(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("psdy") String psdy, @Query("dischargerType1") String dischargerType1);
}
