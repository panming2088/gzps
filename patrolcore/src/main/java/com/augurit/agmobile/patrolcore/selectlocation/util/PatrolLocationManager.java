package com.augurit.agmobile.patrolcore.selectlocation.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.augurit.agmobile.bluetooth.BluetoothDataManager;
import com.augurit.agmobile.bluetooth.DataCallback;
import com.augurit.agmobile.bluetooth.model.LocationData;
import com.augurit.agmobile.mapengine.common.CoordinateManager;
import com.augurit.agmobile.mapengine.location.ILocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.model.OnBluetoothDeviceConnectEvent;
import com.augurit.agmobile.patrolcore.selectlocation.view.BluetoothDeviceListActivity;
import com.augurit.am.cmpt.coordt.model.Coordinate;
import com.augurit.am.cmpt.loc.ILocationTransform;
import com.augurit.am.cmpt.loc.LocationConfiguration;
import com.augurit.am.cmpt.loc.cnst.ZoomLevel;
import com.augurit.am.cmpt.loc.util.LocationUtil;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.augurit.agmobile.patrolcore.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 在这里自己写一个LocationManager的原因是：LocationManager放在defaultView中，为了减少app的体积，决定不依赖defaultView。
 * 之后会考虑是否要把LocationManager移动到mapengine中，现在为了保持稳定，暂时先在项目中重新写一个PatrolLcationManager。
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agcollection.common.map.view
 * @createTime 创建时间 ：2017-05-18
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-05-18
 * @modifyMemo 修改备注：
 */

public class PatrolLocationManager implements ILocationManager {

    public static final String TAG = "定位模块";

    private LocationListener mLocationListener;
    private Location mLastKnowLocation;

    private Context mContext;
    private MapView mMapView;
    private GraphicsLayer mGraphicsLayer;
    private LocationDisplayManager mLocationDisplayManager; //arcgis本身的定位管理者
    private LocationConfiguration mLocationConfiguration; //全局通用的定位配置，可以设置是GPS定位还是网络定位等
    private BluetoothDataManager manager; //蓝牙定位

    private boolean ifUseArcGISForLocation = false;
    private boolean ifZoomToBestLocation = false;
    private boolean ifFirstLocate = true; //是否是第一次定位
    private boolean ifTryConnect = true;      // 是否尝试连接到定位设备
    private boolean ifRemove = true;      // 是否尝试连接到定位设备

    private ILocationTransform mILocationTransform;

    public static  long DEFAULT_MIN_DISTANCE = 0;
    public static  long DEFAULT_MIN_TIME = 10000;

    /**
     * 构造函数
     * @param context
     * @param mapView 可为空
     */
    public PatrolLocationManager(Context context, MapView mapView){
        this.mContext = context;
        this.mMapView = mapView;
        EventBus.getDefault().register(this);
    }

//    @NeedPermission(permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION})
    @Override
    public void startLocate(final LocationListener locationListener) {
        /*PermissionsUtil2.getInstance()
                .requestPermissions((Activity) mContext,
                        "需要位置权限才能正常工作，点击确定允许", 101,
                        new PermissionsUtil2.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                startLocateWithCheck(locationListener);
                            }
                        },
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION);*/

        PermissionsUtil.getInstance()
                .requestPermissions((Activity) mContext,
                        new PermissionsUtil.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                startLocateWithCheck(locationListener);
                            }

                            @Override
                            public void onPermissionsDenied(List<String> perms) {

                            }
                        },
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private void startLocateWithCheck(LocationListener locationListener) {

        //得到定位管理者
        initLocationDisplayManager();

        if (isStarted()){
            return;
        }

        ifFirstLocate = true;

        mLocationListener = locationListener;

        addGraphicToMap();

        initLocationConfiguration();//定位条件设置

        if (ifUseArcGISForLocation){
            locateByArcGis(); //采用arcgis自带的
        }else if (SelectLocationConstant.ifLocateByBlueTooth){
            locateByBlueTooth();
        }else {
            locateByAgmobile();
        }
    }

    private void locateByBlueTooth() {
        if (manager == null){
            manager = BluetoothDataManager.getInstance(mContext);
        }

        if (manager.getConnectedDevice() == null && ifTryConnect) {   // 未连接，则跳转至设备选择界面
            ((Activity) mContext).startActivityForResult(new Intent(mContext, BluetoothDeviceListActivity.class), 0);
        } else {
            LogUtil.i("PatrolLocationManager", "请求了蓝牙定位");
            manager.requestLocation(new DataCallback<LocationData>() {
                @Override
                public void onCallback(LocationData locationData) {
                    LogUtil.i("PatrolLocationManager", "获取到蓝牙定位信息");
                    Location location = new Location("blueToothProvider");
                    location.setLatitude(locationData.getLatitude());
                    location.setLongitude(locationData.getLongitude());
                    PatrolLocationManager.this.onLocationChanged(location);
                }

                @Override
                public void onError(int i, Exception e) {
                    LogUtil.i("PatrolLocationManager", "获取定位信息失败 " + i);
                    if (i == 603) {
                        ToastUtil.shortToast(mContext, "未连接到定位设备");
                    }
                    PatrolLocationManager.this.onLocationFailed(10);
                }
            });
        }
    }

    /**
     * 定位条件设置
     */
    protected void initLocationConfiguration() {
        if (mLocationConfiguration == null){
            mLocationConfiguration= new LocationConfiguration()
                    .shouldKeepTracking(true)
                    .setMinAccuracy(0.0f)
                    .setZoomLevelAfterGetLocation(ZoomLevel.UNCHANGED)
                    .setGPSMessage("请打开GPS提高定位精度")
                    .askForEnableGPS(false) //当没有gps的时候，不进行请求gps
                    .setRationalMessage("请允许以下权限，否则无法进行定位!")
                    .shouldFilterSameLocation(false);
        }
    }


    public void setLocationConfiguration(LocationConfiguration locationConfiguration) {
        mLocationConfiguration = locationConfiguration;
    }

    private void locateByAgmobile() {

           /* locationManager = new com.augurit.am.cmpt.loc.LocationManager(mLocationConfiguration).
                    on((Activity)mContext).notify(new LocationReceiver() {
                @Override
                public void onLocationChanged(Location location) {

                    LocationManager.this.onLocationChanged(location);
                }

                @Override
                public void onLocationFailed(int type) {

                    LocationManager.this.onLocationFailed(type);
                }
            });
            //进行定位
            locationManager.requestLocation();*/
        LocationUtil.register(mContext, DEFAULT_MIN_TIME, DEFAULT_MIN_DISTANCE, new LocationUtil.OnLocationChangeListener() {
            @Override
            public void getLastKnownLocation(Location location) {

            }

            @Override
            public void onLocationChanged(Location location) {
                PatrolLocationManager.this.onLocationChanged(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        });
    }



    /**
     * 当定位失败时返回的定位失败原因
     * @param failType 定位失败类型，请参考{@link com.augurit.am.cmpt.loc.cnst.FailType}
     */
    private void onLocationFailed(int failType) {
        //进行Log打印输出
        switch (failType){
            case 1:
                LogUtil.w(TAG,"定位失败原因是：没有定位权限");
                ToastUtil.shortToast(mContext,"定位失败，没有定位权限");
                break;
            case 4:
                LogUtil.w(TAG,"定位失败原因是：无法连接到网络");
                ToastUtil.shortToast(mContext,"定位失败，无法连接到网络");
                break;
            case 5:
                LogUtil.w(TAG,"定位失败原因是：请求定位超时");
                ToastUtil.shortToast(mContext,"定位失败，请求定位超时");
                break;
            case 9:
                LogUtil.w(TAG,"定位失败原因是：没有网络定位的权限");
                ToastUtil.shortToast(mContext,"定位失败，没有网络定位的权限，请在位置信息中打开设置");
                break;
            case 10:
                LogUtil.w(TAG,"定位失败原因是：蓝牙定位失败");
//                ToastUtil.shortToast(mContext,"蓝牙定位失败");
                break;
        }
    }

    private void initLocationDisplayManager() {
        if (mLocationDisplayManager == null && mMapView != null){
            mLocationDisplayManager = mMapView.getLocationDisplayManager();
            mLocationDisplayManager.setShowLocation(true);
            mLocationDisplayManager.setShowPings(true);
            mLocationDisplayManager.setAccuracyCircleOn(true);
            mLocationDisplayManager.setLocationFilterCallback(new LocationDisplayManager.LocationFilterCallback() {
                @Override
                public boolean canApplyNewLocation(Location location, Location location1) {
                    if (Math.abs(location1.getElapsedRealtimeNanos() - location.getElapsedRealtimeNanos()) < 8000){
                        //小于5s不返回
                        return false;
                    }

                    return !(location1.getLatitude() == location.getLatitude() && location1.getLongitude() == location.getLongitude());
                }
            });
            //mLocationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
        }
    }

    private void locateByArcGis() {
        if ((mMapView != null) && (mMapView.isLoaded())){

            mLocationDisplayManager.setLocationListener(new ArcGisLocationListener());

            if (isStarted()){
                return;
            }
            //开始定位
            mLocationDisplayManager.start();
        }else {
            LogUtil.e(TAG,"请求使用arcgis进行定位失败，原因是mapview为空");
        }
    }

    private void addGraphicToMap() {
        if (mMapView != null){
            mGraphicsLayer = new GraphicsLayer();
            mMapView.addLayer(mGraphicsLayer);
        }
    }

    @Override
    public void stopLocate() {

        //清空上传的定位数据p
        mLastKnowLocation = null;
        if(ifRemove) {
            removeGraphicLayer();
        }

        stopLocationDisplayManager();

        stopBlueToothManager();

        LocationUtil.unregister(mContext);

        /*if (locationManager != null){
            locationManager.onDestroy();
        }*/

        LogUtil.d("停止定位");
    }

    private void stopBlueToothManager() {
//        if(manager != null){
//            manager.destroy();
//        }
    }

    @Override
    public void setCoordinateSystem(ILocationTransform locationTransform) {
        mILocationTransform = locationTransform;
    }

    @Override
    public void hideLocationSymbol() {
        if (mGraphicsLayer != null){
            mGraphicsLayer.removeAll();
        }
    }

    private void stopLocationDisplayManager() {
        if (mLocationDisplayManager != null && mLocationDisplayManager.isStarted()){
            mLocationDisplayManager.stop();
        }
    }

    public void setRemoveGraphicLayer(boolean isRemove){
        this.ifRemove = isRemove;
    }





    private void removeGraphicLayer() {
        try {
            if (mMapView != null){
                mGraphicsLayer.removeAll();
                mMapView.removeLayer(mGraphicsLayer);
            }
        } catch (Exception e) {

        }
    }

    private class DefaultLocationListener extends EmptyLocationListener{

        @Override
        public void onLocationChanged(Location loc) {

            PatrolLocationManager.this.onLocationChanged(loc);
        }
    }

    private class ArcGisLocationListener extends EmptyLocationListener{

        @Override
        public void onLocationChanged(Location loc) {
            //回调结果
            if (mLocationListener != null){
                mLocationListener.onLocationChanged(loc);
            }
            //PatrolLocationManager.this.onLocationChanged(loc);
        }
    }

    private void onLocationChanged(Location loc) {
        changeRealLocation(loc);
        if (LocationUtil.ifUnRegister() && !ifUseArcGISForLocation){
            return;
        }
        LogUtil.d(TAG,"当前定位到的WGS84坐标是："+loc.getLatitude()+"------》"+loc.getLongitude());

        Location location = changeWGS84ToCurrentCoordinate(loc);
        if (mMapView != null){
            //arcgis的官方文档上说Point中的x是经度，y是纬度
            Point point = new Point(location.getLongitude(),location.getLatitude());
            boolean ifSame = ifSameWithLastLocation(loc, point);
            if (ifSame){
                return; //如果相同,什么都不做
            }
            if (point != null && mMapView.getMaxExtent()!= null){
                boolean isContains = GeometryEngine.contains(mMapView.getMaxExtent(), point, mMapView.getSpatialReference());
                if(!isContains){
                    ToastUtil.shortToast(mContext, R.string.location_not_in_map);
                }
                showLocationOnMap(point,loc.getAccuracy());
            }
        }

        //回调结果
        if (mLocationListener != null){
            mLocationListener.onLocationChanged(location);
        }
    }

    private boolean ifSameWithLastLocation(Location location, Point point) {
        //对比两次坐标，如果相同则不传递给用户
        if (mLastKnowLocation == null || (mLastKnowLocation.getLatitude() != location.getLatitude() ||
                mLastKnowLocation.getLongitude() != location.getLongitude())){
            mLastKnowLocation = location;
            return false;
        }
        LogUtil.d("获取到定位，但是定位结果与上传定位结果相同，不刷新");
        return true;
    }

    public Location changeWGS84ToCurrentCoordinate(Location location) {
        ILocationTransform iLocationTransform = null;
        if (mILocationTransform != null){
            iLocationTransform = mILocationTransform;
        }else {
            iLocationTransform = CoordinateManager.getInstance().getILocationTransform(mContext);
        }

        if (iLocationTransform != null) {
            Coordinate coordinate3 = iLocationTransform.changeWGS84ToCurrentLocation(location);

            //如果坐标转换失败
            if (coordinate3 == null){
                LogUtil.e("坐标转换失败，请查看ILocationTransform的实现类是否正确");
                ToastUtil.shortToast(mContext, R.string.location_not_in_map);
            }
            location.setLatitude(coordinate3.getY());
            location.setLongitude(coordinate3.getX());
            LogUtil.d(TAG,"转换后的坐标的纬度："+location.getLatitude()+"---->经度："+location.getLongitude());
        }else {
            //Log打印输出提醒
            LogUtil.d(TAG,"Current coordinate system is WGS84,is this what you want?If not," +
                    "please call CoordinateManager.getInstance().setILocationTransform()");
        }
        return location;
    }

    @Override
    public void setMapView(MapView mapView) {
        this.mMapView = mapView;
    }

    @Override
    public MapView getMapView() {
        return mMapView;
    }

    /**
     * 在地图上显示点
     * @param point 要显示的点
     */
    private void showLocationOnMap(Point point,float accuracy) {
        mGraphicsLayer.removeAll();
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mContext,
                mContext.getResources().getDrawable(R.mipmap.patrol_location_symbol));
        Graphic graphic = new Graphic(point, pictureMarkerSymbol);
        mGraphicsLayer.addGraphic(graphic);

      /*  if (ifFirstLocate){
            ProjectRouter projectRouter = new ProjectRouter();
            String userId = BaseInfoManager.getUserId(mContext);
            try {
                ProjectInfo currentProject = projectRouter.getCurrentProjectFromLocal(mContext, userId);
                if (currentProject !=null){
                    if (mLocationConfiguration.getZoomLevel() == ZoomLevel.ZOOM_TO_MAX){

                    }else if (mLocationConfiguration.getZoomLevel() == ZoomLevel.UNCHANGED){

                    }else if (mLocationConfiguration.getZoomLevel() == ZoomLevel.ZOOM_TO_MIN){

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            ifFirstLocate = false;
        }*/
       /* if (ifZoomToBestLocation || ifFirstLocate){
            ProjectRouter projectRouter = new ProjectRouter();
            String userId = BaseInfoManager.getUserId(mContext);
            try {
                ProjectInfo currentProject = projectRouter.getCurrentProjectFromLocal(mContext, userId);
                if (currentProject != null){
                    double[] resolution = currentProject.getProjectMapParam().getScale();
                    if (resolution.length >= 2){
                        mMapView.centerAt(point,true);
                        mMapView.setResolution(resolution[resolution.length - 2]);
                    }
                    ifFirstLocate = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    @Subscribe
    public void onReceivedBluetoothDeviceConnectEvent(OnBluetoothDeviceConnectEvent event) {
        if (event.isConnected) {    // 连接上设备则采用蓝牙定位
            locateByBlueTooth();
            return;
        }
        ToastUtil.shortToast(mContext, "连接定位设备失败，采用普通定位");
        ifTryConnect = false;
        if (ifUseArcGISForLocation) {
            locateByArcGis();
        } else {    // 未连接上则采用普通定位
            locateByAgmobile();
        }
        ifTryConnect = false;   // 连接失败后不再尝试连接
    }

    /**
     * 进行改变真实获取到的位置，仅仅用于debug
     * @return
     */
    public Location changeRealLocation(Location location){
        return location;
    }

    /**
     * 注意：arcgis原生定位只支持84坐标系
     */
    @Override
    public void setUseArcGisForLocation(){
        ifUseArcGISForLocation = true;
    }

    public void setZoomToBestLocation(boolean zoomToBestLocation){
        this.ifZoomToBestLocation = zoomToBestLocation;
    }

    public static void setDefaultMinDistance(long defaultMinDistance) {
        DEFAULT_MIN_DISTANCE = defaultMinDistance;
    }

    public static void setDefaultMinTime(long defaultMinTime) {
        DEFAULT_MIN_TIME = defaultMinTime;
    }

    public boolean isStarted(){
        if (ifUseArcGISForLocation && mLocationDisplayManager != null){
           return mLocationDisplayManager.isStarted();
        }

        //蓝牙未暴露方法
        return !LocationUtil.ifUnRegister();
    }


    public void pause(){
        if (ifUseArcGISForLocation && mLocationDisplayManager != null){
            mLocationDisplayManager.pause();
        }
    }

    public void resume(){
        if (ifUseArcGISForLocation && mLocationDisplayManager != null){
            mLocationDisplayManager.resume();
        }
    }
}
