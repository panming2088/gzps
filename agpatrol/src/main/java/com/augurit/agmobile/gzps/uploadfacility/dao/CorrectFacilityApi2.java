package com.augurit.agmobile.gzps.uploadfacility.dao;

import com.augurit.agmobile.gzps.common.model.ResponseBody;

import java.util.HashMap;

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

public interface CorrectFacilityApi2 {
    /**
     * 提交设施纠错（带附件）
     *
     * @return
     */
    @Multipart
    @POST("rest/parts/partsCorr")
    Observable<ResponseBody> partsAdd(
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
            @Query("orginX") double x,
            @Query("orginY") double y,
            @Query("originAddr")String addr,
            @Query("addr")String correctAddress,
            @Query("x") double coox,
            @Query("y")double cooy,
            @Query("correctType")String correctType,
            @Query("layerUrl")String layerUrl,
            @Query("road")String road,
            @Query("reportType")String reportType,
            @Query("userX") double userX,
            @Query("userY") double userY,
            @Query("userAddr")String userAddr,
            @Query("originRoad")String originRoad,
            @Query("originAttrOne")String originAttrOne,
            @Query("originAttrTwo") String originAttrTwo,
            @Query("originAttrThree")String originAttrThree,
            @Query("originAttrFour")String originAttrFour,
            @Query("originAttrFive")String originAttrFive,
            @Query("attachment") String attachment,
            @Query("pcode") String pCode,
            @Query("childCode") String childCodes,
            @Query("cityVillage") String uploadPlace,
            @Query("attrSix") String attrSix,
            @Query("attrSeven") String attrSeven,
            @Query("riverx") double riverx,
            @Query("rivery") double rivery,
            @Query("sfCzwscl") String sfCzwscl,
            @Query("mpBeen") String mpBeen,
            @Query("deletempBeen") String deletempBeen,
            @Query("sfgjjd") String sfgjjd,//是否关键节点
            @Query("gjjdBh") String gjjdBh,//节点编号
            @Query("gjjdZrr") String gjjdZrr,//节点负责人
            @Query("gjjdLxdh") String gjjdLxdh,//联系电话
            @Query("yjbh") String yjbh,//窨井编号
            @Query("sfpsdyhxn") String sfpsdyhxn,//属于排水单元红线内
            @PartMap() HashMap<String, RequestBody> requestBody);



    /**
     * 提交设施纠错（不带附件）
     *
     * @return
     */
    @POST("rest/parts/partsCorr")
    Observable<ResponseBody> partsAdd(
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
            @Query("orginX") double x,
            @Query("orginY") double y,
            @Query("originAddr")String addr,
            @Query("addr")String correctAddress,
            @Query("x") double coox,
            @Query("y")double cooy,
            @Query("correctType")String correctType,
            @Query("layerUrl")String layerUrl,
            @Query("road")String road,
            @Query("reportType")String reportType,
            @Query("userX") double userX,
            @Query("userY") double userY,
            @Query("userAddr")String userAddr,
            @Query("originRoad")String originRoad,
            @Query("originAttrOne")String originAttrOne,
            @Query("originAttrTwo") String originAttrTwo,
            @Query("originAttrThree")String originAttrThree,
            @Query("originAttrFour")String originAttrFour,
            @Query("originAttrFive")String originAttrFive,
            @Query("attachment") String attachment,
            @Query("pcode") String pCode,
            @Query("childCode") String childCodes,
            @Query("cityVillage") String uploadPlace,
            @Query("attrSix") String attrSix,
            @Query("attrSeven") String attrSeven,
            @Query("riverx") double riverx,
            @Query("rivery") double rivery,
            @Query("sfCzwscl") String sfCzwscl,
            @Query("mpBeen") String mpBeen,
            @Query("deletempBeen") String deletempBeen,
            @Query("sfgjjd") String sfgjjd,//是否关键节点
            @Query("gjjdBh") String gjjdBh,//节点编号
            @Query("gjjdZrr") String gjjdZrr,//节点负责人
            @Query("gjjdLxdh") String gjjdLxdh,//联系电话
            @Query("yjbh") String yjbh,//窨井编号
            @Query("sfpsdyhxn") String sfpsdyhxn//属于排水单元红线内
    );

}
