package com.augurit.agmobile.mapengine.common.utils;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.am.fw.utils.ListUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.utils
 * @createTime 创建时间 ：2017-04-05
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-05
 * @modifyMemo 修改备注：
 */

public class EsriUtil {

    public static Map<String, int[]> getFindableOrIdentifiableLayer(LayerInfo info) {
        Map<String, int[]> layerMap = new HashMap<>();
        List<LayerInfo> childLayer = info.getChildLayer();
        if (!ListUtil.isEmpty(childLayer)) {
            int[] childIds = new int[childLayer.size()];
            for (int i = 0; i < childLayer.size(); i++) {
                childIds[i] = childLayer.get(i).getLayerId();
            }
            layerMap.put(info.getUrl(), childIds);
        } else {
            if (info.getType() == LayerType.FeatureLayer) {
                //如果是featureLayer，那么也是可以查询的
                String[] split = info.getUrl().split("/");
                String lastString = split[split.length - 1]; //获取到最后的字符串
                //判断是否包含“FeatureServer”
                boolean ifContainsFeatureServer = lastString.contains("FeatureServer");
                if (ifContainsFeatureServer) {
                    //如果是xxx/FeatureServer结尾的，在获取图层的时候已经把它们转换成MapServer并获取到它的子图层了，所以这里不需要考虑
                } else {
                    //那么是xxx/FeatureServer/0的形式
                    String serviceUrl = info.getUrl().substring(0, info.getUrl().lastIndexOf("/"));
                    // LogUtil.d("获取到的serviceUrl是：" + serviceUrl);
                    int childId = Integer.valueOf(lastString);
                    int[] childLayerId = new int[]{childId};
                    String mapserviceUrl = serviceUrl.replace("FeatureServer", "MapServer");
                    //put的时候会覆盖,所以要先判断是否存在，如果存在，那么进行取出添加
                    int[] ints = layerMap.get(mapserviceUrl);
                    if (ints != null) {
                        //取出进行添加
                        int[] newIds = new int[ints.length + 1];
                        for (int i = 0; i < ints.length; i++) {
                            newIds[i] = ints[i];
                        }
                        newIds[ints.length] = childId;
                        layerMap.put(mapserviceUrl, newIds);
                    } else {
                        layerMap.put(mapserviceUrl, childLayerId);
                    }
                }
            }
        }
        return layerMap;
    }
}
