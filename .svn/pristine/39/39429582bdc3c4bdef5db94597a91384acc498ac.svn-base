package com.augurit.agmobile.mapengine.layerquery.dao;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.find.FindParameters;
import com.esri.core.tasks.ags.find.FindResult;
import com.esri.core.tasks.ags.find.FindTask;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通过arcgis接口进行图层查询，如果有关键字，用find进行查询，没有关键字，用Identify进行查询，
 *  默认返回500条数据
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.layerq.newarc.dao
 * @createTime 创建时间 ：2016-11-30
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-30
 */

public class RemoteArcgisLayerQueryDao {


    public static final int DEFAULT_MAX_COUNT = 1000; //返回的最大结果数
    /******
     * Find,Query,Identify的区别：
     * Find:查询子图层，需要的参数: 关键字以及子图层id，无法进行空间查询。全字段查询
     * Query:用来查询某个图层的信息，可以不需要关键字，可以进行空间查询/属性查询，单字段查询，只查询DisplayFieldName
     * Identiry：用于点选
     */

    //////////////////如果有关键字，用find进行查询，没有关键字，用Identify进行查询，如果遇到featureLayer或者featuerServer，
    // 统一将FeatureServer换成MapServer后再进行查询////////////
   // public static final int DEFAULT_MAX_COUNT = 500;

    public List<AMFindResult> queryLayer(Context context,
                                         String searchKey,
                                         List<LayerInfo> queryLayers,
                                         Geometry geometry,
                                         SpatialReference spatialReference){

       /* if (geometry == null){
            //默认查询全图
            geometry = mapView.getExtent();
        }*/

        List<AMFindResult> findResults = new ArrayList<>();
        List<LayerInfo> listExcludedFeatureLayer = new ArrayList<>();

        for (LayerInfo amLayerInfo: queryLayers){
            if (amLayerInfo.getType() == LayerType.FeatureLayer){

                //featureLayer的格式是"xxx/FeatureServer/0"的格式
                String serviceUrl = amLayerInfo.getUrl().substring(0,amLayerInfo.getUrl().lastIndexOf("/"));
                String[] split = amLayerInfo.getUrl().split("/");
                String lastString = split[split.length - 1]; //获取到最后的字符串
                int  childId = Integer.valueOf(lastString);
                String mapserviceUrl = serviceUrl.replace("FeatureServer","MapServer");

                //构造子Layer
                LayerInfo child = new LayerInfo();
                child.setLayerId(childId);
                List<LayerInfo> children = new ArrayList<>();
                children.add(child);

                LayerInfo layerInfo = new LayerInfo();
                layerInfo.setUrl(mapserviceUrl);
                layerInfo.setChildLayer(children);

                listExcludedFeatureLayer.add(layerInfo);
               // featureLayerList.add(amLayerInfo);
            }else {
                listExcludedFeatureLayer.add(amLayerInfo);
            }
        }

        if (TextUtils.isEmpty(searchKey)){
            //如果查询关键字为空，那么进行Identify查询
            List<AMFindResult> results = queryFeatureByIdentifyTask(context, listExcludedFeatureLayer, spatialReference, geometry);
            findResults.addAll(results);
        }else {
            //否则，进行find查询
            List<AMFindResult> results = queryFeatureByFindTask(searchKey, listExcludedFeatureLayer, geometry, spatialReference);
            findResults.addAll(results);
        }

        if (ValidateUtil.isListNull(findResults)){ //如果查不到数据
            return findResults;
        }
        //TODO 这里暂时自己进行过滤数据大小，之后要配合服务端接口
        List<AMFindResult> limitedResult = new ArrayList<>();
        if (findResults.size() > DEFAULT_MAX_COUNT){
            for (int i=0; i< DEFAULT_MAX_COUNT ; i++){
                limitedResult.add(findResults.get(i));
            }
            return limitedResult;
        }

        return findResults;
    }

    //////////////////现在的做法，如果有关键字，用find进行查询，没有关键字，用Identify进行查询，如果遇到featureLayer，
    // 采用query进行查询，如果是featureServer，那么不查询，因为我们无法获取featureServer的子图层，也无法获知其是否有对应的MapServer////////////
   /* public List<AMFindResult> queryLayer(Context context,
                                         String searchKey,
                                         List<LayerInfo> queryLayers,
                                         Geometry geometry,
                                         MapView mapView){

        if (geometry == null){
            //默认查询全图
            geometry = mapView.getMaxExtent();
        }

        List<AMFindResult> findResults = new ArrayList<>();
        List<LayerInfo> listExcludedFeatureLayer = new ArrayList<>();
        List<LayerInfo> featurelayerList = new ArrayList<>();

        for (LayerInfo amLayerInfo: queryLayers){
            if (amLayerInfo.getType() == LayerType.FeatureLayer){
                 featurelayerList.add(amLayerInfo);
            }else {
                 listExcludedFeatureLayer.add(amLayerInfo);
            }
        }
        //一定进行query查询
        List<AMFindResult> findResults1 = queryFeatureByQueryTask(geometry, searchKey, featurelayerList,mapView, DEFAULT_MAX_COUNT);
        if (findResults1 != null){
            findResults.addAll(findResults1);
        }

        if (searchKey == null|| searchKey.equals("")){
            //如果查询关键字为空，那么进行Identify查询
            List<AMFindResult> results = queryFeatureByIdentifyTask(context, listExcludedFeatureLayer, mapView, geometry);
            findResults.addAll(results);
        }else {
            //否则，进行find查询
            List<AMFindResult> results = queryFeatureByFindTask(searchKey, listExcludedFeatureLayer, geometry, mapView.getSpatialReference());
            findResults.addAll(results);
        }

        return findResults;
    }*/


    private List<AMFindResult> queryFeatureByIdentifyTask(Context context, List<LayerInfo> queryLayers,
                                                          SpatialReference spatialReference, Geometry geometry){

        IdentifyTaskWithoutCallback identifyService = new IdentifyTaskWithoutCallback();
        IdentifyResult[] identifyResults = identifyService.selectedFeature(spatialReference, geometry,queryLayers, 20);

        List<AMFindResult> findResults = new ArrayList<>();
        for (IdentifyResult identifyResult : identifyResults){
            AMFindResult agFindResult = new AMFindResult();
            agFindResult.setLayerId(identifyResult.getLayerId());
            agFindResult.setDisplayFieldName(identifyResult.getDisplayFieldName());
            if (identifyResult.getValue() instanceof String){
                if (identifyResult.getAttributes().get(identifyResult.getDisplayFieldName()) instanceof String){
                    agFindResult.setValue((String) identifyResult.getAttributes().get(identifyResult.getDisplayFieldName()));
                }else {
                    agFindResult.setValue((String) identifyResult.getValue());
                }
            }
            agFindResult.setGeometry(identifyResult.getGeometry());
            agFindResult.setAttributes(identifyResult.getAttributes());
            agFindResult.setLayerName(identifyResult.getLayerName());
            findResults.add(agFindResult);
            if (findResults.size() >= DEFAULT_MAX_COUNT){
                break;
            }
        }
        return findResults;
    }


    /**
     * 查询图层下的所有图层，如果没有子图层是查询不到任何东西的
     * @param searchKey
     * @param queryLayers 要查询的图层
     */
    private List<AMFindResult> queryFeatureByFindTask(String searchKey, List<LayerInfo> queryLayers, Geometry geometry,
                                                      SpatialReference spatialReference){

        //进行过滤图层，只过滤出可以查询的图层
        Map<String,int[]> queryMap = filterLayerFindable(queryLayers);
        Set<Map.Entry<String, int[]>> entries = queryMap.entrySet();
        //最后要返回的查询结果
        List<FindResult> findResults = new ArrayList<>();
        for (Map.Entry<String, int[]> entry : entries){
            FindParameters fParameters = new FindParameters(searchKey,entry.getValue());
            fParameters.setLayerIds(entry.getValue());
            fParameters.setReturnGeometry(true);
            fParameters.setOutputSpatialRef(spatialReference);
            fParameters.setSearchText(searchKey);//设置查询关键字
            FindTask findTask = new FindTask(entry.getKey());
            try {
                List<FindResult> results = findTask.execute(fParameters);
                if (results != null){
                    findResults.addAll(results);
                }
                if (findResults.size() >= DEFAULT_MAX_COUNT){
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        List<FindResult> finalResult = filterResultNotNeeded(findResults,geometry,spatialReference);

        return transformFindResultToAgFindResult(finalResult);
    }

    /**
     * 进行实体转换
     * @param finalResults
     * @return
     */
    private List<AMFindResult> transformFindResultToAgFindResult(List<FindResult> finalResults) {

        List<AMFindResult> results = new ArrayList<>();
        for (FindResult agFindResult: finalResults){
            AMFindResult findResult = new AMFindResult();
            findResult.setGeometry(agFindResult.getGeometry());
            findResult.setAttributes(agFindResult.getAttributes());
            findResult.setDisplayFieldName(agFindResult.getDisplayFieldName());
            if ((agFindResult.getAttributes().get(agFindResult.getDisplayFieldName())) instanceof String){
                findResult.setValue((String) agFindResult.getAttributes().get(agFindResult.getDisplayFieldName()));
            }else {
                findResult.setValue( agFindResult.getValue());
            }
            //findResult.setValue(agFindResult.getAttributes().requestLocation(agFindResult.getDisplayFieldName()));
            findResult.setLayerId(agFindResult.getLayerId());
            findResult.setLayerName(agFindResult.getLayerName());
            results.add(findResult);
        }
        return results;
    }

    /**
     * 过滤掉不在范围中的结果
     * @param findResults
     * @return
     */
    private List<FindResult> filterResultNotNeeded(List<FindResult> findResults, Geometry geometry, SpatialReference spatialReference) {

        if (geometry == null){
            return findResults;
        }

        List<FindResult> result = new ArrayList<>();
        for (FindResult findResult : findResults){
            boolean contains = GeometryEngine.contains(geometry, findResult.getGeometry(), spatialReference);
            if (contains){
                result.add(findResult);
            }
        }
        return result;
    }

    /**
     * 过滤出可以进行Find查询的图层
     * @param queryLayers
     * @return
     */
    private Map<String, int[]> filterLayerFindable(List<LayerInfo> queryLayers) {
        Map<String,int[]>  queryMap = new HashMap<>();
        for (LayerInfo layerInfo : queryLayers){
            //如果子图层为空，无法查询。因为Find要求查询图层有子图层
            List<LayerInfo> childLayer = layerInfo.getChildLayer();
            if (ValidateUtil.isListNull(childLayer)){
                continue;
            }
            String parentUrl = layerInfo.getUrl();
            parentUrl = parentUrl.replace("FeatureServer","MapServer");
            int[] childIds = new int[childLayer.size()];
            //如果有子图层，取出子图层id
            for (int i=0; i< childLayer.size(); i++){
                childIds[i] = childLayer.get(i).getLayerId();
            }
            //put的时候会覆盖,所以要先判断是否存在，如果存在，那么进行取出添加
            int[] previousIds = queryMap.get(parentUrl);
            if (previousIds != null){
                //取出进行添加
                int[] newIds = new int[previousIds.length+childIds.length];
                /*for (int i=0;i<previousIds.length;i++){
                    newIds[i] = previousIds[i];
                }*/
                System.arraycopy(previousIds,0,newIds,0,previousIds.length);
                /*for (int i = previousIds.length; i< previousIds.length+childIds.length; i++){
                    newIds[i] = childIds[i-previousIds.length];
                }*/
                System.arraycopy(childIds,0,newIds, previousIds.length, childIds.length);
                queryMap.put(parentUrl,newIds);
            }else {
                queryMap.put(parentUrl,childIds);
            }
            //存放到要查询的集合中
           // queryMap.put(parentUrl,childIds);
        }
        return queryMap;
    }



    /**
     * 遍历查询单个图层
     * @param geometry 可以为空
     * @param keyword  可以为空
     * @param queryLayers 不可以未空
     * @param maxResultCount 返回的最大结果数
     *
     */
    public List<AMFindResult> queryFeatureByQueryTask(Geometry geometry, String keyword, List<LayerInfo> queryLayers,
                                                       MapView mapView, int maxResultCount){

        List<String> queryLayerUrls = filterLayerQueryable(queryLayers);
        long resultCount =0;
        List<AMFindResult> findResults = new ArrayList<>();
        //进行遍历查询
        for (String queryLayer : queryLayerUrls){
            QueryTask queryTask = new QueryTask(queryLayer);
            QueryParameters queryParameters = new QueryParameters();
//            queryParameters.setOutSpatialReference(mapView.getSpatialReference());
            queryParameters.setReturnGeometry(true);
            if (geometry != null){
                queryParameters.setGeometry(geometry);
            }
            if (!TextUtils.isEmpty(keyword)){
                queryParameters.setText(keyword);
            }
            queryParameters.setOutFields(new String[]{"*"});
            queryParameters.setSpatialRelationship(SpatialRelationship.INTERSECTS);
            queryParameters.setMaxFeatures(maxResultCount);//设置最大返回数目
            queryParameters.setWhere("1=1");
            try {
                FeatureResult execute = queryTask.execute(queryParameters);
                String displayFieldName = execute.getDisplayFieldName();
                resultCount += execute.featureCount();
                if ((execute.isExceededTransferLimit() || resultCount >= maxResultCount)
                        && findResults.size() != 0){
                    //如果加上这次的结果超过最大限制，那么不加上这次的查询结果
                    return findResults;
                }
                // 转成通用的AgFindResult
                for (Object element : execute) {
                    if (element instanceof Feature) {
                            Feature feature  = (Feature) element;
                        AMFindResult findResult = new AMFindResult();
                        Geometry geometry1 = feature.getGeometry();
                        Map<String, Object> attributes = feature.getAttributes();
                        findResult.setGeometry(geometry1);
                        findResult.setAttributes(attributes);
                        findResult.setDisplayFieldName(displayFieldName);
                        //TODO 这里是否需要加一下检查，强制转成String是否不好
                        findResult.setValue((String) feature.getAttributeValue(displayFieldName));
                        findResults.add(findResult);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return findResults;
    }

    /**
     * 过滤出可以进行Query查询的layer
     * @param queryLayers 要查询的所有图层
     * @return
     */
    private List<String> filterLayerQueryable(List<LayerInfo> queryLayers) {

        //query只能是子图层
        List<String> queryLayerUrls = new ArrayList<>();
        for (LayerInfo layerInfo : queryLayers){
            List<LayerInfo> childLayer = layerInfo.getChildLayer();
            if (ValidateUtil.isListNull(childLayer)){
                queryLayerUrls.add(layerInfo.getUrl());
            }else {
                //有子图层，遍历子图层
                for (LayerInfo child : childLayer){
                    String url = child.getUrl() + "/" + child.getLayerId();
                    queryLayerUrls.add(url);
                }
            }
        }
        return queryLayerUrls;
    }


}
