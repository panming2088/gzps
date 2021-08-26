package com.augurit.agmobile.gzps.statistic.service;

import android.content.Context;

import com.augurit.agmobile.gzps.statistic.dao.StatisticDao;
import com.augurit.agmobile.gzps.statistic.model.StatisticResult;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.am.cmpt.common.base.BaseInfoManager;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.statistic.service
 * @createTime 创建时间 ：2017/8/15
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/8/15
 * @modifyMemo 修改备注：
 */
@Deprecated
public class StatisticService {

    protected StatisticDao mDao;

    public StatisticService(Context context) {
        String serverUrl = BaseInfoManager.getBaseServerUrl(context);
        mDao = new StatisticDao(context, serverUrl);
    }

    public Observable<List<TableItem>> getStatisticFields(String url, String projectId) {
        return mDao.getStatisticFields(url, projectId);
    }

    public Observable<List<TableItem>> getGroupFields(String url, String projectId) {
        return mDao.getStatisticFields(url, projectId);
    }

    public Observable<List<StatisticResult>> statistic(@Url String url, @Field("params") String conditions) {
        return mDao.statistic(url, conditions);
    }
}
