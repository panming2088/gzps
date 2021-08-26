package com.augurit.agmobile.mapengine.identify.util;

import android.content.Context;

import com.augurit.agmobile.mapengine.identify.service.IIdentifyService;
import com.augurit.agmobile.mapengine.identify.service.IdentifyService;
import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayersService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.identify.util
 * @createTime 创建时间 ：17/7/26
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/26
 * @modifyMemo 修改备注：
 */

public class IdentifyServiceFactory {

    public static final String DEFAULT_TAG = "default_service_provider";

    private static Map<String,IIdentifyService> sProviderMap = new HashMap<>();

    public static void injectLayerService(IIdentifyService layersService){
        if (sProviderMap == null){
            sProviderMap = new HashMap<>();
        }
        sProviderMap.put(DEFAULT_TAG,layersService);
    }

    public static void injectLayerService(String tag,IIdentifyService layersService){
        if (sProviderMap == null){
            sProviderMap = new HashMap<>();
        }
        sProviderMap.put(tag,layersService);
    }

    public static IIdentifyService provideLayerService(){
        if (sProviderMap.get(DEFAULT_TAG) == null){
            return new IdentifyService();
        }
        return sProviderMap.get(DEFAULT_TAG);
    }

    public static IIdentifyService provideLayerService(String tag){
        if (sProviderMap.get(tag) == null){
            return new IdentifyService();
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
