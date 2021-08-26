package com.augurit.agmobile.mapengine.addrsearch.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.addrsearch.dao.AgcomAddressSearchDao;
import com.augurit.agmobile.mapengine.addrsearch.model.DetailQueryCondition;
import com.augurit.agmobile.mapengine.addrsearch.model.LocationResult;
import com.augurit.agmobile.mapengine.addrsearch.model.LocationSearchSuggestion;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.agmobile.mapengine.common.model.FeatureSet;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 描述：
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.addrsearch.service
 * @createTime 创建时间 ：2017-01-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-20
 */

public class AgcomAddressSearchService extends BaseAddressSearchService implements IAddressSearchService{


    private AgcomAddressSearchDao mAgcomAddressSearchDao;
    private Context mContext;

    public AgcomAddressSearchService(Context context){
        this.mContext = context;
        mAgcomAddressSearchDao = new AgcomAddressSearchDao();
    }
    public AgcomAddressSearchService(Context context,
                                     AgcomAddressSearchDao agcomAddressSearchDao){
        this.mContext = context;
        mAgcomAddressSearchDao = agcomAddressSearchDao;
    }


    @Override
    public Observable<List<LocationSearchSuggestion>> suggest(final String keyword, Point center, SpatialReference spatialReference) {
        return Observable.create(new Observable.OnSubscribe<List<LocationSearchSuggestion>>() {
            @Override
            public void call(Subscriber<? super List<LocationSearchSuggestion>> subscriber) {
                try {
                    List<LocationSearchSuggestion> suggest = mAgcomAddressSearchDao.suggest(mContext, keyword);
                    subscriber.onNext(suggest);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<LocationResult>> searchBySuggestionResult(final String location) {
        return Observable.create(new Observable.OnSubscribe<List<LocationResult>>() {
            @Override
            public void call(Subscriber<? super List<LocationResult>> subscriber) {
                try {
                    List<LocationResult> locationResults = mAgcomAddressSearchDao.searchBySuggestResult(mContext, location);
                    subscriber.onNext(locationResults);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<LocationResult>> searchLocationByKeyWord(final String key) {


        return searchBySuggestionResult(key);

     /*   return Observable.create(new Observable.OnSubscribe<List<LocationResult>>() {
            @Override
            public void call(Subscriber<? super List<LocationResult>> subscriber) {
                List<LocationResult> locationResults = new ArrayList<LocationResult>();
                //先进行模糊查询
                try {
                    List<LocationSearchSuggestion> suggest = mAgcomAddressSearchDao.suggest(mContext, key);
                    if (ValidateUtil.isListNull(suggest)){
                        subscriber.onError(new Exception("模糊搜索结果为空"));
                    }
                    //遍历模糊查询结果，进行精确查询
                    for (LocationSearchSuggestion address : suggest){
                        List<LocationResult> resultList = mAgcomAddressSearchDao.searchBySuggestResult(mContext, address.getBody());
                        if (!ValidateUtil.isListNull(resultList)){
                            locationResults.addAll(resultList);
                        }
                    }
                    subscriber.onNext(locationResults);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }

            }
        }).subscribeOn(Schedulers.io());*/
    }

    @Override
    public Observable<FeatureSet> getDetailedAddressInfo(final LocationResult locationResult) {
        return Observable.create(new Observable.OnSubscribe<FeatureSet>() {
            @Override
            public void call(Subscriber<? super FeatureSet> subscriber) {

                String suggestUrl = getProjectLayerIdUrl(locationResult);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(suggestUrl).build();
                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    if (response.body() == null) {
                        subscriber.onError(new Exception("模糊查询失败，请检查地名地址服务"));
                    }
                    String projectLayerId = response.body().string();

                    if (projectLayerId.equals("[null]") || projectLayerId.equals("[]")) {
                        subscriber.onNext(null);
                        subscriber.onCompleted();
                    }

                    //获取到projectLayerId后就可以利用这个进行请求详情了
                    //[{"where":"OBJECTID=156698","endRow":100,"filtrateNum":0.0,"
                    // geometry":null,"isReturnCount":false,
                    // "isReturnMis":false,"projectLayerId":5926,"returnAlias":true,
                    // "returnGeometry":true,"returnValues":true,"startRow":0}]
                    DetailQueryCondition detailQueryCondition = new DetailQueryCondition();
                    detailQueryCondition.setStartRow(0);
                    detailQueryCondition.setEndRow(100);
                    detailQueryCondition.setIsReturnCount(false);
                    detailQueryCondition.setIsReturnMis(false);
                    detailQueryCondition.setProjectLayerId(Integer.valueOf(projectLayerId));
                    // detailQueryCondition.setProjectLayerId(5926);
                    detailQueryCondition.setReturnAlias(true);
                    detailQueryCondition.setReturnGeometry(true);
                    detailQueryCondition.setReturnValues(true);
                    detailQueryCondition.setWhere("OBJECTID=" + locationResult.getObjectid());

                    List<DetailQueryCondition> detailQueryConditions = new ArrayList<DetailQueryCondition>();
                    detailQueryConditions.add(detailQueryCondition);

                    String json = JsonUtil.getJson(detailQueryConditions);
                    LogUtil.d("post请求中的json是："+ json);
                    String postUrl = getPostUrl();
                    String postResult = postJson(postUrl, json);
                    if (postResult == null){
                        subscriber.onNext(null);
                        subscriber.onCompleted();
                    }else {
                        List<FeatureSet> addressNames = JsonUtil.getObject(postResult, new TypeToken<List<FeatureSet>>() {
                        }.getType());
                        LogUtil.d("post请求返回的结果是："+ postResult);
                        subscriber.onNext(addressNames.get(0));
                        subscriber.onCompleted();
                    }

                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public String getProjectLayerIdUrl(LocationResult locationResult){
        BaseInfoManager baseInfoManager = new BaseInfoManager();
        String baseServerUrl = BaseInfoManager.getBaseServerUrl(mContext);
        String  url = baseServerUrl+"rest/system/getPorjectLayerBaseIdByLayerId/"+locationResult.getLayerid()+"?d="+ System.currentTimeMillis();
        LogUtil.d("请求的URL是："+url);
        return url;
    }

    public String getPostUrl(){
        BaseInfoManager baseInfoManager = new BaseInfoManager();
        String baseServerUrl = BaseInfoManager.getBaseServerUrl(mContext);
        String  url = baseServerUrl.concat("rest/spatial/spatialQuery/");
        LogUtil.d("请求的URL是："+url);
        return url;
    }


    private String postJson(String postUrl,String json) {

        AMNetwork amNetwork = new AMNetwork(postUrl);
        amNetwork.build();
        amNetwork.registerApi(AddressQueryApi.class);
        AddressQueryApi service = (AddressQueryApi) amNetwork.getServiceApi(AddressQueryApi.class);
        String url = postUrl +"?param=" + json;
        Call<ResponseBody> call = service.getDetailedInfo(url);
        try {
            Response<ResponseBody> response = call.execute();
            if(response.isSuccessful()){
                //打印服务端返回结果
                String responseStr = response.body().string();
                return responseStr;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
