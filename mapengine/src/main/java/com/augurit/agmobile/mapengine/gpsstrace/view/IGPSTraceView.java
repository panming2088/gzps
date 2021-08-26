package com.augurit.agmobile.mapengine.gpsstrace.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.common.ILoadingView;
import com.augurit.am.fw.common.IView;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.gpstrace1.view
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public interface IGPSTraceView extends IView,ILoadingView {


    void showDialogWhenGpsIsOff();

    MapView getMapView();

    void showLocationOnMap(Point point);

    /**
     * 展示输入GPS名称的弹窗
     */
    void showInputTrackNameDialog();

    /**
     * 当无法获取到GPS时，改变GPS状态的图标
     */
    void refreshGpsStateIconWhenGpsOff();

    /**
     * 当无法获取定位权限时要做的事情
     */
    void showDialogWhenLocationPermissionDenied();

    /**
     * 当当前定位点不在地图内时要做的事情
     */
    void showMessageWhenLocationOutOfBounds();

    /**
     * 弹窗询问是否开始GPS轨迹记录
     */
    void showDialogToAskIfStartGpsTrack();

    /**
     * 初始化gps轨迹操作视图
     */
    void initGpsTraceToolView();


    void showTrackList(List<List<GPSTrack>> tracks, String length, String time);


    void drawPathOnMapAndThenCenter(List<Point> points);

    void setTrackListContainer(ViewGroup viewGroup);

    void init();

    void notifyForTitleDataChange(String duration, String length);

    void destroy();

    Context getActivity();

    void showGpsTraceTool();

    void showGpsTrackList();

    void addGpsTraceViewToContainer();
}
