package com.augurit.agmobile.mapengine.edit.service;

import com.augurit.agmobile.mapengine.edit.view.SelectForEditCallback;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Geometry;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.edit.service
 * @createTime 创建时间 ：17/2/9
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/2/9
 * @modifyMemo 修改备注：
 */

public interface ISelectForEditService {
    /**
     * 点击查询为编辑做准备
     * @param mapView
     * @param geometry
     * @param tempLayer
     * @param callback
     */
    void selectFeature(MapView mapView, Geometry geometry, ArcGISFeatureLayer tempLayer, SelectForEditCallback callback);
}
