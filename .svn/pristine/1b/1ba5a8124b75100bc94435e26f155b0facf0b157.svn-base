package com.augurit.agmobile.gzps.setting.view.myupload;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.fragment
 * @createTime 创建时间 ：2017-03-13
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-13
 * @modifyMemo 修改备注：
 */

public class MyUploadStatiscPagerAdapter extends FragmentStatePagerAdapter {
    private String[] titles = new String[]{  "管井新增","管井校核"
            ,"管线新增","管线校核","关键节点监测"
    };
    private List<Fragment> mFragments = new ArrayList<>();
    private Context context;

    public MyUploadStatiscPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        mFragments.add(new MyAddFacilityListFragment());
        mFragments.add(new MyCheckedFacilityListFragment());
        mFragments.add(new MyAddPipeListFragment());
        mFragments.add(new MyCheckedPipeListFragment());
        mFragments.add(new MyKeyNodeMonitorListFragment());
//        mFragments.add(new MyHookListFragment());
//        mFragments.add(new MyJbjMonitorListFragment());
        //        mFragments.add(new MyDeletePipeListFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
