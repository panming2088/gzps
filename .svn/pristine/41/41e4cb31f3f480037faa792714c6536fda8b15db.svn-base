package com.augurit.agmobile.mapengine.addrsearch.router;

import android.content.Context;

import com.augurit.agmobile.mapengine.addrsearch.model.LocationResult;
import com.augurit.agmobile.mapengine.addrsearch.model.LocationSearchSuggestion;
import com.augurit.agmobile.mapengine.addrsearch.model.SearchHistory;
import com.augurit.agmobile.mapengine.addrsearch.service.AgcomAddressSearchService;
import com.augurit.agmobile.mapengine.addrsearch.service.IAddressSearchService;
import com.augurit.agmobile.mapengine.addrsearch.util.AddressSearchConstant;
import com.augurit.agmobile.mapengine.common.model.FeatureSet;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import java.util.List;

import rx.Observable;

/**
 * 描述：
 *
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.addrsearch.service
 * @createTime 创建时间 ：2017-01-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-20
 */

public class AddressSearchServiceRouter implements IAddressSearchService {

    private IAddressSearchService mIAddressQueryService;
    public AddressSearchServiceRouter(Context context){
        if (AddressSearchConstant.USE_AGOM_SERVICE){
            mIAddressQueryService = new AgcomAddressSearchService(context);
        }
    }


    @Override
    public Observable<List<LocationSearchSuggestion>> suggest(String keyword, Point center, SpatialReference spatialReference) {
        return mIAddressQueryService.suggest(keyword,center,spatialReference);
    }

    @Override
    public Observable<List<LocationResult>> searchBySuggestionResult(String location) {
        return mIAddressQueryService.searchBySuggestionResult(location);
    }

    @Override
    public List<SearchHistory> getHistorySearchData() {
        return mIAddressQueryService.getHistorySearchData();
    }

    @Override
    public void saveSearchHistory(SearchHistory searchText) {
        mIAddressQueryService.saveSearchHistory(searchText);
    }

    @Override
    public void clearSearchHistory() {
        mIAddressQueryService.clearSearchHistory();
    }

    @Override
    public Observable<List<LocationResult>> searchLocationByKeyWord(String key) {
        return mIAddressQueryService.searchBySuggestionResult(key);
    }

    @Override
    public String getAddressSearchServerUrl(Context context) {
        return mIAddressQueryService.getAddressSearchServerUrl(context);
    }

    @Override
    public Observable<FeatureSet> getDetailedAddressInfo(LocationResult locationResult) {
        return mIAddressQueryService.getDetailedAddressInfo(locationResult);
    }
}
