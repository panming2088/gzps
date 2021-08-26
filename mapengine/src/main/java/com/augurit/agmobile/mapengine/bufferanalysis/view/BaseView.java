package com.augurit.agmobile.mapengine.bufferanalysis.view;

import android.content.Context;
import android.view.ViewGroup;

/**
 * View组件的基类
 * @author 创建人 ：liangshenghong
 * @package 包名 ：com.augurit.agmobile.mapengine.bufferanalysis.view
 * @createTime 创建时间 ：2017-01-24
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-01-24
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public abstract class BaseView {

    protected Context mContext;
    protected ViewGroup mContainer;
    protected ViewGroup mView;

    public BaseView(Context context, ViewGroup container){
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

    /**
     * 返回View布局
     * @return
     */
    protected abstract ViewGroup getLayout();

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 初始化监听
     */
    protected abstract void initListener();
}
