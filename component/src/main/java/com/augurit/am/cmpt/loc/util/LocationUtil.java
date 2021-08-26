package com.augurit.am.cmpt.loc.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.location
 * @createTime 创建时间 ：2017-03-09
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-09
 * @modifyMemo 修改备注：
 */
public class  LocationUtil {

    public static final String TAG = "定位模块";

    private static Map<Object, OnLocationChangeListener> mListeners;
    private static MyLocationListener networkListener;
    private static MyLocationListener gpsListener;
    private static LocationManager mLocationManager;
    private static boolean ifLocatedSuccess = false;

    private LocationUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断Gps是否可用
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断定位是否可用
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isLocationEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean ifEnable = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        LogUtil.d(TAG,"1.定位是否允许"+ ifEnable);
        return ifEnable;
    }

    /**
     * 打开Gps设置界面
     */
    public static void openGpsSettings() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getContext().startActivity(intent);
    }

    /**
     * 注册
     * <p>使用完记得调用{@link #unregister(Context context)}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>}</p>
     * <p>如果{@code minDistance}为0，则通过{@code minTime}来定时更新；</p>
     * <p>{@code minDistance}不为0，则以{@code minDistance}为准；</p>
     * <p>两者都为0，则随时刷新。</p>
     *
     * @param minTime     位置信息更新周期（单位：毫秒）
     * @param minDistance 位置变化最小距离：当位置距离变化超过此值时，将更新位置信息（单位：米）
     * @param listener    位置刷新的回调接口
     * @return {@code true}: 初始化成功<br>{@code false}: 初始化失败
     */
//    @NeedPermission(permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION})
    public static void register(final Context context, final long minTime, final long minDistance, final OnLocationChangeListener listener) {
        /*PermissionsUtil2.getInstance()
                .requestPermissions(
                        (Activity) context,
                        "需要位置权限才能正常工作，请点击确定允许", 101,
                        new PermissionsUtil2.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                registerWithCheck(context, minTime, minDistance, listener);
                            }
                        },
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION);*/

        PermissionsUtil.getInstance()
                .requestPermissions(
                        (Activity) context,
                        new PermissionsUtil.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                registerWithCheck(context, minTime, minDistance, listener);
                            }

                            @Override
                            public void onPermissionsDenied(List<String> perms) {

                            }
                        },
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private static void registerWithCheck(final Context context,
                                          final long minTime,
                                          final long minDistance,
                                          final OnLocationChangeListener listener) {
        if (listener == null) return ;
        if(mListeners == null){
            mListeners = new HashMap<>();
        }
        if(mLocationManager == null) {
            mLocationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        mListeners.put(context, listener);
        if (!isLocationEnabled(context)) {
            ToastUtil.shortToast(context,"无法定位，请打开定位服务");
            return ;
        }
        String provider = mLocationManager.getBestProvider(getCriteria(), true);
        LogProvider(provider);
        Location location = mLocationManager.getLastKnownLocation(provider);
        if (location != null){
            listener.getLastKnownLocation(location);
//            listener.onLocationChanged(location);
            LogUtil.d(TAG,"上次获取到的地址是："+ location.getLatitude() + " -->"+ location.getLongitude());
        }
        if (networkListener == null) networkListener = new MyLocationListener(LocationManager.NETWORK_PROVIDER);
        if (gpsListener == null) gpsListener = new MyLocationListener(LocationManager.GPS_PROVIDER);
        if (mLocationManager != null){
            ifLocatedSuccess = false;
            //startTimer(provider,minTime,minDistance);
            //同时使用网络定位和GPS定位，网络定位快、精度不高，GPS定位慢、精度高
            //GPS定位开始后就算在室外也可能好几分钟都无法定位成功，GPS定位成功后停用网络定位
            if (mLocationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null){
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, networkListener);
            }

            if (mLocationManager.getProvider(LocationManager.GPS_PROVIDER) != null){
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
            }
        }
    }

    /**
     * 开启定时器，如果超过5s依然获取不到gps定位，那么说明是在室内，改用网络定位
     */
    private static void startTimer(final String provider, final long minTime, final long minDistance) {
        if (LocationManager.GPS_PROVIDER.equals(provider)){
            //开始计时
           Observable.timer(5000, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            if (!ifLocatedSuccess){ //如果此时依然没有定位成功，改用网络定位
                                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, networkListener);
                            }
                        }
                    });
        }
    }


    private static void LogProvider(String provider) {
        switch (provider){
            case LocationManager.GPS_PROVIDER:
                LogUtil.d(TAG,"2.使用GPS进行定位");
                break;
            case LocationManager.NETWORK_PROVIDER:
                LogUtil.d(TAG,"2.使用网络进行定位");
                break;
        }
    }


    /**
     * 注销
     */
    public static void unregister(Context context) {
        if(mListeners == null){
            return;
        }
        mListeners.remove(context);
        if(mListeners.size() <= 0){
            unregisterAll();
        }
    }

    /**
     * 注销全部定位监听器
     */
    public static void unregisterAll() {
        if(mListeners != null){
            mListeners.clear();
            mListeners = null;
        }
        if (mLocationManager != null) {
            if (networkListener != null) {
                mLocationManager.removeUpdates(networkListener);
                networkListener = null;
            }
            if (gpsListener != null) {
                mLocationManager.removeUpdates(gpsListener);
                gpsListener = null;
            }
            mLocationManager = null;
        }
    }

    public static boolean ifUnRegister(){
        return mLocationManager == null;
    }

    /**
     * 设置定位参数
     *
     * @return {@link Criteria}
     */
    private static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(true);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    /**
     * 根据经纬度获取地理位置
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return {@link Address}
     */
    public static Address getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(Utils.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据经纬度获取所在国家
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在国家
     */
    public static String getCountryName(double latitude, double longitude) {
        Address address = getAddress(latitude, longitude);
        return address == null ? "unknown" : address.getCountryName();
    }

    /**
     * 根据经纬度获取所在地
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在地
     */
    public static String getLocality(double latitude, double longitude) {
        Address address = getAddress(latitude, longitude);
        return address == null ? "unknown" : address.getLocality();
    }

    /**
     * 根据经纬度获取所在街道
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在街道
     */
    public static String getStreet(double latitude, double longitude) {
        Address address = getAddress(latitude, longitude);
        return address == null ? "unknown" : address.getAddressLine(0);
    }

    private static class MyLocationListener
            implements LocationListener {

        private String provider = LocationManager.NETWORK_PROVIDER;

        public MyLocationListener(@NonNull String provider){
            this.provider = provider;
        }

        /**
         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         * @param location 坐标
         */
        @Override
        public void onLocationChanged(Location location) {
            ifLocatedSuccess = true;
            //GPS定位成功后停用网络定位
            if(provider.equals(LocationManager.GPS_PROVIDER)){
                if(mLocationManager != null
                        && networkListener != null){
                    mLocationManager.removeUpdates(networkListener);
                }
            }
            if (mListeners != null) {
                for(OnLocationChangeListener onLocationChangeListener : mListeners.values()){
                    onLocationChangeListener.onLocationChanged(location);
                }
            }
        }

        /**
         * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (mListeners != null) {
                for(OnLocationChangeListener onLocationChangeListener : mListeners.values()){
                    onLocationChangeListener.onStatusChanged(provider, status, extras);
                }
            }
            switch (status) {
                case LocationProvider.AVAILABLE:
                    LogUtil.d(TAG, "当前GPS状态为可见状态");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    LogUtil.d(TAG, "当前GPS状态为服务区外状态");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    LogUtil.d(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * provider被enable时触发此函数，比如GPS被打开
         */
        @Override
        public void onProviderEnabled(String provider) {
        }

        /**
         * provider被disable时触发此函数，比如GPS被关闭
         */
        @Override
        public void onProviderDisabled(String provider) {
            LogUtil.d("定位",provider+"被禁止");
        }
    }

    public interface OnLocationChangeListener {

        /**
         * 获取最后一次保留的坐标
         *
         * @param location 坐标
         */
        void getLastKnownLocation(Location location);

        /**
         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         *
         * @param location 坐标
         */
        void onLocationChanged(Location location);

        /**
         * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        void onStatusChanged(String provider, int status, Bundle extras);//位置状态发生改变
    }
}
