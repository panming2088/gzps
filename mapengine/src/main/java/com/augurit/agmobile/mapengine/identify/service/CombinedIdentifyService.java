package com.augurit.agmobile.mapengine.identify.service;

import android.app.Activity;

import com.augurit.agmobile.mapengine.common.utils.GeodatabaseAndShapeFileManagerFactory;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.log.util.NetUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.table.FeatureTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 整合了离线和在线点查，当在本地数据库中找不到表的时候，采用在线查询的方式,
 * 默认离线采用geodatabase和shapefile结合的方式，在线采用arcgis接口；
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.identify.service
 * @createTime 创建时间 ：2017-04-17
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-17
 * @modifyMemo 修改备注：
 */

public class CombinedIdentifyService implements IIdentifyService {

    private AtomicInteger queryTime = new AtomicInteger(0); //查询次数

    @Override
    public void selectedFeature(Activity context, MapView mapView, List<LayerInfo> visibleQueryableLayers,
                                Geometry geometry,
                                int tolerance,
                                final Callback2<AMFindResult[]> callback) {

       // List<LayerInfo> visibleQueryableLayers = layersService.getVisibleQueryableLayers();
        if (ValidateUtil.isListNull(visibleQueryableLayers)) {
            callback.onFail(new Exception("无可查询图层"));
        }
        final List<AMFindResult> findResults = new ArrayList<>();
        List<LayerInfo> onlineLayer = new ArrayList<>(); //在线查询的图层
        List<LayerInfo> offLineLayer = new ArrayList<>(); //离线查询的图层
        for (LayerInfo layerInfo : visibleQueryableLayers) {
            FeatureTable featureTable = GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().getTable(context, layerInfo.getLayerTable());
            if (featureTable == null) {
                onlineLayer.add(layerInfo); //在线
            }else {
                offLineLayer.add(layerInfo); //离线
            }
        }
        queryTime.set(0);
        if (ValidateUtil.isListNull(offLineLayer)){
            queryTime.addAndGet(1);
            checkAndReturnResult(findResults,callback);
        }else {
            //离线查找
            GeodatabaseIdentifyService offlineIdentifyService = new GeodatabaseIdentifyService();
            offlineIdentifyService.selectedFeature(context, mapView,offLineLayer, geometry, tolerance, new Callback2<AMFindResult[]>() {
                @Override
                public void onSuccess(AMFindResult[] amFindResults) {
                    findResults.addAll(Arrays.asList(amFindResults));
                    queryTime.addAndGet(1);
                    checkAndReturnResult(findResults,callback);
                }

                @Override
                public void onFail(Exception error) {
                    queryTime.addAndGet(1);
                    checkAndReturnResult(findResults,callback);
                }
            });
        }


        if (NetUtil.isConnected(context)){ //如果网络正常
            //在线查找
            IdentifyService onlineService = new IdentifyService();
            onlineService.selectedFeature(context, mapView, onlineLayer, geometry, tolerance, new Callback2<AMFindResult[]>() {
                @Override
                public void onSuccess(AMFindResult[] amFindResults) {
                    findResults.addAll(Arrays.asList(amFindResults));
                    queryTime.addAndGet(1);
                    checkAndReturnResult(findResults,callback);
                }

                @Override
                public void onFail(Exception error) {
                    queryTime.addAndGet(1);
                    checkAndReturnResult(findResults,callback);
                }
            });
        }else {
             //如果网络不通
            queryTime.addAndGet(1);
            checkAndReturnResult(findResults,callback);
        }
    }

    private void checkAndReturnResult(List<AMFindResult> findResults,
                                      Callback2<AMFindResult[]> callback) {
        if (queryTime.get() == 2){
            if (ValidateUtil.isListNull(findResults)){
                callback.onFail(new Exception("无可查询图层"));
            }else {
                AMFindResult[] array = findResults.toArray(new AMFindResult[findResults.size()]);
           /* AMFindResult[] finalResult = new AMFindResult[findResults.size()];
            for (int i=0; i< findResults.size();i++){
                finalResult[i] = findResults.get(i);
            }*/
                callback.onSuccess(array);
            }

        }
    }

}
