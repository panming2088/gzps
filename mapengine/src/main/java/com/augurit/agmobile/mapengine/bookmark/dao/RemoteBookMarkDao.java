package com.augurit.agmobile.mapengine.bookmark.dao;

import android.content.Context;

import com.augurit.agmobile.mapengine.bookmark.model.BookMark;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 服务端书签数据访问
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.bookmark.dao
 * @createTime 创建时间 ：2017-01-19 10:41
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-01-19 10:41
 */
public class RemoteBookMarkDao {
    // 目前还没有书签接口

    /**
     * 从服务端获取书签列表
     * @param context Context
     * @return 书签List
     */
    public List<BookMark> getBookMarks(Context context) throws IOException {
        BaseInfoManager baseInfoManager = new BaseInfoManager();
        String baseServerUrl = BaseInfoManager.getRestSystemUrl(context);
        String url = baseServerUrl + "getBookMarks/";
        LogUtil.d("请求的URL是："+url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            List<BookMark> bookMarks = JsonUtil.getObject(response.body().string(),
                    new TypeToken<List<BookMark>>(){}.getType());
            return bookMarks;
        }
        return null;
    }

    /**
     * 上传书签
     * @param context Context
     * @param bookMark 书签
     */
    public boolean uploadBookMark(Context context, BookMark bookMark) {
        return false;
    }
}
