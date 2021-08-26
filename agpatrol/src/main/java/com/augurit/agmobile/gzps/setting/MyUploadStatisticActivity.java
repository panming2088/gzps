package com.augurit.agmobile.gzps.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.jbjpsdy.HookListFilterConditionView;
import com.augurit.agmobile.gzps.jbjpsdy.KeyNodeInspectionWellHookListFilterConditionView;
import com.augurit.agmobile.gzps.setting.view.myupload.MyUploadStatiscPagerAdapter;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.FacilityFilterCondition;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.FacilityListFilterConditionView;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.PipeListFilterConditionView;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.UploadFacilitySumEvent;

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

public class MyUploadStatisticActivity extends BaseActivity {

    private TextView all_count, install_count, no_install_count;
    private DrawerLayout drawer_layout;
    private MyUploadStatiscPagerAdapter adapter;
    private int fAdd = 0, fChecked = 0, pipeAdd = 0, pipeCheck = 0, pipeDelete = 0, keyNode = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_uploadstats);
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("我的数据上报");
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
                openDrawer();
            }
        });

        final ViewGroup llModifiedFilterCondition = (ViewGroup) findViewById(R.id.ll_modified_filter_condition);
        new FacilityListFilterConditionView(this, "校核列表筛选", FacilityFilterCondition.MODIFIED_LIST, llModifiedFilterCondition);

        final ViewGroup llUploadFilterCondition = (ViewGroup) findViewById(R.id.ll_upload_filter_condition);
        new FacilityListFilterConditionView(this, "新增列表筛选", FacilityFilterCondition.NEW_ADDED_LIST, llUploadFilterCondition);

        final ViewGroup llAddPipeLine = (ViewGroup) findViewById(R.id.ll_addpipe_filter_condition);
        new PipeListFilterConditionView(this, "新增列表筛选", FacilityFilterCondition.NEW_PIPE_LIST, llAddPipeLine);

        final ViewGroup llUpdatePipeLine = (ViewGroup) findViewById(R.id.ll_updatepipe_filter_condition);
        new PipeListFilterConditionView(this, "校核列表筛选", FacilityFilterCondition.MODIFIED_PIPE_LIST, llUpdatePipeLine);

        final ViewGroup llKeyNodeFilterCondition = (ViewGroup) findViewById(R.id.ll_key_node_filter_condition);
        // fixme 这里的因为关键节点筛选条件和其他的有些不一样，没有“雨污类型”和“地址”，编号也不支持模糊搜索，
        //  所以在这里使用了自定义的一个筛选类，以后如果有修改，可以直接使用原来的。ps：不是上面那个
        new KeyNodeInspectionWellHookListFilterConditionView(this, "关键节点监测列表筛选", FacilityFilterCondition.KEY_NODE_MONITOR_LIST, llKeyNodeFilterCondition);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(5);
        adapter = new MyUploadStatiscPagerAdapter(getSupportFragmentManager(),
                this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeFilterConditionView(llUploadFilterCondition, 0, position);
                changeFilterConditionView(llModifiedFilterCondition, 1, position);
                changeFilterConditionView(llAddPipeLine, 2, position);
                changeFilterConditionView(llUpdatePipeLine, 3, position);
                changeFilterConditionView(llKeyNodeFilterCondition, 4, position);

                /*if (position == 0) {
                    llModifiedFilterCondition.setVisibility(View.GONE);
                    llUploadFilterCondition.setVisibility(View.VISIBLE);
                    llUpdatePipeLine.setVisibility(View.GONE);
                    llAddPipeLine.setVisibility(View.GONE);
                } else if (position == 1) {
                    llModifiedFilterCondition.setVisibility(View.VISIBLE);
                    llUploadFilterCondition.setVisibility(View.GONE);
                    llUpdatePipeLine.setVisibility(View.GONE);
                    llAddPipeLine.setVisibility(View.GONE);
                } else if (position == 2) {
                    llModifiedFilterCondition.setVisibility(View.GONE);
                    llUploadFilterCondition.setVisibility(View.GONE);
                    llUpdatePipeLine.setVisibility(View.GONE);
                    llAddPipeLine.setVisibility(View.VISIBLE);
                } else {
                    llModifiedFilterCondition.setVisibility(View.GONE);
                    llUploadFilterCondition.setVisibility(View.GONE);
                    llUpdatePipeLine.setVisibility(View.VISIBLE);
                    llAddPipeLine.setVisibility(View.GONE);
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(adapter);

        //TabLayout
        final TabLayout tabLayout = (TabLayout) findViewById(com.augurit.agmobile.gzps.R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 显示/隐藏对应筛选列表
     */
    private void changeFilterConditionView(View view, int requestIndex, int currIndex) {
        view.setVisibility(requestIndex == currIndex ? View.VISIBLE : View.GONE);
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
        int status = uploadFacilitySumEvent.getStatus();
        if(status == 1){
            fAdd = uploadFacilitySumEvent.getfAdd();
        }else if(status == 2){
            fChecked = uploadFacilitySumEvent.getfChecked();
        }else if(status == 3){
            pipeAdd = uploadFacilitySumEvent.getPipeAdd();
        }else if(status == 4){
            pipeCheck = uploadFacilitySumEvent.getPipeChecked();
        }else if(status == 5){
            pipeDelete = uploadFacilitySumEvent.getPipeDelete();
        }else if (status == 6) {
            keyNode = uploadFacilitySumEvent.getKeynode();
        }
        int sum = fAdd + fChecked + pipeAdd + pipeCheck + pipeDelete + keyNode;
        ((TextView) findViewById(R.id.tv_title)).setText("我的数据上报（" + getStringFromDouble(Double.valueOf(sum)) + "条）");
//        ((TextView) findViewById(R.id.tv_title)).setText("我的数据上报（" + getStringFromDouble(Double.valueOf(uploadFacilitySumEvent.getSum())) + "条）");
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
    public void onEventConditionChanged(FacilityFilterCondition eventAffairConditionEvent) {
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
