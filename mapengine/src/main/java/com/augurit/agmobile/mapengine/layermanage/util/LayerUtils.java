package com.augurit.agmobile.mapengine.layermanage.util;


import com.augurit.agmobile.mapengine.layermanage.model.AGCOMLayerType;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 图层工具类，主要的作用有：
 * （1）进行图层Model之间的转换，将不同的对象转成我们需要的AMLayerInfo。
 * （2）获取图层类型
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.layer.util
 * @createTime 创建时间 ：2016-10-14 14:00
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2016-10-14 14:00
 */
public final class LayerUtils {
    private LayerUtils() {
    }

    public static LayerType getLayerType(String layerType) {
        switch (layerType) {
            case LayerConstant.DYNAMICLAYER:
                return LayerType.DynamicLayer;
            case LayerConstant.TILELAYER:
                return LayerType.TileLayer;
            case LayerConstant.FEATURELAYER:
                return LayerType.FeatureLayer;
        }
        return LayerType.Unsupported;
    }

    /**
     * 将AGCOM的地图服务类型转成Arcgis中相对应的类型
     *
     * @param layerType AGCOM中地图服务类型的编号
     * @param layerName AGCOM中地图服务类型的名称
     * @return 项目中的地图服务类型
     */
    public static LayerType getLayerType(String layerType, String layerName, String url) {

        switch (layerType) {
            case "1": //如果编号是1，那么可能的情况有：AST,AST_C,AGT,AGTB,ASR
                Pattern pattern = Pattern.compile("\\d+$");
                Matcher matcher = pattern.matcher(url);
                if (matcher.find()) {
                    return LayerType.FeatureLayer; //矢量图层
                } else {
                    if (layerName.equals(AGCOMLayerType.AST.toString())) {
                        return LayerType.DynamicLayer;
                    } else {
                        return LayerType.TileLayer;
                    }
                }
            case "3":
                //如果是3，可能是矢量图层也可能是矢量服务
                //如果是矢量图层也就是以数字结束的
                pattern = Pattern.compile("\\d+$");
                matcher = pattern.matcher(url);
                if (matcher.find()) {
                    return LayerType.FeatureLayer; //矢量图层
                } else {
                    return LayerType.FeatureServer; //矢量服务
                }
            case "4":
                return LayerType.WMSLayer;

            case "5":
                return LayerType.WFSLayer;

            case "6":
                return LayerType.WFSLayer;

            case "7":
                return LayerType.WMTSLayer;

            case "10":
                return LayerType.WMTSLayer;

            case "11":
                return LayerType.TianDiTu;

            case "12":
                return LayerType.Unsupported;

            case "13":
                return LayerType.Unsupported;

            case "14":
                return LayerType.Unsupported;

            case "15":
                return LayerType.Unsupported;

            default:
                return LayerType.Unsupported;
        }
    }


    /**
     * 对AMLayerInfo进行分组
     *
     * @param layerInfos 要进行分组的图层数据
     * @return 分组后的图层数据；key是每一组的组名，value是对应包含的图层
     */
    public static LinkedHashMap<String, List<LayerInfo>> getGroups(List<LayerInfo> layerInfos) {

        LinkedHashMap<String, List<LayerInfo>> groups = new LinkedHashMap<>();
        for (LayerInfo info : layerInfos) {
            String typeName = info.getDirTypeName();
            //如果存在就添加，不存在就创建
            if (groups.containsKey(typeName)) {
                groups.get(typeName).add(info);
            } else {
                List<LayerInfo> infoList = new ArrayList<>();
                infoList.add(info);
                groups.put(typeName, infoList);
            }
        }
        return groups;
    }


    /**
     * 获取分组的组名列表
     *
     * @param listMap
     * @return 组名列表
     */
    public static List<String> getGroupNameList(Map<String, List<LayerInfo>> listMap) {
        List<String> keys = new ArrayList<>();
        Set<Map.Entry<String, List<LayerInfo>>> entryset = listMap.entrySet();//通过entrySet方法获取封装键值对的对象
        Iterator<Map.Entry<String, List<LayerInfo>>> it = entryset.iterator();//获取迭代器对象
        while (it.hasNext()) {
            Map.Entry<String, List<LayerInfo>> me = it.next();//获取封装键值对的对象
            String key = me.getKey();   //通过getKey  getValue方法获取键与值
            //  LogUtil.d("Layer的组名：", key);
            keys.add(key);
        }
        return keys;
    }


    /**
     * 过滤掉非ArcGIS发布的图层
     *
     * @param allLayer
     * @return
     */
    public static List<LayerInfo> getArcgisServerLayer(List<LayerInfo> allLayer) {
        List<LayerInfo> arcgisServerLayer = new ArrayList<>();
        for (LayerInfo layerInfo : allLayer) {
            if (layerInfo.getUrl().contains("MapServer") || layerInfo.getUrl().contains("FeatureServer")) {
                arcgisServerLayer.add(layerInfo);
            }
        }
        return arcgisServerLayer;
    }

    /**
     * 过滤掉功能图层
     *
     * @param allLayer
     * @return
     */
    public static List<LayerInfo> getNormalLayer(List<LayerInfo> allLayer) {
        List<LayerInfo> normalLayers = new ArrayList<>();
        for (LayerInfo layerInfo : allLayer) {
            if (layerInfo.getRemarkFunc() == LayerConstant.NORMAL_LAYER) {
                normalLayers.add(layerInfo);
            }
        }
        return normalLayers;
    }

    public static List<LayerInfo> getActiveInBsLayer(List<LayerInfo> allLayer) {
        List<LayerInfo> activeInBsLayers = new ArrayList<>();
        for (LayerInfo layerInfo : allLayer) {
            if (layerInfo.isIfShowInLayerList()) {
                activeInBsLayers.add(layerInfo);
            }
        }
        return activeInBsLayers;
    }
}
