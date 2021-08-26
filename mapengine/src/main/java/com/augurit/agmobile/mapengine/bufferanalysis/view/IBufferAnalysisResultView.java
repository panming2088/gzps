package com.augurit.agmobile.mapengine.bufferanalysis.view;

import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.am.cmpt.common.Callback1;
import com.esri.core.tasks.identify.IdentifyResult;

import java.util.Map;

/**
 * 缓冲分析结果View的接口
 * @author 创建人 ：liangshenghong
 * @package 包名 ：com.augurit.agmobile.mapengine.bufferanalysis.view
 * @createTime 创建时间 ：2017-02-07
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-02-07
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public interface IBufferAnalysisResultView {
    /**
     * 显示根据图层名进行分组后的分析结果
     * @param identifyResultMap
     * @param container
     */
    void show(Map<String, AMFindResult[]> identifyResultMap, ViewGroup container);

    /**
     * 高亮选中的项
     * @param selectItemPosition 需高亮的项在列表中的位置
     */
    void setSelectItem(int selectItemPosition);

    /**
     * 消除高亮状态
     */
    void clearSelectedItem();

    /**
     * 设置更改显示的图层时的监听
     * @param onSelectedLayerListener 监听回调
     */
    void setOnSelectedLayerListener(Callback1 onSelectedLayerListener);

    /**
     * 设置结果列表项的点击监听
     * @param onRecyclerItemClickListener 监听回调
     */
    void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener);
}
