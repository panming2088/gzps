package com.augurit.agmobile.mapengine.route.view;

import android.content.Context;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.route.view.presenter.IRouteInputPresenter;
import com.augurit.agmobile.mapengine.route.view.presenter.IRouteResultPresenter;
import com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;

import java.util.List;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.agmobile.mapengine.route.view
 * @createTime 创建时间 ：17/1/16
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 17/1/16
 */

public interface IRouteInputView {

    /**
     * 获取要搜索的地址数据列表
     * @return
     */
    List<Point> getInuptLocationList();

    /**
     * 添加路径点输入相关UI界面
     * @param context
     * @param containerView
     * @param _mapView
     * @param routeServiceUrl
     * @param behavior
     * @param provider
     */
    void addRouteInputView(Context context, ViewGroup containerView, MapView _mapView, String routeServiceUrl, BottomSheetBehavior behavior, ISelectorProvider provider);

    /**
     * 消耗当前对象
     */
    void destory();

    /**
     * 获取结果 Presenter
     * @return
     */
    IRouteResultPresenter getResultPresenter();

    /**
     * 获取输入 Presenter
     * @return
     */
    IRouteInputPresenter getInputPresenter();

}
