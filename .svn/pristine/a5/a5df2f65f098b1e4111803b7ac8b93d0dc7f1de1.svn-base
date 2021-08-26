package com.augurit.agmobile.gzps.publicaffair.view.facilityaffair;

import android.os.Bundle;
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
import com.augurit.agmobile.gzps.publicaffair.view.distribute.FacilityAffairDistributeMapFragment;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;

/**
 * 事务公开中上报设施列表中的『纠错设施』详情
 *
 * Created by xcl on 2017/11/17.
 */

public class PublicAffairModifiedFacilityDetailAcitivty extends BaseActivity implements IDrawerController {

    private DrawerLayout drawer_layout;
    private ViewGroup progress_linearlayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifiedfacilitymap);
        progress_linearlayout = (ViewGroup) findViewById(R.id.progress_linearlayout);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置右面的侧滑菜单只能通过编程来打开
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END);
        ((TextView)findViewById(R.id.tv_title)).setText("校核详情");
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String district = getIntent().getStringExtra("district");
        String userOrg =  district + "区";
        if (userOrg.contains("全部")){
            userOrg = "全市";
        }


        ModifiedFacility modifiedFacility = getIntent().getParcelableExtra("data");
        Bundle bundle = new Bundle();
        bundle.putString("uploadType", getIntent().getStringExtra("uploadType"));
        bundle.putString("facilityType", getIntent().getStringExtra("facilityType"));
        bundle.putString("district",district);
        bundle.putBoolean("isShiPaiOrKexuecheng", getIntent().getBooleanExtra("isShiPaiOrKexuecheng",false));
        bundle.putParcelable("modifiedFacility",modifiedFacility);
        bundle.putLong("startDate",getIntent().getLongExtra("startDate",0));
        bundle.putLong("endDate",getIntent().getLongExtra("endDate",0));
        FacilityAffairDistributeMapFragment uploadDistributeMapFragment = FacilityAffairDistributeMapFragment.getInstance(bundle);
        //final ModifiedFacilityDetailFragment problemListMapFragment = ModifiedFacilityDetailFragment.getInstance(modifiedFacility);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //problemListMapFragment.setLocate(false);
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