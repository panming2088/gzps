package com.augurit.agmobile.gzps.statistic.dao;

import android.content.Context;

import com.augurit.agmobile.gzps.statistic.model.StatisticResult;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.am.fw.net.AMNetwork;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.statistic.dao
 * @createTime 创建时间 ：2017/8/17
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/8/17
 * @modifyMemo 修改备注：
 */

public class StatisticDao {

    protected AMNetwork amNetwork;
    protected StatisticApi mApi;

    public StatisticDao(Context context, String serverUrl) {
        amNetwork = new AMNetwork(serverUrl);
        amNetwork.addLogPrint();
        amNetwork.addRxjavaConverterFactory();
        amNetwork.build();
        amNetwork.registerApi(StatisticApi.class);
        mApi = (StatisticApi) amNetwork.getServiceApi(StatisticApi.class);
    }

    public Observable<List<TableItem>> getStatisticFields(String url, String projectId) {
        return mApi.getStatisticFields(url, projectId);
    }

    public Observable<List<TableItem>> getGroupFields(String url, String projectId) {
        return mApi.getStatisticFields(url, projectId);
    }

    public Observable<List<StatisticResult>> statistic(@Url String url, @Field("conditions") String conditions) {
        return mApi.statistic(url, conditions);
    }
}
