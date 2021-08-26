package com.augurit.agmobile.patrolcore.selectlocation.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.augurit.agmobile.mapengine.addrsearch.dao.ArcgisAddressSearchDao;
import com.augurit.agmobile.mapengine.common.CoordinateManager;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.baiduapi.BaiduApiService;
import com.augurit.agmobile.patrolcore.layer.dao.RemoteLayerDao;
import com.augurit.agmobile.patrolcore.layer.model.GetDirLayersResult;
import com.augurit.agmobile.patrolcore.selectlocation.model.BaiduGeocodeResult;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.coordt.model.Coordinate;
import com.augurit.am.cmpt.loc.IReverseLocationTransform;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorFindParameters;
import com.esri.core.tasks.geocode.LocatorGeocodeParameters;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;
import com.esri.core.tasks.geocode.LocatorReverseGeocodeResult;
import com.esri.core.tasks.geocode.LocatorSuggestionParameters;
import com.esri.core.tasks.geocode.LocatorSuggestionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 选择位置服务，不再通过SelectLocationConstant进行设置，而是通过判断图层列表中是否有以GeocodeServer结尾的，如果有，那么就采用arcgis进行查询，否则用百度API进行查询；
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agcollection.selectlocation.service
 * @createTime 创建时间 ：2017-05-19
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-05-19
 * @modifyMemo 修改备注：
 */

public class SelectLocationService implements ISelectLocationService {

    private RemoteLayerDao mMapRemoteDao;
    private ArcgisAddressSearchDao mArcgisAddressSearchDao;
    private Context mContext;
    private Locator mLocator;
    private BaiduApiService baiduApiService;

    private static String LOCATOR_URL = "";

    public SelectLocationService(Context context, Locator locator) {
        mContext = context;
        mMapRemoteDao = new RemoteLayerDao(context);
        mArcgisAddressSearchDao = new ArcgisAddressSearchDao(locator);
    }

    /**
     * 根据坐标返回当前位置信息
     *
     * @param latLng           当前位置坐标
     * @param spatialReference 坐标系
     * @return 当前位置信息
     */
    @Override
    public Observable<DetailAddress> parseLocation(final LatLng latLng, final SpatialReference spatialReference) {

        Observable<String> locatorUrl = getLocatorUrl();

        return locatorUrl.map(new Func1<String, String>() {
            @Override
            public String call(String locatorLayer) {
                if (!TextUtils.isEmpty(locatorLayer)) { //如果发现获取到GeoServer的Url不为空
                    LogUtil.d("地名地址查询服务的url是：" + locatorLayer);
                    mArcgisAddressSearchDao.setLocator(Locator.createOnlineLocator(locatorLayer));
                    try {
                        LocatorReverseGeocodeResult locatorReverseGeocodeResult = mArcgisAddressSearchDao.searchByLocation(latLng.getLatitude(), latLng.getLongitude(), spatialReference);
                        //改变文字
                        Map<String, String> addressFields = locatorReverseGeocodeResult.getAddressFields();
                        StringBuilder address = new StringBuilder();
                        address.append(addressFields.get("SingleKey"));
                        return mContext.getResources().getString(R.string.the_current_address_is_near, address.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //如果去找了依然没有找到GeoServer，那么就用百度地址
                return null;
            }
        }).flatMap(new Func1<String, Observable<DetailAddress>>() {
            @Override
            public Observable<DetailAddress> call(final String s) {
                if (TextUtils.isEmpty(s)) { //为空，用百度API进行获取地址
                    return getLocationByBaidu(latLng);
                }
                return Observable.fromCallable(new Callable<DetailAddress>() {
                    @Override
                    public DetailAddress call() throws Exception {
                        DetailAddress detailAddress = new DetailAddress();
                        detailAddress.setDetailAddress(s);
                        return detailAddress; //否则返回用GeoServer获取到的地址
                    }
                });
            }
        }).subscribeOn(Schedulers.io());
             /*locatorUrl.map(new Func1<String, String>() {
                @Override
                public String call(String locatorLayer) {
                    if (!TextUtils.isEmpty(locatorLayer)) {
                        LogUtil.d("地名地址查询服务的url是：" + locatorLayer);
                        mArcgisAddressSearchDao.setLocator(Locator.createOnlineLocator(locatorLayer));
                        try {
                            LocatorReverseGeocodeResult locatorReverseGeocodeResult = mArcgisAddressSearchDao.searchByLocation(latLng.getLatitude(), latLng.getLongitude(),spatialReference);
                            //改变文字
                            Map<String,String> addressFields = locatorReverseGeocodeResult.getAddressFields();
                            StringBuilder address = new StringBuilder();
                            address.append(addressFields.get("SingleKey"));
                            return mContext.getResources().getString(R.string.the_current_address_is_near,address.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
            }).subscribeOn(Schedulers.io());*/
    }

    /**
     * 获取GeoServer的URL
     *
     * @return GeoServer的URL
     */
    private Observable<String> getLocatorUrl() {

        Observable<String> locatorUrl;

        /*if (TextUtils.isEmpty(LOCATOR_URL) || !LOCATOR_URL.endsWith("GeocodeServer")) {
            locatorUrl = mMapRemoteDao.getLayerList(BaseInfoManager.getUserId(mContext))
                    .map(new Func1<LayerList, String>() {
                        @Override
                        public String call(LayerList layerList) {
                            List<LayerList.PatrolLayer> rows = layerList.getRows();
                            if (!ValidateUtil.isListNull(rows)) {
                                for (LayerList.PatrolLayer layer : rows) {
                                   *//* if (layer.getName().contains("地名地址84")) {
                                        LOCATOR_URL = layer.getUrl(); //进行赋值
                                        return layer.getUrl();
                                    }*//*
                                   if (layer.getUrl().endsWith("GeocodeServer")){
                                        LOCATOR_URL = layer.getUrl(); //进行赋值
                                        return layer.getUrl();
                                    }
                                }
                            }
                            return null;
                        }
                    });
        } else {
            locatorUrl = Observable.fromCallable(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return LOCATOR_URL;
                }
            });
        } */

        locatorUrl = LayerServiceFactory.provideLayerService(mContext)
                .getSortedLayerInfos()
                .map(new Func1<List<LayerInfo>, String>() {
                    @Override
                    public String call(List<LayerInfo> layerInfos) {
                        for (LayerInfo layerInfo : layerInfos) {
                            if (layerInfo.getUrl().endsWith("GeocodeServer")) { //遍历所有图层，查找以GeoServer结尾的
                                LOCATOR_URL = layerInfo.getUrl(); //进行赋值
                                return layerInfo.getUrl();
                            }
                        }
                        return null;
                    }
                });

        return locatorUrl;
    }

    private Observable<DetailAddress> getLocationByBaidu(final LatLng latLng) {
        //如果要采用百度进行解析地址，必须要满足先转成84坐标才行
        IReverseLocationTransform iReverseLocationTransform = CoordinateManager.getInstance().getIReverseLocationTransform();
        LatLng location = null;
        if (iReverseLocationTransform == null) { //默认是84坐标系
            location = latLng;
        } else {
            Coordinate coordinate = iReverseLocationTransform.changeCurrentLocationToWGS84(latLng.getLongitude(), latLng.getLatitude());
            location = new LatLng();
            location.setLatitude(coordinate.getY());
            location.setLongitude(coordinate.getX());
        }
        return parseLocationByBaidu(location)
                .map(new Func1<BaiduGeocodeResult, DetailAddress>() {
                    @Override
                    public DetailAddress call(BaiduGeocodeResult baiduGeocodeResult) {
                        DetailAddress detailAddress = new DetailAddress();
                        if(baiduGeocodeResult == null){
                            detailAddress.setDetailAddress("");
                            return detailAddress;
                        }
                        String address = baiduGeocodeResult.getAddressWithoutCity(mContext);
                        detailAddress.setDetailAddress(address);
                        BaiduGeocodeResult.ResultBean.AddressComponentBean addressComponent = baiduGeocodeResult.getResult().getAddressComponent();
                        detailAddress.setCity(addressComponent.getCity());
                        detailAddress.setDirection(addressComponent.getDirection());
                        detailAddress.setDistrict(addressComponent.getDistrict());
                        detailAddress.setStreet(addressComponent.getStreet());
                        detailAddress.setX(latLng.getLongitude());
                        detailAddress.setY(latLng.getLatitude());
                        return detailAddress;
                    }
                }).subscribeOn(Schedulers.io());
    }


    public Observable<BaiduGeocodeResult> parseLocationByBaidu(final LatLng latLng) {
        baiduApiService = getBaiduApiService();
        return baiduApiService.parseLocation(latLng);
    }

    @NonNull
    private BaiduApiService getBaiduApiService() {
        if (baiduApiService == null) {
            baiduApiService = new BaiduApiService(mContext);
        }
        return baiduApiService;
    }


    /**
     * 这个方法无法查询到附近的地址，不过可以使用该方法进行POI搜索，详情请参考arcgis demo中的nearBy模块
     *
     * @param address
     * @param latLng
     * @param mapView
     * @param spatialReference
     * @param distance
     * @param callback2
     */
    @Deprecated
    @Override
    public void getPOIByArcgis(String address, LatLng latLng, MapView mapView, SpatialReference spatialReference, int distance,
                               final Callback2<List<LocatorGeocodeResult>> callback2) {

        //构造假数据
        // address = "广州市番禺区东环街道甘棠村甘庙前大街";
        //latLng = new LatLng(22.96163393,113.373406);

        setFakeLocatorUrl();
        LocatorFindParameters locatorFindParameters = new LocatorFindParameters(address);
        locatorFindParameters.setLocation(new Point(latLng.getLongitude(), latLng.getLatitude()), spatialReference);
        locatorFindParameters.setDistance(distance);
        //locatorFindParameters.setOutSR(spatialReference);
        // Get the current map extent.
        Envelope currExt = new Envelope();
        mapView.getExtent().queryEnvelope(currExt);
        locatorFindParameters.setSearchExtent(currExt, spatialReference);

        if (!TextUtils.isEmpty(LOCATOR_URL) && LOCATOR_URL.endsWith("GeocodeServer")) {
            mLocator = Locator.createOnlineLocator(LOCATOR_URL);
            mLocator.find(locatorFindParameters, new CallbackListener<List<LocatorGeocodeResult>>() {
                @Override
                public void onCallback(List<LocatorGeocodeResult> locatorGeocodeResults) {
                    callback2.onSuccess(locatorGeocodeResults);
                }

                @Override
                public void onError(Throwable throwable) {
                    callback2.onFail(new Exception(throwable));
                }
            });
        }
    }

    @Override
    public void getNearByLocationByArcgis(Map<String, String> addressFields, LatLng latLng, MapView mapView, SpatialReference spatialReference, int distance, Callback2<List<LocatorGeocodeResult>> callback2) {
        setFakeLocatorUrl();
        if (!TextUtils.isEmpty(LOCATOR_URL) && LOCATOR_URL.endsWith("GeocodeServer")) {
            Envelope currExt = new Envelope();
            mapView.getExtent().queryEnvelope(currExt);
            mLocator = Locator.createOnlineLocator(LOCATOR_URL);
            LocatorGeocodeParameters locatorGeocodeParameters = new LocatorGeocodeParameters();
            locatorGeocodeParameters.setSearchExtent(currExt, spatialReference);
            locatorGeocodeParameters.setOutSR(spatialReference);
            locatorGeocodeParameters.setAddressFields(addressFields);
            List<String> outfield = new ArrayList<>();
            outfield.add("*");
            locatorGeocodeParameters.setOutFields(outfield);
            mLocator.geocode(locatorGeocodeParameters, new CallbackListener<List<LocatorGeocodeResult>>() {
                @Override
                public void onCallback(List<LocatorGeocodeResult> locatorGeocodeResults) {

                }

                @Override
                public void onError(Throwable throwable) {

                }
            });
        }
    }

    /**
     * 这个方法仅用于调试
     */
    @Deprecated
    private void setFakeLocatorUrl() {
        // LOCATOR_URL = "http://192.168.19.96:6080/arcgis/rest/services/yuanqu/dii_CreateAddressLocator/GeocodeServer";
    }

    @Override
    public void getNearByThroughSuggest(String address, LatLng latLng, SpatialReference spatialReference, int distance, final Callback2<List<LocatorSuggestionResult>> callback2) {
        setFakeLocatorUrl();
        if (!TextUtils.isEmpty(LOCATOR_URL) && LOCATOR_URL.endsWith("GeocodeServer")) {
            //TODO 后面加上范围，比如在100米之内
            // GeometryEngine.buffer(new Point(latLng.getLongitude(),latLng.getLatitude()),spatialReference,100,null);
          /*  Envelope currExt = new Envelope();
            mapView.getExtent().queryEnvelope(currExt);*/
            String[] split = address.replace("附近", "").split("[1-9]");
            mLocator = Locator.createOnlineLocator(LOCATOR_URL);
            LocatorSuggestionParameters locatorSuggestionParameters = new LocatorSuggestionParameters(split[0]);
            //  locatorSuggestionParameters.setSearchExtent(currExt,spatialReference);
            locatorSuggestionParameters.setDistance(distance);
            locatorSuggestionParameters.setMaxSuggestions(50);
            locatorSuggestionParameters.setLocation(new Point(latLng.getLongitude(), latLng.getLatitude()), spatialReference);
            mLocator.suggest(locatorSuggestionParameters, new CallbackListener<List<LocatorSuggestionResult>>() {
                @Override
                public void onCallback(List<LocatorSuggestionResult> locatorSuggestionResults) {
                    callback2.onSuccess(locatorSuggestionResults);
                }

                @Override
                public void onError(Throwable throwable) {
                    callback2.onFail(new Exception(throwable));
                }
            });
        }
    }


    public String searchForLocatorLayer(List<GetDirLayersResult.ResultBean> resultBeen) {
        for (GetDirLayersResult.ResultBean resultBean : resultBeen) {
            List<GetDirLayersResult.ResultBean.ChildrenBean> children = resultBean.getChildren();
            if (!ValidateUtil.isListNull(children)) {
                for (GetDirLayersResult.ResultBean.ChildrenBean child : children) {
                    if (!ValidateUtil.isListNull(child.getLayers()) && child.getText().equals("地名地址")) {
                        //默认地名地址目录下只有一个图层，取出
                        return child.getLayers().get(0).getUrl();
                    }
                }
            }
        }
        return null;
    }
}
