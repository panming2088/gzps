package com.augurit.agmobile.patrolcore.common.file.dao;

import android.content.Context;

import com.augurit.agmobile.patrolcore.common.file.model.FileResponse;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by long on 2017/10/30.
 */

public class RemoteFileRestDao {

    private AMNetwork amNetwork;
    private FileApi mFileApi;

    public RemoteFileRestDao(Context context){
        String supportUrl = BaseInfoManager.getSupportUrl(context);
//        String supportUrl = "http://139.159.247.149:8080/agsupport_swj/";
        amNetwork = new AMNetwork(supportUrl);
        amNetwork.addLogPrint();
        amNetwork.addRxjavaConverterFactory();
        amNetwork.build();
        amNetwork.registerApi(FileApi.class);
        mFileApi = (FileApi) amNetwork.getServiceApi(FileApi.class);
    }

    public String upload(String layerName, String objectid, HashMap<String, RequestBody> fileBodyMap){
        String result = null;
        Call<ResponseBody> call = mFileApi.upload(layerName, objectid, fileBodyMap);
        try {
            Response<ResponseBody> response =  call.execute();
            result = response.body().string();
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public FileResponse getList(String layerName, String objectid){
        FileResponse result = null;
        Call<FileResponse> call = mFileApi.getList(layerName, objectid);
        try {
            Response<FileResponse> response =  call.execute();
            result = response.body();
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public String delete(String layerName, String objectid, String fileName){
        String result = null;
        Call<ResponseBody> call = mFileApi.delete(layerName, objectid, fileName);
        try {
            Response<ResponseBody> response =  call.execute();
            result = response.body().string();
        } catch (Exception e) {
            return null;
        }
        return result;
    }

}
