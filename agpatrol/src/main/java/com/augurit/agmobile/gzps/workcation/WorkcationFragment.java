package com.augurit.agmobile.gzps.workcation;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.LazyLoadFragment;
import com.augurit.agmobile.gzps.common.OnInitLayerUrlFinishEvent;
import com.augurit.agmobile.gzps.common.OnRefreshEvent;
import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.journal.RoutineInspectionActivity;
import com.augurit.agmobile.gzps.setting.MyUploadStatisticActivity;
import com.augurit.agmobile.gzps.track.view.TrackListActivity;
import com.augurit.agmobile.gzps.track.view.TrackRecordView;
import com.augurit.agmobile.gzps.track.view.presenter.TrackPresenter;
import com.augurit.agmobile.gzps.uploadevent.view.uploadevent.EventUploadActivity;
import com.augurit.agmobile.gzps.uploadfacility.UploadFacilityMapActivity;
import com.augurit.agmobile.gzps.workcation.view.LocalDraftActivity;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerPresenter;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.mapengine.legend.service.OnlineLegendService;
import com.augurit.agmobile.mapengine.legend.view.ILegendView;
import com.augurit.agmobile.patrolcore.common.legend.ImageLegendView;
import com.augurit.agmobile.patrolcore.common.legend.ImagePmLegendView;
import com.augurit.agmobile.patrolcore.common.legend.LegendPresenter;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.widget.LocationButton;
import com.augurit.agmobile.patrolcore.layer.service.AgwebPatrolLayerService2;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView2;
import com.augurit.agmobile.patrolcore.search.view.SearchFragmentWithoutMap;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by xcl on 2017/11/14.
 */

public class WorkcationFragment extends LazyLoadFragment {

    private MapView mapView;
    private PatrolLayerPresenter layerPresenter;
    private boolean loadLayersSuccess = true;
    private PatrolLocationManager mLocationManager;
    private boolean ifFirstLocate = false;
    private TextView tv_hint;
    private LocationManager lm;
    private LocationButton locationButton;
    private View ll_layer_url_init_fail;
    /**
     * 是否注册过
     */
    private boolean ifRegistered = false;
    private LegendPresenter legendPresenter;
    private View mRootView, rl_track;//轨迹容器
    private OnStatusChangedListener.STATUS mStatus;

    private TrackPresenter trackPresenter;
    private TrackRecordView mTrackView;

    public LocationButton getLocationButton() {
        return locationButton;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_work_station2;
    }

    //切换账号时停止定位轨迹
    public void finishTrackService() {
        if (trackPresenter != null) {
            trackPresenter.stopService();
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        this.mRootView = view;

        EventBus.getDefault().register(this);

        mapView = (MapView) view.findViewById(R.id.mapview);
        // setupWindowAnimations();

        mapView.setMapBackground(Color.WHITE, Color.TRANSPARENT, 0f, 0f);//设置地图背景色、去掉网格线

        ll_layer_url_init_fail = view.findViewById(R.id.ll_layer_url_init_fail);

        rl_track = view.findViewById(R.id.rl_track);

        //开始巡检
   /*     view.findViewById(R.id.iv_start_patrol).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), TrackRecordActivity.class);
//                startActivity(intent);
//                ToastUtil.shortToast(getActivity().getApplicationContext(), "建设中");

                initTrackView(mRootView);

                rl_track.setVisibility(View.VISIBLE);

                locationButton.setVisibility(View.GONE);

                locationButton.setStateNormal();
                locationButton.removeDrawLocation();

//                if (locationButton != null && !locationButton.ifLocating()) {
//                    locationButton.followLocation();
//                }

            }
        });*/
    //我的上报

        view.findViewById(R.id.iv_my_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyUploadStatisticActivity.class));
            }
        });


        //巡检日志
        view.findViewById(R.id.iv_patrol_journal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RoutineInspectionActivity.class);
//                Intent intent = new Intent(getActivity(), PfkJournalActivity.class);
                startActivity(intent);
                // ToastUtil.shortToast(getActivity().getApplicationContext(), "建设中");
            }
        });

        //问题上报
        view.findViewById(R.id.iv_upload_problem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableViewManager.isEditingFeatureLayer = false;
                Intent intent = null;
                //intent = new Intent(getActivity(), EditTableActivity.class);
                intent = new Intent(getActivity(), EventUploadActivity.class);
                intent.putExtra("projectId", SearchFragmentWithoutMap.PROJECT_ID);
                intent.putExtra("projectName", "问题上报");
                startActivity(intent);
            }
        });

        //本地草稿
        view.findViewById(R.id.iv_local_draft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                ToastUtil.shortToast(getActivity().getApplicationContext(),"建设中");
                Intent intent = new Intent(getActivity(), LocalDraftActivity.class);
                startActivity(intent);
            }
        });

        //历史记录
        view.findViewById(R.id.iv_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TrackListActivity.class);
                startActivity(intent);
//                ToastUtil.shortToast(getActivity().getApplicationContext(), "建设中");
            }
        });

        /**
         * 数据上报
         */
        view.findViewById(R.id.iv_upload_new_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), UploadFacilityTabActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), UploadFacilityMapActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 数据纠错 visibility="gone"
         */
        view.findViewById(R.id.iv_modify_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), ModifyFacilityTabActivity.class);
//                startActivity(intent);
//                Intent intent = new Intent(getActivity(), ModifyFacilityListActivity.class);
//                startActivity(intent);
            }
        });


        /**
         * 图例
         */
        View ll_legend = view.findViewById(R.id.ll_legend);
        // ll_legend.setVisibility(View.GONE);
        ll_legend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLegendPresenter();
                if (layerPresenter != null) {
                    legendPresenter.showLegends(layerPresenter.getService().getVisibleLayerInfos());
                } else {
                    legendPresenter.showLegends();
                }

            }
        });

//        /**
//         * 数据修正
//         */
//        view.findViewById(R.id.iv_amend_data).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Intent intent = new Intent(getActivity(), ComponentMaintenanceActivityBase.class);
//                //startActivity(intent);
//            }
//        });
//        /**
//         * 数据新增
//         */
//        view.findViewById(R.id.iv_add_new_data).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Intent intent = new Intent(getActivity(), AddComponentActivity2.class);
//                // startActivity(intent);
//            }
//        });

        /**
         * 定位
         */
        locationButton = (LocationButton) view.findViewById(R.id.locationButton);
        locationButton.setMapView(mapView);
        //locationButton.setUseArcgisLocation();

        //图层按钮
        view.findViewById(R.id.btn_layer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof IDrawerController) {
                    IDrawerController drawerController = (IDrawerController) activity;
                    drawerController.openDrawer(new IDrawerController.OnDrawerOpenListener() {
                        @Override
                        public void onOpened(View drawerView) {
                            showLayerList();
                        }
                    });

                }
            }
        });

        View ll_bottomsheet = view.findViewById(R.id.ll_bottomsheet);
        BottomSheetBehavior<View> from = BottomSheetBehavior.from(ll_bottomsheet);
        from.setState(BottomSheetBehavior.STATE_EXPANDED);

        tv_hint = (TextView) view.findViewById(R.id.tv_hint);
    }

    private void initTrackView(View view) {
        if (mTrackView == null) {
            mTrackView = new TrackRecordView(getActivity(), view);
            trackPresenter = new TrackPresenter(getActivity(), mTrackView);
            trackPresenter.setBackListener(new Callback1() {
                @Override
                public void onCallback(Object o) {

                    rl_track.setVisibility(View.GONE);
                    locationButton.setVisibility(View.VISIBLE);
                }
            });
        } else {
            trackPresenter.bindService();
        }
    }

    public void checkGPSState() {
        //得到系统的位置服务，判断GPS是否激活
        LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!ok) {
            ToastUtil.shortToast(getActivity().getApplicationContext(), "检测到未开启GPS定位服务");
        }
        //  boolean network = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //   boolean ok = gps || network;

//        if (ok) {
//            startLocate();
//            if (ifRegistered) {
//                unRegisterListener();
//                ifRegistered = false;
//            }
//        } else {
//            if (!ifRegistered) {
//                ToastUtil.shortToast(getActivity().getApplicationContext(), "系统检测到未开启GPS定位服务");
//                registerLocationListener();
//            }
//        }
    }

    private final ContentObserver mGpsMonitor = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            checkGPSState();
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if (ifRegistered) {
            unRegisterListener();
        }
    }

    private void unRegisterListener() {
        getActivity().getContentResolver().unregisterContentObserver(mGpsMonitor);
    }

    /**
     * 注册监听广播
     *
     * @throws Exception
     */
    private void registerLocationListener() {
        getActivity().getContentResolver()
                .registerContentObserver(
                        Settings.Secure
                                .getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED),
                        false, mGpsMonitor);
    }

    /**
     * 显示图层列表
     */
    public void showLayerList() {
        if (layerPresenter != null) {
            layerPresenter.showLayerList();
        }
    }

    /**
     * 开启定位
     */
    public void startLocate() {

        if (locationButton != null) {
            locationButton.followLocation();
        }

//        if (mLocationManager == null) {
//            mLocationManager = new PatrolLocationManager(getActivity(), mapView);
//        }
//        this.mLocationManager.setMapView(mapView);
//        this.mLocationManager.setUseArcGisForLocation();
//        mLocationManager.startLocate(new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//
//                tv_hint.setText("定位成功");
//
//                final Point point = new Point(location.getLongitude(), location.getLatitude());
//                //mLastSelectedLocation = point;
//                if (mapView.getMaxExtent() == null || mapView.getSpatialReference() == null) {
//                    return;
//                }
//                if (GeometryEngine.contains(mapView.getMaxExtent(), point, mapView.getSpatialReference())) {
//                    if (ifFirstLocate) {
//                        ifFirstLocate = false;
//                        mapView.centerAt(point, true);
//                    }
//                } else {
//                    ToastUtil.shortToast(getActivity(), "当前位置不在地图范围内");
//                }
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mLocationManager != null) {
            mLocationManager.stopLocate();
        }
    }


    private void initLegendPresenter() {
        if (legendPresenter == null) {
            ILegendView legendView = new ImagePmLegendView(getActivity());
            legendPresenter = new LegendPresenter(legendView, new OnlineLegendService(getActivity()));
        }

    }

    public void loadLayer() {
        if (layerPresenter != null || mapView == null) {
            return;
        }
        ILayerView layerView = null;
        if (getActivity() instanceof IDrawerController) {
            IDrawerController drawerController = (IDrawerController) getActivity();
            layerView = new PatrolLayerView2(getActivity(), mapView, drawerController.getDrawerContainer());
        } else {
            layerView = new PatrolLayerView2(getActivity(), mapView, null);
        }
        layerPresenter = new PatrolLayerPresenter(layerView, new AgwebPatrolLayerService2(getActivity().getApplicationContext()));
        layerPresenter.loadLayer(new Callback2<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                loadLayersSuccess = true;
            }

            @Override
            public void onFail(Exception error) {
                loadLayersSuccess = false;
            }
        });
//        ILayerView2 layerView2 = null;
//        if (getActivity() instanceof IDrawerController) {
//            IDrawerController drawerController = (IDrawerController) getActivity();
//            layerView2 = new PatrolLayerView2(getActivity(), mapView, drawerController.getDrawerContainer());
//        } else {
//            layerView2 = new PatrolLayerView2(getActivity(), mapView, null);
//        }
//        layerPresenter = new LayerPresenter2(layerView2,new AgwebPatrolLayerService2(getActivity()));
//        layerPresenter.loadLayer();

        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                mStatus = status;
                if (STATUS.INITIALIZED == mStatus) {

                    if (PatrolLayerPresenter.initScale != -1 && PatrolLayerPresenter.initScale != 0) {
                        mapView.setScale(PatrolLayerPresenter.initScale);
//                        Point point = new Point(PatrolLayerPresenter.longitude, PatrolLayerPresenter.latitude);
//                        mapView.centerAt(point, true);
                    }
                    if(layerPresenter != null) {
                        layerPresenter.changeLayerVisibility(PatrolLayerPresenter.DRAINAGE_PUMPING_STATION, true);
                    }
                    startLocate();
                    checkGPSState();
                    mapView.setMaxScale(30);
                }
            }
        });
        mapView.setOnTouchListener(new MapOnTouchListener(getActivity(), mapView) {

            @Override
            public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
                if (locationButton.getVisibility() == View.VISIBLE && locationButton != null && locationButton.ifLocating()) {
                    locationButton.setStateNormal();
                }
                return super.onDragPointerMove(from, to);
            }
        });
    }

    @Override
    protected void lazyLoad() {
        loadLayer();
    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
        if (ifRegistered) {
            unRegisterListener();
        }
    }

    private void changeLayerUrlInitFailState() {
        if (LayerUrlConstant.ifLayerUrlInitSuccess()
                && loadLayersSuccess
                && ll_layer_url_init_fail != null) {
            ll_layer_url_init_fail.setVisibility(View.GONE);
        }
        if ((!LayerUrlConstant.ifLayerUrlInitSuccess()
                || !loadLayersSuccess)
                && ll_layer_url_init_fail != null) {
            ll_layer_url_init_fail.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onInitLayerUrlFinished(OnInitLayerUrlFinishEvent onInitLayerUrlFinishEvent) {
        changeLayerUrlInitFailState();
        if (mapView.getLayers() == null
                || mapView.getLayers().length == 0) {
            if (layerPresenter != null) {
                layerPresenter.loadLayer(new Callback2<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        loadLayersSuccess = true;
                        changeLayerUrlInitFailState();
                    }

                    @Override
                    public void onFail(Exception error) {
                        loadLayersSuccess = false;
                        changeLayerUrlInitFailState();
                    }
                });
            }
        }
    }

    @Subscribe
    public void onRefreshEventEvent(OnRefreshEvent onRefreshEvent) {
        if (!LayerUrlConstant.ifLayerUrlInitSuccess()) {
            LayerUrlConstant.initComponentLayers(getContext(), new Callback2<String[]>() {
                @Override
                public void onSuccess(String[] result) {
                    changeLayerUrlInitFailState();
                }

                @Override
                public void onFail(Exception error) {
                    changeLayerUrlInitFailState();
                }
            });
        }
    }
}
