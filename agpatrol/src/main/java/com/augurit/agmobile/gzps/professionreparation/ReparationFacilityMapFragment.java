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

package com.augurit.agmobile.gzps.professionreparation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.OnInitLayerUrlFinishEvent;
import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.search.IOnSearchClickListener;
import com.augurit.agmobile.gzps.common.search.SearchFragment;
import com.augurit.agmobile.gzps.common.selectcomponent.LimitedLayerAdapter;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.ComponetListAdapter;
import com.augurit.agmobile.gzps.componentmaintenance.SelectComponentEvent;
import com.augurit.agmobile.gzps.componentmaintenance.model.QueryFeatureSet;
import com.augurit.agmobile.gzps.componentmaintenance.service.ComponentService;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.measure.view.MapMeasureView;
import com.augurit.agmobile.gzps.professionreparation.reparationnewfacility.ReparationNewFacilityActivity;
import com.augurit.agmobile.gzps.setting.MyUploadStatisticActivity;
import com.augurit.agmobile.gzps.uploadfacility.model.CheckUploadRecordResult;
import com.augurit.agmobile.gzps.uploadfacility.model.CompleteTableInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.service.CorrectFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadLayerService;
import com.augurit.agmobile.gzps.uploadfacility.view.UploadFacilitySuccessEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.CorrectOrConfirimFacilityActivity;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.MyModifiedFacilityTableViewManager;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.RefreshMyUploadList;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.UploadFacilityTableViewManager;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.common.widget.compassview.CompassView;
import com.augurit.agmobile.mapengine.common.widget.scaleview.MapScaleView;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.LayersService;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.mapengine.legend.service.OnlineLegendService;
import com.augurit.agmobile.mapengine.legend.view.ILegendView;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.common.SelectLocationTouchListener;
import com.augurit.agmobile.patrolcore.common.legend.ImageLegendView;
import com.augurit.agmobile.patrolcore.common.legend.LegendPresenter;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.common.model.LocationInfo;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.widget.LocationButton;
import com.augurit.agmobile.patrolcore.editmap.widget.LocationMarker;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView2;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.Callback4;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior;
import com.augurit.am.fw.utils.DensityUtil;
import com.augurit.am.fw.utils.DrawableUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.geocode.Locator;
import com.jakewharton.rxbinding.view.RxView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.augurit.agmobile.gzps.R.id.subtype;
import static com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior.STATE_ANCHOR;
import static com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior.STATE_COLLAPSED;
import static com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior.STATE_EXPANDED;

/**
 * ?????????????????????????????????????????????????????????
 *
 * @author ????????? ???liangshenghong
 * @version 1.0
 * @package ?????? ???com.augurit.agmobile.gzps.map
 * @createTime ???????????? ???2017-10-14
 * @modifyBy ????????? ???liangshenghong
 * @modifyTime ???????????? ???2017-10-14
 * @modifyMemo ???????????????
 */
public class ReparationFacilityMapFragment extends Fragment implements View.OnClickListener {

    private static final String KEY_MAP_STATE = "com.esri.MapState";
    private final String TAG = "ReparationSearchFragment";
    private LocationMarker locationMarker;
    /**
     * ????????????????????????
     */
    private boolean ifInEditMode = false;
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
    private View ll_layer_url_init_fail;
    private ViewGroup ll_topbar_container; //??????????????????
    private ViewGroup ll_tool_container;   //??????????????????
    View mView;

    MapView mMapView;

    private MapMeasureView mMapMeasureView;

    private ILayerView layerView;

    private PatrolLayerPresenter layerPresenter;
    private boolean loadLayersSuccess = true;

    private TextView show_all_layer;
    private GridView gridView;
    private LimitedLayerAdapter layerAdapter;
    //????????????????????????????????????????????????????????????URL
    private String currComponentUrl = LayerUrlConstant.newComponentUrls[0];

    ProgressDialog pd;
    ViewGroup map_bottom_sheet;
    ViewGroup dis_map_bottom_sheet;
    BottomSheetBehavior mBehavior;
    AnchorSheetBehavior mDisBehavior;
    private ComponetListAdapter componetListAdapter;
    //????????????????????????????????????
    private View component_intro;
    private View component_detail_ll;
    //?????????????????????????????????????????????TableViewManager
    private ViewGroup component_detail_container;
    private View btn_upload;
    private ArrayList<TableItem> tableItems = null;
    private ArrayList<Photo> photoList = new ArrayList<>();
    private String projectId;
    private TableViewManager tableViewManager;
    //????????????????????????
    private List<Component> mComponentQueryResult = new ArrayList<>();
    private int currIndex = 0;
    private View btn_prev;
    private View btn_next;
    private boolean ifFirstAdd = true;
    private boolean ifFirstEdit = true;
    private View layoutBottom;
    private Context mContext;

    /**
     * ???????????????????????????
     */
    private SelectLocationTouchListener defaultMapOnTouchListener;
    private View btn_add;
    private View btn_cancel;
    private SelectLocationTouchListener addModeSelectLocationTouchListener;
    private View.OnClickListener addModeCalloutSureButtonClickListener;
    private View btn_edit;
    private View btn_edit_cancel;
    private SelectLocationTouchListener editModeSelectLocationTouchListener;
    private View.OnClickListener editModeCalloutSureButtonClickListener;
    private ProgressDialog progressDialog;
    private int bottomHeight;
    private int bottomMargin;
    private View btn_dis_prev;
    private View btn_dis_next;

    /**
     * ??????????????????????????????
     */
    private boolean ifMyUploadLayerVisible = false;
    private boolean ifUploadLayerVisible = false;
    private ComponentService componentService;

    private DetailAddress componentDetailAddress = null;

    /**
     * ????????????????????????
     */
    private TextView tv_distribute_error_correct;
    private TextView tv_distribute_sure;
    private List<UploadedFacility> mUploadedFacilitys;
    private Component mCurrentComponent;
    private UploadLayerService mUploadLayerService;
    private ViewGroup ll_next_and_prev_container;
    private ViewGroup ll_table_item_container;
    private ModifiedFacility mCurrentModifiedFacility;
    private UploadedFacility mCurrentUploadedFacility;
    private CompleteTableInfo mCurrentCompleteTableInfo;
    private List<UploadInfo> mUploadInfos = new ArrayList<>();
    private ViewGroup dis_detail_ll;
    private ViewGroup dis_detail_container;
    private View myUploadLayerBtn;
    private View uploadLayerBtn;
    private TextView tv_check_hint;
    private View ll_check_hint;
    //private View pb_load_check_hint;
    private TextView tv_check_person;
    private TextView tv_check_phone;

    private CompassView mCompassView;
    private SearchFragment searchFragment;


    public static ReparationFacilityMapFragment getInstance(Bundle data) {
        ReparationFacilityMapFragment addComponentFragment2 = new ReparationFacilityMapFragment();
        addComponentFragment2.setArguments(data);
        return addComponentFragment2;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context != null) {
            mContext = context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity != null) {
            mContext = activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reparationmap, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        mView.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        ((TextView) mView.findViewById(R.id.tv_title)).setText("?????????????????????");

        mView.findViewById(R.id.ll_search).setVisibility(View.VISIBLE);
//        mView.findViewById(R.id.ll_search_obj).setVisibility(View.VISIBLE);
        ImageView iv_open_search = (ImageView) mView.findViewById(R.id.iv_open_search);
        iv_open_search.setImageResource(R.mipmap.ic_list);
        ((TextView) mView.findViewById(R.id.tv_search)).setText("????????????");
        mView.findViewById(R.id.tv_search).setVisibility(View.VISIBLE);

        mView.findViewById(R.id.ll_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MyUploadStatisticActivity.class);
                startActivity(intent);
            }
        });

        mView.findViewById(R.id.ll_search_obj).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFragment.show(getFragmentManager(),TAG);
            }
        });
        searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                //??????????????????
                if(!StringUtil.isEmpty(keyword)){
                    queryForObjctId(keyword);
                }
            }
        });
        ll_topbar_container = (ViewGroup) view.findViewById(R.id.ll_topbar_container);
        ll_tool_container = (ViewGroup) view.findViewById(R.id.ll_tool_container);

        // Find MapView and add feature layers
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.setMapBackground(Color.WHITE, Color.TRANSPARENT, 0f, 0f);//???????????????????????????????????????
        /**
         * ?????????
         */
        final MapScaleView scaleView = (MapScaleView) view.findViewById(R.id.scale_view);
        ViewGroup ll_rotate_container = (ViewGroup) view.findViewById(R.id.ll_rotate_container);
//        MapRotateView mapRotateVew = new MapRotateView(mContext, mMapView, ll_rotate_container);

        ViewGroup ll_compass_container = (ViewGroup) view.findViewById(R.id.ll_compass_container);
        mCompassView = new CompassView(mContext, ll_compass_container, mMapView);


        scaleView.setMapView(mMapView);
        // Set listeners on MapView
        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onStatusChanged(final Object source, final STATUS status) {
                if (STATUS.INITIALIZED == status) {
                    if (source instanceof MapView) {
                        //todo ????????????
                        if (PatrolLayerPresenter.initScale != -1 && PatrolLayerPresenter.initScale != 0) {
                            mMapView.setScale(PatrolLayerPresenter.initScale);
                            scaleView.setScale(PatrolLayerPresenter.initScale);
                        }

                        if (PatrolLayerPresenter.longitude != 0 && PatrolLayerPresenter.latitude != 0) {
                            Point point = new Point(PatrolLayerPresenter.longitude, PatrolLayerPresenter.latitude);
                            mMapView.centerAt(point, true);
                        }

                        if (locationButton != null) {
                            locationButton.followLocation();
                        }
                        mMapView.setMaxScale(30);
                        if (myUploadLayerBtn != null) {
                            myUploadLayerBtn.performClick();
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

        //???????????????????????????????????????????????????
        mUploadLayerService = new UploadLayerService(getContext());

        //????????????
        locationMarker = (LocationMarker) view.findViewById(R.id.locationMarker);

        locationButton = (LocationButton) view.findViewById(R.id.locationButton);
//        RotateManager mapRotateManager = mapRotateVew.getMapRotateManager();
//        locationButton.setRotaManage(mapRotateManager);
        locationButton.setRotateEnable(true);
        locationButton.setIfShowCallout(false);
        locationButton.setMapView(mMapView);
        locationButton.setScaleView(scaleView);
        locationButton.setOnceLocation(false);
        locationButton.setIfAlwaysCeterToLocation(true);
        locationButton.setOnStateChangedListener(new LocationButton.OnStateChangedListener() {
            @Override
            public void onStateChanged(LocationButton.State state) {

                if (mCompassView != null) {
                    if (state == LocationButton.State.rotate) {
                        mCompassView.setVisible(true);
                        mCompassView.setRotatable(true);
                    } else {
                        mCompassView.setRotatable(false);
                    }
                }
            }
        });
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) locationButton
                .getLayoutParams();
        bottomMargin = lp.bottomMargin;
        mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
        /**
         * ??????????????????
         */
        ll_component_list = (ViewGroup) view.findViewById(R.id.ll_component_list);

        /**
         * ??????????????????
         */
        locationMarker.changeIcon(R.mipmap.ic_add_facility_2);
        show_all_layer = (TextView) view.findViewById(R.id.show_all_layer);
        show_all_layer.setOnClickListener(this);
        gridView = (GridView) view.findViewById(R.id.gridview);
        layerAdapter = new LimitedLayerAdapter(getContext());
        gridView.setAdapter(layerAdapter);
        layerAdapter.setOnItemClickListener(new OnRecyclerItemClickListener<String>() {
            @Override
            public void onItemClick(View view, int position, String selectedData) {
                currComponentUrl = selectedData;
                initGLayer();
                hideBottomSheet();
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
        layoutBottom = view.findViewById(R.id.ll_bottm);
        map_bottom_sheet = (ViewGroup) view.findViewById(R.id.map_bottom_sheet);
        dis_map_bottom_sheet = (ViewGroup) view.findViewById(R.id.dis_map_bottom_sheet);
        layoutBottom.post(new Runnable() {
            @Override
            public void run() {
                bottomHeight = layoutBottom.getHeight();
            }
        });
        mDisBehavior = AnchorSheetBehavior.from(dis_map_bottom_sheet);
        mBehavior = BottomSheetBehavior.from(map_bottom_sheet);
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        btn_prev = view.findViewById(R.id.prev);
        btn_next = view.findViewById(R.id.next);
        btn_dis_prev = view.findViewById(R.id.dis_prev);
        btn_dis_next = view.findViewById(R.id.dis_next);
        component_intro = view.findViewById(R.id.intro);
        component_detail_ll = view.findViewById(R.id.detail_ll);
        component_detail_container = (ViewGroup) view.findViewById(R.id.detail_container);
        dis_detail_ll = (ViewGroup) view.findViewById(R.id.dis_detail_ll);
        dis_detail_container = (ViewGroup) view.findViewById(R.id.dis_detail_container);
        btn_upload = view.findViewById(R.id.btn_upload);
        mDisBehavior.setAnchorHeight(DensityUtil.dp2px(getContext(), 230));

        /*
        mBehavior.setAnchorHeight(DensityUtil.dp2px(getContext(), 250));
        mBehavior.setAnchorHeight(DensityUtil.dp2px(getContext(), 230));
        mDisBehavior.setAnchorHeight(DensityUtil.dp2px(getContext(), 230));
        mBehavior.setBottomSheetCallback(new AnchorSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int oldState, int newState) {
                //????????????????????????
               // onChangedBottomSheetState(newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        */

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
                        showBottomSheet(mComponentQueryResult.get(currIndex));
                        if (currIndex == (mComponentQueryResult.size() - 1)) {
                            btn_next.setVisibility(View.GONE);
                        }
                        if (currIndex > 0) {
                            btn_prev.setVisibility(View.VISIBLE);
                        }
                    }
                });

        RxView.clicks(btn_dis_prev)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currIndex--;
                        if (currIndex < 0) {
                            btn_dis_prev.setVisibility(View.GONE);
                            return;
                        }
                        dis_detail_container.removeAllViews();
                        ll_table_item_container.removeAllViews();
                        resetStatus(true);
                        mCurrentCompleteTableInfo = mUploadInfos.get(currIndex).getCompleteTableInfo();
                        if (mCurrentCompleteTableInfo != null) {
                            tv_distribute_error_correct.setVisibility(View.VISIBLE);
                        }
                        if (mUploadInfos.get(currIndex).getModifiedFacilities() != null) {
                            showBottomSheet(mUploadInfos.get(currIndex).getModifiedFacilities());
                        } else if (mUploadInfos.get(currIndex).getUploadedFacilities() != null) {
                            showBottomSheet(mUploadInfos.get(currIndex).getUploadedFacilities());
                        }

                        if (currIndex == 0) {
                            btn_dis_prev.setVisibility(View.GONE);
                        }
                        if (mUploadInfos.size() > 1) {
                            btn_dis_next.setVisibility(View.VISIBLE);
                        }
                    }
                });
        RxView.clicks(btn_dis_next)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currIndex++;
                        if (currIndex > mUploadInfos.size()) {
                            btn_dis_next.setVisibility(View.GONE);
                            return;
                        }
                        dis_detail_container.removeAllViews();
                        ll_table_item_container.removeAllViews();
                        resetStatus(true);
                        mCurrentCompleteTableInfo = mUploadInfos.get(currIndex).getCompleteTableInfo();
                        if (mCurrentCompleteTableInfo != null) {
                            tv_distribute_error_correct.setVisibility(View.VISIBLE);
                        }
                        if (mUploadInfos.get(currIndex).getModifiedFacilities() != null) {
                            showBottomSheet(mUploadInfos.get(currIndex).getModifiedFacilities());
                        } else if (mUploadInfos.get(currIndex).getUploadedFacilities() != null) {
                            showBottomSheet(mUploadInfos.get(currIndex).getUploadedFacilities());
                        }
                        if (currIndex == (mUploadInfos.size() - 1)) {
                            btn_dis_next.setVisibility(View.GONE);
                        }
                        if (currIndex > 0) {
                            btn_dis_prev.setVisibility(View.VISIBLE);
                        }
                    }
                });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableViewManager.uploadEdit();
            }
        });

        loadMap();
        /**
         * ??????
         */
        View ll_legend = view.findViewById(R.id.ll_legend);
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

        btn_add = view.findViewById(R.id.btn_add);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationMarker != null) {
                    locationMarker.setVisibility(View.GONE);
                }
                mMapView.getCallout().hide();
                btn_add.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.GONE);
                mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapMeasureView.stopMeasure();
                boolean locate = false;
                if (btn_edit_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_edit_cancel.performClick();
                }
                if (ifFirstAdd) {
                    ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "???????????????????????????????????????");
                    ifFirstAdd = false;
                }

                if (locationMarker != null) {
                    locationMarker.changeIcon(R.mipmap.ic_add_facility_2);
                    locationMarker.setVisibility(View.VISIBLE);
                }
                btn_add.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.VISIBLE);
                hideBottomSheet();
                initGLayer();
                setAddNewFacilityListener();
                //?????????????????????????????? setAddNewFacilityListener ??????????????????
                // ?????? addModeSelectLocationTouchListener ??? addModeCalloutSureButtonClickListener ????????????
                if (locate) {
                    addModeSelectLocationTouchListener.locate();
                }
            }
        });

        btn_edit = view.findViewById(R.id.btn_edit);
        btn_edit_cancel = view.findViewById(R.id.btn_edit_cancel);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapMeasureView.stopMeasure();
                boolean locate = false;
                if (btn_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_cancel.performClick();
                }
                ifInEditMode = true;
                btn_edit.setVisibility(View.GONE);
                btn_edit_cancel.setVisibility(View.VISIBLE);
                if (ifFirstEdit) {
                    ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "?????????????????????????????????");
                    ifFirstEdit = false;
                }

                if (locationMarker != null) {
                    locationMarker.changeIcon(R.mipmap.ic_check_data_2);
                    locationMarker.setVisibility(View.VISIBLE);
                }
                hideBottomSheet();
                initGLayer();
                setSearchFacilityListener();
                //?????????????????????????????? setSearchFacilityListener ??????????????????
                // ??????editModeSelectLocationTouchListener ??? editModeCalloutSureButtonClickListener ????????????
                if (locate) {
                    editModeSelectLocationTouchListener.locate();
                    if (mMapView.getCallout().isShowing()) {
                        mMapView.getCallout().animatedHide();
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
                if (locationMarker != null) {
                    locationMarker.setVisibility(View.GONE);
                }

                mMapView.getCallout().hide();
                hideBottomSheet();
                initGLayer();
                //1
                mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
            }
        });

        //????????????????????????
        myUploadLayerBtn = view.findViewById(R.id.ll_my_upload_layer);
        final TextView tv_my_upload_layer = (TextView) view.findViewById(R.id.tv_my_upload_layer);
        final SwitchCompat myUploadIv = (SwitchCompat) view.findViewById(R.id.iv_my_upload_layer);
        myUploadLayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ifMyUploadLayerVisible) {
                    myUploadIv.setChecked(false);
                    //myUploadIv.setImageResource(R.drawable.ic_invisible);
                    tv_my_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.invisible_state_text_color, null));
                    ifMyUploadLayerVisible = false;
                } else {
                    myUploadIv.setChecked(true);
                    //  myUploadIv.setImageResource(R.drawable.ic_visible);
                    tv_my_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                    ifMyUploadLayerVisible = true;
                }

                if (layerPresenter != null) {
                    layerPresenter.changeLayerVisibility(PatrolLayerPresenter.MY_UPLOAD_LAYER_NAME, ifMyUploadLayerVisible);
                }
            }
        });

        //????????????????????????
        uploadLayerBtn = view.findViewById(R.id.ll_upload_layer);
        final TextView tv_upload_layer = (TextView) view.findViewById(R.id.tv_upload_layer);
        final SwitchCompat uploadIv = (SwitchCompat) view.findViewById(R.id.iv_upload_layer);
        uploadLayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ifUploadLayerVisible) {
                    uploadIv.setChecked(false);
                    //myUploadIv.setImageResource(R.drawable.ic_invisible);
                    tv_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.invisible_state_text_color, null));
                    ifUploadLayerVisible = false;
                } else {
                    uploadIv.setChecked(true);
                    //  myUploadIv.setImageResource(R.drawable.ic_visible);
                    tv_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                    ifUploadLayerVisible = true;
                }

                if (layerPresenter != null) {
                    layerPresenter.changeLayerVisibility(PatrolLayerPresenter.LOCAL_UPLOAD_LAYER_NAME, ifUploadLayerVisible);
                }
            }
        });
        //????????????????????????????????????????????????
        if (layerPresenter != null) {
            layerPresenter.registerLayerVisibilityChangedListener(new PatrolLayerPresenter.OnLayerVisibilityChangedListener() {
                @Override
                public void changed(boolean visible, LayerInfo layerInfo) {
                    if (!visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.LOCAL_UPLOAD_LAYER_NAME)) {
                        //?????????
                        //  myUploadIv.setImageResource(R.drawable.ic_upload_data_normal2);
                        uploadIv.setChecked(false);
                        tv_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.invisible_state_text_color, null));
                        ifUploadLayerVisible = false;
                    } else if (visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.LOCAL_UPLOAD_LAYER_NAME)) {
                        //??????
                        // myUploadIv.setImageResource(R.drawable.ic_upload_data_pressed);
                        uploadIv.setChecked(true);
                        tv_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                        ifUploadLayerVisible = true;
                    } else if (!visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.MY_UPLOAD_LAYER_NAME)) {
                        //?????????
                        //  myUploadIv.setImageResource(R.drawable.ic_upload_data_normal2);
                        myUploadIv.setChecked(false);
                        tv_my_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.invisible_state_text_color, null));
                        ifMyUploadLayerVisible = false;
                    } else if (visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.MY_UPLOAD_LAYER_NAME)) {
                        //??????
                        // myUploadIv.setImageResource(R.drawable.ic_upload_data_pressed);
                        myUploadIv.setChecked(true);
                        tv_my_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                        ifMyUploadLayerVisible = true;
                    }
                }
            });
        }
        initBottomSheetView();
    }

    private void showOnBottomSheet(List<UploadInfo> uploadInfos) {
        if (uploadInfos.size() > 1) {
            ll_next_and_prev_container.setVisibility(View.VISIBLE);
            btn_next.setVisibility(View.VISIBLE);
        }
        if (mMapView.getCallout().isShowing()) {
            mMapView.getCallout().animatedHide();
        }

        component_detail_container.removeAllViews();
        ll_table_item_container.removeAllViews();
        //??????marker
        locationMarker.setVisibility(View.GONE);
        initGLayer();
        Geometry geometry = null;
        mCurrentCompleteTableInfo = uploadInfos.get(0).getCompleteTableInfo();
        if (uploadInfos.get(0).getUploadedFacilities() != null) {
            geometry = new Point(uploadInfos.get(0).getUploadedFacilities().getX(), uploadInfos.get(0).getUploadedFacilities().getY());
            showBottomSheet(uploadInfos.get(0).getUploadedFacilities());
        } else if (uploadInfos.get(0).getModifiedFacilities() != null) {
            geometry = new Point(uploadInfos.get(0).getModifiedFacilities().getOriginX(), uploadInfos.get(0).getModifiedFacilities().getOriginY());
            showBottomSheet(uploadInfos.get(0).getModifiedFacilities());
        }
        if (geometry != null) {
            drawGeometry(geometry, mGLayer, true, true);
        }
    }

    /**
     * ????????????
     * ????????????????????????????????????BottomSheet???
     */
    private void showBottomSheet(final UploadedFacility uploadedFacility) {
        mCurrentModifiedFacility = null;
        //initGLayer();
        if (uploadedFacility == null) {
            return;
        }
        mCurrentUploadedFacility = uploadedFacility;

        /**
         * ??????????????????
         */

        if (uploadedFacility.getIsBinding() == 1 && mCurrentCompleteTableInfo != null) {
            tv_distribute_error_correct.setVisibility(View.VISIBLE);
        } else {
            tv_distribute_error_correct.setVisibility(View.GONE);
        }
        dis_map_bottom_sheet.setVisibility(View.VISIBLE);
        dis_detail_container.setVisibility(View.VISIBLE);

        if (dis_detail_container.getChildCount() == 0) {
            initGLayer();
            Geometry geometry = new Point(uploadedFacility.getX(), uploadedFacility.getY());
            drawGeometry(geometry, mGLayer, true, true);
            UploadFacilityTableViewManager modifiedIdentificationTableViewManager = new UploadFacilityTableViewManager(getContext(),
                    uploadedFacility);
            modifiedIdentificationTableViewManager.addTo(dis_detail_container);
            if (mDisBehavior.getState() == STATE_COLLAPSED || mDisBehavior.getState() == com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior.STATE_HIDDEN) {
                dis_detail_container.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDisBehavior.setState(STATE_ANCHOR);
                    }
                }, 200);
            }
        }

        //component_detail_container.removeAllViews();

        dis_detail_ll.setVisibility(View.VISIBLE);
    }

    /**
     * ????????????
     * ????????????????????????????????????BottomSheet???
     */
    private void showBottomSheet(final CompleteTableInfo CompleteTableInfo) {
        //initGLayer();
        mCurrentCompleteTableInfo = CompleteTableInfo;
        ll_table_item_container.removeAllViews();

        //        addrTv.setVisibility(View.GONE);
        TextView tv_errorinfo = (TextView) map_bottom_sheet.findViewById(R.id.tv_errorinfo);
        TextView tv_parent_org_name = (TextView) map_bottom_sheet.findViewById(R.id.tv_parent_org_name);

        String name = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.NAME));


        String subtype = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.SUBTYPE));
        String usid = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.USID));


//        String title = StringUtil.getNotNullString(type, "") + "(" + usid + ")";
//        titleTv.setText(title);

//        String date = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.UPDATEDATE));
//        String formatDate = "";
//        try {
//            formatDate = TimeUtil.getStringTimeYMD(new Date(Long.valueOf(date)));
//        } catch (Exception e) {
//
//        }
//        dateTv.setText(StringUtil.getNotNullString(formatDate, ""));

        String sort = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.SORT));

        int color = ResourcesCompat.getColor(getResources(), R.color.mark_light_purple, null);

        if (sort.contains("????????????")) {
            color = ResourcesCompat.getColor(getResources(), R.color.mark_light_purple, null);
        } else if (sort.contains("??????")) {
            color = ResourcesCompat.getColor(getResources(), R.color.progress_line_green, null);
        } else if (sort.contains("??????")) {
            color = ResourcesCompat.getColor(getResources(), R.color.agmobile_red, null);
        }
        TextItemTableItem sortTv = new TextItemTableItem(getContext());
        sortTv.setTextViewName("??????");
        sortTv.setText(StringUtil.getNotNullString(sort, ""));
        sortTv.setReadOnly();

        /**
         * ??????????????????????????????????????????
         */
        String layertype = "";
        if (mCurrentModifiedFacility != null) {
            layertype = mCurrentModifiedFacility.getLayerName();
        } else if (mCurrentUploadedFacility != null) {
            layertype = mCurrentUploadedFacility.getLayerName();
        }
        TextItemTableItem ssTv = new TextItemTableItem(getContext());
        ssTv.setTextViewName("??????");
        ssTv.setText(StringUtil.getNotNullString(layertype, "") + "(" + usid + ")");
        ssTv.setReadOnly();
        ll_table_item_container.addView(ssTv);
        if (layertype.equals("?????????")) {
            String feature = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.FEATURE));
            sortTv.setText(StringUtil.getNotNullString(feature, ""));
        }
//
        ll_table_item_container.addView(sortTv);
        /**
         * ???????????????
         */
//        if ("?????????".equals(type)) {
//            String style = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.STYLE));
//            subtypeTv.setText(StringUtil.getNotNullString(style, ""));
//        } else {
//            subtypeTv.setText(StringUtil.getNotNullString(subtype, ""));
//        }
//
//        if ("?????????".equals(type)) {
//            field3Tv.setVisibility(View.GONE);
//        } else {
//            field3Tv.setVisibility(View.VISIBLE);
//        }
        String field3 = "";
        TextItemTableItem csortTv = new TextItemTableItem(getContext());
        if (layertype.equals("??????")) {
//            field3 = "????????????: " + String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.MATERIAL));
            csortTv.setTextViewName("????????????");
            csortTv.setText(String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.MATERIAL)));
            csortTv.setReadOnly();
        } else if (layertype.equals("?????????")) {
            field3 = "????????????: " + String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.RIVER));
            csortTv.setTextViewName("????????????");
            csortTv.setText(String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.RIVER)));
            csortTv.setReadOnly();
        }
        ll_table_item_container.addView(csortTv);


        final String address = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.ADDR));
        TextItemTableItem addressTv = new TextItemTableItem(getContext());
        addressTv.setTextViewName("????????????");
        addressTv.setText(StringUtil.getNotNullString(address, ""));
        addressTv.setReadOnly();
        ll_table_item_container.addView(addressTv);
//        addrTv.setText("????????????" + "???" + StringUtil.getNotNullString(address, ""));

        //???????????????
        TextItemTableItem bianhaoTv = new TextItemTableItem(getContext());
        bianhaoTv.setTextViewName("???????????????");
        bianhaoTv.setReadOnly();
        String codeValue = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.CODE));
        codeValue = codeValue.trim();
        if (layertype.equals("??????")) {
            if (!codeValue.isEmpty()) {
                bianhaoTv.setText(StringUtil.getNotNullString(codeValue, ""));
            } else {
//                bianhaoTv.setText("???");
                bianhaoTv.setText("");
            }
            ll_table_item_container.addView(bianhaoTv);
        }
        String parentOrg = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.OWNERDEPT));
        TextItemTableItem quanshuTv = new TextItemTableItem(getContext());
        quanshuTv.setTextViewName("????????????");
        quanshuTv.setText(StringUtil.getNotNullString(parentOrg, ""));
        quanshuTv.setReadOnly();
        ll_table_item_container.addView(quanshuTv);
    }


    /**
     * ????????????
     * ????????????????????????????????????BottomSheet???
     */
    private void showBottomSheet(final ModifiedFacility modifiedFacility) {
        //initGLayer();
        if (modifiedFacility == null) {
            return;
        }

        dis_detail_container.setVisibility(View.VISIBLE);

        if (dis_detail_container.getChildCount() == 0) {
            if (mCurrentCompleteTableInfo != null) {
                tv_distribute_error_correct.setVisibility(View.VISIBLE);
            } else {
                tv_distribute_error_correct.setVisibility(View.GONE);
            }
            mCurrentModifiedFacility = modifiedFacility;
            Geometry geometry = new Point(modifiedFacility.getOriginX(), modifiedFacility.getOriginY());
            initGLayer();
            drawGeometry(geometry, mGLayer, true, true);
            dis_map_bottom_sheet.setVisibility(View.VISIBLE);
            //component_detail_container.removeAllViews();
            MyModifiedFacilityTableViewManager modifiedIdentificationTableViewManager = new MyModifiedFacilityTableViewManager(getContext(),
                    modifiedFacility, true);
            modifiedIdentificationTableViewManager.addTo(dis_detail_container);
            modifiedIdentificationTableViewManager.setReadOnly(modifiedFacility, null);
            if (mDisBehavior.getState() == STATE_COLLAPSED || mDisBehavior.getState() == com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior.STATE_HIDDEN) {
                dis_detail_container.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDisBehavior.setState(com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior.STATE_ANCHOR);
                    }
                }, 200);
            }

        }


        dis_detail_ll.setVisibility(View.VISIBLE);
    }

    private void initBottomSheetView() {
        ll_next_and_prev_container = (ViewGroup) dis_map_bottom_sheet.findViewById(R.id.ll_next_and_prev_container);
        ll_table_item_container = (ViewGroup) dis_map_bottom_sheet.findViewById(R.id.ll_table_item_container);
        // pb_distribute = (ProgressBar) map_bottom_sheet.findViewById(R.id.pb_distribute);

        //????????????
        tv_distribute_sure = (TextView) dis_map_bottom_sheet.findViewById(R.id.tv_distribute_sure);
        tv_distribute_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetStatus(true);
                dis_detail_container.setVisibility(View.VISIBLE);
                ll_table_item_container.setVisibility(View.GONE);
                showBottomSheet(mCurrentModifiedFacility);

            }
        });


        //???????????????
        tv_distribute_error_correct = (TextView) dis_map_bottom_sheet.findViewById(R.id.tv_distribute_error_correct);
        tv_distribute_error_correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetStatus(false);
                dis_detail_container.setVisibility(View.GONE);
                ll_table_item_container.setVisibility(View.VISIBLE);
                if (mCurrentCompleteTableInfo != null) {
                    showBottomSheet(mCurrentCompleteTableInfo);
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
    }

    private void resetStatus(boolean reset) {
        if (reset) {
            tv_distribute_sure.setBackground(getResources().getDrawable(R.drawable.round_blue_rectangle));
            tv_distribute_sure.setTextColor(getResources().getColor(R.color.agmobile_white));
            tv_distribute_error_correct.setBackground(getResources().getDrawable(R.drawable.round_grey_rectangle));
            tv_distribute_error_correct.setTextColor(getResources().getColor(R.color.agmobile_blue));
        } else {
            tv_distribute_sure.setBackground(getResources().getDrawable(R.drawable.round_grey_rectangle));
            tv_distribute_sure.setTextColor(getResources().getColor(R.color.agmobile_blue));
            tv_distribute_error_correct.setBackground(getResources().getDrawable(R.drawable.round_blue_rectangle));
            tv_distribute_error_correct.setTextColor(getResources().getColor(R.color.agmobile_white));
        }
    }

    private void initLegendPresenter() {
        if (legendPresenter == null) {
            ILegendView legendView = new ImageLegendView(mContext);
            legendPresenter = new LegendPresenter(legendView, new OnlineLegendService(mContext));
        }

    }


    public void loadMap() {
        final IDrawerController drawerController = (IDrawerController) mContext;
        PatrolLayerView2 patrolLayerView2 = new PatrolLayerView2(mContext, mMapView, drawerController.getDrawerContainer());
        layerView = patrolLayerView2;
        layerPresenter = new PatrolLayerPresenter(layerView, new UploadLayerService(mContext.getApplicationContext()));
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


        ViewGroup ll_measure_container = patrolLayerView2.getBottomLayout();
        mMapMeasureView = new MapMeasureView(mContext, mMapView, ll_measure_container,
                ll_topbar_container, ll_tool_container);
        mMapMeasureView.setOnStartCallback(new Callback4() {
            @Override
            public void before() {
                if (btn_cancel.getVisibility() == View.VISIBLE) {
                    btn_cancel.performClick();
                }
                if (btn_edit_cancel.getVisibility() == View.VISIBLE) {
                    btn_edit_cancel.performClick();
                }
            }

            @Override
            public void after() {
                drawerController.closeDrawer();
            }
        });
        mMapMeasureView.setOnStopCallback(new Callback4() {
            @Override
            public void before() {

            }

            @Override
            public void after() {
                mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
            }
        });
    }


    private void showCallout(DetailAddress address, Point point, final View.OnClickListener calloutSureButtonClickListener) {
        if (address != null) {
            final Point geometry = point;
            final Callout callout = mMapView.getCallout();
            View view = View.inflate(mContext, R.layout.view_addfacility_callout, null);
            ((TextView) view.findViewById(com.augurit.agmobile.patrolcore.R.id.tv_listcallout_title)).setText(address.getDetailAddress());
            view.findViewById(com.augurit.agmobile.patrolcore.R.id.btn_select_device).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            callout.setStyle(com.augurit.agmobile.patrolcore.R.xml.editmap_callout_style);
            callout.setContent(view);
            if (point == null) {
                point = mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2, locationMarker.getY() + locationMarker.getHeight() / 2 - 140);
            }
            callout.show(point);
        }

    }


    /**
     * ??????????????????
     *
     * @param point
     * @param callback1
     */
    private void requestLocation(Point point, SpatialReference spatialReference, final Callback1<String> callback1) {
        SelectLocationService selectLocationService = new SelectLocationService(mContext, Locator.createOnlineLocator());
        selectLocationService.parseLocation(new LatLng(point.getY(), point.getX()), spatialReference)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DetailAddress>() {
                    @Override
                    public void call(DetailAddress detailAddress) {
                        if (callback1 != null) {
                            callback1.onCallback(detailAddress.getDetailAddress());
                        }
                    }
                });
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

    private Point getMapCenterPoint() {
        //?????????????????????
        return mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2, locationMarker.getY() + locationMarker.getHeight() / 2);
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

    private boolean hasLoadLayerBefore = false;

    /**
     * ??????????????????
     */
    public void showLayerList() {

//        if (hasLoadLayerBefore) {
//            return;
//        }

        if (layerPresenter != null) {
            layerPresenter.showLayerList();
            hasLoadLayerBefore = true;
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
        if (map_bottom_sheet.getVisibility() == View.GONE && dis_map_bottom_sheet.getVisibility() == View.GONE) {
            ((Activity) mContext).finish();
            return;
        }
        if (mBehavior != null
                && map_bottom_sheet != null) {
            if (mBehavior.getState() == STATE_EXPANDED) {
                mBehavior.setState(STATE_COLLAPSED);
            } else if (mBehavior.getState() == STATE_COLLAPSED
                    || mBehavior.getState() == AnchorSheetBehavior.STATE_ANCHOR) {
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
        if (mDisBehavior != null
                && dis_map_bottom_sheet != null) {
            if (mDisBehavior.getState() == STATE_EXPANDED) {
                mDisBehavior.setState(STATE_COLLAPSED);
            } else if (mDisBehavior.getState() == STATE_COLLAPSED
                    || mDisBehavior.getState() == AnchorSheetBehavior.STATE_ANCHOR) {
                initGLayer();
                dis_map_bottom_sheet.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapMeasureView.stopMeasure();
        mMapView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.unpause();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_MAP_STATE, mMapView.retainState());
    }


    /**
     * @return
     */
    private SelectLocationTouchListener getDefaultMapOnTouchListener() {
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
            if (locationMarker != null) {
                locationMarker.setVisibility(View.GONE);
            }
            if (mMapView.getCallout().isShowing()) {
                mMapView.getCallout().hide();
            }
            int visibility = map_bottom_sheet.getVisibility();
            int disvisibility = dis_map_bottom_sheet.getVisibility();
            initGLayer();
            hideBottomSheet();
            if (visibility == View.VISIBLE || disvisibility == View.VISIBLE) {
                return;
            }
            double scale = mMapView.getScale();
            if (scale < LayerUrlConstant.MIN_QUERY_SCALE) {
                final Point point = mMapView.toMapPoint(e.getX(), e.getY());
//                if(ifMyUploadLayerVisible){
//                    queryDistribute(point.getX(), point.getY());
//                }else {
//                    query(point.getX(), point.getY());
//                }
                query(point.getX(), point.getY());
            }


        }

    }

    /**
     * ???????????????????????????
     *
     * @param x
     * @param y
     */
    private void queryDistribute(final double x, final double y) {
        pd = new ProgressDialog(getContext());
        pd.setMessage("????????????????????????...");
        pd.show();
        mUploadInfos.clear();
        ll_next_and_prev_container.setVisibility(View.GONE);
        btn_next.setVisibility(View.GONE);
        btn_prev.setVisibility(View.GONE);
        currIndex = 0;
//        final Point point = mMapView.toMapPoint(x, y);
        final Point point = new Point(x, y);

        mCurrentModifiedFacility = null;
        mCurrentUploadedFacility = null;
        mCurrentCompleteTableInfo = null;
        tv_distribute_error_correct.setVisibility(View.VISIBLE);
        //tv_sure.setVisibility(View.GONE);
        resetStatus(true);
        mUploadLayerService.setQueryByWhere("MARK_PID='" + BaseInfoManager.getLoginName(getContext()) + "'");
        mUploadLayerService.queryUploadDataInfo(point, mMapView.getSpatialReference(), mMapView.getResolution(), new Callback2<List<UploadInfo>>() {
            @Override
            public void onSuccess(List<UploadInfo> uploadInfos) {
                pd.dismiss();
                if (ListUtil.isEmpty(uploadInfos) || (uploadInfos.size() == 1 && uploadInfos.get(0).getUploadedFacilities() == null && uploadInfos.get(0).getModifiedFacilities() == null)) {
//                    ToastUtil.iconLongToast(getContext(), R.mipmap.ic_alert_yellow, "???????????????");
                    query(x, y);
                    return;
                }
                mUploadInfos = uploadInfos;
                showOnBottomSheet(uploadInfos);
            }

            @Override
            public void onFail(Exception error) {
                query(x, y);
                pd.dismiss();
            }
        });
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
        final List<LayerInfo> layerInfoList = new ArrayList<>();
        for (String url : LayerUrlConstant.newComponentUrls) {
            LayerInfo layerInfo = new LayerInfo();
            layerInfo.setUrl(url);
            layerInfoList.add(layerInfo);
        }
        Geometry geometry = GeometryEngine.buffer(point, mMapView.getSpatialReference(), 40 * mMapView.getResolution(), null);

        initComponentService();
        if (true) {
//            componentService.queryReparationComponents(geometry, new Callback2<List<Component>>() {
//                @Override
//                public void onSuccess(List<Component> components) {
//                    if (pd != null) {
//                        pd.dismiss();
//                    }
//                    if (ListUtil.isEmpty(components)) {
//                        ToastUtil.shortToast(getContext(), "???????????????????????????");
//                        return;
//                    }
//                    mComponentQueryResult = new ArrayList<Component>();
//                    mComponentQueryResult.addAll(components);
//                    if (ListUtil.isEmpty(mComponentQueryResult)) {
//                        initGLayer();
//                        if (pd != null) {
//                            pd.dismiss();
//                        }
//                        ToastUtil.shortToast(getContext(), "???????????????????????????");
//                        return;
//                    }
//                    showComponentsOnBottomSheet(mComponentQueryResult);
//                }
//
//                @Override
//                public void onFail(Exception error) {
//                    if (pd != null) {
//                        pd.dismiss();
//                    }
//                    ToastUtil.shortToast(getContext(), "???????????????????????????");
//                }
//            });
//            componentService.queryPrimaryComponentsIdentify(geometry, mMapView, null);
        } else {
            final String oldLayerUrl = LayerUrlConstant.getLayerUrlByLayerName(LayerUrlConstant.getLayerNameByNewLayerUrl(currComponentUrl));
            componentService.queryComponents(geometry, oldLayerUrl, currComponentUrl, new Callback2<List<QueryFeatureSet>>() {
                @Override
                public void onSuccess(List<QueryFeatureSet> queryFeatureSetList) {
                    if (ListUtil.isEmpty(queryFeatureSetList)) {
                        if (pd != null) {
                            pd.dismiss();
                        }
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

                    if (ListUtil.isEmpty(mComponentQueryResult)) {
                        initGLayer();
                        if (pd != null) {
                            pd.dismiss();
                        }
                        ToastUtil.shortToast(getContext(), "???????????????????????????");
                        return;
                    }

                    componentService.queryUpStreamAndDownStreams(oldLayerUrl, mComponentQueryResult)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<List<Component>>() {
                                @Override
                                public void onCompleted() {
                                    if (pd != null) {
                                        pd.dismiss();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (pd != null) {
                                        pd.dismiss();
                                    }
                                    showComponentsOnBottomSheet(mComponentQueryResult);
                                }

                                @Override
                                public void onNext(List<Component> components) {
                                    mComponentQueryResult = components;
                                    showComponentsOnBottomSheet(mComponentQueryResult);
                                }
                            });

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

    }

    private void queryForObjctId(String objectId) {
        pd = new ProgressDialog(getContext());
        pd.setMessage("??????????????????...");
        pd.show();
        mComponentQueryResult.clear();
        btn_next.setVisibility(View.GONE);
        btn_prev.setVisibility(View.GONE);
        currIndex = 0;
        initComponentService();
        if (true) {
            componentService.queryPrimaryComponentsForObjectId(objectId, new Callback2<List<Component>>() {
                @Override
                public void onSuccess(List<Component> components) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    if (ListUtil.isEmpty(components)) {
                        hideBottomSheet();
                        ToastUtil.shortToast(getContext(), "????????????????????????????????????");
                        return;
                    }
                    mComponentQueryResult = new ArrayList<Component>();
                    mComponentQueryResult.addAll(components);
                    if (ListUtil.isEmpty(mComponentQueryResult)) {
                        initGLayer();
                        if (pd != null) {
                            pd.dismiss();
                        }
                        hideBottomSheet();
                        ToastUtil.shortToast(getContext(), "????????????????????????????????????");
                        return;
                    }
                    showComponentsOnBottomSheet(mComponentQueryResult);
                }

                @Override
                public void onFail(Exception error) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    hideBottomSheet();
                    ToastUtil.shortToast(getContext(), "????????????????????????????????????");
                }
            });
        }
    }

    @NonNull
    private void initComponentService() {
        if (componentService == null) {
            componentService = new ComponentService(mContext.getApplicationContext());
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

        if (graphicsLayer == null) {
            return;
        }
        Symbol symbol = null;
        switch (geometry.getType()) {
            case LINE:
            case POLYLINE:
                symbol = new SimpleLineSymbol(Color.RED, 5);
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

        if (ifCenter) {
            mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        }
    }

    /**
     * ?????????????????????
     *
     * @param graphicsLayer
     * @param ifRemoveAll   ?????????????????????????????????
     * @param geometry
     */
    private void drawGeometry(Geometry geometry, GraphicsLayer graphicsLayer, Symbol designatedSymbol, boolean ifRemoveAll, boolean ifCenter) {

        if (graphicsLayer == null) {
            return;
        }
        Symbol symbol = designatedSymbol;
        if (symbol == null) {
            switch (geometry.getType()) {
                case LINE:
                case POLYLINE:
                    symbol = new SimpleLineSymbol(Color.RED, 5);
                    break;
                case POINT:
//                symbol = new SimpleMarkerSymbol(Color.RED, 25, SimpleMarkerSymbol.STYLE.CIRCLE);
                    symbol = getPointSymbol();
                    break;
                default:
                    break;
            }
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

        if (ifCenter) {
            mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
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
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(getContext(), drawable);// xjx ????????????api19???????????????drawable
        pictureMarkerSymbol.setOffsetY(16);
        return pictureMarkerSymbol;
    }

    /*
    private void onChangedBottomSheetState(int state) {
        if (state == STATE_EXPANDED) {
            //BottomSheet???????????????????????????
            if (component_intro.getVisibility() == View.VISIBLE) {
                showBottomSheetContent(component_detail_container.getId());
                showDetail();
            }

        } else if (state == STATE_COLLAPSED) {
            //BottomSheet?????????????????????????????????
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
        dis_map_bottom_sheet.setVisibility(View.GONE);
        final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) locationButton
                .getLayoutParams();
        lp.bottomMargin = bottomMargin;
        locationButton.setLayoutParams(lp);
    }

    private void showComponentsOnBottomSheet(List<Component> componentQueryResult) {
        if (componentQueryResult.size() > 1) {
            btn_next.setVisibility(View.VISIBLE);
        }
        if (mMapView.getCallout().isShowing()) {
            mMapView.getCallout().animatedHide();
        }

        //??????marker
        locationMarker.setVisibility(View.GONE);
        //initGLayer();
        showBottomSheet(mComponentQueryResult.get(0));
    }

    private void drawUpStream(Component component, int color) {
        List<Component> upStreams = component.getUpStream();
        if (ListUtil.isEmpty(upStreams)) {
            return;
        }

        Point componentCenter = GeometryUtil.getGeometryCenter(component.getGraphic().getGeometry());
        for (Component upStream : upStreams) {
            Polyline polyline = new Polyline();
            Point geometryCenter = GeometryUtil.getGeometryCenter(upStream.getGraphic().getGeometry());
            if (geometryCenter != null) {
                polyline.startPath(geometryCenter);
                polyline.lineTo(GeometryUtil.getGeometryCenter(component.getGraphic().getGeometry()));
                drawGeometry(polyline, mGLayer, new SimpleLineSymbol(color, 5), false, false);
                //?????????????????????????????????????????????????????????????????????
                //?????????
                Point point = mMapView.toScreenPoint(geometryCenter);
                //?????????
                Point point2 = mMapView.toScreenPoint(componentCenter);
                double angle = GeometryUtil.getAngle(point.getX(), point.getY(), point2.getX(), point2.getY());
                drawArrow(GeometryUtil.getGeometryCenter(polyline), angle, color, false);
            }

            List<Component> upStream1 = upStream.getUpStream();
            if (!ListUtil.isEmpty(upStream1)) {
                drawUpStream(upStream, color);
            }
        }
    }

    private void drawDownStream(Component component, int color) {
        List<Component> downStreams = component.getDownStream();
        if (ListUtil.isEmpty(downStreams)) {
            return;
        }
        Point componentCenter = GeometryUtil.getGeometryCenter(component.getGraphic().getGeometry());
        for (Component downStream : downStreams) {
            Polyline polyline = new Polyline();
            Point geometryCenter = GeometryUtil.getGeometryCenter(downStream.getGraphic().getGeometry());
            if (geometryCenter != null) {
                polyline.startPath(geometryCenter);
                polyline.lineTo(GeometryUtil.getGeometryCenter(component.getGraphic().getGeometry()));
                drawGeometry(polyline, mGLayer, new SimpleLineSymbol(color, 5), false, false);
                //?????????????????????????????????????????????????????????????????????
                //?????????
                Point point = mMapView.toScreenPoint(geometryCenter);
                //?????????
                Point point2 = mMapView.toScreenPoint(componentCenter);
                double angle = GeometryUtil.getAngle(point.getX(), point.getY(), point2.getX(), point2.getY());
                drawArrow(GeometryUtil.getGeometryCenter(polyline), angle, color, true);
            }

            List<Component> upStream1 = downStream.getDownStream();
            if (!ListUtil.isEmpty(upStream1)) {
                drawDownStream(downStream, color);
            }
        }
    }


    /**
     * ????????????????????????????????????????????????{@link #initGLayer()}??????
     *
     * @param geometryCenter   ?????????????????????
     * @param ifDrawRightArrow ??????????????????????????????true??????????????????false???????????????
     */
    private void drawArrow(Point geometryCenter, double angle, int color, boolean ifDrawRightArrow) {

        PictureMarkerSymbol symbol = null;
        if (ifDrawRightArrow) {
            Drawable tintDrawable = DrawableUtil.tintDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_forward_red_24dp, null), ColorStateList.valueOf(color));
            symbol = new PictureMarkerSymbol(mContext, tintDrawable);
        } else {
            Drawable tintDrawable = DrawableUtil.tintDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_red_24dp, null), ColorStateList.valueOf(color));
            symbol = new PictureMarkerSymbol(mContext, tintDrawable);
        }
        symbol.setAngle((float) angle);
        Graphic graphic = new Graphic(geometryCenter, symbol);
        mGLayer.addGraphic(graphic);
    }


    /**
     * ????????????????????????????????????BottomSheet???
     */
    private void showBottomSheet(final Component component) {

        // map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.GONE);
        // pb_load_check_hint.setVisibility(View.VISIBLE);
        tv_check_person.setText("");
        ll_check_hint.setVisibility(View.GONE);
        //??????????????????????????????????????????(?????????)
        checkIfHasBeenUploadBefore(component);

        final Point point = (Point) component.getGraphic().getGeometry();
        componentDetailAddress = null;
        requestLocation2(point, mMapView.getSpatialReference(), new Callback1<DetailAddress>() {
            @Override
            public void onCallback(DetailAddress detailAddress) {
                detailAddress.setX(point.getX());
                detailAddress.setY(point.getY());
                componentDetailAddress = detailAddress;
            }
        });

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

        final String type = LayerUrlConstant.getLayerNameByUnknownLayerUrl(component.getLayerUrl());


        String subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE));
        String usid = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.USID));

        String title = StringUtil.getNotNullString(type, "") + "(" + usid + ")";
        titleTv.setText(title);

        String date = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.UPDATEDATE));
        String formatDate = "";
        try {
            formatDate = TimeUtil.getStringTimeYMD(new Date(Long.valueOf(date)));
        } catch (Exception e) {

        }
        dateTv.setText(StringUtil.getNotNullString(formatDate, ""));

        String sort = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SORT));

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
            String feature = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.FEATURE));
            sortTv.setText(StringUtil.getNotNullString(feature, ""));
        }
        if ("?????????".equals(type)) {
            String style = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.STYLE));
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
            field3 = "????????????: " + String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.MATERIAL));
        } else if ("?????????".equals(component.getLayerName())) {
            field3 = "????????????: " + String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.RIVER));
        }
        field3Tv.setText(field3);
        field3Tv.setVisibility(View.GONE);
        tv_parent_org_name.setVisibility(View.GONE);

        final String address = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ADDR));
        addrTv.setText("????????????" + "???" + StringUtil.getNotNullString(address, ""));

        //???????????????
        String codeValue = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.CODE));
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

        String parentOrg = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.OWNERDEPT));
        tv_parent_org_name.setText("???????????????" + StringUtil.getNotNullString(parentOrg, ""));

        /**
         * ????????????
         */
        map_bottom_sheet.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CorrectOrConfirimFacilityActivity.class);
                intent.putExtra("component", component);
                intent.putExtra("detailAddress", componentDetailAddress);
                startActivityForResult(intent, 123);
            }
        });


        /**
         * ????????????
         */
        map_bottom_sheet.findViewById(R.id.tv_error_correct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // initGLayer();
                //  hideBottomSheet();
                if (tv_check_person != null && !TextUtils.isEmpty(tv_check_person.getText().toString())) {
                    //???????????????????????????????????????
                    DialogUtil.MessageBox(mContext, "??????", "        " + tv_check_person.getText().toString().replace("(", "") + "??????????????????????????????????????????????????????????????????",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(mContext, CorrectOrConfirimFacilityActivity.class);
                                    if(StringUtil.isEmpty(component.getLayerName())){
                                        if(!StringUtil.isEmpty(type)){
                                            component.setLayerName(type);
                                        }
                                    }
                                    intent.putExtra("component", component);
                                    startActivityForResult(intent, 123);
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                } else {
                    Intent intent = new Intent(mContext, CorrectOrConfirimFacilityActivity.class);
                    if(StringUtil.isEmpty(component.getLayerName())){
                        if(!StringUtil.isEmpty(type)){
                            component.setLayerName(type);
                        }
                    }
                    intent.putExtra("component", component);
                    startActivityForResult(intent, 123);
                }

            }
        });

        ////// showBottomSheetContent(component_intro.getId());
        map_bottom_sheet.setVisibility(View.VISIBLE);
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    /**
     * ??????????????????????????????????????????(?????????)
     *
     * @param component
     */
    private void checkIfHasBeenUploadBefore(Component component) {
        if (component.getGraphic() != null && component.getGraphic().getAttributes() != null
                && component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.USID) != null) {
            //??????usid????????????????????????????????????????????????
            String usid = component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.USID).toString();
            CorrectFacilityService correctFacilityService = new CorrectFacilityService(mContext.getApplicationContext());
            correctFacilityService.checkIfTheFacilityHasUploadedBefore(usid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Result2<CheckUploadRecordResult>>() {
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
                        public void onNext(Result2<CheckUploadRecordResult> stringResult2) {
                            final CheckUploadRecordResult checkPerson = stringResult2.getData();
                            if (checkPerson == null) {
                                //???????????????????????????????????????????????????
                                //  map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.VISIBLE);
                                ll_check_hint.setVisibility(View.GONE);
                            } else {
                                String checkPersonName = checkPerson.getUserName();
                                if (BaseInfoManager.getLoginName(mContext).equals(checkPerson.getPhone())) {
                                    checkPersonName = "???";
                                }
                                ll_check_hint.setVisibility(View.VISIBLE);
                                if (checkPersonName.equals("???") || TextUtils.isEmpty(checkPerson.getPhone())) {
                                    tv_check_person.setText(checkPersonName);
                                    tv_check_phone.setVisibility(View.GONE);
                                    map_bottom_sheet.findViewById(R.id.iv_phone).setVisibility(View.GONE);
                                } else {
                                    map_bottom_sheet.findViewById(R.id.iv_phone).setVisibility(View.VISIBLE);
                                    tv_check_person.setText(checkPersonName + "(");
                                    tv_check_phone.setText(checkPerson.getPhone() + ")");
                                    tv_check_person.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + checkPerson.getPhone()));
                                            startActivity(intent);
                                        }
                                    });
                                    tv_check_phone.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + checkPerson.getPhone()));
                                            startActivity(intent);
                                        }
                                    });
                                }
                                tv_check_hint.setText("????????????????????????????????????????????????" + checkPerson.getTotal() + "???");
                            }
                        }
                    });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectComponent(SelectComponentEvent selectComponentEvent) {
        Component component = selectComponentEvent.getComponent();
        currComponentUrl = component.getLayerUrl();
        layerAdapter.notifyDataSetChanged(LayerUrlConstant.getIndexByUnknowsLayerUrl(currComponentUrl));
        showBottomSheet(component);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (tableViewManager != null) {
            tableViewManager.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == 123
                && resultCode == 123) {
            initGLayer();
            hideBottomSheet();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.recycle();
        if (layerPresenter != null) {
            layerPresenter.destroy();
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

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        LayersService.clearInstance();
    }


    private void setAddNewFacilityListener() {
        if (addModeSelectLocationTouchListener == null) {
            addModeSelectLocationTouchListener = new SelectLocationTouchListener(mContext,
                    mMapView, locationMarker, null) {
                @Override
                public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
                    if (locationButton != null && locationButton.ifLocating()) {
                        //5
                        locationButton.setStateNormal();
                    }
                    return super.onDragPointerMove(from, to);
                }
            };
        }

        if (addModeCalloutSureButtonClickListener == null) {
            addModeCalloutSureButtonClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //??????callout
                    if (mMapView.getCallout().isShowing()) {
                        mMapView.getCallout().animatedHide();
                    }

                    //??????????????????
                    mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                    //??????marker
                    locationMarker.setVisibility(View.GONE);

                    LocationInfo locationInfo = addModeSelectLocationTouchListener.getLoationInfo();
                    if (locationInfo.getDetailAddress() == null || locationInfo.getPoint() == null) {
                        ToastUtil.shortToast(mContext, "?????????????????????");
                        return;
                    }

                    btn_add.setVisibility(View.VISIBLE);
                    btn_cancel.setVisibility(View.GONE);
                    mMapView.setOnTouchListener(getDefaultMapOnTouchListener());

                    Intent intent = new Intent(mContext, ReparationNewFacilityActivity.class);
                    intent.putExtra("detailAddress", locationInfo.getDetailAddress());
                    intent.putExtra("x", locationInfo.getPoint().getX());
                    intent.putExtra("y", locationInfo.getPoint().getY());
                    startActivity(intent);
                }
            };
        }

        /**
         * ???????????????????????????????????????
         */
        addModeSelectLocationTouchListener.setCalloutSureButtonClickListener(addModeCalloutSureButtonClickListener);
        mMapView.setOnTouchListener(addModeSelectLocationTouchListener);
    }


    private void setSearchFacilityListener() {
        if (editModeSelectLocationTouchListener == null) {
            editModeSelectLocationTouchListener = new SelectLocationTouchListener(mContext,
                    mMapView, locationMarker, false, null) {
                @Override
                public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
                    if (locationButton != null && locationButton.ifLocating()) {
                        //4---1
                        locationButton.setStateNormal();
                    }
//                if(map_bottom_sheet.getVisibility() != View.VISIBLE){
//                    if (!mMapView.getCallout().isShowing()) {
//                        mMapView.getCallout().show();
//                    }
//                    if(locationMarker.getVisibility() != View.VISIBLE){
//                        locationMarker.setVisibility(View.VISIBLE);
//                    }
//                }

                    return super.onDragPointerMove(from, to);
                }

                @Override
                public boolean onSingleTap(MotionEvent point) {
//                if (!mMapView.getCallout().isShowing()) {
//                    mMapView.getCallout().show();
//                }
                    locationMarker.setVisibility(View.VISIBLE);
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
                    double scale = mMapView.getScale();
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
        mMapView.setOnTouchListener(editModeSelectLocationTouchListener);
    }

    @Subscribe
    public void onRefreshMyUploadListEvent(RefreshMyUploadList refreshMyUploadList) {
        if (btn_edit_cancel.getVisibility() == View.VISIBLE) {
            btn_edit_cancel.performClick();
        }
        if (btn_cancel.getVisibility() == View.VISIBLE) {
            btn_cancel.performClick();
        }
        Layer[] layers = mMapView.getLayers();
        for (Layer layer : layers) {
            if (layer instanceof ArcGISDynamicMapServiceLayer) {
                ((ArcGISDynamicMapServiceLayer) layer).refresh();
            }
        }
    }

    @Subscribe
    public void onReceivedUploadFacilityEvent(UploadFacilitySuccessEvent uploadFacilitySuccessEvent) {
        initGLayer();
        hideBottomSheet();
        if (btn_edit_cancel.getVisibility() == View.VISIBLE) {
            btn_edit_cancel.performClick();
        }
        if (btn_cancel.getVisibility() == View.VISIBLE) {
            btn_cancel.performClick();
        }
        Layer[] layers = mMapView.getLayers();
        for (Layer layer : layers) {
            if (layer instanceof ArcGISDynamicMapServiceLayer) {
                ((ArcGISDynamicMapServiceLayer) layer).refresh();
            }
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

}
