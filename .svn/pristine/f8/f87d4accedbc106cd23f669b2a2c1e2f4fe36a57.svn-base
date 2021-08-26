package com.augurit.agmobile.mapengine.layermanage.service;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * ILayerService提供类，为了能够动态的修改整个应用中的ILayerService实现类，由于ILayerService在整个应用中的引用很多，当修改一个实现类时，
 * 需要同时修改其他地方的实现类，为了方便管理，故产生了该类。
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layermanage.service
 * @createTime 创建时间 ：2017-04-13
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-13
 * @modifyMemo 修改备注：
 */

public class LayerServiceFactory {

    public static final String DEFAULT_TAG = "default_service_provider";

    private static Map<String,ILayersService> sProviderMap = new HashMap<>();

    public static void injectLayerService(ILayersService layersService){
        if (sProviderMap == null){
            sProviderMap = new HashMap<>();
        }
        sProviderMap.put(DEFAULT_TAG,layersService);
    }

    public static void injectLayerService(String tag,ILayersService layersService){
        if (sProviderMap == null){
            sProviderMap = new HashMap<>();
        }
        sProviderMap.put(tag,layersService);
    }

    public static ILayersService provideLayerService(Context context){
        if (sProviderMap.get(DEFAULT_TAG) == null){
            return new LayersService(context);
        }
        return sProviderMap.get(DEFAULT_TAG);
    }

    public static ILayersService provideLayerService(Context context,String tag){
        if (sProviderMap.get(tag) == null){
            return new LayersService(context);
        }
        return sProviderMap.get(tag);
    }

    /**
     * 注意，有可能返回null
     * @return
     */
    public static ILayersService getCurrentLayerService(){
        if(sProviderMap.containsKey(DEFAULT_TAG)){
            return sProviderMap.get(DEFAULT_TAG);
        }
        return null;
    }


    public static void clearInstance(){
        if (sProviderMap != null){
            sProviderMap.clear();
            sProviderMap = null;
        }
    }

}
