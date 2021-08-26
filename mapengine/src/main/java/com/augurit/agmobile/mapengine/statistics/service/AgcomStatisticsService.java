package com.augurit.agmobile.mapengine.statistics.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.dao.RemoteAgcomRestDao;
import com.augurit.agmobile.mapengine.common.model.FieldStats;
import com.augurit.agmobile.mapengine.common.utils.wktutil.WktUtil;
import com.augurit.agmobile.mapengine.statistics.model.StatisticsParam;
import com.augurit.agmobile.mapengine.statistics.model.StatisticsResult;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 调用AGCOM接口进行统计
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.statistics.service
 * @createTime 创建时间 ：2017-02-23
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-02-23
 * @modifyMemo 修改备注：
 */
public class AgcomStatisticsService implements IStatisticsService {

    private Context mContext;
    private RemoteAgcomRestDao remoteAgcomRestDao;

    public AgcomStatisticsService(Context context){
        this.mContext = context;
        remoteAgcomRestDao = new RemoteAgcomRestDao(mContext);
    }

    @Override
    public Observable<List<StatisticsResult>> statistics(final StatisticsParam statisticsParam) {
        return Observable.create(new Observable.OnSubscribe<List<StatisticsResult>>() {
            @Override
            public void call(Subscriber<? super List<StatisticsResult>> subscriber) {
                String wkt = WktUtil.getWKTFromGeometry(statisticsParam.getSpatialReference(), statisticsParam.getGeometry());
                String projectLayerId = remoteAgcomRestDao.getPorjectLayerBaseIdByLayerId(statisticsParam.getLayerId());
                List<FieldStats> fieldStats = remoteAgcomRestDao.statistics(projectLayerId, wkt, statisticsParam.getStatsField(),
                        statisticsParam.getGroupField(), statisticsParam.getStatsType().name(),
                        statisticsParam.getUserId(), statisticsParam.getProjectId());
                subscriber.onNext(convert(fieldStats));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()); // 指定 subscribe() 发生在 IO 线程
    }

    private List<StatisticsResult> convert(List<FieldStats> fieldStatss){
        List<StatisticsResult> statisticsResults = new ArrayList<>();
        for(FieldStats fieldStats : fieldStatss){
            StatisticsResult statisticsResult = new StatisticsResult();
            statisticsResult.setGroup(fieldStats.getStatsField());
            statisticsResult.setValue(Double.parseDouble(fieldStats.getFieldCount()));
            statisticsResults.add(statisticsResult);
        }
        return statisticsResults;
    }
}
