package com.augurit.agmobile.patrolcore.common.table.dao.remote.api;

import java.util.List;
import java.util.Map;


import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 描述：在线模糊查询API
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.table.dao.remote.api
 * @createTime 创建时间 ：2017/7/3
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/7/3
 * @modifyMemo 修改备注：
 */

public interface FuzzySearchApi {

    @GET("")
    Observable<List<Map<String,String>>> fuzzySearch(@Url String url, @Query("keyWords") String keyWords,
                                                     @QueryMap Map<String, String> params);
}

