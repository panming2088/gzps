package com.augurit.agmobile.gzps.track.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.track.service.TrackNetService;
import com.augurit.agmobile.gzps.track.service.TrackNewService;
import com.augurit.agmobile.gzps.track.service.TrackService;
import com.augurit.agmobile.gzps.track.util.DrawGPSHelper;
import com.augurit.agmobile.gzps.track.util.OnGPSPlayBackFinish;
import com.augurit.agmobile.gzps.track.util.OnTrackListener;
import com.augurit.agmobile.gzps.track.util.TrackConstant;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.agmobile.mapengine.gpsstrace.service.LocalTrackServiceImpl;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerPresenter;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.patrolcore.layer.service.AgwebPatrolLayerService2;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView2;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * 轨迹回放界面 + 轨迹定位界面
 *
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.view
 * @createTime 创建时间 ：2017-06-15
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-15
 * @modifyMemo 修改备注：
 */

public class ShowTrackActivity extends BaseActivity implements View.OnClickListener {

    private static final int STOP = 0;       //轨迹回放状态：停止
    private static final int PLAYING = 1;       //轨迹回放状态：回放中
    private static final int PAUSE = 2;       //轨迹回放状态：暂停
    GraphicsLayer graphicsLayer = new GraphicsLayer();
    List<GPSTrack> track = null;
    private MapView mMapView;
    private View ll_trackop;
    private Button track_start;
    private Button track_stop;
    private String trackId = null;
    private DrawGPSHelper mDrawGPSHelper;
    private TrackNewService mTrackService;
    private OnTrackListener mOnTrackListener;
    private ILayerPresenter layerPresenter;
    private int trackState = STOP;

    private ViewGroup ll_back;

    private String operation = TrackConstant.TRACK_LOCATE;   //可能值TRACK_LOCATE(只定位) TRACK_FOLLOW(定位且画出轨迹)  TRACK_VIEW(只查看轨迹)
    private Location lastLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_track);
        trackId = getIntent().getStringExtra("trackId");
        String operation = getIntent().getStringExtra("operation");
        if (operation != null) {
            this.operation = operation;
        } else {
            this.operation = TrackConstant.TRACK_LOCATE;
        }
        init();

        /*ViewGroup container = (ViewGroup) findViewById(R.id.container);
        IShowTrackView showTrackView = new ShowTrackView(this, container);
        IShowTrackPresenter showTrackPresenter = new ShowTrackPresenter(this, showTrackView, track);*/
    }

    private void init() {
//        mTrackService = TrackService.getInstance(this);

        if (trackId != null) {
            track = new LocalTrackServiceImpl().getGPSTracksByTrackId(trackId);
        }
        mOnTrackListener = new OnTrackListener() {
            @Override
            public void onTrack(GPSTrack gpsTrack) {
                mDrawGPSHelper.drawPointAndMark(gpsTrack, R.drawable.location_symbol);
            }

            @Override
            public void onTime(int second) {

            }

            @Override
            public void onLength(double length) {

            }
        };


        ll_back = (ViewGroup) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mMapView = (MapView) findViewById(R.id.mapview);

        ILayerView layerView = new PatrolLayerView2(this, mMapView, null);
        layerPresenter = new PatrolLayerPresenter(layerView, new AgwebPatrolLayerService2(getApplicationContext()));
        layerPresenter.loadLayer(new Callback2<Integer>() {
            @Override
            public void onSuccess(Integer integer) {

            }

            @Override
            public void onFail(Exception error) {
            }
        });

        ll_trackop = findViewById(R.id.ll_trackop);
        track_start = (Button) findViewById(R.id.track_start);
        track_stop = (Button) findViewById(R.id.track_stop);
        graphicsLayer = new GraphicsLayer();
//        TdtLayer tdtLayer = new TdtLayer(789, "http://services.tianditugd.com/gdvec2014/wmts",
//                new NonEncryptTileCacheHelper(this, String.valueOf(789)), true);
//        TdtLayer tdtLayer2 = new TdtLayer(456, "http://t0.tianditu.com/vec_c/wmts",
//                new NonEncryptTileCacheHelper(this, String.valueOf(456)), true);
//        TdtLayer tdtLayer3 = new TdtLayer(789, "http://services.tianditugd.com/gdvec2014/wmts",
//                new NonEncryptTileCacheHelper(this, String.valueOf(789)), true);
//        mMapView.addLayer(tdtLayer);
//        mMapView.addLayer(tdtLayer2);
//        mMapView.addLayer(tdtLayer3);
//
        track_start.setOnClickListener(this);
        track_stop.setOnClickListener(this);
        mDrawGPSHelper = new DrawGPSHelper(this, mMapView);

        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {

                if (status == STATUS.INITIALIZED) {
                    mMapView.addLayer(graphicsLayer);

                    if (operation.equals(TrackConstant.TRACK_FOLLOW)) {

                        if (mTrackService == null) {
                            Intent intent = new Intent(ShowTrackActivity.this, TrackNewService.class);
                            bindService(intent, conn, Context.BIND_AUTO_CREATE);
                        } else {

                            List<GPSTrack> gpsTracks = mTrackService.getmGPSTracks();
                            mDrawGPSHelper.drawGPSTrackOnMap(gpsTracks);
                            mDrawGPSHelper.markStartPoint(R.drawable.track_start);
                            mDrawGPSHelper.drawPointAndMark(gpsTracks.get(gpsTracks.size() - 1), R.drawable.location_symbol);
                            mTrackService.addLocationListener(mOnTrackListener);
                        }

                    } else if (operation.equals(TrackConstant.TRACK_VIEW)) {
                        if (ListUtil.isEmpty(track)) {
                            ToastUtil.shortToast(ShowTrackActivity.this, "无法获取轨迹记录，定位到当前位置");
                            locate();
                            return;
                        }
                        ll_trackop.setVisibility(View.VISIBLE);
                        GPSTrack gpsTrack = track.get(0);
                        Point point = new Point(gpsTrack.getLongitude(), gpsTrack.getLatitude());
                        //生成缓冲区
                        Geometry bufferGeometry = GeometryEngine.buffer(point, SpatialReference.create(4326),
                                200, null);
                        mMapView.setExtent(bufferGeometry);
                        mDrawGPSHelper.drawGPSTrackOnMap(track);
                        mDrawGPSHelper.markStartPoint(R.drawable.track_start);
                        mDrawGPSHelper.markEndPoint(R.drawable.track_end);
                    } else if (operation.equals(TrackConstant.TRACK_FROM_NET)) {
                        getGPSTrackListFromNet();
                    } else {
                        locate();
                    }
                }
            }
        });

    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mTrackService = ((TrackNewService.MyBinder) service).getService();

            List<GPSTrack> gpsTracks = mTrackService.getmGPSTracks();
            mDrawGPSHelper.drawGPSTrackOnMap(gpsTracks);
            mDrawGPSHelper.markStartPoint(R.drawable.track_start);
            mDrawGPSHelper.drawPointAndMark(gpsTracks.get(gpsTracks.size() - 1), R.drawable.location_symbol);
            mTrackService.addLocationListener(mOnTrackListener);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mTrackService = null;
        }
    };


    private void getGPSTrackListFromNet() {
        new TrackNetService(this).getTracePointsByTraceId(Long.valueOf(trackId))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result2<List<GPSTrack>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.shortToast(ShowTrackActivity.this, "无法获取轨迹记录，请重试");
                    }

                    @Override
                    public void onNext(Result2<List<GPSTrack>> listResult) {
                        track = listResult.getData();
                        if (ListUtil.isEmpty(track)) {
                            ToastUtil.shortToast(ShowTrackActivity.this, "无法获取轨迹记录，定位到当前位置");
                            locate();
                            return;
                        }
                        ll_trackop.setVisibility(View.VISIBLE);
                        GPSTrack gpsTrack = track.get(0);
                        Point point = new Point(gpsTrack.getLongitude(), gpsTrack.getLatitude());
                        //生成缓冲区
                        Geometry bufferGeometry = GeometryEngine.buffer(point, SpatialReference.create(4326),
                                500, null);
                        mMapView.setExtent(bufferGeometry);
                        mDrawGPSHelper.drawGPSTrackOnMap(track);
                        mDrawGPSHelper.markStartPoint(R.drawable.track_start);
                        mDrawGPSHelper.markEndPoint(R.drawable.track_end);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.track_start) {
            if (trackState == STOP) {
                track_start.setText("暂停");
                track_stop.setEnabled(true);
                trackState = PLAYING;
                mDrawGPSHelper.playRoute(new OnGPSPlayBackFinish() {
                    @Override
                    public void finish() {
                        track_start.setText("回放");
                        track_stop.setEnabled(false);
                        trackState = STOP;
                    }
                });
            } else if (trackState == PAUSE) {
                trackState = PLAYING;
                mDrawGPSHelper.continuePlay();
            } else if (trackState == PLAYING) {
                track_start.setText("继续");
                trackState = PAUSE;
                mDrawGPSHelper.pausePlay();
            }
        } else if (id == R.id.track_stop) {
            track_start.setText("回放");
            track_stop.setEnabled(false);
            trackState = STOP;
            mDrawGPSHelper.stopPlay();
        }
    }

   /* public void showTrack(List<GPSTrack> track) {
        if(ListUtil.isEmpty(track)){
            locate();
            return;
        }
        Polyline line = new Polyline();
        GPSTrack firstGPSTrack = track.get(0);
        Point firstPoint = new Point(firstGPSTrack.getLongitude(), firstGPSTrack.getLatitude());
        line.startPath(firstPoint);
        for(GPSTrack gpsTrack : track){
            Point point = new Point(gpsTrack.getLongitude(), gpsTrack.getLatitude());
            line.lineTo(point);
        }
        GraphicsLayer graphicsLayer = new GraphicsLayer();
        Graphic graphic = new Graphic(line, new SimpleLineSymbol(Color.RED, 10));
        int i = graphicsLayer.addGraphic(graphic);
        mMapView.addLayer(graphicsLayer);
        mMapView.setExtent(line);
    }*/

    private void showLocation() {
        TrackService trackService = TrackService.getInstance(this);
        trackService.addLocationListener(new OnTrackListener() {
            @Override
            public void onTrack(GPSTrack gpsTrack) {
                Graphic graphic = new Graphic(new Point(gpsTrack.getLongitude(), gpsTrack.getLatitude()), new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE));
                graphicsLayer.removeAll();
                int i = graphicsLayer.addGraphic(graphic);
                mMapView.centerAt(new Point(gpsTrack.getLongitude(), gpsTrack.getLatitude()), true);
            }

            @Override
            public void onTime(int second) {

            }

            @Override
            public void onLength(double length) {

            }
        });
        trackService.startLocate();
    }

    private void locate() {
        LocationDisplayManager locationDisplayManager = mMapView.getLocationDisplayManager();
        locationDisplayManager.setLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Point point = new Point(location.getLongitude(), location.getLatitude());
                //生成缓冲区
                Geometry bufferGeometry = GeometryEngine.buffer(point, mMapView.getSpatialReference(),
                        0.005, null);
                //                Graphic graphic = new Graphic(bufferGeometry, new SimpleLineSymbol(Color.RED, 4));
                //                int i = graphicsLayer.addGraphic(graphic);
                if (lastLocation == null
                        || (lastLocation.getLongitude() != location.getLongitude()
                        || lastLocation.getLatitude() != location.getLatitude())) {
                    mMapView.setExtent(bufferGeometry);
                }

                lastLocation = location;
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
        locationDisplayManager.start();
    }

//    @Override
//    public void onBackPressed() {
//
//    }

    @Override
    protected void onDestroy() {

        if (mTrackService != null
                && mOnTrackListener != null) {
            mTrackService.removeLocationListener(mOnTrackListener);
        }

        if (mTrackService != null) {
            unbindService(conn);
        }

        super.onDestroy();
    }
}
