package com.augurit.agmobile.gzps.uploadfacility.view.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;

/**
 * Created by xcl on 2017/11/15.
 */

public class FeedbackEventMapActivity extends BaseActivity implements IDrawerController {

    protected ViewGroup progress_linearlayout;
    protected DrawerLayout drawer_layout;
    private FeedbackEventMapFragment eventDetailMapFragment;
    private String layerUrl;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcomponent2);
        progress_linearlayout = (ViewGroup) findViewById(R.id.progress_linearlayout);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置右面的侧滑菜单只能通过编程来打开
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END);

        layerUrl = getIntent().getStringExtra("layerUrl");
        String layerName = getIntent().getStringExtra("layerName");
        String usid = getIntent().getStringExtra("object_id");
        double x = getIntent().getDoubleExtra("x",0);
        double y = getIntent().getDoubleExtra("y",0);
        eventDetailMapFragment =  FeedbackEventMapFragment.getInstance(layerUrl, layerName, usid, x, y);
        eventDetailMapFragment.setArguments(getIntent().getExtras());
        android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(com.augurit.agmobile.patrolcore.R.id.ly_content, eventDetailMapFragment);
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

    }

    @Override
    public void onBackPressed(){
        finish();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
