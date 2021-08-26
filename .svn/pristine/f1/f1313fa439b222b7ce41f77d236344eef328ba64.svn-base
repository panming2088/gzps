package com.augurit.agmobile.mapengine.project.dao;

import android.content.Context;

import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.agmobile.mapengine.project.model.AgcomProjectDataMapper;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.agmobile.mapengine.project.model.UserProject;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.proj.dao
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public class RemoteProjectRestDao {

    public List<ProjectInfo> getAllProjects(Context context, String userId) throws IOException {
      /*  AMNetwork amNetwork = AMNetworkProvider.getAMNetwork(context);
        amNetwork.addLogPrint();
        amNetwork.build();
        amNetwork.registerApi(RestApi.class);
        RestApi serviceApi = (RestApi) amNetwork.getServiceApi(RestApi.class);
        Call<ResponseBody> projects = serviceApi.getProjects(userId);
        Response<ResponseBody> response = null;
        response = projects.execute();
        ResponseBody body = response.body();
        List<UserProject> userProjects = JsonUtil.getObject(body.string(), new TypeToken<List<UserProject>>() {
        }.getType());
        AgcomProjectDataMapper projectDataMapper = new AgcomProjectDataMapper();
        List<ProjectInfo> projectInfos = projectDataMapper.transformToList(userProjects);
        return projectInfos;*/
        BaseInfoManager baseInfoManager = new BaseInfoManager();
        String baseServerUrl = BaseInfoManager.getRestSystemUrl(context);
        String  url = baseServerUrl+"getUserProject/"+userId;
        LogUtil.d("请求的URL是："+url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okhttp3.Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            List<UserProject> userProjects = JsonUtil.getObject(response.body().string(), new TypeToken<List<UserProject>>() {
            }.getType());
            AgcomProjectDataMapper projectDataMapper = new AgcomProjectDataMapper();
            List<ProjectInfo> projectInfos = projectDataMapper.transformToList(userProjects);
            return projectInfos;
            } else {
                throw new IOException("Unexpected code " + response);
            }
    }
}
