package com.augurit.agmobile.mapengine.layerquery.view;

import android.app.Activity;

import com.augurit.agmobile.mapengine.common.ILoadingView;
import com.augurit.am.fw.common.IView;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;

import java.util.List;

/**
 * 图层查询模块结果显示视图
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerquery.view
 * @createTime 创建时间 ：2017-02-07
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-07
 * @modifyMemo 修改备注：
 */

public interface ILayerQueryResultView extends IView,ILoadingView{

    void showDrawGeometryTool();

    void hideDrawGeometryTool();

    MapView getMapView();

    void showQueryResult(String searchKey,int size, List<AMFindResult> results);

    void addViewToContainer();

    void hideLayerQueryResult();

    void drawLayerQueryResultOnMap(List<AMFindResult> results);

    void showAttributeInfoPopup(String searchText,AMFindResult findResult);

    void clearResultOnMap();

    void removeGraphicLayer();

    OnSingleTapListener getOldOnSingleTapListener();

    void setMapOnSingleTapListener(OnSingleTapListener onSingleTapListener);

    void clearSelection();

    void clearSelectionItemOnResultList();

    int[] getGraphicIDs(float v,float v1);

    Integer getPositionInListByGraphicId(int id);

    void setSelectedGraphic(int id);

    AMFindResult getAgFindResultByGraphicId(int id);

    void highlightItemInList(int position);

    void setOnLayerQueryResultListItemClickListener(OnRecyclerItemClickListener<AMFindResult> findResultOnRecyclerItemClickListener);

    void removeResultView();

    void loadMoreFinished(List<AMFindResult> newAddedData,boolean  ifHasMore);

    void clearFindResultMap();

    void drawSingleResultOnMap(int positionInList, AMFindResult feature);

    /**
     * 设置是否还有更多
     * @param ifNoMore
     */
    void setIfNoMore(boolean ifNoMore);

    void hideMoreAttributeView();

    Activity getActivity();
}
