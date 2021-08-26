package com.augurit.agmobile.gzps.track.view;

import android.os.Bundle;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.track.view.presenter.ITrackPresenter;
import com.augurit.agmobile.gzps.track.view.presenter.TrackPresenter;
import com.augurit.am.cmpt.common.Callback1;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.view
 * @createTime 创建时间 ：2017-06-14
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-14
 * @modifyMemo 修改备注：
 */

public class TrackActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        ITrackView trackView = new TrackView(this, container);
        ITrackPresenter trackPresenter = new TrackPresenter(this, trackView);
        trackPresenter.setBackListener(new Callback1() {
            @Override
            public void onCallback(Object o) {
                finish();
            }
        });
    }
}
