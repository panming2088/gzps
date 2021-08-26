package com.augurit.agmobile.patrolcore.layer.dao;



import com.augurit.agmobile.patrolcore.layer.model.GetDirLayersResult;
import com.augurit.agmobile.patrolcore.layer.model.GetLayerInfoResult;
import com.augurit.agmobile.patrolcore.layer.model.LayerList;
import com.augurit.agmobile.patrolcore.layer.model.NewBaseMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 地图接口
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.map.dao
 * @createTime 创建时间 ：2017-03-03
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-03
 * @modifyMemo 修改备注：
 */

public interface LayerApi {


//    /**
//     * 获取系统底图
//     *
//     * @param userId 用户ID
//     * @return
//     */
//    @GET("rest/system/getBaseLayer/{userId}")
//    Call<GetBaseMapResult> getBaseMap(@Path("userId") String userId);

    /**
     * 获取目录图层结构
     *
     * @return
     */
    @GET("rest/system/getDirLayers/{userId}")
    Call<GetDirLayersResult> getDirLayers(@Path("userId") String userId);

    //这个接口返回的图层，第0个图层是最晚加载的，处于地图顶层；返回的最后一个图层是最先加载的，也就是底图；
    @GET("rest/dirService/layerList")
    Observable<LayerList> getLayerList(@Query("userId") String userId);

    @GET("rest/system/getLayerInfo/{layerId}")
    Call<GetLayerInfoResult> getLayerInfo(@Path("layerId") String layerId);


    /**
     * 林峰新写的图层接口
     * @return
     */
  //  @GET("rest/system/getBaseLayer/{userId}")
  //  Call<NewBaseMap> getBaseMapFromNewInterface(@Path("userId") String userId);
    @GET()
    Call<NewBaseMap> getBaseMapFromNewInterface(@Url String url);

    @GET()
    Call<ResponseBody> parseLocation(@Url String url);
}
