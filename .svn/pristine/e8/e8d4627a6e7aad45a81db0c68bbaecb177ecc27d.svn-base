package com.augurit.agmobile.gzps.track.dao;

import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.model.StringResult;
import com.augurit.agmobile.gzps.track.model.TrackConfig;
import com.augurit.agmobile.gzps.track.model.TrackList;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.dao
 * @createTime 创建时间 ：2017-08-14
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-14
 * @modifyMemo 修改备注：
 */

public interface TrackApi {

    /**
     * 获取轨迹功能配置
     */
//    @GET("rest/trace/getTraceConfig")
    @POST("rest/diary/gettrackConfig")
    Call<TrackConfig> getTraceConfig();

//    /**
//     * 上传轨迹点
//     */
////    @FormUrlEncoded
//    @POST("rest/trace/saveTracePoint")
//    Call<StringResult> saveTracePoint(@Query("id") long id,
//                                      @Query("trackId") long trackId,
//                                      @Query("trackName") String trackName,
//                                      @Query("userId") String userId,
//                                      @Query("longitude") double longitude,
//                                      @Query("latitude") double latitude,
//                                      @Query("recordDate") long recordDate,
//                                      @Query("recordLength") double recordLength,
//                                      @Query("pointState") int pointState);
    /**
     * 上传轨迹点
     */
//    @FormUrlEncoded
    @POST("rest/diary/addTrackPoint")
    Call<Result2<String>> saveTracePoint(@Query("trackId") long trackId,
                                      @Query("loginName") String userName,
                                      @Query("x") double longitude,
                                      @Query("y") double latitude,
                                      @Query("time") long recordDate,
                                      @Query("recordLength") double recordLength,
                                      @Query("state") int pointState);

    /**
     * 获取轨迹列表
     */
//    @GET("rest/trace/getTraceLinesByUserId")
    @POST("rest/diary/getTrackLinesByLoginName")
    Call<TrackList> getTraceLinesByUserId(@Query("loginName") String userId,
                                          @Query("pageNo") int pageNo,
                                          @Query("pageSize") int pageSize);

    /**
     * 获取轨迹点集合
     */
//    @GET("rest/trace/getTracePointsByTraceId")
    @POST("rest/diary/getTrackPointsByTrackId")
    Call<Result2<List<GPSTrack>>> getTracePointsByTraceId(@Query("trackId") long trackId);


//    /**
//     * 上传轨迹
//     */
//    @POST("rest/trace/saveTraceLine")
//    Call<StringResult> saveTraceLine(@Query("id") long id,
//                                     @Query("trackName") String trackName,
//                                     @Query("userId") String userId,
//                                     @Query("startTime") long startTime,
//                                     @Query("endTime") long endTime,
//                                     @Query("recordLength") double recordLength,
//                                     @Query("state") int state);

    /**
     * 上传轨迹
     */
    @POST("rest/diary/addTrackLine")
    Call<Result2<Long>> saveTraceLine(@Query("loginName") String loginName,
                                @Query("id") String trackId,
                                @Query("recordLength") double recordLength);

//    @GET("rest/trace/deleteTraceLine")
    @POST("rest/diary/deleteTrackLine")
    Call<StringResult> deleteTraceLine(@Query("id") long trackId);
}
