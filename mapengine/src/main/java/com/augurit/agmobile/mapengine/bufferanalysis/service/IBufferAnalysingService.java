package com.augurit.agmobile.mapengine.bufferanalysis.service;

import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.am.cmpt.common.Callback2;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;

import java.util.List;
import java.util.Map;

/**
 * @author 创建人 ：liangshenghong
 * @package 包名 ：com.augurit.agmobile.mapengine.bufferanalysis.service
 * @createTime 创建时间 ：2016-12-06
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2016-12-06
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public interface IBufferAnalysingService {

    /**
     * 使用identify方法进行缓冲分析，并回调分析结果
     * @param geometry 待分析地图范围
     * @param selectedLayers 待分析图层
     * @param mapMaxExetnt 地图最大范围
     * @param callback2 结果回调
     */
    void getAnalysingResult(final Geometry geometry,
                            final List<LayerInfo> selectedLayers,
                            final Envelope mapMaxExetnt,
                            final Callback2<Map<String, AMFindResult[]>> callback2);

    /**
     * 获取图层列表
     * @param callback2 回调
     */
    void getLayers(Callback2<List<LayerInfo>> callback2);
}
