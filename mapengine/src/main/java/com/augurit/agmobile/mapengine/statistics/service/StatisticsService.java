package com.augurit.agmobile.mapengine.statistics.service;

import com.augurit.agmobile.mapengine.statistics.dao.RemoteStatisticsDao;
import com.augurit.agmobile.mapengine.statistics.model.StatisticsParam;
import com.augurit.agmobile.mapengine.statistics.model.StatisticsResult;
import com.augurit.am.fw.utils.common.ValidateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 描述：默认地图统计Service
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.statistics.service
 * @createTime 创建时间 ：2017-02-07
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-07
 * @modifyMemo 修改备注：
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-02-23
 * @modifyMemo 修改备注：参数封装在实体类StatisticsParam中
 */
public class StatisticsService implements IStatisticsService {

    private RemoteStatisticsDao mRemoteDao;

    public StatisticsService() {
        mRemoteDao = new RemoteStatisticsDao();
    }

    @Override
    public Observable<List<StatisticsResult>> statistics(final StatisticsParam statisticsParam) {
        return Observable.create(new Observable.OnSubscribe<List<StatisticsResult>>() {
            @Override
            public void call(Subscriber<? super List<StatisticsResult>> subscriber) {
                try {
                    List<Map<String, Object>> result =
                            mRemoteDao.statistics(statisticsParam);
                    if (!ValidateUtil.isObjectNull(result)) {
                        subscriber.onNext(convert(result));
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public List<StatisticsResult> convert(List<Map<String, Object>> arcgisStatsResult){
        List<StatisticsResult> statisticsResults = new ArrayList<>();
        for(int i=0; i<arcgisStatsResult.size(); ++i){
            StatisticsResult statistics = new StatisticsResult();
            Map<String, Object> result = arcgisStatsResult.get(i);
            Set<String> keySet = result.keySet();
            if(keySet.size()==1){
                String key = keySet.iterator().next();
                statistics.setGroup(key);
                Object v = result.get(key);
                Double value = 0.0;
                if(v instanceof Double){
                    value = (Double)v;
                } else if(v instanceof Integer){
                    value = ((Integer) v).doubleValue();
                }
                statistics.setValue(value);
            } else {
                int j = 0;
                for(String key : keySet){
                    if(j==0){
                        Object v = result.get(key);
                        String value = " ";
                        if(v != null){
                            value = v.toString();
                        }
                        statistics.setGroup(value);
                    } else {
                        Object v = result.get(key);
                        Double value = 0.0;
                        if(v instanceof Double){
                            value = (Double)v;
                        } else if(v instanceof Integer){
                            value = ((Integer) v).doubleValue();
                        }
                        statistics.setValue(value);
                    }
                    j++;
                }
            }
            statisticsResults.add(statistics);
        }
        return statisticsResults;
    }
}
