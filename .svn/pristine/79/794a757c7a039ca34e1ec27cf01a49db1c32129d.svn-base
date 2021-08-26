package com.augurit.agmobile.mapengine.addrsearch.service;

import android.graphics.drawable.shapes.OvalShape;
import android.location.Location;

import com.augurit.agmobile.mapengine.addrsearch.dao.ArcgisAddressSearchDao;
import com.augurit.agmobile.mapengine.addrsearch.model.LocationResult;
import com.augurit.agmobile.mapengine.addrsearch.model.LocationSearchSuggestion;
import com.augurit.agmobile.mapengine.common.model.FeatureSet;
import com.augurit.agmobile.mapengine.common.model.SpatialBufferParam;
import com.augurit.am.cmpt.common.Callback2;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;
import com.esri.core.tasks.geocode.LocatorSuggestionResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.addrsearch.service
 * @createTime 创建时间 ：2017-03-10
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-10
 * @modifyMemo 修改备注：
 */

public class ArcgisAddressSearchService extends BaseAddressSearchService implements IAddressSearchService {


    private ArcgisAddressSearchDao mArcgisAddressSearchDao;

    private int mSelectedPosition = -1;

    private List<LocatorSuggestionResult> mCacheLocationSearchSuggestions = new ArrayList<>(); //从arcgis查询到的模糊查询结果

    private Map<Point,LocatorGeocodeResult> mCacheGeoCodeResult = new HashMap<>(); //从arcgis查询到的详细地址信息

    private MapView mapView;


    private ArcgisAddressSearchService(MapView mapView, Locator locator) {
        this.mapView = mapView;
        mArcgisAddressSearchDao = new ArcgisAddressSearchDao(locator);
    }

    @Override
    public Observable<List<LocationSearchSuggestion>> suggest(final String keyword, final Point center, final SpatialReference spatialReference) {
       return Observable.create(new Observable.OnSubscribe<List<LocationSearchSuggestion>>() {
            @Override
            public void call(final Subscriber<? super List<LocationSearchSuggestion>> subscriber) {
                mArcgisAddressSearchDao.suggest(keyword,center,spatialReference, new Callback2<List<LocatorSuggestionResult>>() {
                    @Override
                    public void onSuccess(List<LocatorSuggestionResult> suggestionResults) {
                        //将LocatorSuggestionResult转成通用的LocationSearchSuggestion
                        List<LocationSearchSuggestion> locationSearchSuggestions = new ArrayList<LocationSearchSuggestion>();
                        for (LocatorSuggestionResult locatorSuggestionResult: suggestionResults){
                            LocationSearchSuggestion searchSuggestion = new LocationSearchSuggestion(locatorSuggestionResult.getText());
                            locationSearchSuggestions.add(searchSuggestion);
                        }

                        mCacheLocationSearchSuggestions.clear();
                        mCacheLocationSearchSuggestions.addAll(suggestionResults);

                        subscriber.onNext(locationSearchSuggestions);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onFail(Exception error) {
                        error.printStackTrace();
                        subscriber.onError(error);
                    }
                });
            }
        });
    }

    @Override
    public Observable<List<LocationResult>> searchBySuggestionResult(String location) {
        return Observable.create(new Observable.OnSubscribe<List<LocationResult>>() {
            @Override
            public void call(Subscriber<? super List<LocationResult>> subscriber) {
                if (mSelectedPosition == -1){
                    subscriber.onError(new Exception("请先调用setSelectedPosition（）方法"));
                }
                LocatorSuggestionResult locatorSuggestionResult = mCacheLocationSearchSuggestions.get(mSelectedPosition);
                try {
                    List<LocatorGeocodeResult> geocodeResults = mArcgisAddressSearchDao.searchBySuggestResult(locatorSuggestionResult,mapView.getSpatialReference());

                    List<LocationResult>  locationResults = new ArrayList<LocationResult>();
                    //进行转换
                    for (LocatorGeocodeResult geocodeResult : geocodeResults){
                        LocationResult locationResult = new LocationResult();
                        locationResult.setAddress(geocodeResult.getAddress());
                        locationResult.setDetailedAddresss(geocodeResult.getAddress());
                        locationResult.setX(geocodeResult.getLocation().getX());
                        locationResult.setY(geocodeResult.getLocation().getY());
                        locationResults.add(locationResult);
                    }

                    subscriber.onNext(locationResults);
                    subscriber.onCompleted();

                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<List<LocationResult>> searchLocationByKeyWord(final String key) {
        return Observable.create(new Observable.OnSubscribe<List<LocationResult>>() {
            @Override
            public void call(Subscriber<? super List<LocationResult>> subscriber) {
                try {
                    List<LocatorGeocodeResult> geocodeResults = mArcgisAddressSearchDao.searchByKeyWord(key,mapView.getCenter(),mapView.getSpatialReference());
                    mCacheGeoCodeResult.clear();

                    //进行实体类转换以及缓存数据
                    List<LocationResult> locationResultList = new ArrayList<LocationResult>();

                    for (LocatorGeocodeResult geocodeResult : geocodeResults){
                        LocationResult locationResult = new LocationResult();
                        locationResult.setX(geocodeResult.getLocation().getX());
                        locationResult.setY(geocodeResult.getLocation().getY());
                        locationResult.setDetailedAddresss(geocodeResult.getAddress());
                        locationResultList.add(locationResult);

                        mCacheGeoCodeResult.put(geocodeResult.getLocation(),geocodeResult);
                    }

                    subscriber.onNext(locationResultList);
                    subscriber.onCompleted();

                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<FeatureSet> getDetailedAddressInfo(final LocationResult locationResult) {
        return Observable.create(new Observable.OnSubscribe<FeatureSet>() {
            @Override
            public void call(Subscriber<? super FeatureSet> subscriber) {
                //key
                Point point = new Point(locationResult.getX(),locationResult.getY());
                LocatorGeocodeResult geocodeResult = mCacheGeoCodeResult.get(point);
                if (geocodeResult != null){
                    FeatureSet featureSet = new FeatureSet();
                    //todo
                }else {
                    subscriber.onNext(new FeatureSet());
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public void setSelectedPosition(int selectedPosition) {
        mSelectedPosition = selectedPosition;
    }
}
