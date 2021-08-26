package com.augurit.agmobile.mapengine.statistics.dao;

import com.augurit.agmobile.mapengine.statistics.model.StatisticsParam;
import com.esri.core.map.FeatureResult;
import com.esri.core.tasks.query.OutStatistics;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 描述：服务端统计数据访问
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.statistics.dao
 * @createTime 创建时间 ：2017-02-07
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-07
 * @modifyMemo 修改备注：
 */
public class RemoteStatisticsDao {

    public List<Map<String, Object>> statistics(StatisticsParam statisticsParam) throws Exception {
        QueryParameters qParameters = new QueryParameters();
        qParameters.setGeometry(statisticsParam.getGeometry());
        qParameters.setOutSpatialReference(statisticsParam.getSpatialReference());
        qParameters.setReturnGeometry(false);
        qParameters.setGroupByFieldsForStatistics(new String[]{statisticsParam.getGroupField()});
        qParameters.setOutStatistics(new OutStatistics[]{new OutStatistics(statisticsParam.getStatsType(), statisticsParam.getStatsField(), "AA")});

        QueryTask qTask = new QueryTask(statisticsParam.getLayerUrl());
        FeatureResult featureResult = qTask.execute(qParameters);
        List<Map<String, Object>> result = new ArrayList<>();
        Iterator iterator = featureResult.iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof Map) {
                Map<String, Object> element = (Map<String, Object>) o;
                result.add(element);
            }
        }
        return result;
    }
}
