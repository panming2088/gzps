package com.augurit.agmobile.gzps.maintain.dao;

import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.maintain.model.Conserve;
import com.augurit.agmobile.gzps.maintain.model.ConserveDetail;
import com.augurit.agmobile.gzps.maintain.model.MaintainBatchInfo;
import com.augurit.agmobile.gzps.setting.view.myupload.Result3;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 设施新增API
 * <p>
 * Created by xcl on 2017/11/15.
 */

public interface MaintainFacilityApi {

    /**
     * 获取部件缺失列表
     *
     * @param pageNo
     * @param pageSize
     * @param planId
     * @param loginName
     * @param checkState
     * @return
     */
    @POST("rest/maintenance/getMaintenanceByPlanId")
    Observable<Result3<List<Conserve>>> getMaintenanceByPlanId(@Query("pageNo") int pageNo,
                                                               @Query("pageSize") int pageSize,
                                                               @Query("planId") String planId,
                                                               @Query("loginName") String loginName,
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
    @POST("rest/maintenance/searchPlanAll")
    Observable<MaintainBatchInfo> getBatchInfo(@Query("loginName") String loginName, @Query("pageNo") int pageNo, @Query("pageSize") int pageSize);

    /**
     * 部件的缺失（带附件）
     *
     * @return
     */
    @Multipart
    @POST("rest/maintenance/add")
    Observable<ResponseBody> partsNew(
            @Query("json") String json,
            @Query("attachment") String attachment,
            @PartMap() HashMap<String, RequestBody> requestBody);

    /**
     * 部件的缺失（无附件）
     *
     * @return
     */
    @POST("rest/maintenance/add")
    Observable<ResponseBody> partsNew(
            @Query("json") String json,
            @Query("attachment") String attachment);

    /**
     * 部件的缺失（无附件）
     *
     * @return
     */
    @POST("rest/maintenance/toView/{id}")
    Observable<Result2<ConserveDetail>> getDetailForId(@Path("id") Long id);

    /**
     * @param id
     * @param loginName
     * @return
     */
    @POST("rest/maintenance/delete")
    Observable<Result2<String>> delete(@Query("id") Long id,
                                            @Query("loginName") String loginName);

}
