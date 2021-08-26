package com.augurit.agmobile.patrolcore.editmap;

import android.view.ViewGroup;

import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.view.IFinishSelectLocationListener;
import com.augurit.am.fw.common.IView;
import com.esri.core.geometry.Geometry;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view
 * @createTime 创建时间 ：17/7/27
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/27
 * @modifyMemo 修改备注：
 */

public interface IEditMapFeatureView extends IView {

    void showAddressNotFound();

    void showAddress(DetailAddress address);

    void setIfReadOnly(boolean mIfReadOnly);

    void setDestinationOrLastTimeSelectLocation(Geometry mDestinationOrLastTimeSelectLocation);

    void setLastSelectedAddress(String mLastSelectedAddress);

    void setInitialScale(double mInitialScale);

    void loadMap();

    /**
     * 选择位置结束时的回调
     * @param finishSelectLocationListener 选择位置结束时的回调
     */
    void setFinishSelectLocationListener(IFinishSelectLocationListener finishSelectLocationListener);

    void turnOffStartLocateWhenMapLoaded();

    void stopLocate();

    void addViewToContaner();

    void showLayerList(ViewGroup container);

    void setEditMode(String editMode);

    void recycleMapView();

    void setFacilityOriginLocation(Geometry geometry);
}
