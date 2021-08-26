package com.augurit.agmobile.patrolcore.selectdevice.view;


import com.augurit.agmobile.patrolcore.selectdevice.model.Device;
import com.augurit.am.fw.common.IView;
import com.esri.core.geometry.Geometry;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.selectdevice
 * @createTime 创建时间 ：2017-03-29
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-29
 * @modifyMemo 修改备注：
 */

public interface IPatrolSelectDeviceView extends IView {


    void addSelectDeviceViewToContainerWithoutRemoveAllViews();

    void setDeviceName(String deviceName);

    void showLoading();

    void hideLoading();

    void setSuggestions(List<Device> suggestDevice);

    void notifySuggestionDevicesChanged(List<Device> newSuggestDevice);

    void jumpToSelectDeviceActivity();

    void setKey(String key);

    void showBottomSheetDialog();

    void setReadOnly(String deviceName);

    String getDeviceName();

    void jumpToReadOnlyDeviceActivity(String wkt, String deviceName);

    void jumpToReadOnlyDeviceActivity(Geometry geometry, String deviceName);

    void jumpToReEditDeviceActivity(String wkt, String deviceName);

    void jumpToReEditDeviceActivity(Geometry geometry, String deviceName);

    void hideJumpToMapButton();

    void resetBottomsheetClickListener();

    void setUnEditable();
}
