package com.augurit.agmobile.patrolcore.editmap;

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
import com.augurit.agmobile.patrolcore.editmap.util.EditMapConstant;
import com.augurit.agmobile.patrolcore.editmap.util.EditModeConstant;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.esri.core.geometry.Geometry;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/10/15
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/15
 * @modifyMemo 修改备注：
 */

public class EditMapFeatureActivity extends AppCompatActivity implements IDrawerController {

    private LatLng mDestinationOrLastTimeSelectLocation;
    private String mLastSelectedAddress;
    private ViewGroup progress_linearlayout;
    private DrawerLayout drawer_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);


        //是否允许编辑位置,默认允许
        boolean mIfReadOnly = getIntent().getBooleanExtra(EditMapConstant.BundleKey.IF_READ_ONLY, false);
        Geometry geometry = (Geometry) getIntent().getSerializableExtra(EditMapConstant.BundleKey.GEOMETRY);

        String editMode = getIntent().getStringExtra(EditMapConstant.BundleKey.EDIT_MODE);
        double initScale = getIntent().getDoubleExtra(EditMapConstant.BundleKey.INIT_SCALE, 0);
        String address = getIntent().getStringExtra(EditMapConstant.BundleKey.ADDRESS);

        if (EditModeConstant.EDIT_LINE.equals(editMode)) { //编辑线的界面
            EditLineFeatureFragment3 selectLocationFragment = EditLineFeatureFragment3
                    .newInstance(editMode, geometry, initScale, mIfReadOnly, address);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.ly_content, selectLocationFragment);
            fragmentTransaction.commit();
        } else {
            // 编辑点的界面
//            EditPointFragment selectLocationFragment = EditPointFragment
//                    .newInstance(geometry, initScale, mIfReadOnly, address);
//            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.ly_content, selectLocationFragment);
//            fragmentTransaction.commit();
            EditPointFragment selectLocationFragment = EditPointFragment
                    .newInstance(geometry,initScale, mIfReadOnly, address);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.ly_content, selectLocationFragment);
            fragmentTransaction.commit();
        }
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