package com.augurit.agmobile.gzps.doorno.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xiaoyu on 2017/12/5.
 */

public abstract class MyBaseFragment extends Fragment {
    ViewDataBinding dataBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LayoutInflater from = LayoutInflater.from(getActivity());
        dataBinding = DataBindingUtil.inflate(from, initview(), null, false);
        return dataBinding.getRoot();
    }

   public abstract int initview();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    public <T extends ViewDataBinding> T getBind() {
        return (T) dataBinding;
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }
}
