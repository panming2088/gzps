package com.augurit.agmobile.gzps.setting.dao;

import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.ServerAttachment;
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
 * 设施纠错API
 * Created by xcl on 2017/11/15.
 */
public interface MyCorrectFacilityApi {

    /**
     * 提交设施纠错（带附件）
     *
     * @return
     */
    @Multipart
    @POST("rest/parts/partsCorr")
    Observable<ResponseBody> partsAdd(
            @Query("id") Long id,
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
            @Query("orginX") double x,
            @Query("orginY") double y,
            @Query("originAddr") String addr,
            @Query("addr") String correctAddress,
            @Query("x") double coox,
            @Query("y") double cooy,
            @Query("correctType") String correctType,
            @Query("layerUrl") String layerUrl,
            @Query("road") String road,
            @Query("reportType") String reportType,
            @Query("userX") double userX,
            @Query("userY") double userY,
            @Query("userAddr") String userAddr,
            @Query("originRoad") String originRoad,
            @Query("originAttrOne") String originAttrOne,
            @Query("originAttrTwo") String originAttrTwo,
            @Query("originAttrThree") String originAttrThree,
            @Query("originAttrFour") String originAttrFour,
            @Query("originAttrFive") String originAttrFive,
            @Query("attachment") String attachment,
            @PartMap() HashMap<String, RequestBody> requestBody);



    /**
     * 提交设施纠错（不带附件）
     *
     * @return
     */
    @POST("rest/parts/partsCorr")
    Observable<ResponseBody> partsAdd(
            @Query("id") Long id,
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
            @Query("orginX") double x,
            @Query("orginY") double y,
            @Query("originAddr") String addr,
            @Query("addr") String correctAddress,
            @Query("x") double coox,
            @Query("y") double cooy,
            @Query("correctType") String correctType,
            @Query("layerUrl") String layerUrl,
            @Query("road") String road,
            @Query("reportType") String reportType,
            @Query("userX") double userX,
            @Query("userY") double userY,
            @Query("userAddr") String userAddr,
            @Query("originRoad") String originRoad,
            @Query("originAttrOne") String originAttrOne,
            @Query("originAttrTwo") String originAttrTwo,
            @Query("originAttrThree") String originAttrThree,
            @Query("originAttrFour") String originAttrFour,
            @Query("originAttrFive") String originAttrFive,
            @Query("attachment") String attachment

    );

    /**
     * 获取设施纠错列表
     * @param pageNo
     * @param pageSize
     * @param userName
     * @return
     */
    @POST("rest/parts/getPartsCorr")
    Observable<Result3<List<ModifiedFacility>>> getPartsCorr(@Query("pageNo") int pageNo,
                                                             @Query("pageSize") int pageSize,
                                                             @Query("markPersonId") String userName,
                                                             @Query("checkState") String checkState,
                                                             @Query("startTime") Long startTime,
                                                             @Query("endTime") Long endTime,
                                                             @Query("attrFive") String attrFive,
                                                             @Query("road") String road,
                                                             @Query("addr") String addr,
                                                             @Query("layerName") String layerName,
                                                             @Query("objectId") String markId);

    /**
     * 获取附件列表接口
     * @param markId 部件标识id
     * @return
     */
    @POST("rest/parts/getPartsCorrAttach")
    Observable<ServerAttachment> getPartsCorrAttach(@Query("markId") Long markId);


}
