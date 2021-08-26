package com.augurit.agmobile.gzps.journal.dao;

import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.model.StringResult2;
import com.augurit.agmobile.gzps.journal.model.Journal;
import com.augurit.agmobile.gzps.journal.model.JournalAttachment;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.journal.dao
 * @createTime 创建时间 ：17/11/7
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/7
 * @modifyMemo 修改备注：
 */

public interface JournalApi {


    /**
     * 新增巡查日志（带附件）
     *
     * @return
     */
    @Multipart
    @POST("rest/parts/diayrNew")
    Observable<StringResult2> diayrNew(
            @Query("loginName") String loginName,
            @Query("writerId") String writerId,
            @Query("writerName") String writerName,
            @Query("description") String description,
            @Query("road") String road,
            @Query("addr") String addr,
            @Query("x") double x,
            @Query("y") double y,
            @Query("layerUrl") String layerUrl,
            @Query("layerName") String layerName,
            @Query("objectId") String objectId,
            @Query("teamMember") String teamMember,
            @Query("waterLevel") String waterLevel,
            @PartMap() HashMap<String, RequestBody> requestBody);

    /**
     * 新增巡查日志（不带附件）
     *
     * @return
     */
    @POST("rest/parts/diayrNew")
    Observable<StringResult2> diayrNew(
            @Query("loginName") String loginName,
            @Query("writerId") String writerId,
            @Query("writerName") String writerName,
            @Query("description") String description,
            @Query("road") String road,
            @Query("addr") String addr,
            @Query("x") double x,
            @Query("y") double y,
            @Query("layerUrl") String layerUrl,
            @Query("layerName") String layerName,
            @Query("objectId") String objectId,
            @Query("teamMember") String teamMember,
            @Query("waterLevel") String waterLevel);

    /**
     * 更新巡查日志（带附件）
     *
     * @return
     */
    @Multipart
    @POST("rest/parts/diayrNew")
    Observable<StringResult2> diayrNew(
            @Query("id") String id,
            @Query("loginName") String loginName,
            @Query("writerId") String writerId,
            @Query("writerName") String writerName,
            @Query("description") String description,
            @Query("road") String road,
            @Query("addr") String addr,
            @Query("x") double x,
            @Query("y") double y,
            @Query("layerUrl") String layerUrl,
            @Query("layerName") String layerName,
            @Query("objectId") String objectId,
            @Query("teamMember") String teamMember,
            @Query("waterLevel") String waterLevel,
            @PartMap() HashMap<String, RequestBody> requestBody);

    /**
     * 更新巡查日志（不带附件）
     *
     * @return
     */
    @Multipart
    @POST("rest/parts/diayrNew")
    Observable<StringResult2> diayrNew(
            @Query("id") String id,
            @Query("loginName") String loginName,
            @Query("writerId") String writerId,
            @Query("writerName") String writerName,
            @Query("description") String description,
            @Query("road") String road,
            @Query("addr") String addr,
            @Query("x") double x,
            @Query("y") double y,
            @Query("layerUrl") String layerUrl,
            @Query("layerName") String layerName,
            @Query("objectId") String objectId,
            @Query("teamMember") String teamMember,
            @Query("waterLevel") String waterLevel);

    /**
     * 获取巡查日志列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @POST("rest/parts/getDiayr")
    Observable<Result2<List<Journal>>> getDiayr(@Query("pageNo") int pageNo,
                                                @Query("pageSize") int pageSize,
                                                @Query("loginName") String loginName,
                                                @Query("userName") String userName
    );

    /**
     * 获取附件列表接口
     *
     * @param markId 部件标识id
     * @return
     */
    @POST("rest/parts/getDiayrAttach")
    Observable<Result2<List<JournalAttachment>>> getDiayrAttach(@Query("markId") String markId);

    /**
     * 获取问题上报列表
     *
     * @param objectId
     * @return
     */
    @POST("rest/parts/getDiayrAttach")
    Observable<Result2> getUploadProblemList(String objectId);

    /**
     * 获取设施上报列表
     *
     * @param objectId
     * @return
     */
    @POST("rest/parts/getDiayrAttach")
    Observable<Result2> getUploadFacilityList(String objectId);

    /**
     * 获取日常巡检列表
     *
     * @param objectId
     * @return
     */
    @POST("rest/parts/getDiayrAttach")
    Observable<Result2> getDayPatrolList(String objectId);

    /**
     * 获取设施上报详情
     *
     * @param objectId
     * @param itemId
     * @return
     */
    @POST("rest/parts/getDiayrAttach")
    Observable<Result2> getUploadFacilityDetail(String objectId, String itemId);

    /**
     * 获取日常巡检详情
     *
     * @param objectId
     * @param itemId
     * @return
     */
    @POST("rest/parts/toDiaryView")
    Observable<Result2> getDayPatrolDetail(@Query("id") String objectId);

    /**
     * 获取问题上报详情
     *
     * @param objectId
     * @param itemId
     * @return
     */
    @POST("rest/parts/getDiayrAttach")
    Observable<Result2> getUploadProblemDetail(String objectId, String itemId);



}
