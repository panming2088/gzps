package com.augurit.agmobile.patrolcore.search.util;

import android.content.Context;


import com.augurit.agmobile.patrolcore.search.service.IPatrolSearchService;
import com.augurit.agmobile.patrolcore.search.service.PatrolSearchServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.search.util
 * @createTime 创建时间 ：2017-06-02
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-06-02
 * @modifyMemo 修改备注：
 */

public class PatrolSearchServiceProvider {

    public static final String DEFAULT_TAG = "default_layer_factory";

    private static Map<String,IPatrolSearchService> sProviderMap = new HashMap<>();

    public static void injectSearchService(IPatrolSearchService layerFactory){
        sProviderMap.put(DEFAULT_TAG,layerFactory);
    }

    public static void injectSearchService(String tag,IPatrolSearchService layerFactory){
        sProviderMap.put(tag,layerFactory);
    }

    public static IPatrolSearchService provideSearchService(Context context){
        if (sProviderMap == null || sProviderMap.get(DEFAULT_TAG) == null){
            return new PatrolSearchServiceImpl(context);
        }
        return sProviderMap.get(DEFAULT_TAG);
    }

    public static IPatrolSearchService provideSearchService(String tag,Context context){
        if (sProviderMap == null || sProviderMap.get(tag) == null){
            return new PatrolSearchServiceImpl(context);
        }
        return sProviderMap.get(tag);
    }

    public static void clearInstance(){
        if (sProviderMap != null){
            sProviderMap.clear();
            sProviderMap = null;
        }
    }
}
