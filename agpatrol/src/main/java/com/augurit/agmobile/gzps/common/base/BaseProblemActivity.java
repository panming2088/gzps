package com.augurit.agmobile.gzps.common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.componentmaintenance.IChangeTabListener;
import com.augurit.agmobile.patrolcore.search.view.SearchFragmentWithoutMap;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.patrol
 * @createTime 创建时间 ：2017-10-13
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-10-13
 * @modifyMemo 修改备注：
 */

public class BaseProblemActivity extends BaseActivity  implements IDrawerController,IChangeTabListener {

    protected ViewGroup progress_linearlayout;
    protected DrawerLayout drawer_layout;
    protected TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problemlist);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setText(getTab1Title());
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setText(getTab2Title());

        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);

        initTabLayout();

        progress_linearlayout = (ViewGroup) findViewById(R.id.progress_linearlayout);


        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置右面的侧滑菜单只能通过编程来打开
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END);

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    protected String getTab1Title(){
        return "我的上报问题";
    }
    protected String getTab2Title(){
        return "问题定位";
    }

    protected void initTabLayout() {

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final ProblemMapFragment problemMapFragment = new ProblemMapFragment();
        final SearchFragmentWithoutMap problemListFragment = SearchFragmentWithoutMap.newInstance("",false,true);

       /*
        problemListFragment.setOnClickTaskBtnListener(new Callback1<Integer>() {
            @Override
            public void onCallback(Integer integer) {
                Intent intent = null;
                if(integer == 0){
                    //待签收
                    intent = new Intent(BaseProblemActivity.this, UnAcceptedTaskActivity.class);
                } else if(integer == 1){
                    //已签收
                    intent = new Intent(BaseProblemActivity.this, AcceptedTaskActivity.class);
                }
                startActivity(intent);
            }
        });
        */

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals(getTab1Title())){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.hide(problemMapFragment).show(problemListFragment);
                    fragmentTransaction.commit();
                } else if(tab.getText().equals(getTab2Title())){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.hide(problemListFragment).show(problemMapFragment);
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fg_container, problemListFragment).add(R.id.fg_container, problemMapFragment);
        fragmentTransaction.hide(problemMapFragment).show(problemListFragment);
        fragmentTransaction.commit();
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
    public void changeToTab(int tabNo) {
        if (tabLayout != null){
            int count  = tabLayout.getTabCount();
            if (tabNo < count){
                tabLayout.getTabAt(tabNo).select();
            }
        }
    }


    class ProblemFragmentPagerAdapter extends FragmentStatePagerAdapter {
        public final int COUNT = 2;
        private String[] titles = new String[]{"我的上报问题", "地图展示"};
        private List<Fragment> mFragments = new ArrayList<>();
        private Context context;

        public ProblemFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
            mFragments.add(new ProblemListFragment());
            mFragments.add(new ProblemMapFragment());

        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Override
    public void closeDrawer() {

    }

    @Override
    public ViewGroup getDrawerContainer() {
        return progress_linearlayout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        mapFragment.onActivityResult(requestCode, resultCode, data);
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
