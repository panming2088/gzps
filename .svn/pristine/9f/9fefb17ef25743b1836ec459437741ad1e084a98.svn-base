package com.augurit.agmobile.gzps.publicaffair.dao;

import com.augurit.agmobile.gzps.monitor.model.WellMonitorInfo;
import com.augurit.agmobile.gzps.publicaffair.model.FacilityAffairResponseBody;
import com.augurit.agmobile.gzps.publicaffair.model.ModifyFacilityDetail;
import com.augurit.agmobile.gzps.publicaffair.model.UploadFacilityDetail;
import com.augurit.agmobile.gzps.setting.view.myupload.Result4;
import com.augurit.agmobile.gzps.uploadfacility.model.PipeBean;

import java.util.List;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by xcl on 2017/11/17.
 */

public interface IFacilityAffairApi {

    @POST("rest/parts/searchCorrOrLack")
    Observable<FacilityAffairResponseBody> getFacilityAffairList(@Query("pageNo") int page,
                                                                 @Query("pageSize") int pageSize,
                                                                 @Query("layerName") String layerName,
                                                                 @Query("parentOrgName") String parentOrgName,
                                                                 @Query("type") String type,
                                                                 @Query("startTime") Long startTime,
                                                                 @Query("endTime") Long endTime,
                                                                 @Query("loginName") String loginName);

    @POST("rest/parts/searchCorrOrLackByOId")
    Observable<FacilityAffairResponseBody> searchCorrOrLackByOId(@Query("pageNo") int page,
                                                                 @Query("pageSize") int pageSize,
                                                                 @Query("loginName") String loginName,
                                                                 @Query("objectId") String objectId
    );


    @POST("rest/parts/toView?type=correct")
    Observable<ModifyFacilityDetail> getModifiedDetail(@Query("id")long id,@Query("loginName") String loginName);

    @POST("rest/parts/toView?type=lack")
    Observable<UploadFacilityDetail> getUploadDetail(@Query("id")long id,@Query("loginName") String loginName);
    @POST("rest/parts/toView?type=lack")
    Observable<WellMonitorInfo> getWell(@Query("id")long id, @Query("loginName") String loginName);

    //    http://139.159.243.185:8081/agsupport_swj/rest/parts/toShiPaiView?id=113341
//    当数据为：广州市城市排水有限公司 查看详情的时候 调用上面接口 传列表对应的id就行
    @POST("rest/parts/toShiPaiView?type=correct")
    Observable<ModifyFacilityDetail> getModifiedSPDetail(@Query("id")long id,@Query("loginName") String loginName,@Query("correctType") String correctType,
                                                         @Query("source") String source,@Query("layerName") String layerName);

    @POST("rest/parts/toShiPaiView?type=lack")
    Observable<UploadFacilityDetail> getUploadSPDetail(@Query("id")long id,@Query("loginName") String loginName,@Query("correctType") String correctType,
                                                       @Query("source") String source ,@Query("layerName") String layerName);

    /**
     * 获取管道列表（事务公开）
     *
     * @param type      1新增、2校核、3删除
     * @param pipeType  管道类型
     * @param direction 方向
     */
    @POST("rest/parts/getLinePipe")
    Observable<Result4<List<PipeBean>>> getLinePipe(@Query("pageNo") int pageNo,
                                                    @Query("pageSize") int pageSize,
                                                    @Query("type") String type,
                                                    @Query("startTime") Long startTime,
                                                    @Query("endTime") Long endTime,
                                                    @Query("loginName") String loginName,
                                                    @Query("pipeType") String pipeType,
                                                    @Query("direction") String direction,
                                                    @Query("parentOrgName") String parentOrgName);
}
