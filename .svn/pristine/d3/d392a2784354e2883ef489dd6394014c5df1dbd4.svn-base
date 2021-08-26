package com.augurit.agmobile.gzps.jbjpsdy;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.augurit.agmobile.gzps.setting.view.myupload.MyAddFacilityListFragment;
import com.augurit.agmobile.gzps.setting.view.myupload.MyAddPipeListFragment;
import com.augurit.agmobile.gzps.setting.view.myupload.MyCheckedFacilityListFragment;
import com.augurit.agmobile.gzps.setting.view.myupload.MyCheckedPipeListFragment;
import com.augurit.agmobile.gzps.setting.view.myupload.MyHookListFragment;
import com.augurit.agmobile.gzps.setting.view.myupload.MyJbjMonitorListFragment;

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

public class MyJbjListPagerAdapter extends FragmentStatePagerAdapter {
    private String[] titles = new String[]{"接驳信息","接驳井监测"
    };
    private List<Fragment> mFragments = new ArrayList<>();
    private Context context;

    public MyJbjListPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        mFragments.add(new MyHookListFragment());
        mFragments.add(new MyJbjMonitorListFragment());
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
