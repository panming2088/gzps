package com.augurit.agmobile.gzps.uploadfacility.dao;

import java.util.List;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.dao
 * @createTime 创建时间 ：18/1/25
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：18/1/25
 * @modifyMemo 修改备注：
 */

public interface ISearchRoadApi {

    @POST
    Observable<List<String>> getRoads(@Query("road") String key, @Query("loginName")String loginName);
}
