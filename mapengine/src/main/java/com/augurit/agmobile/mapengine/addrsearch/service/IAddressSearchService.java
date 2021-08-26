package com.augurit.agmobile.mapengine.addrsearch.service;


import android.content.Context;

import com.augurit.agmobile.mapengine.addrsearch.model.LocationResult;
import com.augurit.agmobile.mapengine.addrsearch.model.LocationSearchSuggestion;
import com.augurit.agmobile.mapengine.addrsearch.model.SearchHistory;
import com.augurit.agmobile.mapengine.common.model.FeatureSet;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import java.util.List;

import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.addrq.service
 * @createTime 创建时间 ：2017-01-10
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-10
 */

public interface IAddressSearchService {

    /**
     * 模糊查询
     * @param keyword
     * @return
     */
    Observable<List<LocationSearchSuggestion>> suggest(String keyword, Point center,  SpatialReference spatialReference);

    /**
     * 根据模糊查询的结果返回结果
     * @param location
     * @return
     */
    Observable<List<LocationResult>> searchBySuggestionResult(String location);

    List<SearchHistory> getHistorySearchData();

    void saveSearchHistory(SearchHistory searchText);

    void clearSearchHistory();

    /**
     * 根据关键字进行查询（不是模糊查询结果）
     * @param key
     * @return
     */
    Observable<List<LocationResult>> searchLocationByKeyWord(String key);

    String getAddressSearchServerUrl(Context context);

    Observable<FeatureSet> getDetailedAddressInfo(LocationResult locationResult);
}
