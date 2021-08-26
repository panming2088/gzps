package com.augurit.agmobile.mapengine.addrsearch.view;

import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.addrsearch.model.LocationResult;
import com.augurit.am.fw.common.IView;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.esri.android.map.MapView;

import java.util.List;
import java.util.Map;

/**
 * 描述：地名地址查询视图接口
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.addrsearch.view
 * @createTime 创建时间 ：2017-01-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-20
 */

public interface IAddressSearchResultView extends IView {

    /**
     * 初始化搜索结果
     * @param searchKey 当前搜索关键字
     * @param results 查询到的结果
     */
    void initSearchResultView(String searchKey,List<LocationResult> results);

    void addSearchResultViewToContainer();

    void showSearchResultView();

    void hideSearchResultView();

    void drawSearchResultOnMap(LocationResult locationResult);

    void clearMap();

    void showSearchErrorView();

    void showSearchEmptyView();

    void addGraphicLayer();

    void showSearchingView();

    void hideSearchingView();


   // void drawResultOnMap(LocationResult locationResult);

    void showCallout(LocationResult locationResult);

    void showSimpleLocationInfo(String title, Map<String,Object> attributes);

    void removeAllGraphics();

    void hideCallout();

    void hideSearchErrorView();

    void initRootView();

    void setOnLocationItemClickListener(OnRecyclerItemClickListener<LocationResult> onLocationItemClickListener);

    View getRootView();

    void setMapView(MapView mapView);

    void setAddressSearchResultContainer(ViewGroup addressSearchResultContainer);

    void removeGraphicLayer();

    MapView getMapView();
}
