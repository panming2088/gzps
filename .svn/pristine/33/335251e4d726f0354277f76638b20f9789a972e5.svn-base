package com.augurit.agmobile.mapengine.common.utils;

import android.text.TextUtils;

import com.augurit.agmobile.mapengine.common.utils.wktutil.GeometryAreaUtil;
import com.augurit.agmobile.mapengine.common.utils.wktutil.model.Polygon;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.DefaultFilterCondition;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.IFilterCondition;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.layermanage.service.LayersService;
import com.augurit.agmobile.mapengine.layermanage.util.LayerFactoryProvider;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.AreaUnit;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Field;
import com.esri.core.table.FeatureTable;

import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.augurit.agmobile.mapengine.common.widget.callout.attribute.AttributeListActivity.attributes;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.utils
 * @createTime 创建时间 ：2017-04-12
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-12
 * @modifyMemo 修改备注：
 */

public class FeatureResultUtil {

    /**
     * 将FeatureResult转成AMFindResult，并且key值取的是字段的alias属性
     *
     * @param geodatabaseFeatureTable 表
     * @param objects                 FeatureResult
     * @param displayFieldName        优先显示成标题的字段的key
     * @return AMFindResult
     */
    @Deprecated
    private static List<AMFindResult> transFeatureResultToAgFindResult(FeatureTable geodatabaseFeatureTable,
                                                                      FeatureResult objects, String displayFieldName) {
        String featureServiceLayerName = null;
        if (geodatabaseFeatureTable instanceof GeodatabaseFeatureTable){
             featureServiceLayerName = ((GeodatabaseFeatureTable) geodatabaseFeatureTable).getFeatureServiceLayerName();
        }else {
            featureServiceLayerName = geodatabaseFeatureTable.getTableName();
        }
        int layerId = 0;
        if (!MapUtils.isEmpty(LayersService.sFindLayerIdByTableName)){
            layerId = LayersService.sFindLayerIdByTableName.get(geodatabaseFeatureTable.getTableName());
        }

        List<AMFindResult> results = transFeatureResultToAgFindResult(objects, displayFieldName, geodatabaseFeatureTable.getFields(), featureServiceLayerName);
        return results;
    }

    /**
     * 将FeatureResult转成AMFindResult，并且key值取的是字段的name属性
     *
     * @param objects          FeatureResult
     * @param displayFieldName 优先显示成标题的字段的key
     * @return AMFindResult
     */
    @Deprecated
    private static List<AMFindResult> transFeatureResultToAgFindResult(FeatureResult objects, String displayFieldName) {
        List<AMFindResult> findResults = new ArrayList<>();
        // 转成通用的AgFindResult
        for (Object element : objects) {
            if (element instanceof Feature) {
                Feature feature = (Feature) element;
                AMFindResult findResult = new AMFindResult();
                Geometry geometry1 = feature.getGeometry();
                Map<String, Object> attributes = feature.getAttributes();
                findResult.setGeometry(geometry1);
                findResult.setAttributes(attributes);
                if (displayFieldName != null) {
                    findResult.setDisplayFieldName(displayFieldName);
                    if (feature.getAttributeValue(displayFieldName) instanceof String) { //字符串
                        findResult.setValue((String) feature.getAttributeValue(displayFieldName));
                    } else if (feature.getAttributeValue(displayFieldName) instanceof Integer) { //整形
                        findResult.setValue(String.valueOf(feature.getAttributeValue(displayFieldName)));
                    } else if (feature.getAttributeValue(displayFieldName) instanceof Double) { //浮点型
                        findResult.setValue(String.valueOf(feature.getAttributeValue(displayFieldName)));
                    } else {
                        findResult.setValue("空");
                    }
                } else {
                    findResult.setDisplayFieldName("空");
                    findResult.setValue("空");
                }
                findResult.setId(feature.getId());
                //findResult.setLayerId();
                findResults.add(findResult);
            }
        }
        // List<AMFindResult> results = transFeatureResultToAgFindResult(objects, displayFieldName, geodatabaseFeatureTable.getFields());
        return findResults;
    }

    @Deprecated
    private static List<AMFindResult> transFeatureResultToAgFindResult(FeatureResult objects, String displayFieldName,
                                                                      List<Field> fields, String layerName) {
        List<AMFindResult> findResults = new ArrayList<>();
        // 转成通用的AgFindResult
        for (Object element : objects) {
            if (element instanceof Feature) {
                Feature feature = (Feature) element;
                AMFindResult findResult = new AMFindResult();
                Geometry geometry1 = feature.getGeometry();
                findResult.setGeometry(geometry1);

                Map<String, Object> attributes = feature.getAttributes();
                IFilterCondition filterCondition = new DefaultFilterCondition();
                Map<String, Object> filterMap = filterCondition.filter(attributes); //过滤掉不需要的属性

                Map<String, Object> chineseAttributes = transAttributes(filterMap, fields);
                findResult.setAttributes(chineseAttributes);

                if (displayFieldName != null) {
                    findResult.setDisplayFieldName(displayFieldName);
                    if (feature.getAttributeValue(displayFieldName) instanceof String) { //字符串
                        findResult.setValue((String) feature.getAttributeValue(displayFieldName));
                    } else if (feature.getAttributeValue(displayFieldName) instanceof Integer) { //整形
                        findResult.setValue(String.valueOf(feature.getAttributeValue(displayFieldName)));
                    } else if (feature.getAttributeValue(displayFieldName) instanceof Double) { //浮点型
                        findResult.setValue(String.valueOf(feature.getAttributeValue(displayFieldName)));
                    } else {
                        findResult.setValue("空");
                    }
                } else {
                    findResult.setDisplayFieldName("空");
                    findResult.setValue("空");
                }
                findResult.setId(feature.getId());
                findResult.setLayerName(layerName);
               // findResult.setLayerId(layerId);
                findResults.add(findResult);
            }
        }
        return findResults;
    }

    /**
     * 冲突检测
     *
     * @param geometry
     * @param spatialReference
     * @param objects          FeatureResult
     * @param displayFieldName 优先显示成标题的字段的key
     * @return AMFindResult
     */
    public static List<AMFindResult> transFeatureResultToAgFindResult(Geometry geometry, SpatialReference spatialReference,
                                                                      FeatureResult objects, String displayFieldName) {
        List<AMFindResult> findResults = new ArrayList<>();
        for (Object element : objects) {
            if (element instanceof Feature) {
                Feature feature = (Feature) element;
                AMFindResult findResult = new AMFindResult();
                Geometry geometry1 = feature.getGeometry();
                if (!GeometryEngine.intersects(geometry1, geometry, spatialReference)) {
                    continue;
                }
                Geometry geo = GeometryEngine.intersect(geometry1, geometry, spatialReference);
                Map<String, Object> attributes = feature.getAttributes();
                String strJson = GeometryEngine.geometryToJson(SpatialReference.create(spatialReference.getID()), geo);
                Polygon polygon = JsonUtil.getObject(strJson, Polygon.class);
                String squareMeter = null;
                try {
                    squareMeter = GeometryAreaUtil.calculatedGeoArea(polygon);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(squareMeter) || squareMeter.equals("0.0")) {
                    continue;
                }
                findResult.setGeometry(geo);
                if (attributes.containsKey(displayFieldName) && !TextUtils.isEmpty(squareMeter)) {
                    attributes.put(displayFieldName, String.valueOf(squareMeter));
                }
                findResult.setDisplayFieldName(displayFieldName);
                findResult.setAttributes(attributes);
                findResult.setId(feature.getId());
              //  findResult.setLayerId(layerId);
                findResults.add(findResult);
            }
        }
        return findResults;
    }

    /**
     * 冲突检测,并且加入当前findResult的attribute；
     *
     * @param geometry
     * @param spatialReference
     * @param objects          FeatureResult
     * @param displayFieldName 优先显示成标题的字段的key
     * @return AMFindResult
     */
    public static List<AMFindResult> transFeatureResultToAgFindResult(Geometry geometry, SpatialReference spatialReference,
                                                                      FeatureResult objects, String displayFieldName,
                                                                      AMFindResult cityUseFindResult) {
        List<AMFindResult> findResults = new ArrayList<>();
        // 转成通用的AgFindResult
        for (Object element : objects) {
            if (element instanceof Feature) {
                Feature feature = (Feature) element;
                AMFindResult findResult = new AMFindResult();
                Geometry geometry1 = feature.getGeometry();
                if (!GeometryEngine.intersects(geometry1, geometry, spatialReference)) {
                    continue;
                }
                Geometry geo = GeometryEngine.intersect(geometry1, geometry, spatialReference);
                Map<String, Object> attributes = feature.getAttributes();
                String strJson = GeometryEngine.geometryToJson(SpatialReference.create(spatialReference.getID()), geo);
                Polygon polygon = JsonUtil.getObject(strJson, Polygon.class);
                String squareMeter = null;
                try {
                    squareMeter = GeometryAreaUtil.calculatedGeoArea(polygon);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(squareMeter) || squareMeter.equals("0.0")) {
                    continue;
                }
                findResult.setGeometry(geo);
                if (attributes.containsKey(displayFieldName) && !TextUtils.isEmpty(squareMeter)) {
                    attributes.put(displayFieldName, String.valueOf(squareMeter));
                }
                findResult.setDisplayFieldName(displayFieldName);
                attributes.putAll(cityUseFindResult.getAttributes()); //加入外界传递进来的attribute
                findResult.setAttributes(attributes);
                findResult.setId(feature.getId());
              //  findResult.setLayerId(layerId);
                findResults.add(findResult);
            }
        }
        // List<AMFindResult> results = transFeatureResultToAgFindResult(objects, displayFieldName, geodatabaseFeatureTable.getFields());
        return findResults;
    }

    /**
     * 将FeatureResult转成AMFindResult，并且key值取的是字段的alias属性
     *
     * @param objects          FeatureResult
     * @param displayFieldName 优先显示成标题的字段的key
     * @param fields           所有字段的Key
     * @return AMFindResult
     */
    @Deprecated
    private static List<AMFindResult> transFeatureResultToAgFindResult(FeatureResult objects, String displayFieldName,
                                                                      List<Field> fields) {
        List<AMFindResult> findResults = new ArrayList<>();
        // 转成通用的AgFindResult
        for (Object element : objects) {
            if (element instanceof Feature) {
                Feature feature = (Feature) element;
                AMFindResult findResult = new AMFindResult();
                Geometry geometry1 = feature.getGeometry();
                findResult.setGeometry(geometry1);

                Map<String, Object> attributes = feature.getAttributes();
                IFilterCondition filterCondition = new DefaultFilterCondition();
                Map<String, Object> filterMap = filterCondition.filter(attributes); //过滤掉不需要的属性

                Map<String, Object> chineseAttributes = transAttributes(filterMap, fields);
                findResult.setAttributes(chineseAttributes);

                if (displayFieldName != null) {
                    findResult.setDisplayFieldName(displayFieldName);
                    if (feature.getAttributeValue(displayFieldName) instanceof String) { //字符串
                        findResult.setValue((String) feature.getAttributeValue(displayFieldName));
                    } else if (feature.getAttributeValue(displayFieldName) instanceof Integer) { //整形
                        findResult.setValue(String.valueOf(feature.getAttributeValue(displayFieldName)));
                    } else if (feature.getAttributeValue(displayFieldName) instanceof Double) { //浮点型
                        findResult.setValue(String.valueOf(feature.getAttributeValue(displayFieldName)));
                    } else {
                        findResult.setValue("空");
                    }
                } else {
                    findResult.setDisplayFieldName("空");
                    findResult.setValue("空");
                }
                findResult.setId(feature.getId());
                //findResult.setLayerId(layerId);
                findResults.add(findResult);
            }
        }
        return findResults;
    }


    /**
     * 将key改成使用字段的别名
     * @param attributes 属性
     * @param fields 所有字段（包含别名）
     * @return
     */
    public static Map<String, Object> transAttributes(Map<String, Object> attributes, List<Field> fields) {
        Map<String, Object> chineseAttributes = new LinkedHashMap<>();
        for (Field field : fields) {
            Object o = attributes.get(field.getName());
            if (o != null) {
                if (!TextUtils.isEmpty(field.getAlias()) && !field.getAlias().equalsIgnoreCase("null")){
                    chineseAttributes.put(field.getAlias(), o); //如果有别名，那么使用别名
                }else {
                    chineseAttributes.put(field.getName(),o);//如果没有别名，使用Name
                }
            }
        }
        return chineseAttributes;
    }


    /**
     * 将FeatureResult转成AMFindResult
     * @param objects featureResult
     * @param displayFieldName 有显示成标题的字段，可以为null
     * @param fields 包含别名的字段列表，可以为null
     * @param layerName 图层名称
     * @param tableName 表名
     * @return
     */
    public static List<AMFindResult> transFeatureResultToAgFindResult(FeatureResult objects, String displayFieldName,
                                                                      List<Field> fields, String layerName,String tableName){
        int layerId = 0;

        if (!TextUtils.isEmpty(tableName)){
            if (!MapUtils.isEmpty(LayersService.sFindLayerIdByTableName)){
                Integer integer = LayersService.sFindLayerIdByTableName.get(tableName);
                if (integer != null){
                    layerId = integer;
                }
            }
        }

        List<AMFindResult> findResults = new ArrayList<>();
        // 转成通用的AgFindResult
        for (Object element : objects) {
            if (element instanceof Feature) {
                Feature feature = (Feature) element;
                AMFindResult findResult = new AMFindResult();
                Geometry geometry1 = feature.getGeometry();
                findResult.setGeometry(geometry1);

                Map<String, Object> attributes = feature.getAttributes();
                IFilterCondition filterCondition = new DefaultFilterCondition();
                Map<String, Object> filterMap = filterCondition.filter(attributes); //过滤掉不需要的属性

                //是否有别名
                if (!ListUtil.isEmpty(fields)){
                    Map<String, Object> chineseAttributes = transAttributes(filterMap, fields);
                    findResult.setAttributes(chineseAttributes);
                }else {
                    findResult.setAttributes(filterMap);
                }

                if (displayFieldName != null) {
                    findResult.setDisplayFieldName(displayFieldName);
                    if (feature.getAttributeValue(displayFieldName) instanceof String) { //字符串
                        findResult.setValue((String) feature.getAttributeValue(displayFieldName));
                    } else if (feature.getAttributeValue(displayFieldName) instanceof Integer) { //整形
                        findResult.setValue(String.valueOf(feature.getAttributeValue(displayFieldName)));
                    } else if (feature.getAttributeValue(displayFieldName) instanceof Double) { //浮点型
                        findResult.setValue(String.valueOf(feature.getAttributeValue(displayFieldName)));
                    } else {
                        findResult.setValue("空");
                    }
                } else {
                    findResult.setDisplayFieldName("空");
                    findResult.setValue("空");
                }
                findResult.setId(feature.getId());
                findResult.setLayerName(layerName);
                findResult.setLayerId(layerId);
                findResults.add(findResult);
            }
        }

        return findResults;
    }

}
