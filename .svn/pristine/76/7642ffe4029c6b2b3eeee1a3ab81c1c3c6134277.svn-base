package com.augurit.agmobile.patrolcore.selectlocation.view;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.selectlocation.util.SelectLocationConstant;
import com.augurit.am.cmpt.permission.PermissionsUtil2;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view
 * @createTime 创建时间 ：17/7/27
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/27
 * @modifyMemo 修改备注：
 */

public class SelectLocationActivity extends AppCompatActivity implements IDrawerController {

    private LatLng mDestinationOrLastTimeSelectLocation;
    private String mLastSelectedAddress ;
    private ViewGroup progress_linearlayout;
    private DrawerLayout drawer_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);


        //是否允许编辑位置,默认允许
        boolean mIfReadOnly = getIntent().getBooleanExtra(SelectLocationConstant.IF_READ_ONLY, false);
        mDestinationOrLastTimeSelectLocation = getIntent().getParcelableExtra(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_LOCATION);
        mLastSelectedAddress = getIntent().getStringExtra(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_ADDRESS);
        double mInitialScale = getIntent().getDoubleExtra(SelectLocationConstant.INITIAL_SCALE, -1);

        SelectLocationFragment selectLocationFragment = SelectLocationFragment
                .newInstance(mIfReadOnly,mDestinationOrLastTimeSelectLocation,mInitialScale,mLastSelectedAddress);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ly_content,selectLocationFragment);
        fragmentTransaction.commit();



        progress_linearlayout = (ViewGroup) findViewById(R.id.progress_linearlayout);


        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置右面的侧滑菜单只能通过编程来打开
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END);
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

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
