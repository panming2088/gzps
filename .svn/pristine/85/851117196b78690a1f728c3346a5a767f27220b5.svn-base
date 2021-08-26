package com.augurit.agmobile.gzps.track.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.track.util.TrackConstant;
import com.augurit.agmobile.gzps.track.view.presenter.ITrackListPresenter;
import com.augurit.agmobile.gzps.track.view.presenter.TrackListPresenter;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.fw.db.AMDatabase;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.view
 * @createTime 创建时间 ：2017-08-22
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-22
 * @modifyMemo 修改备注：
 */

public class TrackListActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);
//        String userId = getIntent().getStringExtra("userId");
        User user = new LoginService(this, AMDatabase.getInstance()).getUser();
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        ITrackListView trackListView = new TrackListView(this, container);
        ITrackListPresenter trackListPresenter = new TrackListPresenter(this, trackListView, user.getLoginName());
        trackListPresenter.setBackListener(new Callback1() {
            @Override
            public void onCallback(Object o) {
                finish();
            }
        });
        trackListPresenter.setViewTrackListener(new Callback1() {
            @Override
            public void onCallback(Object o) {
                long trackId = (Long) o;
                Intent intent = new Intent(TrackListActivity.this, ShowTrackActivity.class);
                intent.putExtra("trackId", "" + trackId);
                intent.putExtra("operation", TrackConstant.TRACK_FROM_NET);
                startActivity(intent);
            }
        });
    }
}
