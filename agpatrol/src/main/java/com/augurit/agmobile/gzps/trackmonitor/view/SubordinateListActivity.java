package com.augurit.agmobile.gzps.trackmonitor.view;

import android.os.Bundle;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.trackmonitor.view.presenter.ISubordinateListPresenter;
import com.augurit.agmobile.gzps.trackmonitor.view.presenter.SubordinateListPresenter;
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

public class SubordinateListActivity extends BaseActivity {

    ISubordinateListPresenter subordinateListPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinate_list);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        ISubordinateListView subordinateListView = new SubordinateListView(this, container);
        subordinateListPresenter = new SubordinateListPresenter(this, subordinateListView);
        subordinateListPresenter.setBackListener(new Callback1() {
            @Override
            public void onCallback(Object o) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        subordinateListPresenter.back();
    }
}
