package com.augurit.am.fw.net.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by liangsh on 2016-09-14.
 */
public interface CommonApi {
    @GET
    Call<ResponseBody> get(@Url String url);

    @POST
    Call<ResponseBody> post(@Url String url);
}
