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

package com.augurit.agmobile.gzps.uploadfacility.view.facilitydetail;

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
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MapHelper;
import com.augurit.agmobile.gzps.common.OnInitLayerUrlFinishEvent;
import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.selectcomponent.LimitedLayerAdapter;
import com.augurit.agmobile.gzps.common.util.Attachment2PhotoUtil;
import com.augurit.agmobile.gzps.componentmaintenance.ComponetListAdapter;
import com.augurit.agmobile.gzps.jbjpsdy.WellMonitorRefreshEvent;
import com.augurit.agmobile.gzps.journal.service.JournalService;
import com.augurit.agmobile.gzps.monitor.InspectionWellMonitorEditOrDetailActivity;
import com.augurit.agmobile.gzps.monitor.WellMonitorActivity;
import com.augurit.agmobile.gzps.monitor.model.InspectionWellMonitorInfo;
import com.augurit.agmobile.gzps.monitor.service.InspectionWellMonitorService;
import com.augurit.agmobile.gzps.uploadfacility.model.CheckState;
import com.augurit.agmobile.gzps.uploadfacility.model.CompleteTableInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.PsdyJbj;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.service.CorrectFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.DeleteFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.FeedbackFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadDetailLayerService;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadLayerService;
import com.augurit.agmobile.gzps.uploadfacility.util.CompleteTableInfoUtil;
import com.augurit.agmobile.gzps.uploadfacility.util.UploadLayerFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadfacility.view.approvalopinion.ApprovalOpinionViewManager;
import com.augurit.agmobile.gzps.uploadfacility.view.approvalopinion.InspectionOpinionViewManager;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.MyModifiedFacilityTableViewManager;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.RefreshMyModificationListEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.feedback.FacilityFeedbackActivity;
import com.augurit.agmobile.gzps.uploadfacility.view.feedback.FacilityFeedbackListActivity;
import com.augurit.agmobile.gzps.uploadfacility.view.reeditfacility.ReEditModifiedFacilityActivity;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.RefreshMyUploadList;
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
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.widget.LocationButton;
import com.augurit.agmobile.patrolcore.editmap.widget.LocationMarker;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView2;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.DensityUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior.STATE_ANCHOR;
import static com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior.STATE_COLLAPSED;
import static com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior.STATE_EXPANDED;

/**
 * ????????????????????????
 *
 * @author ????????? ???huangchongwu
 * @version 1.0
 * @package ?????? ???com.augurit.agmobile.gzps
 * @createTime ???????????? ???2020-09-16
 */
public class UploadKeyNodeMonitorDetailMapFragment extends Fragment implements View.OnClickListener {

    private static final String KEY_MAP_STATE = "com.esri.MapState";

    private LocationMarker locationMarker;
    //?????????????????????????????????

    private boolean isFirst = true;
    //????????????????????????????????????????????????????????????????????????
    private boolean isNeedRefresh = false;
    private ModifiedFacility mCurrentModifiedFacility;
    private InspectionWellMonitorInfo mCurrentUploadedFacility;
    private CompleteTableInfo mCurrentCompleteTableInfo;

    /**
     * ????????????????????????????????????????????????????????????????????????????????????
     */
    private boolean ifFirstLocate = true;
    private LocationButton locationButton;
    private LegendPresenter legendPresenter;
    View mView;

    MapView mMapView;
    private  ProgressDialog progressDialog;
    private ILayerView layerView;

    private PatrolLayerPresenter layerPresenter;
    private boolean loadLayersSuccess = true;

    private View ll_layer_url_init_fail;
    private TextView show_all_layer;
    private GridView gridView;
    private LimitedLayerAdapter layerAdapter;
    //????????????????????????????????????????????????????????????URL
    private String currComponentUrl = LayerUrlConstant.newComponentUrls[0];

    ProgressDialog pd;
    ViewGroup map_bottom_sheet;
    AnchorSheetBehavior mBehavior;
    private ComponetListAdapter componetListAdapter;

    private ViewGroup component_detail_ll;
    /**
     * ?????????????????????????????????????????????TableViewManager
     */
    private ViewGroup component_detail_container;

    private ArrayList<TableItem> tableItems = null;
    private ArrayList<Photo> photoList = new ArrayList<>();
    private String projectId;
    private TableViewManager tableViewManager;
    //????????????????????????
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
    private List<PsdyJbj> mPsdyKeyNodeInspectionWells;

    /**
     * ???????????????????????????
     */
    private SelectLocationTouchListener defaultMapOnTouchListener;
    private int bottomHeight;
    private int bottomMargin;
    private TextView tv_error_correct;
    private TextView tv_sure;
    private List<UploadedFacility> mUploadedFacilitys;
    private Component mCurrentComponent;
    private UploadLayerService mUploadLayerService;
    private Context mContext;
    private ViewGroup nextAndPrevContainer;
    private ViewGroup tableItemContainer;
    private MapHelper mapHelper;
    private View myUploadLayerBtn;
    private View uploadLayerBtn;
    private boolean ifUploadLayerVisible = false;
    private boolean ifMyUploadLayerVisible = false;

    private InspectionWellMonitorInfo firstUploadedFacility;
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
    private CorrectFacilityService mIdentificationService;
    /**
     * ???????????????????????????
     */
    private GraphicsLayer mGLayerForOriginLocation;
    private static int isShowCancelDeleteButton;
    private GraphicsLayer mGraphicSelectLayer = null;

    public static UploadKeyNodeMonitorDetailMapFragment getInstance(Bundle data) {
        UploadKeyNodeMonitorDetailMapFragment addComponentFragment2 = new UploadKeyNodeMonitorDetailMapFragment();
        addComponentFragment2.setArguments(data);
        isShowCancelDeleteButton = data.getInt("isShowCancelDeleteButton",0);
        return addComponentFragment2;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(mContext, R.layout.fragment_uploadmap_distribute, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        mPsdyKeyNodeInspectionWells = new ArrayList<>();
        mIdentificationService = new CorrectFacilityService(mContext);
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
                        if (myUploadLayerBtn != null) {
                            myUploadLayerBtn.performClick();
                        }
                        // layerPresenter.changeLayerVisibility(PatrolLayerPresenter.LOCAL_UPLOAD_LAYER_NAME, true);

                        if (getArguments() != null
                                && getArguments().getParcelable("uploadedFacility") != null) {
                            InspectionWellMonitorInfo wellMonitorInfo = getArguments().getParcelable("uploadedFacility");
                            if (wellMonitorInfo != null) {
                                firstUploadedFacility = wellMonitorInfo;
                                mUploadInfos.clear();
                                nextAndPrevContainer.setVisibility(View.GONE);
                                btn_next.setVisibility(View.GONE);
                                btn_prev.setVisibility(View.GONE);
                                currIndex = 0;
                                mCurrentModifiedFacility = null;
                                mCurrentUploadedFacility = null;
                                mCurrentCompleteTableInfo = null;
                                hasComponent = false;
                                tv_error_correct.setVisibility(View.GONE);
                                resetStatus(true);
                                showBottomSheet(wellMonitorInfo, null);
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
                                tv_error_correct.setVisibility(View.GONE);
                                resetStatus(true);
                                CompleteTableInfo completeTableInfo = CompleteTableInfoUtil.getCompleteTableInfo(modifiedFacility);
                                mCurrentCompleteTableInfo = completeTableInfo;
                                firstCompleteTableInfo = completeTableInfo;
                                showBottomSheet(modifiedFacility, mCurrentCompleteTableInfo);
                            }
                        } else {
                            //??????????????????????????????????????????????????????
                            if (locationButton != null) {
                                // 2
                                locationButton.setStateNormal();
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

        mUploadLayerService = new UploadLayerService(mContext);
        //????????????
        locationMarker = (LocationMarker) view.findViewById(R.id.locationMarker);
        //????????????
        locationButton = (LocationButton) view.findViewById(R.id.locationButton);
        locationButton.setIfShowCallout(false);
        locationButton.setMapView(mMapView);
        locationButton.setOnceLocation(false);
        locationButton.setIfAlwaysCeterToLocation(true);


        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) locationButton
                .getLayoutParams();
        bottomMargin = lp.bottomMargin;
        mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
        /**
         * ??????????????????
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
                            tv_error_correct.setVisibility(View.GONE);
                        }
                        if (mUploadInfos.get(currIndex).getModifiedFacilities() != null) {
                            showBottomSheet(mUploadInfos.get(currIndex).getModifiedFacilities(), mUploadInfos.get(currIndex).getCompleteTableInfo());
                        } else if (mUploadInfos.get(currIndex).getUploadedFacilities() != null) {
//                            showBottomSheet(mUploadInfos.get(currIndex).getUploadedFacilities(), mUploadInfos.get(currIndex).getCompleteTableInfo());
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
                            tv_error_correct.setVisibility(View.GONE);
                        }
                        if (mUploadInfos.get(currIndex).getModifiedFacilities() != null) {
                            showBottomSheet(mUploadInfos.get(currIndex).getModifiedFacilities(), mUploadInfos.get(currIndex).getCompleteTableInfo());
                        } else if (mUploadInfos.get(currIndex).getUploadedFacilities() != null) {
//                            showBottomSheet(mUploadInfos.get(currIndex).getUploadedFacilities(), mUploadInfos.get(currIndex).getCompleteTableInfo());
                        }

                        if (currIndex == 0) {
                            btn_prev.setVisibility(View.GONE);
                        }
                        if (mUploadInfos.size() > 1) {
                            btn_next.setVisibility(View.VISIBLE);
                        }
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
//                    if (!visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.LOCAL_UPLOAD_LAYER_NAME)) {
//                        //?????????
//                        //  myUploadIv.setImageResource(R.drawable.ic_upload_data_normal2);
//                        uploadIv.setChecked(false);
//                        tv_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(),R.color.invisible_state_text_color,null));
//                        ifUploadLayerVisible = false;
//                    } else if (visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.LOCAL_UPLOAD_LAYER_NAME)) {
//                        //??????
//                        // myUploadIv.setImageResource(R.drawable.ic_upload_data_pressed);
//                        uploadIv.setChecked(true);
//                        tv_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(),R.color.colorAccent,null));
//                        ifUploadLayerVisible = true;
//                    }else
                    if (!visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.MY_UPLOAD_LAYER_NAME)) {
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
                    tv_error_correct.setVisibility(View.GONE);
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
                    tv_error_correct.setVisibility(View.GONE);
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


        //??????????????????
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
                            Intent intent = new Intent(mContext, InspectionWellMonitorEditOrDetailActivity.class);
                            InspectionWellMonitorInfo info = mCurrentUploadedFacility;
                            info.setPhotos(Attachment2PhotoUtil.toPhotos(mCurrentUploadedFacility.getAttachments()));
                            intent.putExtra("InspectionWellMonitorInfo", (Serializable) info);
                            intent.putExtra("type", info.getJbjType());
                            intent.putExtra("objectid",info.getJbjObjectId());
                            intent.putExtra("usid",info.getUsid());
                            intent.putExtra("X",info.getJbjX()+"");
                            intent.putExtra("Y",info.getJbjY()+"");
                            intent.putExtra("isFromMap",true);
//                            mContext.startActivity(intent);
                            startActivityForResult(intent,99);
                        }
                    }
                });

        //????????????
        btnDelete = view.findViewById(R.id.btn_delete);
//        RxView.clicks(btnDelete)
//                .throttleFirst(500, TimeUnit.MILLISECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        DialogUtil.MessageBox(mContext, "??????", "????????????????????????????????????", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                deleteFacility();
//                            }
//                        }, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
//                    }
//                });

        ll_feedback = view.findViewById(R.id.ll_feedback);

        btnFeedback = view.findViewById(R.id.btn_feedback);
        RxView.clicks(btnFeedback)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mCurrentModifiedFacility != null) {
                            Intent intent = new Intent(mContext, FacilityFeedbackActivity.class);
                            intent.putExtra("id", mCurrentModifiedFacility.getId().longValue());
                            intent.putExtra("tableType", "1");
                            intent.putExtra("objectid", mCurrentModifiedFacility.getObjectId());
                            startActivity(intent);
                        } else if (mCurrentUploadedFacility != null) {
                            Intent intent = new Intent(mContext, FacilityFeedbackActivity.class);
                            intent.putExtra("id", mCurrentUploadedFacility.getId().longValue());
                            intent.putExtra("tableType", "0");
//                            intent.putExtra("objectid", mCurrentUploadedFacility.getObjectId());
                            startActivity(intent);
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
//                    intent.putExtra("objectid", mCurrentUploadedFacility.getObjectId());
                    startActivity(intent);
                }
            }
        });
    }


    /**
     * ????????????
     */
    private void deleteFacility() {
        initDeleteFacilityService();
        if (mCurrentUploadedFacility != null) {
            if(progressDialog ==null){
                progressDialog = new ProgressDialog(mContext);
            }
            progressDialog.setMessage("???????????????.....");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            deleteFacilityService.deleteFacility(UploadLayerFieldKeyConstant.DELETE, mCurrentUploadedFacility.getId())
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
                            CrashReport.postCatchedException(new Exception("???????????????????????????????????????" +
                                    BaseInfoManager.getUserName(mContext) + "????????????" + e.getLocalizedMessage()));
                            ToastUtil.iconShortToast(mContext, R.mipmap.ic_alert_yellow, "????????????????????????????????????");
                        }

                        @Override
                        public void onNext(Result2<String> s) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            if (s.getCode() == 200) {
                                //????????????
                                initGLayer();
                                hideBottomSheet();
//                                ModifiedFacility modifiedFacility = createModifyForUpload(mCurrentUploadedFacility);
                                //????????????????????????app?????????????????????????????????????????????????????????
//                                uploadJournal(mCurrentModifiedFacility);
//                                Toast.makeText(mContext, "??????????????????", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new RefreshMyUploadList());
                                ToastUtil.shortToast(mContext,"??????????????????");
                                /**
                                 * ??????????????????????????????????????????????????????????????????????????????????????????????????????
                                 */
                                if (firstUploadedFacility != null &&
                                        firstUploadedFacility.getId() != null &&
                                        firstUploadedFacility.getId().equals(mCurrentUploadedFacility.getId())) {
                                    ll_reset.setVisibility(View.GONE);
                                }
                                ((Activity) mContext).finish();
                            } else {
                                CrashReport.postCatchedException(new Exception("???????????????????????????????????????" +
                                        BaseInfoManager.getUserName(mContext) + "????????????" + s.getMessage()));
                                Toast.makeText(mContext, s.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else if (mCurrentModifiedFacility != null) {
            if(progressDialog ==null){
                progressDialog = new ProgressDialog(mContext);
            }
            progressDialog.setMessage("???????????????.....");
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
                            ToastUtil.iconShortToast(mContext, R.mipmap.ic_alert_yellow, "????????????????????????????????????");
                        }

                        @Override
                        public void onNext(Result2<String> s) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            if (s.getCode() == 200) {
                                //????????????
                                initGLayer();
                                hideBottomSheet();
                                //????????????????????????app?????????????????????????????????????????????????????????
//                                uploadJournal(mCurrentModifiedFacility);
//                                Toast.makeText(mContext, "??????????????????", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new RefreshMyModificationListEvent());
                                ToastUtil.shortToast(mContext,"??????????????????");

                                /**
                                 * ??????????????????????????????????????????????????????????????????????????????????????????????????????
                                 */
                                if (firstModifiedFacility != null
                                        && mCurrentModifiedFacility != null
                                        && firstModifiedFacility.getId() != null
                                        && firstModifiedFacility.getId().equals(mCurrentModifiedFacility.getId())) {
                                    ll_reset.setVisibility(View.GONE);
                                }
                                ((Activity) mContext).finish();
                            } else {
                                CrashReport.postCatchedException(new Exception("???????????????????????????????????????" +
                                        BaseInfoManager.getUserName(mContext) + "????????????" + s.getMessage()));
                                Toast.makeText(mContext, s.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    private ModifiedFacility createModifyForUpload(UploadedFacility uploadedFacility) {
        ModifiedFacility modifiedFacility = new ModifiedFacility();
        modifiedFacility.setObjectId(uploadedFacility.getObjectId());
        modifiedFacility.setAddr(uploadedFacility.getAddr());
        modifiedFacility.setUserY(uploadedFacility.getUserLocationY());
        modifiedFacility.setUserX(uploadedFacility.getUserLocationX());
        modifiedFacility.setAttrOne(uploadedFacility.getAttrOne());
        modifiedFacility.setAttrTwo(uploadedFacility.getAttrTwo());
        modifiedFacility.setAttrThree(uploadedFacility.getAttrThree());
        modifiedFacility.setAttrFour(uploadedFacility.getAttrFour());
        modifiedFacility.setAttrFive(uploadedFacility.getAttrFive());
        modifiedFacility.setLayerName(uploadedFacility.getLayerName());
        modifiedFacility.setRoad(uploadedFacility.getRoad());
        modifiedFacility.setX(uploadedFacility.getX());
        modifiedFacility.setY(uploadedFacility.getY());
        return modifiedFacility;
    }

    private void initDeleteFacilityService() {
        if (deleteFacilityService == null) {
            deleteFacilityService = new DeleteFacilityService(mContext);
        }
    }

        //??????????????????????????????
    public void showMissedComponent(final InspectionWellMonitorInfo missedIdentification) {
//        final double x = missedIdentification.getX();
//        final double y = missedIdentification.getY();
//        if (missedIdentification.getIsBinding() == 1) {
//            queryCompoenntInfo(missedIdentification);
//        } else {
//            List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
//            UploadInfo uploadInfo = new UploadInfo();
//            uploadInfo.setUploadedFacilities(missedIdentification);
//            uploadInfos.add(uploadInfo);
//            showOnBottomSheet(uploadInfos);
//        }
    }

    public void queryCompoenntInfo(final UploadedFacility modifiedIdentification) {
        if(progressDialog ==null){
            progressDialog = new ProgressDialog(mContext);
        }
        progressDialog.setMessage("?????????????????????.....");
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
//                    ToastUtil.shortToast(mContext.getApplicationContext(), "????????????");
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
        IDrawerController drawerController = (IDrawerController) mContext;
        layerView = new PatrolLayerView2(mContext, mMapView, drawerController.getDrawerContainer());
        layerPresenter = new PatrolLayerPresenter(layerView, new UploadDetailLayerService(mContext.getApplicationContext()));
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
        if(activity != null) {
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
        if(isFirst){
            isFirst = false;
            return ;
        }

        if(!isNeedRefresh)return ;
        isNeedRefresh = false;

        if (firstModifiedFacility != null) {

            if(progressDialog ==null){
                progressDialog = new ProgressDialog(mContext);
            }
            progressDialog.setMessage("????????????????????????");
            progressDialog.show();
            CorrectFacilityService correctFacilityService = new CorrectFacilityService(mContext);
            correctFacilityService.getModificationById(firstModifiedFacility.getId())
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
                            tv_error_correct.setVisibility(View.GONE);
                            resetStatus(true);
                            if (firstModifiedFacility != null) {
                                CompleteTableInfo completeTableInfo = CompleteTableInfoUtil.getCompleteTableInfo(firstModifiedFacility);
                                mCurrentCompleteTableInfo = completeTableInfo;
                            }
                            component_detail_container.removeAllViews();
                            tableItemContainer.removeAllViews();
                            approvalOpinionContainer.removeAllViews();
                            showBottomSheet(firstModifiedFacility, mCurrentCompleteTableInfo);
                            /**
                             * ???????????????????????????????????????????????????????????????????????????
                             */
                            refreshFirstModifiedFacility(firstUploadedFacility);
                        }

                        @Override
                        public void onNext(ModifiedFacility modifiedFacility) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            mUploadInfos.clear();
                            List<Photo>photos= new ArrayList<>();
                            List<Photo>photosWells= new ArrayList<>();
                            if(!ListUtil.isEmpty(modifiedFacility.getPhotos())){
                                for(Photo photo:modifiedFacility.getPhotos()){
                                    if(photo.getPhotoName().indexOf("well_")!=-1){
                                        photos.add(photo);
                                    }else{
                                        photosWells.add(photo);
                                    }
                                }
                            }
                            modifiedFacility.setPhotos(photos);
                            modifiedFacility.setWellPhotos(photosWells);

                            nextAndPrevContainer.setVisibility(View.GONE);
                            btn_next.setVisibility(View.GONE);
                            btn_prev.setVisibility(View.GONE);
                            currIndex = 0;
                            mCurrentModifiedFacility = null;
                            mCurrentUploadedFacility = null;
                            mCurrentCompleteTableInfo = null;
                            hasComponent = false;
                            tv_error_correct.setVisibility(View.GONE);
                            resetStatus(true);
                            if (firstModifiedFacility != null) {
                                CompleteTableInfo completeTableInfo = CompleteTableInfoUtil.getCompleteTableInfo(modifiedFacility);
                                mCurrentCompleteTableInfo = completeTableInfo;
                            }
                            component_detail_container.removeAllViews();
                            tableItemContainer.removeAllViews();
                            approvalOpinionContainer.removeAllViews();
                            showBottomSheet(modifiedFacility, mCurrentCompleteTableInfo);
                            /**
                             * ???????????????????????????????????????????????????????????????????????????
                             */
//                            refreshFirstModifiedFacility(modifiedFacility);
                        }
                    });
        } else if (firstUploadedFacility!= null) {
            //???????????????????????????????????????
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????id
            if(progressDialog ==null){
                progressDialog = new ProgressDialog(mContext);
            }
            progressDialog.setMessage("????????????????????????");
            progressDialog.show();

            InspectionWellMonitorService wellMonitorService = new InspectionWellMonitorService(mContext);
            wellMonitorService.getInspectionWellMonitorById(firstUploadedFacility.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Result2<InspectionWellMonitorInfo>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            mCurrentModifiedFacility = null;
                            mCurrentUploadedFacility = null;
                            mCurrentCompleteTableInfo = null;
                            hasComponent = false;
                            tv_error_correct.setVisibility(View.GONE);
                            resetStatus(true);
                            firstUploadedFacility.getPhotos().clear();
                            showMissedComponent(firstUploadedFacility);
                            /**
                             * ?????????????????????????????????????????????????????????????????????
                             */
                            refreshFirstUploadFacility(firstUploadedFacility);
                        }

                        @Override
                        public void onNext(Result2<InspectionWellMonitorInfo> result2) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            mCurrentModifiedFacility = null;
                            mCurrentUploadedFacility = null;
                            mCurrentCompleteTableInfo = null;
                            hasComponent = false;
                            tv_error_correct.setVisibility(View.GONE);
                            resetStatus(true);

                            InspectionWellMonitorInfo wellMonitorInfo = result2.getData();
                            if(wellMonitorInfo ==null ) return;
                            showMissedComponent(wellMonitorInfo);
                            /**
                             * ?????????????????????????????????????????????????????????????????????
                             */
                            refreshFirstUploadFacility(wellMonitorInfo);
                            showBottomSheet(wellMonitorInfo, null);
                        }
                    });

        } else {
            initGLayer();
            hideBottomSheet();
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
//            if (locationMarker != null) {
//                locationMarker.setVisibility(View.GONE);
//            }
//            if (mMapView.getCallout().isShowing()) {
//                mMapView.getCallout().hide();
//            }
//            int visibility = map_bottom_sheet.getVisibility();
//            initGLayer();
//            hideBottomSheet();
//            tableItemContainer.removeAllViews();
//            component_detail_container.removeAllViews();
//            approvalOpinionContainer.removeAllViews();
//            if (visibility == View.VISIBLE) {
//                return;
//            }
//            double scale = mMapView.getScale();
//            if (scale < LayerUrlConstant.MIN_QUERY_SCALE && (ifUploadLayerVisible || ifMyUploadLayerVisible)) {
//                final Point point = mMapView.toMapPoint(e.getX(), e.getY());
//                query(point.getX(), point.getY());
//
//            }
        }

    }

    /**
     * ???????????????????????????
     *
     * @param x
     * @param y
     */
    private void query(double x, double y) {
        pd = new ProgressDialog(mContext);
        pd.setMessage("????????????????????????...");
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
        tv_error_correct.setVisibility(View.GONE);
        //tv_sure.setVisibility(View.GONE);
        resetStatus(true);
        if (ifUploadLayerVisible) {
            mUploadLayerService.setQueryByWhere(getDistrictQueryWhere());
        } else if (ifMyUploadLayerVisible) {
            mUploadLayerService.setQueryByWhere(getQueryWhere());
        }
        mUploadLayerService.queryUploadDataInfos(point, mMapView.getSpatialReference(), mMapView.getResolution(), new Callback2<List<UploadInfo>>() {
            @Override
            public void onSuccess(List<UploadInfo> uploadInfos) {
                pd.dismiss();
                if (ListUtil.isEmpty(uploadInfos) || (uploadInfos.size() == 1 && uploadInfos.get(0).getUploadedFacilities() == null && uploadInfos.get(0).getModifiedFacilities() == null)) {
                    ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "???????????????");
                    return;
                }
                mUploadInfos = uploadInfos;
                showOnBottomSheet(uploadInfos);
            }

            @Override
            public void onFail(Exception error) {
                ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, error.getLocalizedMessage());
                pd.dismiss();
            }
        });
    }

    private String getQueryWhere() {

//        FacilityAffairService facilityAffairService = new FacilityAffairService(mContext);
//        String parentOrgOfCurrentUser = facilityAffairService.getParentOrgOfCurrentUser();
//        boolean ifCurrentUserBelongToCityUser = facilityAffairService.ifCurrentUserBelongToCityUser();
//        //??????????????????????????????????????????
//        if (!ifCurrentUserBelongToCityUser) {
//            return " PARENT_ORG_NAME like '%" + parentOrgOfCurrentUser + "%'";
//        }
//        return null;
        //???????????????????????????
        return "USER_ID='" + BaseInfoManager.getUserId(mContext) + "'";
//        return "1=1 ";
    }

    private String getDistrictQueryWhere() {
        String district = BaseInfoManager.getUserOrg(mContext);
        if (district.contains("??????")) {
            district = "??????";
        }
        String where = "";
        if (!district.contains("???")) {
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
                symbol = new SimpleLineSymbol(Color.RED, 4, SimpleLineSymbol.STYLE.DASH);
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

    @NonNull
    private Symbol getPointSymbol() {
        Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), R.mipmap.ic_select_location_orange, null);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mContext, drawable);// xjx ????????????api19???????????????drawable
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
        //????????????????????????????????????
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

        //??????marker
        locationMarker.setVisibility(View.GONE);
        initGLayer();
        Geometry geometry = null;
        mCurrentCompleteTableInfo = uploadInfos.get(0).getCompleteTableInfo();
//        if (uploadInfos.get(0).getUploadedFacilities() != null) {//????????????
//            geometry = new Point(uploadInfos.get(0).getUploadedFacilities().getX(), uploadInfos.get(0).getUploadedFacilities().getY());
//            showBottomSheet(uploadInfos.get(0).getUploadedFacilities(), uploadInfos.get(0).getCompleteTableInfo());
//        } else if (uploadInfos.get(0).getModifiedFacilities() != null) {
//
//            showBottomSheet(uploadInfos.get(0).getModifiedFacilities(), uploadInfos.get(0).getCompleteTableInfo());
//        }
    }

    /**
     * ????????????
     * ????????????????????????????????????BottomSheet???
     */
    private void showBottomSheet(final ModifiedFacility modifiedFacility, CompleteTableInfo completeTableInfo) {
        //initGLayer();
        if (modifiedFacility == null) {
            return;
        }


        /**
         * ??????????????????????????????????????????????????????????????????????????????????????????????????????
         */
        if (CheckState.UNCHECK.indexOf(modifiedFacility.getCheckState())!=-1 ||
                CheckState.IN_DOUBT.indexOf(modifiedFacility.getCheckState())!=-1) {
            btnReEdit.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(modifiedFacility.getCheckState()) && modifiedFacility.getId() != -1) {
            //????????????????????????????????????????????????
            btnReEdit.setVisibility(View.VISIBLE);
        } else {
            btnReEdit.setVisibility(View.GONE);
        }

        /**
         * ??????????????????????????????????????????????????????????????????????????????????????????????????????
         */
        if (!CheckState.SUCCESS.equals(modifiedFacility.getCheckState()) && modifiedFacility.getId() != -1) {
            btnDelete.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(modifiedFacility.getCheckState()) && modifiedFacility.getId() != -1) {
            //????????????????????????????????????????????????
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        if("delete".equals(modifiedFacility.getReportType())){
            btnReEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }
        //?????????????????????????????????????????????????????????????????????????????????
        if(isShowCancelDeleteButton == 1){
            btnDelete.setVisibility(View.GONE);
        }else if(isShowCancelDeleteButton == 2 ){
            if( "delete".equals(modifiedFacility.getReportType())){
                btnDelete.setVisibility(View.VISIBLE);
                ((Button)btnDelete).setText("????????????");
                RxView.clicks(btnDelete)
                        .throttleFirst(500, TimeUnit.MILLISECONDS)
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                DialogUtil.MessageBox(mContext, "??????", "??????????????????????????????????????????", new DialogInterface.OnClickListener() {
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
            }else{
                btnDelete.setVisibility(View.GONE);
            }

        }else{
            btnDelete.setVisibility(View.GONE);
        }



//        /**
//         * ???????????????????????????
//         */
//        if(CheckState.SUCCESS.equals(modifiedFacility.getCheckState()) && modifiedFacility.getId() != -1){
//            ll_feedback.setVisibility(View.VISIBLE);
//            btnFeedback.setVisibility(View.VISIBLE);
//            llGoFeedBackList.setVisibility(View.VISIBLE);
//            /*if(feedbackFacilityService == null){
//                feedbackFacilityService = new FeedbackFacilityService(mContext);
//            }
//            feedbackFacilityService.getFeedbackInfos(modifiedFacility.getId(), "1")
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<Result2<List<FeedbackInfo>>>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            llGoFeedBackList.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onNext(Result2<List<FeedbackInfo>> listResult2) {
//                            if(listResult2 != null
//                                    && !ListUtil.isEmpty(listResult2.getData())){
//                                llGoFeedBackList.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    });*/
//        } else {
//            ll_feedback.setVisibility(View.GONE);
//            btnFeedback.setVisibility(View.GONE);
//            llGoFeedBackList.setVisibility(View.GONE);
//        }

        component_detail_container.setVisibility(View.VISIBLE);

        if (component_detail_container.getChildCount() == 0) {

            /**
             * ?????????????????????
             */
            if (mCurrentCompleteTableInfo != null) {
                tv_error_correct.setVisibility(View.GONE);
            } else {
                tv_error_correct.setVisibility(View.GONE);
            }


            /**
             * ????????????????????????
             */
//            if (ListUtil.isEmpty(modifiedFacility.getApprovalOpinions())) {
//                tv_approval_opinion_list.setVisibility(View.GONE);
//            } else {
//                tv_approval_opinion_list.setVisibility(View.VISIBLE);
//            }

            mCurrentModifiedFacility = modifiedFacility;
            Geometry geometry = null;
            /**
             * ??????getX,getY????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
            MyModifiedFacilityTableViewManager modifiedIdentificationTableViewManager = new MyModifiedFacilityTableViewManager(mContext,
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
                }, 600);
            }
        }

        component_detail_ll.setVisibility(View.VISIBLE);
    }

    private void initBottomSheetView() {
        nextAndPrevContainer = (ViewGroup) map_bottom_sheet.findViewById(R.id.ll_next_and_prev_container);

        //????????????
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

        //???????????????
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
                if (mCurrentUploadedFacility != null) {
                    showBottomSheet(mCurrentUploadedFacility.getId(), "gjjd_jc");
                }

            }
        });
    }

    private void showBottomSheet(Long markId, String reportType) {
        if (approvalOpinionContainer.getChildCount() == 0) {
            InspectionOpinionViewManager myModifiedFacilityTableViewManage = new InspectionOpinionViewManager(mContext, markId, reportType);
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
     * ??????tab??????
     *
     * @param textView
     */
    private void highlight(TextView textView) {
        textView.setBackground(getResources().getDrawable(R.drawable.round_blue_rectangle));
        textView.setTextColor(getResources().getColor(R.color.agmobile_white));
    }

    /**
     * ????????????tab??????
     *
     * @param textView
     */
    private void cancelHighlight(TextView textView) {
        textView.setBackground(getResources().getDrawable(R.drawable.round_grey_rectangle));
        textView.setTextColor(getResources().getColor(R.color.agmobile_blue));
    }




    /**
     * ????????????
     * ????????????????????????????????????BottomSheet???
     */
    private void showBottomSheet(final InspectionWellMonitorInfo uploadedFacility, CompleteTableInfo completeTableInfo) {
        mCurrentModifiedFacility = null;
        //initGLayer();
        if (uploadedFacility == null) {
            return;
        }
        mCurrentUploadedFacility = uploadedFacility;
        tv_approval_opinion_list.setVisibility(View.VISIBLE);
        // ???????????????????????????????????????????????????????????????
        /*String usId = uploadedFacility.getUsid();
        String objectId = uploadedFacility.getJbjObjectId();
        getGjInfo(usId,objectId,null);*/

        //?????????????????????????????????????????????????????????????????????????????????
        if(isShowCancelDeleteButton == 1){
            btnDelete.setVisibility(View.GONE);
        }else if(isShowCancelDeleteButton == 2){
            btnDelete.setVisibility(View.VISIBLE);
            ((Button)btnDelete).setText("????????????");
            RxView.clicks(btnDelete)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            DialogUtil.MessageBox(mContext, "??????", "??????????????????????????????????????????", new DialogInterface.OnClickListener() {
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
        }
        btnDelete.setVisibility(View.GONE);
        btnReEdit.setVisibility(View.VISIBLE);
        if(CheckState.UNCHECK2.equals(uploadedFacility.getCheckState()) && uploadedFacility.getId() != -1) {
            btnReEdit.setVisibility(View.GONE);
        }
        /**
         * ???????????????????????????
         */ // TODO: 2018/11/9 ????????????????????????
      /*  if(CheckState.SUCCESS.equals(uploadedFacility.getCheckState()) && uploadedFacility.getId() != -1){
            if("?????????".equals(uploadedFacility.getLayerName())) {
                btnReEdit.setVisibility(View.VISIBLE);
            }
            *//*if(feedbackFacilityService == null){
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
                    });*//*
        } else {
//            ll_feedback.setVisibility(View.GONE);
//            btnFeedback.setVisibility(View.GONE);
//            llGoFeedBackList.setVisibility(View.GONE);
//            btnReEdit.setVisibility(View.GONE);
        }*/

        /**
         * ??????????????????
         */

      /*  if (uploadedFacility.getIsBinding() == 1 && mCurrentCompleteTableInfo != null) {
            tv_error_correct.setVisibility(View.VISIBLE);
            hasComponent = true;
        } else {
            tv_error_correct.setVisibility(View.GONE);
            hasComponent = false;
        }*/
        /**
         * ????????????????????????
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
            Geometry geometry = new Point(uploadedFacility.getJbjX(), uploadedFacility.getJbjY());
            drawGeometry(geometry, mGLayer, true, true);
            InspectionWellMonitorTableViewManager modifiedIdentificationTableViewManager = new InspectionWellMonitorTableViewManager(mContext,
                    uploadedFacility);
//            if (completeTableInfo != null) {
//                modifiedIdentificationTableViewManager = new MyJbjMonitorTableViewManager(mContext,
//                        uploadedFacility, completeTableInfo.getAttrs());
//            } else {//??????????????????
//                modifiedIdentificationTableViewManager = new MyJbjMonitorTableViewManager(mContext,
//                        uploadedFacility);
//            }
            modifiedIdentificationTableViewManager.addTo(component_detail_container);
            if (mBehavior.getState() == STATE_COLLAPSED) {
                component_detail_container.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBehavior.setState(STATE_ANCHOR);
                    }
                }, 700);
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
     * ????????????
     * ????????????????????????????????????BottomSheet???
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
        //??????
//        /**
//         * ??????????????????????????????????????????
//         */
//        String layertype = "";
//        if (mCurrentModifiedFacility != null) {
//            layertype = mCurrentModifiedFacility.getLayerName();
//        } else if (mCurrentUploadedFacility != null) {
//            layertype = mCurrentUploadedFacility.getLayerName();
//        }
//
//        TextItemTableItem ssTv = new TextItemTableItem(getContext());
//        ssTv.setTextViewName("??????");
//        ssTv.setText(StringUtil.getNotNullString(layertype, "") + "(" + usid + ")");
//        ssTv.setReadOnly();
//        tableItemContainer.addView(ssTv);
//
//        //????????????
//        String address = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.ADDR));
//        TextItemTableItem addressTv = new TextItemTableItem(getContext());
//        addressTv.setTextViewName("????????????");
//        address = replaceSpaceCharacter(address);
////        addressTv.setText(StringUtil.getNotNullString(address, "???"));
//        addressTv.setText(StringUtil.getNotNullString(address, ""));
//        addressTv.setReadOnly();
//        tableItemContainer.addView(addressTv);
//
//
//        //????????????
//        String road = String.valueOf(CompleteTableInfo.getAttrs().get
//                (ComponentFieldKeyConstant.ROAD));
//        TextItemTableItem roadTv = new TextItemTableItem(getContext());
//        roadTv.setTextViewName("????????????");
//        road = replaceSpaceCharacter(road);
//        roadTv.setText(StringUtil.getNotNullString(road, ""));
//        roadTv.setReadOnly();
//        tableItemContainer.addView(roadTv);
//
//        if (layertype.equals("??????")) {
//            String yinjing_type = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.SUBTYPE));
//            String sort = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.SORT));
//            String MATERIAL = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.MATERIAL));
//
//            TextItemTableItem oneAttrItem = new TextItemTableItem(getContext());
//            TextItemTableItem twoAttrItem = new TextItemTableItem(getContext());
//            TextItemTableItem threeAttrItem = new TextItemTableItem(getContext());
//
//            oneAttrItem.setTextViewName("????????????");
//            yinjing_type = replaceSpaceCharacter(yinjing_type);
//            oneAttrItem.setText(StringUtil.getNotNullString(yinjing_type, ""));
//            oneAttrItem.setReadOnly();
//
//            twoAttrItem.setTextViewName("????????????");
//            sort = replaceSpaceCharacter(sort);
//            twoAttrItem.setText(StringUtil.getNotNullString(sort, ""));
//            twoAttrItem.setReadOnly();
//
//            threeAttrItem.setTextViewName("????????????");
//            MATERIAL = replaceSpaceCharacter(MATERIAL);
//            threeAttrItem.setText(StringUtil.getNotNullString(MATERIAL, ""));
//            threeAttrItem.setReadOnly();
//
//            tableItemContainer.addView(oneAttrItem);
//            tableItemContainer.addView(twoAttrItem);
//            tableItemContainer.addView(threeAttrItem);
//
//        } else if (layertype.equals("?????????")) {
//            TextItemTableItem oneAttrItem = new TextItemTableItem(getContext());
//            TextItemTableItem twoAttrItem = new TextItemTableItem(getContext());
//            String sort = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.SORT));
//            String direction = String.valueOf(CompleteTableInfo.getAttrs().get
//                    (ComponentFieldKeyConstant.RIVER));
//
//            oneAttrItem.setTextViewName("????????????");
//            direction = replaceSpaceCharacter(direction);
//            oneAttrItem.setText(StringUtil.getNotNullString(direction, ""));
//            oneAttrItem.setReadOnly();
//
//            twoAttrItem.setTextViewName("????????????");
//            sort = replaceSpaceCharacter(sort);
//            twoAttrItem.setText(StringUtil.getNotNullString(sort, ""));
//            twoAttrItem.setReadOnly();
//
//            tableItemContainer.addView(oneAttrItem);
//            tableItemContainer.addView(twoAttrItem);
//
//        } else if (layertype.equals("?????????")) {
//            String feature = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.FEATURE));
//            String style = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.STYLE));
//            TextItemTableItem oneAttrItem = new TextItemTableItem(getContext());
//            TextItemTableItem twoAttrItem = new TextItemTableItem(getContext());
//
//            oneAttrItem.setTextViewName("??????");
//            feature = replaceSpaceCharacter(feature);
//            oneAttrItem.setText(StringUtil.getNotNullString(feature, ""));
//            oneAttrItem.setReadOnly();
//
//            twoAttrItem.setTextViewName("??????");
//            style = replaceSpaceCharacter(style);
//            twoAttrItem.setText(StringUtil.getNotNullString(style, ""));
//            twoAttrItem.setReadOnly();
//
//            tableItemContainer.addView(oneAttrItem);
//            tableItemContainer.addView(twoAttrItem);
//        }
//
//        //????????????
//        String parentOrg = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.OWNERDEPT));
//        TextItemTableItem quanshuTv = new TextItemTableItem(getContext());
//        quanshuTv.setTextViewName("????????????");
//        parentOrg = replaceSpaceCharacter(parentOrg);
//        quanshuTv.setText(StringUtil.getNotNullString(parentOrg, ""));
//        quanshuTv.setReadOnly();
//        tableItemContainer.addView(quanshuTv);
//        //???????????????
//        TextItemTableItem bianhaoTv = new TextItemTableItem(getContext());
//        bianhaoTv.setTextViewName("???????????????");
//        bianhaoTv.setReadOnly();
//        String codeValue = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.CODE));
//        codeValue = codeValue.trim();
//        if (layertype.equals("??????")) {
//            if (!codeValue.isEmpty()) {
//                bianhaoTv.setText(StringUtil.getNotNullString(codeValue, ""));
//            } else {
//                bianhaoTv.setText("");
//            }
//            tableItemContainer.addView(bianhaoTv);
//        }

        /**
         * ??????????????????????????????????????????
         */
//        String layertype = "";
//        if (mCurrentModifiedFacility != null) {
//            layertype = mCurrentModifiedFacility.getLayerName();
//        } else if (mCurrentUploadedFacility != null) {
//            layertype = mCurrentUploadedFacility.getLayerName();
//        }


        if (mCurrentModifiedFacility != null) {
            double originX = mCurrentModifiedFacility.getOriginX();
            double originY = mCurrentModifiedFacility.getOriginY();
            double x = mCurrentModifiedFacility.getX();
            double y = mCurrentModifiedFacility.getY();
            initGLayerForOriginLocation();
//            new OriginalAttributesViewManager(getActivity(),
//                    tableItemContainer, mCurrentCompleteTableInfo, mMapView, originX, originY, layertype, mGLayerForOriginLocation);
            return;
        }
//        new OriginalAttributesViewManager(getActivity(),
//                tableItemContainer, mCurrentCompleteTableInfo, mMapView, 0, 0, layertype, mGLayerForOriginLocation);
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

    /**
     * ??????????????????
     * @param usId
     * @param objectId
     * @param o
     */
    private void getGjInfo(String usId, String objectId, String o) {
        mPsdyKeyNodeInspectionWells.clear();
        mIdentificationService.queryPsdyKeyNodeInspectionWell(usId, objectId, o)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result2<List<PsdyJbj>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        ToastUtil.shortToast(mContext, "????????????????????????");
                    }

                    @Override
                    public void onNext(Result2<List<PsdyJbj>> responseBody) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (responseBody.getCode() == 200) {
                            if (!ListUtil.isEmpty(responseBody.getData())) {
                                drawPsdyLines(responseBody.getData());
                                List<PsdyJbj> tempList = responseBody.getData();
                                mPsdyKeyNodeInspectionWells.clear();
                                mPsdyKeyNodeInspectionWells.addAll(tempList);
                            }
                        } else {
                            ToastUtil.shortToast(mContext, "????????????????????????");
                        }
                    }
                });
    }

    private void drawPsdyLines(List<PsdyJbj> data) {
        if (ListUtil.isEmpty(data)) return;
        Polyline polyline = null;
        PsdyJbj psdyJbj = null;
        Point startP = null;
        Point endP = null;
        initGraphicSelectGLayer();
        for (int j = 0; j < data.size(); j++) {
            polyline = new Polyline();
            psdyJbj = data.get(j);
            startP = new Point();
            startP.setXY(psdyJbj.getJbjX(),psdyJbj.getJbjY());
            endP = new Point();
            endP.setXY(psdyJbj.getPsdyX(),psdyJbj.getPsdyY());
            polyline.startPath(startP);
            polyline.lineTo(endP);
            drawGeometry(psdyJbj,polyline, mGraphicSelectLayer, new SimpleLineSymbol(Color.BLUE, 2,SimpleLineSymbol.STYLE.DASH), false, false);
        }
    }

    /**
     * ?????????????????????
     *
     * @param graphicsLayer
     * @param ifRemoveAll   ?????????????????????????????????
     * @param geometry
     */
    private void drawGeometry(PsdyJbj psdyJbj,Geometry geometry, GraphicsLayer graphicsLayer, Symbol designatedSymbol, boolean ifRemoveAll, boolean ifCenter) {

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
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("Id",psdyJbj.getId());
            Graphic graphic = new Graphic(geometry, symbol,attributes);
            graphicsLayer.addGraphic(graphic);
            if (geometry.getType() == Geometry.Type.POLYLINE) { //???????????????????????????????????????????????????????????????
                this.graphic = graphic;
            }
        }

        if (ifCenter) {
            mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        }
    }

    private void initGraphicSelectGLayer() {
        if (mGraphicSelectLayer == null) {
            mGraphicSelectLayer = new GraphicsLayer();
            mMapView.addLayer(mGraphicSelectLayer);
        } else {
            mGraphicSelectLayer.removeAll();
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
    public void onReceivedWellMonitorUpdataEvent(final WellMonitorRefreshEvent refreshEvent) {

        if (refreshEvent !=null) {
            //???????????????finish???
            if(refreshEvent.getType() == 1){
                ((Activity) mContext).finish();
                return;
            }
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????id
            if(progressDialog ==null){
                progressDialog = new ProgressDialog(mContext);
            }
            progressDialog.setMessage("????????????????????????");
            progressDialog.show();

            InspectionWellMonitorService wellMonitorService = new InspectionWellMonitorService(mContext);
            wellMonitorService.getInspectionWellMonitorById(mCurrentUploadedFacility.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Result2<InspectionWellMonitorInfo>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            mCurrentModifiedFacility = null;
                            mCurrentUploadedFacility = null;
                            mCurrentCompleteTableInfo = null;
                            hasComponent = false;
                            tv_error_correct.setVisibility(View.GONE);
                            resetStatus(true);
//                            if (firstUploadedFacility != null) {
//                                CompleteTableInfo completeTableInfo = CompleteTableInfoUtil.getCompleteTableInfo(firstUploadedFacility);
//                                mCurrentCompleteTableInfo = completeTableInfo;
//                            }
                            component_detail_container.removeAllViews();
                            tableItemContainer.removeAllViews();
                            approvalOpinionContainer.removeAllViews();
                            showBottomSheet(firstUploadedFacility, null);
                            refreshFirstModifiedFacility(firstUploadedFacility);
                        }

                        @Override
                        public void onNext(Result2<InspectionWellMonitorInfo> result2) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            InspectionWellMonitorInfo wellMonitorInfo = result2.getData();
//                            mUploadInfos.clear();
//                            List<Photo>photos= new ArrayList<>();
//                            List<Photo>photosWells= new ArrayList<>();
//                            for(Photo photo:wellMonitorInfo.getPhotos()){
//                                if(photo.getPhotoName().indexOf("well_")!=-1){
//                                    photos.add(photo);
//                                }else{
//                                    photosWells.add(photo);
//                                }
//                            }
//                            wellMonitorInfo.setPhotos(photos);
//                            wellMonitorInfo.setWellPhotos(photosWells);
                            if(wellMonitorInfo==null)return;
                            wellMonitorInfo.setPhotos(Attachment2PhotoUtil.toPhotos(wellMonitorInfo.getAttachments()));
//                            nextAndPrevContainer.setVisibility(View.GONE);
//                            btn_next.setVisibility(View.GONE);
//                            btn_prev.setVisibility(View.GONE);
//                            currIndex = 0;
//                            mCurrentModifiedFacility = null;
                            mCurrentUploadedFacility = null;
//                            mCurrentCompleteTableInfo = null;
                            hasComponent = false;
                            tv_error_correct.setVisibility(View.GONE);
                            resetStatus(true);
//                            if (uploadFacilitySuccessEvent.getModifiedFacility() != null) {
//                                CompleteTableInfo completeTableInfo = CompleteTableInfoUtil.getCompleteTableInfo(modifiedFacility);
//                                mCurrentCompleteTableInfo = completeTableInfo;
//                            }
                            component_detail_container.removeAllViews();
                            tableItemContainer.removeAllViews();
                            approvalOpinionContainer.removeAllViews();

                            showBottomSheet(wellMonitorInfo, null);
                            refreshFirstModifiedFacility(wellMonitorInfo);
                        }
                    });
        }
//        else if (uploadFacilitySuccessEvent.getUploadedFacility() != null) {//???????????????????????????????????????
//            final UploadedFacility uploadedFacility = uploadFacilitySuccessEvent.getUploadedFacility();
//            //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????id
//            final ProgressDialog progressDialog = new ProgressDialog(mContext);
//            progressDialog.setMessage("????????????????????????");
//            progressDialog.show();
//
//            UploadFacilityService uploadFacilityService = new UploadFacilityService(mContext);
//            uploadFacilityService.getUploadFacilityById(uploadedFacility.getId())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<UploadedFacility>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            if (progressDialog != null && progressDialog.isShowing()) {
//                                progressDialog.dismiss();
//                            }
//                            mUploadInfos.clear();
//                            nextAndPrevContainer.setVisibility(View.GONE);
//                            btn_next.setVisibility(View.GONE);
//                            btn_prev.setVisibility(View.GONE);
//                            currIndex = 0;
//                            mCurrentModifiedFacility = null;
//                            mCurrentUploadedFacility = null;
//                            mCurrentCompleteTableInfo = null;
//                            hasComponent = false;
//                            tv_error_correct.setVisibility(View.VISIBLE);
//                            resetStatus(true);
//                            uploadedFacility.getPhotos().clear();
//                            showMissedComponent(uploadedFacility);
//
//
//                            refreshFirstUploadFacility(uploadedFacility);
//                        }
//
//                        @Override
//                        public void onNext(UploadedFacility uploadedFacility1) {
//                            if (progressDialog != null && progressDialog.isShowing()) {
//                                progressDialog.dismiss();
//                            }
//                            mUploadInfos.clear();
//                            nextAndPrevContainer.setVisibility(View.GONE);
//                            btn_next.setVisibility(View.GONE);
//                            btn_prev.setVisibility(View.GONE);
//                            currIndex = 0;
//                            mCurrentModifiedFacility = null;
//                            mCurrentUploadedFacility = null;
//                            mCurrentCompleteTableInfo = null;
//                            hasComponent = false;
//                            tv_error_correct.setVisibility(View.VISIBLE);
//                            resetStatus(true);
//
//                            showMissedComponent(uploadedFacility1);
//                            refreshFirstUploadFacility(uploadedFacility1);
//                        }
//                    });
//
//        }
        else {
            initGLayer();
            hideBottomSheet();
        }
    }

    private void refreshFirstModifiedFacility(InspectionWellMonitorInfo wellMonitorInfo) {
        if (firstUploadedFacility != null) {
            if (firstUploadedFacility.getId() != null
                    && firstUploadedFacility.getId().longValue() == wellMonitorInfo.getId().longValue()) {
                firstUploadedFacility = wellMonitorInfo;
            }
        }
    }

    private void refreshFirstUploadFacility(InspectionWellMonitorInfo uploadedFacility) {
        if (firstUploadedFacility != null) {
            if (firstUploadedFacility.getId() != null && firstUploadedFacility.getId().equals(uploadedFacility.getId())) {
                firstUploadedFacility = uploadedFacility;
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

    /**
     * ??????????????????
     * @param modifiedFacility
     */
    private void uploadJournal(ModifiedFacility modifiedFacility) {
        modifiedFacility.setId(null);
        User user = new LoginService(mContext, AMDatabase.getInstance()).getUser();
        String userName = user.getUserName();
        long currentTimeMillis = System.currentTimeMillis();

        modifiedFacility.setMarkTime(currentTimeMillis);
        modifiedFacility.setMarkPerson(userName);
        modifiedFacility.setMarkPersonId(user.getId());
        //????????????
        JournalService identificationService = new JournalService(mContext.getApplicationContext());
        identificationService.upload(modifiedFacility)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        CrashReport.postCatchedException(new Exception("???????????????????????????????????????" +
                                BaseInfoManager.getUserName(mContext) + "????????????" + e.getLocalizedMessage()));
                        ToastUtil.shortToast(mContext,"?????????????????????????????????");
                        //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "????????????");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (responseBody.getCode() == 200) {
                            ToastUtil.shortToast(mContext,"????????????");
                        } else {
                            CrashReport.postCatchedException(new Exception("???????????????????????????????????????" +
                                    BaseInfoManager.getUserName(mContext) + "????????????" + responseBody.getMessage()));
                            ToastUtil.shortToast(mContext,"????????????????????????????????????????????????" + responseBody.getMessage());
                            //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "???????????????????????????" + responseBody.getMessage());
                        }
                    }
                });
    }

}
