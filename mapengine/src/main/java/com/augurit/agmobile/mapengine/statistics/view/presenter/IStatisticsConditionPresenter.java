package com.augurit.agmobile.mapengine.statistics.view.presenter;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.statistics.model.StatisticsResult;
import com.augurit.agmobile.mapengine.statistics.view.IStatisticsConditionView;
import com.augurit.am.cmpt.common.Callback1;
import com.esri.core.map.Field;
import com.esri.core.tasks.query.OutStatistics;

import java.util.List;
import java.util.Map;

/**
 * 描述：地图统计条件选择Presenter接口
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.statistics.view.presenter
 * @createTime 创建时间 ：2017-02-07
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-07
 * @modifyMemo 修改备注：
 */
public interface IStatisticsConditionPresenter {

    /**
     * 开启统计<br>
     * 统计结果通过{@link OnStatisticsCompleteListener}返回<br>
     * 使用{@link #setOnStatisticsCompleteListener}方法来设置此监听
     * @param layerInfo 要统计的图层信息
     * @param field 统计字段
     * @param groupField 分组字段
     * @param statsType 统计类型
     */
    void statistics(LayerInfo layerInfo, Field field, Field groupField, OutStatistics.Type statsType);

    /**
     * 返回
     */
    void back();

    /**
     * 设置返回监听
     * @param callback
     */
    void setBackListener(Callback1 callback);

    /**
     * 设置统计条件视图
     * @param statisticsConditionView
     */
    void setStatisticsConditionView(IStatisticsConditionView statisticsConditionView);

    /**
     * 设置统计完成监听
     * @param onStatisticsCompleteListener
     */
    void setOnStatisticsCompleteListener(OnStatisticsCompleteListener onStatisticsCompleteListener);

    /**
     * 获取统计完成监听
     * @return
     */
    OnStatisticsCompleteListener getOnStatisticsCompleteListener();

    /**
     * 统计完成监听接口
     */
    interface OnStatisticsCompleteListener {
        void onStatisticsComplete(List<StatisticsResult> result);
    }
}
