package com.augurit.agmobile.mapengine.layermanage.model;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.layer.model
 * @createTime 创建时间 ：2017-07-11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-07-11
 * @modifyMemo 修改备注：
 */

public final class AgwebLayerType {

    /*    public static final String POINT = "011";
    public static final String LINE = "012";
    public static final String POLYGON = "013";
    public static final String AGT = "021"; //栅格_AGT
    public static final String ARCGIS_DYNAMIC = "022"; //栅格arcgis动态
    public static final String ARCGIS_TILED = "023"; //栅格arcgis静态
    public static final String RASTER = "024"; // 栅格（2.5维度）
    public static final String PHOTO_MAP = "025";//影像地图
    public static final String WMS = "031";
    public static final String WFS = "032";*/

    public static final String TIANDITU = "020005"; //天地图瓦片
    public static final String BAIDU = "020006";//百度地图
    public static final String SOUGOU = "020007";

    public static final String ARCGIS_DYNAMIC = "020202";
    public static final String ARCGIS_TILED  = "020302";
    // public static final String ARCGIS_TILED_YINAGXIANG = "02060302" ;//arcgis瓦片
    //  public static final String ARCGIS_DYNAMIC_YINGXIANG = "02060202"; //arcgis动态

    // public static final String ARCGIS_FEATURE = "02040302";//arcgis

    public static final String ARCGIS_WFS ="040002"; //todo 在巡查项目中，featureLayer是使用wfs发布的
    //  public static final String ARCGIS_WFS_LINE = "04020002";
    //  public static final String ARCGIS_WFS_POLYGON = "04030002";

    public static final String ARCGIS_WMS = "030002";//arcgis wms栅格
    //  public static final String ARCGIS_WMS_YINGXIANG = "03060002" ;//arcgis wms影像

    public static final String ARCGIS_WMTS = "050002";

    public static final String ARCGIS_FEATURELAYER = "070002";
    //   public static final String ARCGIS_WMTS_YINGXIANG = "050600020";





    private AgwebLayerType() {
    }

    public String layerType = null;

    public AgwebLayerType(@LayerType String type) {
        layerType = type;
    }

    public String values(){
        return layerType;
    }


    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TIANDITU,ARCGIS_DYNAMIC,
            ARCGIS_TILED, ARCGIS_WFS,ARCGIS_WMS,ARCGIS_WMTS,ARCGIS_FEATURELAYER
    })
    public @interface LayerType {

    }
}
