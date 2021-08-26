package com.augurit.agmobile.mapengine.project.util;

import android.content.Context;

import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayersService;
import com.augurit.agmobile.mapengine.project.service.AgwebProjectService;
import com.augurit.agmobile.mapengine.project.service.IProjectService;
import com.augurit.agmobile.mapengine.project.service.ProjectServiceImpl;

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

public class ProjectServiceFactory {

    public static final String DEFAULT_TAG = "default_service_provider";

    private static Map<String,IProjectService> sProviderMap = new HashMap<>();

    public static void injectProjectService(IProjectService layersService){
        if (sProviderMap == null){
            sProviderMap = new HashMap<>();
        }
        sProviderMap.put(DEFAULT_TAG,layersService);
    }

    public static void injectProjectService(String tag,IProjectService layersService){
        if (sProviderMap == null){
            sProviderMap = new HashMap<>();
        }
        sProviderMap.put(tag,layersService);
    }

    public static IProjectService provideProjectService(){
        if (sProviderMap.get(DEFAULT_TAG) == null){
            return new AgwebProjectService();
        }
        return sProviderMap.get(DEFAULT_TAG);
    }

    public static IProjectService provideProjectService(String tag){
        if (sProviderMap.get(tag) == null){
            return new AgwebProjectService();
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
