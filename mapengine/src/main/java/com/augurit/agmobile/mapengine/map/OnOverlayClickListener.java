package com.augurit.agmobile.mapengine.map;


import com.augurit.agmobile.mapengine.map.graphic.Overlay;

/**
 * 地图覆盖物点击事件监听接口
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.map
 * @createTime 创建时间 ：2017-02-15
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-15
 * @modifyMemo 修改备注：
 */

public interface OnOverlayClickListener {
    void onOverlayClick(Overlay marker);
}
