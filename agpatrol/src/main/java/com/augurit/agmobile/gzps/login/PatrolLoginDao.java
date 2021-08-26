package com.augurit.agmobile.gzps.login;

import android.text.TextUtils;

import com.augurit.am.cmpt.login.dao.RemoteLoginDao;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.login
 * @createTime 创建时间 ：2017-03-21
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-21
 * @modifyMemo 修改备注：
 */

public class PatrolLoginDao extends RemoteLoginDao {

    /**
     * 登录Get方式
     * @param url url
     * @return 登录成功后从服务端返回的User
     * @throws Exception 登录错误
     */
    @Override
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
                LoginResult loginResult = JsonUtil.getObject(htmlStr, new TypeToken<LoginResult>(){}.getType());
                if (loginResult == null || loginResult.getResult() == null) {
                    throw new Exception("用户名或密码错误");
                }
                return loginResult.getResult();
            } else {
                throw new Exception("登陆失败，请检查设置");
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw new Exception("登录超时，请稍后重试");
        }
    }
}
