package com.augurit.agmobile.gzps.doorno.dao;

import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.doorno.model.PSHApprovalOpinion;
import com.augurit.agmobile.gzps.doorno.model.QueryIdBeanResult;
import com.augurit.agmobile.gzps.setting.view.myupload.Result3;
import com.augurit.agmobile.gzps.uploadfacility.model.ApprovalOpinion;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadDoorNoDetailBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 设施新增API
 * <p>
 * Created by xcl on 2017/11/15.
 */

public interface PSHMyUploadDoorNoApi {

    /**
     *获取我的门牌上报列表
     * dzqc 地址全称
     */
    @POST("rest/pshSbssInfRest/getMpList")
    Observable<Result3<List<UploadDoorNoDetailBean>>> getUploadDoorNoList(@Query("loginName") String loginName, @Query("dzqc") String dzqc, @Query("state") String state, @Query("page") int page, @Query("count") int pageNo);
//            @Query("pageNo") int pageNo, int pageSize, String checkState, DoorNoFilterCondition doorNoFilterCondition);


    /**
     * 删除门牌接口
     * @param sgid
     * @return
     */
    @FormUrlEncoded
    @POST("rest/pshSbssInfRest/deleteMp")
    Observable<ResponseBody> deleteDoorNoBySGid(@Field("sGuid") String sgid);



    /**
     * 获取审核意见
     * http://139.159.243.185:8080/agsupport_swj/rest/discharge/pshCheckRecord/123
     * @return
     */
    @POST("rest/discharge/pshCheckRecord/{id}")
    Observable<Result2<List<ApprovalOpinion>>> getOpinion(@Path("id") String id);


    /**
     *根据输入的关键字查询门牌的id
     * keyword 输入的关键字
     */
    @POST("rest/discharge/getObjIdByKw")
    Observable<QueryIdBeanResult> queryObjectIdByKeyword(@Query("keyword") String keyword);


    /**
     *根据输入的关键字查询排水单元的id
     * keyword 输入的关键字
     */
    @POST("rest/discharge/getObjIdByKw")
    Observable<QueryIdBeanResult> queryPshUnitObjectIdByKeyword(@Query("keyword") String keyword);
}
