package com.augurit.agmobile.patrolcore.common.table.service;

import android.content.Context;


import com.augurit.agmobile.patrolcore.common.table.dao.remote.api.FuzzySearchApi;
import com.augurit.am.fw.net.AMNetwork;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.schedulers.Schedulers;


/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.table.service
 * @createTime 创建时间 ：2017/7/3
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/7/3
 * @modifyMemo 修改备注：
 */

public class FuzzySearchService {
    private AMNetwork mAMNetwork;
    private FuzzySearchApi mApi;

    public FuzzySearchService(Context context){
        String url = "http://112.74.13.45:8888//sbss/rest/address/street/";
        mAMNetwork = new AMNetwork(url);
        mAMNetwork.addLogPrint();
        mAMNetwork.addRxjavaConverterFactory();
        mAMNetwork.build();
        mAMNetwork.registerApi(FuzzySearchApi.class);
        mApi = (FuzzySearchApi)mAMNetwork.getServiceApi(FuzzySearchApi.class);

    }

    public Observable<List<Map<String,String>>> fuzzySearch(String url, String keyWords, Map<String, String> params){
        return mApi.fuzzySearch(url,keyWords,params).subscribeOn(Schedulers.io());
    }

}
