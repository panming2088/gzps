package com.augurit.am.cmpt.login.dao;

import android.text.TextUtils;

import com.augurit.am.cmpt.login.model.LoginResult;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 描述：服务端登录数据访问
 * @author 创建人 ：xiejiexin
 * @version
 * @package 包名 ：com.augurit.am.cmpt.login.dao
 * @createTime 创建时间 ：2017-01-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-20
 */
public class RemoteLoginDao {

    protected int mTimeOut = 8;

    /**
     * 登录Get方式
     * @param url url
     * @return 登录成功后从服务端返回的User
     * @throws Exception 登录错误
     */
    public User login(String url) throws Exception {
        try {
            LogUtil.d("登陆模块","登陆的Url是："+url); //xcl 2017-03-14 加入打印url的Log
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(mTimeOut, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String htmlStr = response.body().string();
                if (TextUtils.isEmpty(htmlStr) || htmlStr.equals("null")) {
                    throw new Exception("用户名或密码错误");
                }
                LoginResult loginResult = JsonUtil.getObject(htmlStr, new TypeToken<LoginResult>(){}.getType()); //先用agweb的登录对象解析
                if (loginResult == null || loginResult.getResult() == null) { //解析失败的话，采用agsupport的登录对象解析
                    User user = JsonUtil.getObject(htmlStr, new TypeToken<User>(){}.getType());
                    if (user == null || user.getId() == null) {
                        throw new Exception("用户名或密码错误");
                    }else {
                        return user;
                    }
                }
                return loginResult.getResult();
            } else {
                throw new Exception("登陆失败，请检查设置");
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw new SocketTimeoutException("登录超时，请稍后重试");
        }
    }

    /**
     * 登录Post方式
     * @param url url
     * @param params 参数Map
     * @return 登录成功后从服务端返回的User
     * @throws Exception 登录错误
     */
    public User login(String url, Map<String, String> params) throws Exception {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(mTimeOut, TimeUnit.SECONDS)
                .build();
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder()
                .url(url)
                .post(formBuilder.build())
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String htmlStr = response.body().string();
            if (TextUtils.isEmpty(htmlStr) || htmlStr.equals("null")) {
                throw new Exception("用户名或密码错误");
            }
            User user = JsonUtil.getObject(htmlStr, new TypeToken<User>(){}.getType());
            if (user == null) {
                throw new Exception("用户名或密码错误");
            }
            return user;
        } else {
            throw new Exception("登陆失败，请检查设置");
        }
    }

    /**
     * 验证设备Id
     * @param url url
     * @param params 参数
     * @return 验证成功或失败
     * @throws Exception 验证出错
     */
    public boolean checkDeviceId(String url, Map<String, String> params) throws Exception {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder()
                .url(url)
                .post(formBuilder.build())
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String htmlStr = response.body().string();
            if (!TextUtils.isEmpty(htmlStr) && htmlStr.equals("true")) {
                return true;
            } else if (!TextUtils.isEmpty(htmlStr) && htmlStr.equals("false")){
                return false;
            } else {
                throw new Exception("验证设备出错，请检查设置");
            }
        } else {
            throw new Exception("验证设备出错，请检查设置");
        }
    }

    /**
     * 验证用户名是否有效
     * @param url 请求url
     * @return 用户名是否有效
     * @throws Exception 验证出错
     */
    public boolean checkUserName(String url) throws Exception {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(mTimeOut, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String htmlStr = response.body().string();
            if (TextUtils.isEmpty(htmlStr) || htmlStr.equals("null")) {
                throw new Exception("验证用户名出错");
            }
            try {
                JSONObject jo = new JSONObject(htmlStr);
                return jo.getBoolean("success");
            } catch (JSONException e) {
                e.printStackTrace();
                throw new Exception("验证用户名出错");
            }
        } else {
            throw new Exception("验证用户名出错");
        }
    }

    /**
     * 设置登录超时
     * @param timeOut 超时
     */
    public void setTimeOut(int timeOut) {
        mTimeOut = timeOut;
    }
}
