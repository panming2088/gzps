package com.augurit.agmobile.mapengine.layerdownload.view;

import com.esri.core.geometry.Geometry;

/**
 * 描述：图层下载工具视图接口
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerdownload.view
 * @createTime 创建时间 ：2017-02-15
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-15
 * @modifyMemo 修改备注：
 */
public interface ILayerDownloadToolView {

    /**
     * 显示工具按钮
     */
    void show();

    /**
     * 取消显示
     */
    void dismiss();

    /**
     * 得到当前圈选范围
     * @return 当前圈选范围
     */
    Geometry getCurrentGeometry();

}
