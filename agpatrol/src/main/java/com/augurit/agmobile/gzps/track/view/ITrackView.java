package com.augurit.agmobile.gzps.track.view;

import com.augurit.agmobile.gzps.track.view.presenter.ITrackPresenter;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.view
 * @createTime 创建时间 ：2017-06-14
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-14
 * @modifyMemo 修改备注：
 */

public interface ITrackView {

    void initState(int currentTrackState);
    void setPresenter(ITrackPresenter trackPresenter);
    void showMessage(String msg);
    void showTrackRecordOnMapView(ArrayList<GPSTrack> gpsTrackList, String operation);
    void setTrackHistory(List<GPSTrack> trackHistory);
    void onLoadMore(List<List<GPSTrack>> moreTrack);
    void setTrackLength(double trackLength);
    void setTrackTime(int trackTime);
    void setTrackPoint(GPSTrack trackPoint);
    void switchState(int trackTime);
}
