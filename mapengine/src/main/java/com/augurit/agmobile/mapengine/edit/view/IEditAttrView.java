package com.augurit.agmobile.mapengine.edit.view;

import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.augurit.agmobile.mapengine.edit.view.presenter.IEditShowAttrPresenter;
import com.esri.android.map.MapView;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.am.map.arcgis.edit.view
 * @createTime 创建时间 ：16/11/22
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 16/11/22
 */

public interface IEditAttrView {

    void setPresenter(IEditShowAttrPresenter presenter);

    /*
    void setAttachAdapter(AttachmentsListAdapter adapter);
    */
    void setAttachAdapter(BaseAdapter adapter);

    //删除要素
    void deleteFeature();

    //更新属性数据
    void updateFeature();

    //获取View
    void showAttrEditView(ViewGroup container, MapView mapView);

}
