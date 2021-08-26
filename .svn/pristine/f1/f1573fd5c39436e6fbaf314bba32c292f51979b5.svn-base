package com.augurit.agmobile.gzps.publicaffair;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.augurit.agmobile.gzps.journal.view.detaillist.DayPatrolListFragment;
import com.augurit.agmobile.gzps.publicaffair.view.eventaffair.EventAffairFragment;
import com.augurit.agmobile.gzps.publicaffair.view.facilityaffair.FacilityAffairFragment;
import com.augurit.agmobile.gzps.publicaffair.view.facilityaffair.PipeAffairFragment;

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

public class PublicAffairPagerAdapter extends FragmentStatePagerAdapter {
    private String[] titles = new String[]{ "管井复核列表" ,"管线复核列表","问题上报列表",  "日常巡检列表"};
    private List<Fragment> mFragments = new ArrayList<>();
    private Context context;

    public PublicAffairPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        mFragments.add(new FacilityAffairFragment());
        mFragments.add(new PipeAffairFragment());
        mFragments.add(new EventAffairFragment());
        mFragments.add(new DayPatrolListFragment());
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
