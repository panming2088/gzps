package com.augurit.agmobile.mapengine.statistics.view;

import android.content.Context;
import android.view.ViewGroup;

/**
 * 描述：统计视图基类
 *
 * @author 创建人 ：liangsh
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.statistics.view
 * @createTime 创建时间 ：2017-01-24
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-07
 * @modifyMemo 修改备注：
 */
public abstract class StatisticsBaseView {

    protected Context mContext;
    protected ViewGroup mContainer;
    protected ViewGroup mView;

    public StatisticsBaseView(Context context, ViewGroup container){
        mContext = context;
        mContainer = container;
        init();
    }

    protected void init(){
        mView = getLayout();
        initView();
        initListener();
        mContainer.removeAllViews();
        mContainer.addView(mView);
    }

    protected abstract ViewGroup getLayout();

    protected abstract void initView();

    protected abstract void initListener();
}
