package com.augurit.agmobile.mapengine.legend.model;

import java.util.List;

/**
 * @author 创建人 ：luobiao
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.legend.model
 * @createTime 创建时间 ：17/8/31
 * @modifyBy 修改人 ：luobiao
 * @modifyTime 修改时间 ：17/8/31
 * @modifyMemo 修改备注：
 */

public class GroupLegend {
    private String parentName;
    private List<LayerLegend> layerLegends;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<LayerLegend> getLayerLegends() {
        return layerLegends;
    }

    public void setLayerLegends(List<LayerLegend> layerLegends) {
        this.layerLegends = layerLegends;
    }
}
