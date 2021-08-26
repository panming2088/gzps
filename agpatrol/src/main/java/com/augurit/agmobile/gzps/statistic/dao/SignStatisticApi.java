package com.augurit.agmobile.gzps.statistic.dao;

import com.augurit.agmobile.gzps.statistic.model.SignInfo;
import com.augurit.agmobile.gzps.statistic.model.SignStatisticInfoBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by luob on 2017/12/21.
 */

public interface SignStatisticApi {
    @GET("rest/dailySign/getStatisticsInfo")
    Observable<List<SignStatisticInfoBean>> getSignNearTimeData(@Query("orgName") String org_name,@Query("roleType") boolean roleType);

    @GET("rest/dailySign/getDetailSignInfo/{orgName}/{yearMonth}/{roleType}")
    Observable<SignInfo> getDetailSignInfo(@Path("orgName") String orgName, @Path("yearMonth") String yearMonth,@Path("roleType") boolean roleType);
}
