package com.augurit.agmobile.mapengine.edit.service;

import android.util.Log;

import com.augurit.agmobile.mapengine.edit.view.SelectForEditCallback;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.query.Query;

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

public class SelectForEditService implements ISelectForEditService {

    @Override
    public void selectFeature(MapView mapView, Geometry geometry, final ArcGISFeatureLayer tempLayer, final SelectForEditCallback callback) {
        //查找点击点的要素信息
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setReturnGeometry(true);
        query.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        query.setGeometry(geometry);
        query.setInSpatialReference(mapView.getSpatialReference());

        //调用API去查找当前点要素
        tempLayer.selectFeatures(query, ArcGISFeatureLayer.SELECTION_METHOD.NEW, new CallbackListener<FeatureSet>() {
            @Override
            public void onCallback(FeatureSet queryResults) {
                if (queryResults.getGraphics().length > 0) {
                    callback.onSelect(queryResults);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("FeatureEditPresenter", "Select Features Error" + throwable.getLocalizedMessage());
            }
        });
    }
}
