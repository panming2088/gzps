package com.augurit.agmobile.gzps.monitor;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.UploadFacilitySumEvent;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;


/**
 * 窨井监测列表页面
 *
 * @author 创建人 ：huangchongwu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps
 * @createTime 创建时间 ：2020/09/14
 */

public class InspectionWellMonitorListActivity extends BaseActivity {

    private DrawerLayout drawer_layout;
    private InspectionWellMonitorListFragment fireListFragment;
    protected ProgressLinearLayout progress_linearlayout;
    private String mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_well_monitor_uploadlist);
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {

        Bundle extras = getIntent().getExtras();
        String objectid = "";
        if (extras != null) {
            objectid = extras.getString("objectid", "");
            mType = extras.getString("type", "");
        }

        String titleFormat = "%1$s%2$s";
        String titleStr = String.format(titleFormat,
                "关键节点监测列表", TextUtils.isEmpty(objectid) ? "" : "(" + objectid + ")");
        ((TextView) findViewById(R.id.tv_title)).setText(titleStr);
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置右面的侧滑菜单只能通过编程来打开
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END);

        fireListFragment = InspectionWellMonitorListFragment.newInstance(extras);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(com.augurit.agmobile.patrolcore.R.id.ly_content, fireListFragment);
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
        ((TextView) findViewById(R.id.tv_title)).setText(
                "关键节点监测列表(" + getStringFromDouble(Double.valueOf(uploadFacilitySumEvent.getSum())) + "条)");
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
     * @param
     */

//    /**
//     * 收到新增筛选条件中的『确定』按钮点击事件，事件发出在：{@link com.augurit.agmobile.gzps.setting.view.myupload.MyAddFacilityListFragment}
//     *
//     * @param eventAffairConditionEvent
//     */
//    @Subscribe
//    public void onEventConditionChanged(UploadedFacilityFilterCondition eventAffairConditionEvent) {
//        drawer_layout.closeDrawers();
//    }


    /**
     * 跳转到这个Activity
     */
    public static void jump(Context context, String usid, String objectid, String type, String x, String y) {
        Intent intent = new Intent(context, InspectionWellMonitorListActivity.class);
        Bundle extras = new Bundle();
        extras.putString("usid", usid);
        extras.putString("objectid", objectid);
        extras.putString("type", type);
        extras.putString("X", x);
        extras.putString("Y", y);
        intent.putExtras(extras);
        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}
