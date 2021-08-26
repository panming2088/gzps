package com.augurit.am.cmpt.maintenance.view.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.maintenance.view.MaintenancePresenter;

/**
 * 描述：运维统计入口Activity
 * @author 创建人 ：liangsh
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.stats.view.activity
 * @createTime 创建时间 ：2016-09-27
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-08
 * @modifyMemo 修改备注：
 */
public class MaintenanceMenuActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintenance_activity);
        ViewGroup root_view = (ViewGroup) findViewById(R.id.root_view);
        MaintenancePresenter presenter = new MaintenancePresenter(this);
        presenter.setBackListener(new Callback1<Void>() {
            @Override
            public void onCallback(Void aVoid) {
                finish();
            }
        });
        presenter.show(root_view);
    }
}
