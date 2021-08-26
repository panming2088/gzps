package com.augurit.agmobile.gzps.publicaffair.view.distribute;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;

/**
 *
 * 事务公开中的设施分布图
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.publicaffair
 * @createTime 创建时间 ：17/12/13
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/13
 * @modifyMemo 修改备注：
 */

public class FacilityAffairDistributeMapActivity extends BaseActivity implements IDrawerController {

    protected ProgressLinearLayout progress_linearlayout;
    protected DrawerLayout drawer_layout;


    private FacilityAffairDistributeMapFragment addComponentFragment;

    private String componentName;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_affair_map);

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

       //FacilityAffairService facilityAffairService= new FacilityAffairService(getApplicationContext());
        String district = getIntent().getStringExtra("district");
        String userOrg =  district + "区";
        if (userOrg.contains("全部")){
            userOrg = "全市";
        }
//        boolean ifCurrentUserBelongToCityUser = facilityAffairService.ifCurrentUserBelongToCityUser();
//        if (ifCurrentUserBelongToCityUser){
//            userOrg = "全市";
//        }
        ((TextView)findViewById(R.id.tv_title)).setText(userOrg + "数据上报分布图");
        progress_linearlayout = (ProgressLinearLayout) findViewById(R.id.progress_linearlayout);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置右面的侧滑菜单只能通过编程来打开
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END);

        Bundle bundle = new Bundle();
        bundle.putString("uploadType", getIntent().getStringExtra("uploadType"));
        bundle.putString("facilityType", getIntent().getStringExtra("facilityType"));
        bundle.putString("district",district);
        bundle.putLong("startDate",getIntent().getLongExtra("startDate",0));
        bundle.putLong("endDate",getIntent().getLongExtra("endDate",0));
        addComponentFragment = FacilityAffairDistributeMapFragment.getInstance(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(com.augurit.agmobile.patrolcore.R.id.ly_content, addComponentFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume(){
        super.onResume();

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
//                progress_linearlayout.showLoading();
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



    /**
     * 主要用于拍照、打开照片、地图浏览等返回Activity的刷新操作
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if(!addComponentFragment.isHidden()) {
            addComponentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed(){
        if(!addComponentFragment.isHidden()){
            addComponentFragment.onBackPressed();
        } else {
            finish();
        }
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
