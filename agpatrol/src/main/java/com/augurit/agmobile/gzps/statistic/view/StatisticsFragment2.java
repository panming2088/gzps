package com.augurit.agmobile.gzps.statistic.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.LazyLoadFragment;

/**
 * Created by liangsh on 2017/11/6.
 */

public class StatisticsFragment2 extends Fragment {

        StatisticsFragmentPagerAdapter adapter;

@Nullable
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics2, null);
        }


@Override
public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().findViewById(R.id.ll_back).setVisibility(View.GONE);
        ((TextView) getView().findViewById(R.id.tv_title)).setText("统计");
final ViewPager viewPager = (ViewPager) getView().findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        adapter = new StatisticsFragmentPagerAdapter(getChildFragmentManager(),
        getActivity());
        viewPager.setAdapter(adapter);

//TabLayout
final TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.post(new Runnable() {
@Override
public void run() {
//                    setIndicator(getContext(), tabLayout, 65, 65);
        }
        });

        }
        }
