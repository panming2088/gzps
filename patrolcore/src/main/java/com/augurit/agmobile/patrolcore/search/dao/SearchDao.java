package com.augurit.agmobile.patrolcore.search.dao;

import android.content.Context;

import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableItems;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.search.model.BriefSearchResult;
import com.augurit.agmobile.patrolcore.search.model.GetPhotosResult;
import com.augurit.agmobile.patrolcore.search.model.NewBriefSearchResult;
import com.augurit.agmobile.patrolcore.search.model.NewGetPhotosResult;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.search.dao
 * @createTime 创建时间 ：2017-03-16
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-16
 * @modifyMemo 修改备注：
 */
public class SearchDao {

    protected AMNetwork amNetwork;
    protected SearchApi searchApi;
    protected Context mContext;

    public SearchDao(Context context){
       this.mContext = context;
    }

    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null){
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.build();
            this.amNetwork.registerApi(SearchApi.class);
            this.searchApi = (SearchApi) this.amNetwork.getServiceApi(SearchApi.class);
        }
    }


    public Observable<GetPhotosResult> getPhotos(String patrolId) {
        initAMNetwork(BaseInfoManager.getBaseServerUrl(mContext));
        return searchApi.getPhotos(patrolId);
    }

    public Observable<NewGetPhotosResult> getNewPhotos(String patrolId) {
        initAMNetwork(BaseInfoManager.getBaseServerUrl(mContext));
        return searchApi.getNewPhotos(patrolId);
    }

    public Observable<BriefSearchResult> getHistories(int page, int rows){
        initAMNetwork(BaseInfoManager.getBaseServerUrl(mContext));
        return searchApi.getHistories(page,rows);
    }

    /**
     * 林峰修改的历史摘要接口
     * @param page
     * @param rows
     * @return
     */
    public Observable<NewBriefSearchResult> getNewHistories(int page, int rows){
        initAMNetwork(BaseInfoManager.getBaseServerUrl(mContext));
        return searchApi.getNewHistories(page,rows);
    }

    public Observable<NewBriefSearchResult> getNewHistories(int page, int rows, Map<String, String> params) {
        initAMNetwork(BaseInfoManager.getBaseServerUrl(mContext));
        return searchApi.getNewHistories(page, rows, params);
    }

    public Observable<List<List<TableItem>>> getCompleteUploadInfo(String patrolId){
        initAMNetwork(BaseInfoManager.getBaseServerUrl(mContext));
        return searchApi.getCompleteUploadInfo(patrolId);
    }

    /**
     * 获取关键字查询字段配置
     */
    public Observable<TableItems> getKeywordFields(String url, String projectId) {
        return searchApi.getKeywordFileds(url, projectId);
    }

    /**
     * 获取筛选条件
     */
    public Observable<List<TableItem>> getFilterConditions(String url) {
        return searchApi.getFilterConditions(url);
    }

}
