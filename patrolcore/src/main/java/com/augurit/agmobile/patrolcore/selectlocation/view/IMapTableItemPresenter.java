package com.augurit.agmobile.patrolcore.selectlocation.view;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.am.fw.common.IPresenter;
import com.esri.android.map.MapView;

/**
 * 地图选点表格项的presenter
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.map.view
 * @createTime 创建时间 ：2017-03-03
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-03
 * @modifyMemo 修改备注：
 */

public interface IMapTableItemPresenter extends IPresenter{

    void retryWhenLoadedFail();
    /**
     * 显示图层目录
     */
    void showLayerDirectory();
    /**
     * 加载地图
     */
    void loadMap();

    void showMapShortCut();

    void startMapActivity();

    void startWebViewMapActivity();

    void setReadOnly(LatLng location, String address);

    void setReEdit(LatLng location, String address);

    /**
     * 传入目的地坐标（采用的坐标系是：地图坐标系）
     * @param lng
     */
    void setDestinationOrLastTimeSelectLocationInLocalCoordinateSystem(LatLng lng, String address);

    void setOnReceivedSelectedLocationListener(OnReceivedSelectedLocationListener onReceivedSelectedLocationListener);

    void exit();

   // void parseLocation(LatLng latLng, Callback2<BaiduGeocodeResult> callback2);

    //void parseLocationByArcgis(LatLng latLng, Callback2<LocatorReverseGeocodeResult> callback2);

   // InputStream getMapShortCutStream();

    //void setMapShortCutStream(InputStream mapShortCutStream);

    LatLng getSelectedLocation();

    String getSelectedLocationAddress();

    IMapTableItem getView();

    void startLocate();

    MapView getMapView();

    interface OnReceivedSelectedLocationListener{
        void onReceivedLocation(LatLng mapLatlng, String address);
    }

}
