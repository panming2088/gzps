package com.augurit.agmobile.mapengine.layermanage.dao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.augurit.agmobile.mapengine.layermanage.model.AgcomLayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.AgcomMapper;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.agmobile.mapengine.layermanage.util.LayerConstant;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.net.api.CommonApi;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.core.ags.LayerServiceInfo;
import com.esri.core.ags.MapServiceInfo;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 从网络获取图层数据并进行完善
 * 包名：com.augur.agmobile.ammap.layer1
 * 类描述：
 * 创建人：Augurit20160517
 * 创建时间：2016-11-25 11:13
 * 修改人：Augurit20160517
 * 修改时间：2016-11-25 11:13
 * 修改备注：
 */
public class RemoteLayerRestDao {

    private LayerApi mApi;
    private AMNetwork mAMNetwork;

    public RemoteLayerRestDao(){

       /* String serverUrl = BaseInfoManager.getBaseServerUrl(context);
        this.mAMNetwork = new AMNetwork(serverUrl);
        this.mAMNetwork.addLogPrint();
        this.mAMNetwork.addRxjavaConverterFactory();
        this.mAMNetwork.build();
        this.mAMNetwork.registerApi(LayerApi.class);
        this.mApi = (LayerApi) this.mAMNetwork.getServiceApi(LayerApi.class);*/
    }


    /**
     * 获取所有图层信息，耗时操作，请确保在子线程操作
     * @param context
     * @return
     */
    protected List<LayerInfo> getLayersFromInternet(Context context) {
        /*String currentProjectId = ProjectDataManager.getInstance().getCurrentProjectId(context);
        String baseServerUrl = BaseInfoManager.getBaseLayerServerUrl(context);
        String  url = baseServerUrl+"getLayerInfos/"+currentProjectId+"/"+ BaseInfoManager.getUserName(context);
        LogUtil.d("图层请求的URL是："+url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {//从rest/agmobilelayer 进行获取
            okhttp3.Response response = client.newCall(request).execute();
            String result = response.body().string();
            if (response.isSuccessful() && !TextUtils.isEmpty(result)&& !result.contains("<!--")) {
                AgcomLayerInfo agcomLayerInfo = JsonUtil.getObject(result, AgcomLayerInfo.class);
                AgcomMapper agcomMapper = new AgcomMapper();
                List<LayerInfo> layerInfos = agcomMapper.transform(agcomLayerInfo);
                return layerInfos;
            }else { //从rest/system进行获取
                baseServerUrl = BaseInfoManager.getBaseLayerServerUrlWithSystem(context);
                url = baseServerUrl+"getLayerInfos/"+currentProjectId+"/"+ BaseInfoManager.getUserName(context);
                request = new Request.Builder().url(url).build();
                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    AgcomLayerInfo agcomLayerInfo = JsonUtil.getObject(response.body().string(), AgcomLayerInfo.class);
                    AgcomMapper agcomMapper = new AgcomMapper();
                    List<LayerInfo> layerInfos = agcomMapper.transform(agcomLayerInfo);
                    return layerInfos;
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }*/
        AgcomLayerInfo agcomLayerInfo = getAgcomLayersFromInternet(context);
        if (agcomLayerInfo != null){
            AgcomMapper agcomMapper = getAgcomMapper();
            return agcomMapper.transform(agcomLayerInfo);
        }
        return null;
    }

    @NonNull
    public AgcomMapper getAgcomMapper() {
        return new AgcomMapper();
    }

    /*public AgcomLayerInfo getAgcomLayersFromInternet(Context context) { //xcl 2017-04-20 修改
        String currentProjectId = ProjectDataManager.getInstance().getCurrentProjectId(context);
        BaseInfoManager baseInfoManager = new BaseInfoManager();
        String baseServerUrl = baseInfoManager.getBaseLayerServerUrl(context);
        String  url = baseServerUrl+"getLayerInfos/"+currentProjectId+"/"+baseInfoManager.getUserName(context);
        LogUtil.d("图层请求的URL是："+url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            okhttp3.Response response = client.newCall(request).execute();
            String result = response.body().string();
            return JsonUtil.getObject(result, AgcomLayerInfo.class);
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
    }*/
    public AgcomLayerInfo getAgcomLayersFromInternet(Context context) {
        String currentProjectId = ProjectDataManager.getInstance().getCurrentProjectId(context);
        String baseServerUrl = BaseInfoManager.getBaseLayerServerUrl(context);
        String  url = baseServerUrl+"getLayerInfos/"+currentProjectId+"/"+ BaseInfoManager.getUserId(context);
        LogUtil.d("图层请求的URL是："+url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {//从rest/agmobilelayer 进行获取
            okhttp3.Response response = client.newCall(request).execute();
            String result = response.body().string();
            if (response.isSuccessful() && !TextUtils.isEmpty(result)&& !result.contains("<!--")) {
                AgcomLayerInfo agcomLayerInfo = JsonUtil.getObject(result, AgcomLayerInfo.class);
                return agcomLayerInfo;
            }else { //从rest/system进行获取
                baseServerUrl = BaseInfoManager.getBaseLayerServerUrlWithSystem(context);
                url = baseServerUrl+"getLayerInfos/"+currentProjectId+"/"+ BaseInfoManager.getUserId(context);
                request = new Request.Builder().url(url).build();
                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    AgcomLayerInfo agcomLayerInfo = JsonUtil.getObject(response.body().string(), AgcomLayerInfo.class);
                    return agcomLayerInfo;
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 获取所有图层数据并填充完整子图层信息，耗时操作，请确保在子线程操作
     * @param context
     * @return
     */
    public List<LayerInfo> getLayer(Context context){
        //(1)从网络获取到所有图层,此时，tileLayer在前，vectorLayer在后
        List<LayerInfo> layers = getLayersFromInternet(context);
        //(2)填充子图层信息
        List<LayerInfo> layerInfos = new ArrayList<>();
        if (ValidateUtil.isListNull(layers)){
            return layerInfos;
        }
        for (LayerInfo amLayerInfo : layers){
            LayerInfo childAMLayerInfo = completeChildAMLayerInfo(context,amLayerInfo);
            layerInfos.add(childAMLayerInfo);
        }
        return layerInfos;
    }


   /* private Observable<List<LayerInfo>> getLayersFromInternetObservable(final Context context){
        return  Observable.create(new Observable.OnSubscribe<List<LayerInfo>>() {
            @Override
            public void call(Subscriber<? super List<LayerInfo>> subscriber) {
                List<LayerInfo> layers = getLayersFromInternet(context);
                subscriber.onNext(layers);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }*/


    /***
     * 完善子Layer
     * @param amLayerInfo
     * @return
     */
    public LayerInfo completeChildAMLayerInfo(Context context,LayerInfo amLayerInfo) {
        LayerInfo completAMLayerInfo = null;
        switch (amLayerInfo.getType()){
            case DynamicLayer:
                completAMLayerInfo = completeMapServer(context,amLayerInfo);
                break;
            case TileLayer:
                completAMLayerInfo = completeMapServer(context,amLayerInfo);
                break;
            case FeatureServer:
                //判断是否是功能图层，如果是功能图层，就不需要完善信息
                if (amLayerInfo.getRemarkFunc() != LayerConstant.NORMAL_LAYER){
                    completAMLayerInfo = amLayerInfo;
                }else {
                    completAMLayerInfo = completeFeatureServer(context,amLayerInfo);
                }
                break;
            default:
                completAMLayerInfo = amLayerInfo;
                break;
        }
        return completAMLayerInfo;
    }

    /**
     * 完善FeatureServer子图层的信息
     * @param layerInfo
     * @return
     */
    private LayerInfo completeFeatureServer(Context context,LayerInfo layerInfo) {
        if (!TextUtils.isEmpty(layerInfo.getUrl())){
            String requestUrl = layerInfo.getUrl().replace("FeatureServer","MapServer").concat("?f=pjson");
            return completeAMLayerInfo(context,layerInfo, requestUrl);
        }
        return layerInfo;
    }

    private LayerInfo completeAMLayerInfo(Context context,LayerInfo layerInfo, String requestUrl) {
        OkHttpClient client =   new OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(2,TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(2,TimeUnit.SECONDS)//设置连接超时时间
                .build();
        Request request = new Request.Builder().url(requestUrl).build();
        try {
            okhttp3.Response response = client.newCall(request).execute();
            //保存到本地
            String currentProjectId = ProjectDataManager.getInstance().getCurrentProjectId(context);
            String resultString = response.body().string();
            LocalLayerStorageDao.saveMapServiceInfoToLocalFile(context,layerInfo.getLayerId(),currentProjectId, resultString);

            JsonParser jsonParser = new JsonFactory().createJsonParser(resultString);
            MapServiceInfo mapServiceInfo = MapServiceInfo.fromJson(jsonParser, requestUrl);
            LayerServiceInfo[] mapServiceLayerInfos = mapServiceInfo.getMapServiceLayerInfos();
            List<LayerInfo> infos = new ArrayList<>();
            for (LayerServiceInfo layerServiceInfo : mapServiceLayerInfos){
                LayerInfo layerInfo1 = new LayerInfo();
                layerInfo1.setLayerId(layerServiceInfo.getId());
                layerInfo1.setLayerName(layerServiceInfo.getName());
                layerInfo1.setUrl(layerInfo.getUrl());
                layerInfo1.setType(LayerType.DynamicLayer_SubLayer);
                layerInfo1.setParentLayerId(String.valueOf(layerInfo.getLayerId()));
                layerInfo1.setDefaultVisibility(layerInfo.isDefaultVisible());
                infos.add(layerInfo1);
            }
             //layerInfo.setSpatialReference(mapServiceInfo.getSpatialReference());
            // layerInfo.setMaxExtent(mapServiceInfo.getFullExtent());
             layerInfo.setMaxExtent(mapServiceInfo.getFullExtent());
            //  layerInfo.setMinScale(mapServiceInfo.getMinScale());
            //  layerInfo.setMaxScale(mapServiceInfo.getMaxScale());
            layerInfo.setChildLayer(infos);
            return layerInfo;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return layerInfo;
    }

    /**
     * 完善瓦片的子图层的信息
     * @param layerInfo
     * @return
     */
    private LayerInfo completeMapServer(Context context,LayerInfo layerInfo) {
        if (!TextUtils.isEmpty(layerInfo.getUrl())){
            String requestUrl = layerInfo.getUrl().concat("?f=pjson");
            return  completeAMLayerInfo(context,layerInfo, requestUrl);
        }
       return layerInfo;
    }


    /**
     * 从网上下载MapServiceInfo
     * @param url
     * @param onLoadDataListener
     */
    public void downloadMapServiceInfoFromInternet(String url,final Callback2<String> onLoadDataListener) {
        AMNetwork amNetwork = new AMNetwork(url + "/");
        amNetwork.setConnectTime(2);
        amNetwork.build();
        CommonApi commonApi = (CommonApi) amNetwork.getServiceApi(CommonApi.class);
        Call<ResponseBody> call = commonApi.get(url  + "?f=json");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
//                    JsonParser jsonParser = new JsonFactory().createJsonParser(json);
//                    MapServiceInfo mapServiceInfo =
//                            MapServiceInfo.fromJson(jsonParser, url);
                    onLoadDataListener.onSuccess(json);
                } catch (Exception e) {
                    e.printStackTrace();
                    onLoadDataListener.onFail(e);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onLoadDataListener.onFail(new Exception("", t));
            }
        });
    }

    /**
     * 从网上下载MapServiceInfo,耗时操作，请确保在子线程进行
     * @param url 下载图层的url
     */
    public String  downloadMapServiceInfoFromInternet(String url) throws IOException {
        AMNetwork amNetwork = new AMNetwork(url + "/");
        amNetwork.build();
        CommonApi commonApi = (CommonApi) amNetwork.getServiceApi(CommonApi.class);
        Call<ResponseBody> call = commonApi.get(url  + "?f=json");
        Response<ResponseBody> response = call.execute();
        String json = response.body().string();
        return json;
    }


}
