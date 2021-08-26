package com.augurit.agmobile.mapengine.layermanage.util;

import android.content.Context;


import java.util.HashMap;
import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layermanage.util
 * @createTime 创建时间 ：2017-04-27
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-27
 * @modifyMemo 修改备注：
 */

public class LayerFactoryProvider {

    public static final String DEFAULT_TAG = "default_layer_factory";

    private static Map<String,ILayerFactory> sProviderMap = new HashMap<>();

    public static void injectLayerFactory(ILayerFactory layerFactory){
        if(sProviderMap == null){
            sProviderMap = new HashMap<>();
        }
        sProviderMap.put(DEFAULT_TAG,layerFactory);
    }

    public static void injectLayerFactory(String tag,ILayerFactory layerFactory){
        if(sProviderMap == null){
            sProviderMap = new HashMap<>();
        }
        sProviderMap.put(tag,layerFactory);
    }

    public static ILayerFactory provideLayerFactory(){
        if (sProviderMap == null || sProviderMap.get(DEFAULT_TAG) == null){
            return new LayerFactory();
        }
        return sProviderMap.get(DEFAULT_TAG);
    }

    public static ILayerFactory provideLayerFactory(String tag){
        if (sProviderMap == null || sProviderMap.get(tag) == null){
            return new LayerFactory();
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
