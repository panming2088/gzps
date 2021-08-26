package com.augurit.agmobile.patrolcore.common.opinion.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.opinion.model.OpinionTemplate;
import com.augurit.agmobile.patrolcore.common.opinion.view.presenter.IOpinionTemplateEditPresenter;
import com.augurit.agmobile.patrolcore.common.opinion.view.presenter.OpinionTemplateEditPresenter;
import com.augurit.am.cmpt.common.Callback1;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.view
 * @createTime 创建时间 ：2017-07-18
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-18
 * @modifyMemo 修改备注：
 */

public class EditOpinionTemplateActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_opinion_template);
        OpinionTemplate opinionTemplate = (OpinionTemplate) getIntent().getSerializableExtra("opinionTemplate");
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("编辑意见模板");
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        IOpinionTemplateEditView opinionTemplateEditView = new OpinionTemplateEditVeiw(this, container, opinionTemplate);
        IOpinionTemplateEditPresenter opinionTemplateEditPresenter
                = new OpinionTemplateEditPresenter(this, opinionTemplateEditView);
        opinionTemplateEditPresenter.setBackListener(new Callback1() {
            @Override
            public void onCallback(Object o) {
                finish();
            }
        });
    }
}
