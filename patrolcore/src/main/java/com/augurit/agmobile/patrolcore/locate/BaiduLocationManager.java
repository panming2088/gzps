package com.augurit.agmobile.patrolcore.locate;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

import com.augurit.agmobile.mapengine.common.baidutransform.pointer.BDPointer;
import com.augurit.agmobile.mapengine.common.baidutransform.pointer.WGSPointer;
import com.augurit.agmobile.mapengine.location.ILocationManager;
import com.augurit.agmobile.patrolcore.common.model.LocationInfo;
import com.augurit.agmobile.patrolcore.common.widget.LocationButton;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.am.cmpt.loc.ILocationTransform;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;

import java.lang.ref.WeakReference;


/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.locate
 * @createTime 创建时间 ：17/12/19
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/19
 * @modifyMemo 修改备注：
 */

public class BaiduLocationManager implements ILocationManager {

    private Context mContext;
    private LocationClient mLocClient;
    private BDLocation mPreBDLocation;
    private float mPreBDRadius;//前一个定位的精确度
    private final static int MIN_DISTANCE = 2;
    private final static int MIN_RADIUS = 100;
    private LocationInfo mLastLocation;
    private LocationListener locationListener;

    private BDLocationListener mBDLocationListener;

    public BaiduLocationManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void initBaiduLocation() {
        if (mLocClient == null) {
            /**
             * LocationClientOption 该类用来设置定位SDK的定位方式。
             */
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true); //打开GPRS
            option.setAddrType("all");//返回的定位结果包含地址信息
            option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
            option.setPriority(LocationClientOption.GpsFirst); // 设置GPS优先
            option.setScanSpan(3000); //设置发起定位请求的间隔时间为3s
            option.disableCache(true);//启用缓存定位
            mLocClient = new LocationClient(mContext);
            mBDLocationListener = new BDLocationListener(this);
            mLocClient.registerLocationListener(mBDLocationListener);
            //设置定位参数
            mLocClient.setLocOption(option);
        }
    }

    @Override
    public void startLocate(LocationListener locationListener) {
        initBaiduLocation();
        this.locationListener = locationListener;
        if(mPreBDLocation != null){
            BDPointer bdPointer = new BDPointer(mPreBDLocation.getLatitude(), mPreBDLocation.getLongitude());
            WGSPointer wgsPointer = bdPointer.toWGSPointer();

            Location location = new Location("baidu");
            location.setLatitude(wgsPointer.getLatitude());
            location.setLongitude(wgsPointer.getLongtitude());

            LocationInfo locationInfo = new LocationInfo();
            locationInfo.setLocation(location);
            Point point = new Point(location.getLongitude(), location.getLatitude());
            locationInfo.setPoint(point);

            DetailAddress detailAddress = new DetailAddress();
            detailAddress.setDetailAddress(mPreBDLocation.getAddrStr());
            detailAddress.setStreet(mPreBDLocation.getAddress().street);
            locationInfo.setDetailAddress(detailAddress);

            mLastLocation = locationInfo;
            if (locationListener != null){
                locationListener.onLocationChanged(location);
            }
        }
        mLocClient.start();//调用此方法开始定位
        if(mLocClient != null && mLocClient.isStarted())
        {
            mLocClient.requestLocation();
        }
    }

    @Override
    public void stopLocate() {
        if (mLocClient != null) {
            mLocClient.stop();
            mLocClient = null;
        }
    }

    @Override
    public void setCoordinateSystem(ILocationTransform locationTransform) {

    }

    @Override
    public void hideLocationSymbol() {

    }

    @Override
    public Location changeWGS84ToCurrentCoordinate(Location location) {
        return null;
    }

    @Override
    public void setMapView(MapView mapView) {

    }

    @Override
    public MapView getMapView() {
        return null;
    }

    @Override
    public void setUseArcGisForLocation() {

    }

    public LocationInfo getLastLocation() {
        return mLastLocation;
    }


    static class BDLocationListener extends BDAbstractLocationListener {

        private WeakReference<BaiduLocationManager> baiduLocationManager;

        public BDLocationListener(BaiduLocationManager baiduLocationManager) {
            this.baiduLocationManager = new WeakReference<>(baiduLocationManager);
        }

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            boolean isGps = false;
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                isGps = true;

            }

            if(baiduLocationManager.get() == null){
                return;
            }

            if (baiduLocationManager.get().mPreBDLocation == null) {
                baiduLocationManager.get().mPreBDLocation = bdLocation;
                baiduLocationManager.get().mPreBDRadius = bdLocation.getRadius();
            } else {
                BDPointer pre = new BDPointer(baiduLocationManager.get().mPreBDLocation.getLatitude(), baiduLocationManager.get().mPreBDLocation.getLongitude());
                BDPointer cur = new BDPointer(bdLocation.getLatitude(), bdLocation.getLongitude());
                float curRadius = bdLocation.getRadius();

                if (cur.distance(pre) < MIN_DISTANCE) {
                    //  ToastUtil.shortToast(getContext(),"前后两个点太近了,跳过吧");
                    return;
                }

                //Gps环境下才过滤掉
                if (isGps && curRadius > MIN_RADIUS) {
                    // ToastUtil.shortToast(getContext(),"当前精确度大于100,跳过吧!");
                    return;
                }

                baiduLocationManager.get().mPreBDLocation = bdLocation;
                baiduLocationManager.get().mPreBDRadius = bdLocation.getRadius();
            }


            BDPointer bdPointer = new BDPointer(bdLocation.getLatitude(), bdLocation.getLongitude());
            WGSPointer wgsPointer = bdPointer.toWGSPointer();

            Location location = new Location("baidu");
            location.setLatitude(wgsPointer.getLatitude());
            location.setLongitude(wgsPointer.getLongtitude());

            LocationInfo locationInfo = new LocationInfo();
            locationInfo.setLocation(location);
            Point point = new Point(location.getLongitude(), location.getLatitude());
            locationInfo.setPoint(point);

            DetailAddress detailAddress = new DetailAddress();
            detailAddress.setDetailAddress(bdLocation.getAddrStr());
            detailAddress.setStreet(bdLocation.getAddress().street);
            locationInfo.setDetailAddress(detailAddress);

            baiduLocationManager.get().mLastLocation = locationInfo;

            if (baiduLocationManager.get().locationListener != null){
                baiduLocationManager.get().locationListener.onLocationChanged(location);
            }

        }
    }
}
