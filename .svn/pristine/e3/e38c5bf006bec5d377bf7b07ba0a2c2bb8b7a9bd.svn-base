package com.augurit.agmobile.mapengine.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ArcGIS服务URL相关工具类。功能有判断URL指向的是否为子图层，从子图层URL中获取子图层ID等
 * Created by liangsh on 2016-12-13.
 */
public final class LayerUrlUtil {
    private LayerUrlUtil() {
    }

    /**
     * 判断一个URL是否为子图层URL
     *
     * @param layerUrl 图层URL
     * @return
     */
    public static boolean isChildLayer(String layerUrl) {
        //        layerUrl.endsWith("Server");
        //正则表达式，区配URL以 Server/+数字 结尾
        Pattern pattern = Pattern.compile(".*Server/\\d+");
        Matcher m = pattern.matcher(layerUrl);
        return m.matches();
    }

    /**
     * 从一个图层URL中获取图层的服务URL
     * 如 http://127.0.0.1:6080/arcgis/rest/services/hd/ditu/MapServer/1
     * 其中http://127.0.0.1:6080/arcgis/rest/services/hd/ditu/MapServer 即为服务URL，1为图层ID
     *
     * @param layerUrl
     * @return
     */
    public static String getServerUrlFromLayerUrl(String layerUrl) {
        //http://127.0.0.1:6080/arcgis/rest/services/hd/ditu/MapServer/1
        //把URL分为三组，第一组为最后一个 "/" 字符之前的所有字符，第二组为 "/" 字符，第三组为子图层ID
        String pattern = "(.*Server)(/)(\\d+)";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(layerUrl);
        if (m.find()) {
            //m.group(0) 是一个特殊的组，值为原字符串
            return m.group(1);
        } else {
            return layerUrl;
        }
    }

    /**
     * 从一个图层URL中获取图层ID
     * 如 http://127.0.0.1:6080/arcgis/rest/services/hd/ditu/MapServer/1
     * 其中http://127.0.0.1:6080/arcgis/rest/services/hd/ditu/MapServer 为服务URL，1为图层ID
     *
     * @param layerUrl
     * @return
     */
    public static int getChildLayerIdFromLayerUrl(String layerUrl) {
        if (!isChildLayer(layerUrl)) {
            return -1;
        }
        //http://127.0.0.1:6080/arcgis/rest/services/hd/ditu/MapServer/1
        //把URL分为三组，第一组为最后一个 "/" 字符之前的所有字符，第二组为 "/" 字符，第三组为子图层ID
        String pattern = "(.*)(/)(\\d+)";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(layerUrl);
        if (m.find()) {
            //m.group(0) 是一个特殊的组，值为原字符串
            return Integer.valueOf(m.group(3));
        } else {
            return -1;
        }
    }

}
