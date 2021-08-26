package com.augurit.agmobile.mapengine.bufferanalysis.view.presenter;

import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layerimport.model.Document;
import com.augurit.am.cmpt.common.Callback1;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Feature;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.identify.IdentifyResult;

import java.util.Map;

/**
 * 缓冲分析中Presenter的接口
 * @author 创建人 ：liangshenghong
 * @package 包名 ：com.augurit.agmobile.mapengine.bufferanalysis.view.presenter
 * @createTime 创建时间 ：2017-01-24
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-01-24
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public interface IBufferAnalysisPresenter {

    /**
     * 导入红线（shape文件）时选中文件的回调
     * @param doc 选中的文件
     */
    void onShpPick(Document doc);

    /**
     * 导入shape文件后，用户点击shape图层上的要素回调
     * @param feature
     * @param clickPoint
     */
    void clickFeature(Feature feature, Point clickPoint);

    /**
     * 用户触发导入shape文件事件
     */
    void clickImportShpBtn();

    /**
     * 进入缓冲分析功能
     * @param topBarContainer 顶部栏容器
     * @param toolContainer 工具栏容器
     * @param bottomSheetContainer 图层列表及分析结果展示容器
     * @param candidateContainer 结果详情容器
     */
    void startBufferAnalysing(ViewGroup topBarContainer, final ViewGroup toolContainer, ViewGroup bottomSheetContainer, ViewGroup candidateContainer);

    /**
     * 显示分析结果列表
     * @param identifyResultMap 以图层名分组的分析结果
     */
    void showAnalysingResultView(Map<String, AMFindResult[]> identifyResultMap);

    /**
     * 从分析结果界面返回图层列表界面
     */
    void backFromResultView();

    /**
     * 设置缓冲半径，并生成缓冲区
     * @param radius
     */
    void setBufferRadius(int radius);

    /**
     * 执行缓冲分析操作
     */
    void analysis();

    /**
     * 移除shape图层
     */
    void removeImportShpLayer();

    /**
     * 清空地图数据
     */
    void clearMapView();

    /**
     * 缓冲分析状态重置
     */
    void reset();

    /**
     * 设置点击分析结果列表项的监听器
     * @param onResultClickListener
     */
    void setOnResultClickListener(View.OnClickListener onResultClickListener);

    /**
     * 退出功能后的回调，以恢复主界面状态
     * @param callback
     */
    void setBackListener(Callback1 callback);

    /**
     * 退出缓冲分析功能，通常在此方法中进行资源释放
     */
    void onClose();

    /**
     * 返回主界面
     */
    void backToMain();
}
