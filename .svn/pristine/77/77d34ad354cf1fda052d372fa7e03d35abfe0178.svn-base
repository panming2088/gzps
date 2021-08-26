package com.augurit.agmobile.gzps.uploadfacility.dao;

import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.setting.view.myupload.Result3;
import com.augurit.agmobile.gzps.uploadfacility.model.BatchInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.BuildInfoBySGuid;
import com.augurit.agmobile.gzps.uploadfacility.model.DoorNoRespone;
import com.augurit.agmobile.gzps.uploadfacility.model.DoubtBean;
import com.augurit.agmobile.gzps.uploadfacility.model.SewerageItemBean;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadDoorNoDetailBean;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 设施新增API
 * <p>
 * Created by xcl on 2017/11/15.
 */

public interface UploadFacilityApi {

    /**
     * 获取部件缺失列表
     *
     * @param pageNo
     * @param pageSize
     * @param userName
     * @return
     */
    @POST("rest/parts/getPartsNew")
    Observable<Result2<List<UploadedFacility>>> getPartsNew(@Query("pageNo") int pageNo,
                                                            @Query("pageSize") int pageSize,
                                                            @Query("markPersonId") String userName,
                                                            @Query("checkState") String checkState);


    /**
     * 部件缺失（新增）的附件获取接口
     *
     * @param markId 部件标识id
     * @return
     */
    @POST("rest/parts/getPartsNewAttach")
    Observable<ServerAttachment> getPartsNewAttach(@Query("markId") long markId);

    /**
     * 获取不同批次
     */
    @POST("rest/assign/searchQd")
    Observable<BatchInfo> getBatchInfo(@Query("loginName") String loginName, @Query("pageNo") int pageNo, @Query("pageSize") int pageSize);

    /**
     * 新增管线
     */
    @POST("rest/parts/addLinePipe")
    Observable<ResponseBody> addLinePipe(@Query("loginName") String loginName
            , @Query("datas") String dataJson);

    /**
     * 更新管线
     */
    @POST("rest/parts/updateLinePipe")
    Observable<ResponseBody> updateLinePipe(@Query("loginName") String loginName, @Query("datas") String dataJson);

    /**
     * @param loginName
     * @param datasJson
     * @return
     */
    @POST("rest/parts/deleteLinePipe")
    Observable<Result2<String>> deletePipe(@Query("loginName") String loginName,
            @Query("datas") String datasJson);

    /**
     * 对上报管线数据的修改
     */
    @POST("rest/parts/updateReportLinePipe")
    Observable<ResponseBody> updateReportLinePipe(@Query("id") String id, @Query("loginName") String loginName, @Query("datas") String dataJson);

    /**
     * 新增存疑
     * @param objectId 设施的objectId
     * @param doubtType 存疑类型：1点，2线，3面
     * @param loginName 登录名
     * @param description 原因
     * @param layerName 设施名称（画面的就是“面”）
     * @return
     */
    @POST("rest/parts/partsDoubt")
    Observable<ResponseBody> partsDoubt(@Query("objectId") String objectId, @Query("doubtType") int doubtType,@Query("loginName") String loginName, @Query("description") String description,@Query("layerName") String layerName,@Query("rings") String rings);

    /**
     * 新增和取消无法挂接
     * @param objectId 设施的objectId
     * @param wfgj
     * @param wfgj_reason
     * @return
     */
    @POST("rest/psdyJbjRest/updatePsdyState")
    Observable<ResponseBody> partsNotHook(@Query("loginName") String loginName,@Query("objectid") String objectId, @Query("wfgj") int wfgj, @Query("wfgj_reason") String wfgj_reason);

    /**
     * 获取存疑
     * @param pageNo
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param status 1：存疑，2：取消存疑）
     * @param doubtType （3：面，4：点或线）
     * @param markPersonId (登录名)
     * @return
     */
    @POST("rest/parts/getDoubtMark")
    Observable<Result3<List<DoubtBean>>> getDoubtMark(@Query("pageNo") int pageNo,
                                                      @Query("pageSize") int pageSize,
                                                      @Query("startTime") Long startTime,
                                                      @Query("endTime") Long endTime,
                                                      @Query("status") String status,
                                                      @Query("doubtType") int doubtType,
                                                      @Query("markPersonId") String markPersonId);


    /**
     * @param id
     * @param loginName 登录名
     * @return
     */
    @POST("rest/parts/partsDoubt")
    Observable<Result2<String>> partsDoubt(@Query("id") String id,
                                                             @Query("loginName") String loginName);

    @FormUrlEncoded
    @POST("rest/pshSbssInfRest/getHouseUnitPop")
    Observable<SewerageItemBean> getSewerageItemData(@Field("id") String id, @Field("dzdm") String dzdm, @Field("page") int page, @Field("count") int count,
                                                     @Field("refresh_item") int refresh_item, @Field("refresh_list_type") int refresh_list_type, @Field("name") String name, @Field("add_type") String add_type);

    @POST("rest/pshSbssInfRest/getBuildInfBySGuid")
    Observable<BuildInfoBySGuid> getBuildInfBySGuid(@Query("sGuid") String sGuid);

    /**
     * 新增门牌号码/编辑后提交接口
     * 提交接口字段Gsid有值
     * @param json
     * @return
     */
    @POST("rest/pshSbssInfRest/sbssMp")
    Observable<DoorNoRespone>  addNewDoorNo(@Query("json") String json);

    /**
     *获取我的门牌上报列表
     * dzqc 地址全称
     */
    @POST("rest/pshSbssInfRest/getMpList")
    Observable<Result3<List<UploadDoorNoDetailBean>>> getUploadDoorNoList(@Query("loginName") String loginName, @Query("dzqc") String dzqc, @Query("state") String state , @Query("page") int page, @Query("count") int pageNo);

    /**
     * 删除门牌接口
     * @param sgid
     * @return
     */
    @FormUrlEncoded
    @POST("rest/pshSbssInfRest/deleteMp")
    Observable<ResponseBody> deleteDoorNoBySGid(@Field("sGuid") String sgid);
}
