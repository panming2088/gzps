package com.augurit.agmobile.mapengine.statistics.view.presenter;

import android.view.View;
import android.view.ViewGroup;

import com.augurit.am.cmpt.common.Callback1;
import com.esri.core.geometry.Geometry;

/**
 * 描述：地图统计范围选择Presenter接口
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.statistics.view.presenter
 * @createTime 创建时间 ：2017-02-07
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-07
 * @modifyMemo 修改备注：
 */
public interface IStatisticsPresenter {

    /**
     * 显示统计界面
     * @param topBarContainer 顶栏视图容器
     * @param toolContainer 工具视图容器
     */
    void startStatistics(ViewGroup topBarContainer, final ViewGroup toolContainer, ViewGroup areaContainer);

    /**
     * 功能关闭
     */
    void onClose();

    /**
     * 点击导入按钮
     */
    void clickImportShpBtn();

    /**
     * 显示行政区域列表
     * @param view
     */
    void showAreaPopup(View view);

    /**
     * 设置缓冲范围
     * @param radius
     */
    void setBufferRadius(int radius);

    /**
     * 显示统计条件视图
     */
    void showConditionView();

    /**
     * 清除圈选区域
     */
    void clear();

    /**
     * 获取当前圈选范围
     * @return 当前圈选范围
     */
    Geometry getGeometry();

    /**
     * 设置返回监听
     * @param backListener 返回监听
     */
    void setBackListener(Callback1 backListener);
}
