package com.augurit.agmobile.mapengine.route.view;

import android.content.Context;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.route.view.presenter.IRouteResultPresenter;
import com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior;
import com.esri.android.map.MapView;
import com.esri.core.tasks.na.Route;
import com.esri.core.tasks.na.RouteResult;

import java.util.List;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.agmobile.mapengine.route.view
 * @createTime 创建时间 ：17/1/16
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 17/1/16
 */

public interface IRouteResultView {

    /**
     * 注入当前View的presenter
     * @param presenter
     */
    void setPresenter(IRouteResultPresenter presenter);

    void showResultView(Context context,
                        MapView mapView,
                        RouteResult result,
                        Route route,
                        List<String> curDirections,
                        List<String> curChineseDirections);


    void setBehavior(BottomSheetBehavior behavior) ;



    /**
     * 添加路段列表到界面
     * @param context
     * @param containView
     * @param directions
     * @param chineseDirections
     */
    void addRouteList(Context context, ViewGroup containView, List<String> directions, List<String> chineseDirections);

    /**
     * 添加功能模块顶栏显示
     * @param context
     * @param topContainer
     * @param bottomsheetContainer
     * @param mapView
     * @param listener
     */
    void addTopbarView(Context context, ViewGroup topContainer, ViewGroup bottomsheetContainer, MapView mapView, IRouteBackListener listener);

    void destory();
}
