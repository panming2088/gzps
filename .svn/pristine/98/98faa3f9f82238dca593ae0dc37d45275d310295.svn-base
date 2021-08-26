package com.augurit.agmobile.mapengine.addrsearch.dao;

import android.content.Context;
import android.location.Location;

import com.augurit.agmobile.mapengine.addrsearch.model.LocationResult;
import com.augurit.agmobile.mapengine.addrsearch.model.LocationSearchSuggestion;
import com.augurit.agmobile.mapengine.panorama.service.IPanoListDataService;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.login.util.LoginConstant;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorFindParameters;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;
import com.esri.core.tasks.geocode.LocatorReverseGeocodeResult;
import com.esri.core.tasks.geocode.LocatorSuggestionParameters;
import com.esri.core.tasks.geocode.LocatorSuggestionResult;
import com.google.gson.JsonParser;

import org.codehaus.jackson.JsonFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.addrsearch.dao
 * @createTime 创建时间 ：2017-03-09
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-09
 * @modifyMemo 修改备注：
 */
public class ArcgisAddressSearchDao {

    //protected MapView mMapView;
    protected Locator mLocator;

    public static int DEFAULT_SEARCH_DISTANCE = 100;
    public static int MAX_LOCATION = 100;


    public ArcgisAddressSearchDao(Locator locator){
        //this.mMapView = mapView;
        this.mLocator = locator;
    }

    /**
     * 模糊查询
     * @param keyword
     * @param callback2
     */
    public void suggest(String keyword, Point center, SpatialReference spatialReference,final Callback2<List<LocatorSuggestionResult>> callback2){
        final LocatorSuggestionParameters suggestParams = new LocatorSuggestionParameters(keyword);
        suggestParams.setLocation(center, spatialReference);
        suggestParams.setDistance(DEFAULT_SEARCH_DISTANCE);
        mLocator.suggest(suggestParams, new CallbackListener<List<LocatorSuggestionResult>>() {
            @Override
            public void onCallback(List<LocatorSuggestionResult> suggestionResults) {
                //将LocationSuggestion转成LocationSearchSuggestion
               /* List<LocationSearchSuggestion> locationSearchSuggestions = new ArrayList<LocationSearchSuggestion>();
                for (LocatorSuggestionResult locatorSuggest : suggestionResults){
                    LocationSearchSuggestion locationSearchSuggestion = new LocationSearchSuggestion(locatorSuggest.getText());
                    locationSearchSuggestions.add(locationSearchSuggestion);
                }*/
                callback2.onSuccess(suggestionResults);
            }

            @Override
            public void onError(Throwable throwable) {
                if (callback2 != null){
                    callback2.onFail(new Exception(throwable));
                }
            }
        });
    }

    /**
     * 根据模糊查询结果进行查询
     * @param suggestResult
     * @return
     * @throws Exception
     */
    public List<LocatorGeocodeResult> searchBySuggestResult(LocatorSuggestionResult suggestResult,SpatialReference spatialReference) throws Exception {

        return mLocator.find(suggestResult,MAX_LOCATION, null, spatialReference);
    }

    /**
     * 进行地址查询，返回该地址点的坐标
     * @param queryStr         查询条件
     */
    public List<LocatorGeocodeResult> searchByKeyWord(String queryStr,Point center,SpatialReference spatialReference) throws Exception {
        final LocatorFindParameters findParams = new LocatorFindParameters(queryStr);
        findParams.setLocation(center, spatialReference);
        findParams.setDistance(DEFAULT_SEARCH_DISTANCE);
        return mLocator.find(findParams);
    }


    public static void setDefaultSearchDistance(int defaultSearchDistance) {
        DEFAULT_SEARCH_DISTANCE = defaultSearchDistance;
    }

    /**
     * 根据经纬度坐标进行查询地址
     * @param lat
     * @param lon
     * @param callback2
     */
    public void searchByLocation(double lat, double lon, SpatialReference spatialReference,final Callback2<LocatorReverseGeocodeResult> callback2){

        mLocator.reverseGeocode(new Point(lon, lat), 30, spatialReference,
                spatialReference,
                new CallbackListener<LocatorReverseGeocodeResult>() {
            @Override
            public void onCallback(LocatorReverseGeocodeResult locatorReverseGeocodeResult) {
                if (callback2!= null){
                    callback2.onSuccess(locatorReverseGeocodeResult);
                }
            }

            @Override
            public void onError(Throwable throwable) {

                if (callback2 != null){
                    callback2.onFail(new Exception(throwable));
                }
            }
        });
       /* Locator.findAddress(lat, lon, mLocator, new CallbackListener<LocatorReverseGeocodeResult>() {
            @Override
            public void onCallback(LocatorReverseGeocodeResult locatorReverseGeocodeResult) {
                if (callback2!= null){
                    callback2.onSuccess(locatorReverseGeocodeResult);
                }
            }

            @Override
            public void onError(Throwable throwable) {

                if (callback2 != null){
                    callback2.onFail(new Exception(throwable));
                }
            }
        });*/
    }
    /**
     * 根据经纬度坐标进行查询地址
     * @param lat
     * @param lon
     */
    public LocatorReverseGeocodeResult searchByLocation(double lat, double lon,SpatialReference spatialReference) throws Exception {

       return mLocator.reverseGeocode(new Point(lon, lat), 30, spatialReference,
               spatialReference);
       /* Locator.findAddress(lat, lon, mLocator, new CallbackListener<LocatorReverseGeocodeResult>() {
            @Override
            public void onCallback(LocatorReverseGeocodeResult locatorReverseGeocodeResult) {
                if (callback2!= null){
                    callback2.onSuccess(locatorReverseGeocodeResult);
                }
            }

            @Override
            public void onError(Throwable throwable) {

                if (callback2 != null){
                    callback2.onFail(new Exception(throwable));
                }
            }
        });*/
    }

    public void setLocator(Locator locator) {
        mLocator = locator;
    }
}
