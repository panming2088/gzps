package com.augurit.agmobile.patrolcore.selectlocation.view.tableitem;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.common.IView;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view
 * @createTime 创建时间 ：17/11/1
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/1
 * @modifyMemo 修改备注：
 */

public interface ISelectLocationTableItemView extends IView {

    void addTo(ViewGroup container);

    Context getContext();

    void setAddress(List<String> spinnerList);

    void setMapCenter(Point center);

    View getAddressEditTextContainer();

    void hideMapView();

    void showLocationOnMapWhenLayerLoaded(Point point, Callback1 callback1);

    void drawLocationOnMap(Point point);

    //  void showMapShortCut();

    MapView getMapView();

    void removeMapView();

    void hideSelectLocationButton();

    void showMapView();

    void setScale(double scale);

    View getRootView();

    boolean containsLocation(Location location);

    void addView(View view);

    void addGraphic(Geometry geometry, boolean ifClearOtherGraphic);

    void registerMapViewInitializedCallback(Callback1<Boolean> callback1);

    /**
     * MapView是否初始化完成
     *
     * @return
     */
    boolean ifMapInitialized();

    void loadMap();
}
