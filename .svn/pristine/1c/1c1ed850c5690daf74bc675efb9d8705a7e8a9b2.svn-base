package com.augurit.agmobile.mapengine.edit.service;

import android.widget.BaseAdapter;

import com.augurit.agmobile.mapengine.edit.view.EditDataCallback;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.FeatureSet;

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

public interface IEditAttrDataService {

    /**
     *  //几何编辑 ->删除要素
     * @param featureLayer
     * @param featureId
     * @param callback
     */
    void deleteFeature(ArcGISFeatureLayer featureLayer, Object featureId, EditDataCallback callback);


    /**
     * 提交属性编辑
     * @param featureLayer
     * @param listAdapter
     * @param featureSet
     * @param callback
     */
    void updateFeature(ArcGISFeatureLayer featureLayer, BaseAdapter listAdapter, FeatureSet featureSet, EditDataCallback callback);
}
