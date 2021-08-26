package com.augurit.agmobile.mapengine.common.dao;

import com.augurit.agmobile.mapengine.common.model.AreaLocate;
import com.augurit.agmobile.mapengine.common.model.FeatureSet;
import com.augurit.agmobile.mapengine.common.model.Field;
import com.augurit.agmobile.mapengine.common.model.FieldStats;
import com.augurit.agmobile.mapengine.common.model.SpatialBufferResult;
import com.augurit.agmobile.mapengine.common.model.UserArea;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * AGCOM接口
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.dao
 * @createTime 创建时间 ：2017-03-28
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-28
 * @modifyMemo 修改备注：
 */
public interface AgcomApi {

    /**
     * 缓冲分析接口
     * @param param SpatialBufferParam的JSON字符串
     * @return
     */
    @POST("rest/spatial/spatialBuffer")
    Call<SpatialBufferResult> spatialBuffer(@Query("param") String param);

    /**
     * 空间查询接口
     * @param param SpatialQueryParam数组的JSON字符串
     * @return
     */
    @POST("rest/spatial/spatialQuery")
    Call<List<FeatureSet>> spatialQuery(@Query("param") String param);


    /**
     * 统计
     * @param projectLayerId
     * @param groupField
     * @param wkt
     * @param where
     * @param statsType
     * @param statsField
     * @param userId
     * @param projectId
     * @param timestamp
     * @return
     */
    @GET("rest/spatial/getFieldStats/{projectLayerId}/{groupField}/{wkt}/{where}/{statsType}/{statsField}")
    Call<List<FieldStats>> getFieldStats(@Path("projectLayerId") String projectLayerId,
                                         @Path("groupField") String groupField,
                                         @Path("wkt") String wkt,
                                         @Path("where") String where,
                                         @Path("statsType") String statsType,
                                         @Path("statsField") String statsField,
                                         @Query("userId") String userId,
                                         @Query("projectId") String projectId,
                                         @Query("timestamp") String timestamp);

    /**
     * 由图层ID获取对应的专题图层ID
     * @param layerId
     * @return
     */
    @GET("rest/system/getPorjectLayerBaseIdByLayerId/{layerId}")
    Call<ResponseBody> getPorjectLayerBaseIdByLayerId(@Path("layerId") String layerId);


    @GET("rest/system/getUserArea/{discodeId}")
    Call<UserArea> getUserArea(@Path("discodeId") int discodeId);

    @GET("rest/system/locateDiscode/{userAreaId}/{discodeLocateId}")
    Call<AreaLocate> getAreaLocate(@Path("userAreaId") int userAreaId, @Path("discodeLocateId") int discodeLocateId);


    @GET("rest/system/getLayerField/{projectLayerId}")
    Call<List<Field>> getLayerField(@Path("projectLayerId") String projectLayerId);
}
