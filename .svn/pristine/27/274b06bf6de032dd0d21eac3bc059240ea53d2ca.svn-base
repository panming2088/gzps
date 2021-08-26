package com.augurit.agmobile.mapengine.project.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.layermanage.dao.LocalLayerStorageDao;
import com.augurit.agmobile.mapengine.layermanage.dao.RemoteLayerRestDao;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.project.dao.AgwebRemoteProjectRestDao;
import com.augurit.agmobile.mapengine.project.dao.IAgwebProjectLayerApi;
import com.augurit.agmobile.mapengine.project.dao.LocalProjectStorageDao;
import com.augurit.agmobile.mapengine.project.model.AgwebMapper;
import com.augurit.agmobile.mapengine.project.model.AgwebProjectLayer;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.project.service
 * @createTime 创建时间 ：2017-07-11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-07-11
 * @modifyMemo 修改备注：
 */

public class AgwebProjectService extends ProjectServiceImpl {


    public AgwebProjectService(){
        super();
        this.mRouter.setRemoteProjectRestDao(new AgwebRemoteProjectRestDao());
    }

    /*
    @Override
    public Observable<List<ProjectInfo>> getProjects(final Context context) {


        final List<ProjectInfo> projectsFromLocal = mRouter.getProjectsFromLocal(context, BaseInfoManager.getUserName(context));
        //返回本地
        if (projectsFromLocal != null) {
            return Observable.fromCallable(new Callable<List<ProjectInfo>>() {
                @Override
                public List<ProjectInfo> call() throws Exception {
                    return projectsFromLocal;
                }
            }).subscribeOn(Schedulers.io());
        }
        //从网络获取
        return getProjectFromNet(context)
                .map(new Func1<List<ProjectInfo>, List<ProjectInfo>>() {
                    @Override
                    public List<ProjectInfo> call(List<ProjectInfo> projectInfos) {
                        mRouter.saveProjectsToLocal(context, projectInfos, BaseInfoManager.getUserName(context));
                        return projectInfos;
                    }
                })
                .subscribeOn(Schedulers.io());
    }*/

    private Observable<List<ProjectInfo>> getProjectFromNet(final Context context) {
        AMNetwork amNetwork = new AMNetwork(BaseInfoManager.getBaseServerUrl(context));
        amNetwork.addLogPrint();
        amNetwork.addRxjavaConverterFactory();
        amNetwork.build();
        amNetwork.registerApi(IAgwebProjectLayerApi.class);
        IAgwebProjectLayerApi agwebLayerApi = (IAgwebProjectLayerApi) amNetwork.getServiceApi(IAgwebProjectLayerApi.class);
        //String url = BaseInfoManager.getSupportUrl(context) + "rest/system/getAllProjectLayers/" + BaseInfoManager.getUserName(context);

        String url = BaseInfoManager.getBaseServerUrl(context) + "rest/system/getSpecialLayer/";
       // String url = BaseInfoManager.getBaseServerUrl(context) + "rest/system/getSpecialLayer/" + BaseInfoManager.getUserName(context);
        LogUtil.d("agweb获取专题图层的接口url:" + url);

        return agwebLayerApi
                .getAllProjectLayers(url) //获取所有的专题图层
                .map(new Func1<AgwebProjectLayer, List<AgwebProjectLayer.AgwebProject>>() {
                    @Override
                    public List<AgwebProjectLayer.AgwebProject> call(AgwebProjectLayer agwebProjectLayer) {
                        return agwebProjectLayer.getResult();
                    }
                })
                .map(new Func1<List<AgwebProjectLayer.AgwebProject>, AgwebMapper.AgwebMapperTransformResult>() {
                    @Override
                    public AgwebMapper.AgwebMapperTransformResult call(List<AgwebProjectLayer.AgwebProject> agwebProjects) {
                        return new AgwebMapper().transform(agwebProjects); //转换成AGMobile中通用的projectInfo和LayerInfo
                    }
                })
                .map(new Func1<AgwebMapper.AgwebMapperTransformResult, List<ProjectInfo>>() {
                    @Override
                    public List<ProjectInfo> call(AgwebMapper.AgwebMapperTransformResult objects) {

                        return getProjectInfos(objects, context);
                    }
                }).subscribeOn(Schedulers.io());
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
                localLayerStorageDao.saveLayerInfo(context, entry.getKey(), BaseInfoManager.getUserId(context),completeLayerInfos);
            }
        }

        //保存专题信息到本地
        LocalProjectStorageDao localProjectStorageDao = new LocalProjectStorageDao();
        localProjectStorageDao.saveProjectsToLocal(context, BaseInfoManager.getUserId(context), projects);

        return projects;
    }
}
