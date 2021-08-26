package com.augurit.agmobile.gzps.uploadfacility.view.facilitydetail;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.monitor.model.InspectionWellMonitorInfo;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;

/**
 * 新增关键节点详情
 *
 * @author 创建人 ：huangchongwu
 * @package 包名 ：com.augurit.agmobile.gzps
 * @createTime 创建时间 ：2020/09/17
 * @version 1.0
 */

public class UploadKeyNodeMonitorDetailActivity extends BaseActivity implements IDrawerController {

    private DrawerLayout drawer_layout;
    private ViewGroup progress_linearlayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.remove("android:support:fragments");  //注意：基类是Activity时参数为android:fragments， 一定要在super.onCreate函数前执行！！！
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifiedfacilitymap);
        progress_linearlayout = (ViewGroup) findViewById(R.id.progress_linearlayout);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置右面的侧滑菜单只能通过编程来打开
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END);
        ((TextView)findViewById(R.id.tv_title)).setText("上报详情");
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        InspectionWellMonitorInfo uploadFacility = (InspectionWellMonitorInfo) getIntent().getParcelableExtra("data");
        int isShowCancelDeleteButton = getIntent().getIntExtra("isShowCancelDeleteButton",0);

        Bundle bundle = new Bundle();
        bundle.putParcelable("uploadedFacility", (Parcelable) uploadFacility);
        bundle.putInt("isShowCancelDeleteButton",isShowCancelDeleteButton);
        UploadKeyNodeMonitorDetailMapFragment uploadDistributeMapFragment = UploadKeyNodeMonitorDetailMapFragment.getInstance(bundle);
       // final UploadFacilityDetailtFragment problemListMapFragment = UploadFacilityDetailtFragment.getInstance(uploadFacility);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
       // problemListMapFragment.setLocate(false);
        fragmentTransaction.replace(com.augurit.agmobile.patrolcore.R.id.ly_content, uploadDistributeMapFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void openDrawer(final OnDrawerOpenListener listener) {
        drawer_layout.openDrawer(Gravity.RIGHT);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                GravityCompat.END);    //解除锁定
        drawer_layout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (listener != null){
                    listener.onOpened(drawerView);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //设置右面的侧滑菜单只能通过编程来打开
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                        GravityCompat.END);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void closeDrawer() {

    }

    @Override
    public ViewGroup getDrawerContainer() {
        return progress_linearlayout;
    }
}
