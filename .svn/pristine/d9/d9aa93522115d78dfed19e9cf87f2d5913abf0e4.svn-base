package com.augurit.agmobile.gzps.common.project;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.project.view.IProjectPresenter;

/**
 * 修改ProjectPresenter的实现类
 */

public class ProjectListActivity2 extends BaseActivity {

    private IProjectPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitActivity();
            }

        });
        ((TextView)findViewById(R.id.tv_title)).setText("项目选择");

        ViewGroup container = (ViewGroup)findViewById(R.id.container);
        presenter = new ProjectPresenter2(this,container, getIntent().getStringExtra("componentName"));
    }

    private void exitActivity() {
        ProjectListActivity2.this.finish();
        overridePendingTransition(0, R.anim.out_toptobottom);
    }
}
