package com.augurit.agmobile.patrolcore.localhistory.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.RefreshLocalEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class LocalProblemActivity extends AppCompatActivity{
    private ILocalProblemPresenter problemPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_problem);

        org.greenrobot.eventbus.EventBus.getDefault().register(this);//订阅

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitActivity();
            }

        });
        ((TextView)findViewById(R.id.tv_title)).setText("本地保存");
        ViewGroup container = (ViewGroup)findViewById(R.id.container);
        ViewGroup menuContainer = (ViewGroup)findViewById(R.id.menu_container);
        ViewGroup selectBtn = (ViewGroup) findViewById(R.id.select_btn);
        menuContainer.setVisibility(View.GONE);
        problemPresenter = new LocalProblemPresenter(this,container,menuContainer,selectBtn);
    }



    private void exitActivity() {
        LocalProblemActivity.this.finish();
        overridePendingTransition(0, R.anim.out_toptobottom);
    }


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRefreshLocalEvent(RefreshLocalEvent event) {
       problemPresenter. showLocalProblemListView();
    }
}
