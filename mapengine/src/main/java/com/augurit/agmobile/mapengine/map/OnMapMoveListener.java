package com.augurit.agmobile.mapengine.map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.map
 * @createTime 创建时间 ：2017-02-21
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-21
 * @modifyMemo 修改备注：
 */

public interface OnMapMoveListener {

    boolean onMapMove(float fromScreenX, float fromScreenY, float toScreenX, float toScreenY);

    boolean onMoveOver(float fromScreenX, float fromScreenY, float toScreenX, float toScreenY);
}
