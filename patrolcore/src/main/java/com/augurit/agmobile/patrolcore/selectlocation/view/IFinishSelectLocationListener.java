package com.augurit.agmobile.patrolcore.selectlocation.view;

import com.augurit.agmobile.patrolcore.selectlocation.model.OnSelectLocationFinishEvent;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view
 * @createTime 创建时间 ：17/7/27
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/27
 * @modifyMemo 修改备注：
 */

public interface IFinishSelectLocationListener {

    void onFinish(OnSelectLocationFinishEvent onSelectLocationFinishEvent);
}
