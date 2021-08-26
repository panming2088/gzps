package com.augurit.agmobile.mapengine.edit.service;

import com.augurit.agmobile.mapengine.edit.util.EditMode;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureTemplate;

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

public interface IAddEditService {

    /**
     * 增加要素
     * @param points
     * @param template
     * @param mode
     * @param sp
     * @param callback
     */
    void saveAfterAdding(List<Point> points, FeatureTemplate template, EditMode mode, SpatialReference sp, final AddEditService.AddCallback callback);
}
