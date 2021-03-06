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
 * ?????????
 *
 * @author ????????? ???guokunhu
 * @version 1.0
 * @package ?????? ???com.augurit.agmobile.agpatrol.common.table
 * @createTime ???????????? ???17/3/13
 * @modifyBy ????????? ???guokunhu
 * @modifyTime ???????????? ???17/3/13
 * @modifyMemo ???????????????
 */
public class TableViewManager {
    private static final String NOT_EDITABLE = "A00601"; //???tableitem??????????????????????????????????????????
    /**
     * ???????????????????????????????????????
     */
    public static String OLD_LAYER_OBJECTID_FIELD_IN_NEW = "OLD_OBJECTID";
    public static boolean isEditingFeatureLayer = true;   //???????????????????????????ArcGIS?????????
    public static String featueLayerUrl = "";        //???????????????ArcGIS?????????URL
    public static Graphic graphic = null;                   //?????????????????????
    private Callback2 uploadEditCallback = null;
    public static Geometry geometry = null;                       //??????????????????
    protected TableDataManager tableDataManager;
    //????????????
    private TableState tableState;
    //??????????????????????????????
    private boolean isUpdateFromNet;
    private List<TableItem> tableItems;
    private ViewGroup mainView;
    private Map<String, View> map; //????????????
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
    private ViewGroup container_for_other_Items_exclude_map_and_photos; //???????????????????????????????????????????????????item?????????
    private View container_for_other_Items_exclude_map_and_photos_root_view;//???????????????????????????????????????????????????item?????????????????????
    private boolean ifAddedContainerForOtherItems = false; //????????????????????????????????????????????????????????????????????????item?????????
    private Callback1 patrolProgressClickListener;
    private Map<String, HorizontalScrollPhotoView> mPhotoViewMap;
    private Map<String, HorizontalScrollPhotoViewAdapter> mPhotoViewAdapterMap;
    private Map<String, List<Photo>> mPhotoListMap;
    private Map<String, String> mPhotoNameMap;
    private CustomTableListener mAddCustomTableListener;
    private TableValueListener mTableValueListener;
    private String link = "";  //??????????????????????????????
    private PatrolSelectDevicePresenterImpl mPatrolSelectDevicePresenter;
    private String tableName; //????????????,????????????????????????"DEFAULT",?????????????????????????????????????????????????????????????????????????????????
    private String patrolCode;//???????????? ???????????????????????????
    private String recordId = "";//????????????ID(???????????????,??????????????????)
    private String taskId = "";//?????????????????????????????????ID
    private String cardTypeName = "";  //????????????
    private View.OnClickListener mapButtonClickListener; //???????????????????????????????????????
    /************************************* xcl ????????????????????????*************************/

    private String mAddressName; //????????????
    private String mParentRecordId; //?????????ID
    private String mParentRecordName;
    private String mParentRecordType; //??????????????????
    private String mRoomName; //?????????/??????
    private String dongName; //?????????
    private BasicRenKouInfo basicRenKouInfo;
    private String shiyouDanweiTableName;
    private String renkouleibei; //?????????????????????????????????????????????????????????
    private BasicDanweiInfo basicDanweiInfo;
    private BasicDongInfo basicDongInfo; //???????????????????????????????????????
    private String dataStataAfterUpload;//?????????????????????????????????
    private String dataStataAfterDeleted; //????????????????????????????????????????????????
    private SelectLocationTableItem2 selectLocationTableItem2;
    private ISelectLocationTableItemPresenter selectLocationTableItemPresenter;

    /**
     * ????????????????????????????????????
     */
    private boolean dontShowCheckLocationButton = false;
    private ISelectLocationTableItemView iSelectLocationTableItemView;

    /**
     * ???????????????????????????
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

        // ???????????????????????????
        //    AppManager.getAppManager().finishAllActivity();
    }

    /**
     * ???????????????????????????
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
                            String tableName) { //xcl 2017-08-14 ??????????????????

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

        // ???????????????????????????
        //   AppManager.getAppManager().finishAllActivity();
    }

    /**
     * ????????????(????????????)
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

        // ???????????????????????????
        //  AppManager.getAppManager().finishAllActivity();
    }

    /**
     * ?????????????????????????????????????????????
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

        // ???????????????????????????
        //     AppManager.getAppManager().finishAllActivity();
    }

    /**
     * ?????????????????????????????????????????????
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
                            String tableName) { //xcl 2017-08-14 ???????????????????????????
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

        // ???????????????????????????
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
     * ????????????ID
     * ?????????????????????????????????????????????????????????
     * ????????????????????????????????????
     *
     * @param recordId
     */
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    /**
     * ???????????????????????????????????????ID
     *
     * @param taskId
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * ??????feild1???????????????
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
            if (key == null && photoPath != null && !photoPath.isEmpty()) {  // ?????????????????????  TODO ????????????????????????
                key = "photo_close";
            }
            if (mPhotoListMap.containsKey(key)) {
                mPhotoListMap.get(key).add(photo);

                //??????????????????
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
     * ????????????????????????????????????????????????????????????
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
     * ????????????????????????????????????????????????????????????
     *
     * @param listener
     */
    public void addLoadListener(TableLoadListener listener) {
        this.listeners.add(listener);
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param listener
     */
    public void setAddCustomTableItemListener(CustomTableListener listener) {
        this.mAddCustomTableListener = listener;
    }

    /**
     * ???????????????????????????View     *
     *
     * @param ifShowDeviceNameView ??????????????????id
     * @param ifShowDevicIdView    ????????????????????????
     */
    public void addDeviceView(boolean ifShowDevicIdView, boolean ifShowDeviceNameView) {
        if (!ifShowDeviceNameView && !ifShowDevicIdView) {
            return; //???????????????????????????????????????
        }
        String deviceNameKey = "";
        if (getTableItemByField1("NAME") != null) {
            deviceNameKey = getTableItemByField1("NAME").getHtml_name();
        }

        mPatrolSelectDevicePresenter = new PatrolSelectDevicePresenterImpl(mContext,
                container_for_other_Items_exclude_map_and_photos, true, false, ifShowDevicIdView, ifShowDeviceNameView);
        mPatrolSelectDevicePresenter.setDeviceNameKey(deviceNameKey);
        mPatrolSelectDevicePresenter.setUnEditable();//???????????????????????????????????????????????????????????????
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

        if (tableState == TableState.READING || tableState == TableState.REEDITNG) { //xcl 2017-04-05 ????????????????????????
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
     * ???????????????????????????View
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
//                //??????????????????????????????????????????????????????
//                if (mPatrolSelectDevicePresenter != null && mMapPresenter != null && mMapPresenter.getMapView() != null) {
//                    mPatrolSelectDevicePresenter.searchNearByDevice(mMapPresenter.getMapView(), mapLatlng.getLongitude(), mapLatlng.getLatitude());
//                }
//               /* ToastUtil.shortToast(mContext,"???????????????????????????" + mapLatlng.getLatitude() +"-???"
//                        +mapLatlng.getLongitude());*/
//                //  tv_result.setText("??????????????????:"+address);
//                // mMapPresenter.setDestinationOrLastTimeSelectLocationInLocalCoordinateSystem(mapLatlng,address);
//            }
//        });
//        mMapPresenter.showMapShortCut();
//        mainView.addView(view);
//        String address = null; //????????????
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
//                if (tableState == TableState.READING || tableState == TableState.REEDITNG) { //xcl 2017-04-05 ????????????????????????
//                    LatLng latLng = getLatLng();
//                    if (latLng == null && tableState == TableState.READING) { //???????????????????????????????????????????????????????????????
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
//                //???????????????x,y??????
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
         * ????????????
         */
        if (tableState == TableState.EDITING) {
            tableItemView = new SelectLocationEditStateTableItemView(mContext);
            selectLocationTableItemPresenter = new SelectLocationEditTableItemPresenter(mContext,
                    new SelectLocationService(mContext, null), tableItemView);
        } else if (tableState == TableState.REEDITNG) {
            /**
             * ??????????????????,???????????????????????????????????????????????????????????????
             */
            tableItemView = new SelectLocationReEditTableItemView(mContext);
            selectLocationTableItemPresenter = new SelectLocationReEditStatePresenter(mContext,
                    new SelectLocationService(mContext, null),
                    tableItemView,
                    null,
                    tableItem.getValue());
        } else if (tableState == TableState.READING) {
            /**
             * ????????????,???????????????????????????????????????????????????????????????
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
                        //??????
                        getEditTextViewByField1Type("X").setText(((Point) geometry).getX() + "");
                        //??????
                        getEditTextViewByField1Type("Y").setText(((Point) geometry).getY() + "");
                    }


                    //??????????????????????????????????????????????????????
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

                //?????????????????????????????????????????????????????????????????????????????????
                selectLocationTableItemPresenter.loadMap();

                //???????????????x,y??????
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
     * ????????????????????????
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
        String address = null; //????????????
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
                if (tableState == TableState.READING || tableState == TableState.REEDITNG) { //xcl 2017-04-05 ????????????????????????
                    LatLng latLng = getLatLng();
                    if (latLng == null && tableState == TableState.READING) { //???????????????????????????????????????????????????????????????
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


                //???????????????x,y??????
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
                //?????????????????????X,Y
                if (data != null && data.getType() == Geometry.Type.POINT) {
                    Point point = (Point) data;
                    if (getEditTextViewByField1Type("X") != null && getEditTextViewByField1Type("Y") != null) {
                        getEditTextViewByField1Type("X").setText(point.getX() + "");
                        getEditTextViewByField1Type("Y").setText(point.getY() + "");
                    }
                }
                /***************************????????????????????????????????????*********************************/
                if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                    //????????????
                    setTableItemValue("district", detailAddress.getDistrict());
                    //????????????
                    setTableItemValue("lane_way", detailAddress.getStreet());
                    setTableItemValue("jdmc", detailAddress.getStreet());
                }
            }
        });

        //?????????X,Y???????????????????????????
        if (getTableItemByField1("X") != null && getTableItemByField1("Y") != null) {
            (mMapPresenter).setEditMode(EditModeConstant.EDIT_POINT);
        } else {
            (mMapPresenter).setEditMode(EditModeConstant.EDIT_LINE);
        }

    }

    /**
     * ?????????????????????????????????????????????
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
//                //?????????????????????X,Y
//                if (data != null && data.getType() == Geometry.Type.POINT) {
//                    Point point = (Point) data;
//                    if (getEditTextViewByField1Type("X") != null && getEditTextViewByField1Type("Y") != null) {
//                        getEditTextViewByField1Type("X").setText(point.getX() + "");
//                        getEditTextViewByField1Type("Y").setText(point.getY() + "");
//                    }
//                }
//
//                /***************************????????????????????????????????????*********************************/
//                if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
//                    //????????????
//                    setTableItemValue("district", detailAddress.getDistrict());
//                    //????????????
//                    setTableItemValue("lane_way", detailAddress.getStreet());
//                    setTableItemValue("jdmc", detailAddress.getStreet());
//                }
//
//            }
//        });
//        editMapItemPresenter.showMapShortCut();
//        //?????????????????????????????????????????????
//        editMapItemPresenter.startLocate();
//        mainView.addView(view);
//        String address = null; //????????????
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
//                if (tableState == TableState.READING || tableState == TableState.REEDITNG) { //xcl 2017-04-05 ????????????????????????
//                    LatLng latLng = getLatLng();
//                    if (latLng == null && tableState == TableState.READING) { //???????????????????????????????????????????????????????????????
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
//                //???????????????x,y??????
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
//                 * ????????????????????????
//                 */
//                if (mapButtonClickListener != null) {
//                    selectLocationTableItem2.setOnButtonClickListener(mapButtonClickListener);
//                }
//            }
//        });
//
//        //?????????X,Y???????????????????????????
//        if (getTableItemByField1("X") != null && getTableItemByField1("Y") != null) {
//            editMapItemPresenter.setEditMode(EditModeConstant.EDIT_POINT);
//        } else {
//            editMapItemPresenter.setEditMode(EditModeConstant.EDIT_LINE);
//        }


        final ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_map_container_item, null);
        final ViewGroup mapContainer = (ViewGroup) view.findViewById(R.id.ll_map);

        ISelectLocationTableItemView tableItemView = null;


        /**
         * ????????????
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
             * ??????????????????,???????????????????????????????????????????????????????????????
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
             * ????????????,???????????????????????????????????????????????????????????????
             */
            tableItemView = new NoMapSelectLocationReadStateTableItemView(mContext);
            selectLocationTableItemPresenter = new SelectLocationReadStatePresenter(mContext,
                    new SelectLocationService(mContext, null),
                    tableItemView,
                    null,
                    tableItem.getValue());
            tableItemView.hideSelectLocationButton();
        }

        //?????????X,Y???????????????????????????
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
                        //??????
                        getEditTextViewByField1Type("X").setText(String.valueOf(((Point) selectedGeometry).getX()));
                        //??????
                        getEditTextViewByField1Type("Y").setText(String.valueOf(((Point) selectedGeometry).getY()));
                    }


                    /***************************????????????????????????????????????*********************************/
                    if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                        //????????????
                        setTableItemValue("district", detailAddress.getDistrict());
                        //????????????
                        setTableItemValue("lane_way", detailAddress.getStreet());
                        setTableItemValue("jdmc", detailAddress.getStreet());
                    }


                    //??????????????????????????????????????????????????????
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
                 * ??????????????????????????????????????????????????????,????????????
                 */
                if (tableState == TableState.READING) {
                    if (dontShowCheckLocationButton && selectLocationTableItemPresenter.getView() instanceof NoMapSelectLocationEditStateTableItemView) {
                        ((NoMapSelectLocationEditStateTableItemView) selectLocationTableItemPresenter.getView()).hideCheckLocationButton();
                    }
                }

                //?????????????????????????????????????????????????????????????????????????????????
                selectLocationTableItemPresenter.loadMap();
                //?????????????????????????????????????????????
                //selectLocationTableItemPresenter.startLocate();

                //???????????????x,y??????
                View x = getTableItemViewByFiled1("X");
                if (x != null) {
                    selectLocationTableItemPresenter.getView().addView(x);
                }

                View y = getTableItemViewByFiled1("Y");
                if (y != null) {
                    selectLocationTableItemPresenter.getView().addView(y);
                }


                /**
                 * ????????????????????????
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
     * ??????tableItem
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
     * ????????????????????????????????????????????????????????????
     *
     * @param onMapItemClickListener
     */
    public void setOnMapItemClickListener(final View.OnClickListener onMapItemClickListener) {
        this.mapButtonClickListener = onMapItemClickListener;
        if (selectLocationTableItemPresenter.getView() instanceof NoMapSelectLocationEditStateTableItemView) {
            ((NoMapSelectLocationEditStateTableItemView) selectLocationTableItemPresenter.getView())
                    .setOnButtonClickListener(mapButtonClickListener);
        }
        //todo ????????????????????????????????????
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
     * ????????????????????????????????????
     */
    public void setDontShowCheckLocationButton() {
        dontShowCheckLocationButton = true;
        /**
         * ??????????????????????????????????????????????????????,????????????
         */
        if (tableState == TableState.READING) {
            if (selectLocationTableItemPresenter != null &&
                    selectLocationTableItemPresenter.getView() instanceof NoMapSelectLocationEditStateTableItemView) {
                ((NoMapSelectLocationEditStateTableItemView) selectLocationTableItemPresenter.getView()).hideCheckLocationButton();
            }
        }
    }








    /**
     * ????????????
     *
     * @param address
     */
    public void setAddress(String address) {
        if (selectLocationTableItemPresenter != null) {
            selectLocationTableItemPresenter.setAddress(address);
        }
        //todo ????????????????????????????????????
        if (selectLocationTableItem2 != null) {
            selectLocationTableItem2.showEditableAddress(address);
        }

    }

    /**
     * ?????????????????????
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
     * ??????WebView?????????????????????View
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
        String address = null; //????????????
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
                if (tableState == TableState.READING || tableState == TableState.REEDITNG) { //xcl 2017-04-05 ????????????????????????
                    LatLng latLng = getLatLng();
                    if (latLng == null && tableState == TableState.READING) { //???????????????????????????????????????????????????????????????
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

                    //???????????????x,y??????
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
     * ??????????????????
     *
     * @return
     */
    private LatLng getLatLng() {
        // mMapPresenter.getView().setMapInvisible();
        //??????????????????
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
     * ?????????????????????????????????
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
     * ???????????????????????????????????????
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
     * ?????????????????????????????????????????????
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
     * ???????????????????????????
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
     * ?????????????????????????????????
     * ???:??????????????????,????????????????????????
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
     * ?????? Field1???????????????????????????
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
     * ??????HtmlName ?????????????????????TableItem
     * <p>
     * ???: HtmlName ????????????????????????????????????????????????
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
        //??????????????????EditText???????????????????????????
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
     * ?????????TableItem???????????????????????????EditText
     *
     * @param field1
     * @return
     */
    public EditText getTableItemEditTextByFiled(String field1) {
        EditText editText = null;
        View view = getTableItemViewByFiled1(field1);
        //??????????????????EditText???????????????????????????
        if (view != null) {
            editText = (EditText) view.findViewById(R.id.et_);
        }
        return editText;
    }

    /**
     * ??????Field1???????????????TableItem??????????????????View
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
     * ??????ID???????????????TableItem
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
     * ??????????????????????????????????????????
     */
    public void initDataAndView() {

        //???????????????tableItems??????null
        if (tableItems == null) {
            tableItems = new ArrayList<>();
        }
        map = new HashMap<>();

        mPhotoViewMap = new HashMap<>();
        mPhotoViewAdapterMap = new HashMap<>();

        //????????????
        if (tableState == TableState.EDITING) {
            //?????????????????????????????????
            /*
            List<ProjectTable> tables = tableDataManager.getProjectTableFromDB(projectId);
            if(tables.size() > 0){
                tableItems = tables.get(0).getTableItems();
                initSpecialControlViewBefore();
                initControlView();
                isUpdateFromNet = false;
            }
            */

            //????????????????????????
            if (isUpdateFromNet) {
                final ProgressDialog progressDialog = ProgressDialog.show(mContext, "??????", "?????????????????????");
                tableDataManager.getTableItemsFromNet(projectId, url, new TableNetCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        TableItems tmp = (TableItems) data;
                        progressDialog.dismiss();
                        if (tmp.getResult() != null) {
                            tableItems = tmp.getResult();
                            //   tableDataManager.setTableItemsToDB(tableItems);
                            //?????????????????????
                            Realm realm = Realm.getDefaultInstance();
                            ProjectTable projectTable = new ProjectTable();
                            projectTable.setId(projectId);
                            realm.beginTransaction();
                            projectTable.setTableItems(new RealmList<TableItem>(tableItems.toArray(new TableItem[tableItems.size()])));
                            realm.commitTransaction();
                            tableDataManager.setProjectTableToDB(projectTable);
                            initControlView();
                        } else {
                            //?????????????????????????????????
                            ToastUtil.shortToast(mContext, "????????????????????????????????????!");
                            tableItems = getLocalTableItems();
                            if (tableItems != null && tableItems.size() > 0) {
                                initControlView();
                            }
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        progressDialog.dismiss();
                        ToastUtil.shortToast(mContext, "????????????????????????????????????!");
                        tableItems = getLocalTableItems();
                        if (tableItems != null && tableItems.size() > 0) {
                            initControlView();
                        }
                    }
                });
            }
        } else if (tableState == TableState.READING) {
            //????????????
            if (tableItems != null) {
                initControlView();
            }
        } else if (tableState == TableState.REEDITNG) {
            //??????????????????
            if (tableItems != null) {
                initControlView();
            }
        }
    }

    //???????????????????????????????????????????????????
    public List<TableItem> getLocalTableItems() {
        List<TableItem> tableItems = null;
        //?????????????????????????????????
        List<ProjectTable> tables = tableDataManager.getProjectTableFromDB(projectId);
        if (tables.size() > 0) {
            tableItems = tables.get(0).getTableItems();
            initControlView();
            isUpdateFromNet = false;
        }
        return tableItems;
    }

    /**
     * ????????????????????????View?????????
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

        //??????????????????
        if (listener != null) {
            listener.onFinishedLoad();
        }
        if (listeners != null && !listeners.isEmpty()) {
            for (TableLoadListener loadListener : listeners) {
                loadListener.onFinishedLoad();
            }
        }

        //?????? ????????????
        // addQRScanBtn(null);
    }

    /**
     * ?????????????????????
     */
    public void sortTableItem() {
        if (tableItems != null) {
            /*for (int i = 0; i < tableItems.size(); i++) {
                if (tableItems.get(i).getDisplay_order() == 0) {
                    //?????????????????????????????????1000000,?????????????????????
                    tableItems.get(i).setDisplay_order(1000000);
                }
            }*/

            //??????????????????
            Collections.sort(tableItems, new Comparator<TableItem>() {
                @Override
                public int compare(TableItem tableItem, TableItem t1) {
                    return (tableItem.getDisplay_order() - t1.getDisplay_order());
                }
            });

        }
    }

    /**
     * ???????????????????????????View
     * (?????????????????????)
     *
     * @param tableItem
     */
    public void addTableItemView(final TableItem tableItem) {
        if (tableItem.getControl_type() == null) return;
        initContainerForItemsOtherThanMapAndPhotos();
        if (tableItem.getControl_type().equals(ControlType.TEXT_FIELD)) { //??????????????????
            addTextItem(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.DATE)) {  //????????????
            addDateItem(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.SPINNER)) { //????????????
            getSpinnerItemData(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.CHEXK_BOX)) {
            // getCheckItemData(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.TEXT_AREA)) { //???????????????
            addTextAreaItem(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.AUTO_COMPLETE_LOCAL)) { //??????????????????
            addAutoCompleteLocal(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.AUTO_COMPLETE_NET)) { //??????????????????
            addAutoCompleteNet(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.IMAGE_PICKER)) { //??????????????????
            if (tableItem.getIf_hidden() != null && tableItem.getIf_hidden().equals(ControlState.VISIBLE)) {
                // initHorizontalScrollPhotoView(tableItem);
                addPhotoViewItem(tableItem);
            }
        } else if (tableItem.getControl_type().equals(ControlType.MAP_PICKER)) { //????????????
            if (tableItem.getIf_hidden() != null && tableItem.getIf_hidden().equals(ControlState.VISIBLE)) {
                addMapView(tableItem);
                //addEditMapFeatureView();
            }
        } else if (tableItem.getControl_type().equals(ControlType.WEB_MAP_PICKER)) { //webview????????????
            if (tableItem.getIf_hidden() != null && tableItem.getIf_hidden().equals(ControlState.VISIBLE)) {
                addWebViewMapView();
                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
                sharedPreferencesUtil.setString("firstUrl", tableItem.getFirst_correlation());
                sharedPreferencesUtil.setString("twoUrl", tableItem.getThree_correlation());
            }
        } else if (tableItem.getControl_type().equals(ControlType.PATROL_PROGRESS)) {  //????????????
            if (tableItem.getIf_hidden() != null && tableItem.getIf_hidden().equals(ControlState.VISIBLE)) {
                addPatrolProgress(tableItem);
            }
        } else if (tableItem.getControl_type().equals(ControlType.OPINION_TEMPLATE)) {  //????????????
            if (tableItem.getIf_hidden() != null && tableItem.getIf_hidden().equals(ControlState.VISIBLE)) {
                if (tableState == TableState.EDITING
                        || tableState == TableState.REEDITNG) {
                    addOpinionTemplateEditView(tableItem);
                } else {
                    addTextItem(tableItem);
                }
            }
        } else if (tableItem.getControl_type().equals(ControlType.CUSSTOM)) { //???????????????????????????
            if (mAddCustomTableListener != null) {
                mAddCustomTableListener.addCustomTableItems(tableItem, this);
            }
        } else if (tableItem.getControl_type().equals(ControlType.PHOTO_IDENTIFY_TEXT_FIELD)) {//?????????????????????
            addTextItemForIdentify(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.QR_SCAN_BTN)) { //???????????????
            //            addQRScanBtn(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.H5_HTML)) {   // h5????????????
            addH5Html(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.EDIT_MAP_FEATURE)) { //??????????????????
            addEditMapFeatureView();
        } else if (tableItem.getControl_type().equals(ControlType.RECEIVE_MAP_GEOMETRY)) { //???????????????????????????
            addSelectLocationView(tableItem);
        } else if (tableItem.getControl_type().equals(ControlType.ABSOLUTE_IMAGE_PICKER)) { //????????????????????????
            addPhotoViewItem(tableItem);
        }

        //???????????????????????????
        if ("NAME".equals(tableItem.getField1())) {
            boolean ifShowDevicIdView = false;
            boolean ifShowDeviceNameView = true;
            addDeviceView(ifShowDevicIdView, ifShowDeviceNameView);
        }
    }

    /**
     * ??????param??????????????????
     *
     * @param param
     */
    public void initScanValue(Map<String, String> param) {
        for (TableItem item : tableItems) {
            if (item.getHtml_name().equals("????????????") && param.containsKey("??????")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("??????"));
                }
            }

            if (item.getHtml_name().equals("????????????") && param.containsKey("???????????????")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("???????????????"));
                }
            }

            if (item.getHtml_name().equals("????????????") && param.containsKey("????????????")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("????????????"));
                }
            }

            if (item.getHtml_name().equals("????????????") && param.containsKey("??????")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("??????"));
                }
            }
            if (item.getField1().equals("name") && param.containsKey("\uFEFF??????")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("\uFEFF??????"));
                }
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param item
     */
    private void addPhotoViewItem(final TableItem item) {
        if (tableState.equals(TableState.READING) && mPhotoListMap != null
                && ListUtil.isEmpty(mPhotoListMap.get(item.getField1()))) {   // ????????????????????????????????????
            return;
        }
        ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_horizontal_scroll_photo_item, null);
        TextView title = (TextView) view.findViewById(R.id.tv_);
        TextView tvTip = (TextView) view.findViewById(R.id.tv_tip);
        if (item.getHtml_name() != null) {
            title.setText(item.getHtml_name());
        }
        if (tableState.equals(TableState.READING)) {
            //            title.setText("????????????");
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
        if (!tableState.equals(TableState.READING)) { //????????????????????????????????????????????????
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
                                    ToastUtil.shortToast(mContext, "????????????!");
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
     * ??????????????????????????????????????????????????????????????????item
     */
    private void initContainerForItemsOtherThanMapAndPhotos() {
        if (container_for_other_Items_exclude_map_and_photos == null) {
            container_for_other_Items_exclude_map_and_photos_root_view = View.inflate(mContext,
                    R.layout.table_container_for_items, null);
            container_for_other_Items_exclude_map_and_photos = (ViewGroup) container_for_other_Items_exclude_map_and_photos_root_view.
                    findViewById(R.id.ll_container_for_other_Items_exclude_map_and_photos);
        }
    }

    //????????????????????????
    public void addAutoCompleteNet(TableItem tableItem) {
        final String url = tableItem.getFirst_correlation();
        if (url == null) {
            ToastUtil.longToast(mContext, "????????????????????????????????????!");
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
                    ToastUtil.longToast(mContext, "???????????????!");
                    return;
                }

                fuzzySearch(url, searshText, getAutoCompleteOtherParams(), view);
            }
        });

        if (tableState == TableState.REEDITNG || tableState == TableState.EDITING) {
            //?????????,???AutoText?????????????????????2?????????????????????????????????
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
     * ??????????????????,????????????????????????????????????????????????
     *
     * @return
     */
    public Map<String, String> getAutoCompleteOtherParams() {
        HashMap<String, String> map = new HashMap<>();
        // map.put("street","????????????");
        //   map.put("village","?????????");
        return map;
    }

    public void fuzzySearch(String url, String keyWords, Map<String, String> params, View view) {
        final AutoCompleteTextView auto = (AutoCompleteTextView) view.findViewById(R.id.autotext);
        final EditText editText = (EditText) view.findViewById(R.id.et_);

        ToastUtil.shortToast(mContext, "?????????????????????...");
        FuzzySearchService service = new FuzzySearchService(mContext);
        service.fuzzySearch(url, keyWords, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Map<String, String>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.shortToast(mContext, "????????????");
                    }

                    @Override
                    public void onNext(final List<Map<String, String>> maps) {
                        if (maps.isEmpty()) {
                            ToastUtil.shortToast(mContext, "?????????????????????");
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

                                //??????????????????????????????????????????????????????????????????????????????
                                prepareAutoCompleteNetUploadValue(editText, maps, candidates.get(i));
                            }
                        });
                        auto.showDropDown();

                    }
                });
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     * ??????:??????????????? candidates ??????id ?????????????????? editText,????????????????????????????????????id?????????
     *
     * @param editText ?????????????????????
     * @param maps     ????????????????????????map ??????
     * @param value    ????????????  ??????????????? ??????Map?????? name ??????????????????????????????
     */
    public void prepareAutoCompleteNetUploadValue(EditText editText, List<Map<String, String>> maps, String value) {
        //????????????
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

    //????????????????????????
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
     * ???????????????????????????
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
     * ???????????????
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
                    null, editText, "??????????????????" + total + "??????").setDismissErrorDelay(1500)});
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

    //xcl 2017.9.3 ??????????????????????????????????????????????????????
    public void addDateItem(final TableItem tableItem) {
        final ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_datefield_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_);
        EditText et = ((EditText) view.findViewById(R.id.et_));
        setRequireTagState(view, tableItem);
        final Button btn = (Button) view.findViewById(R.id.btn_datepicker);
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);       //????????????????????????
        month = cal.get(Calendar.MONTH);   //????????????????????????0????????????
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        second = cal.get(Calendar.SECOND);

//        btn.setText(year + "-" + (month + 1) + "-" + day);//xcl 2017-03-20 ????????????????????????

        if (tableItem.getHtml_name().contains("????????????") || tableItem.getField1().toLowerCase().equals("time")) {
            btn.setText(year + "-" + 9 + "-" + 1); //????????????????????????????????????????????????9???1???
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
                showTimePicker(btn, view, tableItem); //xcl 2017-04-05 ?????????????????????????????????TimePicker
            }
        });
        textView.setText(tableItem.getHtml_name());
        map.put(tableItem.getId(), view);
        addViewToContainer(view);
        setDateItemValue(tableItem, view);
        setTableItemState(tableItem, view);

        if (tableItem.getHtml_name().equals("????????????")
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

        if (tableItem.getHtml_name().equals("????????????")
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
     * ???????????????
     *
     * @param tableItem
     */
    public void addTextItem(TableItem tableItem) {
        //?????????????????????????????????????????????,???????????????
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
                    null, editText, "??????????????????" + total + "??????").setDismissErrorDelay(1500)});
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

        //?????????????????????
        if (tableItem.getField1().equals("OBJECT_ID") || tableItem.getField1().equals("NAME")) {
            view.setVisibility(View.GONE);
        }

        /**
         * ????????????????????????????????????????????????????????????????????????????????????
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

        // TODO 2017-09-16 ??????????????????????????????
        ifShowWebview(tableItem, view);
        // TODO 2017-09-26 ???????????????????????????
        ifShowSearchButton(tableItem, view);
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param tableItem
     * @param view
     */
    private void ifShowSearchButton(TableItem tableItem, ViewGroup view) {
        if (tableItem.getHtml_name().equals("??????????????????")) {
            final EditText editText = (EditText) view.findViewById(R.id.et_);
            final Button btn_query = (Button) view.findViewById(R.id.btn_query); //????????????
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

                    if (!btn_query.getText().equals("??????")) { //??????????????????
                        btn_query.setText("??????");
                    }
                }
            });


        }
    }

    /**
     * ????????????????????????????????????????????????????????????
     *
     * @param orgCode ????????????
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
                        btn_query.setText("??????????????????");
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
                                btn_query.setText("??????????????????");
                            }
                        });
                    } else {
                        tableActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb_loading.setVisibility(View.GONE);
                                btn_query.setVisibility(View.VISIBLE);
                                btn_query.setText("????????????");
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
                            btn_query.setText("??????????????????");
                        }
                    });
                }
            }
        });
    }

    /**
     * ?????????????????????????????????????????????
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
     * ??????EditText???????????????
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
                    && !tableItem.getValue().equals("null")) {  // 2017-09-16 ???????????????null
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

        /************************************ xcl ?????????????????????????????????????????????********************************************/

        //????????????item???????????????"????????????"/"????????????"?????????????????????????????????????????? todo ??????????????????????????????patrolcore??????
        if (tableItem.getHtml_name().contains("????????????") || tableItem.getHtml_name().contains("????????????")
                || tableItem.getField1().equals("NOW_ADD")) {
            if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                if (TextUtils.isEmpty(tableItem.getValue())) {
                    EditText editText = (EditText) view.findViewById(R.id.et_);
                    editText.setText(mAddressName);
                }
            }
        }


        //????????????item?????????????????????/????????????????????????????????????????????????
        if (tableItem.getHtml_name().contains("??????") || tableItem.getHtml_name().contains("?????????") || tableItem.getField1().equals("MPFJH")) {

            if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                if (TextUtils.isEmpty(tableItem.getValue())) {
                    EditText editText = (EditText) view.findViewById(R.id.et_);
                    editText.setText(mRoomName);
                }
            }

        }

        //????????????????????????????????????????????????"??????"
        if (tableItem.getHtml_name().contains("??????")) {
            if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                if (TextUtils.isEmpty(tableItem.getValue())) {
                    EditText editText = (EditText) view.findViewById(R.id.et_);
                    editText.setText("??????");
                }
            }
        }
        //?????????????????????
        if (tableItem.getHtml_name().contains("?????????") || tableItem.getHtml_name().contains("?????????") || tableItem.getHtml_name().contains("?????????")) {
            if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                if (TextUtils.isEmpty(tableItem.getValue())) {
                    String userName = new LoginService(mContext, AMDatabase.getInstance()).getUser().getUserName();
                    EditText editText = (EditText) view.findViewById(R.id.et_);
                    editText.setText(userName);
                }
            }
        }

        //?????????????????????
        if (tableItem.getHtml_name().contains("?????????")) {
            if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                if (TextUtils.isEmpty(tableItem.getValue())) {
                    EditText editText = (EditText) view.findViewById(R.id.et_);
                    editText.setText(dongName);
                }
            }
        }

        //????????????????????????
        if (tableItem.getHtml_name().contains("????????????") || tableItem.getField1().contains("uint_people_type")) {
            if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                if (TextUtils.isEmpty(tableItem.getValue())) {
                    EditText editText = (EditText) view.findViewById(R.id.et_);
                    editText.setText(renkouleibei);
                }
            }
        }

        //????????????????????????
        if (tableItem.getHtml_name().contains("????????????") || tableItem.getField1().contains("sch_name")) {
            if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
                if (TextUtils.isEmpty(tableItem.getValue()) && basicDanweiInfo != null) {
                    EditText editText = (EditText) view.findViewById(R.id.et_);
                    editText.setText(basicDanweiInfo.getDanweiName());
                }
            }
        }

        //????????????????????????
        if (tableItem.getHtml_name().equals("????????????")) {
            EditText editText = (EditText) view.findViewById(R.id.et_);
            editText.setEnabled(false);
        }

        //???????????????????????????
        if (tableItem.getField1().equals("ldh") && NOT_EDITABLE.equals(tableItem.getIs_edit())) {
            EditText editText = (EditText) view.findViewById(R.id.et_);
            editText.setEnabled(false);
        }

        //???????????????????????????????????????
        if (tableItem.getField1().equals("sch_name")) {
            EditText editText = (EditText) view.findViewById(R.id.et_);
            editText.setEnabled(false);
        }

        //???????????????????????????????????????
        if (tableItem.getField1().equals("fjh") && NOT_EDITABLE.equals(tableItem.getIs_edit())) {
            //            if (tableItem.getValue() != null && !tableItem.getValue().isEmpty()){
            // 2017-09-19 ?????????????????????????????????
            EditText editText = (EditText) view.findViewById(R.id.et_);
            editText.setEnabled(false);
            //            }
        }

        //????????????????????????????????????
        if (tableItem.getField1().equals("mph") && NOT_EDITABLE.equals(tableItem.getIs_edit())) {
            EditText editText = (EditText) view.findViewById(R.id.et_);
            editText.setEnabled(false);
        }

        // ????????????????????????
        if (tableItem.getField1().equals("yhqk") && NOT_EDITABLE.equals(tableItem.getIs_edit())) {
            EditText editText = (EditText) view.findViewById(R.id.et_);
            editText.setEnabled(false);
        }

        /***************  ????????????????????????   ?????????????????? -> ???????????????????????????  by gkh ******/
        if (tableState == TableState.EDITING || tableState == TableState.REEDITNG) {
            if (getBasicRenKouInfo() != null) {
                //??????
                if (getBasicRenKouInfo().getName() != null) {
                    if (tableItem.getHtml_name().contains("????????????")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getName());
                        }
                    }

                    if (tableItem.getField1().toLowerCase().equals("name")) { //??????
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getName());
                        }
                    }
                }


                //????????????
                if (getBasicRenKouInfo().getAddress() != null) {
                    if (tableItem.getHtml_name().contains("????????????")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getAddress());
                        }
                    }

                    if (tableItem.getHtml_name().contains("???????????????")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getAddress());
                        }
                    }
                }


                //????????????
                if (getBasicRenKouInfo().getTelephone() != null) {

                    //????????????
                    if (tableItem.getField1().toLowerCase().equals("tel")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getTelephone());
                        }
                    }

                    /*if (tableItem.getHtml_name().contains("?????????/????????????")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getTelephone());
                        }
                    }

                    if (tableItem.getHtml_name().contains("???????????????")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getTelephone());
                        }
                    }

                    if (tableItem.getHtml_name().contains("????????????")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getTelephone());
                        }
                    }*/
                }

                //????????????
                if (getBasicRenKouInfo().getIdenitfyNo() != null) {

                    if (tableItem.getHtml_name().contains("?????????????????????")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getIdenitfyNo());
                        }
                    }

                    if (tableItem.getField1().toLowerCase().equals("zjhm") || tableItem.getField1().toLowerCase().equals("per_id")
                            || tableItem.getField1().toLowerCase().equals("pes_id")) {//?????????????????????
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getIdenitfyNo());
                        }
                    }
                }

                //??????
                if (getBasicRenKouInfo().getHouseOwner() != null) {

                    if (tableItem.getHtml_name().contains("???????????????????????????") || tableItem.getField1().toLowerCase().equals("wz_name")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getHouseOwner());
                        }
                    }
                }


                //????????????
                if (getBasicRenKouInfo().getHouseOwnerPhone() != null) {

                    if (tableItem.getHtml_name().contains("???????????????????????????") || tableItem.getField1().toLowerCase().equals("wz_tel")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getHouseOwnerPhone());
                        }
                    }
                }

                //??????????????????
                if (getBasicRenKouInfo().getHouseOwnerIdentifyId() != null) {

                    if (tableItem.getHtml_name().contains("??????????????????")) {
                        if (TextUtils.isEmpty(tableItem.getValue())) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(getBasicRenKouInfo().getHouseOwnerIdentifyId());
                        }
                    }
                }
            }
        }


        /************************************?????????????????????????????????????????????********************************************/

        /************************************???????????????????????????????????????????????????********************************************/
        if (tableState == TableState.EDITING) {
            if ("cd18e3b2-b2e7-463b-891d-b4d4cc13d2a3".equals(projectId)) {//??????
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
            } else if ("0d1bcade-a43a-4e02-8bcd-abba4502e891".equals(projectId)) {//?????????
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
            } else if ("224074c8-7703-4f63-a391-acfe6f89df1f".equals(projectId)) {//?????????
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
            } else if ("67762e14-b3d7-4a62-8b30-1cd0e933fd5a".equals(projectId)) {//????????????
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
            } else if ("ad08522c-e38d-4e3f-a8cf-8bccb7113227".equals(projectId)) {//?????????
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
            } else if ("e7c86272-6364-4296-ba57-f76d63200cb3".equals(projectId)) {//??????
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
            } else if ("f95772c6-77d4-4f27-8efe-1a4688701896".equals(projectId)) {//??????
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
            } else if ("fff90089-5039-4b45-b697-42743c14ca8c".equals(projectId)) {//??????
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
        /************************************?????????????????????????????????????????????********************************************/
    }

    /**
     * ??????????????????
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
                        btn_identify_result.setText("???????????????????????????");
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
     * ????????????????????????????????? ??????????????????????????????????????????????????????????????????
     *
     * @param identify
     */
    private void getRenkouInfoByIDCard(String identify, final View pb_querying, final Button btn_identify_result) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = BaseInfoManager.getBaseServerUrl(mContext);
        //Request request = new Request.Builder().url(url + "sbss/info/queryPersion/" + identify).build(); //xcl 2017.09.26 ????????????
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
                        btn_identify_result.setText("????????????????????????");

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
                                btn_identify_result.setText("????????????????????????");
                            }
                        });
                    } else {
                        tableActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb_querying.setVisibility(View.GONE);
                                btn_identify_result.setVisibility(View.VISIBLE);
                                btn_identify_result.setText("????????????????????????");
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
                            btn_identify_result.setText("????????????????????????");
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
                        if (!TextUtils.isEmpty(StringUtil.getNotNullString(personInfoMap.get(key), ""))) { //???????????????spinner??????editText????????????????????????
                            etText.setText(personInfoMap.get(key));
                        }

                        //}

                        final Button btn = (Button) view.findViewById(R.id.btn_datepicker);
                        if (btn != null) {
                            //?????????????????????
                            btn.setText(personInfoMap.get(key));
                        }

                        //????????????????????????
                        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                        if (spinner != null) {
                            String value = personInfoMap.get(key);
                            for (int j = 0; j < spinner.getAdapter().getCount(); ++j) {
                                String item = (String) spinner.getAdapter().getItem(j);
                                if (item.equals(value) || value.contains(item)) { //???????????????????????????????????????????????????????????????????????????
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
     * ??????????????? AlertDialog
     */
    private void showDialog(final EditText editText) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setTitle("??????");
        builder.setMessage("????????????????????????????????????????");
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
                String devcode = Devcode.devcode;// ?????????????????????
                Intent intent = new Intent(getmContext(), CameraActivity.class);
                intent.putExtra("nMainId", SharedPreferencesHelper.getInt(
                        mContext, "nMainId", 2));
                intent.putExtra("devcode", devcode);
                intent.putExtra("flag", 0);
                intent.putExtra("nCropType", 0);

                //?????????Activity
                ((Activity)mContext).startActivityForResult(intent,520);
                */
            }
        });
        builder.show();
    }

    /**
     * ????????????????????????
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


        /****************  ??????????????????????????????  by gkh *************/
        if (tableState == TableState.EDITING) {
            if (getBasicRenKouInfo() != null) {
                //????????????
                if (getBasicRenKouInfo().getIdenitfyNo() != null) {
                    if (tableItem.getHtml_name().contains("???????????????")) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText(getBasicRenKouInfo().getIdenitfyNo());
                    }

                    if (tableItem.getHtml_name().contains("??????????????????")) {
                        EditText editText = (EditText) view.findViewById(R.id.et_);
                        editText.setText(getBasicRenKouInfo().getIdenitfyNo());
                    }
                }

            }
        }
    }

    /**
     * ????????????????????????
     *
     * @param list
     */
    public void processOrder(List<TableChildItem> list) {
        for (TableChildItem item : list) {
            String code = item.getCode();
            String target = code;
            if (code.length() > 1) {
                target = code.replaceAll("[^(0-9)]", "");//??????????????????
            }
            item.setCode(target);
        }
    }

    /**
     * ???????????????????????????
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
     * H5????????????
     * TODO ?????????????????????????????????
     */
    public void addH5Html(TableItem tableItem) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.table_h5info_item, null);
        final EditText editText = (EditText) view.findViewById(R.id.et_);
        ViewGroup container_webview = (ViewGroup) view.findViewById(R.id.container_webview);
        final FireFightingView fireFightingView = new FireFightingView(mContext, "", container_webview);
        // ????????????
        this.listeners.add(new TableLoadListener() {
            @Override
            public void onFinishedLoad() {
                // TODO ????????????????????????????????????html??????
                // ??????????????????????????????json??????
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
     * ???????????????
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

        //??????????????????
        processOrder(list);
        //?????????
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
                //?????????????????????????????????
                itemSelectHandle(parent, position, editText, list, tableItem);

                //                ifShowWebview(position,tableItem,viewGroup,list); // ??????????????????
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
     * ??????????????????????????????
     *
     * @param tableItem
     * @param viewGroup
     */
    public void ifShowWebview(final TableItem tableItem, final ViewGroup viewGroup) {
        if (tableItem.getHtml_name().equals("????????????")) {  // TODO ????????????????????????????????????????????????html_name?????????
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
                    //??????????????????????????????
                    // ??????????????????parentCode,???????????????????????????
                    List<TableChildItem> b003 = getTableChildItemFormDB("B003");
                    TableChildItem[] b003A = b003.toArray(new TableChildItem[0]);
                    // ???Code??????????????????
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
                    // ????????????
                    // ????????????json??????edittext
                    final EditText dataEditText = (EditText) map.get(getTableItemByField1("yhqk_data").getId()).findViewById(R.id.et_);
                    String itemValue = dataEditText.getText().toString();    // ??????????????????
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
                        String typeCode = "B" + format.format(4 + i);   // TODO ??????parentCode??????????????????(???B003????????????????????????)
                        TableChildItem item = b003.get(i);
                        List<TableChildItem> childItems = tableDataManager.getTableChildItemsByTypeCode(typeCode);
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("item", item);
                        map.put("childItems", childItems);
                        map.put("isSelected", false);   // ??????????????????
                        //                            map.put("selectedIndex", -1);   // ??????????????????
                        map.put("selectedCode", "");    // ????????????Code
                        // ??????????????????
                        if (valueMap.containsKey(item.getCode())) {
                            map.put("isSelected", true);
                        }
                        ArrayList<String> selectedCodes = new ArrayList<String>(); // 2017-09-20 ??????????????????
                        // ????????????
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
                    //??????webview
                    final SaftyHazardView popup = new SaftyHazardView(mContext, tableItem.getHtml_name());
                    popup.setOnJsCallback(new SaftyHazardView.OnJsCallback() {
                        @Override
                        public void onCallback(String dataJson, String show) {
                            popup.dismiss();
                            if (tableState != TableState.READING) {
                                dataEditText.setText(dataJson); // ???webview????????????????????????edittext
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
     * ??????????????????????????????
     *
     * @param parent
     * @param position
     * @param editText
     * @param list
     * @param tableItem
     */
    private void itemSelectHandle(AdapterView<?> parent, int position, EditText editText, List<TableChildItem> list, TableItem tableItem) {
        //??????Spinner??????????????????
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
        if (editText != null) {
            //            editText.setText(adapter.getItem(position));
            if (isEditingFeatureLayer) {
                editText.setText(list.get(position).getName());
            } else {
                editText.setText(list.get(position).getCode());//???????????????code
            }
            //             editText.setText(list.get(position).getCode());//???????????????code
//            editText.setText(list.get(position).getName());  //???????????????name
        }
        //??????????????????
        TableChildItem parentSelectedItem = list.get(position);//????????????????????????
        if (parentSelectedItem != null) {
            String childId = tableItem.getChildren_code();
            String pCode = parentSelectedItem.getCode();//?????????????????????????????????code?????????A009001??????????????????????????????????????????????????????
            List<TableChildItem> childItems = tableDataManager.getTableChildItemsByPCode(pCode);//??????pCode??????????????????????????????
            if (childItems != null && !childItems.isEmpty()) {
                resetSpinnerChildItemAdapter(childId, childItems);//???????????????????????????
            }
        }
    }

    /**
     * ?????? dic_code ???????????????????????????
     *
     * @param dic_code
     * @return
     */
    public TableItem getTableItemByCode(String dic_code) {
        TableItem tableItem = null;
        for (TableItem tmp : tableItems) {
            //??????????????????
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
     * ???????????????????????????URL
     * ??????????????????????????????????????????
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
     * ????????????????????????
     *
     * @param tableItem
     */
    public void getSpinnerItemData(final TableItem tableItem) {
        List<TableChildItem> tableChildItems = getTableChildItemFormDB(tableItem.getDic_code());
        String url = getTableChildUrl(tableItem.getDic_code());
        if (tableChildItems.isEmpty()) {
            ToastUtil.shortToast(mContext, "??????????????????????????????!");
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
     * ?????????????????????????????????
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
                 * ???????????????
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
                // TODO liangsh ?????????????????????????????????????????????????????????????????????????????????????????????bug
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

        /************************************ xcl ?????????????????????????????????????????????********************************************/
        if (tableState == TableState.EDITING) {

            //????????????"??????"?????????????????????
            if (tableItem.getHtml_name().contains("??????")) {
                Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                String value = "??????";
                List<TableChildItem> tableChildItems = getTableChildItemFormDB(tableItem.getDic_code());
                TableDBService tableDBService = new TableDBService(mContext);
                for (int i = 0; i < spinner.getAdapter().getCount(); ++i) {
                    String item = (String) spinner.getAdapter().getItem(i);
                    if (item.equals(value)) {
                        spinner.setSelection(i);
                    }
                }
            }

            //????????????"????????????"???????????????"??????????????????????????????" //todo ??????????????????????????????????????????????????????????????????????????????
            if (tableItem.getHtml_name().contains("????????????") || tableItem.getHtml_name().equals("????????????")
                    || tableItem.getField1().equals("RKLB") || tableItem.getField1().equals("RKLX")) {
                Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                String value = "??????????????????????????????";
                if (shiyouDanweiTableName != null && shiyouDanweiTableName.contains("??????")) {
                    value = "??????";
                } else if (shiyouDanweiTableName != null && shiyouDanweiTableName.contains("??????")) {
                    value = "????????????";
                }

                for (int i = 0; i < spinner.getAdapter().getCount(); ++i) {
                    String item = (String) spinner.getAdapter().getItem(i);
                    if (item.equals(value)) {
                        spinner.setSelection(i);
                    }
                }
            }
            /**
             * ???????????????
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
             * ??????????????????
             */
            if (tableItem.getHtml_name().contains("??????????????????") && basicDongInfo != null && !TextUtils.isEmpty(basicDongInfo.getHouseType())) {
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
             * ??????
             */
            if (tableItem.getHtml_name().contains("??????") && basicRenKouInfo != null && !TextUtils.isEmpty(basicRenKouInfo.getGender())) {
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
                 * ???????????????
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
        /************************************?????????????????????????????????????????????********************************************/
    }

    /**
     * ??????????????????????????????,??????????????????
     *
     * @return
     */
    public List<TableItem> getUploadTableItems() {
        valueMap.clear();
        for (TableItem tableItem : tableItems) {
            if (tableItem.getIf_hidden() != null) {
                //    if (tableItem.getIf_hidden().equals(ControlState.VISIBLE)) {
                View view = map.get(tableItem.getId());
                //??????????????????EditText???????????????????????????
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
                                value = patrolCode; //??????patrolCode?????????????????????????????????
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
                                value = patrolCode; //??????patrolCode?????????????????????????????????
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
     * ???????????????????????????EditText
     *
     * @param type "X"->??????   "Y"->?????? ???????????????????????????
     *             "OBJECT_ID" -> ??????ID   "NAME" ->????????????
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
            //?????????????????????
            if (item.getControl_type().equals(ControlType.PHOTO_IDENTIFY_TEXT_FIELD) && param.containsKey("??????????????????")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("??????????????????"));
                }
            }

            if (item.getField1().equals("name") && param.containsKey("??????")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("??????"));
                }
            }

            if (item.getField1().equals("sex") && param.containsKey("??????")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("??????"));
                }
                View view = map.get(item.getId());
                if (view != null) {
                    //????????????????????????
                    Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                    if (spinner != null) {
                        String value = param.get("??????");
                        for (int j = 0; j < spinner.getAdapter().getCount(); ++j) {
                            String str = (String) spinner.getAdapter().getItem(j);
                            if (str.equals(value) || value.contains(str)) { //???????????????????????????????????????????????????????????????????????????
                                spinner.setSelection(j);
                            }
                        }
                    }
                }
            }
            if (item.getField1().equals("mz") && param.containsKey("??????")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("??????"));
                }
                View view = map.get(item.getId());
                if (view != null) {
                    //????????????????????????
                    Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                    if (spinner != null) {
                        String value = param.get("??????");
                        for (int j = 0; j < spinner.getAdapter().getCount(); ++j) {
                            String str = (String) spinner.getAdapter().getItem(j);
                            if (str.equals(value) || value.contains(str) || str.contains(value)) { //???????????????????????????????????????????????????????????????????????????
                                spinner.setSelection(j);
                            }
                        }
                    }
                }
            }
            /*
            if (item.getField1().equals("nation") && param.containsKey("??????")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("??????"));
                }
            }
            */

            if (item.getField1().equals("bth_date") && param.containsKey("??????")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("??????"));
                    View view = map.get(item.getId());
                    if (view != null) {
                        Button btn = (Button) view.findViewById(R.id.btn_datepicker);
                        if (btn != null) {
                            btn.setText(param.get("??????"));
                        }
                    }
                }
            }

            if (item.getField1().equals("cqr_id") && param.containsKey("??????????????????")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("??????????????????"));
                }
            }

            if (item.getField1().equals("cqr_name") && param.containsKey("??????")) {
                EditText editText = getEditTextViewByField1Type(item.getField1());
                if (editText != null) {
                    editText.setText(param.get("??????"));
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
     * ?????????????????????????????????????????????????????????Activity???????????????
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
                case RESULT_CAPTURE_PHOTO://????????????
                    refreshImageAdapter();
                    break;
                case RESULT_OPEN_PHOTO://??????????????????
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

                        //??????????????????????????????????????????????????????
                        if (mPatrolSelectDevicePresenter != null && mMapPresenter != null && mMapPresenter.getMapView() != null) {
                            mPatrolSelectDevicePresenter.searchNearByDevice(mMapPresenter.getMapView(), mapLatlng.getLongitude(), mapLatlng.getLatitude());
                        }
                    }
                });
            }

            // TODO ???????????????????????????????????????????????????
            // mMapPresenter.startLocate();//xcl 2017-03-27 ????????????????????????????????????
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
                //??????????????????
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
     * ?????????????????????????????????????????????
     * ?????????????????????projectId????????????????????????
     * ???????????? LocalTable ?????????????????????????????????,?????????????????????????????? LocalTable ???????????????ID
     * by gkh
     */
    public void saveEdited() {
        List<TableItem> list = getUploadTableItems();
        //????????????????????????
        //2017.7.31 ???????????????????????????????????????
        //2017.9.16 ???????????????????????????????????????
        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "?????????????????????!");
            return;
        }

        if (list == null) return;
        //   tableDataManager.setEditedTableItemToDB(projectId, list, pList, valueMap);
        tableDataManager.setEditedTableItemToDB(projectId, list, mPhotoListMap, valueMap);

        //    ((Activity) mContext).finish();
        //   ToastUtil.shortToast(mContext, "??????????????????!");
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????
     * ?????????????????????projectId????????????????????????
     * ???????????? LocalTable ?????????????????????????????????,?????????????????????????????? LocalTable ???????????????ID
     * by gkh
     */
    public void saveEdited2(String recordId) {
        List<TableItem> list = getUploadTableItems();
        //????????????????????????
        //2017.7.31 ???????????????????????????????????????
        //2017.9.16 ???????????????????????????????????????
        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "?????????????????????!");
            return;
        }

        if (list == null) return;
        //   tableDataManager.setEditedTableItemToDB(projectId, list, pList, valueMap);
        //????????????address_id,u_id,t_id,d_id
        if ("1".equals(mParentRecordType)) {  //?????????????????????????????????????????????????????????
            valueMap.put("address_id", mParentRecordId);
            TableItem tableItem = getTableItemByField1("address_id");
            if (tableItem != null) {
                tableItem.setValue(mParentRecordId);
            }
        } else if ("2".equals(mParentRecordType)) {  //???????????????????????? ?????????????????????
            valueMap.put("d_id", mParentRecordId);

            TableItem tableItem = getTableItemByField1("d_id");
            if (tableItem != null) {
                tableItem.setValue(mParentRecordId);
            }
        } else if ("3".equals(mParentRecordType)) {  //?????????????????????????????????????????????????????????
            valueMap.put("t_id", mParentRecordId);
            TableItem tableItem = getTableItemByField1("t_id");
            if (tableItem != null) {
                tableItem.setValue(mParentRecordId);
            }

        } else if ("4".equals(mParentRecordType)) {
            valueMap.put("u_id", mParentRecordId);   //????????????????????????????????????????????????ID???
            TableItem tableItem = getTableItemByField1("u_id");
            if (tableItem != null) {
                tableItem.setValue(mParentRecordId);
            }
        }
        tableDataManager.setEditedTableItemToDB2(projectId, recordId, list, mPhotoListMap, valueMap);

        //    ((Activity) mContext).finish();
        //   ToastUtil.shortToast(mContext, "??????????????????!");
    }

    /**
     * ?????????????????????????????????????????????
     * ?????????????????????projectId????????????????????????
     * ???????????? LocalTable ?????????????????????????????????,?????????????????????????????? LocalTable ???????????????ID
     *
     * @param tableKey ??????????????????????????????????????????????????? tableKey ????????? ??????????????????????????????ID
     *                 <p>
     *                 by gkh
     */
    public void saveEdited(String tableKey) {
        List<TableItem> list = getUploadTableItems();
        //????????????????????????
        //2017.9.16 ???????????????????????????????????????
        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "?????????????????????!");
            return;
        }


        if (list == null) return;
        //   tableDataManager.setEditedTableItemToDB(projectId, list, pList, valueMap);
        tableDataManager.setEditedTableItemToDB(tableKey, projectId, list, mPhotoListMap, valueMap);
        // ((Activity) mContext).finish();
        //ToastUtil.shortToast(mContext, "??????????????????!");

        //    ((Activity) mContext).finish();
        //     ToastUtil.shortToast(mContext, "??????????????????!");

    }

    /**
     * ?????????????????????
     *
     * @param tableName ???????????????????????????????????????????????????????????????????????????????????????
     */
    @Deprecated
    public void saveEdited(String tableKey, String tableName) { //xcl ??????????????????
        List<TableItem> list = getUploadTableItems();
        //????????????????????????

        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "?????????????????????!");
            return;
        }

        if (list == null) return;
        //   tableDataManager.setEditedTableItemToDB(projectId, list, pList, valueMap);
        tableDataManager.setEditedTableItemToDB(tableName, tableKey, projectId, list, mPhotoListMap, valueMap);
        ((Activity) mContext).finish();
        ToastUtil.shortToast(mContext, "??????????????????!");
    }

    /**
     * ????????????????????????
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

            //TODO ????????????????????????????????????????????????????????????????????????????????????
            if (TextUtils.isEmpty(m.getValue())) {
                continue;
            }

            // type = getValidateType(m.getKey());
            tableItem = getTableItemByField1(m.getKey());

            if (tableItem == null) {
                continue;
            }

            if (tableItem.getHtml_name() != null
                    && tableItem.getHtml_name().equals("????????????")) {
                if (tableItem.getControl_type().equals(ControlType.SPINNER)) {
                    //????????????????????????value?????????????????????????????????????????????????????????????????????
                    cardTypeName = getSpinnerValueByCode(m.getValue());
                } else {
                    cardTypeName = m.getValue();
                }
            }

            //?????????????????????????????????
            if (tableItem.getControl_type().equals(ControlType.PHOTO_IDENTIFY_TEXT_FIELD)) {
                if (tableItem.getIf_required() != null) {
                    /*if (tableItem.getIf_required().equals(RequireState.REQUIRE)
                            && TextUtils.isEmpty(m.getValue())) {
                        ToastUtil.shortToast(mContext, "????????????????????????");
                        return false;
                    }*/
                }
                if (!TextUtils.isEmpty(cardTypeName)
                        && !TextUtils.isEmpty(m.getValue())
                        && cardTypeName.equals("?????????")
                        && !IdcardValidator.isValidatedAllIdcard(m.getValue())) {
                    ToastUtil.shortToast(mContext, "??????????????????????????????");
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
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "??????" + "?????????????????????!");
                            return false;
                        }
                    } else if (type.equals(ValidateType.INTEGER)) {
                        if (!ValidateUtils.isInteger(m.getValue())) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "??????" + "???????????????!");
                            return false;
                        }
                    } else if (type.equals(ValidateType.STRING)) {
                        if (!ValidateUtils.isString(m.getValue())) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "??????" + "???????????????!");
                            return false;
                        }
                    } else if (type.equals(ValidateType.PHONE)) {  //????????????
                        if (!ValidateUtils.isMobileNO(m.getValue())) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "???????????????" + "???????????????!");
                            return false;
                        }
                    } else if (type.equals(ValidateType.TEL_PHONE)) { //??????????????????
                        if (!ValidateUtils.isTelPhoneNO(m.getValue())) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "???????????????" + "???????????????!");
                            return false;
                        }
                    } else if (type.equals(ValidateType.IDENTIFY)) { //???????????????
                        //???????????????????????????????????????,???????????????????????????????????????????????????
                        TableItem item = getTableItemByField1("zjlx");
                        String zjlx = null;
                        if (item != null && tableItem.getHtml_name().equals("????????????")) {
                            if (valueMap.containsKey("zjlx")) {
                                zjlx = valueMap.get("zjlx");
                            }
                        }
                        if (zjlx != null && !zjlx.equals("?????????")) {
                            continue;
                        }


                        if (!IdcardValidator.isValidatedAllIdcard(m.getValue())) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "???????????????" + "???????????????!");
                            return false;
                        }

                    } else if (type.equals(ValidateType.ALL_NUMBER)) {//????????? ?????????????????????
                        if (!ValidateUtils.isPositiveFloat(m.getValue()) &&
                                !ValidateUtils.isNegativeFloat(m.getValue()) &&
                                !ValidateUtils.isInteger(m.getValue())) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "??????" + "?????????????????????!");
                            return false;
                        }
                    } else if (type.equals(ValidateType.ALL_PHONE)) {//?????????????????????????????????
                        if (!ValidateUtils.isMobileNO(m.getValue()) && !ValidateUtils.isTelPhoneNO(m.getValue())) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "???????????????" + "???????????????!");
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
     * ????????????????????????????????????????????????????????????????????????
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
     * ????????????????????????
     */
    public void uploadEdit() {
        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);

        String url;

        if (TextUtils.isEmpty(tableName) || "DEFAULT".equals(tableName)) { //xcl ??????????????????????????????????????????
            url = serverUrl + "rest/report/saveRpt";
        } else { //xcl ?????????????????????????????????????????????
            url = serverUrl + "rest/report/save";
        }
        //?????????????????????
        url = serverUrl + "rest/report/save";

        //xcl 2017-08-26 ????????????????????????
        //    url = serverUrl + "rest/report/saveMoreTable";


        List<TableItem> list = getUploadTableItems();
        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "?????????????????????!");
            return;
        }

        if (list == null) return;
        if (!checkIfValidate()) {
            return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "??????", "??????????????????");
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
                                    ToastUtil.shortToast(mContext, "????????????!");
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
                                    ToastUtil.shortToast(mContext, "????????????!");
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
                            ToastUtil.shortToast(mContext, "????????????!");
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
                            ToastUtil.shortToast(mContext, "????????????!");
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
             * ??????????????????
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
                        ToastUtil.shortToast(mContext, "????????????!");
                    }

                    //?????????????????????,??????????????????ID
                /*
                if(uploadTableItemResult.getId() != null){
                    ServerRecord taskRecord = new ServerRecord();
                    taskRecord.setId(uploadTableItemResult.getId());
                    taskRecord.setProjectId(projectId);
                    if (!TextUtils.isEmpty(tableName)){
                        taskRecord.setTasktableName(tableName);
                    }
                    //??????????????????????????????????????????????????????????????????
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
     * ????????????????????????????????????????????????
     * <p>
     * ??????????????????
     */
    public void uploadEditMultiWithUserName(final Callback1<Boolean> successCallback) {
        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);

        String url;

        /*
        if (TextUtils.isEmpty(tableName) || "DEFAULT".equals(tableName)) { //xcl ??????????????????????????????????????????
            url = serverUrl + "rest/report/saveRpt";
        } else { //xcl ?????????????????????????????????????????????
            url = serverUrl + "rest/report/save";
        }
        */
        //url = serverUrl + "rest/report/saveMoreTable2"; //xcl 8.31 ??????URL
        url = serverUrl + "am/report/saveMoreTable2"; //xcl 9.9  ??????URL

        List<TableItem> list = getUploadTableItems();

        //2017.9.16 ???????????????????????????
        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "?????????????????????!");
            return;
        }


        if (list == null) return;
        if (!checkIfValidate()) {
            return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "??????", "??????????????????");
        String username = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        if ("1".equals(mParentRecordType)) {  //?????????????????????????????????????????????????????????
            valueMap.put("address_id", mParentRecordId);
        } else if ("2".equals(mParentRecordType)) {  //???????????????????????? ?????????????????????
            valueMap.put("d_id", mParentRecordId);
        } else if ("3".equals(mParentRecordType)) {  //?????????????????????????????????????????????????????????
            valueMap.put("t_id", mParentRecordId);
        } else if ("4".equals(mParentRecordType)) {
            valueMap.put("u_id", mParentRecordId);   //????????????????????????????????????????????????ID???
        }

        if (dataStataAfterUpload != null) {
            valueMap.put("data_stata", dataStataAfterUpload);
        }
        // ???????????? (?????????????????????)
        String uploadTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        valueMap.put("fill_time", uploadTime);
        valueMap.put("fill_date", uploadTime);

        tableDataManager.uploadTableItemsWithUserName(url, username, taskId, recordId, projectId, valueMap, list, mPhotoNameMap, new TableNetCallback() {//8.31 xcl ??????username
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
                    ToastUtil.shortToast(mContext, "????????????!");
                }

                //?????????????????????,??????????????????ID
                if (uploadTableItemResult.getId() != null) {
                    ClientTaskRecord clientTaskRecord = new ClientTaskRecord();
                    clientTaskRecord.setId(uploadTableItemResult.getId());
                    clientTaskRecord.setProjectId(projectId);
                    if (!TextUtils.isEmpty(tableName)) {
                        clientTaskRecord.setProjectName(tableName);
                    }
                    //??????????????????????????????????????????????????????????????????
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
     * ????????????????????????????????????
     * <p>
     * ??????????????????
     */
    public void deleteMultiWithUserName(final Callback1<Boolean> successCallback) {
        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);

        String url;

        /*
        if (TextUtils.isEmpty(tableName) || "DEFAULT".equals(tableName)) { //xcl ??????????????????????????????????????????
            url = serverUrl + "rest/report/saveRpt";
        } else { //xcl ?????????????????????????????????????????????
            url = serverUrl + "rest/report/save";
        }
        */
        //url = serverUrl + "rest/report/saveMoreTable2"; //xcl 8.31 ??????URL
        url = serverUrl + "am/report/saveMoreTable2"; //xcl 9.9  ??????URL

        List<TableItem> list = getUploadTableItems();

        /*
        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "?????????????????????!");
            return;
        }
        */

        if (list == null) return;
        if (!checkIfValidate()) {
            return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "??????", "??????????????????");
        String username = new LoginService(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        if ("1".equals(mParentRecordType)) {  //?????????????????????????????????????????????????????????
            valueMap.put("address_id", mParentRecordId);
        } else if ("2".equals(mParentRecordType)) {  //???????????????????????? ?????????????????????
            valueMap.put("d_id", mParentRecordId);
        } else if ("3".equals(mParentRecordType)) {  //?????????????????????????????????????????????????????????
            valueMap.put("t_id", mParentRecordId);
        } else if ("4".equals(mParentRecordType)) {
            valueMap.put("u_id", mParentRecordId);   //????????????????????????????????????????????????ID???
        }

        if (dataStataAfterUpload != null) {
            valueMap.put("data_stata", dataStataAfterDeleted); //??????????????????
        }

        tableDataManager.uploadTableItemsWithUserName(url, username, taskId, recordId, projectId, valueMap, list, mPhotoNameMap, new TableNetCallback() {//8.31 xcl ??????username
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
                    ToastUtil.shortToast(mContext, "????????????!");
                }

                //?????????????????????,??????????????????ID
                if (uploadTableItemResult.getId() != null) {
                    ClientTaskRecord clientTaskRecord = new ClientTaskRecord();
                    clientTaskRecord.setId(uploadTableItemResult.getId());
                    clientTaskRecord.setProjectId(projectId);
                    if (!TextUtils.isEmpty(tableName)) {
                        clientTaskRecord.setProjectName(tableName);
                    }
                    //??????????????????????????????????????????????????????????????????
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
     * ????????????????????????????????????  ??????????????????
     *
     * @return
     */
    public boolean isPhotoEmpty() {
        boolean isEmpty = true;
        for (Map.Entry<String, List<Photo>> entry : mPhotoListMap.entrySet()) {
            //????????????????????????,?????????
            if (!entry.getValue().isEmpty()) {
                isEmpty = false;
                break;
            }
        }

        return isEmpty;
    }

    /**
     * ????????????????????????
     */
    public void uploadEdit(final Callback1<Boolean> callback1) {
        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);

        String url;

        if (TextUtils.isEmpty(tableName) || "DEFAULT".equals(tableName)) { //xcl ??????????????????????????????????????????
            url = serverUrl + "rest/report/saveRpt";
        } else { //xcl ?????????????????????????????????????????????
            url = serverUrl + "rest/report/save";
        }

        List<TableItem> list = getUploadTableItems();
        if (checkIfRequireEmpty()) {
            ToastUtil.shortToast(mContext, "?????????????????????!");
            return;
        }

        if (list == null) return;
        if (!checkIfValidate()) {
            return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "??????", "??????????????????");
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
                    ToastUtil.shortToast(mContext, "????????????!");
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
     * ??????????????????????????????????????????
     *
     * @return
     */
    public boolean checkIfRequireEmpty() {
        boolean unfinish = false;
        //??????????????????????????????
        for (TableItem tableItem : tableItems) {
            if (tableItem.getIf_required() != null) {
                if (tableItem.getIf_required().equals(RequireState.REQUIRE)) {
                    if (valueMap.containsKey(tableItem.getField1())) {
                        String value = valueMap.get(tableItem.getField1());
                        if (value == null || value.isEmpty()) {
                            ToastUtil.longToast(mContext, tableItem.getHtml_name() + "???????????????");
                            unfinish = true;
                            break;
                        }
                    }
                }
            }
        }
        //??????????????????
        if (!unfinish) {
            unfinish = checkSpecialItemRequireEmpty();
        }
        return unfinish;
    }

    /**
     * ??????????????????????????????????????????
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
        showBottomsheetDialog(key);//xcl 2017-03-29 ????????????BottomsheetDialog
    }

    /**
     * ????????????
     *
     * @param callback ?????????????????????,??????true; ????????????,??????false;
     */
    public void uploadFiles(String patrolCode, String prefix, final Callback1<Boolean> callback) {
        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);
        //xcl 2017-08-14 ?????????
        String url = serverUrl + "rest/upload/add";

        //????????????????????????????????????????????????
//        String url = serverUrl + "rest/enclosureUpload/adds";
        url = url + "/" + patrolCode;
        //        String url = serverUrl +
        //                "am/system/add2";
        //        url = url + "/" + patrolCode; //todo ?????????????????????????????????

        // String url = serverUrl + "rest/enclosureUpload/uploadFile";

        //   http://210.21.98.71:8088/agweb14/rest/upload/add/{patrolCode}
        if (mPhotoListMap == null || mPhotoListMap.size() <= 0) {
            ((Activity) mContext).finish();
            return;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "??????", "??????????????????");
        tableDataManager.uploadPhotos(url, mPhotoListMap, prefix, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                ToastUtil.shortToast(mContext, "??????????????????!");
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
                ToastUtil.shortToast(mContext, "?????????????????????");
                if (callback != null) {
                    callback.onCallback(false);
                }
            }
        });
    }

    /**
     * ???????????????,?????????????????????????????????
     */
    public void afterUpload() {
        //????????????????????????
        EventBus.getDefault().post(new RefreshHistoryEvent());

        if (tableKey == null) return;
        if (tableState == TableState.REEDITNG) {
            tableDataManager.deleteEditedTableItemsFromBD(tableKey, new TableDBCallback() {
                @Override
                public void onSuccess(Object data) {
                    clearPhoto();

                    //??????????????????
                    EventBus.getDefault().post(new RefreshLocalEvent());


                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }

    /**
     * ??????????????????
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
                        "????????????????????????????????????????????????????????????", 101,
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


          /*      *//*****************?????????????????????????????????***********************//*
                if (key.equals("photo")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).setMessage("???????????????????????????")
                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPhotoExtra = PhotoButtonUtil.registTakePhotoButton((Activity) mContext, HSPVFileUtil.getPathPhoto());
                                    mPhotoExtra.setKey(key);
                                    bottomSheetDialog.dismiss();
                                }
                            }).show();
                    *//*****************?????????????????????????????????***********************//*

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
     * ???????????????????????????
     *
     * @param btn
     */
   /* private void showTimePicker(final Button btn, final ViewGroup view) {
        final StringBuffer time = new StringBuffer();
        //??????Calendar?????????????????????????????????
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //?????????TimePickerDialog??????
        final TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            //??????????????????????????????????????????
            @Override
            public void onTimeSet(TimePicker datePicker, int hourOfDay, int minute) {
                time.append(" " + hourOfDay + ":" + minute + ":00");
                //?????????????????????????????????
                btn.setText(time);
                ((EditText) view.findViewById(R.id.et_)).setText(time);
            }
        }, hour, minute, true);
        //?????????DatePickerDialog??????
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            //??????????????????????????????????????????
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                //??????monthOfYear?????????????????????????????????????????????1
                time.append(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                //?????????????????????????????????????????????
               / timePickerDialog.show();
            }
        }, year, month, day);
        //???????????????????????????
        datePickerDialog.show();
    }*/
    private void showTimePicker(final Button btn, final ViewGroup view, TableItem tableItem) {
        final StringBuffer time = new StringBuffer();
        //??????Calendar?????????????????????????????????
        final Calendar calendar = Calendar.getInstance();
        String text = btn.getText().toString();
        if (!TextUtils.isEmpty(text)) { // ??????????????????
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

        //        if (tableItem.getHtml_name().contains("????????????") || tableItem.getField1().toLowerCase().equals("time")) {
        //            btn.setText(year + "-" + 9 + "-" + 1); //????????????????????????????????????????????????9???1???
        //
        //            ((EditText) view.findViewById(R.id.et_)).setText(year + "-"+"9" +"-" +"1");//?????????9???1???
        //            month = 9;
        //            day = 1;
        //        }         // 2017-09-19 ????????????????????????????????????????????????????????????????????????????????????

        //?????????TimePickerDialog??????
        final TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            //??????????????????????????????????????????
            @Override
            public void onTimeSet(TimePicker datePicker, int hourOfDay, int minute) {
                time.append(" " + hourOfDay + ":" + minute + ":00");
            }
        }, hour, minute, true);
        //?????????DatePickerDialog??????
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            //??????????????????????????????????????????
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                //??????monthOfYear?????????????????????????????????????????????1
                time.append(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                ((EditText) view.findViewById(R.id.et_)).setText(time);
                btn.setText(time);//?????????????????????????????????
                //?????????????????????????????????????????????
                // timePickerDialog.show();
            }
        }, year, month, day);
        //???????????????????????????
        datePickerDialog.show();
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public TableState getTableState() {
        return tableState;
    }

    /**
     * ???????????????ID???View????????????Map
     *
     * @return
     */
    public Map<String, View> getMap() {
        return map;
    }

    /**
     * ????????????????????????TableItem
     *
     * @return
     */
    public List<TableItem> getTableItems() {
        return tableItems;
    }

    /**
     * ????????????????????????????????????View??????
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
     * ?????????????????????
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
     * ???????????????????????????ID
     * ??????????????????????????????
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
     * ???????????????????????????????????????
     *
     * @param mTableValueListener
     */
    public void setTableValueListener(TableValueListener mTableValueListener) {
        this.mTableValueListener = mTableValueListener;
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    public String getLink() {
        return link;
    }

    /**
     * ??????????????????????????????
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
     * ?????????????????????????????????????????????????????????????????????????????????"????????????"?????????????????????
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

    /********************** xcl ????????????????????????*************************/

}
