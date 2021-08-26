package com.augurit.agmobile.patrolcore.layer.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;


import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.agmobile.patrolcore.layer.model.PatrolLayerType;
import com.augurit.am.fw.utils.ResourceUtil;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.map
 * @createTime 创建时间 ：2017-03-06
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-06
 * @modifyMemo 修改备注：
 */

public class PatrolLayerUtil {

    public static final String FOLDER_NAME = "common_ic_folder";

    public static Drawable getDrawableFromIconName(Context context, String iconName){
        Drawable icon = null;
        switch (iconName){
            case "": //如果为空，默认返回文件夹图标
            case "icon-folder":
                String mipmapName = FOLDER_NAME;
                int mipMapId = ResourceUtil.getMipMapId(context, mipmapName);
                icon = ContextCompat.getDrawable(context, mipMapId);
                break;
        }
        return icon;
    }

    public static LayerType changeToLayerType(String layerType){

        switch (layerType) {

            case PatrolLayerType.TIANDITU:
                return LayerType.TianDiTu;

            case PatrolLayerType.ARCGIS_DYNAMIC :
                return LayerType.DynamicLayer;


            case PatrolLayerType.ARCGIS_TILED:
                return LayerType.TileLayer;


            case PatrolLayerType.ARCGIS_WFS:
                return LayerType.WFSLayer;


            case PatrolLayerType.ARCGIS_WMS:
                return LayerType.WMSLayer;

            case PatrolLayerType.ARCGIS_WMTS:
                return LayerType.WMTSLayer;

            case PatrolLayerType.ARCGIS_FEATURELAYER:
                return LayerType.FeatureLayer;

            default:
                return LayerType.Unsupported;
        }
    }
}
