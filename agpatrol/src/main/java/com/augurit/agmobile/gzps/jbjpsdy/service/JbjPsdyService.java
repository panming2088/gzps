package com.augurit.agmobile.gzps.jbjpsdy.service;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.gzps.BuildConfig;
import com.augurit.agmobile.gzps.common.service.GzpsService;
import com.augurit.agmobile.gzps.common.util.RequestResultHelper;
import com.augurit.agmobile.gzps.jbjpsdy.SewerageUserEntity;
import com.augurit.agmobile.gzps.jbjpsdy.SewerageUserResultEntity;
import com.augurit.agmobile.gzps.jbjpsdy.util.SewerageUserEntityHelper;
import com.augurit.agmobile.gzps.publicaffair.dao.IFacilityAffairApi;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;

/**
 * 接驳井-排水单元  请求接口相关方法
 *
 * @PROJECT GZPS
 * @USER Augurit
 * @CREATE 2020/11/10 11:08
 */
public class JbjPsdyService {

    private Context mContext;
    private AMNetwork amNetwork;
    private JbjPsdyApi mJbjPsdyApi;
    public static final int TIMEOUT = 30;  //网络超时时间（秒）

    public JbjPsdyService(Context context) {
        mContext = context;
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);
    }

    /**
     * 获取所有排水户数据
     */
    public Observable<SewerageUserResultEntity> getAllSewerageUsers(int pageNo, int pageSize, String psdy) {
        return getSewerageUsersByType(pageNo, pageSize, psdy, null);
    }

    /**
     * 根据排水户类型获取排水户数据
     */
    public Observable<SewerageUserResultEntity> getSewerageUsersByType(int pageNo, int pageSize, String psdy, String type) {
        Observable<ResponseBody> observable;
        if (TextUtils.isEmpty(type) || "全部".equals(type)) {
            observable = mJbjPsdyApi.getAllSewerageUsers(pageNo, pageSize, psdy);
        } else {
            observable = mJbjPsdyApi.getSewerageUsersByType(pageNo, pageSize, psdy, type);
        }
        return observable.map(new Func1<ResponseBody, SewerageUserResultEntity>() {
            @Override
            public SewerageUserResultEntity call(ResponseBody responseBody) {
                SewerageUserResultEntity resultEntity = new SewerageUserResultEntity();
                try {
                    String s = responseBody.string();
                    RequestResultHelper.Result result = RequestResultHelper.handleResult(s, new String[]{"sh", "cdw", "cy", "ydyh", "total"});
                    JSONArray jsarData = result.getJsarData();
                    if (result.isSuccess()) {
                        if (jsarData != null && jsarData.length() > 0) {
                            ArrayList<SewerageUserEntity> sewerageUsers = new ArrayList<>(jsarData.length());
                            for (int i = 0; i < jsarData.length(); i++) {
                                try {
                                    JSONObject jsob = jsarData.getJSONObject(i);
                                    SewerageUserEntity entity = SewerageUserEntityHelper.parseJsonObject(jsob);
                                    if (entity != null) {
                                        sewerageUsers.add(entity);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            resultEntity.setSewerageUserEntities(sewerageUsers);
                        } else {
                            resultEntity.setSewerageUserEntities(Collections.<SewerageUserEntity>emptyList());
                        }
                        Map<String, String> extras = result.getExtras();
                        String cy = extras.get("cy");
                        String cdw = extras.get("cdw");
                        String ydyh = extras.get("ydyh");
                        String sh = extras.get("sh");
                        String total = extras.get("total");
                        resultEntity.setCateringTradeCount(TextUtils.isEmpty(cy) ? 0 : Integer.parseInt(cy));
                        resultEntity.setPrecipitateCount(TextUtils.isEmpty(cdw) ? 0 : Integer.parseInt(cdw));
                        resultEntity.setDangerCount(TextUtils.isEmpty(ydyh) ? 0 : Integer.parseInt(ydyh));
                        resultEntity.setLifeCount(TextUtils.isEmpty(sh) ? 0 : Integer.parseInt(sh));
                        resultEntity.setTotalCount(TextUtils.isEmpty(total) ? 0 : Integer.parseInt(total));
                    } else {
                        resultEntity.setSewerageUserEntities(Collections.<SewerageUserEntity>emptyList());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    resultEntity.setSewerageUserEntities(Collections.<SewerageUserEntity>emptyList());
                }
                return resultEntity;
            }
        });
    }

//    private void initAMNetwork(String serverUrl) {
//        if (amNetwork == null) {
//            this.amNetwork = new AMNetwork(serverUrl);
//            this.amNetwork.addLogPrint();
//            this.amNetwork.addRxjavaConverterFactory();
//            this.amNetwork.build();
//            this.amNetwork.registerApi(JbjPsdyApi.class);
//            this.mJbjPsdyApi = (JbjPsdyApi) this.amNetwork.getServiceApi(JbjPsdyApi.class);
//        }
//    }

    private void initAMNetwork(String serverUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        }
        OkHttpClient client = builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS).
                readTimeout(TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(TIMEOUT, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                //使用自定义的mGsonConverterFactory
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(serverUrl)
                .build();
        this.mJbjPsdyApi = retrofit.create(JbjPsdyApi.class);
    }
}
