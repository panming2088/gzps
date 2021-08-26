package com.augurit.agmobile.patrolcore.common.opinion.dao;

import com.augurit.agmobile.patrolcore.common.opinion.model.OpinionTemplate;
import com.augurit.agmobile.patrolcore.common.opinion.model.TemplateResult;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.dao
 * @createTime 创建时间 ：2017-07-07
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-07
 * @modifyMemo 修改备注：
 */

public interface OpinionApi {

    @FormUrlEncoded
    @POST("rest/template/saveTemplate")
    Observable<Response<ResponseBody>> saveOpinionTemplate(@Field("id") String id,
                                                           @Field("name") String name,
                                                           @Field("content") String content,
                                                           @Field("projectId") String projectId,
                                                           @Field("link") String link,
                                                           @Field("userId") String userId,
                                                           @Field("authorization") String authorization);

    @GET("rest/template/deleteTemplateById")
    Observable<Response<ResponseBody>> deleteOpinionTemplate(@Query("id") String id);

    @GET("rest/template/templateList")
    Observable<TemplateResult> getOpinionTemplates(@Query("userId") String userId,
                                                         @Query("name") String name,
                                                         @Query("projectId") String projectId,
                                                         @Query("link") String link,
                                                         @Query("page") int pageNo,
                                                         @Query("rows") int pageSize);
}
