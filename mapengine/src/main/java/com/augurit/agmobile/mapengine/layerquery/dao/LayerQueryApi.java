package com.augurit.agmobile.mapengine.layerquery.dao;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerquery.dao
 * @createTime 创建时间 ：2017-03-02
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-02
 * @modifyMemo 修改备注：
 */

public interface LayerQueryApi {
    /*************************获取可查询图层列表******************************/
    @GET("rest/system/getQueryLayerInfo/{username}/{projectid}")
    Call<ResponseBody> getQueryableLayers(@Path("username") String username, @Path("projectid") String projectId);

}
