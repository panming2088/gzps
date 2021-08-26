package com.augurit.agmobile.patrolcore.common.table;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.RefreshHistoryEvent;
import com.augurit.agmobile.patrolcore.common.RefreshLocalEvent;
import com.augurit.agmobile.patrolcore.common.opinion.view.IOpinionTemplateView;
import com.augurit.agmobile.patrolcore.common.opinion.view.OpinionTemplateView;
import com.augurit.agmobile.patrolcore.common.opinion.view.presenter.IOpinionTemplatePresenter;
import com.augurit.agmobile.patrolcore.common.opinion.view.presenter.OpinionTemplatePresenter;
import com.augurit.agmobile.patrolcore.common.preview.view.PhotoPagerActivity;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBCallback;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableChildItems;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableItems;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.UploadTableItemResult;
import com.augurit.agmobile.patrolcore.common.table.event.AddUploadRecordEvent;
import com.augurit.agmobile.patrolcore.common.table.model.ClientTaskRecord;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.common.table.model.ProjectTable;
import com.augurit.agmobile.patrolcore.common.table.model.TableChildItem;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.service.FuzzySearchService;
import com.augurit.agmobile.patrolcore.common.table.util.ControlState;
import com.augurit.agmobile.patrolcore.common.table.util.ControlType;
import com.augurit.agmobile.patrolcore.common.table.util.FireFightingView;
import com.augurit.agmobile.patrolcore.common.table.util.RequireState;
import com.augurit.agmobile.patrolcore.common.table.util.SaftyHazardView;
import com.augurit.agmobile.patrolcore.common.table.util.TableState;
import com.augurit.agmobile.patrolcore.common.table.util.ValidateType;
import com.augurit.agmobile.patrolcore.common.table.util.ValidateUtils;
import com.augurit.agmobile.patrolcore.common.util.IdcardValidator;
import com.augurit.agmobile.patrolcore.common.util.MaxLengthInputFilter;
import com.augurit.agmobile.patrolcore.editmap.EditMapItemPresenter;
import com.augurit.agmobile.patrolcore.editmap.OnEditMapFeatureCompleteCallback;
import com.augurit.agmobile.patrolcore.editmap.util.EditModeConstant;
import com.augurit.agmobile.patrolcore.layer.service.EditLayerService2;
import com.augurit.agmobile.patrolcore.selectdevice.view.OnReceivedSelectedDeviceListener;
import com.augurit.agmobile.patrolcore.selectdevice.view.PatrolSelectDevicePresenterImpl;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.agmobile.patrolcore.selectlocation.view.IMapTableItemPresenter;
import com.augurit.agmobile.patrolcore.selectlocation.view.MapTableItem;
import com.augurit.agmobile.patrolcore.selectlocation.view.MapTableItemPresenter;
import com.augurit.agmobile.patrolcore.selectlocation.view.SelectLocationTableItem2;
import com.augurit.agmobile.patrolcore.selectlocation.view.WebViewMapTableItem;
import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.IReceivedSelectLocationListener;
import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.ISelectLocationTableItemPresenter;
import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.ISelectLocationTableItemView;
import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.SelectLocationEditTableItemPresenter;
import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.SelectLocationReEditStatePresenter;
import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.SelectLocationReadStatePresenter;
import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.view.NoMapSelectLocationEditStateTableItemView;
import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.view.NoMapSelectLocationReadStateTableItemView;
import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.view.SelectLocationEditStateTableItemView;
import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.view.SelectLocationReEditTableItemView;
import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.view.SelectLocationReadStateTableItemView;
import com.augurit.agmobile.patrolcore.survey.model.BasicDanweiInfo;
import com.augurit.agmobile.patrolcore.survey.model.BasicDongInfo;
import com.augurit.agmobile.patrolcore.survey.model.BasicRenKouInfo;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.FileHeaderConstant;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.HSPVFileUtil;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.HorizontalScrollPhotoView;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.HorizontalScrollPhotoViewAdapter;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.ImageUtil;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.PhotoButtonUtil;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.PhotoExtra;
import com.augurit.am.cmpt.widget.searchview.util.adapter.TextWatcherAdapter;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.CompressPictureUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.file.SharedPreferencesUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.Graphic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.collections4.MapUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.PhotoButtonUtil.RESULT_CAPTURE_PHOTO;
import static com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.PhotoButtonUtil.RESULT_OPEN_PHOTO;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table
 * @createTime 创建时间 ：17/3/13
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/13
 * @modifyMemo 修改备注：
 */
public class TableViewManager {
    private static final String NOT_EDITABLE = "A00601"; //当tableitem不可编辑时服务端传递过来的值
    /**
     * 以下为编辑图层数据相关变量
     */
    public static String OLD_LAYER_OBJECTID_FIELD_IN_NEW = "OLD_OBJECTID";
    public static boolean isEditingFeatureLayer = true;   //当前编辑的表单是否ArcGIS的图层
    public static String featueLayerUrl = "";        //当前编辑的ArcGIS的图层URL
    public static Graphic graphic = null;                   //当前编辑的要素
    private Callback2 uploadEditCallback = null;
    public static Geometry geometry = null;                       //当前要素图形
    protected TableDataManager tableDataManager;
    //表格状态
    private TableState tableState;
    //是否更新同步网络数据
    private boolean isUpdateFromNet;
    private List<TableItem> tableItems;
    private ViewGroup mainView;
    private Map<String, View> map; //控件容器
    private int year, month, day, hour, minute, second;
    private Calendar cal;
    private List<Photo> pList;
    private PhotoExtra mPhotoExtra;
    private Intent mPhotoIntentData;
    private PopupWindow popupWindow;
    private IMapTableItemPresenter mMapPresenter;
    private Context mContext;
    private Map<String, String> valueMap;
    private String url;
    private String projectId;
    private String tableKey;
    private TableLoadListener listener;
    private List<TableLoadListener> listeners = new ArrayList<>();
    private ViewGroup container_for_other_Items_exclude_map_and_photos; //用于装载除了地图和图片选择控件外的item的容器
    private View container_for_other_Items_exclude_map_and_photos_root_view;//用于装载除了地图和图片选择控件外的item的容器的根布局
    private boolean ifAddedContainerForOtherItems = false; //是否已经添加了用于装载除了地图和图片选择控件外的item的容器
    private Callback1 patrolProgressClickListener;
    private Map<String, HorizontalScrollPhotoView> mPhotoViewMap;
    private Map<String, HorizontalScrollPhotoViewAdapter> mPhotoViewAdapterMap;
    private Map<String, List<Photo>> mPhotoListMap;
    private Map<String, String> mPhotoNameMap;
    private CustomTableListener mAddCustomTableListener;
    private TableValueListener mTableValueListener;
    private String link = "";  //意见模板的流程节点名
    private PatrolSelectDevicePresenterImpl mPatrolSelectDevicePresenter;
    private String tableName; //行业表名,如果行业表名称为"DEFAULT",那么使用商业接口上传数据；否则，使用行业接口上传数据；
    private String patrolCode;//工单编号 当前上报内容的标识
    private String recordId = "";//上报记录ID(如果是更新,则由服务端给)
    private String taskId = "";//该表格模板所涉及的任务ID
    private String cardTypeName = "";  //证件类型
    private View.OnClickListener mapButtonClickListener; //重新设置地图控件的点击事件
    /************************************* xcl 番禺四标四实特制*************************/

    private String mAddressName; //标准地址
    private String mParentRecordId; //父记录ID
    private String mParentRecordName;
    private String mParentRecordType; //父记录的类型
    private String mRoomName; //房间号/门牌
    private String dongName; //楼栋号
    private BasicRenKouInfo basicRenKouInfo;
    private String shiyouDanweiTableName;
    private String renkouleibei; //人口类别（用于从业人员表和学生信息表）
    private BasicDanweiInfo basicDanweiInfo;
    private BasicDongInfo basicDongInfo; //栋的基本信息（用于填充套）
    private String dataStataAfterUpload;//提交给服务端的数据状态
    private String dataStataAfterDeleted; //删除记录后提交给服务端的数据状态
    private SelectLocationTableItem2 selectLocationTableItem2;
    private ISelectLocationTableItemPresenter selectLocationTableItemPresenter;

    /**
     * 不要显示『查看位置』按钮
     */
    private boolean dontShowCheckLocationButton = false;
    private ISelectLocationTableItemView iSelectLocationTableItemView;

    /**
     * 编辑模式的构造函数
     *
     * @param context
     * @param mainView
     * @param isUpdateFromNet
     * @param tableState
     * @param url
     * @param projectId
     */
    public TableViewManager(Context context,
                            ViewGroup mainView,
                            boolean isUpdateFromNet,
                            TableState tableState,
                            String url,
                            String projectId) {
        this.mContext = context;
        this.mainView = mainView;
        this.isUpdateFromNet = isUpdateFromNet;
        this.tableState = tableState;
        this.projectId = projectId;
        this.url = url;
        this.valueMap = new HashMap<>();
        tableDataManager = new TableDataManager(this.mContext);
        initDataAndView();

        // 需要释放掉相机界面
        //    AppManager.getAppManager().finishAllActivity();
    }

    /**
     * 编辑模式的构造函数
     *
     * @param context
     * @param mainView
     * @param isUpdateFromNet
     * @param tableState
     * @param url
     * @param projectId
     */
    public TableViewManager(Context context,
                            ViewGroup mainView,
                            boolean isUpdateFromNet,
                            TableState tableState,
                            String url,
                            String projectId,
                            String tableName) { //xcl 2017-08-14 加入行业表名

        this.mContext = context;
        this.mainView = mainView;
        this.isUpdateFromNet = isUpdateFromNet;
        this.tableState = tableState;
        this.projectId = projectId;
        this.url = url;
        this.valueMap = new HashMap<>();
        tableDataManager = new TableDataManager(this.mContext);
        initDataAndView();
        this.tableName = tableName;

        // 需要释放掉相机界面
        //   AppManager.getAppManager().finishAllActivity();
    }

    /**
     * 构造函数(即将废弃)
     *
     * @param context
     * @param mainView
     * @param isUpdateFromNet
     * @param tableState
     * @param list
     * @param photos
     * @param projectId
     */
    @Deprecated
    public TableViewManager(Context context,
                            ViewGroup mainView,
                            boolean isUpdateFromNet,
                            TableState tableState,
                            List<TableItem> list,
                            List<Photo> photos,
                            String projectId) {
        this.mContext = context;
        this.mainView = mainView;
        this.isUpdateFromNet = isUpdateFromNet;
        this.tableState = tableState;
        this.tableItems = list;
        this.projectId = projectId;
        this.pList = photos;
        this.valueMap = new HashMap<>();
        tableDataManager = new TableDataManager(this.mContext);
        initDataAndView();

        // 需要释放掉相机界面
        //  AppManager.getAppManager().finishAllActivity();
    }

    /**
     * 阅读模式和再编辑模式的构造函数
     *
     * @param context
     * @param mainView
     * @param isUpdateFromNet
     * @param tableState
     * @param list
     * @param photos
     * @param projectId
     * @param tableKey
     */
    public TableViewManager(Context context,
                            ViewGroup mainView,
                            boolean isUpdateFromNet,
                            TableState tableState,
                            List<TableItem> list,
                            List<Photo> photos,
                            String projectId,
                            String tableKey) {
        this.mContext = context;
        this.mainView = mainView;
        this.isUpdateFromNet = isUpdateFromNet;
        this.tableState = tableState;
        this.tableItems = list;
        this.projectId = projectId;
        this.pList = photos;
        this.tableKey = tableKey;
        this.valueMap = new HashMap<>();
        tableDataManager = new TableDataManager(this.mContext);
        sortPhotos(photos);
        initDataAndView();

        // 需要释放掉相机界面
        //     AppManager.getAppManager().finishAllActivity();
    }

    /**
     * 阅读模式和再编辑模式的构造函数
     *
     * @param context
     * @param mainView
     * @param isUpdateFromNet
     * @param tableState
     * @param list
     * @param photos
     * @param projectId
     * @param tableKey
     */
    public TableViewManager(Context context,
                            ViewGroup mainView,
                            boolean isUpdateFromNet,
                            TableState tableState,
                            List<TableItem> list,
                            List<Photo> photos,
                            String projectId,
                            String tableKey,
                            String tableName) { //xcl 2017-08-14 加入行业表名子字段
        this.mContext = context;
        this.mainView = mainView;
        this.isUpdateFromNet = isUpdateFromNet;
        this.tableState = tableState;
        this.tableItems = list;
        this.projectId = projectId;
        this.pList = photos;
        this.tableKey = tableKey;
        this.valueMap = new HashMap<>();
        this.tableName = tableName;
        tableDataManager = new TableDataManager(this.mContext);
        sortPhotos(photos);
        initDataAndView();

        // 需要释放掉相机界面
        //AppManager.getAppManager().finishAllActivity();
    }

    /**
     * @param patrolCode
     */
    @Deprecated
    public void setPatrolCode(String patrolCode) {
        this.patrolCode = patrolCode;
    }

    /**
     * 上报记录ID
     * 如果是之前已经上传到服务器则需要传该值
     * 才能更新到服务端对应数据
     *
     * @param recordId
     */
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    /**
     * 设置该上报内容所涉及的任务ID
     *
     * @param taskId
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * 根据feild1给图片分类
     */
    private void sortPhotos(List<Photo> photos) {
        mPhotoListMap = new HashMap<>();
        mPhotoNameMap = new HashMap<>();

        if (photos == null) {
            return;
        }
        for (Photo photo : photos) {
            String key = photo.getField1();
            String photoPath = photo.getPhotoPath();
            if (key == null && photoPath != null && !photoPath.isEmpty()) {  // 针对老版本处理  TODO 之后决定是否移除
                key = "photo_close";
            }
            if (mPhotoListMap.containsKey(key)) {
                mPhotoListMap.get(key).add(photo);

                //拼接图片名字
                String allPhotoName = mPhotoNameMap.get(key);
                if (allPhotoName != null) {
                    allPhotoName = allPhotoName + "|" + photo.getPhotoName();
                } else {
                    allPhotoName = photo.getPhotoName();
                }
                mPhotoNameMap.put(key, allPhotoName);
            } else {
                ArrayList<Photo> photoList = new ArrayList<>();
                photoList.add(photo);
                mPhotoListMap.put(key, photoList);

                mPhotoNameMap.put(key, photo.getPhotoName());
            }
        }
    }

    /**
     * 设置所有表格项内容加载完毕后触发的监听器
     *
     * @param listener
     */
    public void setLoadListener(TableLoadListener listener) {
        this.listener = listener;
    }

    /*private void addQRScanBtn(TableItem tableItem) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_item_for_qr_scan, null);
        Button btn_scan = (Button) view.findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getmContext().startActivity(new Intent(getmContext(), QRCodeScanActivity.class));
                if (!EventBus.getDefault().isRegistered(TableViewManager.this)) {
                    EventBus.getDefault().register(TableViewManager.this);
                }
            }
        });

        if (tableState != TableState.EDITING) {
            btn_scan.setVisibility(View.GONE);
        }
        addViewToContainer(view);
    }*/


    /*@Subscribe(threadMode = ThreadMode.POSTING)
    public void onGetQRScanResultEvent(GetQRScanResultEvent event) {
        if (event.getResult() != null) {
            List<Map<String, String>> list = event.getResult();
            for (Map<String, String> item : list) {
                initScanValue(item);
            }
        }

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }*/

    /**
     * 添加所有表格项内容加载完毕后触发的监听器
     *
     * @param listener
     */
    public void addLoadListener(TableLoadListener listener) {
        this.listeners.add(listener);
    }

    /**
     * 设置加载自定义表格项的监听器
     *
     * @param listener
     */
    public void setAddCustomTableItemListener(CustomTableListener listener) {
        this.mAddCustomTableListener = listener;
    }

    /**
     * 增加设施选点表格项View     *
     *
     * @param ifShowDeviceNameView 是否显示设施id
     * @param ifShowDevicIdView    是否显示设施名称
     */
    public void addDeviceView(boolean ifShowDevicIdView, boolean ifShowDeviceNameView) {
        if (!ifShowDeviceNameView && !ifShowDevicIdView) {
            return; //如果都不需要显示，直接返回
        }
        String deviceNameKey = "";
        if (getTableItemByField1("NAME") != null) {
            deviceNameKey = getTableItemByField1("NAME").getHtml_name();
        }

        mPatrolSelectDevicePresenter = new PatrolSelectDevicePresenterImpl(mContext,
                container_for_other_Items_exclude_map_and_photos, true, false, ifShowDevicIdView, ifShowDeviceNameView);
        mPatrolSelectDevicePresenter.setDeviceNameKey(deviceNameKey);
        mPatrolSelectDevicePresenter.setUnEditable();//暂时设置无法进行手动编辑，只能通过底图选择
        mPatrolSelectDevicePresenter.setOnReceivedSelectedDeviceListener(new OnReceivedSelectedDeviceListener() {
            @Override
            public void onReceivedDevice(String deviceName, String deviceId) {
                if (getEditTextViewByField1Type("OBJECT_ID") != null) {
                    getEditTextViewByField1Type("OBJECT_ID").setText(deviceId);
                }

                if (getEditTextViewByField1Type("NAME") != null) {
                    getEditTextViewByField1Type("NAME").setText(deviceName);
                }
            }
        });
        mPatrolSelectDevicePresenter.showSelectDeviceView();

        if (tableState == TableState.READING || tableState == TableState.REEDITNG) { //xcl 2017-04-05 加入再次编辑模式
            String deviceId = getDeviceId();
            String deviceName = getDeviceName();
            if (tableState == TableState.READING) {
                mPatrolSelectDevicePresenter.setReadOnly(deviceId, deviceName);
            } else {
                mPatrolSelectDevicePresenter.setReEdit(deviceId, deviceName);
            }
        }
    }

    @NonNull
    private String getDeviceName() {
        String deviceName = "";
        if (getEditTextViewByField1Type("NAME") != null) {
            deviceName = getEditTextViewByField1Type("NAME").getText().toString();
        }
        return deviceName;
    }

    @NonNull
    private String getDeviceId() {
        String deviceId = "";
        if (getEditTextViewByField1Type("OBJECT_ID") != null) {
            deviceId = getEditTextViewByField1Type("OBJECT_ID").getText().toString();
        }
        return deviceId;
    }

    /**
     * 增加地图选点表格项View
     */
    public void addMapView(TableItem tableItem) {
//        final ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_map_container_item, null);
//        final ViewGroup mapView = (ViewGroup) view.findViewById(R.id.ll_map);
//        final MapTableItem mapTableItem = new MapTableItem(mContext, mapView);
//        mMapPresenter = new MapTableItemPresenter(mContext, new SelectLocationService(mContext, null),
//                mapTableItem);
//        mMapPresenter.setOnReceivedSelectedLocationListener(new IMapTableItemPresenter.OnReceivedSelectedLocationListener() {
//            @Override
//            public void onReceivedLocation(LatLng mapLatlng, String address) {
//
//                if (getEditTextViewByField1Type("X") != null && getEditTextViewByField1Type("Y") != null) {
//                    getEditTextViewByField1Type("X").setText(mapLatlng.getLongitude() + "");
//                    getEditTextViewByField1Type("Y").setText(mapLatlng.getLatitude() + "");
//                }
//
//
//                //如果设施控件不为空，自动查找附近设施
//                if (mPatrolSelectDevicePresenter != null && mMapPresenter != null && mMapPresenter.getMapView() != null) {
//                    mPatrolSelectDevicePresenter.searchNearByDevice(mMapPresenter.getMapView(), mapLatlng.getLongitude(), mapLatlng.getLatitude());
//                }
//               /* ToastUtil.shortToast(mContext,"最后上传的坐标是：" + mapLatlng.getLatitude() +"-》"
//                        +mapLatlng.getLongitude());*/
//                //  tv_result.setText("收到的地址是:"+address);
//                // mMapPresenter.setDestinationOrLastTimeSelectLocationInLocalCoordinateSystem(mapLatlng,address);
//            }
//        });
//        mMapPresenter.showMapShortCut();
//        mainView.addView(view);
//        String address = null; //详细地址
//        TableItem tableItem = getTableItemByType(ControlType.MAP_PICKER);
//        final ViewGroup valueView = (ViewGroup) mMapPresenter.getView().getRootView();
//        resetSpecialReqiredTag(ControlType.MAP_PICKER, valueView, tableItem);
//        if (tableItem != null) {
//            map.put(tableItem.getId(), valueView);
//            if (tableItem.getValue() != null) {
//                EditText editText = (EditText) valueView.findViewById(R.id.et_);
//                editText.setText(tableItem.getValue());
//                address = tableItem.getValue();
//            }
//        }
//        final String finalAddress = address;
//        setLoadListener(new TableLoadListener() {
//            @Override
//            public void onFinishedLoad() {
//                if (tableState == TableState.READING || tableState == TableState.REEDITNG) { //xcl 2017-04-05 加入再次编辑模式
//                    LatLng latLng = getLatLng();
//                    if (latLng == null && tableState == TableState.READING) { //如果是阅读模式下经纬度为空，那么不显示地图
//                        mMapPresenter.getView().setMapInvisible();
//                        EditText editText = (EditText) valueView.findViewById(R.id.et_);
//                        editText.setEnabled(false);
//                    } else if (latLng != null && tableState == TableState.READING) {
//                        mMapPresenter.setReadOnly(latLng, finalAddress);
//                        EditText editText = (EditText) valueView.findViewById(R.id.et_);
//                        editText.setEnabled(false);
//                    } else if (tableState == TableState.REEDITNG) {
//                        mMapPresenter.setReEdit(latLng, finalAddress);
//                    }
//                }
//
//
//                //判断是否有x,y坐标
//                View x = getTableItemViewByFiled1("X");
//                if (x != null) {
//                    mapTableItem.addTableItemToMapItem(x);
//                }
//
//                View y = getTableItemViewByFiled1("Y");
//                if (y != null) {
//                    mapTableItem.addTableItemToMapItem(y);
//                }
//
//            }
//        });
        final ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_map_container_item, null);
        final ViewGroup mapContainer = (ViewGroup) view.findViewById(R.id.ll_map);

        ISelectLocationTableItemView tableItemView = null;
        /**
         * 编辑模式
         */
        if (tableState == TableState.EDITING) {
            tableItemView = new SelectLocationEditStateTableItemView(mContext);
            selectLocationTableItemPresenter = new SelectLocationEditTableItemPresenter(mContext,
                    new SelectLocationService(mContext, null), tableItemView);
        } else if (tableState == TableState.REEDITNG) {
            /**
             * 再次编辑模式,在这里获取不到经纬度的，必须等全部加载完成
             */
            tableItemView = new SelectLocationReEditTableItemView(mContext);
            selectLocationTableItemPresenter = new SelectLocationReEditStatePresenter(mContext,
                    new SelectLocationService(mContext, null),
                    tableItemView,
                    null,
                    tableItem.getValue());
        } else if (tableState == TableState.READING) {
            /**
             * 只读模式,在这里获取不到经纬度的，必须等全部加载完成
             */
            tableItemView = new SelectLocationReadStateTableItemView(mContext);
            selectLocationTableItemPresenter = new SelectLocationReadStatePresenter(mContext,
                    new SelectLocationService(mContext, null),
                    tableItemView,
                    null,
                    tableItem.getValue());
        }

        selectLocationTableItemPresenter.addTo(mapContainer);
        mainView.addView(view);

        selectLocationTableItemPresenter.setOnReceivedSelectedLocationListener(new IReceivedSelectLocationListener() {
            @Override
            public void onReceivedLocation(Geometry geometry, DetailAddress detailAddress) {

                if (geometry instanceof Point) {

                    if (getEditTextViewByField1Type("X") != null && getEditTextViewByField1Type("Y") != null) {
                        //经度
                        getEditTextViewByField1Type("X").setText(((Point) geometry).getX() + "");
                        //纬度
                        getEditTextViewByField1Type("Y").setText(((Point) geometry).getY() + "");
                    }


                    //如果设施控件不为空，自动查找附近设施
                    if (mPatrolSelectDevicePresenter != null && selectLocationTableItemPresenter != null && selectLocationTableItemPresenter.getView().getMapView() != null) {
                        mPatrolSelectDevicePresenter.searchNearByDevice(selectLocationTableItemPresenter.getView().getMapView(), ((Point) geometry).getX(), ((Point) geometry).getY());
                    }
                }

            }
        });


        setLoadListener(new TableLoadListener() {
            @Override
            public void onFinishedLoad() {

                if (tableState == TableState.READING || tableState == TableState.READING) {
                    LatLng latLng = getLatLng();
                    if (latLng != null){
                        Point geometry = new Point(latLng.getLongitude(), latLng.getLatitude());
                        selectLocationTableItemPresenter.setGeometry(geometry);
                    }
                }

                //当全部加载完成后才进行加载地图，可以提高加载表单的速度
                selectLocationTableItemPresenter.loadMap();

                //判断是否有x,y坐标
                View x = getTableItemViewByFiled1("X");
                if (x != null) {
                    selectLocationTableItemPresenter.getView().addView(x);
                }

                View y = getTableItemViewByFiled1("Y");
                if (y != null) {
                    selectLocationTableItemPresenter.getView().addView(y);
                }
            }
        });

        final ViewGroup valueView = (ViewGroup) selectLocationTableItemPresenter.getView().getRootView();
        resetSpecialReqiredTag(ControlType.MAP_PICKER, valueView, tableItem);
        if (tableItem != null) {
            map.put(tableItem.getId(), valueView);
        }

    }

    /**
     * 增加编辑地图控件
     */
    public void addEditMapFeatureView() {

        //        final ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_map_container_item, null);
        //        final ViewGroup mapView = (ViewGroup) view.findViewById(R.id.ll_map);
        //
        //        EditMapFeatureItemView editMapFeatureItemView = new EditMapFeatureItemView(mContext, new EditMapFeatureItemView.OnEditMapFeatureCompleteCallback<Geometry>() {
        //            @Override
        //            public void onFinished(Geometry data) {
        //                geometry = data;
        //            }
        //        });
        //
        //        editMapFeatureItemView.addViewToContainer(mapView);
        //        mainView.addView(view);
        //
        //
        //        if (tableState == TableState.REEDITNG && geometry != null){
        //            editMapFeatureItemView.setReEditting(geometry);
        //        }else if (tableState == TableState.READING && geometry != null){
        //            editMapFeatureItemView.setReading(geometry);
        //        }

        final ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_map_container_item, null);
        final ViewGroup mapView = (ViewGroup) view.findViewById(R.id.ll_map);
        final MapTableItem mapTableItem = new MapTableItem(mContext, mapView);
        final EditMapItemPresenter mMapPresenter = new EditMapItemPresenter(mContext, new SelectLocationService(mContext, null),
                mapTableItem);

        mMapPresenter.showMapShortCut();
        mainView.addView(view);
        String address = null; //详细地址
        TableItem tableItem = getTableItemByType(ControlType.EDIT_MAP_FEATURE);
        final ViewGroup valueView = (ViewGroup) mMapPresenter.getView().getRootView();
        resetSpecialReqiredTag(ControlType.EDIT_MAP_FEATURE, valueView, tableItem);
        if (tableItem != null) {
            map.put(tableItem.getId(), valueView);
            if (tableItem.getValue() != null) {
                EditText editText = (EditText) valueView.findViewById(R.id.et_);
                editText.setText(tableItem.getValue());
                address = tableItem.getValue();
            }
        }
        final String finalAddress = address;
        setLoadListener(new TableLoadListener() {
            @Override
            public void onFinishedLoad() {
                if (tableState == TableState.READING || tableState == TableState.REEDITNG) { //xcl 2017-04-05 加入再次编辑模式
                    LatLng latLng = getLatLng();
                    if (latLng == null && tableState == TableState.READING) { //如果是阅读模式下经纬度为空，那么不显示地图
                        mMapPresenter.getView().setMapInvisible();
                        EditText editText = (EditText) valueView.findViewById(R.id.et_);
                        editText.setEnabled(false);
                    } else if (latLng != null && tableState == TableState.READING) {
                        mMapPresenter.setReadOnly(latLng, finalAddress);
                        EditText editText = (EditText) valueView.findViewById(R.id.et_);
                        editText.setEnabled(false);
                    } else if (tableState == TableState.REEDITNG) {
                        mMapPresenter.setReEdit(latLng, finalAddress);
                    }
                }


                //判断是否有x,y坐标
                View x = getTableItemViewByFiled1("X");
                if (x != null) {
                    mapTableItem.addTableItemToMapItem(x);
                }

                View y = getTableItemViewByFiled1("Y");
                if (y != null) {
                    mapTableItem.addTableItemToMapItem(y);
                }
            }
        });

        if (tableState == TableState.REEDITNG && geometry != null) {
            ((EditMapItemPresenter) mMapPresenter).setReEditting(new Graphic(geometry, null, null), finalAddress);
        } else if (tableState == TableState.READING && geometry != null) {
            ((EditMapItemPresenter) mMapPresenter).setReading(new Graphic(geometry, null, null), finalAddress);
        } else if (tableState == TableState.EDITING && geometry != null) {
            ((EditMapItemPresenter) mMapPresenter).setReEditting(new Graphic(geometry, null, null), "");
        }

        (mMapPresenter).setOnEditMapFeatureCompleteCallback(new OnEditMapFeatureCompleteCallback<Geometry>() {
            @Override
            public void onFinished(Geometry data, DetailAddress detailAddress) {
                geometry = data;
                //如果是点，填充X,Y
                if (data != null && data.getType() == Geometry.Type.POINT) {
                    Point point = (Point) data;
                    if (getEditTextViewByField1Type("X") != null && getEditTextViewByField1Type("Y") != null) {
                        getEditTextViewByField1Type("X").setText(point.getX() + "");
                        getEditTextViewByField1Type("Y").setText(point.getY() + "");
                    }
                }
                /***************************广州排水设施自动填充内容*********************************/
                if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                    //行政区域
                    setTableItemValue("district", detailAddress.getDistrict());
                    //所在道路
                    setTableItemValue("lane_way", detailAddress.getStreet());
                    setTableItemValue("jdmc", detailAddress.getStreet());
                }
            }
        });

        //如果有X,Y就是点，否则就是线
        if (getTableItemByField1("X") != null && getTableItemByField1("Y") != null) {
            (mMapPresenter).setEditMode(EditModeConstant.EDIT_POINT);
        } else {
            (mMapPresenter).setEditMode(EditModeConstant.EDIT_LINE);
        }

    }

    /**
     * 添加一个不带地图控件的选点控件
     *
     * @param tableItem
     */
    private void addSelectLocationView(final TableItem tableItem) {
//        final ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_map_container_item, null);
//        final ViewGroup mapView = (ViewGroup) view.findViewById(R.id.ll_map);
//
//        selectLocationTableItem2 = new SelectLocationTableItem2(mContext, mapView);
//        final EditMapItemPresenter editMapItemPresenter = new EditMapItemPresenter(mContext, new SelectLocationService(mContext, null),
//                selectLocationTableItem2);
//        editMapItemPresenter.setOnEditMapFeatureCompleteCallback(new OnEditMapFeatureCompleteCallback<Geometry>() {
//            @Override
//            public void onFinished(Geometry data, DetailAddress detailAddress) {
//                geometry = data;
//                //如果是点，填充X,Y
//                if (data != null && data.getType() == Geometry.Type.POINT) {
//                    Point point = (Point) data;
//                    if (getEditTextViewByField1Type("X") != null && getEditTextViewByField1Type("Y") != null) {
//                        getEditTextViewByField1Type("X").setText(point.getX() + "");
//                        getEditTextViewByField1Type("Y").setText(point.getY() + "");
//                    }
//                }
//
//                /***************************广州排水设施自动填充内容*********************************/
//                if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
//                    //行政区域
//                    setTableItemValue("district", detailAddress.getDistrict());
//                    //所在道路
//                    setTableItemValue("lane_way", detailAddress.getStreet());
//                    setTableItemValue("jdmc", detailAddress.getStreet());
//                }
//
//            }
//        });
//        editMapItemPresenter.showMapShortCut();
//        //一进来默认定位（高德地图做法）
//        editMapItemPresenter.startLocate();
//        mainView.addView(view);
//        String address = null; //详细地址
//
//        final ViewGroup valueView = (ViewGroup) editMapItemPresenter.getView().getRootView();
//        resetSpecialReqiredTag(ControlType.MAP_PICKER, valueView, tableItem);
//        if (tableItem != null) {
//            map.put(tableItem.getId(), valueView);
//            if (tableItem.getValue() != null) {
//                EditText editText = (EditText) valueView.findViewById(R.id.et_);
//                editText.setText(tableItem.getValue());
//                address = tableItem.getValue();
//            }
//        }
//        final String finalAddress = address;
//        setLoadListener(new TableLoadListener() {
//            @Override
//            public void onFinishedLoad() {
//                if (tableState == TableState.READING || tableState == TableState.REEDITNG) { //xcl 2017-04-05 加入再次编辑模式
//                    LatLng latLng = getLatLng();
//                    if (latLng == null && tableState == TableState.READING) { //如果是阅读模式下经纬度为空，那么不显示地图
//                        editMapItemPresenter.getView().setMapInvisible();
//                        EditText editText = (EditText) valueView.findViewById(R.id.et_);
//                        editText.setEnabled(false);
//                    } else if (latLng != null && tableState == TableState.READING) {
//                        editMapItemPresenter.setReadOnly(latLng, finalAddress);
//                        EditText editText = (EditText) valueView.findViewById(R.id.et_);
//                        editText.setEnabled(false);
//                        if (graphic != null) {
//                            editMapItemPresenter.setReading(graphic, finalAddress);
//                        }
//                    } else if (tableState == TableState.REEDITNG) {
//                        editMapItemPresenter.setReEdit(latLng, finalAddress);
//                        if (graphic != null) {
//                            editMapItemPresenter.setReEditting(graphic, finalAddress);
//                        }
//                    }
//                }
//
//                //判断是否有x,y坐标
//                View x = getTableItemViewByFiled1("X");
//                if (x != null) {
//                    selectLocationTableItem2.addTableItemToMapItem(x);
//                }
//
//                View y = getTableItemViewByFiled1("Y");
//                if (y != null) {
//                    selectLocationTableItem2.addTableItemToMapItem(y);
//                }
//
//                /**
//                 * 覆盖原有点击事件
//                 */
//                if (mapButtonClickListener != null) {
//                    selectLocationTableItem2.setOnButtonClickListener(mapButtonClickListener);
//                }
//            }
//        });
//
//        //如果有X,Y就是点，否则就是线
//        if (getTableItemByField1("X") != null && getTableItemByField1("Y") != null) {
//            editMapItemPresenter.setEditMode(EditModeConstant.EDIT_POINT);
//        } else {
//            editMapItemPresenter.setEditMode(EditModeConstant.EDIT_LINE);
//        }


        final ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_map_container_item, null);
        final ViewGroup mapContainer = (ViewGroup) view.findViewById(R.id.ll_map);

        ISelectLocationTableItemView tableItemView = null;


        /**
         * 编辑模式
         */
        if (tableState == TableState.EDITING) {
            if (this.iSelectLocationTableItemView != null) {
                tableItemView = iSelectLocationTableItemView;
                iSelectLocationTableItemView = null;
            } else {
                tableItemView = new NoMapSelectLocationEditStateTableItemView(mContext);

            }
            selectLocationTableItemPresenter = new SelectLocationEditTableItemPresenter(mContext,
                    new SelectLocationService(mContext, null), tableItemView);

        } else if (tableState == TableState.REEDITNG) {
            /**
             * 再次编辑模式,在这里获取不到经纬度的，必须等全部加载完成
             */
            if (this.iSelectLocationTableItemView != null) {
                tableItemView = iSelectLocationTableItemView;
                iSelectLocationTableItemView = null;
            } else {
                tableItemView = new NoMapSelectLocationEditStateTableItemView(mContext);

            }
            selectLocationTableItemPresenter = new SelectLocationReEditStatePresenter(mContext,
                    new SelectLocationService(mContext, null),
                    tableItemView,
                    null,
                    tableItem.getValue());
        } else if (tableState == TableState.READING) {
            /**
             * 只读模式,在这里获取不到经纬度的，必须等全部加载完成
             */
            tableItemView = new NoMapSelectLocationReadStateTableItemView(mContext);
            selectLocationTableItemPresenter = new SelectLocationReadStatePresenter(mContext,
                    new SelectLocationService(mContext, null),
                    tableItemView,
                    null,
                    tableItem.getValue());
            tableItemView.hideSelectLocationButton();
        }

        //如果有X,Y就是点，否则就是线
        if (getTableItemByField1("X") != null && getTableItemByField1("Y") != null) {
            selectLocationTableItemPresenter.setEditMode(EditModeConstant.EDIT_POINT);
        } else {
            selectLocationTableItemPresenter.setEditMode(EditModeConstant.EDIT_LINE);
        }

        selectLocationTableItemPresenter.addTo(mapContainer);
        mainView.addView(view);

        selectLocationTableItemPresenter.setOnReceivedSelectedLocationListener(new IReceivedSelectLocationListener() {
            @Override
            public void onReceivedLocation(Geometry selectedGeometry, DetailAddress detailAddress) {

                geometry = selectedGeometry;

                if (selectedGeometry instanceof Point) {

                    if (getEditTextViewByField1Type("X") != null && getEditTextViewByField1Type("Y") != null) {
                        //经度
                        getEditTextViewByField1Type("X").setText(String.valueOf(((Point) selectedGeometry).getX()));
                        //纬度
                        getEditTextViewByField1Type("Y").setText(String.valueOf(((Point) selectedGeometry).getY()));
                    }


                    /***************************广州排水设施自动填充内容*********************************/
                    if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                        //行政区域
                        setTableItemValue("district", detailAddress.getDistrict());
                        //所在道路
                        setTableItemValue("lane_way", detailAddress.getStreet());
                        setTableItemValue("jdmc", detailAddress.getStreet());
                    }


                    //如果设施控件不为空，自动查找附近设施
                    if (mPatrolSelectDevicePresenter != null && selectLocationTableItemPresenter != null && selectLocationTableItemPresenter.getView().getMapView() != null) {
                        mPatrolSelectDevicePresenter.searchNearByDevice(selectLocationTableItemPresenter.getView().getMapView(), ((Point) selectedGeometry).getX(), ((Point) selectedGeometry).getY());
                    }
                }

            }
        });


        setLoadListener(new TableLoadListener() {
            @Override
            public void onFinishedLoad() {

                if (tableState == TableState.REEDITNG || tableState == TableState.EDITING) {
                    if (iSelectLocationTableItemView != null) {
                        selectLocationTableItemPresenter.setView(iSelectLocationTableItemView);
                    }
                }


                if (tableState == TableState.READING || tableState == TableState.REEDITNG) {
                    LatLng latLng = getLatLng();
                    if (latLng != null){
                        Point geometry = new Point(latLng.getLongitude(), latLng.getLatitude());
                        selectLocationTableItemPresenter.setGeometry(geometry);
                    }

                }
                /**
                 * 阅读模式下，是否显示『查看位置』按钮,默认显示
                 */
                if (tableState == TableState.READING) {
                    if (dontShowCheckLocationButton && selectLocationTableItemPresenter.getView() instanceof NoMapSelectLocationEditStateTableItemView) {
                        ((NoMapSelectLocationEditStateTableItemView) selectLocationTableItemPresenter.getView()).hideCheckLocationButton();
                    }
                }

                //当全部加载完成后才进行加载地图，可以提高加载表单的速度
                selectLocationTableItemPresenter.loadMap();
                //一进来默认定位（高德地图做法）
                //selectLocationTableItemPresenter.startLocate();

                //判断是否有x,y坐标
                View x = getTableItemViewByFiled1("X");
                if (x != null) {
                    selectLocationTableItemPresenter.getView().addView(x);
                }

                View y = getTableItemViewByFiled1("Y");
                if (y != null) {
                    selectLocationTableItemPresenter.getView().addView(y);
                }


                /**
                 * 覆盖原有点击事件
                 */
                if (mapButtonClickListener != null && selectLocationTableItemPresenter.getView() instanceof NoMapSelectLocationEditStateTableItemView) {
                    ((NoMapSelectLocationEditStateTableItemView) selectLocationTableItemPresenter.getView())
                            .setOnButtonClickListener(mapButtonClickListener);
                }
            }
        });

        final ViewGroup valueView = (ViewGroup) selectLocationTableItemPresenter.getView().getRootView();
        resetSpecialReqiredTag(ControlType.MAP_PICKER, valueView, tableItem);
        if (tableItem != null) {
            map.put(tableItem.getId(), valueView);
        }

    }

    /**
     * 填充tableItem
     *
     * @param field1
     * @param expectedValue
     */
    private void setTableItemValue(String field1, String expectedValue) {
        if (TextUtils.isEmpty(expectedValue)) {
            return;
        }
        TableItem district = getTableItemByField1(field1);
        if (district != null) {
            if (ControlType.SPINNER.equals(district.getControl_type())) {
                View view = getViewByField1(district.getId());
                if (view != null) {
                    Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                    changeSpinnerValue(spinner, expectedValue);
                }

            } else if (ControlType.TEXT_FIELD.equals(district.getControl_type())) {
                View view = getViewByField1(district.getId());
                if (view != null) {
                    EditText editText = (EditText) view.findViewById(R.id.et_);
                    editText.setText(expectedValue);
                }
            }
        }

    }

    /**
     * 重新设置地图控件『重新选择位置』点击事件
     *
     * @param onMapItemClickListener
     */
    public void setOnMapItemClickListener(final View.OnClickListener onMapItemClickListener) {
        this.mapButtonClickListener = onMapItemClickListener;
        if (selectLocationTableItemPresenter.getView() instanceof NoMapSelectLocationEditStateTableItemView) {
            ((NoMapSelectLocationEditStateTableItemView) selectLocationTableItemPresenter.getView())
                    .setOnButtonClickListener(mapButtonClickListener);
        }
        //todo 等稳定下来把这两句话删除
        if (selectLocationTableItem2 != null) {
            selectLocationTableItem2.setOnButtonClickListener(onMapItemClickListener);
        }

    }

    public void changeSelectLocationItemView(ISelectLocationTableItemView selectLocationTableItemView) {
        if (selectLocationTableItemPresenter != null) {
            selectLocationTableItemPresenter.setView(selectLocationTableItemView);
        } else {
            this.iSelectLocationTableItemView = selectLocationTableItemView;
        }
    }


    /**
     * 不要显示『查看位置』按钮
     */
    public void setDontShowCheckLocationButton() {
        dontShowCheckLocationButton = true;
        /**
         * 阅读模式下，是否显示『查看位置』按钮,默认显示
         */
        if (tableState == TableState.READING) {
            if (selectLocationTableItemPresenter != null &&
                    selectLocationTableItemPresenter.getView() instanceof NoMapSelectLocationEditStateTableItemView) {
                ((NoMapSelectLocationEditStateTableItemView) selectLocationTableItemPresenter.getView()).hideCheckLocationButton();
            }
        }
    }








    /**
     * 设置地址
     *
     * @param address
     */
    public void setAddress(String address) {
        if (selectLocationTableItemPresenter != null) {
            selectLocationTableItemPresenter.setAddress(address);
        }
        //todo 等稳定下来把这两句话删除
        if (selectLocationTableItem2 != null) {
            selectLocationTableItem2.showEditableAddress(address);
        }

    }

    /**
     * 改变下拉默认值
     *
     * @param spinner
     * @param expectedValue
     */
    public void changeSpinnerValue(Spinner spinner, String expectedValue) {
        for (int i = 0; i < spinner.getAdapter().getCount(); ++i) {
            String value = (String) spinner.getAdapter().getItem(i);
            if (!StringUtil.isEmpty(expectedValue)) {
                if (value.equals(expectedValue)) {
                    spinner.setSelection(i);
                } else {
                    TableDBService tableDBService = new TableDBService(mContext);
                    List<DictionaryItem> dictionaryByCode = tableDBService.getDictionaryByCode(expectedValue);
                    if (dictionaryByCode.size() > 0) {
                        if (dictionaryByCode.get(0).getName().equals(value)) {
                            spinner.setSelection(i);
                        }
                    }
                }
            }
        }
    }

    /**
     * 增加WebView地图选点表格项View
     */
    public void addWebViewMapView() {
        ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_map_container_item, null);
        final ViewGroup mapView = (ViewGroup) view.findViewById(R.id.ll_map);
        final WebViewMapTableItem webViewMapTableItem = new WebViewMapTableItem(mContext, mapView);
        mMapPresenter = new MapTableItemPresenter(mContext, new SelectLocationService(mContext, null),
                webViewMapTableItem);
        mMapPresenter.setOnReceivedSelectedLocationListener(new IMapTableItemPresenter.OnReceivedSelectedLocationListener() {
            @Override
            public void onReceivedLocation(final LatLng mapLatlng, String address) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getEditTextViewByField1Type("X") != null && getEditTextViewByField1Type("Y") != null) {
                            getEditTextViewByField1Type("X").setText(mapLatlng.getLongitude() + "");
                            getEditTextViewByField1Type("Y").setText(mapLatlng.getLatitude() + "");
                        }
                    }
                });
            }
        });
        mMapPresenter.showMapShortCut();
        mainView.addView(view);
        String address = null; //详细地址
        TableItem tableItem = getTableItemByType(ControlType.WEB_MAP_PICKER);
        final ViewGroup valueView = (ViewGroup) mMapPresenter.getView().getRootView();
        resetSpecialReqiredTag(ControlType.WEB_MAP_PICKER, valueView, tableItem);
        if (tableItem != null) {
            map.put(tableItem.getId(), valueView);
            if (tableItem.getValue() != null) {
                EditText editText = (EditText) valueView.findViewById(R.id.et_);
                editText.setText(tableItem.getValue());
                address = tableItem.getValue();
            }
        }
        final String finalAddress = address;
        setLoadListener(new TableLoadListener() {
            @Override
            public void onFinishedLoad() {
                if (tableState == TableState.READING || tableState == TableState.REEDITNG) { //xcl 2017-04-05 加入再次编辑模式
                    LatLng latLng = getLatLng();
                    if (latLng == null && tableState == TableState.READING) { //如果是阅读模式下经纬度为空，那么不显示地图
                        mMapPresenter.getView().setMapInvisible();
                        EditText editText = (EditText) valueView.findViewById(R.id.et_);
                        editText.setEnabled(false);
                    } else if (latLng != null && tableState == TableState.READING) {

                        mMapPresenter.setReadOnly(latLng, finalAddress);
                        EditText editText = (EditText) valueView.findViewById(R.id.et_);
                        editText.setEnabled(false);
                    } else if (tableState == TableState.REEDITNG) {
                        mMapPresenter.setReEdit(latLng, finalAddress);
                    }

                    //判断是否有x,y坐标
                    View x = getTableItemViewByFiled1("X");
                    if (x != null) {
                        webViewMapTableItem.addTableItemToMapItem(x);
                    }

                    View y = getTableItemViewByFiled1("Y");
                    if (y != null) {
                        webViewMapTableItem.addTableItemToMapItem(y);
                    }
                }
            }
        });

    }

    private void addPatrolProgress(TableItem tableItem) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_patrol_progress_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tv);
        textView.setText(tableItem.getHtml_name());
        TextView textView2 = (TextView) view.findViewById(R.id.tv2);
        textView2.setText(tableItem.getValue());
        map.put(tableItem.getId(), view);
        addViewToContainer(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (patrolProgressClickListener != null) {
                    patrolProgressClickListener.onCallback(null);
                }
            }
        });
    }

    public void setPatrolProgressClickListener(Callback1 callback) {
        this.patrolProgressClickListener = callback;
    }

    private void addOpinionTemplateEditView(TableItem tableItem) {

       /* View view = View.inflate(mContext,
                R.layout.table_container_for_items, null);
        ViewGroup container = (ViewGroup) view.
                findViewById(R.id.ll_container_for_other_Items_exclude_map_and_photos);*/

        IOpinionTemplateView opinionTemplateView = new OpinionTemplateView(mContext, mainView, tableItem);
        IOpinionTemplatePresenter opinionTemplatePresenter = new OpinionTemplatePresenter(mContext, opinionTemplateView, this);
        map.put(tableItem.getId(), opinionTemplateView.getView());

        // mainView.addView(view);
        //        mainView.removeView(opinionTemplateView.getView());
        //        mainView.addView(opinionTemplateView.getView());
    }

    /**
     * 获取经度纬度
     *
     * @return
     */
    private LatLng getLatLng() {
        // mMapPresenter.getView().setMapInvisible();
        //进行显示地图
        if (getEditTextViewByField1Type("X") != null && getEditTextViewByField1Type("Y") != null) {
            String longitude = getEditTextViewByField1Type("X").getText().toString();
            String latitude = getEditTextViewByField1Type("Y").getText().toString();
            if (TextUtils.isEmpty(longitude) || TextUtils.isEmpty(latitude)){
                return null;
            }
            if (!TextUtils.isEmpty(longitude) && !TextUtils.isEmpty(latitude)) {
                return new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            }
        }
        return null;
    }

    /**
     * 是否显示特殊表格模板项
     *
     * @param controlType
     * @return
     */
    public boolean isShowSpecilTableItem(String controlType) {
        boolean isShow = false;
        if (tableItems != null) {
            for (TableItem tableItem : tableItems) {
                if (tableItem.getControl_type() != null && tableItem.getIf_hidden() != null) {
                    if (tableItem.getControl_type().equals(controlType)) {
                        if (tableItem.getIf_hidden().equals(ControlState.VISIBLE)) {
                            isShow = true;
                        }
                    }
                }
            }
        }
        return isShow;
    }

    /**
     * 根据类型获得特殊表格模板项
     *
     * @param controlType
     * @return
     */
    public TableItem getSpecialTableItem(String controlType) {
        TableItem tableItem = null;
        for (TableItem tmp : tableItems) {
            if (tmp.getControl_type() != null) {
                if (tmp.getControl_type().equals(controlType)) {
                    tableItem = tmp;
                    break;
                }
            }
        }
        return tableItem;
    }

    /**
     * 判断特殊表格模板项是否为必填项
     *
     * @param controlType
     * @return
     */
    public boolean isSpecialTableItemRequire(String controlType) {
        boolean reqired = false;
        TableItem tableItem = getSpecialTableItem(controlType);
        if (tableItem != null) {
            if (tableItem.getIf_required() != null) {
                if (tableItem.getIf_required().equals(RequireState.REQUIRE)) {
                    reqired = true;
                }
            }
        }
        return reqired;
    }

    /**
     * 重置必填项标志显示
     *
     * @param controlType
     * @param viewGroup
     * @param tableItem
     */
    public void resetSpecialReqiredTag(String controlType, ViewGroup viewGroup, TableItem tableItem) {
        TextView textView = (TextView) viewGroup.findViewById(R.id.tv_requiredTag);
        if (textView != null) {
            textView.setVisibility(View.INVISIBLE);
            if (tableState != TableState.READING) {
                if (tableItem.getIf_required() != null) {
                    if (tableItem.getIf_required().equals(RequireState.REQUIRE)) {
                        textView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    /**
     * 通过控件类型获取表格项
     * 注:可能存在多值,模式只取第一个值
     *
     * @param controlType
     * @return
     */
    public TableItem getTableItemByType(String controlType) {
        TableItem tableItem = null;
        if (tableItems != null) {
            for (TableItem temp : tableItems) {
                if (temp.getControl_type() != null) {
                    if (temp.getControl_type().equals(controlType)) {
                        tableItem = temp;
                        break;
                    }
                }
            }
        }
        return tableItem;
    }

    /**
     * 根据 Field1字段获取表格模板项
     *
     * @param field1
     * @return
     */
    public TableItem getTableItemByField1(String field1) {
        TableItem tableItem = null;
        if (tableItems != null) {
            for (TableItem temp : tableItems) {
                if (temp.getField1() != null) {
                    if (temp.getField1().equals(field1)) {
                        tableItem = temp;
                        break;
                    }
                }
            }
        }
        return tableItem;
    }

    /**
     * 通过HtmlName 字段属性来获取TableItem
     * <p>
     * 注: HtmlName 即为显示在界面上的模板项提示文字
     *
     * @param htmlName
     * @return
     */
    public TableItem getTableItemByHtmlName(String htmlName) {
        //        getValueMap();
        TableItem tableItem = null;
        if (tableItems != null) {
            for (TableItem temp : tableItems) {
                if (temp.getHtml_name() != null) {
                    if (temp.getHtml_name().equals(htmlName)) {
                        tableItem = temp;
                        break;
                    }
                }
            }
        }
        if (tableItem == null) {
            return null;
        }
        View view = map.get(tableItem.getId());
        //此处用隐藏的EditText来存储具体的填写值
        EditText editText = null;
        if (view != null) {
            editText = (EditText) view.findViewById(R.id.et_);
        }
        if (editText != null) {
            if (editText.getText() != null) {
                String value = editText.getText().toString();
                tableItem.setValue(value);
            }
        }
        return tableItem;
    }

    public View getViewByField1(String id) {
        View view = map.get(id);
        return view;
    }

    /**
     * 获取跟TableItem填写值绑定在一起的EditText
     *
     * @param field1
     * @return
     */
    public EditText getTableItemEditTextByFiled(String field1) {
        EditText editText = null;
        View view = getTableItemViewByFiled1(field1);
        //此处用隐藏的EditText来存储具体的填写值
        if (view != null) {
            editText = (EditText) view.findViewById(R.id.et_);
        }
        return editText;
    }

    /**
     * 根据Field1字段获取跟TableItem绑定在一起的View
     *
     * @param field1
     * @return
     */
    public View getTableItemViewByFiled1(String field1) {
        View itemview = null;
        if (map != null) {
            for (Map.Entry<String, View> item : map.entrySet()) {
                String id = item.getKey();
                if (TextUtils.isEmpty(id)) {
                    continue;
                }
                TableItem tableItem = getTableItemById(id);
                if (tableItem.getField1().equals(field1)) {
                    itemview = item.getValue();
                    break;
                }
            }
        }
        return itemview;
    }

    /**
     * 根据ID获取对应的TableItem
     *
     * @param id
     * @return
     */
    public TableItem getTableItemById(String id) {
        TableItem tableItem = null;
        for (TableItem item : tableItems) {
            if (id.equals(item.getId())) {
                tableItem = item;
                break;
            }
        }
        return tableItem;
    }

    /**
     * 初始化数据表格模板项列表数据
     */
    public void initDataAndView() {

        //编辑模式下tableItems值为null
        if (tableItems == null) {
            tableItems = new ArrayList<>();
        }
        map = new HashMap<>();

        mPhotoViewMap = new HashMap<>();
        mPhotoViewAdapterMap = new HashMap<>();

        //编辑模式
        if (tableState == TableState.EDITING) {
            //先从数据库获取本地缓存
            /*
            List<ProjectTable> tables = tableDataManager.getProjectTableFromDB(projectId);
            if(tables.size() > 0){
                tableItems = tables.get(0).getTableItems();
                initSpecialControlViewBefore();
                initControlView();
                isUpdateFromNet = false;
            }
            */

            //先从网络获取数据
            if (isUpdateFromNet) {
                final ProgressDialog progressDialog = ProgressDialog.show(mContext, "提示", "正在更新数据中");
                tableDataManager.getTableItemsFromNet(projectId, url, new TableNetCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        TableItems tmp = (TableItems) data;
                        progressDialog.dismiss();
                        if (tmp.getResult() != null) {
                            tableItems = tmp.getResult();
                            //   tableDataManager.setTableItemsToDB(tableItems);
                            //缓存在数据库中
                            Realm realm = Realm.getDefaultInstance();
                            ProjectTable projectTable = new ProjectTable();
                            projectTable.setId(projectId);
                            realm.beginTransaction();
                            projectTable.setTableItems(new RealmList<TableItem>(tableItems.toArray(new TableItem[tableItems.size()])));
                            realm.commitTransaction();
                            tableDataManager.setProjectTableToDB(projectTable);
                            initControlView();
                        } else {
                            //没有网络数据再读取缓存
                            ToastUtil.shortToast(mContext, "读取本地缓存表单模板数据!");
                            tableItems = getLocalTableItems();
                            if (tableItems != null && tableItems.size() > 0) {
                                initControlView();
                            }
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        progressDialog.dismiss();
                        ToastUtil.shortToast(mContext, "读取本地缓存表单模板数据!");
                        tableItems = getLocalTableItems();
                        if (tableItems != null && tableItems.size() > 0) {
                            initControlView();
                        }
                    }
                });
            }
        } else if (tableState == TableState.READING) {
            //阅读模式
            if (tableItems != null) {
                initControlView();
            }
        } else if (tableState == TableState.REEDITNG) {
            //再次编辑模式
            if (tableItems != null) {
                initControlView();
            }
        }
    }

    //从数据库获取本地缓存表格模板项列表
    public List<TableItem> getLocalTableItems() {
        List<TableItem> tableItems = null;
        //先从数据库获取本地缓存
        List<ProjectTable> tables = tableDataManager.getProjectTableFromDB(projectId);
        if (tables.size() > 0) {
            tableItems = tables.get(0).getTableItems();
            initControlView();
            isUpdateFromNet = false;
        }
        return tableItems;
    }

    /**
     * 添加所有的表格项View到界面
     */
    public void initControlView() {
        sortTableItem();
        for (int i = 0; i < tableItems.size(); i++) {
            if (tableItems.get(i).getIf_hidden() != null) {
                //    if (tableItems.get(i).getIf_hidden().equals(ControlState.VISIBLE)) {
                addTableItemView(tableItems.get(i));
                //       }
            }
        }
        String json = new Gson().toJson(tableItems);

        //表格加载完毕
        if (listener != null) {
            listener.onFinishedLoad();
        }
        if (listeners != null && !listeners.isEmpty()) {
            for (TableLoadListener loadListener : listeners) {
                loadListener.onFinishedLoad();
            }
        }

        //测试 后续去掉
        // addQRScanBtn(null);
    }

    /**
     * 排序模板表格项
     */
    public void sortTableItem() {
        if (tableItems != null) {
            /*for (int i = 0; i < tableItems.size(); i++) {
                if (tableItems.get(i).getDisplay_order() == 0) {
                    //没有配置该字段的默认为1000000,即将在最后显示
                    tableItems.get(i).setDisplay_order(1000000);
                }
            }*/

            //进行升序排序
            Collections.sort(tableItems, new Comparator<TableItem>() {
                @Override
                public int compare(TableItem tableItem, TableItem t1) {
                    return (tableItem.getDisplay_order() - t1.getDisplay_order());
                }
            });

        }
    }

    /**
     * 添加具体模板表格项View
     * (常规模板表格项)
     *
     * @param tableItem
     */
    public void addTableItemView(final TableItem tableItem) {
        if (tableItem.getControl_type() == null) return;
        initContainerForItemsOtherThanMapAndPhotos();
        if (tableItem.getControl_type().equals(ControlType.TEXT_FIELD)) { //一般文本输入
            addTextItem(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.DATE)) {  //日期输入
            addDateItem(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.SPINNER)) { //下拉输入
            getSpinnerItemData(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.CHEXK_BOX)) {
            // getCheckItemData(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.TEXT_AREA)) { //文本域输入
            addTextAreaItem(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.AUTO_COMPLETE_LOCAL)) { //本地模糊匹配
            addAutoCompleteLocal(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.AUTO_COMPLETE_NET)) { //在线模糊匹配
            addAutoCompleteNet(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.IMAGE_PICKER)) { //图片选择控件
            if (tableItem.getIf_hidden() != null && tableItem.getIf_hidden().equals(ControlState.VISIBLE)) {
                // initHorizontalScrollPhotoView(tableItem);
                addPhotoViewItem(tableItem);
            }
        } else if (tableItem.getControl_type().equals(ControlType.MAP_PICKER)) { //地图控件
            if (tableItem.getIf_hidden() != null && tableItem.getIf_hidden().equals(ControlState.VISIBLE)) {
                addMapView(tableItem);
                //addEditMapFeatureView();
            }
        } else if (tableItem.getControl_type().equals(ControlType.WEB_MAP_PICKER)) { //webview地图控件
            if (tableItem.getIf_hidden() != null && tableItem.getIf_hidden().equals(ControlState.VISIBLE)) {
                addWebViewMapView();
                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
                sharedPreferencesUtil.setString("firstUrl", tableItem.getFirst_correlation());
                sharedPreferencesUtil.setString("twoUrl", tableItem.getThree_correlation());
            }
        } else if (tableItem.getControl_type().equals(ControlType.PATROL_PROGRESS)) {  //工单进度
            if (tableItem.getIf_hidden() != null && tableItem.getIf_hidden().equals(ControlState.VISIBLE)) {
                addPatrolProgress(tableItem);
            }
        } else if (tableItem.getControl_type().equals(ControlType.OPINION_TEMPLATE)) {  //意见模板
            if (tableItem.getIf_hidden() != null && tableItem.getIf_hidden().equals(ControlState.VISIBLE)) {
                if (tableState == TableState.EDITING
                        || tableState == TableState.REEDITNG) {
                    addOpinionTemplateEditView(tableItem);
                } else {
                    addTextItem(tableItem);
                }
            }
        } else if (tableItem.getControl_type().equals(ControlType.CUSSTOM)) { //其他用户自定义控件
            if (mAddCustomTableListener != null) {
                mAddCustomTableListener.addCustomTableItems(tableItem, this);
            }
        } else if (tableItem.getControl_type().equals(ControlType.PHOTO_IDENTIFY_TEXT_FIELD)) {//身份证识别输入
            addTextItemForIdentify(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.QR_SCAN_BTN)) { //二维码扫描
            //            addQRScanBtn(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.H5_HTML)) {   // h5详情控件
            addH5Html(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.EDIT_MAP_FEATURE)) { //地图编辑控件
            addEditMapFeatureView();
        } else if (tableItem.getControl_type().equals(ControlType.RECEIVE_MAP_GEOMETRY)) { //跳到地图选位置控件
            addSelectLocationView(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.ABSOLUTE_IMAGE_PICKER)) { //绝对路径图片控件
            addPhotoViewItem(tableItem);
        }

        //当前项是否为设施项
        if ("NAME".equals(tableItem.getField1())) {
            boolean ifShowDevicIdView = false;
            boolean ifShowDeviceNameView = true;
            addDeviceView(ifShowDevicIdView, ifShowDeviceNameView);
        }
    }

    /**
     * 根据param刷新表单的值
     *
     * @param param
     */
    public void initScanValue(Map<String, String> param) {
        for (TableItem item : tableItems) {
            if (item.getHtml_name().equals("采集单位") && param.containsKey("名称")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("名称"));
                }
            }

            if (item.getHtml_name().equals("法人姓名") && param.containsKey("法定代表人")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("法定代表人"));
                }
            }

            if (item.getHtml_name().equals("经营范围") && param.containsKey("经营范围")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("经营范围"));
                }
            }

            if (item.getHtml_name().equals("注册地址") && param.containsKey("住所")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("住所"));
                }
            }
            if (item.getField1().equals("name") && param.containsKey("\uFEFF名称")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("\uFEFF名称"));
                }
            }
        }
    }

    /**
     * 添加图片视图
     *
     * @param item
     */
    private void addPhotoViewItem(final TableItem item) {
        if (tableState.equals(TableState.READING) && mPhotoListMap != null
                && ListUtil.isEmpty(mPhotoListMap.get(item.getField1()))) {   // 阅读模式且没有照片则返回
            return;
        }
        ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_horizontal_scroll_photo_item, null);
        TextView title = (TextView) view.findViewById(R.id.tv_);
        TextView tvTip = (TextView) view.findViewById(R.id.tv_tip);
        if (item.getHtml_name() != null) {
            title.setText(item.getHtml_name());
        }
        if (tableState.equals(TableState.READING)) {
            //            title.setText("查阅图片");
            tvTip.setVisibility(View.GONE);
        }
        TextView reqiredTag = (TextView) view.findViewById(R.id.tv_requiredTag);
        if (reqiredTag != null) reqiredTag.setVisibility(View.INVISIBLE);
        HorizontalScrollPhotoView photoView = (HorizontalScrollPhotoView) view.findViewById(R.id.horizontalScrollPhotoView);
        mPhotoViewMap.put(item.getField1(), photoView);
        if (mPhotoListMap == null)
            mPhotoListMap = new HashMap<>();
        if (mPhotoNameMap == null)
            mPhotoNameMap = new HashMap<>();

        List<Photo> photos = mPhotoListMap.get(item.getField1());
        if (photos == null) {
            photos = new ArrayList<Photo>();
            mPhotoListMap.put(item.getField1(), photos);
        }
        HorizontalScrollPhotoViewAdapter adapter = new HorizontalScrollPhotoViewAdapter(
                mContext, photoView, photos);
        mPhotoViewAdapterMap.put(item.getField1(), adapter);

        View openPhotoView = view.findViewById(R.id.action_open_photo);
        openPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPouupWindow(item.getField1());
            }
        });
        if (tableState.equals(TableState.READING)) {
            openPhotoView.setVisibility(View.INVISIBLE);
        }
        mainView.addView(view);
        refreshPhotoViewToFirst(item.getField1());
        //  if (tableState == TableState.REEDITNG || tableState == TableState.EDITING) {
        photoView.setOnItemClickListener(new HorizontalScrollPhotoView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                view.setBackgroundDrawable(mContext.getResources().getDrawable(
                        R.drawable.itme_background_checked));
                ArrayList<Photo> photos = (ArrayList<Photo>) mPhotoListMap.get(item.getField1());
                if (!ListUtil.isEmpty(photos)) {
                    Intent intent = new Intent(mContext, PhotoPagerActivity.class);
                    intent.putExtra("BITMAPLIST", photos);
                    intent.putExtra("POSITION", position);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, view, "shareTransition").toBundle());
                    } else {
                        mContext.startActivity(intent);
                    }
                    //                    ImageUtil.photoViewClick(mContext, photos, position, CommonViewPhotoActivity.class);
                }
            }
        });
        photoView.setCurrentImageChangeListener(new HorizontalScrollPhotoView.CurrentImageChangeListener() {
            @Override
            public void onCurrentImgChanged(int position, View viewIndicator) {
                if (viewIndicator != null) {
                    viewIndicator.setBackgroundDrawable(mContext.getResources().getDrawable(
                            R.drawable.itme_background_checked));
                }
            }
        });
        if (!tableState.equals(TableState.READING)) { //当阅读模式下，长按删除模式不启动
            photoView.setOnItemLongClickListener(new HorizontalScrollPhotoView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(View view, final int position) {
                    final List<Photo> photos = mPhotoListMap.get(item.getField1());
                    if (ListUtil.isEmpty(photos)) return true;
                    view.setBackgroundDrawable(mContext.getResources().getDrawable(
                            R.drawable.itme_background_checked));
                    ImageUtil.deleteImage(mContext,
                            photos.get(position).getLocalPath(),
                            new ImageUtil.OnDeletedPhotoListener() {
                                @Override
                                public void onDeletedPhoto() {
                                    tableDataManager.deletePhotoInDB(photos.get(position));
                                    if (mPhotoNameMap.containsKey(item.getField1())) {
                                        String allPhotoName = mPhotoNameMap.get(item.getField1());
                                        String deletePhotoName = photos.get(position).getPhotoName();
                                        if (allPhotoName != null) {
                                            allPhotoName = "";
                                            for (Photo photo : photos) {
                                                if (!photo.getPhotoName().equals(deletePhotoName)) {
                                                    allPhotoName = photo.getPhotoName() + "|" + allPhotoName;
                                                }
                                            }

                                            mPhotoNameMap.put(item.getField1(), allPhotoName);
                                        }
                                    }

                                    photos.remove(position);
                                    refreshPhotoViewToFirst(item.getField1());
                                    ToastUtil.shortToast(mContext, "删除成功!");
                                }
                            });

                    /*
                    if (mPhotoNameMap.containsKey(item.getField1())) {
                        String allPhotoName = mPhotoNameMap.get(item.getField1());
                        String deletePhotoName = photos.get(position).getPhotoName();
                        if (allPhotoName != null) {
                            allPhotoName = "";
                            for (Photo photo : photos) {
                                if (!photo.getPhotoName().equals(deletePhotoName)) {
                                    allPhotoName = photo.getPhotoName() + "|" + allPhotoName;
                                }
                            }

                            mPhotoNameMap.put(item.getField1(), allPhotoName);
                        }


                    }
                    */
                    return true;
                }
            });
        }

        //  }
        TableItem tableItem = getSpecialTableItem(ControlType.IMAGE_PICKER);
        if (tableItem != null) {
            resetSpecialReqiredTag(ControlType.IMAGE_PICKER, view, tableItem);
        }
    }

    /**
     * 初始化容器用于装载除了地图和图片选择控件外的item
     */
    private void initContainerForItemsOtherThanMapAndPhotos() {
        if (container_for_other_Items_exclude_map_and_photos == null) {
            container_for_other_Items_exclude_map_and_photos_root_view = View.inflate(mContext,
                    R.layout.table_container_for_items, null);
            container_for_other_Items_exclude_map_and_photos = (ViewGroup) container_for_other_Items_exclude_map_and_photos_root_view.
                    findViewById(R.id.ll_container_for_other_Items_exclude_map_and_photos);
        }
    }

    //添加在线模糊匹配
    public void addAutoCompleteNet(TableItem tableItem) {
        final String url = tableItem.getFirst_correlation();
        if (url == null) {
            ToastUtil.longToast(mContext, "模糊匹配的服务器地址为空!");
            return;
        }
        final ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_auto_complete_net_textview_item, null);
        final AutoCompleteTextView auto = (AutoCompleteTextView) view.findViewById(R.id.autotext);
        TextView textView = (TextView) view.findViewById(R.id.tv_);
        ImageButton btn_search = (ImageButton) view.findViewById(R.id.auto_btn_search);
        textView.setText(tableItem.getHtml_name());
        setRequireTagState(view, tableItem);
        auto.setThreshold(0);

        final EditText editText = (EditText) view.findViewById(R.id.et_);
        btn_search.setVisibility(View.GONE);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                String searshText = auto.getText().toString();

                if (searshText.isEmpty()) {
                    ToastUtil.longToast(mContext, "查询值为空!");
                    return;
                }

                fuzzySearch(url, searshText, getAutoCompleteOtherParams(), view);
            }
        });

        if (tableState == TableState.REEDITNG || tableState == TableState.EDITING) {
            //定时器,在AutoText输入文本改变后2秒进行网络请求模糊匹配
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    String searshText = auto.getText().toString();
                    if (searshText != null) {
                        fuzzySearch(url, searshText, getAutoCompleteOtherParams(), view);
                    }
                }
            };
            auto.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    handler.removeCallbacks(runnable);
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    handler.removeCallbacks(runnable);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    handler.postDelayed(runnable, 2000);
                    editText.setText(auto.getText());

                }
            });

            auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus) {
                        handler.removeCallbacks(runnable);
                        auto.dismissDropDown();
                    }
                }
            });
        }

        map.put(tableItem.getId(), view);
        if (this.tableState == TableState.READING) {
            view.findViewById(R.id.auto_btn_search).setVisibility(View.GONE);
        }
        addViewToContainer(view);
        setAutoCompleteNetValue(tableItem, view);
    }

    /**
     * 请重写该方法,传入自己想要的其他辅助查询的参数
     *
     * @return
     */
    public Map<String, String> getAutoCompleteOtherParams() {
        HashMap<String, String> map = new HashMap<>();
        // map.put("street","东环街道");
        //   map.put("village","甘棠村");
        return map;
    }

    public void fuzzySearch(String url, String keyWords, Map<String, String> params, View view) {
        final AutoCompleteTextView auto = (AutoCompleteTextView) view.findViewById(R.id.autotext);
        final EditText editText = (EditText) view.findViewById(R.id.et_);

        ToastUtil.shortToast(mContext, "查询中，请稍候...");
        FuzzySearchService service = new FuzzySearchService(mContext);
        service.fuzzySearch(url, keyWords, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Map<String, String>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.shortToast(mContext, "查询失败");
                    }

                    @Override
                    public void onNext(final List<Map<String, String>> maps) {
                        if (maps.isEmpty()) {
                            ToastUtil.shortToast(mContext, "没有查询到结果");
                            return;
                        }

                        final List<String> candidates = new ArrayList<String>();
                        for (Map<String, String> map : maps) {
                            candidates.add(map.get("name"));
                        }

                        ArrayAdapter adapter = new ArrayAdapter<>(mContext,
                                android.R.layout.simple_dropdown_item_1line, candidates);
                        auto.setAdapter(adapter);
                        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                ToastUtil.shortToast(mContext, candidates.get(i));
                                editText.setText(candidates.get(i));

                                //预留重写函数以进一步自定义确定要模糊匹配确定的目标值
                                prepareAutoCompleteNetUploadValue(editText, maps, candidates.get(i));
                            }
                        });
                        auto.showDropDown();

                    }
                });
    }

    /**
     * 通过重写此函数进行在线模糊匹配后上传值的处理
     * 例如:可以通过把 candidates 中的id 拿处理设置给 editText,即可完成模糊匹配后确定的id上传值
     *
     * @param editText 用来存储上传值
     * @param maps     模糊匹配后的键值map 列表
     * @param value    确定的值  此处用的是 键值Map中的 name 键来进一步确定文本的
     */
    public void prepareAutoCompleteNetUploadValue(EditText editText, List<Map<String, String>> maps, String value) {
        //示例如下
        /*
        String  id = null;
        for (Map<String, String> map : maps) {
            if(map.get("name").equals(value)){
                id = map.get("id");
                break;
            }
        }
        if(id != null) {
            editText.setText(id);
        }
        */
    }

    public void setAutoCompleteNetValue(TableItem tableItem, View view) {
        if (tableState == TableState.READING) {
            AutoCompleteTextView auto = (AutoCompleteTextView) view.findViewById(R.id.autotext);
            auto.setText(tableItem.getValue());
            auto.setEnabled(false);
        } else if (tableState == TableState.REEDITNG) {
            AutoCompleteTextView auto = (AutoCompleteTextView) view.findViewById(R.id.autotext);
            auto.setText(tableItem.getValue());

            EditText editText = (EditText) view.findViewById(R.id.et_);
            editText.setText(tableItem.getValue());
        }
    }

    //添加本地模糊匹配
    public void addAutoCompleteLocal(TableItem tableItem) {
        List<TableChildItem> tableChildItems = getTableChildItemFormDB(tableItem.getDic_code());
        String url = getTableChildUrl(tableItem.getDic_code());
        if (tableChildItems == null) return;
        ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_auto_complete_local_textview_item, null);
        AutoCompleteTextView auto = (AutoCompleteTextView) view.findViewById(R.id.autotext);
        auto.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        TextView textView = (TextView) view.findViewById(R.id.tv_);
        textView.setText(tableItem.getHtml_name());
        setRequireTagState(view, tableItem);
        map.put(tableItem.getId(), view);
        addViewToContainer(view);
        setAutoCompleteLocalValue(tableItem, view, tableChildItems);
    }

    public void addViewToContainer(ViewGroup view) {
        if (!ifAddedContainerForOtherItems) {
            mainView.addView(container_for_other_Items_exclude_map_and_photos_root_view);
            ifAddedContainerForOtherItems = true;
        }
        container_for_other_Items_exclude_map_and_photos.addView(view);
    }

    /**
     * 设置必填项显示状态
     *
     * @param view
     * @param tableItem
     */
    public void setRequireTagState(ViewGroup view, TableItem tableItem) {
        TextView tv_reqiredTag = (TextView) view.findViewById(R.id.tv_requiredTag);
        tv_reqiredTag.setVisibility(View.INVISIBLE);
        if (tableItem.getIf_required() != null) {
            if (tableState != TableState.READING) {
                if (tableItem.getIf_required().equals(RequireState.REQUIRE)) {
                    tv_reqiredTag.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void setAutoCompleteLocalValue(TableItem tableItem, View view, List<TableChildItem> tableChildItems) {
        final EditText editText = (EditText) view.findViewById(R.id.et_);
        if (tableState == TableState.READING) {
            AutoCompleteTextView auto = (AutoCompleteTextView) view.findViewById(R.id.autotext);
            auto.setText(tableItem.getValue());
            auto.setEnabled(false);
            if (tableItem.getValue() != null) {
                auto.setText(tableItem.getValue());
            }
        } else if (tableState == TableState.REEDITNG || tableState == TableState.EDITING) {
            // if (isUpdateFromNet) {

            if (tableState == TableState.REEDITNG) {
                AutoCompleteTextView auto = (AutoCompleteTextView) view.findViewById(R.id.autotext);
                auto.setText(tableItem.getValue());
                if (tableItem.getValue() != null) {
                    auto.setText(tableItem.getValue());
                    editText.setText(tableItem.getValue());
                }
            }
            final List<String> list = new ArrayList<>();
            for (TableChildItem tableChildItem : tableChildItems) {
                list.add(tableChildItem.getName());
            }
            // String[] arr = {"aa", "aab", "aac"};
            ArrayAdapter<String> arrayAdapter;
            arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list);

            final AutoCompleteTextView auto = ((AutoCompleteTextView) view.findViewById(R.id.autotext));
            auto.setThreshold(1);
            auto.setAdapter(arrayAdapter);
            auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    editText.setText(list.get(i));
                }
            });

            auto.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    editText.setText(auto.getText());
                }
            });

            // }
        }
    }

    /**
     * 添加文本域
     *
     * @param tableItem
     */
    public void addTextAreaItem(TableItem tableItem) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_textarea_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_);
        final TextView tv_size = (TextView) view.findViewById(R.id.tv_size);
        EditText editText = (EditText) view.findViewById(R.id.et_);
        int total = 0;
        if (!TextUtils.isEmpty(tableItem.getRow_num()) && !TextUtils.isEmpty(tableItem.getColum_num())) {
            total = Integer.valueOf(tableItem.getColum_num()) * Integer.valueOf(tableItem.getRow_num());
        }
        tv_size.setText("0/" + total);
        if (total == 0) {
            tv_size.setVisibility(View.GONE);
        } else {
            editText.setFilters(new InputFilter[]{new MaxLengthInputFilter(total,
                    null, editText, "长度不能超过" + total + "个字").setDismissErrorDelay(1500)});
        }
        textView.setText(tableItem.getHtml_name());
        final int finalTotal = total;
        editText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                try {
                    String inputText = s.toString();
                    if (TextUtils.isEmpty(inputText)) {
                        tv_size.setText("0/" + finalTotal);
                        return;
                    }
                    tv_size.setText(inputText.getBytes("GB2312").length / 2 + "/" + finalTotal);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        setRequireTagState(view, tableItem);
        map.put(tableItem.getId(), view);
        addViewToContainer(view);
        setTextAreaItemValue(tableItem, view);
        setTableItemState(tableItem, view);
    }

    public void setTextAreaItemValue(TableItem tableItem, View view) {
        if (tableState == TableState.READING || tableState == TableState.REEDITNG) {
            EditText editText = (EditText) view.findViewById(R.id.et_);
            if (tableItem.getValue() != null) {
                editText.setText(tableItem.getValue());
            }
            if (tableState == TableState.READING) {
                editText.setEnabled(false);
            } else if (tableState == TableState.REEDITNG) {
                editText.setEnabled(true);
            }
        }
    }

    public List<TableChildItem> getTableChildItemFormDB(String code) {
        List<TableChildItem> list = new ArrayList<>();
        list = tableDataManager.getTableChildItemsByTypeCode(code);
        return list;
    }

    //xcl 2017.9.3 根据包总意见去掉显示到秒，只显示到天
    public void addDateItem(final TableItem tableItem) {
        final ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_datefield_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_);
        EditText et = ((EditText) view.findViewById(R.id.et_));
        setRequireTagState(view, tableItem);
        final Button btn = (Button) view.findViewById(R.id.btn_datepicker);
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        month = cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        second = cal.get(Calendar.SECOND);

//        btn.setText(year + "-" + (month + 1) + "-" + day);//xcl 2017-03-20 默认显示当前时间

        if (tableItem.getHtml_name().contains("入学时间") || tableItem.getField1().toLowerCase().equals("time")) {
            btn.setText(year + "-" + 9 + "-" + 1); //如果是学生信息表，那么默认设置成9月1号
        }

        if (tableItem.getValue() != null) {
            try {
                Integer.valueOf(tableItem.getValue());
                btn.setText(tableItem.getValue());
            } catch (Exception e) {
                try {
                    String v = TimeUtil.getStringTimeYMD(new Date(Long.valueOf(tableItem.getValue())));
                    btn.setText(v);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        }

        et.setText(year + "-" + (month + 1) + "-" + day);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view_) {
                showTimePicker(btn, view, tableItem); //xcl 2017-04-05 修改时间选择方法，加入TimePicker
            }
        });
        textView.setText(tableItem.getHtml_name());
        map.put(tableItem.getId(), view);
        addViewToContainer(view);
        setDateItemValue(tableItem, view);
        setTableItemState(tableItem, view);

        if (tableItem.getHtml_name().equals("调查日期")
                || tableItem.getField1().equals("REPAIR_DAT")
                || tableItem.getField1().equals("repair_dat")) {
            btn.setEnabled(false);
            if (StringUtil.isEmpty(tableItem.getValue())) {
                btn.setText("");
                et.setText("");
            } else {
                try {
                    String v = TimeUtil.getStringTimeYMD(new Date(Long.valueOf(tableItem.getValue())));
                    btn.setText(v);
                    et.setText(v);
                } catch (Exception e) {
                    try {
                        btn.setText(tableItem.getValue());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
            if(tableState != TableState.READING){
                et.setText(System.currentTimeMillis() + "");
            }

        }

        if (tableItem.getHtml_name().equals("竣工日期")
                || tableItem.getField1().equals("FINISH_DAT")
                || tableItem.getField1().equals("finish_dat")) {
//            btn.setEnabled(false);
            if (StringUtil.isEmpty(tableItem.getValue())) {
                btn.setText("");
                et.setText("");
            } else {
                try {
                    String v = TimeUtil.getStringTimeYMD(new Date(Long.valueOf(tableItem.getValue())));
                    btn.setText(v);
                    et.setText(v);
                } catch (Exception e) {
                    try {
                        btn.setText(tableItem.getValue());
                        et.setText(tableItem.getValue());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

        }
    }

    public void setDateItemValue(TableItem tableItem, View view) {
        if (tableState == TableState.REEDITNG || tableState == TableState.READING) {
            TextView textView = (TextView) view.findViewById(R.id.tv_2);
            textView.setVisibility(View.GONE);
            EditText et = (EditText) view.findViewById(R.id.et_);
            et.setEnabled(false);
            Button btn = (Button) view.findViewById(R.id.btn_datepicker);
            if (tableState == TableState.READING) {
                btn.setVisibility(View.GONE);
                et.setVisibility(View.VISIBLE);
            } else if (tableState == TableState.REEDITNG) {
                btn.setVisibility(View.VISIBLE);
                et.setVisibility(View.INVISIBLE);
            }

            if (tableItem.getValue() != null) {
                textView.setText(tableItem.getValue());
                et.setText(tableItem.getValue());
            }
        }
    }

    /**
     * 添加文本项
     *
     * @param tableItem
     */
    public void addTextItem(TableItem tableItem) {
        //如果该文本项是用于身份证识别的,则特殊处理
        /*
        if (tableItem.getField1().equals("TEXT_FIELD_IDENTIFY")) {
            addTextItemForIdentify(tableItem);
            return;
        }
        */

        ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_textfield_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_);
        EditText editText = (EditText) view.findViewById(R.id.et_);
        TextView tv_size = (TextView) view.findViewById(R.id.tv_size);
        int total = 0;
        if (!TextUtils.isEmpty(tableItem.getRow_num()) && !TextUtils.isEmpty(tableItem.getColum_num())) {
            total = Integer.valueOf(tableItem.getColum_num()) * Integer.valueOf(tableItem.getRow_num());
        }
        tv_size.setText("0/" + total);
        if (total == 0) {
            tv_size.setVisibility(View.GONE);
        } else {
            editText.setFilters(new InputFilter[]{new MaxLengthInputFilter(total,
                    null, editText, "长度不能超过" + total + "个字").setDismissErrorDelay(1500)});
        }
        editText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        textView.setText(tableItem.getHtml_name());
        setRequireTagState(view, tableItem);
        map.put(tableItem.getId(), view);

        if (!tableItem.getField1().equals("X") && !tableItem.getField1().equals("Y")) {
            addViewToContainer(view);
        }

        //设施做特殊处理
        if (tableItem.getField1().equals("OBJECT_ID") || tableItem.getField1().equals("NAME")) {
            view.setVisibility(View.GONE);
        }

        /**
         * 錯誤信息新增的情況下不可見；在編輯和閱讀狀態下不可編輯；
         */
        if (tableItem.getField1().toLowerCase().equals("errorinfo")) {
            if (tableState == TableState.EDITING) {
                view.setVisibility(View.GONE);
            } else if (tableState == TableState.REEDITNG || tableState == TableState.READING) {
                view.findViewById(R.id.et_).setEnabled(false);
            }
        }

        setInputType(tableItem, view);
        setTextItemValue(tableItem, view);
        setTableItemState(tableItem, view);

        // TODO 2017-09-16 四标四实隐患情况专用
        ifShowWebview(tableItem, view);
        // TODO 2017-09-26 四标四实单位表专用
        ifShowSearchButton(tableItem, view);
    }

    /**
     * 如果是组织机构代码，那么显示查询按钮，让用户可以进行查询单位信息并自动填充
     *
     * @param tableItem
     * @param view
     */
    private void ifShowSearchButton(TableItem tableItem, ViewGroup view) {
        if (tableItem.getHtml_name().equals("组织机构代码")) {
            final EditText editText = (EditText) view.findViewById(R.id.et_);
            final Button btn_query = (Button) view.findViewById(R.id.btn_query); //查询按钮
            btn_query.setVisibility(View.VISIBLE);
            final ProgressBar pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
            btn_query.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDanweiInfoByIDCard(editText.getText().toString(), btn_query, pb_loading);
                }
            });

            editText.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    super.onTextChanged(s, start, before, count);

                    if (!btn_query.getText().equals("查询")) { //回复查询文字
                        btn_query.setText("查询");
                    }
                }
            });


        }
    }

    /**
     * 根据单位编码获取单位信息（四标四实专用）
     *
     * @param orgCode 单位编码
     */
    private void getDanweiInfoByIDCard(String orgCode, final Button btn_query, final View pb_loading) {

        pb_loading.setVisibility(View.VISIBLE);
        btn_query.setVisibility(View.GONE);

        OkHttpClient okHttpClient = new OkHttpClient();
        String url = BaseInfoManager.getBaseServerUrl(mContext);
        Request request = new Request.Builder().url(url + "am/multitable/getDwOrgCode?orgCode=" + orgCode + "&projectId=" + projectId).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                AppCompatActivity tableActivity = (AppCompatActivity) mContext;
                tableActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb_loading.setVisibility(View.GONE);
                        btn_query.setVisibility(View.VISIBLE);
                        btn_query.setText("查无单位信息");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                AppCompatActivity tableActivity = (AppCompatActivity) mContext;
                String result = response.body().string();
                final Map<String, String> danweiInfoMap = new HashMap<>();
                String key;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Iterator<String> keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        key = keys.next();
                        String value = jsonObject.getString(key);
                        danweiInfoMap.put(key, value);
                    }
                    if (MapUtils.isEmpty(danweiInfoMap)) {
                        tableActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb_loading.setVisibility(View.GONE);
                                btn_query.setVisibility(View.VISIBLE);
                                btn_query.setText("查无单位信息");
                            }
                        });
                    } else {
                        tableActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb_loading.setVisibility(View.GONE);
                                btn_query.setVisibility(View.VISIBLE);
                                btn_query.setText("查询成功");
                                reSetTextItemValue(danweiInfoMap);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    tableActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pb_loading.setVisibility(View.GONE);
                            btn_query.setVisibility(View.VISIBLE);
                            btn_query.setText("查无单位信息");
                        }
                    });
                }
            }
        });
    }

    /**
     * 根据后台配置处理表格项显示与否
     *
     * @param tableItem
     * @param view
     */
    public void setTableItemState(TableItem tableItem, View view) {
        if (tableItem.getIf_hidden() != null) {
            if (tableItem.getIf_hidden().equals(ControlState.INVISIBLE)) {
                view.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 设置EditText输入法格式
     *
     * @param tableItem
     * @param view
     */
    public void setInputType(TableItem tableItem, View view) {
        EditText editText = (EditText) view.findViewById(R.id.et_);
        if (editText == null) {
            return;
        }

        if (tableItem.getValidate_type() != null) {
            if (tableItem.getValidate_type().equals(ValidateType.NUMBER)) {
                //                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL |  InputType.TYPE_NUMBER_FLAG_SIGNED);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else if (tableItem.getValidate_type().equals(ValidateType.STRING)) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (tableItem.getValidate_type().equals(ValidateType.INTEGER)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else if (tableItem.getValidate_type().equals(ValidateType.ALL_NUMBER)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else if (tableItem.getValidate_type().equals(ValidateType.PHONE)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else if (tableItem.getValidate_type().equals(ValidateType.IDENTIFY)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else if (tableItem.getValidate_type().equals(ValidateType.TEL_PHONE)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else if (tableItem.getValidate_type().equals(ValidateType.ALL_PHONE)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }
        }
    }

    public void setTextItemValue(TableItem tableItem, View view) {
        if (tableState == TableState.READING || tableState == TableState.REEDITNG) {
            EditText editText = (EditText) view.findViewById(R.id.et_);

            if (tableItem.getValue() != null
                    && !tableItem.getValue().equals("null")) {  // 2017-09-16 不显示任何null
                editText.setText(tableItem.getValue());
            }
            String type = tableItem.getValidate_type();
            if (!TextUtils.isEmpty(tableItem.getValue())
                    && (ValidateUtils.isPositiveFloat(tableItem.getValue()) || ValidateUtils.isNegativeFloat(tableItem.getValue()))
                    && tableItem.getValue().contains(".")) {
                editText.setText(StringUtil.valueOf(Double.valueOf(tableItem.getValue()), 2));
            }
            if (tableState == TableState.READING) {
                editText.setEnabled(false);

            } else if (tableState == TableState.REEDITNG) {
                editText.setEnabled(true);
            }
        }

        if (tableItem.getField1().equals("REPORT_USER_ID")) {
            EditText editText = (EditText) view.findViewById(R.id.et_);
            LoginService loginService = new LoginService(mContext, AMDatabase.getInstance());
            String userName = loginService.getUser().getUserName();
            editText.setText(userName);
        }

        /************************************ xcl 番禺四标四实项目个性化定制部分********************************************/

        //判断当前item中是否包含"居住地址"/"标准地址"等字样，如果是，自动填充地址 todo 项目完成之后要迁移到patrolcore之外
        if (tableItem.getHtml_name().contains("居住地址") || tableItem.getHtml_name().contains("标准地址")
                || tableItem.getField1().equals("NOW_ADD")) {
            if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                if (TextUtils.isEmpty(tableItem.getValue())) {
                    EditText editText = (EditText) view.findViewById(R.id.et_);
                    editText.setText(mAddressName);
                }
            }
        }


        //判断当前item中是否包含门牌/房间号，如果是，自动填充记录名称
        if (tableItem.getHtml_name().contains("门牌") || tableItem.getHtml_name().contains("房间号") || tableItem.getField1().equals("MPFJH")) {

            if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                if (TextUtils.isEmpty(tableItem.getValue())) {
                    EditText editText = (EditText) view.findViewById(R.id.et_);
                    editText.setText(mRoomName);
                }
            }

        }

        //判断是否包含国籍，是，则默认填充"中国"
        if (tableItem.getHtml_name().contains("国籍")) {
            if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                if (TextUtils.isEmpty(tableItem.getValue())) {
                    EditText editText = (EditText) view.findViewById(R.id.et_);
                    editText.setText("中国");
                }
            }
        }
        //是否包含填表人
        if (tableItem.getHtml_name().contains("填表人") || tableItem.getHtml_name().contains("登记人") || tableItem.getHtml_name().contains("采集人")) {
            if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                if (TextUtils.isEmpty(tableItem.getValue())) {
                    String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getUserName();
                    EditText editText = (EditText) view.findViewById(R.id.et_);
                    editText.setText(userName);
                }
            }
        }

        //是否包含楼栋号
        if (tableItem.getHtml_name().contains("楼栋号")) {
            if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                if (TextUtils.isEmpty(tableItem.getValue())) {
                    EditText editText = (EditText) view.findViewById(R.id.et_);
                    editText.setText(dongName);
                }
            }
        }

        //是否包含人口类别
        if (tableItem.getHtml_name().contains("人口类型") || tableItem.getField1().contains("uint_people_type")) {
            if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                if (TextUtils.isEmpty(tableItem.getValue())) {
                    EditText editText = (EditText) view.findViewById(R.id.et_);
                    editText.setText(renkouleibei);
                }
            }
        }

        //是否包含学校名称
        if (tableItem.getHtml_name().contains("学校名称") || tableItem.getField1().contains("sch_name")) {
            if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                if (TextUtils.isEmpty(tableItem.getValue()) && basicDanweiInfo != null) {
                    EditText editText = (EditText) view.findViewById(R.id.et_);
                    editText.setText(basicDanweiInfo.getDanweiName());
                }
            }
        }

        //标准地址不可编辑
        if (tableItem.getHtml_name().equals("标准地址")) {
            EditText editText = (EditText) view.findViewById(R.id.et_);
            editText.setEnabled(false);
        }

        //套的楼栋号不可编辑
        if (tableItem.getField1().equals("ldh") && NOT_EDITABLE.equals(tableItem.getIs_edit())) {
            EditText editText = (EditText) view.findViewById(R.id.et_);
            editText.setEnabled(false);
        }

        //学生表的的学校名称不可编辑
        if (tableItem.getField1().equals("sch_name")) {
            EditText editText = (EditText) view.findViewById(R.id.et_);
            editText.setEnabled(false);
        }

        //人口信息表的房间号不可编辑
        if (tableItem.getField1().equals("fjh") && NOT_EDITABLE.equals(tableItem.getIs_edit())) {
            //            if (tableItem.getValue() != null && !tableItem.getValue().isEmpty()){
            // 2017-09-19 无论是否有值均不可编辑
            EditText editText = (EditText) view.findViewById(R.id.et_);
            editText.setEnabled(false);
            //            }
        }

        //人口信息表门牌号不可编辑
        if (tableItem.getField1().equals("mph") && NOT_EDITABLE.equals(tableItem.getIs_edit())) {
            EditText editText = (EditText) view.findViewById(R.id.et_);
            editText.setEnabled(false);
        }

        // 隐患情况不可编辑
        if (tableItem.getField1().equals("yhqk") && NOT_EDITABLE.equals(tableItem.getIs_edit())) {
            EditText editText = (EditText) view.findViewById(R.id.et_);
            editText.setEnabled(false);
        }

        /***************  番禺四标四实项目   人口基本信息 -> 境外人口和流动人口  by gkh ******/
        if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
            if (getBasicRenKouInfo() != null) {
                //姓名
                if (getBasicRenKouInfo().getName() != null) {
                    if (tableItem.getHtml_name().contains("中文姓名")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getName());
                        }
                    }

                    if (tableItem.getField1().toLowerCase().equals("name")) { //姓名
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getName());
                        }
                    }
                }


                //标准地址
                if (getBasicRenKouInfo().getAddress() != null) {
                    if (tableItem.getHtml_name().contains("在华住址")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getAddress());
                        }
                    }

                    if (tableItem.getHtml_name().contains("现居住地址")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getAddress());
                        }
                    }
                }


                //电话号码
                if (getBasicRenKouInfo().getTelephone() != null) {

                    //联系电话
                    if (tableItem.getField1().toLowerCase().equals("tel")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getTelephone());
                        }
                    }

                    /*if (tableItem.getHtml_name().contains("留宿人/屋主电话")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getTelephone());
                        }
                    }

                    if (tableItem.getHtml_name().contains("代办人电话")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getTelephone());
                        }
                    }

                    if (tableItem.getHtml_name().contains("联系电话")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getTelephone());
                        }
                    }*/
                }

                //证件号码
                if (getBasicRenKouInfo().getIdenitfyNo() != null) {

                    if (tableItem.getHtml_name().contains("签证（注）号码")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getIdenitfyNo());
                        }
                    }

                    if (tableItem.getField1().toLowerCase().equals("zjhm") || tableItem.getField1().toLowerCase().equals("per_id")
                            || tableItem.getField1().toLowerCase().equals("pes_id")) {//公民身份证号码
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getIdenitfyNo());
                        }
                    }
                }

                //屋主
                if (getBasicRenKouInfo().getHouseOwner() != null) {

                    if (tableItem.getHtml_name().contains("屋主（代理人）姓名") || tableItem.getField1().toLowerCase().equals("wz_name")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getHouseOwner());
                        }
                    }
                }


                //屋主电话
                if (getBasicRenKouInfo().getHouseOwnerPhone() != null) {

                    if (tableItem.getHtml_name().contains("屋主（代理人）电话") || tableItem.getField1().toLowerCase().equals("wz_tel")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getHouseOwnerPhone());
                        }
                    }
                }

                //留宿人证件号
                if (getBasicRenKouInfo().getHouseOwnerIdentifyId() != null) {

                    if (tableItem.getHtml_name().contains("留宿人证件号")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getHouseOwnerIdentifyId());
                        }
                    }
                }
            }
        }


        /************************************番禺四标四实项目个性化定制部分********************************************/

        /************************************广州排水设施核查项目个性化定制部分********************************************/
        if (tableState == TableState.EDITING) {
            if ("cd18e3b2-b2e7-463b-891d-b4d4cc13d2a3".equals(projectId)) {//窨井
                if (tableItem.getField1().contains("fcode")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060209");
                    }
                }
                if (tableItem.getField1().contains("usid")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060209-28421516-0000" + (int) (1 + Math.random() * (99 - 10 + 1)));
                    }
                }
            } else if ("0d1bcade-a43a-4e02-8bcd-abba4502e891".equals(projectId)) {//雨水口
                if (tableItem.getField1().contains("fcode")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060206");
                    }
                }
                if (tableItem.getField1().contains("usid")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060206-28421516-0000" + (int) (1 + Math.random() * (99 - 10 + 1)));
                    }
                }
            } else if ("224074c8-7703-4f63-a391-acfe6f89df1f".equals(projectId)) {//排水口
                if (tableItem.getField1().contains("fcode")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060208");
                    }
                }
                if (tableItem.getField1().contains("usid")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060208-28421516-0000" + (int) (1 + Math.random() * (99 - 10 + 1)));
                    }
                }
            } else if ("67762e14-b3d7-4a62-8b30-1cd0e933fd5a".equals(projectId)) {//排水管道
                if (tableItem.getField1().contains("fcode")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060101");
                    }
                }
                if (tableItem.getField1().contains("usid")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060101-28421516-0000" + (int) (1 + Math.random() * (99 - 10 + 1)));
                    }
                }
            } else if ("ad08522c-e38d-4e3f-a8cf-8bccb7113227".equals(projectId)) {//溢流堰
                if (tableItem.getField1().contains("fcode")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060205");
                    }
                }
                if (tableItem.getField1().contains("usid")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060205-28421516-0000" + (int) (1 + Math.random() * (99 - 10 + 1)));
                    }
                }
            } else if ("e7c86272-6364-4296-ba57-f76d63200cb3".equals(projectId)) {//拍门
                if (tableItem.getField1().contains("fcode")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060210");
                    }
                }
                if (tableItem.getField1().contains("usid")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060210-28421516-0000" + (int) (1 + Math.random() * (99 - 10 + 1)));
                    }
                }
            } else if ("f95772c6-77d4-4f27-8efe-1a4688701896".equals(projectId)) {//阀门
                if (tableItem.getField1().contains("fcode")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060204");
                    }
                }
                if (tableItem.getField1().contains("usid")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060204-28421516-0000" + (int) (1 + Math.random() * (99 - 10 + 1)));
                    }
                }
            } else if ("fff90089-5039-4b45-b697-42743c14ca8c".equals(projectId)) {//泵站
                if (tableItem.getField1().contains("fcode")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060302");
                    }
                }
                if (tableItem.getField1().contains("usid")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText("060302-28421516-0000" + (int) (1 + Math.random() * (99 - 10 + 1)));
                    }
                }
            }
        }
        /************************************广州排水设施核查个性化定制部分********************************************/
    }

    /**
     * 身份证识别项
     *
     * @param tableItem
     */

    public void addTextItemForIdentify(TableItem tableItem) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_textfield_item_for_identify, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_);
        final View pb_querying = view.findViewById(R.id.pb_querying);
        final EditText editText = (EditText) view.findViewById(R.id.et_);
        final Button btn_identify_result = (Button) view.findViewById(R.id.btn_identify_result);
        /*********************add by taoerxiang*************************/
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    btn_identify_result.setVisibility(View.GONE);
                    String Identify = s.toString().trim();
                    if (Identify.length() == 18 && before != count) {
                        pb_querying.setVisibility(View.VISIBLE);
                        getRenkouInfoByIDCard(Identify, pb_querying, btn_identify_result);
                    } else if (Identify.length() > 18) {
                        btn_identify_result.setVisibility(View.VISIBLE);
                        btn_identify_result.setText("身份证号个数不正确");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        /*****************************************************************/
        Button btn_identify = (Button) view.findViewById(R.id.btn_identify);
        btn_identify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(editText);
            }
        });

        if (tableState == TableState.READING) {
            btn_identify.setVisibility(View.GONE);
        }

        textView.setText(tableItem.getHtml_name());
        setRequireTagState(view, tableItem);
        map.put(tableItem.getId(), view);
        addViewToContainer(view);

        setIdentifyTextItemValue(tableItem, view);
        setTableItemState(tableItem, view);

    }

    /**
     * 根据身份证获取用户信息 对应存在的文本输入框进行填充（四标四实专用）
     *
     * @param identify
     */
    private void getRenkouInfoByIDCard(String identify, final View pb_querying, final Button btn_identify_result) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = BaseInfoManager.getBaseServerUrl(mContext);
        //Request request = new Request.Builder().url(url + "sbss/info/queryPersion/" + identify).build(); //xcl 2017.09.26 修改接口
        Request request = new Request.Builder().url(url + "am/multitable/getDetailByZjhm?zjhm=" + identify + "&projectId=" + projectId).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                AppCompatActivity tableActivity = (AppCompatActivity) mContext;
                tableActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb_querying.setVisibility(View.GONE);
                        btn_identify_result.setVisibility(View.VISIBLE);
                        btn_identify_result.setText("查无对应人口信息");

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                AppCompatActivity tableActivity = (AppCompatActivity) mContext;
               /* tableActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb_querying.setVisibility(View.GONE);

                    }
                });*/
                String result = response.body().string();
                final Map<String, String> personInfoMap = new HashMap<>();
                String key;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Iterator<String> keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        key = keys.next();
                        String value = jsonObject.getString(key);
                        personInfoMap.put(key, value);
                    }
                    if (MapUtils.isEmpty(personInfoMap)) {
                        tableActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb_querying.setVisibility(View.GONE);
                                btn_identify_result.setVisibility(View.VISIBLE);
                                btn_identify_result.setText("查无对应人口信息");
                            }
                        });
                    } else {
                        tableActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb_querying.setVisibility(View.GONE);
                                btn_identify_result.setVisibility(View.VISIBLE);
                                btn_identify_result.setText("查无对应人口信息");
                                reSetTextItemValue(personInfoMap);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    tableActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pb_querying.setVisibility(View.GONE);
                            btn_identify_result.setVisibility(View.VISIBLE);
                            btn_identify_result.setText("查无对应人口信息");
                        }
                    });
                }
            }
        });
    }

    private void reSetTextItemValue(Map<String, String> personInfoMap) {
        for (int i = 0; i < tableItems.size(); i++) {
            for (String key : personInfoMap.keySet()) {
                if (tableItems.get(i).getField1().equals(key)) {
                    View view = map.get(tableItems.get(i).getId());
                    if (view != null) {
                        EditText etText = (EditText) view.findViewById(R.id.et_);
                        //if (TextUtils.isEmpty(etText.getText().toString())){
                        if (!TextUtils.isEmpty(StringUtil.getNotNullString(personInfoMap.get(key), ""))) { //避免覆盖掉spinner中的editText里面默认设置的值
                            etText.setText(personInfoMap.get(key));
                        }

                        //}

                        final Button btn = (Button) view.findViewById(R.id.btn_datepicker);
                        if (btn != null) {
                            //说明是日期控件
                            btn.setText(personInfoMap.get(key));
                        }

                        //如果是下拉框控件
                        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                        if (spinner != null) {
                            String value = personInfoMap.get(key);
                            for (int j = 0; j < spinner.getAdapter().getCount(); ++j) {
                                String item = (String) spinner.getAdapter().getItem(j);
                                if (item.equals(value) || value.contains(item)) { //服务端性别返回『女性』，但是数据字典里配的是『女』
                                    spinner.setSelection(j);
                                }
                            }
                        }
                    }


                }
            }
        }
    }

    /**
     * 这是兼容的 AlertDialog
     */
    private void showDialog(final EditText editText) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("是否进行照相扫描身份证号码?");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*
                PhotoIdentifyManager.getInstance(mContext).takePhotoForIdentify(new PhotoIdentifyManager.OnIdetifyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
                        editText.setText(result);
                    }

                    @Override
                    public void onFail(String msg) {
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                    }
                });
                */
                /*
                String devcode = Devcode.devcode;// 项目授权开发码
                Intent intent = new Intent(getmContext(), CameraActivity.class);
                intent.putExtra("nMainId", SharedPreferencesHelper.getInt(
                        mContext, "nMainId", 2));
                intent.putExtra("devcode", devcode);
                intent.putExtra("flag", 0);
                intent.putExtra("nCropType", 0);

                //强制成Activity
                ((Activity)mContext).startActivityForResult(intent,520);
                */
            }
        });
        builder.show();
    }

    /**
     * 身份证识别默认值
     *
     * @param tableItem
     * @param view
     */
    public void setIdentifyTextItemValue(TableItem tableItem, View view) {
        if (tableState == TableState.READING || tableState == TableState.REEDITNG) {
            EditText editText = (EditText) view.findViewById(R.id.et_);
            if (tableItem.getValue() != null) {
                editText.setText(tableItem.getValue());
            }
            if (tableState == TableState.READING) {
                editText.setEnabled(false);
            } else if (tableState == TableState.REEDITNG) {
                editText.setEnabled(true);
            }
        }


        /****************  番禺四标四实项目特制  by gkh *************/
        if (tableState == TableState.EDITING) {
            if (getBasicRenKouInfo() != null) {
                //证件号码
                if (getBasicRenKouInfo().getIdenitfyNo() != null) {
                    if (tableItem.getHtml_name().contains("身份证号码")) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText(getBasicRenKouInfo().getIdenitfyNo());
                    }

                    if (tableItem.getHtml_name().contains("留宿人证件号")) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText(getBasicRenKouInfo().getIdenitfyNo());
                    }
                }

            }
        }
    }

    /**
     * 排序下拉字典数组
     *
     * @param list
     */
    public void processOrder(List<TableChildItem> list) {
        for (TableChildItem item : list) {
            String code = item.getCode();
            String target = code;
            if (code.length() > 1) {
                target = code.replaceAll("[^(0-9)]", "");//去掉所有字母
            }
            item.setCode(target);
        }
    }

    /**
     * 获取下拉字典默认项
     *
     * @param list
     * @return
     */
    public TableChildItem getDefaultItem(List<TableChildItem> list) {
        TableChildItem item = null;
        for (TableChildItem childItem : list) {
            if (childItem.getNote() != null)
                if (childItem.getNote().equals("1")) {
                    item = childItem;
                    break;
                }
        }

        return item;
    }

    /**
     * H5详情控件
     * TODO 目前仅用于四标四实项目
     */
    public void addH5Html(TableItem tableItem) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_h5info_item, null);
        final EditText editText = (EditText) view.findViewById(R.id.et_);
        ViewGroup container_webview = (ViewGroup) view.findViewById(R.id.container_webview);
        final FireFightingView fireFightingView = new FireFightingView(mContext, "", container_webview);
        // 详情按钮
        this.listeners.add(new TableLoadListener() {
            @Override
            public void onFinishedLoad() {
                // TODO 目前还没有区分是加载何种html页面
                // 取功能类型及具体信息json数据
                final EditText et_types = (EditText) map.get(getTableItemByField1("sygnlb").getId()).findViewById(R.id.et_);
                final EditText et_infos = (EditText) map.get(getTableItemByField1("jtxx").getId()).findViewById(R.id.et_);
                String types = et_types.getText().toString();
                String infos = et_infos.getText().toString();

                fireFightingView.setOnJsCallback(new FireFightingView.OnJsCallback() {
                    @Override
                    public void onCallback(String typesJson, String infosJson, String show) {
                        if (tableState != TableState.READING) {
                            et_types.setText(typesJson);
                            et_infos.setText(infosJson);
                            editText.setText(show);
                        }
                    }
                });
                if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                    fireFightingView.show(types, infos, false);
                } else if (tableState == TableState.READING) {
                    fireFightingView.show(types, infos, true);
                }
            }
        });

        //        setRequireTagState(view, tableItem);
        map.put(tableItem.getId(), view);
        addViewToContainer(view);

        setInputType(tableItem, view);
        setTextItemValue(tableItem, view);
        setTableItemState(tableItem, view);

        editText.setEnabled(false);
    }

    /**
     * 下拉类型项
     *
     * @param list
     * @param tableItem
     */
    public void addSpinnerItem(final List<TableChildItem> list, final TableItem tableItem) {
        final ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_combo_item, null);
        TextView textView = (TextView) viewGroup.findViewById(R.id.tv_);
        textView.setText(tableItem.getHtml_name());
        setRequireTagState(viewGroup, tableItem);
        Spinner spinner = (Spinner) viewGroup.findViewById(R.id.spinner);
        final EditText editText = (EditText) viewGroup.findViewById(R.id.et_);
        List<String> spinerString = new ArrayList<>();

        //处理排序字段
        processOrder(list);
        //再排序
        Collections.sort(list, new Comparator<TableChildItem>() {
            @Override
            public int compare(TableChildItem t1, TableChildItem t2) {
                int num1 = Integer.valueOf(t1.getCode());
                int num2 = Integer.valueOf(t2.getCode());
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

        for (TableChildItem tableChildItem : list) {
            spinerString.add(tableChildItem.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, spinerString);
        //        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                //使子下拉跟随父下拉联动
                itemSelectHandle(parent, position, editText, list, tableItem);

                //                ifShowWebview(position,tableItem,viewGroup,list); // 隐患情况改为
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        map.put(tableItem.getId(), viewGroup);
        addViewToContainer(viewGroup);
        setSpinnerItemValue(tableItem, viewGroup, getDefaultItem(list));
        setTableItemState(tableItem, viewGroup);
    }

    /**
     * 四标四实隐患情况专用
     *
     * @param tableItem
     * @param viewGroup
     */
    public void ifShowWebview(final TableItem tableItem, final ViewGroup viewGroup) {
        if (tableItem.getHtml_name().equals("事件类型")) {  // TODO 这里要改成不同控件类型，不能通过html_name来判断
            final EditText editText = (EditText) viewGroup.findViewById(R.id.et_);
            final Button btn = (Button) viewGroup.findViewById(R.id.btn);
            if (TextUtils.isEmpty(tableItem.getValue())) {
                editText.setText("0");
            }
            editText.setEnabled(false);
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //从字典里获取所有数据
                    // 目前没有配置parentCode,暂时写死来获取所有
                    List<TableChildItem> b003 = getTableChildItemFormDB("B003");
                    TableChildItem[] b003A = b003.toArray(new TableChildItem[0]);
                    // 按Code从小到大排序
                    for (int i = 0; i < b003A.length - 1; i++) {
                        for (int j = 0; j < b003A.length - 1 - i; j++) {
                            int jCode = Integer.parseInt(b003A[j].getCode().substring(1));
                            int j1Code = Integer.parseInt(b003A[j + 1].getCode().substring(1));
                            if (jCode > j1Code) {
                                TableChildItem temp = b003A[j];
                                b003A[j] = b003A[j + 1];
                                b003A[j + 1] = temp;
                            }
                        }
                    }
                    b003 = Arrays.asList(b003A);
                    // 构造数据
                    // 拿到存放json字段edittext
                    final EditText dataEditText = (EditText) map.get(getTableItemByField1("yhqk_data").getId()).findViewById(R.id.et_);
                    String itemValue = dataEditText.getText().toString();    // 已经有的数据
                    Map<String, String> valueMap = new HashMap<String, String>();
                    if (!TextUtils.isEmpty(itemValue)) {
                        try {
                            valueMap = new Gson().fromJson(itemValue, new TypeToken<Map<String, String>>() {
                            }.getType());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    DecimalFormat format = new DecimalFormat("000");
                    ArrayList<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < b003.size(); i++) {
                        String typeCode = "B" + format.format(4 + i);   // TODO 通过parentCode里获取更合理(在B003字典里挨个配一下)
                        TableChildItem item = b003.get(i);
                        List<TableChildItem> childItems = tableDataManager.getTableChildItemsByTypeCode(typeCode);
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("item", item);
                        map.put("childItems", childItems);
                        map.put("isSelected", false);   // 父项是否勾选
                        //                            map.put("selectedIndex", -1);   // 选中子项位置
                        map.put("selectedCode", "");    // 选中子项Code
                        // 父项是否选中
                        if (valueMap.containsKey(item.getCode())) {
                            map.put("isSelected", true);
                        }
                        ArrayList<String> selectedCodes = new ArrayList<String>(); // 2017-09-20 改为多选模式
                        // 选中子项
                        for (TableChildItem childItem : childItems) {
                            if (valueMap.containsKey(childItem.getCode())) {
                                selectedCodes.add(childItem.getCode());
                                map.put("isSelected", true);
                            }
                        }
                        map.put("selectedCode", selectedCodes.toArray(new String[0]));
                        maps.add(map);
                    }
                    String json = new Gson().toJson(maps);
                    //加载webview
                    final SaftyHazardView popup = new SaftyHazardView(mContext, tableItem.getHtml_name());
                    popup.setOnJsCallback(new SaftyHazardView.OnJsCallback() {
                        @Override
                        public void onCallback(String dataJson, String show) {
                            popup.dismiss();
                            if (tableState != TableState.READING) {
                                dataEditText.setText(dataJson); // 从webview返回结果，设置到edittext
                                editText.setText(show);
                            }
                        }
                    });
                    if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                        popup.show(view, json, false);
                    } else if (tableState == TableState.READING) {
                        popup.show(view, json, true);
                    }
                }
            });
        }
    }

    /**
     * 处理下拉选中时的级联
     *
     * @param parent
     * @param position
     * @param editText
     * @param list
     * @param tableItem
     */
    private void itemSelectHandle(AdapterView<?> parent, int position, EditText editText, List<TableChildItem> list, TableItem tableItem) {
        //获取Spinner控件的适配器
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
        if (editText != null) {
            //            editText.setText(adapter.getItem(position));
            if (isEditingFeatureLayer) {
                editText.setText(list.get(position).getName());
            } else {
                editText.setText(list.get(position).getCode());//下拉改成存code
            }
            //             editText.setText(list.get(position).getCode());//下拉改成存code
//            editText.setText(list.get(position).getName());  //下接改成存name
        }
        //级联子项数据
        TableChildItem parentSelectedItem = list.get(position);//当前父下拉选中项
        if (parentSelectedItem != null) {
            String childId = tableItem.getChildren_code();
            String pCode = parentSelectedItem.getCode();//获取当前父下拉选中项的code值，如A009001，父下拉的选中值决定了子下拉的填充值
            List<TableChildItem> childItems = tableDataManager.getTableChildItemsByPCode(pCode);//根据pCode筛选子下拉项的填充值
            if (childItems != null && !childItems.isEmpty()) {
                resetSpinnerChildItemAdapter(childId, childItems);//刷新子下拉的填充值
            }
        }
    }

    /**
     * 根据 dic_code 获取级联类型表格项
     *
     * @param dic_code
     * @return
     */
    public TableItem getTableItemByCode(String dic_code) {
        TableItem tableItem = null;
        for (TableItem tmp : tableItems) {
            //是否级联类型
            if (tmp.getControl_type() != null) {
                if (tmp.getControl_type().equals(ControlType.SPINNER)) {
                    if (tmp.getDic_code() != null) {
                        if (tmp.getDic_code().equals(dic_code)) {
                            tableItem = tmp;
                            break;
                        }
                    }
                }
            }
        }
        return tableItem;
    }

    /**
     * 获取下拉级联数据的URL
     * 默认初始化时当前项都是第一项
     *
     * @param typecode
     * @return
     */
    public String getTableChildUrl(String typecode) {
        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);
        String url = serverUrl + "rest/agdic/agdicByTypeCode";
        url = url + "/" + typecode;
        return url;
    }

    /**
     * 获取下拉级联数据
     *
     * @param tableItem
     */
    public void getSpinnerItemData(final TableItem tableItem) {
        List<TableChildItem> tableChildItems = getTableChildItemFormDB(tableItem.getDic_code());
        String url = getTableChildUrl(tableItem.getDic_code());
        if (tableChildItems.isEmpty()) {
            ToastUtil.shortToast(mContext, "数据字典数据存在空值!");
            tableDataManager.getTableChildItemsFromNet(url, new TableNetCallback() {
                @Override
                public void onSuccess(Object data) {
                    TableChildItems tableChildItems = (TableChildItems) data;
                    List<TableChildItem> list = tableChildItems.getRows();
                    //   tableDataManager.setTableChildItemsToDB(list,tableItem.getDic_code());
                    addSpinnerItem(list, tableItem);
                }

                @Override
                public void onError(String msg) {
                }
            });
        } else {
            //    List<TableChildItem> list = tableDataManager.getTableChildItemsFromDB(tableItem.getDic_code());
            addSpinnerItem(tableChildItems, tableItem);
        }
    }

    /**
     * 重置子项级联数据适配器
     *
     * @param dic_code
     * @param list
     */
    public void resetSpinnerChildItemAdapter(String dic_code, final List<TableChildItem> list) {
        final TableItem tableItem = getTableItemByCode(dic_code);
        if (tableItem == null) return;
        View view = map.get(tableItem.getId());
        if (view != null) {
            Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
            final EditText editText = (EditText) view.findViewById(R.id.et_);
            if (spinner != null) {
                List<String> spinerString = new ArrayList<>();
                for (TableChildItem tableChildItem : list) {
                    spinerString.add(tableChildItem.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, spinerString);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                        itemSelectHandle(parent, position, editText, list, tableItem);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                /**
                 * 处理默认值
                 */
                TableChildItem defaultItem = getDefaultItem(list);
                if (getDefaultItem(list) != null) {
                    String value = defaultItem.getName();
                    for (int i = 0; i < spinner.getAdapter().getCount(); ++i) {
                        String item = (String) spinner.getAdapter().getItem(i);
                        if (item.equals(value)) {
                            spinner.setSelection(i);
                        }
                    }
                }
            }
        }
    }

    public void setSpinnerItemValue(TableItem tableItem, View view, TableChildItem defaultItem) {
        if (tableState == TableState.READING || tableState == TableState.REEDITNG) {
            TextView tv = (TextView) view.findViewById(R.id.tv_2);
            tv.setVisibility(View.GONE);
            EditText et = (EditText) view.findViewById(R.id.et_);
            et.setEnabled(false);
            et.setVisibility(View.VISIBLE);
            Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
            if (tableState == TableState.READING) {
                spinner.setVisibility(View.GONE);
            } else if (tableState == TableState.REEDITNG) {
                spinner.setVisibility(View.VISIBLE);
                // TODO liangsh 修复再次编缉状态两个控件重叠且下拉列表的选中的值不是实际的值的bug
                et.setVisibility(View.GONE);
                for (int i = 0; i < spinner.getAdapter().getCount(); ++i) {
                    String value = (String) spinner.getAdapter().getItem(i);
                    if (!StringUtil.isEmpty(tableItem.getValue())) {
                        if (value.equals(tableItem.getValue())) {
                            spinner.setSelection(i);
                        } else {
                            TableDBService tableDBService = new TableDBService(mContext);
                            List<DictionaryItem> dictionaryByCode = tableDBService.getDictionaryByCode(tableItem.getValue());
                            if (dictionaryByCode.size() > 0) {
                                if (dictionaryByCode.get(0).getName().equals(value)) {
                                    spinner.setSelection(i);
                                }
                            }
                        }
                    }
                }
            }
            if (tableItem.getValue() != null) {

                TableDBService tableDBService = new TableDBService(mContext);
                List<DictionaryItem> tableChildItems = tableDBService.getDictionaryByCode(tableItem.getValue());

                DictionaryItem childItem = null;
                if (tableChildItems != null && tableChildItems.size() > 0) {
                    childItem = tableChildItems.get(0);
                }

                if (childItem != null) {
                    tv.setText(childItem.getName());
                    et.setText(childItem.getName());
                } else {
                    tv.setText(tableItem.getValue());
                    et.setText(tableItem.getValue());
                }
            }
        }

        /************************************ xcl 番禺四标四实项目个性化定制部分********************************************/
        if (tableState == TableState.EDITING) {

            //如果包含"民族"，默认选择汉族
            if (tableItem.getHtml_name().contains("民族")) {
                Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                String value = "汉族";
                List<TableChildItem> tableChildItems = getTableChildItemFormDB(tableItem.getDic_code());
                TableDBService tableDBService = new TableDBService(mContext);
                for (int i = 0; i < spinner.getAdapter().getCount(); ++i) {
                    String item = (String) spinner.getAdapter().getItem(i);
                    if (item.equals(value)) {
                        spinner.setSelection(i);
                    }
                }
            }

            //是否包含"人口类别"，默认选择"本市户籍（人户一致）" //todo 这里要改，如果是境外人员和流动人员则不是默认选择这个
            if (tableItem.getHtml_name().contains("人口类别") || tableItem.getHtml_name().equals("人口类别")
                    || tableItem.getField1().equals("RKLB") || tableItem.getField1().equals("RKLX")) {
                Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                String value = "本市户籍（人户一致）";
                if (shiyouDanweiTableName != null && shiyouDanweiTableName.contains("学生")) {
                    value = "学生";
                } else if (shiyouDanweiTableName != null && shiyouDanweiTableName.contains("从业")) {
                    value = "从业人员";
                }

                for (int i = 0; i < spinner.getAdapter().getCount(); ++i) {
                    String item = (String) spinner.getAdapter().getItem(i);
                    if (item.equals(value)) {
                        spinner.setSelection(i);
                    }
                }
            }
            /**
             * 处理默认值
             */
            if (defaultItem != null) {
                Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                String value = defaultItem.getName();
                List<TableChildItem> tableChildItems = getTableChildItemFormDB(tableItem.getDic_code());
                TableDBService tableDBService = new TableDBService(mContext);
                for (int i = 0; i < spinner.getAdapter().getCount(); ++i) {
                    String item = (String) spinner.getAdapter().getItem(i);
                    if (item.equals(value)) {
                        spinner.setSelection(i);
                    }
                }
            }


            /**
             * 房屋使用性质
             */
            if (tableItem.getHtml_name().contains("房屋使用性质") && basicDongInfo != null && !TextUtils.isEmpty(basicDongInfo.getHouseType())) {
                Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                String value = basicDongInfo.getHouseType();
                List<TableChildItem> tableChildItems = getTableChildItemFormDB(tableItem.getDic_code());
                TableDBService tableDBService = new TableDBService(mContext);
                for (int i = 0; i < spinner.getAdapter().getCount(); ++i) {
                    String item = (String) spinner.getAdapter().getItem(i);
                    if (item.equals(value)) {
                        spinner.setSelection(i);
                    }
                }
            }

            /**
             * 性别
             */
            if (tableItem.getHtml_name().contains("性别") && basicRenKouInfo != null && !TextUtils.isEmpty(basicRenKouInfo.getGender())) {
                Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                String value = basicRenKouInfo.getGender();
                List<TableChildItem> tableChildItems = getTableChildItemFormDB(tableItem.getDic_code());
                TableDBService tableDBService = new TableDBService(mContext);
                for (int i = 0; i < spinner.getAdapter().getCount(); ++i) {
                    String item = (String) spinner.getAdapter().getItem(i);
                    if (item.equals(value)) {
                        spinner.setSelection(i);
                    }
                }
            }

        }

        if (tableState == TableState.REEDITNG) {
            if (tableItem.getValue() == null || tableItem.getValue().isEmpty()) {

                /**
                 * 处理默认值
                 */
                if (defaultItem != null) {
                    Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                    String value = defaultItem.getName();
                    List<TableChildItem> tableChildItems = getTableChildItemFormDB(tableItem.getDic_code());
                    TableDBService tableDBService = new TableDBService(mContext);
                    for (int i = 0; i < spinner.getAdapter().getCount(); ++i) {
                        String item = (String) spinner.getAdapter().getItem(i);
                        if (item.equals(value)) {
                            spinner.setSelection(i);
                        }
                    }
                }
            }
        }
        /************************************番禺四标四实项目个性化定制部分********************************************/
    }

    /**
     * 更新表格项里面的数据,为上传做准备
     *
     * @return
     */
    public List<TableItem> getUploadTableItems() {
        valueMap.clear();
        for (TableItem tableItem : tableItems) {
            if (tableItem.getIf_hidden() != null) {
                //    if (tableItem.getIf_hidden().equals(ControlState.VISIBLE)) {
                View view = map.get(tableItem.getId());
                //此处用隐藏的EditText来存储具体的填写值
                EditText editText = null;
                if (view != null) {
                    editText = (EditText) view.findViewById(R.id.et_);
                }
                if (editText != null) {
                    if (editText.getText() != null) {
                        String value = editText.getText().toString();
                        //   if (tableItem.getField1() != null) {
                        if (tableItem.getField1().equals("PATROL_CODE")) {
                            if (patrolCode == null) {
                                long time = System.currentTimeMillis();
                                value = String.valueOf(time);
                            } else {
                                value = patrolCode; //通过patrolCode来更新服务器已上报数据
                            }
                        }
                        valueMap.put(tableItem.getField1(), value);
                        tableItem.setValue(value);
                        //    }
                    } else {
                        if (tableItem.getField1().equals("PATROL_CODE")) {
                            String value = null;
                            if (patrolCode == null) {
                                long time = System.currentTimeMillis();
                                value = String.valueOf(time);
                            } else {
                                value = patrolCode; //通过patrolCode来更新服务器已上报数据
                            }
                            valueMap.put(tableItem.getField1(), value);
                            tableItem.setValue(value);
                        }
                    }
                }
            }
        }

        if (mTableValueListener != null) {
            mTableValueListener.changeValueForUpload(valueMap, this);
        }

        return tableItems;
    }

    public Map<String, String> getValueMap() {
        getUploadTableItems();
        return valueMap;
    }

    public Map<String, List<Photo>> getPhotos() {
        getUploadTableItems();
        return mPhotoListMap;
    }

    /**
     * 获得经纬度相关联的EditText
     *
     * @param type "X"->经度   "Y"->纬度 已经跟服务器协商好
     *             "OBJECT_ID" -> 设施ID   "NAME" ->设施名字
     * @return
     */
    public EditText getEditTextViewByField1Type(String type) {
        EditText editText = null;
        for (TableItem tableItem : tableItems) {
            if (tableItem.getField1() != null) {
                if (tableItem.getField1().equals(type)) {
                    ViewGroup viewGroup = (ViewGroup) map.get(tableItem.getId());
                    if (viewGroup != null) {
                        editText = (EditText) viewGroup.findViewById(R.id.et_);
                    }
                    break;
                }
            }
        }
        return editText;
    }

    private void refreshPhotoViewToFirst(String key) {
        HorizontalScrollPhotoViewAdapter adapter = mPhotoViewAdapterMap.get(key);
        HorizontalScrollPhotoView photoView = mPhotoViewMap.get(key);
        adapter.notifyDataSetChanged();
        photoView.initDatas(adapter);
    }

    private void refreshPhotoViewToLast(String key) {
        HorizontalScrollPhotoViewAdapter adapter = mPhotoViewAdapterMap.get(key);
        HorizontalScrollPhotoView photoView = mPhotoViewMap.get(key);
        adapter.notifyDataSetChanged();
        photoView.notifyDataSetChanged(adapter);
    }

    private void onCopyRsultToItems(List<Map<String, String>> list) {
        for (Map<String, String> map : list) {
            onCopyResultToItem(map);
        }
    }

    private void onCopyResultToItem(Map<String, String> param) {
        for (TableItem item : tableItems) {
            //身份证识别控件
            if (item.getControl_type().equals(ControlType.PHOTO_IDENTIFY_TEXT_FIELD) && param.containsKey("公民身份号码")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("公民身份号码"));
                }
            }

            if (item.getField1().equals("name") && param.containsKey("姓名")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("姓名"));
                }
            }

            if (item.getField1().equals("sex") && param.containsKey("性别")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("性别"));
                }
                View view = map.get(item.getId());
                if (view != null) {
                    //如果是下拉框控件
                    Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                    if (spinner != null) {
                        String value = param.get("性别");
                        for (int j = 0; j < spinner.getAdapter().getCount(); ++j) {
                            String str = (String) spinner.getAdapter().getItem(j);
                            if (str.equals(value) || value.contains(str)) { //服务端性别返回『女性』，但是数据字典里配的是『女』
                                spinner.setSelection(j);
                            }
                        }
                    }
                }
            }
            if (item.getField1().equals("mz") && param.containsKey("民族")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("民族"));
                }
                View view = map.get(item.getId());
                if (view != null) {
                    //如果是下拉框控件
                    Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                    if (spinner != null) {
                        String value = param.get("民族");
                        for (int j = 0; j < spinner.getAdapter().getCount(); ++j) {
                            String str = (String) spinner.getAdapter().getItem(j);
                            if (str.equals(value) || value.contains(str) || str.contains(value)) { //服务端性别返回『女性』，但是数据字典里配的是『女』
                                spinner.setSelection(j);
                            }
                        }
                    }
                }
            }
            /*
            if (item.getField1().equals("nation") && param.containsKey("民族")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("民族"));
                }
            }
            */

            if (item.getField1().equals("bth_date") && param.containsKey("出生")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("出生"));
                    View view = map.get(item.getId());
                    if (view != null) {
                        Button btn = (Button) view.findViewById(R.id.btn_datepicker);
                        if (btn != null) {
                            btn.setText(param.get("出生"));
                        }
                    }
                }
            }

            if (item.getField1().equals("cqr_id") && param.containsKey("公民身份号码")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("公民身份号码"));
                }
            }

            if (item.getField1().equals("cqr_name") && param.containsKey("姓名")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("姓名"));
                }
            }
        }
    }

    private void onPhotoIdetifyResult(String result) {
        if (result == null || result.isEmpty()) {
            return;
        }

        String str[] = result.split(",");
        int length = str.length;
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Map<String, String> map = new HashMap<>();
            String string = str[i];
            String str2[] = string.split(":");
            if (str2.length == 2) {
                map.put(str2[0], str2[1]);
            }
            if (!map.isEmpty())
                list.add(map);
        }

        onCopyRsultToItems(list);
    }

    /**
     * 主要用于拍照、打开照片、地图浏览等返回Activity的刷新操作
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RESULT_CAPTURE_PHOTO://拍照返回
                    refreshImageAdapter();
                    break;
                case RESULT_OPEN_PHOTO://打开照片返回
                    if (null == data)
                        return;
                    mPhotoIntentData = data;
                    doCopyPhoto();
                    break;
                case 520:
                    String result = data.getStringExtra("recogResult");
                    //       ToastUtil.longToast(mContext,result);
                    onPhotoIdetifyResult(result);
                    break;
            }
            if (mMapPresenter == null) {
                mMapPresenter = new MapTableItemPresenter(mContext, new SelectLocationService(mContext, null), null);
                mMapPresenter.setOnReceivedSelectedLocationListener(new IMapTableItemPresenter.OnReceivedSelectedLocationListener() {
                    @Override
                    public void onReceivedLocation(LatLng mapLatlng, String address) {
                        if (getEditTextViewByField1Type("X") != null && getEditTextViewByField1Type("Y") != null) {
                            getEditTextViewByField1Type("X").setText(mapLatlng.getLongitude() + "");
                            getEditTextViewByField1Type("Y").setText(mapLatlng.getLatitude() + "");
                        }

                        //如果设施控件不为空，自动查找附近设施
                        if (mPatrolSelectDevicePresenter != null && mMapPresenter != null && mMapPresenter.getMapView() != null) {
                            mPatrolSelectDevicePresenter.searchNearByDevice(mMapPresenter.getMapView(), mapLatlng.getLongitude(), mapLatlng.getLatitude());
                        }
                    }
                });
            }

            // TODO 番禺四标四实目前不需要定位暂时去除
            // mMapPresenter.startLocate();//xcl 2017-03-27 当选择完图片后再进行定位
        }
    }

    public void doCopyPhoto() {
        try {
            PhotoButtonUtil.openPhotoCopy(mContext, mPhotoIntentData, mPhotoExtra.getFilePath());
            refreshImageAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshImageAdapter() {
        if (mPhotoExtra != null) {
            CompressPictureUtil
                    .startAsyAsyncTaskOrNot(
                            mContext,
                            mPhotoExtra.getFilePath(),
                            new CompressPictureUtil.OnCompressPictureOverListener() {
                                @Override
                                public void onCompressPictureOver(String filePath) {
                                    addPhotoAndUpdateImageAdapter();
                                }
                            });
        }
    }

    private void addPhotoAndUpdateImageAdapter() {
        if (mPhotoExtra != null) {
            Photo photo = new Photo();
            photo.setLocalPath(mPhotoExtra.getFilePath());
            photo.setPhotoPath(FileHeaderConstant.SDCARD_URL_BASE
                    + mPhotoExtra.getFilePath());
            photo.setPhotoName(mPhotoExtra.getFilename());
            photo.setPhotoTime(mPhotoExtra.getPhotoTime());
            String key = mPhotoExtra.getKey();
            List<Photo> photos = mPhotoListMap.get(key);


            if (photos != null) {
                //拼接图片名字
                String allPhotoName = mPhotoNameMap.get(key);
                if (allPhotoName != null) {
                    allPhotoName = allPhotoName + "|" + photo.getPhotoName();
                } else {
                    allPhotoName = photo.getPhotoName();
                }
                mPhotoNameMap.put(key, allPhotoName);

                photos.add(photo);
                refreshPhotoViewToLast(key);
            }

        }
    }

    /**
     * 保存所有表格项的编辑数据到本地
     * 这些表格项通过projectId标识为某个项目的
     * 通过存为 LocalTable 表示为某一次上报的内容,并且通过时间戳产生该 LocalTable 实体的唯一ID
     * by gkh
     */
    public void saveEdited() {
        List<TableItem> list = getUploadTableItems();
        //校验数据是否为空
        //2017.7.31 本地保存去掉数据完整性校验
        //2017.9.16 本地保存加上数据完整性校验
        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "存在必填项为空!");
            return;
        }

        if (list == null) return;
        //   tableDataManager.setEditedTableItemToDB(projectId, list, pList, valueMap);
        tableDataManager.setEditedTableItemToDB(projectId, list, mPhotoListMap, valueMap);

        //    ((Activity) mContext).finish();
        //   ToastUtil.shortToast(mContext, "保存数据成功!");
    }

    /**
     * 保存所有表格项的编辑数据到本地（四标四实项目专用）
     * 这些表格项通过projectId标识为某个项目的
     * 通过存为 LocalTable 表示为某一次上报的内容,并且通过时间戳产生该 LocalTable 实体的唯一ID
     * by gkh
     */
    public void saveEdited2(String recordId) {
        List<TableItem> list = getUploadTableItems();
        //校验数据是否为空
        //2017.7.31 本地保存去掉数据完整性校验
        //2017.9.16 本地保存加上数据完整性校验
        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "存在必填项为空!");
            return;
        }

        if (list == null) return;
        //   tableDataManager.setEditedTableItemToDB(projectId, list, pList, valueMap);
        //手动设置address_id,u_id,t_id,d_id
        if ("1".equals(mParentRecordType)) {  //父记录类型是任务或地址，则当前是新增栋
            valueMap.put("address_id", mParentRecordId);
            TableItem tableItem = getTableItemByField1("address_id");
            if (tableItem != null) {
                tableItem.setValue(mParentRecordId);
            }
        } else if ("2".equals(mParentRecordType)) {  //父记录类型是栋， 则当前是新增套
            valueMap.put("d_id", mParentRecordId);

            TableItem tableItem = getTableItemByField1("d_id");
            if (tableItem != null) {
                tableItem.setValue(mParentRecordId);
            }
        } else if ("3".equals(mParentRecordType)) {  //父记录类型是套，则当前是新增人口或单位
            valueMap.put("t_id", mParentRecordId);
            TableItem tableItem = getTableItemByField1("t_id");
            if (tableItem != null) {
                tableItem.setValue(mParentRecordId);
            }

        } else if ("4".equals(mParentRecordType)) {
            valueMap.put("u_id", mParentRecordId);   //如果新增人口，这里传的应该是单位ID，
            TableItem tableItem = getTableItemByField1("u_id");
            if (tableItem != null) {
                tableItem.setValue(mParentRecordId);
            }
        }
        tableDataManager.setEditedTableItemToDB2(projectId, recordId, list, mPhotoListMap, valueMap);

        //    ((Activity) mContext).finish();
        //   ToastUtil.shortToast(mContext, "保存数据成功!");
    }

    /**
     * 保存所有表格项的编辑数据到本地
     * 这些表格项通过projectId标识为某个项目的
     * 通过存为 LocalTable 表示为某一次上报的内容,并且通过时间戳产生该 LocalTable 实体的唯一ID
     *
     * @param tableKey 如果是本地再次编辑则需要传入上次的 tableKey 来代替 通过时间戳产生的唯一ID
     *                 <p>
     *                 by gkh
     */
    public void saveEdited(String tableKey) {
        List<TableItem> list = getUploadTableItems();
        //校验数据是否为空
        //2017.9.16 本地保存加上数据完整性校验
        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "存在必填项为空!");
            return;
        }


        if (list == null) return;
        //   tableDataManager.setEditedTableItemToDB(projectId, list, pList, valueMap);
        tableDataManager.setEditedTableItemToDB(tableKey, projectId, list, mPhotoListMap, valueMap);
        // ((Activity) mContext).finish();
        //ToastUtil.shortToast(mContext, "保存数据成功!");

        //    ((Activity) mContext).finish();
        //     ToastUtil.shortToast(mContext, "保存数据成功!");

    }

    /**
     * 保存编辑到本地
     *
     * @param tableName 行业表名，用于在上传的时候判断是采用商业接口还是行业接口；
     */
    @Deprecated
    public void saveEdited(String tableKey, String tableName) { //xcl 加入行业表名
        List<TableItem> list = getUploadTableItems();
        //校验数据是否为空

        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "存在必填项为空!");
            return;
        }

        if (list == null) return;
        //   tableDataManager.setEditedTableItemToDB(projectId, list, pList, valueMap);
        tableDataManager.setEditedTableItemToDB(tableName, tableKey, projectId, list, mPhotoListMap, valueMap);
        ((Activity) mContext).finish();
        ToastUtil.shortToast(mContext, "保存数据成功!");
    }

    /**
     * 校验数据是否有效
     *
     * @param
     * @return
     */
    public boolean checkIfValidate() {
        if (valueMap == null) {
            return false;
        }
        String type = null;
        TableItem tableItem = null;
        for (Map.Entry<String, String> m : valueMap.entrySet()) {

            //TODO 如果表单值为空，则不进行非空校验，目前只用于番禺四标四实
            if (TextUtils.isEmpty(m.getValue())) {
                continue;
            }

            // type = getValidateType(m.getKey());
            tableItem = getTableItemByField1(m.getKey());

            if (tableItem == null) {
                continue;
            }

            if (tableItem.getHtml_name() != null
                    && tableItem.getHtml_name().equals("证件类型")) {
                if (tableItem.getControl_type().equals(ControlType.SPINNER)) {
                    //表单项为下拉框，value为数据字典编码，从本地数据库中查找对应的显示值
                    cardTypeName = getSpinnerValueByCode(m.getValue());
                } else {
                    cardTypeName = m.getValue();
                }
            }

            //身份证号码校验（大陆）
            if (tableItem.getControl_type().equals(ControlType.PHOTO_IDENTIFY_TEXT_FIELD)) {
                if (tableItem.getIf_required() != null) {
                    /*if (tableItem.getIf_required().equals(RequireState.REQUIRE)
                            && TextUtils.isEmpty(m.getValue())) {
                        ToastUtil.shortToast(mContext, "证件号码不能为空");
                        return false;
                    }*/
                }
                if (!TextUtils.isEmpty(cardTypeName)
                        && !TextUtils.isEmpty(m.getValue())
                        && cardTypeName.equals("身份证")
                        && !IdcardValidator.isValidatedAllIdcard(m.getValue())) {
                    ToastUtil.shortToast(mContext, "身份证号码不符合规范");
                    return false;
                }
                //  continue;
            }
            type = tableItem.getValidate_type();
            if (type != null) {
                // if (tableItem.getIf_required() != null) {
                //    if (tableItem.getIf_required().equals(RequireState.REQUIRE)) {
                if (!TextUtils.isEmpty(m.getValue())) {
                    if (type.equals(ValidateType.NUMBER)) {
                        if (!ValidateUtils.isPositiveFloat(m.getValue()) && !ValidateUtils.isNegativeFloat(m.getValue())) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "的值" + "必须是数字类型!");
                            return false;
                        }
                    } else if (type.equals(ValidateType.INTEGER)) {
                        if (!ValidateUtils.isInteger(m.getValue())) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "的值" + "必须是整数!");
                            return false;
                        }
                    } else if (type.equals(ValidateType.STRING)) {
                        if (!ValidateUtils.isString(m.getValue())) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "的值" + "格式不正确!");
                            return false;
                        }
                    } else if (type.equals(ValidateType.PHONE)) {  //手机号码
                        if (!ValidateUtils.isMobileNO(m.getValue())) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "的号码格式" + "格式不正确!");
                            return false;
                        }
                    } else if (type.equals(ValidateType.TEL_PHONE)) { //固定电话号码
                        if (!ValidateUtils.isTelPhoneNO(m.getValue())) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "的号码格式" + "格式不正确!");
                            return false;
                        }
                    } else if (type.equals(ValidateType.IDENTIFY)) { //身份证验证
                        //如果证件号码与证件类型同在,则当证件类型不是身份证时不校验号码
                        TableItem item = getTableItemByField1("zjlx");
                        String zjlx = null;
                        if (item != null && tableItem.getHtml_name().equals("证件号码")) {
                            if (valueMap.containsKey("zjlx")) {
                                zjlx = valueMap.get("zjlx");
                            }
                        }
                        if (zjlx != null && !zjlx.equals("身份证")) {
                            continue;
                        }


                        if (!IdcardValidator.isValidatedAllIdcard(m.getValue())) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "的号码格式" + "格式不正确!");
                            return false;
                        }

                    } else if (type.equals(ValidateType.ALL_NUMBER)) {//实数型 包括整数和小数
                        if (!ValidateUtils.isPositiveFloat(m.getValue()) &&
                                !ValidateUtils.isNegativeFloat(m.getValue()) &&
                                !ValidateUtils.isInteger(m.getValue())) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "的值" + "必须是数字类型!");
                            return false;
                        }
                    } else if (type.equals(ValidateType.ALL_PHONE)) {//手机号码或固定电话号码
                        if (!ValidateUtils.isMobileNO(m.getValue()) && !ValidateUtils.isTelPhoneNO(m.getValue())) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "的号码格式" + "格式不正确!");
                            return false;
                        }
                    }
                }
                //  }
                // }
            }
            tableItem = null;
            type = null;
        }
        return true;
    }

    /**
     * 根据数据字典编码，从本地数据库中查找对应的显示值
     *
     * @param code
     * @return
     */
    public String getSpinnerValueByCode(String code) {
        List<DictionaryItem> dictionaryItems = new TableDBService(mContext).getDictionaryByCode(code);
        if (ListUtil.isEmpty(dictionaryItems)) {
            return "";
        }
        DictionaryItem dictionaryItem = dictionaryItems.get(0);
        return dictionaryItem.getName();
    }

    public String getValidateType(String field1) {
        String type = null;
        for (TableItem item : tableItems) {
            if (item.getField1().equals(field1)) {
                type = item.getValidate_type();
                break;
            }
        }
        return type;
    }

    /**
     * 提交编辑到服务器
     */
    public void uploadEdit() {
        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);

        String url;

        if (TextUtils.isEmpty(tableName) || "DEFAULT".equals(tableName)) { //xcl 行业表为空，采用商业接口上传
            url = serverUrl + "rest/report/saveRpt";
        } else { //xcl 行业表不为空，采用行业接口上传
            url = serverUrl + "rest/report/save";
        }
        //用行业接口上传
        url = serverUrl + "rest/report/save";

        //xcl 2017-08-26 再次修改接口名称
        //    url = serverUrl + "rest/report/saveMoreTable";


        List<TableItem> list = getUploadTableItems();
        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "存在必填项为空!");
            return;
        }

        if (list == null) return;
        if (!checkIfValidate()) {
            return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "提示", "正在提交数据");
        if (isEditingFeatureLayer) {
            List<Photo> photoList = new ArrayList<>();
            if (mPhotoListMap != null && mPhotoListMap.size() > 0) {
                Collection<List<Photo>> values = mPhotoListMap.values();
                for (List<Photo> photos : values) {
                    if (ListUtil.isEmpty(photos)) {
                        continue;
                    }
                    photoList.addAll(photos);
                }
            }
            EditLayerService2.applyEdit(mContext, featueLayerUrl, graphic, OLD_LAYER_OBJECTID_FIELD_IN_NEW,
                    geometry, valueMap, photoList, new CallbackListener<FeatureEditResult[][]>() {
                        @Override
                        public void onCallback(FeatureEditResult[][] featureEditResults) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.shortToast(mContext, "提交成功!");
                                    progressDialog.dismiss();
                                    if (uploadEditCallback == null) {
                                        ((Activity) mContext).finish();
                                    } else {
                                        uploadEditCallback.onSuccess(null);
                                    }

                                }
                            });

                        }

                        @Override
                        public void onError(Throwable throwable) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.shortToast(mContext, "提交失败!");
                                    progressDialog.dismiss();
                                    if (uploadEditCallback == null) {
                                    } else {
                                        uploadEditCallback.onFail(null);
                                    }
                                }
                            });

                        }
                    });
            /*EditLayerService.applyEdit(mContext, featueLayerUrl, graphic, geometry, valueMap, photoList, new CallbackListener<FeatureEditResult[][]>() {
                @Override
                public void onCallback(FeatureEditResult[][] featureEditResults) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.shortToast(mContext, "提交成功!");
                            progressDialog.dismiss();
                            if (uploadEditCallback == null) {
                                ((Activity) mContext).finish();
                            } else {
                                uploadEditCallback.onSuccess(null);
                            }

                        }
                    });

                }

                @Override
                public void onError(Throwable throwable) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.shortToast(mContext, "提交失败!");
                            progressDialog.dismiss();
                            if (uploadEditCallback == null) {
                            } else {
                                uploadEditCallback.onFail(null);
                            }
                        }
                    });

                }
            });*/
        } else {

            /**
             * 写死绝对路径
             */
            TableItem fjzd = getTableItemByField1("file");
            if (fjzd != null && fjzd.getControl_type().equals(ControlType.ABSOLUTE_IMAGE_PICKER)) {
                String fjzd1 = mPhotoNameMap.get("file");
                String absolutePhotoPath = "";
                if (fjzd1 != null) {
                    String[] split = fjzd1.split("\\|");
                    String baseUrl = "http://39.108.72.145:8081/img/";
                    for (int i = 0; i < split.length; i++) {
                        if (i == split.length - 1) {
                            absolutePhotoPath += baseUrl + split[0];
                        } else {
                            absolutePhotoPath += baseUrl + split[0] + "|";
                        }
                    }
                    valueMap.put("file", absolutePhotoPath);
                }
            }

            tableDataManager.uploadTableItems(url, projectId, valueMap, list, new TableNetCallback() {
                @Override
                public void onSuccess(Object data) {
                    String result = null;
                    try {
                        result = ((ResponseBody) data).string();
                    } catch (IOException e) {
                        progressDialog.dismiss();
                        ToastUtil.longToast(mContext, e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    UploadTableItemResult uploadTableItemResult = gson.fromJson(result, UploadTableItemResult.class);
                    if (uploadTableItemResult.getSuccess().equals("true")) {
                        ToastUtil.shortToast(mContext, "提交成功!");
                    }

                    //如果是多表任务,则会返回记录ID
                /*
                if(uploadTableItemResult.getId() != null){
                    ServerRecord taskRecord = new ServerRecord();
                    taskRecord.setId(uploadTableItemResult.getId());
                    taskRecord.setProjectId(projectId);
                    if (!TextUtils.isEmpty(tableName)){
                        taskRecord.setTasktableName(tableName);
                    }
                    //发送消息记录下当前任务已经上传到服务器的记录
                    EventBus.getDefault().post(new AddUploadRecordEvent(taskRecord));
                }
                */

                    progressDialog.dismiss();
                    if (mPhotoListMap == null || mPhotoListMap.size() <= 0) {
                        afterUpload();
                        ((Activity) mContext).finish();
                    } else {
                        if (uploadTableItemResult.getPatrolId() != null) {
                            // uploadFiles(uploadTableItemResult.getPatrolId());
                            String prefix = "";
                            uploadFiles(uploadTableItemResult.getPatrolId(), prefix, null);
                        }
                    }
                }

                @Override
                public void onError(String msg) {
                    progressDialog.dismiss();
                    ToastUtil.longToast(mContext, msg);
                }


            });
        }
    }

    /**
     * 提交编辑到服务器（四标四实专用）
     * <p>
     * 多表任务版本
     */
    public void uploadEditMultiWithUserName(final Callback1<Boolean> successCallback) {
        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);

        String url;

        /*
        if (TextUtils.isEmpty(tableName) || "DEFAULT".equals(tableName)) { //xcl 行业表为空，采用商业接口上传
            url = serverUrl + "rest/report/saveRpt";
        } else { //xcl 行业表不为空，采用行业接口上传
            url = serverUrl + "rest/report/save";
        }
        */
        //url = serverUrl + "rest/report/saveMoreTable2"; //xcl 8.31 修改URL
        url = serverUrl + "am/report/saveMoreTable2"; //xcl 9.9  修改URL

        List<TableItem> list = getUploadTableItems();

        //2017.9.16 加上数据完整性校验
        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "存在必填项为空!");
            return;
        }


        if (list == null) return;
        if (!checkIfValidate()) {
            return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "提示", "正在提交数据");
        String username = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        if ("1".equals(mParentRecordType)) {  //父记录类型是任务或地址，则当前是新增栋
            valueMap.put("address_id", mParentRecordId);
        } else if ("2".equals(mParentRecordType)) {  //父记录类型是栋， 则当前是新增套
            valueMap.put("d_id", mParentRecordId);
        } else if ("3".equals(mParentRecordType)) {  //父记录类型是套，则当前是新增人口或单位
            valueMap.put("t_id", mParentRecordId);
        } else if ("4".equals(mParentRecordType)) {
            valueMap.put("u_id", mParentRecordId);   //如果新增人口，这里传的应该是单位ID，
        }

        if (dataStataAfterUpload != null) {
            valueMap.put("data_stata", dataStataAfterUpload);
        }
        // 提交时间 (遵循之前的格式)
        String uploadTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        valueMap.put("fill_time", uploadTime);
        valueMap.put("fill_date", uploadTime);

        tableDataManager.uploadTableItemsWithUserName(url, username, taskId, recordId, projectId, valueMap, list, mPhotoNameMap, new TableNetCallback() {//8.31 xcl 加入username
            @Override
            public void onSuccess(Object data) {
                String result = null;
                try {
                    result = ((ResponseBody) data).string();
                } catch (IOException e) {
                    progressDialog.dismiss();
                    ToastUtil.longToast(mContext, e.getLocalizedMessage());
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                UploadTableItemResult uploadTableItemResult = gson.fromJson(result, UploadTableItemResult.class);
                if (uploadTableItemResult.getSuccess().equals("true")) {
                    ToastUtil.shortToast(mContext, "提交成功!");
                }

                //如果是多表任务,则会返回记录ID
                if (uploadTableItemResult.getId() != null) {
                    ClientTaskRecord clientTaskRecord = new ClientTaskRecord();
                    clientTaskRecord.setId(uploadTableItemResult.getId());
                    clientTaskRecord.setProjectId(projectId);
                    if (!TextUtils.isEmpty(tableName)) {
                        clientTaskRecord.setProjectName(tableName);
                    }
                    //发送消息记录下当前任务已经上传到服务器的记录
                    EventBus.getDefault().post(new AddUploadRecordEvent(clientTaskRecord));
                }

                progressDialog.dismiss();
                if (mPhotoListMap == null || mPhotoListMap.size() <= 0 || isPhotoEmpty()) {
                    afterUpload();
                    if (successCallback != null) {
                        successCallback.onCallback(true);
                    } else {
                        ((Activity) mContext).finish();
                    }

                } else {
                    if (uploadTableItemResult.getId() != null) {
                        // uploadFiles(uploadTableItemResult.getPatrolId());
                        String prefix = "";
                        uploadFiles(uploadTableItemResult.getId(), prefix, successCallback);
                    }
                }
            }

            @Override
            public void onError(String msg) {
                progressDialog.dismiss();
                ToastUtil.longToast(mContext, msg);
            }


        });
    }

    /**
     * 删除记录（四标四实专用）
     * <p>
     * 多表任务版本
     */
    public void deleteMultiWithUserName(final Callback1<Boolean> successCallback) {
        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);

        String url;

        /*
        if (TextUtils.isEmpty(tableName) || "DEFAULT".equals(tableName)) { //xcl 行业表为空，采用商业接口上传
            url = serverUrl + "rest/report/saveRpt";
        } else { //xcl 行业表不为空，采用行业接口上传
            url = serverUrl + "rest/report/save";
        }
        */
        //url = serverUrl + "rest/report/saveMoreTable2"; //xcl 8.31 修改URL
        url = serverUrl + "am/report/saveMoreTable2"; //xcl 9.9  修改URL

        List<TableItem> list = getUploadTableItems();

        /*
        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "存在必填项为空!");
            return;
        }
        */

        if (list == null) return;
        if (!checkIfValidate()) {
            return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "提示", "正在提交数据");
        String username = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        if ("1".equals(mParentRecordType)) {  //父记录类型是任务或地址，则当前是新增栋
            valueMap.put("address_id", mParentRecordId);
        } else if ("2".equals(mParentRecordType)) {  //父记录类型是栋， 则当前是新增套
            valueMap.put("d_id", mParentRecordId);
        } else if ("3".equals(mParentRecordType)) {  //父记录类型是套，则当前是新增人口或单位
            valueMap.put("t_id", mParentRecordId);
        } else if ("4".equals(mParentRecordType)) {
            valueMap.put("u_id", mParentRecordId);   //如果新增人口，这里传的应该是单位ID，
        }

        if (dataStataAfterUpload != null) {
            valueMap.put("data_stata", dataStataAfterDeleted); //与提交的区别
        }

        tableDataManager.uploadTableItemsWithUserName(url, username, taskId, recordId, projectId, valueMap, list, mPhotoNameMap, new TableNetCallback() {//8.31 xcl 加入username
            @Override
            public void onSuccess(Object data) {
                String result = null;
                try {
                    result = ((ResponseBody) data).string();
                } catch (IOException e) {
                    progressDialog.dismiss();
                    ToastUtil.longToast(mContext, e.getLocalizedMessage());
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                UploadTableItemResult uploadTableItemResult = gson.fromJson(result, UploadTableItemResult.class);
                if (uploadTableItemResult.getSuccess().equals("true")) {
                    ToastUtil.shortToast(mContext, "提交成功!");
                }

                //如果是多表任务,则会返回记录ID
                if (uploadTableItemResult.getId() != null) {
                    ClientTaskRecord clientTaskRecord = new ClientTaskRecord();
                    clientTaskRecord.setId(uploadTableItemResult.getId());
                    clientTaskRecord.setProjectId(projectId);
                    if (!TextUtils.isEmpty(tableName)) {
                        clientTaskRecord.setProjectName(tableName);
                    }
                    //发送消息记录下当前任务已经上传到服务器的记录
                    EventBus.getDefault().post(new AddUploadRecordEvent(clientTaskRecord));
                }

                progressDialog.dismiss();
                if (mPhotoListMap == null || mPhotoListMap.size() <= 0 || isPhotoEmpty()) {
                    afterUpload();
                    if (successCallback != null) {
                        successCallback.onCallback(true);
                    } else {
                        ((Activity) mContext).finish();
                    }

                } else {
                    if (uploadTableItemResult.getId() != null) {
                        // uploadFiles(uploadTableItemResult.getPatrolId());
                        String prefix = "";
                        uploadFiles(uploadTableItemResult.getId(), prefix, successCallback);
                    }
                }
            }

            @Override
            public void onError(String msg) {
                progressDialog.dismiss();
                ToastUtil.longToast(mContext, msg);
            }


        });
    }

    /**
     * 判断图片集里面是否只有键  没有图片内容
     *
     * @return
     */
    public boolean isPhotoEmpty() {
        boolean isEmpty = true;
        for (Map.Entry<String, List<Photo>> entry : mPhotoListMap.entrySet()) {
            //有一个值则不为空,要上传
            if (!entry.getValue().isEmpty()) {
                isEmpty = false;
                break;
            }
        }

        return isEmpty;
    }

    /**
     * 提交编辑到服务器
     */
    public void uploadEdit(final Callback1<Boolean> callback1) {
        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);

        String url;

        if (TextUtils.isEmpty(tableName) || "DEFAULT".equals(tableName)) { //xcl 行业表为空，采用商业接口上传
            url = serverUrl + "rest/report/saveRpt";
        } else { //xcl 行业表不为空，采用行业接口上传
            url = serverUrl + "rest/report/save";
        }

        List<TableItem> list = getUploadTableItems();
        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "存在必填项为空!");
            return;
        }

        if (list == null) return;
        if (!checkIfValidate()) {
            return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "提示", "正在提交数据");
        tableDataManager.uploadTableItems(url, projectId, valueMap, list, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                String result = null;
                try {
                    result = ((ResponseBody) data).string();
                } catch (IOException e) {
                    progressDialog.dismiss();
                    ToastUtil.longToast(mContext, e.getLocalizedMessage());
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                UploadTableItemResult uploadTableItemResult = gson.fromJson(result, UploadTableItemResult.class);
                if (uploadTableItemResult.getSuccess().equals("true")) {
                    ToastUtil.shortToast(mContext, "提交成功!");
                }
                progressDialog.dismiss();
                if (mPhotoListMap == null || mPhotoListMap.size() <= 0) {

                    if (callback1 == null) {
                        afterUpload();
                        ((Activity) mContext).finish();
                    } else {
                        callback1.onCallback(true);
                    }

                } else {
                    if (uploadTableItemResult.getPatrolId() != null) {
                        // uploadFiles(uploadTableItemResult.getPatrolId());
                        String prefix = "";
                        uploadFiles(uploadTableItemResult.getPatrolId(), prefix, callback1);
                    }
                }
            }

            @Override
            public void onError(String msg) {
                progressDialog.dismiss();
                ToastUtil.longToast(mContext, msg);
                if (callback1 != null) {
                    callback1.onCallback(false);
                }
            }


        });
    }

    /**
     * 检查是否存在必填项空值的情况
     *
     * @return
     */
    public boolean checkIfRequireEmpty() {
        boolean unfinish = false;
        //先检查一般表格模板项
        for (TableItem tableItem : tableItems) {
            if (tableItem.getIf_required() != null) {
                if (tableItem.getIf_required().equals(RequireState.REQUIRE)) {
                    if (valueMap.containsKey(tableItem.getField1())) {
                        String value = valueMap.get(tableItem.getField1());
                        if (value == null || value.isEmpty()) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "填写值为空");
                            unfinish = true;
                            break;
                        }
                    }
                }
            }
        }
        //再检查特殊项
        if (!unfinish) {
            unfinish = checkSpecialItemRequireEmpty();
        }
        return unfinish;
    }

    /**
     * 检查特殊表格模型版项是否为空
     *
     * @return
     */
    public boolean checkSpecialItemRequireEmpty() {
        boolean unfinish = false;
        if (isShowSpecilTableItem(ControlType.IMAGE_PICKER)) {
            if (isSpecialTableItemRequire(ControlType.IMAGE_PICKER)) {
                if (mPhotoListMap != null) {
                    for (List<Photo> photos : mPhotoListMap.values()) {
                        if (photos.isEmpty()) {
                            unfinish = true;
                            break;
                        }
                    }
                }
            }
        }
        return unfinish;
    }

    public void openPouupWindow(String key) {
        showBottomsheetDialog(key);//xcl 2017-03-29 改成使用BottomsheetDialog
    }

    /**
     * 上传图片
     *
     * @param callback 回调，如果成功,返回true; 如果失败,返回false;
     */
    public void uploadFiles(String patrolCode, String prefix, final Callback1<Boolean> callback) {
        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);
        //xcl 2017-08-14 旧接口
        String url = serverUrl + "rest/upload/add";

        //这里图片上传已经兼容不了老版本了
//        String url = serverUrl + "rest/enclosureUpload/adds";
        url = url + "/" + patrolCode;
        //        String url = serverUrl +
        //                "am/system/add2";
        //        url = url + "/" + patrolCode; //todo 胜洪修改，未知原来接口

        // String url = serverUrl + "rest/enclosureUpload/uploadFile";

        //   http://210.21.98.71:8088/agweb14/rest/upload/add/{patrolCode}
        if (mPhotoListMap == null || mPhotoListMap.size() <= 0) {
            ((Activity) mContext).finish();
            return;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "提示", "正在提交照片");
        tableDataManager.uploadPhotos(url, mPhotoListMap, prefix, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                ToastUtil.shortToast(mContext, "照片上传成功!");
                afterUpload();
                progressDialog.dismiss();
                if (callback != null)
                    callback.onCallback(null);
                else
                    ((Activity) mContext).finish();
            }

            @Override
            public void onError(String msg) {
                progressDialog.dismiss();
                ToastUtil.shortToast(mContext, "上传照片失败！");
                if (callback != null) {
                    callback.onCallback(false);
                }
            }
        });
    }

    /**
     * 再编辑模式,编辑后删除本地保存数据
     */
    public void afterUpload() {
        //刷新历史提交列表
        EventBus.getDefault().post(new RefreshHistoryEvent());

        if (tableKey == null) return;
        if (tableState == TableState.REEDITNG) {
            tableDataManager.deleteEditedTableItemsFromBD(tableKey, new TableDBCallback() {
                @Override
                public void onSuccess(Object data) {
                    clearPhoto();

                    //刷新本地列表
                    EventBus.getDefault().post(new RefreshLocalEvent());


                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }

    /**
     * 清除缓存图片
     */
    public void clearPhoto() {
        if (mPhotoListMap.size() <= 0) return;
        List<String> pathList = new ArrayList<>();
        for (Map.Entry<String, List<Photo>> entry : mPhotoListMap.entrySet()) {
            List<Photo> photoList = entry.getValue();
            for (Photo photo : photoList) {
                pathList.add(photo.getLocalPath());
            }
        }

        if (pathList.isEmpty()) return;
        //  DelPhotoUtils.delPhoto(pathList);
    }

    //    @NeedPermission(permissions = {Manifest.permission.CAMERA})
    public void showBottomsheetDialog(final String key) {
        /*PermissionsUtil2.getInstance()
                .requestPermissions(
                        (Activity) mContext,
                        "需要相机权限才能正常工作，请点击确定允许", 101,
                        new PermissionsUtil2.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                showBottomsheetDialogWithCheck(key);
                            }
                        },
                        Manifest.permission.CAMERA);*/

        PermissionsUtil.getInstance()
                .requestPermissions(
                        (Activity) mContext,
                        new PermissionsUtil.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                showBottomsheetDialogWithCheck(key);
                            }

                            @Override
                            public void onPermissionsDenied(List<String> perms) {

                            }
                        },
                        Manifest.permission.CAMERA);
    }

    private void showBottomsheetDialogWithCheck(final String key) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        View popupWindowView = LayoutInflater.from(mContext).inflate(R.layout.bottom_pop, null);
        ImageButton camera = (ImageButton) popupWindowView.findViewById(R.id.camera);
        ImageButton photo = (ImageButton) popupWindowView.findViewById(R.id.photo);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPhotoExtra = PhotoButtonUtil.registTakePhotoButton((Activity) mContext, HSPVFileUtil.getPathPhoto());
                mPhotoExtra.setKey(key);
                bottomSheetDialog.dismiss();


          /*      *//*****************番禺四标四实个性化部分***********************//*
                if (key.equals("photo")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).setMessage("请拍摄正面免冠照片")
                            .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPhotoExtra = PhotoButtonUtil.registTakePhotoButton((Activity) mContext, HSPVFileUtil.getPathPhoto());
                                    mPhotoExtra.setKey(key);
                                    bottomSheetDialog.dismiss();
                                }
                            }).show();
                    *//*****************番禺四标四实个性化部分***********************//*

                } else {
                    mPhotoExtra = PhotoButtonUtil.registTakePhotoButton((Activity) mContext, HSPVFileUtil.getPathPhoto());
                    mPhotoExtra.setKey(key);
                    bottomSheetDialog.dismiss();
                }*/

            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoExtra = PhotoButtonUtil.registOpenPhotoButton((Activity) mContext, HSPVFileUtil.getPathPhoto());
                mPhotoExtra.setKey(key);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(popupWindowView);
        bottomSheetDialog.show();
    }

    /**
     * 显示选择时间的控件
     *
     * @param btn
     */
   /* private void showTimePicker(final Button btn, final ViewGroup view) {
        final StringBuffer time = new StringBuffer();
        //获取Calendar对象，用于获取当前时间
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //实例化TimePickerDialog对象
        final TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            //选择完时间后会调用该回调函数
            @Override
            public void onTimeSet(TimePicker datePicker, int hourOfDay, int minute) {
                time.append(" " + hourOfDay + ":" + minute + ":00");
                //设置显示最终选择的时间
                btn.setText(time);
                ((EditText) view.findViewById(R.id.et_)).setText(time);
            }
        }, hour, minute, true);
        //实例化DatePickerDialog对象
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            //选择完日期后会调用该回调函数
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                //因为monthOfYear会比实际月份少一月所以这边要加1
                time.append(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                //选择完日期后弹出选择时间对话框
               / timePickerDialog.show();
            }
        }, year, month, day);
        //弹出选择日期对话框
        datePickerDialog.show();
    }*/
    private void showTimePicker(final Button btn, final ViewGroup view, TableItem tableItem) {
        final StringBuffer time = new StringBuffer();
        //获取Calendar对象，用于获取当前时间
        final Calendar calendar = Calendar.getInstance();
        String text = btn.getText().toString();
        if (!TextUtils.isEmpty(text)) { // 还原当前时间
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date parse = format.parse(text);
                if (parse != null) {
                    calendar.setTime(parse);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        //        if (tableItem.getHtml_name().contains("入学时间") || tableItem.getField1().toLowerCase().equals("time")) {
        //            btn.setText(year + "-" + 9 + "-" + 1); //如果是学生信息表，那么默认设置成9月1号
        //
        //            ((EditText) view.findViewById(R.id.et_)).setText(year + "-"+"9" +"-" +"1");//默认写9月1号
        //            month = 9;
        //            day = 1;
        //        }         // 2017-09-19 因为之前已经改变过默认时间了，所以不必要在点击时再次改变

        //实例化TimePickerDialog对象
        final TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            //选择完时间后会调用该回调函数
            @Override
            public void onTimeSet(TimePicker datePicker, int hourOfDay, int minute) {
                time.append(" " + hourOfDay + ":" + minute + ":00");
            }
        }, hour, minute, true);
        //实例化DatePickerDialog对象
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            //选择完日期后会调用该回调函数
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                //因为monthOfYear会比实际月份少一月所以这边要加1
                time.append(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                ((EditText) view.findViewById(R.id.et_)).setText(time);
                btn.setText(time);//设置显示最终选择的时间
                //选择完日期后弹出选择时间对话框
                // timePickerDialog.show();
            }
        }, year, month, day);
        //弹出选择日期对话框
        datePickerDialog.show();
    }

    /**
     * 获取表格状态
     *
     * @return
     */
    public TableState getTableState() {
        return tableState;
    }

    /**
     * 获取表格项ID与View的键值对Map
     *
     * @return
     */
    public Map<String, View> getMap() {
        return map;
    }

    /**
     * 获取所有的表格项TableItem
     *
     * @return
     */
    public List<TableItem> getTableItems() {
        return tableItems;
    }

    /**
     * 获取用于填充一般表格项的View容器
     *
     * @return
     */
    public ViewGroup getContainer_for_other_Items_exclude_map_and_photos() {
        return container_for_other_Items_exclude_map_and_photos;
    }

    public Context getmContext() {
        return mContext;
    }

    /**
     * 获取某一表的键
     *
     * @return
     */
    public String getTableKey() {
        return tableKey;
    }

    public void setTableKey(String tableKey) {
        this.tableKey = tableKey;
    }

    /**
     * 获取表格所属的项目ID
     * 一个项目配置一个表格
     *
     * @return
     */
    public String getProjectId() {
        return projectId;
    }

    public Callback2 getUploadEditCallback() {
        return uploadEditCallback;
    }

    public void setUploadEditCallback(Callback2 uploadEditCallback) {
        this.uploadEditCallback = uploadEditCallback;
    }

    /**
     * 设置修改表格上传值得监听器
     *
     * @param mTableValueListener
     */
    public void setTableValueListener(TableValueListener mTableValueListener) {
        this.mTableValueListener = mTableValueListener;
    }

    /**
     * 意见模板的流程节点名
     *
     * @return
     */
    public String getLink() {
        return link;
    }

    /**
     * 意见模板的流程节点名
     *
     * @param link
     */
    public void setLink(String link) {
        this.link = link;
    }

    public BasicDongInfo getBasicDongInfo() {
        return basicDongInfo;
    }

    public void setBasicDongInfo(BasicDongInfo basicDongInfo) {
        this.basicDongInfo = basicDongInfo;
    }

    public BasicDanweiInfo getBasicDanweiInfo() {
        return basicDanweiInfo;
    }

    public void setBasicDanweiInfo(BasicDanweiInfo basicDanweiInfo) {
        this.basicDanweiInfo = basicDanweiInfo;
    }

    public String getRenkouleibei() {
        return renkouleibei;
    }

    public void setRenkouleibei(String renkouleibei) {
        this.renkouleibei = renkouleibei;
    }

    /**
     * 传递过来地址进行填充（番禺四标四实项目中用于填充包含有"标准地址"字样的文本域）
     *
     * @param addressName
     */
    public void setAddressName(String addressName) {
        mAddressName = addressName;
    }

    public void setParentRecordId(String parentRecordId) {
        this.mParentRecordId = parentRecordId;
    }

    public void setParentRecordType(String parentRecordType) {
        this.mParentRecordType = parentRecordType;
    }

    public void setRoomName(String roomName) {
        this.mRoomName = roomName;
    }

    public void setParentRecordName(String parentRecordName) {
        this.mParentRecordName = parentRecordName;
    }

    public String getDongName() {
        return dongName;
    }

    public void setDongName(String dongName) {
        this.dongName = dongName;
    }

    public void setShiyouDanweiTableName(String tableName) {
        this.shiyouDanweiTableName = tableName;
    }


    public BasicRenKouInfo getBasicRenKouInfo() {
        return basicRenKouInfo;
    }

    public void setBasicRenKouInfo(BasicRenKouInfo basicRenKouInfo) {
        this.basicRenKouInfo = basicRenKouInfo;
    }

    public String getDataStataAfterUpload() {
        return dataStataAfterUpload;
    }

    public void setDataStateAfterUpload(String dataStataAfterUpload) {
        this.dataStataAfterUpload = dataStataAfterUpload;
    }

    public String getDataStataAfterDeleted() {
        return dataStataAfterDeleted;
    }

    public void setDataStateAfterDeleted(String dataStataAfterDeleted) {
        this.dataStataAfterDeleted = dataStataAfterDeleted;
    }

    /********************** xcl 番禺四标四实特制*************************/

}
