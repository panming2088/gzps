package com.augurit.agmobile.mapengine.map;

import com.augurit.agmobile.mapengine.map.graphic.Overlay;

/**
 * 绘制覆盖物的图层，相当于arcgis中的GraphicLayer
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.map
 * @createTime 创建时间 ：2017-02-16
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-16
 * @modifyMemo 修改备注：
 */

public interface IOverlayCanvas {

    /**
     * 在该图层上绘制覆盖物
     * @param overlay 要绘制的覆盖物
     * @return  覆盖物的id ，用于要移除特定的覆盖物时使用
     */
    int  addOverlayer(Overlay overlay);


    void removeOverlay(int overlayId);
}
