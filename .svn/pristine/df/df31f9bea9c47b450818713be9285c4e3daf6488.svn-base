package com.augurit.agmobile.patrolcore.selectdevice.view;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.selectdevice.model.Device;
import com.augurit.am.fw.common.IPresenter;
import com.esri.android.map.MapView;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.selectdevice
 * @createTime 创建时间 ：2017-03-29
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-29
 * @modifyMemo 修改备注：
 */

public interface IPatrolSelectDevicePresenter extends IPresenter {

    void showSelectDeviceView();

    void startAutoComplete(MapView mapView,LatLng location);

    void search(String text);

    void setDeviceNameKey(String key);

    void setDeviceIdKey(String key);

    void setOnReceivedSelectedDeviceListener(OnReceivedSelectedDeviceListener onReceivedSelectedDeviceListener);

    IPatrolSelectDeviceView getView();

    void setReadOnly(String deviceId, String deviceNames);

    void setReEdit(String deviceId, String deviceNames);

    String getDeviceName();

    void loadDeviceWkt(boolean ifReadOnly);

    void exit();

    String getCurrentDeviceName();

    String getCurrentDeviceId();

    void setUnEditable();

    void searchNearByDevice(MapView mapView, double longitude, double latitude);

    Device getCurrentDevice();
}
