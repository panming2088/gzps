package com.augurit.agmobile.patrolcore.selectlocation.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.bluetooth.BluetoothDataManager;
import com.augurit.agmobile.bluetooth.DataCallback;
import com.augurit.agmobile.bluetooth.model.LocationData;
import com.augurit.agmobile.bluetooth.model.MeasureData;
import com.augurit.agmobile.mapengine.common.GeographyInfoManager;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerPresenter;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.mapengine.layermanage.view.LayerView;
import com.augurit.agmobile.mapengine.location.ILocationManager;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.MapLocateEvent;
import com.augurit.agmobile.patrolcore.common.widget.LocationButton;
import com.augurit.agmobile.patrolcore.layer.service.PatrolLayerService;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView2;
import com.augurit.agmobile.patrolcore.layerdownload.activity.LayerDownloadActivity;
import com.augurit.agmobile.patrolcore.selectlocation.model.Ease;
import com.augurit.agmobile.patrolcore.selectlocation.model.EasingInterpolator;
import com.augurit.agmobile.patrolcore.selectlocation.model.OnSelectLocationFinishEvent;
import com.augurit.agmobile.patrolcore.selectlocation.util.SelectLocationConstant;
import com.augurit.am.fw.common.BaseView;
import com.augurit.am.fw.utils.DeviceUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view
 * @createTime 创建时间 ：17/7/27
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/27
 * @modifyMemo 修改备注：
 */

public class SelectLocationVIew extends BaseView<ISelectLocationPresenter> implements ISelectLocationView {

    public static final String TAG = "巡查模块--地图";

    private TextView mTv_address;
    private ImageView mIv_location;
    private TextView mTv_address_poi;
    private MapView mMapView;
    private TextView mBtn_finish_select_point; //"确定选点"
    private TextView mBtn_go_to_destination;  //"到这里去"
    private View mLl_target;    // 目标点按钮
    private TextView mTv_title;
    private ViewGroup mContainer; //容器
    //private ImageButton ib_location;
    private View btn_back; //返回按钮
    private View ib_zoom_in;
    private View ib_zoom_out;
    private View tv_map_manage;


    /**
     * 是否处于阅读状态
     */
    protected boolean mIfReadOnly;
    /**
     * 上次选择的地址坐标
     */
    protected LatLng mDestinationOrLastTimeSelectLocation;
    /**
     * 上次选择的地址
     */
    protected String mLastSelectedAddress;
    /**
     * 是否处于跟随模式
     */
    protected boolean ifInFollowMode = false;

    /**
     * 是否是第一次定位
     */
    private boolean isFirstLocate = true;
    /**
     * 地图的初始缩放比例
     */
    private double mInitialScale = -1;

    private GraphicsLayer mGraphicsLayer;   // 显示目标点
    private int mTargetGraphicId = -1;   // 目标点graphic Id
    private GraphicsLayer mOfflineGraphicsLayer;   // 离线地图
    /**
     * 定位管理者
     */
    protected ILocationManager mLocationManager;
    private GraphicsLayer mGLayerFroDrawLocation;

    /**
     * 地图加载完成后是否进行定位
     */
    protected boolean ifStartLocateWhenMapLoaded = true;

    protected IFinishSelectLocationListener mFinishSelectLocationListener;
    private View mRoot;
    private ILayerPresenter layerPresenter;
    private LocationButton locationButton;
    private ILayerView layerView;

    /**
     * 构造函数
     *
     * @param context
     * @param container
     * @param locationManager
     */
    public SelectLocationVIew(Context context, ViewGroup container, ILocationManager locationManager) {
        super(context);
        EventBus.getDefault().register(this);//订阅
        this.mContainer = container;
        this.mLocationManager = locationManager;
        initView();
        initListener();
    }

    @Override
    public void setFinishSelectLocationListener(IFinishSelectLocationListener finishSelectLocationListener) {
        this.mFinishSelectLocationListener = finishSelectLocationListener;
    }

    @Override
    public void turnOffStartLocateWhenMapLoaded() {
        ifStartLocateWhenMapLoaded = false;
    }

    private void initListener() {
//        //普通定位
//        ib_location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                followLocation(ib_location);
//            }
//        });

        //设备定位
        mLl_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locateTarget();
            }
        });

        //结束选择
        mBtn_finish_select_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mGLayerFroDrawLocation != null) {
                    mGLayerFroDrawLocation.removeAll();//隐藏定位图标，避免被截屏进去
                    mMapView.getCallout().hide(); //隐藏callout，避免被截屏进去
                }

                //获取当前的位置
                float x = DeviceUtil.getScreenWidth(mContext) / 2;
                float y = DeviceUtil.getScreenHeight(mContext) / 2 - mIv_location.getHeight() / 3;

                Point point = mMapView.toMapPoint(x, y);

                if (point != null) {
                    LogUtil.d(TAG, "最后上传的坐标是：" + point.getX() + ":" + point.getY());
                } else {
                    LogUtil.e(TAG, "最后上传的坐标是为空");
                }

                /**
                 * 接收方是：{@link MapTableItemPresenter#onReceivedSelectLocationFinishEvent(OnSelectLocationFinishEvent)}}
                 */
                OnSelectLocationFinishEvent onSelectLocationFinishEvent = new OnSelectLocationFinishEvent(new LatLng(point.getY(), point.getX()), mMapView.getScale(), mTv_address.getText().toString());
                EventBus.getDefault().post(onSelectLocationFinishEvent);

                if (mFinishSelectLocationListener != null) {
                    mFinishSelectLocationListener.onFinish(onSelectLocationFinishEvent);
                }
            }
        });


        mBtn_go_to_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.shortToast(mContext, "暂不支持路径导航");
            }
        });

        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (status == STATUS.INITIALIZED && isFirstLocate) { //当加载地图完成时进行定位
                    //TODO 地图下载功能没用到，这句注释
//                    GeographyInfoManager.init(mMapView);
                    //如果有传递坐标过来，进行绘制
                    drawDestinationOnMap(mIv_location, mMapView);
                    addDrawLocationGraphicLayer();

                    if (ifStartLocateWhenMapLoaded && mDestinationOrLastTimeSelectLocation == null) {
                        startLocate();
                    }

                    if (mInitialScale == -1 || mInitialScale == 0) {
                        mMapView.setScale(PatrolLayerPresenter.initScale);
                    }

                    if (!TextUtils.isEmpty(PatrolLayerService.MAP_EXTENT)) {
                        String[] extents = PatrolLayerService.MAP_EXTENT.split(",");
                        double[] longExtents = new double[extents.length + 1];
                        for (int j = 0; j < extents.length; j++) {
                            longExtents[j] = Double.valueOf(extents[j]);
                        }
                        Envelope envelope = new Envelope(longExtents[0], longExtents[1], longExtents[2], longExtents[3]);
                        mMapView.setMaxExtent(envelope);
                    }

                } else if (status == STATUS.INITIALIZED) {
                    //TODO 地图下载功能没用到，这句注释
//                    GeographyInfoManager.init(mMapView);
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

        //放大缩小按钮
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

        //地图管理
        tv_map_manage = mRoot.findViewById(R.id.tv_map_manager);
        tv_map_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到地图管理界面
                GeographyInfoManager.getInstance().setExtent(mMapView.getExtent());
                Intent intent = new Intent(mContext, LayerDownloadActivity.class);
                mContext.startActivity(intent);
            }
        });

    }


    private void addDrawLocationGraphicLayer() {
        mGLayerFroDrawLocation = new GraphicsLayer();
        mMapView.addLayer(mGLayerFroDrawLocation);
    }

    /**
     * 如果有位置传递过来，阅读模式下：直接画在地图上，让它跟着地图一起动；编辑模式下：移动图标
     *
     * @param iv_location
     * @param mapView
     */
    private void drawDestinationOnMap(ImageView iv_location, MapView mapView) {

        if (mDestinationOrLastTimeSelectLocation != null) {
            Point point = new Point(mDestinationOrLastTimeSelectLocation.getLongitude(), mDestinationOrLastTimeSelectLocation.getLatitude());

            if (mIfReadOnly) { //不可编辑目的地位置(仅查阅)，那么直接画在地图上，让它跟着地图一起动
                iv_location.setVisibility(View.GONE);
                GraphicsLayer graphicsLayer = new GraphicsLayer();
                mapView.addLayer(graphicsLayer);
                Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), R.mipmap.ic_select_location, null);
                Symbol symbol = new PictureMarkerSymbol(mContext, drawable);    // xjx 改为兼容api19的方式获取drawable
                Graphic graphic = new Graphic(point, symbol);
                graphicsLayer.addGraphic(graphic);
                mMapView.centerAt(point, true);
            } else { //如果是可编辑目的地位置的
                Point mapPoint = new Point(mDestinationOrLastTimeSelectLocation.getLongitude(),
                        mDestinationOrLastTimeSelectLocation.getLatitude());
                mMapView.centerAt(mapPoint, true);
                //     mMapView.setScale(mMapView.getMaxScale());
            }
            zoomToBestScale();
            if (!TextUtils.isEmpty(mLastSelectedAddress)) {
                mTv_address.setText(mLastSelectedAddress);
            }
        } else {
            // 2017-09-22 四标四实项目 不管有没有目标点，都缩放
            zoomToBestScale();
        }
    }

    /**
     * 缩放到合适比例
     */
    private void zoomToBestScale() {

        if (mInitialScale != -1) {
            mMapView.setScale(mInitialScale);
        }
    }


    /**
     * 定位目标点
     */
    private void locateTarget() {
        final BluetoothDataManager manager = BluetoothDataManager.getInstance(mContext);
        if (manager.getConnectedDevice() == null) {
            ToastUtil.shortToast(mContext, "未连接到定位设备");
            return;
        }
        // 激光测距
        ToastUtil.shortToast(mContext, "测量目标距离中，请保持设备平稳");
        manager.requestMeasure(new DataCallback<MeasureData>() {
            @Override
            public void onCallback(MeasureData measureData) {
                // 定位目标点
                manager.requestLocation(LocationData.POINT_TARGET, LocationData.DATA_BLH, new DataCallback<LocationData>() {
                    @Override
                    public void onCallback(LocationData locationData) {
                        // 显示目标点位置
                        ToastUtil.shortToast(mContext, "定位目标成功");
                        Location location = new Location("BluetoothProvider");
                        location.setLatitude(locationData.getLatitude());
                        location.setLongitude(locationData.getLongitude());
                        if (mLocationManager != null) {
                            mLocationManager.setMapView(mMapView);
                            location = mLocationManager.changeWGS84ToCurrentCoordinate(location);
                            drawTargetOnMap(location);
                        }

                    }

                    @Override
                    public void onError(int i, Exception e) {
                        e.printStackTrace();
                        LogUtil.e("SelectLocationActivity", "定位目标点失败，错误码:" + i);
                        ToastUtil.shortToast(mContext, "定位目标点失败");
                    }
                });
            }

            @Override
            public void onError(int i, Exception e) {
                e.printStackTrace();
                LogUtil.e("SelectLocationActivity", "测距失败，错误码:" + i);
                ToastUtil.shortToast(mContext, "测距失败，请重试");
            }
        });
    }


    /**
     * 地图显示目标点
     *
     * @param target 目标点location
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
     * 定位跟随模式
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
            startLocate();
            iv_location.setImageResource(R.mipmap.common_ic_location_follow);
        }
    }

    public void startLocate() {
        if (locationButton != null) {
            locationButton.followLocation();
        }
    }


//    /**
//     * 开启定位
//     */
//    public void startLocate() {
//
//        if (mLocationManager != null){
//            this.mLocationManager.setMapView(mMapView);
//            mLocationManager.startLocate(new LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//                    // LogUtil.e(TAG,"定位到的坐标是："+location.getLatitude() +"-->"+ location.getLongitude());
//                    // Point point = drawLocationOnMap(location); //在地图上画点
//                   /* if (mLocationManager.getMapView() == null){
//                        drawLocationOnMap(location);
//                    }*/
//                    drawLocationOnMap(location);
//                    Point point = new Point(location.getLongitude(),location.getLatitude());
//                    if (mMapView.getMaxExtent() == null || mMapView.getSpatialReference() == null){
//                        return;
//                    }
//                    if (GeometryEngine.contains(mMapView.getMaxExtent(),point,mMapView.getSpatialReference())){
//                        Callout callout = mMapView.getCallout();
//                        TextView textView = new TextView(mContext);
//                        textView.setText("当前所在位置");
//                        callout.setContent(textView);
//                        callout.show(point); //显示气泡
//                        if (isFirstLocate){
//                            // mapView.setScale(mapView.getMaxScale());
//                            isFirstLocate = false;
//                            if (mDestinationOrLastTimeSelectLocation == null){//只有当没有传递过来地址的是才去请求当前地址做为目标地址
//                                if (mPresenter != null){
//                                    mPresenter.requestLocation(location.getLongitude(),location.getLatitude(),mMapView.getSpatialReference());
//                                }
//                                //requestAddress(location.getLatitude(),location.getLongitude());
//                                mMapView.centerAt(point,true);
//                            }
//                            stopLocate(); //当第一次定位结束后，自动停止定位，除非用户手动发起定位
//                        }
//                    }else {
//                        ToastUtil.shortToast(mContext,"当前位置不在地图范围内");
//                        stopLocate(); //当第一次定位结束后，自动停止定位，除非用户手动发起定位
//                    }
//
//                }
//
//                @Override
//                public void onStatusChanged(String provider, int status, Bundle extras) {
//
//                }
//
//                @Override
//                public void onProviderEnabled(String provider) {
//
//                }
//
//                @Override
//                public void onProviderDisabled(String provider) {
//
//                }
//            });
//        }
//    }

    /**
     * 在地图上绘制当前位置
     *
     * @param location
     * @return
     */
    private void drawLocationOnMap(Location location) {
        Point point = new Point(location.getLongitude(), location.getLatitude());
        mGLayerFroDrawLocation.removeAll();
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mContext,
                mContext.getResources().getDrawable(R.mipmap.patrol_location_symbol));
        Graphic graphic = new Graphic(new Point(location.getLongitude(), location.getLatitude()), pictureMarkerSymbol);
        mGLayerFroDrawLocation.addGraphic(graphic);
    }

    /**
     * 停止定位
     */
    @Override
    public void stopLocate() {
        if (mLocationManager != null) {
            mLocationManager.stopLocate();
        }
    }

    @Override
    public void addViewToContaner() {
        mContainer.removeAllViews();
        mContainer.addView(mRoot);
    }


    protected void initView() {

        mRoot = View.inflate(mContext, R.layout.activity_map_patrol, null);

        mTv_address = (TextView) mRoot.findViewById(R.id.tv_map_address);
        mTv_address_poi = (TextView) mRoot.findViewById(R.id.tv_map_address_poi);

        mTv_title = (TextView) mRoot.findViewById(R.id.tv_title);
        //定位图标
        mIv_location = (ImageView) mRoot.findViewById(R.id.iv_location);

        mMapView = (MapView) mRoot.findViewById(R.id.mapview);
        mMapView.setMapBackground(Color.WHITE, Color.TRANSPARENT, 0f, 0f);//设置地图背景色、去掉网格线

        //“到这里去”按钮
        mBtn_go_to_destination = (TextView) mRoot.findViewById(R.id.btn_go_to_destination);

        //“确定选点”按钮
        mBtn_finish_select_point = (TextView) mRoot.findViewById(R.id.btn_finish_select_point);

        mLl_target = mRoot.findViewById(R.id.ll_btn_target);

        if (!SelectLocationConstant.ifLocateByBlueTooth) {
            mLl_target.setVisibility(View.GONE);
        }

        locationButton = (LocationButton) mRoot.findViewById(R.id.locationButton);
        locationButton.setMapView(mMapView);

        // ib_location = (ImageButton) mRoot.findViewById(R.id.btn_location);

        btn_back = mRoot.findViewById(R.id.btn_back);

        ib_zoom_in = mRoot.findViewById(R.id.ib_zoom_in);

        ib_zoom_out = mRoot.findViewById(R.id.ib_zoom_out);
    }


    /**
     * 加载地图
     */
    @Override
    public void loadMap() {
        layerView = new PatrolLayerView2(mContext, mMapView, null);
        mMapView.setOnTouchListener(new MapOnTouchListener(mContext, mMapView) {

            private boolean shouldBounce = false; //是否应该跳动

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        shouldBounce = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (shouldBounce) {
                            doBounceAnimation(mIv_location);
                        }
                        shouldBounce = false;
                        Point point = mMapView.toMapPoint(mIv_location.getX(), mIv_location.getY());
                        if (!mIfReadOnly && point != null) {
                            if (mPresenter != null) {
                                mPresenter.requestLocation(point.getX(), point.getY(), mMapView.getSpatialReference());
                            }
                        }
                        break;
                    default:
                        break;
                }
                return super.onTouch(v, event);
            }

            @Override
            public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
                if (locationButton != null && locationButton.ifLocating()) {
                    locationButton.followLocation();
                }
                return super.onDragPointerMove(from, to);
            }
        });
        layerPresenter = new PatrolLayerPresenter(layerView);
        layerPresenter.loadLayer();
    }


    /**
     * 定位图标动画
     *
     * @param targetView
     */
    private void doBounceAnimation(View targetView) {
        if (targetView.getVisibility() == View.VISIBLE) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationY", 0, -25, 0);
            animator.setInterpolator(new EasingInterpolator(Ease.ELASTIC_IN_OUT));
            animator.setDuration(1000);
            animator.start();
        }
    }


    @Override
    public void showAddress(String address) {

        mTv_address.setText(address);

        mTv_address_poi.setText(address);
    }


    /**
     * 当当前位置找不到位置信息
     */
    @Override
    public void showAddressNotFound() {

        mTv_address.setText("未知地址");
        mTv_address_poi.setText("未知地址");
    }

    @Override
    public void setIfReadOnly(boolean mIfReadOnly) {
        this.mIfReadOnly = mIfReadOnly;
        if (mIfReadOnly) {
            //如果是只读状态
            mBtn_finish_select_point.setVisibility(View.GONE);
            mBtn_go_to_destination.setVisibility(View.VISIBLE);
            mIv_location.setVisibility(View.GONE);
            tv_map_manage.setVisibility(View.GONE);
            mTv_title.setText("上报位置查看");
        }
    }

    @Override
    public void setDestinationOrLastTimeSelectLocation(LatLng mDestinationOrLastTimeSelectLocation) {
        this.mDestinationOrLastTimeSelectLocation = mDestinationOrLastTimeSelectLocation;
    }

    @Override
    public void setLastSelectedAddress(String mLastSelectedAddress) {
        this.mLastSelectedAddress = mLastSelectedAddress;
    }

    @Override
    public void setInitialScale(double mInitialScale) {
        this.mInitialScale = mInitialScale;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMapLocateEvent(MapLocateEvent event) {
        //地图定位
        if (mMapView != null && event.getEvelope() != null) {
//            if(mOfflineGraphicsLayer == null){
//                mOfflineGraphicsLayer = new GraphicsLayer();
//                mMapView.addLayer(mOfflineGraphicsLayer);
//            }
//            mOfflineGraphicsLayer.removeAll();
//            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#FFFF00"),
//                    2, SimpleLineSymbol.STYLE.SOLID);
//            Graphic graphicBuffer = new Graphic(event.getEvelope(), lineSymbol);
//            mOfflineGraphicsLayer.addGraphic(graphicBuffer);
            mMapView.setExtent(event.getEvelope());
        }
    }

    @Override
    public void showLayerList(ViewGroup container) {

        if (layerView != null) {
            layerView.setContainer(container);
        }

        if (layerPresenter != null) {
            layerPresenter.showLayerList();
        }
//        PatrolLayerView layerView = new PatrolLayerView(mContext,mMapView,container);
//        PatrolLayerPresenter layerPresenter = new PatrolLayerPresenter(layerView);
//        layerPresenter.showLayerList();
    }


}
