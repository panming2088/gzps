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

package com.augurit.agmobile.gzps.maintain.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MapHelper;
import com.augurit.agmobile.gzps.common.OnInitLayerUrlFinishEvent;
import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.search.IOnSearchClickListener;
import com.augurit.agmobile.gzps.common.search.SearchFragment;
import com.augurit.agmobile.gzps.common.selectcomponent.LimitedLayerAdapter;
import com.augurit.agmobile.gzps.componentmaintenance.ComponetListAdapter;
import com.augurit.agmobile.gzps.maintain.model.MaintainBatchInfo;
import com.augurit.agmobile.gzps.maintain.service.MaintainLayerFactory;
import com.augurit.agmobile.gzps.maintain.service.MaintainLayerService;
import com.augurit.agmobile.gzps.maintain.view.miantianlist.MaintainListActivity;
import com.augurit.agmobile.gzps.maintain.view.uploadnewmaintain.MaintainNewActivity;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.service.DeleteFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.FeedbackFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.FeedbackLayerService;
import com.augurit.agmobile.gzps.uploadfacility.util.UploadLayerFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadfacility.view.UploadFacilitySuccessEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.approvalopinion.ApprovalOpinionViewManager;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.RefreshMyModificationListEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.RefreshMyUploadList;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.common.widget.scaleview.MapScaleView;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.layermanage.service.LayersService;
import com.augurit.agmobile.mapengine.layermanage.util.ILayerFactory;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.mapengine.legend.service.OnlineLegendService;
import com.augurit.agmobile.mapengine.legend.view.ILegendView;
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
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior;
import com.augurit.am.fw.utils.DensityUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior.STATE_COLLAPSED;
import static com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior.STATE_EXPANDED;


/**
 * 养护计划地图界面
 *
 * @author 创建人 ：luobiao
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.map
 * @createTime 创建时间 ：2017-12-7
 * @modifyBy 修改人 ：luobiao,xuciluan
 * @modifyTime 修改时间 ：2017-12-7
 * @modifyMemo 修改备注：
 */
public class MaintainMapFragment extends Fragment implements View.OnClickListener {

    private static final String KEY_MAP_STATE = "com.esri.MapState";
    private final String TAG = "MaintainMapFragment";

    private LocationMarker locationMarker;

    private ModifiedFacility mCurrentModifiedFacility;
    private UploadedFacility mCurrentUploadedFacility;
    private MaintainBatchInfo.RowsBean mCurrentCompleteTableInfo;

    /**
     * 是否是第一次定位，如果是，那么居中；否则，不做任何操作；
     */
    private boolean ifFirstLocate = true;
    private LocationButton locationButton;
    private LegendPresenter legendPresenter;
    View mView;
    ImageView iv_see_batch;
    MapView mMapView;

    private ILayerView layerView;

    private PatrolLayerPresenter layerPresenter;
    private boolean loadLayersSuccess = true;

    private View ll_layer_url_init_fail;
    private TextView show_all_layer;
    private GridView gridView;
    private LimitedLayerAdapter layerAdapter;
    //顶部图层列表中当前选中的设施类型对应的务URL
    private String currComponentUrl = LayerUrlConstant.newComponentUrls[0];
    ProgressDialog pd;
    ViewGroup map_bottom_sheet;
    AnchorSheetBehavior mBehavior;
    private ComponetListAdapter componetListAdapter;

    private ViewGroup component_detail_ll;
    /**
     * 点查后设施的详细信息布局，用了TableViewManager
     */
    private ViewGroup component_detail_container;

    private ArrayList<TableItem> tableItems = null;
    private ArrayList<Photo> photoList = new ArrayList<>();
    private String projectId;
    private TableViewManager tableViewManager;
    //点查后的设施结果
    private List<Component> mComponentQueryResult = new ArrayList<>();
    private List<MaintainBatchInfo.RowsBean> mUploadInfos = new ArrayList<>();
    private int currIndex = 0;
    private View btn_prev;
    private View btn_next;
    private boolean ifFirstAdd = true;
    private boolean ifFirstEdit = true;
    //private View layoutBottom;
    private boolean hasComponent = false;
    //private ProgressBar pb_distribute;

    private SelectLocationTouchListener addModeSelectLocationTouchListener;
    private View.OnClickListener addModeCalloutSureButtonClickListener;
    private View btn_edit;
    private View btn_edit_cancel;
    private SelectLocationTouchListener editModeSelectLocationTouchListener;
    private View.OnClickListener editModeCalloutSureButtonClickListener;
    /**
     * 地图默认的事件处理
     */
    private SelectLocationTouchListener defaultMapOnTouchListener;
    private int bottomHeight;
    private int bottomMargin;
    private TextView tv_error_correct;
    private TextView tv_sure;
    private List<UploadedFacility> mUploadedFacilitys;
    private Component mCurrentComponent;
    private MaintainLayerService mFeedbackLayerService;
    private Context mContext;
    private ViewGroup tableItemContainer;
    private MapHelper mapHelper;
    private View myUploadLayerBtn;
    private View uploadLayerBtn;
    private boolean ifUploadLayerVisible = false;
    private boolean ifMyUploadLayerVisible = false;

    private UploadedFacility firstUploadedFacility;
    private ModifiedFacility firstModifiedFacility;
    private MaintainBatchInfo.RowsBean firstCompleteTableInfo;

    private View ll_reset;
    private View btnReEdit;
    private View btnDelete;
    private View ll_feedback;
    private View btnFeedback;
    private View llGoFeedBackList;
    private ViewGroup approvalOpinionContainer;
    private TextView tv_approval_opinion_list;
    private DeleteFacilityService deleteFacilityService;
    private FeedbackFacilityService feedbackFacilityService;
    private View ll_select_group;
    private View ll_check_hint;

    /**
     * 设施原位置绘制图层
     */
    private GraphicsLayer mGLayerForOriginLocation;
    private int page = 1;
    public final static int COUNT_PER_PAGE = 10;
    private MaintainFilterGroupDialog filterGroupDialog;
    private Set<MaintainBatchInfo.RowsBean> batchList = null;
    private MaintainBatchInfo mBatchInfo = null;
    private boolean ifFeedBackLayerVisible = true;


    //获取批次的状态
    private final static int FIRST_IN = 0;
    private final static int FIRST_IN_NOT_BATCH = 1;
    private final static int FIRST_IN_HAS_BATCH = 2;
    //    boolean isFirstIn = true;
    private int batchState = 0;
    private boolean ifInEditMode;
    private SearchFragment searchFragment;//搜索框

    public static MaintainMapFragment getInstance(Bundle data) {
        MaintainMapFragment addComponentFragment2 = new MaintainMapFragment();
        addComponentFragment2.setArguments(data);
        return addComponentFragment2;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(mContext, R.layout.fragment_map_maintain, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        mView.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
            }
        });
        ((TextView) mView.findViewById(R.id.tv_title)).setText("管道养护");
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
        searchFragment.setHintText("搜索养护计划编号");
        searchFragment.setTAG(TAG);
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                //这里处理逻辑
                if (!StringUtil.isEmpty(keyword)) {
                    queryForPlanId(keyword);
                }
            }
        });
        mFeedbackLayerService = new MaintainLayerService(mContext);
        //获取交办批次的信息
        getBatchInfo(page, COUNT_PER_PAGE, batchState);
        if (EventBus.getDefault().isRegistered(getContext())) {
            EventBus.getDefault().register(getContext());
        }
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
                        if (PatrolLayerPresenter.initScale != -1 && PatrolLayerPresenter.initScale != 0) {
                            mMapView.setScale(PatrolLayerPresenter.initScale);
                            scaleView.setScale(PatrolLayerPresenter.initScale);
                        }
                        Point point = new Point(PatrolLayerPresenter.longitude, PatrolLayerPresenter.latitude);
                        mMapView.centerAt(point, true);

                        if (locationButton != null) {
                            // 2
                            locationButton.setStateNormal();
                        }
                        if (locationButton != null) {
                            locationButton.followLocation();
                        }
                        mMapView.setMaxScale(30);
                        if (layerPresenter != null) {
                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter.MAINTAIN_LAYER_NAME, true);
                            LayerInfo planLayerInfo = getPlanLayerInfo();
                            if (planLayerInfo != null) {
                                layerPresenter.onClickOpacityButton(planLayerInfo.getLayerId(), planLayerInfo, 0.7f);
                            }
                        }
                    }
                } else if (STATUS.LAYER_LOADED == status) {
                    if (layerPresenter != null) {
                        layerPresenter.changeLayerVisibility(PatrolLayerPresenter.MAINTAIN_LAYER_NAME, true);
                        LayerInfo planLayerInfo = getPlanLayerInfo();
                        if (planLayerInfo != null) {
                            layerPresenter.onClickOpacityButton(planLayerInfo.getLayerId(), planLayerInfo, 0.7f);
                        }
                    }
                }
            }
        });
        initFilterDialog();
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


        ll_select_group = view.findViewById(R.id.ll_select_group);
        ll_select_group.setOnClickListener(this);
        iv_see_batch = (ImageView) view.findViewById(R.id.iv_see_batch);

        //定位图标
        locationMarker = (LocationMarker) view.findViewById(R.id.locationMarker);
        //定位按钮
        locationButton = (LocationButton) view.findViewById(R.id.locationButton);
        locationButton.setIfShowCallout(false);
        locationButton.setMapView(mMapView);
        locationButton.setOnceLocation(false);
        locationButton.setIfAlwaysCeterToLocation(true);
        btn_edit = view.findViewById(R.id.btn_edit);
        btn_edit_cancel = view.findViewById(R.id.btn_edit_cancel);

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) locationButton
                .getLayoutParams();
        bottomMargin = lp.bottomMargin;
        mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
        /**
         * 默认使用窨井
         */
        locationMarker.changeIcon(R.mipmap.ic_add_facility);

        view.findViewById(R.id.btn_layer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = ((Activity) mContext);
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
        map_bottom_sheet = (ViewGroup) view.findViewById(R.id.map_bottom_sheet);
        mBehavior = AnchorSheetBehavior.from(map_bottom_sheet);
        mBehavior.setAnchorHeight(DensityUtil.dp2px(getContext(), 120));
        btn_prev = view.findViewById(R.id.prev);
        btn_next = view.findViewById(R.id.next);
        component_detail_ll = (ViewGroup) view.findViewById(R.id.detail_ll);
        component_detail_container = (ViewGroup) view.findViewById(R.id.detail_container);
        tableItemContainer = (ViewGroup) view.findViewById(R.id.ll_table_item_container);
        approvalOpinionContainer = (ViewGroup) view.findViewById(R.id.ll_approval_opinion_container);
        tv_error_correct = (TextView) view.findViewById(R.id.tv_error_correct);
//        ll_locate_pos.setOnClickListener(this);

        mBehavior.setAnchorHeight(DensityUtil.dp2px(mContext, 230));
        RxView.clicks(btn_next)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currIndex++;
                        if (currIndex > mUploadInfos.size()) {
                            btn_next.setVisibility(View.GONE);
                            return;
                        }
                        component_detail_container.removeAllViews();
                        tableItemContainer.removeAllViews();
                        approvalOpinionContainer.removeAllViews();
                        mCurrentCompleteTableInfo = mUploadInfos.get(currIndex);
                        showBottomSheet(mUploadInfos.get(currIndex));
                        if (currIndex == (mUploadInfos.size() - 1)) {
                            btn_next.setVisibility(View.GONE);
                        }
                        if (currIndex > 0) {
                            btn_prev.setVisibility(View.VISIBLE);
                        }
                    }
                });
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
                        component_detail_container.removeAllViews();
                        tableItemContainer.removeAllViews();
                        approvalOpinionContainer.removeAllViews();
                        mCurrentCompleteTableInfo = mUploadInfos.get(currIndex);
                        showBottomSheet(mUploadInfos.get(currIndex));

                        if (currIndex == 0) {
                            btn_prev.setVisibility(View.GONE);
                        }
                        if (mUploadInfos.size() > 1) {
                            btn_next.setVisibility(View.VISIBLE);
                        }
                    }
                });

//        loadMap();


        /**
         * 图例
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

        myUploadLayerBtn = view.findViewById(R.id.ll_my_upload_layer);
        final TextView tv_my_upload_layer = (TextView) view.findViewById(R.id.tv_my_upload_layer);
        final SwitchCompat myUploadIv = (SwitchCompat) view.findViewById(R.id.iv_my_upload_layer);
        myUploadLayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ifMyUploadLayerVisible) {
                    myUploadIv.setChecked(false);
                    tv_my_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.invisible_state_text_color, null));
                    ifMyUploadLayerVisible = false;
                } else {
                    myUploadIv.setChecked(true);
                    tv_my_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                    ifMyUploadLayerVisible = true;
                }

                if (layerPresenter != null) {
                    layerPresenter.changeLayerVisibility(PatrolLayerPresenter.MY_UPLOAD_LAYER_NAME, ifMyUploadLayerVisible);
                }
            }
        });

        //我的上报图层按钮
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

        //注册当图层可见度发生改变时的回调
        if (layerPresenter != null) {
            layerPresenter.registerLayerVisibilityChangedListener(new PatrolLayerPresenter.OnLayerVisibilityChangedListener() {
                @Override
                public void changed(boolean visible, LayerInfo layerInfo) {
//                    if (!visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.LOCAL_UPLOAD_LAYER_NAME)) {
//                        //不可见
//                        //  myUploadIv.setImageResource(R.drawable.ic_upload_data_normal2);
//                        uploadIv.setChecked(false);
//                        tv_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(),R.color.invisible_state_text_color,null));
//                        ifUploadLayerVisible = false;
//                    } else if (visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.LOCAL_UPLOAD_LAYER_NAME)) {
//                        //可见
//                        // myUploadIv.setImageResource(R.drawable.ic_upload_data_pressed);
//                        uploadIv.setChecked(true);
//                        tv_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(),R.color.colorAccent,null));
//                        ifUploadLayerVisible = true;
//                    }else
                    if (!visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.MAINTAIN_LAYER_NAME)) {
                        //不可见
                        //  myUploadIv.setImageResource(R.drawable.ic_upload_data_normal2);
//                        myUploadIv.setChecked(false);
//                        tv_my_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.invisible_state_text_color, null));
                        ifMyUploadLayerVisible = false;
                    } else if (visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.MAINTAIN_LAYER_NAME)) {
                        //可见
                        // myUploadIv.setImageResource(R.drawable.ic_upload_data_pressed);
//                        myUploadIv.setChecked(true);
//                        tv_my_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                        ifMyUploadLayerVisible = true;
                    }
//                    else if (visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.MAINTAIN_LAYER_NAME)) {
//                        ifFeedBackLayerVisible = true;
//                    } else if (!visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.MAINTAIN_LAYER_NAME)) {
//                        ifFeedBackLayerVisible = false;
//
//                    }
                }
            });
        }

//        initBottomSheetView();

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
                    ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "移动地图选择设施的位置");
                    ifFirstEdit = false;
                }

                if (locationMarker != null) {
                    locationMarker.changeIcon(R.mipmap.ic_check_data_2);
                    locationMarker.setVisibility(View.VISIBLE);
                }
                hideBottomSheet();
                initGLayer();
                setSearchFacilityListener();
                //下面的代码不能在调用 setSearchFacilityListener 方法前调用，
                // 因为editModeSelectLocationTouchListener 和 editModeCalloutSureButtonClickListener 未初始化
//                if (locate) {
//                    editModeSelectLocationTouchListener.locate();
//                    if (mMapView.getCallout().isShowing()) {
//                        mMapView.getCallout().animatedHide();
//                    }
//                    editModeCalloutSureButtonClickListener.onClick(null);
//                }

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

    }

    public void MessageBox(Context context, String title, String msg,
                           DialogInterface.OnClickListener positiveListener,
                           DialogInterface.OnClickListener negetiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        if (positiveListener != null) {
            builder.setPositiveButton("继续", positiveListener);
        }
        if (negetiveListener != null) {
            builder.setNegativeButton("放弃",
                    negetiveListener);
        }
        builder.show();
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
            //查询设施模式下，点击callout中的确定按钮时的回调
            editModeCalloutSureButtonClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double scale = mMapView.getScale();
                    if (scale > LayerUrlConstant.MIN_QUERY_SCALE) {
                        ToastUtil.shortToast(getContext(), "请先放大到可以看到设施的级别");
                        return;
                    }

                    LocationInfo locationInfo = editModeSelectLocationTouchListener.getLoationInfo();
                    if (locationInfo.getDetailAddress() == null && locationInfo.getPoint() == null) {
                        ToastUtil.shortToast(mContext, "请重新选择位置");
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
         * 气泡中“确定”按钮点击事件
         */
        editModeSelectLocationTouchListener.setCalloutSureButtonClickListener(editModeCalloutSureButtonClickListener);
        mMapView.setOnTouchListener(editModeSelectLocationTouchListener);
    }

    private void initFilterDialog() {
        if (null == filterGroupDialog) {
            filterGroupDialog = new MaintainFilterGroupDialog(mContext);
        }
        filterGroupDialog.setFilterGroupListener(new MaintainFilterGroupListener() {
            @Override
            public void filterByIds(Set<MaintainBatchInfo.RowsBean> selectedList) {
                batchList = selectedList;
                filterData(selectedList);
            }

            @Override
            public void getPrePage() {
                if (page == 1) {
                    ToastUtil.shortToast(mContext, "已经是第一页了");
                    return;
                }
                getBatchInfo(--page, COUNT_PER_PAGE, FIRST_IN_HAS_BATCH);
            }

            @Override
            public void getNextPage() {
                getBatchInfo(++page, COUNT_PER_PAGE, FIRST_IN_HAS_BATCH);
            }

            @Override
            public void searchFor(String searchString) {

            }
        });

    }


    private void getBatchInfo(final int page, final int countPerPage, final int isFirstIn) {
//        if(!isReLoadBatchInfo){
//            showPopMenu(mBatchInfo);
//            return ;
//        }

        mFeedbackLayerService.getBatchInfo(page, countPerPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MaintainBatchInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (batchState == FIRST_IN) {
                            loadMap();
                            batchState = FIRST_IN_NOT_BATCH;
                        } else {
                            showPopMenu(null);
                        }
                    }

                    @Override
                    public void onNext(MaintainBatchInfo batchInfo) {
                        mBatchInfo = batchInfo;
                        if (batchInfo != null && !ListUtil.isEmpty(batchInfo.getRows()) && batchInfo.getCode() == 200) {
                            showPopMenu(batchInfo);
                        } else {
                            if (batchState == FIRST_IN) {
                                loadMap();
                                batchState = FIRST_IN_NOT_BATCH;
                            } else {
                                showPopMenu(batchInfo);
                            }
                        }
                    }
                });
    }

    private void showPopMenu(MaintainBatchInfo info) {
//        if (info == null) {
//            return;
//        }
        if (batchState == FIRST_IN) {
            filterGroupDialog.setSelectFirstInStete(info);
            batchState = FIRST_IN_HAS_BATCH;
            ;
            return;
        }
        filterGroupDialog.showDialog(ll_select_group, info, page);
    }

    /**
     * 删除设施
     */
    private void deleteFacility() {
        initDeleteFacilityService();
        if (mCurrentUploadedFacility != null) {
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("删除中.....");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            deleteFacilityService.deleteFacility(UploadLayerFieldKeyConstant.ADD, mCurrentUploadedFacility.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Result2<String>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
                                    BaseInfoManager.getUserName(mContext) + "原因是：" + e.getLocalizedMessage()));
                            ToastUtil.iconShortToast(mContext, R.mipmap.ic_alert_yellow, "删除失败，请稍后重试");
                        }

                        @Override
                        public void onNext(Result2<String> s) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            if (s.getCode() == 200) {
                                //清空界面
                                initGLayer();
                                hideBottomSheet();
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new RefreshMyUploadList());
                                //ToastUtil.shortToast(mContext,"删除成功");
                                /**
                                 * 判断是否是通过列表点击进入的记录，如果是，那么此时需要隐藏“原”按钮
                                 */
                                if (firstUploadedFacility != null &&
                                        firstUploadedFacility.getId() != null &&
                                        firstUploadedFacility.getId().equals(mCurrentUploadedFacility.getId())) {
                                    ll_reset.setVisibility(View.GONE);
                                }
                            } else {
                                CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
                                        BaseInfoManager.getUserName(mContext) + "原因是：" + s.getMessage()));
                                Toast.makeText(mContext, s.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else if (mCurrentModifiedFacility != null) {
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("删除中.....");
            progressDialog.show();
            deleteFacilityService.deleteFacility(mCurrentModifiedFacility.getReportType(), mCurrentModifiedFacility.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Result2<String>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            ToastUtil.iconShortToast(mContext, R.mipmap.ic_alert_yellow, "删除失败，请稍后重试");
                        }

                        @Override
                        public void onNext(Result2<String> s) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            if (s.getCode() == 200) {
                                //清空界面
                                initGLayer();
                                hideBottomSheet();
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new RefreshMyModificationListEvent());
                                /**
                                 * 判断是否是通过列表点击进入的记录，如果是，那么此时需要隐藏“原”按钮
                                 */
                                if (firstModifiedFacility != null
                                        && mCurrentModifiedFacility != null
                                        && firstModifiedFacility.getId() != null
                                        && firstModifiedFacility.getId().equals(mCurrentModifiedFacility.getId())) {
                                    ll_reset.setVisibility(View.GONE);
                                }
                            } else {
                                CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
                                        BaseInfoManager.getUserName(mContext) + "原因是：" + s.getMessage()));
                                Toast.makeText(mContext, s.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    private void initDeleteFacilityService() {
        if (deleteFacilityService == null) {
            deleteFacilityService = new DeleteFacilityService(mContext);
        }
    }

    private void initLegendPresenter() {
        if (legendPresenter == null) {
            ILegendView legendView = new ImageLegendView(mContext);
            legendPresenter = new LegendPresenter(legendView, new OnlineLegendService(mContext));
        }

    }

    public void loadMap() {
        clearGLayer();
        IDrawerController drawerController = (IDrawerController) mContext;
        layerView = new PatrolLayerView2(mContext, mMapView, drawerController.getDrawerContainer());
//        layerPresenter = new PatrolLayerPresenter(layerView, new UploadDetailLayerService(mContext.getApplicationContext());
        layerPresenter = new PatrolLayerPresenter(layerView, new FeedbackLayerService(mContext.getApplicationContext())) {
            private ILayerFactory layerFactory;

            @Override
            protected Layer getSingleLayer(LayerInfo layer) {
                initLayerFactory();
                return layerFactory.getLayer(mContext, layer);
            }

            private void initLayerFactory() {
                if (layerFactory == null) {
                    layerFactory = new MaintainLayerFactory(batchList, mContext);
                }
            }
        };
        layerPresenter.loadLayer(new Callback2<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                loadLayersSuccess = true;
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }

            @Override
            public void onFail(Exception error) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                loadLayersSuccess = false;
            }
        });
    }

    /**
     * 清空
     */
    private void clearGLayer() {
        if (mGLayer != null) {
            mGLayer.removeAll();
            mGLayer = null;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            this.mContext = context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity != null) {
            this.mContext = activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
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

            case R.id.ll_select_group:
                //筛选不同批次的交办数据
//                getBatchInfo(page,COUNT_PER_PAGE,batchList == null || batchList.size()==0?false:true);
                getBatchInfo(page, COUNT_PER_PAGE, batchState);
//                showPopMenu(page, COUNT_PER_PAGE, true);
                break;

            case R.id.btn_edit:

                break;

            case R.id.btn_edit_cancel:

                break;
            default:
        }
    }


    //手动隐藏
    private void hideCallout() {
        Callout callout = mMapView.getCallout();
        if (null != callout && callout.isShowing()) {
            mMapView.getCallout().hide();
        }
    }

    private void filterData(Set<MaintainBatchInfo.RowsBean> selectedList) {

        Callout callout = mMapView.getCallout();
        if (callout != null && callout.isShowing()) {
            callout.hide();
        }
        if (btn_edit_cancel.getVisibility() == View.VISIBLE) {
            btn_edit_cancel.performClick();
        }

        if (pd == null) {
            pd = new ProgressDialog(mContext);
        }
        pd.setMessage("正在加载交办批次...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        if (selectedList == null || selectedList.size() == 0) {
            iv_see_batch.setImageResource(R.drawable.ic_invisible);
        } else {
            iv_see_batch.setImageResource(R.drawable.ic_visible);
        }
        loadMap();
        //TODO 根据不同批次的显示地图上的标记
//        initLayerFactory(getFilterInfo(selectedList));
    }


    public void onBackPressed() {
        if (map_bottom_sheet.getVisibility() == View.GONE) {
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
                //3
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
            initGLayer();
            hideBottomSheet();
//            tableItemContainer.removeAllViews();
//            component_detail_container.removeAllViews();
//            approvalOpinionContainer.removeAllViews();
            if (visibility == View.VISIBLE) {
                return;
            }
            double scale = mMapView.getScale();
//            if (scale < LayerUrlConstant.MIN_QUERY_SCALE && (ifUploadLayerVisible || ifMyUploadLayerVisible)) {
            if (scale < LayerUrlConstant.MIN_QUERY_SCALE && ifFeedBackLayerVisible) {
                final Point point = mMapView.toMapPoint(e.getX(), e.getY());
                query(point.getX(), point.getY());

            }
        }

    }

    /**
     * 点击地图后查询设施
     *
     * @param x
     * @param y
     */
    private void query(double x, double y) {
        if (pd == null) {
            pd = new ProgressDialog(mContext);
        }
        pd.setMessage("正在查询上报信息...");
        pd.show();
        mUploadInfos.clear();
        btn_next.setVisibility(View.GONE);
        btn_prev.setVisibility(View.GONE);
        currIndex = 0;
        //        final Point point = mMapView.toMapPoint(x, y);
        final Point point = new Point(x, y);

        mCurrentModifiedFacility = null;
        mCurrentUploadedFacility = null;
        mCurrentCompleteTableInfo = null;
        hasComponent = false;
        //tv_sure.setVisibility(View.GONE);

        //交办反馈不可见
        if (!ifFeedBackLayerVisible) return;
//        mFeedbackLayerService.setQueryByWhere(getQueryWhere() +" and "+ getFilterInfo(batchList));
        if (!StringUtil.isEmpty(getFilterInfo(batchList))) {
//            mFeedbackLayerService.setQueryByWhere(getDistrictQueryWhere());
            mFeedbackLayerService.setQueryByWhere(getFilterInfo(batchList));
        }
//        else {
//            mFeedbackLayerService.setQueryByWhere(TextUtils.isEmpty(getDistrictQueryWhere()) ? getFilterInfo(batchList) : getDistrictQueryWhere() + " and " + getFilterInfo(batchList));
//        }
        mFeedbackLayerService.queryUploadDataInfo(point, mMapView.getSpatialReference(), mMapView.getResolution(), new Callback2<List<MaintainBatchInfo.RowsBean>>() {
            @Override
            public void onSuccess(List<MaintainBatchInfo.RowsBean> uploadInfos) {
                pd.dismiss();
                if (ListUtil.isEmpty(uploadInfos)) {
                    ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "查无数据！");
                    return;
                }
                mUploadInfos = uploadInfos;
                showOnBottomSheet(uploadInfos);
            }

            @Override
            public void onFail(Exception error) {
                pd.dismiss();
                ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, error.getLocalizedMessage());
                pd.dismiss();
            }
        });
    }

    private LayerInfo getPlanLayerInfo() {
        List<LayerInfo> layerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
        if (ListUtil.isEmpty(layerInfosFromLocal)) {
            return null;
        }
        for (LayerInfo layerInfo : layerInfosFromLocal) {
            if (layerInfo.getLayerName().contains(PatrolLayerPresenter.MAINTAIN_LAYER_NAME)) {
                return layerInfo;
            }
        }
        return null;
    }

    /**
     * 点击地图后查询设施
     *
     * @param planId
     */
    private void queryForPlanId(String planId) {
        if (pd == null) {
            pd = new ProgressDialog(mContext);
        }
        pd.setMessage("正在查询上报信息...");
        pd.show();
        mUploadInfos.clear();
        btn_next.setVisibility(View.GONE);
        btn_prev.setVisibility(View.GONE);
        currIndex = 0;

        mCurrentModifiedFacility = null;
        mCurrentUploadedFacility = null;
        mCurrentCompleteTableInfo = null;
        hasComponent = false;
        //tv_sure.setVisibility(View.GONE);

        //交办反馈不可见
        if (!ifFeedBackLayerVisible) return;
        String where = "";
        if (StringUtil.isEmpty(getFilterInfo(batchList))) {
            where += ("JHID=" + planId);
        } else {
            where += getFilterInfo(batchList) + " and JHID=" + planId;
        }
        mFeedbackLayerService.setQueryByWhere(where);
        mFeedbackLayerService.queryUploadDataForObjectId(new Callback2<List<MaintainBatchInfo.RowsBean>>() {
            @Override
            public void onSuccess(List<MaintainBatchInfo.RowsBean> uploadInfos) {
                pd.dismiss();
                if (ListUtil.isEmpty(uploadInfos)) {
                    ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "查无数据！");
                    return;
                }
                mUploadInfos = uploadInfos;
                showOnBottomSheet(uploadInfos);
            }

            @Override
            public void onFail(Exception error) {
                pd.dismiss();
                initGLayer();
                if (map_bottom_sheet.getVisibility() == View.GONE) {
                    map_bottom_sheet.setVisibility(View.GONE);
                }
                ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, error.getLocalizedMessage());
            }
        });
    }

    private String getFilterInfo(Set<MaintainBatchInfo.RowsBean> batchList1) {
        if (batchList1 == null || batchList1.size() == 0) return " OBJECTID = -1 ";
        StringBuilder sb = new StringBuilder("  OBJECTID in ( ");
        for (MaintainBatchInfo.RowsBean info : batchList1) {
            sb.append("'").append(info.getObjectId()).append("'").append(",");
//            sb.append(info).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    private String getQueryWhere() {

//        FacilityAffairService facilityAffairService = new FacilityAffairService(mContext);
//        String parentOrgOfCurrentUser = facilityAffairService.getParentOrgOfCurrentUser();
//        boolean ifCurrentUserBelongToCityUser = facilityAffairService.ifCurrentUserBelongToCityUser();
//        //当不是全市查询时加入区域限制
//        if (!ifCurrentUserBelongToCityUser) {
//            return " PARENT_ORG_NAME like '%" + parentOrgOfCurrentUser + "%'";
//        }
//        return null;
        //只能查询自己上报的
        return "MARK_PID='" + BaseInfoManager.getLoginName(mContext) + "'";
    }

    private String getDistrictQueryWhere() {
        String district = BaseInfoManager.getUserOrg(mContext);
        if (district.contains("净水公司")) {
            district = "净水";
        }
        String where = "";
        if (!district.contains("市")) {
            if (TextUtils.isEmpty(where)) {
                where += " PARENT_ORG_NAME like '%" + district + "%'";
            } else {
                where += "and PARENT_ORG_NAME like '%" + district + "%'";
            }
        }
        return where;
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

        if (ifCenter) {
            mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        }
    }

    @NonNull
    private Symbol getPointSymbol() {
        Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), R.mipmap.ic_select_location_orange, null);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mContext, drawable);// xjx 改为兼容api19的方式获取drawable
        pictureMarkerSymbol.setOffsetY(16);
        return pictureMarkerSymbol;
    }

    private void hideBottomSheet() {
        map_bottom_sheet.setVisibility(View.GONE);
        final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) locationButton
                .getLayoutParams();
        lp.bottomMargin = bottomMargin;
        locationButton.setLayoutParams(lp);
    }

    private void showOnBottomSheet(List<MaintainBatchInfo.RowsBean> uploadInfos) {
        if (uploadInfos.size() > 1) {
            btn_next.setVisibility(View.VISIBLE);
        }
        if (mMapView.getCallout().isShowing()) {
            mMapView.getCallout().animatedHide();
        }

//        component_detail_container.removeAllViews();
//        tableItemContainer.removeAllViews();
//        approvalOpinionContainer.removeAllViews();

        //隐藏marker
        locationMarker.setVisibility(View.GONE);
        initGLayer();
        Geometry geometry = null;
        mCurrentCompleteTableInfo = uploadInfos.get(0);

        showBottomSheet(uploadInfos.get(0));
    }

//    private void initBottomSheetView() {
//        nextAndPrevContainer = (ViewGroup) map_bottom_sheet.findViewById(R.id.ll_next_and_prev_container);
//
//        //上报信息
//        tv_sure = (TextView) map_bottom_sheet.findViewById(R.id.tv_sure);
//        tv_sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                resetStatus(true);
//                component_detail_container.setVisibility(View.VISIBLE);
//                tableItemContainer.setVisibility(View.GONE);
//                showBottomSheet(mCurrentModifiedFacility, mCurrentCompleteTableInfo);
//
//            }
//        });
//
//        //原部件按钮
//        tv_error_correct = (TextView) map_bottom_sheet.findViewById(R.id.tv_error_correct);
//        tv_error_correct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                resetStatus(false);
//                component_detail_container.setVisibility(View.GONE);
//                tableItemContainer.setVisibility(View.VISIBLE);
//                if (mCurrentCompleteTableInfo != null) {
//                    showBottomSheet(mCurrentCompleteTableInfo);
//                }
//            }
//        });
//        ll_check_hint = map_bottom_sheet.findViewById(R.id.ll_check_hint);
//        tv_approval_opinion_list = (TextView) map_bottom_sheet.findViewById(R.id.tv_approval_opinion_list);
//        tv_approval_opinion_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                resetStatus(false, true);
//                component_detail_container.setVisibility(View.GONE);
//                tableItemContainer.setVisibility(View.GONE);
//                approvalOpinionContainer.setVisibility(View.VISIBLE);
//                if (mCurrentModifiedFacility != null) {
//                    showBottomSheet(mCurrentModifiedFacility.getId(), mCurrentModifiedFacility.getReportType());
//                } else if (mCurrentUploadedFacility != null) {
//                    showBottomSheet(mCurrentUploadedFacility.getId(), UploadLayerFieldKeyConstant.ADD);
//                }
//
//            }
//        });
//        tv_approval_opinion_list.setVisibility(View.VISIBLE);
//    }

    private void showBottomSheet(Long markId, String reportType) {
        if (approvalOpinionContainer.getChildCount() == 0) {
            ApprovalOpinionViewManager myModifiedFacilityTableViewManage = new ApprovalOpinionViewManager(mContext, markId, reportType);
            myModifiedFacilityTableViewManage.addTo(approvalOpinionContainer);
        }
    }

    @Nullable
    private String replaceSpaceCharacter(String sort) {
        if (sort != null && TextUtils.isEmpty(sort.replace(" ", ""))) {
            sort = null;
        }
        return sort;
    }

    /**
     * 设施部件
     * 加载设施信息，显示中底部BottomSheet中
     */
    private void showBottomSheet(final MaintainBatchInfo.RowsBean CompleteTableInfo) {
        initGLayer();
        mGLayer.removeAll();
        drawGeometry(CompleteTableInfo.getGeometry(), mGLayer, false, true);

        TextView titleTv = (TextView) map_bottom_sheet.findViewById(R.id.title);
        TextView maintainStartDate = (TextView) map_bottom_sheet.findViewById(R.id.maintain_start_date);
        TextView maintainEndDate = (TextView) map_bottom_sheet.findViewById(R.id.maintain_end_date);
        TextView maintainContent = (TextView) map_bottom_sheet.findViewById(R.id.maintain_plan_content);
        maintainStartDate.setText("计划开始日期：" + TimeUtil.getStringTimeYMDChines(new Date(CompleteTableInfo.getStartDate())));
        maintainEndDate.setText("计划结束日期：" + TimeUtil.getStringTimeYMDChines(new Date(CompleteTableInfo.getEndDate())));
        maintainContent.setText("计划养护内容：" + CompleteTableInfo.getContent());
        titleTv.setText(CompleteTableInfo.getName());
        /**
         * 新增按钮
         */
        map_bottom_sheet.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 新增数据
                if (StringUtil.isEmpty(CompleteTableInfo.getObjectId())) {
                    ToastUtil.longToast(mContext, "获取不到计划Id");
                    return;
                }
                MaintainBatchInfo.RowsBean rowsBean = getIdForObjectId(CompleteTableInfo.getObjectId());
                Intent intent = new Intent(mContext, MaintainNewActivity.class);
                intent.putExtra("rowsbean", rowsBean);
                if (locationButton.getLastLocation() != null) {
                    intent.putExtra("detailAddress", locationButton.getLastLocation().getDetailAddress());
                    intent.putExtra("x", locationButton.getLastLocation().getLocation().getLongitude());
                    intent.putExtra("y", locationButton.getLastLocation().getLocation().getLatitude());
                }
                startActivityForResult(intent, 123);
            }
        });

        /**
         * 养护列表
         */
        map_bottom_sheet.findViewById(R.id.tv_error_correct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 新增数据
                if (StringUtil.isEmpty(CompleteTableInfo.getObjectId())) {
                    ToastUtil.longToast(mContext, "获取不到计划Id");
                    return;
                }
                MaintainBatchInfo.RowsBean rowsBean = getIdForObjectId(CompleteTableInfo.getObjectId());
                Intent intent = new Intent(mContext, MaintainListActivity.class);
                intent.putExtra("rowsbean", rowsBean);
                intent.putExtra("geometry", CompleteTableInfo.getGeometry());
                startActivityForResult(intent, 123);
            }
        });

        ////// showBottomSheetContent(component_intro.getId());
        map_bottom_sheet.setVisibility(View.VISIBLE);
        if (mBehavior.getState() == STATE_COLLAPSED || mBehavior.getState() == com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior.STATE_HIDDEN) {
            map_bottom_sheet.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBehavior.setState(com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior.STATE_ANCHOR);
                }
            }, 200);
        }

    }

    private MaintainBatchInfo.RowsBean getIdForObjectId(String objectId) {
        if (ListUtil.isEmpty(batchList)) {
            return null;
        }
        for (MaintainBatchInfo.RowsBean bean : batchList) {
            if (objectId.equals(bean.getObjectId())) {
                return bean;
            }
        }
        return null;
    }

    public void initGLayerForOriginLocation() {
        if (mGLayerForOriginLocation == null) {
            mGLayerForOriginLocation = new GraphicsLayer();
            mGLayerForOriginLocation.setSelectionColor(Color.BLUE);
            mMapView.addLayer(mGLayerForOriginLocation);
        }
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
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        LayersService.clearInstance();
    }

    @Subscribe
    public void onReceivedUploadFacilityEvent(final UploadFacilitySuccessEvent uploadFacilitySuccessEvent) {

    }

    private void refreshFirstModifiedFacility(ModifiedFacility modifiedFacility) {
        if (firstModifiedFacility != null) {
            if (firstModifiedFacility.getId() != null && firstModifiedFacility.getId().equals(modifiedFacility.getId())) {
                firstModifiedFacility = modifiedFacility;
            }
        }
    }

    private void refreshFirstUploadFacility(UploadedFacility uploadedFacility) {
        if (firstUploadedFacility != null) {
            if (firstUploadedFacility.getId() != null && firstUploadedFacility.getId().equals(uploadedFacility.getId())) {
                firstUploadedFacility = uploadedFacility;
            }
        }
    }

    @Subscribe
    public void onRefreshMyUploadListEvent(RefreshMyUploadList refreshMyUploadList) {
//        if (btn_edit_cancel.getVisibility() == View.VISIBLE) {
//            btn_edit_cancel.performClick();
//        }
//        if (btn_cancel.getVisibility() == View.VISIBLE) {
//            btn_cancel.performClick();
//        }
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

}
