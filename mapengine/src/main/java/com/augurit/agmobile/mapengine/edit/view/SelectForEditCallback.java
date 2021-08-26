package com.augurit.agmobile.mapengine.edit.view;

import com.esri.core.geometry.Geometry;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.edit.view
 * @createTime 创建时间 ：17/2/9
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/2/9
 * @modifyMemo 修改备注：
 */

public interface SelectForEditCallback {
    /**
     * 点击查询后回调函数
     *
     * @param queryResults
     */
    void onSelect(FeatureSet queryResults);
}
