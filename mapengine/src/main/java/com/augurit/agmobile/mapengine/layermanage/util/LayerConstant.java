package com.augurit.agmobile.mapengine.layermanage.util;


/**
 * 图层模块中的常量
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.layer.util.cnst
 * @createTime 创建时间 ：2016-10-14 13:59
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2016-10-14 13:59
 */
public final class LayerConstant {
    public static final String DYNAMICLAYER = "DynamicLayer";
    public static final String TILELAYER = "TileLayer";
    public static final String FEATURELAYER = "FeatureLayer";
    //默认显示
    public static final String DEFAULT_SHOW = "1";
    //图层类型
    public static final int PANO_LAYER = 1;//360全景
    public static final int EDIT_LAYER = 2;//编辑图层
    public static final int MARK_LAYER = 3;//标注图层
    public static final int GPS_LAYER = 4;//GPS轨迹图层
    public static final int NORMAL_LAYER = 0;//普通图层
    public static final int ROOUTE_SERVER_LAYER = 5; //路网服务
    public static final int GEOCODE_SERVER_LAYER = 6;//地名解析服务
    public static final String SP_KEY_PANO_LAYER = "PANO_LAYER"; //360全景在sp中的键名
    public static final String SP_KEY_EDIT_LAYER = "EDIT_LAYER";//编辑图层在sp中的键名
    public static final String SP_KEY_MARK_LAYER = "MARK_LAYER";//标注图层在sp中的键名
    public static final String SP_KEY_MARK_LAYER_POINT = "MARK_LAYER_POINT";//点标注图层在sp中的键名
    public static final String SP_KEY_MARK_LAYER_LINE = "MARK_LAYER_LINE";//线标注图层在sp中的键名
    public static final String SP_KEY_MARK_LAYER_POLYGON = "MARK_LAYER_POLYGON";//面标注图层在sp中的键名
    public static final String SP_KEY_GPS_LAYER = "GPS_LAYER";//GPS轨迹在sp中的键名
    public static final String SP_ROOUTE_SERVER_LAYER = "ROOUTE_SERVER_LAYER"; //路网服务
    public static final String SP_GEOCODE_SERVER_LAYER = "GEOCODE_SERVER_LAYER";//地名解析服务
    public static final String IS_BASE_MAP = "1";
    public static final String IF_QUERYABLE = "1";//图层是否可查询，0代表不可查询 1代表可查询
    public static final String IF_ACTIVEINBS = "1";//图层是否可查询，0代表不可查询 1代表可查询
    private LayerConstant() {
    }

}
