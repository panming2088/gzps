package com.augurit.agmobile.gzps.lawsandregulation;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.augurit.agmobile.gzps.common.webview.WebViewConstant;

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

public class LawsAndRegulationPagerAdapter extends FragmentStatePagerAdapter {
    private String[] titles = new String[]{"标准规范", "政策法规"};
    private List<Fragment> mFragments = new ArrayList<>();
    private Context context;

    public LawsAndRegulationPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        mFragments.add(LawsAndRegulationsFragment.getInstance(WebViewConstant.H5_URLS
                .STANDARD_SPECIFICATION));
        mFragments.add(LawsAndRegulationsFragment.getInstance(WebViewConstant.H5_URLS
                .POLICY_REGULATIOS));
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
