package com.augurit.agmobile.gzps.billboard;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.augurit.agmobile.gzps.common.webview.WebViewConstant;
import com.augurit.agmobile.gzps.lawsandregulation.LawsAndRegulationsFragment;

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

public class BillBoardPagerAdapter extends FragmentStatePagerAdapter {
    private String[] titles = new String[]{"红榜", "黑榜"};
    private List<Fragment> mFragments = new ArrayList<>();
    private Context context;

    public BillBoardPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        mFragments.add(LawsAndRegulationsFragment.getInstance(WebViewConstant.H5_URLS.RED_BOARD));
        mFragments.add(LawsAndRegulationsFragment.getInstance(WebViewConstant.H5_URLS.BLACK_BOARD));
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
