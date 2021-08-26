package com.augurit.agmobile.mapengine.project.dao;

import com.augurit.am.fw.cache.cache.Cache;
import com.augurit.am.fw.utils.common.ValidateUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.proj.newarc.dao
 * @createTime 创建时间 ：2016-12-03
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-03
 */

public final class CacheProjectDao {

   // private static String sCurrentProjectId = null;

    private static Map<String,String> cacheProjects ;


    private CacheProjectDao() {
    }

    public static String getCurrentProjectId(String userId) {
        if (cacheProjects == null){
            return null;
        }
        return cacheProjects.get(userId);
       // return sCurrentProjectId;
    }

    public static void setCurrentProjectId(String userId,String currentProjectId) {
        if (cacheProjects == null){
            cacheProjects = new HashMap<>();
        }
        cacheProjects.put(userId, currentProjectId);
        //sCurrentProjectId = currentProjectId;
    }

    public static void clearInstance(){
        if (!ValidateUtil.isMapNull(cacheProjects)){
            cacheProjects.clear();
            cacheProjects = null;
        }

       // sCurrentProjectId = null;
    }
}
