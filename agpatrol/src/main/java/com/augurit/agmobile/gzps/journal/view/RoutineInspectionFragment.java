package com.augurit.agmobile.gzps.journal.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.OnInitLayerUrlFinishEvent;
import com.augurit.agmobile.gzps.common.OnRefreshEvent;
import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.componentmaintenance.service.ComponentService;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.journal.JournalsDetailListActivity;
import com.augurit.agmobile.gzps.journal.service.JournalService;
import com.augurit.agmobile.gzps.journal.view.journallist.MyJournalListActivity;
import com.augurit.agmobile.gzps.journal.view.uploadevent.JournalEventUploadActivity;
import com.augurit.agmobile.gzps.track.view.TrackRecordView;
import com.augurit.agmobile.gzps.track.view.presenter.TrackPresenter;
import com.augurit.agmobile.gzps.uploadfacility.model.CheckResult;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.service.CorrectFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.util.UploadLayerFieldKeyConstant;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerPresenter;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.mapengine.legend.service.OnlineLegendService;
import com.augurit.agmobile.mapengine.legend.view.ILegendView;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.common.SelectLocationTouchListener;
import com.augurit.agmobile.patrolcore.common.legend.ImageLegendView;
import com.augurit.agmobile.patrolcore.common.legend.LegendPresenter;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.common.model.LocationInfo;
import com.augurit.agmobile.patrolcore.common.widget.LocationButton;
import com.augurit.agmobile.patrolcore.editmap.widget.LocationMarker;
import com.augurit.agmobile.patrolcore.layer.service.AgwebPatrolLayerService2;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView2;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.geocode.Locator;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.content.Context.LOCATION_SERVICE;
import static com.augurit.agmobile.gzps.R.id.subtype;

/**
 * Created by xcl on 2017/11/14.
 */

public class RoutineInspectionFragment extends Fragment {

    private MapView mapView;
    private ILayerPresenter layerPresenter;
    private boolean loadLayersSuccess = true;
    private PatrolLocationManager mLocationManager;
    private boolean ifFirstLocate = false;
    private TextView tv_hint;
    private LocationManager lm;
    private LocationButton locationButton;
    private View ll_layer_url_init_fail;
    /**
     * ???????????????
     */
    private boolean ifRegistered = false;
    private LegendPresenter legendPresenter;
    private View mRootView, rl_track;//????????????
    private OnStatusChangedListener.STATUS mStatus;

    private TrackPresenter trackPresenter;
    private TrackRecordView mTrackView;
    private View mView;
    private Context mContext;
    private View track_time;
    private View track_length;
    private LinearLayout track_start;
    private View track_supend;
    private View iv_start_record;
    private View track_stop;
    private View btn_edit;
    private View btn_edit_cancel;
    private boolean ifInEditMode;
    private boolean ifFirstEdit = true;
    private LocationMarker locationMarker;
    private SelectLocationTouchListener editModeSelectLocationTouchListener;
    private View.OnClickListener editModeCalloutSureButtonClickListener;
    private ComponentService componentService;
    private ProgressDialog pd;
    private ArrayList<Component> mComponentQueryResult = new ArrayList<>();
    private View btn_prev;
    private View btn_next;
    private int currIndex = 0;
    private ViewGroup map_bottom_sheet;
    private AnchorSheetBehavior<ViewGroup> mBehavior;
    private TextView tv_check_hint;
    private View ll_check_hint;
    //private View pb_load_check_hint;
    private TextView tv_check_person;
    private TextView tv_check_phone;
    private String status = "2";
    private String person = "";
    private DetailAddress componentDetailAddress = null;
    private List<String> attribute = Arrays.asList("??????(????????????)","??????","?????????","?????????","????????????","????????????(????????????)","????????????","????????????(????????????)");
    private DefaultTouchListener defaultMapOnTouchListener;
    /**
     * ?????????????????????
     */
    private Point mLastSelectedLocation = null;
    private ProgressDialog progressDialog;

    public LocationButton getLocationButton() {
        return locationButton;
    }

    protected int setContentView() {
        return R.layout.fragment_routine_inspection;
    }

    //?????????????????????????????????
    public void finishTrackService() {
        if (trackPresenter != null) {
            trackPresenter.stopService();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(setContentView(), container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.mRootView = view;

        EventBus.getDefault().register(this);

        mView.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
            }
        });

        mView.findViewById(R.id.ll_search).setVisibility(View.VISIBLE);
        ImageView iv_open_search = (ImageView) mView.findViewById(R.id.iv_open_search);
        iv_open_search.setImageResource(R.mipmap.ic_list);
        ((TextView) mView.findViewById(R.id.tv_search)).setText("????????????");
        mView.findViewById(R.id.tv_search).setVisibility(View.VISIBLE);

        mView.findViewById(R.id.ll_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MyJournalListActivity.class);
                startActivity(intent);
            }
        });
        ((TextView) mView.findViewById(R.id.tv_title)).setText("????????????");
        mapView = (MapView) view.findViewById(R.id.mapview);
        mapView.setMapBackground(Color.WHITE, Color.TRANSPARENT, 0f, 0f);//???????????????????????????????????????

        ll_layer_url_init_fail = view.findViewById(R.id.ll_layer_url_init_fail);
        //????????????
        locationMarker = (LocationMarker) view.findViewById(R.id.locationMarker);
        rl_track = view.findViewById(R.id.rl_track);
        track_time = view.findViewById(R.id.track_time);
        track_length = view.findViewById(R.id.track_length);
        track_start = (LinearLayout) view.findViewById(R.id.track_start);
        track_supend = view.findViewById(R.id.txt_track_suspend);
        track_stop = view.findViewById(R.id.track_stop);
        map_bottom_sheet = (ViewGroup) view.findViewById(R.id.map_bottom_sheet);
        mBehavior = AnchorSheetBehavior.from(map_bottom_sheet);

        btn_prev = view.findViewById(R.id.prev);
        btn_next = view.findViewById(R.id.next);
        RxView.clicks(btn_prev)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currIndex--;
                        if (currIndex < 0) {
                            btn_prev.setVisibility(View.GONE);
                            return;
                        }
                        String layerName = mComponentQueryResult.get(currIndex).getLayerName();

                        showBottomSheet(mComponentQueryResult.get(currIndex));

                        if (currIndex == 0) {
                            btn_prev.setVisibility(View.GONE);
                        }
                        if (mComponentQueryResult.size() > 1) {
                            btn_next.setVisibility(View.VISIBLE);
                        }
                    }
                });
        RxView.clicks(btn_next)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currIndex++;
                        if (currIndex > mComponentQueryResult.size()) {
                            btn_next.setVisibility(View.GONE);
                            return;
                        }
                        String layerName = mComponentQueryResult.get(currIndex).getLayerName();

                        showBottomSheet(mComponentQueryResult.get(currIndex));

                        if (currIndex == (mComponentQueryResult.size() - 1)) {
                            btn_next.setVisibility(View.GONE);
                        }
                        if (currIndex > 0) {
                            btn_prev.setVisibility(View.VISIBLE);
                        }
                    }
                });
        btn_edit = view.findViewById(R.id.btn_edit);
        btn_edit_cancel = view.findViewById(R.id.btn_edit_cancel);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean locate = false;
                ifInEditMode = true;
                btn_edit.setVisibility(View.GONE);
                btn_edit_cancel.setVisibility(View.VISIBLE);
                if (ifFirstEdit) {
                    ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "?????????????????????????????????");
                    ifFirstEdit = false;
                }

//                if (locationMarker != null) {
//                    locationMarker.changeIcon(R.mipmap.ic_xun);
//                    locationMarker.setVisibility(View.VISIBLE);
//                }
                hideBottomSheet();
                initGLayer();
//                setSearchFacilityListener();
                mapView.setOnTouchListener(getDefaultMapOnTouchListener());
                //?????????????????????????????? setSearchFacilityListener ??????????????????
                // ??????editModeSelectLocationTouchListener ??? editModeCalloutSureButtonClickListener ????????????
                if (locate) {
                    editModeSelectLocationTouchListener.locate();
                    if (mapView.getCallout().isShowing()) {
                        mapView.getCallout().animatedHide();
                    }
                    editModeCalloutSureButtonClickListener.onClick(null);
                }

            }
        });
        btn_edit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifInEditMode = false;
                btn_edit.setVisibility(View.VISIBLE);
                btn_edit_cancel.setVisibility(View.GONE);
//                if (locationMarker != null) {
//                    locationMarker.setVisibility(View.GONE);
//                }

                mapView.getCallout().hide();
                hideBottomSheet();
                initGLayer();
                //1
                mapView.setOnTouchListener(getDefaultMapOnTouchListener());
            }
        });


        /**
         * ??????
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


        /**
         * ??????
         */
        locationButton = (LocationButton) view.findViewById(R.id.locationButton);
        locationButton.setMapView(mapView);

        //????????????
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
        /**
         * ?????????????????????????????????
         */
        tv_check_hint = (TextView) map_bottom_sheet.findViewById(R.id.tv_check_hint);
        ll_check_hint = map_bottom_sheet.findViewById(R.id.ll_check_hint);
        tv_check_phone = (TextView) map_bottom_sheet.findViewById(R.id.tv_check_phone);
        tv_check_person = (TextView) map_bottom_sheet.findViewById(R.id.tv_check_person);

        tv_hint = (TextView) view.findViewById(R.id.tv_hint);
        loadLayer();
    }

    /**
     * ??????????????????
     *
     * @param view
     */
    private void initTrackView(View view) {
        if (mTrackView == null) {
            mTrackView = new TrackRecordView(getActivity(), view);
            trackPresenter = new TrackPresenter(getActivity(), mTrackView);
            trackPresenter.setBackListener(new Callback1() {
                @Override
                public void onCallback(Object o) {

//                    rl_track.setVisibility(View.GONE);
                    locationButton.setVisibility(View.VISIBLE);
                }
            });
        } else {
            trackPresenter.bindService();
        }
    }

    public void checkGPSState() {
        //????????????????????????????????????GPS????????????
        LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!ok) {
            ToastUtil.shortToast(getActivity().getApplicationContext(), "??????????????????GPS????????????");
        }
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
     * ??????????????????
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
     * ??????????????????
     */
    public void showLayerList() {
        if (layerPresenter != null) {
            layerPresenter.showLayerList();
        }
    }

    /**
     * ????????????
     */
    public void startLocate() {

        if (locationButton != null) {
            locationButton.followLocation();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mLocationManager != null) {
            mLocationManager.stopLocate();
        }
        if (ifRegistered) {
            unRegisterListener();
        }
        if(trackPresenter != null){
            trackPresenter.back();
        }
    }


    /**
     * ??????
     */
    private void initLegendPresenter() {
        if (legendPresenter == null) {
            ILegendView legendView = new ImageLegendView(getActivity());
            legendPresenter = new LegendPresenter(legendView, new OnlineLegendService(getActivity()));
        }

    }

    /**
     * ????????????
     */
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
        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                mStatus = status;
                if (STATUS.INITIALIZED == mStatus) {

                    if (PatrolLayerPresenter.initScale != -1 && PatrolLayerPresenter.initScale != 0) {
                        mapView.setScale(PatrolLayerPresenter.initScale);
                    }
                    mapView.setMaxScale(30);
                    initTrackView(mRootView);
                    startLocate();
                    checkGPSState();
                }
            }
        });
//        mapView.setOnTouchListener(new MapOnTouchListener(getActivity(), mapView) {
//
//            @Override
//            public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
//                if (locationButton.getVisibility() == View.VISIBLE && locationButton != null && locationButton.ifLocating()) {
//                    locationButton.setStateNormal();
//                }
//                return super.onDragPointerMove(from, to);
//            }
//        });
        mapView.setOnTouchListener(getDefaultMapOnTouchListener());
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
            LayerUrlConstant.initComponentLayers(mContext, new Callback2<String[]>() {
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

    /**
     * ?????????????????????????????????
     */
    private void setSearchFacilityListener() {
        if (editModeSelectLocationTouchListener == null) {
            editModeSelectLocationTouchListener = new SelectLocationTouchListener(mContext,
                    mapView, locationMarker, false, null) {
                @Override
                public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
                    if (locationButton != null && locationButton.ifLocating()) {
                        //4---1
                        locationButton.setStateNormal();
                    }
                    return super.onDragPointerMove(from, to);
                }

                @Override
                public boolean onSingleTap(MotionEvent point) {
//                if (!mMapView.getCallout().isShowing()) {
//                    mMapView.getCallout().show();
//                }
//                    locationMarker.setVisibility(View.VISIBLE);
                    initGLayer();
                    hideBottomSheet();
                    return true;
                }
            };
        }

        if (editModeCalloutSureButtonClickListener == null) {
            //??????????????????????????????callout??????????????????????????????
            editModeCalloutSureButtonClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double scale = mapView.getScale();
                    if (scale > LayerUrlConstant.MIN_QUERY_SCALE) {
                        ToastUtil.shortToast(getContext(), "??????????????????????????????????????????");
                        return;
                    }

                    LocationInfo locationInfo = editModeSelectLocationTouchListener.getLoationInfo();
                    if (locationInfo.getDetailAddress() == null && locationInfo.getPoint() == null) {
                        ToastUtil.shortToast(mContext, "?????????????????????");
                        return;
                    }

                    double x = locationInfo.getPoint().getX();
                    double y = locationInfo.getPoint().getY();

                    if (scale < LayerUrlConstant.MIN_QUERY_SCALE) {
                        query(x, y);
                    }


                }
            };
        }

        /**
         * ???????????????????????????????????????
         */
        editModeSelectLocationTouchListener.setCalloutSureButtonClickListener(editModeCalloutSureButtonClickListener);
        mapView.setOnTouchListener(editModeSelectLocationTouchListener);
    }

    GraphicsLayer mGLayer = null;
    Graphic graphic = null;

    private void initGLayer() {
        if (mGLayer == null) {
            mGLayer = new GraphicsLayer();
            mapView.addLayer(mGLayer);
        } else {
            mGLayer.removeAll();
        }
    }

    @NonNull
    private void initComponentService() {
        if (componentService == null) {
            componentService = new ComponentService(mContext.getApplicationContext());
        }
    }

    /**
     * ???????????????????????????
     *
     * @param x
     * @param y
     */
    private void query(double x, double y) {
        pd = new ProgressDialog(getContext());
        pd.setMessage("??????????????????...");
        pd.show();
        mComponentQueryResult.clear();
        btn_next.setVisibility(View.GONE);
        btn_prev.setVisibility(View.GONE);
        currIndex = 0;
        final Point point = new Point(x, y);
//        final List<LayerInfo> layerInfoList = new ArrayList<>();
//        for (String url : LayerUrlConstant.newComponentUrls) {
//            LayerInfo layerInfo = new LayerInfo();
//            layerInfo.setUrl(url);
//            layerInfoList.add(layerInfo);
//        }
        Geometry geometry = GeometryEngine.buffer(point, mapView.getSpatialReference(), 40 * mapView.getResolution(), null);
        initComponentService();
        componentService.queryComponentsForWell(geometry, new Callback2<List<Component>>() {
            @Override
            public void onSuccess(List<Component> components) {
                if (pd != null) {
                    pd.dismiss();
                }
                if (ListUtil.isEmpty(components)) {
                    ToastUtil.shortToast(getContext(), "???????????????????????????");
                    return;
                }
                mComponentQueryResult = new ArrayList<Component>();
                mComponentQueryResult.addAll(components);
                if (ListUtil.isEmpty(mComponentQueryResult)) {
                    initGLayer();
                    if (pd != null) {
                        pd.dismiss();
                    }
                    ToastUtil.shortToast(getContext(), "???????????????????????????");
                    return;
                }
                showComponentsOnBottomSheet(mComponentQueryResult);
            }

            @Override
            public void onFail(Exception error) {
                if (pd != null) {
                    pd.dismiss();
                }
                ToastUtil.shortToast(getContext(), "???????????????????????????");
            }
        });
    }

    private void showComponentsOnBottomSheet(List<Component> componentQueryResult) {
        if (componentQueryResult.size() > 1) {
            btn_next.setVisibility(View.VISIBLE);
        }
        mapView.getCallout().hide();
        //??????marker
        locationMarker.setVisibility(View.GONE);
        //initGLayer();
        String layerName = mComponentQueryResult.get(0).getLayerName();
        showBottomSheet(mComponentQueryResult.get(0));
    }

    /**
     * ????????????????????????????????????BottomSheet???
     */
    private void showBottomSheet(final Component component) {

        // map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.GONE);
        // pb_load_check_hint.setVisibility(View.VISIBLE);
        status = "2";
        person = "";
        tv_check_person.setText("");
        ll_check_hint.setVisibility(View.GONE);
        String layerName = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(ComponentFieldKeyConstant.LAYER_NAME), "");
        if (StringUtil.isEmpty(layerName)) {
            layerName = component.getLayerName();
        }
        //??????????????????????????????????????????(?????????)
        if ("??????".equals(layerName) || "?????????".equals(layerName) || "?????????".equals(layerName)) {
            checkIfHasBeenUploadBefore(component, 1);
            if (component.getGraphic().getGeometry() instanceof Point) {
                final Point point = (Point) component.getGraphic().getGeometry();
                componentDetailAddress = null;
                requestLocation2(point, mapView.getSpatialReference(), new Callback1<DetailAddress>() {
                    @Override
                    public void onCallback(DetailAddress detailAddress) {
                        detailAddress.setX(point.getX());
                        detailAddress.setY(point.getY());
                        componentDetailAddress = detailAddress;
                    }
                });
            }
        }


        initGLayer();
        drawGeometry(component.getGraphic().getGeometry(), mGLayer, false, true);

        if (layerName == null || !attribute.contains(layerName)) {
            hideBottomSheet();
            return;
        }
        String errorInfo = null;
        Object oErrorInfo = component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ERROR_INFO);

        if (oErrorInfo != null) {
            errorInfo = oErrorInfo.toString();
        }

        TextView titleTv = (TextView) map_bottom_sheet.findViewById(R.id.title);
        TextView dateTv = (TextView) map_bottom_sheet.findViewById(R.id.date);
        TextView sortTv = (TextView) map_bottom_sheet.findViewById(R.id.sort);
        sortTv.setVisibility(View.VISIBLE);
        TextView subtypeTv = (TextView) map_bottom_sheet.findViewById(subtype);
        TextView field1Tv = (TextView) map_bottom_sheet.findViewById(R.id.field1);
        TextView field2Tv = (TextView) map_bottom_sheet.findViewById(R.id.field2);
        TextView field3Tv = (TextView) map_bottom_sheet.findViewById(R.id.field3);
        TextView addrTv = (TextView) map_bottom_sheet.findViewById(R.id.addr);
//        TextView code = (TextView) map_bottom_sheet.findViewById(R.id.code);
        View btn_container = map_bottom_sheet.findViewById(R.id.btn_container);//??????????????????
        TextView tv_gc_begin = (TextView) map_bottom_sheet.findViewById(R.id.tv_gc_begin);//????????????
        TextView tv_gc_end = (TextView) map_bottom_sheet.findViewById(R.id.tv_gc_end);//????????????
        tv_gc_end.setVisibility(View.VISIBLE);
        TextView tv_ms_begin = (TextView) map_bottom_sheet.findViewById(R.id.tv_ms_begin);//????????????
        TextView tv_ms_end = (TextView) map_bottom_sheet.findViewById(R.id.tv_ms_end);//????????????
        TextView tv_gj = (TextView) map_bottom_sheet.findViewById(R.id.tv_gj);//??????
        View ll_gj = map_bottom_sheet.findViewById(R.id.ll_gj);//??????
        TextView tv_cz = (TextView) map_bottom_sheet.findViewById(R.id.tv_cz);//??????
        View ll_gq = map_bottom_sheet.findViewById(R.id.ll_gq);//??????
        TextView tv_tycd = (TextView) map_bottom_sheet.findViewById(R.id.tv_tycd);//????????????
        TextView tv_pd = (TextView) map_bottom_sheet.findViewById(R.id.tv_pd);//??????
        View line = map_bottom_sheet.findViewById(R.id.line);
        View line2 = map_bottom_sheet.findViewById(R.id.line2);//?????????
        View line3 = map_bottom_sheet.findViewById(R.id.line3);//?????????
        View line4 = map_bottom_sheet.findViewById(R.id.line4);//?????????
//        TextView code = (TextView) map_bottom_sheet.findViewById(R.id.code);
        initValue(component, addrTv, "???????????????", ComponentFieldKeyConstant.ADDR, 0, "");
        line.setVisibility(View.VISIBLE);
        field3Tv.setVisibility(View.GONE);
        ll_gj.setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.ll_no_ysk).setVisibility(View.VISIBLE);
        btn_container.setVisibility(View.VISIBLE);
        if (layerName.contains("????????????")) {//???????????????????????????
            line2.setVisibility(View.VISIBLE);
            line3.setVisibility(View.VISIBLE);
            line4.setVisibility(View.VISIBLE);
            addrTv.setVisibility(View.VISIBLE);
            ll_gq.setVisibility(View.GONE);
            initValue(component, tv_gc_begin, "?????????????????????", ComponentFieldKeyConstant.BEG_H);
            initValue(component, tv_gc_end, "?????????????????????", ComponentFieldKeyConstant.END_H);
            initValue(component, tv_ms_begin, "?????????????????????", ComponentFieldKeyConstant.BEGCEN_DEE);
            initValue(component, tv_ms_end, "?????????????????????", ComponentFieldKeyConstant.ENDCEN_DEE);
            initValue(component, tv_gj, "?????????", ComponentFieldKeyConstant.PIPE_GJ, 0, "mm");
            initValue(component, tv_cz, "???????????????", ComponentFieldKeyConstant.LENGTH);
        } else if (layerName.contains("??????")) {
            line2.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
            ll_gq.setVisibility(View.GONE);
            tv_gc_end.setVisibility(View.GONE);
            field3Tv.setVisibility(View.VISIBLE);
            tv_gc_end.setText("");
            if (layerName.contains("????????????")) {
                addrTv.setVisibility(View.VISIBLE);
                tv_cz.setVisibility(View.GONE);
                tv_ms_end.setText("");
                initValue(component, tv_gj, "???????????????", ComponentFieldKeyConstant.COVER_SIZE, 2, "mm");
                initValue(component, tv_gc_begin, "???????????????", ComponentFieldKeyConstant.SUR_H);
                initValue(component, tv_ms_begin, "???????????????", ComponentFieldKeyConstant.BOTTOM_H);
            } else {
                addrTv.setVisibility(View.VISIBLE);
                tv_cz.setVisibility(View.GONE);
                tv_ms_end.setText("");
                tv_gj.setText("");
                initValue(component, tv_gc_begin, "??????????????????", ComponentFieldKeyConstant.ATTR_FIVE, 0, "");
                initValue(component, tv_gj, "???????????????", ComponentFieldKeyConstant.COVER_SIZE, 2, "mm");
                ll_gj.setVisibility(View.GONE);
                initValue(component, tv_ms_begin, "???????????????", ComponentFieldKeyConstant.ROAD, 0, "");
                initValue(component, addrTv, "???????????????", ComponentFieldKeyConstant.ADDR, 0, "");
            }

        } else if (layerName.contains("?????????")) {
            ll_gj.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
            ll_gq.setVisibility(View.GONE);
            sortTv.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            addrTv.setVisibility(View.VISIBLE);
            tv_cz.setVisibility(View.GONE);
            tv_ms_end.setText("");
            tv_gj.setText("");
//            subtypeTv.setText("???????????????" + component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
            initValue(component, tv_gc_begin, "??????????????????", ComponentFieldKeyConstant.ATTR_FOUR, 0, "");
            initValue(component, subtypeTv, "???????????????", ComponentFieldKeyConstant.ATTR_ONE, 0, "");
            initValue(component, tv_ms_begin, "???????????????", ComponentFieldKeyConstant.ROAD, 0, "");
            initValue(component, addrTv, "???????????????", ComponentFieldKeyConstant.ADDR, 0, "");
        } else if (layerName.contains("?????????")) {
            map_bottom_sheet.findViewById(R.id.ll_no_ysk).setVisibility(View.GONE);
        } else {
            line2.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
            ll_gq.setVisibility(View.GONE);
            addrTv.setVisibility(View.GONE);
            field3Tv.setText("");
            tv_cz.setText("");
            tv_gc_begin.setText("");
            tv_gc_end.setText("");
            tv_ms_begin.setText("");
            tv_ms_end.setText("");
            tv_gj.setText("");
        }
        TextView tv_errorinfo = (TextView) map_bottom_sheet.findViewById(R.id.tv_errorinfo);
        final String type = layerName;

        String subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
        if (StringUtil.isEmpty(subtype) || "null".equals(subtype)) {
            subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE));
        }
        String usid = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.USID));
        String objectId = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.OBJECTID));
        if (StringUtil.isEmpty(usid) || "null".equals(usid)) {
            usid = objectId;
        }
        final String title = StringUtil.getNotNullString(getLayerName(type), "") + "(" + usid + ")";
        titleTv.setText(title);

        String date = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.UPDATEDATE));
        String formatDate = "";
        try {
            formatDate = TimeUtil.getStringTimeYMD(new Date(Long.valueOf(date)));
        } catch (Exception e) {

        }
        dateTv.setText(StringUtil.getNotNullString(formatDate, ""));
        String sort = "";
        if (layerName.contains("????????????")) {
            sort = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SORT));
        } else {
            sort = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_TWO));
            if (StringUtil.isEmpty(sort) || "null".equals(sort)) {
                sort = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SORT));
            }
        }
        int color = ResourcesCompat.getColor(getResources(), R.color.mark_light_purple, null);

        if (sort.contains("????????????")) {
            color = ResourcesCompat.getColor(getResources(), R.color.mark_light_purple, null);
        } else if (sort.contains("??????")) {
            color = ResourcesCompat.getColor(getResources(), R.color.progress_line_green, null);
        } else if (sort.contains("??????")) {
            color = ResourcesCompat.getColor(getResources(), R.color.agmobile_red, null);
        }
        sortTv.setTextColor(color);

        sortTv.setText(StringUtil.getNotNullString(sort, ""));


        String listFields = ComponentFieldKeyConstant.getListFieldsByLayerName(component.getLayerName());
        String[] listFieldArray = listFields.split(",");
        if (!StringUtil.isEmpty(listFields)
                && listFieldArray.length == 3) {
            String field1_1 = listFieldArray[0].split(":")[0];
            String field1_2 = listFieldArray[0].split(":")[1];
            String field1 = String.valueOf(component.getGraphic().getAttributes().get(field1_2));
            field1Tv.setText(field1_1 + "???" + StringUtil.getNotNullString(field1, ""));

            String field2_1 = listFieldArray[1].split(":")[0];
            String field2_2 = listFieldArray[1].split(":")[1];
            String field2 = String.valueOf(component.getGraphic().getAttributes().get(field2_2));
            field2Tv.setText(field2_1 + "???" + StringUtil.getNotNullString(field2, ""));

            String field3_1 = listFieldArray[2].split(":")[0];
            String field3_2 = listFieldArray[2].split(":")[1];
            String field3 = String.valueOf(component.getGraphic().getAttributes().get(field3_2));
            field3Tv.setText(field3_1 + "???" + StringUtil.getNotNullString(field3, ""));
        }

        /**
         * ??????????????????????????????????????????
         */
        if ("?????????".equals(layerName)) {
            String feature = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
            sortTv.setText(StringUtil.getNotNullString(feature, ""));
        }
        if ("?????????".equals(type)) {
            String style = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE));
            subtypeTv.setText(StringUtil.getNotNullString(style, ""));
            subtypeTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.dust_grey, null));
        } else if (!"?????????".equals(type)) {
            subtypeTv.setText(StringUtil.getNotNullString(subtype, ""));
        }


        //???????????????
        String codeValue = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FIVE));
        if (!StringUtil.isEmpty(codeValue)) {
            codeValue = codeValue.trim();
        }
        /**
         * ???????????????
         */
        String field3 = "";
        if (layerName.contains("??????")) {
            String cz = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE), "");
            if (StringUtil.isEmpty(cz)) {
                field3 = "????????????: " + StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.COVER_MATERIAL), "");
            }
            field3Tv.setText(field3);
        }
        tv_errorinfo.setVisibility(View.GONE);
        /**
         * ????????????
         */
        map_bottom_sheet.findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        map_bottom_sheet.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifiedFacility modifiedFacility = getModifiedFacilityFromComponent(component);
                modifiedFacility.setId(null);
                uploadJournal(modifiedFacility);
                return;
            }
        });
        /**
         * ????????????
         */
        map_bottom_sheet.findViewById(R.id.tv_error_correct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, JournalEventUploadActivity.class);
                String reportType = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.REPORT_TYPE), "");
                if("add".equals(reportType)){
                    UploadedFacility facility = getUploadedFacilityFromComponent(component);
                    intent.putExtra("upload", (Parcelable) facility);
                }else {
                    ModifiedFacility modifiedFacility = getModifiedFacilityFromComponent(component);
                    intent.putExtra("modified", modifiedFacility);
                }
                startActivity(intent);
            }
        });

        /**
         * ????????????
         */
        map_bottom_sheet.findViewById(R.id.tv_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, JournalsDetailListActivity.class);
                if (StringUtil.isEmpty(component.getLayerName())) {
                    if (!StringUtil.isEmpty(type)) {
                        component.setLayerName(type);
                    }
                }
                intent.putExtra("component", component);
                startActivityForResult(intent, 123);
            }
        });
        ////// showBottomSheetContent(component_intro.getId());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                map_bottom_sheet.setVisibility(View.VISIBLE);
                mBehavior.setState(AnchorSheetBehavior.STATE_EXPANDED);
            }
        }, 200);


    }

    /**
     * ??????????????????
     * @param modifiedFacility
     */
    private void uploadJournal(ModifiedFacility modifiedFacility) {
        //????????????
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("???????????????????????????");
        progressDialog.setCancelable(false);
        progressDialog.show();
        JournalService identificationService = new JournalService(mContext);
        modifiedFacility.setWellLength(trackPresenter.getTrackLength());
        modifiedFacility.setTrackId(trackPresenter.getCurrentTrackId());
        identificationService.upload(modifiedFacility)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        CrashReport.postCatchedException(new Exception("???????????????????????????????????????" +
                                BaseInfoManager.getUserName(mContext) + "????????????" + e.getLocalizedMessage()));
                        ToastUtil.shortToast(mContext,"??????????????????");
                        //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "????????????");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (responseBody.getCode() == 200) {
                            ToastUtil.shortToast(mContext,"????????????");
                        } else {
                            CrashReport.postCatchedException(new Exception("???????????????????????????????????????" +
                                    BaseInfoManager.getUserName(mContext) + "????????????" + responseBody.getMessage()));
                            ToastUtil.shortToast(mContext,"???????????????????????????" + responseBody.getMessage());
                            //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "???????????????????????????" + responseBody.getMessage());
                        }
                    }
                });
    }

    /**
     * ?????????????????????
     *
     * @param graphicsLayer
     * @param ifRemoveAll   ?????????????????????????????????
     * @param geometry
     */
    private void drawGeometry(Geometry geometry, GraphicsLayer graphicsLayer, boolean ifRemoveAll, boolean ifCenter) {

        if (graphicsLayer == null || geometry == null) {
            return;
        }
        Symbol symbol = null;
        SimpleFillSymbol simpleFillSymbol = null;
        switch (geometry.getType()) {
            case LINE:
            case POLYLINE:
                symbol = new SimpleLineSymbol(Color.RED, 5);
                break;
            case POLYGON:
                simpleFillSymbol = new SimpleFillSymbol(Color.RED, SimpleFillSymbol.STYLE.SOLID);
                simpleFillSymbol.setAlpha(100);
                break;
            case POINT:
                //                symbol = new SimpleMarkerSymbol(Color.RED, 25, SimpleMarkerSymbol.STYLE.CIRCLE);
                symbol = getPointSymbol();
                break;
            default:
                break;
        }

        if (ifRemoveAll) {
            graphicsLayer.removeAll();
        }

        if (symbol != null) {
            Graphic graphic = new Graphic(geometry, symbol);
            graphicsLayer.addGraphic(graphic);
            if (geometry.getType() == Geometry.Type.POLYLINE) { //???????????????????????????????????????????????????????????????
                this.graphic = graphic;
            }
        }
        if (simpleFillSymbol != null) {
            Graphic graphic = new Graphic(geometry, simpleFillSymbol);
            graphicsLayer.addGraphic(graphic);
            if (geometry.getType() == Geometry.Type.POLYLINE) { //???????????????????????????????????????????????????????????????
                this.graphic = graphic;
            }
        }
        if (ifCenter && simpleFillSymbol == null) {
            mapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        } else {
            mapView.setExtent(geometry);
        }
    }

    @NonNull
    private Symbol getPointSymbol() {
        Drawable drawable = null;
        drawable = ResourcesCompat.getDrawable(mContext.getResources(), com.augurit.agmobile.patrolcore.R.mipmap.ic_select_location, null);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(getContext(), drawable);// xjx ????????????api19???????????????drawable
        pictureMarkerSymbol.setOffsetY(16);
        return pictureMarkerSymbol;
    }

    /**
     * ??????????????????????????????????????????(?????????)
     *
     * @param component
     */
    private void checkIfHasBeenUploadBefore(Component component, int type) {
        if (component.getGraphic() != null && component.getGraphic().getAttributes() != null
                && component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.OBJECTID) != null) {
            //??????usid????????????????????????????????????????????????
            String objectId = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.OBJECTID), "");
            CorrectFacilityService correctFacilityService = new CorrectFacilityService(mContext.getApplicationContext());
            correctFacilityService.getGwyxtLastRecord(objectId, type)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CheckResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            //???????????????????????????????????????????????????????????????????????????????????????
                            //  map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.VISIBLE);
                            tv_check_person.setText("");
                            ll_check_hint.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(CheckResult stringResult2) {
                            if (stringResult2 == null || stringResult2.getCode() != 200) {
                                //???????????????????????????????????????????????????
                                //  map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.VISIBLE);
                                ll_check_hint.setVisibility(View.GONE);
                                status = "2";
                                person = "";
                            } else {
                                String checkPersonName = stringResult2.getMarkPerson();
                                if (BaseInfoManager.getUserName(mContext).equals(checkPersonName)) {
                                    checkPersonName = "???";
                                }
                                ll_check_hint.setVisibility(View.VISIBLE);
                                tv_check_person.setVisibility(View.VISIBLE);
                                String date = TimeUtil.getStringTimeYMD(new Date(stringResult2.getMarkTime()));
                                tv_check_person.setText(checkPersonName + "?????????" + date);
                                person = checkPersonName;
                                tv_check_phone.setVisibility(View.VISIBLE);
                                String handle = "";
                                if ("0".equals(stringResult2.getHandle())) {
                                    status = "0";
                                    handle = "??????";
                                } else if ("1".equals(stringResult2.getHandle())) {
                                    status = "1";
                                    handle = "??????";
                                } else if ("3".equals(stringResult2.getHandle())) {
                                    status = "3";
                                    handle = "??????";
                                }
                                tv_check_phone.setText(handle);
                                //????????????????????????????????????????????????" + checkPerson.getTotal() + "???"
                                tv_check_hint.setText("????????????");
                            }
                        }
                    });
        }
    }

    /**
     * ??????????????????
     *
     * @param point
     * @param callback1
     */
    private void requestLocation2(Point point, SpatialReference spatialReference, final Callback1<DetailAddress> callback1) {
        SelectLocationService selectLocationService = new SelectLocationService(mContext, Locator.createOnlineLocator());
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            mContext = context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity != null) {
            mContext = activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    private void hideBottomSheet() {
        map_bottom_sheet.setVisibility(View.GONE);
//        dis_map_bottom_sheet.setVisibility(View.GONE);
        final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) locationButton
                .getLayoutParams();
//        lp.bottomMargin = bottomMargin;
        locationButton.setLayoutParams(lp);
    }

    private void initValue(Component component, TextView tv_gc_begin, String name, String key) {
        tv_gc_begin.setVisibility(View.VISIBLE);
        String value = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(key), "");
        if (StringUtil.isEmpty(value)) {
            tv_gc_begin.setText(name);
            return;
        }
        String format = name + formatToSecond(value,"m",2);
        tv_gc_begin.setText(format);
    }

    private void initValue(Component component, TextView tv_gc_begin, String name, String key,int newScale,String unit) {
        String value = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(key), "");
        if (StringUtil.isEmpty(value)) {
            tv_gc_begin.setText(name);
            return;
        }
        if(newScale == 0){
            tv_gc_begin.setText(name+value+unit);
            return;
        }
        String format = name + formatToSecond(value,unit,newScale);
        tv_gc_begin.setText(format);
    }

    private String formatToSecond(String value,String unit,int newScale) {
        if (StringUtil.isEmpty(value)) {
            return "";
        }
        Double num = Double.valueOf(value);
        BigDecimal bd = new BigDecimal(num);
        num = bd.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return num +""+ unit;
    }

    /**
     * ?????????????????????????????????
     * @param oldLayerName
     * @return
     */
    private String getLayerName(String oldLayerName){
        if(!attribute.contains(oldLayerName))
            return "";
        if(oldLayerName.contains("??????")){
            return "??????";
        }else if(oldLayerName.contains("????????????")){
            return "????????????";
        }else if(oldLayerName.contains("?????????")){
            return "?????????";
        }else if(oldLayerName.contains("?????????")){
            return "?????????";
        }else if(oldLayerName.contains("????????????")){
            return "????????????";
        }else{
            return "";
        }
    }

    /**
     * @return
     */
    private SelectLocationTouchListener getDefaultMapOnTouchListener() {
        if (defaultMapOnTouchListener == null) {
            defaultMapOnTouchListener = new DefaultTouchListener(mContext, mapView, locationMarker, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            /**
             * ????????????????????????????????????
             */
            defaultMapOnTouchListener.registerLocationChangedListener(new SelectLocationTouchListener.OnSelectedLocationChangedListener() {
                @Override
                public void onLocationChanged(Point newLocation) {
                    mLastSelectedLocation = newLocation;
                }

                @Override
                public void onAddressChanged(DetailAddress detailAddress) {

                }
            });
        }
        return defaultMapOnTouchListener;
    }

    /**
     * The MapView's touch listener.
     */
    private class DefaultTouchListener extends SelectLocationTouchListener {

        public DefaultTouchListener(Context context, MapView view, LocationMarker locationMarker, View.OnClickListener calloutSureButtonClickListener) {
            super(context, view, locationMarker, calloutSureButtonClickListener);
        }

        @Override
        public boolean onSingleTap(final MotionEvent e) {
            handleTap(e);
            return true;
        }

        @Override
        public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
            if (locationButton != null && locationButton.ifLocating()) {
                //
                locationButton.setStateNormal();
            }
            return super.onDragPointerMove(from, to);
        }


        /***
         * Handle a tap on the map (or the end of a magnifier long-press event).
         *
         * @param e The point that was tapped.
         */
        private void handleTap(final MotionEvent e) {
            int visibility = map_bottom_sheet.getVisibility();
            initGLayer();
            hideBottomSheet();
            if (visibility == View.VISIBLE ) {
                return;
            }
            final Point point = mMapView.toMapPoint(e.getX(), e.getY());
            if (mapView.getScale() < LayerUrlConstant.MIN_QUERY_SCALE) {
                query(point.getX(), point.getY());
            }
        }
    }

    private ModifiedFacility getModifiedFacilityFromComponent(Component component) {
        Map<String, Object> attribute = component.getGraphic().getAttributes();
        long currentTimeMillis = System.currentTimeMillis();
        User user = new LoginService(mContext, AMDatabase.getInstance()).getUser();
        ModifiedFacility modifiedFacility = new ModifiedFacility();
        modifiedFacility.setMarkTime(currentTimeMillis);
        String userName = user.getUserName();
        String userId = user.getId();
        modifiedFacility.setMarkPerson(userName);
        modifiedFacility.setMarkPersonId(userId);
        modifiedFacility.setObjectId(objectToString(attribute.get("OBJECTID")));
        modifiedFacility.setOriginAddr(objectToString(attribute.get("ORIGIN_ADDR")));
        modifiedFacility.setUserAddr(objectToString(attribute.get("ORIGIN_ADDR")));
        modifiedFacility.setAddr(objectToString(attribute.get("ADDR")));
        String road = objectToString(attribute.get("ROAD"));
        if(StringUtil.isEmpty(road)){
            road = objectToString(attribute.get(ComponentFieldKeyConstant.LANE_WAY));
        }
        modifiedFacility.setRoad(road);
        modifiedFacility.setAttrFive(objectToString(attribute.get("ATTR_FIVE")));
        modifiedFacility.setAttrFour(objectToString(attribute.get("ATTR_FOUR")));
        modifiedFacility.setAttrThree(objectToString(attribute.get("ATTR_THREE")));
        modifiedFacility.setAttrTwo(objectToString(attribute.get("ATTR_TWO")));
        String subType = objectToString(attribute.get("ATTR_ONE"));
        if(StringUtil.isEmpty(subType)){
            subType = objectToString(attribute.get(ComponentFieldKeyConstant.SUBTYPE));
        }
        modifiedFacility.setAttrOne(subType);
        modifiedFacility.setCorrectType(objectToString(attribute.get("CORRECT_TYPE")));
        modifiedFacility.setReportType(objectToString(attribute.get("REPORT_TYPE")));
        modifiedFacility.setDescription(objectToString(attribute.get("DESRIPTION")));
        modifiedFacility.setParentOrgName(objectToString(attribute.get("PARENT_ORG_NAME")));
        modifiedFacility.setDirectOrgName(objectToString(attribute.get("DIRECT_ORG_NAME")));
        modifiedFacility.setTeamOrgName(objectToString(attribute.get("TEAM_ORG_NAME")));
        modifiedFacility.setSuperviseOrgName(objectToString(attribute.get("SUPERVISE_ORG_NAME")));
        String layerName = objectToString(attribute.get("LAYER_NAME"));
        if(StringUtil.isEmpty(layerName)){
            layerName = getLayerName(component.getLayerName());
        }
        modifiedFacility.setLayerName(layerName);
        String usid = objectToString(attribute.get("US_ID"));
        if(StringUtil.isEmpty(usid)){
            usid = objectToString(attribute.get(ComponentFieldKeyConstant.USID));
        }
        modifiedFacility.setUsid(usid);
        modifiedFacility.setLayerUrl(component.getLayerUrl());
        //??????????????????
        modifiedFacility.setX(objectToDouble(attribute.get("X")));
        modifiedFacility.setY(objectToDouble(attribute.get("Y")));
        modifiedFacility.setUserX(objectToDouble(attribute.get("X")));
        modifiedFacility.setUserY(objectToDouble(attribute.get("Y")));

        //???????????????
        //Point geometryCenter = GeometryUtil.getGeometryCenter(geometry);
        modifiedFacility.setOriginX(objectToDouble(attribute.get("ORGIN_X")));
        modifiedFacility.setOriginY(objectToDouble(attribute.get("ORGIN_Y")));
        modifiedFacility.setMarkPerson(objectToString(attribute.get("MARK_PERSON")));
        modifiedFacility.setMarkTime(objectToLong(attribute.get("MARK_TIME")));
        modifiedFacility.setUpdateTime(objectToLong(attribute.get("UPDATE_TIME")));
        modifiedFacility.setCheckState(objectToString(attribute.get("CHECK_STATE")));
        modifiedFacility.setId(objectToLong(attribute.get(UploadLayerFieldKeyConstant.MARK_ID)));

        //???????????????
        modifiedFacility.setOriginRoad(objectToString(attribute.get("ORIGIN_ROAD")));
        String orginOne = objectToString(attribute.get("ORIGIN_ATTR_ONE"));
        if(StringUtil.isEmpty(orginOne)){
            orginOne = objectToString(attribute.get(ComponentFieldKeyConstant.SUBTYPE));
        }
        modifiedFacility.setOriginAttrOne(orginOne);
        modifiedFacility.setOriginAttrTwo(objectToString(attribute.get("ORIGIN_ATTR_TWO")));
        modifiedFacility.setOriginAttrThree(objectToString(attribute.get("ORIGIN_ATTR_THREE")));
        modifiedFacility.setOriginAttrFour(objectToString(attribute.get("ORIGIN_ATTR_FOUR")));
        modifiedFacility.setOriginAttrFive(objectToString(attribute.get("ORIGIN_ATTR_FIVE")));

        //????????????
        modifiedFacility.setpCode(objectToString(attribute.get("PCODE")));
        modifiedFacility.setChildCode(objectToString(attribute.get("CHILD_CODE")));

        //????????????
        modifiedFacility.setCityVillage(objectToString(attribute.get("CITY_VILLAGE")));
        return modifiedFacility;
    }

    private UploadedFacility getUploadedFacilityFromComponent(Component component) {
        Geometry geometry = component.getGraphic().getGeometry();
        Map<String, Object> attribute = component.getGraphic().getAttributes();
        UploadedFacility uploadedFacility = new UploadedFacility();
        uploadedFacility.setAddr(objectToString(attribute.get("ADDR")));
        uploadedFacility.setLayerUrl(component.getLayerUrl());
        uploadedFacility.setObjectId(objectToString(attribute.get("OBJECTID")));
        uploadedFacility.setRoad(objectToString(attribute.get("ROAD")));
        uploadedFacility.setAttrFive(objectToString(attribute.get("ATTR_FIVE")));
        uploadedFacility.setAttrFour(objectToString(attribute.get("ATTR_FOUR")));
        uploadedFacility.setAttrThree(objectToString(attribute.get("ATTR_THREE")));
        uploadedFacility.setAttrTwo(objectToString(attribute.get("ATTR_TWO")));
        uploadedFacility.setAttrOne(objectToString(attribute.get("ATTR_ONE")));
        uploadedFacility.setDescription(objectToString(attribute.get("DESRIPTION")));
        uploadedFacility.setParentOrgName(objectToString(attribute.get("PARENT_ORG_NAME")));
        uploadedFacility.setDirectOrgName(objectToString(attribute.get("DIRECT_ORG_NAME")));
        uploadedFacility.setTeamOrgName(objectToString(attribute.get("TEAM_ORG_NAME")));
        uploadedFacility.setSuperviseOrgName(objectToString(attribute.get("SUPERVISE_ORG_NAME")));
        uploadedFacility.setComponentType(objectToString(attribute.get("LAYER_NAME")));
        uploadedFacility.setLayerName(objectToString(attribute.get("LAYER_NAME")));
        //????????????
        uploadedFacility.setpCode(objectToString(attribute.get("PCODE")));
        uploadedFacility.setChildCode(objectToString(attribute.get("CHILD_CODE")));

        Point geometryCenter = GeometryUtil.getGeometryCenter(geometry);
        uploadedFacility.setX(geometryCenter.getX());
        uploadedFacility.setY(geometryCenter.getY());
        uploadedFacility.setMarkPerson(objectToString(attribute.get("MARK_PERSON")));
        uploadedFacility.setMarkTime(objectToLong(attribute.get("MARK_TIME")));
        uploadedFacility.setUpdateTime(objectToLong(attribute.get("UPDATE_TIME")));
        uploadedFacility.setCheckState(objectToString(attribute.get("CHECK_STATE")));
        uploadedFacility.setId(objectToLong(attribute.get(UploadLayerFieldKeyConstant.MARK_ID)));

//        //????????????
        uploadedFacility.setpCode(objectToString(attribute.get("PCODE")));
        uploadedFacility.setChildCode(objectToString(attribute.get("CHILD_CODE")));
//        //????????????
        uploadedFacility.setCityVillage(objectToString(attribute.get("CITY_VILLAGE")));
        return uploadedFacility;
    }

    private String objectToString(Object object) {
        if (object == null) {
            return "";
        }
        return StringUtil.getNotNullString(object.toString(), "");
    }

    private double objectToDouble(Object object) {
        if (object == null) {
            return 0;
        }
        return Double.valueOf(object.toString());
    }

    private long objectToLong(Object object) {
        if (object == null) {
            return -1L;
        }
        return Long.valueOf(object.toString());
    }
}
