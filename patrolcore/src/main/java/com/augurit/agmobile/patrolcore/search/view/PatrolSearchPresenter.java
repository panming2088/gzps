package com.augurit.agmobile.patrolcore.search.view;

import android.Manifest;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.TextUtils;

import com.augurit.agmobile.mapengine.layermanage.view.ILayerPresenter;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.agmobile.patrolcore.common.table.model.TableChildItem;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.ControlType;
import com.augurit.agmobile.patrolcore.search.model.CompleteUploadInfo;
import com.augurit.agmobile.patrolcore.search.model.SearchResult;
import com.augurit.agmobile.patrolcore.search.service.IPatrolSearchService;
import com.augurit.agmobile.patrolcore.search.util.PatrolSearchServiceProvider;
import com.augurit.agmobile.patrolcore.search.view.filterview.FilterItem;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.loc.WGS84LocationTransform;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.search
 * @createTime 创建时间 ：2017-03-21
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-21
 * @modifyMemo 修改备注：
 */

public class PatrolSearchPresenter implements IPatrolSearchPresenter {


    private PatrolLocationManager mLocationManager;

    protected IPatrolSearchView mISearchFragment;

    protected ILayerPresenter mLayerPresenter;

    protected IPatrolSearchService mIPatrolSearchService;

    protected AtomicInteger currPage = new AtomicInteger(1);

    protected  int LOAD_SIZE_PER_PAGE = 10; //每次请求多少条数据

    protected Map<String, String> mParams;  // 当前查询条件Map

    protected String mKeywordFeild;      // 关键字查询字段

    protected String mProjectIdCur;     // 当前选择的项目ID

    protected TableDataManager mTableDataManager;

    /**
     * 构造函数
     * @param fragment 不可以为空
     * @param layerPresenter 可以为空
     * @param searchService  可以为空
     */
    public PatrolSearchPresenter(IPatrolSearchView fragment, ILayerPresenter layerPresenter, IPatrolSearchService searchService){

        this.mISearchFragment = fragment;

        this.mLayerPresenter = layerPresenter;
        this.mISearchFragment.setPresenter(this);

        if (searchService == null){
            searchService = PatrolSearchServiceProvider.provideSearchService(mISearchFragment.getApplicationContext());
        }
        this.mIPatrolSearchService = searchService;
        mParams = new HashMap<>();

        mTableDataManager = new TableDataManager(mISearchFragment.getApplicationContext());
    }

    @Override
    public void loadLayer() {
        loadMap();
    }

//    @NeedPermission(permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION})
    @Override
    public void startLocate() {
        /*PermissionsUtil2.getInstance()
                .requestPermissions(
                        mISearchFragment.getActivity() ,
                        "需要位置权限才能正常工作，请点击确定允许", 101,
                        new PermissionsUtil2.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                startLocateWithCheck();
                            }
                        },
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION);*/

        PermissionsUtil.getInstance()
                .requestPermissions(
                        mISearchFragment.getActivity() ,
                        new PermissionsUtil.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                startLocateWithCheck();
                            }

                            @Override
                            public void onPermissionsDenied(List<String> perms) {

                            }
                        },
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private void startLocateWithCheck() {
        if (mLocationManager == null) {
            mLocationManager = new PatrolLocationManager(mISearchFragment.getApplicationContext(), mISearchFragment.getMapView());
            mLocationManager.setCoordinateSystem(new WGS84LocationTransform());
        }

        mLocationManager.startLocate(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mISearchFragment.showLocationCallout(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    @Override
    public void loadData() {
        currPage.set(1);
        mISearchFragment.showLoading();
        if (mProjectIdCur != null && !mProjectIdCur.isEmpty()) {
            mParams.put("projectId", mProjectIdCur);
        }
        mIPatrolSearchService.getHistory(1,LOAD_SIZE_PER_PAGE, mParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SearchResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof SocketTimeoutException){
                            mISearchFragment.showLoadedError("服务器连接超时...");
                            ToastUtil.shortToast(mISearchFragment.getApplicationContext(),"服务器连接超时...");
                        }else {
                            mISearchFragment.showLoadedError("查询出错");
                        }
                    }

                    @Override
                    public void onNext(List<SearchResult> searchResults) {
                            mISearchFragment.hideLoading();
                            if (ValidateUtil.isListNull(searchResults)){
                                mISearchFragment.showLoadedEmpty();
                            }else {
                                mISearchFragment.showUploadHistory(searchResults);
                            }
                        currPage.addAndGet(1); //加一
                    }
                });
    }

    /**
     * 初始化筛选控件
     */
    @Override
    public void initFilterView() {
        // 获取所有项目 // TODO 之后考虑统一获取，TaskFragment在启动时也获取了一次
        String serverUrl = BaseInfoManager.getBaseServerUrl(mISearchFragment.getApplicationContext());
        String userId =BaseInfoManager.getUserId(mISearchFragment.getApplicationContext());
        String  url = serverUrl +"rest/patrol/getAllProject?userId=" + userId;
        mTableDataManager.getAllProjectByNet(url, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                if(data == null){
                    return;
                }
                List<Project> projects = (List<Project>) data;
                if(ListUtil.isEmpty(projects)){
                    return;
                }
                try {
                    // 设置项目选项到FilterView
                    FilterItem headItem = new FilterItem("", "projectId", "项目", ControlType.SPINNER, "");
                    LinkedHashMap<String, Object> spinnerDatas = new LinkedHashMap<String, Object>();
                    for (Project project : projects) {
                        spinnerDatas.put(project.getName(), project.getId());
                    }
                    headItem.setSpinnerDatas(spinnerDatas);
                    mISearchFragment.initFilterView(headItem, null, null);   // 初始化FilterView
                    // 获取关键字查询字段及筛选条件
                    String projectId = projects.get(0).getId();
                    getFilterConditions(projectId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                LogUtil.e("PartrolSearchPresenter", "获取项目失败");
            }
        });
    }

    /**
     * 获取相应的关键字查询及筛选条件配置
     * @param projectId 项目id
     */
    @Override
    public void getFilterConditions(String projectId) {
        String baseUrl = BaseInfoManager.getBaseServerUrl(mISearchFragment.getApplicationContext());
        String urlKeyword =  baseUrl + "rest/report/rptform";  // 由于需要同步执行请求，暂时不使用TableNetService
        Observable.zip(getFilterConditionsObservable(projectId),    // 筛选条件，目前从数据字典获取
                mIPatrolSearchService.getKeywordFileds(urlKeyword, projectId), // 关键字字段，目前从配置表单获取
                new Func2<List<FilterItem>, List<TableItem>, Object[]>() {
                    @Override
                    public Object[] call(List<FilterItem> filterItems, List<TableItem> tableItems) {
                        Object[] objects = new Object[2];
                        objects[0] = filterItems;
                        objects[1] = tableItems;
                        return objects;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object[]>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        LogUtil.e("获取筛选条件失败");
                    }

                    @Override
                    public void onNext(Object[] objects) {
                        List<FilterItem> filterItems = (List<FilterItem>) objects[0];
                        List<TableItem> keywordItems = (List<TableItem>) objects[1];
                        mISearchFragment.initFilterView(null, filterItems, keywordItems);
                    }
                });
        // 切换项目筛选条件
        mProjectIdCur = projectId; //todo 10.15 写死ProjectId是问题上报的id
        mProjectIdCur = SearchFragmentWithoutMap.PROJECT_ID;
        loadData();
    }

    /**
     * 获取筛选条件并将其转换为FilterItem
     */
    private Observable<List<FilterItem>> getFilterConditionsObservable(final String projectId) {
        return Observable.just(1)
                .map(new Func1<Integer, List<FilterItem>>() {
                    @Override
                    public List<FilterItem> call(Integer integer) {
                        List<DictionaryItem> dictionaries = mTableDataManager.getAllDictionariesFromDB();
                        // 过滤出筛选条件字典项
                        List<DictionaryItem> filterDics = new ArrayList<DictionaryItem>();
                        for (DictionaryItem item : dictionaries) {
                            if (item.getNote() != null &&
                                    item.getNote().contains(projectId)) {
                                int i;
                                for (i = 0; i < filterDics.size(); i++) {
                                    if (filterDics.get(i).getCode().compareTo(item.getCode()) > 0) {
                                        break;
                                    }
                                }
                                filterDics.add(i, item);
                            }
                        }
                        // 构造FilterItem
                        List<FilterItem> filterItems = new ArrayList<FilterItem>();
                        for (DictionaryItem filterDic : filterDics) {
                            FilterItem filterItem = new FilterItem(filterDic.getId(),
                                    filterDic.getValue(),
                                    filterDic.getName(),
                                    ControlType.SPINNER,    // 暂时固定Spinner类型
                                    filterDic.getPcode());
                            // 获取数据字典
                            List<TableChildItem> tableChildItems = mTableDataManager.getTableChildItemsByTypeCode(filterDic.getPcode());
                            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
                            for (TableChildItem tableChildItem : tableChildItems) {
                                String key = tableChildItem.getName();
                                String value = tableChildItem.getValue();
                                if (value == null || value.isEmpty()) {
                                    value = key;
                                }
                                map.put(key, value);
                            }
                            filterItem.setSpinnerDatas(map);
                            filterItems.add(filterItem);
                        }
                        return filterItems;
                    }
                });
    }

    @Override
    public void loadImageUrl(final SearchResult searchResult) {
         mIPatrolSearchService.getPhotos(searchResult.getPatrolId())
                 .doOnSubscribe(new Action0() {
                     @Override
                     public void call() {

                     }
                 })
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Subscriber<List<Photo>>() {
                     @Override
                     public void onCompleted() {
                         mISearchFragment.hideLoadingCompleteInfo();
                     }

                     @Override
                     public void onError(Throwable e) {
                         mISearchFragment.hideLoadingCompleteInfo();
                         mISearchFragment.showLoadingCompleteInfoFailed();
                     }

                     @Override
                     public void onNext(List<Photo> photos) {
                         mISearchFragment.showLoadingCompleteInfo();
                         List<TableItem> tableItems = mIPatrolSearchService.convert(searchResult);
                         mISearchFragment.jumpToDetailedUploadInfoPage(tableItems,photos);
                     }
                 });
    }

    @Override
    public void setPerPageLoadDataNum(int perPageLoadDataNum) {
         LOAD_SIZE_PER_PAGE = perPageLoadDataNum;
    }

    /**
     * 加载更多
     */
    @Override
    public void loadMore() {
        mIPatrolSearchService.getHistory(currPage.get(),LOAD_SIZE_PER_PAGE, mParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SearchResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof SocketTimeoutException){
                            mISearchFragment.loadMoreFailed("服务器连接超时");
                        }else {
                            mISearchFragment.loadMoreFailed("加载失败");
                        }

                    }

                    @Override
                    public void onNext(List<SearchResult> searchResults) {
                            if (ValidateUtil.isListNull(searchResults) || searchResults.size() < LOAD_SIZE_PER_PAGE ){
                                mISearchFragment.loadMoreFinished(searchResults,true);
                            }else {
                                mISearchFragment.loadMoreFinished(searchResults,false);
                            }
                        currPage.addAndGet(1); //加一
                    }
                });
    }

    @Override
    public void jumpToDetailPage(final int position, SearchResult searchResult) {

        if(TextUtils.isEmpty(searchResult.getId())){
            ToastUtil.shortToast(mISearchFragment.getApplicationContext(),"上报表主键为空，无法获取详细信息");
            return;
        }

        mISearchFragment.showLoadingCompleteInfo();
        mIPatrolSearchService.getCompleteUploadInfoBySearchResult(searchResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CompleteUploadInfo>() {
                    @Override
                    public void onCompleted() {
                        mISearchFragment.hideLoadingCompleteInfo();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("巡查模块-查询","查询详细信息失败，原因是： "+ e.getMessage());
                        mISearchFragment.hideLoadingCompleteInfo();
                        mISearchFragment.showLoadingCompleteInfoFailed();
                    }

                    @Override
                    public void onNext(CompleteUploadInfo completeUploadInfo) {
                        mISearchFragment.hideLoadingCompleteInfo();
                       // List<TableItem> tableItems = mIPatrolSearchService.getTableItemsByPosition(position);
                        mISearchFragment.jumpToDetailedUploadInfoPage(completeUploadInfo.getTableItems(),
                                completeUploadInfo.getPhotos());
                    }
                });


      /*  mIPatrolSearchService.getPhotos(searchResult.getPatrolId())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Photo>>() {
                    @Override
                    public void onCompleted() {
                        mISearchFragment.hideLoadingCompleteInfo();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mISearchFragment.hideLoadingCompleteInfo();
                        mISearchFragment.showLoadingCompleteInfoFailed();
                    }

                    @Override
                    public void onNext(List<Photo> photos) {
                        mISearchFragment.showLoadingCompleteInfo();
                        List<TableItem> tableItems = mIPatrolSearchService.getTableItemsByPosition(position);
                        mISearchFragment.jumpToDetailedUploadInfoPage(tableItems,photos);
                    }
                });*/

    }

    /**
     * 进行加载最新数据
     */
    @Override
    public void refreshData() {
        currPage.set(1);
        mIPatrolSearchService.getHistory(currPage.get(),LOAD_SIZE_PER_PAGE, mParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SearchResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof SocketTimeoutException){
                            mISearchFragment.showLoadedError("服务器连接超时");
                        }else {
                            mISearchFragment.showLoadedError("查询出错");
                        }

                    }

                    @Override
                    public void onNext(List<SearchResult> searchResults) {
                        mISearchFragment.hideLoading();
                        mISearchFragment.refreshFinished(searchResults);
                        /*if (ValidateUtil.isListNull(searchResults)){
                            mISearchFragment.showLoadedEmpty();
                        }else {
                            mISearchFragment.showUploadHistory(searchResults);
                        }*/
                        currPage.addAndGet(1); //加一
                    }
                });
    }

    @Override
    public void search(String keyWord) {
        mParams = new HashMap<>();
        if (mKeywordFeild == null) mKeywordFeild = "poision";
        mParams.put(mKeywordFeild, keyWord);
        currPage.set(1);
        mISearchFragment.showLoading();
        mIPatrolSearchService.getHistory(1, LOAD_SIZE_PER_PAGE, mParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SearchResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof SocketTimeoutException){
                            mISearchFragment.showLoadedError("服务器连接超时...");
                            ToastUtil.shortToast(mISearchFragment.getApplicationContext(),"服务器连接超时...");
                        }else {
                            mISearchFragment.showLoadedError("查询出错");
                        }
                    }

                    @Override
                    public void onNext(List<SearchResult> searchResults) {
                        mISearchFragment.hideLoading();
                        if (ValidateUtil.isListNull(searchResults)){
                            mISearchFragment.showLoadedEmpty();
                        }else {
                            mISearchFragment.showUploadHistory(searchResults);
                        }
                        currPage.addAndGet(1); //加一
                    }
                });
    }

    /**
     * 设置当前筛选参数
     * @param conditionMap 参数map
     */
    @Override
    public void setFilterParams(Map<String, String> conditionMap) {
        // 清空之前的筛选条件，保留关键字条件
        String keyword = null;
        if (mKeywordFeild != null && mParams.containsKey(mKeywordFeild)) {
            keyword = mParams.get(mKeywordFeild);
        }
        mParams.clear();
        if (keyword != null) mParams.put(mKeywordFeild, keyword);
        for (Map.Entry<String, String> entry : conditionMap.entrySet()) {
            mParams.put(entry.getKey(), entry.getValue());
        }
        refreshData();
    }

    /**
     * 设置关键字查询字段
     * @param field 字段
     */
    @Override
    public void setKeywordField(String field) {
        mKeywordFeild = field;
    }

    /**
     * 加载地图
     */
//    @NeedPermission(permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    protected void loadMap() {
        /*PermissionsUtil2.getInstance()
                .requestPermissions(
                        mISearchFragment.getActivity(),
                        "需要存储权限才能正常工作，请点击确定允许", 101,
                        new PermissionsUtil2.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                loadMapWithCheck();
                            }
                        },
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);*/

        PermissionsUtil.getInstance()
                .requestPermissions(
                        mISearchFragment.getActivity(),
                        new PermissionsUtil.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                loadMapWithCheck();
                            }

                            @Override
                            public void onPermissionsDenied(List<String> perms) {

                            }
                        },
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    protected void loadMapWithCheck() {
               /* ILayersService layersService = new LayerService2(mISearchFragment.getApplicationContext());
        ILayerView layerView = new LayerView(mISearchFragment.getApplicationContext(),mapview,null);
        ILayerPresenter layerPresenter = new LayerPresenter(layerView,layersService);*/
        if (mLayerPresenter != null){
            mLayerPresenter.loadLayer();
        }
    }
}
