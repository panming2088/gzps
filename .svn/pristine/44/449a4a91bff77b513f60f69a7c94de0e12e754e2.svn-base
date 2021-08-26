package com.augurit.agmobile.mapengine.layermanage.dao;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.augurit.agmobile.mapengine.common.agmobilelayer.util.EncryptFileCacheHelper;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.IFileCacheHelper;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.agmobile.mapengine.layermanage.model.AgcomLayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.util.LayerConstant;
import com.augurit.agmobile.mapengine.layermanage.util.LayerFactoryProvider;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.AMFileUtil;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.file.SharedPreferencesUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.Layer;
import com.esri.core.ags.LayerServiceInfo;
import com.esri.core.ags.MapServiceInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 本地图层文件
 * 包名：com.augur.agmobile.ammap.layer1.dao
 * 类描述：
 * 创建人：Augurit20160517
 * 创建时间：2016-11-25 11:10
 * 修改人：Augurit20160517
 * 修改时间：2016-11-25 11:10
 * 修改备注：
 */
public class LocalLayerStorageDao {

    public final static SimpleDateFormat yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss", Locale.getDefault());

    public LocalLayerStorageDao() {
    }

    public static MapServiceInfo getMapServiceInfo(Context context, int layerId, String projectId, String serviceUrl) {
        String savePath = getMapServiceInfoSavePath(context, layerId, projectId);
        try {
            IFileCacheHelper amFileHelper2 = new EncryptFileCacheHelper();
            byte[] cacheFile = amFileHelper2.getOfflineCacheFile(savePath);
            if (ValidateUtil.isObjectNull(cacheFile)) {
                return null;
            }
            LogUtil.d("从" + savePath + "中读取MapServiceInfo，MapServiceInfo不为空");
            String result = new String(cacheFile);
           /* JsonReader reader = new JsonReader(new StringReader(result));
            reader.setLenient(true);
            return JsonUtil.getObject(reader, new TypeToken<List<LayerInfo>>() {
            }.getType());*/

            // String result = AMFileUtil.readUtf8(new File(savePath));
            JsonParser jsonParser = new JsonFactory().createJsonParser(result);
            MapServiceInfo mapServiceInfo = MapServiceInfo.fromJson(jsonParser, serviceUrl);
            return mapServiceInfo;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMapServiceInfoSavePath(Context context, int layerId, String projectId) {
        FilePathUtil baseInfoManager = new FilePathUtil(context);
        String serviceInfoFileSavePath = baseInfoManager.getMapServiceInfoSavePath(context, layerId, projectId);
        return serviceInfoFileSavePath;
    }

    /**
     * 从本地加载LayerServiceInfoFromLocalFile
     *
     * @param context   上下文
     * @param layerId   图层ID
     * @param projectId 专题ID
     * @return 图层元数据
     */
    //TODO 抽出去到utils下不是更好
    public static LayerServiceInfo getLayerServiceInfoFromLocalFile(Context context, int layerId, String projectId) {

        FilePathUtil filePath = new FilePathUtil(context);
        String layerServiceInfoSavePath = filePath.getLayerServiceInfoSavePath(context, layerId, projectId);
        try {
            IFileCacheHelper amFileHelper2 = new EncryptFileCacheHelper();
            byte[] bytes = amFileHelper2.getOfflineCacheFile(layerServiceInfoSavePath);
            // result = AMFileUtil.readUtf8(new File(projectFileSavePath));
            if (ValidateUtil.isObjectNull(bytes)) {
                return null;
            }
            LogUtil.d("从" + layerServiceInfoSavePath + "中读取到的LayerServiceInfo不为空");
            String result = new String(bytes);
            JsonReader reader = new JsonReader(new StringReader(result));
            reader.setLenient(true);


            //String result = AMFileUtil.readUtf8(new File(layerServiceInfoSavePath));
            JsonParser jsonParser = new JsonFactory().createJsonParser(result);
            LayerServiceInfo layerServiceInfo = LayerServiceInfo.fromJson(jsonParser);
            return layerServiceInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO 抽出去到utils下不是更好
    public static void saveMapServiceInfoToLocalFile(Context context, int layerId, String projectId, String json) {
        FilePathUtil filePathUtil = new FilePathUtil(context);
        String mapServiceInfoSavePath = filePathUtil.getMapServiceInfoSavePath(context, layerId, projectId);
        try {
            IFileCacheHelper amFileHelper2 = new EncryptFileCacheHelper();
            amFileHelper2.addOfflineCacheFile(json.getBytes(), mapServiceInfoSavePath);
            LogUtil.d("保存MapServiceInfo到" + mapServiceInfoSavePath + "成功");
            //AMFileUtil.saveStringToFile(json, Charset.defaultCharset(), new File(mapServiceInfoSavePath));
        } catch (Exception e) {
            LogUtil.d("保存图层文件失败");
        }
    }

    public synchronized List<LayerInfo> getLayerInfos(Context context, String projectId, String userId) {
        String projectFileSavePath = getProjectConfigFileStoragePath(context, projectId, userId);
        String result = null;
        IFileCacheHelper amFileHelper2 = new EncryptFileCacheHelper();
        byte[] bytes = amFileHelper2.getOfflineCacheFile(projectFileSavePath);
        // result = AMFileUtil.readUtf8(new File(projectFileSavePath));
        if (ValidateUtil.isObjectNull(bytes)) {
            return null;
        }
        LogUtil.d("从" + projectFileSavePath + "中读取图层数据，图层数据不为空");
        result = new String(bytes);
        JsonReader reader = new JsonReader(new StringReader(result));
        reader.setLenient(true);
        return JsonUtil.getObject(reader, new TypeToken<List<LayerInfo>>() {
        }.getType());
    }

    @Nullable
    private String getProjectConfigFileStoragePath(Context context, String projectId, String userId) {
        FilePathUtil manager = new FilePathUtil(context);
        String topPath = manager.getProjectFileSavePath();
        String projectFileSavePath = null;
        if (!ValidateUtil.isObjectNull(topPath)) {
            projectFileSavePath = topPath + "/project" + "/user" + userId + projectId + "-layers.txt";
        }
        return projectFileSavePath;
    }

    @Nullable
    private String getAgcomLayersFileStoragePath(Context context, String projectId, String userId) {
        FilePathUtil manager = new FilePathUtil(context);
        String topPath = manager.getProjectFileSavePath();
        String projectFileSavePath = null;
        if (!ValidateUtil.isObjectNull(topPath)) {
            projectFileSavePath = topPath + "/user" + userId + "/project" + projectId + "-agcomlayers.txt";
        }
        return projectFileSavePath;
    }

    public LayerInfo getLayerInfoById(Context context, int layerId, String projectId, String userId) {
        LayerInfo info = null;
        List<LayerInfo> infos = null;
        infos = getLayerInfos(context, projectId, userId);
        for (int i = 0; i < infos.size(); i++) {
            if (layerId == (infos.get(i).getLayerId())) {
                info = infos.get(i);
                break;
            }
        }
        return info;
    }

    public synchronized void saveLayerInfo(Context context, String projectId, String userId, List<LayerInfo> projectInfos) {

        String projectFileSavePath = getProjectConfigFileStoragePath(context, projectId, userId);

        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        String toJson = gson.toJson(projectInfos);
        IFileCacheHelper amFileHelper2 = new EncryptFileCacheHelper();
        amFileHelper2.addOfflineCacheFile(toJson.getBytes(), projectFileSavePath);
           /* AMFileUtil.saveStringToFile(toJson, Charset.defaultCharset(),
                    new File(projectFileSavePath));*/
        refreshLayerFunction(context, projectInfos);
    }

    public void deleteAllLayerInfos(Context context, List<ProjectInfo> projectInfos, String userId) {
        int count = projectInfos.size();
        for (int i = 0; i < count; i++) {
            String storagePath = getProjectConfigFileStoragePath(context, projectInfos.get(i).getProjectId(), userId);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/AGMobile/project"+SharePreferencesManager.getInstance(getApplicationContext).getUserName()+".txt";
                File file = new File(storagePath);
                if (file.exists()) {
                    if (file.delete()) {
                        Toast.makeText(context, "文件删除成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "文件删除失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /**
     * 获取从agcom接口获取到的原始数据
     *
     * @param context
     * @param projectId
     * @return
     */
    public AgcomLayerInfo getAgcomLayers(Context context, String projectId, String userId) {
        String projectFileSavePath = getAgcomLayersFileStoragePath(context, projectId, userId);
        String result = null;
        IFileCacheHelper amFileHelper2 = new EncryptFileCacheHelper();
        byte[] bytes = amFileHelper2.getOfflineCacheFile(projectFileSavePath);
        // result = AMFileUtil.readUtf8(new File(projectFileSavePath));
        if (ValidateUtil.isObjectNull(bytes)) {
            return null;
        }
        result = new String(bytes);
        //JsonReader reader = new JsonReader(new StringReader(result));// 采用宽大的模式进行解析
        // reader.setLenient(true);
        try {
            return JsonUtil.getObject(result, AgcomLayerInfo.class);
        } catch (Exception e) {
            //将解析错误的json对象保存到AMLog文件夹中
            String filePath = new FilePathUtil(context).getSavePath() + "/AMLog/解析失败的文件-" + yyyy_MM_dd_HH_mm_ss.format(new Date(System.currentTimeMillis())) + ".txt";
            try {
                AMFileUtil.saveStringToFile(result, Charset.defaultCharset(), new File(filePath));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 保存从agcom接口获取到的原始数据
     *
     * @param context
     * @param projectId
     * @param agcomLayerInfo
     */
    public  void saveAgcomLayers(Context context, String projectId, String userId, AgcomLayerInfo agcomLayerInfo) {
        String projectFileSavePath = getAgcomLayersFileStoragePath(context, projectId, userId);
        String toJson = JsonUtil.getJson(agcomLayerInfo);
        IFileCacheHelper amFileHelper2 = new EncryptFileCacheHelper();
        amFileHelper2.addOfflineCacheFile(toJson.getBytes(), projectFileSavePath);
    }

    /*********************************
     * set方法
     *********************************************************/
    public void setEditLayerUrl(Context context, String editUrl) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(LayerConstant.SP_KEY_EDIT_LAYER, editUrl);
    }

    public void setPanoLayerUrl(Context context, String panoUrl) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(LayerConstant.SP_KEY_PANO_LAYER, panoUrl);
    }

    public void setMarkLayerUrl(Context context, String markUrl) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(LayerConstant.SP_KEY_MARK_LAYER, markUrl);
    }

    public void setGPSLayerUrl(Context context, String gpsUrl) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(LayerConstant.SP_KEY_GPS_LAYER, gpsUrl);
    }

    /*********************************
     * get方法
     *********************************************************/
    public String getEditLayerUrl(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String string = sharedPreferencesUtil.getString(LayerConstant.SP_KEY_EDIT_LAYER, "");
        LogUtil.d("编辑模块的url:" + string);
        return string;
    }

    public String getPanoLayerUrl(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String string = sharedPreferencesUtil.getString(LayerConstant.SP_KEY_PANO_LAYER, "");
        LogUtil.d("全景模块的url:" + string);
        return string;
    }

    public String getMarkLayerUrl(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String string = sharedPreferencesUtil.getString(LayerConstant.SP_KEY_MARK_LAYER, "");
        LogUtil.d("标注模块的url:" + string);
        return string;
    }

    public String getGPSLayerUrl(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String string = sharedPreferencesUtil.getString(LayerConstant.SP_KEY_GPS_LAYER, "");
        LogUtil.d("GPS模块的url:" + string);
        return string;
    }

    public String getRouterServerUrl(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String string = sharedPreferencesUtil.getString(LayerConstant.SP_ROOUTE_SERVER_LAYER, "");
        LogUtil.d("路径分析模块的url:" + string);
        return string;
    }

    public String getGeoCodeServerUrl(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String string = sharedPreferencesUtil.getString(LayerConstant.SP_GEOCODE_SERVER_LAYER, "");
        LogUtil.d("地名地址模块的url:" + string);
        return string;
    }

    /*****************************
     * 标注的featurelayer
     ******************************************/
    public String getMarkPointFeatureLayerUrl(Context context) {
        //        return getMarkLayerUrl(context) + "/0";
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String string = sharedPreferencesUtil.getString(LayerConstant.SP_KEY_MARK_LAYER_POINT, "");
        LogUtil.d("点标注:" + string);
        return string;
    }

    public String getMarkLineFeatureLayerUrl(Context context) {
        //        return getMarkLayerUrl(context) + "/1";
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String string = sharedPreferencesUtil.getString(LayerConstant.SP_KEY_MARK_LAYER_LINE, "");
        LogUtil.d("线标注:" + string);
        return string;
    }

    public String getMarkPolygonFeatureLayerUrl(Context context) {
        //        return getMarkLayerUrl(context) + "/2";
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String string = sharedPreferencesUtil.getString(LayerConstant.SP_KEY_MARK_LAYER_POLYGON, "");
        LogUtil.d("面标注:" + string);
        return string;
    }

    /*****************************
     * 编辑的featurelayer
     ******************************************/
    public String getEditPointFeatureLayerUrl(Context context) {
        return getEditLayerUrl(context) + "/0";
    }

    public String getEditLineFeatureLayerUrl(Context context) {
        return getEditLayerUrl(context) + "/1";
    }

    public String getEditPolygonFeatureLayerUrl(Context context) {
        return getEditLayerUrl(context) + "/2";
    }

    /*********************************
     * 获取底图
     ***************************************************/
    public List<Layer> getBaseMap(Context context) {
        FilePathUtil projectFileSavePathManager = new FilePathUtil(context);
        String savePath = projectFileSavePathManager.getProjectFileSavePath();
        File file = new File(savePath + "/basemap.txt");
        IFileCacheHelper amFileHelper2 = new EncryptFileCacheHelper();
        byte[] bytes = amFileHelper2.getOfflineCacheFile(savePath + "/basemap.txt");
        if (bytes == null) {
            return null;
        }
        String layers = new String(bytes);
        JsonReader reader = new JsonReader(new StringReader(layers));
        reader.setLenient(true);
        // String layers = AMFileUtil.readStringToFile(file, Charset.defaultCharset());
        List<LayerInfo> infos = JsonUtil.getObject(reader, new TypeToken<List<LayerInfo>>() {
        }.getType());
        //转成可以直接add的layer
        List<Layer> baseMaps = new ArrayList<>();
        for (LayerInfo info : infos) {
            //  Layer layer = LayerFactory.getLayer(context, info.getType(), info.getLayerId(), info.getLayerTable(), info.getUrl(), true);//xcl 2017-04-07 修改获取Layer的函数
            Layer layer = LayerFactoryProvider.provideLayerFactory().getLayer(context, info);
            baseMaps.add(layer);
        }
        return baseMaps;

    }

    /**
     * 获取可查询图层
     *
     * @param context 上下文
     * @return 返回所有可查询图层，不管是否可见
     */
    public List<LayerInfo> getQueryableLayers(Context context) {
        String currentProjectId = ProjectDataManager.getInstance().getCurrentProjectId(context);
        BaseInfoManager baseInfoManager = new BaseInfoManager();
        String userId = BaseInfoManager.getUserId(context);
        List<LayerInfo> layerInfoFromLocalFile = getLayerInfos(context, currentProjectId, userId);
        List<LayerInfo> queryableLayers = new ArrayList<>();
        for (LayerInfo layerInfo : layerInfoFromLocalFile) {
            if (layerInfo.isQueryable()) {
                queryableLayers.add(layerInfo);
            }
        }
        LogUtil.d("项目中可查询图层的个数是：" + queryableLayers.size());
        return queryableLayers;
    }

    public void refreshBaseMapInfo(Context context, List<LayerInfo> projectInfos) {
        //进行遍历查找底图
        List<LayerInfo> baseMaps = new ArrayList<LayerInfo>();
        for (LayerInfo amLayerInfo : projectInfos) {
            if (amLayerInfo.isBaseMap()) {
                baseMaps.add(amLayerInfo);
            }
        }

        //变成json保存到本地
        FilePathUtil projectFileSavePathManager = new FilePathUtil(context);
        String savePath = projectFileSavePathManager.getProjectFileSavePath();//xcl 2016-12-28 将basemap的保存位置改成专题文件下
        File file = new File(savePath + "/basemap.txt");

        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        String toJson = gson.toJson(baseMaps);
        IFileCacheHelper amFileHelper2 = new EncryptFileCacheHelper();
        amFileHelper2.addOfflineCacheFile(toJson.getBytes(), savePath + "/basemap.txt");

        LogUtil.d("保存底图信息到本地成功");
        // AMFileUtil.saveStringToFile(toJson, Charset.defaultCharset(), file);
    }

    /**
     * 更新图层功能的SP文件
     *
     * @param context
     * @param layerInfos
     */
    public void refreshLayerFunction(Context context, List<LayerInfo> layerInfos) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        for (LayerInfo layerInfo : layerInfos) {
            switch (layerInfo.getRemarkFunc()) {
                case LayerConstant.PANO_LAYER:
                    sharedPreferencesUtil.setString(LayerConstant.SP_KEY_PANO_LAYER, layerInfo.getUrl());
                    break;
                case LayerConstant.EDIT_LAYER:
                    sharedPreferencesUtil.setString(LayerConstant.SP_KEY_EDIT_LAYER, layerInfo.getUrl());
                    break;
                case LayerConstant.MARK_LAYER:
                    sharedPreferencesUtil.setString(LayerConstant.SP_KEY_MARK_LAYER, layerInfo.getUrl());
                    sharedPreferencesUtil.setString(LayerConstant.SP_KEY_MARK_LAYER_POINT, layerInfo.getUrl() + "/0");
                    sharedPreferencesUtil.setString(LayerConstant.SP_KEY_MARK_LAYER_LINE, layerInfo.getUrl() + "/1");
                    sharedPreferencesUtil.setString(LayerConstant.SP_KEY_MARK_LAYER_POLYGON, layerInfo.getUrl() + "/2");
                    break;
                case LayerConstant.GPS_LAYER:
                    sharedPreferencesUtil.setString(LayerConstant.SP_KEY_GPS_LAYER, layerInfo.getUrl());
                    break;
                case LayerConstant.ROOUTE_SERVER_LAYER:
                    sharedPreferencesUtil.setString(LayerConstant.SP_ROOUTE_SERVER_LAYER, layerInfo.getUrl());
                    break;
                case LayerConstant.GEOCODE_SERVER_LAYER:
                    sharedPreferencesUtil.setString(LayerConstant.SP_GEOCODE_SERVER_LAYER, layerInfo.getUrl());
                    break;
            }
        }
        LogUtil.d("保存各模块url信息到本地成功");
    }

    /**
     * 清空图层功能的SP文件
     *
     * @param context
     */
    public void resetLayerFunction(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(LayerConstant.SP_KEY_PANO_LAYER, "");
        sharedPreferencesUtil.setString(LayerConstant.SP_KEY_EDIT_LAYER, "");
        sharedPreferencesUtil.setString(LayerConstant.SP_KEY_MARK_LAYER, "");
        sharedPreferencesUtil.setString(LayerConstant.SP_KEY_GPS_LAYER, "");
        sharedPreferencesUtil.setString(LayerConstant.SP_ROOUTE_SERVER_LAYER, "");
        sharedPreferencesUtil.setString(LayerConstant.SP_GEOCODE_SERVER_LAYER, "");
        LogUtil.d("清空各模块url信息");
    }
}
