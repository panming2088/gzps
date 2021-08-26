package com.augurit.agmobile.gzps.doorno.dao;


import com.augurit.agmobile.gzps.doorno.model.PSHAffairDetail;
import com.augurit.agmobile.gzps.doorno.model.PSHEventBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * com.augurit.agmobile.gzpssb.pshpublicaffair.dao
 * Created by sdb on 2018/4/11  19:20.
 * Desc：
 */

public interface PSHAffairApi {

    @FormUrlEncoded
    @POST("rest/discharge/searchCollectAll")
    Observable<PSHEventBean> getPSHAffairList(@Field("pageNo") int page,
                                              @Field("pageSize") int pageSize,
                                              @Field("parentOrgName") String parentOrgName,
                                              @Field("dischargerType1") String bigtype,
                                              @Field("dischargerType2") String smalltype,
                                              @Field("startTime") Long startTime,
                                              @Field("endTime") Long endTime,
                                              @Field("loginName") String loginName);

    @POST("rest/discharge/toCollectView/{id}")
    Observable<PSHAffairDetail> getPSHAffairDetail(@Path("id") int affairId);

    @FormUrlEncoded
    @POST("rest/discharge/toCollectViewByUnitId")
    Observable<PSHAffairDetail> getPSHUnitDetail(@Field("unitId") int unitId, @Field("loginName") String loginName);

}
