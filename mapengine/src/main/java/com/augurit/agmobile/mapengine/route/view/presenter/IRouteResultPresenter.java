package com.augurit.agmobile.mapengine.route.view.presenter;

import android.view.ViewGroup;

import java.util.List;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.agmobile.mapengine.route.view.presenter
 * @createTime 创建时间 ：17/1/16
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 17/1/16
 */

public interface IRouteResultPresenter {

    /**
     * 展示路径分析结果
     */
    void showResultView();

    /**
     * 添加路径列表到界面View
     * @param containView
     * @param directions
     */
    void addRouteListView(ViewGroup containView, List<String> directions);

    /**
     * 重置恢复路径分析前结果
     */
    void reset();

    /**
     * 退出路径分析功能
     */
    void outOfRoute();

    /**
     * 销毁资源
     */
    void destory();

}
