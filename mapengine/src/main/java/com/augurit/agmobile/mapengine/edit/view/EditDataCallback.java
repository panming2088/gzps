package com.augurit.agmobile.mapengine.edit.view;

import com.esri.core.map.FeatureEditResult;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.edit.view
 * @createTime 创建时间 ：17/2/7
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/2/7
 * @modifyMemo 修改备注：
 */

public interface EditDataCallback {
    void onSuccess(FeatureEditResult[][] result);
    void onFail(Throwable e);
}
