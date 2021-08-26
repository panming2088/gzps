package com.augurit.agmobile.gzps.statistic.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.statistic.service.StatisticService;

/**
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.statistic
 * @createTime 创建时间 ：2017/8/15
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/8/15
 * @modifyMemo 修改备注：
 */

public class StatisticActivity extends BaseActivity {

    private StatisticPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        ViewGroup container = (ViewGroup) findViewById(R.id.view_container);
        View btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        StatisticView statisticView = new StatisticView(this, container);
        StatisticService statisticService = new StatisticService(this);
        mPresenter = new StatisticPresenter(this, statisticView, statisticService);
        mPresenter.start();
    }

}
