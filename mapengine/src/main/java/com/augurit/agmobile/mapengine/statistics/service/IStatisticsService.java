package com.augurit.agmobile.mapengine.statistics.service;

import com.augurit.agmobile.mapengine.statistics.model.StatisticsParam;
import com.augurit.agmobile.mapengine.statistics.model.StatisticsResult;
import com.augurit.am.cmpt.common.Callback2;
import com.esri.core.geometry.Geometry;
import com.esri.core.tasks.query.OutStatistics;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * 描述：地图统计Service接口
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.statistics.service
 * @createTime 创建时间 ：2017-02-07
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-07
 * @modifyMemo 修改备注：
 */
public interface IStatisticsService {

    /**
     * 统计，返回Observable
     * @param statisticsParam 统计参数
     * @return Observable
     */
    Observable<List<StatisticsResult>> statistics(StatisticsParam statisticsParam);
}
