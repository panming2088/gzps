package com.augurit.agmobile.gzps.uploadfacility.dao;

import com.augurit.agmobile.gzps.uploadfacility.model.ServerCorrectErrorData;
import com.augurit.agmobile.gzps.uploadfacility.model.ServerNewAddedData;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 数据上报图层
 * Created by xcl on 2017/12/7.
 */

public interface UploadLayerApi {

    /**
     * 获取纠错数据
     * @param objectId
     * @return
     */
    @POST("rest/parts/toFeatureAttach?reportType=modify")
    Observable<ServerCorrectErrorData> getCorrectErrorData(@Query("objectId")String objectId,
                                                           @Query("reportType")String reportType);


    /**
     * 获取新增数据
     * @param objectId
     * @param reportType 新增类型，：（1）confirm（部件确认）（2）add（新增）
     * @return
     */
    @POST("rest/parts/toFeatureAttach")
    Observable<ServerNewAddedData> getNewAddedData(@Query("objectId")String objectId,
                                                   @Query("reportType")String reportType);



//    /**
//     * 获取新增数据
//     * @param markId
//     * @param reportType 上报类型，：（1）confirm（部件确认）（2）add（新增） (3)modify （数据纠错）
//     * @return
//     */
//    @POST("rest/parts/toFeatureAttach")
//    Observable<ServerAttachment> getAttachment(@Query("markId")String markId,
//                                                              @Query("reportType")String reportType);

}
