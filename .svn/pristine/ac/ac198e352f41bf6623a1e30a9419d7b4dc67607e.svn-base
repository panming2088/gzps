package com.augurit.agmobile.patrolcore.editmap;

import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.esri.core.map.Graphic;

/**
 *
 * 编辑管线时，当管线发生变化时的回调
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/11/1
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/1
 * @modifyMemo 修改备注：
 */

public interface OnGraphicChangedListener {

    /**
     * 只要地图上绘制的图形发生变化都会回调，不管画的是点还是线；
     * @param graphic
     */
    void onGraphicChanged(Graphic graphic);

    void onAddressChanged(DetailAddress detailAddress);

    /**
     * 当地图上的图形被全部清空时
     */
    void onGraphicClear();
}
