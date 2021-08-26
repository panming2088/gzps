package com.augurit.agmobile.mapengine.edit.util;


import com.augurit.agmobile.mapengine.edit.view.presenter.IEditGeometryPresenter;

/**
 * 持有 IEditGeometryPresenter 以便销毁
 *
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.am.map.arcgis.edit.util
 * @createTime 创建时间 ：16/12/2
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 16/12/2
 */

public class EditGeoPresenterUtil {
    public static EditGeoPresenterUtil instance;
    public IEditGeometryPresenter presenter;

    public static EditGeoPresenterUtil getInstance(){
        if(instance == null){
            instance = new EditGeoPresenterUtil();
        }
        return instance;
    }

    public IEditGeometryPresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(IEditGeometryPresenter presenter) {
        this.presenter = presenter;
    }

    public void clear(){
        presenter = null;
        instance = null;
    }
}
