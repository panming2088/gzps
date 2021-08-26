package com.augurit.agmobile.gzps.doorno.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.addcomponent.LayerAdapter;
import com.augurit.agmobile.gzps.addcomponent.model.ComponentMap;
import com.augurit.agmobile.gzps.common.OnInitLayerUrlFinishEvent;
import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.util.MapIconUtil;
import com.augurit.agmobile.gzps.componentmaintenance.ComponetListAdapter;
import com.augurit.agmobile.gzps.componentmaintenance.service.ComponentService;

import com.augurit.agmobile.gzps.doorno.model.DoorLineBean;
import com.augurit.agmobile.gzps.doorno.model.DoorNOBean;
import com.augurit.agmobile.gzps.doorno.model.MyApplication;
import com.augurit.agmobile.gzps.doorno.model.PSHAffairDetail;
import com.augurit.agmobile.gzps.doorno.model.RefreshDataListEvent;
import com.augurit.agmobile.gzps.doorno.model.SelectComponentFinishEvent;
import com.augurit.agmobile.gzps.doorno.model.SewerageInfoBean;
import com.augurit.agmobile.gzps.doorno.model.SewerageInvestBean;
import com.augurit.agmobile.gzps.doorno.model.SewerageItemBean;
import com.augurit.agmobile.gzps.doorno.service.SewerageLayerService;
import com.augurit.agmobile.gzps.doorno.service.SewerageWellService;
import com.augurit.agmobile.gzps.uploadfacility.model.BuildInfoBySGuid;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.common.widget.scaleview.MapScaleView;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.LayersService;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.mapengine.legend.service.OnlineLegendService;
import com.augurit.agmobile.mapengine.legend.view.ILegendView;
import com.augurit.agmobile.patrolcore.common.SelectLocationTouchListener;
import com.augurit.agmobile.patrolcore.common.legend.ImageLegendView;
import com.augurit.agmobile.patrolcore.common.legend.LegendPresenter;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.widget.LocationButton;
import com.augurit.agmobile.patrolcore.editmap.widget.LocationMarker;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView2;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.fw.utils.DensityUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.augurit.am.cmpt.widget.bottomsheet.IBottomSheetBehavior.STATE_COLLAPSED;

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

public class SewerageDoorMapFragment extends Fragment implements View.OnClickListener, SewerageItemPresenter.IGetRoomInfo, ISewerageItemView {
    private List<DoorNOBean> mDoorNOBeans = new ArrayList<>();
    private static final String KEY_MAP_STATE = "com.esri.MapState";
//    ViewGroup dis_map_bottom_sheet;
    private LocationMarker locationMarker;
    private SewerageLayerService mSewerageLayerService;
    private ViewGroup ll_next_and_prev_container;
    private DoorNOBean mCurrentDoorNOBean;
    AnchorSheetBehavior mDisBehavior;
    private ProgressLinearLayout pb_loading;
    private View item1;
    private View item2;
    private double x,y;
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


    private TextView show_all_layer;
    private GridView gridView;
    private LayerAdapter layerAdapter;
    private String currComponentUrl = LayerUrlConstant.newComponentUrls[0];

    ProgressDialog pd;
    ViewGroup map_bottom_sheet;
//    AnchorSheetBehavior mBehavior;
    private ComponetListAdapter componetListAdapter;
//    private View component_intro;
    private View component_detail_ll;
    private ViewGroup component_detail_container;
    private View btn_upload;
    private ArrayList<TableItem> tableItems = null;
    private ArrayList<Photo> photoList = new ArrayList<>();
    private String projectId;
    private TextView tv_address;
//    private TableViewManager tableViewManager;
    private List<Component> mOrgComponentQueryResult = new ArrayList<>();
    private List<Component> mComponentQueryResult = new ArrayList<>();
    private int currIndex = 0;
    private View btn_dis_prev;
    private View btn_dis_next;
    private DetailAddress mCurrentAddress;
    //原位置图层
    private GraphicsLayer mOriginGraphicLayer;
    private GraphicsLayer mDoorGraphicLayer;
    private GraphicsLayer mDoorGraphicLineLayer;

    /**
     * 地图默认的事件处理
     */
    private DefaultTouchListener defaultMapOnTouchListener;
    private ProgressDialog progressDialog;
    private SewerageInfoBean.WellBeen mfromBean;
    private View ll_reset;
    private View ll_reset_door;
    private Geometry orgGeometry;
    private Geometry orgMenPaiGeometry;
    private DoorNOBean doorNOBean;
    private Point doorPoint;
    private Geometry geometry;
    private boolean isReset = false;
    private PSHAffairDetail pshAffairDetail;
    private SewerageWellService sewerageWellService;
    private View ll_add_well, btn_edit, btn_edit_cancel;
    private boolean ifFirstAdd = true;
    /**
     * 是否处于编辑模式
     */
    private boolean ifInEditMode = false;
    //排水户上报选择门牌界面进来
    private boolean EDIT_MODE = false;
    private View.OnClickListener addModeCalloutSureButtonClickListener;
    private SelectLocationTouchListener addModeSelectLocationTouchListener;
    private List<SewerageInfoBean.WellBeen> wellBeen;
    private MapScaleView scaleView;
    boolean isIndustry;
    private SewerageItemPresenter sewerageItemPresenter = new SewerageItemPresenter(this);
    public static SewerageDoorMapFragment getInstance(Bundle data) {
        SewerageDoorMapFragment addComponentFragment2 = new SewerageDoorMapFragment();
        addComponentFragment2.setArguments(data);
        return addComponentFragment2;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(mContext, R.layout.fragment_psh_door_map, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        mView = view;

        pb_loading = (ProgressLinearLayout) mView.findViewById(R.id.pb_loading);
        item1 = mView.findViewById(R.id.ll_item1);
        item2 =  mView.findViewById(R.id.tv_floor);
        sewerageItemPresenter.setIGetRoomInfoListener(this);
        // Find MapView and add feature layers
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.setMapBackground(Color.WHITE, Color.TRANSPARENT, 0f, 0f);//设置地图背景色、去掉网格线
        /**
         * 比例尺
         */
        scaleView = (MapScaleView) view.findViewById(R.id.scale_view);
        scaleView.setMapView(mMapView);
        // Set listeners on MapView
        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onStatusChanged(final Object source, final STATUS status) {
                if (STATUS.INITIALIZED == status) {
                    if (source instanceof MapView) {
                        /**
                         * 判断是否有位置传递过来
                         */
                        if (getArguments() != null && getArguments().getSerializable("pshAffair") != null) {
                            pshAffairDetail = (PSHAffairDetail) getArguments().getSerializable("pshAffair");
                            isIndustry = getArguments().getBoolean("isIndustry");
                            EDIT_MODE =  getArguments().getBoolean("EDIT_MODE");
                            wellBeen = pshAffairDetail.getData()== null ? null:pshAffairDetail.getData().getWellBeen();
                            if (pshAffairDetail.getData() != null && !TextUtils.isEmpty(pshAffairDetail.getData().getDoorplateAddressCode())) {
                                queryMenPaiForId(pshAffairDetail.getData().getDoorplateAddressCode());
                            }
                        } else {
                            //默认定位到水务局附近
//                            Point point = new Point(PatrolLayerPresenter.longitude, PatrolLayerPresenter.latitude);
//                            mMapView.centerAt(point, true);
                            if (locationButton != null && doorPoint == null) {
                                locationButton.followLocation();
                            }
                            // startLocate();
                        }

                        locationMarker.setVisibility(View.GONE);
                        mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
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
        /**
         * 候选列表容器
         */
        ll_component_list = (ViewGroup) view.findViewById(R.id.ll_component_list);

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
        mDisBehavior = AnchorSheetBehavior.from(map_bottom_sheet);
        mDisBehavior.setState(AnchorSheetBehavior.STATE_EXPANDED);
        initBottomSheetView();

        btn_dis_prev = view.findViewById(R.id.dis_prev);
        btn_dis_next = view.findViewById(R.id.dis_next);
        component_detail_container = (ViewGroup) view.findViewById(R.id.detail_container);
        btn_dis_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currIndex--;
                if (mDoorNOBeans.size() > 1) {
                    btn_dis_next.setVisibility(View.VISIBLE);
                }

                if (currIndex < 0) {
                    btn_dis_prev.setVisibility(View.GONE);
                    return;
                }
                mCurrentDoorNOBean = mDoorNOBeans.get(currIndex);
                sewerageItemPresenter.getBuildInfBySGuid(mDoorNOBeans.get(currIndex).getS_guid());
                if (currIndex == 0) {
                    btn_dis_prev.setVisibility(View.GONE);
                }
            }
        });
        btn_dis_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currIndex++;
                if(currIndex<0) currIndex = 0;
                if (currIndex > 0) {
                    btn_dis_prev.setVisibility(View.VISIBLE);
                }

                if (currIndex > mDoorNOBeans.size()) {
                    btn_dis_next.setVisibility(View.GONE);
                    return;
                }
                mCurrentDoorNOBean = mDoorNOBeans.get(currIndex);
                sewerageItemPresenter.getBuildInfBySGuid(mDoorNOBeans.get(currIndex).getS_guid());
                if (currIndex == (mDoorNOBeans.size() - 1)) {
                    btn_dis_next.setVisibility(View.GONE);
                }

            }
        });

        //修改标题
        ((TextView) view.findViewById(R.id.tv_title)).setText("门牌位置查看");
        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

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
        mSewerageLayerService = new SewerageLayerService(getContext());

        ll_reset = view.findViewById(R.id.ll_reset);
        ll_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isReset = true;
                locationMarker.setVisibility(View.GONE);
                if (mMapView.getCallout().isShowing()) {
                    mMapView.getCallout().hide();
                }
                if (geometry != null) {
                    mMapView.centerAt((Point) geometry, true);
                } else if (mfromBean != null) {
                    btn_dis_next.setVisibility(View.GONE);
                    btn_dis_prev.setVisibility(View.GONE);
                    currIndex = 0;
                    component_detail_container.removeAllViews();
                    if (mOrgComponentQueryResult != null && orgGeometry != null) {
                        mMapView.centerAt((Point) orgGeometry, true);
//                        showComponentsOnBottomSheet(mOrgComponentQueryResult);
                    } else if (mfromBean != null) {
//                        queryForId(mfromBean.getWellId());
                    }
                }
            }
        });


        ll_reset_door = view.findViewById(R.id.ll_reset_door);
        ll_reset_door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationMarker.setVisibility(View.GONE);
                if (mMapView.getCallout().isShowing()) {
                    mMapView.getCallout().hide();
                }
                hideBottomSheet();
                if (orgMenPaiGeometry != null) {
                    mMapView.centerAt((Point) orgMenPaiGeometry, true);
                } else if (pshAffairDetail != null && pshAffairDetail.getData() != null) {
                    queryMenPaiForId(pshAffairDetail.getData().getDoorplateAddressCode());
                }
            }
        });

//        view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clearData();
//            }
//        });

    }

    private void clearData() {
        if(isIndustry){
            if (isIndustry) {
                MyApplication.refreshListType = 0;
                MyApplication.buildId = null;
                MyApplication.doorBean = null;
                MyApplication.X = 0;
                MyApplication.Y = 0;
                MyApplication.GUID = null;
                MyApplication.DZDM = null;
            }
        }
    }

    private void initBottomSheetView() {
        ll_next_and_prev_container = (ViewGroup) map_bottom_sheet.findViewById(R.id.ll_next_and_prev_container);
        tv_address = (TextView) map_bottom_sheet.findViewById(R.id.tv_address);
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
        layerPresenter = new PSHWelllayerPresenter(layerView);
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
    private void highLightFacilityOriginLocation(List<DoorLineBean> mDestinationOrLastTimeSelectLocation) {
        if (mOriginGraphicLayer == null) {
            mOriginGraphicLayer = new GraphicsLayer();
            mMapView.addLayer(mOriginGraphicLayer);
        }
        mOriginGraphicLayer.removeAll();
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(getContext(),
                getContext().getResources().getDrawable(R.mipmap.ic_select_yinjing));
        pictureMarkerSymbol.setOffsetY(16);
        for(DoorLineBean geometry:mDestinationOrLastTimeSelectLocation) {
            Graphic graphic = new Graphic(geometry.getGeometry(), pictureMarkerSymbol);
            mOriginGraphicLayer.addGraphic(graphic);
            mOriginGraphicLayer.setSelectedGraphics(new int[]{graphic.getUid()}, true);
        }
//        mOriginGraphicLayer.setSelectedGraphics(new int[]{graphic.getUid()}, true);
    }


    /**
     * 高亮设施原来的位置
     *
     * @param mDestinationOrLastTimeSelectLocation
     */
    private void highLightDoorOriginLocation(Geometry mDestinationOrLastTimeSelectLocation) {
        if (mDoorGraphicLayer == null) {
            mDoorGraphicLayer = new GraphicsLayer();
            mMapView.addLayer(mDoorGraphicLayer);
        }
        mDoorGraphicLayer.removeAll();
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(getContext(),
                getContext().getResources().getDrawable(R.mipmap.ic_add_facility_door));
        pictureMarkerSymbol.setOffsetY(16);
        Graphic graphic = new Graphic(mDestinationOrLastTimeSelectLocation, pictureMarkerSymbol);
        mDoorGraphicLayer.addGraphic(graphic);
        mDoorGraphicLayer.setSelectedGraphics(new int[]{graphic.getUid()}, true);
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

                x = point.getX();
                y = point.getY();
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
        if (mDisBehavior != null
                && map_bottom_sheet != null) {
            if (mDisBehavior.getState() == AnchorSheetBehavior.STATE_EXPANDED) {
                mDisBehavior.setState(STATE_COLLAPSED);
            } else if (mDisBehavior.getState() == STATE_COLLAPSED) {
                initGLayer();
                map_bottom_sheet.setVisibility(View.GONE);
//                tableViewManager = null;
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
        mContext = context;
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


    private Point getMapCenterPoint() {
        //获取当前的位置
        return mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2, locationMarker.getY() + locationMarker.getHeight() / 2);
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
             * 注册位置发生改变时的回调
             */
            defaultMapOnTouchListener.registerLocationChangedListener(new SelectLocationTouchListener.OnSelectedLocationChangedListener() {
                @Override
                public void onLocationChanged(Point newLocation) {
                }

                @Override
                public void onAddressChanged(DetailAddress detailAddress) {

                }
            });
        }
        return defaultMapOnTouchListener;
    }


    private Observable<DoorLineBean> getObservable(final String id, final String name) {
        return Observable.create(new Observable.OnSubscribe<DoorLineBean>() {
            @Override
            public void call(final Subscriber<? super DoorLineBean> subscriber) {
                SewerageWellService componentMaintenanceService = new SewerageWellService(mContext.getApplicationContext());
                componentMaintenanceService.queryWellComponent(id,name, new Callback2<List<Component>>() {
                        @Override
                        public void onSuccess(List<Component> components) {
                            if (ListUtil.isEmpty(components)) {
                                return;
                            }
                            mOrgComponentQueryResult = new ArrayList<Component>();
                            mOrgComponentQueryResult.addAll(components);
                            if (mOrgComponentQueryResult.get(0).getGraphic() != null) {
                                orgGeometry = mOrgComponentQueryResult.get(0).getGraphic().getGeometry();
                                DoorLineBean doorLineBean = new DoorLineBean();
                                doorLineBean.setGeometry(orgGeometry);
                                doorLineBean.setName(name);
                                subscriber.onNext(doorLineBean);
                                subscriber.onCompleted();
                            }else{
                                subscriber.onError(null);
                            }
                        }

                        @Override
                        public void onFail(Exception error) {
                            subscriber.onError(null);
                        }
                    });
                }
        }).onErrorReturn(new Func1<Throwable, DoorLineBean>() {
            @Override
            public DoorLineBean call(Throwable throwable) {
                return null;
            }
        })
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<DoorLineBean>> queryAllComponents() {
        List<Observable<DoorLineBean>> observableList = new ArrayList<>();
        for (SewerageInfoBean.WellBeen wellBean : wellBeen) {
            Observable<DoorLineBean> observable = getObservable(wellBean.getWellId(),wellBean.getPipeType());
            observableList.add(observable);
        }
        return Observable.zip(observableList, new FuncN<List<DoorLineBean>>() {
            @Override
            public List<DoorLineBean> call(Object... objects) {
                if (objects.length == 0) {
                    return null;
                }
                List<DoorLineBean> queryFeatureSetList = new ArrayList<DoorLineBean>();
                for (Object object : objects) {
                    if (object == null) {
                        continue;
                    }
                    DoorLineBean queryFeatureSet = (DoorLineBean) object;
                    queryFeatureSetList.add(queryFeatureSet);
                }
                return queryFeatureSetList;
            }
        }).subscribeOn(Schedulers.io());
    }

    public void getWellData(boolean ifShowPb) {
        final ComponentService componentMaintenanceService = new ComponentService(getActivity().getApplicationContext());
        queryAllComponents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<DoorLineBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
//                        showLoadedError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<DoorLineBean> components) {
                        if (ListUtil.isEmpty(components)){
                            return;
                        }
                        highLightFacilityOriginLocation(components);
//                        highLightDoor(components);

                    }
                });
    }

    private void queryMenPaiForId(String id) {
        mOrgComponentQueryResult.clear();
        btn_dis_next.setVisibility(View.GONE);
        btn_dis_prev.setVisibility(View.GONE);
        currIndex = 0;
        final List<LayerInfo> layerInfoList = new ArrayList<>();
        for (String url : LayerUrlConstant.newComponentUrls) {
            LayerInfo layerInfo = new LayerInfo();
            layerInfo.setUrl(url);
            layerInfoList.add(layerInfo);
        }
        SewerageWellService componentMaintenanceService = new SewerageWellService(mContext.getApplicationContext());
        if (true) {
            componentMaintenanceService.queryMenPaiComponents(id, new Callback2<List<Component>>() {
                @Override
                public void onSuccess(List<Component> components) {
                    if (ListUtil.isEmpty(components)) {
                        return;
                    }
                    if (components.get(0).getGraphic() != null) {
                        orgMenPaiGeometry = components.get(0).getGraphic().getGeometry();
                        highLightDoorOriginLocation(orgMenPaiGeometry);
                        mMapView.centerAt((Point) orgMenPaiGeometry,true);
                        mMapView.setScale(mMapView.getMaxScale());
                        scaleView.setScale(mMapView.getMaxScale());
                        if(!ListUtil.isEmpty(wellBeen)){
                            getWellData(true);
                        }
                    }
                }

                @Override
                public void onFail(Exception error) {

                }
            });
        }
    }



    @Override
    public void onInvestCode(SewerageInvestBean bean) {

    }

    @Override
    public void onLoadStart() {

    }

    @Override
    public void onCompelete() {

    }

    /**
     * The MapView's touch listener.
     */
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

        private void handleTap(final MotionEvent e) {
            if (locationMarker != null) {
                locationMarker.setVisibility(View.GONE);
            }
            if (mMapView.getCallout().isShowing()) {
                mMapView.getCallout().hide();
            }
            initGLayer();
            hideBottomSheet();
            double scale = mMapView.getScale();
            if (scale < LayerUrlConstant.MIN_QUERY_SCALE) {
                final Point point = mMapView.toMapPoint(e.getX(), e.getY());
//                if(ifMyUploadLayerVisible){
                if (mMapView.getCallout().isShowing()) {
                    mMapView.getCallout().animatedHide();
                }
                //隐藏marker
                if (locationMarker != null) {
                    locationMarker.setVisibility(View.GONE);
                }
                queryDistribute(point.getX(), point.getY());
            }
        }
    }

    /**
     * 点击地图后查询设施
     * @param x
     * @param y
     */
    private void queryDistribute(final double x, final double y) {

        if (pd == null) {
            pd = new ProgressDialog(getContext());
        }
        pd.setMessage("正在查询门牌...");
        if (!pd.isShowing()) {
            pd.show();
        }

        ll_next_and_prev_container.setVisibility(View.INVISIBLE);
        currIndex = 0;
        final Point point = new Point(x, y);
        mCurrentDoorNOBean = null;
        mSewerageLayerService.setQueryByWhere("1=1");
        mSewerageLayerService.queryDoorDataInfo(point, mMapView.getSpatialReference(), mMapView.getResolution(), new Callback2<List<DoorNOBean>>() {
            @Override
            public void onSuccess(List<DoorNOBean> doorNOBeans) {
                pd.dismiss();
                if (ListUtil.isEmpty(doorNOBeans) || (doorNOBeans.size() == 1 && doorNOBeans.get(0).getDzdm() == null && doorNOBeans.get(0).getAddress() == null)) {
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.iconLongToast(getActivity(), R.mipmap.ic_alert_yellow, "请选择门牌！");
                        }
                    });
                    return;
                }
                mDoorNOBeans.clear();
                mDoorNOBeans.addAll(doorNOBeans);
                sewerageItemPresenter.getBuildInfBySGuid(mDoorNOBeans.get(0).getS_guid());
            }

            @Override
            public void onFail(Exception error) {
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.iconLongToast(getContext(), R.mipmap.ic_alert_yellow, "请选择门牌！");
                    }
                });
                pd.dismiss();
            }
        });
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
        switch (geometry.getType()) {
            case LINE:
            case POLYLINE:
                symbol = new SimpleLineSymbol(Color.RED, 7);
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

        if (ifCenter) {
            mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        }
    }


    @NonNull
    private Symbol getPointSymbol() {
        Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), com.augurit.agmobile.patrolcore.R.mipmap.ic_select_location, null);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mContext, drawable);
        pictureMarkerSymbol.setOffsetY(16);
        return pictureMarkerSymbol;    // xjx 改为兼容api19的方式获取drawable
    }

    private void hideBottomSheet() {
        map_bottom_sheet.setVisibility(View.GONE);
        if (mGLayer != null) {
            mGLayer.removeAll();
        }
    }

   /* private void showComponentsOnBottomSheet(List<Component> componentQueryResult) {
        if (componentQueryResult.size() > 1) {
            btn_dis_next.setVisibility(View.VISIBLE);
        }
        showBottomSheet(componentQueryResult.get(0));

    }*/

    private void showCallout(final Component component) {
//        //显示callout
        String usid = String.valueOf(component.getGraphic().getAttributes().get("OBJECTID"));
//        String type = LayerUrlConstant.getLayerNameByUnknownLayerUrl(component.getLayerUrl());
        String type = String.valueOf(component.getGraphic().getAttributes().get("LAYER_NAME"));
        String title = StringUtil.getNotNullString(type, "") + "(" + StringUtil.getNotNullString(usid, "") + ")";
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
                EventBus.getDefault().post(new SelectComponentFinishEvent(component, mCurrentAddress, mMapView.getScale()));
                getActivity().finish();
            }
        });
    }

    /*private void showBottomSheet(final Component component) {
        if (!isReset) {
            map_bottom_sheet.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showCallout(component);
                }
            }, 300);
        }

        String errorInfo = null;
        Object oErrorInfo = component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ERROR_INFO);

        if (oErrorInfo != null) {
            errorInfo = oErrorInfo.toString();
        }

        TextView titleTv = (TextView) map_bottom_sheet.findViewById(R.id.title);
        TextView dateTv = (TextView) map_bottom_sheet.findViewById(R.id.date);
        TextView sortTv = (TextView) map_bottom_sheet.findViewById(sort);
        TextView subtypeTv = (TextView) map_bottom_sheet.findViewById(R.id.subtype);
        TextView field1Tv = (TextView) map_bottom_sheet.findViewById(R.id.field1);
        TextView field2Tv = (TextView) map_bottom_sheet.findViewById(R.id.field2);
        TextView field3Tv = (TextView) map_bottom_sheet.findViewById(R.id.field3);
        TextView addrTv = (TextView) map_bottom_sheet.findViewById(R.id.addr);
        TextView code = (TextView) map_bottom_sheet.findViewById(R.id.code);
//        addrTv.setVisibility(View.GONE);
        TextView tv_errorinfo = (TextView) map_bottom_sheet.findViewById(R.id.tv_errorinfo);
        TextView tv_parent_org_name = (TextView) map_bottom_sheet.findViewById(R.id.tv_parent_org_name);
        LinearLayout ll_photo = (LinearLayout) map_bottom_sheet.findViewById(R.id.ll_photo);
        TakePhotoTableItem3 view_photos = (TakePhotoTableItem3) map_bottom_sheet.findViewById(R.id.view_photos);
        view_photos.setReadOnly();
        view_photos.setEnabled(false);
        view_photos.setImageIsShow(false);
        if (pshAffairDetail != null) {
            ll_photo.setVisibility(View.VISIBLE);
            observeWellPhotos(component.getObjectId(), view_photos, ll_photo);
            ViewGroup.LayoutParams layoutParams = map_bottom_sheet.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.height = Util.dpToPx(300);
                map_bottom_sheet.setLayoutParams(layoutParams);
            }
        }


        String name = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.NAME));

        String type = String.valueOf(component.getGraphic().getAttributes().get("LAYER_NAME"));


        String subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE));
        String usid = String.valueOf(component.getGraphic().getAttributes().get("OBJECTID"));

        String title = StringUtil.getNotNullString(type, "") + "(" + usid + ")";
        titleTv.setText(title);

        String date = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.UPDATEDATE));
        String formatDate = "";
        try {
            formatDate = TimeUtil.getStringTimeYMD(new Date(Long.valueOf(date)));
        } catch (Exception e) {

        }
        dateTv.setText(StringUtil.getNotNullString(formatDate, ""));

        String sort = String.valueOf(component.getGraphic().getAttributes().get("ORIGIN_ATTR_TWO"));

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
        String layerName = String.valueOf(component.getGraphic().getAttributes().get("LAYER_NAME"));
        *//**
         * 如果是雨水口，显示特性：方形
         *//*
        if (layerName.equals("雨水口")) {
            String feature = String.valueOf(component.getGraphic().getAttributes().get("ATTR_ONE"));
            sortTv.setText(StringUtil.getNotNullString(feature, ""));
        }
        if ("雨水口".equals(type)) {
            String style = String.valueOf(component.getGraphic().getAttributes().get("ATTR_ONE"));
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
        codeValue = codeValue.trim();
        if (layerName.equals("窨井")) {
            code.setVisibility(View.VISIBLE);
            if (!codeValue.isEmpty()) {
                code.setText("已挂牌编码" + ":" + StringUtil.getNotNullString(codeValue, ""));
            } else {
                code.setText("已挂牌编码" + ":" + "");
            }
        } else {
            code.setVisibility(View.GONE);
        }

        *//**
         * 修改属性三
         *//*
        String field3 = "";
        if (layerName.equals("窨井")) {
            field3 = "井盖材质: " + String.valueOf(component.getGraphic().getAttributes().get("ATTR_THREE"));
        } else if (layerName.equals("排放口")) {
            field3 = "排放去向: " + String.valueOf(component.getGraphic().getAttributes().get("ATTR_THREE"));
        }
        field3Tv.setText(field3);


        final String address = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ADDR));
        addrTv.setText("设施位置" + "：" + StringUtil.getNotNullString(address, ""));

        *//*
        if (errorInfo == null) {
            tv_errorinfo.setVisibility(View.GONE);
        } else {
            tv_errorinfo.setVisibility(View.VISIBLE);
            tv_errorinfo.setText(errorInfo + "?");
        }
        *//*
        tv_errorinfo.setVisibility(View.GONE);


        String parentOrg = String.valueOf(component.getGraphic().getAttributes().get("PARENT_ORG_NAME"));
        tv_parent_org_name.setText("权属单位：" + StringUtil.getNotNullString(parentOrg, ""));
        ////   showBottomSheetContent(component_intro.getId());
        map_bottom_sheet.setVisibility(View.VISIBLE);
//        mBehavior.setState(AnchorSheetBehavior.STATE_EXPANDED);
        // loadCompleteDataAsync(component);
    }*/

//    private void observeWellPhotos(Integer objectId, final TakePhotoTableItem3 view_photos, final LinearLayout ll_photo) {
//        if (sewerageWellService == null) {
//            sewerageWellService = new SewerageWellService(getActivity());
//        }
//        sewerageWellService.getWellPhotos(objectId + "").subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<WellPhoto>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(WellPhoto wellPhoto) {
//                        if (wellPhoto == null || wellPhoto.getCode() != 200) {
//                            ToastUtil.shortToast(getActivity(), "获取接驳井图片失败");
//                            return;
//                        }
//                        List<WellPhoto.RowsBean> rows = wellPhoto.getRows();
//                        if (!ListUtil.isEmpty(rows)) {
//                            List<Photo> welPhotos = new ArrayList<>();
//                            for (WellPhoto.RowsBean rowsPhoto : rows) {
//                                Photo photo = new Photo();
//                                photo.setPhotoName(rowsPhoto.getAttName());
//                                photo.setHasBeenUp(1);
//                                photo.setPhotoPath(rowsPhoto.getAttPath());
//                                photo.setThumbPath(rowsPhoto.getThumPath());
//                                photo.setField1("photo");
//                                welPhotos.add(photo);
//                            }
//                            view_photos.setSelectedPhotos(welPhotos);
//                        } else {
//                            ll_photo.setVisibility(View.GONE);
//                        }
//                    }
//                });
//
//    }

   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectComponent(SelectComponentEvent selectComponentEvent) {
        Component component = selectComponentEvent.getComponent();
        currComponentUrl = component.getLayerUrl();
        layerAdapter.notifyDataSetChanged(LayerUrlConstant.getIndexByUnknowsLayerUrl(currComponentUrl));
        showBottomSheet(component);
    }*/

    /**
     * 网络切换时，若设施URL为null，会重新初始化设施URL，然后发送OnInitLayerUrlFinishEvent事件
     * @param onInitLayerUrlFinishEvent
     */
    @Subscribe
    public void onInitLayerUrlFinished(OnInitLayerUrlFinishEvent onInitLayerUrlFinishEvent) {
//        changeLayerUrlInitFailState();
        if (!loadLayersSuccess && layerPresenter != null) {
            layerPresenter.loadLayer(new Callback2<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    loadLayersSuccess = true;
//                    changeLayerUrlInitFailState();
                }

                @Override
                public void onFail(Exception error) {
                    loadLayersSuccess = false;
//                    changeLayerUrlInitFailState();
                }
            });
        }
    }
    @Override
    public void getBuildInfBySGuid(BuildInfoBySGuid buildInfoBySGuid) {
        item1.setVisibility(View.VISIBLE);
        item2.setVisibility(View.VISIBLE);
        pb_loading.showContent();

        if (mDoorNOBeans.size() > 1) {
            ll_next_and_prev_container.setVisibility(View.VISIBLE);
            if (null != buildInfoBySGuid && buildInfoBySGuid.getCode() == 200 && buildInfoBySGuid.getData() != null) {
                showBottomSheet(mDoorNOBeans.get(currIndex), buildInfoBySGuid.getData());
            } else {
                //显示默认
                showBottomSheet(mDoorNOBeans.get(currIndex), null);
            }
        } else {
            if (buildInfoBySGuid.getCode() == 200 && buildInfoBySGuid.getData() != null) {
                showOnBottomSheet(mDoorNOBeans, buildInfoBySGuid.getData());
            } else {
                //显示默认
                showOnBottomSheet(mDoorNOBeans, null);
            }
        }
    }
    /**
     * 设施部件
     * 加载设施信息，显示中底部BottomSheet中
     */
    private void showBottomSheet(final DoorNOBean doorNOBean, BuildInfoBySGuid.DataBean buildInfoBySGuid) {
        map_bottom_sheet.setVisibility(View.VISIBLE);
        if (mDoorNOBeans.size() > 1) {
            ll_next_and_prev_container.setVisibility(View.VISIBLE);
        }
        mCurrentDoorNOBean = doorNOBean;
        initGLayer();
        Point centerPt = new Point(doorNOBean.getX(), doorNOBean.getY());
        if (centerPt != null) {
            drawGeometry(centerPt, mGLayer, true, true);
        }
        initBottomSheetText(doorNOBean, buildInfoBySGuid);
        TextView tv = (TextView) getView().findViewById(R.id.door_detail_btn);
        if(!isIndustry || !EDIT_MODE){tv.setVisibility(View.GONE);}
        tv.setText("选择门牌");
        tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (locationMarker != null) {
                            locationMarker.setVisibility(View.GONE);
                        }
                        if(!TextUtils.isEmpty(pshAffairDetail.getData().getDoorplateAddressCode()) && !TextUtils.isEmpty(mCurrentDoorNOBean.getS_guid())
                        &&pshAffairDetail.getData().getDoorplateAddressCode().equals(mCurrentDoorNOBean.getS_guid())){
                            ToastUtil.shortToast(mContext,"所选门牌与原有门牌是同一个，请重新选择");
                            return;
                        }
                        pd.setMessage("正在获取门牌信息...");
                        pd.show();
                        sewerageItemPresenter.getdata(mCurrentDoorNOBean.getS_guid(), mCurrentDoorNOBean.getDzdm(),
                                1, 10, 0, 2, "", null);

                    }
                });
        getView().findViewById(R.id.wrong_door_btn).setVisibility(View.GONE);
        mDisBehavior.setAnchorHeight(DensityUtil.dp2px(getContext(), 230));
        map_bottom_sheet.postDelayed(new Runnable() {
            @Override
            public void run() {
        mDisBehavior.setState(com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior.STATE_EXPANDED);
            }
        }, 500);
    }
    @Override
    public void setSewerageItemList(SewerageItemBean sewerageItemList) {
        pd.dismiss();
        hideCallout();
        MyApplication.refreshListType = RefreshDataListEvent.UPDATE_MIAN_LIST;
        MyApplication.SEWERAGEITEMBEAN = sewerageItemList;
        MyApplication.buildId = sewerageItemList.getBuildId();
        MyApplication.doorBean = mCurrentDoorNOBean;
        MyApplication.X = mCurrentDoorNOBean.getX();
        MyApplication.Y = mCurrentDoorNOBean.getY();

        if (mCurrentDoorNOBean.getS_guid() != null) {
            MyApplication.GUID = mCurrentDoorNOBean.getS_guid();
        }
        if (mCurrentDoorNOBean.getDzdm() != null) {
            MyApplication.DZDM = mCurrentDoorNOBean.getDzdm();
        }
        PSHAffairDetail.DataBean bean  = pshAffairDetail.getData();
        bean.setAddr(sewerageItemList.getAddress());
        bean.setTown(sewerageItemList.getJlx());
        bean.setMph(sewerageItemList.getMpdzmc());
        Intent intent = new Intent();
        intent.putExtra("doorBean", mCurrentDoorNOBean);
        intent.putExtra("isExist", mCurrentDoorNOBean.getIsExist());
        intent.putExtra("HouseIdFlag", "0");
        intent.putExtra("HouseId", MyApplication.buildId);
        intent.putExtra("UnitId", "");
        intent.putExtra("pshAffairDetail",pshAffairDetail);
        getActivity().setResult(RESULT_OK,intent);
        getActivity().finish();
    }
    private void showOnBottomSheet(List<DoorNOBean> beans, BuildInfoBySGuid.DataBean buildInfoBySGuids) {
        if (beans.size() > 1) {
            ll_next_and_prev_container.setVisibility(View.VISIBLE);
        }

        //隐藏marker
        initGLayer();
        Geometry geometry = null;
        mCurrentDoorNOBean = beans.get(0);
        if (beans.get(0) != null) {
            geometry = new Point(beans.get(0).getX(), beans.get(0).getY());
            showBottomSheet(beans.get(0), buildInfoBySGuids != null ? buildInfoBySGuids : null);
        }
        if (geometry != null) {
            drawGeometry(geometry, mGLayer, true, true);
        }
    }
    private void hideCallout() {
        Callout callout = mMapView.getCallout();
        if (null != callout && callout.isShowing()) {
            mMapView.getCallout().hide();
        }
    }
    /**
     * bottomSheetLayout的显示样式
     *
     * @param doorNOBean
     * @param buildInfoBySGuid
     */
    private void initBottomSheetText(final DoorNOBean doorNOBean, BuildInfoBySGuid.DataBean buildInfoBySGuid) {
        tv_address.setText(StringUtil.getNotNullString(doorNOBean.getStree(), ""));
        TextView tv_house_Property = (TextView) mView.findViewById(R.id.tv_house_Property);
        TextView tv_floor = (TextView) mView.findViewById(R.id.tv_floor);

        StringBuilder propertySB = new StringBuilder();
        StringBuilder floorSB = new StringBuilder();
        String houseProperty = buildInfoBySGuid.getHouseProperty();
        String structure = buildInfoBySGuid.getStructure();
        String houseType = buildInfoBySGuid.getHouseType();
        String houseUse = buildInfoBySGuid.getHouseUse();

        boolean empty1 = TextUtils.isEmpty(houseProperty) || TextUtils.isEmpty(houseProperty.trim());
        boolean empty2 = TextUtils.isEmpty(structure) || TextUtils.isEmpty(structure.trim());
        boolean empty3 = TextUtils.isEmpty(houseType) || TextUtils.isEmpty(houseType.trim());
        boolean empty4 = TextUtils.isEmpty(houseUse) || TextUtils.isEmpty(houseUse.trim());

        propertySB.append(empty1?"":houseProperty).append("|")
                .append(empty2?"":structure).append("|")
                .append(empty3?"":houseType).append("|")
                .append(empty4?"":houseUse).append("|");

        String str = propertySB.toString();
        str = str.replaceAll("\\|\\|\\|\\|", "\\|").replaceAll("\\|\\|\\|", "\\|").replaceAll("\\|\\|", "\\|");
        //去掉结尾|
        str = str.lastIndexOf("|")==str.length()-1?str.substring(0, str.length()-1):str;
        //去掉开头|
        str = str.indexOf("|")== 0?str.substring(1):str;
        tv_house_Property.setText(str.replaceAll("\\|", "\\ | "));

        String floor = buildInfoBySGuid.getFloor();
        String sets = buildInfoBySGuid.getSets();

        boolean empty5 = TextUtils.isEmpty(floor) || TextUtils.isEmpty(floor.trim());
        boolean empty6 = TextUtils.isEmpty(sets) || TextUtils.isEmpty(sets.trim());

        floorSB.append(empty5 ? "":"总层数:" +floor).append("|")
                .append(empty6 ? "":"总套数:" +sets).append("|");

        String str1 = floorSB.toString();
        str1 = str1.replaceAll("\\|\\|\\|", "\\|").replaceAll("\\|\\|", "\\|");
        str1 = str1.lastIndexOf("|")==str1.length()-1?str1.substring(0, str1.length()-1):str1;
        tv_floor.setText(str1.replaceAll("\\|", "\\ | "));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (tableViewManager != null) {
//            tableViewManager.onActivityResult(requestCode, resultCode, data);
//        }
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
}
