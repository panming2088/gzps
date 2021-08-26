package com.augurit.agmobile.patrolcore.selectdevice.view;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.identify.service.IIdentifyService;
import com.augurit.agmobile.mapengine.identify.util.IdentifyServiceFactory;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerPresenter;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.patrolcore.layer.service.PatrolLayerService;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView;
import com.augurit.agmobile.patrolcore.selectdevice.model.OnSelectDeviceFinishEvent;
import com.augurit.agmobile.patrolcore.selectdevice.utils.SelectDeviceConstant;
import com.augurit.agmobile.patrolcore.selectdevice.utils.SelectDeviceUtil;
import com.augurit.agmobile.patrolcore.selectlocation.model.Ease;
import com.augurit.agmobile.patrolcore.selectlocation.model.EasingInterpolator;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.loc.WGS84LocationTransform;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.augurit.am.fw.utils.file.SharedPreferencesUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.geocode.Locator;
import com.augurit.agmobile.patrolcore.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.map.view.selectdevice
 * @createTime 创建时间 ：2017-03-29
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-29
 * @modifyMemo 修改备注：
 */
//todo xcl 之后要把它跟SelectLocationActivity抽离出公共部分，两个的重复代码太多
public class SelectDeviceFragment extends Fragment{


    public static final String TAG = "巡查模块--地图";
    private PatrolLocationManager mLocationManager;

    private String mWkt ;
    private Geometry mGeometry;

    //是否启动了定位
    private boolean ifLocated = true;
    //是否是第一次定位
    private boolean isFirstLocate = true;
    //是否用户手动发出定位请求
    private boolean ifStartLocateByUser = false ;
    //是否处于跟随模式
    private boolean ifInFollowMode = false ;

    private ImageView mIv_location;

    private MapView mMapView;

    private Locator mLocator;

    private double mInitialScale = -1;

    private GraphicsLayer mLayerForDrawDevice;
    protected View mView_selectdevice_mask;
    protected View mIv_finger;
    protected View mTv_select_device_tip;
    protected TextView mTv_title;

    public static final String HAS_ENTER_SELECT_DEVICE_BEFORE = "HAS_ENTER_SELECT_DEVICE_BEFORE" ;//是否是第一次进入这个界面
    private GraphicsLayer mGLayerFroDrawLocation;

    public static SelectDeviceFragment newInstance(String wkt,String deviceName,String title){
        Bundle bundle = getBundle(wkt, deviceName,title);
        SelectDeviceFragment fragment = new SelectDeviceFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 构造bundle
     * @param wkt 设施的wkt
     * @param deviceName
     * @return
     */
    @NonNull
    protected static Bundle getBundle(String wkt, String deviceName,String title) {
        Bundle bundle = new Bundle();
        bundle.putString(SelectDeviceConstant.DEVICE_WKT,wkt);
        putCommonPartToBundle(deviceName, title, bundle);
        return bundle;
    }

    /**
     * 设置公共的信息
     * @param deviceName
     * @param title
     * @param bundle
     */
    private static void putCommonPartToBundle(String deviceName, String title, Bundle bundle) {
        bundle.putString(SelectDeviceConstant.DEVICE_NAME,deviceName);
        bundle.putString(SelectDeviceConstant.DEVICE_KEY_NAME,title);
    }

    public static SelectDeviceFragment newInstance(Geometry geometry,String deviceName,String title){
        Bundle bundle = getBundle(geometry, deviceName,title);
        SelectDeviceFragment fragment = new SelectDeviceFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 构造bundle
     * @param geometry  设施的geometry
     * @param deviceName
     * @return
     */
    @NonNull
    protected static Bundle getBundle(Geometry geometry, String deviceName,String title) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SelectDeviceConstant.DEVICE_GEOMETRY,geometry);
        putCommonPartToBundle(deviceName, title, bundle);
        return bundle;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_selectdevice,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        initDatas();

        initListener(view);
    }
    private void initListener(View root) {
        //定位按钮
        final ImageButton ib_location  = (ImageButton) root.findViewById(R.id.btn_location);
        root.findViewById(R.id.ll_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followLocation(ib_location);
                // startOrStopLocate(ib_location, mMapView);
            }
        });

        ib_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followLocation(ib_location);
                //startOrStopLocate(ib_location, mMapView);
            }
        });


        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (status == STATUS.LAYER_LOADED && isFirstLocate){ //当加载地图完成时进行定位
                    //如果有传递坐标过来，进行绘制
                 //  drawDestinationOnMap(mIv_location, mMapView);
                    startLocate(mMapView);
                    if (!TextUtils.isEmpty(PatrolLayerService.MAP_EXTENT)){
                        String[] extents = PatrolLayerService.MAP_EXTENT.split(",");
                        double[] longExtents = new double[extents.length + 1];
                        for (int j = 0; j < extents.length; j++) {
                            longExtents[j] = Double.valueOf(extents[j]);
                        }
                        Envelope envelope = new Envelope(longExtents[0], longExtents[1], longExtents[2], longExtents[3]);
                        mMapView.setMaxExtent(envelope);
                    }
                   // Envelope envelope = new Envelope(-180,-90,180,90);
                   // mMapView.setMaxExtent(envelope);

                    if (mLayerForDrawDevice == null){
                        mLayerForDrawDevice = new GraphicsLayer(GraphicsLayer.RenderingMode.STATIC);
                        mMapView.addLayer(mLayerForDrawDevice);

                        if (mGeometry != null){
                            highLight(mGeometry);
                        }
                    }
                }
            }
        });

        //查看图层目录
        root.findViewById(R.id.btn_show_layerDir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SelectDeviceActivity)getActivity()).openDrawer();
            }
        });

        root.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitActivity();
            }
        });

        //放大缩小按钮
        //放大缩小
        root.findViewById(R.id.ib_zoom_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView.zoomin();

            }
        });

        root.findViewById(R.id.ib_zoom_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView.zoomout();
            }
        });

        mView_selectdevice_mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideTip();
            }
        });
        mTv_select_device_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideTip();
            }
        });

        mIv_finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideTip();
            }
        });

        setMapOnTapListener();

    }

    private void hideTip() {
        mView_selectdevice_mask.setVisibility(View.GONE);
        mTv_select_device_tip.setVisibility(View.GONE);
        mIv_finger.setVisibility(View.GONE);
    }

    protected void setMapOnTapListener() {
        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
          @Override
          public void onSingleTap(float v, float v1) {

              final ProgressDialog progressDialog = new ProgressDialog(getActivity());
              progressDialog.setMessage("查询周围设施中...");
              //progressDialog.setCancelable(false);
              progressDialog.show();
              final Point point = mMapView.toMapPoint(v,v1);
              IIdentifyService identifyService = IdentifyServiceFactory.provideLayerService();
              identifyService.selectedFeature(getActivity(), mMapView,
                      LayerServiceFactory.provideLayerService(getActivity()).getVisibleQueryableLayers(), point, 25, new Callback2<AMFindResult[]>() {
                          @Override
                          public void onSuccess(final AMFindResult[] amFindResults) {
                              progressDialog.dismiss();
                              if (amFindResults != null && amFindResults.length >= 1){
                                  Geometry geometry = amFindResults[0].getGeometry();
                                  Callout callout = mMapView.getCallout();
                                  View view = View.inflate(getActivity(), R.layout.callout_select_device,null);
                                  ((TextView)view.findViewById(R.id.tv_listcallout_title)).setText(amFindResults[0].getValue());
                                  view.findViewById(R.id.btn_select_device).setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {

                                          String id = SelectDeviceUtil.getIdFromAMFindResult(amFindResults[0]);
                                          EventBus.getDefault().post(new OnSelectDeviceFinishEvent(
                                                  amFindResults[0].getValue(),id));

                                          /*if(amFindResults[0].getAttributes().get("OBJECTID_1")!= null){
                                              String objectid1 = amFindResults[0].getAttributes().get("OBJECTID_1").toString();
                                              EventBus.getDefault().post(new OnSelectDeviceFinishEvent(
                                                      amFindResults[0].getValue(),objectid1));
                                          }else if (amFindResults[0].getAttributes().get("OBJECTID")!= null){
                                              String objectid1 = amFindResults[0].getAttributes().get("OBJECTID").toString();
                                              EventBus.getDefault().post(new OnSelectDeviceFinishEvent(
                                                      amFindResults[0].getValue(),objectid1));
                                          }else if (amFindResults[0].getAttributes().get("唯一标识")!= null){
                                              String objectid1 = amFindResults[0].getAttributes().get("唯一标识").toString();
                                              EventBus.getDefault().post(new OnSelectDeviceFinishEvent(
                                                      amFindResults[0].getValue(),objectid1));
                                          }else if (amFindResults[0].getAttributes().get("FID")!= null){
                                              String objectid1 = amFindResults[0].getAttributes().get("FID").toString();
                                              EventBus.getDefault().post(new OnSelectDeviceFinishEvent(
                                                      amFindResults[0].getValue(),objectid1));
                                          }else {
                                              EventBus.getDefault().post(new OnSelectDeviceFinishEvent(
                                                      amFindResults[0].getValue(),"-1"));
                                          }*/
                                          //退出
                                          exitActivity();
                                      }
                                  });
                                  callout.setContent(view);
                                  if (geometry.getType() == Geometry.Type.POINT){
                                      callout.show((Point)geometry);
                                  }else {
                                      callout.show(point);
                                  }
                                  highLight(geometry);
                              }else {
                                  Callout callout = mMapView.getCallout();
                                  View view = View.inflate(getActivity(),R.layout.listcallout_text_view,null);
                                 // view.findViewById(R.id.btn_select_device).setVisibility(View.GONE);
                                  ((TextView)view.findViewById(R.id.tv_listcallout_title)).setText("当前位置无设施");
                                  callout.setContent(view);
                                  callout.show(point);
                              }
                          }

                          @Override
                          public void onFail(Exception error) {
                              progressDialog.dismiss();
                              ToastUtil.shortToast(getActivity(),"抱歉，附近查无设施");
                          }
                      });
          }
      });
    }

    private void highLight(Geometry geometry) {
        Symbol symbol;
        switch (geometry.getType()) {
            case POINT:
            case MULTIPOINT:
                symbol = new SimpleMarkerSymbol(ContextCompat.getColor(getActivity(),R.color.patrol_identify_point_color), 8, SimpleMarkerSymbol.STYLE.CIRCLE);
                break;
            case LINE:
            case POLYLINE:
                symbol = new SimpleLineSymbol(ContextCompat.getColor(getActivity(), R.color.patrol_identify_line_color), 8, SimpleLineSymbol.STYLE.SOLID);
                break;
            case POLYGON:
                SimpleLineSymbol line = new SimpleLineSymbol(ContextCompat.getColor(getActivity(),R.color.patrol_identify_line_color), 8, SimpleLineSymbol.STYLE.SOLID);
                symbol = new SimpleFillSymbol(ContextCompat.getColor(getActivity(), R.color.patrol_identify_polygon_color));
                ((SimpleFillSymbol) symbol).setOutline(line);
                break;
            case UNKNOWN:
            case ENVELOPE:
            default:
                return;
        }

        Graphic graphic = new Graphic(geometry, symbol);
        mLayerForDrawDevice.setRenderer(new SimpleRenderer(symbol));
        mLayerForDrawDevice.removeAll();
        int i = mLayerForDrawDevice.addGraphic(graphic);
        int[] ids = new int[]{i};
        mLayerForDrawDevice.setSelectedGraphics(ids, true);
    }


    private void initDatas() {
        //是否允许编辑位置,默认允许
        mGeometry = (Geometry) getArguments().getSerializable(SelectDeviceConstant.DEVICE_GEOMETRY);
        mWkt = getArguments().getString(SelectDeviceConstant.DEVICE_WKT);

        //设置标题
        String title = getArguments().getString(SelectDeviceConstant.DEVICE_KEY_NAME);
        setTitle(title);

        //进行加载图层
        loadLayer(mIv_location, mMapView);

        mLocator = Locator.createOnlineLocator("http://192.168.19.96:6080/arcgis/rest/services/yuanqu/address/GeocodeServer");

        mIv_location.setVisibility(View.GONE);

        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getActivity());
        sharedPreferencesUtil.setBoolean(HAS_ENTER_SELECT_DEVICE_BEFORE,false);
    }

    protected void setTitle(String title) {
        if (!TextUtils.isEmpty(title)){
            mTv_title.setText(getString(R.string.select_device,title));
        }
    }

    private void initView(View root) {

        //定位图标
        mIv_location = (ImageView) root.findViewById(R.id.iv_location);

        mMapView = (MapView) root.findViewById(R.id.mapview);
        mMapView.setMapBackground(Color.WHITE, Color.TRANSPARENT, 0f, 0f);//设置地图背景色、去掉网格线
        mMapView.setShowMagnifierOnLongPress(true);

        mView_selectdevice_mask = root.findViewById(R.id.view_selectdevice_mask);
        mIv_finger = root.findViewById(R.id.iv_finger);
        mTv_select_device_tip = root.findViewById(R.id.tv_select_device_tip);

        //是否应该显示提示
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getActivity());
        boolean ifFirstEnter = sharedPreferencesUtil.getBoolean(HAS_ENTER_SELECT_DEVICE_BEFORE, true);

        if (ifFirstEnter){
            mView_selectdevice_mask.setVisibility(View.VISIBLE);
            mIv_finger.setVisibility(View.VISIBLE);
            mTv_select_device_tip.setVisibility(View.VISIBLE);
        }

        mTv_title = (TextView) root.findViewById(R.id.tv_title);

    }

    private void exitActivity() {
        //  mMapPresenter.exit();
        this.getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_from_lefthide_to_rightshow,R.anim.slide_from_leftshow_to_righthide);
    }


    private void startOrStopLocate(ImageButton iv_location, MapView mapView) {
        if (ifLocated){
            stopLocate();
            ifLocated = false;
            iv_location.setImageResource(R.mipmap.common_ic_target);
        }else {
            ifStartLocateByUser = true;
            startLocate(mapView);
            ifLocated = true;
            iv_location.setImageResource(R.mipmap.common_ic_location_follow);
        }
    }

    private void followLocation(ImageButton iv_location){
        if (ifInFollowMode){
            //   stopLocate();
            ifInFollowMode = false;
            iv_location.setImageResource(R.mipmap.common_ic_target);
        }else {
            ifInFollowMode = true;
            // mMapView.setRotation(90);
            //  startLocate(mapView);
            //  ifLocated = true;
            iv_location.setImageResource(R.mipmap.common_ic_location_follow);
        }

    }


    private void zoomToBestScale() {
        //缩放到合适比例
        if (mInitialScale != -1){
            mMapView.setScale(mInitialScale);
        }
    }

    /**
     * 加载地图
     * @param iv_location
     * @param mapView
     */
//    @NeedPermission(permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void loadLayer(final ImageView iv_location, final MapView mapView) {
        /*PermissionsUtil2.getInstance()
                .requestPermissions(
                        SelectDeviceFragment.this,
                        "需要存储权限才能正常工作，请点击确定允许", 101,
                        new PermissionsUtil2.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                loadLayerWithCheck(iv_location, mapView);
                            }
                        },
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);*/
        PermissionsUtil.getInstance()
                .requestPermissions(
                        SelectDeviceFragment.this,
                        new PermissionsUtil.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                loadLayerWithCheck(iv_location, mapView);
                            }

                            @Override
                            public void onPermissionsDenied(List<String> perms) {

                            }
                        },
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void loadLayerWithCheck(final ImageView iv_location, final MapView mapView) {
        // ILayersService layersService = new PatrolLayerSercice(getActivity());
        ILayerView layerView = new PatrolLayerView(getActivity(),mapView,null);
        ILayerPresenter layerPresenter = new PatrolLayerPresenter(layerView);
        layerPresenter.loadLayer();
    }

  /*  private void requestAddress() {
        PatrolMapService patrolSelectDeviceService = new PatrolMapService(getActivity(),null,null);
        patrolSelectDeviceService.parseLocationByBaidu(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaiduGeocodeResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mTv_address.setText("未知地址");
                        mTv_address_poi.setText("未知地址");
                    }

                    @Override
                    public void onNext(BaiduGeocodeResult baiduGeocodeResult) {
                        //改变文字
                        String address = baiduGeocodeResult.getResult().getFormatted_address();
                        int provinceLocation = address.indexOf("市");
                        String formattedAddress = address.substring(provinceLocation +1);
                        mTv_address.setText(getResources().getString(R.string.the_current_address_is_near,formattedAddress));

                        mTv_address_poi.setText(getResources().getString(R.string.the_current_address_is_near,baiduGeocodeResult.getResult().getPois().get(0).getName()));
                    }
                });
    }*/

    /**
     * 开启定位
     * @param mapView
     */
    private void startLocate(final MapView mapView) {
        if (mLocationManager == null){
            mLocationManager = new PatrolLocationManager(getActivity(),mMapView);
           // mLocationManager.setUseArcGisForLocation();
            mLocationManager.setCoordinateSystem(new WGS84LocationTransform());
        }

        mLocationManager.startLocate(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LogUtil.e(TAG,"定位到的坐标是："+location.getLatitude() +"-->"+ location.getLongitude());
                Point point = new Point(location.getLongitude(),location.getLatitude());
                if (GeometryEngine.contains(mMapView.getMaxExtent(),point,mMapView.getSpatialReference())){
                    Callout callout = mMapView.getCallout();
                    TextView textView = new TextView(getActivity());
                    textView.setText("当前所在位置");
                    callout.setContent(textView);
                    callout.show(point);
                    if (isFirstLocate){
                        //放大到最大，并且居中
                        mMapView.setScale(mMapView.getMaxScale());
                        mMapView.centerAt(point,true);
                        // stopLocate(); //只定位一次
                        isFirstLocate = false;
                    }
                }else {
                    ToastUtil.shortToast(getActivity(),"当前位置不在地图范围内");
                }
            }


            /**
             * 在地图上绘制当前位置
             * @param location
             * @return
             */
            @NonNull
            private Point drawLocationOnMap(Location location) {
                Point point = new Point(location.getLongitude(),location.getLatitude());
                mGLayerFroDrawLocation.removeAll();
                PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(getActivity(),
                        getResources().getDrawable(R.mipmap.patrol_location_symbol));
                Graphic graphic = new Graphic(new Point(location.getLongitude(),location.getLatitude()), pictureMarkerSymbol);
                mGLayerFroDrawLocation.addGraphic(graphic);
                return point;
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

    private void addDrawLocationGraphicLayer() {
        if (mGLayerFroDrawLocation == null){
            mGLayerFroDrawLocation = new GraphicsLayer();
            mMapView.addLayer(mGLayerFroDrawLocation);
        }
    }

    /**
     * 定位图标动画
     * @param targetView
     */
    private void doBounceAnimation(View targetView) {
        if (targetView.getVisibility() == View.VISIBLE){
            ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationY", 0, -25, 0);
            animator.setInterpolator(new EasingInterpolator(Ease.ELASTIC_IN_OUT));
            animator.setDuration(1000);
            animator.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mLocationManager != null){
            mLocationManager.stopLocate();
        }
    }



    public void stopLocate(){
        if(mLocationManager != null){
            mLocationManager.stopLocate();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
