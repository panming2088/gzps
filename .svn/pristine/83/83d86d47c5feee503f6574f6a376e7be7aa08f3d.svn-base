package com.augurit.agmobile.gzps.workcation.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;

/**
 * Created by liangsh on 2017/11/21.
 */

public class LocalDraftActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_draft);
        initView();
    }

    private void initView(){
        ((TextView)findViewById(R.id.tv_title)).setText("本地草稿");
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        LocalDraftPagerAdapter adapter = new LocalDraftPagerAdapter(getSupportFragmentManager(),
                this);
        viewPager.setAdapter(adapter);

        //TabLayout
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
//                    setIndicator(getContext(), tabLayout, 65, 65);
            }
        });
    }
}
