package com.augurit.agmobile.mapengine.addrsearch.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.addrsearch.model.SearchHistory;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.db.AMDatabase;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.addrq.service
 * @createTime 创建时间 ：2017-01-10
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-10
 */

public class BaseAddressSearchService  {



    public List<SearchHistory> getHistorySearchData() {
        /*-------------------从数据库获取数据--------------------------*/
        List<SearchHistory> models = AMDatabase.getInstance().getQueryAll(SearchHistory.class);
        return models;
    }


    public void saveSearchHistory(SearchHistory searchText) {
       /* SearchHistory searchHistory = new SearchHistory();
        searchHistory.setHistory(searchText);*/
        AMDatabase.getInstance().save(searchText);
    }


    public void clearSearchHistory() {
        AMDatabase.getInstance().deleteAll(SearchHistory.class);
    }


    public String getAddressSearchServerUrl(Context context) {
        String supportUrl = BaseInfoManager.getSupportUrl(context);
        return supportUrl;
    }
}
