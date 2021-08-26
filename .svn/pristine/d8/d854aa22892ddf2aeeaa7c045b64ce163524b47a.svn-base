package com.augurit.agmobile.mapengine.edit.service;

import android.util.Log;

import com.augurit.agmobile.mapengine.edit.util.EditMode;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.Graphic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augur.agmobile.ammap.edit.mgr
 * @createTime 创建时间 ：16/11/15
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 16/11/15
 */

public class MoveAllEditService implements  IMoveAllEditService{

    @Override
    public void saveAfterMoveAll(ArcGISFeatureLayer featureLayer,
                                 Object featureId,
                                 List<Point> points,
                                 EditMode mode,
                                 SpatialReference sp,
                                 AllMoveCallback callback){
        Graphic g;
        Map<String,Object> attrs = new HashMap<>();

        //传入ID
        attrs.put(featureLayer.getObjectIdField(),featureId);
        if(mode == EditMode.POINT){
            //For a point, just create a Overlay from the point
            //  g = mTemplateLayer.createFeatureWithTemplate(mTemplate, mPoints.get(0));
            g = new Graphic(points.get(0), null, attrs);
        }else {
            // For polylines and polygons, create a MultiPath from the points...
            MultiPath multiPath;
            if(mode == EditMode.POLYLINE){
                multiPath = new Polyline();
            }else if(mode == EditMode.POLYGON){
                multiPath = new Polygon();
            }else {
                return;
            }

            if(points.size() == 0){
                return;
            }

            multiPath.startPath(points.get(0));
            for(int i=1;i<points.size();i++){
                multiPath.lineTo(points.get(i));
            }

            // ...then simplify the geometry and create a Overlay from it
            Geometry geom = GeometryEngine.simplify(multiPath, sp);
            g = new Graphic(geom, null, attrs);
        }

        featureLayer.applyEdits(null, null, new Graphic[]{g}, callbackListener(callback));
    }

    private CallbackListener<FeatureEditResult[][]> callbackListener(final AllMoveCallback callback){
        return new CallbackListener<FeatureEditResult[][]>() {
            @Override
            public void onCallback(FeatureEditResult[][] result) {
                if(result[2] != null && result[2][0] != null && result[2][0].isSuccess()){
                    Log.d("Editing:", "Success updating feature with id=" + result[2][0].getObjectId());
                    callback.result(result);
                }else {
                    callback.result(null);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("Update ", "error updating feature: " + throwable.getLocalizedMessage());
                callback.result(null);
            }
        };
    }

    public interface AllMoveCallback{
        void result(FeatureEditResult[][] result);
    }
}
