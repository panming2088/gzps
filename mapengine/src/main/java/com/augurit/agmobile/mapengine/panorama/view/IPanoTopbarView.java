package com.augurit.agmobile.mapengine.panorama.view;

import android.content.Context;
import android.view.ViewGroup;
import com.esri.android.map.MapView;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.am.map.arcgis.pano.view
 * @createTime 创建时间 ：16/12/7
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 16/12/7
 */

public interface IPanoTopbarView {
    /**
     * 全景顶栏界面接口
     *
     * @param context
     * @param mapView
     * @param container
     * @param listener
     */
    void showTopbarView(Context context, MapView mapView, ViewGroup container, IOutOfPanoListener listener);
}
