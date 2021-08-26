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
import android.content.res.ColorStateList;
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
import com.augurit.agmobile.gzps.common.util.DictionarySortUtil;
import com.augurit.agmobile.gzps.common.widget.CheckBoxItem;
import com.augurit.agmobile.gzps.common.widget.NumberItemTableItem;
import com.augurit.agmobile.gzps.common.widget.SpinnerTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.ComponetListAdapter;
import com.augurit.agmobile.gzps.componentmaintenance.service.ComponentService;
import com.augurit.agmobile.gzps.uploadfacility.model.CheckState;
import com.augurit.agmobile.gzps.uploadfacility.model.CompleteTableInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.PipeBean;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.service.DeleteFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.FeedbackFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadDetailLayerService;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadLayerService;
import com.augurit.agmobile.gzps.uploadfacility.view.UploadFacilitySuccessEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.approvalopinion.PipeOpinionViewManager;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.OriginalPipeViewManager;
import com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist.RefreshPipeListEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.PipeTableViewManager;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.UploadFacilityTableViewManager;
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
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
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
import com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior;
import com.augurit.am.cmpt.widget.spinner.AMSpinner;
import com.augurit.am.fw.utils.DensityUtil;
import com.augurit.am.fw.utils.DrawableUtil;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
public class UploadPipeDetailMapFragment extends Fragment implements View.OnClickListener {

    private static final String KEY_MAP_STATE = "com.esri.MapState";

    private LocationMarker locationMarker;

    private PipeBean mCurrentModifiedFacility;
    private UploadedFacility mCurrentUploadedFacility;
    private PipeBean mCurrentCompleteTableInfo;
    private boolean isShiPaiOrKexuecheng;
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

    private PipeBean firstUploadedFacility;
    private PipeBean firstModifiedFacility;
    private PipeBean firstCompleteTableInfo;

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
    /**
     * 设施原位置绘制图层
     */
    private GraphicsLayer mGLayerForOriginLocation;
    private ComponentService componentService;
    private Geometry mGeometry;
    private ViewGroup pipe_view;
    private int type = 1;
    private Button btn_save2;
    private Button btn_cancel2;
    private SpinnerTableItem sp_ywlx;
    private SpinnerTableItem sp_lx;

    private UploadFacilityService mUploadFacilityService;
    private GraphicsLayer mArrowGLayer;
    private TextView pipe_title;
    private boolean mIsAffair;
    private SpinnerTableItem sp_gj;
    String mGjCode = "";
    private NumberItemTableItem sp_gxcd;
    private CheckBoxItem cb_isFacilityOfRedLine;
    private static int isShowCancelDeleteButton;

    public static UploadPipeDetailMapFragment getInstance(Bundle data) {
        UploadPipeDetailMapFragment addComponentFragment2 = new UploadPipeDetailMapFragment();
        addComponentFragment2.setArguments(data);
        isShowCancelDeleteButton = data.getInt("isShowCancelDeleteButton", 0);
        return addComponentFragment2;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(mContext, R.layout.fragment_pipemap_distribute, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        pipe_view = (ViewGroup) view.findViewById(R.id.pipe_view);
        pipe_title = (TextView) view.findViewById(R.id.pipe_title);
        pipe_title.setText("修改管线信息");
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
                        if (myUploadLayerBtn != null) {
                            myUploadLayerBtn.performClick();
                        }
                        if (layerPresenter != null) {
                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter.DRAINAGE_PIPELINE, true);
                        }
                        // layerPresenter.changeLayerVisibility(PatrolLayerPresenter.LOCAL_UPLOAD_LAYER_NAME, true);

                        if (getArguments() != null
                                && getArguments().getParcelable("uploadedFacility") != null) {
                            PipeBean uploadedFacility = (PipeBean) getArguments().getParcelable("uploadedFacility");
                            mIsAffair = (boolean) getArguments().getBoolean("isAffair", false);
                            isShiPaiOrKexuecheng = (boolean) getArguments().getBoolean("isShiPaiOrKexuecheng", false);
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
                                showBottomSheet(uploadedFacility);
//                                queryForObjctId(firstUploadedFacility.getObjectId());
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
        ll_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstUploadedFacility != null) {
                    Polyline polyline = new Polyline();
                    if (firstUploadedFacility.getStartXcoord() > 0 && firstUploadedFacility.getStartYcoord() > 0 && firstUploadedFacility.getEndXcoord() > 0 && firstUploadedFacility.getEndYcoord() > 0) {
                        polyline.startPath(firstUploadedFacility.getStartXcoord(), firstUploadedFacility.getStartYcoord());
                        polyline.lineTo(firstUploadedFacility.getEndXcoord(), firstUploadedFacility.getEndYcoord());
                        initGLayer();
                        drawGeometry(polyline, mGLayer, true, true);
                    }
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
                        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        drawPipeline(mGeometry, firstUploadedFacility, R.color.agmobile_red);
                        showPopWindow(firstUploadedFacility);
                    }
                });

        //删除按钮
        btnDelete = view.findViewById(R.id.btn_delete);
//        RxView.clicks(btnDelete)
//                .throttleFirst(500, TimeUnit.MILLISECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        DialogUtil.MessageBox(mContext, "提醒", "是否确定要删除这条记录？", new DialogInterface.OnClickListener() {
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

        llGoFeedBackList = view.findViewById(R.id.ll_go_feedback_list);
    }

    /**
     * 撤销删除设施
     */
    private void deleteFacility() {
        initDeleteFacilityService();
        if (firstUploadedFacility != null) {
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("  撤销删除中.....");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            deleteFacilityService.deleteReportLinePipe(firstUploadedFacility.getId())
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
                            ToastUtil.iconShortToast(mContext, R.mipmap.ic_alert_yellow, "撤销删除失败，请稍后重试");
                        }

                        @Override
                        public void onNext(Result2<String> s) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            if (s.getCode() == 200) {
                                //清空界面
                                Toast.makeText(mContext, "撤销删除成功", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new RefreshPipeListEvent(type));
                                EventBus.getDefault().post(new UploadFacilitySuccessEvent(new ModifiedFacility()));
                                ((Activity) mContext).finish();
                                //ToastUtil.shortToast(mContext,"撤销删除成功");
                                /**
                                 * 判断是否是通过列表点击进入的记录，如果是，那么此时需要隐藏“原”按钮
                                 */

                            } else {
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
            return;
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
//        }
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

        mCurrentModifiedFacility = null;
        mCurrentUploadedFacility = null;
        mCurrentCompleteTableInfo = null;
        hasComponent = false;
        tv_error_correct.setVisibility(View.VISIBLE);
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
                symbol = new SimpleLineSymbol(Color.RED, 4);
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
    }

    /**
     * 纠错数据
     * 加载设施信息，显示中底部BottomSheet中
     */
    private void showBottomSheet(final PipeBean pipeBean) {
        //initGLayer();
        if (pipeBean == null) {
            return;
        }

        Polyline polyline = new Polyline();
        if (pipeBean.getStartXcoord() > 0 && pipeBean.getStartYcoord() > 0 && pipeBean.getEndXcoord() > 0 && pipeBean.getEndYcoord() > 0) {
            polyline.startPath(pipeBean.getStartXcoord(), pipeBean.getStartYcoord());
            polyline.lineTo(pipeBean.getEndXcoord(), pipeBean.getEndYcoord());
            initGLayer();
            drawGeometry(polyline, mGLayer, true, true);
        }
        /**
         * “未审核”和“存在疑问”可以编辑与删除，“审核通过”不允许编辑和删除
         */
        if (CheckState.UNCHECK.indexOf(pipeBean.getCheckState()) != -1 ||
                CheckState.IN_DOUBT.indexOf(pipeBean.getCheckState()) != -1) {
            btnReEdit.setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(pipeBean.getCheckState()) && !StringUtil.isEmpty(pipeBean.getId())) {
            //当还未插入到设施图层时也允许删除
            btnReEdit.setVisibility(View.GONE);
        } else {
            btnReEdit.setVisibility(View.GONE);
        }
        btnReEdit.setVisibility(View.VISIBLE);
        /**
         * “未审核”和“存在疑问”可以编辑与删除，“审核通过”不允许编辑和删除
         */
        if (!CheckState.SUCCESS.equals(pipeBean.getCheckState()) && !StringUtil.isEmpty(pipeBean.getId())) {
            btnDelete.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(pipeBean.getCheckState()) && !StringUtil.isEmpty(pipeBean.getId())) {
            //当还未插入到设施图层时也允许删除
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }
//        btnDelete.setVisibility(View.VISIBLE);
        if (mIsAffair || "3".equals(pipeBean.getLackType())) {
            btnReEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }
        //我的上报里面的，所有的记录删除功能去掉，只保留编辑功能
        if (isShowCancelDeleteButton == 1) {
            btnDelete.setVisibility(View.GONE);
        } else if (isShowCancelDeleteButton == 2) {
            if (!mIsAffair && "3".equals(pipeBean.getLackType())) {
                btnDelete.setVisibility(View.VISIBLE);
                ((Button) btnDelete).setText("撤销刪除");
                RxView.clicks(btnDelete)
                        .throttleFirst(500, TimeUnit.MILLISECONDS)
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                DialogUtil.MessageBox(mContext, "提醒", "是否确定要撤销删除这条记录？", new DialogInterface.OnClickListener() {
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
            } else {
                btnDelete.setVisibility(View.GONE);
            }

        } else {
            btnDelete.setVisibility(View.GONE);
        }
        component_detail_container.setVisibility(View.VISIBLE);
        if (component_detail_container.getChildCount() != 0) {
            component_detail_container.removeAllViews();
        }
        if (component_detail_container.getChildCount() == 0) {

            /**
             * 是否显示原部件
             */
            if (!StringUtil.isEmpty(pipeBean.getOldPipeType())) {
                tv_error_correct.setVisibility(View.VISIBLE);
                type = 2;
            } else {
                type = 1;
                tv_error_correct.setVisibility(View.GONE);
            }

            if (isShiPaiOrKexuecheng) {


                tv_error_correct.setVisibility(View.GONE);
                tv_approval_opinion_list.setVisibility(View.GONE);
            }
            /**
             * 是否显示审批意见
             */
//            if (ListUtil.isEmpty(modifiedFacility.getApprovalOpinions())) {
//                tv_approval_opinion_list.setVisibility(View.GONE);
//            } else {
//                tv_approval_opinion_list.setVisibility(View.VISIBLE);
//            }
//            tv_approval_opinion_list.setVisibility(View.GONE);
            mCurrentModifiedFacility = pipeBean;
            Geometry geometry = null;
            /**
             * 如果getX,getY不为空，说明修改过位置，那么默认用修改后的位置（依据：上报图层显示方式）
             */

//            geometry = new Point(pipeBean.getX(), pipeBean.getY());
//            if(mGeometry != null){
//                geometry = mGeometry;
//            }
            //Geometry geometry = new Point(modifiedFacility.getOriginX(), modifiedFacility.getOriginY());
//            initGLayer();
//            drawGeometry(geometry, mGLayer, true, true);
            hasComponent = true;
            map_bottom_sheet.setVisibility(View.VISIBLE);
            //component_detail_container.removeAllViews();
            PipeTableViewManager modifiedIdentificationTableViewManager = new PipeTableViewManager(mContext,
                    pipeBean, isShiPaiOrKexuecheng);
            modifiedIdentificationTableViewManager.addTo(component_detail_container);
            if (mBehavior.getState() == STATE_COLLAPSED) {
                component_detail_container.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBehavior.setState(com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior.STATE_ANCHOR);
                    }
                }, 300);
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
                showBottomSheet(mCurrentModifiedFacility);

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
                if (mCurrentModifiedFacility != null) {
                    showBottomSheetForOldPipe(mCurrentModifiedFacility);
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
                if (firstUploadedFacility != null) {
                    showBottomSheet(Long.valueOf(firstUploadedFacility.getId()));
                }
            }
        });
        tv_approval_opinion_list.setVisibility(View.VISIBLE);
    }

    private void showBottomSheet(Long markId) {
        if (approvalOpinionContainer.getChildCount() == 0) {
            PipeOpinionViewManager myModifiedFacilityTableViewManage = new PipeOpinionViewManager(mContext, markId);
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

        /**
         * “未审核”和“存在疑问”可以编辑与删除，“审核通过”不允许编辑和删除
         */
        if (CheckState.UNCHECK.indexOf(uploadedFacility.getCheckState()) != -1 ||
                CheckState.IN_DOUBT.indexOf(uploadedFacility.getCheckState()) != -1) {
            btnReEdit.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(uploadedFacility.getCheckState()) && uploadedFacility.getId() != -1) {
            btnReEdit.setVisibility(View.VISIBLE);
        } else {
            btnReEdit.setVisibility(View.GONE);
        }


        /**
         * “未审核”和“存在疑问”可以编辑与删除，“审核通过”不允许编辑和删除
         */
        if (!CheckState.SUCCESS.equals(uploadedFacility.getCheckState()) && uploadedFacility.getId() != -1) {
            btnDelete.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(uploadedFacility.getCheckState()) && uploadedFacility.getId() != -1) {
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }
        //我的上报里面的，所有的记录删除功能去掉，只保留编辑功能
//        if(isShowCancelDeleteButton == 1){
//            btnDelete.setVisibility(View.GONE);
//        }else if(isShowCancelDeleteButton == 2){
//            btnDelete.setVisibility(View.VISIBLE);
//            ((Button)btnDelete).setText("撤销刪除");
//            RxView.clicks(btnDelete)
//                    .throttleFirst(500, TimeUnit.MILLISECONDS)
//                    .subscribe(new Action1<Void>() {
//                        @Override
//                        public void call(Void aVoid) {
//                            DialogUtil.MessageBox(mContext, "提醒", "是否确定要撤销删除这条记录？", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    cancelDeleteFacility();
//                                }
//                            }, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//                                }
//                            });
//                        }
//                    });
//        }else{
//            btnDelete.setVisibility(View.GONE);
//        }
        /**
         * 审核通过后允许反馈
         */ // TODO: 2018/11/9 不用审批后的反馈
        if (CheckState.SUCCESS.equals(uploadedFacility.getCheckState()) && uploadedFacility.getId() != -1) {
//            ll_feedback.setVisibility(View.VISIBLE);
//            btnFeedback.setVisibility(View.VISIBLE);
//            llGoFeedBackList.setVisibility(View.VISIBLE);
            if ("排水口".equals(uploadedFacility.getLayerName())) {
                btnReEdit.setVisibility(View.VISIBLE);
            }
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
//            btnReEdit.setVisibility(View.GONE);
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
            UploadFacilityTableViewManager modifiedIdentificationTableViewManager = null;
            if (completeTableInfo != null) {
                modifiedIdentificationTableViewManager = new UploadFacilityTableViewManager(mContext,
                        uploadedFacility, completeTableInfo.getAttrs());
            } else {
                modifiedIdentificationTableViewManager = new UploadFacilityTableViewManager(mContext,
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
    private void showBottomSheetForOldPipe(final PipeBean pipeBean) {

        mCurrentCompleteTableInfo = pipeBean;
        //tableItemContainer.removeAllViews();
        if (pipeBean.getOldPipeType() == null) {
            return;
        }
        if (tableItemContainer.getChildCount() > 0) {
            return;
        }
        new OriginalPipeViewManager(getActivity(),
                tableItemContainer, mCurrentCompleteTableInfo);
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

//    @Subscribe
//    public void onReceivedUploadFacilityEvent(final UploadFacilitySuccessEvent uploadFacilitySuccessEvent) {
//
//
//        if (uploadFacilitySuccessEvent.getModifiedFacility() != null) {
//
//            final ModifiedFacility
//                    modifiedFacility = uploadFacilitySuccessEvent.getModifiedFacility();
//            //进行查询图片，因为如果再次编辑的时候增加了图片，如果再次编辑这样图片的时候需要这张图片在服务端的id
//            final ProgressDialog progressDialog = new ProgressDialog(mContext);
//            progressDialog.setMessage("正在更新设施信息");
//            progressDialog.show();
//
//            CorrectFacilityService correctFacilityService = new CorrectFacilityService(mContext);
//            correctFacilityService.getModificationById(modifiedFacility.getId())
////                    .map(new Func1<ServerAttachment, ModifiedFacility>() {
////                        @Override
////                        public ModifiedFacility call(ServerAttachment serverIdentificationAttachment) {
////                            List<ServerAttachment.ServerAttachmentDataBean> data = serverIdentificationAttachment.getData();
////                            if (!ListUtil.isEmpty(data)) {
////                                List<Photo> photos = new ArrayList<>();
////                                for (ServerAttachment.ServerAttachmentDataBean dataBean : data) {
////                                    Photo photo = new Photo();
////                                    photo.setId(Long.valueOf(dataBean.getId()));
////                                    photo.setPhotoPath(dataBean.getAttPath());
////                                    photo.setThumbPath(dataBean.getThumPath());
////                                    photos.add(photo);
////                                }
////                                modifiedFacility.setPhotos(photos);
////                            }
////                            return modifiedFacility;
////                        }
////                    })
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<ModifiedFacility>() {
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
////                            if (modifiedFacility != null) {
////                                CompleteTableInfo completeTableInfo = CompleteTableInfoUtil.getCompleteTableInfo(modifiedFacility);
////                                mCurrentCompleteTableInfo = completeTableInfo;
////                            }
////                            component_detail_container.removeAllViews();
////                            tableItemContainer.removeAllViews();
////                            approvalOpinionContainer.removeAllViews();
////                            showBottomSheet(modifiedFacility, mCurrentCompleteTableInfo);
//                            /**
//                             * 判断是否是通过列表点击进来的数据，如果是，那么更新
//                             */
//                            refreshFirstModifiedFacility(modifiedFacility);
//                        }
//
//                        @Override
//                        public void onNext(ModifiedFacility modifiedFacility) {
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
////                            if (uploadFacilitySuccessEvent.getModifiedFacility() != null) {
////                                CompleteTableInfo completeTableInfo = CompleteTableInfoUtil.getCompleteTableInfo(modifiedFacility);
////                                mCurrentCompleteTableInfo = completeTableInfo;
////                            }
////                            component_detail_container.removeAllViews();
////                            tableItemContainer.removeAllViews();
////                            approvalOpinionContainer.removeAllViews();
////                            showBottomSheet(modifiedFacility, mCurrentCompleteTableInfo);
//                            /**
//                             * 判断是否是通过列表点击进来的数据，如果是，那么更新
//                             */
//                            refreshFirstModifiedFacility(modifiedFacility);
//                        }
//                    });
//        } else if (uploadFacilitySuccessEvent.getUploadedFacility() != null) {
//            final UploadedFacility uploadedFacility = uploadFacilitySuccessEvent.getUploadedFacility();
//            //进行查询图片，因为如果再次编辑的时候增加了图片，如果再次编辑这样图片的时候需要这张图片在服务端的id
//            final ProgressDialog progressDialog = new ProgressDialog(mContext);
//            progressDialog.setMessage("正在更新设施信息");
//            progressDialog.show();
//
//            UploadFacilityService uploadFacilityService = new UploadFacilityService(mContext);
//            uploadFacilityService.getUploadFacilityById(uploadedFacility.getId())
////                    .map(new Func1<ServerAttachment, UploadedFacility>() {
////                        @Override
////                        public UploadedFacility call(ServerAttachment serverIdentificationAttachment) {
////                            List<ServerAttachment.ServerAttachmentDataBean> data = serverIdentificationAttachment.getData();
////                            if (!ListUtil.isEmpty(data)) {
////                                List<Photo> photos = new ArrayList<>();
////                                for (ServerAttachment.ServerAttachmentDataBean dataBean : data) {
////                                    Photo photo = new Photo();
////                                    photo.setId(Long.valueOf(dataBean.getId()));
////                                    photo.setPhotoPath(dataBean.getAttPath());
////                                    photo.setThumbPath(dataBean.getThumPath());
////                                    photos.add(photo);
////                                }
////                                uploadedFacility.setPhotos(photos);
////                            }
////                            return uploadedFacility;
////                        }
////                    })
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
//                            /**
//                             * 判断是否是通过点击进来的数据，如果是，那么更新
//                             */
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
//                            /**
//                             * 判断是否是通过点击进来的数据，如果是，那么更新
//                             */
//                            refreshFirstUploadFacility(uploadedFacility1);
//                        }
//                    });
//
//        } else {
//            initGLayer();
//            hideBottomSheet();
//        }
//
//    }

    private void refreshFirstModifiedFacility(ModifiedFacility modifiedFacility) {
//        if (firstModifiedFacility != null) {
//            if (firstModifiedFacility.getId() != null && firstModifiedFacility.getId().equals(modifiedFacility.getId())) {
//                firstModifiedFacility = modifiedFacility;
//            }
//        }
    }

    private void refreshFirstUploadFacility(UploadedFacility uploadedFacility) {
//        if (firstUploadedFacility != null) {
//            if (firstUploadedFacility.getId() != null && firstUploadedFacility.getId().equals(uploadedFacility.getId())) {
//                firstUploadedFacility = uploadedFacility;
//            }
//        }
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

    @NonNull
    private void initComponentService() {
        if (componentService == null) {
            componentService = new ComponentService(mContext.getApplicationContext());
        }
    }

    private void queryForObjctId(String objectId) {
        pd = new ProgressDialog(getContext());
        pd.setMessage("获取位置中...");
        pd.show();
        mComponentQueryResult.clear();
        btn_next.setVisibility(View.GONE);
        btn_prev.setVisibility(View.GONE);
        currIndex = 0;
        initComponentService();
        if (true) {
            componentService.queryPrimaryPipeForObjectId(objectId, new Callback2<List<Component>>() {
                @Override
                public void onSuccess(List<Component> components) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    if (ListUtil.isEmpty(components)) {
//                        ToastUtil.shortToast(getContext(), "获取定位失败");
                        return;
                    }
                    initGLayer();
                    Component component = components.get(0);
                    mGeometry = component.getGraphic().getGeometry();
                    if (mGeometry == null) {
//                        ToastUtil.shortToast(getContext(), "获取定位失败");
                        return;
                    }
                    drawGeometry(mGeometry, mGLayer, true, true);
                }

                @Override
                public void onFail(Exception error) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    ToastUtil.shortToast(getContext(), "获取定位失败");
                }
            });
        }
    }

    private void showPopWindow(final PipeBean component) {
        final PipeBean pipeBean = new PipeBean();
        String pipeType = "";
        String direction = "";
        String gjlx = "";
        boolean isFacilityOfRedLine = false;
        TableDBService dbService = new TableDBService(mContext);
        List<DictionaryItem> a163 = dbService.getDictionaryByTypecodeInDB("A163");
        pipeType = component.getPipeType();
        direction = component.getDirection();
        gjlx = component.getPipeGj();
        isFacilityOfRedLine = "1".equals(component.isSfpsdyhxn());
        pipeBean.setObjectId(component.getObjectId());
        final Map<String, Object> spinnerData = new LinkedHashMap<>();
        List<String> allowValues = new ArrayList<>();
        for (DictionaryItem dictionaryItem : a163) {
            allowValues.add(dictionaryItem.getName());
            spinnerData.put(dictionaryItem.getName(), dictionaryItem);
        }
        final Map<String, Object> spinnerData1 = new LinkedHashMap<>();
        spinnerData1.put("正向", "正向");
        spinnerData1.put("反向", "反向");
        btn_save2 = (Button) mView.findViewById(R.id.btn_save2);
        btn_cancel2 = (Button) mView.findViewById(R.id.btn_cancel2);
        sp_ywlx = (SpinnerTableItem) mView.findViewById(R.id.sp_ywlx);
        sp_gxcd = (NumberItemTableItem) mView.findViewById(R.id.sp_gxcd);
        if (component.getLineLength() != 0 && component.getLineLength() != -1) {
            sp_gxcd.setText(formatToSecond(component.getLineLength() + "", 2));
        } else {
            sp_gxcd.setText("");
        }
        sp_ywlx.setSpinnerData(spinnerData);
        if (!StringUtil.isEmpty(pipeType)) {
            sp_ywlx.selectItem(pipeType);
        }
        sp_lx = (SpinnerTableItem) mView.findViewById(R.id.sp_lx);
        sp_lx.setSpinnerData(spinnerData1);
        sp_lx.setOnSpinnerChangeListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                if (position == 0) {
                    pipeBean.setDirection("正向");
                } else {
                    pipeBean.setDirection("反向");
                }
                drawPipeline(mGeometry, pipeBean, R.color.agmobile_red);
//                    drawStreamam(componentCenter, mTempPipeBeans, R.color.agmobile_red);
            }
        });
        if (!StringUtil.isEmpty(direction)) {
            sp_lx.selectItem(direction);
        } else {
            sp_lx.selectItem(0);
        }
        List<DictionaryItem> a205 = dbService.getDictionaryByTypecodeInDB("A205");
        DictionarySortUtil.processOrder(a205);
        final Map<String, Object> gjData = new LinkedHashMap<>();
        List<String> gjValues = new ArrayList<>();
        String gjName = "";
        for (DictionaryItem dictionaryItem : a205) {
            if (dictionaryItem.getCode().equals(gjlx)) {
                gjName = dictionaryItem.getName();
            }
            gjValues.add(dictionaryItem.getName());
            gjData.put(dictionaryItem.getName(), dictionaryItem);
        }
        sp_gj = (SpinnerTableItem) mView.findViewById(R.id.sp_gj);

        sp_gj.setOnSpinnerChangeListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                DictionaryItem dictionaryItem = (DictionaryItem) item;
                mGjCode = dictionaryItem.getCode();
            }
        });
        sp_gj.setSpinnerData(gjData);
        if (!StringUtil.isEmpty(gjName)) {
            sp_gj.selectItem(gjName);
        } else {
            sp_gj.selectItem(0);
        }
        cb_isFacilityOfRedLine = mView.findViewById(R.id.cb_isFacilityOfRedLine);
        cb_isFacilityOfRedLine.setCheck(isFacilityOfRedLine);
        btn_save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pipeBean.setPipeType(sp_ywlx.getText());
                pipeBean.setDirection(sp_lx.getText());
                pipeBean.setPipeGj(mGjCode);
                pipeBean.setSfpsdyhxn(cb_isFacilityOfRedLine.isCheck() ? "1" : "0");
                if (!StringUtil.isEmpty(sp_gxcd.getText().trim())) {
                    pipeBean.setLineLength(Double.valueOf(sp_gxcd.getText().trim()));
                } else {
                    ToastUtil.shortToast(mContext, "管线长度不能为空");
                    return;
                }
                updateLinePipe(pipe_view, pipeBean);
            }
        });
        btn_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pipe_view.setVisibility(View.GONE);
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                initArrowGLayer();
            }
        });
        pipe_view.setVisibility(View.VISIBLE);
    }

    /**
     * 修改上报管线信息
     *
     * @param dialog
     * @param tempPipeBean
     */
    private void updateLinePipe(final ViewGroup dialog, final PipeBean tempPipeBean) {
        pd = new ProgressDialog(getContext());
        pd.setMessage("正在提交，请等待");
        pd.show();
        String dataJson = JsonUtil.getJson(tempPipeBean);
        if (mUploadFacilityService == null) {
            mUploadFacilityService = new UploadFacilityService(mContext);
        }
        mUploadFacilityService.updateReportLinePipe(firstUploadedFacility.getId(), dataJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        ToastUtil.shortToast(mContext, "提交失败");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        if (responseBody.getCode() == 200) {
                            refreshMap();
                            initArrowGLayer();
                            ToastUtil.shortToast(mContext, "提交成功");
                            EventBus.getDefault().post(new RefreshPipeListEvent(type));
                            EventBus.getDefault().post(new UploadFacilitySuccessEvent(new ModifiedFacility()));
                            firstUploadedFacility.setDirection(tempPipeBean.getDirection());
                            firstUploadedFacility.setPipeType(tempPipeBean.getPipeType());
                            firstUploadedFacility.setPipeGj(tempPipeBean.getPipeGj());
                            firstUploadedFacility.setLineLength(tempPipeBean.getLineLength());
                            firstUploadedFacility.setSfpsdyhxn(tempPipeBean.isSfpsdyhxn());
                            dialog.setVisibility(View.GONE);
                            showBottomSheet(firstUploadedFacility);
//                            component_detail_container.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                                }
//                            }, 800);
                        } else {
                            ToastUtil.shortToast(mContext, "保存失败，原因是：" + responseBody.getMessage());
                        }
                    }
                });
    }

    /**
     * 刷新地图
     */
    public void refreshMap() {
        Layer[] layers = mMapView.getLayers();
        for (Layer layer : layers) {
            if (layer instanceof ArcGISDynamicMapServiceLayer) {
                ((ArcGISDynamicMapServiceLayer) layer).refresh();
            }
        }
    }

    private void drawPipeline(Geometry component, PipeBean pipeBean, int color) {
        initArrowGLayer();
        if (component == null) {
            return;
        }
        Polyline polyline = (Polyline) component;
        Point startPoint = mMapView.toScreenPoint(polyline.getPoint(0));
        Point endPoint = mMapView.toScreenPoint(polyline.getPoint(polyline.getPointCount() - 1));
        double angle = GeometryUtil.getAngle(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
        if ("正向".equals(pipeBean.getDirection())) {
            drawArrow(GeometryUtil.getGeometryCenter(polyline), angle, color, false);
        } else {
            drawArrow(GeometryUtil.getGeometryCenter(polyline), angle, color, true);
        }
    }

    /**
     * 绘制箭头，请确保调用前已经调用了{@link #initGLayer()}方法
     *
     * @param geometryCenter   箭头摆放的位置
     * @param ifDrawRightArrow 是否绘制的是右箭头，true绘制右箭头，false绘制左箭头
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
        mArrowGLayer.addGraphic(graphic);
    }

    private void initArrowGLayer() {
        if (mArrowGLayer == null) {
            mArrowGLayer = new GraphicsLayer();
            mMapView.addLayer(mArrowGLayer);
        } else {
            mArrowGLayer.removeAll();
        }
    }

    private String formatToSecond(String value, int newScale) {
        if (StringUtil.isEmpty(value)) {
            return "";
        }
        Double num = Double.valueOf(value);
        BigDecimal bd = new BigDecimal(num);
        num = bd.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return num + "";
    }
}
