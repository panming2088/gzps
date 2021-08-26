package com.augurit.agmobile.mapengine.bufferanalysis.view;

import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;

import java.util.List;

/**
 * 缓冲分析条件选择View的接口
 * @author 创建人 ：liangshenghong
 * @package 包名 ：com.augurit.agmobile.mapengine.bufferanalysis.view
 * @createTime 创建时间 ：2017-02-07
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-02-07
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public interface IBufferAnalysisConditionView {

    /**
     * 显示缓冲分析的条件选择View
     * @param container
     */
    void show(ViewGroup container);

    /**
     * 显示图层列表
     * @param layers 待显示图层
     */
    void initLayers(List<LayerInfo> layers);

    /**
     * 获取当前选中的图层
     * @return 选中图层
     */
    List<LayerInfo> getBufferLayers();
}
