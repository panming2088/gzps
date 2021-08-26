package com.augurit.agmobile.gzps.publicaffair.view.eventaffair;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.widget.FlexRadioGroup;
import com.augurit.agmobile.gzps.publicaffair.view.condition.EventAffairConditionEvent;
import com.augurit.agmobile.gzps.publicaffair.view.condition.EventAffairConditionView;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 事件事务公开
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view
 * @createTime 创建时间 ：17/7/27
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/27
 * @modifyMemo 修改备注：
 */

public class EventAffairActivity extends BaseActivity implements IDrawerController, View.OnClickListener, Animation.AnimationListener {

    private LatLng mDestinationOrLastTimeSelectLocation;
    private String mLastSelectedAddress;
    private ViewGroup progress_linearlayout;
    private DrawerLayout drawer_layout;
    private ImageView iv_open_search;
    //private View ll_facility_condition;
    private View ll_event_condition;
   // private PublicAffairPagerAdapter adapter;


    private Animation animCollapse, animExpand;

    private SparseBooleanArray isCollapses; //是否收缩

    private Drawable dropUp, dropDown;

    private int currentAnimId;//当前正在执行动画的ID

    private boolean mProtectFromCheckedChange = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventaffair);
        EventBus.getDefault().register(this);
        findViewById(R.id.ll_search).setVisibility(View.VISIBLE);
        iv_open_search = ((ImageView) findViewById(R.id.iv_open_search));
        iv_open_search.setImageResource(R.mipmap.ic_filter);
        findViewById(R.id.ll_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(null);
            }
        });

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("事务公开");


        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置右面的侧滑菜单只能通过编程来打开
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END);

        progress_linearlayout = (ViewGroup) findViewById(R.id.progress_linearlayout);
        //ll_facility_condition = findViewById(ll_facility_condition);
        ll_event_condition = findViewById(R.id.ll_event_condition);

        EventAffairConditionView eventAffairConditionView = new EventAffairConditionView(ll_event_condition, this);
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
                if (listener != null) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear:

                break;
            case R.id.btn_submit:
                drawer_layout.closeDrawers();

                break;
            case R.id.tv_type:

                break;
            case R.id.tv_price:

                break;
            case R.id.tv_structure:

                break;
        }
    }


    /**
     * 设置箭头
     */
    private void setArrow(TextView view, boolean isCollapse) {
        if (!isCollapse) {
            if (dropUp == null) {
                dropUp = getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp);
                dropUp.setBounds(0, 0, dropUp.getMinimumWidth(), dropUp.getMinimumHeight());
            }
            view.setCompoundDrawables(null, null, dropUp, null);
        } else {
            if (dropDown == null) {
                dropDown = getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp);
                dropDown.setBounds(0, 0, dropDown.getMinimumWidth(), dropDown.getMinimumHeight());
            }
            view.setCompoundDrawables(null, null, dropDown, null);
        }


    }

    /**
     * 重新设置isCollapse值，保存当前动画状态
     * 启动动画
     *
     * @param group
     */
    private void startAnim(FlexRadioGroup group, TextView view) {
        currentAnimId = group.getId();
        boolean isCollapse = !isCollapses.get(group.getId());
        isCollapses.put(group.getId(), isCollapse);
        if (isCollapse) {
            group.startAnimation(animCollapse);
        } else {
            group.startAnimation(animExpand);
        }
        setArrow(view, isCollapse);
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.END))
            drawer_layout.closeDrawers();
        else
            super.onBackPressed();
    }


    @Override
    public void onAnimationStart(Animation animation) {
        if (!isCollapses.get(currentAnimId)) {
            findViewById(currentAnimId).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (isCollapses.get(currentAnimId)) {
            findViewById(currentAnimId).setVisibility(View.GONE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Subscribe
    public void onEventConditionChanged(EventAffairConditionEvent eventAffairConditionEvent){
        drawer_layout.closeDrawers();
    }
}
