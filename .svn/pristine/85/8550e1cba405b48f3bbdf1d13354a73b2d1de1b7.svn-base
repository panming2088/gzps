package com.augurit.agmobile.mapengine.area.view.presenter;

import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.common.model.Area;
import com.augurit.am.cmpt.common.Callback1;

/**
 * Created by liangsh on 2017-01-23.
 */
public interface IAreaPresenter {


    /**
     * 初始化
     */
    void init();

    /**
     * 在地图上定位到指定区域
     * @param area
     */
    void locateArea(Area area);

    /**
     * 高亮选中项
     */
    void setSelectItem();

    /**
     * 显示指定区域的子区域
     * @param area
     */
    void showChildAreas(Area area);

    /**
     * 返回上一级
     */
    void back();

    /**
     * 退出功能后的回调，以恢复主界面状态
     * @param callback
     */
    void setBackListener(Callback1 callback);

    /**
     * 进入区域功能
     * @param container
     */
    void startArea(ViewGroup container);

    /**
     * 退出区域功能
     */
    void onClose();
}
