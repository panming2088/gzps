package com.augurit.agmobile.gzps.setting.dao;

import com.augurit.agmobile.gzps.setting.model.SignBean;
import com.augurit.agmobile.gzps.setting.model.SignResultBean;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by luob on 2017/12/25.
 */

public interface SignApi {
    @GET("rest/dailySign/getSignInfo/{signerId}/{yearMonth}")
    Observable<SignBean> getSignInfo(@Path("signerId") String signerId, @Path("yearMonth") String yearMonth);



    @POST("rest/dailySign/sign")
    Observable<SignResultBean> sign(@Query("signerId") String signerId,
                                    @Query("signerName") String signerName,
                                    @Query("x") double x,
                                    @Query("y") double y,
                                    @Query("road") String road,
                                    @Query("addr") String addr,
                                    @Query("orgSeq") String orgSeq,
                                    @Query("orgName") String orgName,
                                    @Query("appVersion") String appVersion);
}
