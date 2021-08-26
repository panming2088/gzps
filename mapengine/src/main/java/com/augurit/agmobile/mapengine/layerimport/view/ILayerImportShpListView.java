package com.augurit.agmobile.mapengine.layerimport.view;

import com.augurit.agmobile.mapengine.layerimport.model.Document;

import java.util.List;

/**
 * 描述：图层导入图形列表视图
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerimport.view
 * @createTime 创建时间 ：2017-02-10
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-10
 * @modifyMemo 修改备注：
 */
public interface ILayerImportShpListView {

    /**
     * 更新图形列表
     * @param shps 图形文档List
     */
    void updateList(List<Document> shps);
}
