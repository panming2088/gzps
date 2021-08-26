package com.augurit.agmobile.mapengine.project.dao;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.mapengine.layermanage.dao.LocalLayerStorageDao;
import com.augurit.agmobile.mapengine.layermanage.dao.RemoteLayerRestDao;
import com.augurit.agmobile.mapengine.layermanage.model.AgcomLayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.project.model.AgcomProjectDataMapper;
import com.augurit.agmobile.mapengine.project.model.AgwebMapper;
import com.augurit.agmobile.mapengine.project.model.AgwebProjectLayer;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.agmobile.mapengine.project.model.UserProject;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.project.dao
 * @createTime 创建时间 ：17/8/9
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/9
 * @modifyMemo 修改备注：
 */

public class AgwebRemoteProjectRestDao extends RemoteProjectRestDao {

    @Override

    public List<ProjectInfo> getAllProjects(Context context, String userId) throws IOException {
        BaseInfoManager baseInfoManager = new BaseInfoManager();
        String baseServerUrl = BaseInfoManager.getRestSystemUrl(context);
       // String url = BaseInfoManager.getBaseServerUrl(context) + "rest/system/getSpecialLayer/";
        String url = BaseInfoManager.getBaseServerUrl(context) + "rest/system/getSpecialLayer/" + BaseInfoManager.getUserId(context);
        LogUtil.d("请求的URL是：" + url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okhttp3.Response response = client.newCall(request).execute();
        String result = response.body().string();
        AgwebProjectLayer agwebProjectLayer = null;

        if (response.isSuccessful() && !TextUtils.isEmpty(result)&& !result.contains("<!--")) {
            agwebProjectLayer = JsonUtil.getObject(result, AgwebProjectLayer.class);

        }else {
            //从rest/system/getSpecialLayer获取
             url = BaseInfoManager.getBaseServerUrl(context) + "rest/system/getSpecialLayer" ;
             request = new Request.Builder().url(url).build();
             response = client.newCall(request).execute();
             result = response.body().string();
            agwebProjectLayer = JsonUtil.getObject(result, AgwebProjectLayer.class);
        }

        List<AgwebProjectLayer.AgwebProject> agwebProjects = agwebProjectLayer.getResult();

        AgwebMapper.AgwebMapperTransformResult transform = new AgwebMapper().transform(agwebProjects);//转换成AGMobile中通用的projectInfo和LayerInfo

        return getProjectInfos(transform, context);
    }

    private List<ProjectInfo> getProjectInfos(AgwebMapper.AgwebMapperTransformResult objects, Context context) {

        List<ProjectInfo> projects = objects.getProjectInfos();
        Map<String, List<LayerInfo>> layerMap = objects.getLayerMap();

        //进行完善图层中的子图层信息,并保存到本地
        Set<Map.Entry<String, List<LayerInfo>>> entries = layerMap.entrySet();
        RemoteLayerRestDao remoteLayerRestDao = new RemoteLayerRestDao();
        LocalLayerStorageDao localLayerStorageDao = new LocalLayerStorageDao();
        for (Map.Entry<String, List<LayerInfo>> entry : entries) {
            List<LayerInfo> infoList = entry.getValue();
            if (!ListUtil.isEmpty(infoList)) {
                List<LayerInfo> completeLayerInfos = new ArrayList<LayerInfo>();
                for (LayerInfo layerInfo : infoList) {
                    try {
                        LayerInfo info = remoteLayerRestDao.completeChildAMLayerInfo(context, layerInfo);
                        completeLayerInfos.add(info); //加入填充后的子图层
                    } catch (Exception e) {
                        e.printStackTrace();
                        completeLayerInfos.add(layerInfo);//直接加入从服务端获取到的图层
                    }
                }
                //将图层信息根据专题id保存到本地
                localLayerStorageDao.saveLayerInfo(context, entry.getKey(), BaseInfoManager.getUserId(context), completeLayerInfos);
            }
        }

        //保存专题信息到本地
        LocalProjectStorageDao localProjectStorageDao = new LocalProjectStorageDao();
        localProjectStorageDao.saveProjectsToLocal(context, BaseInfoManager.getUserId(context), projects);

        return projects;
    }
}
