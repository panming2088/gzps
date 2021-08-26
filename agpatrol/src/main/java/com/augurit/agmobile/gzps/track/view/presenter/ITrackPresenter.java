package com.augurit.agmobile.gzps.track.view.presenter;

import com.augurit.agmobile.gzps.track.service.TrackNewService;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.am.cmpt.common.Callback1;

import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.view.presenter
 * @createTime 创建时间 ：2017-06-14
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-14
 * @modifyMemo 修改备注：
 */

public interface ITrackPresenter {

    void switchState(@TrackNewService.TrackLocateState int trackState);
    void locate();
    void back();
//    void directFinish(boolean isDirect);
    void setBackListener(Callback1 backListener);
    int getCurrentTrackState();
    List<GPSTrack> getGPSTracks();
    double getTrackLength();
    int getTrackTime();
}
