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

package com.augurit.agmobile.gzps.uploadfacility.view.feedback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.augurit.agmobile.gzps.uploadfacility.model.BatchInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.CheckState;
import com.augurit.agmobile.gzps.uploadfacility.model.CompleteTableInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.service.CorrectFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.DeleteFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.FeedBackLayerFactory;
import com.augurit.agmobile.gzps.uploadfacility.service.FeedbackFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.FeedbackLayerService;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.util.CompleteTableInfoUtil;
import com.augurit.agmobile.gzps.uploadfacility.util.UploadLayerFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadfacility.view.UploadFacilitySuccessEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.approvalopinion.ApprovalOpinionViewManager;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.OriginalAttributesViewManager;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.RefreshMyModificationListEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.reeditfacility.ReEditModifiedFacilityActivity;
import com.augurit.agmobile.gzps.uploadfacility.view.reeditfacility.ReEditUploadFacilityActivity;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.RefreshMyUploadList;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.common.widget.scaleview.MapScaleView;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
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
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior.STATE_ANCHOR;
import static com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior.STATE_COLLAPSED;
import static com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior.STATE_EXPANDED;


/**
 * 上报设施详情
 *
 * @author 创建人 ：luobiao
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.map
 * @createTime 创建时间 ：2017-12-7
 * @modifyBy 修改人 ：luobiao,xuciluan
 * @modifyTime 修改时间 ：2017-12-7
 * @modifyMemo 修改备注：
 */
public class UploadFeedbackMapFragment extends Fragment implements View.OnClickListener {

    private static final String KEY_MAP_STATE = "com.esri.MapState";
    private final String TAG = "FeedbackSearchFragment";

    private LocationMarker locationMarker;

    private ModifiedFacility mCurrentModifiedFacility;
    private UploadedFacility mCurrentUploadedFacility;
    private CompleteTableInfo mCurrentCompleteTableInfo;

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
    private List<UploadInfo> mUploadInfos = new ArrayList<>();
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
    private FeedbackLayerService mFeedbackLayerService;
    private Context mContext;
    private ViewGroup nextAndPrevContainer;
    private ViewGroup tableItemContainer;
    private MapHelper mapHelper;
    private View myUploadLayerBtn;
    private View uploadLayerBtn;
    private boolean ifUploadLayerVisible = false;
    private boolean ifMyUploadLayerVisible = false;

    private UploadedFacility firstUploadedFacility;
    private ModifiedFacility firstModifiedFacility;
    private CompleteTableInfo firstCompleteTableInfo;

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

    /**
     * 设施原位置绘制图层
     */
    private GraphicsLayer mGLayerForOriginLocation;
    private int page = 1;
    public final static int COUNT_PER_PAGE = 10;
    private FilterGroupDialog filterGroupDialog;
    private Set<BatchInfo.Info> batchList = null;
    private BatchInfo mBatchInfo = null;
    private boolean ifFeedBackLayerVisible = true;


    //获取批次的状态
    private final static int FIRST_IN = 0;
    private final static int FIRST_IN_NOT_BATCH = 1;
    private final static int FIRST_IN_HAS_BATCH = 2;
    //    boolean isFirstIn = true;
    private int batchState = 0;
    private boolean ifInEditMode;
    private SearchFragment searchFragment;//搜索框

    public static UploadFeedbackMapFragment getInstance(Bundle data) {
        UploadFeedbackMapFragment addComponentFragment2 = new UploadFeedbackMapFragment();
        addComponentFragment2.setArguments(data);
        return addComponentFragment2;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(mContext, R.layout.fragment_uploadmap_feedback, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        mView.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)mContext).finish();
            }
        });
        ((TextView) mView.findViewById(R.id.tv_title)).setText("交办反馈");
        mView.findViewById(R.id.ll_search).setVisibility(View.VISIBLE);
//        ImageView iv_open_search = (ImageView) mView.findViewById(R.id.iv_open_search);
        ((TextView) mView.findViewById(R.id.tv_search)).setText("");
        mView.findViewById(R.id.tv_search).setVisibility(View.VISIBLE);
        mView.findViewById(R.id.toolbar_view).setVisibility(View.VISIBLE);
        mView.findViewById(R.id.ll_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!searchFragment.isAdded()
                        && !searchFragment.isVisible()
                        && !searchFragment.isRemoving()) {
                    searchFragment.show(getFragmentManager(),TAG);
                }
            }
        });
        searchFragment = SearchFragment.newInstance();
        searchFragment.setHintText("搜索核查数据编号");
        searchFragment.setTAG(TAG);
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                //这里处理逻辑
                if(!StringUtil.isEmpty(keyword)){
                    queryForObjctId(keyword);
                }
            }
        });
        mFeedbackLayerService = new FeedbackLayerService(mContext);
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
                        mMapView.setMaxScale(30);
//                        if (myUploadLayerBtn != null) {
//                            myUploadLayerBtn.performClick();
//                        }
                        // layerPresenter.changeLayerVisibility(PatrolLayerPresenter.LOCAL_UPLOAD_LAYER_NAME, true);

                        if (getArguments() != null
                                && getArguments().getParcelable("uploadedFacility") != null) {
                            UploadedFacility uploadedFacility = (UploadedFacility) getArguments().getParcelable("uploadedFacility");
                            if (uploadedFacility != null) {

                                firstUploadedFacility = uploadedFacility;
                                mUploadInfos.clear();
                                nextAndPrevContainer.setVisibility(View.GONE);
                                btn_next.setVisibility(View.GONE);
                                btn_prev.setVisibility(View.GONE);
                                currIndex = 0;
                                mCurrentModifiedFacility = null;
                                mCurrentUploadedFacility = null;
                                mCurrentCompleteTableInfo = null;
                                hasComponent = false;
                                tv_error_correct.setVisibility(View.VISIBLE);
                                resetStatus(true);
                                showBottomSheet(uploadedFacility, null);
                            }
                        } else if (getArguments() != null
                                && getArguments().getParcelable("modifiedFacility") != null) {

                            ModifiedFacility modifiedFacility = getArguments().getParcelable("modifiedFacility");
                            if (modifiedFacility != null) {

                                firstModifiedFacility = modifiedFacility;

                                mUploadInfos.clear();
                                nextAndPrevContainer.setVisibility(View.GONE);
                                btn_next.setVisibility(View.GONE);
                                btn_prev.setVisibility(View.GONE);
                                currIndex = 0;
                                mCurrentModifiedFacility = null;
                                mCurrentUploadedFacility = null;
                                mCurrentCompleteTableInfo = null;
                                hasComponent = false;
                                tv_error_correct.setVisibility(View.VISIBLE);
                                resetStatus(true);
                                //填充completeTableInfo
//                                if (modifiedFacility.getOriginAttrOne() != null) {
//                                    CompleteTableInfo completeTableInfo = CompleteTableInfoUtil.getCompleteTableInfo(modifiedFacility);
//                                    mCurrentCompleteTableInfo = completeTableInfo;
//                                    firstCompleteTableInfo = completeTableInfo;
//                                }
                                CompleteTableInfo completeTableInfo = CompleteTableInfoUtil.getCompleteTableInfo(modifiedFacility);
                                mCurrentCompleteTableInfo = completeTableInfo;
                                firstCompleteTableInfo = completeTableInfo;
                                showBottomSheet(modifiedFacility, mCurrentCompleteTableInfo);
                            }
                        } else {
                            //在没有传递过来坐标的情况下才进行定位
                            if (locationButton != null) {
                                // 2
                                locationButton.followLocation();
                            }
                        }
                        if (layerPresenter != null) {
                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter.FEEDBACK_LAYER_NAME, true);
                        }
                    }
                } else if (STATUS.LAYER_LOADED == status) {
                    if (layerPresenter != null) {
                        layerPresenter.changeLayerVisibility(PatrolLayerPresenter.FEEDBACK_LAYER_NAME, true);

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
                        resetStatus(true);
                        mCurrentCompleteTableInfo = mUploadInfos.get(currIndex).getCompleteTableInfo();
                        if (mCurrentCompleteTableInfo != null) {
                            tv_error_correct.setVisibility(View.VISIBLE);
                        }
                        if (mUploadInfos.get(currIndex).getModifiedFacilities() != null) {
                            showBottomSheet(mUploadInfos.get(currIndex).getModifiedFacilities(), mUploadInfos.get(currIndex).getCompleteTableInfo());
                        } else if (mUploadInfos.get(currIndex).getUploadedFacilities() != null) {
                            showBottomSheet(mUploadInfos.get(currIndex).getUploadedFacilities(), mUploadInfos.get(currIndex).getCompleteTableInfo());
                        }
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
                        resetStatus(true);
                        mCurrentCompleteTableInfo = mUploadInfos.get(currIndex).getCompleteTableInfo();
                        if (mCurrentCompleteTableInfo != null) {
                            tv_error_correct.setVisibility(View.VISIBLE);
                        }
                        if (mUploadInfos.get(currIndex).getModifiedFacilities() != null) {
                            showBottomSheet(mUploadInfos.get(currIndex).getModifiedFacilities(), mUploadInfos.get(currIndex).getCompleteTableInfo());
                        } else if (mUploadInfos.get(currIndex).getUploadedFacilities() != null) {
                            showBottomSheet(mUploadInfos.get(currIndex).getUploadedFacilities(), mUploadInfos.get(currIndex).getCompleteTableInfo());
                        }

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
                    if (!visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.FEEDBACK_LAYER_NAME)) {
                        //不可见
                        //  myUploadIv.setImageResource(R.drawable.ic_upload_data_normal2);
//                        myUploadIv.setChecked(false);
//                        tv_my_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.invisible_state_text_color, null));
                        ifMyUploadLayerVisible = false;
                    } else if (visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.FEEDBACK_LAYER_NAME)) {
                        //可见
                        // myUploadIv.setImageResource(R.drawable.ic_upload_data_pressed);
//                        myUploadIv.setChecked(true);
//                        tv_my_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                        ifMyUploadLayerVisible = true;
                    }
//                    else if (visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.FEEDBACK_LAYER_NAME)) {
//                        ifFeedBackLayerVisible = true;
//                    } else if (!visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.FEEDBACK_LAYER_NAME)) {
//                        ifFeedBackLayerVisible = false;
//
//                    }
                }
            });
        }

        ll_reset = view.findViewById(R.id.ll_reset);
        ll_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstModifiedFacility != null) {
                    mUploadInfos.clear();
                    nextAndPrevContainer.setVisibility(View.GONE);
                    btn_next.setVisibility(View.GONE);
                    btn_prev.setVisibility(View.GONE);
                    currIndex = 0;
                    mCurrentModifiedFacility = null;
                    mCurrentUploadedFacility = null;
                    mCurrentCompleteTableInfo = null;
                    hasComponent = false;
                    tv_error_correct.setVisibility(View.VISIBLE);
                    resetStatus(true);
                    tableItemContainer.removeAllViews();
                    approvalOpinionContainer.removeAllViews();
                    component_detail_container.removeAllViews();
                    mCurrentCompleteTableInfo = firstCompleteTableInfo;
                    showBottomSheet(firstModifiedFacility, firstCompleteTableInfo);
                } else if (firstUploadedFacility != null) {

                    mUploadInfos.clear();
                    nextAndPrevContainer.setVisibility(View.GONE);
                    btn_next.setVisibility(View.GONE);
                    btn_prev.setVisibility(View.GONE);
                    currIndex = 0;
                    mCurrentModifiedFacility = null;
                    mCurrentUploadedFacility = null;
                    mCurrentCompleteTableInfo = null;
                    hasComponent = false;
                    tv_error_correct.setVisibility(View.VISIBLE);
                    resetStatus(true);
                    tableItemContainer.removeAllViews();
                    approvalOpinionContainer.removeAllViews();
                    component_detail_container.removeAllViews();
                    mCurrentCompleteTableInfo = firstCompleteTableInfo;
                    showBottomSheet(firstUploadedFacility, firstCompleteTableInfo);
                }
            }
        });

        initBottomSheetView();


        //再次编辑按钮
        btnReEdit = view.findViewById(R.id.btn_reedit);
        RxView.clicks(btnReEdit)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mCurrentModifiedFacility != null) {
                            Intent intent = new Intent(mContext, ReEditModifiedFacilityActivity.class);
                            intent.putExtra("data", mCurrentModifiedFacility);
                            startActivity(intent);
                        } else if (mCurrentUploadedFacility != null) {
                            Intent intent = new Intent(mContext, ReEditUploadFacilityActivity.class);
                            intent.putExtra("data", (Parcelable) mCurrentUploadedFacility);
                            startActivity(intent);
                        }
                    }
                });

        //删除按钮
        btnDelete = view.findViewById(R.id.btn_delete);
        RxView.clicks(btnDelete)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        DialogUtil.MessageBox(mContext, "提醒", "是否确定要删除这条记录？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteFacility();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }
                });

        ll_feedback = view.findViewById(R.id.ll_feedback);

        btnFeedback = view.findViewById(R.id.btn_feedback);
        RxView.clicks(btnFeedback)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mCurrentModifiedFacility != null) {
                            if(!StringUtil.isEmpty(mCurrentModifiedFacility.getQDZT()) && !"3".equals(mCurrentModifiedFacility.getQDZT())){
                                showDialogTin(2);
                            }else {
                                Intent intent = new Intent(mContext, FeedbackEventUploadActivity.class);
                                intent.putExtra("modified", mCurrentModifiedFacility);
                                startActivity(intent);
                            }
                        } else if (mCurrentUploadedFacility != null) {
                            if(!StringUtil.isEmpty(mCurrentUploadedFacility.getQDZT()) && !"3".equals(mCurrentUploadedFacility.getQDZT())){
                                showDialogTin(1);
                            }else {
                                Intent intent = new Intent(mContext, FeedbackEventUploadActivity.class);
                                intent.putExtra("upload", (Parcelable) mCurrentUploadedFacility);
                                startActivity(intent);
                            }
                        }
                    }
                });
        llGoFeedBackList = view.findViewById(R.id.ll_go_feedback_list);
        llGoFeedBackList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentModifiedFacility != null) {
                    Intent intent = new Intent(mContext, FacilityFeedbackListActivity.class);
                    intent.putExtra("id", mCurrentModifiedFacility.getId().longValue());
                    intent.putExtra("tableType", "1");
                    intent.putExtra("objectid", mCurrentModifiedFacility.getObjectId());
                    startActivity(intent);
                } else if (mCurrentUploadedFacility != null) {
                    Intent intent = new Intent(mContext, FacilityFeedbackListActivity.class);
                    intent.putExtra("id", mCurrentUploadedFacility.getId().longValue());
                    intent.putExtra("tableType", "0");
                    intent.putExtra("objectid", mCurrentUploadedFacility.getObjectId());
                    startActivity(intent);
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

    /**
     * 交办反馈提示（已反馈）
     */
    private void showDialogTin(final int type) {
        MessageBox(mContext, "提示", "该设施已反馈上报，是否继续反馈？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (type == 1) {
                    Intent intent = new Intent(mContext, FeedbackEventUploadActivity.class);
                    intent.putExtra("upload", (Parcelable) mCurrentUploadedFacility);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, FeedbackEventUploadActivity.class);
                    intent.putExtra("modified", mCurrentModifiedFacility);
                    startActivity(intent);
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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
            filterGroupDialog = new FilterGroupDialog(mContext);
        }
        filterGroupDialog.setFilterGroupListener(new FilterGroupListener() {
            @Override
            public void filterByIds(Set<BatchInfo.Info> selectedList) {
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
                .subscribe(new Subscriber<BatchInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(batchState == FIRST_IN){
                            loadMap();
                            batchState = FIRST_IN_NOT_BATCH;
                        }else{
                            showPopMenu(null);
                        }
                    }

                    @Override
                    public void onNext(BatchInfo batchInfo) {
                        mBatchInfo = batchInfo;
                        if (batchInfo != null && !ListUtil.isEmpty(batchInfo.getRows()) && batchInfo.getCode() == 200) {
                            showPopMenu(batchInfo);
                        } else {
                            if(batchState == FIRST_IN){
                                loadMap();
                                batchState = FIRST_IN_NOT_BATCH;
                            }else{
                                showPopMenu(batchInfo);
                            }
                        }
                    }
                });
    }

    private void showPopMenu(BatchInfo info) {
//        if (info == null) {
//            return;
//        }
        if (batchState == FIRST_IN) {
            filterGroupDialog.setSelectFirstInStete(info);
             batchState = FIRST_IN_HAS_BATCH;;
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


    public void showMissedComponent(final UploadedFacility missedIdentification) {
        final double x = missedIdentification.getX();
        final double y = missedIdentification.getY();
        if (missedIdentification.getIsBinding() == 1) {
            queryCompoenntInfo(missedIdentification);
        } else {
            List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
            UploadInfo uploadInfo = new UploadInfo();
            uploadInfo.setUploadedFacilities(missedIdentification);
            uploadInfos.add(uploadInfo);
            showOnBottomSheet(uploadInfos);
        }
    }

    public void queryCompoenntInfo(final UploadedFacility modifiedIdentification) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("查询详细信息中.....");
        progressDialog.show();
        UploadFacilityService identificationService = new UploadFacilityService(mContext);
        identificationService.queryComponent(modifiedIdentification, new Callback2<CompleteTableInfo>() {
            @Override
            public void onSuccess(CompleteTableInfo completeTableInfo) {
                progressDialog.dismiss();
                List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
                UploadInfo uploadInfo = new UploadInfo();
                uploadInfo.setCompleteTableInfo(completeTableInfo);
                uploadInfo.setUploadedFacilities(modifiedIdentification);
                uploadInfos.add(uploadInfo);
                showOnBottomSheet(uploadInfos);
            }

            @Override
            public void onFail(Exception error) {
                progressDialog.dismiss();
                List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
                UploadInfo uploadInfo = new UploadInfo();
                uploadInfo.setUploadedFacilities(modifiedIdentification);
                uploadInfos.add(uploadInfo);
                showOnBottomSheet(uploadInfos);
                // ll_modified.setVisibility(View.GONE);
//                if (mContext != null) {
//                    ToastUtil.shortToast(mContext.getApplicationContext(), "查询失败");
//                }
            }
        });
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
                    layerFactory = new FeedBackLayerFactory(batchList, mContext);
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
        if(context != null) {
            this.mContext = context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity != null){
            this.mContext = activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        locationButton.unregisterSensor();
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

    private void filterData(Set<BatchInfo.Info> selectedList) {

        Callout callout = mMapView.getCallout();
        if(callout!=null && callout.isShowing()){
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
            tableItemContainer.removeAllViews();
            component_detail_container.removeAllViews();
            approvalOpinionContainer.removeAllViews();
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
        nextAndPrevContainer.setVisibility(View.GONE);
        btn_next.setVisibility(View.GONE);
        btn_prev.setVisibility(View.GONE);
        currIndex = 0;
        //        final Point point = mMapView.toMapPoint(x, y);
        final Point point = new Point(x, y);

        mCurrentModifiedFacility = null;
        mCurrentUploadedFacility = null;
        mCurrentCompleteTableInfo = null;
        hasComponent = false;
        tv_error_correct.setVisibility(View.VISIBLE);
        //tv_sure.setVisibility(View.GONE);

        //交办反馈不可见
        if (!ifFeedBackLayerVisible) return;
//        mFeedbackLayerService.setQueryByWhere(getQueryWhere() +" and "+ getFilterInfo(batchList));
        if (StringUtil.isEmpty(getFilterInfo(batchList))) {
            mFeedbackLayerService.setQueryByWhere(getDistrictQueryWhere());
        } else {
            mFeedbackLayerService.setQueryByWhere(TextUtils.isEmpty(getDistrictQueryWhere()) ? getFilterInfo(batchList) : getDistrictQueryWhere() + " and " + getFilterInfo(batchList));
        }
        resetStatus(true);
        mFeedbackLayerService.queryUploadDataInfo(point, mMapView.getSpatialReference(), mMapView.getResolution(), new Callback2<List<UploadInfo>>() {
            @Override
            public void onSuccess(List<UploadInfo> uploadInfos) {
                pd.dismiss();
                if (ListUtil.isEmpty(uploadInfos) || (uploadInfos.size() == 1 && uploadInfos.get(0).getUploadedFacilities() == null && uploadInfos.get(0).getModifiedFacilities() == null)) {
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

    /**
     * 点击地图后查询设施
     *
     * @param objectid
     */
    private void queryForObjctId(String objectid) {
        if (pd == null) {
            pd = new ProgressDialog(mContext);
        }
        pd.setMessage("正在查询上报信息...");
        pd.show();
        mUploadInfos.clear();
        nextAndPrevContainer.setVisibility(View.GONE);
        btn_next.setVisibility(View.GONE);
        btn_prev.setVisibility(View.GONE);
        currIndex = 0;

        mCurrentModifiedFacility = null;
        mCurrentUploadedFacility = null;
        mCurrentCompleteTableInfo = null;
        hasComponent = false;
        tv_error_correct.setVisibility(View.VISIBLE);
        //tv_sure.setVisibility(View.GONE);

        //交办反馈不可见
        if (!ifFeedBackLayerVisible) return;
        String where = "";
        if (StringUtil.isEmpty(getFilterInfo(batchList))) {
            where += StringUtil.isEmpty(getDistrictQueryWhere())?("OLDOID="+objectid):(getDistrictQueryWhere()+" and OLDOID="+objectid);
        } else {
            where +=TextUtils.isEmpty(getDistrictQueryWhere()) ? getFilterInfo(batchList)+" and OLDOID="+objectid : getDistrictQueryWhere() + " and " + getFilterInfo(batchList)+" and OLDOID="+objectid;
        }
        mFeedbackLayerService.setQueryByWhere(where);
        resetStatus(true);
        mFeedbackLayerService.queryUploadDataForObjectId(new Callback2<List<UploadInfo>>() {
            @Override
            public void onSuccess(List<UploadInfo> uploadInfos) {
                pd.dismiss();
                if (ListUtil.isEmpty(uploadInfos) || (uploadInfos.size() == 1 && uploadInfos.get(0).getUploadedFacilities() == null && uploadInfos.get(0).getModifiedFacilities() == null)) {
                    initGLayer();
                    if (map_bottom_sheet.getVisibility() != View.GONE) {
                        map_bottom_sheet.setVisibility(View.GONE);
                    }
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

    private String getFilterInfo(Set<BatchInfo.Info> batchList1) {
        if (batchList1 == null || batchList1.size() == 0) return " QDBH = -1 ";
        StringBuilder sb = new StringBuilder("  QDBH in ( ");
        for (BatchInfo.Info info : batchList1) {
            sb.append("'").append(info.getAssignId()).append("'").append(",");
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

    private void showOnBottomSheet(List<UploadInfo> uploadInfos) {
        if (uploadInfos.size() > 1) {
            nextAndPrevContainer.setVisibility(View.VISIBLE);
            btn_next.setVisibility(View.VISIBLE);
        }
        if (mMapView.getCallout().isShowing()) {
            mMapView.getCallout().animatedHide();
        }

        component_detail_container.removeAllViews();
        tableItemContainer.removeAllViews();
        approvalOpinionContainer.removeAllViews();

        //隐藏marker
        locationMarker.setVisibility(View.GONE);
        initGLayer();
        Geometry geometry = null;
        mCurrentCompleteTableInfo = uploadInfos.get(0).getCompleteTableInfo();
        if (uploadInfos.get(0).getUploadedFacilities() != null) {
            geometry = new Point(uploadInfos.get(0).getUploadedFacilities().getX(), uploadInfos.get(0).getUploadedFacilities().getY());
            showBottomSheet(uploadInfos.get(0).getUploadedFacilities(), uploadInfos.get(0).getCompleteTableInfo());
        } else if (uploadInfos.get(0).getModifiedFacilities() != null) {

            showBottomSheet(uploadInfos.get(0).getModifiedFacilities(), uploadInfos.get(0).getCompleteTableInfo());
        }
//        if (geometry != null) {
//            drawGeometry(geometry, mGLayer, true, true);
//        }
    }

    /**
     * 纠错数据
     * 加载设施信息，显示中底部BottomSheet中
     */
    private void showBottomSheet(final ModifiedFacility modifiedFacility, CompleteTableInfo completeTableInfo) {
        //initGLayer();
        if (modifiedFacility == null) {
            return;
        }

        if (!StringUtil.isEmpty(modifiedFacility.getQDBH())) {
            map_bottom_sheet.findViewById(R.id.feedback_qdbh).setVisibility(View.VISIBLE);
            ((TextView) map_bottom_sheet.findViewById(R.id.feedback_qdbh)).setText("第" + modifiedFacility.getQDBH() + "批次的设施数据");
        } else {
            map_bottom_sheet.findViewById(R.id.feedback_qdbh).setVisibility(View.GONE);
        }
        /**
         * “未审核”和“存在疑问”可以编辑与删除，“审核通过”不允许编辑和删除
         */
        if (CheckState.UNCHECK.indexOf(modifiedFacility.getCheckState())!=-1 ||
                CheckState.IN_DOUBT.indexOf(modifiedFacility.getCheckState())!=-1) {
//            btnReEdit.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(modifiedFacility.getCheckState()) && modifiedFacility.getId() != -1) {
            //当还未插入到设施图层时也允许删除
//            btnReEdit.setVisibility(View.VISIBLE);
        } else {
            btnReEdit.setVisibility(View.GONE);
        }

        /**
         * “未审核”和“存在疑问”可以编辑与删除，“审核通过”不允许编辑和删除
         */
        if (!CheckState.SUCCESS.equals(modifiedFacility.getCheckState()) && modifiedFacility.getId() != -1) {
//            btnDelete.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(modifiedFacility.getCheckState()) && modifiedFacility.getId() != -1) {
            //当还未插入到设施图层时也允许删除
//            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        /**
         * 审核通过后允许反馈
         */
        if (CheckState.SUCCESS.equals(modifiedFacility.getCheckState()) && modifiedFacility.getId() != -1) {
            ll_feedback.setVisibility(View.VISIBLE);
            btnFeedback.setVisibility(View.VISIBLE);
//            llGoFeedBackList.setVisibility(View.VISIBLE);
            /*if(feedbackFacilityService == null){
                feedbackFacilityService = new FeedbackFacilityService(mContext);
            }
            feedbackFacilityService.getFeedbackInfos(modifiedFacility.getId(), "1")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Result2<List<FeedbackInfo>>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            llGoFeedBackList.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(Result2<List<FeedbackInfo>> listResult2) {
                            if(listResult2 != null
                                    && !ListUtil.isEmpty(listResult2.getData())){
                                llGoFeedBackList.setVisibility(View.VISIBLE);
                            }
                        }
                    });*/
        } else {
//            ll_feedback.setVisibility(View.GONE);
//            btnFeedback.setVisibility(View.GONE);
//            llGoFeedBackList.setVisibility(View.GONE);
        }

        component_detail_container.setVisibility(View.VISIBLE);

        if (component_detail_container.getChildCount() == 0) {

            /**
             * 是否显示原部件
             */
            if (mCurrentCompleteTableInfo != null) {
                tv_error_correct.setVisibility(View.VISIBLE);
            } else {
                tv_error_correct.setVisibility(View.GONE);
            }


            /**
             * 是否显示审批意见
             */
//            if (ListUtil.isEmpty(modifiedFacility.getApprovalOpinions())) {
//                tv_approval_opinion_list.setVisibility(View.GONE);
//            } else {
//                tv_approval_opinion_list.setVisibility(View.VISIBLE);
//            }

            mCurrentModifiedFacility = modifiedFacility;
            Geometry geometry = null;
            /**
             * 如果getX,getY不为空，说明修改过位置，那么默认用修改后的位置（依据：上报图层显示方式）
             */
            if (modifiedFacility.getX() == 0 || modifiedFacility.getY() == 0) {
                geometry = new Point(modifiedFacility.getOriginX(), modifiedFacility.getOriginY());
            } else {
                geometry = new Point(modifiedFacility.getX(), modifiedFacility.getY());
            }

            //Geometry geometry = new Point(modifiedFacility.getOriginX(), modifiedFacility.getOriginY());
            initGLayer();
            drawGeometry(geometry, mGLayer, true, true);
            hasComponent = true;
            map_bottom_sheet.setVisibility(View.VISIBLE);
            //component_detail_container.removeAllViews();
            MyFeedbackTableViewManager modifiedIdentificationTableViewManager = new MyFeedbackTableViewManager(mContext,
                    modifiedFacility, true);
            modifiedIdentificationTableViewManager.addTo(component_detail_container);
            if (completeTableInfo != null) {
                modifiedIdentificationTableViewManager.setReadOnly(modifiedFacility, completeTableInfo.getAttrs());
            } else {
                modifiedIdentificationTableViewManager.setReadOnly(modifiedFacility, null);
            }
            if (mBehavior.getState() == STATE_COLLAPSED) {
                component_detail_container.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBehavior.setState(com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior.STATE_ANCHOR);
                    }
                }, 200);
            }
        }

        component_detail_ll.setVisibility(View.VISIBLE);
    }

    private void initBottomSheetView() {
        nextAndPrevContainer = (ViewGroup) map_bottom_sheet.findViewById(R.id.ll_next_and_prev_container);

        //上报信息
        tv_sure = (TextView) map_bottom_sheet.findViewById(R.id.tv_sure);
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetStatus(true);
                component_detail_container.setVisibility(View.VISIBLE);
                tableItemContainer.setVisibility(View.GONE);
                showBottomSheet(mCurrentModifiedFacility, mCurrentCompleteTableInfo);

            }
        });

        //原部件按钮
        tv_error_correct = (TextView) map_bottom_sheet.findViewById(R.id.tv_error_correct);
        tv_error_correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetStatus(false);
                component_detail_container.setVisibility(View.GONE);
                tableItemContainer.setVisibility(View.VISIBLE);
                if (mCurrentCompleteTableInfo != null) {
                    showBottomSheet(mCurrentCompleteTableInfo);
                }
            }
        });

        tv_approval_opinion_list = (TextView) map_bottom_sheet.findViewById(R.id.tv_approval_opinion_list);
        tv_approval_opinion_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStatus(false, true);
                component_detail_container.setVisibility(View.GONE);
                tableItemContainer.setVisibility(View.GONE);
                approvalOpinionContainer.setVisibility(View.VISIBLE);
                if (mCurrentModifiedFacility != null) {
                    showBottomSheet(mCurrentModifiedFacility.getId(), mCurrentModifiedFacility.getReportType());
                } else if (mCurrentUploadedFacility != null) {
                    showBottomSheet(mCurrentUploadedFacility.getId(), UploadLayerFieldKeyConstant.ADD);
                }

            }
        });
        tv_approval_opinion_list.setVisibility(View.VISIBLE);
    }

    private void showBottomSheet(Long markId, String reportType) {
        if (approvalOpinionContainer.getChildCount() == 0) {
            ApprovalOpinionViewManager myModifiedFacilityTableViewManage = new ApprovalOpinionViewManager(mContext, markId, reportType);
            myModifiedFacilityTableViewManage.addTo(approvalOpinionContainer);
        }
    }

    private void resetStatus(boolean reset) {
        resetStatus(reset, false);
    }


    private void resetStatus(boolean reset, boolean ifShowApprovalList) {

        if (reset) {
            highlight(tv_sure);
            cancelHighlight(tv_error_correct);
            cancelHighlight(tv_approval_opinion_list);
        } else if (ifShowApprovalList) {
            highlight(tv_approval_opinion_list);
            cancelHighlight(tv_error_correct);
            cancelHighlight(tv_sure);
        } else {
            cancelHighlight(tv_sure);
            highlight(tv_error_correct);
            cancelHighlight(tv_approval_opinion_list);
        }
    }

    /**
     * 高亮tab文字
     *
     * @param textView
     */
    private void highlight(TextView textView) {
        textView.setBackground(getResources().getDrawable(R.drawable.round_blue_rectangle));
        textView.setTextColor(getResources().getColor(R.color.agmobile_white));
    }

    /**
     * 取消高亮tab文字
     *
     * @param textView
     */
    private void cancelHighlight(TextView textView) {
        textView.setBackground(getResources().getDrawable(R.drawable.round_grey_rectangle));
        textView.setTextColor(getResources().getColor(R.color.agmobile_blue));
    }


    /**
     * 新增数据
     * 加载设施信息，显示中底部BottomSheet中
     */
    private void showBottomSheet(final UploadedFacility uploadedFacility, CompleteTableInfo completeTableInfo) {
        mCurrentModifiedFacility = null;
        //initGLayer();
        if (uploadedFacility == null) {
            return;
        }
        mCurrentUploadedFacility = uploadedFacility;
        if (!StringUtil.isEmpty(uploadedFacility.getQDBH())) {
            map_bottom_sheet.findViewById(R.id.feedback_qdbh).setVisibility(View.VISIBLE);
            ((TextView) map_bottom_sheet.findViewById(R.id.feedback_qdbh)).setText("第" + uploadedFacility.getQDBH() + "批次的设施数据");
        } else {
            map_bottom_sheet.findViewById(R.id.feedback_qdbh).setVisibility(View.GONE);
        }
        /**
         * “未审核”和“存在疑问”可以编辑与删除，“审核通过”不允许编辑和删除
         */
        if (CheckState.UNCHECK.indexOf(uploadedFacility.getCheckState())!=-1 ||
                CheckState.IN_DOUBT.indexOf(uploadedFacility.getCheckState())!=-1) {
//            btnReEdit.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(uploadedFacility.getCheckState()) && uploadedFacility.getId() != -1) {
//            btnReEdit.setVisibility(View.VISIBLE);
        } else {
            btnReEdit.setVisibility(View.GONE);
        }


        /**
         * “未审核”和“存在疑问”可以编辑与删除，“审核通过”不允许编辑和删除
         */
        if (!CheckState.SUCCESS.equals(uploadedFacility.getCheckState()) && uploadedFacility.getId() != -1) {
//            btnDelete.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(uploadedFacility.getCheckState()) && uploadedFacility.getId() != -1) {
//            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        /**
         * 审核通过后允许反馈
         */
        if (CheckState.SUCCESS.equals(uploadedFacility.getCheckState()) && uploadedFacility.getId() != -1) {
            ll_feedback.setVisibility(View.VISIBLE);
            btnFeedback.setVisibility(View.VISIBLE);
//            llGoFeedBackList.setVisibility(View.VISIBLE);
            /*if(feedbackFacilityService == null){
                feedbackFacilityService = new FeedbackFacilityService(mContext);
            }
            feedbackFacilityService.getFeedbackInfos(uploadedFacility.getId(), "0")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Result2<List<FeedbackInfo>>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            llGoFeedBackList.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(Result2<List<FeedbackInfo>> listResult2) {
                            if(listResult2 != null
                                    && !ListUtil.isEmpty(listResult2.getData())){
                                llGoFeedBackList.setVisibility(View.VISIBLE);
                            }
                        }
                    });*/
        } else {
//            ll_feedback.setVisibility(View.GONE);
//            btnFeedback.setVisibility(View.GONE);
//            llGoFeedBackList.setVisibility(View.GONE);
        }

        /**
         * 上报信息按钮
         */

        if (uploadedFacility.getIsBinding() == 1 && mCurrentCompleteTableInfo != null) {
            tv_error_correct.setVisibility(View.VISIBLE);
            hasComponent = true;
        } else {
            tv_error_correct.setVisibility(View.GONE);
            hasComponent = false;
        }


        /**
         * 是否显示审批意见
         */
//        if (ListUtil.isEmpty(uploadedFacility.getApprovalOpinions())) {
//            tv_approval_opinion_list.setVisibility(View.GONE);
//        } else {
//            tv_approval_opinion_list.setVisibility(View.VISIBLE);
//        }

        map_bottom_sheet.setVisibility(View.VISIBLE);
        component_detail_container.setVisibility(View.VISIBLE);

        if (component_detail_container.getChildCount() == 0) {
            initGLayer();
            Geometry geometry = new Point(uploadedFacility.getX(), uploadedFacility.getY());
            drawGeometry(geometry, mGLayer, true, true);
            UploadFeedbackTableViewManager modifiedIdentificationTableViewManager = null;
            if (completeTableInfo != null) {
                modifiedIdentificationTableViewManager = new UploadFeedbackTableViewManager(mContext,
                        uploadedFacility, completeTableInfo.getAttrs());
            } else {
                modifiedIdentificationTableViewManager = new UploadFeedbackTableViewManager(mContext,
                        uploadedFacility);
            }
            modifiedIdentificationTableViewManager.addTo(component_detail_container);
            if (mBehavior.getState() == STATE_COLLAPSED) {
                component_detail_container.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBehavior.setState(STATE_ANCHOR);
                    }
                }, 200);
            }
        }

        //component_detail_container.removeAllViews();

        component_detail_ll.setVisibility(View.VISIBLE);
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
    private void showBottomSheet(final CompleteTableInfo CompleteTableInfo) {

        mCurrentCompleteTableInfo = CompleteTableInfo;
        //tableItemContainer.removeAllViews();
        if (CompleteTableInfo.getAttrs() == null) {
            return;
        }

        if (tableItemContainer.getChildCount() > 0) {
            return;
        }
//        String usid = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.USID));
        //设施
//        /**
//         * 如果是雨水口，显示特性：方形
//         */
//        String layertype = "";
//        if (mCurrentModifiedFacility != null) {
//            layertype = mCurrentModifiedFacility.getLayerName();
//        } else if (mCurrentUploadedFacility != null) {
//            layertype = mCurrentUploadedFacility.getLayerName();
//        }
//
//        TextItemTableItem ssTv = new TextItemTableItem(getContext());
//        ssTv.setTextViewName("设施");
//        ssTv.setText(StringUtil.getNotNullString(layertype, "") + "(" + usid + ")");
//        ssTv.setReadOnly();
//        tableItemContainer.addView(ssTv);
//
//        //设施位置
//        String address = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.ADDR));
//        TextItemTableItem addressTv = new TextItemTableItem(getContext());
//        addressTv.setTextViewName("设施位置");
//        address = replaceSpaceCharacter(address);
////        addressTv.setText(StringUtil.getNotNullString(address, "无"));
//        addressTv.setText(StringUtil.getNotNullString(address, ""));
//        addressTv.setReadOnly();
//        tableItemContainer.addView(addressTv);
//
//
//        //所在道路
//        String road = String.valueOf(CompleteTableInfo.getAttrs().get
//                (ComponentFieldKeyConstant.ROAD));
//        TextItemTableItem roadTv = new TextItemTableItem(getContext());
//        roadTv.setTextViewName("所在道路");
//        road = replaceSpaceCharacter(road);
//        roadTv.setText(StringUtil.getNotNullString(road, ""));
//        roadTv.setReadOnly();
//        tableItemContainer.addView(roadTv);
//
//        if (layertype.equals("窨井")) {
//            String yinjing_type = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.SUBTYPE));
//            String sort = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.SORT));
//            String MATERIAL = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.MATERIAL));
//
//            TextItemTableItem oneAttrItem = new TextItemTableItem(getContext());
//            TextItemTableItem twoAttrItem = new TextItemTableItem(getContext());
//            TextItemTableItem threeAttrItem = new TextItemTableItem(getContext());
//
//            oneAttrItem.setTextViewName("窨井类型");
//            yinjing_type = replaceSpaceCharacter(yinjing_type);
//            oneAttrItem.setText(StringUtil.getNotNullString(yinjing_type, ""));
//            oneAttrItem.setReadOnly();
//
//            twoAttrItem.setTextViewName("雨污类别");
//            sort = replaceSpaceCharacter(sort);
//            twoAttrItem.setText(StringUtil.getNotNullString(sort, ""));
//            twoAttrItem.setReadOnly();
//
//            threeAttrItem.setTextViewName("井盖材质");
//            MATERIAL = replaceSpaceCharacter(MATERIAL);
//            threeAttrItem.setText(StringUtil.getNotNullString(MATERIAL, ""));
//            threeAttrItem.setReadOnly();
//
//            tableItemContainer.addView(oneAttrItem);
//            tableItemContainer.addView(twoAttrItem);
//            tableItemContainer.addView(threeAttrItem);
//
//        } else if (layertype.equals("排水口")) {
//            TextItemTableItem oneAttrItem = new TextItemTableItem(getContext());
//            TextItemTableItem twoAttrItem = new TextItemTableItem(getContext());
//            String sort = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.SORT));
//            String direction = String.valueOf(CompleteTableInfo.getAttrs().get
//                    (ComponentFieldKeyConstant.RIVER));
//
//            oneAttrItem.setTextViewName("排放去向");
//            direction = replaceSpaceCharacter(direction);
//            oneAttrItem.setText(StringUtil.getNotNullString(direction, ""));
//            oneAttrItem.setReadOnly();
//
//            twoAttrItem.setTextViewName("雨污类别");
//            sort = replaceSpaceCharacter(sort);
//            twoAttrItem.setText(StringUtil.getNotNullString(sort, ""));
//            twoAttrItem.setReadOnly();
//
//            tableItemContainer.addView(oneAttrItem);
//            tableItemContainer.addView(twoAttrItem);
//
//        } else if (layertype.equals("雨水口")) {
//            String feature = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.FEATURE));
//            String style = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.STYLE));
//            TextItemTableItem oneAttrItem = new TextItemTableItem(getContext());
//            TextItemTableItem twoAttrItem = new TextItemTableItem(getContext());
//
//            oneAttrItem.setTextViewName("特征");
//            feature = replaceSpaceCharacter(feature);
//            oneAttrItem.setText(StringUtil.getNotNullString(feature, ""));
//            oneAttrItem.setReadOnly();
//
//            twoAttrItem.setTextViewName("形式");
//            style = replaceSpaceCharacter(style);
//            twoAttrItem.setText(StringUtil.getNotNullString(style, ""));
//            twoAttrItem.setReadOnly();
//
//            tableItemContainer.addView(oneAttrItem);
//            tableItemContainer.addView(twoAttrItem);
//        }
//
//        //权属单位
//        String parentOrg = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.OWNERDEPT));
//        TextItemTableItem quanshuTv = new TextItemTableItem(getContext());
//        quanshuTv.setTextViewName("权属单位");
//        parentOrg = replaceSpaceCharacter(parentOrg);
//        quanshuTv.setText(StringUtil.getNotNullString(parentOrg, ""));
//        quanshuTv.setReadOnly();
//        tableItemContainer.addView(quanshuTv);
//        //已挂牌编号
//        TextItemTableItem bianhaoTv = new TextItemTableItem(getContext());
//        bianhaoTv.setTextViewName("已挂牌编号");
//        bianhaoTv.setReadOnly();
//        String codeValue = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.CODE));
//        codeValue = codeValue.trim();
//        if (layertype.equals("窨井")) {
//            if (!codeValue.isEmpty()) {
//                bianhaoTv.setText(StringUtil.getNotNullString(codeValue, ""));
//            } else {
//                bianhaoTv.setText("");
//            }
//            tableItemContainer.addView(bianhaoTv);
//        }

        /**
         * 如果是雨水口，显示特性：方形
         */
        String layertype = "";
        if (mCurrentModifiedFacility != null) {
            layertype = mCurrentModifiedFacility.getLayerName();
        } else if (mCurrentUploadedFacility != null) {
            layertype = mCurrentUploadedFacility.getLayerName();
        }


        if (mCurrentModifiedFacility != null) {
            double originX = mCurrentModifiedFacility.getOriginX();
            double originY = mCurrentModifiedFacility.getOriginY();
            double x = mCurrentModifiedFacility.getX();
            double y = mCurrentModifiedFacility.getY();
            initGLayerForOriginLocation();
            new OriginalAttributesViewManager(mContext,
                    tableItemContainer, mCurrentCompleteTableInfo, mMapView, originX, originY, layertype, mGLayerForOriginLocation);
            return;
        }
        new OriginalAttributesViewManager(mContext,
                tableItemContainer, mCurrentCompleteTableInfo, mMapView, 0, 0, layertype, mGLayerForOriginLocation);
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


        if (uploadFacilitySuccessEvent.getModifiedFacility() != null) {

            final ModifiedFacility
                    modifiedFacility = uploadFacilitySuccessEvent.getModifiedFacility();
            //进行查询图片，因为如果再次编辑的时候增加了图片，如果再次编辑这样图片的时候需要这张图片在服务端的id
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("正在更新设施信息");
            progressDialog.show();

            CorrectFacilityService correctFacilityService = new CorrectFacilityService(mContext);
            correctFacilityService.getModificationById(modifiedFacility.getId())
//                    .map(new Func1<ServerAttachment, ModifiedFacility>() {
//                        @Override
//                        public ModifiedFacility call(ServerAttachment serverIdentificationAttachment) {
//                            List<ServerAttachment.ServerAttachmentDataBean> data = serverIdentificationAttachment.getData();
//                            if (!ListUtil.isEmpty(data)) {
//                                List<Photo> photos = new ArrayList<>();
//                                for (ServerAttachment.ServerAttachmentDataBean dataBean : data) {
//                                    Photo photo = new Photo();
//                                    photo.setId(Long.valueOf(dataBean.getId()));
//                                    photo.setPhotoPath(dataBean.getAttPath());
//                                    photo.setThumbPath(dataBean.getThumPath());
//                                    photos.add(photo);
//                                }
//                                modifiedFacility.setPhotos(photos);
//                            }
//                            return modifiedFacility;
//                        }
//                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModifiedFacility>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            mUploadInfos.clear();
                            nextAndPrevContainer.setVisibility(View.GONE);
                            btn_next.setVisibility(View.GONE);
                            btn_prev.setVisibility(View.GONE);
                            currIndex = 0;
                            mCurrentModifiedFacility = null;
                            mCurrentUploadedFacility = null;
                            mCurrentCompleteTableInfo = null;
                            hasComponent = false;
                            tv_error_correct.setVisibility(View.VISIBLE);
                            resetStatus(true);
                            if (modifiedFacility != null) {
                                CompleteTableInfo completeTableInfo = CompleteTableInfoUtil.getCompleteTableInfo(modifiedFacility);
                                mCurrentCompleteTableInfo = completeTableInfo;
                            }
                            component_detail_container.removeAllViews();
                            tableItemContainer.removeAllViews();
                            approvalOpinionContainer.removeAllViews();
                            showBottomSheet(modifiedFacility, mCurrentCompleteTableInfo);
                            /**
                             * 判断是否是通过列表点击进来的数据，如果是，那么更新
                             */
                            refreshFirstModifiedFacility(modifiedFacility);
                        }

                        @Override
                        public void onNext(ModifiedFacility modifiedFacility) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            mUploadInfos.clear();
                            nextAndPrevContainer.setVisibility(View.GONE);
                            btn_next.setVisibility(View.GONE);
                            btn_prev.setVisibility(View.GONE);
                            currIndex = 0;
                            mCurrentModifiedFacility = null;
                            mCurrentUploadedFacility = null;
                            mCurrentCompleteTableInfo = null;
                            hasComponent = false;
                            tv_error_correct.setVisibility(View.VISIBLE);
                            resetStatus(true);
                            if (uploadFacilitySuccessEvent.getModifiedFacility() != null) {
                                CompleteTableInfo completeTableInfo = CompleteTableInfoUtil.getCompleteTableInfo(modifiedFacility);
                                mCurrentCompleteTableInfo = completeTableInfo;
                            }
                            component_detail_container.removeAllViews();
                            tableItemContainer.removeAllViews();
                            approvalOpinionContainer.removeAllViews();
                            showBottomSheet(modifiedFacility, mCurrentCompleteTableInfo);
                            /**
                             * 判断是否是通过列表点击进来的数据，如果是，那么更新
                             */
                            refreshFirstModifiedFacility(modifiedFacility);
                        }
                    });
        } else if (uploadFacilitySuccessEvent.getUploadedFacility() != null) {
            final UploadedFacility uploadedFacility = uploadFacilitySuccessEvent.getUploadedFacility();
            //进行查询图片，因为如果再次编辑的时候增加了图片，如果再次编辑这样图片的时候需要这张图片在服务端的id
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("正在更新设施信息");
            progressDialog.show();

            UploadFacilityService uploadFacilityService = new UploadFacilityService(mContext);
            uploadFacilityService.getUploadFacilityById(uploadedFacility.getId())
//                    .map(new Func1<ServerAttachment, UploadedFacility>() {
//                        @Override
//                        public UploadedFacility call(ServerAttachment serverIdentificationAttachment) {
//                            List<ServerAttachment.ServerAttachmentDataBean> data = serverIdentificationAttachment.getData();
//                            if (!ListUtil.isEmpty(data)) {
//                                List<Photo> photos = new ArrayList<>();
//                                for (ServerAttachment.ServerAttachmentDataBean dataBean : data) {
//                                    Photo photo = new Photo();
//                                    photo.setId(Long.valueOf(dataBean.getId()));
//                                    photo.setPhotoPath(dataBean.getAttPath());
//                                    photo.setThumbPath(dataBean.getThumPath());
//                                    photos.add(photo);
//                                }
//                                uploadedFacility.setPhotos(photos);
//                            }
//                            return uploadedFacility;
//                        }
//                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UploadedFacility>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            mUploadInfos.clear();
                            nextAndPrevContainer.setVisibility(View.GONE);
                            btn_next.setVisibility(View.GONE);
                            btn_prev.setVisibility(View.GONE);
                            currIndex = 0;
                            mCurrentModifiedFacility = null;
                            mCurrentUploadedFacility = null;
                            mCurrentCompleteTableInfo = null;
                            hasComponent = false;
                            tv_error_correct.setVisibility(View.VISIBLE);
                            resetStatus(true);
                            uploadedFacility.getPhotos().clear();
                            showMissedComponent(uploadedFacility);
                            /**
                             * 判断是否是通过点击进来的数据，如果是，那么更新
                             */
                            refreshFirstUploadFacility(uploadedFacility);
                        }

                        @Override
                        public void onNext(UploadedFacility uploadedFacility1) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            mUploadInfos.clear();
                            nextAndPrevContainer.setVisibility(View.GONE);
                            btn_next.setVisibility(View.GONE);
                            btn_prev.setVisibility(View.GONE);
                            currIndex = 0;
                            mCurrentModifiedFacility = null;
                            mCurrentUploadedFacility = null;
                            mCurrentCompleteTableInfo = null;
                            hasComponent = false;
                            tv_error_correct.setVisibility(View.VISIBLE);
                            resetStatus(true);

                            showMissedComponent(uploadedFacility1);
                            /**
                             * 判断是否是通过点击进来的数据，如果是，那么更新
                             */
                            refreshFirstUploadFacility(uploadedFacility1);
                        }
                    });

        } else {
            initGLayer();
            hideBottomSheet();
        }

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
