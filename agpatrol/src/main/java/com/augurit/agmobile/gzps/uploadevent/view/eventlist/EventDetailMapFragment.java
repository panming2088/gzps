/* Copyright 2015 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.augurit.agmobile.gzps.uploadevent.view.eventlist;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.addcomponent.LayerAdapter;
import com.augurit.agmobile.gzps.common.MapHelper;
import com.augurit.agmobile.gzps.common.OnInitLayerUrlFinishEvent;
import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.editmap.NoMapSelectLocationEditStateTableItemView2;
import com.augurit.agmobile.gzps.common.util.MapIconUtil;
import com.augurit.agmobile.gzps.componentmaintenance.ComponetListAdapter;
import com.augurit.agmobile.gzps.componentmaintenance.SelectComponentEvent;
import com.augurit.agmobile.gzps.componentmaintenance.model.QueryFeatureSet;
import com.augurit.agmobile.gzps.componentmaintenance.service.ComponentService;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.common.widget.scaleview.MapScaleView;
import com.augurit.agmobile.mapengine.identify.service.IIdentifyService;
import com.augurit.agmobile.mapengine.identify.util.IdentifyServiceFactory;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.layermanage.service.LayersService;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.mapengine.legend.service.OnlineLegendService;
import com.augurit.agmobile.mapengine.legend.view.ILegendView;
import com.augurit.agmobile.patrolcore.common.SelectLocationTouchListener;
import com.augurit.agmobile.patrolcore.common.file.model.FileResult;
import com.augurit.agmobile.patrolcore.common.file.service.FileService;
import com.augurit.agmobile.patrolcore.common.legend.ImageLegendView;
import com.augurit.agmobile.patrolcore.common.legend.LegendPresenter;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableItems;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.agmobile.patrolcore.common.table.model.ProjectTable;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.TableState;
import com.augurit.agmobile.patrolcore.common.widget.LocationButton;
import com.augurit.agmobile.patrolcore.editmap.EditLineReEditStateMapListener;
import com.augurit.agmobile.patrolcore.editmap.OnGraphicChangedListener;
import com.augurit.agmobile.patrolcore.editmap.widget.LocationMarker;
import com.augurit.agmobile.patrolcore.layer.service.AgwebPatrolLayerService2;
import com.augurit.agmobile.patrolcore.layer.service.EditLayerService;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView2;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.agmobile.patrolcore.upload.view.ReEditTableActivity;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior;
import com.augurit.am.fw.utils.AMFileUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.AttachmentInfo;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
import static com.augurit.agmobile.gzps.R.id.subtype;

;

/**
 *
 */
public class EventDetailMapFragment extends Fragment implements View.OnClickListener {

    private static final String KEY_MAP_STATE = "com.esri.MapState";

    private LocationMarker locationMarker;
//    /**
//     * ?????????????????????????????????
//     */
//    private boolean hasZoomBefore = false;
//
    /**
     * ?????????????????????
     */
    private Point mLastSelectedLocation = null;
    private PatrolLocationManager mLocationManager;
    /**
     * ?????????????????????
     */
    private GraphicsLayer mGLayerFroDrawLocation;

    /**
     * ????????????????????????????????????????????????????????????????????????????????????
     */
    private boolean ifFirstLocate = true;
    private LocationButton locationButton;
    private LegendPresenter legendPresenter;
    private ViewGroup ll_component_list;


    View mView;

    MapView mMapView;

    private ILayerView layerView;

    private PatrolLayerPresenter layerPresenter;
    private boolean loadLayersSuccess = true;

    private View ll_layer_url_init_fail;
    private TextView show_all_layer;
    private GridView gridView;
    private LayerAdapter layerAdapter;
    private String currComponentUrl = LayerUrlConstant.newComponentUrls[0];

    ProgressDialog pd;
    ViewGroup map_bottom_sheet;
    BottomSheetBehavior mBehavior;
    private ComponetListAdapter componetListAdapter;
    private View component_intro;
    private View component_detail_ll;
    private ViewGroup component_detail_container;
    private View btn_upload;
    private ArrayList<TableItem> tableItems = null;
    private ArrayList<Photo> photoList = new ArrayList<>();
    private String projectId;
    private TableViewManager tableViewManager;
    private List<Component> mComponentQueryResult = new ArrayList<>();
    private int currIndex = 0;
    private View btn_prev;
    private View btn_next;

    private View btn_back;

    /**
     * ???????????????????????????
     */
    private SelectLocationTouchListener defaultMapOnTouchListener;
    private MapHelper mapHelper;
    private Context mContext;
    private List<String> attribute = Arrays.asList("??????(????????????)","??????","?????????","?????????","????????????","????????????(????????????)","????????????","????????????(????????????)","????????????");

    public static EventDetailMapFragment getInstance(String layerUrl, String layerName, String usid, String objectId, double x, double y) {
        EventDetailMapFragment addComponentFragment2 = new EventDetailMapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("layerUrl", layerUrl);
        bundle.putString("layerName", layerName);
        bundle.putString("usid", usid);
        bundle.putDouble("x", x);
        bundle.putDouble("y", y);
        bundle.putString("objectId", objectId);
        addComponentFragment2.setArguments(bundle);
        return addComponentFragment2;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(mContext, R.layout.fragment_eventmap, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        mView = view;


        // Find MapView and add feature layers
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.setMapBackground(Color.WHITE, Color.TRANSPARENT, 0f, 0f);//???????????????????????????????????????
        /**
         * ?????????
         */
        final MapScaleView scaleView = (MapScaleView) view.findViewById(R.id.scale_view);
        scaleView.setMapView(mMapView);
        // Set listeners on MapView
        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onStatusChanged(final Object source, final STATUS status) {
                if (STATUS.INITIALIZED == status) {
                    if (source instanceof MapView) {
                        //todo ????????????
                        //mMapView.setScale(3029.5773243397634);
                        if (PatrolLayerPresenter.initScale != -1 && PatrolLayerPresenter.initScale != 0) {
                            mMapView.setScale(PatrolLayerPresenter.initScale);
                            scaleView.setScale(PatrolLayerPresenter.initScale);
                        }
                        if (PatrolLayerPresenter.longitude != 0 && PatrolLayerPresenter.latitude != 0) {
                            Point point = new Point(PatrolLayerPresenter.longitude, PatrolLayerPresenter.latitude);
                            mMapView.centerAt(point, true);
                        }
                        mMapView.setMaxScale(30);
//                        startLocate();
                        if (mapHelper == null) {
                            mapHelper = new MapHelper(mContext, mMapView);
                        }
                        queryModification();
                        if (layerPresenter != null) {
                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter.FEEDBACK_LAYER_NAME, true);
                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter.RIVER_FLOW_LAYER_NAME, true);
                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter.JOURNAL_LAYER_NAME, true);
                            LayerInfo planLayerInfo = getPlanLayerInfo();
                            if (planLayerInfo != null) {
                                layerPresenter.onClickOpacityButton(planLayerInfo.getLayerId(), planLayerInfo, 0.7f);
                            }
                        }
                    }
                }
            }
        });

        mMapView.setOnZoomListener(new OnZoomListener() {
            @Override
            public void preAction(float v, float v1, double v2) {

            }

            @Override
            public void postAction(float v, float v1, double v2) {
                scaleView.refreshScaleView();
            }
        });

        ll_layer_url_init_fail = view.findViewById(R.id.ll_layer_url_init_fail);
        onInitLayerUrlFinished(null);

        locationMarker = (LocationMarker) view.findViewById(R.id.locationMarker);
        locationButton = (LocationButton) view.findViewById(R.id.locationButton);
        locationButton.setIfShowCallout(false);
        locationButton.setMapView(mMapView);
        locationButton.setIfDrawLocation(false);
        mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
        /**
         * ??????????????????
         */
        ll_component_list = (ViewGroup) view.findViewById(R.id.ll_component_list);
        view.findViewById(R.id.ll_query_layer_grid).setVisibility(View.GONE);

        /**
         * ??????????????????
         */
        locationMarker.changeIcon(MapIconUtil.getResId(LayerUrlConstant.componentNames[0]));
        show_all_layer = (TextView) view.findViewById(R.id.show_all_layer);
        show_all_layer.setOnClickListener(this);
        gridView = (GridView) view.findViewById(R.id.gridview);
        layerAdapter = new LayerAdapter(getActivity());
        gridView.setAdapter(layerAdapter);
        layerAdapter.setOnItemClickListener(new OnRecyclerItemClickListener<String>() {
            @Override
            public void onItemClick(View view, int position, String selectedData) {
                currComponentUrl = selectedData;
                /**
                 * ??????????????????
                 */
                if (locationMarker != null && position < LayerUrlConstant.componentNames.length) {
                    locationMarker.changeIcon(MapIconUtil.getResId(LayerUrlConstant.componentNames[position]));
                }
            }

            @Override
            public void onItemLongClick(View view, int position, String selectedData) {

            }
        });

        view.findViewById(R.id.btn_layer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) mContext;
                if (activity instanceof IDrawerController) {
                    IDrawerController drawerController = (IDrawerController) activity;
                    drawerController.openDrawer(new IDrawerController.OnDrawerOpenListener() {
                        @Override
                        public void onOpened(View drawerView) {
                            showLayerList();
                            double scale = mMapView.getScale();
                        }
                    });
                }
            }
        });

        map_bottom_sheet = (ViewGroup) view.findViewById(R.id.map_bottom_sheet);
        mBehavior = BottomSheetBehavior.from(map_bottom_sheet);
        btn_prev = view.findViewById(R.id.prev);
        btn_next = view.findViewById(R.id.next);
        btn_back = view.findViewById(R.id.btn_back);
        component_intro = view.findViewById(R.id.intro);
        component_detail_ll = view.findViewById(R.id.detail_ll);
        component_detail_container = (ViewGroup) view.findViewById(R.id.detail_container);
        btn_upload = view.findViewById(R.id.btn_upload);
        btn_upload.setVisibility(View.GONE);


        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                onChangedBottomSheetState(newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currIndex--;
                if (currIndex < 0) {
                    btn_prev.setVisibility(View.GONE);
                    return;
                }
                showBottomSheet(mComponentQueryResult.get(currIndex));
                if (currIndex == 0) {
                    btn_prev.setVisibility(View.GONE);
                }
                if (mComponentQueryResult.size() > 1) {
                    btn_next.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currIndex++;
                if (currIndex > mComponentQueryResult.size()) {
                    btn_next.setVisibility(View.GONE);
                    return;
                }
                showBottomSheet(mComponentQueryResult.get(currIndex));
                if (currIndex == (mComponentQueryResult.size() - 1)) {
                    btn_next.setVisibility(View.GONE);
                }
                if (currIndex > 0) {
                    btn_prev.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableViewManager.uploadEdit();
            }
        });
        /*mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, @IBottomSheetBehavior.State int newState) {
                if(newState == STATE_EXPANDED){
                    component_intro.setVisibility(View.GONE);
                    component_detail_ll.setVisibility(View.VISIBLE);
                } else if(newState == STATE_COLLAPSED){
                    component_intro.setVisibility(View.VISIBLE);
                    component_detail_ll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });*/
        loadMap();


        /**
         * ??????
         */
        View ll_legend = view.findViewById(R.id.ll_legend);
        ll_legend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLegendPresenter();
                // legendPresenter.showLegends();

                if (layerPresenter != null) {
                    legendPresenter.showLegends(layerPresenter.getService().getVisibleLayerInfos());
                } else {
                    legendPresenter.showLegends();
                }


            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)mContext).finish();
            }
        });

        view.findViewById(R.id.btn_edit).setVisibility(View.GONE);

    }

    private String getLayerUrlByLayName(String layerName) {
        String url = "";
        List<LayerInfo> layerInfosFromLocal = LayerServiceFactory.provideLayerService(getActivity()).getLayerInfosFromLocal();
        if (ListUtil.isEmpty(layerInfosFromLocal)) {
            return url;
        }
        for (LayerInfo layerInfo : layerInfosFromLocal) {
            if (layerInfo.getLayerName().contains(layerName)) {
                url = layerInfo.getUrl();
            }
        }
        return url;
    }

    public void queryModification() {

        final double x = getArguments().getDouble("x");
        final double y = getArguments().getDouble("y");
        final String layerUrl = getArguments().getString("layerUrl");
        final String layerName = getArguments().getString("layerName");
        String usid = getArguments().getString("usid");
        String objectId = getArguments().getString("objectId");
        if (mapHelper == null) {
            mapHelper = new MapHelper(mContext, mMapView);
        }
//        mapHelper.drawPoints(new Point(x,y), true);
//        mapHelper.highLightGraphic(0, true);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("?????????????????????.....");
        progressDialog.show();
        UploadEventService uploadEventService = new UploadEventService(getActivity());
        if (!TextUtils.isEmpty(layerUrl)) {
            uploadEventService.queryComponentsWithLayerName(usid, objectId, layerName, layerUrl, new Callback2<Component>() {
                @Override
                public void onSuccess(Component completeTableInfo) {
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                    showBottomSheetForCheck(completeTableInfo);
                }

                @Override
                public void onFail(Exception error) {
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                    // ll_modified.setVisibility(View.GONE);
                    initGLayer();
                    Point point = new Point(x, y);
                    drawGeometry((Geometry) point, mGLayer, true, true);
                    if (mContext != null) {
                        ToastUtil.longToast(mContext.getApplicationContext(), "????????????????????????:"+error.getMessage());
                    }
                }
            });

        } else {
            uploadEventService.queryComponentsWithLayerName(usid, layerName, new Callback2<Component>() {
                @Override
                public void onSuccess(Component completeTableInfo) {
                    progressDialog.dismiss();
                    showBottomSheet(completeTableInfo);
                }

                @Override
                public void onFail(Exception error) {
                    progressDialog.dismiss();
                    // ll_modified.setVisibility(View.GONE);
                    initGLayer();
                    Point point = new Point(x, y);
                    drawGeometry((Geometry) point, mGLayer, true, true);
                    if (mContext != null) {
                        ToastUtil.longToast(mContext.getApplicationContext(), "????????????????????????:"+error.getMessage());
                    }
                }
            });
        }


    }


    private void initLegendPresenter() {
        if (legendPresenter == null) {
            ILegendView legendView = new ImageLegendView(getActivity());
            legendPresenter = new LegendPresenter(legendView, new OnlineLegendService(mContext.getApplicationContext()));
        }

    }

    public void loadMap() {
        IDrawerController drawerController = (IDrawerController) mContext;

        layerView = new PatrolLayerView2(mContext, mMapView, drawerController.getDrawerContainer());
        layerPresenter = new PatrolLayerPresenter(layerView, new AgwebPatrolLayerService2(mContext.getApplicationContext()));
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
    }

    /**
     * ????????????
     */
    public void startLocate() {
        if (mLocationManager == null) {
            mLocationManager = new PatrolLocationManager(mContext, mMapView);
        }
        this.mLocationManager.setMapView(mMapView);
        this.mLocationManager.setUseArcGisForLocation();
        mLocationManager.startLocate(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // drawLocationOnMap(location);
                final Point point = new Point(location.getLongitude(), location.getLatitude());
                //mLastSelectedLocation = point;
                if (mMapView.getMaxExtent() == null || mMapView.getSpatialReference() == null) {
                    return;
                }
                if (GeometryEngine.contains(mMapView.getMaxExtent(), point, mMapView.getSpatialReference())) {
                    if (ifFirstLocate) {
                        ifFirstLocate = false;
                        mMapView.centerAt(point, true);
                    }

                } else {
                    ToastUtil.shortToast(mContext, "?????????????????????????????????");
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


    /**
     * ??????????????????????????????
     *
     * @param location
     * @return
     */
    private void drawLocationOnMap(Location location) {
        if (mGLayerFroDrawLocation == null) {
            mGLayerFroDrawLocation = new GraphicsLayer();
            mMapView.addLayer(mGLayerFroDrawLocation);
        }
        Point point = new Point(location.getLongitude(), location.getLatitude());
        mGLayerFroDrawLocation.removeAll();
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mContext,
                mContext.getResources().getDrawable(com.augurit.agmobile.patrolcore.R.mipmap.patrol_location_symbol));
        Graphic graphic = new Graphic(new Point(location.getLongitude(), location.getLatitude()), pictureMarkerSymbol);
        mGLayerFroDrawLocation.addGraphic(graphic);
    }

    /**
     * ??????????????????
     */
    public void showLayerList() {
        if (layerPresenter != null) {
            layerPresenter.showLayerList();
        }
    }


    @Override
    public void onClick(View view) {
        // Handle presses on the action bar items
        switch (view.getId()) {
            case R.id.show_all_layer:

                break;
            default:
        }
    }

    public void onBackPressed() {
        if (map_bottom_sheet.getVisibility() == View.GONE) {
            ((Activity)mContext).finish();
            return;
        }
        if (mBehavior != null
                && map_bottom_sheet != null) {
            if (mBehavior.getState() == STATE_EXPANDED) {
                mBehavior.setState(STATE_COLLAPSED);
            } else if (mBehavior.getState() == STATE_COLLAPSED) {
                initGLayer();
                map_bottom_sheet.setVisibility(View.GONE);
                tableViewManager = null;
                if (locationMarker != null) {
                    locationMarker.setVisibility(View.GONE);
                }
                if (mMapView.getCallout().isShowing()) {
                    mMapView.getCallout().hide();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.unpause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_MAP_STATE, mMapView.retainState());
    }


//
//    private void showCallout(String address, Point point, final View.OnClickListener calloutSureButtonClickListener) {
//        final Point geometry = point;
//        final Callout callout = mMapView.getCallout();
//        View view = View.inflate(mContext, com.augurit.agmobile.patrolcore.R.layout.editmap_callout, null);
//        ((TextView) view.findViewById(com.augurit.agmobile.patrolcore.R.id.tv_listcallout_title)).setText(address);
//        view.findViewById(com.augurit.agmobile.patrolcore.R.id.btn_select_device).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (calloutSureButtonClickListener != null){
//                    calloutSureButtonClickListener.onClick(view);
//                }
//
////                Point geo = mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2,
////                        locationMarker.getY() + locationMarker.getHeight() / 2 - 140);
////                goAttrEdit(geo);
//            }
//        });
//        callout.setStyle(com.augurit.agmobile.patrolcore.R.xml.editmap_callout_style);
//        callout.setContent(view);
//        if (point == null) {
//            point = mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2, locationMarker.getY() + locationMarker.getHeight() / 2 - 140);
//        }
//        callout.show(point);
//    }
//
//
//    /**
//     * ??????????????????
//     *
//     * @param point
//     * @param callback1
//     */
//    private void requestLocation(Point point, SpatialReference spatialReference, final Callback1<String> callback1) {
//        SelectLocationService selectLocationService = new SelectLocationService(mContext, Locator.createOnlineLocator());
//        selectLocationService.parseLocation(new LatLng(point.getY(), point.getX()), spatialReference)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<DetailAddress>() {
//                    @Override
//                    public void call(DetailAddress detailAddress) {
//                        if (callback1 != null) {
//                            callback1.onCallback(detailAddress.getDetailAddress());
//                        }
//                    }
//                });
//    }

    private Point getMapCenterPoint() {
        //?????????????????????
        return mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2, locationMarker.getY() + locationMarker.getHeight() / 2);
    }


    /**
     * @return
     */
    private MapOnTouchListener getDefaultMapOnTouchListener() {
        if (defaultMapOnTouchListener == null) {
            defaultMapOnTouchListener = new DefaultTouchListener(mContext, mMapView, locationMarker, new View.OnClickListener() {
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
//        MapView mapView;
//        /**
//         * callout??????????????????????????????
//         */
//        private View.OnClickListener calloutSureButtonClickListener;
//
//        private boolean mShouldRequestNewAddress = true;
//
//        public DefaultTouchListener(Context context, MapView view, View.OnClickListener calloutSureButtonClickListener) {
//            super(context, view);
//            mapView = view;
//            this.calloutSureButtonClickListener = calloutSureButtonClickListener;
//        }
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            LogUtil.d("onTouch");
//            if (locationMarker.getVisibility() == View.GONE) {
//                return super.onTouch(v, event);
//            }
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_MOVE:
//                    if (mMapView.getCallout().isShowing()) {
//                        mMapView.getCallout().hide();
//                    }
//                    locationMarker.startUpAnimation(null);
//                    LogUtil.d("ACTION_DOWN");
//                    break;
//                case MotionEvent.ACTION_UP:
//                    centerToNewAddress(calloutSureButtonClickListener);
//                    /**
//                     * ???????????????????????????????????????????????????????????????????????????
//                     */
////                    if (hasZoomBefore){
////                        hasZoomBefore = false;
////                        locationMarker.startDownAnimation(null);
////                    }else {
////                        centerToNewAddress();
////                    }
//                    break;
//                case MotionEvent.ACTION_CANCEL:
//                    centerToNewAddress(calloutSureButtonClickListener);
//                    break;
//                default:
//                    break;
//            }
//            return super.onTouch(v, event);
//        }
//
//        private void centerToNewAddress(final View.OnClickListener calloutSureButtonClickListener) {
//            locationMarker.startDownAnimation(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    Point point = getMapCenterPoint();
//                    mLastSelectedLocation = point;
//                    showCallout("?????????.....", null, calloutSureButtonClickListener);
//                    requestLocation(point, mMapView.getSpatialReference(), new Callback1<String>() {
//                        @Override
//                        public void onCallback(String s) {
//                            showCallout(s, null, calloutSureButtonClickListener);
//                        }
//                    });
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//        }
//
//
//        @Override
//        public boolean onPinchPointersDown(MotionEvent event) {
//            // LogUtil.d("onPinchPointersDown");
//            mShouldRequestNewAddress = false;
//            return super.onPinchPointersDown(event);
//        }
//
//        @Override
//        public boolean onPinchPointersMove(MotionEvent event) {
//            LogUtil.d("onPinchPointersMove");
//            return super.onPinchPointersMove(event);
//        }
//
//        @Override
//        public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
//            LogUtil.d("onDragPointerMove");
//            return super.onDragPointerMove(from, to);
//        }
//
//        @Override
//        public boolean onPinchPointersUp(MotionEvent event) {
//            LogUtil.d("onPinchPointersUp");
//            if (mLastSelectedLocation != null && locationMarker.getVisibility() == View.VISIBLE) {
//                /**
//                 * ??????????????????????????????????????????
//                 */
//                mMapView.centerAt(mLastSelectedLocation, true);
//            }
//            mShouldRequestNewAddress = true;
//            hasZoomBefore = true;
//            return super.onPinchPointersUp(event);
//        }
//
//        @Override
//        public boolean onDoubleTap(MotionEvent point) {
//            if (mLastSelectedLocation != null && locationMarker.getVisibility() == View.VISIBLE) {
//                /**
//                 * ??????????????????????????????????????????
//                 */
//                mMapView.centerAt(mLastSelectedLocation, true);
//                requestLocation(mLastSelectedLocation, mMapView.getSpatialReference(), new Callback1<String>() {
//                    @Override
//                    public void onCallback(String s) {
//                        showCallout(s, null, calloutSureButtonClickListener);
//                    }
//                });
//            }
//            mShouldRequestNewAddress = true;
//            hasZoomBefore = true;
//            return super.onDoubleTap(point);
//        }
//
//
//        @Override
//        public boolean onLongPressUp(MotionEvent point) {
////            handleTap(point);
//
//            super.onLongPressUp(point);
//            return true;
//        }

        @Override
        public boolean onSingleTap(final MotionEvent e) {
            if (true) {
                return true;
            }

            initGLayer();
            hideBottomSheet();
            // ll_modified.setVisibility(View.GONE);
            handleTap(e);
            return true;
        }


        private void query(MotionEvent e) {
            mComponentQueryResult.clear();
            btn_next.setVisibility(View.GONE);
            btn_prev.setVisibility(View.GONE);
            currIndex = 0;
            final Point point = mMapView.toMapPoint(e.getX(), e.getY());
            final List<LayerInfo> layerInfoList = new ArrayList<>();
            for (String url : LayerUrlConstant.newComponentUrls) {
                LayerInfo layerInfo = new LayerInfo();
                layerInfo.setUrl(url);
                layerInfoList.add(layerInfo);
            }
            Geometry geometry = GeometryEngine.buffer(point, mMapView.getSpatialReference(), 40 * mMapView.getResolution(), null);
            ComponentService componentMaintenanceService = new ComponentService(mContext.getApplicationContext());
            String oldLayerUrl = LayerUrlConstant.getLayerUrlByLayerName(LayerUrlConstant.getLayerNameByNewLayerUrl(currComponentUrl));
            componentMaintenanceService.queryComponents(geometry, oldLayerUrl, currComponentUrl, new Callback2<List<QueryFeatureSet>>() {
                @Override
                public void onSuccess(List<QueryFeatureSet> queryFeatureSetList) {
                    if (ListUtil.isEmpty(queryFeatureSetList)) {
                        return;
                    }
                    mComponentQueryResult = new ArrayList<Component>();
                    for (QueryFeatureSet queryFeatureSet : queryFeatureSetList) {
                        FeatureSet featureSet = queryFeatureSet.getFeatureSet();
                        Graphic[] graphics = featureSet.getGraphics();
                        if (graphics == null
                                || graphics.length <= 0) {
                            continue;
                        }

                        for (Graphic graphic : graphics) {
                            Component component = new Component();
                            component.setLayerUrl(queryFeatureSet.getLayerUrl());
                            component.setLayerName(queryFeatureSet.getLayerName());
                            component.setDisplayFieldName(featureSet.getDisplayFieldName());
//                            component.setFieldAlias(featureSet.getFieldAliases());
//                            component.setFields(featureSet.getFields());
                            component.setGraphic(graphic);
                            Object o = graphic.getAttributes().get(featureSet.getObjectIdFieldName());
                            if (o != null && o instanceof Integer) {
                                component.setObjectId((Integer) o); //????????????objectId?????????integer???
                            }
                            mComponentQueryResult.add(component);
                        }
                    }

                    showComponentsOnBottomSheet(mComponentQueryResult);
                }

                @Override
                public void onFail(Exception error) {

                }
            });
            /*componentMaintenanceService.queryComponents(geometry, currComponentUrl, new Callback2<QueryFeatureSet>() {
                @Override
                public void onSuccess(QueryFeatureSet queryFeatureSet) {
                    if (queryFeatureSet == null) {
                        return;
                    }
                    FeatureSet featureSet = queryFeatureSet.getFeatureSet();
                    Graphic[] graphics = featureSet.getGraphics();
                    if (graphics == null
                            || graphics.length <= 0) {
                        return;
                    }
                    mComponentQueryResult = new ArrayList<Component>();
                    for (Graphic graphic : graphics) {
                        Component component = new Component();
                        component.setLayerUrl(queryFeatureSet.getLayerUrl());
                        component.setLayerName(queryFeatureSet.getLayerName());
                        component.setDisplayFieldName(featureSet.getDisplayFieldName());
                        component.setFieldAlias(featureSet.getFieldAliases());
                        component.setFields(featureSet.getFields());
                        component.setGraphic(graphic);
                        Object o = graphic.getAttributes().get(featureSet.getObjectIdFieldName());
                        if (o != null && o instanceof Integer) {
                            component.setObjectId((Integer) o); //????????????objectId?????????integer???
                        }
                        mComponentQueryResult.add(component);
                    }

                    showComponentsOnBottomSheet(mComponentQueryResult);
                }

                @Override
                public void onFail(Exception error) {

                }
            });*/

        }

        @Deprecated
        private void identify(MotionEvent e) {
            /*final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("?????????????????????...");
            progressDialog.show();*/
            Point point = mMapView.toMapPoint(e.getX(), e.getY());
            LayerInfo layerInfo = new LayerInfo();
            layerInfo.setUrl(LayerUrlConstant.mapServerUrl);
            ArrayList<LayerInfo> childs = new ArrayList<>();
            LayerInfo child1 = new LayerInfo();
            child1.setLayerId(0);
            childs.add(child1);
            LayerInfo child2 = new LayerInfo();
            child2.setLayerId(1);
            childs.add(child2);
            LayerInfo child3 = new LayerInfo();
            child3.setLayerId(2);
            childs.add(child3);
            LayerInfo child4 = new LayerInfo();
            child4.setLayerId(3);
            childs.add(child4);
            LayerInfo child5 = new LayerInfo();
            child5.setLayerId(4);
            childs.add(child5);
            LayerInfo child6 = new LayerInfo();
            child6.setLayerId(5);
            childs.add(child6);
            LayerInfo child7 = new LayerInfo();
            child7.setLayerId(6);
            childs.add(child7);
            LayerInfo child8 = new LayerInfo();
            child8.setLayerId(7);
            childs.add(child8);
            layerInfo.setChildLayer(childs);
            List<LayerInfo> layerInfoList = new ArrayList<>();
            layerInfoList.add(layerInfo);
            IIdentifyService identifyService = IdentifyServiceFactory.provideLayerService();
            identifyService.selectedFeature(((Activity)mContext), mMapView,
                    layerInfoList, point, 25, new Callback2<AMFindResult[]>() {
                        @Override
                        public void onSuccess(final AMFindResult[] amFindResults) {
//                            progressDialog.dismiss();

                            List<AMFindResult> findResults = new ArrayList<AMFindResult>();
                            if (amFindResults != null && amFindResults.length >= 1) {
                                //???????????????????????????
                                for (AMFindResult findResult : amFindResults) {
//                                    if (findResult.getGeometry().getType() == Geometry.Type.POINT) {
                                    findResults.add(findResult);
//                                    }
                                }
                            }

                            if (findResults.size() >= 1) {
                                AMFindResult findResult = findResults.get(0);
                                initGLayer();
                                drawGeometry(findResults.get(0).getGeometry(), mGLayer, true, true);
                                Component component = new Component();
                                component.setLayerUrl(LayerUrlConstant.getNewLayerUrlByLayerName(findResult.getLayerName()));
                                component.setLayerName(findResult.getLayerName());
                                component.setDisplayFieldName(findResult.getDisplayFieldName());
                                component.setGraphic(new Graphic(findResult.getGeometry(), null, findResult.getAttributes()));
                                loadCompleteData(component);
                            }
                        }

                        @Override
                        public void onFail(Exception error) {
                        }
                    });
        }

        /***
         * Handle a tap on the map (or the end of a magnifier long-press event).
         *
         * @param e The point that was tapped.
         */
        private void handleTap(final MotionEvent e) {
            if (locationMarker != null) {
                locationMarker.setVisibility(View.GONE);
            }
            if (mMapView.getCallout().isShowing()) {
                mMapView.getCallout().hide();
            }

            if (mapHelper != null && mapHelper.getGraphicId(e.getX(), e.getY(), 15) != null &&
                    mapHelper.getGraphicId(e.getX(), e.getY(), 15).length > 0) {
                hideBottomSheet();
            } else {
                query(e);
            }
        }

    }

    GraphicsLayer mGLayer = null;
    Graphic graphic = null;

    private void initGLayer() {
        if (mGLayer == null) {
            mGLayer = new GraphicsLayer();
            mMapView.addLayer(mGLayer);
        } else {
            mGLayer.removeAll();
        }
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
            mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        } else {
            mMapView.setExtent(geometry);
        }
    }


    /**
     * ?????????????????????
     *
     * @param graphicsLayer
     * @param ifRemoveAll   ?????????????????????????????????
     * @param geometry
     */
    private int drawGeometry(Geometry geometry, Symbol symbol, GraphicsLayer graphicsLayer, boolean ifRemoveAll, boolean ifCenter) {

        if (graphicsLayer == null) {
            return -1;
        }

        if (ifRemoveAll) {
            graphicsLayer.removeAll();
        }

        if (ifCenter) {
            mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        }

        if (symbol != null) {
            Graphic graphic = new Graphic(geometry, symbol);
            if (geometry.getType() == Geometry.Type.POLYLINE) { //???????????????????????????????????????????????????????????????
                this.graphic = graphic;
            }
            return graphicsLayer.addGraphic(graphic);
        }
        return -1;

    }

    @NonNull
    private Symbol getPointSymbol() {
        Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), com.augurit.agmobile.patrolcore.R.mipmap.ic_select_location, null);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mContext, drawable);// xjx ????????????api19???????????????drawable
        pictureMarkerSymbol.setOffsetY(16);
        return pictureMarkerSymbol;
    }

    private void onChangedBottomSheetState(int state) {
        if (state == STATE_EXPANDED) {
            if (component_intro.getVisibility() == View.VISIBLE) {
                showBottomSheetContent(component_detail_container.getId());
                showDetail();
            }

        } else if (state == STATE_COLLAPSED) {
            showBottomSheetContent(component_intro.getId());

        }
    }

    private void showBottomSheetContent(int viewId) {
        if (viewId == component_intro.getId()) {
            component_intro.setVisibility(View.VISIBLE);
            component_detail_container.setVisibility(View.GONE);
        } else if (viewId == component_detail_container.getId()) {
            component_intro.setVisibility(View.GONE);
            component_detail_container.setVisibility(View.VISIBLE);
        }
    }


    private void hideBottomSheet() {
        if (map_bottom_sheet != null) {
            map_bottom_sheet.setVisibility(View.GONE);
        }
    }

    private void showComponentsOnBottomSheet(List<Component> componentQueryResult) {
        if (componentQueryResult.size() > 1) {
            btn_next.setVisibility(View.VISIBLE);
        }
        initGLayer();
        drawGeometry(componentQueryResult.get(0).getGraphic().getGeometry(), mGLayer, true, true);
        showBottomSheet(mComponentQueryResult.get(0));

    }

    @Deprecated
    private void showBottomSheet2(final Component component) {
        initGLayer();
        drawGeometry(component.getGraphic().getGeometry(), mGLayer, true, true);

        String errorInfo = null;
        Object oErrorInfo = component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ERROR_INFO);

        if (oErrorInfo != null) {
            errorInfo = oErrorInfo.toString();
        }

        TextView titleTv = (TextView) map_bottom_sheet.findViewById(R.id.title);
        TextView dateTv = (TextView) map_bottom_sheet.findViewById(R.id.date);
        TextView sortTv = (TextView) map_bottom_sheet.findViewById(R.id.sort);
        TextView subtypeTv = (TextView) map_bottom_sheet.findViewById(R.id.subtype);
        TextView field1Tv = (TextView) map_bottom_sheet.findViewById(R.id.field1);
        TextView field2Tv = (TextView) map_bottom_sheet.findViewById(R.id.field2);
        TextView field3Tv = (TextView) map_bottom_sheet.findViewById(R.id.field3);
        TextView addrTv = (TextView) map_bottom_sheet.findViewById(R.id.addr);
        TextView code = (TextView) map_bottom_sheet.findViewById(R.id.code);


//        addrTv.setVisibility(View.GONE);
        TextView tv_errorinfo = (TextView) map_bottom_sheet.findViewById(R.id.tv_errorinfo);
        String name = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.NAME));

        String type = LayerUrlConstant.getLayerNameByUnknownLayerUrl(component.getLayerUrl());

        String sort = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SORT));

        String subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE));

        String title = StringUtil.getNotNullString(name, "") + "  " + StringUtil.getNotNullString(type, "");
        titleTv.setText(title);

        String date = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.UPDATEDATE));
        String formatDate = "";
        try {
            formatDate = TimeUtil.getStringTimeYMD(new Date(Long.valueOf(date)));
        } catch (Exception e) {

        }
        dateTv.setText(StringUtil.getNotNullString(formatDate, ""));

        sortTv.setText(StringUtil.getNotNullString(sort, ""));

        subtypeTv.setText(StringUtil.getNotNullString(subtype, ""));

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

        String address = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ADDR));
        addrTv.setText(StringUtil.getNotNullString(address, ""));

        //???????????????
        String codeValue = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.CODE));
        codeValue = codeValue.trim();
        if (component.getLayerName().equals("??????")) {
            if (!codeValue.isEmpty()) {
                code.setText("???????????????" + ":" + StringUtil.getNotNullString(codeValue, ""));
            } else {
//                code.setText("???????????????" + ":" + "???");
                code.setText("???????????????" + ":" + "");
            }
        } else {
            code.setVisibility(View.GONE);
        }


        //if (errorInfo == null) {
        tv_errorinfo.setVisibility(View.GONE);
        // } else {
        //     tv_errorinfo.setVisibility(View.VISIBLE);
        //     tv_errorinfo.setText(errorInfo);
        // }
        showBottomSheetContent(component_intro.getId());
        map_bottom_sheet.setVisibility(View.VISIBLE);
        mBehavior.setState(STATE_COLLAPSED);
//        loadCompleteDataAsync(component);
    }

    private void showBottomSheet1(final Component component) {
        initGLayer();
        mGLayer.removeAll();
        drawGeometry(component.getGraphic().getGeometry(), mGLayer, false, true);

        String errorInfo = null;
        Object oErrorInfo = component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ERROR_INFO);

        if (oErrorInfo != null) {
            errorInfo = oErrorInfo.toString();
        }

        TextView titleTv = (TextView) map_bottom_sheet.findViewById(R.id.title);
        TextView dateTv = (TextView) map_bottom_sheet.findViewById(R.id.date);
        TextView sortTv = (TextView) map_bottom_sheet.findViewById(R.id.sort);
        TextView subtypeTv = (TextView) map_bottom_sheet.findViewById(subtype);
        TextView field1Tv = (TextView) map_bottom_sheet.findViewById(R.id.field1);
        TextView field2Tv = (TextView) map_bottom_sheet.findViewById(R.id.field2);
        TextView field3Tv = (TextView) map_bottom_sheet.findViewById(R.id.field3);
        TextView addrTv = (TextView) map_bottom_sheet.findViewById(R.id.addr);
        TextView code = (TextView) map_bottom_sheet.findViewById(R.id.code);

//        addrTv.setVisibility(View.GONE);
        TextView tv_errorinfo = (TextView) map_bottom_sheet.findViewById(R.id.tv_errorinfo);
        TextView tv_parent_org_name = (TextView) map_bottom_sheet.findViewById(R.id.tv_parent_org_name);

        String name = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.NAME));

//        final String type = LayerUrlConstant.getLayerNameByUnknownLayerUrl(component.getLayerUrl());
        final String type = (String) component.getGraphic().getAttributes().get("LAYER_NAME");


        String subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
//        String subtype = String.valueOf(TextUtils.isEmpty((String) component.getGraphic().getAttributes().get("ORIGIN_ATTR_ONE"))?component.getGraphic().getAttributes().get("ATTR_ONE"):component.getGraphic().getAttributes().get("ORIGIN_ATTR_ONE"));
        String usid = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.OBJECTID));

        String title = StringUtil.getNotNullString(type, "") + "(" + (TextUtils.isEmpty(usid) || "null".equals(usid) ? component.getObjectId() : usid) + ")";
        titleTv.setText(title);

        String date = String.valueOf(component.getGraphic().getAttributes().get("UPDATE_DATE"));
        String formatDate = "";
        try {
            formatDate = TimeUtil.getStringTimeYMD(new Date(Long.valueOf(date)));
        } catch (Exception e) {

        }
        dateTv.setText(StringUtil.getNotNullString(formatDate, ""));

//        String sort = String.valueOf(TextUtils.isEmpty((String)component.getGraphic().getAttributes().get("ORIGIN_ATTR_TWO"))?component.getGraphic().getAttributes().get("ATTR_TWO"):component.getGraphic().getAttributes().get("ORIGIN_ATTR_TWO"));
        String sort = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_TWO));

        int color = ResourcesCompat.getColor(getResources(), R.color.mark_light_purple, null);

        if (sort.contains("????????????")) {
            color = ResourcesCompat.getColor(getResources(), R.color.mark_light_purple, null);
        } else if (sort.contains("??????")) {
            color = ResourcesCompat.getColor(getResources(), R.color.progress_line_green, null);
        } else if (sort.contains("??????")) {
            color = ResourcesCompat.getColor(getResources(), R.color.agmobile_red, null);
        }
        sortTv.setTextColor(color);
//
//        if (sort.contains("????????????")) {
//            sortTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.mark_light_purple, null));
//        } else if (sort.contains("??????")) {
//            sortTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.progress_line_green, null));
//        } else if (sort.contains("??????")) {
//            sortTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.agmobile_red, null));
//        }

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
        if ("?????????".equals(type)) {
            String feature = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
            sortTv.setText(StringUtil.getNotNullString(feature, ""));
        }
        if ("?????????".equals(type)) {
            String style = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE));
            subtypeTv.setText(StringUtil.getNotNullString(style, ""));
            subtypeTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.dust_grey, null));
        } else {
            subtypeTv.setText(StringUtil.getNotNullString(subtype, ""));
        }

        if ("?????????".equals(type)) {
            field3Tv.setVisibility(View.GONE);
        } else {
            field3Tv.setVisibility(View.VISIBLE);
        }

        /**
         * ???????????????
         */
        String field3 = "";
        if ("??????".equals(component.getLayerName())) {
            field3 = "????????????: " + String.valueOf(TextUtils.isEmpty((String) component.getGraphic().getAttributes().get("ATTR_THREE")) ? component.getGraphic().getAttributes().get("ORIGIN_ATTR_THREE") : component.getGraphic().getAttributes().get("ATTR_THREE"));
        } else if ("?????????".equals(component.getLayerName())) {
            field3 = "????????????: " + String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.RIVER));
        }
        field3Tv.setText(field3);
        tv_parent_org_name.setVisibility(View.GONE);
        field3Tv.setVisibility(View.GONE);

        final String address = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ADDR));
        addrTv.setText("????????????" + "???" + StringUtil.getNotNullString(address, ""));
        //???????????????
        String codeValue = String.valueOf(component.getGraphic().getAttributes().get("ATTR_FIVE"));
        codeValue = codeValue.trim();
        if ("??????".equals(component.getLayerName())) {
            if (!codeValue.isEmpty()) {
                code.setText("???????????????" + ":" + StringUtil.getNotNullString(codeValue, ""));
            } else {
//                code.setText("???????????????" + ":" +"???");
                code.setText("???????????????" + ":" + "");
            }
            code.setVisibility(View.VISIBLE);
        } else {
            code.setVisibility(View.GONE);
        }

        tv_errorinfo.setVisibility(View.GONE);

        String parentOrg = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FOUR));

        View line = map_bottom_sheet.findViewById(R.id.line);

        if ("?????????".equals(type)) {
            parentOrg = String.valueOf(component.getGraphic().getAttributes().get("ATTR_THREE"));
            sortTv.setVisibility(View.GONE);
            code.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            subtypeTv.setText("???????????????" + component.getGraphic().getAttributes().get("ATTR_ONE"));
            field3 = "??????????????????" + component.getGraphic().getAttributes().get("ATTR_FOUR");
        } else {
            sortTv.setVisibility(View.VISIBLE);
            code.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
        }
        field3Tv.setText(field3);
        field3Tv.setVisibility(View.GONE);
        tv_parent_org_name.setVisibility(View.GONE);

        tv_parent_org_name.setText("???????????????" + StringUtil.getNotNullString(parentOrg, ""));
        ////// showBottomSheetContent(component_intro.getId());
        map_bottom_sheet.setVisibility(View.VISIBLE);
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    /**
     * ????????????????????????????????????BottomSheet???
     */
    private void showBottomSheet(final Component component) {
        initGLayer();

        drawGeometry(component.getGraphic().getGeometry(), mGLayer, false, true);

        String errorInfo = null;
        Object oErrorInfo = component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ERROR_INFO);

        if (oErrorInfo != null) {
            errorInfo = oErrorInfo.toString();
        }

        TextView titleTv = (TextView) map_bottom_sheet.findViewById(R.id.title);
        TextView dateTv = (TextView) map_bottom_sheet.findViewById(R.id.date);
        TextView sortTv = (TextView) map_bottom_sheet.findViewById(R.id.sort);
        TextView subtypeTv = (TextView) map_bottom_sheet.findViewById(subtype);
        TextView field1Tv = (TextView) map_bottom_sheet.findViewById(R.id.field1);
        TextView field2Tv = (TextView) map_bottom_sheet.findViewById(R.id.field2);
        TextView field3Tv = (TextView) map_bottom_sheet.findViewById(R.id.field3);
        TextView addrTv = (TextView) map_bottom_sheet.findViewById(R.id.addr);

        TextView code = (TextView) map_bottom_sheet.findViewById(R.id.code);


//        addrTv.setVisibility(View.GONE);
        TextView tv_errorinfo = (TextView) map_bottom_sheet.findViewById(R.id.tv_errorinfo);
        TextView tv_parent_org_name = (TextView) map_bottom_sheet.findViewById(R.id.tv_parent_org_name);
        String layerName = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(ComponentFieldKeyConstant.LAYER_NAME), "");
        if (StringUtil.isEmpty(layerName)) {
            layerName = component.getLayerName();
        }
        String name = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.NAME));

        String type = layerName;

        //huozhijiewaigou
        String subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
        String usid = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.USID));
        String objectId = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.OBJECTID));

        if (StringUtil.isEmpty(usid) || "null".equals(usid)) {
            usid = objectId;
        }
        final String title = StringUtil.getNotNullString(type, "") + "(" + usid + ")";
        titleTv.setText(title);

        String date = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.UPDATEDATE));
        String formatDate = "";
        try {
            formatDate = TimeUtil.getStringTimeYMD(new Date(Long.valueOf(date)));
        } catch (Exception e) {

        }
//        dateTv.setText(StringUtil.getNotNullString(formatDate, ""));
        dateTv.setText(StringUtil.getNotNullString(null, ""));

        String sort = "";
        if ("????????????".equals(layerName)) {
            sort = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SORT));
        } else {
            sort = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_TWO));
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
//
//        if (sort.contains("????????????")) {
//            sortTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.mark_light_purple, null));
//        } else if (sort.contains("??????")) {
//            sortTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.progress_line_green, null));
//        } else if (sort.contains("??????")) {
//            sortTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.agmobile_red, null));
//        }

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
        if (layerName.equals("?????????")) {
            String feature = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
            sortTv.setText(StringUtil.getNotNullString(feature, ""));
        }
        if ("?????????".equals(type)) {
            String style = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE));
            subtypeTv.setText(StringUtil.getNotNullString(style, ""));
            subtypeTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.dust_grey, null));
        } else {
            subtypeTv.setText(StringUtil.getNotNullString(subtype, ""));
        }

        if ("?????????".equals(type)) {
            field3Tv.setVisibility(View.GONE);
        } else {
            field3Tv.setVisibility(View.VISIBLE);
        }

        /**
         * ???????????????
         */
        //???????????????
        String codeValue = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FIVE));
        if (!StringUtil.isEmpty(codeValue)) {
            codeValue = codeValue.trim();
        }

        if ("??????".equals(layerName)) {
            code.setVisibility(View.VISIBLE);
            if (!codeValue.isEmpty()) {
                code.setText("???????????????" + ":" + StringUtil.getNotNullString(codeValue, ""));
            } else {
                code.setText("???????????????" + ":" + "");
            }
        } else {
            code.setVisibility(View.GONE);
        }

        /**
         * ???????????????
         */
        String field3 = "";
        if ("??????".equals(layerName)) {
            field3 = "????????????: " + StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE), "");
        } else if ("?????????".equals(layerName)) {
            field3 = "????????????: " + String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.RIVER));
        }
        field3Tv.setText(field3);
        tv_parent_org_name.setVisibility(View.GONE);
        field3Tv.setVisibility(View.GONE);

        final String address = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ADDR));
        addrTv.setText("????????????" + "???" + StringUtil.getNotNullString(address, ""));

        // if (errorInfo == null) {
        tv_errorinfo.setVisibility(View.GONE);
        //  } else {
        //      tv_errorinfo.setVisibility(View.VISIBLE);
        //     tv_errorinfo.setText(errorInfo + "?");
        // }


        String parentOrg = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FOUR));
        tv_parent_org_name.setText("???????????????" + StringUtil.getNotNullString(parentOrg, ""));

        showBottomSheetContent(component_intro.getId());
        map_bottom_sheet.setVisibility(View.VISIBLE);
        mBehavior.setState(STATE_COLLAPSED);
//        loadCompleteDataAsync(component);
    }

    private void showDetail() {
        if (tableItems == null
                || projectId == null) {
            return;
        }
        if (tableViewManager == null) {
//            component_detail_container.removeAllViews();
            //??????????????????
            tableViewManager =
                    new TableViewManager(mContext, component_detail_container,
                            false, TableState.READING, tableItems,
                            photoList, projectId, null, null);
            tableViewManager.setUploadEditCallback(new Callback2() {
                @Override
                public void onSuccess(Object o) {
                    mBehavior.setState(STATE_COLLAPSED);
                    map_bottom_sheet.setVisibility(View.GONE);
                    if (locationMarker != null) {
                        locationMarker.setVisibility(View.GONE);
                    }
                    if (mMapView.getCallout().isShowing()) {
                        mMapView.getCallout().hide();
                    }
                }

                @Override
                public void onFail(Exception error) {

                }
            });

            tableViewManager.changeSelectLocationItemView(new NoMapSelectLocationEditStateTableItemView2(getActivity()));

            tableViewManager.setOnMapItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBehavior != null) {
                        //       mBehavior.setState(STATE_COLLAPSED);
                        map_bottom_sheet.setVisibility(View.GONE);
                        //         mMapView.setScale(mMapView.getMinScale());
                        TableItem tableX = tableViewManager.getTableItemByField1("X");
                        TableItem tableY = tableViewManager.getTableItemByField1("Y");
                        if (tableX == null || tableY == null) {
                            /**
                             * ?????????????????????
                             */
                            if (TableViewManager.geometry != null &&
                                    TableViewManager.geometry.getType() == Geometry.Type.POLYLINE) {
                                //???????????????
                                editLine((Polyline) TableViewManager.geometry);
                                return;
                            } else {
                                return;
                            }

                        }


                        /**
                         * ??????x,y??????????????????geometry???????????????????????????????????????????????????
                         */
                        if (tableX.getValue() == null || tableY.getValue() == null) {
                            if (TableViewManager.geometry != null && TableViewManager.geometry.getType() == Geometry.Type.POINT) {
                                tableX.setValue(((Point) TableViewManager.geometry).getX() + "");
                                tableY.setValue(((Point) TableViewManager.geometry).getY() + "");
                            } else {
                                return;
                            }
                        }

                        try {
                            double x = Double.valueOf(tableX.getValue());
                            double y = Double.valueOf(tableY.getValue());
                            Point point = new Point(x, y);
                            mMapView.centerAt(point, true);
                            if (locationMarker != null) {
                                locationMarker.setVisibility(View.VISIBLE);
                                locationMarker.bounce();

                                SelectLocationTouchListener selectLocationTouchListener = new SelectLocationTouchListener(mContext, mMapView, locationMarker, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //??????callout
                                        if (mMapView.getCallout().isShowing()) {
                                            mMapView.getCallout().animatedHide();
                                        }

                                        //????????????????????????????????????
                                        mBehavior.setState(STATE_EXPANDED);
                                        map_bottom_sheet.setVisibility(View.VISIBLE);
                                        showDetail();
                                        if (tableViewManager != null) {
                                            //????????????????????????TableViewManager
                                            TableViewManager.geometry = mLastSelectedLocation;
                                            EditText x = tableViewManager.getEditTextViewByField1Type("X");
                                            TableItem tableX = tableViewManager.getTableItemByField1("X");
                                            if (x != null && mLastSelectedLocation != null) {
                                                x.setText(mLastSelectedLocation.getX() + "");
                                            }
                                            /**
                                             * ?????????tableItem??????????????????????????????{@link EventDetailMapFragment#showDetail()}??????????????????tableItem?????????????????????????????????????????????????????????????????????
                                             * ???????????????????????????????????????????????????????????????????????????????????????
                                             */
                                            if (tableX != null && mLastSelectedLocation != null) {
                                                tableX.setValue(mLastSelectedLocation.getX() + "");
                                            }

                                            EditText y = tableViewManager.getEditTextViewByField1Type("Y");
                                            TableItem tableY = tableViewManager.getTableItemByField1("Y");
                                            if (y != null && mLastSelectedLocation != null) {
                                                y.setText(mLastSelectedLocation.getY() + "");
                                            }
                                            if (tableY != null && mLastSelectedLocation != null) {
                                                tableY.setValue(mLastSelectedLocation.getY() + "");
                                            }
                                        }

                                        //????????????????????????
                                        Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), R.mipmap.common_ic_location_orange, null);
                                        Symbol symbol = new PictureMarkerSymbol(mContext, drawable);
                                        drawGeometry(mLastSelectedLocation, symbol, mGLayer, true, true);

                                        //??????????????????
                                        mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                                        //??????marker
                                        locationMarker.setVisibility(View.GONE);

                                    }
                                });

                                selectLocationTouchListener.registerLocationChangedListener(new SelectLocationTouchListener.OnSelectedLocationChangedListener() {
                                    @Override
                                    public void onLocationChanged(Point newLocation) {
                                        mLastSelectedLocation = newLocation;
                                    }

                                    @Override
                                    public void onAddressChanged(DetailAddress detailAddress) {
                                        if (tableViewManager != null) {
                                            tableViewManager.setAddress(detailAddress.getDetailAddress());
                                        }
                                    }
                                });

                                mMapView.setOnTouchListener(selectLocationTouchListener);
                            }
                        } catch (NumberFormatException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            });
        }

    }

    /**
     * ????????????
     *
     * @param polyline
     */

    private void editLine(final Polyline polyline) {

        //????????????????????????????????????
        mGLayer.removeAll();
        final EditLineReEditStateMapListener editLineReEditStateMapListener = new EditLineReEditStateMapListener(mContext, mMapView, polyline, 0, ll_component_list) {
            @Override
            public boolean onDoubleTap(MotionEvent point) {

                Graphic currentGraphic = this.getCurrentGraphic();
                mGLayer.addGraphic(currentGraphic);
                this.clearAllGrapic();
                map_bottom_sheet.setVisibility(View.VISIBLE);
                mBehavior.setState(STATE_EXPANDED);
                showDetail();
                //??????????????????
                mMapView.setOnTouchListener(getDefaultMapOnTouchListener());

                return true;
            }
        };
        editLineReEditStateMapListener.setOnGraphicChangedListener(new OnGraphicChangedListener() {
            @Override
            public void onGraphicChanged(Graphic graphic) {

            }

            @Override
            public void onAddressChanged(DetailAddress detailAddress) {
                if (tableViewManager != null && detailAddress != null) {
                    tableViewManager.setAddress(detailAddress.getDetailAddress());
                }
            }


            @Override
            public void onGraphicClear() {

            }
        });
        mMapView.setOnTouchListener(editLineReEditStateMapListener);
    }


    private void loadCompleteDataAsync(final Component component) {
        tableItems = null;
        projectId = null;
        tableViewManager = null;
        component_detail_container.removeAllViews();
        photoList.clear();
        final TableDataManager tableDataManager = new TableDataManager(mContext.getApplicationContext());
        List<Project> projects = tableDataManager.getProjectFromDB();
        Project project = null;
        for (Project p : projects) {
            if (p.getName().equals(component.getLayerName())) {
                project = p;
            }
        }
        if (project == null) {
            ToastUtil.shortToast(mContext, "???????????????????????????");
            return;
        }
        projectId = project.getId();
        String getFormStructureUrl = BaseInfoManager.getBaseServerUrl(getActivity()) + "rest/report/rptform";
        tableDataManager.getTableItemsFromNet(project.getId(), getFormStructureUrl, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                TableItems tmp = (TableItems) data;
                if (tmp.getResult() != null) {
                    tableItems = new ArrayList<TableItem>();
                    tableItems.addAll(tmp.getResult());
                    //   tableDataManager.setTableItemsToDB(tableItems);
                    //?????????????????????
                    Realm realm = Realm.getDefaultInstance();
                    ProjectTable projectTable = new ProjectTable();
                    projectTable.setId(projectId);
                    realm.beginTransaction();
                    projectTable.setTableItems(new RealmList<TableItem>(tableItems.toArray(new TableItem[tableItems.size()])));
                    realm.commitTransaction();
                    tableDataManager.setProjectTableToDB(projectTable);
                    ArrayList<TableItem> completeTableItems = EditLayerService.getCompleteTableItem(component.getGraphic(), tableItems);
                    TableViewManager.isEditingFeatureLayer = true;
                    TableViewManager.graphic = component.getGraphic();
                    TableViewManager.geometry = component.getGraphic().getGeometry();
//                    TableViewManager.featueLayerUrl = component.getLayerUrl();
                    TableViewManager.featueLayerUrl = LayerUrlConstant.getNewLayerUrlByUnknownLayerUrl(component.getLayerUrl());
                    if (false) {
                        Intent intent = new Intent(mContext, ReEditTableActivity.class);
                        intent.putExtra("tableitems", completeTableItems);
                        intent.putExtra("projectId", projectId);
                        startActivity(intent);
                    } else {
                        queryAttachmentInfosAsync(component.getLayerUrl(), component.getGraphic(), completeTableItems, projectId);
                    }
                } else {
                }
            }

            @Override
            public void onError(String msg) {
            }
        });
    }

    @Deprecated
    private void loadCompleteData(final Component component) {
        pd = new ProgressDialog(getActivity());
        pd.setMessage("????????????????????????...");
        pd.show();
        final TableDataManager tableDataManager = new TableDataManager(mContext.getApplicationContext());
        List<Project> projects = tableDataManager.getProjectFromDB();
        Project project = null;
        for (Project p : projects) {
            if (p.getName().equals(component.getLayerName())) {
                project = p;
            }
        }
        if (project == null) {
            ToastUtil.shortToast(mContext, "???????????????????????????");
            return;
        }
        final String projectId = project.getId();
        String getFormStructureUrl = BaseInfoManager.getBaseServerUrl(getActivity()) + "rest/report/rptform";
        tableDataManager.getTableItemsFromNet(project.getId(), getFormStructureUrl, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                TableItems tmp = (TableItems) data;
                if (tmp.getResult() != null) {
                    tableItems = new ArrayList<TableItem>();
                    tableItems.addAll(tmp.getResult());
                    //   tableDataManager.setTableItemsToDB(tableItems);
                    //?????????????????????
                    Realm realm = Realm.getDefaultInstance();
                    ProjectTable projectTable = new ProjectTable();
                    projectTable.setId(projectId);
                    realm.beginTransaction();
                    projectTable.setTableItems(new RealmList<TableItem>(tableItems.toArray(new TableItem[tableItems.size()])));
                    realm.commitTransaction();
                    tableDataManager.setProjectTableToDB(projectTable);
                    ArrayList<TableItem> completeTableItems = EditLayerService.getCompleteTableItem(component.getGraphic(), tableItems);
                    TableViewManager.isEditingFeatureLayer = true;
                    TableViewManager.graphic = component.getGraphic();
                    TableViewManager.geometry = component.getGraphic().getGeometry();
//                    TableViewManager.featueLayerUrl = component.getLayerUrl();
                    TableViewManager.featueLayerUrl = LayerUrlConstant.getNewLayerUrlByUnknownLayerUrl(component.getLayerUrl());
                    if (false) {
                        Intent intent = new Intent(mContext, ReEditTableActivity.class);
                        intent.putExtra("tableitems", completeTableItems);
                        intent.putExtra("projectId", projectId);
                        startActivity(intent);
                    } else {
                        queryAttachmentInfos(component.getLayerUrl(), component.getGraphic(), completeTableItems, projectId);
                    }
                } else {
                }
//                pd.dismiss();
            }

            @Override
            public void onError(String msg) {
                pd.dismiss();
                ToastUtil.shortToast(mContext, "????????????????????????!");
            }
        });
    }

    private void queryAttachmentInfosAsync(String layerUrl, final Graphic graphic, final ArrayList<TableItem> completeTableItems, final String projectId) {
        final ArcGISFeatureLayer arcGISFeatureLayer = new ArcGISFeatureLayer(layerUrl, ArcGISFeatureLayer.MODE.SNAPSHOT);
        arcGISFeatureLayer.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                final int objectid = Integer.valueOf(graphic.getAttributes().get(arcGISFeatureLayer.getObjectIdField()).toString());
                FileService fileService = new FileService(getActivity());
                fileService.getList(arcGISFeatureLayer.getName(), objectid + "")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<FileResult>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }

                            @Override
                            public void onNext(List<FileResult> fileResults) {
                                if (ListUtil.isEmpty(fileResults)) {
                                    return;
                                }
                                Map<String, Integer> map = new HashMap<>();
                                for (FileResult fileResult : fileResults) {
                                    if (map.containsKey(fileResult.getAttachName())) {
                                        continue;
                                    }
                                    if (!fileResult.getType().contains("image")) {
                                        continue;
                                    }
                                    Photo photo = new Photo();
                                    photo.setPhotoPath(fileResult.getFilePath());
                                    photo.setField1("photo");
                                    photo.setHasBeenUp(1);
                                    photoList.add(photo);
                                    map.put(fileResult.getAttachName(), fileResult.getId());
                                }
                            }
                        });
            }
        });
    }

    private void queryAttachmentInfosAsync2(String layerUrl, final Graphic graphic, final ArrayList<TableItem> completeTableItems, final String projectId) {
        final ArcGISFeatureLayer arcGISFeatureLayer = new ArcGISFeatureLayer(layerUrl, ArcGISFeatureLayer.MODE.ONDEMAND);
        arcGISFeatureLayer.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (status == STATUS.INITIALIZED) {
                    final int objectid = Integer.valueOf(graphic.getAttributes().get(arcGISFeatureLayer.getObjectIdField()).toString());
                    arcGISFeatureLayer.queryAttachmentInfos(objectid, new CallbackListener<AttachmentInfo[]>() {
                        @Override
                        public void onCallback(AttachmentInfo[] attachmentInfos) {
//                            final ArrayList<Photo> photoList = new ArrayList<Photo>();
                            Intent intent = new Intent(mContext, ReEditTableActivity.class);
                            intent.putExtra("tableitems", completeTableItems);
                            intent.putExtra("projectId", projectId);
                            if (attachmentInfos == null
                                    || attachmentInfos.length == 0) {

                            } else {
                                Map<String, Long> map = new HashMap<>();
                                for (AttachmentInfo attachmentInfo : attachmentInfos) {
                                    if (map.containsKey(attachmentInfo.getName())) {
                                        continue;
                                    }
                                    if (!attachmentInfo.getContentType().contains("image")) {
                                        continue;
                                    }
                                    try {
                                        InputStream inputStream = arcGISFeatureLayer.retrieveAttachment(objectid,
                                                Integer.valueOf(String.valueOf(attachmentInfo.getId())));
                                        String tempFile = new FilePathUtil(getActivity()).getSavePath() + "/images/" + attachmentInfo.getName() + ".jpg";
                                        File targetFile = new File(tempFile);
                                        if (!targetFile.getParentFile().exists()) {
                                            targetFile.getParentFile().mkdirs();
                                        }
                                        if (!targetFile.exists()) {
                                            targetFile.createNewFile();
                                        }
                                        AMFileUtil.saveInputStreamToFile(inputStream, targetFile);
                                        inputStream.close();
                                        Photo photo = new Photo();
                                        photo.setPhotoPath("file://" + tempFile);
                                        photo.setLocalPath(tempFile);
                                        photo.setField1("photo");
                                        photo.setHasBeenUp(1);
                                        photoList.add(photo);
                                        map.put(attachmentInfo.getName(), attachmentInfo.getId());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (!ListUtil.isEmpty(photoList)) {
                                    intent.putExtra("photos", photoList);
                                }
                            }

                            ((Activity)mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    /*component_detail_container.removeAllViews();
                                    //??????????????????
                                    TableViewManager tableViewManager =
                                            new TableViewManager(mContext, component_detail_container,
                                                    false, TableState.REEDITNG, tableItems,
                                                    photoList, projectId, null, null);*/
                                }
                            });

                        }

                        @Override
                        public void onError(Throwable throwable) {
                        }
                    });
                }
            }
        });
    }


    private void queryAttachmentInfos(String layerUrl, final Graphic graphic, final ArrayList<TableItem> completeTableItems, final String projectId) {
        final ArcGISFeatureLayer arcGISFeatureLayer = new ArcGISFeatureLayer(layerUrl, ArcGISFeatureLayer.MODE.ONDEMAND);
        arcGISFeatureLayer.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (status == STATUS.INITIALIZED) {
                    final int objectid = Integer.valueOf(graphic.getAttributes().get(arcGISFeatureLayer.getObjectIdField()).toString());
                    arcGISFeatureLayer.queryAttachmentInfos(objectid, new CallbackListener<AttachmentInfo[]>() {
                        @Override
                        public void onCallback(AttachmentInfo[] attachmentInfos) {
                            Intent intent = new Intent(mContext, ReEditTableActivity.class);
                            intent.putExtra("tableitems", completeTableItems);
                            intent.putExtra("projectId", projectId);
                            if (attachmentInfos == null
                                    || attachmentInfos.length == 0) {

                            } else {
                                ArrayList<Photo> photoList = new ArrayList<Photo>();
                                Map<String, Long> map = new HashMap<>();
                                for (AttachmentInfo attachmentInfo : attachmentInfos) {
                                    if (map.containsKey(attachmentInfo.getName())) {
                                        continue;
                                    }
                                    if (!attachmentInfo.getContentType().contains("image")) {
                                        continue;
                                    }
                                    try {
                                        InputStream inputStream = arcGISFeatureLayer.retrieveAttachment(objectid,
                                                Integer.valueOf(String.valueOf(attachmentInfo.getId())));
                                        String tempFile = new FilePathUtil(getActivity()).getSavePath() + "/images/" + attachmentInfo.getName() + ".jpg";
                                        File targetFile = new File(tempFile);
                                        if (!targetFile.getParentFile().exists()) {
                                            targetFile.getParentFile().mkdirs();
                                        }
                                        if (!targetFile.exists()) {
                                            targetFile.createNewFile();
                                        }
//
                                        AMFileUtil.saveInputStreamToFile(inputStream, targetFile);
                                        inputStream.close();
//                                        outputStream.close();
                                        Photo photo = new Photo();
                                        photo.setPhotoPath("file://" + tempFile);
                                        photo.setLocalPath(tempFile);
                                        photo.setField1("photo");
                                        photo.setHasBeenUp(1);
                                        photoList.add(photo);
                                        map.put(attachmentInfo.getName(), attachmentInfo.getId());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (!ListUtil.isEmpty(photoList)) {
                                    intent.putExtra("photos", photoList);
                                }
                            }
                            pd.dismiss();
                            startActivity(intent);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            pd.dismiss();
                            ToastUtil.shortToast(mContext, "????????????????????????!");
                        }
                    });
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectComponent(SelectComponentEvent selectComponentEvent) {
        Component component = selectComponentEvent.getComponent();
        currComponentUrl = component.getLayerUrl();
        layerAdapter.notifyDataSetChanged(LayerUrlConstant.getIndexByUnknowsLayerUrl(currComponentUrl));
        showBottomSheet(component);
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

    /**
     * ???????????????????????????URL???null???????????????????????????URL???????????????OnInitLayerUrlFinishEvent??????
     *
     * @param onInitLayerUrlFinishEvent
     */
    @Subscribe
    public void onInitLayerUrlFinished(OnInitLayerUrlFinishEvent onInitLayerUrlFinishEvent) {
        changeLayerUrlInitFailState();
        if (!loadLayersSuccess && layerPresenter != null) {
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (tableViewManager != null) {
            tableViewManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.recycle();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /**
         * ?????????????????????
         */
        if (mLocationManager != null) {
            mLocationManager.stopLocate();
        }

        LayersService.clearInstance();
    }

    private LayerInfo getPlanLayerInfo() {
        List<LayerInfo> layerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
        if (ListUtil.isEmpty(layerInfosFromLocal)) {
            return null;
        }
        for (LayerInfo layerInfo : layerInfosFromLocal) {
            if (layerInfo.getLayerName().contains(PatrolLayerPresenter.RIVER_FLOW_LAYER_NAME)) {
                return layerInfo;
            }
        }
        return null;
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
        locationButton.unregisterSensor();
        mContext = null;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param component
     */
    private void showBottomSheetForCheck(final Component component) {

//        ll_check_hint.setVisibility(View.GONE);
        final String layerName = component.getLayerName();
        if (layerName == null || !attribute.contains(layerName)) {
            hideBottomSheet();
            return;
        }

        initGLayer();
        drawGeometry(component.getGraphic().getGeometry(), mGLayer, false, true);
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
        subtypeTv.setVisibility(View.VISIBLE);
        TextView field1Tv = (TextView) map_bottom_sheet.findViewById(R.id.field1);
        TextView field2Tv = (TextView) map_bottom_sheet.findViewById(R.id.field2);
        TextView field3Tv = (TextView) map_bottom_sheet.findViewById(R.id.field3);

        View btn_container = map_bottom_sheet.findViewById(R.id.btn_container);//??????????????????
        TextView addrTv = (TextView) map_bottom_sheet.findViewById(R.id.addr);//??????
        TextView tv_gc_begin = (TextView) map_bottom_sheet.findViewById(R.id.tv_gc_begin);//????????????
        TextView tv_gc_end = (TextView) map_bottom_sheet.findViewById(R.id.tv_gc_end);//????????????
        TextView tv_ms_begin = (TextView) map_bottom_sheet.findViewById(R.id.tv_ms_begin);//????????????
        TextView tv_ms_end = (TextView) map_bottom_sheet.findViewById(R.id.tv_ms_end);//????????????
        TextView tv_gj = (TextView) map_bottom_sheet.findViewById(R.id.tv_gj);//??????
        View ll_gj = map_bottom_sheet.findViewById(R.id.ll_gj);//??????
        TextView tv_cz = (TextView) map_bottom_sheet.findViewById(R.id.tv_cz);//??????
        View ll_gq = map_bottom_sheet.findViewById(R.id.ll_gq);//??????
        TextView tv_tycd = (TextView) map_bottom_sheet.findViewById(R.id.tv_tycd);//????????????
        TextView tv_pd = (TextView) map_bottom_sheet.findViewById(R.id.tv_pd);//??????
        View line3 = map_bottom_sheet.findViewById(R.id.line3);//?????????
        View line2 = map_bottom_sheet.findViewById(R.id.line2);//?????????
        View line = map_bottom_sheet.findViewById(R.id.line);//?????????
        View line4 = map_bottom_sheet.findViewById(R.id.line4);//?????????
        TextView tv_errorinfo = (TextView) map_bottom_sheet.findViewById(R.id.tv_errorinfo);
        tv_errorinfo.setVisibility(View.GONE);
//        TextView code = (TextView) map_bottom_sheet.findViewById(R.id.code);
        line.setVisibility(View.VISIBLE);
        field3Tv.setVisibility(View.GONE);
        ll_gj.setVisibility(View.VISIBLE);
        btn_container.setVisibility(View.GONE);
        View ll_ms = map_bottom_sheet.findViewById(R.id.ll_msd);
        ll_ms.setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.ll_no_ysk).setVisibility(View.VISIBLE);
        initValue(component, addrTv, "???????????????", ComponentFieldKeyConstant.ADDR,0,"");
        String subtype = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.GRADE), "");
        if (layerName.contains("????????????")) {//???????????????????????????
            line2.setVisibility(View.GONE);
            tv_cz.setVisibility(View.GONE);
            line3.setVisibility(View.VISIBLE);
            line4.setVisibility(View.VISIBLE);
            addrTv.setVisibility(View.VISIBLE);
            ll_gq.setVisibility(View.GONE);
            initValue(component, tv_gc_begin, "?????????????????????", ComponentFieldKeyConstant.BEG_H);
            initValue(component, tv_gc_end, "?????????????????????", ComponentFieldKeyConstant.END_H);
            initValue(component, tv_ms_begin, "?????????????????????", ComponentFieldKeyConstant.BEGCEN_DEE);
            initValue(component, tv_ms_end, "?????????????????????", ComponentFieldKeyConstant.ENDCEN_DEE);
            initValue(component, tv_gj, "?????????", ComponentFieldKeyConstant.PIPE_GJ,0,"");
            initValue(component, field3Tv, "???????????????", ComponentFieldKeyConstant.LENGTH);
        } else if (layerName.contains("??????")) {
            line2.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
            ll_gq.setVisibility(View.GONE);
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
        }else if(layerName.contains("?????????")){
            map_bottom_sheet.findViewById(R.id.ll_no_ysk).setVisibility(View.GONE);
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
        } else if (layerName.contains("????????????")) {
            line3.setVisibility(View.VISIBLE);
            line4.setVisibility(View.VISIBLE);
            ll_gq.setVisibility(View.VISIBLE);
            addrTv.setVisibility(View.VISIBLE);
            tv_cz.setVisibility(View.VISIBLE);
            subtype = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE), "");
            initValue(component, tv_gc_begin, "?????????????????????", ComponentFieldKeyConstant.BEG_H);
            initValue(component, tv_gc_end, "?????????????????????", ComponentFieldKeyConstant.END_H);
            initValue(component, tv_ms_begin, "?????????????????????", ComponentFieldKeyConstant.BEGCIN_DEEP);
            initValue(component, tv_ms_end, "?????????????????????", ComponentFieldKeyConstant.ENDCIN_DEEP);
            initValue(component, tv_gj, "?????????", ComponentFieldKeyConstant.WIDTH);
            initValue(component, tv_cz, "?????????", ComponentFieldKeyConstant.HEIGHT);
            initValue(component, tv_tycd, "???????????????", ComponentFieldKeyConstant.LENGTH);
            initValue(component, tv_pd, "?????????", ComponentFieldKeyConstant.IP);
        }else if(layerName.contains("????????????")){
            ll_ms.setVisibility(View.GONE);
            line4.setVisibility(View.VISIBLE);
            ll_gj.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
            ll_gq.setVisibility(View.GONE);
            sortTv.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            addrTv.setVisibility(View.VISIBLE);
            tv_cz.setVisibility(View.GONE);
            tv_ms_begin.setVisibility(View.GONE);
            tv_ms_end.setText("");
            tv_gj.setText("");
//            subtypeTv.setText("???????????????" + component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
            initValue(component,tv_gc_begin,"???????????????",ComponentFieldKeyConstant.MEMO2,0,"");
            initValue(component, subtypeTv, "????????????", ComponentFieldKeyConstant.LRR,0,"");
            initValue(component, addrTv, "???????????????", ComponentFieldKeyConstant.MEMO,0,"");
        }  else {
            line3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
            addrTv.setVisibility(View.GONE);
            ll_gq.setVisibility(View.GONE);
            tv_gc_begin.setText("");
            tv_gc_end.setText("");
            tv_ms_begin.setText("");
            tv_ms_end.setText("");
            tv_gj.setText("");
        }
//        TextView tv_errorinfo = (TextView) map_bottom_sheet.findViewById(R.id.tv_errorinfo);
//        TextView tv_parent_org_name = (TextView) map_bottom_sheet.findViewById(R.id.tv_parent_org_name);

        String name = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.NAME));

        String type = component.getLayerName();

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
            if(StringUtil.isEmpty(sort) || "null".equals(sort)){
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

        subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
        if(StringUtil.isEmpty(subtype) || "null".equals(subtype)){
            subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE));
        }

        /**
         * ??????????????????????????????????????????
         */
        if (layerName.contains("?????????")) {
            String feature = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
            sortTv.setText(StringUtil.getNotNullString(feature, ""));
        }
        if (layerName.contains("?????????")) {
            String style = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE));
            subtypeTv.setText(StringUtil.getNotNullString(style, ""));
            subtypeTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.dust_grey, null));
        } else if(!"?????????".equals(layerName) && !"????????????".equals(layerName)){
            subtypeTv.setText(StringUtil.getNotNullString(subtype, ""));
        }


        //???????????????
        String codeValue = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FIVE));
        if (!StringUtil.isEmpty(codeValue)) {
            codeValue = codeValue.trim();
        }

//        if ("??????".equals(component.getLayerName())) {
//            code.setVisibility(View.VISIBLE);
//            if (!codeValue.isEmpty()) {
//                code.setText("???????????????" + ":" + StringUtil.getNotNullString(codeValue, ""));
//            } else {
//                code.setText("???????????????" + ":" + "");
//            }
//        } else {
//            code.setVisibility(View.GONE);
//        }

        /**
         * ???????????????
         */
        String field3 = "";
        if (layerName.contains("??????")) {
            field3 = "????????????: " + StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE), "");
            if(StringUtil.isEmpty(field3)){
                field3 = "????????????: " + StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.COVER_MATERIAL), "");
            }
            field3Tv.setText(field3);
        } else if (layerName.contains("????????????")) {
            field3 = "????????????: " + StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.MATERIAL1), "");
        } else if (layerName.contains("????????????")) {
            field3 = "????????????: " + StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE), "");
            field3Tv.setText(field3);
        }


        String parentOrg = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FOUR));

        View treamLine = map_bottom_sheet.findViewById(R.id.stream_line);

        /**
         * ????????????
         */
        map_bottom_sheet.findViewById(R.id.tv_error_correct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_delete).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_sure).setVisibility(View.GONE);
        ((TextView) map_bottom_sheet.findViewById(R.id.tv_error_correct)).setText("??????");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                map_bottom_sheet.setVisibility(View.VISIBLE);
                mBehavior.setState(AnchorSheetBehavior.STATE_EXPANDED);
            }
        }, 200);
    }

    private void initValue(Component component, TextView tv_gc_begin, String name, String key) {
        tv_gc_begin.setVisibility(View.VISIBLE);
        String value = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(key), "");
        if (StringUtil.isEmpty(value)) {
            tv_gc_begin.setText(name);
            return;
        }
        String format = name + formatToSecond(value, "m", 2);
        tv_gc_begin.setText(format);
    }

    private void initValue(Component component, TextView tv_gc_begin, String name, String key, int newScale, String unit) {
        String value = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(key), "");
        String gjName = "";
        if(!StringUtil.isEmpty(value) && ComponentFieldKeyConstant.PIPE_GJ.equals(key)){
            TableDBService dbService = new TableDBService(mContext);
            List<DictionaryItem> a205 = dbService.getDictionaryByTypecodeInDB("A205");
            for (DictionaryItem dictionaryItem : a205) {
                if(dictionaryItem.getCode().equals(value)){
                    gjName = dictionaryItem.getName();
                }
            }
        }
        if (StringUtil.isEmpty(value)) {
            tv_gc_begin.setText(name);
            return;
        }
        if(ComponentFieldKeyConstant.PIPE_GJ.equals(key)){
            tv_gc_begin.setText(name + gjName + unit);
            return;
        }else if (newScale == 0) {
            tv_gc_begin.setText(name + value + unit);
            return;
        }
        String format = name + formatToSecond(value, unit, newScale);
        tv_gc_begin.setText(format);
    }

    private String formatToSecond(String value, String unit, int newScale) {
        if (StringUtil.isEmpty(value)) {
            return "";
        }
        Double num = Double.valueOf(value);
        BigDecimal bd = new BigDecimal(num);
        num = bd.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return num + "" + unit;
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
        }else if(oldLayerName.equals("????????????")){
            return "????????????";
        }else{
            return "";
        }
    }
}
