package com.augurit.agmobile.mapengine.statistics.view;

import android.view.View;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;

import java.util.List;

/**
 * 描述：地图统计条件选择视图接口
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.statistics.view
 * @createTime 创建时间 ：2017-02-07
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-07
 * @modifyMemo 修改备注：
 */
public interface IStatisticsConditionView {

    /**
     * 初始化图层列表
     * @param layers 图层信息
     */
    void initLayers(List<LayerInfo> layers);

    /**
     * 获取View
     * @return View
     */
    View getConditionView();
}
