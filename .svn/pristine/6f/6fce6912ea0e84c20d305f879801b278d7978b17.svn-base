package com.augurit.agmobile.gzps.trackmonitor.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.trackmonitor.view.presenter.IOnlineMonitorPresenter;
import com.augurit.agmobile.gzps.trackmonitor.view.presenter.OnlineMonitorPresenter;
import com.augurit.am.cmpt.common.Callback1;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.trackmonitor.view
 * @createTime 创建时间 ：2017-08-21
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-21
 * @modifyMemo 修改备注：
 */

public class OnlineMonitorActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_monitor);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        IOnlineMonitorView onlineMonitorView = new OnlineMonitorView(this, container);
        IOnlineMonitorPresenter onlineMonitorPresenter = new OnlineMonitorPresenter(this, onlineMonitorView);
        onlineMonitorPresenter.setBackListener(new Callback1() {
            @Override
            public void onCallback(Object o) {
                finish();
            }
        });
        onlineMonitorPresenter.setShowSubordinateListListener(new Callback1() {
            @Override
            public void onCallback(Object o) {
                Intent intent = new Intent(OnlineMonitorActivity.this, SubordinateListActivity.class);
                startActivity(intent);
            }
        });
    }
}
