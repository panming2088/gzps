package com.augurit.agmobile.gzps.uploadfacility.dao;

import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.Result;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.uploadfacility.model.CheckResult;
import com.augurit.agmobile.gzps.uploadfacility.model.CheckUploadRecordResult;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.PsdyJbj;

import java.util.List;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 设施纠错API
 * Created by xcl on 2017/11/15.
 */
public interface CorrectFacilityApi {

    /**
     * 获取设施纠错列表
     * @param pageNo
     * @param pageSize
     * @param userName
     * @return
     */
    @POST("rest/parts/getPartsCorr")
    Observable<Result2<List<ModifiedFacility>>> getPartsCorr(@Query("pageNo") int pageNo,
                                                             @Query("pageSize") int pageSize,
                                                             @Query("markPersonId")String userName,
                                                             @Query("checkState")String checkState);

    /**
     * 获取附件列表接口
     * @param markId 部件标识id
     * @return
     */
    @POST("rest/parts/getPartsCorrAttach")
    Observable<ServerAttachment> getPartsCorrAttach(@Query("markId") Long markId);

    /**
     * 该设施是否已经被同区域的人报过
     * @return
     */
    @POST("rest/parts/checkRecord")
    Observable<Result2<CheckUploadRecordResult>> checkIfTheFacilityHasUploadedBefore(@Query("loginName") String loginName,
                                                                                     @Query("usid") String usid);


    /**
     * 该设施是否已经被巡查员报过
     * @return
     */
    @POST("rest/parts/getGwyxtLastRecord")
    Observable<CheckResult> getGwyxtLastRecord(@Query("objectId") String objectId,
                                               @Query("type") int type);

    /**
     *挂接排水单元
     * @return
     */
    @POST("rest/psdyJbjRest/addPsdyJbj")
    Observable<ResponseBody> addPsdyJbj(@Query("data")String dataJson);
    /**
     *删除接驳井与排水单元连线
     * @return
     */
    @POST("rest/psdyJbjRest/deletePsdyJbj")
    Observable<ResponseBody> deletePsdyJbj(@Query("loginName")String loginName,@Query("id")String id);
    /**
     *查询排水单元连线
     * usid=""&jbjObjectId=""&psdyObjectId=usid,jbjObjectId,psdyObjectId其中一个
     * @return
     */
    @POST("rest/psdyJbjRest/getPsdyJbj")
    Observable<Result2<List<PsdyJbj>>> queryPsdyJbj(@Query("usid")String usid, @Query("jbjObjectId")String jbjObjectId, @Query("psdyObjectId")String psdyObjectId);
    /**
     *查询排水单元连线
     * usid=""&jbjObjectId=""&psdyObjectId=usid,jbjObjectId,psdyObjectId其中一个
     * @return
     */
    @POST("rest/psdyJbjRest/getPsdyJbj")
    Observable<Result2<List<PsdyJbj>>> queryPsdyKeyNodeInspectionWell(@Query("usid")String usid, @Query("jbjObjectId")String jbjObjectId, @Query("psdyObjectId")String psdyObjectId, @Query("sfgjjd")String sfgjjd);


}
