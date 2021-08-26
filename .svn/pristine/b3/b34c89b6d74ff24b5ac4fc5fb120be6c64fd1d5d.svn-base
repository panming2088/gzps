package com.augurit.agmobile.gzps.journal.dao;

import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.publicaffair.model.ModifyFacilityDetail;
import com.augurit.agmobile.gzps.setting.view.myupload.Result3;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 校核上报接口（上报的端口号变了，为了不影响其他接口，单独抽出）
 * Created by xucil on 2018/1/17.
 */

public interface JournalApi2 {
    /**
     * 提交设施纠错（带附件）
     *
     * @return
     */
    @Multipart
    @POST("rest/parts/diayrNew")
    Observable<ResponseBody> diayrNew(
            @Query("id") Long id,
            @Query("objectId") String objectId,
            @Query("loginName") String loginName,
            @Query("markPersonId") String markPersonId,
            @Query("markPerson") String markPerson,
//            @Query("markTime") Long markTime,
            @Query("description") String description,
            @Query("layerName") String layerName,
            @Query("usid") String usid,
            @Query("attrOne") String attrOne,
            @Query("attrTwo") String attrTwo,
            @Query("attrThree") String attrThree,
            @Query("attrFour") String attrFour,
            @Query("attrFive") String attrFive,
            @Query("orginx") double x,
            @Query("orginy") double y,
            @Query("originAddr") String addr,
            @Query("addr") String correctAddress,
            @Query("x") double coox,
            @Query("y") double cooy,
            @Query("correctType") String correctType,
            @Query("layerUrl") String layerUrl,
            @Query("road") String road,
            @Query("reportType") String reportType,
            @Query("userx") double userX,
            @Query("usery") double userY,
            @Query("userAddr") String userAddr,
            @Query("originRoad") String originRoad,
            @Query("originAttrOne") String originAttrOne,
            @Query("originAttrTwo") String originAttrTwo,
            @Query("originAttrThree") String originAttrThree,
            @Query("originAttrFour") String originAttrFour,
            @Query("originAttrFive") String originAttrFive,
            @Query("originAttrSix") String originAttrSix,
            @Query("originAttrSeven") String originAttrSeven,
            @Query("attachment") String attachment,
            @Query("pcode") String pCode,
            @Query("childCode") String childCodes,
            @Query("cityVillage") String uploadPlace,
            @Query("teamMember") String teamMember,
            @Query("tqzq") String tqzq,
            @Query("pskpszt") String pskpszt,
            @Query("gpbh") String gpbh,
            @Query("clff") String clff,
            @Query("cljg") String cljg,
            @Query("ph") String ph,
            @Query("adnd") String adnd,
            @Query("wellLength") double wellLength,
            @Query("trackId") Long trackId ,
            @PartMap() HashMap<String, RequestBody> requestBody);


    /**
     * 提交设施纠错（不带附件）
     *
     * @return
     */
    @POST("rest/parts/diayrNew")
    Observable<ResponseBody> diayrNew(
            @Query("id") Long id,
            @Query("objectId") String objectId,
            @Query("loginName") String loginName,
            @Query("markPersonId") String markPersonId,
            @Query("markPerson") String markPerson,
//            @Query("markTime") Long markTime,
            @Query("description") String description,
            @Query("layerName") String layerName,
            @Query("usid") String usid,
            @Query("attrOne") String attrOne,
            @Query("attrTwo") String attrTwo,
            @Query("attrThree") String attrThree,
            @Query("attrFour") String attrFour,
            @Query("attrFive") String attrFive,
            @Query("orginx") double x,
            @Query("orginy") double y,
            @Query("originAddr") String addr,
            @Query("addr") String correctAddress,
            @Query("x") double coox,
            @Query("y") double cooy,
            @Query("correctType") String correctType,
            @Query("layerUrl") String layerUrl,
            @Query("road") String road,
            @Query("reportType") String reportType,
            @Query("userx") double userX,
            @Query("usery") double userY,
            @Query("userAddr") String userAddr,
            @Query("originRoad") String originRoad,
            @Query("originAttrOne") String originAttrOne,
            @Query("originAttrTwo") String originAttrTwo,
            @Query("originAttrThree") String originAttrThree,
            @Query("originAttrFour") String originAttrFour,
            @Query("originAttrFive") String originAttrFive,
            @Query("originAttrSix") String originAttrSix,
            @Query("originAttrSeven") String originAttrSeven,
            @Query("attachment") String attachment,
            @Query("pcode") String pCode,
            @Query("childCode") String childCodes,
            @Query("cityVillage") String uploadPlace,
            @Query("teamMember") String teamMember,
            @Query("tqzq") String tqzq,
            @Query("pskpszt") String pskpszt,
            @Query("gpbh") String gpbh,
            @Query("clff") String clff,
            @Query("cljg") String cljg,
            @Query("ph") String ph,
            @Query("adnd") String adnd,
            @Query("wellLength") double wellLength,
            @Query("trackId") Long trackId

    );

    /**
     * 再次编辑（带附件）
     *
     * @return
     */
    @Multipart
    @POST("rest/parts/toEditDiary")
    Observable<ResponseBody> toEditDiary(
            @Query("id") Long id,
            @Query("objectId") String objectId,
            @Query("loginName") String loginName,
            @Query("markPersonId") String markPersonId,
            @Query("markPerson") String markPerson,
//            @Query("markTime") Long markTime,
            @Query("description") String description,
            @Query("layerName") String layerName,
            @Query("usid") String usid,
            @Query("attrOne") String attrOne,
            @Query("attrTwo") String attrTwo,
            @Query("attrThree") String attrThree,
            @Query("attrFour") String attrFour,
            @Query("attrFive") String attrFive,
            @Query("orginx") double x,
            @Query("orginy") double y,
            @Query("originAddr") String addr,
            @Query("addr") String correctAddress,
            @Query("x") double coox,
            @Query("y") double cooy,
            @Query("correctType") String correctType,
            @Query("layerUrl") String layerUrl,
            @Query("road") String road,
            @Query("reportType") String reportType,
            @Query("userx") double userX,
            @Query("usery") double userY,
            @Query("userAddr") String userAddr,
            @Query("originRoad") String originRoad,
            @Query("originAttrOne") String originAttrOne,
            @Query("originAttrTwo") String originAttrTwo,
            @Query("originAttrThree") String originAttrThree,
            @Query("originAttrFour") String originAttrFour,
            @Query("originAttrFive") String originAttrFive,
            @Query("originAttrSix") String originAttrSix,
            @Query("originAttrSeven") String originAttrSeven,
            @Query("attachment") String attachment,
            @Query("pcode") String pCode,
            @Query("childCode") String childCodes,
            @Query("cityVillage") String uploadPlace,
            @Query("teamMember") String teamMember,
            @Query("tqzq") String tqzq,
            @Query("pskpszt") String pskpszt,
            @Query("gpbh") String gpbh,
            @Query("clff") String clff,
            @Query("cljg") String cljg,
            @Query("ph") String ph,
            @Query("adnd") String adnd,
            @PartMap() HashMap<String, RequestBody> requestBody);


    /**
     * 再次编辑（不带附件）
     *
     * @return
     */
    @POST("rest/parts/toEditDiary")
    Observable<ResponseBody> toEditDiary(
            @Query("id") Long id,
            @Query("objectId") String objectId,
            @Query("loginName") String loginName,
            @Query("markPersonId") String markPersonId,
            @Query("markPerson") String markPerson,
//            @Query("markTime") Long markTime,
            @Query("description") String description,
            @Query("layerName") String layerName,
            @Query("usid") String usid,
            @Query("attrOne") String attrOne,
            @Query("attrTwo") String attrTwo,
            @Query("attrThree") String attrThree,
            @Query("attrFour") String attrFour,
            @Query("attrFive") String attrFive,
            @Query("orginx") double x,
            @Query("orginy") double y,
            @Query("originAddr") String addr,
            @Query("addr") String correctAddress,
            @Query("x") double coox,
            @Query("y") double cooy,
            @Query("correctType") String correctType,
            @Query("layerUrl") String layerUrl,
            @Query("road") String road,
            @Query("reportType") String reportType,
            @Query("userx") double userX,
            @Query("usery") double userY,
            @Query("userAddr") String userAddr,
            @Query("originRoad") String originRoad,
            @Query("originAttrOne") String originAttrOne,
            @Query("originAttrTwo") String originAttrTwo,
            @Query("originAttrThree") String originAttrThree,
            @Query("originAttrFour") String originAttrFour,
            @Query("originAttrFive") String originAttrFive,
            @Query("originAttrSix") String originAttrSix,
            @Query("originAttrSeven") String originAttrSeven,
            @Query("attachment") String attachment,
            @Query("pcode") String pCode,
            @Query("childCode") String childCodes,
            @Query("cityVillage") String uploadPlace,
            @Query("teamMember") String teamMember,
            @Query("tqzq") String tqzq,
            @Query("pskpszt") String pskpszt,
            @Query("gpbh") String gpbh,
            @Query("clff") String clff,
            @Query("cljg") String cljg,
            @Query("ph") String ph,
            @Query("adnd") String adnd

    );

    /**
     * 获取巡查日志列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @POST("rest/parts/getDiayr")
    Observable<Result3<List<ModifiedFacility>>> getDiayr(@Query("pageNo") int pageNo,
                                                         @Query("pageSize") int pageSize,
                                                         @Query("loginName") String loginName,
                                                         @Query("userName") String userName,
                                                         @Query("startTime") Long startTime,
                                                         @Query("endTime") Long endTime,
                                                         @Query("attrFive") String attrFive,
                                                         @Query("road") String road,
                                                         @Query("addr") String addr,
                                                         @Query("layerName") String layerName,
                                                         @Query("objectId") String markId,
                                                         @Query("id") String id);

    /**
     * 获取附件列表接口
     *
     * @param markId 部件标识id
     * @return
     */
    @POST("rest/parts/getDiayrAttach")
    Observable<ServerAttachment> getDiayrAttach(@Query("markId") Long markId);

    /**
     * @param id
     * @param loginName
     * @param phoneBrand 手机型号
     * @return
     */
    @POST("rest/parts/deleteDiary")
    Observable<Result2<String>> deleteDiary(@Query("id") Long id,
                                            @Query("loginName") String loginName,
                                            @Query("phoneBrand") String phoneBrand);

    /**
     * 获取巡检日志详情
     *
     * @param id
     * @return
     */
    @POST("rest/parts/toDiaryView")
    Observable<ModifyFacilityDetail> toDiaryView(@Query("id") Long id);

    /**
     * 获取过滤后的巡查日志列表
     * 根据objectId
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @POST("rest/parts/getFacilityDiary")
    Observable<Result3<List<ModifiedFacility>>> getDiayList1(@Query("pageNo") int pageNo,
                                                             @Query("pageSize") int pageSize,
                                                             @Query("loginName") String loginName,
                                                             @Query("objectId") String objectId,
                                                             @Query("usid") String usid,
                                                             @Query("sfss") String sfss);














    /**
     * 获取巡查日志列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @POST("rest/parts/getFacilityDiary")
    Observable<Result3<List<ModifiedFacility>>> getDiayList(@Query("pageNo") int pageNo,
                                                            @Query("pageSize") int pageSize,
                                                            @Query("loginName") String loginName,
                                                            @Query("layerName") String facilityType,
                                                            @Query("parentOrgName") String district,
                                                            @Query("startTime") String startTime,
                                                            @Query("endTime") String endTime,
                                                            @Query("sfss") String sfss);
}
