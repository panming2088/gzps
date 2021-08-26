package com.augurit.agmobile.patrolcore.editmap;

import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;

/**
 *
 * 当编辑地图属性完成后的回调
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/10/15
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/15
 * @modifyMemo 修改备注：
 */

public interface OnEditMapFeatureCompleteCallback<T> {
    void onFinished(T data , DetailAddress detailAddress);
}
