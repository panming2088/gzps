package com.augurit.agmobile.patrolcore.layer.dao;

import android.content.Context;


import com.augurit.agmobile.mapengine.layermanage.dao.RemoteLayerRestDao;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.layer.model.GetBaseMapResult;
import com.augurit.agmobile.patrolcore.layer.model.GetDirLayersResult;
import com.augurit.agmobile.patrolcore.layer.model.GetLayerInfoResult;
import com.augurit.agmobile.patrolcore.layer.model.LayerList;
import com.augurit.agmobile.patrolcore.layer.model.NewBaseMap;
import com.augurit.agmobile.patrolcore.layer.model.PatrolLayerInfo2;
import com.augurit.agmobile.patrolcore.layer.util.PatrolLayerUtil;
import com.augurit.agmobile.patrolcore.selectlocation.model.BaiduGeocodeResult;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.core.geometry.Envelope;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.map.dao
 * @createTime 创建时间 ：2017-03-03
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-03
 * @modifyMemo 修改备注：
 */

public class RemoteLayerDao {

    private Context mContext;
    private LayerApi mApi;
    private AMNetwork mAMNetwork;


    public static final String IS_BASE_MAP = "1";

    public RemoteLayerDao(Context context){
        this.mContext = context;
    }

    private void initAMNetwork() {
        if (mAMNetwork == null){
            String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);
            this.mAMNetwork = new AMNetwork(serverUrl);
            this.mAMNetwork.addLogPrint();
            this.mAMNetwork.addRxjavaConverterFactory();
            this.mAMNetwork.build();
            this.mAMNetwork.registerApi(LayerApi.class);
            this.mApi = (LayerApi) this.mAMNetwork.getServiceApi(LayerApi.class);
        }
    }


    public Observable<LayerList> getLayerList(String userId){
        initAMNetwork();
        return mApi.getLayerList(userId);
    }


    /**
     * 获取详细地址信息,示例：
     * http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&coordtype=wgs84ll
     * &location=23.174708,113.406429&output=json&pois=1&ak=HHeKkfwGdlLoICEg5j4LEioCGkXlO7Ma
     * &mcode=3A:F4:A4:3E:81:9F:17:A7:4C:69:F4:44:8C:A5:46:06:B7:13:C5:86;com.augurit.agmobile.agpatrol
     * @param latLng
     * @return
     * @throws IOException
     */
//    public BaiduGeocodeResult parseLocation(LatLng latLng) throws IOException {
//
//        String url = "http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&coordtype=wgs84ll&location="+
//                latLng.getLatitude()+","+
//                latLng.getLongitude()+"&output=json&pois=1&ak=HHeKkfwGdlLoICEg5j4LEioCGkXlO7Ma&" +
//                "mcode=3A:F4:A4:3E:81:9F:17:A7:4C:69:F4:44:8C:A5:46:06:B7:13:C5:86;com.augurit.agmobile.agpatrol";
//
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().
//                url(url).
//                build();
//        okhttp3.Response response = client.newCall(request).execute();
//        ResponseBody body = response.body();
//        if (body != null){
//            String result = body.string();
//            String finalResultStr = result.replace("renderReverse&&renderReverse(", "");
//            String substring = finalResultStr.substring(0, finalResultStr.length() - 1);
//            LogUtil.d("百度接口返回的数据是："+ substring);
//            return JsonUtil.getObject(substring, BaiduGeocodeResult.class);
//        }
//        return null;
//    }


    /**
     * 林峰新写的图层接口
     * @param userId
     * @return
     * @throws IOException
     */
    public Observable<List<LayerInfo>> getBaseMapFromNewInterface(final String userId) {
        initAMNetwork();

        return Observable.fromCallable(new Callable<NewBaseMap>() {
            @Override
            public NewBaseMap call() throws Exception {
                //从服务端获取图层信息
                String url = BaseInfoManager.getBaseServerUrl(mContext) +"rest/system/getBaseLayer/" + userId;
                Call<NewBaseMap> baseMapFromNewInterface = mApi.getBaseMapFromNewInterface(url);
                LogUtil.d("地图模块","获取底图信息的url是：" + baseMapFromNewInterface.request().url());
                Response<NewBaseMap> response = baseMapFromNewInterface.execute();
                NewBaseMap mapResult = response.body();
                return mapResult;
            }
        }).map(new Func1<NewBaseMap, List<LayerInfo>>() { //将BaseMap转成LayerInfo
            @Override
            public List<LayerInfo> call(NewBaseMap newBaseMap) {

                List<NewBaseMap.ResultBean> layerList = newBaseMap.getResult();
                List<LayerInfo> layerInfos = new ArrayList<LayerInfo>();
                for (NewBaseMap.ResultBean mapInfo : layerList) {
                    PatrolLayerInfo2 layerInfo = new PatrolLayerInfo2();
                    layerInfo.setLayerName(mapInfo.getName());
                    layerInfo.setBaseMap(mapInfo.getIs_base_map().equals(IS_BASE_MAP));
                    //   layerInfo.setParentId(mapInfo.getId());
                    //layerInfo.setDownloadId(mapInfo.getId());
                    layerInfo.setUrl(mapInfo.getUrl());
                    layerInfo.setDefaultVisibility(true);
                    layerInfo.setLayerId(mapInfo.getCachekey());
                    layerInfo.setType(PatrolLayerUtil.changeToLayerType(mapInfo.getLayer_type()));
                    layerInfo.setReference(mapInfo.getReference());
                    if (layerInfo.isBaseMap()) {
                        String extent = mapInfo.getExtent();
                        String[] extents = extent.split(",");
                        double[] longExtents = new double[extents.length + 1];
                        for (int j = 0; j < extents.length; j++) {
                            longExtents[j] = Double.valueOf(extents[j]);
                        }
                        Envelope envelope = new Envelope(longExtents[0], longExtents[1], longExtents[2], longExtents[3]);
                        layerInfo.setMaxExtent(envelope); //最大范围
                        String scales = mapInfo.getScales();
                        String[] split = scales.split(",");
                        layerInfo.setMinScale(Double.valueOf(split[0]));
                        layerInfo.setMaxScale(Double.valueOf(split[split.length - 1])); //最大最小缩放比例
                        if (Integer.valueOf(mapInfo.getZoom()) - 1 < split.length){
                            layerInfo.setInitScale(Double.valueOf(split[Integer.valueOf(mapInfo.getZoom()) - 1])); //初始缩放比例
                        }
                        layerInfo.setInitZoomLevel(Integer.valueOf(mapInfo.getZoom()));
                        String center = mapInfo.getCenter();
                        String[] centers = center.split(",");
                        layerInfo.setCenterX(Double.valueOf(centers[0]));
                        layerInfo.setCenterY(Double.valueOf(centers[1])); //初始中心点
                        layerInfo.setStringId(mapInfo.getId());
                    }
                    layerInfo.setLayerOrder(Integer.valueOf(mapInfo.getOrder_nm())); //设置图层顺序
                    if (layerInfo.isBaseMap()) {
                        layerInfo.setQueryable(false);
                    } else {
                        layerInfo.setQueryable(true);
                    }
                    layerInfos.add(layerInfo);
                    LogUtil.d("巡查模块", "获取到底图的Url是：" + mapInfo.getUrl());
                }
                //按照叠加顺序排序(LayerOrder越大，图层越上面)
                Collections.sort(layerInfos, new Comparator<LayerInfo>() {
                    @Override
                    public int compare(LayerInfo layerInfo, LayerInfo t1) {
                        return  layerInfo.getLayerOrder() -t1.getLayerOrder()  ;
                    }
                });
                return layerInfos;
            }
        }).map(new Func1<List<LayerInfo>, List<LayerInfo>>() {
            @Override
            public List<LayerInfo> call(List<LayerInfo> layerInfos) {
                List<LayerInfo> layerInfoList = new ArrayList<LayerInfo>();
                LayerInfo info = null;
                //进行完善图层信息
                for (LayerInfo layerInfo  : layerInfos){
                    if (layerInfo.getType() == LayerType.DynamicLayer){
                        info = new RemoteLayerRestDao().completeChildAMLayerInfo(mContext,layerInfo);
                    }else {
                        info = layerInfo;
                    }
                    layerInfoList.add(info);
                }
                return layerInfoList;
            }
        }).observeOn(Schedulers.io());
    }

}
