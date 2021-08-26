package com.augurit.agmobile.patrolcore.search.service;

import android.content.Context;

import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableItems;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.search.dao.SearchDao;
import com.augurit.agmobile.patrolcore.search.model.NewBriefSearchResult;
import com.augurit.agmobile.patrolcore.search.model.SearchResult;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.search.service
 * @createTime 创建时间 ：2017-06-02
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-06-02
 * @modifyMemo 修改备注：
 */

public class PatrolSearchServiceImpl2 extends PatrolSearchServiceImpl {

    public static final String GET_SMALL_IMAGE_PREFIX = "img/imgSmall/";

    public PatrolSearchServiceImpl2(Context context, SearchDao searchDao) {
        super(context, searchDao);
    }

    public PatrolSearchServiceImpl2(Context context) {
        super(context);
    }

    @Deprecated
    @Override
    public Observable<List<SearchResult>> getHistory(final int index, final int loadNum) {
        //使用林峰新的接口
        return mSearchDao.getNewHistories(index, loadNum)
                .map(new Func1<NewBriefSearchResult, List<SearchResult>>() {
                    @Override
                    public List<SearchResult> call(NewBriefSearchResult tempSearchResult) {
                        List<SearchResult> searchResults = new ArrayList<SearchResult>();
                        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);
                        //进行实体类转换
                        List<List<NewBriefSearchResult.NewRows>> rows = tempSearchResult.getRows();
                        for (List<NewBriefSearchResult.NewRows> rowList : rows) {
                            SearchResult searchResult = new SearchResult();
                            for (NewBriefSearchResult.NewRows singleRow : rowList) {
                                switch (singleRow.getBrief_location().toLowerCase()) { //统一转成小写，免得服务端配错
                                    case "leftup": //左上角
                                        searchResult.setName(singleRow.getValue());
                                        break;
                                    case "rightup":
                                        searchResult.setDate(singleRow.getValue());
                                        break;
                                    case "leftdown":
                                        searchResult.setAddress(singleRow.getValue());
                                        break;
                                    case "rightdown":
                                        searchResult.setStatus(singleRow.getValue());
                                        break;
                                }
                                switch (singleRow.getField1()) {
                                    case "patrol_code"://工单编号
                                        searchResult.setPatrolId(singleRow.getValue());
                                        break;
                                    case "patrol_id": //上报表主键
                                        searchResult.setId(singleRow.getValue());
                                        break;
                                    case "photo":
                                        //得到缩略图片路径
                                       // String completePath = getSmallImageUrl(singleRow.getValue());
                                        //xcl 2017-08-14 接口已改，现在直接返回完整的url
                                        String completePath = singleRow.getValue();
                                        searchResult.setPic(completePath);
                                        break;
                                }
                            }
                            searchResult.setTotal(tempSearchResult.getTotal());
                            searchResults.add(searchResult);
                        }
                        return searchResults;
                    }
                }).

                        subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<SearchResult>> getHistory(int index, int loadNum, Map<String, String> params) {
        return mSearchDao.getNewHistories(index, loadNum, params)
                .map(new Func1<NewBriefSearchResult, List<SearchResult>>() {
                    @Override
                    public List<SearchResult> call(NewBriefSearchResult tempSearchResult) {
                        List<SearchResult> searchResults = new ArrayList<SearchResult>();
                        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);
                        //进行实体类转换
                        List<List<NewBriefSearchResult.NewRows>> rows = tempSearchResult.getRows();
                        for (List<NewBriefSearchResult.NewRows> rowList : rows) {
                            SearchResult searchResult = new SearchResult();
                            for (NewBriefSearchResult.NewRows singleRow : rowList) {
                                switch (singleRow.getBrief_location().toLowerCase()) { //统一转成小写，免得服务端配错
                                    case "leftup": //左上角
                                        //searchResult.setName(singleRow.getValue());
                                        /**
                                         * 广州排水项目有多个字段
                                         */
                                        String name = searchResult.getName();
                                        if (name == null){
                                            searchResult.setName(singleRow.getValue());
                                        }else if (!name.contains("(")){
                                            if(!StringUtil.isEmpty(singleRow.getValue())){
                                                //只允许两个字段拼接，太多导致标题过长
                                                name = name + "(" + singleRow.getValue()+")";
                                            }
                                            searchResult.setName(name);
                                        }
                                        break;
                                    case "rightup":
                                        searchResult.setDate(singleRow.getValue());
                                        break;
                                    case "leftdown":
                                        //searchResult.setAddress(singleRow.getValue());
                                        /**
                                         * 如果是地址，那么从区之后开始截取
                                         */
                                        if (singleRow.getValue().contains("区")){
                                            int index = singleRow.getValue().indexOf("区");
                                            String result = singleRow.getValue().substring(index +1);
                                            searchResult.setAddress(result);
                                        }else {
                                            searchResult.setAddress(singleRow.getValue());
                                        }
                                        break;
                                    case "rightdown":
                                        //searchResult.setStatus(singleRow.getValue());
                                        /**
                                         * 广州项目不需要状态字段，过滤掉
                                         */
                                        if (!"state".equals(singleRow.getField1())){
                                            searchResult.setStatus(singleRow.getValue().replace("00:00:00.0","")); //去掉时间
                                        }
                                        break;
                                }
                                switch (singleRow.getField1()) {
                                    case "patrol_code"://工单编号
                                        searchResult.setPatrolId(singleRow.getValue());
                                        break;
                                    case "patrol_id": //上报表主键
                                        searchResult.setId(singleRow.getValue());
                                        break;
                                    case "photo":
                                        //得到缩略图片路径
                                        //String completePath = getSmallImageUrl(singleRow.getValue());
                                        // String completePath = getSmallImageUrl(singleRow.getValue());
                                        //xcl 2017-08-14 接口已改，现在直接返回完整的url
                                        String completePath = singleRow.getValue();
                                        searchResult.setPic(completePath);
                                        break;
                                }
                            }
                            searchResult.setTotal(tempSearchResult.getTotal());
                            searchResults.add(searchResult);
                        }
                        return searchResults;
                    }
                }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<TableItem>> getKeywordFileds(String url, String projectId) {
        return mSearchDao.getKeywordFields(url, projectId)
                .map(new Func1<TableItems, List<TableItem>>() {
                    @Override
                    public List<TableItem> call(TableItems tableItems) {
                        List<TableItem> searchItems = new ArrayList<TableItem>();
                        for (TableItem tableItem : tableItems.getResult()) {
                            if (tableItem.getIs_search() != null &&
                                    tableItem.getIs_search().equals("A00602")) {
                                searchItems.add(tableItem);
                            }
                        }
                        return searchItems;
                    }
                });
    }

    /**
     * 获取筛选条件
     * @param url 获取url
     * @deprecated 目前不通过单独接口获取
     */
    @Deprecated
    @Override
    public Observable<List<TableItem>> getFilterConditions(String url) {
        return mSearchDao.getFilterConditions(url);
    }

    /*@NonNull
    protected String getSmallImageUrl(String value) {
        String baseServerUrl = BaseInfoManager.getBaseServerUrl(mContext);
        String imageUrl = baseServerUrl + GET_SMALL_IMAGE_PREFIX + value;
        LogUtil.d("获取上报图片", "缩略图的url是：" + imageUrl);
        return imageUrl;
    }*/


   /* @NonNull
    protected String getImageUrl(String value) {
        String baseServerUrl = BaseInfoManager.getBaseServerUrl(mContext);
        String imageUrl = baseServerUrl + GET_IMAGE_PREFIX + value;
        LogUtil.d("获取上报图片", "上报图片的url是：" + imageUrl);
        return imageUrl;
    }*/
}
