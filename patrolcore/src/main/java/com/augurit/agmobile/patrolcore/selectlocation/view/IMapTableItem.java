package com.augurit.agmobile.patrolcore.selectlocation.view;

import android.content.Context;
import android.location.Location;
import android.view.View;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.common.IView;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;

import java.util.List;

/**
 * 地图选点表格项View
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.map.view
 * @createTime 创建时间 ：2017-03-08
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-08
 * @modifyMemo 修改备注：
 */

public interface IMapTableItem extends IView{

    void addMapShortCutToContainerWithRemoveAllViews();

    void setIfEditable(boolean ifEditable);

    Context getContext();

    void showEditableAddress(String detailedAddress);

    void showEditableMap(LatLng latLng,double scale);

    View getAddressEditTextContainer();

    void setMapInvisible();

    void showLocationOnMapWhenLayerLoaded(Point point, Callback1 callback1);

    void drawLocationOnMap(Point point);

  //  void showMapShortCut();

    MapView getMapView();

    void removeMapView();

    void hideSelectLocationButton();

    void showMapView();

    void setScale(double scale);

    View getRootView();

    void showUnEditableAddress(List<String> spinnerList);

    void setReadOnly(String address);

    boolean containsLocation(Location location);

    void addTableItemToMapItem(View view);

    void addGraphic(Graphic graphic,boolean ifClearOtherGraphic);

    void setGraphic(Graphic graphic);
}
