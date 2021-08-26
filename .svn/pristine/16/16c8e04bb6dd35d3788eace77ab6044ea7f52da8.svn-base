package com.augurit.agmobile.mapengine.route.service;

import com.esri.core.tasks.na.RouteResult;

import java.util.List;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.agmobile.mapengine.route.service
 * @createTime 创建时间 ：17/1/16
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 17/1/16
 */

public interface IRouteService {

    /**
     * 路径分析服务
     * @param points
     * @param listener
     */
    void startPointsRoute(List<com.esri.core.geometry.Point> points, OnRouteListener listener);

    /**
     * 分析结果回调
     */
    interface OnRouteListener{
        void onFail(String errMsg);
        void onLocating(String msg);
        void onSuccess(RouteResult routeResult);
    }
}
