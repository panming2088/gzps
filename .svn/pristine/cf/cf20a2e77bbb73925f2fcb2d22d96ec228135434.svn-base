package com.augurit.agmobile.gzps.track.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.augurit.am.cmpt.loc.util.Utils;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * com.augurit.agmobile.gzps.track.util
 * Created by sdb on 2018/7/6  14:28.
 * Desc：
 */

public class MyLocationUtil {


    public static final String TAG = "定位模块";

    private static OnLocationChangeListener mlistener;
    private static MyLocationListener sMyLocationListener;


    private MyLocationUtil() {
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
        LogUtil.d(TAG, "1.定位是否允许" + ifEnable);
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
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private static void registerWithCheck(final Context context,
                                          final long minTime,
                                          final long minDistance,
                                          final OnLocationChangeListener listener) {

        mlistener = listener;

        if (sMyLocationListener == null) {
            sMyLocationListener = new MyLocationListener();
        }

//        BaiduLoactionManager.getInstance().startLocate(context, minTime, minDistance, sMyLocationListener);

//        if (listener == null) return;
//        if (mListeners == null) {
//            mListeners = new HashMap<>();
//        }
//        if (mLocationManager == null) {
//            mLocationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//        }
//        mListeners.put(context, listener);
//        if (!isLocationEnabled(context)) {
//            ToastUtil.shortToast(context, "无法定位，请打开定位服务");
//            return;
//        }
//        String provider = mLocationManager.getBestProvider(getCriteria(), true);
//        LogProvider(provider);
//        Location location = mLocationManager.getLastKnownLocation(provider);
//        if (location != null) {
//            listener.getLastKnownLocation(location);
////            listener.onLocationChanged(location);
//            LogUtil.d(TAG, "上次获取到的地址是：" + location.getLatitude() + " -->" + location.getLongitude());
//        }
//        if (networkListener == null)
//            networkListener = new MyLocationListener(LocationManager.NETWORK_PROVIDER);
//        if (gpsListener == null)
//            gpsListener = new MyLocationListener(LocationManager.GPS_PROVIDER);
//        if (mLocationManager != null) {
//            ifLocatedSuccess = false;
//            //startTimer(provider,minTime,minDistance);
//            //同时使用网络定位和GPS定位，网络定位快、精度不高，GPS定位慢、精度高
//            //GPS定位开始后就算在室外也可能好几分钟都无法定位成功，GPS定位成功后停用网络定位
//            if (mLocationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
//                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, networkListener);
//            }
//
//            if (mLocationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
//                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
//            }
//        }
    }


    /**
     * 注销
     */
    public static void unregister(Context context) {
        if (mlistener == null) {
            return;
        }
        mlistener = null;
        BaiduLoactionManager.getInstance().stopLocate();
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

//    private static class MyLocationListener
//            implements LocationListener {
//
//        private String provider = LocationManager.NETWORK_PROVIDER;
//
//        public MyLocationListener(@NonNull String provider) {
//            this.provider = provider;
//        }
//
//        /**
//         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
//         *
//         * @param location 坐标
//         */
//        @Override
//        public void onLocationChanged(Location location) {
//            ifLocatedSuccess = true;
//            //GPS定位成功后停用网络定位
//            if (provider.equals(LocationManager.GPS_PROVIDER)) {
//                if (mLocationManager != null
//                        && networkListener != null) {
//                    mLocationManager.removeUpdates(networkListener);
//                }
//            }
//            if (mListeners != null) {
//                for (OnLocationChangeListener onLocationChangeListener : mListeners.values()) {
//                    onLocationChangeListener.onLocationChanged(location);
//                }
//            }
//        }
//
//        /**
//         * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
//         *
//         * @param provider 提供者
//         * @param status   状态
//         * @param extras   provider可选包
//         */
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            if (mListeners != null) {
//                for (OnLocationChangeListener onLocationChangeListener : mListeners.values()) {
//                    onLocationChangeListener.onStatusChanged(provider, status, extras);
//                }
//            }
//            switch (status) {
//                case LocationProvider.AVAILABLE:
//                    LogUtil.d(TAG, "当前GPS状态为可见状态");
//                    break;
//                case LocationProvider.OUT_OF_SERVICE:
//                    LogUtil.d(TAG, "当前GPS状态为服务区外状态");
//                    break;
//                case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                    LogUtil.d(TAG, "当前GPS状态为暂停服务状态");
//                    break;
//            }
//        }
//
//        /**
//         * provider被enable时触发此函数，比如GPS被打开
//         */
//        @Override
//        public void onProviderEnabled(String provider) {
//        }
//
//        /**
//         * provider被disable时触发此函数，比如GPS被关闭
//         */
//        @Override
//        public void onProviderDisabled(String provider) {
//            LogUtil.d("定位", provider + "被禁止");
//        }
//    }
//

    public static class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
//
//            double latitude = location.getLatitude();    //获取纬度信息
//            double longitude = location.getLongitude();    //获取经度信息
//            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
//
//            String coorType = location.getCoorType();
//            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
//
//            int errorCode = location.getLocType();
//            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

            if (mlistener != null) {
                mlistener.onLocationChanged(location);
            }

        }
    }

    public interface OnLocationChangeListener {

        /**
         * 获取最后一次保留的坐标
         *
         * @param location 坐标
         */
        void getLastKnownLocation(BDLocation location);

        /**
         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         *
         * @param location 坐标
         */
        void onLocationChanged(BDLocation location);

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
