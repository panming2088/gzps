package com.augurit.agmobile.gzps.common.baiduselectlocation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.augurit.agmobile.bluetooth.BluetoothDataManager;
import com.augurit.agmobile.bluetooth.DataCallback;
import com.augurit.agmobile.bluetooth.model.LocationData;
import com.augurit.agmobile.bluetooth.model.MeasureData;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.mapengine.common.GeographyInfoManager;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.common.widget.scaleview.MapScaleView;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerPresenter;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.mapengine.location.ILocationManager;
import com.augurit.agmobile.patrolcore.common.MapLocateEvent;
import com.augurit.agmobile.patrolcore.common.legend.LegendPresenter;
import com.augurit.agmobile.patrolcore.editmap.IEditMapFeaturePresenter;
import com.augurit.agmobile.patrolcore.editmap.IEditMapFeatureView;
import com.augurit.agmobile.patrolcore.editmap.SendMapFeatureEvent;
import com.augurit.agmobile.patrolcore.editmap.widget.LocationMarker;
import com.augurit.agmobile.patrolcore.layer.service.PatrolLayerService;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView2;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.util.SelectLocationConstant;
import com.augurit.agmobile.patrolcore.selectlocation.view.IFinishSelectLocationListener;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.augurit.am.fw.common.BaseView;
import com.augurit.am.fw.utils.DeviceUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.augurit.agmobile.patrolcore.common.table.TableViewManager.geometry;


/**
 * ???????????????????????????????????????????????????
 *
 * @author ????????? ???xuciluan
 * @version 1.0
 * @package ?????? ???com.augurit.agmobile.patrolcore.selectlocation.view
 * @createTime ???????????? ???17/7/27
 * @modifyBy ????????? ???xuciluan
 * @modifyTime ???????????? ???17/7/27
 * @modifyMemo ???????????????
 */

public class BaiduSelectLocationView extends BaseView<IEditMapFeaturePresenter> implements IEditMapFeatureView {

    public static final String TAG = "????????????--??????";

    private TextView mTv_address;
    // private ImageView mIv_location;
    private TextView mTv_address_poi;
    private MapView mMapView;
    private TextView mBtn_finish_select_point; //"????????????"
    private TextView mBtn_go_to_destination;  //"????????????"
    private View mLl_target;    // ???????????????
    private TextView mTv_title;
    private ViewGroup mContainer; //??????
    private ImageButton ib_location;
    private View btn_back; //????????????
    private View ib_zoom_in;
    private View ib_zoom_out;
    //private View tv_map_manage;
    private Location mCurrrentLocation; //????????????
    private final int MSG = 1232;


    /**
     * ?????????????????????
     */
    private Point mLastSelectedLocation = null;

    /**
     * ????????????????????????
     */
    protected boolean mIfReadOnly;
    /**
     * ???????????????????????????
     */
    protected Geometry mDestinationOrLastTimeSelectLocation;

    /**
     * ??????????????????????????????
     */
    protected Geometry mInitialLocation;
    /**
     * ??????????????????????????????
     */
    protected String mInitSelectAddress;

    /**
     * ??????????????????
     */
    protected Geometry mFacilityOriginalLocation;
    /**
     * ?????????????????????
     */
    protected String mLastSelectedAddress;
    /**
     * ????????????????????????
     */
    protected boolean ifInFollowMode = false;

    /**
     * ????????????????????????
     */
    private boolean isFirstLocate = true;
    /**
     * ???????????????????????????
     */
    private double mInitialScale = -1;

    private GraphicsLayer mGraphicsLayer;   // ???????????????
    private int mTargetGraphicId = -1;   // ?????????graphic Id
    private GraphicsLayer mOfflineGraphicsLayer;   // ????????????
    /**
     * ???????????????
     */
    protected ILocationManager mLocationManager;
    private GraphicsLayer mGLayerFroDrawLocation;

    /**
     * ???????????????????????????????????????
     */
    protected boolean ifStartLocateWhenMapLoaded = true;

    protected IFinishSelectLocationListener mFinishSelectLocationListener;
    private View mRoot;
    /**
     * ???????????????????????????????????????????????????
     */
    private GraphicsLayer mGLayer;
    private LocationMarker locationMarker;

    private boolean mShouldRequestNewAddress = true;

    /**
     * ?????????????????????????????????
     */
    private boolean hasZoomBefore = false;
    private PatrolLayerPresenter layerPresenter;

    /**
     * ?????????????????????
     */
    private DetailAddress mCurrentAddress;
    private LegendPresenter legendPresenter;
    private MapScaleView scaleView;
    private GraphicsLayer mResultGraphicsLayer;
    private GraphicsLayer mOriginGraphicLayer;

    /**
     * ????????????
     *
     * @param context
     * @param container
     * @param locationManager
     */
    public BaiduSelectLocationView(Context context, ViewGroup container, ILocationManager locationManager) {
        super(context);
        EventBus.getDefault().register(this);//??????
        this.mContainer = container;
        this.mLocationManager = locationManager;
        initView();
        initListener();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MSG:
                    showCallout(mLastSelectedAddress, null);
                    break;
            }
            return false;
        }
    });

    @Override
    public void setFinishSelectLocationListener(IFinishSelectLocationListener finishSelectLocationListener) {
        this.mFinishSelectLocationListener = finishSelectLocationListener;
    }

    @Override
    public void turnOffStartLocateWhenMapLoaded() {
        ifStartLocateWhenMapLoaded = false;
    }

    private void initListener() {
        //????????????
        ib_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followLocation(ib_location);
            }
        });

        //????????????
        mLl_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locateTarget();
            }
        });

        //????????????
        mBtn_finish_select_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * ????????????
                 */
                finishSelect();
            }
        });


        mBtn_go_to_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.shortToast(mContext, "????????????????????????");
            }
        });

        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (status == STATUS.INITIALIZED && isFirstLocate) { //????????????????????????????????????
                    GeographyInfoManager.init(mMapView);
                    //??????????????????????????????????????????
                    drawDestinationOnMap(mMapView);
                    addDrawLocationGraphicLayer();
                    //scaleView.refreshScaleView();
                    if (PatrolLayerPresenter.initScale != -1 && PatrolLayerPresenter.initScale != 0) {
                        mMapView.setScale(PatrolLayerPresenter.initScale);
                        scaleView.setScale(PatrolLayerPresenter.initScale);
                    }
                    if (ifStartLocateWhenMapLoaded && mDestinationOrLastTimeSelectLocation == null) {
                        startLocate();
                    }
                    mMapView.setMaxScale(30);
                    if (mDestinationOrLastTimeSelectLocation != null) {
                        Envelope tEnvelope = new Envelope();
                        geometry.queryEnvelope(tEnvelope);
                        Point tPoint = tEnvelope.getCenter();
                        mMapView.centerAt(tPoint, false);
                        if (mInitialScale != -1 && mInitialScale != 0.0) {
                            mMapView.setScale(mInitialScale);
                        }
                        if(!TextUtils.isEmpty(mLastSelectedAddress)) {
                            showCallout(mLastSelectedAddress, null);
                        }
                    }
                    /**
                     * ?????????????????????
                     */
                    moveToInitialExtent();
                } else if (status == STATUS.INITIALIZED) {
                    GeographyInfoManager.init(mMapView);
                }
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFinishSelectLocationListener != null) {
                    mFinishSelectLocationListener.onFinish(null);
                }
            }
        });

        //??????????????????
        ib_zoom_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView.zoomin();

            }
        });

        ib_zoom_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView.zoomout();
            }
        });

        /**
         * ??????????????????
         */
        mRoot.findViewById(R.id.tv_choose_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mCurrrentLocation != null) {
//                    Point point = new Point(mCurrrentLocation.getLongitude(), mCurrrentLocation.getLatitude());
//                    mMapView.centerAt(point, true);
//                    /**
//                     * ????????????????????????callout,?????????????????????callout?????????????????????????????????????????????
//                     */
//                    suspendOneSecond();
//                    mLastSelectedLocation = point;
//                    if (mPresenter != null) {
//                        mPresenter.requestLocation(point.getX(), point.getY(), mMapView.getSpatialReference());
//                    }
//                } else if (mDestinationOrLastTimeSelectLocation != null) {
//                    Envelope tEnvelope = new Envelope();
//                    mDestinationOrLastTimeSelectLocation.queryEnvelope(tEnvelope);
//                    Point tPoint = tEnvelope.getCenter();
//                    mMapView.centerAt(tPoint, false);
//                    if (mInitialScale != -1 && mInitialScale != 0.0) {
//                        mMapView.setScale(mInitialScale);
//                    }
//                    if (!TextUtils.isEmpty(mLastSelectedAddress)){
//                        showCallout(mLastSelectedAddress, getMapCalloutCenterPoint());
//                    }
//                }

                if (mInitialLocation != null) {
                    Envelope tEnvelope = new Envelope();
                    mInitialLocation.queryEnvelope(tEnvelope);
                    Point tPoint = tEnvelope.getCenter();
                    if (mInitialScale != -1 && mInitialScale != 0.0) {
                        mMapView.setScale(mInitialScale);
                    }
                    mMapView.centerAt(tPoint, false);
                    showCallout(mInitSelectAddress, GeometryUtil.getGeometryCenter(mInitialLocation));
                }
            }
        });

    }

    /**
     * ????????????
     */
    public void suspendOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }


    /**
     * ?????????????????????
     */
    private void moveToInitialExtent() {
        if (!TextUtils.isEmpty(PatrolLayerService.MAP_EXTENT)) {
            String[] extents = PatrolLayerService.MAP_EXTENT.split(",");
            double[] longExtents = new double[extents.length + 1];
            for (int j = 0; j < extents.length; j++) {
                longExtents[j] = Double.valueOf(extents[j]);
            }
            Envelope envelope = new Envelope(longExtents[0], longExtents[1], longExtents[2], longExtents[3]);
            mMapView.setMaxExtent(envelope);
        }
    }

    private void finishSelect() {
        //?????????????????????
        Point point = getMapCenterPoint();

        if (point != null) {
            LogUtil.d(TAG, "???????????????????????????" + point.getX() + ":" + point.getY());
        } else {
            LogUtil.e(TAG, "??????????????????????????????");
        }
        Graphic graphic = getPointGraphic(point);
        EventBus.getDefault().post(new SendMapFeatureEvent(graphic, mMapView.getScale(), mCurrentAddress));

        if (mFinishSelectLocationListener != null) {
            mFinishSelectLocationListener.onFinish(null);
        }
    }


    private void addDrawLocationGraphicLayer() {
        mGLayerFroDrawLocation = new GraphicsLayer();
        mMapView.addLayer(mGLayerFroDrawLocation);
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param mapView
     */
    private void drawDestinationOnMap(MapView mapView) {

        if (mDestinationOrLastTimeSelectLocation != null && mDestinationOrLastTimeSelectLocation.getType() == Geometry.Type.POINT) {

            Point point = (Point) mDestinationOrLastTimeSelectLocation;
//            Point point = new Point(mDestinationOrLastTimeSelectLocation.getLongitude(),mDestinationOrLastTimeSelectLocation.getLatitude());

            if (mIfReadOnly) { //???????????????????????????(?????????)????????????????????????????????????????????????????????????
                locationMarker.setVisibility(View.GONE);
                GraphicsLayer graphicsLayer = new GraphicsLayer();
                mapView.addLayer(graphicsLayer);
                Graphic graphic = getPointGraphic(point);
                graphicsLayer.addGraphic(graphic);
                mMapView.centerAt(point, true);
            } else { //????????????????????????????????????
//                Point mapPoint = new Point(mDestinationOrLastTimeSelectLocation.getLongitude(),
//                        mDestinationOrLastTimeSelectLocation.getLatitude());
                Point mapPoint = (Point) mDestinationOrLastTimeSelectLocation;
                mMapView.centerAt(mapPoint, true);
                //     mMapView.setScale(mMapView.getMaxScale());
            }
            zoomToBestScale();
            if (!TextUtils.isEmpty(mLastSelectedAddress)) {
                mTv_address.setText(mLastSelectedAddress);
            }
        } else {
            // 2017-09-22 ?????????????????? ????????????????????????????????????
            zoomToBestScale();
        }
    }

    @NonNull
    private Graphic getPointGraphic(Point point) {
        Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), R.mipmap.ic_select_location, null);
        Symbol symbol = new PictureMarkerSymbol(mContext, drawable);    // xjx ????????????api19???????????????drawable
        return new Graphic(point, symbol);
    }

    /**
     * ?????????????????????
     */
    private void zoomToBestScale() {

        if (mInitialScale != -1 && mInitialScale != 0.0) {
            mMapView.setScale(mInitialScale);
        }
    }


    /**
     * ???????????????
     */
    private void locateTarget() {
        final BluetoothDataManager manager = BluetoothDataManager.getInstance(mContext);
        if (manager.getConnectedDevice() == null) {
            ToastUtil.shortToast(mContext, "????????????????????????");
            return;
        }
        // ????????????
        ToastUtil.shortToast(mContext, "?????????????????????????????????????????????");
        manager.requestMeasure(new DataCallback<MeasureData>() {
            @Override
            public void onCallback(MeasureData measureData) {
                // ???????????????
                manager.requestLocation(LocationData.POINT_TARGET, LocationData.DATA_BLH, new DataCallback<LocationData>() {
                    @Override
                    public void onCallback(LocationData locationData) {
                        // ?????????????????????
                        ToastUtil.shortToast(mContext, "??????????????????");
                        Location location = new Location("BluetoothProvider");
                        location.setLatitude(locationData.getLatitude());
                        location.setLongitude(locationData.getLongitude());
                        mCurrrentLocation = location;
                        if (mLocationManager != null) {
                            mLocationManager.setMapView(mMapView);
                            location = mLocationManager.changeWGS84ToCurrentCoordinate(location);
                            drawTargetOnMap(location);
                        }

                    }

                    @Override
                    public void onError(int i, Exception e) {
                        e.printStackTrace();
                        LogUtil.e("SelectLocationActivity", "?????????????????????????????????:" + i);
                        ToastUtil.shortToast(mContext, "?????????????????????");
                    }
                });
            }

            @Override
            public void onError(int i, Exception e) {
                e.printStackTrace();
                LogUtil.e("SelectLocationActivity", "????????????????????????:" + i);
                ToastUtil.shortToast(mContext, "????????????????????????");
            }
        });
    }


    /**
     * ?????????????????????
     *
     * @param target ?????????location
     */
    private void drawTargetOnMap(Location target) {
        if (mGraphicsLayer == null) {
            mGraphicsLayer = new GraphicsLayer();
            mMapView.addLayer(mGraphicsLayer);
        }
        Point point = new Point(target.getLongitude(), target.getLatitude());
        if (mTargetGraphicId == -1) {
            int color = ResourcesCompat.getColor(mContext.getResources(), R.color.agmobile_blue, null);
            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(color, 15, SimpleMarkerSymbol.STYLE.X);
            Graphic graphic = new Graphic(point, symbol);
            mTargetGraphicId = mGraphicsLayer.addGraphic(graphic);
        } else {
            mGraphicsLayer.updateGraphic(mTargetGraphicId, point);
        }
        mMapView.centerAt(point, true);
    }

    /**
     * ??????????????????
     *
     * @param iv_location
     */
    protected void followLocation(ImageButton iv_location) {
        if (ifInFollowMode) {
            stopLocate();
            ifInFollowMode = false;
            iv_location.setImageResource(R.mipmap.common_ic_target);
        } else {
            ifInFollowMode = true;
            isFirstLocate = false;
            iv_location.setImageResource(R.mipmap.common_ic_location_follow);
            if (mLocationManager != null && mLocationManager instanceof PatrolLocationManager && ((PatrolLocationManager) mLocationManager).isStarted()) {
                return;
            }
            startLocate();
        }
    }


    /**
     * ????????????
     */
//    @NeedPermission(permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void startLocate() {
        /*PermissionsUtil2.getInstance()
                .requestPermissions(
                        (Activity) mContext,
                        "????????????????????????????????????????????????????????????", 101,
                        new PermissionsUtil2.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                startLocateWithCheck();
                            }
                        },
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);*/

        PermissionsUtil.getInstance()
                .requestPermissions(
                        (Activity) mContext,
                        new PermissionsUtil.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                startLocateWithCheck();
                            }

                            @Override
                            public void onPermissionsDenied(List<String> perms) {

                            }
                        },
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private void startLocateWithCheck() {
        if (mLocationManager != null) {
            //this.mLocationManager.setMapView(mMapView);
            mLocationManager.startLocate(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    mCurrrentLocation = location;
                    drawLocationOnMap(location);
                    Point point = new Point(location.getLongitude(), location.getLatitude());
                    mLastSelectedLocation = point;
                    if (mMapView.getMaxExtent() == null || mMapView.getSpatialReference() == null) {
                        return;
                    }
                    if (GeometryEngine.contains(mMapView.getMaxExtent(), point, mMapView.getSpatialReference())) {
                        if (isFirstLocate) {
                            isFirstLocate = false;
                            if (mDestinationOrLastTimeSelectLocation == null) {//?????????????????????????????????????????????????????????????????????????????????
                                mMapView.centerAt(point, true);
                                showCallout("?????????.....", null);
                                /**
                                 * ????????????????????????callout,?????????????????????callout?????????????????????????????????????????????
                                 */
                                suspendOneSecond();
                                if (mPresenter != null) {
                                    mPresenter.requestLocation(location.getLongitude(), location.getLatitude(), mMapView.getSpatialReference());
                                }
                                //requestAddress(location.getLatitude(),location.getLongitude());
                            }
                            stopLocate(); //?????????????????????????????????????????????????????????????????????????????????
                        }
                    } else {
                        ToastUtil.shortToast(mContext, "?????????????????????????????????");
                        stopLocate(); //?????????????????????????????????????????????????????????????????????????????????
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

    /**
     * ??????????????????????????????
     *
     * @param location
     * @return
     */
    private void drawLocationOnMap(Location location) {
        Point point = new Point(location.getLongitude(), location.getLatitude());
        if (mGLayerFroDrawLocation != null) {
            mGLayerFroDrawLocation.removeAll();
            PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mContext,
                    mContext.getResources().getDrawable(R.mipmap.patrol_location_symbol));
            Graphic graphic = new Graphic(new Point(location.getLongitude(), location.getLatitude()), pictureMarkerSymbol);
            mGLayerFroDrawLocation.addGraphic(graphic);
        }
    }

    /**
     * ????????????
     */
    public void stopLocate() {
        if (mLocationManager != null) {
            mLocationManager.stopLocate();
        }
    }

    @Override
    public void recycleMapView() {
        if (mMapView != null) {
            mMapView.recycle();
        }
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void setFacilityOriginLocation(Geometry geometry) {
        this.mFacilityOriginalLocation = geometry;
    }

    @Override
    public void addViewToContaner() {
        mContainer.removeAllViews();
        mContainer.addView(mRoot);
    }


    protected void initView() {

        mRoot = View.inflate(mContext, R.layout.view_baidu_select_location, null);
        mTv_address = (TextView) mRoot.findViewById(R.id.tv_map_address);
        mTv_address_poi = (TextView) mRoot.findViewById(R.id.tv_map_address_poi);

        mTv_title = (TextView) mRoot.findViewById(R.id.tv_title);
        //????????????
        // mIv_location = (ImageView) mRoot.findViewById(R.id.iv_location);

        mMapView = (MapView) mRoot.findViewById(R.id.mapview);
        mMapView.setMapBackground(Color.WHITE, Color.TRANSPARENT, 0f, 0f);//???????????????????????????????????????
        mLocationManager.setMapView(mMapView);
        //????????????????????????
        mBtn_go_to_destination = (TextView) mRoot.findViewById(R.id.btn_go_to_destination);

        //????????????????????????
        mBtn_finish_select_point = (TextView) mRoot.findViewById(R.id.btn_finish_select_point);

        mLl_target = mRoot.findViewById(R.id.ll_btn_target);

        if (!SelectLocationConstant.ifLocateByBlueTooth) {
            mLl_target.setVisibility(View.GONE);
        }


        ib_location = (ImageButton) mRoot.findViewById(R.id.btn_location);

        btn_back = mRoot.findViewById(R.id.btn_back);

        ib_zoom_in = mRoot.findViewById(R.id.ib_zoom_in);

        ib_zoom_out = mRoot.findViewById(R.id.ib_zoom_out);

        locationMarker = (LocationMarker) mRoot.findViewById(R.id.locationmarker);

        /**
         * ?????????
         */
        scaleView = (MapScaleView) mRoot.findViewById(R.id.scale_view);
        scaleView.setMapView(mMapView);
        mMapView.setOnZoomListener(new OnZoomListener() {
            @Override
            public void preAction(float v, float v1, double v2) {

            }

            @Override
            public void postAction(float v, float v1, double v2) {
                scaleView.refreshScaleView();
            }
        });

    }

    /**
     * ????????????
     */
    @Override
    public void loadMap() {
        ILayerView layerView = new PatrolLayerView2(mContext, mMapView, null);
        mMapView.setOnTouchListener(getMapOnTouchListener());
        ILayerPresenter layerPresenter = new PatrolLayerPresenter(layerView);
        layerPresenter.loadLayer();
        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (status == STATUS.INITIALIZED) {
                    if (PatrolLayerPresenter.initScale != -1 && PatrolLayerPresenter.initScale != 0) {
                        mMapView.setScale(PatrolLayerPresenter.initScale);
                    }

                    if (mDestinationOrLastTimeSelectLocation == null && PatrolLayerPresenter.longitude != 0 && PatrolLayerPresenter.latitude != 0) {
                        mMapView.centerAt(PatrolLayerPresenter.latitude, PatrolLayerPresenter.longitude, true);
                        followLocation(ib_location);
                    }
                    if (mDestinationOrLastTimeSelectLocation != null) {
                        Envelope tEnvelope = new Envelope();
                        mDestinationOrLastTimeSelectLocation.queryEnvelope(tEnvelope);
                        Point tPoint = tEnvelope.getCenter();
                        if (mInitialScale != -1 && mInitialScale != 0.0) {
                            mMapView.setScale(mInitialScale);
                        }
                        mMapView.centerAt(tPoint, false);
                        if(!TextUtils.isEmpty(mLastSelectedAddress)) {
                            handler.sendEmptyMessageDelayed(MSG, 500);
                        }
                      // highLightMap(mDestinationOrLastTimeSelectLocation);
                    }

                    if (mFacilityOriginalLocation != null) {
                        Envelope tEnvelope = new Envelope();
                        mFacilityOriginalLocation.queryEnvelope(tEnvelope);
                        Point tPoint = tEnvelope.getCenter();
                        if (mInitialScale != -1 && mInitialScale != 0.0) {
                            mMapView.setScale(mInitialScale);
                        }
                        //mMapView.centerAt(tPoint, false);
                        if(!TextUtils.isEmpty(mLastSelectedAddress)) {
                            handler.sendEmptyMessageDelayed(MSG, 500);
                        }
                        highLightFacilityOriginLocation(mFacilityOriginalLocation);
                    }

                }
            }
        });
    }


    /**
     * ???????????????????????????
     * @param mDestinationOrLastTimeSelectLocation
     */
    private void highLightFacilityOriginLocation(Geometry mDestinationOrLastTimeSelectLocation) {
        if (mOriginGraphicLayer == null) {
            mOriginGraphicLayer = new GraphicsLayer();
            mMapView.addLayer(mOriginGraphicLayer);
        }
        mOriginGraphicLayer.removeAll();
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mContext,
                mContext.getResources().getDrawable(R.drawable.old_map_point));
        pictureMarkerSymbol.setOffsetY(16);
        Graphic graphic = new Graphic(mDestinationOrLastTimeSelectLocation, pictureMarkerSymbol);
        mOriginGraphicLayer.addGraphic(graphic);
        mOriginGraphicLayer.setSelectedGraphics(new int[]{graphic.getUid()}, true);
    }


    /**
     * Fling???????????????
     */
    // private boolean hasFlingFinished = true;

    /**
     * ??????????????????
     *
     * @return
     */
    @NonNull
    private MapOnTouchListener getMapOnTouchListener() {
        return new MapOnTouchListener(mContext, mMapView) {
            private final int DOUBLE_TAP_TIMEOUT = 200;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LogUtil.d("onTouch");
//                /**
//                 * ????????????????????????????????????
//                 */
//                if (!mShouldRequestNewAddress){
//                    return super.onTouch(v, event);
//                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (mMapView.getCallout().isShowing()) {
                            mMapView.getCallout().hide();
                        }
                        locationMarker.startUpAnimation(null);
                        break;
                    case MotionEvent.ACTION_UP:
                        centerToNewAddress();
                        /**
                         * ???????????????????????????????????????????????????????????????????????????
                         */
//                        if (hasZoomBefore){
//                            hasZoomBefore = false;
//                            locationMarker.startDownAnimation(null);
//                        }else {
//                            centerToNewAddress();
//                        }

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        centerToNewAddress();
                        break;
                    default:
                        break;
                }
                return super.onTouch(v, event);
            }

            @Override
            public boolean onFling(MotionEvent from, MotionEvent to, float velocityX, float velocityY) {
                //LogUtil.d("?????????x" + velocityX + "?????????y" + velocityY);
//                hasFlingFinished = false;
//                long duration = from.getEventTime() - to.getEventTime();
                return super.onFling(from, to, velocityX, velocityY);
            }

            @Override
            public boolean onPinchPointersUp(MotionEvent event) {
                LogUtil.d("onPinchPointersUp");
                if (mLastSelectedLocation != null) {
                    /**
                     * ??????????????????????????????????????????
                     */
                    mMapView.centerAt(mLastSelectedLocation, true);
                    drawPoint(mLastSelectedLocation);
                }
                mShouldRequestNewAddress = true;
                hasZoomBefore = true;
                return super.onPinchPointersUp(event);
            }

            @Override
            public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
                // LogUtil.d("onDragPointerMove");
                return super.onDragPointerMove(from, to);
            }

            @Override
            public boolean onDoubleTapDrag(MotionEvent from, MotionEvent to) {
                //LogUtil.d("onDoubleTapDrag");
                return super.onDoubleTapDrag(from, to);
            }

            @Override
            public boolean onPinchPointersDown(MotionEvent event) {
                // LogUtil.d("onPinchPointersDown");
                mShouldRequestNewAddress = false;
                return super.onPinchPointersDown(event);
            }

            @Override
            public boolean onDoubleTap(MotionEvent point) {
                if (mLastSelectedLocation != null) {
                    /**
                     * ??????????????????????????????????????????
                     */
                    mMapView.centerAt(mLastSelectedLocation, true);
                    drawPoint(mLastSelectedLocation);
                    if (mPresenter != null) {
                        mPresenter.requestLocation(mLastSelectedLocation.getX(), mLastSelectedLocation.getY(), mMapView.getSpatialReference());
                    }

                }
                mShouldRequestNewAddress = true;
                hasZoomBefore = true;
                return super.onDoubleTap(point);
            }
        };
    }

    private void centerToNewAddress() {
        locationMarker.startDownAnimation(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Point point = getMapCenterPoint();
                mLastSelectedLocation = point;
                drawPoint(point);
                showCallout("?????????.....", null);
                if (!mIfReadOnly && point != null) {
                    if (mPresenter != null) {
                        mPresenter.requestLocation(point.getX(), point.getY(), mMapView.getSpatialReference());
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param point
     */
    private void drawPoint(Point point) {
//        initGLayer();
//        Symbol symbol = new SimpleMarkerSymbol(mContext.getResources().getColor(R.color.agmobile_grey_2), 7, SimpleMarkerSymbol.STYLE.CIRCLE);
//        mGLayer.removeAll();
//        Graphic graphic = new Graphic(point, symbol);
//        mGLayer.addGraphic(graphic);
    }

    private void initGLayer() {
        if (mGLayer == null) {
            mGLayer = new GraphicsLayer();
            mMapView.addLayer(mGLayer);
        }
    }


    @Override
    public void showAddress(DetailAddress address) {
        mCurrentAddress = address;
        mTv_address.setText(address.getDetailAddress());
        mTv_address_poi.setText(address.getDetailAddress());

        showCallout(address.getDetailAddress(), null);
    }

    private void showCallout(String address, Point point) {
        Callout callout = mMapView.getCallout();
        View view = View.inflate(mContext, R.layout.editmap_callout, null);
        ((TextView) view.findViewById(R.id.tv_listcallout_title)).setText(address);
        view.findViewById(R.id.btn_select_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishSelect();
            }
        });
        callout.setStyle(R.xml.editmap_callout_style);
        callout.setContent(view);
        if (point == null) {
            point = mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2, locationMarker.getY() + locationMarker.getHeight() / 2 - 140);
        }
        // Point point = getMapCenterPoint();
        callout.show(point);
    }

    private Point getMapCenterPoint() {
        //?????????????????????
        // float x = DeviceUtil.getScreenWidth(mContext)/2 ;
        // float y = DeviceUtil.getScreenHeight(mContext)/2 ;
        // float y = DeviceUtil.getScreenHeight(mContext)/2 +  mIv_location.getHeight()/3;
        // return mMapView.toMapPoint(x, y);
        return mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2, locationMarker.getY() + locationMarker.getHeight() / 2);
    }

    private Point getMapCalloutCenterPoint() {
        //?????????????????????
        Point mPoint = new Point();
        if (mDestinationOrLastTimeSelectLocation != null) {
            Envelope tEnvelope = new Envelope();
            mDestinationOrLastTimeSelectLocation.queryEnvelope(tEnvelope);
            mPoint = tEnvelope.getCenter();
            if (mInitialScale != -1 && mInitialScale != 0.0) {
                mMapView.setScale(mInitialScale);
            }
            Point tPoint = mMapView.toScreenPoint(mPoint);
            Point mapPoint = mMapView.toMapPoint((float) tPoint.getX(), (float) tPoint.getY() - 140);
            return mapPoint;
        }
        return mPoint;
    }

    private Point getScreenCenterPoint() {
        //?????????????????????
        float x = DeviceUtil.getScreenWidth(mContext) / 2;
        float y = DeviceUtil.getScreenHeight(mContext) / 2 - 140;
        // float y = DeviceUtil.getScreenHeight(mContext)/2 +  mIv_location.getHeight()/3;
        return mMapView.toMapPoint(x, y);
        // return mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth()/2 , locationMarker.getY() +locationMarker.getHeight()/2);
    }

    /**
     * ????????????????????????????????????
     */
    @Override
    public void showAddressNotFound() {

        mTv_address.setText("????????????");
        mTv_address_poi.setText("????????????");
    }

    @Override
    public void setIfReadOnly(boolean mIfReadOnly) {
        this.mIfReadOnly = mIfReadOnly;
        if (mIfReadOnly) {
            //?????????????????????
            mBtn_finish_select_point.setVisibility(View.GONE);
            mBtn_go_to_destination.setVisibility(View.VISIBLE);
            locationMarker.setVisibility(View.GONE);
            //tv_map_manage.setVisibility(View.GONE);
            mTv_title.setText("??????????????????");
        }
    }

    @Override
    public void setDestinationOrLastTimeSelectLocation(Geometry mDestinationOrLastTimeSelectLocation) {
        this.mDestinationOrLastTimeSelectLocation = mDestinationOrLastTimeSelectLocation;
        this.mInitialLocation = mDestinationOrLastTimeSelectLocation;
        if (mLastSelectedLocation == null && mDestinationOrLastTimeSelectLocation != null && mDestinationOrLastTimeSelectLocation.getType() == Geometry.Type.POINT) {
            mLastSelectedLocation = (Point) mDestinationOrLastTimeSelectLocation;
        }
    }

    @Override
    public void setLastSelectedAddress(String mLastSelectedAddress) {
        this.mLastSelectedAddress = mLastSelectedAddress;
        this.mInitSelectAddress = mLastSelectedAddress;
    }


    @Override
    public void setInitialScale(double mInitialScale) {
        this.mInitialScale = mInitialScale;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMapLocateEvent(MapLocateEvent event) {
        //????????????
        if (mMapView != null && event.getEvelope() != null) {
            mMapView.setExtent(event.getEvelope());
        }
    }

    @Override
    public void showLayerList(ViewGroup container) {
        if (layerPresenter == null) {
            PatrolLayerView2 layerView = new PatrolLayerView2(mContext, mMapView, container);
            layerPresenter = new PatrolLayerPresenter(layerView);
        }
        layerPresenter.showLayerList();
    }

    /**
     * ????????????
     *
     * @param editMode
     */
    @Deprecated
    @Override
    public void setEditMode(String editMode) {

    }

}
