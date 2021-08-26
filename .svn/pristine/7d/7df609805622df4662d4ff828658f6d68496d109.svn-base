package com.augurit.agmobile.gzps.common.search;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.gzps.common.search.util.model.SearchHistory;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.db.AMQueryBuilder;
import com.augurit.am.fw.db.AMWhereBuilder;
import com.augurit.am.fw.utils.ListUtil;

import java.util.ArrayList;

/**
 * Created by Won on 2017/1/13.
 */

public class SearchHistoryDao {
    private String typeName;
    private Context mContext;

    public SearchHistoryDao(Context context, String name) {
        this.typeName = name;
        this.mContext = context;
    }

    /**
     * 查询全部搜索记录
     */
    public ArrayList<String> queryAllHistoryForId() {
        ArrayList<String> historys = new ArrayList<>();
        String userId = BaseInfoManager.getUserId(mContext);
        AMWhereBuilder whereBuilder = AMWhereBuilder.create(SearchHistory.class);
        whereBuilder.where("1=1");
        if (!TextUtils.isEmpty(userId)) {
            whereBuilder.and("userId=?", userId);
        }
        if (!TextUtils.isEmpty(typeName)) {
            whereBuilder.and("typeName=?",typeName);
        }
        ArrayList<SearchHistory> query = AMDatabase.getInstance().query(
                new AMQueryBuilder<SearchHistory>(SearchHistory.class)
                        .where(whereBuilder));
        if(ListUtil.isEmpty(query)){
            return historys;
        }
        for(SearchHistory searchHistory:query){
            historys.add(searchHistory.getKey());
        }
        return historys;
    }

    /**
     * 插入数据到数据库
     */
    public void insertHistory(String keyword) {
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setUserId(BaseInfoManager.getUserId(mContext));
        searchHistory.setTypeName(typeName);
        searchHistory.setKey(keyword);

        //保存该用户的所有任务
        AMDatabase.getInstance().save(searchHistory);
    }

    /**
     * 删除某条数据
     */
    public void deleteHistory(String keyword) {
        AMDatabase amDatabase = AMDatabase.getInstance();
        String userId = BaseInfoManager.getUserId(mContext);
        AMWhereBuilder whereBuilder = AMWhereBuilder.create(SearchHistory.class);
        whereBuilder.where("1=1");
        if (!TextUtils.isEmpty(userId)) {
            whereBuilder.and("userId=?", userId);
        }
        if (!TextUtils.isEmpty(typeName)) {
            whereBuilder.and("typeName=?",typeName );
        }
        if (!TextUtils.isEmpty(keyword)) {
            whereBuilder.and("key=?", keyword );
        }
        amDatabase.deleteWhere(whereBuilder);
    }

    /**
     * 删除全部数据
     */
    public void deleteAllHistory() {
        AMDatabase amDatabase = AMDatabase.getInstance();
        String userId = BaseInfoManager.getUserId(mContext);
        AMWhereBuilder whereBuilder = AMWhereBuilder.create(SearchHistory.class);
        whereBuilder.where("1=1");
        if (!TextUtils.isEmpty(userId)) {
            whereBuilder.and("userId=?", userId);
        }
        if (!TextUtils.isEmpty(typeName)) {
            whereBuilder.and("typeName=?",typeName );
        }
        amDatabase.deleteWhere(whereBuilder);
    }

}
