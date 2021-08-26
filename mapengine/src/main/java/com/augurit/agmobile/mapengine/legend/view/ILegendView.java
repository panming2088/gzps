package com.augurit.agmobile.mapengine.legend.view;

import com.augurit.agmobile.mapengine.legend.model.LayerLegend;
import com.augurit.agmobile.mapengine.legend.model.Legend;
import com.augurit.am.fw.common.IView;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.legend.view
 * @createTime 创建时间 ：17/7/19
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/19
 * @modifyMemo 修改备注：
 */

public interface ILegendView extends IView {

    void showLegends(List<Legend> legends);

    /**
     * 通过地图服务获取图例，显示图例信息
     * @param legends
     */
    void showLayerLegends(List<LayerLegend> legends);

    void showLoadingLegend();

    void hideLoadingLegend();

    void showLoadingLegendError(String message);

    void showToast(String message);

    void clearData();
}
