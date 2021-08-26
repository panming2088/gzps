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

package com.augurit.agmobile.gzps.jbjpsdy;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.OnInitLayerUrlFinishEvent;
import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.search.IOnSearchClickListener;
import com.augurit.agmobile.gzps.common.search.SearchFragment;
import com.augurit.agmobile.gzps.common.selectcomponent.LimitedLayerAdapter;
import com.augurit.agmobile.gzps.common.widget.CheckBoxItem;
import com.augurit.agmobile.gzps.common.widget.NumberItemTableItem;
import com.augurit.agmobile.gzps.common.widget.SpinnerTableItem;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.ComponetListAdapter;
import com.augurit.agmobile.gzps.componentmaintenance.SelectComponentEvent;
import com.augurit.agmobile.gzps.componentmaintenance.service.ComponentService;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.doorno.model.PSHAffairDetail;
import com.augurit.agmobile.gzps.doorno.view.PSHAffairDetailContract;
import com.augurit.agmobile.gzps.doorno.view.PSHAffairDetailPresenter;
import com.augurit.agmobile.gzps.doorno.view.SewerageTableActivity;
import com.augurit.agmobile.gzps.journal.JournalsDetailListActivity;
import com.augurit.agmobile.gzps.journal.service.JournalService;
import com.augurit.agmobile.gzps.measure.view.MapMeasureView;
import com.augurit.agmobile.gzps.monitor.WellMonitorListActivity;
import com.augurit.agmobile.gzps.uploadfacility.model.CheckResult;
import com.augurit.agmobile.gzps.uploadfacility.model.CompleteTableInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.DoubtBean;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.PipeBean;
import com.augurit.agmobile.gzps.uploadfacility.model.PsdyJbj;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadInfo;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.service.CorrectFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.DeleteFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.PsdyLayerFactory;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadLayerService;
import com.augurit.agmobile.gzps.uploadfacility.util.UploadLayerFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadfacility.view.GeometryDragOnTouchListener;
import com.augurit.agmobile.gzps.uploadfacility.view.MapDrawQuestionView;
import com.augurit.agmobile.gzps.uploadfacility.view.PipeStreamView;
import com.augurit.agmobile.gzps.uploadfacility.view.UploadFacilitySuccessEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.CorrectOrConfirimFacilityActivity;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.MyModifiedFacilityTableViewManager;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.RefreshMyModificationListEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.reeditfacility.ReEditUploadFacilityActivity;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.RefreshMyUploadList;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.UploadFacilityTableViewManager;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.UploadNewFacilityActivity;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.common.widget.compassview.CompassView;
import com.augurit.agmobile.mapengine.common.widget.scaleview.MapScaleView;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
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
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.widget.LocationButton;
import com.augurit.agmobile.patrolcore.editmap.widget.LocationMarker;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter4JbjPsdy;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView3;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.Callback4;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior;
import com.augurit.am.cmpt.widget.spinner.AMSpinner;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.DensityUtil;
import com.augurit.am.fw.utils.DeviceUtil;
import com.augurit.am.fw.utils.DrawableUtil;
import com.augurit.am.fw.utils.JsonUtil;
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
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
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
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.augurit.agmobile.gzps.R.id.subtype;
import static com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior.STATE_ANCHOR;
import static com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior.STATE_COLLAPSED;
import static com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior.STATE_EXPANDED;

/**
 * 工作台的数据上报和主界面的数据上报功能
 *
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.map
 * @createTime 创建时间 ：2017-10-14
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-10-14
 * @modifyMemo 修改备注：
 */
public class JbjPsdyMapFragment extends Fragment implements View.OnClickListener, PSHAffairDetailContract.View {

    private static final String KEY_MAP_STATE = "com.esri.MapState";

    private LocationMarker locationMarker;
    private final String TAG = "UploadSearchFragment";
    /**
     * 是否处于编辑模式
     */
    private boolean ifInEditMode = false;

    /**
     * 是否处于井连线模式
     */
    private boolean ifInCheckMode = false;

    /**
     * 是否处于修改管模式
     */
    private boolean ifInPipeMode = false;

    /**
     * 是否处于修改井模式
     */
    private boolean ifInWellMode = false;

    /**
     * 是否处于排水户模式
     */
    private boolean ifInSwerageUserMode = false;
//    /**
//     * 是否执行了放大缩小操作
//     */
//    private boolean hasZoomBefore = false;
//
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
    private ViewGroup ll_topbar_container; //顶部工具容器
    private ViewGroup ll_topbar_container2; //顶部工具容器
    private ViewGroup ll_question_topbar_container; //顶部工具容器
    private ViewGroup ll_question_tool_container; //顶部工具容器
    private ViewGroup ll_tool_container;   //左边工具容器
    View mView;

    MapView mMapView;

    private MapMeasureView mMapMeasureView;
    private MapDrawQuestionView mMapDrawQuestionView;

    private ILayerView layerView;

    private PatrolLayerPresenter4JbjPsdy layerPresenter;
    private boolean loadLayersSuccess = true;

    private TextView show_all_layer;
    private GridView gridView;
    private LimitedLayerAdapter layerAdapter;
    //顶部图层列表中当前选中的设施类型对应的务URL
    private String currComponentUrl = LayerUrlConstant.newComponentUrls[0];

    ProgressDialog pd;
    ViewGroup map_bottom_sheet;
    ViewGroup pipe_view;
    ViewGroup dis_map_bottom_sheet;
    AnchorSheetBehavior mBehavior;
    AnchorSheetBehavior mDisBehavior;
    private ComponetListAdapter componetListAdapter;
    //点查后设施的简要信息布局
    private View component_intro;
    private View component_detail_ll;
    //点查后设施的详细信息布局，用了TableViewManager
    private ViewGroup component_detail_container;
    private View btn_upload;
    private ArrayList<TableItem> tableItems = null;
    private ArrayList<Photo> photoList = new ArrayList<>();
    private String projectId;
    private TableViewManager tableViewManager;
    //点查后的设施结果
    private List<Component> mComponentQueryResult = new ArrayList<>();
    private int currIndex = 0;
    private View btn_prev;
    private View btn_next;
    private boolean ifFirstAdd = true;
    private boolean ifFirstEdit = true;
    private View layoutBottom;
    private Context mContext;

    /**
     * 地图默认的事件处理
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

    /**
     * 数据上报图层是否显示
     */
    private boolean ifMyUploadLayerVisible = false;
    private boolean ifUploadLayerVisible = false;
    private ComponentService componentService;

    private DetailAddress componentDetailAddress = null;
    ReentrantLock reentrantLock = new ReentrantLock();

    /**
     * 上报地图分布图层
     */
//    private TextView tv_distribute_error_correct;
//    private TextView tv_distribute_sure;
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
    private View btn_check;
    private View btn_check_cancel;
    private int mStatus = 0;//0正常模式，1上下游，2修改排水管道，3多个上下游
    private boolean UpOrDownStream = false;
    private View mReView;
    private SpinnerTableItem sp_ywlx;
    private SpinnerTableItem sp_lx;
    private List<PipeBean> mAllPipeBeans = new ArrayList<>();
    private List<PipeBean> mTempPipeBeans = new ArrayList<>();
    private Component componentCenter;
    private Component currentComponent;
    private Component currentDYComponent;
    private PipeBean originPipe;
    private boolean isShow = false;
    private GraphicsLayer mStreamGLayer;
    private ViewGroup mReviewContainter;
    private PipeStreamView mPipeStreamView;
    private GraphicsLayer mArrowGLayer;
    private PipeBean currentStream;
    private boolean isDrawStream = false;
    private UploadFacilityService mUploadFacilityService;
    private Button btn_reedit;
    private Button btn_cancel2;
    private TextView tv_query_tip;
    private View btn_edit_pipe;
    private View btn_pipe_cancel;
    private View btn_edit_yj;
    private View btn_yj_cancel;
    private String mTitle = "选择关联窨井";
    private List<String> mAttribute = Arrays.asList("污水", "雨水", "雨污合流");
    private String mFirstAttribute = "";
    private String mSecondAttribute = "";
    private GeometryDragOnTouchListener mGeometryMapOnTouchListener;//拖动窨井的位置
    private GraphicsLayer mGeometryMapGLayer;
    private CorrectFacilityService mIdentificationService;
    private DeleteFacilityService deleteFacilityService;
    private String status = "2";
    private String person = "";
    private boolean isClick = false;
    private List<String> attribute = Arrays.asList("窨井(中心城区)", "窨井", "雨水口", "雨水口(中心城区)", "排水口", "排水口(中心城区)", "排水管道", "排水管道(中心城区)", "排水沟渠", "排水沟渠(中心城区)", "存疑区域");
    private SpinnerTableItem sp_gj;
    private View ll_gj;
    private String mGjCode;
    private NumberItemTableItem sp_gxcd;
    private CheckBoxItem cb_isFacilityOfRedLine;
    private double mLineLength = -1;
    private boolean mIsAdd;
    private boolean whileTake = true;
    private boolean isLoading = false;
    private boolean isFirst = true;
    private boolean isNextOrLast = false;
    private View btn_edit_zhiyi;
    private View btn_zhiyi_cancel;
    private UploadFacilityService mFacilityService;
    private DoubtBean mDoubtBean;
    private boolean ifInDoubtMode = false;
    private boolean ifInGuaJie = false;
    private boolean ifInLine = false;
    private boolean ifInDY = false;
    private ProgressDialog mDialog;
    private List<LayerInfo> mLayerInfosFromLocal;
    private User mUser;
    private boolean userCanEdit = false;
    private PatrolLayerView3 patrolLayerView3;
    private UploadLayerService layersService;
    private List<PsdyJbj> mPsdyJbjs;
    private View btn_edit_dy;
    private View btn_dy_cancel;
    private ImageView btnSwerageUser;
    /**
     * 管井信息
     */
    private LinearLayout llResponsible;
    private FrameLayout llInfo;
    private TextView tvResponsible;
    private ImageView ivBack, ivResponsible;
    private View btn_check_situation;
    private View btn_situation_cancel;
    private ViewGroup check_legend_view;
    private int mIndex = -1;

    /**
     * 排水户列表
     */
    private JbjPsdySewerageUserListView mSewerageUserListView;
    private BottomSheetBehavior<JbjPsdySewerageUserListView> mSwerageUserListBehavoir;
    private float mMapViewMinTranslationY = 0f;
    private float mMapViewMaxTranslationY = 0f;
    private PsdyLayerFactory mPsdyLayerFactory;
    private PSHAffairDetailPresenter pshAffairDetailPresenter;

    public static JbjPsdyMapFragment getInstance(Bundle data) {
        JbjPsdyMapFragment addComponentFragment2 = new JbjPsdyMapFragment();
        addComponentFragment2.setArguments(data);
        return addComponentFragment2;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jbjpsdy_map, null);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        mView.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
//                exit();
            }
        });
        pshAffairDetailPresenter = new PSHAffairDetailPresenter(this, mContext);
        mUser = new LoginRouter(mContext, AMDatabase.getInstance()).getUser();
        if (mUser.getRoleCode().contains("ps_xc")
                || mUser.getRoleCode().contains("ps_yh")
                || mUser.getRoleCode().contains("ps_yz_chief")) {
            userCanEdit = true;
        }
        mDoubtBean = new DoubtBean();
        ((TextView) mView.findViewById(R.id.tv_title)).setText("单元挂接");
        mPsdyJbjs = new ArrayList<>();
        mView.findViewById(R.id.ll_search).setVisibility(View.VISIBLE);
        mView.findViewById(R.id.ll_search_obj).setVisibility(View.VISIBLE);
        ImageView iv_open_search = (ImageView) mView.findViewById(R.id.iv_open_search);
        iv_open_search.setImageResource(R.mipmap.ic_list);
        ((TextView) mView.findViewById(R.id.tv_search)).setText("上报列表");
        mView.findViewById(R.id.tv_search).setVisibility(View.VISIBLE);
        if (!userCanEdit) {
            mView.findViewById(R.id.ll_search).setVisibility(View.INVISIBLE);
        }
        mView.findViewById(R.id.ll_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MyJbjListActivity.class);
                startActivity(intent);
            }
        });

        mView.findViewById(R.id.ll_search_obj).setOnClickListener(new View.OnClickListener() {
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
        searchFragment.setHintText("搜索道路名查询设施");
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                //这里处理逻辑
                if (!StringUtil.isEmpty(keyword)) {
                    queryForObjctId(keyword);
                }
            }
        });
        ll_topbar_container = (ViewGroup) view.findViewById(R.id.ll_topbar_container);
        ll_topbar_container2 = (ViewGroup) view.findViewById(R.id.ll_topbar_container2);
        ll_question_topbar_container = (ViewGroup) view.findViewById(R.id.ll_question_topbar_container);
        ll_question_tool_container = (ViewGroup) view.findViewById(R.id.ll_question_tool_container);
        ll_tool_container = (ViewGroup) view.findViewById(R.id.ll_tool_container);
        mReviewContainter = (ViewGroup) view.findViewById(R.id.ll_toview_containter);

        tv_query_tip = (TextView) view.findViewById(R.id.tv_query_tip);
        // Find MapView and add feature layers
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.setMapBackground(Color.WHITE, Color.TRANSPARENT, 0f, 0f);//设置地图背景色、去掉网格线
        /**
         * 比例尺
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
                        //todo 暂时写死
                        if (PatrolLayerPresenter4JbjPsdy.initScale != -1 && PatrolLayerPresenter4JbjPsdy.initScale != 0) {
                            mMapView.setScale(PatrolLayerPresenter4JbjPsdy.initScale);
                            scaleView.setScale(PatrolLayerPresenter4JbjPsdy.initScale);
                        }

                        if (PatrolLayerPresenter4JbjPsdy.longitude != 0 && PatrolLayerPresenter4JbjPsdy.latitude != 0) {
                            Point point = new Point(PatrolLayerPresenter4JbjPsdy.longitude, PatrolLayerPresenter4JbjPsdy.latitude);
                            mMapView.centerAt(point, true);
                        }
                        mMapView.setMaxScale(30);

                        if (locationButton != null) {
                            locationButton.followLocation();
                        }

//                        if (myUploadLayerBtn != null) {
//                            myUploadLayerBtn.performClick();
//                        }

                        if (layerPresenter != null) {
                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter4JbjPsdy.RIVER_FLOW_LAYER_NAME, true);
                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter4JbjPsdy.DRAINAGE_PIPELINE, false);
                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter.DRAINAGE_UNIT_LAYER2, true);
                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter.DRAINAGE_UNIT_LAYER_BZ, true);
                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter.DRAINAGE_JIEBOJING, true);

                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter.DRAINAGE_WELL, false);
                            layerPresenter.changeLayerVisibility(PatrolLayerPresenter.DOOR_NO_LAYER, false);
                            layerPresenter.changeLayerVisibility("排水管井(中心城区)", false);
                            layerPresenter.changeLayerVisibility("排水管道(中心城区)", false);
                            LayerInfo planLayerInfo = getPlanLayerInfo();
                            LayerInfo doubtLayerInfo = getDoubtLayerInfo();
                            LayerInfo dyLayerInfo = getDYLayerInfo();
                            if (planLayerInfo != null) {
                                layerPresenter.onClickOpacityButton(planLayerInfo.getLayerId(), planLayerInfo, 0.7f);
                            }
                            if (doubtLayerInfo != null) {
                                layerPresenter.onClickOpacityButton(doubtLayerInfo.getLayerId(), doubtLayerInfo, 0.4f);
                            }
                            if (dyLayerInfo != null) {
                                layerPresenter.onClickOpacityButton(dyLayerInfo.getLayerId(), dyLayerInfo, 0.5f);
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
                if (ifInDY || ifInLine) {
                    double scale = mMapView.getScale() / 100d;
                    if (scale >= 50) {
                        initGraphicSelectGLayer();
                    } else {
                        initGraphicSelectGLayer();
                        if (!ListUtil.isEmpty(mPsdyJbjs)) {
                            drawPsdyLines(mPsdyJbjs);
                        }
                    }
                }
            }
        });

        ll_layer_url_init_fail = view.findViewById(R.id.ll_layer_url_init_fail);
        onInitLayerUrlFinished(null);

        //上报点查界面和分布图层分布点查界面
        mUploadLayerService = new UploadLayerService(getContext());

        mUploadFacilityService = new UploadFacilityService(mContext);
        //定位图标
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
         * 候选列表容器
         */
        ll_component_list = (ViewGroup) view.findViewById(R.id.ll_component_list);

        /**
         * 默认使用窨井
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
        pipe_view = (ViewGroup) view.findViewById(R.id.pipe_view);
        check_legend_view = (ViewGroup) view.findViewById(R.id.check_legend_view);
        dis_map_bottom_sheet = (ViewGroup) view.findViewById(R.id.dis_map_bottom_sheet);
        layoutBottom.post(new Runnable() {
            @Override
            public void run() {
                bottomHeight = layoutBottom.getHeight();
            }
        });
        mDisBehavior = AnchorSheetBehavior.from(dis_map_bottom_sheet);
        mBehavior = AnchorSheetBehavior.from(map_bottom_sheet);
        mBehavior.setState(AnchorSheetBehavior.STATE_EXPANDED);

        btn_prev = view.findViewById(R.id.prev);
        btn_next = view.findViewById(R.id.next);
        component_intro = view.findViewById(R.id.intro);
        component_detail_ll = view.findViewById(R.id.detail_ll);
        component_detail_container = (ViewGroup) view.findViewById(R.id.detail_container);
        dis_detail_ll = (ViewGroup) view.findViewById(R.id.dis_detail_ll);
        dis_detail_container = (ViewGroup) view.findViewById(R.id.dis_detail_container);
        btn_upload = view.findViewById(R.id.btn_upload);
        mDisBehavior.setAnchorHeight(DensityUtil.dp2px(getContext(), 230));


        RxView.clicks(btn_prev)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (whileTake && !isLoading) {
                            currIndex--;
                            if (currIndex < 0) {
                                btn_prev.setVisibility(View.GONE);
                                return;
                            }
                            String layerName = mComponentQueryResult.get(currIndex).getLayerName();
                            if (ifInCheckMode || ifInPipeMode || mStatus == 1 || mStatus == 2 || (isClick && "排水管道".equals(layerName))) {
                                showBottomSheetForCheck(mComponentQueryResult.get(currIndex));
                            } else {
                                showBottomSheet(mComponentQueryResult.get(currIndex));
                            }
                            if (currIndex == 0) {
                                btn_prev.setVisibility(View.GONE);
                            }
                            if (mComponentQueryResult.size() > 1) {
                                btn_next.setVisibility(View.VISIBLE);
                            }
                        } else if (isLoading && whileTake) {
                            whileTake = false;
                        }
                    }
                });
        RxView.clicks(btn_next)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (whileTake && !isLoading) {
                            currIndex++;
                            if (currIndex > mComponentQueryResult.size()) {
                                btn_next.setVisibility(View.GONE);
                                return;
                            }
                            String layerName = mComponentQueryResult.get(currIndex).getLayerName();
                            if (ifInCheckMode || ifInPipeMode || mStatus == 1 || mStatus == 2 || (isClick && "排水管道".equals(layerName))) {
                                showBottomSheetForCheck(mComponentQueryResult.get(currIndex));
                            } else {
                                showBottomSheet(mComponentQueryResult.get(currIndex));
                            }
                            if (currIndex == (mComponentQueryResult.size() - 1)) {
                                btn_next.setVisibility(View.GONE);
                            }
                            if (currIndex > 0) {
                                btn_prev.setVisibility(View.VISIBLE);
                            }
                        } else if (isLoading && whileTake) {
                            whileTake = false;
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

        //我的上报图层按钮
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
                    layerPresenter.changeLayerVisibility(PatrolLayerPresenter4JbjPsdy.MY_UPLOAD_LAYER_NAME, ifMyUploadLayerVisible);
                }
            }
        });

        //数据上报图层按钮
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
                    layerPresenter.changeLayerVisibility(PatrolLayerPresenter4JbjPsdy.LOCAL_UPLOAD_LAYER_NAME, ifUploadLayerVisible);
                }
            }
        });
        //注册当图层可见度发生改变时的回调
        if (layerPresenter != null) {
            layerPresenter.registerLayerVisibilityChangedListener(new PatrolLayerPresenter4JbjPsdy.OnLayerVisibilityChangedListener() {
                @Override
                public void changed(boolean visible, LayerInfo layerInfo) {
                    if (!visible && layerInfo.getLayerName().contains(PatrolLayerPresenter4JbjPsdy.LOCAL_UPLOAD_LAYER_NAME)) {
                        //不可见
                        //  myUploadIv.setImageResource(R.drawable.ic_upload_data_normal2);
                        uploadIv.setChecked(false);
                        tv_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.invisible_state_text_color, null));
                        ifUploadLayerVisible = false;
                    } else if (visible && layerInfo.getLayerName().contains(PatrolLayerPresenter4JbjPsdy.LOCAL_UPLOAD_LAYER_NAME)) {
                        //可见
                        // myUploadIv.setImageResource(R.drawable.ic_upload_data_pressed);
                        uploadIv.setChecked(true);
                        tv_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                        ifUploadLayerVisible = true;
                    } else if (!visible && layerInfo.getLayerName().contains(PatrolLayerPresenter4JbjPsdy.MY_UPLOAD_LAYER_NAME)) {
                        //不可见
                        //  myUploadIv.setImageResource(R.drawable.ic_upload_data_normal2);
                        myUploadIv.setChecked(false);
                        tv_my_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.invisible_state_text_color, null));
                        ifMyUploadLayerVisible = false;
                    } else if (visible && layerInfo.getLayerName().contains(PatrolLayerPresenter4JbjPsdy.MY_UPLOAD_LAYER_NAME)) {
                        //可见
                        // myUploadIv.setImageResource(R.drawable.ic_upload_data_pressed);
                        myUploadIv.setChecked(true);
                        tv_my_upload_layer.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                        ifMyUploadLayerVisible = true;
                    }
                }
            });
        }
        initBottomSheetView();

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
                if (ifInGuaJie) {
                    exit();
                }
                ifInCheckMode = false;
                ifInPipeMode = false;
                ifInEditMode = false;
                ifInDoubtMode = false;
                ifInGuaJie = false;
                ifInDY = false;
                if (ifInSwerageUserMode) {
                    btnSwerageUser.performClick();
                }
                mMapMeasureView.stopMeasure();
                boolean locate = false;
                if (btn_zhiyi_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_zhiyi_cancel.performClick();
                }
                if (btn_edit_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_edit_cancel.performClick();
                }
                if (btn_check_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_check_cancel.performClick();
                }
                if (btn_pipe_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_pipe_cancel.performClick();
                }
                if (btn_dy_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_dy_cancel.performClick();
                }
                if (btn_situation_cancel.getVisibility() == View.VISIBLE) {
                    btn_situation_cancel.performClick();
                }
                if (ifFirstAdd) {
                    ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "移动地图选择新增设施的位置");
                    ifFirstAdd = false;
                }
                if (ll_topbar_container2 != null) {
                    ll_topbar_container2.setVisibility(View.GONE);
                }

                if (locationMarker != null) {
                    locationMarker.changeIcon(R.mipmap.ic_add_facility_2);
                    locationMarker.setVisibility(View.VISIBLE);
                }
                btn_add.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.VISIBLE);
                showAttributeForStream(false);
                hideBottomSheet();
                initGLayer();
                initArrowGLayer();
                initStreamGLayer();
                initGraphicSelectGLayer();
                setAddNewFacilityListener();
                //下面的代码不能在调用 setAddNewFacilityListener 方法前调用，
                // 因为 addModeSelectLocationTouchListener 和 addModeCalloutSureButtonClickListener 未初始化
                if (locate) {
                    addModeSelectLocationTouchListener.locate();
                }
            }
        });

        btn_edit_zhiyi = view.findViewById(R.id.btn_edit_zhiyi);
        btn_zhiyi_cancel = view.findViewById(R.id.btn_zhiyi_cancel);
        btn_zhiyi_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifInDoubtMode = false;
                ifInGuaJie = false;
                if (locationMarker != null) {
                    locationMarker.setVisibility(View.GONE);
                }
                mMapDrawQuestionView.stopDraw();
                mMapView.getCallout().hide();
                btn_edit_zhiyi.setVisibility(View.VISIBLE);
                btn_zhiyi_cancel.setVisibility(View.GONE);
                mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
            }
        });

        btn_edit_zhiyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ifInGuaJie) {
                    exit();
                }
                ifInCheckMode = false;
                ifInPipeMode = false;
                ifInEditMode = false;
                ifInDoubtMode = true;
                ifInGuaJie = false;
                ifInDY = false;
                mMapMeasureView.stopMeasure();
                if (ll_topbar_container2 != null) {
                    ll_topbar_container2.setVisibility(View.GONE);
                }
                boolean locate = false;
                if (btn_edit_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_edit_cancel.performClick();
                }
                if (btn_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_cancel.performClick();
                }
                if (btn_check_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_check_cancel.performClick();
                }
                if (btn_pipe_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_pipe_cancel.performClick();
                }
                if (btn_dy_cancel.getVisibility() == View.VISIBLE) {
                    btn_dy_cancel.performClick();
                }
                if (btn_situation_cancel.getVisibility() == View.VISIBLE) {
                    btn_situation_cancel.performClick();
                }
                ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "请点击地图设置存疑范围");

                if (locationButton != null && locationButton.ifLocating()) {
                    locationButton.setStateNormal();
                }
                btn_edit_zhiyi.setVisibility(View.GONE);
                btn_zhiyi_cancel.setVisibility(View.VISIBLE);
                showAttributeForStream(false);
                hideBottomSheet();
                initGLayer();
                initArrowGLayer();
                initStreamGLayer();
                initGraphicSelectGLayer();
                mMapDrawQuestionView.startDraw();
            }
        });

        btn_edit_dy = view.findViewById(R.id.btn_edit_dy);
        btn_dy_cancel = view.findViewById(R.id.btn_dy_cancel);
        btn_dy_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifInDY = false;
                btn_edit_dy.setVisibility(View.VISIBLE);
                btn_dy_cancel.setVisibility(View.GONE);
                if (locationMarker != null) {
                    locationMarker.setVisibility(View.GONE);
                }
                mMapView.getCallout().hide();
                initGraphicSelectGLayer();
                hideBottomSheet();
                mStatus = 0;
                setMode(0);
                if (mStatus == 0) {
                    initGLayer();
                    initArrowGLayer();
                    mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                }
            }
        });

        btn_edit_dy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ifInGuaJie) {
                    exit();
                }
                ifInCheckMode = false;
                ifInPipeMode = false;
                ifInEditMode = false;
                ifInDoubtMode = false;
                ifInGuaJie = false;
                if (ifInSwerageUserMode) {
                    btnSwerageUser.performClick();
                }
                ifInDY = true;
                mMapMeasureView.stopMeasure();
                boolean locate = false;
                if (ll_topbar_container2 != null) {
                    ll_topbar_container2.setVisibility(View.GONE);
                }
                if (btn_dy_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_dy_cancel.performClick();
                }
                if (btn_cancel.getVisibility() == View.VISIBLE) {
                    btn_cancel.performClick();
                }
                if (btn_edit_cancel.getVisibility() == View.VISIBLE) {
                    btn_edit_cancel.performClick();
                }
                if (btn_pipe_cancel.getVisibility() == View.VISIBLE) {
                    btn_pipe_cancel.performClick();
                }
                if (btn_zhiyi_cancel.getVisibility() == View.VISIBLE) {
                    btn_zhiyi_cancel.performClick();
                }
                if (btn_check_cancel.getVisibility() == View.VISIBLE) {
                    btn_check_cancel.performClick();
                }
                if (btn_situation_cancel.getVisibility() == View.VISIBLE) {
                    btn_situation_cancel.performClick();
                }
                btn_edit_dy.setVisibility(View.GONE);
                btn_dy_cancel.setVisibility(View.VISIBLE);
                ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "在地图上点击排水单元");
                addTopBarView(mTitle);
                showAttributeForStream(false);
                hideBottomSheet();
                initGLayer();
                initArrowGLayer();
                initStreamGLayer();
                initGraphicSelectGLayer();
                mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                //下面的代码不能在调用 setSearchFacilityListener 方法前调用，
                // 因为editModeSelectLocationTouchListener 和 editModeCalloutSureButtonClickListener 未初始化
                if (locate && editModeSelectLocationTouchListener != null) {
                    editModeSelectLocationTouchListener.locate();
                    if (mMapView.getCallout().isShowing()) {
                        mMapView.getCallout().animatedHide();
                    }
                    editModeCalloutSureButtonClickListener.onClick(null);
                }
            }
        });

        btnSwerageUser = (ImageView) view.findViewById(R.id.btn_swerage_user);
        btnSwerageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 排水户按钮点击
                if (v.isSelected()) {
                    ifInSwerageUserMode = false;
                    dismissSwerageUserBottomSheet();
                    removePsdyMph();
                    mMapView.getCallout().hide();
                    initGraphicSelectGLayer();
                    hideBottomSheet();
                    mStatus = 0;
                    setMode(0);
                    if (mStatus == 0) {
                        initGLayer();
                        initArrowGLayer();
                        mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                    }
                } else {
                    if (ifInGuaJie) {
                        exit();
                    }
                    ifInCheckMode = false;
                    ifInPipeMode = false;
                    ifInEditMode = false;
                    ifInDoubtMode = false;
                    ifInGuaJie = false;
                    ifInDY = false;
                    ifInSwerageUserMode = true;
                    mMapMeasureView.stopMeasure();
                    boolean locate = false;
                    if (ll_topbar_container2 != null) {
                        ll_topbar_container2.setVisibility(View.GONE);
                    }
                    if (btn_dy_cancel.getVisibility() == View.VISIBLE) {
                        locate = true;
                        btn_dy_cancel.performClick();
                    }
                    if (btn_cancel.getVisibility() == View.VISIBLE) {
                        btn_cancel.performClick();
                    }
                    if (btn_edit_cancel.getVisibility() == View.VISIBLE) {
                        btn_edit_cancel.performClick();
                    }
                    if (btn_pipe_cancel.getVisibility() == View.VISIBLE) {
                        btn_pipe_cancel.performClick();
                    }
                    if (btn_zhiyi_cancel.getVisibility() == View.VISIBLE) {
                        btn_zhiyi_cancel.performClick();
                    }
                    if (btn_check_cancel.getVisibility() == View.VISIBLE) {
                        btn_check_cancel.performClick();
                    }
                    if (btn_situation_cancel.getVisibility() == View.VISIBLE) {
                        btn_situation_cancel.performClick();
                    }
                    ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "在地图上点击排水单元");
                    showAttributeForStream(false);
                    hideBottomSheet();
                    initGLayer();
                    initArrowGLayer();
                    initStreamGLayer();
                    initGraphicSelectGLayer();
                    mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                    //下面的代码不能在调用 setSearchFacilityListener 方法前调用，
                    // 因为editModeSelectLocationTouchListener 和 editModeCalloutSureButtonClickListener 未初始化
                    if (locate && editModeSelectLocationTouchListener != null) {
                        editModeSelectLocationTouchListener.locate();
                        if (mMapView.getCallout().isShowing()) {
                            mMapView.getCallout().animatedHide();
                        }
                        editModeCalloutSureButtonClickListener.onClick(null);
                    }
                }
                v.setSelected(!v.isSelected());
            }
        });

        btn_edit = view.findViewById(R.id.btn_edit);
        btn_edit_cancel = view.findViewById(R.id.btn_edit_cancel);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifInGuaJie) {
                    exit();
                }
                mMapMeasureView.stopMeasure();
                boolean locate = false;
                if (ll_topbar_container2 != null) {
                    ll_topbar_container2.setVisibility(View.GONE);
                }
                if (btn_cancel.getVisibility() == View.VISIBLE) {
                    btn_cancel.performClick();
                }
                if (btn_zhiyi_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_zhiyi_cancel.performClick();
                }
                if (btn_check_cancel.getVisibility() == View.VISIBLE) {
                    btn_check_cancel.performClick();
                }
                if (btn_pipe_cancel.getVisibility() == View.VISIBLE) {
                    btn_pipe_cancel.performClick();
                }
                if (btn_dy_cancel.getVisibility() == View.VISIBLE) {
                    btn_dy_cancel.performClick();
                }
                if (btn_situation_cancel.getVisibility() == View.VISIBLE) {
                    btn_situation_cancel.performClick();
                }
                ifInEditMode = true;
                ifInCheckMode = false;
                ifInPipeMode = false;
                ifInDoubtMode = false;
                ifInGuaJie = false;
                btn_edit.setVisibility(View.GONE);
                btn_edit_cancel.setVisibility(View.VISIBLE);

                ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "在地图上点击管井的位置");
                ifFirstEdit = false;

//                if (locationMarker != null) {
//                    locationMarker.changeIcon(R.mipmap.ic_check_data_2);
//                    locationMarker.setVisibility(View.VISIBLE);
//                }
                showAttributeForStream(false);
                hideBottomSheet();
                initGLayer();
                initArrowGLayer();
                initStreamGLayer();
                initGraphicSelectGLayer();
                mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                //下面的代码不能在调用 setSearchFacilityListener 方法前调用，
                // 因为editModeSelectLocationTouchListener 和 editModeCalloutSureButtonClickListener 未初始化
                if (locate && editModeSelectLocationTouchListener != null) {
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

        btn_check = view.findViewById(R.id.btn_check);
        btn_check_cancel = view.findViewById(R.id.btn_check_cancel);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifInGuaJie) {
                    exit();
                }
                mMapMeasureView.stopMeasure();
                mStatus = 0;
                isClick = false;
                ifInEditMode = false;
                ifInCheckMode = true;
                ifInPipeMode = false;
                ifInDoubtMode = false;
                ifInGuaJie = false;
                ifInDY = false;
                boolean locate = false;
                if (ll_topbar_container2 != null) {
                    ll_topbar_container2.setVisibility(View.GONE);
                }
                if (btn_zhiyi_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_zhiyi_cancel.performClick();
                }
                if (btn_cancel.getVisibility() == View.VISIBLE) {
                    btn_cancel.performClick();
                }
                if (btn_edit_cancel.getVisibility() == View.VISIBLE) {
                    btn_edit_cancel.performClick();
                }
                if (btn_pipe_cancel.getVisibility() == View.VISIBLE) {
                    btn_pipe_cancel.performClick();
                }
                if (btn_dy_cancel.getVisibility() == View.VISIBLE) {
                    btn_dy_cancel.performClick();
                }
                if (btn_situation_cancel.getVisibility() == View.VISIBLE) {
                    btn_situation_cancel.performClick();
                }
                btn_check.setVisibility(View.GONE);
                btn_check_cancel.setVisibility(View.VISIBLE);
                ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "移动地图选择窨井");
                addTopBarView(mTitle);
                if (locationMarker != null) {
                    locationMarker.changeIcon(R.mipmap.ic_check_related);
                    locationMarker.setVisibility(View.VISIBLE);
                }
                showAttributeForStream(false);
                hideBottomSheet();
                initGLayer();
                initArrowGLayer();
                initStreamGLayer();
                initGraphicSelectGLayer();
                setSearchFacilityListener();
                //下面的代码不能在调用 setSearchFacilityListener 方法前调用，
                // 因为editModeSelectLocationTouchListener 和 editModeCalloutSureButtonClickListener 未初始化
                if (locate && editModeSelectLocationTouchListener != null) {
                    editModeSelectLocationTouchListener.locate();
                    if (mMapView.getCallout().isShowing()) {
                        mMapView.getCallout().animatedHide();
                    }
                    editModeCalloutSureButtonClickListener.onClick(null);
                }

            }
        });
        btn_check_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifInCheckMode = false;
                btn_check.setVisibility(View.VISIBLE);
                btn_check_cancel.setVisibility(View.GONE);
                if (locationMarker != null) {
                    locationMarker.setVisibility(View.GONE);
                }
                mMapView.getCallout().hide();
                hideBottomSheet();
                mStatus = 0;
                setMode(0);
                if (mStatus == 0) {
                    initGLayer();
                    initArrowGLayer();
                    mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                }
            }
        });
        btn_edit_pipe = view.findViewById(R.id.btn_edit_pipe);
        btn_pipe_cancel = view.findViewById(R.id.btn_pipe_cancel);
        btn_edit_pipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifInGuaJie) {
                    exit();
                }
                mMapMeasureView.stopMeasure();
                mStatus = 0;
                isClick = false;
                ifInEditMode = false;
                ifInCheckMode = false;
                ifInPipeMode = true;
                ifInDoubtMode = false;
                ifInGuaJie = false;
                ifInDY = false;
                boolean locate = false;
                if (ll_topbar_container2 != null) {
                    ll_topbar_container2.setVisibility(View.GONE);
                }
                if (btn_zhiyi_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_zhiyi_cancel.performClick();
                }
                if (btn_cancel.getVisibility() == View.VISIBLE) {
                    btn_cancel.performClick();
                }
                if (btn_edit_cancel.getVisibility() == View.VISIBLE) {
                    btn_edit_cancel.performClick();
                }

                if (btn_check_cancel.getVisibility() == View.VISIBLE) {
                    btn_check_cancel.performClick();
                }
                if (btn_dy_cancel.getVisibility() == View.VISIBLE) {
                    btn_dy_cancel.performClick();
                }
                if (btn_situation_cancel.getVisibility() == View.VISIBLE) {
                    btn_situation_cancel.performClick();
                }
                btn_edit_pipe.setVisibility(View.GONE);
                btn_pipe_cancel.setVisibility(View.VISIBLE);
                ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "在地图上点击排水管线的位置");
                addTopBarView(mTitle);
//                if (locationMarker != null) {
//                    locationMarker.changeIcon(R.mipmap.ic_check_data_2);
//                    locationMarker.setVisibility(View.VISIBLE);
//                }
                showAttributeForStream(false);
                hideBottomSheet();
                initGLayer();
                initArrowGLayer();
                initStreamGLayer();
                initGraphicSelectGLayer();
                mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                //下面的代码不能在调用 setSearchFacilityListener 方法前调用，
                // 因为editModeSelectLocationTouchListener 和 editModeCalloutSureButtonClickListener 未初始化
                if (locate && editModeSelectLocationTouchListener != null) {
                    editModeSelectLocationTouchListener.locate();
                    if (mMapView.getCallout().isShowing()) {
                        mMapView.getCallout().animatedHide();
                    }
                    editModeCalloutSureButtonClickListener.onClick(null);
                }

            }
        });
        btn_pipe_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifInPipeMode = false;
                btn_edit_pipe.setVisibility(View.VISIBLE);
                btn_pipe_cancel.setVisibility(View.GONE);
                if (locationMarker != null) {
                    locationMarker.setVisibility(View.GONE);
                }
                mMapView.getCallout().hide();
                hideBottomSheet();
                mStatus = 0;
                setMode(0);
                if (mStatus == 0) {
                    initGLayer();
                    initArrowGLayer();
                    mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                }
            }
        });

        btn_edit_yj = view.findViewById(R.id.btn_edit_yj);
        btn_yj_cancel = view.findViewById(R.id.btn_yj_cancel);
        btn_edit_yj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifInGuaJie) {
                    exit();
                }
                mMapMeasureView.stopMeasure();
                mStatus = 0;
                isClick = false;
                ifInEditMode = false;
                ifInWellMode = true;
                ifInCheckMode = false;
                ifInPipeMode = false;
                ifInDoubtMode = false;
                ifInGuaJie = false;
                ifInDY = false;
                boolean locate = false;
                if (ll_topbar_container2 != null) {
                    ll_topbar_container2.setVisibility(View.GONE);
                }
                if (btn_zhiyi_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_zhiyi_cancel.performClick();
                }
                if (btn_cancel.getVisibility() == View.VISIBLE) {
                    btn_cancel.performClick();
                }
                if (btn_check_cancel.getVisibility() == View.VISIBLE) {
                    btn_check_cancel.performClick();
                }
                if (btn_edit_cancel.getVisibility() == View.VISIBLE) {
                    btn_edit_cancel.performClick();
                }
                if (btn_situation_cancel.getVisibility() == View.VISIBLE) {
                    btn_situation_cancel.performClick();
                }
                if (btn_pipe_cancel.getVisibility() == View.VISIBLE) {
                    btn_pipe_cancel.performClick();
                }
                if (btn_dy_cancel.getVisibility() == View.VISIBLE) {
                    btn_dy_cancel.performClick();
                }
                btn_edit_yj.setVisibility(View.GONE);
                btn_yj_cancel.setVisibility(View.VISIBLE);
                ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "移动地图选择窨井");
                addTopBarView(mTitle);
                if (locationMarker != null) {
                    locationMarker.changeIcon(R.mipmap.ic_check_data_2);
                    locationMarker.setVisibility(View.VISIBLE);
                }
                showAttributeForStream(false);
                hideBottomSheet();
                initGLayer();
                initArrowGLayer();
                initStreamGLayer();
                initGraphicSelectGLayer();
                setSearchFacilityListener();
                //下面的代码不能在调用 setSearchFacilityListener 方法前调用，
                // 因为editModeSelectLocationTouchListener 和 editModeCalloutSureButtonClickListener 未初始化
                if (locate && editModeSelectLocationTouchListener != null) {
                    editModeSelectLocationTouchListener.locate();
                    if (mMapView.getCallout().isShowing()) {
                        mMapView.getCallout().animatedHide();
                    }
                    editModeCalloutSureButtonClickListener.onClick(null);
                }

            }
        });
        btn_yj_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifInWellMode = false;
                btn_edit_yj.setVisibility(View.VISIBLE);
                btn_yj_cancel.setVisibility(View.GONE);
                if (locationMarker != null) {
                    locationMarker.setVisibility(View.GONE);
                }
                mMapView.getCallout().hide();
                hideBottomSheet();
                mStatus = 0;
                setMode(0);
                if (mStatus == 0) {
                    initGLayer();
                    initArrowGLayer();
                    mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                }
            }
        });

        btn_check_situation = view.findViewById(R.id.btn_check_situation);
        btn_situation_cancel = view.findViewById(R.id.btn_situation_cancel);
        btn_check_situation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_legend_view.setVisibility(View.VISIBLE);
                switchLayerVisibility(true);
                mMapMeasureView.stopMeasure();
                mStatus = 0;
                isClick = false;
                ifInEditMode = false;
                ifInCheckMode = false;
                ifInPipeMode = false;
                ifInDoubtMode = false;
                ifInGuaJie = false;
                ifInDY = false;
                boolean locate = false;
                if (ll_topbar_container2 != null) {
                    ll_topbar_container2.setVisibility(View.GONE);
                }
                if (btn_zhiyi_cancel.getVisibility() == View.VISIBLE) {
                    locate = true;
                    btn_zhiyi_cancel.performClick();
                }
                if (btn_check.getVisibility() == View.VISIBLE) {
                    btn_check_cancel.performClick();
                }
                if (btn_cancel.getVisibility() == View.VISIBLE) {
                    btn_cancel.performClick();
                }
                if (btn_edit_cancel.getVisibility() == View.VISIBLE) {
                    btn_edit_cancel.performClick();
                }
                if (btn_pipe_cancel.getVisibility() == View.VISIBLE) {
                    btn_pipe_cancel.performClick();
                }
                if (btn_dy_cancel.getVisibility() == View.VISIBLE) {
                    btn_dy_cancel.performClick();
                }
                if (ifInSwerageUserMode) {
                    btnSwerageUser.performClick();
                }
                btn_check_situation.setVisibility(View.GONE);
                btn_situation_cancel.setVisibility(View.VISIBLE);
                showAttributeForStream(false);
                hideBottomSheet();
                initGLayer();
                initArrowGLayer();
                initStreamGLayer();
                initGraphicSelectGLayer();
                //下面的代码不能在调用 setSearchFacilityListener 方法前调用，
                // 因为editModeSelectLocationTouchListener 和 editModeCalloutSureButtonClickListener 未初始化
                if (locate && editModeSelectLocationTouchListener != null) {
                    editModeSelectLocationTouchListener.locate();
                    if (mMapView.getCallout().isShowing()) {
                        mMapView.getCallout().animatedHide();
                    }
                    editModeCalloutSureButtonClickListener.onClick(null);
                }

            }
        });
        btn_situation_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifInCheckMode = false;
                switchLayerVisibility(false);
                layerPresenter.changeLayerVisibility(PatrolLayerPresenter.DRAINAGE_JCQK, false);
                check_legend_view.setVisibility(View.GONE);
                btn_check_situation.setVisibility(View.VISIBLE);
                btn_situation_cancel.setVisibility(View.GONE);
                if (locationMarker != null) {
                    locationMarker.setVisibility(View.GONE);
                }
                mMapView.getCallout().hide();
                hideBottomSheet();
            }
        });

        if (!userCanEdit) {
            view.findViewById(R.id.rl_btn_add).setVisibility(View.GONE);
            view.findViewById(R.id.ll_btn_edit).setVisibility(View.GONE);
            view.findViewById(R.id.ll_check).setVisibility(View.GONE);
            view.findViewById(R.id.ll_edit_pipe).setVisibility(View.GONE);
            view.findViewById(R.id.ll_zhiyi).setVisibility(View.GONE);
        }
        initPipeInfo();

        mSewerageUserListView = (JbjPsdySewerageUserListView) view.findViewById(R.id.swerageUserListView);
        mSewerageUserListView.setOnItemClickListener(new JbjPsdySewerageUserListAdapter.OnSewerageUserListItemClickListener() {
            @Override
            public void onItemClick(View v, RecyclerView.ViewHolder holder, SewerageUserEntity entity) {
                // 在这里处理Item点击事件
                pshAffairDetailPresenter.getPSHAffairDetail(Integer.parseInt(entity.getId()));
                // 在这里定位到地图上
                double latitude = entity.getLatitude();
                double longitude = entity.getLongitude();
                Point geometry = new Point(longitude, latitude);
                mMapView.centerAt(geometry, true);
                initGraphicSelectGLayer();
                drawGeometry(geometry, mGraphicSelectLayer, true, true);
            }

            @Override
            public void onLocation(View v, RecyclerView.ViewHolder holder, SewerageUserEntity entity) {
                // 在这里定位到地图上
                double latitude = entity.getLatitude();
                double longitude = entity.getLongitude();
                Point geometry = new Point(longitude, latitude);
                mMapView.centerAt(geometry, true);
                initGraphicSelectGLayer();
                drawGeometry(geometry, mGraphicSelectLayer, true, true);
            }
        });
        mSewerageUserListView.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = mSewerageUserListView.getLayoutParams();
                int parentHeight = ((ViewGroup) mSewerageUserListView.getParent()).getMeasuredHeight();
                layoutParams.height = parentHeight * 3 / 5;
                mSewerageUserListView.setLayoutParams(layoutParams);
                float titleBarHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
//                mMapViewMinTranslationY = parentHeight * (1f / 5 - 1f / 2);
                mMapViewMinTranslationY = parentHeight * (1f / 5 - 1f / 2) + titleBarHeight / 2;
                mMapViewMaxTranslationY = mMapView.getTranslationY();
                mSwerageUserListBehavoir = BottomSheetBehavior.from(mSewerageUserListView);
                mSwerageUserListBehavoir.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        if (slideOffset <= 0) {
                            mMapView.setTranslationY(mMapViewMaxTranslationY);
                        } else if (slideOffset >= 1) {
                            mMapView.setTranslationY(mMapViewMinTranslationY);
                        } else {
                            float targetY = mMapViewMaxTranslationY - (mMapViewMaxTranslationY - mMapViewMinTranslationY) * slideOffset;
                            mMapView.setTranslationY(targetY);
                        }
                    }
                });
            }
        });
    }

    /**
     * 改变检测情况图层的显示状态
     *
     * @param b
     */
    private void switchLayerVisibility(boolean b) {
        if (layerPresenter != null) {
            layerPresenter.changeLayerVisibility(PatrolLayerPresenter.DRAINAGE_JCQK, b);
        }
    }


    /**
     * 初始化管井信息
     */
    private void initPipeInfo() {
        llInfo = (FrameLayout) map_bottom_sheet.findViewById(R.id.fl_info);
        llResponsible = (LinearLayout) map_bottom_sheet.findViewById(R.id.ll_responsible);
        tvResponsible = (TextView) map_bottom_sheet.findViewById(R.id.tv_responsible);
        ivBack = (ImageView) map_bottom_sheet.findViewById(R.id.iv_back);
        ivResponsible = (ImageView) map_bottom_sheet.findViewById(R.id.iv_responsible);
        LinearLayout.LayoutParams lpInfo = new LinearLayout.LayoutParams(DeviceUtil.getScreenWidth(mContext),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llInfo.setLayoutParams(lpInfo);
        LinearLayout.LayoutParams lpResponsible = new LinearLayout.LayoutParams(DeviceUtil.getScreenWidth(mContext),
                ViewGroup.LayoutParams.MATCH_PARENT);
        llResponsible.setLayoutParams(lpResponsible);
        tvResponsible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoAnimator(llInfo, false);
            }
        });
        ivResponsible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoAnimator(llInfo, false);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoAnimator(llInfo, true);
            }
        });
        resetInfoLayout();
    }

    private void resetInfoLayout() {
        ((LinearLayout.LayoutParams) llInfo.getLayoutParams()).leftMargin = 0;
        llInfo.requestLayout();
    }

    private void infoAnimator(final View view, boolean reverse) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(reverse ? view.getWidth() : 0, reverse ? 0 : view.getWidth());
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int magin = -(int) valueAnimator.getAnimatedValue();
                ((LinearLayout.LayoutParams) view.getLayoutParams()).leftMargin = magin;
                view.requestLayout();
            }
        });
        valueAnimator.start();
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
        //隐藏marker
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
     * 新增数据
     * 加载设施信息，显示中底部BottomSheet中
     */
    private void showBottomSheet(final UploadedFacility uploadedFacility) {
        mCurrentModifiedFacility = null;
        //initGLayer();
        if (uploadedFacility == null) {
            return;
        }
        mCurrentUploadedFacility = uploadedFacility;

        /**
         * 上报信息按钮
         */

        if (uploadedFacility.getIsBinding() == 1 && mCurrentCompleteTableInfo != null) {
//            tv_distribute_error_correct.setVisibility(View.VISIBLE);
        } else {
//            tv_distribute_error_correct.setVisibility(View.GONE);
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
            if (mDisBehavior.getState() == STATE_COLLAPSED || mDisBehavior.getState() == AnchorSheetBehavior.STATE_HIDDEN) {
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
     * 设施部件
     * 加载设施信息，显示中底部BottomSheet中
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

        if (sort.contains("雨污合流")) {
            color = ResourcesCompat.getColor(getResources(), R.color.mark_light_purple, null);
        } else if (sort.contains("雨水")) {
            color = ResourcesCompat.getColor(getResources(), R.color.progress_line_green, null);
        } else if (sort.contains("污水")) {
            color = ResourcesCompat.getColor(getResources(), R.color.agmobile_red, null);
        }
        TextItemTableItem sortTv = new TextItemTableItem(getContext());
        sortTv.setTextViewName("类别");
        sortTv.setText(StringUtil.getNotNullString(sort, ""));
        sortTv.setReadOnly();

        /**
         * 如果是雨水口，显示特性：方形
         */
        String layertype = "";
        if (mCurrentModifiedFacility != null) {
            layertype = mCurrentModifiedFacility.getLayerName();
        } else if (mCurrentUploadedFacility != null) {
            layertype = mCurrentUploadedFacility.getLayerName();
        }
        TextItemTableItem ssTv = new TextItemTableItem(getContext());
        ssTv.setTextViewName("设施");
//        ssTv.setText(StringUtil.getNotNullString(layertype, "") + "(" + usid + ")");
        ssTv.setText(StringUtil.getNotNullString(layertype, ""));
        ssTv.setReadOnly();
        ll_table_item_container.addView(ssTv);
        if (layertype.equals("雨水口")) {
            String feature = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.FEATURE));
            sortTv.setText(StringUtil.getNotNullString(feature, ""));
        }
//
        ll_table_item_container.addView(sortTv);
        /**
         * 修改属性三
         */
//        if ("雨水口".equals(type)) {
//            String style = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.STYLE));
//            subtypeTv.setText(StringUtil.getNotNullString(style, ""));
//        } else {
//            subtypeTv.setText(StringUtil.getNotNullString(subtype, ""));
//        }
//
//        if ("雨水口".equals(type)) {
//            field3Tv.setVisibility(View.GONE);
//        } else {
//            field3Tv.setVisibility(View.VISIBLE);
//        }
        String field3 = "";
        TextItemTableItem csortTv = new TextItemTableItem(getContext());
        if (layertype.equals("窨井")) {
//            field3 = "井盖材质: " + String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.MATERIAL));
            csortTv.setTextViewName("井盖材质");
            csortTv.setText(String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.MATERIAL)));
            csortTv.setReadOnly();
        } else if (layertype.equals("排水口")) {
            field3 = "排放去向: " + String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.RIVER));
            csortTv.setTextViewName("排放去向");
            csortTv.setText(String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.RIVER)));
            csortTv.setReadOnly();
        }
        ll_table_item_container.addView(csortTv);


        final String address = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.ADDR));
        TextItemTableItem addressTv = new TextItemTableItem(getContext());
        addressTv.setTextViewName("设施位置");
        addressTv.setText(StringUtil.getNotNullString(address, ""));
        addressTv.setReadOnly();
        ll_table_item_container.addView(addressTv);
//        addrTv.setText("设施位置" + "：" + StringUtil.getNotNullString(address, ""));

        //已挂牌编号
        TextItemTableItem bianhaoTv = new TextItemTableItem(getContext());
        bianhaoTv.setTextViewName("已挂牌编号");
        bianhaoTv.setReadOnly();
        String codeValue = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.CODE));
        codeValue = codeValue.trim();
        if (layertype.equals("窨井")) {
            if (!codeValue.isEmpty()) {
                bianhaoTv.setText(StringUtil.getNotNullString(codeValue, ""));
            } else {
//                bianhaoTv.setText("无");
                bianhaoTv.setText("");
            }
            ll_table_item_container.addView(bianhaoTv);
        }
        String parentOrg = String.valueOf(CompleteTableInfo.getAttrs().get(ComponentFieldKeyConstant.OWNERDEPT));
        TextItemTableItem quanshuTv = new TextItemTableItem(getContext());
        quanshuTv.setTextViewName("权属单位");
        quanshuTv.setText(StringUtil.getNotNullString(parentOrg, ""));
        quanshuTv.setReadOnly();
        ll_table_item_container.addView(quanshuTv);
    }


    /**
     * 纠错数据
     * 加载设施信息，显示中底部BottomSheet中
     */
    private void showBottomSheet(final ModifiedFacility modifiedFacility) {
        //initGLayer();
        if (modifiedFacility == null) {
            return;
        }

        dis_detail_container.setVisibility(View.VISIBLE);

        if (dis_detail_container.getChildCount() == 0) {
            if (mCurrentCompleteTableInfo != null) {
//                tv_distribute_error_correct.setVisibility(View.VISIBLE);
            } else {
//                tv_distribute_error_correct.setVisibility(View.GONE);
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
            if (mDisBehavior.getState() == STATE_COLLAPSED || mDisBehavior.getState() == AnchorSheetBehavior.STATE_HIDDEN) {
                dis_detail_container.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDisBehavior.setState(AnchorSheetBehavior.STATE_ANCHOR);
                    }
                }, 200);
            }

        }


        dis_detail_ll.setVisibility(View.VISIBLE);
    }

    private void initBottomSheetView() {
        ll_next_and_prev_container = (ViewGroup) dis_map_bottom_sheet.findViewById(R.id.ll_next_and_prev_container);
        ll_table_item_container = (ViewGroup) dis_map_bottom_sheet.findViewById(R.id.ll_table_item_container);
        /**
         * 是否有人上报过提醒文本
         */
        tv_check_hint = (TextView) map_bottom_sheet.findViewById(R.id.tv_check_hint);
        ll_check_hint = map_bottom_sheet.findViewById(R.id.ll_check_hint);
        tv_check_phone = (TextView) map_bottom_sheet.findViewById(R.id.tv_check_phone);
        tv_check_person = (TextView) map_bottom_sheet.findViewById(R.id.tv_check_person);
    }

    private void resetStatus(boolean reset) {
//        if (reset) {
//            tv_distribute_sure.setBackground(getResources().getDrawable(R.drawable.round_blue_rectangle));
//            tv_distribute_sure.setTextColor(getResources().getColor(R.color.agmobile_white));
//            tv_distribute_error_correct.setBackground(getResources().getDrawable(R.drawable.round_grey_rectangle));
//            tv_distribute_error_correct.setTextColor(getResources().getColor(R.color.agmobile_blue));
//        } else {
//            tv_distribute_sure.setBackground(getResources().getDrawable(R.drawable.round_grey_rectangle));
//            tv_distribute_sure.setTextColor(getResources().getColor(R.color.agmobile_blue));
//            tv_distribute_error_correct.setBackground(getResources().getDrawable(R.drawable.round_blue_rectangle));
//            tv_distribute_error_correct.setTextColor(getResources().getColor(R.color.agmobile_white));
//        }
    }

    private void initLegendPresenter() {
        if (legendPresenter == null) {
            ILegendView legendView = new ImageLegendView(mContext);
            legendPresenter = new LegendPresenter(legendView, new OnlineLegendService(mContext));
        }

    }


    public void loadMap() {
        final IDrawerController drawerController = (IDrawerController) mContext;
        patrolLayerView3 = new PatrolLayerView3(mContext, mMapView, drawerController.getDrawerContainer());
        layerView = patrolLayerView3;
        layersService = new UploadLayerService(mContext.getApplicationContext());
        layerPresenter = new PatrolLayerPresenter4JbjPsdy(layerView, layersService);
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


        ViewGroup ll_measure_container = patrolLayerView3.getBottomLayout();
        mMapMeasureView = new MapMeasureView(mContext, mMapView, ll_measure_container,
                ll_topbar_container, ll_tool_container);
        mMapMeasureView.setOnStartCallback(new Callback4() {
            @Override
            public void before() {
                if (btn_edit_cancel.getVisibility() == View.VISIBLE) {
                    btn_edit_cancel.performClick();
                }
                if (btn_cancel.getVisibility() == View.VISIBLE) {
                    btn_cancel.performClick();
                }
                if (btn_check_cancel.getVisibility() == View.VISIBLE) {
                    btn_check_cancel.performClick();
                }
                if (btn_pipe_cancel.getVisibility() == View.VISIBLE) {
                    btn_pipe_cancel.performClick();
                }
                if (btn_zhiyi_cancel.getVisibility() == View.VISIBLE) {
                    btn_zhiyi_cancel.performClick();
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
                if (!ifInPipeMode && !ifInCheckMode && ifInEditMode) {
                    mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                }
            }
        });

        mMapDrawQuestionView = new MapDrawQuestionView(mContext, mMapView, ll_measure_container,
                ll_topbar_container, ll_tool_container);
        mMapDrawQuestionView.setOnStartCallback(new Callback4() {
            @Override
            public void before() {
            }

            @Override
            public void after() {
            }
        });
        mMapDrawQuestionView.setOnStopCallback(new Callback4() {
            @Override
            public void before() {

            }

            @Override
            public void after() {
                ifInDoubtMode = false;
                ifInGuaJie = false;
                if (!ifInPipeMode && !ifInCheckMode && ifInEditMode) {
                    mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                }
                if (btn_zhiyi_cancel.getVisibility() == View.VISIBLE) {
                    btn_zhiyi_cancel.setVisibility(View.GONE);
                    btn_edit_zhiyi.setVisibility(View.VISIBLE);
                }
            }
        });
        mMapDrawQuestionView.setOnSubmitListener(new MapDrawQuestionView.OnSubmitListener() {
            @Override
            public void onClickQuestionSubmit(Geometry geometry) {
                //提交质疑范围
                showDoubtDialog(null, geometry, null);
            }

            @Override
            public void onStatusChange(int state) {
//                if(state == 0){
//                    ifInDoubtMode = true;
//                    mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
//                    ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "请在地图上点击查看存疑位置");
//                }else if(state == 1){
//                    ifInDoubtMode = true;
//                    ToastUtil.iconLongToast(mContext, R.mipmap.ic_alert_yellow, "请点击地图设置存疑范围");
//                }
            }
        });
    }


    private void showDYCallout(final String name, Point point, final Component component, final View.OnClickListener calloutSureButtonClickListener) {
        if (name != null) {
            final Point geometry = point;
            final Callout callout = mMapView.getCallout();
            View view = View.inflate(mContext, R.layout.view_psdy_callout, null);
            ((TextView) view.findViewById(com.augurit.agmobile.patrolcore.R.id.tv_listcallout_title)).setText(name);
            view.findViewById(com.augurit.agmobile.patrolcore.R.id.btn_select_device).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPsdyDialog(name, component);
                }
            });
            callout.setStyle(com.augurit.agmobile.patrolcore.R.xml.editmap_callout_style);
            callout.setContent(view);
//            if (point == null) {
//                point = mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2, locationMarker.getY() + locationMarker.getHeight() / 2 - 140);
//            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Point point1 = mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2, locationMarker.getY() + locationMarker.getHeight() / 2 - 140);
                    callout.show(point1);
                }
            }, 800);

        }

    }


    /**
     * 请求百度地址
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
     * 请求百度地址
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
        //获取当前的位置
        return mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2, locationMarker.getY() + locationMarker.getHeight() / 2);
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

    private boolean hasLoadLayerBefore = false;

    /**
     * 显示图层列表
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
        if (map_bottom_sheet.getVisibility() == View.GONE && mStatus == 0) {
            ((Activity) mContext).finish();
            return;
        } else if (mStatus == 1) {
            exit();
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
             * 注册位置发生改变时的回调
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

    @Override
    public void onGetPSHAffairDetail(PSHAffairDetail pshAffairDetail) {
        Intent intent = new Intent(getActivity(), SewerageTableActivity.class);
        intent.putExtra("pshAffair", pshAffairDetail);
        intent.putExtra("fromPSHAffair", true);
        startActivity(intent);
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
            if (mStatus == 6) {
                handleTapJbj(e);
            } else {
                handleTap(e);
            }
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

            ifInLine = false;
            if (pipe_view != null && pipe_view.getVisibility() == View.VISIBLE) {
                return;
            }
            if (locationMarker != null) {
                locationMarker.setVisibility(View.GONE);
            }
            if (mMapView.getCallout().isShowing()) {
                mMapView.getCallout().hide();
            }
            removePsdyMph();
            int visibility = map_bottom_sheet.getVisibility();
            int disvisibility = dis_map_bottom_sheet.getVisibility();
            initGLayer();
            initGraphicSelectGLayer();
            hideBottomSheet();
            if (visibility == View.VISIBLE || disvisibility == View.VISIBLE) {
                return;
            }

            if (mSewerageUserListView.getVisibility() == View.VISIBLE) {
                dismissSwerageUserBottomSheet();
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
                isClick = true;
                query(point.getX(), point.getY());
            } else {
                ToastUtil.shortToast(mContext, "请先放大到可以看到设施的级别");
            }


        }

    }

    /**
     * 点击地图后查询设施
     *
     * @param x
     * @param y
     */
    private void queryDistribute(final double x, final double y) {
        pd = new ProgressDialog(getContext());
        pd.setMessage("正在查询上报信息...");
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
//        tv_distribute_error_correct.setVisibility(View.VISIBLE);
        //tv_sure.setVisibility(View.GONE);
        resetStatus(true);
        mUploadLayerService.setQueryByWhere("MARK_PID='" + BaseInfoManager.getLoginName(getContext()) + "'");
        mUploadLayerService.queryUploadDataInfo(point, mMapView.getSpatialReference(), mMapView.getResolution(), new Callback2<List<UploadInfo>>() {
            @Override
            public void onSuccess(List<UploadInfo> uploadInfos) {
                pd.dismiss();
                if (ListUtil.isEmpty(uploadInfos) || (uploadInfos.size() == 1 && uploadInfos.get(0).getUploadedFacilities() == null && uploadInfos.get(0).getModifiedFacilities() == null)) {
//                    ToastUtil.iconLongToast(getContext(), R.mipmap.ic_alert_yellow, "查无数据！");
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

    private void handleTapJbj(MotionEvent e) {
        double scale = mMapView.getScale();
        List<Graphic> list = new ArrayList<>();
        if (scale < LayerUrlConstant.MIN_QUERY_SCALE) {
            Point point = mMapView.toMapPoint(e.getX(), e.getY());
            int[] graphicIDs1 = mGraphicSelectLayer.getGraphicIDs(e.getX(), e.getY(), 15);
            if (graphicIDs1.length > 0 && mGraphicSelectLayer.isVisible()) {
                for (int id : graphicIDs1) {
                    list.add(mGraphicSelectLayer.getGraphic(id));
                }
                final Graphic graphic = mGraphicSelectLayer.getGraphic(graphicIDs1[0]);
                initSelectGLayer();
                drawSelectGeometry(graphic.getGeometry(), mSelectGLayer, null, true, false);
                final String id = StringUtil.getNotNullString(graphic.getAttributeValue("Id"), "");
                DialogUtil.MessageBox(getContext(), "提示", "是否要删除该连线？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mIdentificationService.deletePsdyJbj(id)
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
                                        initSelectGLayer();
                                        CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
                                                BaseInfoManager.getUserName(mContext) + "原因是：" + e.getLocalizedMessage()));
                                        ToastUtil.shortToast(mContext, "删除失败");
                                    }

                                    @Override
                                    public void onNext(ResponseBody responseBody) {
                                        if (progressDialog != null && progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        initSelectGLayer();
                                        if (responseBody.getCode() == 200) {
                                            mGraphicSelectLayer.removeGraphic(graphic.getUid());
                                            for (PsdyJbj psdyJbj : mPsdyJbjs) {
                                                if (id.equals(psdyJbj.getId())) {
                                                    mPsdyJbjs.remove(psdyJbj);
                                                    break;
                                                }
                                            }
                                            ToastUtil.shortToast(mContext, "删除成功");
                                        } else {
                                            CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
                                                    BaseInfoManager.getUserName(mContext) + "原因是：" + responseBody.getMessage()));
                                            ToastUtil.shortToast(mContext, "删除失败，原因是：" + responseBody.getMessage());
                                        }
                                    }
                                });
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initSelectGLayer();
                        dialog.dismiss();
                    }
                });
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
        pd = new ProgressDialog(getContext());
        pd.setMessage("正在查询设施...");
        pd.show();
        mComponentQueryResult.clear();
        btn_next.setVisibility(View.GONE);
        btn_prev.setVisibility(View.GONE);
        currIndex = 0;
        isFirst = true;
        final Point point = new Point(x, y);
        final List<LayerInfo> layerInfoList = new ArrayList<>();
        for (String url : LayerUrlConstant.newComponentUrls) {
            LayerInfo layerInfo = new LayerInfo();
            layerInfo.setUrl(url);
            layerInfoList.add(layerInfo);
        }
        List<LayerInfo> visibleLayerInfos = layersService.getVisibleLayerInfos();//获取可见图层列表，用于只查询可见图层
        Geometry geometry = GeometryEngine.buffer(point, mMapView.getSpatialReference(), 40 * mMapView.getResolution(), null);

        initComponentService();
        if (ifInDY && mStatus == 7) {
            componentService.queryComponentsForJBJ(geometry, new Callback2<List<Component>>() {
                @Override
                public void onSuccess(List<Component> components) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    if (ListUtil.isEmpty(components)) {
                        ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                        return;
                    }
                    mComponentQueryResult = new ArrayList<Component>();
                    mComponentQueryResult.addAll(components);
                    if (ListUtil.isEmpty(mComponentQueryResult)) {
                        initGLayer();
                        if (pd != null) {
                            pd.dismiss();
                        }
                        ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                        return;
                    }
                    showComponentsOnBottomSheet(mComponentQueryResult);
                }

                @Override
                public void onFail(Exception error) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                }
            }, visibleLayerInfos);
        } else if (ifInGuaJie || ifInDY || ifInSwerageUserMode) {
            componentService.queryComponentsForPSDY(geometry, new Callback2<List<Component>>() {
                @Override
                public void onSuccess(List<Component> components) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    if (ListUtil.isEmpty(components)) {
                        ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                        return;
                    }
                    final Component component = components.get(0);
                    if (ifInGuaJie) {
                        final String name = StringUtil.getNotNullString(component.getGraphic().getAttributeValue("单元名称"), "");
//                    String name = StringUtil.getNotNullString(components.get(0).getGraphic().getAttributeValue("排水单元名"),"");
                        initSelectGLayer();
                        drawGeometryForGD(component.getGraphic().getGeometry(), mSelectGLayer, false, true);
                        showDYCallout(name, null, component, null);

                    } else if (ifInDY || ifInSwerageUserMode) {
                        mComponentQueryResult = new ArrayList<Component>();
                        mComponentQueryResult.addAll(components);
                        showComponentsOnBottomSheet(mComponentQueryResult);
                    }
                }

                @Override
                public void onFail(Exception error) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                }
            });
        } else if (ifInDoubtMode) {
            componentService.queryComponentsForDoubt(geometry, new Callback2<List<Component>>() {
                @Override
                public void onSuccess(List<Component> components) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    if (ListUtil.isEmpty(components)) {
                        ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                        return;
                    }
                    mComponentQueryResult = new ArrayList<Component>();
                    mComponentQueryResult.addAll(components);
                    if (ListUtil.isEmpty(mComponentQueryResult)) {
                        initGLayer();
                        if (pd != null) {
                            pd.dismiss();
                        }
                        ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                        return;
                    }
                    showComponentsOnBottomSheet(mComponentQueryResult);
                }

                @Override
                public void onFail(Exception error) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                }
            });
        } else if (ifInPipeMode && mStatus == 0) {
//            componentService.queryComponentsUpload(geometry, new Callback2<List<Component>>() {
            componentService.queryComponentsForPipe(geometry, new Callback2<List<Component>>() {
                @Override
                public void onSuccess(List<Component> components) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    if (ListUtil.isEmpty(components)) {
                        ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                        return;
                    }
                    mComponentQueryResult = new ArrayList<Component>();
                    mComponentQueryResult.addAll(components);
                    if (ListUtil.isEmpty(mComponentQueryResult)) {
                        initGLayer();
                        if (pd != null) {
                            pd.dismiss();
                        }
                        ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                        return;
                    }
                    showComponentsOnBottomSheet(mComponentQueryResult);
                }

                @Override
                public void onFail(Exception error) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                }
            }, visibleLayerInfos);
        } else if (ifInCheckMode || ifInEditMode || mStatus == 1 || mStatus == 2) {
            componentService.queryComponentsForWell(geometry, new Callback2<List<Component>>() {
                @Override
                public void onSuccess(List<Component> components) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    if (ListUtil.isEmpty(components)) {
                        ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                        return;
                    }
                    mComponentQueryResult = new ArrayList<Component>();
                    mComponentQueryResult.addAll(components);
                    if (ListUtil.isEmpty(mComponentQueryResult)) {
                        initGLayer();
                        if (pd != null) {
                            pd.dismiss();
                        }
                        ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                        return;
                    }
                    showComponentsOnBottomSheet(mComponentQueryResult);
                }

                @Override
                public void onFail(Exception error) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                }
            }, visibleLayerInfos);
        } else {
            componentService.queryComponentsForAll(geometry, new Callback2<List<Component>>() {
                @Override
                public void onSuccess(List<Component> components) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    if (ListUtil.isEmpty(components)) {
                        ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                        return;
                    }
                    mComponentQueryResult = new ArrayList<Component>();
                    mComponentQueryResult.addAll(components);
                    if (ListUtil.isEmpty(mComponentQueryResult)) {
                        initGLayer();
                        if (pd != null) {
                            pd.dismiss();
                        }
                        ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                        return;
                    }
                    showComponentsOnBottomSheet(mComponentQueryResult);
                }

                @Override
                public void onFail(Exception error) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    ToastUtil.shortToast(getContext(), "该地点未查询到设施");
                }
            }, visibleLayerInfos, true);
//            componentService.queryPrimaryComponentsIdentify(geometry, mMapView, null);
        }

    }

    private void showPsdyDialog(String name, final Component component) {
        DialogUtil.MessageBox(mContext, "提示", "确认挂接到" + name + "排水单元？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String usid = StringUtil.getNotNullString(currentComponent.getGraphic().getAttributeValue("USID"), "");
                String objectId = StringUtil.getNotNullString(currentComponent.getGraphic().getAttributeValue("OBJECTID"), "");
                String psdytId = StringUtil.getNotNullString(component.getGraphic().getAttributeValue("OBJECTID"), "");
                double gjx = ((Point) currentComponent.getGraphic().getGeometry()).getX();
                double gjy = ((Point) currentComponent.getGraphic().getGeometry()).getY();
                Point point = GeometryUtil.getPolygonCenterPoint((Polygon) component.getGraphic().getGeometry());

                PsdyJbj psdyJbj = new PsdyJbj();
                psdyJbj.setUsid(usid);
                psdyJbj.setJbjObjectId(Integer.valueOf(objectId));
                psdyJbj.setPsdyObjectId(Integer.valueOf(psdytId));
                psdyJbj.setJbjX(gjx);
                psdyJbj.setJbjY(gjy);
                psdyJbj.setPsdyX(point.getX());
                psdyJbj.setPsdyY(point.getY());
                uploadPsdys(psdyJbj);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    /**
     * 排水单元挂接到接驳井
     *
     * @param name
     * @param component
     */
    private void showJBJDialog(String name, final Component component) {
        DialogUtil.MessageBox(mContext, "提示", "确认挂接到该接驳井？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String usid = StringUtil.getNotNullString(component.getGraphic().getAttributeValue("USID"), "");
                String objectId = StringUtil.getNotNullString(currentDYComponent.getGraphic().getAttributeValue("OBJECTID"), "");
                String psdytId = StringUtil.getNotNullString(component.getGraphic().getAttributeValue("OBJECTID"), "");
                Point centerPoint = (Point) component.getGraphic().getGeometry();
                double gjx = centerPoint.getX();
                double gjy = centerPoint.getY();
                Point point = GeometryUtil.getPolygonCenterPoint((Polygon) currentDYComponent.getGraphic().getGeometry());

                PsdyJbj psdyJbj = new PsdyJbj();
                psdyJbj.setUsid(usid);
                psdyJbj.setJbjObjectId(Integer.valueOf(psdytId));
                psdyJbj.setPsdyObjectId(Integer.valueOf(objectId));
                psdyJbj.setJbjX(gjx);
                psdyJbj.setJbjY(gjy);
                psdyJbj.setPsdyX(point.getX());
                psdyJbj.setPsdyY(point.getY());
                uploadPsdys(psdyJbj);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    private void uploadPsdys(final PsdyJbj psdyJbj) {
        if (mIdentificationService == null) {
            mIdentificationService = new CorrectFacilityService(mContext.getApplicationContext());
        }

        mIdentificationService.addPsdyJbj(psdyJbj)
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
                        CrashReport.postCatchedException(new Exception("连线失败，上报用户是：" +
                                BaseInfoManager.getUserName(mContext) + "原因是：" + e.getLocalizedMessage()));
                        ToastUtil.shortToast(mContext, "连线失败失败");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        final Callout callout = mMapView.getCallout();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (responseBody.getCode() == 200) {
                            callout.hide();
                            initSelectGLayer();
                            hideBottomSheet();
                            if (locationMarker != null) {
                                locationMarker.setVisibility(View.VISIBLE);
                            }
                            ifInLine = true;
                            mPsdyJbjs.add(psdyJbj);
                            initGraphicSelectGLayer();
                            drawPsdyLines(mPsdyJbjs);
                            ToastUtil.shortToast(mContext, "提交成功");
                        } else {
                            CrashReport.postCatchedException(new Exception("连线失败，上报用户是：" +
                                    BaseInfoManager.getUserName(mContext) + "原因是：" + responseBody.getMessage()));
                            ToastUtil.shortToast(mContext, "连线失败，原因是：" + responseBody.getMessage());
                        }
                    }
                });
    }

    private void queryForObjctId(String objectId) {
        pd = new ProgressDialog(getContext());
        pd.setMessage("正在查询道路...");
        pd.show();
        mComponentQueryResult.clear();
        btn_next.setVisibility(View.GONE);
        btn_prev.setVisibility(View.GONE);
        currIndex = 0;
        isFirst = true;
        initComponentService();
        if (true) {
            componentService.queryAllRoadByKeyword(objectId, new Callback2<List<Component>>() {
                //            componentService.queryPrimaryComponentsForObjectId2(objectId, new Callback2<List<Component>>() {
                @Override
                public void onSuccess(List<Component> components) {
                    if (pd != null) {
                        pd.dismiss();
                    }
                    if (ListUtil.isEmpty(components)) {
                        hideBottomSheet();
                        ToastUtil.shortToast(getContext(), "没有道路数据");
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
                        ToastUtil.shortToast(getContext(), "没有道路数据");
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
                    ToastUtil.shortToast(getContext(), "没有设施数据");
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
    GraphicsLayer mGraphicSelectLayer = null;
    GraphicsLayer mSelectGLayer = null;
    Graphic graphic = null;

    private void initStreamGLayer() {
        if (mStreamGLayer == null) {
            mStreamGLayer = new GraphicsLayer();
            mMapView.addLayer(mStreamGLayer);
        } else {
            mStreamGLayer.removeAll();
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

    private void initGLayer() {
        if (mGLayer == null) {
            mGLayer = new GraphicsLayer();
            mMapView.addLayer(mGLayer);
        } else {
            mGLayer.removeAll();
        }
    }

    private void initSelectGLayer() {
        if (mSelectGLayer == null) {
            mSelectGLayer = new GraphicsLayer();
            mMapView.addLayer(mSelectGLayer);
        } else {
            mSelectGLayer.removeAll();
        }
    }

    private void initArrowGLayer() {
        if (mArrowGLayer == null) {
            mArrowGLayer = new GraphicsLayer();
            mMapView.addLayer(mArrowGLayer);
        } else {
            mArrowGLayer.removeAll();
        }
    }

    /**
     * 在地图上画图形
     *
     * @param graphicsLayer
     * @param ifRemoveAll   在添加前是否移除掉所有
     * @param geometry
     */
    private void drawGeometry(final Geometry geometry, GraphicsLayer graphicsLayer, boolean ifRemoveAll, final boolean ifCenter) {

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
        final SimpleFillSymbol finalSimpleFillSymbol = simpleFillSymbol;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ifCenter && finalSimpleFillSymbol == null) {
                    mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
                } else {
                    mMapView.setExtent(geometry);
                }
            }
        }, 200);

    }

    /**
     * 在地图上画图形
     *
     * @param graphicsLayer
     * @param ifRemoveAll   在添加前是否移除掉所有
     * @param geometry
     */
    private void drawGeometryForGD(final Geometry geometry, GraphicsLayer graphicsLayer, boolean ifRemoveAll, final boolean ifCenter) {

        if (graphicsLayer == null || geometry == null) {
            return;
        }
        Symbol symbol = null;
        SimpleFillSymbol simpleFillSymbol = null;
        switch (geometry.getType()) {
            case LINE:
            case POLYLINE:
                symbol = new SimpleLineSymbol(getResources().getColor(R.color.agmobile_black), 5);
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
//        Geometry newGeometry =  null;
//        if(geometry instanceof Point){
//            if(((Point) geometry).getX()>ComponentService.BIG_POINT){
//                newGeometry = GeometryEngine.project(geometry,SpatialReference.create(ComponentService.HUANGPU_WIKI),SpatialReference.create(ComponentService.CHENGQU_WIKI));
//            }else{
//                newGeometry = geometry;
//            }
//        }else if(geometry instanceof Polyline) {
//            if(((Polyline) geometry).getPoint(0).getX() > ComponentService.BIG_POINT) {
//                newGeometry = GeometryEngine.project(geometry, SpatialReference.create(ComponentService.HUANGPU_WIKI), SpatialReference.create(ComponentService.CHENGQU_WIKI));
//            }else{
//                newGeometry = geometry;
//            }
//        }else if(geometry instanceof Polygon){
//            if(((Polygon) geometry).getPoint(0).getX() > ComponentService.BIG_POINT) {
//                newGeometry = GeometryEngine.project(geometry, SpatialReference.create(ComponentService.HUANGPU_WIKI), SpatialReference.create(ComponentService.CHENGQU_WIKI));
//            }else{
//                newGeometry = geometry;
//            }
//        }else if(geometry instanceof Line){
//            if(((Line) geometry).getStartX() > ComponentService.BIG_POINT) {
//                newGeometry = GeometryEngine.project(geometry, SpatialReference.create(ComponentService.HUANGPU_WIKI), SpatialReference.create(ComponentService.CHENGQU_WIKI));
//            }else{
//                newGeometry = geometry;
//            }
//        }

        if (symbol != null) {
            Graphic graphic = new Graphic(geometry, symbol);
            graphicsLayer.addGraphic(graphic);
            if (geometry.getType() == Geometry.Type.POLYLINE) { //如果是线，那么这个就是要传递给服务端的结果
                this.graphic = graphic;
                if (!isFirst) {
                    countDownAnimation(this.graphic, graphicsLayer);
                }
            }
        }
        if (simpleFillSymbol != null) {
            Graphic graphic = new Graphic(geometry, simpleFillSymbol);
            graphicsLayer.addGraphic(graphic);
            if (geometry.getType() == Geometry.Type.POLYLINE) { //如果是线，那么这个就是要传递给服务端的结果
                this.graphic = graphic;
            }
        }
        final SimpleFillSymbol finalSimpleFillSymbol = simpleFillSymbol;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ifCenter && finalSimpleFillSymbol == null) {
                    mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
                } else {
                    mMapView.setExtent(geometry);
                }
            }
        }, 200);

        isFirst = false;
    }

    /**
     * 倒数计时管线闪烁
     *
     * @param graphic
     * @param graphicsLayer
     */
    public void countDownAnimation(final Graphic graphic, final GraphicsLayer graphicsLayer) {
        isLoading = true;
        mIsAdd = true;
        countdown(4)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        //执行向里收缩的动画
                        graphicsLayer.removeAll();

                        graphicsLayer.addGraphic(graphic);

                        whileTake = true;
                        isLoading = false;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (mIsAdd) {
                            graphicsLayer.removeAll();
                            graphicsLayer.addGraphic(graphic);
                        } else {
                            graphicsLayer.removeAll();
                        }
                        mIsAdd = !mIsAdd;
                    }
                });
    }

    /**
     * 在地图上画图形
     *
     * @param graphicsLayer
     * @param ifRemoveAll   在添加前是否移除掉所有
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
            if (geometry.getType() == Geometry.Type.POLYLINE) { //如果是线，那么这个就是要传递给服务端的结果
                this.graphic = graphic;
            }
        }

        if (ifCenter) {
            mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        }
    }

    /**
     * 在地图上画图形
     *
     * @param graphicsLayer
     * @param ifRemoveAll   在添加前是否移除掉所有
     * @param geometry
     */
    private void drawGeometry(PsdyJbj psdyJbj, Geometry geometry, GraphicsLayer graphicsLayer, Symbol designatedSymbol, boolean ifRemoveAll, boolean ifCenter) {

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
            attributes.put("Id", psdyJbj.getId());
            Graphic graphic = new Graphic(geometry, symbol, attributes);
            graphicsLayer.addGraphic(graphic);
            if (geometry.getType() == Geometry.Type.POLYLINE) { //如果是线，那么这个就是要传递给服务端的结果
                this.graphic = graphic;
            }
        }

        if (ifCenter) {
            mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        }
    }

    /**
     * 在地图上画图形
     *
     * @param graphicsLayer
     * @param ifRemoveAll   在添加前是否移除掉所有
     * @param geometry
     */
    private void drawSelectGeometry(Geometry geometry, GraphicsLayer graphicsLayer, Symbol designatedSymbol, boolean ifRemoveAll, boolean ifCenter) {

        if (graphicsLayer == null) {
            return;
        }
        Symbol symbol = designatedSymbol;
        if (symbol == null) {
            switch (geometry.getType()) {
                case LINE:
                case POLYLINE:
                    symbol = new SimpleLineSymbol(0xff00ffff, 6);
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
            if (geometry.getType() == Geometry.Type.POLYLINE) { //如果是线，那么这个就是要传递给服务端的结果
                this.graphic = graphic;
            }
        }

        if (ifCenter) {
            mMapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
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
        Drawable drawable = null;
        if (ifInWellMode || ifInCheckMode && mStatus == 0) {
            drawable = ResourcesCompat.getDrawable(mContext.getResources(), com.augurit.agmobile.patrolcore.R.mipmap.ic_select_yinjing, null);
        } else {
            drawable = ResourcesCompat.getDrawable(mContext.getResources(), com.augurit.agmobile.patrolcore.R.mipmap.ic_select_location, null);
        }
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(getContext(), drawable);// xjx 改为兼容api19的方式获取drawable
        pictureMarkerSymbol.setOffsetY(16);
        return pictureMarkerSymbol;
    }

    /*
    private void onChangedBottomSheetState(int state) {
        if (state == STATE_EXPANDED) {
            //BottomSheet展开时显示设施详情
            if (component_intro.getVisibility() == View.VISIBLE) {
                showBottomSheetContent(component_detail_container.getId());
                showDetail();
            }

        } else if (state == STATE_COLLAPSED) {
            //BottomSheet展开时显示设施简要信息
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
        mBehavior.setState(STATE_EXPANDED);
//        dis_map_bottom_sheet.setVisibility(View.GONE);
        final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) locationButton
                .getLayoutParams();
        lp.bottomMargin = bottomMargin;
        locationButton.setLayoutParams(lp);
    }

    private void showComponentsOnBottomSheet(List<Component> componentQueryResult) {
        if (componentQueryResult.size() > 1) {
            btn_next.setVisibility(View.VISIBLE);
        }
        mMapView.getCallout().hide();
        //隐藏marker
        locationMarker.setVisibility(View.GONE);
        //initGLayer();
        String layerName = mComponentQueryResult.get(0).getLayerName();
        if (ifInCheckMode || ifInPipeMode || mStatus == 1 || mStatus == 2 || (isClick && ("排水管道".equals(layerName) || "排水管道(中心城区)".equals(layerName)))) {
            showBottomSheetForCheck(mComponentQueryResult.get(0));
        } else {
            showBottomSheet(mComponentQueryResult.get(0));
        }
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
                //如果上游，那么此时箭头方向是指向当前点击的部件
                //上游点
                Point point = mMapView.toScreenPoint(geometryCenter);
                //当前点
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
                //如果下游，那么此时箭头方向是指向当前的下游部件
                //下游点
                Point point = mMapView.toScreenPoint(geometryCenter);
                //当前点
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

    //中心城区接驳井
    private void showViewForCentralWell(final Component component) {
        if (mPsdyJbjs == null) mPsdyJbjs = new ArrayList<>();
        if (mIdentificationService == null) {
            mIdentificationService = new CorrectFacilityService(mContext.getApplicationContext());
        }
        final TextView psdy_delete = (TextView) map_bottom_sheet.findViewById(R.id.psdy_delete);
        psdy_delete.setVisibility(View.GONE);
        final TextView tv_cant_hook = (TextView) map_bottom_sheet.findViewById(R.id.tv_not_hook);
        tv_cant_hook.setVisibility(View.GONE);
        View psdy_line_del = map_bottom_sheet.findViewById(R.id.psdy_line_del);
        psdy_line_del.setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.psdy_line_del).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.psdy_jc).setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.ll_gc).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_gjdy).setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.line_gjdy).setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.line2).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line3).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line4).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line5).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line2).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line3).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line4).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_zhiyi).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_detail_left).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_detail_right).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_delete).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_sure).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.ll_check_hint).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_errorinfo).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_errorinfo).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_ms_end).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_tycd).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_pd).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.btn_container).setVisibility(View.VISIBLE);


        Map<String, Object> attributes = component.getGraphic().getAttributes();
        TextView title = (TextView) map_bottom_sheet.findViewById(R.id.title);
        title.setText(String.valueOf(component.getLayerName()).replace(".", ""));
        TextView tv_error_correct = (TextView) map_bottom_sheet.findViewById(R.id.tv_error_correct);
        tv_error_correct.setVisibility(View.GONE);
        TextView sort = (TextView) map_bottom_sheet.findViewById(R.id.sort);
        sort.setVisibility(View.VISIBLE);
//        sort.setText(String.valueOf(attributes.get("SORT")));
        initValue(component, sort, "", ComponentFieldKeyConstant.SORT, 0, "");
        TextView subtype = (TextView) map_bottom_sheet.findViewById(R.id.subtype);
        subtype.setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.line).setVisibility(View.VISIBLE);
        subtype.setText("接驳井");
        TextView field3Tv = (TextView) map_bottom_sheet.findViewById(R.id.field3);
        TextView tv_cz = (TextView) map_bottom_sheet.findViewById(R.id.tv_cz);
        field3Tv.setVisibility(View.VISIBLE);
        tv_cz.setVisibility(View.GONE);
//        field3Tv.setText("材质："+String.valueOf(attributes.get("MATERIAL")));
        initValue(component, field3Tv, "材质：", ComponentFieldKeyConstant.MATERIAL, 0, "");
        TextView tv_gj = (TextView) map_bottom_sheet.findViewById(R.id.tv_gj);
        tv_gj.setVisibility(View.VISIBLE);
//        tv_gj.setText("管理单位："+String.valueOf(attributes.get("MANAGEDEPT")));
        initValue(component, tv_gj, "管理单位：", "MANAGEDEPT", 0, "");
        TextView tv_ms_begin = (TextView) map_bottom_sheet.findViewById(R.id.tv_ms_begin);
        tv_ms_begin.setVisibility(View.VISIBLE);
        TextView addr = (TextView) map_bottom_sheet.findViewById(R.id.addr);
        addr.setVisibility(View.VISIBLE);
        initValue(component, addr, "所在位置：", ComponentFieldKeyConstant.ADDR, 0, "");
        initValue(component, tv_ms_begin, "所在道路：", ComponentFieldKeyConstant.LANE_WAY, 0, "");
//        addr.setText(String.valueOf(attributes.get("ADDR")));
//        TextView tv_gc_begin = (TextView) map_bottom_sheet.findViewById(R.id.tv_gc_begin);
//        tv_gc_begin.setVisibility(View.VISIBLE);
//        TextView tv_gc_end = (TextView) map_bottom_sheet.findViewById(R.id.tv_gc_end);
//        tv_gc_end.setVisibility(View.VISIBLE);
//        map_bottom_sheet.findViewById(R.id.addr).setVisibility(View.GONE);
//        map_bottom_sheet.findViewById(R.id.subtype).setVisibility(View.GONE);
//        map_bottom_sheet.findViewById(R.id.tv_gj).setVisibility(View.GONE);
//        map_bottom_sheet.findViewById(R.id.tv_cz).setVisibility(View.GONE);
//        map_bottom_sheet.findViewById(R.id.tv_gc_begin).setVisibility(View.GONE);
//        map_bottom_sheet.findViewById(R.id.tv_gc_end).setVisibility(View.GONE);
//        btn_next.setVisibility(View.VISIBLE);
//        btn_prev.setVisibility(View.VISIBLE);
        if (!ifInDY) {
            String usId = attributes.get("USID") + "";
            String objectId = attributes.get("OBJECTID") + "";
            getGjInfo(psdy_delete, usId, objectId, null);
            ((TextView) map_bottom_sheet.findViewById(R.id.tv_gjdy)).setText("挂接单元");
        } else {
            map_bottom_sheet.findViewById(R.id.psdy_jc).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.ll_gc).setVisibility(View.GONE);
            ((TextView) map_bottom_sheet.findViewById(R.id.tv_gjdy)).setText("确认挂接");
        }
        map_bottom_sheet.findViewById(R.id.tv_gjdy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ifInDY) {
                    ifInGuaJie = true;
                    addTopBarView("选择排水单元挂接");
                    mStatus = 5;
                    setMode(mStatus);
                    ToastUtil.shortToast(mContext, "请移动地图选择排水单元");
                    locationMarker.changeIcon(R.mipmap.ic_check_stream);
                    if (locationMarker != null) {
                        locationMarker.changeIcon(R.mipmap.ic_check_related);
                        locationMarker.setVisibility(View.VISIBLE);
                    }
                    tv_query_tip.setText("请移动地图选择排水单元");
                    initStreamGLayer();
                    hideBottomSheet();
                    setSearchFacilityListener();
                } else {
                    showJBJDialog(null, component);
                }
            }
        });
        map_bottom_sheet.findViewById(R.id.psdy_jc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent   intent = new Intent(getActivity(), WellMonitorActivity.class);
//                intent.putExtra("objectid",String.valueOf(component.getGraphic().getAttributeValue("OBJECTID")));
//                intent.putExtra("usid",String.valueOf(component.getGraphic().getAttributeValue("USID")));
//                intent.putExtra("type",String.valueOf(component.getGraphic().getAttributeValue("SORT")));
//                intent.putExtra("X",String.valueOf(component.getGraphic().getAttributeValue("X")));
//                intent.putExtra("Y",String.valueOf(component.getGraphic().getAttributeValue("Y")));
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), WellMonitorListActivity.class);
                intent.putExtra("objectid", String.valueOf(component.getGraphic().getAttributeValue("OBJECTID")));
                intent.putExtra("usid", String.valueOf(component.getGraphic().getAttributeValue("USID")));
                intent.putExtra("type", String.valueOf(component.getGraphic().getAttributeValue("SORT")));
                intent.putExtra("X", String.valueOf(component.getGraphic().getAttributeValue("X")));
                intent.putExtra("Y", String.valueOf(component.getGraphic().getAttributeValue("Y")));
                startActivity(intent);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                map_bottom_sheet.setVisibility(View.VISIBLE);
                mBehavior.setState(AnchorSheetBehavior.STATE_EXPANDED);
                resetInfoLayout();
            }
        }, 300);

    }

    /**
     * 获取挂接信息
     *
     * @param psdy_delete
     * @param usId
     * @param objectId
     * @param o
     */
    private void getGjInfo(final TextView psdy_delete, String usId, String objectId, String o) {
        mPsdyJbjs.clear();
        mIdentificationService.queryPsdyJbj(usId, objectId, o)
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
                        ToastUtil.shortToast(mContext, "获取连线信息失败");
                    }

                    @Override
                    public void onNext(Result2<List<PsdyJbj>> responseBody) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (responseBody.getCode() == 200) {
                            if (!ListUtil.isEmpty(responseBody.getData())) {
                                ifInLine = true;
                                map_bottom_sheet.findViewById(R.id.line_gjdy).setVisibility(View.VISIBLE);
                                if (!ifInDY) {
                                    map_bottom_sheet.findViewById(R.id.psdy_line_del).setVisibility(View.VISIBLE);
                                }
                                if (psdy_delete != null) {
                                    psdy_delete.setVisibility(View.VISIBLE);
                                    psdy_delete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            addTopBarView("删除接驳井连线");
                                            mStatus = 6;
                                            setMode(mStatus);
                                            ToastUtil.shortToast(mContext, "请选择连线");
//                                        locationMarker.changeIcon(R.mipmap.ic_check_stream);
//                                        if (locationMarker != null) {
//                                            locationMarker.changeIcon(R.mipmap.ic_check_related);
//                                            locationMarker.setVisibility(View.VISIBLE);
//                                        }
                                            locationMarker.setVisibility(View.GONE);
                                            tv_query_tip.setText("请选择要删除的接驳井连线");
//                                        initStreamGLayer();
                                            hideBottomSheet();
                                            mGLayer.clearSelection();
                                            mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                                        }
                                    });
                                }
                                drawPsdyLines(responseBody.getData());
                                List<PsdyJbj> tempList = responseBody.getData();
                                mPsdyJbjs.clear();
                                mPsdyJbjs.addAll(tempList);
                            }
                        } else {
                            ToastUtil.shortToast(mContext, "获取连线信息失败");
                        }
                    }
                });
    }

    //排水单元
    private void showViewForPSDY(final Component component) {
        currentDYComponent = component;
        tvResponsible.setVisibility(View.GONE);
        if (mPsdyJbjs == null) mPsdyJbjs = new ArrayList<>();
        if (mIdentificationService == null) {
            mIdentificationService = new CorrectFacilityService(mContext.getApplicationContext());
        }
        final TextView psdy_delete = (TextView) map_bottom_sheet.findViewById(R.id.psdy_delete);
        psdy_delete.setVisibility(View.GONE);
        View psdy_line_del = map_bottom_sheet.findViewById(R.id.psdy_line_del);
        psdy_line_del.setVisibility(View.GONE);

        //屏蔽监测按钮
        map_bottom_sheet.findViewById(R.id.psdy_jc).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.psdy_line_jc).setVisibility(View.GONE);
        ((TextView) map_bottom_sheet.findViewById(R.id.tv_gjdy)).setText(" 挂接井 ");
        map_bottom_sheet.findViewById(R.id.tv_gjdy).setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.tv_swerage_user).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line_swerage_user).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line_gjdy).setVisibility(View.INVISIBLE);
        map_bottom_sheet.findViewById(R.id.ll_gc).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line2).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line3).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line4).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line5).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line2).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line3).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line4).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_zhiyi).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_detail_left).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_delete).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_sure).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.ll_check_hint).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_errorinfo).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_errorinfo).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_ms_end).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_tycd).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_pd).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.btn_container).setVisibility(View.VISIBLE);

        Map<String, Object> attributes = component.getGraphic().getAttributes();
        TextView title = (TextView) map_bottom_sheet.findViewById(R.id.title);
        //无法挂接按钮
        final TextView tv_not_hook = (TextView) map_bottom_sheet.findViewById(R.id.tv_not_hook);
        final TextView tv_reason = (TextView) map_bottom_sheet.findViewById(R.id.reason);
        final String type = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(ComponentFieldKeyConstant.WFGJ), "");
        final String reason = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(ComponentFieldKeyConstant.WFGJ_REASON), "");
        if ("1".equals(type)) {
            tv_not_hook.setText("  取消无法挂接  ");
            tv_reason.setVisibility(View.VISIBLE);
            tv_not_hook.setBackgroundResource(R.drawable.sel_btn_delete);
        } else {
            tv_not_hook.setText("无法挂接");
            tv_reason.setVisibility(View.GONE);
            tv_not_hook.setBackgroundResource(R.drawable.round_blue_rectangle);
        }
//        if(StringUtil.isEmpty(reason)){
//            tv_reason.setVisibility(View.GONE);
//        }else{
//
//        }
        title.setText("排水单元");
        TextView tv_error_correct = (TextView) map_bottom_sheet.findViewById(R.id.tv_error_correct);
        tv_error_correct.setVisibility(View.GONE);
        TextView sort = (TextView) map_bottom_sheet.findViewById(R.id.sort);
        sort.setVisibility(View.GONE);
//        sort.setText(String.valueOf(attributes.get("SORT")));
        initValue(component, sort, "", ComponentFieldKeyConstant.SORT, 0, "");
        TextView subtype = (TextView) map_bottom_sheet.findViewById(R.id.subtype);
        subtype.setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.line).setVisibility(View.GONE);
        initValue(component, subtype, "单元名称：", "单元名称", 0, "");
        TextView field3Tv = (TextView) map_bottom_sheet.findViewById(R.id.field3);
        TextView tv_cz = (TextView) map_bottom_sheet.findViewById(R.id.tv_cz);
        field3Tv.setVisibility(View.GONE);
        tv_cz.setVisibility(View.GONE);
        TextView tv_gj = (TextView) map_bottom_sheet.findViewById(R.id.tv_gj);
        tv_gj.setVisibility(View.VISIBLE);
//        tv_gj.setText("管理单位："+String.valueOf(attributes.get("MANAGEDEPT")));
        initValue(component, tv_gj, "类型：", "类型LX", 0, "");
        TextView tv_ms_begin = (TextView) map_bottom_sheet.findViewById(R.id.tv_ms_begin);
        tv_ms_begin.setVisibility(View.VISIBLE);
        initValue(component, tv_ms_begin, "所属区：", "所属区", 0, "");
        TextView addr = (TextView) map_bottom_sheet.findViewById(R.id.addr);
        addr.setVisibility(View.VISIBLE);
        initValue(component, addr, "地址：", "地址DZ", 0, "");
        if ("1".equals(type)) {
            initValue(component, tv_reason, "无法挂接原因：", ComponentFieldKeyConstant.WFGJ_REASON, 0, "");
        }
        String objectId = attributes.get("OBJECTID") + "";
        // 加上这个判断，如果是在查询排水户的情况下，不显示 删除挂接 按钮
        if (!ifInSwerageUserMode) {
            getGjInfo(psdy_delete, null, null, objectId);
            tv_not_hook.setVisibility(View.VISIBLE);
        } else {
            tv_not_hook.setVisibility(View.GONE);
        }

        map_bottom_sheet.findViewById(R.id.tv_gjdy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTopBarView("选择接驳井挂接");
                mStatus = 7;
                setMode(mStatus);
                ToastUtil.shortToast(mContext, "请移动地图选择接驳井");
                locationMarker.changeIcon(R.mipmap.ic_check_stream);
                if (locationMarker != null) {
                    locationMarker.changeIcon(R.mipmap.ic_check_related);
                    locationMarker.setVisibility(View.VISIBLE);
                }
                tv_query_tip.setText("请移动地图选择接驳井");
                initStreamGLayer();
                hideBottomSheet();
                setSearchFacilityListener();
            }
        });

        tv_not_hook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(type)) {
                    //取消无法挂接
                    DialogUtil.MessageBox(mContext, "提示", "确定取消该排水单元的无法挂接状态？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String id = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.OBJECTID), "");
                            String reason = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(ComponentFieldKeyConstant.WFGJ_REASON), "");
                            cancelNotHook(id, reason);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                } else {
                    //提交无法挂接原因
                    showNotHookDialog(component, component.getGraphic().getGeometry(), tv_not_hook);
                }
            }
        });

        showSewerageUserBtnOnly(component);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                map_bottom_sheet.setVisibility(View.VISIBLE);
                mBehavior.setState(AnchorSheetBehavior.STATE_EXPANDED);
                resetInfoLayout();
            }
        }, 300);

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
            startP.setXY(psdyJbj.getJbjX(), psdyJbj.getJbjY());
            endP = new Point();
            endP.setXY(psdyJbj.getPsdyX(), psdyJbj.getPsdyY());
            polyline.startPath(startP);
            polyline.lineTo(endP);
            drawGeometry(psdyJbj, polyline, mGraphicSelectLayer, new SimpleLineSymbol(Color.BLUE, 2, SimpleLineSymbol.STYLE.DASH), false, false);
        }
    }

    /**
     * 加载设施信息，显示中底部BottomSheet中
     */
    private void showBottomSheet(final Component component) {
        currentComponent = component;
        ifInLine = false;
        tvResponsible.setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.reason).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.ll_no_ysk).setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.ll_gc).setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.tv_gjdy).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line_gjdy).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_swerage_user).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line_swerage_user).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.psdy_delete).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.psdy_line_del).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.psdy_jc).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.psdy_line_jc).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.ll_name_phone).setVisibility(View.GONE);
        boolean isOutWell = false;
        if (!TextUtils.isEmpty(component.getLayerName()) &&
                component.getLayerUrl().indexOf("/jbWell/MapServer") != -1 && component.getLayerUrl().endsWith("/0")) {
            isOutWell = true;
        }
        // map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.GONE);
        // pb_load_check_hint.setVisibility(View.VISIBLE);
        if ("接驳井.".equals(component.getLayerName()) && (component.getLayerUrl().endsWith("/2") || component.getLayerUrl().endsWith("/4"))) {
            //外围区接驳井
            if (ifInDY && mStatus == 7) {
                initSelectGLayer();
                drawGeometryForGD(component.getGraphic().getGeometry(), mSelectGLayer, false, true);
            } else {
                initGLayer();
                drawGeometryForGD(component.getGraphic().getGeometry(), mGLayer, false, true);
            }
//            drawGeometry(component.getGraphic().getGeometry(), mGeometryMapGLayer, false, false);
            showViewForCentralWell(component);
            return;
        } else if ("广州排水单元".equals(component.getLayerName())) {
            initGLayer();
            drawGeometryForGD(component.getGraphic().getGeometry(), mGLayer, false, true);
//            drawGeometry(component.getGraphic().getGeometry(), mGeometryMapGLayer, false, false);
            showViewForPSDY(component);
            return;
        } else {
            status = "2";
            person = "";
            tv_check_person.setText("");
            ll_check_hint.setVisibility(View.GONE);
            String layerName = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(ComponentFieldKeyConstant.LAYER_NAME), "");
            if (StringUtil.isEmpty(layerName)) {
                layerName = component.getLayerName();
            }

            if ("道路".equals(layerName)) {
                initGLayer();
                drawGeometryForGD(component.getGraphic().getGeometry(), mGLayer, false, true);
                showSimpleBottomView(component);
                return;
            }
            //检测是否有同区的人已经上报过(校核过)
            if ("窨井".equals(layerName) || "雨水口".equals(layerName) || "排水口".equals(layerName)) {
                checkIfHasBeenUploadBefore(component, 1);
                if (component.getGraphic().getGeometry() instanceof Point) {
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
                }
            } else {
                checkIfHasBeenUploadForComponent(component);
                setResponsibleForHP(component);
            }

            if (mStatus == 0) {
                initGLayer();
                drawGeometryForGD(component.getGraphic().getGeometry(), mGLayer, false, true);
            } else if (mStatus == 1 || mStatus == 2) {
                initStreamGLayer();
                drawGeometry(component.getGraphic().getGeometry(), mStreamGLayer, false, true);
            } else if (mStatus == 7) {
                initSelectGLayer();
                drawGeometry(component.getGraphic().getGeometry(), mSelectGLayer, false, true);
            }
            if (layerName == null || !attribute.contains(layerName)) {
                hideBottomSheet();
                return;
            }
            String errorInfo = null;
            Object oErrorInfo = component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ERROR_INFO);

            if (oErrorInfo != null) {
                errorInfo = oErrorInfo.toString();
            }

            final TextView psdy_delete = (TextView) map_bottom_sheet.findViewById(R.id.psdy_delete);
            psdy_delete.setVisibility(View.GONE);
            final View psdy_line_del = map_bottom_sheet.findViewById(R.id.psdy_line_del);
            psdy_line_del.setVisibility(View.GONE);
            TextView titleTv = (TextView) map_bottom_sheet.findViewById(R.id.title);
            TextView dateTv = (TextView) map_bottom_sheet.findViewById(R.id.date);
            TextView sortTv = (TextView) map_bottom_sheet.findViewById(R.id.sort);
            sortTv.setVisibility(View.VISIBLE);
            TextView subtypeTv = (TextView) map_bottom_sheet.findViewById(R.id.subtype);
            TextView field1Tv = (TextView) map_bottom_sheet.findViewById(R.id.field1);
            TextView field2Tv = (TextView) map_bottom_sheet.findViewById(R.id.field2);
            TextView field3Tv = (TextView) map_bottom_sheet.findViewById(R.id.field3);
            TextView addrTv = (TextView) map_bottom_sheet.findViewById(R.id.addr);
//          TextView code = (TextView) map_bottom_sheet.findViewById(R.id.code);
            View btn_container = map_bottom_sheet.findViewById(R.id.btn_container);//按钮控件容器
            TextView tv_gc_begin = (TextView) map_bottom_sheet.findViewById(R.id.tv_gc_begin);//起始高程
            TextView tv_gc_end = (TextView) map_bottom_sheet.findViewById(R.id.tv_gc_end);//终止高程
            tv_gc_end.setVisibility(View.VISIBLE);
            TextView tv_ms_begin = (TextView) map_bottom_sheet.findViewById(R.id.tv_ms_begin);//起始埋深
            TextView tv_ms_end = (TextView) map_bottom_sheet.findViewById(R.id.tv_ms_end);//终点埋深
            TextView tv_gj = (TextView) map_bottom_sheet.findViewById(R.id.tv_gj);//管径
            View ll_gj = map_bottom_sheet.findViewById(R.id.ll_gj);//管径
            TextView tv_cz = (TextView) map_bottom_sheet.findViewById(R.id.tv_cz);//净高
            View ll_gq = map_bottom_sheet.findViewById(R.id.ll_gq);//净高
            TextView tv_tycd = (TextView) map_bottom_sheet.findViewById(R.id.tv_tycd);//投影长度
            TextView tv_pd = (TextView) map_bottom_sheet.findViewById(R.id.tv_pd);//坡度
            View line = map_bottom_sheet.findViewById(R.id.line);
            View line2 = map_bottom_sheet.findViewById(R.id.line2);//分割线
            View line3 = map_bottom_sheet.findViewById(R.id.line3);//分割线
            View line4 = map_bottom_sheet.findViewById(R.id.line4);//分割线
            line4.setVisibility(View.GONE);
            final TextView tv_zhiyi = (TextView) map_bottom_sheet.findViewById(R.id.tv_zhiyi);//质疑按钮
            View stream_line3 = map_bottom_sheet.findViewById(R.id.stream_line3);//质疑分割线
            View stream_line4 = map_bottom_sheet.findViewById(R.id.stream_line4);//删除和查看详情的分割线
            View stream_line1 = map_bottom_sheet.findViewById(R.id.stream_line1);//删除和查看详情的分割线
            View ll_ms = map_bottom_sheet.findViewById(R.id.ll_msd);//删除和查看详情的分割线
            stream_line4.setVisibility(View.GONE);
            stream_line1.setVisibility(View.GONE);
            tv_zhiyi.setVisibility(View.GONE);
            stream_line3.setVisibility(View.GONE);
//        TextView code = (TextView) map_bottom_sheet.findViewById(R.id.code);
            //左边和右边的详情按钮
            TextView tv_detail_left = (TextView) map_bottom_sheet.findViewById(R.id.tv_detail_left);
            TextView tv_detail_right = (TextView) map_bottom_sheet.findViewById(R.id.tv_detail_right);

            initValue(component, addrTv, "所在道路：", ComponentFieldKeyConstant.ADDR, 0, "");
            line.setVisibility(View.VISIBLE);
            field3Tv.setVisibility(View.GONE);
            ll_gj.setVisibility(View.VISIBLE);
            ll_ms.setVisibility(View.VISIBLE);
            final String DOUBT_STATUS = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.DOUBT_STATUS));
            String checkState = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.CHECK_STATE), "");

            map_bottom_sheet.findViewById(R.id.ll_no_ysk).setVisibility(View.VISIBLE);
            if (layerName.contains("排水管道")) {//管道就把值加载出来
                btn_container.setVisibility(View.VISIBLE);
                addrTv.setVisibility(View.GONE);
                if (layerName.contains("中心城区")) {
                    btn_container.setVisibility(View.GONE);
                    initValue(component, field3Tv, "管线长度：", ComponentFieldKeyConstant.LENGTH);
                } else {
                    if ("-1".equals(checkState) || StringUtil.isEmpty(checkState)) {
                        tv_zhiyi.setVisibility(View.VISIBLE);
                        stream_line3.setVisibility(View.VISIBLE);
                    }
                    initValue(component, field3Tv, "管线长度：", ComponentFieldKeyConstant.LINE_LENGTH);
                }
                line2.setVisibility(View.VISIBLE);
                line3.setVisibility(View.VISIBLE);
                line4.setVisibility(View.VISIBLE);
                ll_gq.setVisibility(View.GONE);
                initValue(component, tv_gc_begin, "起始管底高程：", ComponentFieldKeyConstant.BEG_H);
                initValue(component, tv_gc_end, "终止管底高程：", ComponentFieldKeyConstant.END_H);
                initValue(component, tv_ms_begin, "起点管底埋深：", ComponentFieldKeyConstant.BEGCEN_DEE);
                initValue(component, tv_ms_end, "终点管底埋深：", ComponentFieldKeyConstant.ENDCEN_DEE);
                initValue(component, tv_gj, "管径：", ComponentFieldKeyConstant.PIPE_GJ, 0, "");
            } else if (layerName.contains("排水沟渠")) {
                line2.setVisibility(View.GONE);
                line3.setVisibility(View.VISIBLE);
                line4.setVisibility(View.VISIBLE);
                ll_gq.setVisibility(View.VISIBLE);
                btn_container.setVisibility(View.GONE);
                addrTv.setVisibility(View.VISIBLE);
                tv_cz.setVisibility(View.VISIBLE);
                if ("-1".equals(checkState) || StringUtil.isEmpty(checkState)) {
                    tv_zhiyi.setVisibility(View.VISIBLE);
                    stream_line3.setVisibility(View.VISIBLE);
                }
                initValue(component, tv_gc_begin, "起始渠底高程：", ComponentFieldKeyConstant.BEG_H);
                initValue(component, tv_gc_end, "终止渠底高程：", ComponentFieldKeyConstant.END_H);
                initValue(component, tv_ms_begin, "起点管底埋深：", ComponentFieldKeyConstant.BEGCIN_DEEP);
                initValue(component, tv_ms_end, "终点管底埋深：", ComponentFieldKeyConstant.ENDCIN_DEEP);
                initValue(component, tv_gj, "宽度：", ComponentFieldKeyConstant.WIDTH);
                initValue(component, tv_cz, "净高：", ComponentFieldKeyConstant.HEIGHT);
                initValue(component, tv_tycd, "投影长度：", ComponentFieldKeyConstant.LENGTH);
                initValue(component, tv_pd, "坡度：", ComponentFieldKeyConstant.IP);
            } else if (layerName.contains("窨井")) {

                line2.setVisibility(View.GONE);
                line3.setVisibility(View.GONE);
                line4.setVisibility(View.GONE);
                ll_gq.setVisibility(View.GONE);
                stream_line4.setVisibility(View.VISIBLE);
                tv_gc_end.setVisibility(View.GONE);
                field3Tv.setVisibility(View.GONE);
                tv_gc_end.setText("");
                btn_container.setVisibility(View.VISIBLE);
                btn_container.setVisibility(View.VISIBLE);
                if (layerName.contains("中心城区")) {
                    btn_container.setVisibility(View.GONE);
                    addrTv.setVisibility(View.VISIBLE);
                    tv_cz.setVisibility(View.GONE);
                    tv_ms_end.setText("");
                    initValue(component, tv_gj, "井盖大小：", ComponentFieldKeyConstant.COVER_SIZE, 2, "mm");
                    initValue(component, tv_gc_begin, "地面高程：", ComponentFieldKeyConstant.SUR_H);
                    initValue(component, tv_ms_begin, "井底高程：", ComponentFieldKeyConstant.BOTTOM_H);
                    tv_detail_left.setVisibility(View.GONE);
                    tv_detail_right.setVisibility(View.VISIBLE);
                } else {
                    if ("-1".equals(checkState) || StringUtil.isEmpty(checkState)) {
                        tv_zhiyi.setVisibility(View.VISIBLE);
                        stream_line3.setVisibility(View.VISIBLE);
                    }
                    tv_detail_left.setVisibility(View.VISIBLE);
                    line4.setVisibility(View.GONE);
                    tv_detail_right.setVisibility(View.GONE);
                    btn_container.setVisibility(View.VISIBLE);
                    addrTv.setVisibility(View.VISIBLE);
                    tv_cz.setVisibility(View.GONE);
                    tv_ms_end.setVisibility(View.GONE);
                    tv_gj.setText("");
                    if (isOutWell) {
                        initValue(component, tv_gc_begin, "管理单位：", "SUPERVISE_ORG_NAME", 0, "");
                    } else {
                        initValue(component, tv_gc_begin, "已有挂牌号：", ComponentFieldKeyConstant.ATTR_FIVE, 0, "");
                    }
                    initValue(component, tv_gj, "井盖大小：", ComponentFieldKeyConstant.COVER_SIZE, 2, "mm");
                    ll_gj.setVisibility(View.GONE);
                    initValue(component, tv_ms_begin, "所在道路：", ComponentFieldKeyConstant.ROAD, 0, "");
                    initValue(component, addrTv, "设施位置：", ComponentFieldKeyConstant.ADDR, 0, "");
                }

            } else if (layerName.contains("排水口")) {
                tv_detail_left.setVisibility(View.VISIBLE);
                stream_line4.setVisibility(View.VISIBLE);
                line4.setVisibility(View.VISIBLE);
                tv_detail_right.setVisibility(View.GONE);
                ll_gj.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
                line3.setVisibility(View.GONE);
                line4.setVisibility(View.GONE);
                ll_gq.setVisibility(View.GONE);
                sortTv.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                tv_gc_end.setVisibility(View.GONE);
                btn_container.setVisibility(View.VISIBLE);
                addrTv.setVisibility(View.VISIBLE);
                tv_cz.setVisibility(View.GONE);
                tv_ms_end.setText("");
                tv_gj.setText("");
                if ("-1".equals(checkState) || StringUtil.isEmpty(checkState)) {
                    tv_zhiyi.setVisibility(View.VISIBLE);
                    stream_line3.setVisibility(View.VISIBLE);
                }
                if (layerName.contains("中心城区")) {
                    btn_container.setVisibility(View.GONE);
                    initValue(component, tv_gc_begin, "排水口岸别：", ComponentFieldKeyConstant.ATTR_FOUR, 0, "");
                    initValue(component, subtypeTv, "河涌名称：", ComponentFieldKeyConstant.RIVERNAME, 0, "");
                    initValue(component, tv_ms_begin, "所在道路：", ComponentFieldKeyConstant.LANE_WAY, 0, "");
                    initValue(component, addrTv, "设施位置：", ComponentFieldKeyConstant.ADDR, 0, "");
                } else {
                    btn_container.setVisibility(View.VISIBLE);
                    initValue(component, tv_gc_begin, "排水口岸别：", ComponentFieldKeyConstant.ATTR_FOUR, 0, "");
                    initValue(component, subtypeTv, "河涌名称：", ComponentFieldKeyConstant.ATTR_ONE, 0, "");
                    initValue(component, tv_ms_begin, "所在道路：", ComponentFieldKeyConstant.ROAD, 0, "");
                    initValue(component, addrTv, "设施位置：", ComponentFieldKeyConstant.ADDR, 0, "");
                }
            } else if (layerName.contains("雨水口")) {
                stream_line4.setVisibility(View.VISIBLE);
                if ("-1".equals(checkState) || StringUtil.isEmpty(checkState)) {
                    tv_zhiyi.setVisibility(View.VISIBLE);
                    stream_line3.setVisibility(View.VISIBLE);
                }
                map_bottom_sheet.findViewById(R.id.ll_no_ysk).setVisibility(View.GONE);
                tv_detail_left.setVisibility(View.VISIBLE);
                tv_detail_right.setVisibility(View.GONE);
                line4.setVisibility(View.VISIBLE);
                if (layerName.contains("中心城区")) {
                    btn_container.setVisibility(View.GONE);
                } else {
                    btn_container.setVisibility(View.VISIBLE);
                }
            } else if (layerName.contains("存疑区域")) {
                ll_ms.setVisibility(View.GONE);
                tv_detail_left.setVisibility(View.GONE);
                stream_line4.setVisibility(View.GONE);
                line4.setVisibility(View.VISIBLE);
                tv_detail_right.setVisibility(View.GONE);
                ll_gj.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
                line3.setVisibility(View.GONE);
                line4.setVisibility(View.GONE);
                ll_gq.setVisibility(View.GONE);
                sortTv.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                if ("2".equals(DOUBT_STATUS)) {
                    btn_container.setVisibility(View.GONE);
                } else {
                    btn_container.setVisibility(View.VISIBLE);
                }
                addrTv.setVisibility(View.VISIBLE);
                tv_zhiyi.setVisibility(View.GONE);
                stream_line3.setVisibility(View.GONE);
                tv_cz.setVisibility(View.GONE);
                tv_ms_begin.setVisibility(View.GONE);
                tv_gc_end.setVisibility(View.GONE);
                tv_ms_end.setText("");
                tv_gj.setText("");
//            subtypeTv.setText("河涌名称：" + component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
                initValue(component, tv_gc_begin, "所在单位：", ComponentFieldKeyConstant.MEMO2, 0, "");
                initValue(component, subtypeTv, "上报人：", ComponentFieldKeyConstant.LRR, 0, "");
                initValue(component, addrTv, "存疑原因：", ComponentFieldKeyConstant.MEMO, 0, "");
            } else {
                btn_container.setVisibility(View.VISIBLE);
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

            if ("-1".equals(checkState)) {
                tv_zhiyi.setText("取消存疑");
            } else {
                tv_zhiyi.setText(" 存疑 ");
            }

            final String finalLayerName = layerName;
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, JournalsDetailListActivity.class);
                    intent.putExtra("component", component);
                    startActivityForResult(intent, 123);
                    if (StringUtil.isEmpty(component.getLayerName())) {
                        if (!StringUtil.isEmpty(finalLayerName)) {
                            component.setLayerName(finalLayerName);
                        }
                    }
                }
            };
            tv_detail_left.setOnClickListener(listener);
            tv_detail_right.setOnClickListener(listener);

            TextView tv_errorinfo = (TextView) map_bottom_sheet.findViewById(R.id.tv_errorinfo);
            final String type = layerName;

            String subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
            if (StringUtil.isEmpty(subtype) || "null".equals(subtype)) {
                subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE));
            }
            String usid = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.USID));
            String objectId = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.OBJECTID));

            String title = StringUtil.getNotNullString(getLayerName(type), "");

            if (layerName.contains("排水口")) {
                title = StringUtil.getNotNullString(getLayerName(type), "") + "(" + objectId + ")";
            }
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
                if (StringUtil.isEmpty(sort) || "null".equals(sort)) {
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
            if ("雨水口".equals(layerName)) {
                String feature = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
                sortTv.setText(StringUtil.getNotNullString(feature, ""));
            }
            if ("雨水口".equals(type)) {
                String style = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE));
                subtypeTv.setText(StringUtil.getNotNullString(style, ""));
                subtypeTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.dust_grey, null));
            } else if (!"排水口(中心城区)".equals(type) && !"排水口".equals(type) && !"存疑区域".equals(type)) {
                subtypeTv.setText(StringUtil.getNotNullString(subtype, ""));
            }


            //已挂牌编号
            String codeValue = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FIVE));
            if (!StringUtil.isEmpty(codeValue)) {
                codeValue = codeValue.trim();
            }
            /**
             * 修改属性三
             */
            String field3 = "";
            if (layerName.contains("窨井")) {
                String cz = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE), "");
                if (StringUtil.isEmpty(cz)) {
                    field3 = "井盖材质: " + StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.COVER_MATERIAL), "");
                }
                field3Tv.setText(field3);
            }

//        tv_parent_org_name.setVisibility(View.GONE);
        /*
        if (errorInfo == null) {
            tv_errorinfo.setVisibility(View.GONE);
        } else {
            tv_errorinfo.setVisibility(View.VISIBLE);
            tv_errorinfo.setText(errorInfo + "?");
        }
        */
            tv_errorinfo.setVisibility(View.GONE);


            String parentOrg = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FOUR));
//        tv_parent_org_name.setText("权属单位：" + StringUtil.getNotNullString(parentOrg, ""));
            ////   showBottomSheetContent(component_intro.getId());

            View treamLine = map_bottom_sheet.findViewById(R.id.stream_line);

            final String reportType = StringUtil.getNotNullString(component.getGraphic().getAttributes().get("REPORT_TYPE"), "");
            /**
             * 确认按钮
             */
            map_bottom_sheet.findViewById(R.id.stream_line).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.tv_sure).setVisibility(View.GONE);
            final String finalLayerName1 = layerName;
            map_bottom_sheet.findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("delete".equals(reportType)) {
                        ToastUtil.shortToast(mContext, "该井已被删除，正在审核中，请勿重复操作！");
                        return;
                    }
                    String message = "";
                    if (ifInLine && !ListUtil.isEmpty(mPsdyJbjs)) {
                        message = "该接驳井已挂接" + mPsdyJbjs.size() + "个排水单元，是否确定删除？";
                    } else {
                        message = "是否确定要删除该" + finalLayerName1 + "？";
                    }
                    DialogUtil.MessageBox(mContext, "提示", message, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Object o = component.getGraphic().getAttributes().get(UploadLayerFieldKeyConstant.REPORT_TYPE);

                            Long markId = objectToLong(component.getGraphic().getAttributes().get(UploadLayerFieldKeyConstant.MARK_ID));
                            if (o != null) {
                                if (UploadLayerFieldKeyConstant.CORRECT_ERROR.equals(o.toString()) || UploadLayerFieldKeyConstant.CONFIRM.equals(o.toString())) {
                                    //纠错
                                    ModifiedFacility modifiedFacilityFromGraphic = getModifiedFacilityFromGraphic(component.getGraphic().getAttributes(), component.getGraphic().getGeometry());
                                    deleteModify(modifiedFacilityFromGraphic);
                                } else {
                                    //新增
                                    deleteFacility(markId, component);
                                }
                            } else {
                                ModifiedFacility modifiedFacilityFromGraphic = getModifiedFacilityFromGraphic(component.getGraphic().getAttributes(), component.getGraphic().getGeometry());
                                deleteModify(modifiedFacilityFromGraphic);
                            }
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            });

            map_bottom_sheet.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ifInWellMode = true;
                    if (mGeometryMapGLayer == null) {
                        mGeometryMapGLayer = new GraphicsLayer();
                        mMapView.addLayer(mGeometryMapGLayer);
                    }
                    if (mGeometryMapOnTouchListener == null) {
                        mGeometryMapOnTouchListener = new GeometryDragOnTouchListener(mContext, mMapView, mGeometryMapGLayer);
                    }
                    mMapView.setOnTouchListener(mGeometryMapOnTouchListener);
                    mGeometryMapOnTouchListener.setOperationState(GeometryDragOnTouchListener.OperationState.STATE_DRAW_OVER);
                    drawGeometry(component.getGraphic().getGeometry(), mGeometryMapGLayer, false, false);
                    mGLayer.removeAll();
                    mTitle = "移动窨井位置";
                    ll_topbar_container2.setVisibility(View.VISIBLE);
                    addTopBarView(mTitle);
                    tv_query_tip.setText("长按窨井图标移动位置");
                    ll_topbar_container.setVisibility(View.VISIBLE);
                    hideBottomSheet();
                    return;
                }
            });
            /**
             * 纠错按钮
             */
            ((TextView) map_bottom_sheet.findViewById(R.id.tv_error_correct)).setText(" 修改 ");
            map_bottom_sheet.findViewById(R.id.tv_error_correct).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type.equals("存疑区域")) {
                        DialogUtil.MessageBox(mContext, "提示", "确定取消该设施的存疑状态？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String id = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.DOUBT_ID), "");
                                cancelDoubt(id, tv_zhiyi);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        return;
                    }
                    if ("delete".equals(reportType)) {
                        ToastUtil.shortToast(mContext, "该窨井已删除！");
                        return;
                    }
                    Object o = component.getGraphic().getAttributes().get(UploadLayerFieldKeyConstant.REPORT_TYPE);
                    if (o != null) {
                        if (UploadLayerFieldKeyConstant.ADD.equals(o.toString())) {
                            //新增
                            UploadedFacility uploadedFacility = getUploadedFacilityFromGraphic(component.getGraphic().getAttributes(), component.getGraphic().getGeometry());
                            Intent intent = new Intent(mContext, ReEditUploadFacilityActivity.class);
                            intent.putExtra("isLoad", true);
                            intent.putExtra("data", (Parcelable) uploadedFacility);
                            startActivity(intent);
                            return;
                        }
                    }
                    if ("0".equals(status)) {
                        //说明有人上报过，那么要提示
                        DialogUtil.MessageBox(mContext, "提醒", "        " + person + "已经校核过该设施，请确定是否仍要校核该设施？",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        btn_check.setVisibility(View.VISIBLE);
                                        btn_check_cancel.setVisibility(View.GONE);
                                        Intent intent = new Intent(mContext, CorrectOrConfirimFacilityActivity.class);
                                        if (StringUtil.isEmpty(component.getLayerName())) {
                                            if (!StringUtil.isEmpty(type)) {
                                                component.setLayerName(type);
                                            }
                                        }
                                        intent.putExtra("component", component);
                                        //从此处进入校核界面的话允许用户修改窨井类型
                                        intent.putExtra("isAllowEditWellType", true);
                                        startActivityForResult(intent, 123);
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                    } else {
                        btn_check.setVisibility(View.VISIBLE);
                        btn_check_cancel.setVisibility(View.GONE);
                        Intent intent = new Intent(mContext, CorrectOrConfirimFacilityActivity.class);
                        if (StringUtil.isEmpty(component.getLayerName())) {
                            if (!StringUtil.isEmpty(type)) {
                                component.setLayerName(type);
                            }
                        }
                        intent.putExtra("component", component);
                        //从此处进入校核界面的话允许用户修改窨井类型
                        intent.putExtra("isAllowEditWellType", true);
                        startActivityForResult(intent, 123);
                    }

                }
            });

            ///质疑的提交
            tv_zhiyi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cunyi = tv_zhiyi.getText().toString().trim();
                    if (cunyi.equals("取消存疑")) {
                        DialogUtil.MessageBox(mContext, "提示", "确定取消该设施的存疑状态？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String id = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.DOUBT_ID), "");
                                cancelDoubt(id, tv_zhiyi);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    } else {
                        showDoubtDialog(component, component.getGraphic().getGeometry(), tv_zhiyi);
                    }

                }
            });
            //删除连线
            psdy_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addTopBarView("删除接驳井连线");
                    mStatus = 6;
                    setMode(mStatus);
                    ToastUtil.shortToast(mContext, "请选择连线");
//                                        locationMarker.changeIcon(R.mipmap.ic_check_stream);
//                                        if (locationMarker != null) {
//                                            locationMarker.changeIcon(R.mipmap.ic_check_related);
//                                            locationMarker.setVisibility(View.VISIBLE);
//                                        }
                    locationMarker.setVisibility(View.GONE);
                    tv_query_tip.setText("请选择要删除的接驳井连线");
//                                        initStreamGLayer();
                    hideBottomSheet();
                    mGLayer.clearSelection();
                    mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                }
            });
            if (isOutWell && !ifInDY) {
                if (mIdentificationService == null) {
                    mIdentificationService = new CorrectFacilityService(mContext.getApplicationContext());
                }
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_gjdy)).setText("挂接单元");
                String usids = null;
                if (!StringUtil.isEmpty(usid) && !"null".equals(usid)) {
                    usids = usid;
                }
                mPsdyJbjs.clear();
                mIdentificationService.queryPsdyJbj(usids, objectId, null)
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
                                ToastUtil.shortToast(mContext, "获取连线信息失败");
                            }

                            @Override
                            public void onNext(Result2<List<PsdyJbj>> responseBody) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                if (responseBody.getCode() == 200) {
                                    if (!ListUtil.isEmpty(responseBody.getData())) {
                                        ifInLine = true;
                                        psdy_delete.setVisibility(View.VISIBLE);
                                        psdy_line_del.setVisibility(View.VISIBLE);
                                        drawPsdyLines(responseBody.getData());
                                        List<PsdyJbj> tempList = responseBody.getData();
                                        if (mPsdyJbjs == null) mPsdyJbjs = new ArrayList<>();
                                        mPsdyJbjs.clear();
                                        ;
                                        mPsdyJbjs.addAll(tempList);
                                    }
                                } else {
                                    ToastUtil.shortToast(mContext, "获取连线信息失败");
                                }
                            }
                        });
            } else {
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_gjdy)).setText("确认挂接");
                psdy_line_del.setVisibility(View.GONE);
            }

            //单元挂接
            map_bottom_sheet.findViewById(R.id.tv_gjdy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ifInDY) {
                        ifInGuaJie = true;
                        addTopBarView("选择排水单元挂接");
                        mStatus = 5;
                        setMode(mStatus);
                        ToastUtil.shortToast(mContext, "请移动地图选择排水单元");
                        locationMarker.changeIcon(R.mipmap.ic_check_stream);
                        if (locationMarker != null) {
                            locationMarker.changeIcon(R.mipmap.ic_check_related);
                            locationMarker.setVisibility(View.VISIBLE);
                        }
                        tv_query_tip.setText("请移动地图选择排水单元");
                        initStreamGLayer();
                        hideBottomSheet();
                        setSearchFacilityListener();
                    } else {
                        showJBJDialog(null, component);
                    }
                }
            });


            if (!userCanEdit && (layerName.contains("窨井") || layerName.contains("雨水口") || layerName.contains("排水口"))) {
                tv_zhiyi.setVisibility(View.GONE);
                stream_line3.setVisibility(View.GONE);
                tv_detail_left.setVisibility(View.GONE);
                stream_line4.setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.tv_delete).setVisibility(View.GONE);
                stream_line1.setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.tv_sure).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.stream_line2).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.stream_line).setVisibility(View.GONE);
                tv_detail_right.setVisibility(View.VISIBLE);
            } else if (!userCanEdit) {
                btn_container.setVisibility(View.GONE);
            } else if (layerName.contains("窨井") || layerName.contains("雨水口") || layerName.contains("排水口")) {
                map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.VISIBLE);
                map_bottom_sheet.findViewById(R.id.stream_line2).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.stream_line).setVisibility(View.GONE);
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_delete)).setVisibility(View.VISIBLE);
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_sure)).setVisibility(View.GONE);
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_sure)).setText("修改位置");
                map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.VISIBLE);
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_error_correct)).setText(" 修改 ");
                if ("-1".equals(checkState)) {
                    map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.GONE);
                    map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.GONE);
                }
            } else if (layerName.equals("存疑区域")) {
                map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.stream_line2).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.stream_line).setVisibility(View.GONE);
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_delete)).setVisibility(View.GONE);
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_sure)).setVisibility(View.GONE);
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_sure)).setText("修改位置");
                map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.VISIBLE);
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_error_correct)).setText("取消存疑");
            } else {
                map_bottom_sheet.findViewById(R.id.stream_line).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.tv_delete).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.tv_sure).setVisibility(View.GONE);
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_error_correct)).setText(" 修改 ");
            }
        }
        View jcView = map_bottom_sheet.findViewById(R.id.psdy_jc);
        View jcLineView = map_bottom_sheet.findViewById(R.id.psdy_line_jc);
        if (isOutWell) {

            map_bottom_sheet.findViewById(R.id.btn_container).setVisibility(View.VISIBLE);
            if (ifInDY && mStatus == 7) {
                jcView.setVisibility(View.GONE);
                jcLineView.setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.stream_line2).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.stream_line4).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.stream_line).setVisibility(View.GONE);
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_delete)).setVisibility(View.GONE);
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_sure)).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.tv_zhiyi).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.tv_detail_right).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.tv_detail_left).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.line_gjdy).setVisibility(View.INVISIBLE);
            } else {
                jcView.setVisibility(View.VISIBLE);
                jcLineView.setVisibility(View.VISIBLE);
                map_bottom_sheet.findViewById(R.id.line_gjdy).setVisibility(View.VISIBLE);
            }
            map_bottom_sheet.findViewById(R.id.tv_gjdy).setVisibility(View.VISIBLE);

            jcView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WellMonitorListActivity.class);
                    intent.putExtra("objectid", String.valueOf(component.getGraphic().getAttributeValue("OBJECTID")));
                    intent.putExtra("usid", "");
                    intent.putExtra("type", String.valueOf(component.getGraphic().getAttributeValue("ATTR_TWO")));
                    intent.putExtra("X", String.valueOf(component.getGraphic().getAttributeValue("X")));
                    intent.putExtra("Y", String.valueOf(component.getGraphic().getAttributeValue("Y")));
                    startActivity(intent);
                }
            });

        } else {
            map_bottom_sheet.findViewById(R.id.tv_gjdy).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.line_gjdy).setVisibility(View.GONE);
            jcView.setVisibility(View.GONE);
            jcLineView.setVisibility(View.GONE);
        }
        ////// showBottomSheetContent(component_intro.getId());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                map_bottom_sheet.setVisibility(View.VISIBLE);
                mBehavior.setState(AnchorSheetBehavior.STATE_EXPANDED);
                resetInfoLayout();
            }
        }, 300);
    }

    private void showSimpleBottomView(Component component) {
        TextView title = (TextView) map_bottom_sheet.findViewById(R.id.title);
        title.setText(String.valueOf(component.getGraphic().getAttributes().get("NAME")));
        if(tvResponsible != null){
            tvResponsible.setVisibility(View.GONE);
        }
        map_bottom_sheet.findViewById(R.id.line).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line2).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line3).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line4).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line5).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line2).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line3).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.stream_line4).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_zhiyi).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_detail_left).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_delete).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_sure).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.ll_check_hint).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_errorinfo).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.sort).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.subtype).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.field3).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_gj).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_cz).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_gc_begin).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_gc_end).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_ms_begin).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_ms_end).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_tycd).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.tv_pd).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.addr).setVisibility(View.GONE);
        btn_next.setVisibility(View.VISIBLE);
        btn_prev.setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.btn_container).setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                map_bottom_sheet.setVisibility(View.VISIBLE);
                mBehavior.setState(AnchorSheetBehavior.STATE_EXPANDED);
                resetInfoLayout();
            }
        }, 300);

    }

    /**
     * 展示排水户列表
     */
    private void showSwerageUserBottomSheet(Component component) {
        mSewerageUserListView.setCurrentComponent(component);
        mSewerageUserListView.setVisibility(View.VISIBLE);
        mSwerageUserListBehavoir.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    /**
     * 隐藏排水户列表
     */
    private void dismissSwerageUserBottomSheet() {
        mSewerageUserListView.setVisibility(View.INVISIBLE);
        mSwerageUserListBehavoir.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    /**
     * 设置黄埔区排水管道，排水管井的责任人
     * @param component
     */
    private void setResponsibleForHP(Component component){
        Log.d("检测责任人", "setResponsibleForHP 被调用");
        if(component.getLayerName().contains("中心城区")){
            Map<String, Object> attrs = component.getGraphic().getAttributes();
            if(attrs != null && attrs.get(ComponentFieldKeyConstant.SUPERNAME) != null
                    && attrs.get(ComponentFieldKeyConstant.SUPERNAME).toString().length() > 0){
                map_bottom_sheet.findViewById(R.id.ll_name_phone).setVisibility(View.VISIBLE);
                map_bottom_sheet.findViewById(R.id.tv_no_data).setVisibility(View.GONE);
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_name)).setText("管养负责人:" + attrs.get(ComponentFieldKeyConstant.MAINTNAME));
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone)).setText("电话:" + attrs.get(ComponentFieldKeyConstant.MAINTPHONE));
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_name2)).setText("监管负责人:" + attrs.get(ComponentFieldKeyConstant.SUPERNAME));
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone2)).setText("电话:" + attrs.get(ComponentFieldKeyConstant.SUPERPHONE));
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_name3)).setText("属地水务部门负责人:" + attrs.get(ComponentFieldKeyConstant.SWNAME));
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone3)).setText("电话:" + attrs.get(ComponentFieldKeyConstant.SWPHONE));
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_name4)).setText("技术负责人:" + attrs.get(ComponentFieldKeyConstant.TECHNICNAME));
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone4)).setText("电话:" + attrs.get(ComponentFieldKeyConstant.TECHNICPHONE));
            }
        }
    }

    /**
     * 设置责任人
     *
     * @param component
     */
    private void setResponsible(Component component, int type) {
        Log.d("检测责任人", "setResponsible 被调用");
        if (component.getGraphic() != null && component.getGraphic().getAttributes() != null
                && component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.OBJECTID) != null) {
            //通过usid去请求接口是否已经有同区的人报过
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

                        }

                        @Override
                        public void onNext(CheckResult checkResult) {
                            // 设置负责人 当GyZrr没有值时三个责任人都没有数据
                            if (TextUtils.isEmpty(checkResult.getGyZrr())) {
                                map_bottom_sheet.findViewById(R.id.ll_name_phone).setVisibility(View.GONE);
                                map_bottom_sheet.findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                            } else {
                                map_bottom_sheet.findViewById(R.id.ll_name_phone).setVisibility(View.VISIBLE);
                                map_bottom_sheet.findViewById(R.id.tv_no_data).setVisibility(View.GONE);
                                ((TextView) map_bottom_sheet.findViewById(R.id.tv_name)).setText("管养负责人:" + checkResult.getGyZrr());
                                ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone)).setText("电话:" + checkResult.getGyZrrLxdh());
                                ((TextView) map_bottom_sheet.findViewById(R.id.tv_name2)).setText("监管负责人:" + checkResult.getJgZrr());
                                ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone2)).setText("电话:" + checkResult.getJgZrrLxdh());
                                ((TextView) map_bottom_sheet.findViewById(R.id.tv_name3)).setText("属地水务部门负责人:" + checkResult.getSwbmZrr());
                                ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone3)).setText("电话:" + checkResult.getSwbmZrrLxdh());
                                ((TextView) map_bottom_sheet.findViewById(R.id.tv_name4)).setText("技术负责人:" + checkResult.getJsZrr());
                                ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone4)).setText("电话:" + checkResult.getJsZrrLxdh());
                            }
                        }
                    });
        }
    }

    private void deleteModify(final ModifiedFacility modifiedFacility) {
        //数据纠错
        if (mIdentificationService == null) {
            mIdentificationService = new CorrectFacilityService(mContext.getApplicationContext());
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
        }
        progressDialog.setMessage("删除中.....");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        mIdentificationService.upload(modifiedFacility)
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
                        CrashReport.postCatchedException(new Exception("核准上报失败，上报用户是：" +
                                BaseInfoManager.getUserName(mContext) + "原因是：" + e.getLocalizedMessage()));
                        ToastUtil.shortToast(mContext, "删除失败");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (responseBody.getCode() == 200) {
                            initGLayer();
                            hideBottomSheet();
                            uploadJournal(modifiedFacility);
                        } else {
                            CrashReport.postCatchedException(new Exception("核准上报失败，上报用户是：" +
                                    BaseInfoManager.getUserName(mContext) + "原因是：" + responseBody.getMessage()));
                            ToastUtil.shortToast(mContext, "删除失败，原因是：" + responseBody.getMessage());
                        }
                    }
                });
    }

    private void showBottomSheetForCheck(final Component component) {
        ifInLine = false;
        if (0 == mStatus || isClick) {
            componentCenter = component;
            originPipe = createStreamBean(component);
        }
        tvResponsible.setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.tv_gjdy).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.line_gjdy).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.psdy_delete).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.psdy_line_del).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.psdy_jc).setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.psdy_line_jc).setVisibility(View.GONE);
        ll_check_hint.setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.ll_no_ysk).setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.ll_gc).setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
        map_bottom_sheet.findViewById(R.id.ll_name_phone).setVisibility(View.GONE);
        final String layerName = component.getLayerName();
        if (layerName == null || !attribute.contains(layerName)) {
            hideBottomSheet();
            return;
        }
        if (layerName.contains("排水管道")) {
            if (!layerName.contains("中心城区")) {
                checkIfHasBeenUploadBefore(component, 2);
            } else {
                checkIfHasBeenUploadForComponent(component);
                setResponsibleForHP(component);
            }
        } else if (layerName.contains("窨井") || layerName.contains("雨水口") || layerName.contains("排水口")) {
            if (!layerName.contains("中心城区")) {
                setResponsible(component, 1);
            } else {
                setResponsibleForHP(component);
            }
        }
        if (mStatus == 0 || isClick) {
            mGLayer.removeAll();
            drawGeometryForGD(component.getGraphic().getGeometry(), mGLayer, false, true);
            mFirstAttribute = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(ComponentFieldKeyConstant.ATTR_TWO), "");
            if (!mAttribute.contains(mFirstAttribute)) {
                mFirstAttribute = "";
            }

        } else if (mStatus == 1 || mStatus == 2) {
            initStreamGLayer();
            drawGeometry(component.getGraphic().getGeometry(), mStreamGLayer, false, true);
            mSecondAttribute = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(ComponentFieldKeyConstant.ATTR_TWO), "");
            if (!mAttribute.contains(mSecondAttribute)) {
                mSecondAttribute = "";
            }
        }
        Object oErrorInfo = component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ERROR_INFO);
        TextView titleTv = (TextView) map_bottom_sheet.findViewById(R.id.title);
        TextView dateTv = (TextView) map_bottom_sheet.findViewById(R.id.date);
        TextView sortTv = (TextView) map_bottom_sheet.findViewById(R.id.sort);
        TextView subtypeTv = (TextView) map_bottom_sheet.findViewById(subtype);
        TextView field1Tv = (TextView) map_bottom_sheet.findViewById(R.id.field1);
        TextView field2Tv = (TextView) map_bottom_sheet.findViewById(R.id.field2);
        TextView field3Tv = (TextView) map_bottom_sheet.findViewById(R.id.field3);

        View btn_container = map_bottom_sheet.findViewById(R.id.btn_container);//按钮控件容器
        TextView addrTv = (TextView) map_bottom_sheet.findViewById(R.id.addr);//地址
        TextView tv_gc_begin = (TextView) map_bottom_sheet.findViewById(R.id.tv_gc_begin);//起始高程
        TextView tv_gc_end = (TextView) map_bottom_sheet.findViewById(R.id.tv_gc_end);//终止高程
        tv_gc_end.setVisibility(View.VISIBLE);//终止高程
        TextView tv_ms_begin = (TextView) map_bottom_sheet.findViewById(R.id.tv_ms_begin);//起始埋深
        TextView tv_ms_end = (TextView) map_bottom_sheet.findViewById(R.id.tv_ms_end);//终点埋深
        TextView tv_gj = (TextView) map_bottom_sheet.findViewById(R.id.tv_gj);//管径
        ll_gj = map_bottom_sheet.findViewById(R.id.ll_gj);
        ll_gj.setVisibility(View.VISIBLE);
        TextView tv_cz = (TextView) map_bottom_sheet.findViewById(R.id.tv_cz);//净高
        View ll_gq = map_bottom_sheet.findViewById(R.id.ll_gq);//净高
        TextView tv_tycd = (TextView) map_bottom_sheet.findViewById(R.id.tv_tycd);//投影长度
        TextView tv_pd = (TextView) map_bottom_sheet.findViewById(R.id.tv_pd);//坡度
        View line2 = map_bottom_sheet.findViewById(R.id.line2);//分割线
        View line = map_bottom_sheet.findViewById(R.id.line);//分割线
        View line3 = map_bottom_sheet.findViewById(R.id.line3);//分割线
        View line4 = map_bottom_sheet.findViewById(R.id.line4);//分割线
        final TextView tv_zhiyi = (TextView) map_bottom_sheet.findViewById(R.id.tv_zhiyi);//质疑按钮
        View stream_line3 = map_bottom_sheet.findViewById(R.id.stream_line3);//质疑分割线
        tv_zhiyi.setVisibility(View.GONE);
        stream_line3.setVisibility(View.GONE);
//        TextView code = (TextView) map_bottom_sheet.findViewById(R.id.code);
        map_bottom_sheet.findViewById(R.id.ll_no_ysk).setVisibility(View.VISIBLE);
        //左边和右边的详情按钮
        TextView tv_detail_left = (TextView) map_bottom_sheet.findViewById(R.id.tv_detail_left);
        TextView tv_detail_right = (TextView) map_bottom_sheet.findViewById(R.id.tv_detail_right);
        View stream_line4 = map_bottom_sheet.findViewById(R.id.stream_line4);
        tv_detail_left.setVisibility(View.GONE);
        tv_detail_right.setVisibility(View.GONE);
        stream_line4.setVisibility(View.GONE);
        sortTv.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);
        field3Tv.setVisibility(View.GONE);
        map_bottom_sheet.findViewById(R.id.ll_msd).setVisibility(View.VISIBLE);//删除和查看详情的分割线
        String subtype = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.GRADE), "");
        final String DOUBT_STATUS = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.DOUBT_STATUS));
        String checkState = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.CHECK_STATE), "");
        if (layerName.contains("排水管道")) {//管道就把值加载出来
            line3.setVisibility(View.VISIBLE);
            field3Tv.setVisibility(View.VISIBLE);
            line4.setVisibility(View.VISIBLE);
            line2.setVisibility(View.GONE);
            tv_zhiyi.setVisibility(View.VISIBLE);
            stream_line3.setVisibility(View.VISIBLE);
            btn_container.setVisibility(View.VISIBLE);
            if ("-1".equals(checkState) || StringUtil.isEmpty(checkState)) {
                tv_zhiyi.setVisibility(View.VISIBLE);
                stream_line3.setVisibility(View.VISIBLE);
            }
            addrTv.setVisibility(View.GONE);
            if (layerName.contains("中心城区")) {
                btn_container.setVisibility(View.GONE);
                initValue(component, field3Tv, "管线长度：", ComponentFieldKeyConstant.LENGTH);
            } else {
                initValue(component, field3Tv, "管线长度：", ComponentFieldKeyConstant.LINE_LENGTH);
            }
            ll_gq.setVisibility(View.GONE);
            tv_cz.setVisibility(View.GONE);
            subtype = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.GRADE), "");
            initValue(component, tv_gc_begin, "起始管底高程：", ComponentFieldKeyConstant.BEG_H);
            initValue(component, tv_gc_end, "终止管底高程：", ComponentFieldKeyConstant.END_H);
            initValue(component, tv_ms_begin, "起点管底埋深：", ComponentFieldKeyConstant.BEGCEN_DEE);
            initValue(component, tv_ms_end, "终点管底埋深：", ComponentFieldKeyConstant.ENDCEN_DEE);
            initValue(component, tv_gj, "管径：", ComponentFieldKeyConstant.PIPE_GJ, 0, "");

        } else if (layerName.contains("排水沟渠")) {
            if ("-1".equals(checkState) || StringUtil.isEmpty(checkState)) {
                tv_zhiyi.setVisibility(View.VISIBLE);
                stream_line3.setVisibility(View.VISIBLE);
            }
            line2.setVisibility(View.GONE);
            line3.setVisibility(View.VISIBLE);
            line4.setVisibility(View.VISIBLE);
            ll_gq.setVisibility(View.VISIBLE);
            btn_container.setVisibility(View.GONE);
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
        } else if (layerName.contains("窨井")) {
            line2.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
            ll_gq.setVisibility(View.GONE);
            tv_gc_end.setVisibility(View.GONE);
            field3Tv.setVisibility(View.GONE);
            tv_gc_end.setText("");
            btn_container.setVisibility(View.VISIBLE);
            subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
            if (StringUtil.isEmpty(subtype) || "null".equals(subtype)) {
                subtype = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE));
            }
            if (layerName.contains("中心城区")) {
                addrTv.setVisibility(View.VISIBLE);
                tv_cz.setVisibility(View.GONE);
                tv_ms_end.setText("");
                initValue(component, tv_gj, "井盖大小：", ComponentFieldKeyConstant.COVER_SIZE, 2, "mm");
                initValue(component, tv_gc_begin, "地面高程：", ComponentFieldKeyConstant.SUR_H);
                initValue(component, tv_ms_begin, "井底高程：", ComponentFieldKeyConstant.BOTTOM_H);
//                tv_detail_left.setVisibility(View.GONE);
//                tv_detail_right.setVisibility(View.VISIBLE);
            } else {
//                tv_detail_left.setVisibility(View.VISIBLE);
//                stream_line4.setVisibility(View.VISIBLE);
//                tv_detail_right.setVisibility(View.GONE);
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

        } else if (layerName.contains("排水口")) {
//            tv_detail_left.setVisibility(View.VISIBLE);
//            tv_detail_right.setVisibility(View.GONE);
            ll_gj.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
            ll_gq.setVisibility(View.GONE);
            sortTv.setVisibility(View.GONE);
            tv_gc_end.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            btn_container.setVisibility(View.VISIBLE);
            addrTv.setVisibility(View.VISIBLE);
            tv_cz.setVisibility(View.GONE);
            tv_ms_end.setText("");
            tv_gj.setText("");
//            subtypeTv.setText("河涌名称：" + component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));

            if (layerName.contains("中心城区")) {
                btn_container.setVisibility(View.GONE);
                initValue(component, tv_gc_begin, "排水口岸别：", ComponentFieldKeyConstant.ATTR_FOUR, 0, "");
                initValue(component, subtypeTv, "河涌名称：", ComponentFieldKeyConstant.RIVERNAME, 0, "");
                initValue(component, tv_ms_begin, "所在道路：", ComponentFieldKeyConstant.LANE_WAY, 0, "");
                initValue(component, addrTv, "设施位置：", ComponentFieldKeyConstant.ADDR, 0, "");
            } else {
                btn_container.setVisibility(View.VISIBLE);
                initValue(component, tv_gc_begin, "排水口岸别：", ComponentFieldKeyConstant.ATTR_FOUR, 0, "");
                initValue(component, subtypeTv, "河涌名称：", ComponentFieldKeyConstant.ATTR_ONE, 0, "");
                initValue(component, tv_ms_begin, "所在道路：", ComponentFieldKeyConstant.ROAD, 0, "");
                initValue(component, addrTv, "设施位置：", ComponentFieldKeyConstant.ADDR, 0, "");
            }
        } else if (layerName.contains("雨水口")) {
//            tv_detail_left.setVisibility(View.VISIBLE);
//            tv_detail_right.setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.ll_no_ysk).setVisibility(View.GONE);
            if (layerName.contains("中心城区")) {
                btn_container.setVisibility(View.GONE);
            } else {
                btn_container.setVisibility(View.VISIBLE);
            }
        } else {
            line2.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
            addrTv.setVisibility(View.GONE);
            ll_gq.setVisibility(View.GONE);
            btn_container.setVisibility(View.VISIBLE);
            tv_gc_begin.setText("");
            tv_gc_end.setText("");
            tv_ms_begin.setText("");
            tv_ms_end.setText("");
            tv_gj.setText("");
        }
        if ("-1".equals(checkState)) {
            tv_zhiyi.setText("取消存疑");
        } else {
            tv_zhiyi.setText(" 存疑 ");
        }
        final String finalLayerName = layerName;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, JournalsDetailListActivity.class);
                intent.putExtra("component", component);
                startActivityForResult(intent, 123);
                if (StringUtil.isEmpty(component.getLayerName())) {
                    if (!StringUtil.isEmpty(finalLayerName)) {
                        component.setLayerName(finalLayerName);
                    }
                }
            }
        };

        tv_detail_left.setOnClickListener(listener);
        tv_detail_right.setOnClickListener(listener);

        TextView tv_errorinfo = (TextView) map_bottom_sheet.findViewById(R.id.tv_errorinfo);
        String type = component.getLayerName();

//        String usid = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.USID));
        final String objectId = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.OBJECTID));
//        if (StringUtil.isEmpty(usid) || "null".equals(usid)) {
//            usid = objectId;
//        }
        String title = StringUtil.getNotNullString(getLayerName(type), "");
        if (layerName.contains("排水口")) {
            title = StringUtil.getNotNullString(getLayerName(type), "") + "(" + objectId + ")";
        }
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
            if (StringUtil.isEmpty(sort) || "null".equals(sort)) {
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


        /**
         * 如果是雨水口，显示特性：方形
         */
        if (layerName.contains("雨水口")) {
            String feature = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE));
            sortTv.setText(StringUtil.getNotNullString(feature, ""));
        }
        if (layerName.contains("雨水口")) {
            String style = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE));
            subtypeTv.setText(StringUtil.getNotNullString(style, ""));
            subtypeTv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.dust_grey, null));
        } else if (!"排水口(中心城区)".equals(type) && !"排水口".equals(layerName)) {
            subtypeTv.setText(StringUtil.getNotNullString(subtype, ""));
        }

        //已挂牌编号
        String codeValue = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FIVE));
        if (!StringUtil.isEmpty(codeValue)) {
            codeValue = codeValue.trim();
        }
        /**
         * 修改属性三
         */
        String field3 = "";
        if (layerName.contains("窨井")) {
            String cz = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE), "");
            if (StringUtil.isEmpty(cz)) {
                field3 = "井盖材质: " + StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.COVER_MATERIAL), "");
            }
        } else if (layerName.contains("排水口")) {
            field3 = "排放去向: " + String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.RIVER));
            field3Tv.setText(field3);
        } else if (layerName.contains("排水管道")) {
            field3 = "管道材质: " + StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.MATERIAL1), "");
        } else if (layerName.contains("排水沟渠")) {
            field3 = "沟渠类型: " + StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE), "");
            field3Tv.setText(field3);
        }
//        field3Tv.setVisibility(View.GONE);
//        tv_parent_org_name.setVisibility(View.GONE);
        final String address = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ADDR));
        addrTv.setText("所在道路" + "：" + StringUtil.getNotNullString(address, ""));

        /*
        if (errorInfo == null) {
            tv_errorinfo.setVisibility(View.GONE);
        } else {
            tv_errorinfo.setVisibility(View.VISIBLE);
            tv_errorinfo.setText(errorInfo + "?");
        }
        */
        tv_errorinfo.setVisibility(View.GONE);


//删除按钮
        final String dataType = StringUtil.getNotNullString(component.getGraphic().getAttributes().get("DATA_TYPE"), "");
        map_bottom_sheet.findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("排水管道".equals(layerName)) {
                    if ("3".equals(dataType)) {
                        ToastUtil.shortToast(mContext, "该管线已删除！");
                        return;
                    }
                    DialogUtil.MessageBox(mContext, "提示", "是否确定要删除这条记录？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentStream = createStreamBean(component);
                            deletePipe();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        /**
         * 上下游按钮
         */
        map_bottom_sheet.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (1 == mStatus) {
                    locationMarker.setVisibility(View.VISIBLE);
                    initStreamGLayer();
                    hideBottomSheet();
                } else {
                    locationMarker.setVisibility(View.VISIBLE);
                    initGLayer();
                    hideBottomSheet();
                }
            }
        });


        /**
         * 纠错按钮
         */
        ((TextView) map_bottom_sheet.findViewById(R.id.tv_error_correct)).setText("取消");
        final String finalTitle = title;
        map_bottom_sheet.findViewById(R.id.tv_error_correct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDrawStream = false;
                if ("排水管道".equals(layerName)) {
                    if ("3".equals(dataType)) {
                        ToastUtil.shortToast(mContext, "该管线已删除！");
                        return;
                    }
                    isDrawStream = true;
                    mStatus = 1;
                    currentStream = createStreamBean(component);
                    mAllPipeBeans.clear();
                    mAllPipeBeans.add(currentStream);
                    drawStream(componentCenter, mAllPipeBeans, getResources().getColor(R.color.agmobile_red));
                    showPopWindow(component, layerName, false);
                } else if (mStatus == 0) {
                    mStatus = 1;
                    setMode(mStatus);
                    ToastUtil.shortToast(mContext, "请移动地图选择关联井");
                    locationMarker.changeIcon(R.mipmap.ic_check_stream);
                    if (locationMarker != null) {
                        locationMarker.changeIcon(R.mipmap.ic_check_related);
                        locationMarker.setVisibility(View.VISIBLE);
                    }
                    tv_query_tip.setText("请移动地图选择关联井");
                    initStreamGLayer();
                    hideBottomSheet();
                    setSearchFacilityListener();
                } else if (mStatus == 1) {
                    isDrawStream = true;
                    currentStream = createStreamBean(component);
//                    showDialog(component, layerName);
                    if (!StringUtil.isEmpty(originPipe.getObjectId()) && originPipe.getObjectId().equals(objectId)) {
                        ToastUtil.longToast(mContext, "管线起点和终点不能为同一管井！");
                        return;
                    }
                    showPopWindow(component, layerName, true);
                } else {
                    mStatus = 2;
                    setMode(mStatus);
                    if (mPipeStreamView == null) {
                        mPipeStreamView = new PipeStreamView(mMapView, mReviewContainter, mContext);
                        mPipeStreamView.setOnAddStreamListener(new PipeStreamView.onAddStreamListener() {
                            @Override
                            public void onSelectStream(boolean isUpOrDownStream) {
                                UpOrDownStream = isUpOrDownStream;
                                setMode(mStatus);
                                ToastUtil.shortToast(mContext, "请选择上下游窨井");
                                if (locationMarker != null) {
                                    locationMarker.changeIcon(R.mipmap.ic_check_stream);
                                    locationMarker.setVisibility(View.VISIBLE);
                                }
                                hideBottomSheet();
                                initStreamGLayer();
                                showAttributeForStream(false);
                                setSearchFacilityListener();
                            }

                            @Override
                            public void onDeleteStream(List<PipeBean> upStreams) {
                                mAllPipeBeans.clear();
                                mAllPipeBeans.addAll(upStreams);
                                drawStream(componentCenter, mAllPipeBeans, getResources().getColor(R.color.agmobile_red));
                            }
                        });
                    }
                    mPipeStreamView.setTitle(finalTitle);
                    showAttributeForStream(true);
                    if (btn_check_cancel.getVisibility() == View.VISIBLE) {
                        btn_check_cancel.performClick();
                    }
                    hideBottomSheet();
                }
            }
        });

        ///质疑的提交
        tv_zhiyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cunyi = tv_zhiyi.getText().toString().trim();
                if (cunyi.equals("取消存疑")) {
                    DialogUtil.MessageBox(mContext, "提示", "确定取消该设施的存疑状态？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String id = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.DOUBT_ID), "");
                            cancelDoubt(id, tv_zhiyi);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                } else {
                    showDoubtDialog(component, component.getGraphic().getGeometry(), tv_zhiyi);
                }
            }
        });
        if (!userCanEdit) {
            btn_container.setVisibility(View.GONE);
        } else if (layerName.contains("排水管道")) {
            map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.VISIBLE);
            map_bottom_sheet.findViewById(R.id.tv_delete).setVisibility(View.VISIBLE);
            map_bottom_sheet.findViewById(R.id.stream_line).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.tv_sure).setVisibility(View.GONE);
            ((TextView) map_bottom_sheet.findViewById(R.id.tv_error_correct)).setText("确认完善属性");
            if ("排水管道(中心城区)".equals(layerName) || "-1".equals(checkState)) {
                map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.GONE);
                map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.GONE);
            } else {
                map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.VISIBLE);
                map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.VISIBLE);
            }
        } else if (mStatus == 0) {
            map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.tv_delete).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.stream_line).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.stream_line2).setVisibility(View.VISIBLE);
            ((TextView) map_bottom_sheet.findViewById(R.id.tv_sure)).setText("   取消   ");
            map_bottom_sheet.findViewById(R.id.tv_sure).setVisibility(View.VISIBLE);
            map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.VISIBLE);
            ((TextView) map_bottom_sheet.findViewById(R.id.tv_error_correct)).setText("选择关联井");
        } else if (mStatus == 1 || mStatus == 2) {
            map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.tv_delete).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.stream_line).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.stream_line2).setVisibility(View.VISIBLE);
            ((TextView) map_bottom_sheet.findViewById(R.id.tv_sure)).setText("   取消   ");
            map_bottom_sheet.findViewById(R.id.tv_sure).setVisibility(View.VISIBLE);
            map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.VISIBLE);
            ((TextView) map_bottom_sheet.findViewById(R.id.tv_error_correct)).setText("设置管线信息");
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                map_bottom_sheet.setVisibility(View.VISIBLE);
                mBehavior.setState(AnchorSheetBehavior.STATE_EXPANDED);
                resetInfoLayout();
            }
        }, 300);
    }

    private void initValue(Component component, TextView tv_gc_begin, String name, String key) {
        if (tv_gc_begin != null) {
            tv_gc_begin.setVisibility(View.VISIBLE);
        }
        String value = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(key), "");
        if (StringUtil.isEmpty(value)) {
            tv_gc_begin.setText(name);
            return;
        }
        String format = name + formatToSecond(value, "m", 2);
        tv_gc_begin.setText(format);
    }

    private void initValue(Component component, TextView tv_gc_begin, String name, String key, int newScale, String unit) {
        if (tv_gc_begin != null) {
            tv_gc_begin.setVisibility(View.VISIBLE);
        }
        String value = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(key), "");
        String gjName = "";
        if (!StringUtil.isEmpty(value) && ComponentFieldKeyConstant.PIPE_GJ.equals(key)) {
            TableDBService dbService = new TableDBService(mContext);
            List<DictionaryItem> a205 = dbService.getDictionaryByTypecodeInDB("A205");
            for (DictionaryItem dictionaryItem : a205) {
                if (dictionaryItem.getCode().equals(value)) {
                    gjName = dictionaryItem.getName();
                }
            }
        }
        if (StringUtil.isEmpty(value)) {
            tv_gc_begin.setText(name);
            return;
        }
        if (ComponentFieldKeyConstant.PIPE_GJ.equals(key)) {
            tv_gc_begin.setText(name + gjName + unit);
            return;
        } else if (newScale == 0) {
            tv_gc_begin.setText(name + value + unit);
            return;
        }
        String format = name + formatToSecond(value, unit, newScale);
        tv_gc_begin.setText(format);
    }

    private void showDialog(final ViewGroup dialog1, final PipeBean currentStream) {
        String message = "";
        if (!StringUtil.isEmpty(mFirstAttribute) && mFirstAttribute.equals(mSecondAttribute)) {
            upload(dialog1, currentStream);
            return;
        } else if (!StringUtil.isEmpty(mFirstAttribute)) {
            String name = StringUtil.isEmpty(mSecondAttribute) ? "空" : mSecondAttribute;
            message = "起点为" + mFirstAttribute + "窨井，连线雨污类别为" + name + "，确定连线吗？";
        } else {
            message = "起点窨井雨污类别为空，确定连线吗？";
        }
        DialogUtil.MessageBox(mContext, "提示", message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                upload(dialog1, currentStream);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    private void deletePipe() {
        pd = new ProgressDialog(getContext());
        pd.setMessage("删除中.....");
        pd.show();
        String dataJson = JsonUtil.getJson(currentStream);
        mUploadFacilityService.deletePipe(dataJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result2<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        ToastUtil.iconShortToast(mContext, R.mipmap.ic_alert_yellow, "删除失败，请稍后重试");
                    }

                    @Override
                    public void onNext(Result2<String> s) {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        if (s.getCode() == 200) {
                            refreshMap();
                            if (mStatus == 1) {
                                initStreamGLayer();
                            } else if (mStatus == 0) {
                                initGLayer();
                            }
                            hideBottomSheet();
                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new RefreshMyModificationListEvent());
                        } else {
//                            CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
//                                    BaseInfoManager.getUserName(mContext) + "原因是：" + s.getMessage()));
                            Toast.makeText(mContext, s.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void showAttributeForStream(boolean b) {
//        isShow = b;
//        if (b) {
//            mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
//            dis_map_bottom_sheet.setVisibility(View.VISIBLE);
//            if (mDisBehavior.getState() != AnchorSheetBehavior.STATE_ANCHOR) {
//                mDisBehavior.setState(AnchorSheetBehavior.STATE_EXPANDED);
//            }
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            }, 200);
//        } else {
//            dis_map_bottom_sheet.setVisibility(View.GONE);
//        }
    }

    private void showDoubtDialog(final Component component, final Geometry geometry, final TextView tv_zhiyi) {
        View view = View.inflate(mContext, R.layout.activity_dialog_view, null);
        final TextFieldTableItem fieldTableItem = (TextFieldTableItem) view.findViewById(R.id.textitem_description);
        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("请填写存疑原因")
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (StringUtil.isEmpty(fieldTableItem.getText().trim())) {
                            ToastUtil.shortToast(mContext, "请填写存疑原因");
                            return;
                        }
                        DoubtBean doubtBean = createDoubt(component, geometry, fieldTableItem.getText().trim());
                        uploadDoubt(component, doubtBean, dialog, tv_zhiyi);
                    }
                });
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 将component转换成DoubtBean实体
     *
     * @param component
     * @param geometry
     * @param description
     * @return
     */
    private DoubtBean createDoubt(Component component, Geometry geometry, String description) {
        DoubtBean doubtBean = new DoubtBean();
        if (component != null) {
            String layerName = component.getLayerName();
            if (layerName.contains("排水管道")) {
                doubtBean.setDoubtType(2);
            } else {
                doubtBean.setDoubtType(1);
            }
            doubtBean.setLayerName(getLayerName(layerName));
            String objectId = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.OBJECTID));
            doubtBean.setObjectId(objectId);
        } else {
            doubtBean.setDoubtType(3);
            doubtBean.setLayerName("存疑区域");
            doubtBean.setRings(createRingsForGeometry(geometry));
        }
        doubtBean.setDescription(description);
        return doubtBean;
    }

    private String createRingsForGeometry(Geometry geometry) {
        String rings = "";
        if (geometry instanceof Polygon) {
            Polygon polygon = (Polygon) geometry;
            int count = polygon.getPointCount();
            for (int i = 0; i < count; i++) {
                Point point = polygon.getPoint(i);
                rings += ";" + point.getX() + "," + point.getY();
            }
        }
        if (!StringUtil.isEmpty(rings)) {
            rings = rings.substring(1);
        }
        return rings;
    }

    /**
     * 检测是否有同区的人已经上报过(校核过)
     *
     * @param component
     */
    private void checkIfHasBeenUploadBefore(Component component, int type) {
        if(component.getLayerName().contains("中心城区")){
            Map<String, Object> attrs = component.getGraphic().getAttributes();
            if(attrs != null && !TextUtils.isEmpty(attrs.get(ComponentFieldKeyConstant.SUPERNAME).toString())){
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_name)).setText("管养负责人:" + attrs.get(ComponentFieldKeyConstant.MAINTNAME));
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone)).setText("电话:" + attrs.get(ComponentFieldKeyConstant.MAINTPHONE));
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_name2)).setText("监管负责人:" + attrs.get(ComponentFieldKeyConstant.SUPERNAME));
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone2)).setText("电话:" + attrs.get(ComponentFieldKeyConstant.SUPERPHONE));
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_name3)).setText("属地水务部门负责人:" + attrs.get(ComponentFieldKeyConstant.SWNAME));
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone3)).setText("电话:" + attrs.get(ComponentFieldKeyConstant.SWPHONE));
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_name4)).setText("技术负责人:" + attrs.get(ComponentFieldKeyConstant.TECHNICNAME));
                ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone4)).setText("电话:" + attrs.get(ComponentFieldKeyConstant.TECHNICPHONE));
            }
        } else if (component.getGraphic() != null && component.getGraphic().getAttributes() != null
                && component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.OBJECTID) != null) {
            //通过usid去请求接口是否已经有同区的人报过
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
                            //查询失败，允许上报，但是在上报前要再查询一次是否有人校核过
                            //  map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.VISIBLE);
                            tv_check_person.setText("");
                            ll_check_hint.setVisibility(View.GONE);
                        }

                        @SuppressLint("WrongViewCast")
                        @Override
                        public void onNext(CheckResult stringResult2) {
                            if (stringResult2 == null || stringResult2.getCode() != 200) {
                                //说明没有人上报过，显示“校核”按钮
                                //  map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.VISIBLE);
                                ll_check_hint.setVisibility(View.GONE);
                                status = "2";
                                person = "";
                            } else {
                                String checkPersonName = stringResult2.getMarkPerson();
                                if (BaseInfoManager.getUserName(mContext).equals(checkPersonName)) {
                                    checkPersonName = "您";
                                }
                                ll_check_hint.setVisibility(View.VISIBLE);
                                tv_check_person.setVisibility(View.VISIBLE);
                                String date = TimeUtil.getStringTimeYMD(new Date(stringResult2.getMarkTime()));
                                tv_check_person.setText(checkPersonName + "已经在" + date);
                                person = checkPersonName;
                                tv_check_phone.setVisibility(View.VISIBLE);
                                String handle = "";
                                if ("0".equals(stringResult2.getHandle())) {
                                    status = "0";
                                    handle = "校核";
                                } else if ("1".equals(stringResult2.getHandle())) {
                                    status = "1";
                                    handle = "新增";
                                } else if ("3".equals(stringResult2.getHandle())) {
                                    status = "3";
                                    handle = "删除";
                                    ((TextView) map_bottom_sheet.findViewById(R.id.tv_error_correct)).setVisibility(View.GONE);
                                    map_bottom_sheet.findViewById(R.id.tv_delete).setVisibility(View.GONE);
                                    map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.GONE);
                                    map_bottom_sheet.findViewById(R.id.tv_delete).setVisibility(View.GONE);
                                    map_bottom_sheet.findViewById(R.id.stream_line).setVisibility(View.GONE);
                                    map_bottom_sheet.findViewById(R.id.stream_line2).setVisibility(View.GONE);
                                }
                                tv_check_phone.setText(handle);
                                //已经校核过该设施，该设施共被校核" + checkPerson.getTotal() + "次"
                                tv_check_hint.setText("过该设施");

                                // 设置负责人
                                if (TextUtils.isEmpty(stringResult2.getGyZrr())) {
                                    map_bottom_sheet.findViewById(R.id.ll_name_phone).setVisibility(View.GONE);
                                    map_bottom_sheet.findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                                } else {
                                    map_bottom_sheet.findViewById(R.id.ll_name_phone).setVisibility(View.VISIBLE);
                                    map_bottom_sheet.findViewById(R.id.tv_no_data).setVisibility(View.GONE);
                                    ((TextView) map_bottom_sheet.findViewById(R.id.tv_name)).setText("管养负责人:" + stringResult2.getGyZrr());
                                    ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone)).setText("电话:" + stringResult2.getGyZrrLxdh());
                                    ((TextView) map_bottom_sheet.findViewById(R.id.tv_name2)).setText("监管负责人:" + stringResult2.getJgZrr());
                                    ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone2)).setText("电话:" + stringResult2.getJgZrrLxdh());
                                    ((TextView) map_bottom_sheet.findViewById(R.id.tv_name3)).setText("属地水务部门负责人:" + stringResult2.getSwbmZrr());
                                    ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone3)).setText("电话:" + stringResult2.getSwbmZrrLxdh());
                                    ((TextView) map_bottom_sheet.findViewById(R.id.tv_name4)).setText("技术负责人:" + stringResult2.getJsZrr());
                                    ((TextView) map_bottom_sheet.findViewById(R.id.tv_phone4)).setText("电话:" + stringResult2.getJsZrrLxdh());
                                }
                            }
                        }
                    });
        }
    }

    /**
     * 检测是否有同区的人已经上报过(校核过)
     *
     * @param component
     */
    private void checkIfHasBeenUploadForComponent(Component component) {
        if (component.getGraphic() != null && component.getGraphic().getAttributes() != null
                && component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.OBJECTID) != null) {
            String checkPersonName = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(ComponentFieldKeyConstant.OPERATER), "");
            String time = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(ComponentFieldKeyConstant.SYNCTIME), "");
            if (StringUtil.isEmpty(checkPersonName) || StringUtil.isEmpty(time)) {
                //说明没有人上报过，显示“校核”按钮
                //  map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.VISIBLE);
                ll_check_hint.setVisibility(View.GONE);
                status = "2";
                person = "";
            } else {
                if (BaseInfoManager.getUserName(mContext).equals(checkPersonName)) {
                    checkPersonName = "您";
                }
                ll_check_hint.setVisibility(View.VISIBLE);
                tv_check_person.setVisibility(View.VISIBLE);
                String date = TimeUtil.getStringTimeYMD(new Date(Long.valueOf(time)));
                tv_check_person.setText(checkPersonName + "已经在" + date);
                person = checkPersonName;
                tv_check_phone.setVisibility(View.VISIBLE);
                String handle = "更新";
                tv_check_phone.setText(handle);
                tv_check_hint.setText("过该设施");
            }
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
         * 退出时停止定位
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
            addModeSelectLocationTouchListener.setShowPoint(true);
        }

        if (addModeCalloutSureButtonClickListener == null) {
            addModeCalloutSureButtonClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //隐藏callout
                    if (mMapView.getCallout().isShowing()) {
                        mMapView.getCallout().animatedHide();
                    }

                    //恢复点击事件
                    mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
                    //隐藏marker
                    locationMarker.setVisibility(View.GONE);

                    LocationInfo locationInfo = addModeSelectLocationTouchListener.getLoationInfo();
                    if (locationInfo.getDetailAddress() == null || locationInfo.getPoint() == null) {
                        ToastUtil.shortToast(mContext, "请重新选择位置");
                        return;
                    }

                    btn_add.setVisibility(View.VISIBLE);
                    btn_cancel.setVisibility(View.GONE);
                    mMapView.setOnTouchListener(getDefaultMapOnTouchListener());

                    Intent intent = new Intent(mContext, UploadNewFacilityActivity.class);
                    intent.putExtra("detailAddress", locationInfo.getDetailAddress());
                    intent.putExtra("x", locationInfo.getPoint().getX());
                    intent.putExtra("y", locationInfo.getPoint().getY());
                    startActivity(intent);
                }
            };
        }

        /**
         * 气泡中“确定”按钮点击事件
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
                    if (isLoading) {
                        whileTake = false;
                    }
                    if (isDrawStream) {
                        return true;
                    }
                    locationMarker.setVisibility(View.VISIBLE);
                    if (mStatus == 1) {
                        initStreamGLayer();
                    } else if (mStatus == 0) {
                        initGLayer();
                        initSelectGLayer();
                        initGraphicSelectGLayer();
                    } else if (mStatus == 5 || mStatus == 7) {
                        initSelectGLayer();
                    }
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
                        isClick = false;
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

    private void showPopWindow(final Component component, final String layerName, boolean isLine) {
        String pipeType = "";
        String direction = "";
        String pipeGJ = "";
        String gjName = "";
        String length = "";
        boolean isFacilityOfRedLine = false;
        TableDBService dbService = new TableDBService(mContext);
        List<DictionaryItem> a163 = dbService.getDictionaryByTypecodeInDB("A163");
        if (component != null && component.getGraphic() != null) {
            pipeType = StringUtil.getNotNullString(component.getGraphic().getAttributeValue("SORT"), "");
            direction = StringUtil.getNotNullString(component.getGraphic().getAttributeValue("DIRECTION"), "");
            pipeGJ = StringUtil.getNotNullString(component.getGraphic().getAttributeValue("PIPE_GJ"), "");
            length = StringUtil.getNotNullString(component.getGraphic().getAttributeValue("LINE_LENGTH"), "");
            isFacilityOfRedLine = "是".equals(StringUtil.getNotNullString(component.getGraphic().getAttributeValue("sfpsdyhxn".toUpperCase()), "否"));
        }
        final Map<String, Object> spinnerData = new LinkedHashMap<>();
        List<String> allowValues = new ArrayList<>();
        for (DictionaryItem dictionaryItem : a163) {
            allowValues.add(dictionaryItem.getName());
            spinnerData.put(dictionaryItem.getName(), dictionaryItem);
        }
        final Map<String, Object> spinnerData1 = new LinkedHashMap<>();
        spinnerData1.put("正向", "正向");
        spinnerData1.put("反向", "反向");
        List<DictionaryItem> a205 = dbService.getDictionaryByTypecodeInDB("A205");
        Collections.sort(a205, new Comparator<DictionaryItem>() {
            @Override
            public int compare(DictionaryItem o1, DictionaryItem o2) {
                String code = o1.getCode();
                String target = code;
                if (code.length() > 1) {
                    target = code.replaceAll("[^(0-9)]", "");//去掉所有字母
                }
                String code2 = o2.getCode();
                String target2 = code;
                if (code2.length() > 1) {
                    target2 = code2.replaceAll("[^(0-9)]", "");//去掉所有字母
                }
                int num1 = Integer.valueOf(target);
                int num2 = Integer.valueOf(target2);
                int result = 0;
                if (num1 > num2) {
                    result = 1;
                }
                if (num1 < num2) {
                    result = -1;
                }
                return result;
            }
        });

        final Map<String, Object> gjData = new LinkedHashMap<>();
        List<String> gjValues = new ArrayList<>();
        for (DictionaryItem dictionaryItem : a205) {
            if (dictionaryItem.getCode().equals(pipeGJ)) {
                gjName = dictionaryItem.getName();
            }
            gjValues.add(dictionaryItem.getName());
            gjData.put(dictionaryItem.getName(), dictionaryItem);
        }
        btn_reedit = (Button) mView.findViewById(R.id.btn_save2);
        sp_gxcd = (NumberItemTableItem) mView.findViewById(R.id.sp_gxcd);
        if (layerName.contains("排水管道") && StringUtil.isEmpty(length)) {
            mLineLength = getLineLength(originPipe, true);
        } else if (!StringUtil.isEmpty(length)) {
            mLineLength = Double.valueOf(length);
        } else {
            mLineLength = getLineLength(originPipe, currentStream);
        }
        if (mLineLength != -1) {
            sp_gxcd.setText(formatToSecond(mLineLength + "", "", 2));
        } else {
            sp_gxcd.setText("");
        }
        btn_cancel2 = (Button) mView.findViewById(R.id.btn_cancel2);
        sp_ywlx = (SpinnerTableItem) mView.findViewById(R.id.sp_ywlx);
        sp_ywlx.setSpinnerData(spinnerData);
        if (!StringUtil.isEmpty(pipeType)) {
            sp_ywlx.selectItem(pipeType);
        }
        if (isLine && !StringUtil.isEmpty(mFirstAttribute)) {
            sp_ywlx.selectItem(mFirstAttribute);
        }
        sp_lx = (SpinnerTableItem) mView.findViewById(R.id.sp_lx);
        sp_lx.setSpinnerData(spinnerData1);
        sp_lx.setOnSpinnerChangeListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                if (mStatus == 1 || mStatus == 2) {
                    if (position == 0) {
                        currentStream.setDirection("正向");
                    } else {
                        currentStream.setDirection("反向");
                    }
                    mTempPipeBeans.clear();
                    mTempPipeBeans.add(currentStream);
                    drawStream(componentCenter, mTempPipeBeans, getResources().getColor(R.color.agmobile_red));
                }
            }
        });

        sp_gj = (SpinnerTableItem) mView.findViewById(R.id.sp_gj);
        sp_gj.setOnSpinnerChangeListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                DictionaryItem dictionaryItem = (DictionaryItem) item;
                mGjCode = dictionaryItem.getCode();
            }
        });
        sp_gj.setSpinnerData(gjData);
        if (!StringUtil.isEmpty(direction)) {
            sp_lx.selectItem(direction);
        } else {
            sp_lx.selectItem(0);
        }
        if (!StringUtil.isEmpty(gjName)) {
            sp_gj.selectItem(gjName);
        } else {
            sp_gj.selectItem(0);
        }
        btn_reedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpOrDownStream = false;
                if (component != null) {
                    if (mStatus == 1) {
                        currentStream.setPipeType(sp_ywlx.getText());
                        currentStream.setDirection(sp_lx.getText());
                        currentStream.setPipeGj(mGjCode);
                        List<PipeBean> children = new ArrayList<>();
                        currentStream.setSfpsdyhxn(cb_isFacilityOfRedLine.isCheck() ? "是" : "否");
                        if (StringUtil.isEmpty(sp_gxcd.getText().trim())) {
                            ToastUtil.shortToast(mContext, "管线长度不能为空");
                            return;
                        }
                        mLineLength = Double.valueOf(sp_gxcd.getText().trim());
                        //提交
                        if ("排水管道".equals(layerName)) {//修改排水管道属性
                            currentStream.setPipeType(sp_ywlx.getText());
                            currentStream.setDirection(sp_lx.getText());
                            currentStream.setPipeGj(mGjCode);
                            currentStream.setChildren(children);
                            updateLinePipe(pipe_view, currentStream);
                        } else {
                            //新增排水管道
                            children.add(currentStream);
                            originPipe.setChildren(children);
                            mSecondAttribute = sp_ywlx.getText();
                            showDialog(pipe_view, originPipe);
                        }
                        return;
                    } else if (mStatus == 2) {
                        currentStream.setPipeType(sp_ywlx.getText());
                        currentStream.setDirection(sp_lx.getText());
                        currentStream.setPipeGj(mGjCode);
                        currentStream.setSfpsdyhxn(cb_isFacilityOfRedLine.isCheck() ? "是" : "否");
                        mAllPipeBeans.add(currentStream);
                        mPipeStreamView.addData(mAllPipeBeans);
                        setMode(mStatus);
                        showAttributeForStream(true);
                    }
                    initArrowGLayer();
                    hideBottomSheet();
                    drawStream(componentCenter, mAllPipeBeans, getResources().getColor(R.color.agmobile_red));
                }
            }
        });
        btn_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initArrowGLayer();
                isDrawStream = false;
                pipe_view.setVisibility(View.GONE);
                if ("排水管道".equals(layerName)) {
                    mStatus = 0;
//                    initGLayer();
                }
            }
        });
        cb_isFacilityOfRedLine = mView.findViewById(R.id.cb_isFacilityOfRedLine);
        if (cb_isFacilityOfRedLine != null) {
            cb_isFacilityOfRedLine.setCheck(isFacilityOfRedLine);
        }
        pipe_view.setVisibility(View.VISIBLE);
    }

    /**
     * 新增排水管道
     *
     * @param dialog
     * @param currentStream
     */
    private void upload(final ViewGroup dialog, PipeBean currentStream) {
        pd = new ProgressDialog(getContext());
        pd.setMessage("正在提交...");
        pd.show();

        currentStream.setLineLength(mLineLength);
        String dataJson = JsonUtil.getJson(currentStream);
        mUploadFacilityService.addLinePipe(dataJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        mLineLength = -1;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
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
                            ToastUtil.shortToast(mContext, "提交成功");
                            refreshMap();
//                            mStatus = 0;
//                            //提交文件
//                            setMode(mStatus);
//                            mAllPipeBeans.clear();
//                            initArrowGLayer();
                            locationMarker.setVisibility(View.VISIBLE);
                            isDrawStream = false;
                            tv_query_tip.setText("继续关联窨井或取消");
                            initStreamGLayer();
                            initArrowGLayer();
                            hideBottomSheet();
                            dialog.setVisibility(View.GONE);
                        } else {
                            ToastUtil.shortToast(mContext, "保存失败，原因是：" + responseBody.getMessage());
                        }
                    }
                });
    }

    private double getLineLength(PipeBean currentStream, boolean isUpdate) {
        Polyline mLine = new Polyline();
        if (currentStream != null) {
            mLine.startPath(new Point(currentStream.getX(), currentStream.getY()));
            if (isUpdate) {
                mLine.lineTo(currentStream.getEndX(), currentStream.getEndY());
                return GeometryEngine.geodesicLength(mLine, mMapView.getSpatialReference(), new LinearUnit(LinearUnit.Code.METER));
            }
            List<PipeBean> pipeBeans = currentStream.getChildren();
            if (!ListUtil.isEmpty(pipeBeans)) {
                PipeBean pipeBean = pipeBeans.get(0);
                mLine.lineTo(pipeBean.getX(), pipeBean.getY());
                return GeometryEngine.geodesicLength(mLine, mMapView.getSpatialReference(), new LinearUnit(LinearUnit.Code.METER));
            } else {
                return 0;
            }
        }
        return 0;
    }

    private double getLineLength(PipeBean startStream, PipeBean endStream) {
        Polyline mLine = new Polyline();
        if (startStream != null && endStream != null) {
            mLine.startPath(new Point(startStream.getX(), startStream.getY()));
            mLine.lineTo(endStream.getX(), endStream.getY());
            return GeometryEngine.geodesicLength(mLine, mMapView.getSpatialReference(), new LinearUnit(LinearUnit.Code.METER));
        } else {
            return 0;
        }
    }

    /**
     * 新增排水管道
     *
     * @param dialog
     * @param currentStream
     */
    private void updateLinePipe(final ViewGroup dialog, PipeBean currentStream) {
        pd = new ProgressDialog(getContext());
        pd.setMessage("正在提交，请稍后...");
        pd.show();
        currentStream.setLineLength(mLineLength);
        String dataJson = JsonUtil.getJson(currentStream);
        mUploadFacilityService.updateLinePipe(dataJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        mLineLength = -1;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
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
                            mStatus = 0;
                            //提交文件
                            isDrawStream = false;
                            initStreamGLayer();
                            initArrowGLayer();
                            initGLayer();
                            refreshMap();
                            hideBottomSheet();
                            ToastUtil.shortToast(mContext, "提交成功");
                            dialog.setVisibility(View.GONE);
                        } else {
                            ToastUtil.shortToast(mContext, "保存失败，原因是：" + responseBody.getMessage());
                        }
                    }
                });
    }

    private PipeBean createStreamBean(Component component) {
        String layerName = component.getLayerName();
        PipeBean pipeBean = new PipeBean();
        pipeBean.setComponentType(layerName);
        pipeBean.setId(StringUtil.getNotNullString(component.getGraphic().getAttributes().get("REPORT_ID"), ""));
        pipeBean.setLackType(StringUtil.getNotNullString(component.getGraphic().getAttributes().get("DATA_TYPE"), ""));
        pipeBean.setObjectId(String.valueOf(component.getGraphic().getAttributes().get("OBJECTID")));
        pipeBean.setOldPipeType(StringUtil.getNotNullString(component.getGraphic().getAttributes().get("SORT"), ""));
        String direction = StringUtil.getNotNullString(component.getGraphic().getAttributes().get("DIRECTION"), "");
        String pipeGj = StringUtil.getNotNullString(component.getGraphic().getAttributes().get("PIPE_GJ"), "");
        String pipeLenght = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.LINE_LENGTH), "");
        if (!StringUtil.isEmpty(direction)) {
            pipeBean.setOldDirection(direction);
            pipeBean.setDirection(direction);
        } else {
            pipeBean.setOldDirection("");
            pipeBean.setDirection("正向");
        }
        pipeBean.setPipeGj(pipeGj);

        if (!StringUtil.isEmpty(pipeLenght)) {
            pipeBean.setLineLength(Double.valueOf(pipeLenght));
        }
        if (component.getGraphic().getGeometry() instanceof Point) {
            pipeBean.setX(((Point) component.getGraphic().getGeometry()).getX());
            pipeBean.setY(((Point) component.getGraphic().getGeometry()).getY());
        } else if (component.getGraphic().getGeometry() instanceof Polyline) {
            Polyline polyline = (Polyline) component.getGraphic().getGeometry();
            Point startPoint = polyline.getPoint(0);
            Point endPoint = polyline.getPoint(polyline.getPointCount() - 1);
            pipeBean.setX(startPoint.getX());
            pipeBean.setY(startPoint.getY());
            pipeBean.setEndX(endPoint.getX());
            pipeBean.setEndY(endPoint.getY());
        }
        return pipeBean;
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

    private LayerInfo getPlanLayerInfo() {
        mLayerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
        if (ListUtil.isEmpty(mLayerInfosFromLocal)) {
            return null;
        }
        for (LayerInfo layerInfo : mLayerInfosFromLocal) {
            if (layerInfo.getLayerName().contains(PatrolLayerPresenter4JbjPsdy.RIVER_FLOW_LAYER_NAME)) {
                return layerInfo;
            }
        }
        return null;
    }

    private LayerInfo getDoubtLayerInfo() {
        if (ListUtil.isEmpty(mLayerInfosFromLocal)) {
            mLayerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
        }
        if (ListUtil.isEmpty(mLayerInfosFromLocal)) {
            return null;
        }
        for (LayerInfo layerInfo : mLayerInfosFromLocal) {
            if (layerInfo.getLayerName().contains(PatrolLayerPresenter4JbjPsdy.RIVER_DOUBT_LAYER_NAME)) {
                return layerInfo;
            }
        }
        return null;
    }

    private LayerInfo getDYLayerInfo() {
        if (ListUtil.isEmpty(mLayerInfosFromLocal)) {
            mLayerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
        }
        if (ListUtil.isEmpty(mLayerInfosFromLocal)) {
            return null;
        }
        for (LayerInfo layerInfo : mLayerInfosFromLocal) {
            if (layerInfo.getLayerName().contains(PatrolLayerPresenter4JbjPsdy.DRAINAGE_UNIT_LAYER2)) {
                return layerInfo;
            }
        }
        return null;
    }

    private LayerInfo getPshMpInfo() {
        mLayerInfosFromLocal = LayerServiceFactory.provideLayerService(mContext).getLayerInfosFromLocal();
        if (ListUtil.isEmpty(mLayerInfosFromLocal)) {
            return null;
        }
        for (LayerInfo layerInfo : mLayerInfosFromLocal) {
            if (layerInfo.getLayerName().equals(PatrolLayerPresenter4JbjPsdy.DOOR_NO_LAYER)) {
                layerInfo.setLayerName(PatrolLayerPresenter4JbjPsdy.DRAINAGE_PSHMP);
                layerInfo.setLayerId(9999);
                layerInfo.setDefaultVisibility(true);
                return layerInfo;
            }
        }
        return null;
    }

    private void drawStream(Component component, List<PipeBean> pipeBeans, int color) {
        initArrowGLayer();
        if (ListUtil.isEmpty(pipeBeans)) {
            return;
        }
        if (component.getGraphic().getGeometry() instanceof Point) {
            Point componentCenter = GeometryUtil.getGeometryCenter(component.getGraphic().getGeometry());
            for (PipeBean upStream : pipeBeans) {
                Polyline polyline = new Polyline();
                Point geometryCenter = new Point(upStream.getX(), upStream.getY());
                if (geometryCenter != null) {
                    polyline.startPath(geometryCenter);
                    polyline.lineTo(GeometryUtil.getGeometryCenter(component.getGraphic().getGeometry()));
                    drawGeometry(polyline, mArrowGLayer, new SimpleLineSymbol(color, 3), false, false);
                    //如果上游，那么此时箭头方向是指向当前点击的部件
                    //上游点
                    Point point = mMapView.toScreenPoint(geometryCenter);
                    //当前点
                    Point point2 = mMapView.toScreenPoint(componentCenter);
                    double angle = GeometryUtil.getAngle(point.getX(), point.getY(), point2.getX(), point2.getY());
                    if ("反向".equals(upStream.getDirection())) {
                        drawArrow(GeometryUtil.getGeometryCenter(polyline), angle, color, false);
                    } else {
                        drawArrow(GeometryUtil.getGeometryCenter(polyline), angle, color, true);
                    }
                }
            }
        } else {
            drawPipeline(component, pipeBeans, color);
        }
    }

    private void drawPipeline(Component component, List<PipeBean> pipeBeans, int color) {
        initArrowGLayer();
        if (ListUtil.isEmpty(pipeBeans) || component.getGraphic().getGeometry() == null) {
            return;
        }
        PipeBean pipeBean = pipeBeans.get(0);
        Polyline polyline = (Polyline) component.getGraphic().getGeometry();
        Point startPoint = mMapView.toScreenPoint(polyline.getPoint(0));
        Point endPoint = mMapView.toScreenPoint(polyline.getPoint(polyline.getPointCount() - 1));
        double angle = GeometryUtil.getAngle(endPoint.getX(), endPoint.getY(), startPoint.getX(), startPoint.getY());
        if ("反向".equals(pipeBean.getDirection())) {
            drawArrow(GeometryUtil.getGeometryCenter(polyline), angle, color, false);
        } else {
            drawArrow(GeometryUtil.getGeometryCenter(polyline), angle, color, true);
        }
    }

    private void setMode(int status) {
        mStatus = status;
        if (0 == status) {
            ll_topbar_container.setVisibility(View.GONE);
            pipe_view.setVisibility(View.GONE);
            ll_topbar_container2.setVisibility(View.GONE);
            if (locationMarker != null) {
                locationMarker.setVisibility(View.GONE);
            }
            initStreamGLayer();
            initGLayer();
            hideBottomSheet();
            mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
        } else if (1 == status || UpOrDownStream) {
            ll_topbar_container.setVisibility(View.VISIBLE);
            ll_topbar_container2.setVisibility(View.VISIBLE);
        } else if (2 == mStatus) {
            UpOrDownStream = false;
            ll_topbar_container.setVisibility(View.VISIBLE);
        } else if (status == 5 || status == 6 || status == 7) {
            ll_topbar_container.setVisibility(View.VISIBLE);
            ll_topbar_container2.setVisibility(View.VISIBLE);
        }
    }

    private void exit() {
        ifInGuaJie = false;
        ifInLine = false;
        if (2 == mStatus && UpOrDownStream) {
            UpOrDownStream = false;
            mStatus = 2;
            setMode(mStatus);
            showAttributeForStream(true);
        } else if (1 == mStatus || 2 == mStatus || 5 == mStatus || 6 == mStatus || 7 == mStatus) {
            setMode(0);
            showAttributeForStream(false);
            if (locationMarker != null) {
                locationMarker.setVisibility(View.GONE);
            }
            ifInGuaJie = false;
            if (btn_check_cancel.getVisibility() == View.VISIBLE) {
                btn_check_cancel.performClick();
            }
            if (btn_pipe_cancel.getVisibility() == View.VISIBLE) {
                btn_pipe_cancel.performClick();
            }
            if (btn_dy_cancel.getVisibility() == View.VISIBLE) {
                btn_dy_cancel.performClick();
            }
            mMapView.getCallout().hide();
            hideBottomSheet();
            initGLayer();
            initSelectGLayer();
            initStreamGLayer();
            initGraphicSelectGLayer();
            initArrowGLayer();
            mAllPipeBeans.clear();
            mMapView.setOnTouchListener(getDefaultMapOnTouchListener());
        } else if (0 == mStatus) {
            ((Activity) mContext).finish();
        }
    }

    /**
     * 关联窨井标题栏
     */
    public void addTopBarView(String title) {
        View topbar_view = View.inflate(mContext, R.layout.pipe_topbar_view, null);
        topbar_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View btn_back = topbar_view.findViewById(R.id.btn_back);
        if (StringUtil.isEmpty(title)) {
            ((TextView) topbar_view.findViewById(R.id.scv)).setText("");
        } else {
            ((TextView) topbar_view.findViewById(R.id.scv)).setText(title);
        }
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        topbar_view.findViewById(R.id.container).setOnClickListener(null);  // 拦截测量时的顶栏点击事件，避免点到地图上
        ll_topbar_container.removeAllViews();
        ll_topbar_container.addView(topbar_view);
    }

    /**
     * 将compone转换成校核实体类
     *
     * @param attribute
     * @param geometry
     * @return
     */
    private ModifiedFacility getModifiedFacilityFromGraphic(Map<String, Object> attribute, Geometry geometry) {
        ModifiedFacility modifiedFacility = new ModifiedFacility();
        modifiedFacility.setObjectId(objectToString(attribute.get("OBJECTID")));
        modifiedFacility.setOriginAddr(objectToString(attribute.get("ORIGIN_ADDR")));
        modifiedFacility.setAddr(objectToString(attribute.get("ADDR")));
        modifiedFacility.setRoad(objectToString(attribute.get("ROAD")));
        modifiedFacility.setAttrFive(objectToString(attribute.get("ATTR_FIVE")));
        modifiedFacility.setAttrFour(objectToString(attribute.get("ATTR_FOUR")));
        modifiedFacility.setAttrThree(objectToString(attribute.get("ATTR_THREE")));
        modifiedFacility.setAttrTwo(objectToString(attribute.get("ATTR_TWO")));
        modifiedFacility.setAttrOne(objectToString(attribute.get("ATTR_ONE")));
        modifiedFacility.setCorrectType(objectToString(attribute.get("CORRECT_TYPE")));
        modifiedFacility.setReportType("delete");
        modifiedFacility.setDescription(objectToString(attribute.get("DESRIPTION")));
        modifiedFacility.setParentOrgName(objectToString(attribute.get("PARENT_ORG_NAME")));
        modifiedFacility.setDirectOrgName(objectToString(attribute.get("DIRECT_ORG_NAME")));
        modifiedFacility.setTeamOrgName(objectToString(attribute.get("TEAM_ORG_NAME")));
        modifiedFacility.setSuperviseOrgName(objectToString(attribute.get("SUPERVISE_ORG_NAME")));
        modifiedFacility.setLayerName(objectToString(attribute.get("LAYER_NAME")));
        modifiedFacility.setUsid(objectToString(attribute.get("US_ID")));
        //修改后的位置
        modifiedFacility.setX(((Point) geometry).getX());
        modifiedFacility.setY(((Point) geometry).getY());
        //原设施位置
        //Point geometryCenter = GeometryUtil.getGeometryCenter(geometry);
        modifiedFacility.setOriginX(objectToDouble(attribute.get("ORGIN_X")));
        modifiedFacility.setOriginY(objectToDouble(attribute.get("ORGIN_Y")));
        modifiedFacility.setMarkPerson(objectToString(attribute.get("MARK_PERSON")));
        modifiedFacility.setMarkTime(objectToLong(attribute.get("MARK_TIME")));
        modifiedFacility.setUpdateTime(objectToLong(attribute.get("UPDATE_TIME")));
        modifiedFacility.setCheckState(objectToString(attribute.get("CHECK_STATE")));
        modifiedFacility.setId(objectToLong(attribute.get(UploadLayerFieldKeyConstant.MARK_ID)));

        //原设施信息
        modifiedFacility.setOriginRoad(objectToString(attribute.get("ORIGIN_ROAD")));
        modifiedFacility.setOriginAttrOne(objectToString(attribute.get("ORIGIN_ATTR_ONE")));
        modifiedFacility.setOriginAttrTwo(objectToString(attribute.get("ORIGIN_ATTR_TWO")));
        modifiedFacility.setOriginAttrThree(objectToString(attribute.get("ORIGIN_ATTR_THREE")));
        modifiedFacility.setOriginAttrFour(objectToString(attribute.get("ORIGIN_ATTR_FOUR")));
        modifiedFacility.setOriginAttrFive(objectToString(attribute.get("ORIGIN_ATTR_FIVE")));

        //设施问题
        modifiedFacility.setpCode(objectToString(attribute.get("PCODE")));
        modifiedFacility.setChildCode(objectToString(attribute.get("CHILD_CODE")));

        //管理状态
        modifiedFacility.setCityVillage(objectToString(attribute.get("CITY_VILLAGE")));
        modifiedFacility.setAttrSix(objectToString(attribute.get("ATTR_SIX")));
        modifiedFacility.setAttrSeven(objectToString(attribute.get("ATTR_SEVEN")));
        modifiedFacility.setRiverx(objectToDouble(attribute.get("RIVERX")));
        modifiedFacility.setRivery(objectToDouble(attribute.get("RIVERY")));
        return modifiedFacility;
    }

    /**
     * 将compone转换成新增实体类
     *
     * @param attribute
     * @param geometry
     * @return
     */
    private UploadedFacility getUploadedFacilityFromGraphic(Map<String, Object> attribute, Geometry geometry) {
        UploadedFacility uploadedFacility = new UploadedFacility();
        uploadedFacility.setObjectId(objectToString(attribute.get("OBJECTID")));
        uploadedFacility.setAddr(objectToString(attribute.get("ADDR")));
        uploadedFacility.setRoad(objectToString(attribute.get("ROAD")));
        uploadedFacility.setAttrFive(objectToString(attribute.get("ATTR_FIVE")));
        uploadedFacility.setAttrFour(objectToString(attribute.get("ATTR_FOUR")));
        uploadedFacility.setGpbh(objectToString(attribute.get("gpbh")));
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
        Point geometryCenter = GeometryUtil.getGeometryCenter(geometry);
        uploadedFacility.setX(geometryCenter.getX());
        uploadedFacility.setY(geometryCenter.getY());
        uploadedFacility.setMarkPerson(objectToString(attribute.get("MARK_PERSON")));
        uploadedFacility.setMarkTime(objectToLong(attribute.get("MARK_TIME")));
        uploadedFacility.setUpdateTime(objectToLong(attribute.get("UPDATE_TIME")));
        uploadedFacility.setCheckState(objectToString(attribute.get("CHECK_STATE")));
        uploadedFacility.setId(objectToLong(attribute.get(UploadLayerFieldKeyConstant.MARK_ID)));

        //设施问题
        uploadedFacility.setpCode(objectToString(attribute.get("PCODE")));
        uploadedFacility.setChildCode(objectToString(attribute.get("CHILD_CODE")));
        //管理状态
        uploadedFacility.setCityVillage(objectToString(attribute.get("CITY_VILLAGE")));
        uploadedFacility.setAttrSix(objectToString(attribute.get("ATTR_SIX")));
        uploadedFacility.setAttrSeven(objectToString(attribute.get("ATTR_SEVEN")));
        uploadedFacility.setRiverx(objectToDouble(attribute.get("RIVERX")));
        uploadedFacility.setRivery(objectToDouble(attribute.get("RIVERY")));
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

    private Long objectToLong(Object object) {
        if (object == null) {
            return null;
        }
        return Long.valueOf(object.toString());
    }

    private int objectToInteger(Object object) {
        if (object == null) {
            return -1;
        }
        return Integer.valueOf(object.toString());
    }

    /**
     * 删除设施
     */
    private void deleteFacility(Long id, final Component component) {
        initDeleteFacilityService();
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("删除中.....");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        deleteFacilityService.deleteFacility(UploadLayerFieldKeyConstant.ADD, id)
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
                            refreshMap();
                            ModifiedFacility modifiedFacilityFromGraphic = getModifiedFacilityFromGraphic(component.getGraphic().getAttributes(), component.getGraphic().getGeometry());
                            uploadJournal(modifiedFacilityFromGraphic);
                        } else {
                            CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
                                    BaseInfoManager.getUserName(mContext) + "原因是：" + s.getMessage()));
                            Toast.makeText(mContext, s.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void initDeleteFacilityService() {
        if (deleteFacilityService == null) {
            deleteFacilityService = new DeleteFacilityService(mContext);
        }
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
     *
     * @param oldLayerName
     * @return
     */
    private String getLayerName(String oldLayerName) {
        if (!attribute.contains(oldLayerName))
            return "";
        if (oldLayerName.contains("窨井")) {
            return "窨井";
        } else if (oldLayerName.contains("排水管道")) {
            return "排水管道";
        } else if (oldLayerName.contains("雨水口")) {
            return "雨水口";
        } else if (oldLayerName.contains("排水口")) {
            return "排水口";
        } else if (oldLayerName.contains("排水沟渠")) {
            return "排水沟渠";
        } else if (oldLayerName.equals("存疑区域")) {
            return "存疑区域";
        } else {
            return "";
        }
    }

    /**
     * 倒计时
     *
     * @param time
     * @return
     */
    public Observable<Integer> countdown(int time) {
        if (time < 1) time = 1;
        final int countTime = time;
        return Observable.interval(0, 350, TimeUnit.MILLISECONDS)
                .takeWhile(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return whileTake;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1);

    }

    private void uploadDoubt(final Component component, DoubtBean doubtBean, final AlertDialog dialog, final TextView tv_zhiyi) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("正在提交，请等待");
            progressDialog.setCancelable(false);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        mUploadFacilityService.partsDoubt(doubtBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        ToastUtil.shortToast(mContext, "提交失败");
                        CrashReport.postCatchedException(new Exception("新增存疑失败，上报用户是：" +
                                BaseInfoManager.getUserName(mContext) + "原因是：" + e.getLocalizedMessage()));
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (responseBody.getCode() == 200) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if (mMapDrawQuestionView != null && component == null) {
                                mMapDrawQuestionView.clear();
                            }
                            initGLayer();
                            hideBottomSheet();
                            refreshMap();
//                            if(tv_zhiyi != null){
//                                tv_zhiyi.setText(" 取消存疑 ");
//                            }
                            ToastUtil.shortToast(mContext, "存疑提交成功");
                        } else {
                            ToastUtil.shortToast(mContext, "保存失败，原因是：" + responseBody.getMessage());
                            CrashReport.postCatchedException(new Exception("新增存疑失败，上报用户是：" +
                                    BaseInfoManager.getUserName(mContext) + "原因是：" + responseBody.getMessage()));
                        }
                    }
                });
    }

    private void cancelDoubt(String id, final TextView tv_zhiyi) {
        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("取消存疑中.....");
        mDialog.show();
        if (mUploadLayerService == null) {
            mUploadFacilityService = new UploadFacilityService(mContext);
        }
        mUploadFacilityService.partsDoubt(id)
                //获取列表（不包含附件）
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result2<String>>() {
                    @Override
                    public void onCompleted() {
                        if (mDialog != null && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mDialog != null && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        ToastUtil.iconShortToast(mContext, R.mipmap.ic_alert_yellow, "存疑取消失败，请稍后重试");
                    }

                    @Override
                    public void onNext(Result2<String> s) {
                        if (s.getCode() == 200) {
//                            if(tv_zhiyi != null){
//                                tv_zhiyi.setText("   存疑   ");
//                            }
                            initGLayer();
                            hideBottomSheet();
                            refreshMap();
                            Toast.makeText(mContext, "存疑取消成功", Toast.LENGTH_SHORT).show();
                        } else {
//                            CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
//                                    BaseInfoManager.getUserName(mContext) + "原因是：" + s.getMessage()));
                            Toast.makeText(mContext, s.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    /**
     * 提交巡检日志
     *
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
        //数据纠错
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
                        CrashReport.postCatchedException(new Exception("巡检上报失败，上报用户是：" +
                                BaseInfoManager.getUserName(mContext) + "原因是：" + e.getLocalizedMessage()));
                        ToastUtil.shortToast(mContext, "删除成功，巡检提交失败");
                        //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "提交失败");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (responseBody.getCode() == 200) {
                            ToastUtil.shortToast(mContext, "删除成功");
                        } else {
                            CrashReport.postCatchedException(new Exception("核准上报失败，上报用户是：" +
                                    BaseInfoManager.getUserName(mContext) + "原因是：" + responseBody.getMessage()));
                            ToastUtil.shortToast(mContext, "删除成功，巡检提交失败，原因是：" + responseBody.getMessage());
                            //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "保存失败，原因是：" + responseBody.getMessage());
                        }
                    }
                });
    }

    /**
     * 生成筛选后的门牌号图层，并加载到地图上去
     *
     * @param component
     */
    private void addPsdyMph(Component component) {
        synchronized (this) {
            String objectId = StringUtil.getNotNullString(component.getGraphic().getAttributeValue("OBJECTID"), "");
            if (mPsdyLayerFactory == null) {
                mPsdyLayerFactory = new PsdyLayerFactory(objectId, mContext);
            } else {
                mPsdyLayerFactory.setObjectId(objectId);
            }
            LayerInfo pshMp = getPshMpInfo();
            Layer layer = mPsdyLayerFactory.getLayer(mContext, pshMp);
            mIndex = mMapView.addLayer(layer);
        }
    }

    /**
     * 清空门牌号图层
     */
    private void removePsdyMph() {
        synchronized (this) {
            if (mIndex != -1) {
                mMapView.removeLayer(mIndex);
                mIndex = -1;
            }
        }

    }

    private void showSewerageUserBtnOnly(final Component component) {

        if (ifInSwerageUserMode) {
            map_bottom_sheet.findViewById(R.id.tv_gjdy).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.line_gjdy).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.psdy_delete).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.psdy_line_del).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.psdy_jc).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.psdy_line_jc).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.tv_zhiyi).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.stream_line3).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.tv_detail_left).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.stream_line4).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.tv_delete).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.stream_line1).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.tv_sure).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.stream_line2).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.tv_error_correct).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.stream_line).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.tv_detail_right).setVisibility(View.GONE);
            map_bottom_sheet.findViewById(R.id.tv_swerage_user).setVisibility(View.VISIBLE);
            map_bottom_sheet.findViewById(R.id.line_swerage_user).setVisibility(View.VISIBLE);
            map_bottom_sheet.findViewById(R.id.tv_swerage_user).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 隐藏其他的，然后显示弹窗
                    hideBottomSheet();
                    showSwerageUserBottomSheet(component);
                }
            });
            //显示排水单元里面的门牌号
            removePsdyMph();
            addPsdyMph(component);
        }
//        TextView tv = new TextView(getContext());
//        tv.setTextSize();
    }

    private void showNotHookDialog(final Component component, final Geometry geometry, final TextView tv_zhiyi) {
        View view = View.inflate(mContext, R.layout.activity_dialog_view, null);
        String type = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(ComponentFieldKeyConstant.WFGJ_REASON), "");
        final TextFieldTableItem fieldTableItem = (TextFieldTableItem) view.findViewById(R.id.textitem_description);
        fieldTableItem.setText(type);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("请填写无法挂接原因")
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        TextView title = new TextView(mContext);
        title.setGravity(Gravity.CENTER);
        title.requestLayout();
        title.setText("请填写无法挂接原因");
//        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        title.setTextSize(18);
        builder.setCustomTitle(title);
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (StringUtil.isEmpty(fieldTableItem.getText().trim())) {
                            ToastUtil.shortToast(mContext, "请填写无法挂接原因");
                            return;
                        }
                        uploadNotHookReason(component, fieldTableItem.getText().trim(), dialog, tv_zhiyi);
                    }
                });
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        LinearLayout.MarginLayoutParams marginLayoutParams = (LinearLayout.MarginLayoutParams) title.getLayoutParams();
        marginLayoutParams.setMargins(8,45,8,30);
        title.requestLayout();
    }

    private void uploadNotHookReason(final Component component, String reason, final AlertDialog dialog, final TextView tv_zhiyi) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("正在提交，请等待");
            progressDialog.setCancelable(false);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        String objectId = StringUtil.getNotNullString(component.getGraphic().getAttributeValue(ComponentFieldKeyConstant.OBJECTID), "");
        mUploadFacilityService.partsNotHook(objectId, 1, reason)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        ToastUtil.shortToast(mContext, "提交失败");
                        CrashReport.postCatchedException(new Exception("新增无法挂接原因失败，上报用户是：" +
                                BaseInfoManager.getUserName(mContext) + "原因是：" + e.getLocalizedMessage()));
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (responseBody.getCode() == 200) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if (mMapDrawQuestionView != null && component == null) {
                                mMapDrawQuestionView.clear();
                            }
                            initGLayer();
                            hideBottomSheet();
                            refreshMap();
//                            if(tv_zhiyi != null){
//                                tv_zhiyi.setText(" 取消存疑 ");
//                            }
                            ToastUtil.shortToast(mContext, "提交成功");
                        } else {
                            ToastUtil.shortToast(mContext, "保存失败，原因是：" + responseBody.getMessage());
                            CrashReport.postCatchedException(new Exception("新增无法挂接原因失败，上报用户是：" +
                                    BaseInfoManager.getUserName(mContext) + "原因是：" + responseBody.getMessage()));
                        }
                    }
                });
    }

    private void cancelNotHook(String id, String reason) {
        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("取消无法挂接状态中.....");
        mDialog.show();
        if (mUploadLayerService == null) {
            mUploadFacilityService = new UploadFacilityService(mContext);
        }
        mUploadFacilityService.partsNotHook(id, 0, reason)
                //获取列表（不包含附件）
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        if (mDialog != null && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mDialog != null && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        ToastUtil.iconShortToast(mContext, R.mipmap.ic_alert_yellow, "取消无法挂接状态失败，请稍后重试");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (responseBody.getCode() == 200) {
                            initGLayer();
                            hideBottomSheet();
                            refreshMap();
                            Toast.makeText(mContext, "取消无法挂接状态成功", Toast.LENGTH_SHORT).show();
                        } else {
//                            CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
//                                    BaseInfoManager.getUserName(mContext) + "原因是：" + s.getMessage()));
                            Toast.makeText(mContext, responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
