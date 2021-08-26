package com.augurit.am.fw.net;

import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 网络请求结果回调
 * @author 创建人 ：Gunkunhu
 * @package 包名 ：com.augurit.am.fw.net
 * @createTime 创建时间 ：2017-02-15
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-15
 * @modifyMemo 修改备注：(1)2017-02-15 修改类名，原来是AMCallback
 * @version 1.0
 */
@Deprecated
public abstract class NetworkCallback<T> implements Callback<T> {
    private String TAG = "NetworkCallback";

    @Override
    //失败时回调
    public void onFailure(Call<T> call, Throwable t) {
        String error = "网络请求失败";

        //网络出现问题
        if(t instanceof SocketTimeoutException){
            error = "socket连接超时";
            Log.e(TAG,error + t.getMessage());

        }else if(t instanceof ConnectException){
            error = "网络连接异常";
            Log.e(TAG,error + t.getMessage());
        }else if(t instanceof JsonParseException
                || t instanceof JSONException
                || t instanceof ParseException){
            error = "数据解析异常";
            Log.e(TAG,error + t.getMessage());
        } else if(t instanceof RuntimeException){
            error = "网络响应异常";
            Log.e(TAG,error + t.getMessage());
        }else {
            Log.e(TAG,error + t.getMessage());
        }

        onFail(error);
    }

    @Override
    //服务端响应
    public void onResponse(Call<T> call, Response<T> response) {
        if (response != null) {
            if (response.isSuccessful()) {
                //服务器成功返回数据
                if (response.raw().code() == 200) {
                    //回调处理业务逻辑
                    onSuccess(call, response);
                } else {
                    //响应失败回调
                    onFailure(call, new RuntimeException(TAG+":"+"response error,code:" + response.raw().code() +
                            " detail = " + response.raw().toString()));
                }
            } else {
                if (response.errorBody() != null) {
                    String error = null;
                    try {
                        error = response.errorBody().string();
                    } catch (IOException e) {
                        onFailure(call, e);
                    }
                    onFailure(call, new RuntimeException(TAG+":"+"response error,code:" + response.raw().code() +
                            "response.errorBody:" + error));
                } else {
                    onFailure(call, new RuntimeException(TAG+":"+"response error,code:" + response.raw().code() +
                            " detail = " + response.raw().toString()));
                }
            }
        } else {
            onFailure(call, new RuntimeException(TAG+":"+"error response is null"));
        }

    }

    /**
     * API接口请求成功回调
     * @param response
     */
    public abstract void onSuccess(Call<T> call, Response<T> response);


    /**
     * API接口请求失败回调
     * @param errorMessage
     */
    public abstract void onFail(String errorMessage);

}
