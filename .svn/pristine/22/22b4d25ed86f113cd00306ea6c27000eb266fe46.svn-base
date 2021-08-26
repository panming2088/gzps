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
 * 新增上报接口（上报的端口号变了，为了不影响其他接口，单独抽出）
 * Created by xucil on 2018/1/17.
 */

public interface UploadFacilityApi2 {
    /**
     * 部件的缺失（带附件）
     *
     * @return
     */
    @Multipart
    @POST("rest/parts/partsNew")
    Observable<ResponseBody> partsNew(
            @Query("id") Long markId,
            @Query("loginName") String loginName,
            @Query("markPersonId") String markPersonId,
            @Query("markPerson") String markPerson,
//            @Query("markTime") Long markTime,
            @Query("description") String description,
            @Query("x") double x,
            @Query("y") double y,
            @Query("addr") String addr,
            @Query("componentType") String componentType,
            @Query("attrOne") String attrOne,
            @Query("attrTwo") String attrTwo,
            @Query("attrThree") String attrThree,
            @Query("attrFour") String attrFour,
            @Query("attrFive") String attrFive,
            @Query("road") String road,
            @Query("layerId") int layerId,
            @Query("layerName") String layerName,
            @Query("usid") String usid,
            @Query("isBinding") int isBinding,
            @Query("attachment") String deletedPhotos,
            @Query("userX") double userX,
            @Query("userY") double userY,
            @Query("userAddr")String userAddr,
            @Query("pcode") String pCode,
            @Query("childCode") String childCodes,
            @Query("cityVillage") String uploadPlace,
            @Query("attrSix") String attrSix,
            @Query("attrSeven") String attrSeven,
            @Query("riverx") double riverx,
            @Query("rivery") double rivery,
            @Query("gpbh") String gpbh,
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
     * 部件的缺失（不带附件）
     *
     * @return
     */
    @POST("rest/parts/partsNew")
    Observable<ResponseBody> partsNew(
            @Query("id") Long markId,
            @Query("loginName") String loginName,
            @Query("markPersonId") String markPersonId,
            @Query("markPerson") String markPerson,
//            @Query("markTime") Long markTime,
            @Query("description") String description,
            @Query("x") double x,
            @Query("y") double y,
            @Query("addr") String addr,
            @Query("componentType") String componentType,
            @Query("attrOne") String attrOne,
            @Query("attrTwo") String attrTwo,
            @Query("attrThree") String attrThree,
            @Query("attrFour") String attrFour,
            @Query("attrFive") String attrFive,
            @Query("road") String road,
            @Query("layerId") int layerId,
            @Query("layerName") String layerName,
            @Query("usid") String usid,
            @Query("isBinding") int isBinding,
            @Query("userX") double userX,
            @Query("userY") double userY,
            @Query("userAddr")String userAddr,
            @Query("attachment") String deletedPhotos,
            @Query("pcode") String pCode,
            @Query("childCode") String childCodes,
            @Query("cityVillage") String uploadPlace,
            @Query("attrSix") String attrSix,
            @Query("attrSeven") String attrSeven,
            @Query("riverx") double riverx,
            @Query("rivery") double rivery,
            @Query("gpbh") String gpbh,
            @Query("sfCzwscl") String sfCzwscl,
            @Query("sfgjjd") String sfgjjd,//是否关键节点
            @Query("gjjdBh") String gjjdBh,//节点编号
            @Query("gjjdZrr") String gjjdZrr,//节点负责人
            @Query("gjjdLxdh") String gjjdLxdh,//联系电话
            @Query("yjbh") String yjbh,//窨井编号
            @Query("sfpsdyhxn") String sfpsdyhxn,//属于排水单元红线内
            @Query("mpBeen") String mpBeen,
            @Query("deletempBeen") String deletempBeen);
}
