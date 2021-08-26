package com.augurit.agmobile.patrolcore.selectdevice.view;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;


import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.patrolcore.layer.model.LayerList;
import com.augurit.agmobile.patrolcore.layer.service.IPatrolLayerService;
import com.augurit.agmobile.patrolcore.selectdevice.utils.SelectDeviceConstant;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.augurit.agmobile.patrolcore.R;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.map.view
 * @createTime 创建时间 ：2017-03-28
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-28
 * @modifyMemo 修改备注：
 */

public class SelectDeviceActivity extends AppCompatActivity{

    private MapView mMapView;
    private DrawerLayout drawer_layout;
    private RecyclerView list_right_drawer;
    private ProgressLinearLayout mProgressLinearLayout;
    private String mWkt;
    private Geometry mGeometry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectdevice);

        initView();

        initDatas();
    }

    private void initDatas() {

        mWkt = getIntent().getStringExtra(SelectDeviceConstant.DEVICE_WKT);
        mGeometry = (Geometry) getIntent().getSerializableExtra(SelectDeviceConstant.DEVICE_GEOMETRY);
        boolean ifReadOnly = getIntent().getBooleanExtra(SelectDeviceConstant.IF_DEVICE_READ_ONLY,false);
        String deviceName = getIntent().getStringExtra(SelectDeviceConstant.DEVICE_NAME);
        String title = getIntent().getStringExtra(SelectDeviceConstant.DEVICE_KEY_NAME);


        ILayersService iLayersService = LayerServiceFactory.provideLayerService(this);
        if (iLayersService instanceof IPatrolLayerService){
            mProgressLinearLayout.showLoading();
            ((IPatrolLayerService)iLayersService).getLayerList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<LayerList>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                            mProgressLinearLayout.showError("加载列表",
                                    "加载列表失败",
                                    "",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                        }

                        @Override
                        public void onNext(LayerList layerList) {
                            mProgressLinearLayout.showContent();
                            List<LayerList.PatrolLayer> rows = layerList.getRows();
                            DeviceLayerAdapter deviceLayerAdapter = new DeviceLayerAdapter(rows);
                            list_right_drawer.setAdapter(deviceLayerAdapter);
                            list_right_drawer.setLayoutManager(new LinearLayoutManager(SelectDeviceActivity.this));
                        }
                    });
        }

        if (!TextUtils.isEmpty(mWkt)){ //如果wkt不为空
            SelectDeviceFragment selectDeviceFragment = getSelectDeviceFragment(ifReadOnly,mWkt,deviceName,title);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.ly_content,selectDeviceFragment);
            fragmentTransaction.commit();
        }else if (mGeometry != null){ //如果geometry不为空
            SelectDeviceFragment selectDeviceFragment = getSelectDeviceFragment(ifReadOnly,mGeometry,deviceName,title);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.ly_content,selectDeviceFragment);
            fragmentTransaction.commit();
        }else { //如果都为空
            SelectDeviceFragment selectDeviceFragment = getSelectDeviceFragment(ifReadOnly,"",deviceName,title);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.ly_content,selectDeviceFragment);
            fragmentTransaction.commit();
        }

    }

    /**
     * 获取到SelectDeviceFragment实例
     * @param ifReadOnly 是否只读
     * @param wkt
     * @return
     */
    private SelectDeviceFragment getSelectDeviceFragment(boolean ifReadOnly,String wkt,String deviceName,String title) {
        if (ifReadOnly){
            return ReadOnlyDeviceFragment.newInstance(wkt,deviceName,title);
        }
        return SelectDeviceFragment.newInstance(wkt,deviceName,title);
    }

    /**
     * 获取到SelectDeviceFragment实例
     * @param ifReadOnly 是否只读
     * @param geometry
     * @return
     */
    private SelectDeviceFragment getSelectDeviceFragment(boolean ifReadOnly,Geometry geometry,String deviceName,String title) {
        if (ifReadOnly){
            return ReadOnlyDeviceFragment.newInstance(geometry,deviceName,title);
        }
        return SelectDeviceFragment.newInstance(geometry,deviceName,title);
    }

    private void initView() {

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        list_right_drawer = (RecyclerView) findViewById(R.id.rv_right_drawer);
        mProgressLinearLayout = (ProgressLinearLayout) findViewById(R.id.progress_linearlayout);
        //设置右面的侧滑菜单只能通过编程来打开
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END);
    }

    public void openDrawer() {
        drawer_layout.openDrawer(Gravity.RIGHT);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                GravityCompat.END);    //解除锁定
    }

}
