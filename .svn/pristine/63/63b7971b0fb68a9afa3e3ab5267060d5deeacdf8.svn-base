package com.augurit.agmobile.mapengine.edit.view.presenter;

/**
 * 编辑几何编辑模块
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.am.map.arcgis.edit.prstr
 * @createTime 创建时间 ：16/11/22
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 16/11/22
 */

public interface IEditGeometryPresenter {

    void prepareForGeometryMovePartly();

    /**
     * 几何编辑 删除操作
     */
    void delPoint();

    /**
     * 几何编辑,整体拖动几何图形
     */
    void prepareForMoveAllGeo();

    /**
     * 几何编辑,整体拖动后保存
     */
 //   void moveAllSave();

    /**
     * 几何编辑,拖动后返回
     */
    void moveAllback();

    /**
     * 几何编辑,恢复按钮
     */
    void returnStep();

    /**
     * 几何编辑,撤销操作
     */
    void backStep();

    /**
     * 几何编辑,保存操作
     */
    void saveEdited();

    /**
     * 几何编辑,退出几何编辑
     */
    void outOfGeoEditing();

    void actionSaveAfterMoveAll();

    void outOfGeoMoveAllEditing();
}
