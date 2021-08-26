package com.augurit.agmobile.mapengine.panorama.service;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.map.Graphic;

import java.util.List;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.agmobile.mapengine.panorama.service
 * @createTime 创建时间 ：17/1/18
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 17/1/18
 */

public interface IPanoListDataService {

    /**
     * 获取全景数据列表数据
     * @param mapView
     * @param featureLayer
     * @param _callback
     */
    void getPanoListData(MapView mapView, final ArcGISFeatureLayer featureLayer, Callback _callback);


    /**
     * 数据处理回调接口
     */
    interface Callback {
        void onFail();
        void onSuccess(List<Graphic> graphics);
    }
}
