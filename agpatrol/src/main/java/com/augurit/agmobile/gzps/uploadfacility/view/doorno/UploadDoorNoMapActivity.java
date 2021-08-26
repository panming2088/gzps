package com.augurit.agmobile.gzps.uploadfacility.view.doorno;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.model.DoorNOBean;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.esri.core.geometry.Point;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzpssb.pshdoorno.base
 * @createTime 创建时间 ：2018-04-23
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-04-23
 * @modifyMemo 修改备注：
 */

public class UploadDoorNoMapActivity extends BaseActivity implements IDrawerController {



    protected ProgressLinearLayout progress_linearlayout;
    protected DrawerLayout drawer_layout;

    private UploadDoorNoMapFragment addComponentFragment;

    private String componentName;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadmap);


        progress_linearlayout = (ProgressLinearLayout) findViewById(R.id.progress_linearlayout);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置右面的侧滑菜单只能通过编程来打开
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END);

        DoorNOBean doorNOBean = getIntent().getParcelableExtra("doorbean");
        Point yjPoint = (Point) getIntent().getSerializableExtra("yjPoint");
        boolean isReadOnly = getIntent().getBooleanExtra("isReadOnly",false);
        Bundle bundle = new Bundle();
        bundle.putParcelable("doorbean",doorNOBean);
        bundle.putSerializable("yjPoint",yjPoint);
        bundle.putBoolean("isReadOnly",isReadOnly);
        addComponentFragment = UploadDoorNoMapFragment.getInstance(bundle);
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
        drawer_layout.closeDrawer(Gravity.RIGHT);
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
    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
