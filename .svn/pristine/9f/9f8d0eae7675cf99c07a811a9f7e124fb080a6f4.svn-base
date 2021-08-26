package com.augurit.agmobile.gzps.statistic.dao;

import com.augurit.agmobile.gzps.statistic.model.UploadStatisticBean;
import com.augurit.agmobile.gzps.statistic.model.UploadYTStatisticBean;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by luob on 2017/12/21.
 */

public interface UploadStatisticApi {
    @POST("rest/parts/toPastNowDay")
    Observable<UploadYTStatisticBean> getUploadNearTimeData(@Query("layerName") String layerName);

    @POST("rest/parts/toPartsCount")
    Observable<UploadStatisticBean> getUploadStatisticForDistric(@Query("parentOrgName") String parentOrgName,@Query("reportType") String reportType, @Query("startTime") long startTime, @Query("endTime") long endTime);
}
