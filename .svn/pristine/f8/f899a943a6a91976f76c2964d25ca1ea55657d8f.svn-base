package com.augurit.agmobile.mapengine.layerquery.dao;

import android.support.annotation.NonNull;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通过identify进行空间查询（耗时操作，请不要在主线程调用）
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.identify
 * @createTime 创建时间 ：2016-12-15
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-15
 */

public class IdentifyTaskWithoutCallback {

     public IdentifyResult[] selectedFeature(SpatialReference spatialReference, Geometry geometry,
                                             List<LayerInfo> layerInfos, int tolerance){

        Map<String, int[]> layerMap = getQueryableLayerMap(layerInfos);
        Map<com.esri.core.tasks.identify.IdentifyTask, IdentifyParameters> identifyParametersMap =
                getIdentifyParametersMap(layerMap, geometry, spatialReference, tolerance);
        List<IdentifyResult> identifyResults = getIdentifyResults(identifyParametersMap);
        IdentifyResult[] identifyResult = sortedIdentifyResult(identifyResults);
         return identifyResult;
    }

    @NonNull
    private IdentifyResult[] sortedIdentifyResult(List<IdentifyResult> results) {
        List<IdentifyResult> pointResult = new ArrayList<IdentifyResult>();
        List<IdentifyResult> lineResult = new ArrayList<IdentifyResult>();
        List<IdentifyResult> polygonResult = new ArrayList<IdentifyResult>();
        for (IdentifyResult identifyResult : results){
            if(identifyResult.getGeometry().getType() == Geometry.Type.POINT){
                pointResult.add(identifyResult);
            }else if(identifyResult.getGeometry().getType() == Geometry.Type.POLYLINE ||
                    identifyResult.getGeometry().getType() == Geometry.Type.LINE){ //xcl 2016-12-24 加多一个判断，判断是否是POLYLINE或者LINE
                lineResult.add(identifyResult);
            }else if (identifyResult.getGeometry().getType() == Geometry.Type.POLYGON){
                polygonResult.add(identifyResult);
            }
        }
        results.clear();
        results.addAll(pointResult);
        results.addAll(lineResult);
        results.addAll(polygonResult);
        //将结果传回
        IdentifyResult[] identifyResultsArray = new IdentifyResult[results.size()];
        for (int i=0; i< results.size(); i++){
            identifyResultsArray[i] = results.get(i);
        }
        return identifyResultsArray;
    }

    @NonNull
    private List<IdentifyResult> getIdentifyResults(Map<com.esri.core.tasks.identify.IdentifyTask, IdentifyParameters> identifyMap) {
        List<IdentifyResult> results = new ArrayList<IdentifyResult>();
        Set<Map.Entry<com.esri.core.tasks.identify.IdentifyTask, IdentifyParameters>> entries = identifyMap.entrySet();
        for (Map.Entry<com.esri.core.tasks.identify.IdentifyTask, IdentifyParameters> entry: entries){
            try {
                IdentifyResult[] identifyResults = entry.getKey().execute(entry.getValue());
                if (identifyResults.length >0){
                    List<IdentifyResult> identifyResults1 = ListUtil.arrayToList(identifyResults);
                    results.addAll(identifyResults1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    @NonNull
    private Map<com.esri.core.tasks.identify.IdentifyTask, IdentifyParameters> getIdentifyParametersMap(Map<String, int[]> layers, Geometry geometry,
                                                                                                        SpatialReference spatialReference, int tolerance) {
        final Map<com.esri.core.tasks.identify.IdentifyTask, IdentifyParameters> identifyMap = new HashMap<>();    // 存放Identify任务及其参数
        for (Map.Entry<String, int[]> entry : layers.entrySet()) {
            IdentifyParameters parameters = new IdentifyParameters();
            parameters.setGeometry(geometry);
            Envelope extent = new Envelope();
            geometry.queryEnvelope(extent);
            parameters.setMapExtent(extent);
            parameters.setMapWidth((int) extent.getWidth()*100);
            parameters.setMapHeight((int) extent.getHeight()*100);
            parameters.setSpatialReference(spatialReference);
            parameters.setDPI(98);
            parameters.setLayers(entry.getValue());
            parameters.setReturnGeometry(true);
            parameters.setTolerance(tolerance);
            parameters.setLayerMode(IdentifyParameters.VISIBLE_LAYERS);
            com.esri.core.tasks.identify.IdentifyTask identifyTask = new com.esri.core.tasks.identify.IdentifyTask(entry.getKey());
            LogUtil.d("Identify Url:" + entry.getKey());
            identifyMap.put(identifyTask, parameters);
        }
        return identifyMap;
    }


    /**
     * 获取可以进行identify查询的service
     * @return
     */
    private Map<String,int[]> getQueryableLayerMap( List<LayerInfo> visibleLayers) {
        // 获取当前可见图层
        Map<String, int[]> layerMap = new HashMap<>();
        //List<LayerInfo> visibleLayers = LayerPresenter.getInstance(context, mapView).getAllVisibleLayers();
        for (LayerInfo info : visibleLayers){
            List<LayerInfo> childLayer = info.getChildLayer();
            if (!ListUtil.isEmpty(childLayer)){
                int[] childIds = new int[childLayer.size()];
                for (int i=0; i< childLayer.size(); i++){
                    childIds[i] = childLayer.get(i).getLayerId();
                }
                layerMap.put(info.getUrl().replace("FeatureServer","MapServer"),childIds);
            }else {
                if (info.getType() == LayerType.FeatureLayer){
                    //如果是featureLayer，那么也是可以查询的
                    String[] split = info.getUrl().split("/");
                    String lastString = split[split.length - 1]; //获取到最后的字符串
                    //判断是否包含“FeatureServer”
                    boolean ifContainsFeatureServer = lastString.contains("FeatureServer");
                    if (ifContainsFeatureServer){

                    }else {
                        //那么是xxx/FeatureServer/0的形式
                        String serviceUrl = info.getUrl().substring(0,info.getUrl().lastIndexOf("/"));
                        LogUtil.d("获取到的serviceUrl是："+serviceUrl);
                        int  childId = Integer.valueOf(lastString);
                        int[] childLayerId = new int[]{childId};
                        String mapserviceUrl = serviceUrl.replace("FeatureServer","MapServer");
                        //put的时候会覆盖,所以要先判断是否存在，如果存在，那么进行取出添加
                        int[] ints = layerMap.get(mapserviceUrl);
                        if (ints != null){
                            //取出进行添加
                            int[] newIds = new int[ints.length+1];
                            /*for (int i=0;i<ints.length;i++){
                                newIds[i] = ints[i];
                            }*/
                            System.arraycopy(ints,0,newIds,0,ints.length);
                            newIds[ints.length] = childId;
                            layerMap.put(mapserviceUrl,newIds);
                        }else {
                            layerMap.put(mapserviceUrl,childLayerId);
                        }
                    }
                }
            }
        }
        return layerMap;
    }
}
