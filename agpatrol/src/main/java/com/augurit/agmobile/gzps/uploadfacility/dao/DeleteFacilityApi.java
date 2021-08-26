package com.augurit.agmobile.gzps.uploadfacility.dao;


import com.augurit.agmobile.gzps.common.model.Result2;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by xucil on 2017/12/27.
 */

public interface DeleteFacilityApi {

    /**
     *
     * @param reportType modify / add : 纠错/新增/撤销删除
     * @param reportId
     * @param loginName
     * @param phoneBrand 手机型号
     * @return
     */
    @POST("rest/parts/deleteReport")
    Observable<Result2<String>> deleteFacility(@Query("reportType") String reportType ,
                                               @Query("reportId") Long reportId,
                                               @Query("loginName") String loginName,
                                               @Query("phoneBrand") String phoneBrand);
    /**
     *
     * 删除管线上报信息/撤销删除
     * @param id
     * @param loginName
     * @return
     */
    @POST("rest/parts/deleteReportLinePipe")
    Observable<Result2<String>> deleteReportLinePipe(@Query("id") String id ,
                                               @Query("loginName") String loginName);

}
