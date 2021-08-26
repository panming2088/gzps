package com.augurit.agmobile.mapengine.layermanage.util;


import com.augurit.agmobile.mapengine.layermanage.model.AgwebLayerType;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.layer.util
 * @createTime 创建时间 ：2017-07-11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-07-11
 * @modifyMemo 修改备注：
 */

public class AgwebLayerUtil {

    public static LayerType changeToLayerType(String layerType){

        switch (layerType) {

            case AgwebLayerType.TIANDITU:
                return LayerType.TianDiTu;

            case AgwebLayerType.ARCGIS_DYNAMIC :
                return LayerType.DynamicLayer;

            case AgwebLayerType.ARCGIS_TILED:
                return LayerType.TileLayer;

            case AgwebLayerType.ARCGIS_WFS:
                return LayerType.WFSLayer;

            case AgwebLayerType.ARCGIS_WMS:
                return LayerType.WMSLayer;

            case AgwebLayerType.ARCGIS_WMTS:
                return LayerType.WMTSLayer;

            case AgwebLayerType.ARCGIS_FEATURELAYER:
                return LayerType.FeatureLayer;

            default:
                return LayerType.Unsupported;
        }
    }
}
