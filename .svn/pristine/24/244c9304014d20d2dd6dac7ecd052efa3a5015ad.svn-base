package com.augurit.agmobile.gzps.uploadfacility.dao;

import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.uploadfacility.model.ApprovalOpinion;

import java.util.List;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 审核意见接口
 * Created by xcl on 2017/12/7.
 */

public interface ApprovalOpinionApi {

    /**
     * 获取审核意见
     * @return
     */
    @POST("rest/parts/toCheckRecord")
    Observable<Result2<List<ApprovalOpinion>>> getOpinion(@Query("reportId") Long markId,
                                                          @Query("reportType") String reportType);
    /**
     * 获取审核意见
     * @return
     */
    @POST("rest/parts/linePipeCheckRecord")
    Observable<Result2<List<ApprovalOpinion>>> getlinePipeCheckRecord(@Query("reportId") Long markId);

}
