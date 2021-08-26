package com.augurit.agmobile.gzps.track.view;

import com.augurit.agmobile.gzps.track.view.presenter.IShowTrackPresenter;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;

import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.view
 * @createTime 创建时间 ：2017-06-15
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-15
 * @modifyMemo 修改备注：
 */

public interface IShowTrackView {

    void setPresenter(IShowTrackPresenter showTrackPresenter);
    void showTrack(List<GPSTrack> track);
}
