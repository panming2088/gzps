package com.augurit.agmobile.mapengine.common.utils;

import android.content.Context;

import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayersService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.utils
 * @createTime 创建时间 ：17/7/17
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/17
 * @modifyMemo 修改备注：
 */

public class GeodatabaseAndShapeFileManagerFactory {

    public static final String DEFAULT_TAG = "default_provider";

    private static Map<String,GeodatabaseAndShapeFileManager> sProviderMap = new HashMap<>();

    public static void injectGeodatabaseAndShapeFileManager(GeodatabaseAndShapeFileManager layersService){
        if (sProviderMap == null){
            sProviderMap = new HashMap<>();
        }
        sProviderMap.put(DEFAULT_TAG,layersService);
    }

    public static void injectGeodatabaseAndShapeFileManager(String tag,GeodatabaseAndShapeFileManager layersService){
        if (sProviderMap == null){
            sProviderMap = new HashMap<>();
        }
        sProviderMap.put(tag,layersService);
    }

    public static GeodatabaseAndShapeFileManager provideGeodatabaseAndShapeFileManager(){
        if (sProviderMap.get(DEFAULT_TAG) == null){
            return new GeodatabaseAndShapeFileManager();
        }
        return sProviderMap.get(DEFAULT_TAG);
    }

    public static GeodatabaseAndShapeFileManager provideGeodatabaseAndShapeFileManager(String tag){
        if (sProviderMap.get(tag) == null){
            return new GeodatabaseAndShapeFileManager();
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
