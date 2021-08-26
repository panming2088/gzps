package com.augurit.agmobile.mapengine.gpsstrace.service;

import android.app.Activity;
import android.location.Location;

import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.esri.android.map.MapView;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.gpstrace1.service
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public interface ILocationService  {

    void startLocate(Activity activity, MapView mapView, String trackName, long trackId, LocationChangeListener locationListener);

    void pauseLocate();

    void continueLocate();

    void stopLocate();


    Location getLocation();

    interface LocationChangeListener{

          void onLocationChanged(GPSTrack point);

        /**
         * 无法定位,可能的原因有定位信息较弱，请求超时等。
         */
          void onLocationFailed();

        /**
         * 坐标转换失败
         */
         void onCoordinateTransformFail();

        /**
         * 未授予定位权限，当定位失败时会回调该方法
         */
         void onPermissionNotGranted();

        /**
         * 定位结果不在当前范围内
         */
        void onOutOfMapBounds();
    }
}
