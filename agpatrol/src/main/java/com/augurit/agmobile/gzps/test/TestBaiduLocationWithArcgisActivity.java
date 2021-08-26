package com.augurit.agmobile.gzps.test;

import android.graphics.Color;
import com.augurit.agmobile.gzps.BaseActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView2;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

import static com.augurit.agmobile.gzps.R.id.map;


public class TestBaiduLocationWithArcgisActivity extends BaseActivity {

    private MapView mMapView;
    private GraphicsLayer gLayerGps;
    //提示信息
    private Toast toast;

    private ILayerView layerView;

    private PatrolLayerPresenter layerPresenter;

    protected ProgressLinearLayout progress_linearlayout;

    private Point wgspoint;
    private Point mapPoint;

    //定位SDK的核心类
    private LocationClient mLocClient;
    //是否手动触发请求定位
    private boolean isRequest = true;
    //是否首次定位
    private boolean isFirstLoc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_baidu_location_with_arcgis);

        mMapView = (MapView)findViewById(R.id.mapview);
        progress_linearlayout = (ProgressLinearLayout) findViewById(R.id.progress_linearlayout);


        layerView = new PatrolLayerView2(this, mMapView, progress_linearlayout);
        layerPresenter = new PatrolLayerPresenter(layerView);
        layerPresenter.loadLayer();

        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(status == STATUS.INITIALIZED) {
                    gLayerGps = new GraphicsLayer();
                    mMapView.addLayer(gLayerGps);
                }

            }
        });

        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(new BDLocationListener(){
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location == null)
                {
                    return;
                }

                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                if (location.getLocType() == BDLocation.TypeGpsLocation)
                {
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());

                }
                else if (location.getLocType() == BDLocation.TypeNetWorkLocation)
                {
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());

                }

                Log.e("log", sb.toString());

                if(isFirstLoc || isRequest)
                {
                    isRequest = false;
                }
                isFirstLoc = false;
                double lat=location.getLatitude();
                double lon=location.getLongitude();
                ShowGpsOnMap(lon,lat);
            }
        });//注册定位监听接口

        /**
         * LocationClientOption 该类用来设置定位SDK的定位方式。
         */
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); //打开GPRS
        option.setAddrType("all");//返回的定位结果包含地址信息
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setPriority(LocationClientOption.GpsFirst); // 设置GPS优先
        option.setScanSpan(1000); //设置发起定位请求的间隔时间为1s
        option.disableCache(true);//启用缓存定位
        mLocClient.setLocOption(option);  //设置定位参数

        //开始定位按钮
        Button btnGPS=(Button)findViewById(R.id.btnStartGps);
        btnGPS.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mLocClient.start();//调用此方法开始定位
                requestLocation();
            }
        });
    }


    public void requestLocation()
    {
        isRequest = true;
        if(mLocClient != null && mLocClient.isStarted())
        {
            showToast("GPS正在定位……");
            mLocClient.requestLocation();
        }
        else
        {
            Log.d("log", "locClient is null or not started");
        }
    }

    private void showToast(String msg)
    {
        if(toast == null)
        {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        }
        else
        {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
    /**
     * 将GPS点标注在地图上
     */
    public void ShowGpsOnMap(double lon,double lat){
        //清空定位图层
        gLayerGps.removeAll();
        //接收到的GPS的信号X(lat),Y(lon)
        double locx = lon;
        double locy = lat;
        wgspoint = new Point(locx, locy);
        mapPoint = (Point) GeometryEngine.project(wgspoint, SpatialReference.create(4326),mMapView.getSpatialReference());
        //图层的创建
        Graphic graphic = new Graphic(mapPoint,new SimpleMarkerSymbol(Color.RED,18, SimpleMarkerSymbol.STYLE.CIRCLE));
        gLayerGps.addGraphic(graphic);
    }
}
