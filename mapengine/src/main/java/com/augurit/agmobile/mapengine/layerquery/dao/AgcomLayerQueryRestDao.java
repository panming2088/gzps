package com.augurit.agmobile.mapengine.layerquery.dao;

import android.content.Context;

import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.util.LayerUtils;
import com.augurit.agmobile.mapengine.layerquery.model.AgcomLayerQueryResult;
import com.augurit.agmobile.mapengine.layerquery.model.QueryableLayer;
import com.augurit.agmobile.mapengine.layerquery.model.SpatialQueryParam;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 描述：通过agcom接口进行图层查询
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.layerq.newarc.dao
 * @createTime 创建时间 ：2017-01-22
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-22
 */

public class AgcomLayerQueryRestDao {

    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    public List<AgcomLayerQueryResult> query(long projectLayerId, String searchStr, int startRow, int endRow){

        String geometry = "POLYGON (( 38392000.362496 2571384.568809," +
                " 38392000.362496 2614048.568809, 38446887.862496 2614048.568809," +
                " 38446887.862496 2571384.568809,38392000.362496 2571384.568809))";
        String where = "("+ "MC like '%"+ searchStr + "%' )";
        List<SpatialQueryParam> spatialQueryParams = getSpatialQueryParams(projectLayerId, geometry, 0,
                where, startRow,
                endRow, false, true, null);
        String json = null;
        try {
            json = getRequestParam(spatialQueryParams);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url("http://192.168.20.114:8088/dzzhcom/rest/spatial/spatialQuery")
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            Response response=okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
                String string = response.body().string();
                LogUtil.i(string);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public String getRequestParam(List<SpatialQueryParam> spatialQueryParam) throws UnsupportedEncodingException {
        String json = JsonUtil.getJson(spatialQueryParam);
        String encode = URLEncoder.encode(json, "UTF-8");
        return encode;

    }

    public List<SpatialQueryParam> getMISSpatialQueryParams(
            long projectLayerId, String queryValue, String where,
            boolean isReturnValues, int startRow, int endRow) {
        // System.out.println("ffffffffffffffffffff222");
      /*  String geometry = null;
        if (leftTopGeoPoint != null) {
            geometry = WktUtil.getWkt(leftTopGeoPoint, rightBottomGeoPoint);
            try {
                // System.out.println("FFFFFFFFFFFFFFFFF");
                geometry = URLEncoder.encode(geometry, "UTF-8");
                // System.out.println(geometry);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        List<SpatialQueryParam> spatialQueryParams = this
                .getSpatialQueryParams(projectLayerId, geometry, 0, where,
                        startRow, endRow, false, isReturnValues, null);
        if (queryValue != null) {
            spatialQueryParams.requestLocation(0).setQueryValue(queryValue);
        }
        return spatialQueryParams;*/
        return null;
    }


    public List<SpatialQueryParam> getSpatialQueryParams(long projectLayerId,
                                                         String geometry, double filtrateNum, String where,
                                                         Integer startRow, Integer endRow, boolean isReturnCount,
                                                         boolean isReturnValues, List<String> outFields) {
        List<SpatialQueryParam> spatialQueryParams = new ArrayList<SpatialQueryParam>();
        SpatialQueryParam spatialQueryParam = new SpatialQueryParam();
        spatialQueryParam.setProjectLayerId(projectLayerId);
        spatialQueryParam.setFiltrateNum(filtrateNum);
        // String geometry = "POLYGON ((" + extent[0] + " "
        // + extent[1] + ",  " + extent[2] + " " + extent[1]
        // + ", " + extent[2] + " " + extent[3] + ", "
        // + extent[0] + "  " + extent[3] + " , " + extent[0]
        // + " " + extent[1] + "))";
        List<String> geometrys = new ArrayList<String>();
        if (geometry != null) {
            geometrys.add(geometry);
        }
        spatialQueryParam.setGeometry(geometrys);
        spatialQueryParam.setReturnGeometry(true);
        spatialQueryParam.setReturnCount(isReturnCount);
        spatialQueryParam.setReturnMis(false);
        spatialQueryParam.setReturnAlias(true);
        spatialQueryParam.setReturnValues(true);
        spatialQueryParam.setWhere(where);
        spatialQueryParam.setStartRow(startRow);
        spatialQueryParam.setEndRow(endRow);
        spatialQueryParam.setOutFields(outFields);
        spatialQueryParams.add(spatialQueryParam);
        return spatialQueryParams;
    }

    public Observable<List<LayerInfo>> getQueryableLayer(final Context context,
                                                         final List<LayerInfo> totalLayers,
                                                         final String projectId,
                                                         final String userId){
        return Observable.create(new Observable.OnSubscribe<List<LayerInfo>>() {
            @Override
            public void call(Subscriber<? super List<LayerInfo>> subscriber) {
                /**
                 * （1）先读getLayerInfos这个接口（这个接口配有ArcGIs服务URL），
                 * （2）再读getQueryLayerInfo接口（这个接口没有URL），
                 * （3）然后对比两个接口，从getLayerInfos中取出两个接口都有的图层，
                 * （4）再判断URl是否支持
                 */

                //（1）先读getLayerInfos这个接口
                BaseInfoManager baseInfoManager = new BaseInfoManager();
                String baseServerUrl = BaseInfoManager.getBaseServerUrl(context);
                LogUtil.v(baseServerUrl);
                final AMNetwork amNetwork = new AMNetwork(baseServerUrl);
                amNetwork.addLogPrint();
                amNetwork.build();
                amNetwork.registerApi(LayerQueryApi.class);
                LayerQueryApi service= (LayerQueryApi) amNetwork.getServiceApi(LayerQueryApi.class);
                Call<ResponseBody> layers = service.getQueryableLayers(userId, projectId);
                try {
                    retrofit2.Response<ResponseBody> response = layers.execute();
                    if (response.isSuccessful()){
                         //如果成功
                        if (response.body() != null ){
                            String result = response.body().string();
                            if (!result.equals("[]")){ //如果返回的json字符串不是"[]"
                                List<QueryableLayer> queryableLayers =
                                        JsonUtil.getObject(result, new TypeToken<List<QueryableLayer>>() {
                                        }.getType());


                                List<LayerInfo> completeQueryableLayer = getCompleteLayerInfo(queryableLayers, totalLayers);
                                //对URL进行判断，排除掉地理编码服务
                                List<LayerInfo> arcgisServerLayer = LayerUtils.getArcgisServerLayer(completeQueryableLayer);
                                subscriber.onNext(arcgisServerLayer);
                                subscriber.onCompleted();

                            }else { //如果返回的json字符串是"[]"
                                subscriber.onNext(new ArrayList<LayerInfo>());
                                subscriber.onCompleted();
                            }
                        }else { //如果response.body() == null
                            subscriber.onNext(new ArrayList<LayerInfo>());
                            subscriber.onCompleted();
                        }
                    }else {
                        //如果网络连接失败
                        Exception exception = new Exception("获取可查询图层的网络请求失败，请检查Url是否正确或者网络是否通畅，错误代码是：" + response.code());
                        exception.printStackTrace();
                        subscriber.onError(exception);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            /**
             * 对比两个接口，从getLayerInfos中取出两个接口都有的图层，再判断URl是否支持
             * @param queryableLayers
             * @param totalLayers
             * @return
             */
            private List<LayerInfo> getCompleteLayerInfo(List<QueryableLayer> queryableLayers, List<LayerInfo> totalLayers) {
                HashMap<Integer,LayerInfo> map = new HashMap<Integer, LayerInfo>();
                //把list转成HashMap
                for (LayerInfo amLayerInfo : totalLayers){
                    map.put(amLayerInfo.getLayerId(),amLayerInfo);
                }

                //（3）然后对比两个接口，从getLayerInfos中取出两个接口都有的图层，
                //（4）再判断URl是否支持
                List<LayerInfo> resultlayerList = new ArrayList<LayerInfo>();
                //筛选出两边都有的图层
                for (QueryableLayer queryableLayer : queryableLayers){
                    LayerInfo amLayerInfo = map.get(Integer.valueOf(queryableLayer.getLayerId()));
                    if (amLayerInfo!= null && amLayerInfo.getUrl() != null){
                        resultlayerList.add(amLayerInfo);
                    }
                }
                return resultlayerList;
            }
        }).subscribeOn(Schedulers.io());
    }


}
