package com.augurit.agmobile.patrolcore.search.view;

import com.augurit.agmobile.patrolcore.search.model.SearchResult;
import com.augurit.am.fw.common.IPresenter;

import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.search
 * @createTime 创建时间 ：2017-03-21
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-21
 * @modifyMemo 修改备注：
 */

public interface IPatrolSearchPresenter extends IPresenter{

    void loadLayer();

    void startLocate();

    void loadData();

    /**
     * 初始化筛选控件
     */
    void initFilterView();

    /**
     * 获取相应的关键字查询及筛选条件配置
     * @param projectId 项目id
     */
    void getFilterConditions(String projectId);

    void loadImageUrl(SearchResult patrolId);

    void setPerPageLoadDataNum(int perPageLoadDataNum);

    void loadMore();

    void jumpToDetailPage(int position, SearchResult patrolId);

    void refreshData();

    /**
     * 关键字查询
     * @param keyWord 关键字
     */
    void search(String keyWord);

    /**
     * 设置当前筛选参数
     * @param conditionMap 参数map
     */
    void setFilterParams(Map<String, String> conditionMap);

    /**
     * 设置关键字查询字段
     * @param field 字段
     */
    void setKeywordField(String field);
}
