package com.augurit.agmobile.patrolcore.project.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.patrolcore.R;

/**
 * 项目选择UI,选择相应的项目进行信息录入上报
 */

public class ProjectListActivity extends AppCompatActivity {

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
        presenter = new ProjectPresenter(this,container, getIntent().getStringExtra("componentName"));
    }

    private void exitActivity() {
        ProjectListActivity.this.finish();
        overridePendingTransition(0, R.anim.out_toptobottom);
    }
}
