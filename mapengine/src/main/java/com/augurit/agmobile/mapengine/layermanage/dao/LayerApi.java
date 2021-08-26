package com.augurit.agmobile.mapengine.layermanage.dao;

import com.augurit.agmobile.mapengine.layermanage.model.AgcomLayerInfo;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layermanage.dao
 * @createTime 创建时间 ：2017-04-17
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-17
 * @modifyMemo 修改备注：
 */

public interface LayerApi {

    @GET("rest/agmobilelayer/getLayerInfos/{projectId}/{userId}")
    Observable<AgcomLayerInfo> getLayerList(@Path("projectId") String projectId,@Path("userId") String userId);


    @GET("rest/system/getLayerInfos/{projectId}/{userId}")
    Observable<AgcomLayerInfo> getLayerListByRestSystem(@Path("projectId") String projectId,@Path("userId") String userId);
}
