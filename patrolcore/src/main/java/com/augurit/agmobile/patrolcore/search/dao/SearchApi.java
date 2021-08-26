package com.augurit.agmobile.patrolcore.search.dao;



import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableItems;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.search.model.BriefSearchResult;
import com.augurit.agmobile.patrolcore.search.model.GetPhotosResult;
import com.augurit.agmobile.patrolcore.search.model.NewBriefSearchResult;
import com.augurit.agmobile.patrolcore.search.model.NewGetPhotosResult;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.search.dao
 * @createTime 创建时间 ：2017-03-22
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-22
 * @modifyMemo 修改备注：
 */

public interface SearchApi {

    @POST("rest/upload/find/{patrolId}")
    Observable<GetPhotosResult> getPhotos(@Path("patrolId") String patrolId);

    @POST("rest/upload/find/{patrolId}")
    Observable<NewGetPhotosResult> getNewPhotos(@Path("patrolId") String patrolId);

    @GET("rest/report/briefBuffer")
    Observable<BriefSearchResult> getHistories(@Query("page") int page, @Query("rows") int rows);

    @GET("rest/report/briefBuffer")
    Observable<NewBriefSearchResult> getNewHistories(@Query("page") int page, @Query("rows") int rows);

    @GET("rest/report/briefBuffer")
    Observable<NewBriefSearchResult> getNewHistories(@Query("page") int page, @Query("rows") int rows, @QueryMap Map<String, String> params);

    @GET("rest/report/buffer")
    Observable<List<List<TableItem>>> getCompleteUploadInfo(@Query("id") String id);

    /**
     * 获取关键字查询字段配置
     */
    @FormUrlEncoded
    @POST
    Observable<TableItems> getKeywordFileds(@Url String url, @Field("projectId") String projectId);

    /**
     * 获取历史查询筛选条件
     */
    @GET
    Observable<List<TableItem>> getFilterConditions(@Url String url);

}
