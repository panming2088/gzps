package com.augurit.agmobile.patrolcore.baiduapi;

import com.augurit.agmobile.patrolcore.selectlocation.model.BaiduGeocodeResult;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.baiduapi
 * @createTime 创建时间 ：17/11/3
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/3
 * @modifyMemo 修改备注：
 */

public interface BaiduApi {

    /**
     * latest_admin = 1 表示访问最新版行政区划数据；
     * @param location
     * @param ak
     * @param mcode
     * @return
     */
    @GET("?callback=renderReverse&coordtype=wgs84ll&output=json&pois=0&latest_admin=1")
    Observable<ResponseBody> requestAddress(@Query("location") String location,
                                            @Query("ak") String ak, @Query("mcode") String mcode);
}
