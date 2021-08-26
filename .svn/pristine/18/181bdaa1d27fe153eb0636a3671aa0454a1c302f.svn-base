package com.augurit.agmobile.mapengine.marker.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.layerquery.DefaultLayerQueryServiceFactory;
import com.augurit.agmobile.mapengine.layerquery.ILayerQueryServiceProvider;
import com.augurit.agmobile.mapengine.layerquery.service.ILayerQueryService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerquery
 * @createTime 创建时间 ：2017-04-26
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-26
 * @modifyMemo 修改备注：
 */

public class MarkServiceFactory {

    public static final String DEFAULT_TAG = "default_mark_service";

    private static Map<String,IMarkService> sProviderMap = new HashMap<>();

    public static void injectMarkService(IMarkService markService){
        sProviderMap.put(DEFAULT_TAG,markService);
    }

    public static void injectMarkService(String tag,IMarkService markService){
        sProviderMap.put(tag,markService);
    }

    public static IMarkService provideMarkService(Context context){
        if (sProviderMap.get(DEFAULT_TAG) == null){
            return new MarkService(context);
        }
        return sProviderMap.get(DEFAULT_TAG);
    }

    public static IMarkService provideMarkService(Context context, String tag){
        if (sProviderMap.get(tag) == null){
            return new MarkService(context);
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
