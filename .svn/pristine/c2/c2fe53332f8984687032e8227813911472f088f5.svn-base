package com.augurit.agmobile.gzps.journal.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.augurit.agmobile.gzps.journal.view.detaillist.DayPatrolListFragment;
import com.augurit.agmobile.gzps.journal.view.detaillist.UploadFacilityListFragment;
import com.augurit.agmobile.gzps.journal.view.detaillist.UploadProblemListFragment;
import com.augurit.agmobile.patrolcore.common.model.Component;

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

public class JournalsListPagerAdapter extends FragmentStatePagerAdapter {
    private String[] titles = new String[]{ "复核记录", "巡检记录", "问题上报记录"};
    private List<Fragment> mFragments = new ArrayList<>();
    private Context context;

    public JournalsListPagerAdapter(FragmentManager fm, Context context, Component component) {
        super(fm);
        this.context = context;
        UploadFacilityListFragment uploadFacilityListFragment = new UploadFacilityListFragment();
        DayPatrolListFragment dayPatrolListFragment = new DayPatrolListFragment();
        UploadProblemListFragment uploadProblemListFragment = new UploadProblemListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("component",component);
        dayPatrolListFragment.setArguments(bundle);
        uploadFacilityListFragment.setArguments(bundle);
        uploadProblemListFragment.setArguments(bundle);
        mFragments.add(uploadFacilityListFragment);
        mFragments.add(dayPatrolListFragment);
        mFragments.add(uploadProblemListFragment);
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
