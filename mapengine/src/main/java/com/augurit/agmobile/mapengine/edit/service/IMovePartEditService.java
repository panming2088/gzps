package com.augurit.agmobile.mapengine.edit.service;

import com.augurit.agmobile.mapengine.edit.util.EditMode;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import java.util.List;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.edit.service
 * @createTime 创建时间 ：17/2/9
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/2/9
 * @modifyMemo 修改备注：
 */

public interface IMovePartEditService {

    /**
     * 部分移动(点改变)后提交保存到服务
     * @param featureLayer
     * @param featureId
     * @param points
     * @param mode
     * @param sp
     * @param callback
     */
    void saveAfterMovePart(ArcGISFeatureLayer featureLayer,
                           Object featureId,
                           List<Point> points,
                           EditMode mode,
                           SpatialReference sp,
                           MovePartEditService.PartMoveCallback callback);
}
