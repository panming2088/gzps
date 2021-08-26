package com.augurit.agmobile.gzps.statistic.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;

public class SignDetailActivity extends BaseActivity {
    SignFragmentPagerAdapter adapter;
    private String org_name;
    private boolean   roleType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_detail);
        org_name = getIntent().getStringExtra("org_name");
        roleType = getIntent().getBooleanExtra("roleType",false);
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.tv_title)).setText(org_name+"签到情况名录");
        final ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager .setOffscreenPageLimit(2);
        adapter = new SignFragmentPagerAdapter(this.getSupportFragmentManager(),
                this);
        viewPager.setAdapter(adapter);

        //TabLayout
        final TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }
}
