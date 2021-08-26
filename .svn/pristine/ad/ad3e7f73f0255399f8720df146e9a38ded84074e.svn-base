package com.augurit.agmobile.gzps.uploadfacility.view.feedback;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.am.cmpt.common.Callback1;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.view.feedback
 * @createTime 创建时间 ：2018-03-08
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2018-03-08
 * @modifyMemo 修改备注：
 */

public class FacilityFeedbackListActivity extends BaseActivity {

    FacilityFeedbackListView feedbackListView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        long aid = getIntent().getLongExtra("id", -1);
        String tableType = getIntent().getStringExtra("tableType");
        final TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("反馈列表");
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        feedbackListView = new FacilityFeedbackListView(this, container, aid, tableType);
        feedbackListView.setLoadCompletedListener(new Callback1<Integer>() {
            @Override
            public void onCallback(Integer integer) {
                tv_title.setText("反馈列表（" + integer + "条）");
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        feedbackListView.loadData();
    }
}
