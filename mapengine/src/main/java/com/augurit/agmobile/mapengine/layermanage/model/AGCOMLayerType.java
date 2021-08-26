package com.augurit.agmobile.mapengine.layermanage.model;

/**
 * 创建时间:2016-08-18
 * 创建人： xuciluan
 * 功能描述：AGCOM支持的地图服务类型
 */
/**
 * AGCOM支持的地图服务类型
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.layer.model.enums
 * @createTime 创建时间 ：2016-10-14 13:55
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2016-10-14 13:55
 */
public enum AGCOMLayerType {

     AST("1")//ARCGIS REST栅格服务(动态)（切片）
    ,AST_C("1")//ARCGIS REST栅格服务(静态)（切片）
    ,AGT("1")//奥格瓦片栅格服务（切片），目录十六进制字母为小写
    // TODO: weiqiuyue  2016-01-14  针对第三方瓦片目录大写字母的暂时处理：AGTB
    , AGTB("1")//奥格瓦片栅格服务（切片），利用第三方工具导出，目录十六进制字母为大写
    , ASR("1")//ARCGIS瓦片栅格服务（静态）（切片）
    , WMS("4")//WMS服务
    , WFS("5")//WFS服务
    , NEW_MAP_WFS("6")//NEW MAP WFS服务
    , WMTS("7")//OGC_WMTS服务（切片）
    , NEW_MAP_WMTS("10")//NEW MAP WMTS服务（切片）
    , TDT("11")//天地图（切片）
    , SOGOU("12")//搜狗地图（切片）
    , SOSO("13")//QQ地图（切片）
    , MAPABC("14")//MapABC地图（切片）
    , BAIDU("15"), ;//百度地图（切片）,

     String mLayerType;
     AGCOMLayerType(String layerType){
         mLayerType = layerType;
    }

    public String getLayerType() {
        return mLayerType;
    }

}
