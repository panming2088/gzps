package com.augurit.agmobile.mapengine.addrsearch.view;

import android.app.Activity;

import com.augurit.agmobile.mapengine.addrsearch.model.LocationSearchSuggestion;
import com.augurit.agmobile.mapengine.addrsearch.model.SearchHistory;
import com.augurit.am.fw.common.IView;

import java.util.List;

/**
 * 描述：地名地址搜索视图接口
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.addrsearch.view
 * @createTime 创建时间 ：2017-01-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-20
 */

public interface IAddressSearchView extends IView{


    void initSearchView();

    void addSearchViewToContainer();

    void showSearchView();

    void initHistoryView(List<SearchHistory> queryHistory);


    void disableEditTextListener();

    void enableEditTextListener();

    void setSearchText(String text);

    void hideSuggestionList();

    void refreshHistoryAdapter(List<SearchHistory> histories);

    void notifyForSuggesttDataChanged(List<LocationSearchSuggestion> locationSearchSuggestions);

    void showHistory();

    Activity getActivity();

    void hideSearchHistory();
}
