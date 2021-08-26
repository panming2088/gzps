package com.augurit.agmobile.patrolcore.common.opinion.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.opinion.model.OpinionTemplate;
import com.augurit.agmobile.patrolcore.common.opinion.view.presenter.IOpinionTemplateListPresenter;
import com.augurit.agmobile.patrolcore.common.opinion.view.presenter.OpinionTemplateListPresenter;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.hp.hpl.sparta.Text;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.view
 * @createTime 创建时间 ：2017-07-17
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-17
 * @modifyMemo 修改备注：
 */

public class OpinionTemplateListActivity extends AppCompatActivity {

    OpinionTemplateListPresenter opinionTemplateListPresenter;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_template_list);
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("意见模板列表");
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        IOpinionTemplateListView opinionTemplateListView = new OpinionTemplateListView(this, container);
        opinionTemplateListPresenter = new OpinionTemplateListPresenter(this, opinionTemplateListView);
        opinionTemplateListPresenter.setOnTemplateSelectListener(new Callback1<OpinionTemplate>() {
            @Override
            public void onCallback(OpinionTemplate opinionTemplate) {
//                ToastUtil.shortToast(OpinionTemplateListActivity.this, opinionTemplate.getName());
                Intent intent = new Intent(OpinionTemplateListActivity.this, EditOpinionTemplateActivity.class);
                intent.putExtra("opinionTemplate", opinionTemplate);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        opinionTemplateListPresenter.refresh();
    }
}
