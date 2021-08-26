package com.augurit.agmobile.mapengine.gpsstrace.service;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.augurit.agmobile.mapengine.common.CoordinateManager;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.agmobile.mapengine.gpsstrace.util.GPSTraceConstant;
import com.augurit.agmobile.mapengine.gpsstrace.util.GpsTraceSettingUtil;
import com.augurit.am.cmpt.loc.ILocationTransform;
import com.augurit.am.cmpt.coordt.model.Coordinate;
import com.augurit.am.cmpt.loc.LocationConfiguration;
import com.augurit.am.cmpt.loc.cnst.ZoomLevel;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.gpstrace1.service
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public class LocationServiceImpl implements ILocationService {

    public static final String TAG = LocationServiceImpl.class.getName();
  //  private final LocationManager mLocationManager;
    private Location mLastKnowLocation; //上传定位坐标点
    private long mTrackLength = 0;
    private Context mContext;
    private boolean ifPause  = false ;//是否暂停定位
    private final LocationDisplayManager mLocationDisplayManager;

    public LocationServiceImpl(Context context,MapView mapView){
        this.mContext = context;
        mLocationDisplayManager = mapView.getLocationDisplayManager();
        // mLocationManager = new LocationManager(getDefaultLocationConfiguration());
    }


    @Override
    public void startLocate(Activity activity, final MapView mapView,
                            final String trackName, final long trackId, final LocationChangeListener locationListener) {
        mLocationDisplayManager.setLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (ifPause){ //如果暂停，什么都不做
                    return;
                }

                Point point = null;
                ILocationTransform iLocationTransform = CoordinateManager.getInstance().getILocationTransform(mContext);
                if (iLocationTransform != null) {
                    Coordinate coordinate3 = iLocationTransform.changeWGS84ToCurrentLocation(location);
                    //如果坐标转换失败
                    if (coordinate3 == null) {
                        LogUtil.e(TAG,"坐标转换失败，请查看ILocationTransform的实现类是否正确");
                        locationListener.onCoordinateTransformFail();
                    } else {
                        location.setLatitude(coordinate3.getY());
                        location.setLongitude(coordinate3.getX());
                        point = new Point(coordinate3.getY(), coordinate3.getX());
                        boolean isContains = GeometryEngine.contains(mapView.getMaxExtent(), point, mapView.getSpatialReference());
                        if (!isContains) {
                            //当前定位点不在地图范围内
                            locationListener.onOutOfMapBounds();
                        }
                    }
                } else {
                    //不需要进行坐标转换
                    //Log打印输出提醒
                    LogUtil.d(TAG, "Current coordinate system is WGS84,is this what you want?If not," +
                            "please call CoordinateManager.getInstance().setILocationTransform()");
                    point = new Point(location.getLongitude(), location.getLatitude());
                    boolean isContains = GeometryEngine.contains(mapView.getMaxExtent(), point, mapView.getSpatialReference());
                    if (!isContains) {
                        //当前定位点不在地图范围内
                        locationListener.onOutOfMapBounds();
                    }
                }
                Envelope zoomEnv = null;      // 用于缩放的Envelope
                GPSTrack gpsTrack = new GPSTrack();
                //填充轨迹的名称和id
                gpsTrack.setTrackId(trackId);
                gpsTrack.setTrackName(trackName);
                gpsTrack.setRecordDate(System.currentTimeMillis());
                gpsTrack.setLatitude(point.getX());
                gpsTrack.setLongitude(point.getY());

                //获取设置的时间间隔和距离间隔
                int minTime = GpsTraceSettingUtil.getLocationTimeInterval(mContext);
                int minDistance = GpsTraceSettingUtil.getLocationDistanceInterval(mContext);

                //到这里获取到定位点后，进行判断是否满足条件
                if (mLastKnowLocation != null) {
                    // 计算与当前点与上一个定位点的距离，在距离判断及长度纪录中用到
                    Point lastPoint = new Point(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude());
                    Polyline line = new Polyline();
                    line.startPath(lastPoint);
                    line.lineTo(point);
                    double distance = GeometryEngine.geodesicLength(line, mapView.getSpatialReference(),
                            new LinearUnit(LinearUnit.Code.METER));   // 目前参数设置中使用单位为米
                    zoomEnv = new Envelope();
                    line.queryEnvelope(zoomEnv);    // 顺便获取Envelope

                    if ((location.getTime() - mLastKnowLocation.getTime())
                            >= ((long) minTime * 1000) && distance >= (double) minDistance) {
                        mLastKnowLocation = location;// 大于时间间隔则纪录并且超过最小距离才上传
                    } else return;
                    mTrackLength += (distance / 1000);   // 纪录长度
                    gpsTrack.setPointState(GPSTraceConstant.POINT_STAT_MIDDLE);    // 该点为中间点
                } else {    // 第一次定位直接纪录
                    mLastKnowLocation = location;
                    mTrackLength = 0;
                    gpsTrack.setPointState(GPSTraceConstant.POINT_STAT_START); // 该点为开始点
                }
                if (!ValidateUtil.isObjectNull(locationListener)) {
                    locationListener.onLocationChanged(gpsTrack);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
        mLocationDisplayManager.start();
       /* mLocationManager.on(activity).notify(new LocationReceiver() {
            @Override
            public void onLocationChanged(Location location) {

                if (ifPause){ //如果暂停，什么都不做
                    return;
                }

                Point point = null;
                ILocationTransform iLocationTransform = CoordinateManager.getInstance().getILocationTransform(mContext);
                if (iLocationTransform != null) {
                    Coordinate coordinate3 = iLocationTransform.changeWGS84ToCurrentLocation(location);
                    //如果坐标转换失败
                    if (coordinate3 == null) {
                        LogUtil.e(TAG,"坐标转换失败，请查看ILocationTransform的实现类是否正确");
                        locationListener.onCoordinateTransformFail();
                    } else {
                        location.setLatitude(coordinate3.getY());
                        location.setLongitude(coordinate3.getX());
                        point = new Point(coordinate3.getY(), coordinate3.getX());
                        boolean isContains = GeometryEngine.contains(mapView.getMaxExtent(), point, mapView.getSpatialReference());
                        if (!isContains) {
                            //当前定位点不在地图范围内
                            locationListener.onOutOfMapBounds();
                        }
                    }
                } else {
                    //不需要进行坐标转换
                    //Log打印输出提醒
                    LogUtil.d(TAG, "Current coordinate system is WGS84,is this what you want?If not," +
                            "please call CoordinateManager.getInstance().setILocationTransform()");
                    point = new Point(location.getLatitude(), location.getLongitude());
                    boolean isContains = GeometryEngine.contains(mapView.getMaxExtent(), point, mapView.getSpatialReference());
                    if (!isContains) {
                        //当前定位点不在地图范围内
                        locationListener.onOutOfMapBounds();
                    }
                }
                Envelope zoomEnv = null;      // 用于缩放的Envelope
                GPSTrack gpsTrack = new GPSTrack();
                //填充轨迹的名称和id
                gpsTrack.setTrackId(trackId);
                gpsTrack.setTrackName(trackName);
                gpsTrack.setDate(System.currentTimeMillis());
                gpsTrack.setLatitude(point.getX());
                gpsTrack.setLongitude(point.getY());

                //获取设置的时间间隔和距离间隔
                int minTime = GpsTraceSettingUtil.getLocationTimeInterval(mContext);
                int minDistance = GpsTraceSettingUtil.getLocationDistanceInterval(mContext);

                //到这里获取到定位点后，进行判断是否满足条件
                if (mLastKnowLocation != null) {
                    // 计算与当前点与上一个定位点的距离，在距离判断及长度纪录中用到
                    Point lastPoint = new Point(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude());
                    Polyline line = new Polyline();
                    line.startPath(lastPoint);
                    line.lineTo(point);
                    double distance = GeometryEngine.geodesicLength(line, mapView.getSpatialReference(),
                            new LinearUnit(LinearUnit.Code.METER));   // 目前参数设置中使用单位为米
                    zoomEnv = new Envelope();
                    line.queryEnvelope(zoomEnv);    // 顺便获取Envelope

                    if ((location.getTime() - mLastKnowLocation.getTime())
                            >= ((long) minTime * 1000) && distance >= (double) minDistance) {
                        mLastKnowLocation = location;// 大于时间间隔则纪录并且超过最小距离才上传
                    } else return;
                    mTrackLength += (distance / 1000);   // 纪录长度
                    gpsTrack.setPointState(GPSTraceConstant.POINT_STAT_MIDDLE);    // 该点为中间点
                } else {    // 第一次定位直接纪录
                    mLastKnowLocation = location;
                    mTrackLength = 0;
                    gpsTrack.setPointState(GPSTraceConstant.POINT_STAT_START); // 该点为开始点
                }
                if (!ValidateUtil.isObjectNull(locationListener)) {
                    locationListener.onLocationChanged(gpsTrack);
                }
            }

            @Override
            public void onLocationFailed(int type) {
                //进行Log打印输出
                switch (type){
                    case 1:
                        LogUtil.w(TAG,"定位失败原因是：没有定位权限");
                        locationListener.onPermissionNotGranted();
                        break;
                    case 4:
                        LogUtil.w(TAG,"定位失败原因是：无法连接到网络");
                        locationListener.onLocationFailed();
                        break;
                    case 5:
                        LogUtil.w(TAG,"定位失败原因是：请求定位超时");
                        locationListener.onLocationFailed();
                        break;
                }
            }
        });
        mLocationManager.requestLocation();*/

    }

    //todo 对于暂停定位暂时这么解决
    @Override
    public void pauseLocate() {
        ifPause = true;
    }

    @Override
    public void continueLocate() {
        ifPause = false;
    }

    @Override
    public void stopLocate() {

      /*  if (mLocationManager != null){
            //mLocationManager.onPause();
        }*/
    }

    @Override
    public Location getLocation() {
        return mLastKnowLocation;
    }

    /**
     * 默认定位设置
     * @return
     */
    private LocationConfiguration getDefaultLocationConfiguration() {
        return new LocationConfiguration()
                .setZoomLevelAfterGetLocation(ZoomLevel.ZOOM_TO_MAX) //保持当前级别不变
                .setGPSMessage("请打开GPS提高定位精度")
                .setRationalMessage("请赋予定位权限，否则将无法进行定位!");
    }

}
