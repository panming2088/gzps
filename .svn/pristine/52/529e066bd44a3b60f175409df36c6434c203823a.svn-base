package com.augurit.agmobile.gzps.uploadfacility.view.doorno;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.setting.view.myupload.MyUploadStatiscPagerAdapter;
import com.augurit.agmobile.gzps.uploadfacility.model.DoorNoFilterCondition;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadFilterConditionEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.FacilityFilterCondition;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.UploadFacilitySumEvent;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;


/**
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.statistic
 * @createTime 创建时间 ：2017/8/15
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/8/15
 * @modifyMemo 修改备注：
 */

public class UploadDoorNoListActivity extends BaseActivity {

    private TextView all_count, install_count, no_install_count;
    private DrawerLayout drawer_layout;
    private MyUploadStatiscPagerAdapter adapter;
    private MyAddDoorNoListFragment mMyAddDoorNoListFragment;
    protected ProgressLinearLayout progress_linearlayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doorno_uploadlist);
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("我的门牌上报");
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        all_count = (TextView) findViewById(R.id.all_install_count);
        install_count = (TextView) findViewById(R.id.install_count);
        no_install_count = (TextView) findViewById(R.id.no_install_count);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置右面的侧滑菜单只能通过编程来打开
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END);


        //筛选条件
        findViewById(R.id.ll_search).setVisibility(View.VISIBLE);
        ImageView iv_open_search = ((ImageView) findViewById(R.id.iv_open_search));
        iv_open_search.setImageResource(R.mipmap.ic_filter);
        ((TextView) findViewById(R.id.tv_search)).setText("筛选");
        findViewById(R.id.tv_search).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                openDrawer();
//                ToastUtil.shortToast(PSHUploadDoorNoListActivity.this,"建设中");
            }
        });

        final ViewGroup llModifiedFilterCondition = (ViewGroup) findViewById(R.id.ll_modified_filter_condition);
//        new FacilityListFilterConditionView(this, "校核列表筛选", FacilityFilterCondition.MODIFIED_LIST, llModifiedFilterCondition);

//        final ViewGroup llUploadFilterCondition = (ViewGroup) findViewById(R.id.ll_upload_filter_condition);
        final ViewGroup llUploadFilterCondition = (ViewGroup) findViewById(R.id.ll_upload_filter_condition);
        new DoorNoListFilterConditionView(this, "门牌列表筛选", FacilityFilterCondition.NEW_ADDED_LIST, llUploadFilterCondition);

//        UploadEventConditionView eventAffairConditionView = new UploadEventConditionView(llPSHCondition, this);


//        progress_linearlayout = (ProgressLinearLayout) findViewById(R.id.progress_linearlayout);
//        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        //设置右面的侧滑菜单只能通过编程来打开
//        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
//                GravityCompat.END);

        mMyAddDoorNoListFragment = new MyAddDoorNoListFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(com.augurit.agmobile.patrolcore.R.id.ly_content, mMyAddDoorNoListFragment);
        fragmentTransaction.commit();
    }


    public void openDrawer() {
        drawer_layout.openDrawer(Gravity.RIGHT);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                GravityCompat.END);    //解除锁定
        drawer_layout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

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
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUploadFacilitySum(UploadFacilitySumEvent uploadFacilitySumEvent) {
        ((TextView) findViewById(R.id.tv_title)).setText("我的数据上报（" + getStringFromDouble(Double.valueOf(uploadFacilitySumEvent.getSum())) + "条）");
    }


    @Subscribe
    public void onPSHEventConditionChanged(UploadFilterConditionEvent eventAffairConditionEvent) {
        drawer_layout.closeDrawers();
    }
    private String getStringFromDouble(double n) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(n);
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
            drawer_layout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 收到校核筛选条件中的『确定』按钮点击事件，事件发出在：{@link com.augurit.agmobile.gzps.setting.view.myupload.MyCheckedFacilityListFragment}
     *
     * @param eventAffairConditionEvent
     */
    @Subscribe
    public void onEventConditionChanged(DoorNoFilterCondition eventAffairConditionEvent) {
        drawer_layout.closeDrawers();
    }

//    /**
//     * 收到新增筛选条件中的『确定』按钮点击事件，事件发出在：{@link com.augurit.agmobile.gzps.setting.view.myupload.MyAddFacilityListFragment}
//     *
//     * @param eventAffairConditionEvent
//     */
//    @Subscribe
//    public void onEventConditionChanged(UploadedFacilityFilterCondition eventAffairConditionEvent) {
//        drawer_layout.closeDrawers();
//    }
}
