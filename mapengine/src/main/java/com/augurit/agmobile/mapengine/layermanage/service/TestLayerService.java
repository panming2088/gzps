package com.augurit.agmobile.mapengine.layermanage.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于帮助测试点查功能的LayerService
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layermanage.service
 * @createTime 创建时间 ：2017-02-10
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-10
 * @modifyMemo 修改备注：
 */

public class TestLayerService extends LayersService {

    public TestLayerService(Context context) {
        super(context);
    }

    @Override
    public List<LayerInfo> getVisibleQueryableLayers() {
        List<LayerInfo> queryableLayers = new ArrayList<>();
        LayerInfo amLayerInfo = new LayerInfo();
        String url = "http://192.168.20.114:6080/arcgis/rest/services/hd/hd_jmd_map_display/MapServer";
        amLayerInfo.setUrl(url);
        queryableLayers.add(amLayerInfo);

        List<LayerInfo> childs =new ArrayList<>();

        LayerInfo child1 = new LayerInfo();
        child1.setLayerId(0);
        childs.add(child1);

        LayerInfo child2 = new LayerInfo();
        child1.setLayerId(1);
        childs.add(child2);

        LayerInfo child3 = new LayerInfo();
        child1.setLayerId(2);
        childs.add(child3);

        LayerInfo child4 = new LayerInfo();
        child1.setLayerId(3);
        childs.add(child4);

        amLayerInfo.setChildLayer(childs);

        return queryableLayers;
    }
}
