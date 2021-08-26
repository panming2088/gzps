package com.augurit.agmobile.gzps.common.baiduselectlocation;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.common.baiduselectlocation.model.BaiduSelectLocationModel;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/10/15
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/15
 * @modifyMemo 修改备注：
 */

public class BaiduSelectLocationActivity extends BaseActivity implements IDrawerController {

    private LatLng mDestinationOrLastTimeSelectLocation;
    private String mLastSelectedAddress;
    private ViewGroup progress_linearlayout;
    private DrawerLayout drawer_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);


//        //是否允许编辑位置,默认允许
//        boolean mIfReadOnly = getIntent().getBooleanExtra(EditMapConstant.BundleKey.IF_READ_ONLY, false);
//        Geometry geometry = (Geometry) getIntent().getSerializableExtra(EditMapConstant.BundleKey.GEOMETRY);
//
//        double initScale = getIntent().getDoubleExtra(EditMapConstant.BundleKey.INIT_SCALE, 0);
//        String address = getIntent().getStringExtra(EditMapConstant.BundleKey.ADDRESS);
//        Geometry facilityOriginLocation = (Geometry) getIntent().getSerializableExtra(EditMapConstant.BundleKey.FACIILITY_ORIGIN_LOCATION);


        BaiduSelectLocationModel model = (BaiduSelectLocationModel) getIntent().getSerializableExtra("baiduselectlocation");

        BaiduSelectLocationFragment selectLocationFragment =  null;
        if (model != null){
             selectLocationFragment = BaiduSelectLocationFragment
                    .newInstance(model.getLastSelectLocation(),
                            model.getInitScale(),
                            model.isIfReadOnly(),
                            model.getLastSelectAddress(),
                            model.getFacilityOriginLocation());
        }else {
            selectLocationFragment = new BaiduSelectLocationFragment();
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ly_content, selectLocationFragment);
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