package com.augurit.agmobile.mapengine.area.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.area.view.presenter.IAreaPresenter;
import com.augurit.agmobile.mapengine.common.model.Area;

import java.util.List;


/**
 * Created by liangsh on 2017-01-20.
 */
public abstract class IAreaView {
    protected Context mContext;
    protected View mView;
    protected ViewGroup mContainer;

    public IAreaView(Context context){
        this.mContext = context;
        init();
    }

    /**
     * 初始化
     */
    protected void init(){
        mView = getLayout();
        initViewAndListener();
    }

    /**
     * 返回View的布局
     * @return
     */
    protected abstract View getLayout();

    /**
     * 初始化View，并设置事件监听
     */
    protected abstract void initViewAndListener();

    /**
     * 设置Presenter
     * @param areaPresenter
     */
    public abstract void setPresenter(IAreaPresenter areaPresenter);

    /**
     * 设置要显示的区域列表
     * @param areaList 区域
     * @param isParent 是否非最低级别行政区域
     */
    public abstract void setAreaList(List<Area> areaList, boolean isParent);

    /**
     * 设置标题
     * @param title
     */
    public abstract void setTitle(String title);

    /**
     * 显示View
     * @param container View放置的容器
     */
    public void show(ViewGroup container){
        this.mContainer = container;
        mContainer.removeAllViews();
        mContainer.addView(mView);
        mContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 是否高亮选中项
     * @param selectItem
     */
    public abstract void setSelectItem(boolean selectItem);
}
