package com.augurit.agmobile.gzps.statistic.dao;

import com.augurit.agmobile.gzps.statistic.model.StatisticResult;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.statistic.dao
 * @createTime 创建时间 ：2017/8/15
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/8/15
 * @modifyMemo 修改备注：
 */

public interface StatisticApi {

    @GET
    Observable<List<TableItem>> getStatisticFields(@Url String url, @Query("projectId") String projectId);

    @GET
    Observable<List<TableItem>> getGroupFields(@Url String url, @Query("projectId") String projectId);

    @FormUrlEncoded
    @POST
    Observable<List<StatisticResult>> statistic(@Url String url, @Field("params") String conditions);
}
