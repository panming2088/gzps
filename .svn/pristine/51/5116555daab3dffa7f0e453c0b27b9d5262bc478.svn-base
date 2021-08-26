package com.augurit.agmobile.gzps.statistic.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.augurit.agmobile.gzps.statistic.view.uploadview.UploadStatsFragment;
import com.augurit.agmobile.gzps.statistic.view.uploadview.UploadStatsFragmentNew;

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

public class StatisticsFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private String[] titles = new String[]{  "上报统计","安装统计", "签到统计"};
    private List<Fragment> mFragments = new ArrayList<>();
    private Context context;

    public StatisticsFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        mFragments.add(new UploadStatsFragmentNew());
        mFragments.add(new AppInstalledStatsFragment());
        mFragments.add(new SignInStatsFragment());
//        mFragments.add(new UploadStatsFragment());
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
