package com.augurit.agmobile.patrolcore.search.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.search.dao.SearchDao;
import com.augurit.agmobile.patrolcore.search.model.CompleteUploadInfo;
import com.augurit.agmobile.patrolcore.search.model.FilterCondition;
import com.augurit.agmobile.patrolcore.search.model.NewBriefSearchResult;
import com.augurit.agmobile.patrolcore.search.model.SearchResult;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;


/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.search.service
 * @createTime 创建时间 ：2017-03-21
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-21
 * @modifyMemo 修改备注：
 */

public class FakePatrolSearchService extends PatrolSearchServiceImpl {

    private Context mContext;
    private SearchDao mSearchDao;

    //假数据
    private String[] names = new String[]{"快车道坑洞", "块砌路面裂缝", "人行道残缺", "人行道变形", "沥青路面坑槽"
            , "水泥路面拱起", "快车道沉陷", "人行道裂缝", "人行道变形", "人行道变形", "人行道变形", "人行道变形"};

    private String[] status = new String[]{"待处理", "已办结", "待复核", "已办结", "待处理"
            , "待处理", "待处理", "待处理", "待处理", "待处理", "待处理", "待处理"};

    private String[] address = new String[]{"高普路1033号", "高普路1030号", "高普路1029号",
            "高普路1031号", "高普路1029号"
            , "软件路10号", "思蕴路33号", "思蕴路1-22号", "思蕴路1-11号", "思蕴路1-11号",
            "思蕴路1-11号", "思蕴路1-11号"
    };

    private LatLng[] locations = new LatLng[]{new LatLng(23.18171, 113.420711),
            new LatLng(23.18171, 113.420711),
            new LatLng(23.18171, 113.420711),
            new LatLng(23.18171, 113.420711),
            new LatLng(23.18171, 113.420711),
            new LatLng(23.18171, 113.420711),
            new LatLng(23.18171, 113.420711),
            new LatLng(23.18171, 113.420711),
            new LatLng(23.18171, 113.420711),
            new LatLng(23.18171, 113.420711),
            new LatLng(23.18171, 113.420711),
            new LatLng(23.18171, 113.420711)};


    //筛选假数据
    private String headers[] = {"附近", "类别", "排序", "筛选"};

    private String citys[] = {"不限", "50米以内", "100米以内", "500米以内", "1千米以内", "超过1千米"};
    private String ages[] = {"不限", "快车道线裂", "快车道坑洞", "井盖丢失", "雨水管道破损", "灯杆倾斜"};
    private String sexs[] = {"不限", "按时间排序", "按距离排序"};
    private String constellations[] = {"不限", "已办结", "待处置", "待复核", "待提交"};


    protected List<List<TableItem>> mTempSearchResult = new ArrayList<>();

    public FakePatrolSearchService(Context context, SearchDao searchDao) {
        super(context, searchDao);
    }

    public FakePatrolSearchService(Context context) {
        super(context);
    }

    @Override
    public Observable<List<SearchResult>> getHistory(final int index, final int loadNum) {
        //使用林峰新的接口
        return mSearchDao.getNewHistories(1, 10)
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
                                        String name = searchResult.getName();
                                        if (name == null){
                                            searchResult.setName(singleRow.getValue());
                                        }else {
                                            String newname = name + "(" + singleRow.getValue()+")";
                                            searchResult.setName(newname);
                                        }
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
                                    case "":
                                        //如果没有位置信息
                                        switch (singleRow.getField1()) {
                                            case "patrol_code"://工单编号
                                                searchResult.setPatrolId(singleRow.getValue());
                                                break;
                                            case "patrol_id": //上报表主键
                                                searchResult.setId(singleRow.getValue());
                                                break;
                                            case "photo":
                                                searchResult.setPic(singleRow.getValue());
                                                break;
                                        }
                                        break;
                                }
                            }
                            searchResults.add(searchResult);
                        }
                        return searchResults;
                    }
                }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<FilterCondition>> getFilterCondition() {
        return Observable.fromCallable(new Callable<List<FilterCondition>>() {
            @Override
            public List<FilterCondition> call() throws Exception {
                List<FilterCondition> filterConditions = new ArrayList<FilterCondition>();
                for (int i = 0; i < headers.length; i++) {
                    FilterCondition filterCondition = new FilterCondition();
                    filterCondition.setFilterType(headers[i]);
                    List<String> items = new ArrayList<String>();
                    if (i == 0) {
                        items.clear();
                        provideItems(items, citys);
                    } else if (i == 1) {
                        items.clear();
                        provideItems(items, ages);
                    } else if (i == 2) {
                        items.clear();
                        provideItems(items, sexs);
                    } else {
                        items.clear();
                        provideItems(items, constellations);
                    }
                    filterCondition.setItems(items);
                    filterConditions.add(filterCondition);
                }
                return filterConditions;
            }

            private void provideItems(List<String> items, String[] citys) {
                Collections.addAll(items, citys);
            }
        });
    }


    @Override
    public List<TableItem> convert(SearchResult searchResult) {
        return new ArrayList<>();
    }

    @Override
    public Observable<CompleteUploadInfo> getCompleteUploadInfoBySearchResult(SearchResult searchResult) {
        return Observable.zip(
                mSearchDao.getCompleteUploadInfo(searchResult.getId()),
                getPhotos(searchResult.getPatrolId()), new Func2<List<List<TableItem>>, List<Photo>, CompleteUploadInfo>() {
                    @Override
                    public CompleteUploadInfo call(List<List<TableItem>> lists, List<Photo> photos) {
                        CompleteUploadInfo completeUploadInfo = new CompleteUploadInfo();
                        completeUploadInfo.setPhotos(photos);
                        completeUploadInfo.setTableItems(lists.get(0));
                        return completeUploadInfo;
                    }
                }).subscribeOn(Schedulers.io());
    }



}
