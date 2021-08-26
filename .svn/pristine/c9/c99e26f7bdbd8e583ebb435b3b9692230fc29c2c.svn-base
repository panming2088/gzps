package com.augurit.agmobile.mapengine.edit.service;



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
import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.Graphic;

import java.util.List;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augur.agmobile.ammap.edit.mgr
 * @createTime 创建时间 ：16/11/15
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 16/11/15
 */

public class AddEditService implements IAddEditService{

    //几何要素编辑所针对的图层,即选中的几何模板所在的图层
    ArcGISFeatureLayer mTemplateLayer;



    public AddEditService(ArcGISFeatureLayer featureLayer){
        this.mTemplateLayer = featureLayer;
    }

    @Override
    public void saveAfterAdding(List<Point> points, FeatureTemplate template, EditMode mode, SpatialReference sp, final AddCallback callback){
        Graphic g;
        final Geometry geom;

        if(mode == EditMode.POINT){
            //如果是一个点(包括显示图片的点),则直接创建它
            geom = GeometryEngine.simplify(points.get(0),sp);
            g = mTemplateLayer.createFeatureWithTemplate(template, points.get(0));
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
            multiPath.startPath(points.get(0));
            for(int i=1;i<points.size();i++){
                multiPath.lineTo(points.get(i));
            }

            // ...then simplify the geometry and create a Overlay from it
            geom = GeometryEngine.simplify(multiPath,sp);
            g = mTemplateLayer.createFeatureWithTemplate(template,geom);
        }

        //提交要素到图层
        mTemplateLayer.applyEdits(new Graphic[]{g}, null, null, new CallbackListener<FeatureEditResult[][]>() {

            @Override
            public void onError(Throwable e) {
              //  completeSaveAfterAdd(null);
                callback.result(null,null);
            }

            @Override
            public void onCallback(FeatureEditResult[][] results) {
               // completeSaveAfterAdd(results);
                callback.result(results,geom);
            }

        });

    }


    public interface AddCallback{
        void result(final FeatureEditResult[][] results,Geometry geometry);
    }
}
