package com.augurit.agmobile.patrolcore.common.file.dao;

import com.augurit.agmobile.patrolcore.common.file.model.FileResponse;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by long on 2017/10/30.
 */

public interface FileApi {

    @Multipart
    @POST("rest/report/uploadFile")
    Call<ResponseBody> upload(@Query("layerName") String layerName,
                              @Query("objectId") String objectid,
                              @PartMap HashMap<String, RequestBody> fileBodyMap);

    @POST("rest/report/getFiles")
    Call<FileResponse> getList(@Query("layerName") String layerName,
                               @Query("objectId") String objectid);

    @POST("rest/report/deleteFile")
    Call<ResponseBody> delete(@Query("layerName") String layerName,
                              @Query("objectId") String objectid,
                              @Query("fileName") String fileName);
}
