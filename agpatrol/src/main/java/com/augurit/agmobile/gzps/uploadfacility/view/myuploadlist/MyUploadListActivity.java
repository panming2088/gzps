package com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.setting.view.myupload.MyUploadStatiscPagerAdapter;

/**
 * 我的数据上报列表，最新的我的上报列表界面在{@link com.augurit.agmobile.gzps.setting.MyUploadStatisticActivity}
 *
 * Created by xcl on 2017/12/1.
 */
@Deprecated
public class MyUploadListActivity extends BaseActivity {

    private MyUploadStatiscPagerAdapter adapter;
    private DrawerLayout drawer_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadhistory);
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("我的上报列表");

//        findViewById(R.id.ll_search).setVisibility(View.VISIBLE);
//        ImageView iv_open_search = (ImageView) findViewById(R.id.iv_open_search);
//        iv_open_search.setImageResource(R.drawable.sel_btn_upload_layer);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            iv_open_search.setImageTintList(ColorStateList.valueOf(Color.WHITE));
//        }
//        ((TextView)findViewById(R.id.tv_search)).setText("我的上报");
//        findViewById(R.id.tv_search).setVisibility(View.VISIBLE);
//
//        findViewById(R.id.ll_search).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent =new Intent(MyUploadListActivity.this,UploadFacilityDetailMapActivity.class);
//                startActivity(intent);
//            }
//        });

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置右面的侧滑菜单只能通过编程来打开
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        adapter = new MyUploadStatiscPagerAdapter(getSupportFragmentManager(),
                this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(adapter);

        //TabLayout
        final TabLayout tabLayout = (TabLayout) findViewById(com.augurit.agmobile.gzps.R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
