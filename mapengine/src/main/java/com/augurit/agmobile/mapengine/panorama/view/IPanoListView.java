package com.augurit.agmobile.mapengine.panorama.view;

import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.esri.android.map.MapView;
import com.esri.core.map.Graphic;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.am.map.arcgis.pano.view
 * @createTime 创建时间 ：16/12/14
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 16/12/14
 */

public interface IPanoListView<T> {
    /**
     * 显示全景数据列表
     * @param container
     * @param mapView
     * @param result
     * @param onRecyclerItemClickListener
     */
    void showPanoList(ViewGroup container, MapView mapView, T result,
                      OnRecyclerItemClickListener<Graphic> onRecyclerItemClickListener);


    /**
     * 设置退出列表界面回调接口
     *
     * @param callback
     */
    void setBackListener(PanoCallback callback);
}
