package com.augurit.agmobile.mapengine.edit.view.presenter;

import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.augurit.agmobile.mapengine.edit.view.EditDataCallback;
import com.esri.android.map.ags.ArcGISFeatureLayer;

import java.io.File;

/**
 * 编辑操作展示操作数据模块
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.am.map.arcgis.edit.prstr
 * @createTime 创建时间 ：16/11/22
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 16/11/22
 */

public interface IEditShowAttrPresenter {
    /**
     * 准备附件编辑
     */
    void prepareForAttachmentEdit();

    /**
     * 删除附件
     * @param ids
     */
    void deleteAttachment(int[] ids);

    /**
     * 上传附件
     * @param file
     */
    void addAttachementForFeature(File file);


    /**
     * 准备进去几何编辑,进行移动编辑
     */
    void prepareForGeometryMovePartly();

    /**
     * 删除要素
     * @param featureLayer
     * @param id
     * @param callback
     */
    void deleteFeature(ArcGISFeatureLayer featureLayer, Object id, EditDataCallback callback);

    /**
     * 更新要素属性数据
     */
    void updateFeature();

    /*
    void  showEditAttrListView(ArcGISFeatureLayer featureLayer,
                               Object featureId,
                               AttributeListAdapter adapter,
                               ViewGroup container);
                               */

    void  showEditAttrListView(ArcGISFeatureLayer featureLayer,
                               Object featureId,
                               BaseAdapter adapter,
                               ViewGroup container);

}
