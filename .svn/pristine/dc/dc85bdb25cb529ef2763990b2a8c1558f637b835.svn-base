package com.augurit.agmobile.patrolcore.selectlocation.view;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.am.fw.common.IPresenter;
import com.esri.core.geometry.SpatialReference;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view
 * @createTime 创建时间 ：17/7/27
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/27
 * @modifyMemo 修改备注：
 */

public interface ISelectLocationPresenter extends IPresenter {

    void requestLocation(double longitude, double latitude, SpatialReference spatialReference);

    void loadMap();

    /**
     * 关闭加载地图完成后自动定位功能,注意，请先调用该方法再调用{@link #loadMap()}方法；
     */
    void turnOffStartLocateWhenMapLoaded();

}
