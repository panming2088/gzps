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

package com.augurit.agmobile.gzps.uploadfacility.view.doorno;

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
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MapHelper;
import com.augurit.agmobile.gzps.common.OnInitLayerUrlFinishEvent;
import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.selectcomponent.LimitedLayerAdapter;
import com.augurit.agmobile.gzps.componentmaintenance.ComponetListAdapter;
import com.augurit.agmobile.gzps.uploadfacility.model.CheckState;
import com.augurit.agmobile.gzps.uploadfacility.model.CompleteTableInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadDoorNoDetailBean;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.service.CorrectFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.DoorNoLayerService;
import com.augurit.agmobile.gzps.uploadfacility.service.FeedbackFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadLayerService;
import com.augurit.agmobile.gzps.uploadfacility.util.CompleteTableInfoUtil;
import com.augurit.agmobile.gzps.uploadfacility.view.UploadFacilitySuccessEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.approvalopinion.ApprovalOpinionViewManager;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.MyModifiedFacilityTableViewManager;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.RefreshMyModificationListEvent;
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
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior;
import com.augurit.am.cmpt.widget.bottomsheet.IBottomSheetBehavior;
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
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
public class UploadDoorNoDetailMapFragment extends Fragment implements View.OnClickListener {

    private static final String KEY_MAP_STATE = "com.esri.MapState";

    private LocationMarker locationMarker;

    //    private ModifiedFacility mCurrentModifiedFacility;
    private UploadDoorNoDetailBean mCurrentUploadedDoorNo;
    private CompleteTableInfo mCurrentCompleteTableInfo;

    /**
     * 是否是第一次定位，如果是，那么居中；否则，不做任何操作；
     */
    private boolean ifFirstLocate = true;
    private LocationButton locationButton;
    private LegendPresenter legendPresenter;
    View mView;

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
    /**
     * 地图默认的事件处理
     */
    private SelectLocationTouchListener defaultMapOnTouchListener;
    private ProgressDialog progressDialog;
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

    private UploadDoorNoDetailBean firstUploadedDoorNo;
    private ModifiedFacility firstModifiedFacility;
    private CompleteTableInfo firstCompleteTableInfo;

    private View ll_reset;
    private View btnReEdit;
    private View btnDelete;
    private View ll_feedback;
    private View btnFeedback;
    //    private View llGoFeedBackList;
    private ViewGroup approvalOpinionContainer;
    private TextView tv_approval_opinion_list;
    private UploadFacilityService mUploadDoorNoService;
    private FeedbackFacilityService feedbackFacilityService;
    /**
     * 设施原位置绘制图层
     */
    private GraphicsLayer mGLayerForOriginLocation;


    public static UploadDoorNoDetailMapFragment getInstance(Bundle data) {
        UploadDoorNoDetailMapFragment addComponentFragment2 = new UploadDoorNoDetailMapFragment();
        addComponentFragment2.setArguments(data);
        return addComponentFragment2;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(mContext, R.layout.fragment_upload_doorno_map_distribute, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;


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
                        mMapView.setScale(mMapView.getMaxScale());
                        scaleView.setScale(mMapView.getMaxScale());
                        Point point = new Point(PatrolLayerPresenter.longitude, PatrolLayerPresenter.latitude);
                        mMapView.centerAt(point, true);

                        if (locationButton != null) {
                            // 2
                            locationButton.setStateNormal();
                        }

                        if (myUploadLayerBtn != null) {
                            myUploadLayerBtn.performClick();
                        }

                        if (uploadLayerBtn != null) {
                            uploadLayerBtn.performClick();
                        }
                        // layerPresenter.changeLayerVisibility(PatrolLayerPresenter.LOCAL_UPLOAD_LAYER_NAME, true);

                        if (getArguments() != null
                                && getArguments().getSerializable("uploadDoorNoDetailBean") != null) {
                            UploadDoorNoDetailBean uploadDoorNoDetailBean = (UploadDoorNoDetailBean) getArguments().getSerializable("uploadDoorNoDetailBean");
                            if (uploadDoorNoDetailBean != null) {
                                firstUploadedDoorNo = uploadDoorNoDetailBean;
                                mUploadInfos.clear();
                                nextAndPrevContainer.setVisibility(View.GONE);
                                btn_next.setVisibility(View.GONE);
                                btn_prev.setVisibility(View.GONE);
                                currIndex = 0;
//                                mCurrentModifiedFacility = null;
                                mCurrentUploadedDoorNo = null;
                                mCurrentCompleteTableInfo = null;
                                hasComponent = false;
                                tv_error_correct.setVisibility(View.GONE);
                                resetStatus(true);
                                showBottomSheet(uploadDoorNoDetailBean, null);
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
//                                mCurrentModifiedFacility = null;
                                mCurrentUploadedDoorNo = null;
                                mCurrentCompleteTableInfo = null;
                                hasComponent = false;
//                                tv_error_correct.setVisibility(View.VISIBLE);
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
        //定位图标
        locationMarker = (LocationMarker) view.findViewById(R.id.locationMarker);
        //定位按钮
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
        mBehavior.setAnchorHeight(DensityUtil.dp2px(mContext, 230));
//        RxView.clicks(btn_next)
//                .throttleFirst(500, TimeUnit.MILLISECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        currIndex++;
//                        if (currIndex > mUploadInfos.size()) {
//                            btn_next.setVisibility(View.GONE);
//                            return;
//                        }
//                        component_detail_container.removeAllViews();
//                        tableItemContainer.removeAllViews();
//                        approvalOpinionContainer.removeAllViews();
//                        resetStatus(true);
//                        mCurrentCompleteTableInfo = mUploadInfos.get(currIndex).getCompleteTableInfo();
//                        if (mCurrentCompleteTableInfo != null) {
//                            tv_error_correct.setVisibility(View.VISIBLE);
//                        }
//                        if (mUploadInfos.get(currIndex).getModifiedFacilities() != null) {
//                            showBottomSheet(mUploadInfos.get(currIndex).getModifiedFacilities(), mUploadInfos.get(currIndex).getCompleteTableInfo());
//                        } else if (mUploadInfos.get(currIndex).getUploadedFacilities() != null) {
//                            showBottomSheet(mUploadInfos.get(currIndex).getUploadedFacilities(), mUploadInfos.get(currIndex).getCompleteTableInfo());
//                        }
//                        if (currIndex == (mUploadInfos.size() - 1)) {
//                            btn_next.setVisibility(View.GONE);
//                        }
//                        if (currIndex > 0) {
//                            btn_prev.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//        RxView.clicks(btn_prev)
//                .throttleFirst(500, TimeUnit.MILLISECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        currIndex--;
//                        if (currIndex < 0) {
//                            btn_prev.setVisibility(View.GONE);
//                            return;
//                        }
//                        component_detail_container.removeAllViews();
//                        tableItemContainer.removeAllViews();
//                        approvalOpinionContainer.removeAllViews();
//                        resetStatus(true);
//                        mCurrentCompleteTableInfo = mUploadInfos.get(currIndex).getCompleteTableInfo();
//                        if (mCurrentCompleteTableInfo != null) {
//                            tv_error_correct.setVisibility(View.VISIBLE);
//                        }
//                        if (mUploadInfos.get(currIndex).getModifiedFacilities() != null) {
//                            showBottomSheet(mUploadInfos.get(currIndex).getModifiedFacilities(), mUploadInfos.get(currIndex).getCompleteTableInfo());
//                        } else if (mUploadInfos.get(currIndex).getUploadedFacilities() != null) {
//                            showBottomSheet(mUploadInfos.get(currIndex).getUploadedFacilities(), mUploadInfos.get(currIndex).getCompleteTableInfo());
//                        }
//
//                        if (currIndex == 0) {
//                            btn_prev.setVisibility(View.GONE);
//                        }
//                        if (mUploadInfos.size() > 1) {
//                            btn_next.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });

        loadMap();


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

        //门牌号图层按钮
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
                    layerPresenter.changeLayerVisibility(PatrolLayerPresenter.DOOR_NO_LAYER, ifUploadLayerVisible);
                }
            }
        });

        //注册当图层可见度发生改变时的回调
        if (layerPresenter != null) {
            layerPresenter.registerLayerVisibilityChangedListener(new PatrolLayerPresenter.OnLayerVisibilityChangedListener() {
                @Override
                public void changed(boolean visible, LayerInfo layerInfo) {
                    if (!visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.DOOR_NO_LAYER)) {
                        //不可见
                        //  myUploadIv.setImageResource(R.drawable.ic_upload_data_normal2);
                        uploadIv.setChecked(false);
                        tv_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.invisible_state_text_color,null));
                        ifUploadLayerVisible = false;
                    } else if (visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.DOOR_NO_LAYER)) {
                        //可见
                        // myUploadIv.setImageResource(R.drawable.ic_upload_data_pressed);
                        uploadIv.setChecked(true);
                        tv_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent,null));
                        ifUploadLayerVisible = true;
                    }else
                    if (!visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.MY_UPLOAD_LAYER_NAME)) {
                        //不可见
                        //  myUploadIv.setImageResource(R.drawable.ic_upload_data_normal2);
                        myUploadIv.setChecked(false);
                        tv_my_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.invisible_state_text_color, null));
                        ifMyUploadLayerVisible = false;
                    } else if (visible && layerInfo.getLayerName().contains(PatrolLayerPresenter.MY_UPLOAD_LAYER_NAME)) {
                        //可见
                        // myUploadIv.setImageResource(R.drawable.ic_upload_data_pressed);
                        myUploadIv.setChecked(true);
                        tv_my_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                        ifMyUploadLayerVisible = true;
                    }
                }
            });
        }

        ll_reset = view.findViewById(R.id.ll_reset);

        RxView.clicks(ll_reset)
                .throttleFirst(1, TimeUnit.SECONDS)   //1秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (firstModifiedFacility != null) {
                            mUploadInfos.clear();
                            nextAndPrevContainer.setVisibility(View.GONE);
                            btn_next.setVisibility(View.GONE);
                            btn_prev.setVisibility(View.GONE);
                            currIndex = 0;
                            mCurrentUploadedDoorNo = null;
                            mCurrentCompleteTableInfo = null;
                            hasComponent = false;
                            resetStatus(true);
                            tableItemContainer.removeAllViews();
                            approvalOpinionContainer.removeAllViews();
                            component_detail_container.removeAllViews();
                            mCurrentCompleteTableInfo = firstCompleteTableInfo;
                            showBottomSheet(firstModifiedFacility, firstCompleteTableInfo);
                        } else if (firstUploadedDoorNo != null) {

                            mUploadInfos.clear();
                            nextAndPrevContainer.setVisibility(View.GONE);
                            btn_next.setVisibility(View.GONE);
                            btn_prev.setVisibility(View.GONE);
                            currIndex = 0;
//                            mCurrentUploadedDoorNo = null;
                            mCurrentCompleteTableInfo = null;
                            hasComponent = false;
                            resetStatus(true);
                            tableItemContainer.removeAllViews();
                            approvalOpinionContainer.removeAllViews();
                            component_detail_container.removeAllViews();
                            mCurrentCompleteTableInfo = firstCompleteTableInfo;
                            showBottomSheet(mCurrentUploadedDoorNo, firstCompleteTableInfo);
                        }
                    }
                });


       /* ll_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstModifiedFacility != null) {
                    mUploadInfos.clear();
                    nextAndPrevContainer.setVisibility(View.GONE);
                    btn_next.setVisibility(View.GONE);
                    btn_prev.setVisibility(View.GONE);
                    currIndex = 0;
//                    mCurrentModifiedFacility = null;
                    mCurrentUploadedDoorNo = null;
                    mCurrentCompleteTableInfo = null;
                    hasComponent = false;
//                    tv_error_correct.setVisibility(View.VISIBLE);
                    resetStatus(true);
                    tableItemContainer.removeAllViews();
                    approvalOpinionContainer.removeAllViews();
                    component_detail_container.removeAllViews();
                    mCurrentCompleteTableInfo = firstCompleteTableInfo;
                    showBottomSheet(firstModifiedFacility, firstCompleteTableInfo);
                } else if (firstUploadedDoorNo != null) {

                    mUploadInfos.clear();
                    nextAndPrevContainer.setVisibility(View.GONE);
                    btn_next.setVisibility(View.GONE);
                    btn_prev.setVisibility(View.GONE);
                    currIndex = 0;
//                    mCurrentModifiedFacility = null;
                    mCurrentUploadedDoorNo = null;
                    mCurrentCompleteTableInfo = null;
                    hasComponent = false;
//                    tv_error_correct.setVisibility(View.VISIBLE);
                    resetStatus(true);
                    tableItemContainer.removeAllViews();
                    approvalOpinionContainer.removeAllViews();
                    component_detail_container.removeAllViews();
                    mCurrentCompleteTableInfo = firstCompleteTableInfo;
                    showBottomSheet(firstUploadedDoorNo, firstCompleteTableInfo);
                }
            }
        });*/

        initBottomSheetView();


        //再次编辑按钮
        btnReEdit = view.findViewById(R.id.btn_reedit);
        RxView.clicks(btnReEdit)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mCurrentUploadedDoorNo != null) {
                            Intent intent = new Intent(mContext, UploadNewDoorNoActivity.class);
                            intent.putExtra("data", mCurrentUploadedDoorNo);
                            getActivity().startActivityForResult(intent, 124);
                        }
//                        } else if (mCurrentUploadedDoorNo != null) { //2018 0424 没看懂 sdb
//                            Intent intent = new Intent(mContext, PSHUploadNewDoorNoActivity.class);
//                            intent.putExtra("data", mCurrentUploadedDoorNo);
//                            startActivity(intent);
//                            startActivity(intent);
//                        }
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
//        RxView.clicks(btnFeedback)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        if (mCurrentModifiedFacility != null) {
//                            Intent intent = new Intent(mContext, FacilityFeedbackActivity.class);
//                            intent.putExtra("id", mCurrentModifiedFacility.getId().longValue());
//                            intent.putExtra("tableType", "1");
//                            intent.putExtra("objectid", mCurrentModifiedFacility.getObjectId());
//                            startActivity(intent);
//                        } else if (mCurrentUploadedDoorNo != null) {
//                            Intent intent = new Intent(mContext, FacilityFeedbackActivity.class);
//                            intent.putExtra("id", mCurrentUploadedDoorNo.getId().longValue());
//                            intent.putExtra("tableType", "0");
//                            intent.putExtra("objectid", mCurrentUploadedDoorNo.getObjectId());
//                            startActivity(intent);
//                        }
//                    }
//                });
//        llGoFeedBackList = view.findViewById(R.id.ll_go_feedback_list);
//        llGoFeedBackList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mCurrentModifiedFacility != null) {
//                    Intent intent = new Intent(mContext, FacilityFeedbackListActivity.class);
//                    intent.putExtra("id", mCurrentModifiedFacility.getId().longValue());
//                    intent.putExtra("tableType", "1");
//                    intent.putExtra("objectid", mCurrentModifiedFacility.getObjectId());
//                    startActivity(intent);
//                } else if (mCurrentUploadedDoorNo != null) {
//                    Intent intent = new Intent(mContext, FacilityFeedbackListActivity.class);
//                    intent.putExtra("id", mCurrentUploadedDoorNo.getId().longValue());
//                    intent.putExtra("tableType", "0");
//                    intent.putExtra("objectid", mCurrentUploadedDoorNo.getObjectId());
//                    startActivity(intent);
//                }
//            }
//        });
    }

    /**
     * 删除设施
     */
    private void deleteFacility() {
        initmUploadDoorNoService();

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("删除中.....");
        progressDialog.show();
        mUploadDoorNoService.deleteDoorNo(mCurrentUploadedDoorNo
                .getsGuid())
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
                        ToastUtil.iconShortToast(mContext, R.mipmap.ic_alert_yellow, "删除失败，请稍后重试");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (responseBody.getCode() == 200) {
                            //清空界面
                            initGLayer();
                            hideBottomSheet();
                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new RefreshMyModificationListEvent());
                            /**
                             * 判断是否是通过列表点击进入的记录，如果是，那么此时需要隐藏“原”按钮
                             */
                            if (firstModifiedFacility != null
//                                        && mCurrentModifiedFacility != null
                                    && firstModifiedFacility.getId() != null)
//                                        && firstModifiedFacility.getId().equals(mCurrentModifiedFacility.getId()))
                            {
                                ll_reset.setVisibility(View.GONE);
                            }

                            if (mContext instanceof UploadDoorNoDetailActivity) {
                                ((UploadDoorNoDetailActivity) mContext).finish();
                            }
                        } else if (responseBody.getCode() == 400) {
                            Toast.makeText(mContext, responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
                                    BaseInfoManager.getUserName(mContext) + "原因是：" + responseBody.getMessage()));
                            Toast.makeText(mContext, responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void initmUploadDoorNoService() {
        if (mUploadDoorNoService == null) {
            mUploadDoorNoService = new UploadFacilityService(mContext);
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
//            showOnBottomSheet(uploadInfos);
        }
    }

    public void queryCompoenntInfo(final UploadedFacility modifiedIdentification) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("查询详细信息中.....");
        progressDialog.show();
        UploadLayerService identificationService = new UploadLayerService(mContext);
        identificationService.queryComponent(modifiedIdentification, new Callback2<CompleteTableInfo>() {
            @Override
            public void onSuccess(CompleteTableInfo completeTableInfo) {
                progressDialog.dismiss();
                List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
                UploadInfo uploadInfo = new UploadInfo();
                uploadInfo.setCompleteTableInfo(completeTableInfo);
                uploadInfo.setUploadedFacilities(modifiedIdentification);
                uploadInfos.add(uploadInfo);
//                showOnBottomSheet(uploadInfos);
            }

            @Override
            public void onFail(Exception error) {
                progressDialog.dismiss();
                List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
                UploadInfo uploadInfo = new UploadInfo();
                uploadInfo.setUploadedFacilities(modifiedIdentification);
                uploadInfos.add(uploadInfo);
//                showOnBottomSheet(uploadInfos);
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
        IDrawerController drawerController = (IDrawerController) mContext;
        layerView = new PatrolLayerView2(mContext, mMapView, drawerController.getDrawerContainer());
        layerPresenter = new PatrolLayerPresenter(layerView, new DoorNoLayerService(mContext.getApplicationContext()));
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
        this.mContext = context;
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
            if (mBehavior.getState() == IBottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(IBottomSheetBehavior.STATE_COLLAPSED);
            } else if (mBehavior.getState() == IBottomSheetBehavior.STATE_COLLAPSED
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
            if (scale < LayerUrlConstant.MIN_QUERY_SCALE && (ifUploadLayerVisible || ifMyUploadLayerVisible)) {
//                final Point point = mMapView.toMapPoint(e.getX(), e.getY());
//                query(point.getX(), point.getY());

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
        pd = new ProgressDialog(mContext);
        pd.setMessage("正在查询上报信息...");
        pd.show();
        mUploadInfos.clear();
        nextAndPrevContainer.setVisibility(View.GONE);
        btn_next.setVisibility(View.GONE);
        btn_prev.setVisibility(View.GONE);
        currIndex = 0;
        //        final Point point = mMapView.toMapPoint(x, y);
        final Point point = new Point(x, y);

//        mCurrentModifiedFacility = null;
        mCurrentUploadedDoorNo = null;
        mCurrentCompleteTableInfo = null;
        hasComponent = false;
//        tv_error_correct.setVisibility(View.VISIBLE);
        //tv_sure.setVisibility(View.GONE);
        resetStatus(true);
        if (ifUploadLayerVisible) {
            mUploadLayerService.setQueryByWhere(getDistrictQueryWhere());
        } else if (ifMyUploadLayerVisible) {
            mUploadLayerService.setQueryByWhere(getQueryWhere());
        }
        mUploadLayerService.queryUploadDataInfo(point, mMapView.getSpatialReference(), mMapView.getResolution(), new Callback2<List<UploadInfo>>() {
            @Override
            public void onSuccess(List<UploadInfo> uploadInfos) {
                pd.dismiss();
                if (ListUtil.isEmpty(uploadInfos) || (uploadInfos.size() == 1 && uploadInfos.get(0).getUploadedFacilities() == null && uploadInfos.get(0).getModifiedFacilities() == null)) {
                    ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "查无数据！");
                    return;
                }
                mUploadInfos = uploadInfos;
//                showOnBottomSheet(uploadInfos);
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
        if (district.contains("净水")) {
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

//    private void showOnBottomSheet(List<UploadInfo> uploadInfos) {
//        if (uploadInfos.size() > 1) {
//            nextAndPrevContainer.setVisibility(View.VISIBLE);
//            btn_next.setVisibility(View.VISIBLE);
//        }
//        if (mMapView.getCallout().isShowing()) {
//            mMapView.getCallout().animatedHide();
//        }
//
//        component_detail_container.removeAllViews();
//        tableItemContainer.removeAllViews();
//        approvalOpinionContainer.removeAllViews();
//
//        //隐藏marker
//        locationMarker.setVisibility(View.GONE);
//        initGLayer();
//        Geometry geometry = null;
//        mCurrentCompleteTableInfo = uploadInfos.get(0).getCompleteTableInfo();
//        if (uploadInfos.get(0).getUploadedFacilities() != null) {
//            geometry = new Point(uploadInfos.get(0).getUploadedFacilities().getX(), uploadInfos.get(0).getUploadedFacilities().getY());
//            showBottomSheet(uploadInfos.get(0).getUploadedFacilities(), uploadInfos.get(0).getCompleteTableInfo());
//        } else if (uploadInfos.get(0).getModifiedFacilities() != null) {
//
//            showBottomSheet(uploadInfos.get(0).getModifiedFacilities(), uploadInfos.get(0).getCompleteTableInfo());
//        }
//        if (geometry != null) {
//            drawGeometry(geometry, mGLayer, true, true);
//        }
//    }

    /**
     * 纠错数据
     * 加载设施信息，显示中底部BottomSheet中
     */
    private void showBottomSheet(final ModifiedFacility modifiedFacility, CompleteTableInfo completeTableInfo) {
        //initGLayer();
        if (modifiedFacility == null) {
            return;
        }


        /**
         * “未审核”和“存在疑问”可以编辑与删除，“审核通过”不允许编辑和删除
         */
        if (CheckState.UNCHECK.equals(modifiedFacility.getCheckState()) ||
                CheckState.IN_DOUBT.equals(modifiedFacility.getCheckState())) {
            btnReEdit.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(modifiedFacility.getCheckState()) && modifiedFacility.getId() != -1) {
            //当还未插入到设施图层时也允许删除
            btnReEdit.setVisibility(View.VISIBLE);
        } else {
            btnReEdit.setVisibility(View.GONE);
        }

        /**
         * “未审核”和“存在疑问”可以编辑与删除，“审核通过”不允许编辑和删除
         */
        if (!CheckState.SUCCESS.equals(modifiedFacility.getCheckState()) && modifiedFacility.getId() != -1) {
            btnDelete.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(modifiedFacility.getCheckState()) && modifiedFacility.getId() != -1) {
            //当还未插入到设施图层时也允许删除
            btnDelete.setVisibility(View.VISIBLE);
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
            ll_feedback.setVisibility(View.GONE);
            btnFeedback.setVisibility(View.GONE);
//            llGoFeedBackList.setVisibility(View.GONE);
        }

        component_detail_container.setVisibility(View.VISIBLE);

        if (component_detail_container.getChildCount() == 0) {

            /**
             * 是否显示原部件
             */
            if (mCurrentCompleteTableInfo != null) {
                tv_error_correct.setVisibility(View.GONE);
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

//            mCurrentModifiedFacility = modifiedFacility;
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
            MyModifiedFacilityTableViewManager modifiedIdentificationTableViewManager = new MyModifiedFacilityTableViewManager(mContext,
                    modifiedFacility, true);
            modifiedIdentificationTableViewManager.addTo(component_detail_container);
            if (completeTableInfo != null) {
                modifiedIdentificationTableViewManager.setReadOnly(modifiedFacility, completeTableInfo.getAttrs());
            } else {
                modifiedIdentificationTableViewManager.setReadOnly(modifiedFacility, null);
            }
            if (mBehavior.getState() == IBottomSheetBehavior.STATE_COLLAPSED) {
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
//                showBottomSheet(mCurrentModifiedFacility, mCurrentCompleteTableInfo);

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
                    showBottomSheet(firstUploadedDoorNo);

            }
        });
        tv_approval_opinion_list.setVisibility(View.VISIBLE);
    }

    private void showBottomSheet(UploadDoorNoDetailBean firstUploadedDoorNo) {
        if (approvalOpinionContainer.getChildCount() == 0) {
            UploadDoorNoApprovalViewManager viewManager = new UploadDoorNoApprovalViewManager(mContext, firstUploadedDoorNo);
            viewManager.addTo(approvalOpinionContainer);
        }


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


    /**
     * 高亮tab文字
     *
     * @param textView
     */
    private void highlight(TextView textView) {
        textView.setBackground(getResources().getDrawable(R.drawable.round_blue_rectangle));
        textView.setTextColor(getResources().getColor(R.color.agmobile_white));
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
    private void showBottomSheet(final UploadDoorNoDetailBean uploadedDoorNo, CompleteTableInfo completeTableInfo) {
//        mCurrentModifiedFacility = null;
        //initGLayer();
        if (uploadedDoorNo == null) {
            return;
        }
        mCurrentUploadedDoorNo = uploadedDoorNo;

        /**
         * “未审核”和“存在疑问”可以编辑与删除，“审核通过”不允许编辑和删除
         */
        if (CheckState.UNCHECK.equals(uploadedDoorNo.getState()) || CheckState.IN_DOUBT.equals(uploadedDoorNo.getState())) {
            btnReEdit.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(uploadedDoorNo.getCheckState()) && !TextUtils.isEmpty(uploadedDoorNo.getsGuid())) {
            btnReEdit.setVisibility(View.VISIBLE);
        } else {
            btnReEdit.setVisibility(View.GONE);
        }


        /**
         * “未审核”和“存在疑问”可以编辑与删除，“审核通过”不允许编辑和删除
         */


        if (!CheckState.SUCCESS.equals(uploadedDoorNo.getCheckState()) && !TextUtils.isEmpty(uploadedDoorNo.getsGuid())) {
            btnDelete.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(uploadedDoorNo.getCheckState()) && !TextUtils.isEmpty(uploadedDoorNo.getsGuid())) {
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        /**
         * 审核通过后允许反馈
         */
        if (CheckState.SUCCESS.equals(uploadedDoorNo.getCheckState()) && !TextUtils.isEmpty(uploadedDoorNo.getsGuid())) {
            ll_feedback.setVisibility(View.VISIBLE);
            btnFeedback.setVisibility(View.VISIBLE);
//            llGoFeedBackList.setVisibility(View.VISIBLE);
        } else {
            ll_feedback.setVisibility(View.GONE);
            btnFeedback.setVisibility(View.GONE);
//            llGoFeedBackList.setVisibility(View.GONE);
        }

        /**
         * 上报信息按钮
         */

//        if (uploadedFacility.getIsBinding() == 1 && mCurrentCompleteTableInfo != null) {
//            tv_error_correct.setVisibility(View.VISIBLE);
//            hasComponent = true;
//        } else {
//            tv_error_correct.setVisibility(View.GONE);
//            hasComponent = false;
//        }


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
            Geometry geometry = new Point(uploadedDoorNo.getZxjd(), uploadedDoorNo.getZxwd());
            drawGeometry(geometry, mGLayer, true, true);
            UploadDoorNoTableViewManager modifiedIdentificationTableViewManager = null;
            if (completeTableInfo != null) {
                modifiedIdentificationTableViewManager = new UploadDoorNoTableViewManager(mContext,
                        uploadedDoorNo, completeTableInfo.getAttrs());
            } else {
                modifiedIdentificationTableViewManager = new UploadDoorNoTableViewManager(mContext,
                        uploadedDoorNo);
            }
            modifiedIdentificationTableViewManager.addTo(component_detail_container);
            if (mBehavior.getState() == IBottomSheetBehavior.STATE_COLLAPSED) {
                component_detail_container.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBehavior.setState(IBottomSheetBehavior.STATE_ANCHOR);
                    }
                }, 100);
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

        /**
         * 如果是雨水口，显示特性：方形
         */
        String layertype = "";
//        if (mCurrentModifiedFacility != null) {
//            layertype = mCurrentModifiedFacility.getLayerName();
//        } else if (mCurrentUploadedDoorNo != null) {
//            layertype = mCurrentUploadedDoorNo.getLayerName();//TODO
//        }


//        if (mCurrentModifiedFacility != null) {
//            double originX = mCurrentModifiedFacility.getOriginX();
//            double originY = mCurrentModifiedFacility.getOriginY();
//            double x = mCurrentModifiedFacility.getX();
//            double y = mCurrentModifiedFacility.getY();
//            initGLayerForOriginLocation();
//            new OriginalAttributesViewManager(getActivity(),
//                    tableItemContainer, mCurrentCompleteTableInfo, mMapView, originX, originY, layertype, mGLayerForOriginLocation);
//            return;
//        }
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
        } else if (requestCode == 124 && resultCode == 124) {
            mCurrentUploadedDoorNo = (UploadDoorNoDetailBean) data.getSerializableExtra("doorBean");
            component_detail_container.removeAllViews();
            showBottomSheet(mCurrentUploadedDoorNo, null);
            EventBus.getDefault().post(new RefreshMyModificationListEvent());
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
//                            mCurrentModifiedFacility = null;
                            mCurrentUploadedDoorNo = null;
                            mCurrentCompleteTableInfo = null;
                            hasComponent = false;
                            tv_error_correct.setVisibility(View.GONE);
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
//                            mCurrentModifiedFacility = null;
                            mCurrentUploadedDoorNo = null;
                            mCurrentCompleteTableInfo = null;
                            hasComponent = false;
                            tv_error_correct.setVisibility(View.GONE);
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
//            final UploadedFacility uploadedFacility = uploadFacilitySuccessEvent.getUploadedFacility();
//            //进行查询图片，因为如果再次编辑的时候增加了图片，如果再次编辑这样图片的时候需要这张图片在服务端的id
//            final ProgressDialog progressDialog = new ProgressDialog(mContext);
//            progressDialog.setMessage("正在更新设施信息");
//            progressDialog.show();
//
//            PSHUploadFacilityService uploadFacilityService = new PSHUploadFacilityService(mContext);
//            uploadFacilityService.getUploadDetail(uploadedFacility.getId())
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
//                            mCurrentUploadedDoorNo = null;
//                            mCurrentCompleteTableInfo = null;
//                            hasComponent = false;
//                            tv_error_correct.setVisibility(View.VISIBLE);
//                            resetStatus(true);
//                            uploadedFacility.getPhotos().clear();
//                            showMissedComponent(uploadedFacility);
//                            /**
//                             * 判断是否是通过点击进来的数据，如果是，那么更新
//                             */
//                            refreshFirstUploadDoorNo(uploadedFacility);
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
//                            mCurrentUploadedDoorNo = null;
//                            mCurrentCompleteTableInfo = null;
//                            hasComponent = false;
//                            tv_error_correct.setVisibility(View.VISIBLE);
//                            resetStatus(true);
//
//                            showMissedComponent(uploadedFacility1);
//                            /**
//                             * 判断是否是通过点击进来的数据，如果是，那么更新
//                             */
//                            refreshFirstUploadDoorNo(uploadedFacility1);
//                        }
//                    });

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

    private void refreshFirstUploadDoorNo(UploadDoorNoDetailBean uploadedDoorNo) {
        if (firstUploadedDoorNo != null) {
            if (firstUploadedDoorNo.getsGuid() != null && firstUploadedDoorNo.getsGuid().equals(uploadedDoorNo.getsGuid())) {
                firstUploadedDoorNo = uploadedDoorNo;
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
            ll_layer_url_init_fail.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 网络切换时，若设施URL为null，会重新初始化设施URL，然后发送OnInitLayerUrlFinishEvent事件
     *
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

    @Subscribe
    public void onReceivedUploadFacilityEvent(UploadDoorNoSuccessEvent uploadFacilitySuccessEvent) {
        if(uploadFacilitySuccessEvent != null) {
            mUploadInfos.clear();
            nextAndPrevContainer.setVisibility(View.GONE);
            btn_next.setVisibility(View.GONE);
            btn_prev.setVisibility(View.GONE);
            currIndex = 0;
//                            mCurrentUploadedDoorNo = null;
            mCurrentCompleteTableInfo = null;
            hasComponent = false;
            resetStatus(true);
            tableItemContainer.removeAllViews();
            approvalOpinionContainer.removeAllViews();
            component_detail_container.removeAllViews();
            mCurrentCompleteTableInfo = firstCompleteTableInfo;
            showBottomSheet(uploadFacilitySuccessEvent.getUploadedFacility(),mCurrentCompleteTableInfo);
        }
    }

}
