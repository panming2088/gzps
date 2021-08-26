package com.augurit.agmobile.mapengine.common;

import com.augurit.agmobile.mapengine.common.utils.GeodatabaseAndShapeFileManagerFactory;
import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.layermanage.service.LayersService;
import com.augurit.agmobile.mapengine.layermanage.util.LayerFactoryProvider;
import com.augurit.agmobile.mapengine.layerquery.LayerQueryServiceFactory;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.am.fw.db.AMDatabase;
import com.esri.android.map.Layer;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common
 * @createTime 创建时间 ：2017-02-17
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-17
 * @modifyMemo 修改备注：
 */

public class AGMobileSDK {

    public static void destroy(){
        ILayersService layersService = LayerServiceFactory.getCurrentLayerService();
        if(layersService != null){
            layersService.clearAllLayer();
            layersService.clearVisibleLayer();
            layersService.clearCacheLayer();
            layersService.clearCacheDatas();
        }
        LayersService.clearInstance();
        ProjectDataManager.getInstance().clearInstance();
        MapListenerManager.clearInstance();
        GeographyInfoManager.clearInstance();
//        AMDatabase.getInstance().closeDatabase();
        GeodatabaseAndShapeFileManagerFactory.provideGeodatabaseAndShapeFileManager().clearInstance(null);
        LayerQueryServiceFactory.clearInstance();
        LayerFactoryProvider.clearInstance();
    }
}
