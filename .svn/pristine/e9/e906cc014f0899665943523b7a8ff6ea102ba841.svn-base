package com.augurit.agmobile.patrolcore.search.service;



import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.search.model.CompleteUploadInfo;
import com.augurit.agmobile.patrolcore.search.model.FilterCondition;
import com.augurit.agmobile.patrolcore.search.model.SearchResult;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.liteorm.db.annotation.Table;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.search.service
 * @createTime 创建时间 ：2017-03-21
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-21
 * @modifyMemo 修改备注：
 */

public interface IPatrolSearchService {

    Observable<List<SearchResult>> getHistory(int index, int loadNum);

    /**
     * 通过筛选条件获取最近提交历史
     * @param index
     * @param loadNum
     * @param params 筛选条件参数
     * @return
     */
    Observable<List<SearchResult>> getHistory(int index, int loadNum, Map<String, String> params);

    @Deprecated
    Observable<List<FilterCondition>> getFilterCondition();

    /**
     * 获取关键字查询字段
     */
    Observable<List<TableItem>> getKeywordFileds(String url, String projectId);

    /**
     * 获取筛选条件
     * @param url 获取url
     * @deprecated 目前不通过单独接口获取
     */
    @Deprecated
    Observable<List<TableItem>> getFilterConditions(String url);

    Observable<List<Photo>> getPhotos(String patrolId);

    List<TableItem> convert(SearchResult searchResult);

    Observable<CompleteUploadInfo> getCompleteUploadInfoBySearchResult(SearchResult searchResult);
}
