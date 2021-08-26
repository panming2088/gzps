package com.augurit.agmobile.mapengine.identify.service;

import android.app.Activity;
import android.content.Context;

import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayersService;
import com.augurit.am.cmpt.common.Callback2;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.tasks.identify.IdentifyResult;

import java.util.List;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.agmobile.mapengine.identify.service
 * @createTime 创建时间 ：17/2/6
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 17/2/6
 */

public interface IIdentifyService {
    /*void selectedFeature(Activity context, MapView mapView, final ILayersService layersService, Geometry geometry, int tolerance,
                         Callback2<AMFindResult[]> callback); //xcl 2017-02-10 修改方法参数*/

    void selectedFeature(final Activity context, final MapView mapView,List<LayerInfo> visibleQueryableLayers,
                       Geometry geometry,int tolerance, final Callback2<AMFindResult[]> callback);//xcl 2017-04-18 修改方法参数
}
