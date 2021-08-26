package com.augurit.agmobile.gzps.track.view.presenter;

import com.augurit.am.cmpt.common.Callback1;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.view.presenter
 * @createTime 创建时间 ：2017-08-22
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-22
 * @modifyMemo 修改备注：
 */

public interface ITrackListPresenter {

    void loadMore();
    void back();
    void setBackListener(Callback1 backListener);
    void viewTrack(long trackId);
    void setViewTrackListener(Callback1 viewTrackListener);
}
