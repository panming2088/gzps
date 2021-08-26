package com.augurit.agmobile.gzps.uploadfacility.dao;

import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.uploadfacility.model.FeedbackInfo;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lsh on 2018/3/6.
 */

public interface FeedbackFacilityApi {

    /**
     * 反馈上传接口，带附件
     *
     * @param aid
     * @param tableType
     * @param describe
     * @param situation
     * @param feedbackPerson
     * @param feedbackPersoncode
     * @param objectId
     * @return
     */
    @Multipart
    @POST("rest/feedBack/saveFeedbackInf")
    Observable<ResponseBody> save(@Query("aid") long aid,
                                  @Query("tableType") String tableType,
                                  @Query("describe") String describe,
                                  @Query("situation") String situation,
                                  @Query("feedbackPerson") String feedbackPerson,
                                  @Query("feedbackPersoncode") String feedbackPersoncode,
                                  @Query("objectId") String objectId,
                                  @PartMap() HashMap<String, RequestBody> requestBody);

    /**
     * 反馈上传接口，不带附件
     *
     * @param aid
     * @param tableType
     * @param describe
     * @param situation
     * @param feedbackPersion
     * @param feedbackPersioncode
     * @param objectId
     * @return
     */
    @POST("rest/feedBack/saveFeedbackInf")
    Observable<ResponseBody> save(@Query("aid") long aid,
                                  @Query("tableType") String tableType,
                                  @Query("describe") String describe,
                                  @Query("situation") String situation,
                                  @Query("feedbackPersion") String feedbackPersion,
                                  @Query("feedbackPersioncode") String feedbackPersioncode,
                                  @Query("objectId") String objectId);

    @POST("rest/feedBack/deleteFeedbackInf")
    Observable<ResponseBody> delete(@Query("id") String id);


    /**
     * 反馈修改接口，不带附件
     *
     * @param aid
     * @param tableType
     * @param describe
     * @param situation
     * @param feedbackPersion
     * @param feedbackPersioncode
     * @param objectId
     * @param deletePicId  要删除的图片的ID，多张图片以,号隔开
     * @return
     */
    @Multipart
    @POST("rest/feedBack/updateFeedbackInf")
    Observable<ResponseBody> update(@Query("id") String id,
                                    @Query("aid") long aid,
                                    @Query("tableType") String tableType,
                                    @Query("describe") String describe,
                                    @Query("situation") String situation,
                                    @Query("feedbackPersion") String feedbackPersion,
                                    @Query("feedbackPersioncode") String feedbackPersioncode,
                                    @Query("objectId") String objectId,
                                    @Query("deletePicId") String deletePicId,
                                    @PartMap() HashMap<String, RequestBody> requestBody);

    /**
     * 反馈修改接口，不带附件
     *
     * @param aid
     * @param tableType
     * @param describe
     * @param situation
     * @param feedbackPersion
     * @param feedbackPersioncode
     * @param objectId
     * @param deletePicId  要删除的图片的ID，多张图片以,号隔开
     * @return
     */
    @POST("rest/feedBack/updateFeedbackInf")
    Observable<ResponseBody> update(@Query("id") String id,
                                    @Query("aid") long aid,
                                    @Query("tableType") String tableType,
                                    @Query("describe") String describe,
                                    @Query("situation") String situation,
                                    @Query("feedbackPersion") String feedbackPersion,
                                    @Query("feedbackPersioncode") String feedbackPersioncode,
                                    @Query("objectId") String objectId,
                                    @Query("deletePicId") String deletePicId);

    @GET("rest/feedBack/getFeedbackInf")
    Observable<Result2<List<FeedbackInfo>>> getFeedbackInfos(@Query("aid") long aid,
                                                             @Query("tableType") String tableType);


}
