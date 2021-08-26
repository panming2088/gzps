package com.augurit.agmobile.mapengine.layermanage.util;


import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;

import java.util.Comparator;


/**
 * 进行比较LayerInfo的顺序
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.layer.util
 * @createTime 创建时间 ：2016-10-14 13:59
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2016-10-14 13:59
 */
public class LayerComparator implements Comparator<LayerInfo> {

    @Override
    public int compare(LayerInfo lhs, LayerInfo rhs) {
        if (lhs == null || rhs == null){
            return  -1;
        }

        if (lhs.getDirOrder() != rhs.getDirOrder()){
            return lhs.getDirOrder() < rhs.getDirOrder() ? -1:1;
        }

        return lhs.getLayerOrder() < rhs.getLayerOrder() ? -1:1;
    }
}
