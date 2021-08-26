package com.augurit.agmobile.gzps.doorno.service;


import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.doorno.model.SewerageInvestBean;
import com.augurit.agmobile.gzps.doorno.model.SewerageItemBean;
import com.augurit.agmobile.gzps.doorno.model.SewerageRoomClickItemBean;
import com.augurit.agmobile.gzps.uploadfacility.model.BuildInfoBySGuid;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by xiaoyu on 2017/5/26.
 */

public interface MyRetroService {


    @FormUrlEncoded
    @POST("rest/pshSbssInfRest/getHouseUnitPop")
    Observable<SewerageItemBean> getSewerageItemData(@Field("id") String id, @Field("dzdm") String dzdm, @Field("page") int page, @Field("count") int count,
                                                     @Field("refresh_item") int refresh_item, @Field("refresh_list_type") int refresh_list_type, @Field("add_type") String add_type);

    @FormUrlEncoded
    @POST("rest/pshSbssInfRest/getHouseUnitPop")
    Observable<SewerageItemBean> getSewerageItemData(@Field("id") String id, @Field("dzdm") String dzdm, @Field("page") int page, @Field("count") int count,
                                                     @Field("refresh_item") int refresh_item, @Field("refresh_list_type") int refresh_list_type, @Field("name") String name, @Field("add_type") String add_type);

    @GET("rest/pshSbssInfRest/getInfByHouseId")
    Observable<SewerageRoomClickItemBean> getSewerageRoomClickItem(@Query("id") String id, @Query("page") int page, @Query("count") int count,
                                                                   @Query("refresh_item") int refresh_item, @Query("refresh_list_type") int refresh_list_type);

    @POST("rest/pshSbssInfRest/investEnd")
    Observable<SewerageInvestBean> investEnd(@Query("sGuid") String sGuid, @Query("loginName") String loginName, @Query("add_type") String add_type);


    @POST("rest/pshSbssInfRest/getBuildInfBySGuid")
    Observable<BuildInfoBySGuid> getBuildInfBySGuid(@Query("sGuid") String sGuid);

    /**
     * 排水户信息上报
     */
    @Multipart
    @POST("rest/discharge/toAddCollect")
    Observable<ResponseBody> uploadUserInfo(
            @Query("json") String json,
            @PartMap() HashMap<String, RequestBody> requestBody);

    /**
     * 排水户信息编辑上报
     */
    @Multipart
    @POST("rest/discharge/toUpdateCollect")
    Observable<ResponseBody> toUpdateCollect(
            @Query("json") String json,
            @PartMap() HashMap<String, RequestBody> requestBody);

    /**
     * 排水户信息编辑上报
     */
    @POST("rest/discharge/toUpdateCollect")
    Observable<ResponseBody> toUpdateCollect(
            @Query("json") String json);


    /**
     * 工业排水户排水户信息编辑上报
     */
    @POST("rest/discharge/toUpdateGypsh")
    Observable<ResponseBody> toUpdateCollectGY(
            @Query("json") String json);

    /**
     * 工业排水户排水户信息编辑上报
     */
    @Multipart
    @POST("rest/discharge/toUpdateGypsh")
    Observable<ResponseBody> toUpdateCollectGY(
            @Query("json") String json,
            @PartMap() HashMap<String, RequestBody> requestBody);


    /**
     * 排水户信息编辑删除
     */
    @POST("rest/discharge/deleteCollect")
    Observable<ResponseBody> toDeleteCollect(@Query("id") String id, @Query("loginName") String loginName);

    /**
     * 注销排水户
     */
    @POST("rest/discharge/pshCancle")
    Observable<ResponseBody> toCancelCollect(@Query("id") String id, @Query("loginName") String loginName);
}
