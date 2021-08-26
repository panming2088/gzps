package com.augurit.agmobile.mapengine.layerquery.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.am.cmpt.common.Callback2;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Field;

import java.io.IOException;
import java.util.List;

import rx.Observable;

/**
 *   图层查询业务层，包含以下业务：
 * （1）根据选择的图层和关键字进行查询
 * （2）获取查询历史
 * （3）保存查询关键字
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.layerq.newarc.service
 * @createTime 创建时间 ：2016-11-30
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-30
 */

public interface ILayerQueryService {

    /**
     * 不带字段查询，要求传入空间参考系
     * @param context
     * @param seachKey
     * @param spatialReference 空间参考系
     * @param geometry
     * @param queryLayers
     * @param maxCount
     * @param callback
     */
    void queryLayer(Context context, String seachKey, SpatialReference spatialReference, Geometry geometry,
                    List<LayerInfo> queryLayers,
                    int maxCount, Callback2<List<AMFindResult>> callback);

    /**
     * 不带字段查询，要求传入mapview
     * @param context
     * @param seachKey
     * @param mapView
     * @param geometry
     * @param queryLayers
     * @param maxCount
     * @param callback
     */
    void queryLayer(Context context, String seachKey, MapView mapView, Geometry geometry,
                    List<LayerInfo> queryLayers,
                    int maxCount, Callback2<List<AMFindResult>> callback);

    /**
     * 带字段查询
     * @param context
     * @param seachKey 要查询的关键字
     * @param fieldName 要查询的字段
     * @param mapView
     * @param geometry 要查询的范围
     * @param queryLayers 要查询的图层
     * @param maxCount 返回的最多个数
     * @param callback
     */
    void queryLayer(Context context, String seachKey,String fieldName, MapView mapView, Geometry geometry,
                    List<LayerInfo> queryLayers,
                    int maxCount, Callback2<List<AMFindResult>> callback);


    List<String> getQueryHistory();

    Observable<List<LayerInfo>> getQueryableLayers(Context context) throws IOException;

    void saveQueryKey(String key);

    List<String> suggest(String key);

    /**
     * 获取所有字段
     * @param context
     * @param layerInfo
     * @return
     */
    List<Field> getAllFields(Context context,LayerInfo layerInfo);

    double getBestResolutionForReadingIfItHas(Context context) throws IOException;
}
