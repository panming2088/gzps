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

package com.augurit.agmobile.gzps.common.selectcomponent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.addcomponent.LayerAdapter;
import com.augurit.agmobile.gzps.addcomponent.model.ComponentMap;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.OnInitLayerUrlFinishEvent;
import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.common.search.IOnSearchClickListener;
import com.augurit.agmobile.gzps.common.search.SearchFragment;
import com.augurit.agmobile.gzps.common.util.MapIconUtil;
import com.augurit.agmobile.gzps.componentmaintenance.ComponetListAdapter;
import com.augurit.agmobile.gzps.componentmaintenance.SelectComponentEvent;
import com.augurit.agmobile.gzps.componentmaintenance.model.QueryFeatureSet;
import com.augurit.agmobile.gzps.componentmaintenance.service.ComponentService;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.setting.service.MyCorrectFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.util.UploadLayerFieldKeyConstant;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.common.widget.scaleview.MapScaleView;
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
import com.augurit.agmobile.patrolcore.common.model.LocationInfo;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableItems;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.agmobile.patrolcore.common.table.model.ProjectTable;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.TableState;
import com.augurit.agmobile.patrolcore.common.widget.LocationButton;
import com.augurit.agmobile.patrolcore.editmap.widget.LocationMarker;
import com.augurit.agmobile.patrolcore.layer.service.EditLayerService;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView2;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.agmobile.patrolcore.upload.view.ReEditTableActivity;
import com.augurit.am.cmpt.common.Callback1;
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
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
import static com.augurit.agmobile.gzps.R.id.subtype;

/**
 * 地图上进行选择已修改的部件进行标识（采用bottomsheet）
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.common.selectcomponent
 * @createTime 创建时间 ：17/11/6
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/6
 * @modifyMemo 修改备注：
 */

public class SelectComponentOrAddressFragment2 extends Fragment implements View.OnClickListener {

    private static final String KEY_MAP_STATE = "com.esri.MapState";
    private MultiTakePhotoTableItem photo_item;

    private LocationMarker locationMarker;
    /**
     * 上次选择的位置
     */
    private Point mLastSelectedLocation = null;
    private PatrolLocationManager mLocationManager;
    /**
     * 绘制位置的图层
     */
    private GraphicsLayer mGLayerFroDrawLocation;

    /**
     * 是否是第一次定位，如果是，那么居中；否则，不做任何操作；
     */
    private boolean ifFirstLocate = true;
    private LocationButton locationButton;
    private LegendPresenter legendPresenter;
    private ViewGroup ll_component_list;
    private View ll_layer_url_init_fail;

    private Context mContext;

    View mView;

    MapView mMapView;

    private ILayerView layerView;

    private PatrolLayerPresenter layerPresenter;
    private boolean loadLayersSuccess = true;

    List<ComponentMap> componentMapList = new ArrayList<>();

    boolean mClosingTheApp = false;

    String curComponentName = "";

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
    private DetailAddress mCurrentAddress;

    private View ll_add;
    private View ll_add_to_cancel;
    //原位置图层
    private GraphicsLayer mOriginGraphicLayer;

    private MyCorrectFacilityService uploadFacilityService;

    /**
     * 地图默认的事件处理
     */
    private DefaultTouchListener defaultMapOnTouchListener;

    private List<Component> mComponents;
    private SearchFragment searchFragment;
    private final String TAG = "SelectComponentOrAddressFragment2";
    private ComponentService componentService;
    private boolean isSearch = false;
    private List<String> attribute = Arrays.asList("窨井(中心城区)","窨井","雨水口","排水口","排水管道","排水管道(中心城区)","排水沟渠","排水沟渠(中心城区)");

    public static SelectComponentOrAddressFragment2 getInstance(Bundle data) {
        SelectComponentOrAddressFragment2 addComponentFragment2 = new SelectComponentOrAddressFragment2();
        addComponentFragment2.setArguments(data);
        return addComponentFragment2;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(mContext, R.layout.fragment_select_component_psk, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        mView = view;
        initUploadFacilityService();
        if (getArguments() != null) {
            curComponentName = getArguments().getString("componentName");
        }

        mView.findViewById(R.id.ll_search).setVisibility(View.VISIBLE);
//        ImageView iv_open_search = (ImageView) mView.findViewById(R.id.iv_open_search);
        ((TextView) mView.findViewById(R.id.tv_search)).setText("");
        mView.findViewById(R.id.tv_search).setVisibility(View.VISIBLE);
        mView.findViewById(R.id.toolbar_view).setVisibility(View.VISIBLE);
        mView.findViewById(R.id.ll_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!searchFragment.isAdded()
                        && !searchFragment.isVisible()
                        && !searchFragment.isRemoving()) {
                    searchFragment.show(getFragmentManager(), TAG);
                }
            }
        });
        searchFragment = SearchFragment.newInstance();
        searchFragment.setTAG(TAG);
        searchFragment.setHintText("搜索窨井编号");
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                isSearch = true;
                //这里处理逻辑
                if (!StringUtil.isEmpty(keyword)) {
                    queryForObjctId(keyword);
                }
            }
        });
        // Find MapView and add feature layers
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.setMapBackground(Color.WHITE, Color.TRANSPARENT, 0f, 0f);//设置地图背景色、去掉网格线
        /**
         * 比例尺
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
                        if (getArguments() != null && getArguments().getDouble("initScale") != 0) {
                            double scale = getArguments().getDouble("initScale");
                            mMapView.setScale(PatrolLayerPresenter.initScale);
                            scaleView.setScale(PatrolLayerPresenter.initScale);
                        } else {
                            if (PatrolLayerPresenter.initScale != -1 && PatrolLayerPresenter.initScale != 0) {
                                mMapView.setScale(PatrolLayerPresenter.initScale);
                                scaleView.setScale(PatrolLayerPresenter.initScale);
                            }
                        }
                        mMapView.setMaxScale(30);
                        /**
                         * 判断是否有位置传递过来
                         */
                        if (getArguments() != null && getArguments().getSerializable("geometry") != null) {
                            Geometry geometry = (Geometry) getArguments().getSerializable("geometry");
                            mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
                            highLightFacilityOriginLocation(geometry);
                        } else {
                            //默认定位到水务局附近
                            Point point = new Point(PatrolLayerPresenter.longitude, PatrolLayerPresenter.latitude);
                            mMapView.centerAt(point, true);
                            if (locationButton != null) {
                                locationButton.followLocation();
                            }
                            // startLocate();
                        }
                        if (layerPresenter != null) {
                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter.RIVER_FLOW_LAYER_NAME, true);
                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter.UPLOAD_LAYER_NAME, true);

//                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter.JOURNAL_LAYER_NAME, true);
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
         * 候选列表容器
         */
        ll_component_list = (ViewGroup) view.findViewById(R.id.ll_component_list);

        /**
         * 默认使用窨井
         */
        //locationMarker.changeIcon(MapIconUtil.getResId(LayerUrlConstant.componentNames[0]));
        locationMarker.setVisibility(View.VISIBLE);

        show_all_layer = (TextView) view.findViewById(R.id.show_all_layer);
        show_all_layer.setOnClickListener(this);
        gridView = (GridView) view.findViewById(R.id.gridview);
        layerAdapter = new LayerAdapter(getContext());
        gridView.setAdapter(layerAdapter);
        layerAdapter.setOnItemClickListener(new OnRecyclerItemClickListener<String>() {
            @Override
            public void onItemClick(View view, int position, String selectedData) {
                currComponentUrl = selectedData;
                /**
                 * 切换选点图标
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
                Activity activity = getActivity();
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
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        btn_prev = view.findViewById(R.id.prev);
        btn_next = view.findViewById(R.id.next);
        component_intro = view.findViewById(R.id.intro);
        component_detail_ll = view.findViewById(R.id.detail_ll);
        component_detail_container = (ViewGroup) view.findViewById(R.id.detail_container);
        btn_upload = view.findViewById(R.id.btn_upload);

        /*
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                onChangedBottomSheetState(newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        */
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currIndex--;
                if (currIndex < 0) {
                    btn_prev.setVisibility(View.GONE);
                    return;
                }
                showBottomSheetForCheck(mComponentQueryResult.get(currIndex));
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
                showBottomSheetForCheck(mComponentQueryResult.get(currIndex));
                if (currIndex == (mComponentQueryResult.size() - 1)) {
                    btn_next.setVisibility(View.GONE);
                }
                if (currIndex > 0) {
                    btn_prev.setVisibility(View.VISIBLE);
                }
            }
        });
        //隐藏提交按钮
        btn_upload.setVisibility(View.GONE);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 改成确定按钮
                //tableViewManager.uploadEdit();
            }
        });

        //修改标题
        ((TextView) view.findViewById(R.id.tv_title)).setText("部件选择");
        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
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
         * 图例
         */
        View ll_legend = view.findViewById(R.id.ll_legend);
        ll_legend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLegendPresenter();
                legendPresenter.showLegends();
            }
        });


        ll_add_to_cancel = view.findViewById(R.id.ll_add_to_cancel);
        ll_add_to_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationMarker != null) {
                    locationMarker.setVisibility(View.GONE);
                }
                mMapView.getCallout().hide();
                ll_add.setVisibility(View.VISIBLE);
                ll_add_to_cancel.setVisibility(View.GONE);
                mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
            }
        });

        ll_add = view.findViewById(R.id.ll_add);
        ll_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                if(btn_edit_cancel.getVisibility() == View.VISIBLE){
                    btn_edit_cancel.performClick();
                }
                if (ifFirstAdd) {
                    ToastUtil.iconLongToast(getActivity(),R.mipmap.ic_alert_yellow,"移动地图选择新增设施的位置");
                    ifFirstAdd = false;
                }
                */

                if (locationMarker != null) {
                    locationMarker.changeIcon(R.mipmap.ic_add_facility);
                    locationMarker.setVisibility(View.VISIBLE);
                }
                ll_add.setVisibility(View.GONE);
                ll_add_to_cancel.setVisibility(View.VISIBLE);
                hideBottomSheet();
                initGLayer();
                //  setAddNewFacilityListener();
            }
        });

        ll_add.setVisibility(View.GONE);
        ll_add_to_cancel.setVisibility(View.GONE);

    }

    private void initUploadFacilityService() {
        if (uploadFacilityService == null) {
            uploadFacilityService = new MyCorrectFacilityService(mContext);
        }
    }

    private void initLegendPresenter() {
        if (legendPresenter == null) {
            ILegendView legendView = new ImageLegendView(mContext);
            legendPresenter = new LegendPresenter(legendView, new OnlineLegendService(mContext));
        }

    }

    public void loadMap() {
        IDrawerController drawerController = (IDrawerController) getActivity();
        layerView = new PatrolLayerView2(mContext, mMapView, drawerController.getDrawerContainer());
        layerPresenter = new PatrolLayerPresenter(layerView);
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
     * 高亮设施原来的位置
     *
     * @param mDestinationOrLastTimeSelectLocation
     */
    private void highLightFacilityOriginLocation(Geometry mDestinationOrLastTimeSelectLocation) {
        if (mOriginGraphicLayer == null) {
            mOriginGraphicLayer = new GraphicsLayer();
            mMapView.addLayer(mOriginGraphicLayer);
        }
        mOriginGraphicLayer.removeAll();
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(getContext(),
                getContext().getResources().getDrawable(R.drawable.old_map_point));
        pictureMarkerSymbol.setOffsetY(16);
        Graphic graphic = new Graphic(mDestinationOrLastTimeSelectLocation, pictureMarkerSymbol);
        mOriginGraphicLayer.addGraphic(graphic);
        mOriginGraphicLayer.setSelectedGraphics(new int[]{graphic.getUid()}, true);
    }

    /**
     * 开启定位
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
                    ToastUtil.shortToast(mContext, "当前位置不在地图范围内");
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
     * 显示图层列表
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
            getActivity().finish();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_MAP_STATE, mMapView.retainState());
    }


//
//    private void showCallout(String address, Point point, final View.OnClickListener calloutSureButtonClickListener) {
//        final Point geometry = point;
//        final Callout callout = mMapView.getCallout();
//        View view = View.inflate(getActivity(), com.augurit.agmobile.patrolcore.R.layout.editmap_callout, null);
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
//     * 请求百度地址
//     *
//     * @param point
//     * @param callback1
//     */
//    private void requestLocation(Point point, SpatialReference spatialReference, final Callback1<String> callback1) {
//        SelectLocationService selectLocationService = new SelectLocationService(getActivity(), Locator.createOnlineLocator());
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
        //获取当前的位置
        return mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2, locationMarker.getY() + locationMarker.getHeight() / 2);
    }


    /**
     * @return
     */
    private MapOnTouchListener getDefaultMapOnTouchListener() {
        if (defaultMapOnTouchListener == null) {
            defaultMapOnTouchListener = new DefaultTouchListener(mContext, mMapView, locationMarker, false, new View.OnClickListener() {

                /**
                 * callout的确定点击事件
                 * @param v
                 */
                @Override
                public void onClick(View v) {
                    double scale = mMapView.getScale();
                    if (scale > LayerUrlConstant.MIN_QUERY_SCALE) {
                        ToastUtil.shortToast(mContext, "请先放大到可以看到设施的级别");
                        return;
                    }

                    LocationInfo locationInfo = defaultMapOnTouchListener.getLoationInfo();
                    if (locationInfo.getDetailAddress() == null && locationInfo.getPoint() == null) {
                        ToastUtil.shortToast(mContext, "请重新选择位置");
                        return;
                    }

                    double x = locationInfo.getPoint().getX();
                    double y = locationInfo.getPoint().getY();

                    if (scale < LayerUrlConstant.MIN_QUERY_SCALE) {
                        defaultMapOnTouchListener.query(x, y);
                    }
                }
            });

        }

        return defaultMapOnTouchListener;
    }


    /**
     * The MapView's touch listener.
     */
    private class DefaultTouchListener extends SelectLocationTouchListener {

        private ProgressDialog progressDialog;
        private SelectLocationService selectLocationService;
        private View.OnClickListener calloutSureButtonClickListener;

        public DefaultTouchListener(Context context, MapView view, LocationMarker locationMarker, View.OnClickListener calloutSureButtonClickListener) {
            super(context, view, locationMarker, calloutSureButtonClickListener);
        }

        public DefaultTouchListener(Context context, MapView view, LocationMarker locationMarker,
                                    boolean ifshowCallout, View.OnClickListener calloutSureButtonClickListener) {
            super(context, view, locationMarker, ifshowCallout, calloutSureButtonClickListener);
            this.calloutSureButtonClickListener = calloutSureButtonClickListener;
        }


        @Override
        public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
            if (locationButton != null && locationButton.ifLocating()) {
                locationButton.setStateNormal();
            }
            hideBottomSheet();
            initGLayer();
            return super.onDragPointerMove(from, to);
        }

        @Override
        public boolean onSingleTap(final MotionEvent e) {
//            initGLayer();
            hideBottomSheet();
//            handleTap(e);

            return true;
        }

        /**
         * //         * @param latitude
         * //         * @param longitude
         * //
         */
//        public void requestLocation(final double latitude, final double longitude) {
//
//            if (selectLocationService == null) {
//                selectLocationService = new SelectLocationService(mContext, Locator.createOnlineLocator());
//            }
//
//            selectLocationService.parseLocation(new LatLng(latitude, longitude), mMapView.getSpatialReference())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<DetailAddress>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onNext(DetailAddress detailAddress) {
//                            String address = detailAddress.getDetailAddress();
//                            mCurrentAddress = detailAddress;
//                            mCurrentAddress.setX(longitude);
//                            mCurrentAddress.setY(latitude);
//                            showCallout(new Point(longitude, latitude), detailAddress);
//                        }
//                    });
//        }
        private void query(double x, double y) {
            isSearch = false;
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("正在查询中.....");
            }

            progressDialog.show();

            mComponentQueryResult.clear();
            btn_next.setVisibility(View.GONE);
            btn_prev.setVisibility(View.GONE);
            currIndex = 0;
            final Point point = new Point(x, y);
            final List<LayerInfo> layerInfoList = new ArrayList<>();
            for (String url : LayerUrlConstant.newComponentUrls) {
                LayerInfo layerInfo = new LayerInfo();
                layerInfo.setUrl(url);
                layerInfoList.add(layerInfo);
            }
            Geometry geometry = GeometryEngine.buffer(point, mMapView.getSpatialReference(), 40 * mMapView.getResolution(), null);
            ComponentService componentMaintenanceService = new ComponentService(mContext.getApplicationContext());
            if (true) {
                componentMaintenanceService.queryComponentsForAll(geometry, new Callback2<List<Component>>() {
                    @Override
                    public void onSuccess(List<Component> components) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        if (ListUtil.isEmpty(components)) {
                            ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                            return;
                        }
                        mComponentQueryResult = new ArrayList<Component>();
                        mComponentQueryResult.addAll(components);
                        if (ListUtil.isEmpty(mComponentQueryResult)) {
                            initGLayer();
                            ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                            return;
                        }
                        showComponentsOnBottomSheet(mComponentQueryResult);
                    }

                    @Override
                    public void onFail(Exception error) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                    }
                });
//                        .flatMap(new Func1<List<Component>, Observable<Component>>() {
//                            @Override
//                            public Observable<Component> call(List<Component> modifiedIdentifications) {
//                                mComponents = modifiedIdentifications;
//                                return Observable.from(modifiedIdentifications);
//                            }
//                        })
//                        //获取附件
//                        .flatMap(new Func1<Component, Observable<Component>>() {
//                            @Override
//                            public Observable<Component> call(final Component modifiedIdentification) {
//                                String markId = String.valueOf(modifiedIdentification.getGraphic().getAttributes().get(UploadLayerFieldKeyConstant.MARK_ID));
//                                if (StringUtil.isEmpty(markId) || "null".equals(markId)) {
//                                    markId = "-1";
//                                }
//                                return uploadFacilityService.getMyModificationAttachments(Long.valueOf(markId))
//                                        .map(new Func1<ServerAttachment, Component>() {
//                                            @Override
//                                            public Component call(ServerAttachment serverIdentificationAttachment) {
//                                                List<ServerAttachment.ServerAttachmentDataBean> data = serverIdentificationAttachment.getData();
//                                                if (!ListUtil.isEmpty(data)) {
//                                                    List<Photo> photos = new ArrayList<>();
//                                                    for (ServerAttachment.ServerAttachmentDataBean dataBean : data) {
//                                                        Photo photo = new Photo();
//                                                        photo.setId(Long.valueOf(dataBean.getId()));
//                                                        photo.setPhotoPath(dataBean.getAttPath());
//                                                        photo.setPhotoName(dataBean.getAttName());
//                                                        photo.setThumbPath(dataBean.getThumPath());
//                                                        photos.add(photo);
//                                                    }
//                                                    modifiedIdentification.setPhotos(photos);
//                                                }
//                                                return modifiedIdentification;
//                                            }
//                                        });
//                            }
//                        })
//                        .toList()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Subscriber<List<Component>>() {
//                            @Override
//                            public void onCompleted() {
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                if (progressDialog != null) {
//                                    progressDialog.dismiss();
//                                }
//                                /**
//                                 * 当查询失败的时候进行查询地址
//                                 */
//                                if (ListUtil.isEmpty(mComponents)) {
//                                    requestLocation(point, mMapView.getSpatialReference(), new Callback1<String>() {
//                                        @Override
//                                        public void onCallback(String s) {
//                                            showCalloutMessage(s, null, new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View view) {
//                                                    DetailAddress detailAddress = defaultMapOnTouchListener.getLoationInfo().getDetailAddress();
//                                                    EventBus.getDefault().post(new SelectComponentFinishEvent2(null, detailAddress, mMapView.getScale()));
//                                                    getActivity().finish();
//                                                }
//                                            });
//                                        }
//                                    });
//                                } else {
//                                    mComponentQueryResult = new ArrayList<Component>();
//                                    mComponentQueryResult.addAll(mComponents);
//                                    showComponentsOnBottomSheet(mComponentQueryResult);
//                                }
//                            }
//
//                            @Override
//                            public void onNext(List<Component> components) {
//                                if (progressDialog != null) {
//                                    progressDialog.dismiss();
//                                }
//
//                                if (ListUtil.isEmpty(components)) {
//                                    /**
//                                     * 当查询失败的时候进行查询地址
//                                     */
//                                    requestLocation(point, mMapView.getSpatialReference(), new Callback1<String>() {
//                                        @Override
//                                        public void onCallback(String s) {
//                                            showCalloutMessage(s, null, new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View view) {
//                                                    DetailAddress detailAddress = defaultMapOnTouchListener.getLoationInfo().getDetailAddress();
//                                                    EventBus.getDefault().post(new SelectComponentFinishEvent2(null, detailAddress, mMapView.getScale()));
//                                                    getActivity().finish();
//                                                }
//                                            });
//                                        }
//                                    });
//                                    return;
//                                }
//                                mComponentQueryResult = new ArrayList<Component>();
//                                mComponentQueryResult.addAll(components);
//                                showComponentsOnBottomSheet(mComponentQueryResult);
//                            }
//                        });
            } else {
                String oldLayerUrl = LayerUrlConstant.getLayerUrlByLayerName(LayerUrlConstant.getLayerNameByNewLayerUrl(currComponentUrl));
                componentMaintenanceService.queryComponents(geometry, oldLayerUrl, currComponentUrl, new Callback2<List<QueryFeatureSet>>() {
                    @Override
                    public void onSuccess(List<QueryFeatureSet> queryFeatureSetList) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                        if (ListUtil.isEmpty(queryFeatureSetList)) {
                            /**
                             * 当查询失败的时候进行查询地址
                             */
                            requestLocation(point, mMapView.getSpatialReference(), new Callback1<String>() {
                                @Override
                                public void onCallback(String s) {
                                    showCalloutMessage(s, null, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            DetailAddress detailAddress = defaultMapOnTouchListener.getLoationInfo().getDetailAddress();
                                            EventBus.getDefault().post(new SelectComponentFinishEvent2(null, detailAddress, mMapView.getScale()));
                                            getActivity().finish();
                                        }
                                    });
                                }
                            });
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
//                                component.setFieldAlias(featureSet.getFieldAliases());
//                                component.setFields(featureSet.getFields());
                                component.setGraphic(graphic);
                                Object o = graphic.getAttributes().get(featureSet.getObjectIdFieldName());
                                if (o != null && o instanceof Integer) {
                                    component.setObjectId((Integer) o); //按照道理objectId一定是integer的
                                }
                                mComponentQueryResult.add(component);
                            }
                        }

                        showComponentsOnBottomSheet(mComponentQueryResult);
                    }

                    @Override
                    public void onFail(Exception error) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        /**
                         * 当查询失败的时候进行查询地址
                         */
                        requestLocation(point, mMapView.getSpatialReference(), new Callback1<String>() {
                            @Override
                            public void onCallback(String s) {
                                showCalloutMessage(s, null, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        DetailAddress detailAddress = defaultMapOnTouchListener.getLoationInfo().getDetailAddress();
                                        EventBus.getDefault().post(new SelectComponentFinishEvent2(null, detailAddress, mMapView.getScale()));
                                        getActivity().finish();
                                    }
                                });
                            }
                        });
                    }
                });
            }
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
            final Point point = mMapView.toMapPoint(e.getX(), e.getY());
            //requestLocation(point.getY(), point.getX());
            double scale = mMapView.getScale();
            if (scale < LayerUrlConstant.MIN_QUERY_SCALE) {
                final Point mapPoint = mMapView.toMapPoint(e.getX(), e.getY());
                query(mapPoint.getX(), mapPoint.getY());
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
     * 在地图上画图形
     *
     * @param graphicsLayer
     * @param ifRemoveAll   在添加前是否移除掉所有
     * @param geometry
     */
    private void drawGeometry(Geometry geometry, GraphicsLayer graphicsLayer, boolean ifRemoveAll, boolean ifCenter) {

        if (graphicsLayer == null) {
            return;
        }
        Symbol symbol = null;
        SimpleFillSymbol simpleFillSymbol = null;
        switch (geometry.getType()) {
            case LINE:
            case POLYLINE:
                symbol = new SimpleLineSymbol(Color.RED, 7);
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
            if (geometry.getType() == Geometry.Type.POLYLINE) { //如果是线，那么这个就是要传递给服务端的结果
                this.graphic = graphic;
            }
        }
        if (simpleFillSymbol != null) {
            Graphic graphic = new Graphic(geometry, simpleFillSymbol);
            graphicsLayer.addGraphic(graphic);
            if (geometry.getType() == Geometry.Type.POLYLINE) { //如果是线，那么这个就是要传递给服务端的结果
                this.graphic = graphic;
            }
        }
        if (ifCenter && simpleFillSymbol == null) {
            mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        }else{
            mMapView.setExtent(geometry);
        }
    }


    /**
     * 在地图上画图形
     *
     * @param graphicsLayer
     * @param ifRemoveAll   在添加前是否移除掉所有
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
            if (geometry.getType() == Geometry.Type.POLYLINE) { //如果是线，那么这个就是要传递给服务端的结果
                this.graphic = graphic;
            }
            return graphicsLayer.addGraphic(graphic);
        }
        return -1;

    }

    @NonNull
    private Symbol getPointSymbol() {
        Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), com.augurit.agmobile.patrolcore.R.mipmap.ic_select_location, null);
        return new PictureMarkerSymbol(mContext, drawable);    // xjx 改为兼容api19的方式获取drawable
    }

    /*
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
    */


    private void hideBottomSheet() {
        map_bottom_sheet.setVisibility(View.GONE);
    }

    private void showComponentsOnBottomSheet(List<Component> componentQueryResult) {
        if (componentQueryResult.size() > 1) {
            btn_next.setVisibility(View.VISIBLE);
        }
        //initGLayer();
        //drawGeometry(componentQueryResult.get(0).getGraphic().getGeometry(), mGLayer, true, true);
        showBottomSheetForCheck(mComponentQueryResult.get(0));

    }

    private void showCallout(final Component component) {
//        //显示callout
        String usid = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.USID));
        String objectId = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.OBJECTID));
        if (StringUtil.isEmpty(usid) || "null".equals(usid)) {
            usid = objectId;
        }
//        String type = LayerUrlConstant.getLayerNameByUnknownLayerUrl(component.getLayerUrl());
        String type = component.getLayerName();
        String title = StringUtil.getNotNullString(getLayerName(type), "") + "(" + StringUtil.getNotNullString(usid, "") + ")";
//        Callout callout = mMapView.getCallout();
//        View view = View.inflate(getActivity(), com.augurit.agmobile.patrolcore.R.layout.callout_select_device, null);
//        ((TextView) view.findViewById(com.augurit.agmobile.patrolcore.R.id.tv_listcallout_title)).setText(title);
//
//        view.findViewById(com.augurit.agmobile.patrolcore.R.id.btn_select_device).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EventBus.getDefault().post(new SelectComponentFinishEvent2(component, mCurrentAddress, mMapView.getScale()));
//                getActivity().finish();
//            }
//        });
//        callout.setContent(view);
//        callout.show(GeometryUtil.getGeometryCenter(component.getGraphic().getGeometry()));
        defaultMapOnTouchListener.showCalloutMessage(title, null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new SelectComponentFinishEvent2(component, mCurrentAddress, mMapView.getScale()));
                getActivity().finish();
            }
        });
    }

//    private void showCallout(Point point, final DetailAddress detailAddress) {
//        //显示callout
//        String name = detailAddress.getDetailAddress();
//        // String type = LayerUrlConstant.getLayerNameByUnknownLayerUrl(component.getLayerUrl());
//        // String title = StringUtil.getNotNullString(name, "") + "  " + StringUtil.getNotNullString(type, "");
//        Callout callout = mMapView.getCallout();
//        View view = View.inflate(getActivity(), com.augurit.agmobile.patrolcore.R.layout.editmap_callout, null);
//        ((TextView) view.findViewById(com.augurit.agmobile.patrolcore.R.id.tv_listcallout_title)).setText(name);
//
//        view.findViewById(com.augurit.agmobile.patrolcore.R.id.btn_select_device).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EventBus.getDefault().post(new SelectComponentFinishEvent2(null, detailAddress, mMapView.getScale()));
//                getActivity().finish();
//            }
//        });
//        callout.setContent(view);
//        callout.show(point);
//    }

    @Deprecated
    private void showBottomSheet2(final Component component) {

        showCallout(component);

        //  initGLayer();
        //  drawGeometry(component.getGraphic().getGeometry(), mGLayer, true, true);

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
            field1Tv.setText(field1_1 + "：" + StringUtil.getNotNullString(field1, ""));

            String field2_1 = listFieldArray[1].split(":")[0];
            String field2_2 = listFieldArray[1].split(":")[1];
            String field2 = String.valueOf(component.getGraphic().getAttributes().get(field2_2));
            field2Tv.setText(field2_1 + "：" + StringUtil.getNotNullString(field2, ""));

            String field3_1 = listFieldArray[2].split(":")[0];
            String field3_2 = listFieldArray[2].split(":")[1];
            String field3 = String.valueOf(component.getGraphic().getAttributes().get(field3_2));
            field3Tv.setText(field3_1 + "：" + StringUtil.getNotNullString(field3, ""));
        }

        String address = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ADDR));
        addrTv.setText(StringUtil.getNotNullString(address, ""));


        //已挂牌编号
        String codeValue = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.CODE));
        codeValue = codeValue.trim();
        if (component.getLayerName().equals("窨井")) {
            if (!codeValue.isEmpty()) {
                code.setText("已挂牌编码" + ":" + StringUtil.getNotNullString(codeValue, ""));
            } else {
//                code.setText("已挂牌编码" + ":" +"无");
                code.setText("已挂牌编码" + ":" + "");
            }
        } else {
            code.setVisibility(View.GONE);
        }

        // if (errorInfo == null) {
        tv_errorinfo.setVisibility(View.GONE);
        //  } else {
        //       tv_errorinfo.setVisibility(View.VISIBLE);
        //       tv_errorinfo.setText(errorInfo);
        //  }
        ////  showBottomSheetContent(component_intro.getId());
        map_bottom_sheet.setVisibility(View.VISIBLE);
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        // loadCompleteDataAsync(component);
    }

    private void showBottomSheet(final Component component) {
        String layerName = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(ComponentFieldKeyConstant.LAYER_NAME), "");
        if (StringUtil.isEmpty(layerName)) {
            layerName = component.getLayerName();
        }
        if(isSearch){
            if (locationButton != null) {
                locationButton.setStateNormal();
            }
            initGLayer();
            drawGeometry(component.getGraphic().getGeometry(), mGLayer, true, true);
        }
        if (!"窨井".equals(layerName) && !"雨水口".equals(layerName) && !"排水口".equals(layerName)) {
            hideBottomSheet();
            return;
        }
        showCallout(component);

        //  initGLayer();
        //  drawGeometry(component.getGraphic().getGeometry(), mGLayer, true, true);

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
        photo_item = (MultiTakePhotoTableItem) map_bottom_sheet.findViewById(R.id.photo_item);
        photo_item.setPhotoExampleEnable(false);
        photo_item.setReadOnly();
        photo_item.setAddPhotoEnable(false);
        if (!ListUtil.isEmpty(component.getPhotos())) {
            photo_item.setVisibility(View.VISIBLE);
            photo_item.setSelectedPhotos(component.getPhotos());
            ViewGroup.LayoutParams linearParams = (ViewGroup.LayoutParams) map_bottom_sheet.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
            Resources resources = mContext.getResources();
            float f = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 340, resources.getDisplayMetrics());
            linearParams.height = (int) f;// 控件的宽强制设成30
            map_bottom_sheet.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

        } else {
//            photo_item.setSelectedPhotos(null);
            photo_item.setVisibility(View.GONE);
            ViewGroup.LayoutParams linearParams = (ViewGroup.LayoutParams) map_bottom_sheet.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
            Resources resources = mContext.getResources();
            float f = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 230, resources.getDisplayMetrics());
            linearParams.height = (int) f;// 控件的宽强制设成30
//            linearParams.height = 250;// 控件的宽强制设成30
            map_bottom_sheet.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        }
//        addrTv.setVisibility(View.GONE);
        TextView tv_errorinfo = (TextView) map_bottom_sheet.findViewById(R.id.tv_errorinfo);
        TextView tv_parent_org_name = (TextView) map_bottom_sheet.findViewById(R.id.tv_parent_org_name);

        String type = layerName;


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
        dateTv.setText(StringUtil.getNotNullString(formatDate, ""));

        String sort = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_TWO));

        int color = ResourcesCompat.getColor(getResources(), R.color.mark_light_purple, null);

        if (sort.contains("雨污合流")) {
            color = ResourcesCompat.getColor(getResources(), R.color.mark_light_purple, null);
        } else if (sort.contains("雨水")) {
            color = ResourcesCompat.getColor(getResources(), R.color.progress_line_green, null);
        } else if (sort.contains("污水")) {
            color = ResourcesCompat.getColor(getResources(), R.color.agmobile_red, null);
        }
        sortTv.setTextColor(color);
//
//        if (sort.contains("雨污合流")) {
//            sortTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.mark_light_purple, null));
//        } else if (sort.contains("雨水")) {
//            sortTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.progress_line_green, null));
//        } else if (sort.contains("污水")) {
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
            field1Tv.setText(field1_1 + "：" + StringUtil.getNotNullString(field1, ""));

            String field2_1 = listFieldArray[1].split(":")[0];
            String field2_2 = listFieldArray[1].split(":")[1];
            String field2 = String.valueOf(component.getGraphic().getAttributes().get(field2_2));
            field2Tv.setText(field2_1 + "：" + StringUtil.getNotNullString(field2, ""));

            String field3_1 = listFieldArray[2].split(":")[0];
            String field3_2 = listFieldArray[2].split(":")[1];
            String field3 = String.valueOf(component.getGraphic().getAttributes().get(field3_2));
            field3Tv.setText(field3_1 + "：" + StringUtil.getNotNullString(field3, ""));
        }

        /**
         * 如果是雨水口，显示特性：方形
         */
        if ("雨水口".equals(component.getLayerName())) {
            String feature = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
            sortTv.setText(StringUtil.getNotNullString(feature, ""));
        }
        if ("雨水口".equals(type)) {
            String style = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE));
            subtypeTv.setText(StringUtil.getNotNullString(style, ""));
            subtypeTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.dust_grey, null));
        } else {
            subtypeTv.setText(StringUtil.getNotNullString(subtype, ""));
        }

        if ("雨水口".equals(type)) {
            field3Tv.setVisibility(View.GONE);
        } else {
            field3Tv.setVisibility(View.VISIBLE);
        }


        //已挂牌编号
        String codeValue = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.CODE));
        if (!StringUtil.isEmpty(codeValue)) {
            codeValue = codeValue.trim();
        }

        if ("窨井".equals(component.getLayerName())) {
            code.setVisibility(View.VISIBLE);
            if (!codeValue.isEmpty()) {
                code.setText("已挂牌编码" + ":" + StringUtil.getNotNullString(codeValue, ""));
            } else {
                code.setText("已挂牌编码" + ":" + "");
            }
        } else {
            code.setVisibility(View.GONE);
        }

        /**
         * 修改属性三
         */
        String field3 = "";
        if ("窨井".equals(component.getLayerName())) {
            field3 = "井盖材质: " + String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE));
            field3Tv.setVisibility(View.GONE);
        } else if ("排水口".equals(component.getLayerName())) {
            field3 = "排放去向: " + String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.RIVER));
        }
        field3Tv.setText(field3);


        final String address = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ADDR));
        addrTv.setText("设施位置" + "：" + StringUtil.getNotNullString(address, ""));

        /*
        if (errorInfo == null) {
            tv_errorinfo.setVisibility(View.GONE);
        } else {
            tv_errorinfo.setVisibility(View.VISIBLE);
            tv_errorinfo.setText(errorInfo + "?");
        }
        */
        tv_errorinfo.setVisibility(View.GONE);


//        String parentOrg = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FOUR));
//        tv_parent_org_name.setText("权属单位：" + StringUtil.getNotNullString(parentOrg, ""));
        ////   showBottomSheetContent(component_intro.getId());


        View line = map_bottom_sheet.findViewById(R.id.line);

        if ("排水口".equals(type)) {
//            parentOrg = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE));
            sortTv.setVisibility(View.GONE);
            code.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            subtypeTv.setText("河涌名称：" + component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
            field3 = "排水口岸别：" + component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FOUR);
        } else {
            sortTv.setVisibility(View.VISIBLE);
            code.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
        }
        field3Tv.setText(field3);
        field3Tv.setVisibility(View.GONE);
//        tv_parent_org_name.setText("权属单位：" + StringUtil.getNotNullString(parentOrg, ""));
        tv_parent_org_name.setVisibility(View.GONE);
        map_bottom_sheet.setVisibility(View.VISIBLE);
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        // loadCompleteDataAsync(component);
    }


    private void showDetail() {
        if (tableItems == null
                || projectId == null) {
            return;
        }
        if (tableViewManager == null) {
//            component_detail_container.removeAllViews();
            //动态表单实例
            tableViewManager =
                    new TableViewManager(getActivity(), component_detail_container,
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
            /**
             * 不显示查看位置按钮
             */
            tableViewManager.setDontShowCheckLocationButton();
        }

    }


    /*
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
            ToastUtil.shortToast(mContext, "加载详细信息失败！");
            return;
        }
        projectId = project.getId();
        String getFormStructureUrl = BaseInfoManager.getBaseServerUrl(mContext) + "rest/report/rptform";
        tableDataManager.getTableItemsFromNet(project.getId(), getFormStructureUrl, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                TableItems tmp = (TableItems) data;
                if (tmp.getResult() != null) {
                    tableItems = new ArrayList<TableItem>();
                    tableItems.addAll(tmp.getResult());
                    //   tableDataManager.setTableItemsToDB(tableItems);
                    //缓存在数据库中
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
                    TableViewManager.featueLayerUrl = component.getLayerUrl();
                    if (false) {
                        Intent intent = new Intent(getActivity(), ReEditTableActivity.class);
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
    */

    private void loadCompleteData(final Component component) {
        pd = new ProgressDialog(mContext);
        pd.setMessage("正在加载详细信息...");
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
            ToastUtil.shortToast(mContext, "加载详细信息失败！");
            return;
        }
        final String projectId = project.getId();
        String getFormStructureUrl = BaseInfoManager.getBaseServerUrl(mContext) + "rest/report/rptform";
        tableDataManager.getTableItemsFromNet(project.getId(), getFormStructureUrl, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                TableItems tmp = (TableItems) data;
                if (tmp.getResult() != null) {
                    tableItems = new ArrayList<TableItem>();
                    tableItems.addAll(tmp.getResult());
                    //   tableDataManager.setTableItemsToDB(tableItems);
                    //缓存在数据库中
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
                    TableViewManager.featueLayerUrl = component.getLayerUrl();
                    if (false) {
                        Intent intent = new Intent(getActivity(), ReEditTableActivity.class);
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
                ToastUtil.shortToast(mContext, "加载详细信息失败!");
            }
        });
    }

    private void queryAttachmentInfosAsync(String layerUrl, final Graphic graphic, final ArrayList<TableItem> completeTableItems, final String projectId) {
        final ArcGISFeatureLayer arcGISFeatureLayer = new ArcGISFeatureLayer(layerUrl, ArcGISFeatureLayer.MODE.SNAPSHOT);
        arcGISFeatureLayer.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (status == STATUS.INITIALIZED) {
                    if (mContext == null) {
                        return;
                    }
                    Object oid = graphic.getAttributes().get(arcGISFeatureLayer.getObjectIdField());
                    if (oid == null) {
                        return;
                    }
                    final int objectid = Integer.valueOf(oid.toString());
                    FileService fileService = new FileService(mContext);
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
                            Intent intent = new Intent(getActivity(), ReEditTableActivity.class);
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
                                        String tempFile = new FilePathUtil(mContext).getSavePath() + "/images/" + attachmentInfo.getName() + ".jpg";
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
                            ToastUtil.shortToast(mContext, "加载详细信息失败!");
                        }
                    });
                }
            }
        });
    }

    private void queryForObjctId(String objectId) {
        pd = new ProgressDialog(getContext());
        pd.setMessage("正在查询设施...");
        pd.show();
        mComponentQueryResult.clear();
        btn_next.setVisibility(View.GONE);
        btn_prev.setVisibility(View.GONE);
        currIndex = 0;
        if (componentService == null) {
            componentService = new ComponentService(mContext.getApplicationContext());
        }
        componentService.queryPSGJForObjectId(objectId)
                .flatMap(new Func1<List<Component>, Observable<Component>>() {
                    @Override
                    public Observable<Component> call(List<Component> modifiedIdentifications) {
                        mComponents = modifiedIdentifications;
                        return Observable.from(modifiedIdentifications);
                    }
                })
                //获取附件
                .flatMap(new Func1<Component, Observable<Component>>() {
                    @Override
                    public Observable<Component> call(final Component modifiedIdentification) {
                        String markId = String.valueOf(modifiedIdentification.getGraphic().getAttributes().get(UploadLayerFieldKeyConstant.MARK_ID));
                        if (StringUtil.isEmpty(markId) || "null".equals(markId)) {
                            markId = "-1";
                        }
                        return uploadFacilityService.getMyModificationAttachments(Long.valueOf(markId))
                                .map(new Func1<ServerAttachment, Component>() {
                                    @Override
                                    public Component call(ServerAttachment serverIdentificationAttachment) {
                                        List<ServerAttachment.ServerAttachmentDataBean> data = serverIdentificationAttachment.getData();
                                        if (!ListUtil.isEmpty(data)) {
                                            List<Photo> photos = new ArrayList<>();
                                            for (ServerAttachment.ServerAttachmentDataBean dataBean : data) {
                                                Photo photo = new Photo();
                                                photo.setId(Long.valueOf(dataBean.getId()));
                                                photo.setPhotoPath(dataBean.getAttPath());
                                                photo.setPhotoName(dataBean.getAttName());
                                                photo.setThumbPath(dataBean.getThumPath());
                                                photos.add(photo);
                                            }
                                            modifiedIdentification.setPhotos(photos);
                                        }
                                        return modifiedIdentification;
                                    }
                                });
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (pd != null) {
                            pd.dismiss();
                        }
                        /**
                         * 当查询失败的时候进行查询地址
                         */
                        if (!ListUtil.isEmpty(mComponents)) {
                            mComponentQueryResult = new ArrayList<Component>();
                            mComponentQueryResult.addAll(mComponents);
                            showComponentsOnBottomSheet(mComponentQueryResult);
                        } else {
                            hideBottomSheet();
                            ToastUtil.shortToast(getContext(), "没有设施数据");
                        }
                    }

                    @Override
                    public void onNext(List<Component> components) {
                        if (pd != null) {
                            pd.dismiss();
                        }

                        if (ListUtil.isEmpty(components)) {
                            hideBottomSheet();
                            ToastUtil.shortToast(getContext(), "没有设施数据");
                            return;
                        }
                        mComponentQueryResult = new ArrayList<Component>();
                        mComponentQueryResult.addAll(components);
                        showComponentsOnBottomSheet(mComponentQueryResult);
                    }
                });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectComponent(SelectComponentEvent selectComponentEvent) {
        Component component = selectComponentEvent.getComponent();
        currComponentUrl = component.getLayerUrl();
        layerAdapter.notifyDataSetChanged(LayerUrlConstant.getIndexByUnknowsLayerUrl(currComponentUrl));
        showBottomSheetForCheck(component);
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
     * 网络切换时，若设施URL为null，会重新初始化设施URL，然后发送OnInitLayerUrlFinishEvent事件
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
         * 退出时停止定位
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

    /**
     * 显示排水管井、管线简要信息
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

        mGLayer.removeAll();
        drawGeometry(component.getGraphic().getGeometry(), mGLayer, false, true);
        showCallout(component);
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

        View btn_container = map_bottom_sheet.findViewById(R.id.btn_container);//按钮控件容器
        TextView addrTv = (TextView) map_bottom_sheet.findViewById(R.id.addr);//地址
        TextView tv_gc_begin = (TextView) map_bottom_sheet.findViewById(R.id.tv_gc_begin);//起始高程
        TextView tv_gc_end = (TextView) map_bottom_sheet.findViewById(R.id.tv_gc_end);//终止高程
        TextView tv_ms_begin = (TextView) map_bottom_sheet.findViewById(R.id.tv_ms_begin);//起始埋深
        TextView tv_ms_end = (TextView) map_bottom_sheet.findViewById(R.id.tv_ms_end);//终点埋深
        TextView tv_gj = (TextView) map_bottom_sheet.findViewById(R.id.tv_gj);//管径
        View ll_gj = map_bottom_sheet.findViewById(R.id.ll_gj);//管径
        TextView tv_cz = (TextView) map_bottom_sheet.findViewById(R.id.tv_cz);//净高
        View ll_gq = map_bottom_sheet.findViewById(R.id.ll_gq);//净高
        TextView tv_tycd = (TextView) map_bottom_sheet.findViewById(R.id.tv_tycd);//投影长度
        TextView tv_pd = (TextView) map_bottom_sheet.findViewById(R.id.tv_pd);//坡度
        View line3 = map_bottom_sheet.findViewById(R.id.line3);//分割线
        View line2 = map_bottom_sheet.findViewById(R.id.line2);//分割线
        View line = map_bottom_sheet.findViewById(R.id.line);//分割线
        View line4 = map_bottom_sheet.findViewById(R.id.line4);//分割线
        TextView tv_errorinfo = (TextView) map_bottom_sheet.findViewById(R.id.tv_errorinfo);
        tv_errorinfo.setVisibility(View.GONE);
//        TextView code = (TextView) map_bottom_sheet.findViewById(R.id.code);
        line.setVisibility(View.VISIBLE);
        field3Tv.setVisibility(View.GONE);
        ll_gj.setVisibility(View.VISIBLE);
        btn_container.setVisibility(View.GONE);
        initValue(component, addrTv, "所在道路：", ComponentFieldKeyConstant.ADDR,0,"");
        String subtype = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.GRADE), "");
        map_bottom_sheet.findViewById(R.id.ll_no_ysk).setVisibility(View.VISIBLE);
        if (layerName.contains("排水管道")) {//管道就把值加载出来
            line2.setVisibility(View.VISIBLE);
            line3.setVisibility(View.VISIBLE);
            line4.setVisibility(View.VISIBLE);
            addrTv.setVisibility(View.VISIBLE);
            ll_gq.setVisibility(View.GONE);
            subtype = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.GRADE),"");
            initValue(component, tv_gc_begin, "起始管底高程：", ComponentFieldKeyConstant.BEG_H);
            initValue(component, tv_gc_end, "终止管底高程：", ComponentFieldKeyConstant.END_H);
            initValue(component, tv_ms_begin, "起点管底埋深：", ComponentFieldKeyConstant.BEGCEN_DEE);
            initValue(component, tv_ms_end, "终点管底埋深：", ComponentFieldKeyConstant.ENDCEN_DEE);
            initValue(component, tv_gj, "管径：", ComponentFieldKeyConstant.PIPE_GJ, 0, "mm");
            initValue(component, tv_cz, "管线长度：", ComponentFieldKeyConstant.LENGTH);
        } else if (layerName.contains("窨井")) {
            line2.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
            ll_gq.setVisibility(View.GONE);
            field3Tv.setVisibility(View.VISIBLE);
            tv_gc_end.setText("");
            if (layerName.contains("中心城区")) {
                addrTv.setVisibility(View.VISIBLE);
                tv_cz.setVisibility(View.GONE);
                tv_ms_end.setText("");
                initValue(component, tv_gj, "井盖大小：", ComponentFieldKeyConstant.COVER_SIZE, 2, "mm");
                initValue(component, tv_gc_begin, "地面高程：", ComponentFieldKeyConstant.SUR_H);
                initValue(component, tv_ms_begin, "井底高程：", ComponentFieldKeyConstant.BOTTOM_H);
            } else {
                addrTv.setVisibility(View.VISIBLE);
                tv_cz.setVisibility(View.GONE);
                tv_ms_end.setText("");
                tv_gj.setText("");
                initValue(component, tv_gc_begin, "已有挂牌号：", ComponentFieldKeyConstant.ATTR_FIVE, 0, "");
                initValue(component, tv_gj, "井盖大小：", ComponentFieldKeyConstant.COVER_SIZE, 2, "mm");
                ll_gj.setVisibility(View.GONE);
                initValue(component, tv_ms_begin, "所在道路：", ComponentFieldKeyConstant.ROAD, 0, "");
                initValue(component, addrTv, "设施位置：", ComponentFieldKeyConstant.ADDR, 0, "");
            }
        } else if(layerName.contains("雨水口")){
            map_bottom_sheet.findViewById(R.id.ll_no_ysk).setVisibility(View.GONE);
        }else if (layerName.contains("排水口")) {
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
//            subtypeTv.setText("河涌名称：" + component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
            initValue(component, tv_gc_begin, "排水口岸别：", ComponentFieldKeyConstant.ATTR_FOUR, 0, "");
            initValue(component, subtypeTv, "河涌名称：", ComponentFieldKeyConstant.ATTR_ONE, 0, "");
            initValue(component, tv_ms_begin, "所在道路：", ComponentFieldKeyConstant.ROAD, 0, "");
            initValue(component, addrTv, "设施位置：", ComponentFieldKeyConstant.ADDR, 0, "");
        } else if ("排水沟渠".equals(layerName)) {
            line3.setVisibility(View.VISIBLE);
            line4.setVisibility(View.VISIBLE);
            ll_gq.setVisibility(View.VISIBLE);
            addrTv.setVisibility(View.VISIBLE);
            tv_cz.setVisibility(View.VISIBLE);
            subtype = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE), "");
            initValue(component, tv_gc_begin, "起始渠底高程：", ComponentFieldKeyConstant.BEG_H);
            initValue(component, tv_gc_end, "终止渠底高程：", ComponentFieldKeyConstant.END_H);
            initValue(component, tv_ms_begin, "起点管底埋深：", ComponentFieldKeyConstant.BEGCIN_DEEP);
            initValue(component, tv_ms_end, "终点管底埋深：", ComponentFieldKeyConstant.ENDCIN_DEEP);
            initValue(component, tv_gj, "宽度：", ComponentFieldKeyConstant.WIDTH);
            initValue(component, tv_cz, "净高：", ComponentFieldKeyConstant.HEIGHT);
            initValue(component, tv_tycd, "投影长度：", ComponentFieldKeyConstant.LENGTH);
            initValue(component, tv_pd, "坡度：", ComponentFieldKeyConstant.IP);
        } else {
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
        if (layerName.contains("排水管道")) {
            sort = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SORT));
        } else {
            sort = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_TWO));
            if(StringUtil.isEmpty(sort) || "null".equals(sort)){
                sort = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SORT));
            }
        }
        int color = ResourcesCompat.getColor(getResources(), R.color.mark_light_purple, null);

        if (sort.contains("雨污合流")) {
            color = ResourcesCompat.getColor(getResources(), R.color.mark_light_purple, null);
        } else if (sort.contains("雨水")) {
            color = ResourcesCompat.getColor(getResources(), R.color.progress_line_green, null);
        } else if (sort.contains("污水")) {
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
            field1Tv.setText(field1_1 + "：" + StringUtil.getNotNullString(field1, ""));

            String field2_1 = listFieldArray[1].split(":")[0];
            String field2_2 = listFieldArray[1].split(":")[1];
            String field2 = String.valueOf(component.getGraphic().getAttributes().get(field2_2));
            field2Tv.setText(field2_1 + "：" + StringUtil.getNotNullString(field2, ""));

            String field3_1 = listFieldArray[2].split(":")[0];
            String field3_2 = listFieldArray[2].split(":")[1];
            String field3 = String.valueOf(component.getGraphic().getAttributes().get(field3_2));
            field3Tv.setText(field3_1 + "：" + StringUtil.getNotNullString(field3, ""));
        }

        /**
         * 如果是雨水口，显示特性：方形
         */
        if (layerName.contains("雨水口")) {
            String feature = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
            sortTv.setText(StringUtil.getNotNullString(feature, ""));
        }
        subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
        if(StringUtil.isEmpty(subtype) || "null".equals(subtype)){
            subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE));
        }
        if (layerName.contains("雨水口")) {
            String style = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE));
            subtypeTv.setText(StringUtil.getNotNullString(style, ""));
            subtypeTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.dust_grey, null));
        } else if(!"排水口".equals(layerName)){
            subtypeTv.setText(StringUtil.getNotNullString(subtype, ""));
        }


        //已挂牌编号
        String codeValue = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FIVE));
        if (!StringUtil.isEmpty(codeValue)) {
            codeValue = codeValue.trim();
        }

//        if ("窨井".equals(component.getLayerName())) {
//            code.setVisibility(View.VISIBLE);
//            if (!codeValue.isEmpty()) {
//                code.setText("已挂牌编码" + ":" + StringUtil.getNotNullString(codeValue, ""));
//            } else {
//                code.setText("已挂牌编码" + ":" + "");
//            }
//        } else {
//            code.setVisibility(View.GONE);
//        }

        /**
         * 修改属性三
         */
        String field3 = "";
        if (layerName.contains("窨井")) {
            field3 = "井盖材质: " + StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE), "");
            if(StringUtil.isEmpty(field3)){
                field3 = "井盖材质: " + StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.COVER_MATERIAL), "");
            }
            field3Tv.setText(field3);
        } else if (layerName.contains("排水管道")) {
            field3 = "管道材质: " + StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.MATERIAL1), "");
            field3Tv.setText(field3);
        } else if (layerName.contains("排水沟渠")) {
            field3 = "沟渠类型: " + StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE), "");
            field3Tv.setText(field3);
        }


        String parentOrg = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FOUR));

        View treamLine = map_bottom_sheet.findViewById(R.id.stream_line);

        /**
         * 纠错按钮
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
        ((TextView) map_bottom_sheet.findViewById(R.id.tv_error_correct)).setText("确定");

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
        if (StringUtil.isEmpty(value)) {
            tv_gc_begin.setText(name);
            return;
        }
        if (newScale == 0) {
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
     * 去掉中心城区的设施名称
     * @param oldLayerName
     * @return
     */
    private String getLayerName(String oldLayerName){
        if(!attribute.contains(oldLayerName))
            return "";
        if(oldLayerName.contains("窨井")){
            return "窨井";
        }else if(oldLayerName.contains("排水管道")){
            return "排水管道";
        }else if(oldLayerName.contains("雨水口")){
            return "雨水口";
        }else if(oldLayerName.contains("排水口")){
            return "排水口";
        }else if(oldLayerName.contains("排水沟渠")){
            return "排水沟渠";
        }else{
            return "";
        }
    }
}
