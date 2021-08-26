package com.augurit.agmobile.mapengine.layerquery;

import android.content.Context;

import com.augurit.agmobile.mapengine.layerquery.service.AgcomLayerQueryService;
import com.augurit.agmobile.mapengine.layerquery.service.ILayerQueryService;

import java.util.HashMap;
import java.util.Map;

/**
 * 图层查询service提供类
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerquery
 * @createTime 创建时间 ：2017-04-26
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-26
 * @modifyMemo 修改备注：
 */

public class LayerQueryServiceFactory {

    public static final String DEFAULT_TAG = "default_service_provider";

    private static Map<String,ILayerQueryService> sProviderMap = new HashMap<>();

    public static void injectLayerQueryService(ILayerQueryService layerServiceProvider){
        sProviderMap.put(DEFAULT_TAG,layerServiceProvider);
    }

    public static void injectLayerQueryService(String tag,ILayerQueryService layerServiceProvider){
        sProviderMap.put(tag,layerServiceProvider);
    }

    public static ILayerQueryService provideService(Context context){
        if (sProviderMap == null || sProviderMap.get(DEFAULT_TAG) == null){
            return new AgcomLayerQueryService(context);
        }
        return sProviderMap.get(DEFAULT_TAG);
    }

    public static ILayerQueryService provideService(Context context, String tag){
        if (sProviderMap.get(tag) == null){
            return new AgcomLayerQueryService(context);
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
