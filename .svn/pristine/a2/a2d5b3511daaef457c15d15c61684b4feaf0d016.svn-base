package com.augurit.agmobile.patrolcore.search.view;

import android.app.Activity;
import android.location.Location;

import com.augurit.agmobile.mapengine.common.ILoadingView;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.search.model.SearchResult;
import com.augurit.agmobile.patrolcore.search.view.filterview.FilterItem;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.common.IView;
import com.esri.android.map.MapView;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.search
 * @createTime 创建时间 ：2017-03-21
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-21
 * @modifyMemo 修改备注：
 */

public interface IPatrolSearchView extends IView,ILoadingView{

    //跳转到详情页面时需要用到的Key
    String TABLE_ITEMS = "tableitems";
    String PHOTOS = "photos";

    Activity getActivity();

    MapView getMapView();

    void showLocationCallout(Location location);

    void showUploadHistory(List<SearchResult> searchHistories);

    void initFilterView(FilterItem headItem, List<FilterItem> filterItems, List<TableItem> keywordItems);

    void showLoadingCompleteInfo();

    void hideLoadingCompleteInfo();

    void showLoadingCompleteInfoFailed();

    void jumpToDetailedUploadInfoPage(List<TableItem> tableItems, List<Photo> photoUrls);

    void loadMoreFinished(List<SearchResult> newAddedData, boolean ifHasMore);

    void refreshFinished(List<SearchResult> newData);

    void loadMoreFailed(String message);
}
