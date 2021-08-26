package com.augurit.agmobile.mapengine.addrsearch.dao;

import android.content.Context;
import android.support.annotation.NonNull;

import com.augurit.agmobile.mapengine.addrsearch.model.LocationResult;
import com.augurit.agmobile.mapengine.addrsearch.model.LocationSearchSuggestion;
import com.augurit.agmobile.mapengine.addrsearch.util.AddressSearchUtil;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.addrsearch.dao
 * @createTime 创建时间 ：2017-01-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-20
 */

public class AgcomAddressSearchDao {

    public AgcomAddressSearchDao(){

    }

    public List<LocationSearchSuggestion> suggest(Context context,String keyword) throws IOException {
        String url = getSuggestUrl(context,keyword);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okhttp3.Response response = client.newCall(request).execute();
        List<LocationSearchSuggestion> locationSearchSuggestions =new ArrayList<LocationSearchSuggestion>();
        if (response.isSuccessful()){
            String json = response.body().string();
            boolean ifResultParseable = ifResultParseable(json);
            if (!ifResultParseable){
                return null;
            }
            List<String> addressNames = JsonUtil.getObject(json, new TypeToken<List<String>>() {
            }.getType());

            for (String string : addressNames){
                //过滤掉特殊字符
                String filterSpecialCharacter = AddressSearchUtil.filterSpecialCharacter(string);
                LocationSearchSuggestion locationSearchSuggestion = new LocationSearchSuggestion(filterSpecialCharacter);
                locationSearchSuggestions.add(locationSearchSuggestion);
            }
        }else if (response.code() == 404){
            LogUtil.d("模糊查询失败，失败的原因是：url无法访问");
        }else {
            LogUtil.d("模糊查询失败，失败的url是："+url+",返回码是："+response.code());
        }
        return locationSearchSuggestions;
    }

    /**
     * 返回的结果是否是合理的（可以进行json解析的）
     * @param json
     * @return
     */
    private boolean ifResultParseable(String json) {
        return !(json.equals("[null]") || json.contains("<!-- 风格样式") || json.equals("[]"));
    }

    public List<LocationResult> searchBySuggestResult(Context context,String suggestResult) throws IOException {
        String url = getAccurateSearchUrl(context,suggestResult);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okhttp3.Response response = client.newCall(request).execute();
        if (response.isSuccessful()){
            String json = response.body().string();
            if (!ifResultParseable(json)){
                return null;
            }
            List<LocationResult> locationResults = JsonUtil.getObject(json, new TypeToken<List<LocationResult>>() {
            }.getType());
            return locationResults;
        }else if (response.code() == 404){
            LogUtil.d("精确查询失败，失败的原因是：url无法访问");
        }else {
            LogUtil.d("精确查询失败，失败的url是："+url+",返回码是："+response.code());
        }
        return null;
    }


    /**
     * 得到模糊查询url
     * @param context
     * @param keyword
     * @return
     */
        @NonNull
        private String getSuggestUrl(Context context,String keyword) {
            BaseInfoManager baseInfoManager = new BaseInfoManager();
            String baseServerUrl = BaseInfoManager.getRestSupportUrl(context);
            String  url = baseServerUrl+"search/searchKeyWord/0/"+keyword+"/"+ System.currentTimeMillis();
            LogUtil.d("请求的URL是："+url);
            return url;
        }


    /**
     * 精确查询url
     * @param location
     * @return
     */
    @NonNull
    private String getAccurateSearchUrl(Context context,String location) {
        BaseInfoManager baseInfoManager = new BaseInfoManager();
        String baseServerUrl = BaseInfoManager.getRestSupportUrl(context);
        String  url = baseServerUrl+"search/searchAddressByAll/0/"+location+"/null"+"/"+ System.currentTimeMillis();
        LogUtil.d("请求的URL是："+url);
        return url;
    }
}
