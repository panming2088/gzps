package com.augurit.am.fw.common;

import android.content.Context;

/**
 * 所有视图都应该继承的基类
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.common
 * @createTime 创建时间 ：2017-01-18
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-18
 */

public abstract class BaseView<T extends IPresenter> implements IView {

    protected Context mContext;

    protected T mPresenter;

    public BaseView(Context context){
        this.mContext = context;
    }

    @Override
    public Context getApplicationContext() {
        return mContext.getApplicationContext();
    }

    @Override
    public void setPresenter(IPresenter presenter) {
        mPresenter = (T) presenter;
    }
}
