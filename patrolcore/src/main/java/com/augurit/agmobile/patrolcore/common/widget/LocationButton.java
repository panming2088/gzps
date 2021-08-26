package com.augurit.agmobile.patrolcore.common.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.common.baidutransform.pointer.BDPointer;
import com.augurit.agmobile.mapengine.common.baidutransform.pointer.WGSPointer;
import com.augurit.agmobile.mapengine.common.widget.rotateview.RotateManager;
import com.augurit.agmobile.mapengine.common.widget.scaleview.MapScaleView;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.model.LocationInfo;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.geocode.Locator;

import org.codehaus.jackson.JsonParser;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Time;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 定位按钮，默认采用百度定位
 * Created by xcl on 2017/10/27.
 */

public class LocationButton extends LinearLayout implements SensorEventListener{
    //定位Graphic的id
    private int locationGraphicId = -1;
    private MapView mapView;
    private boolean ifUseArcGis = false;
    private boolean ifUseBaidu = true;
    private PatrolLocationManager mLocationManager;
    /**
     * 是否是第一次定位
     */
    private boolean isFirstLocate = true;
    private boolean isOnceLocation = true;

    /**
     * 定位完成后是否显示"当前位置"的callout
     */
    private boolean ifShowCallout = false;
    private GraphicsLayer mGLayerFroDrawLocation;

    /**
     * 是否处于跟随模式
     */
    private boolean ifInFollowMode = false;

    private boolean rotateEnable = false;  //是否允许地图旋转

    private OnStateChangedListener mOnStateChangedListener;

    /**
     * 获取返回最后定位的位置
     */
    private LocationInfo mLastLocation;
    private ImageView iv_location;
    private Matrix matrix = new Matrix( );
    private boolean ismapview;

    /**
     * 是否每次定位成功后都将地位居中，默认是第一次居中；
     */
    private boolean ifAlwaysCeterToLocation = false;

    /**
     * 是否绘制图标，默认绘制（一般不需要设置）
     */
    private boolean ifDrawLocation = true;
    /**
     * 百度定位
     */
    private LocationClient mLocClient;
    private BDLocationListener mBDLocationListener;
    private Timer timer;
    private BDLocation mPreBDLocation;
    private float mPreBDRadius;//前一个定位的精确度
    private final static int MIN_DISTANCE = 2;
    private final static int MIN_RADIUS = 100;
    private MapScaleView mapScaleView;

    private RotateManager mapRotateManager;
    private float ratateAngle;
    private Graphic graphic;
    private SensorManager mSM;
    Sensor accelerometerSensor ;
    Sensor magneticSensor ;
    float[] accelerometerValues = new float[3];
    float[] magneticValues = new float[3];
    float lastRotateDegree;
    //是否允许定位箭头旋转
    private boolean isAllowRotate;
    public boolean isAllowRotate() {
        return isAllowRotate;
    }

    public void setAllowRotate(boolean allowRotate) {
        isAllowRotate = allowRotate;
    }


    public LocationButton(Context context) {
        super(context);
        mapRotateManager = RotateManager.getInstance(context.getApplicationContext());
        mapRotateManager.addOnRotateChangedListener(this, new RotateManager.OnRotateChangedListener() {
            @Override
            public void onRotateChanged(float direction) {
                if (mapView != null
                        && mLastLocation != null
                        && inState == State.rotate) {
                    mapView.setRotationAngle(direction, new Point(mLastLocation.getPoint().getX(), mLastLocation.getPoint().getY()), true);
                }
            }
        });
        registerSensor(context);
    }

    public LocationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.widget_location_button, this, true);
        iv_location = (ImageView) findViewById(R.id.iv_location);
        findViewById(R.id.ll_location).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                followLocation();
            }
        });
        mapRotateManager = RotateManager.getInstance(context.getApplicationContext());
        mapRotateManager.addOnRotateChangedListener(this, new RotateManager.OnRotateChangedListener() {
            @Override
            public void onRotateChanged(float direction) {
                if (mapView != null
                        && mLastLocation != null
                        && inState == State.rotate) {
                    mapView.setRotationAngle(direction, new Point(mLastLocation.getPoint().getX(), mLastLocation.getPoint().getY()), true);
                }
            }
        });
//        TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.LocationButton);
//        isAllowRotate = tArray.getBoolean(R.styleable.LocationButton_isAllowRotate);
//        tArray.recycle();


        registerSensor(context);
}
    public void registerSensor(Context context){
        mSM = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = mSM.getDefaultSensor(Sensor.
                TYPE_ACCELEROMETER);
        Sensor magneticSensor = mSM.getDefaultSensor(Sensor.
                TYPE_MAGNETIC_FIELD);
        mSM.registerListener(this, accelerometerSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        mSM.registerListener(this, magneticSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }
    public void unregisterSensor(){
        if (mSM != null && this!= null) {
            mSM.unregisterListener(this);
            mSM =  null;
        }
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public void setScaleView(MapScaleView mapScaleView) {
        this.mapScaleView = mapScaleView;
    }

    public void setOnceLocation(boolean onceLocation) {
        isOnceLocation = onceLocation;
    }

    public void setIfAlwaysCeterToLocation(boolean ifAlwaysCeterToLocation) {
        this.ifAlwaysCeterToLocation = ifAlwaysCeterToLocation;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 判断当前是加速度传感器还是地磁传感器
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // 通过clone()获取不同的values引用
            accelerometerValues = event.values.clone();
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticValues = event.values.clone();
        }
        //获取地磁与加速度传感器组合的旋转矩阵
        float[] R = new float[9];
        float[] values = new float[3];
        SensorManager.getRotationMatrix(R, null, accelerometerValues,
                magneticValues);
        SensorManager.getOrientation(R, values);

        //values[0]->X轴、values[1]->y轴、values[2]->z轴
        //使用前请进行转换，因为获取到的值是弧度，示例如下
            double x =  Math.toDegrees(values[0]);
//            double y =  Math.toDegrees(values[1]);
//            double z =  Math.toDegrees(values[2]);
        handleEvent(values);
    }

    public void handleEvent(float[] values) {
        //
        float rotate = -(float) Math.toDegrees(values[0]);
        ratateAngle = rotate;
        if (Math.abs(ratateAngle - lastRotateDegree) > 5) {
            if (mLocClient != null) {
                if (timer == null) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            startLocateByBaidu(false);
                        }
                    }, 50, 350);
                }
            }
        }
            lastRotateDegree = rotate;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public enum State {
        follow(R.mipmap.common_ic_location_follow2),   //跟随
        normal(R.mipmap.common_ic_target),    //不跟随
        rotate(R.mipmap.fd_blue_point),;   //地图旋转

        State(int resid) {
            this.resid = resid;
        }

        public int getResid() {
            return resid;
        }

        private int resid;
    }

    State inState = State.normal;

    public void setState() {
        switch (inState) {
            case follow:
                inState = State.rotate;
                break;
            case normal:
                // 授权状态
                inState = State.follow;
                checkLocationPermission();
                mapView.setScale(PatrolLayerPresenter.initScale);
                if (mapScaleView != null) {
                    mapScaleView.setScale(PatrolLayerPresenter.initScale);
                }
                break;
            case rotate:
                inState = State.normal;
                isFirstLocate = true;
                stopOrPauseLocate();
                break;
        }

        iv_location.setImageResource(inState.getResid());

        if (mOnStateChangedListener != null) {
            mOnStateChangedListener.onStateChanged(inState);
        }
    }

    public void setStateWithoutRotate() {
        switch (inState) {
            case follow:
                inState = State.normal;
                isFirstLocate = true;
                stopOrPauseLocate();
                break;
            case normal:
                // 授权状态
                inState = State.follow;
                checkLocationPermission();
                mapView.setScale(PatrolLayerPresenter.initScale);
                if (mapScaleView != null) {
                    mapScaleView.setScale(PatrolLayerPresenter.initScale);
                }
                break;
            default:
                break;
        }

        iv_location.setImageResource(inState.getResid());

        if (mOnStateChangedListener != null) {
            mOnStateChangedListener.onStateChanged(inState);
        }
    }

    /**
     * 定位跟随模式切换
     */
    public void followLocation() {
        //初始化  被执行
        LogUtil.d("followLocation=======================");
        if (rotateEnable) {
            setState();
        } else {
            setStateWithoutRotate();
        }

        return;
        //---------
    /*    if (ifInFollowMode) {
            stopOrPauseLocate();
            ifInFollowMode = false;
            iv_location.setImageResource(R.mipmap.common_ic_target);
        } else if (!ifInFollowMode) {
            ifInFollowMode = true;
            isFirstLocate = true;
            checkLocationPermission();
            iv_location.setImageResource(R.mipmap.common_ic_location_follow);
        }*/
    }

    public void setRotateEnable(boolean rotateEnable) {
        this.rotateEnable = rotateEnable;
    }

    public void setStateNormal() {
        inState = State.normal;
        isFirstLocate = true;
        stopOrPauseLocate();

        iv_location.setImageResource(inState.getResid());
        if (mOnStateChangedListener != null) {
            mOnStateChangedListener.onStateChanged(inState);
        }
    }

    /**
     * 检查是否有定位权限
     */
    public void checkLocationPermission() {
        PermissionsUtil.getInstance()
                .requestPermissions(
                        (Activity) getContext(),
                        true,
                        new PermissionsUtil.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                startOrResumeLocate();
                            }

                            @Override
                            public void onPermissionsDenied(List<String> perms) {
                                stopOrPauseLocate();

                                //---------------point
//                                setState();
//                                ifInFollowMode = false;
                                inState = State.normal;
                                iv_location.setImageResource(R.mipmap.common_ic_target);
                                ToastUtil.shortToast(getContext(), "未授予定位权限，无法获取当前位置！");
                            }
                        },
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
    }


    /**
     * 暂停定位
     */
    public void stopOrPauseLocate() {
        if (ifUseArcGis && mLocationManager != null) {
            mLocationManager.pause();
        } else if (ifUseBaidu && mLocClient != null) {
            mLocClient.stop();
        } else {
            stopLocate(false);
        }
        //  ifInFollowMode = false;
        inState = State.normal;
        iv_location.setImageResource(R.mipmap.common_ic_target);
    }


    public void startOrResumeLocate() {
        if (ifUseArcGis && mLocationManager != null) {
            mLocationManager.resume();
        } else if (ifUseBaidu) {
            initBaiduLocation();
            startLocateByBaidu(true);
        } else {
            startLocate();
        }
        //   ifInFollowMode = true;
        inState = State.follow;
        isFirstLocate = true;
        iv_location.setImageResource(R.mipmap.common_ic_location_follow);
    }

    /**
     * 是否处于正在定位
     *
     * @return
     */
    public boolean ifLocating() {
        return inState != State.normal;
    }

    /**
     * 注意：此方法只用于当地图坐标系是84的时候
     */
    public void setUseArcgisLocation() {
        ifUseArcGis = true;
    }

    public void setIfShowCallout(boolean ifShowCallout) {
        this.ifShowCallout = ifShowCallout;
    }

    /**
     * 开启定位
     */
    public void startLocate() {
        if (mLocationManager == null) {
            if (ifUseArcGis && mapView != null) {
                mLocationManager = new PatrolLocationManager(getContext(), mapView);
                mLocationManager.setUseArcGisForLocation();
            } else {
                mLocationManager = new PatrolLocationManager(getContext(), null);
            }
            PatrolLocationManager.setDefaultMinDistance(0);
            PatrolLocationManager.setDefaultMinTime(10000);
        }
        if (mLocationManager != null) {
            mLocationManager.startLocate(new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {

//                    if (mGLayerFroDrawLocation != null){
//                        mGLayerFroDrawLocation.removeAll();
//                    }

                    /**
                     * 因为使用arcgis会自动将点画到地图上，所以不需要自己绘制
                     */
                    if (!ifUseArcGis && mapView != null && ifDrawLocation) {
                        drawLocationOnMap(location);
                    }

                    final Point point = new Point(location.getLongitude(), location.getLatitude());
                    if (mapView == null || mapView.getMaxExtent() == null || mapView.getSpatialReference() == null) {
                        LocationInfo locationInfo = new LocationInfo();
                        locationInfo.setLocation(location);
                        locationInfo.setPoint(point);
                        mLastLocation = locationInfo;
                        return;
                    }

                    if (GeometryEngine.contains(mapView.getMaxExtent(), point, mapView.getSpatialReference())) {
                        if (ifShowCallout) {
                            Callout callout = mapView.getCallout();
                            TextView textView = new TextView(getContext());
                            textView.setText("当前所在位置");
                            callout.setContent(textView);
                            callout.show(point); //显示气泡
                        }

                        if (ifAlwaysCeterToLocation) {
                            mapView.centerAt(point, true);
                        } else if (isFirstLocate) {
                            mapView.centerAt(point, true);
                        }
                        isFirstLocate = false;
                        if (isOnceLocation) {
                            isOnceLocation = false;
                            followLocation();
                        }
                        requestLocation(point, mapView.getSpatialReference(), new Callback1<DetailAddress>() {
                            @Override
                            public void onCallback(DetailAddress detailAddress) {
                                LocationInfo locationInfo = new LocationInfo();
                                locationInfo.setDetailAddress(detailAddress);
                                locationInfo.setLocation(location);
                                locationInfo.setPoint(point);
                                mLastLocation = locationInfo;
                            }
                        });
                    } else {
                        ToastUtil.shortToast(getContext(), "当前位置不在地图范围内");
                        LocationInfo locationInfo = new LocationInfo();
                        locationInfo.setLocation(location);
                        locationInfo.setPoint(point);
                        mLastLocation = locationInfo;
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

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
            mLocClient = new LocationClient(getContext().getApplicationContext());
            mBDLocationListener = new BDLocationListener(this);
            mLocClient.registerLocationListener(mBDLocationListener);
            //设置定位参数
            mLocClient.setLocOption(option);
        }
    }


    public void startLocateByBaidu(boolean isCenter) {
        if (mPreBDLocation != null) {
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
            drawLocationOnMap(location);
            //判断定位点是否在当前地图范围内
//            if (GeometryEngine.contains(mapView.getMaxExtent(),point,mapView.getSpatialReference())){
//                mapView.centerAt(point, true);
//            }else {
//                ToastUtil.shortToast(getContext(),"当前定位点不在地图范围内");
//            }
            if(isCenter){
                mapView.centerAt(point, true);
            }
        }
        mLocClient.start();//调用此方法开始定位
        if (mLocClient != null && mLocClient.isStarted()) {
            mLocClient.requestLocation();
        }
    }

    /**
     * 停止定位，移除定位点
     */
    private void stopLocate(boolean ifRemoveLocationPoint) {
        if (mLocationManager != null) {
            mLocationManager.stopLocate();
        }
        if (ifRemoveLocationPoint) {
            if (mGLayerFroDrawLocation != null) {
                mGLayerFroDrawLocation.removeAll();
            }
        }
    }

    /**
     * 停止定位，移除定位点
     */
    private void stopBaiduLocate(boolean ifRemoveLocationPoint) {
        if (mLocClient != null) {
            mLocClient.stop();
        }
        if (ifRemoveLocationPoint) {
            if (mGLayerFroDrawLocation != null) {
                mGLayerFroDrawLocation.removeAll();
            }
        }
    }


    /**
     * 请求地址（来自arcgis地名地址服务或者百度）
     *
     * @param point     这里不能直接传递Location，因为parseLocation方法也可能会使用arcgis的地名地址服务
     * @param callback1
     */
    public void requestLocation(Point point, SpatialReference spatialReference, final Callback1<DetailAddress> callback1) {
        SelectLocationService selectLocationService = new SelectLocationService(getContext().getApplicationContext(),
                Locator.createOnlineLocator());
        selectLocationService.parseLocation(new LatLng(point.getY(), point.getX()), spatialReference)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DetailAddress>() {
                    @Override
                    public void call(DetailAddress detailAddress) {
                        if (callback1 != null) {
                            callback1.onCallback(detailAddress);
                        }
                    }
                });
    }
    /**
     * 在地图上绘制当前位置
     *
     * @param location
     * @return
     */
    protected synchronized void drawLocationOnMap(Location location) {
        if (mGLayerFroDrawLocation == null) {
                mGLayerFroDrawLocation = new GraphicsLayer();
                mapView.addLayer(mGLayerFroDrawLocation);
        }
        Point point = new Point(location.getLongitude(), location.getLatitude());
//        if (false) {
        if (locationGraphicId != -1) {
            //之前已经存在
            PictureMarkerSymbol symbol = (PictureMarkerSymbol) graphic.getSymbol();
            Class<?> classes = Graphic.class;
            symbol.setAngle(-ratateAngle);
            Geometry geometry = (point);


            try {
                Method method = classes.getDeclaredMethod("setSymbol", Symbol.class);
                method.setAccessible(true);
                method.invoke(graphic, symbol);
                Method setGeometry = classes.getDeclaredMethod("setGeometry", Geometry.class);
                setGeometry.setAccessible(true);
                setGeometry.invoke(graphic,geometry);
                mGLayerFroDrawLocation.updateGraphic(locationGraphicId, graphic);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
                drawLocationButton(point);
            }

        } else {
            //之前不存在
//            Drawable drawable = getContext().getResources().getDrawable(R.mipmap.ic_compass);
//            PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(getContext(),
//                    getContext().getResources().getDrawable(R.mipmap.patrol_location_symbol));
            drawLocationButton(point);
        }
    }

    private void drawLocationButton(Point point) {
        Bitmap bitmap = ( (BitmapDrawable) ( getResources ( )
                .getDrawable ( R.mipmap.jiantou ) ) )
                .getBitmap ( );

        // 设置图片旋转的角度
        matrix.setRotate (-ratateAngle);
        bitmap = Bitmap.createBitmap (
                bitmap ,
                0 ,
                0 ,
                bitmap.getWidth ( ) ,
                bitmap.getHeight ( ) ,
                matrix ,
                true );
        Drawable drawable = new BitmapDrawable(bitmap);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(getContext(),
                drawable);
        mGLayerFroDrawLocation.removeAll();
        if(graphic == null){
            graphic = new Graphic(point, pictureMarkerSymbol);
        }
        locationGraphicId = mGLayerFroDrawLocation.addGraphic(graphic);
    }

    public void removeDrawLocation() {
        if (mGLayerFroDrawLocation != null) {
            mGLayerFroDrawLocation.removeAll();
            locationGraphicId = -1;
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLocate(true);
        stopBaiduLocate(true);
        if (mapView != null && !mapView.isRecycled()) {
            try {
                mapView.removeLayer(mGLayerFroDrawLocation);
                mapView.recycle();
                mapRotateManager.removeListener(this);
            } catch (Exception e) {

            }
        }
    }

    public LocationInfo getLastLocation() {
        return mLastLocation;
    }

    public void setIfDrawLocation(boolean ifDrawLocation) {
        this.ifDrawLocation = ifDrawLocation;
    }

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.mOnStateChangedListener = onStateChangedListener;
    }


     class BDLocationListener extends BDAbstractLocationListener {

        private WeakReference<LocationButton> locationButton;

        public BDLocationListener(LocationButton locationButton) {
            this.locationButton = new WeakReference<>(locationButton);
        }

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            boolean isGps = false;
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                //GPS定位
                        /*
                        if(bdLocation.getSpeed() >0){

                        }

                        ToastUtil.shortToast(getContext(),"当前定位是GPS定位");
                        */
                isGps = true;

            }//else if(bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
            //网络定位
            //   ToastUtil.shortToast(getContext(),"当前定位是网络定位");

            // bdLocation.getRadius()
            //  isGps = false;
            // }

            if (locationButton.get() == null) {
                return;
            }

            if (locationButton.get().mPreBDLocation == null) {
                locationButton.get().mPreBDLocation = bdLocation;
                locationButton.get().mPreBDRadius = bdLocation.getRadius();
            } else {
                BDPointer pre = new BDPointer(locationButton.get().mPreBDLocation.getLatitude(), locationButton.get().mPreBDLocation.getLongitude());
                BDPointer cur = new BDPointer(bdLocation.getLatitude(), bdLocation.getLongitude());
                float curRadius = bdLocation.getRadius();

                if (cur.distance(pre) < MIN_DISTANCE) {
                    //  ToastUtil.shortToast(getContext(),"前后两个点太近了,跳过吧");
                    return;
                }

                        /*
                        if(curRadius > mPreBDRadius){
                            ToastUtil.longToast(getContext(),"当前精确度没有之前的高,跳过吧!");
                            return;
                        }
                        */
                //Gps环境下才过滤掉
                if (isGps && curRadius > MIN_RADIUS) {
                    // ToastUtil.shortToast(getContext(),"当前精确度大于100,跳过吧!");
                    return;
                }

                locationButton.get().mPreBDLocation = bdLocation;
                locationButton.get().mPreBDRadius = bdLocation.getRadius();
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

            locationButton.get().mLastLocation = locationInfo;
            locationButton.get().drawLocationOnMap(location);
            //判断定位点是否在当前地图范围内
//                    if (GeometryEngine.contains(mapView.getMaxExtent(),point,mapView.getSpatialReference())){
//                        mapView.centerAt(point, true);
//                    }else {
//                        ToastUtil.shortToast(getContext(),"当前定位点不在地图范围内");
//                    }
            if(inState != State.normal){
                locationButton.get().mapView.centerAt(point, true);
            }
        }

    }



    public interface OnStateChangedListener {
        void onStateChanged(State state);
    }
}
