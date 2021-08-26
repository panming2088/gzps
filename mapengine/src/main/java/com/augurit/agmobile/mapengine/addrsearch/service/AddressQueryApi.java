package com.augurit.agmobile.mapengine.addrsearch.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.addrq.service
 * @createTime 创建时间 ：2017-01-10
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-10
 */

public interface AddressQueryApi {

    /*****************************模糊搜索接口*******************************/
    @GET("search/searchKeyWord/57/{keyword}/{currentTime}")
    Call<ResponseBody> suggest(@Path("keyword") String keyword, @Path("currentTime") long currentTime);


    /*****************************地名地址搜索接口*******************************/
    @GET("search/searchAddressByAll/57/{keyword}/null/{currentTime}")
    Call<ResponseBody> find(@Path("keyword") String keyword, @Path("currentTime") long currentTime);


    @POST()
    Call<ResponseBody> getDetailedInfo(@Url String url);
}
