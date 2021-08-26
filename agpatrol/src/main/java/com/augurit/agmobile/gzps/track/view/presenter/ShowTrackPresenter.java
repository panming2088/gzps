package com.augurit.agmobile.gzps.track.view.presenter;

import android.content.Context;

import com.augurit.agmobile.gzps.track.view.IShowTrackView;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;

import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.view.presenter
 * @createTime 创建时间 ：2017-06-15
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-15
 * @modifyMemo 修改备注：
 */

public class ShowTrackPresenter implements IShowTrackPresenter{

    private Context mContext;
    private IShowTrackView mShowTrackView;
    private List<GPSTrack> mTrack;

    public ShowTrackPresenter(Context context, IShowTrackView showTrackView, List<GPSTrack> track){
        this.mContext = context;
        this.mShowTrackView = showTrackView;
        this.mTrack = track;
        this.mShowTrackView.setPresenter(this);
        this.mShowTrackView.showTrack(track);
    }


}
