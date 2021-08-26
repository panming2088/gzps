package com.augurit.agmobile.mapengine.layermanage.router;

import android.content.ComponentName;
import android.content.Context;


import com.augurit.agmobile.mapengine.layermanage.model.AgcomLayerInfo;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.agmobile.mapengine.layermanage.dao.LocalLayerStorageDao;
import com.augurit.agmobile.mapengine.layermanage.dao.RemoteLayerRestDao;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.Layer;
import com.esri.core.ags.LayerServiceInfo;
import com.esri.core.ags.MapServiceInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 包名：com.augur.agmobile.ammap.layer1.router
 * 类描述：
 * 创建人：Augurit20160517
 * 创建时间：2016-11-25 11:20
 * 修改人：Augurit20160517
 * 修改时间：2016-11-25 11:20
 * 修改备注：
 */
public class LayerRouter {

    protected LocalLayerStorageDao localLayerStorageDao;
    protected RemoteLayerRestDao remoteLayerRestDao;

    public LayerRouter() {
        localLayerStorageDao = new LocalLayerStorageDao();
        remoteLayerRestDao = new RemoteLayerRestDao();
    }

    /**
     * 获取图层信息，耗时操作
     * @param context
     * @param projectId
     * @param userId
     * @return
     * @throws IOException
     */
    public List<LayerInfo> getLayerInfos(Context context, String projectId, String userId) throws IOException {

        List<LayerInfo> layerInfos = null;
        layerInfos = localLayerStorageDao.getLayerInfos(context, projectId, userId);
        if (layerInfos == null) {
            layerInfos = remoteLayerRestDao.getLayer(context);
            if (ValidateUtil.isListNull(layerInfos)){ //如果为空
                return new ArrayList<>();
            }
            //保存一份到本地，并且更新保存图层用途的sp文件
            localLayerStorageDao.saveLayerInfo(context, projectId, userId,layerInfos);
            LogUtil.d("保存图层文件到本地成功");
            //保存到本地和缓存中
            //localLayerStorageDao.saveLayerInfo(context, projectId,layerInfos);
            //更新底图信息
            localLayerStorageDao.refreshBaseMapInfo(context,layerInfos);
            LogUtil.d("更新底图信息成功");
            //更新功能图层信息
            localLayerStorageDao.refreshLayerFunction(context,layerInfos);
            LogUtil.d("更新底图信息成功");
        }

        return layerInfos;
    }

    public AgcomLayerInfo getAgcomLayerInfo(Context context, String projectId, String userId){
        AgcomLayerInfo agcomLayerInfo = null;
        agcomLayerInfo = localLayerStorageDao.getAgcomLayers(context, projectId,userId);
        if(agcomLayerInfo == null){
            agcomLayerInfo = remoteLayerRestDao.getAgcomLayersFromInternet(context);
            localLayerStorageDao.saveAgcomLayers(context, projectId, userId,agcomLayerInfo);
        }
        return agcomLayerInfo;
    }

    public AgcomLayerInfo getAgcomLayerInfoFromLocal(Context context, String projectId, String userId){
        AgcomLayerInfo agcomLayerInfo = null;
        agcomLayerInfo = localLayerStorageDao.getAgcomLayers(context, projectId,userId);
        return agcomLayerInfo;
    }

    /**
     * 下载MapServiceInfo
     * @param url
     * @param onLoadDataListener
     */
    public void downloadMapServiceInfoFromInternet(String url, Callback2<String> onLoadDataListener) {
        remoteLayerRestDao.downloadMapServiceInfoFromInternet(url, onLoadDataListener);
    }

    /**
     * 下载MapServiceInfo，耗时操作，请确保在子线程进行
     * @param url 下载图层的url
     */
    public String downloadMapServiceInfoFromInternet(String url) throws IOException {
       return remoteLayerRestDao.downloadMapServiceInfoFromInternet(url);
    }

    /**
     * 从本地加载LayerServiceInfoFromLocalFile
     * @param context 上下文
     * @param layerId 图层ID
     * @param projectId 专题ID
     * @return 图层元数据
     */
    public LayerServiceInfo getLayerServiceInfoFromLocalFile(Context context, int layerId, String projectId){
        return LocalLayerStorageDao.getLayerServiceInfoFromLocalFile(context, layerId, projectId);
    }


    /**
     * 从本地加载MapServiceInfo
     * @param context 上下文
     * @param layerId 图层ID
     * @param projectId 专题ID
     * @return 图层元数据
     */
    public MapServiceInfo getMapServiceInfoFromLocalFile(Context context, int layerId, String projectId, String serviceUrl){
        return LocalLayerStorageDao.getMapServiceInfo(context, layerId, projectId, serviceUrl);
    }

    public void saveMapServiceInfoToLocalFile(Context context, int layerId, String projectId, String json) {
        LocalLayerStorageDao.saveMapServiceInfoToLocalFile(context,layerId,projectId,json);
    }

    /**
     * 删除除了地图缓存地图外的所有文件
     * @param context
     * @param list
     */
    public void deleteAllLayerExcludedCacheMap(Context  context,List<ProjectInfo> list){
        //首先要先清空缓存中的数据
        localLayerStorageDao.deleteAllLayerInfos(context,list, BaseInfoManager.getUserId(context));
    }

    /*********************************set方法*********************************************************/
    public void setEditLayerUrl(Context context,String editUrl) {
        localLayerStorageDao.setEditLayerUrl(context,editUrl);
    }

    public void setPanoLayerUrl(Context context,String panoUrl) {
       localLayerStorageDao.setPanoLayerUrl(context,panoUrl);
    }

    public void setMarkLayerUrl(Context context,String markUrl) {
       localLayerStorageDao.setMarkLayerUrl(context,markUrl);
    }

    public void setGPSLayerUrl(Context context,String gpsUrl) {
       localLayerStorageDao.setGPSLayerUrl(context,gpsUrl);
    }

    /*********************************get方法*********************************************************/
    public String getEditLayerUrl(Context context) {
       return localLayerStorageDao.getEditLayerUrl(context);
    }

    public String getPanoLayerUrl(Context context) {
       return localLayerStorageDao.getPanoLayerUrl(context);
    }

    public String getMarkLayerUrl(Context context) {
       return localLayerStorageDao.getMarkLayerUrl(context);
    }

    public String getGPSLayerUrl(Context context) {
        return localLayerStorageDao.getGPSLayerUrl(context);
    }


    public String getRouterServerUrl(Context context){
        return localLayerStorageDao.getRouterServerUrl(context);
    }
    public String getGeoCodeServerUrl(Context context){
      return localLayerStorageDao.getGeoCodeServerUrl(context);
    }

    /*****************************标注的featurelayer******************************************/
    public String getMarkPointFeatureLayerUrl(Context context){
       return localLayerStorageDao.getMarkPointFeatureLayerUrl(context);
    }
    public String getMarkLineFeatureLayerUrl(Context context){
        return localLayerStorageDao.getMarkLineFeatureLayerUrl(context);
    }
    public String getMarkPolygonFeatureLayerUrl(Context context){
        return localLayerStorageDao.getMarkPolygonFeatureLayerUrl(context);
    }
    /*****************************编辑的featurelayer******************************************/
    public String getEditPointFeatureLayerUrl(Context context){
        return localLayerStorageDao.getEditPointFeatureLayerUrl(context);
    }
    public String getEditLineFeatureLayerUrl(Context context){
        return localLayerStorageDao.getEditLineFeatureLayerUrl(context);
    }
    public String getEditPolygonFeatureLayerUrl(Context context){
        return localLayerStorageDao.getEditPolygonFeatureLayerUrl(context);
    }

    /*********************************获取底图***************************************************/
    public List<Layer> getBaseMap(Context context){
      return localLayerStorageDao.getBaseMap(context);
    }


    /**
     * 获取可查询图层
     * @param context 上下文
     * @return 返回所有可查询图层，不管是否可见
     */
    public List<LayerInfo> getQueryableLayers(Context context){
        List<LayerInfo> queryableLayers = localLayerStorageDao.getQueryableLayers(context);
        return queryableLayers;
    }


    /**
     * 从本地加载数据
     *
     * @param context   上下文
     * @param projectId 专题ID
     * @return 图层数据
     */
    public List<LayerInfo> getLayerInfoFromLocalFile(Context context, String projectId) {
       return  localLayerStorageDao.getLayerInfos(context,projectId,BaseInfoManager.getUserId(context));
    }

    public void resetFunctionLayer(Context context){
        localLayerStorageDao.resetLayerFunction(context);
    }

}
