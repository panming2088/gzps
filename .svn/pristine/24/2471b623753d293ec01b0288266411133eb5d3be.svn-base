package com.augurit.agmobile.gzps.uploadevent.view.eventedit;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.constant.GzpsConstant;
import com.augurit.agmobile.gzps.common.selectcomponent.SelectComponentFinishEvent2;
import com.augurit.agmobile.gzps.common.selectcomponent.SelectComponentOrAddressActivity;
import com.augurit.agmobile.gzps.common.widget.AutoBreakViewGroup;
import com.augurit.agmobile.gzps.common.widget.MyGridView;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadevent.adapter.EventTypeAdapter;
import com.augurit.agmobile.gzps.uploadevent.adapter.FacilityTypeAdapter;
import com.augurit.agmobile.gzps.uploadevent.adapter.NextLinkAssigneersAdapter;
import com.augurit.agmobile.gzps.uploadevent.dao.GetPersonByOrgApiData;
import com.augurit.agmobile.gzps.uploadevent.event.UpdateAfterEditEvent;
import com.augurit.agmobile.gzps.uploadevent.model.Assigneers;
import com.augurit.agmobile.gzps.uploadevent.model.OrgItem;
import com.augurit.agmobile.gzps.uploadevent.model.Person;
import com.augurit.agmobile.gzps.uploadevent.model.ProblemUploadBean;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.gzps.uploadevent.util.PhotoUploadType;
import com.augurit.agmobile.gzps.uploadevent.view.eventlist.EventDetailMapActivity;
import com.augurit.agmobile.gzps.uploadfacility.view.feedback.FeedbackEventMapActivity;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.baiduapi.BaiduApiService;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.selectlocation.model.BaiduGeocodeResult;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.loc.util.LocationUtil;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.popupview.util.DensityUtils;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.RadioGroupUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.core.geometry.Point;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxbinding.view.RxView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 事件撤回再编辑界面
 * <p>
 * 在撤回和回退后,假如事件流程还处在问题上报流程节点(即除了上报人外,其他人还没真正参与流程处理)时,
 * 此时上报人自己可以对上报问题进行再次编辑!!!!  by gkh
 */
public class EventEditActivity extends BaseActivity {
    private MultiTakePhotoTableItem photo_item;
    private AutoBreakViewGroup disease_type_rg, urgency_state_rg, facility_type_rg;
    private MyGridView gv_facilitytype;
    private FacilityTypeAdapter mFacilityTypeAdapter;
    private View ll_eventtype;
    private MyGridView gv_eventtype;
    private EventTypeAdapter mEventTypeAdapter;
    private RadioGroup.LayoutParams params;
    private Button upload_btn;
    private View btn_save_draft;
    private CheckBox cb_isbyself;
    private View ll_nextlink_assigneers;
    private MyGridView gv_assignee;
    private TextView tv_select_or_check_location;
    private View ll_nextlilnk_org_rg_rm;     //Rg和Rm选择下一环节处理人UI
    private AutoBreakViewGroup radio_nextlink_org_rg_rm;   //Rg和Rm的机构列表UI
    private ProgressDialog progressDialog;

    private TableDBService dbService;

    private String componentId;
    private String layerId;
    private String layerName;
    private String[] urgency_type_array = {"一般", "较紧急", "紧急"};
    private double reportX;       //上报者当前定位X坐标
    private double reportY;   //上报者当前定位Y坐标
    private String reportAddr;      //上报者当前定位地址
    private String isbyself = "false"; //是否自行处理
    private String facilityCode, diseaseCode, urgencyCode, facilityName, diseaseName, urgencyName;

    private Component mSelComponent;
    private DetailAddress mSelDetailAddress;
    private ProblemUploadBean problemUploadBean = null;
    private List<OrgItem> orgItems;
    private List<DictionaryItem> facility_type_list;
    private List<DictionaryItem> problemList;
    private TextItemTableItem roadItem, addrItem;
    private TextFieldTableItem problemitem;
    private TextFieldTableItem remarkItem;
    private NextLinkAssigneersAdapter assigneersAdapter;
    private Assigneers.Assigneer selAssignee;

    private String selAssignOrg;

    private ProblemUploadBean preProblemUploadBean;//事件详情传过来的问题实体类

    private boolean isReSelect_component = false;


    private String activityName;
    private String sjid;
    private String isSendMessage = "0";//默认不发送,1为发送

    private CheckBox cb_is_send_message;//是否发送短信通知对方

    private View ll_self_process;//自行处理过程UI

    private long reportId;

    private List<Photo> prePhotoList = new ArrayList<>();//记录之前已经上传过的图片

    private MultiTakePhotoTableItem photo_item_self;
    private TextFieldTableItem problem_tab_item_self;

    /**
     * 预计处理完成时间
     */
    private Button btn_start_date;
    private Calendar cal;
    private Long startDate = null;
    private Long todayDate = null;
    private static final int START_DATE = 1;
    private Long checkdate = null;
    private EditText et_other;
    private boolean isObjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_edit);
        preProblemUploadBean = (ProblemUploadBean) getIntent().getSerializableExtra("preProblemUploadBean");
        reportId = getIntent().getLongExtra("reportId", -1);
        initView();
        initListener();
        initData();

        // 定位当前位置，reportx、reporty、reportaddr这三个字段要传给接口
        startLocate();
        setPreProblemUploadBean();
    }

    /**
     * 初始化时间
     */
    private void initStartDate() {
        cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        int month = cal.get(Calendar.MONTH) + 1;   //获取到的月份是从0开始计数
        int day = cal.get(Calendar.DAY_OF_MONTH);
        startDate = new Date(year - 1900, month - 1, day).getTime();
        todayDate = new Date(year - 1900, month - 1, day).getTime();
    }

    private void initData() {
        problemUploadBean = new ProblemUploadBean();
        dbService = new TableDBService(EventEditActivity.this);
        facility_type_list = dbService.getDictionaryByTypecodeInDB("A174");

        /*
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        */

        ((TextView) findViewById(R.id.tv_title)).setText("问题编辑修改");
        //初始化设施类型列表
//        initFacilityView(facility_type_list);
        initFacilityType(facility_type_list, null);
        initUrgencyView(null);

        User user = new LoginService(getApplicationContext(), AMDatabase.getInstance()).getUser();
        //如果是Rg和Rm 则获取机构列表
        if (user.getRoleCode().contains(GzpsConstant.roleCodes[3])
                || user.getRoleCode().contains(GzpsConstant.roleCodes[4])) {
            ll_nextlilnk_org_rg_rm.setVisibility(View.VISIBLE);
            getOrgList();

        } else {
            ll_nextlilnk_org_rg_rm.setVisibility(View.GONE);
            if(preProblemUploadBean != null && !StringUtil.isEmpty(preProblemUploadBean.getObject_id())) {
                getNextLinkAssigneers(preProblemUploadBean.getObject_id());
            }else{
                getNextLinkAssigneers("");
            }

        }
    }

    //传进之前上传的问题内容
    private void setPreProblemUploadBean() {
        if (preProblemUploadBean != null) {
            if (preProblemUploadBean.getPhotos() != null) {
                photo_item.setSelectedPhotos(preProblemUploadBean.getPhotos());
                prePhotoList.addAll(preProblemUploadBean.getPhotos());
            }

            if (preProblemUploadBean.getReportaddr() != null) {
                addrItem.setText(preProblemUploadBean.getReportaddr());
                tv_select_or_check_location.setText(preProblemUploadBean.getReportaddr());
            }

            if (preProblemUploadBean.getSSLX() != null) {
                initFacilityType(facility_type_list, preProblemUploadBean.getSSLX());
//                facilityName = preProblemUploadBean.getSSLX();
                //再次编辑有了设备类型
                mFacilityTypeAdapter.setEnable(false);

            }

            if (preProblemUploadBean.getBHLX() != null) {
                initEventTypeView(problemList, preProblemUploadBean.getBHLX());
            }

            if ("false".equals(preProblemUploadBean.getIsbyself()) && "1".equals(preProblemUploadBean.getSfjb())) {
                findViewById(R.id.problem_type).setVisibility(View.VISIBLE);
                btn_start_date.setText(TimeUtil.getStringTimeYMD(new Date(preProblemUploadBean.getYjwcsj())));
                checkdate = preProblemUploadBean.getYjwcsj();
            } else {
                findViewById(R.id.problem_type).setVisibility(View.GONE);
            }

            if (preProblemUploadBean.getJJCD() != null) {
                initUrgencyView(preProblemUploadBean.getJJCD());
            }

            if (preProblemUploadBean.getWTMS() != null) {
                problemitem.setText(preProblemUploadBean.getWTMS());
            }
            if( !StringUtil.isEmpty(preProblemUploadBean.getLayer_name())){
                layerName = preProblemUploadBean.getLayer_name();
            }
            if ("1".equals(preProblemUploadBean.getSfjb()) && !StringUtil.isEmpty(preProblemUploadBean.getLayer_name())) {
                tv_select_or_check_location.setText(preProblemUploadBean.getLayer_name());
            }

            if (preProblemUploadBean.getSZWZ() != null) {
                addrItem.setText(preProblemUploadBean.getSZWZ());
                if (!"1".equals(preProblemUploadBean.getSfjb())) {
                    tv_select_or_check_location.setText(preProblemUploadBean.getSZWZ());
                }
            }

            if (preProblemUploadBean.getJDMC() != null) {
                roadItem.setText(preProblemUploadBean.getJDMC());
            }


        }
    }

    private void initView() {

        ll_nextlilnk_org_rg_rm = (View) findViewById(R.id.ll_nextlilnk_org);
        radio_nextlink_org_rg_rm = (AutoBreakViewGroup) findViewById(R.id.radio_nextlink_org);


        gv_assignee = (MyGridView) findViewById(R.id.gv_assignee);

        roadItem = (TextItemTableItem) findViewById(R.id.road_tab_item);
        addrItem = (TextItemTableItem) findViewById(R.id.addr_tab_item);
        problemitem = (TextFieldTableItem) findViewById(R.id.problem_tab_item);
        tv_select_or_check_location = (TextView) findViewById(R.id.tv_select_or_check_location);
        //personItem = (TextItemTableItem) findViewById(R.id.patrol_person);
        //remarkItem = (TextFieldTableItem) findViewById(R.id.remark);
        photo_item = (MultiTakePhotoTableItem) findViewById(R.id.photo_item);
        photo_item.setPhotoExampleEnable(false);

        photo_item_self = (MultiTakePhotoTableItem) findViewById(R.id.photo_item_self);
        photo_item.setPhotoExampleEnable(false);

        facility_type_rg = (AutoBreakViewGroup) findViewById(R.id.facility_type_rg);
        disease_type_rg = (AutoBreakViewGroup) findViewById(R.id.disease_type_rg);
        urgency_state_rg = (AutoBreakViewGroup) findViewById(R.id.urgency_state_rg);
        gv_facilitytype = (MyGridView) findViewById(R.id.gv_facilitytype);
        ll_eventtype = findViewById(R.id.ll_eventtype);
        gv_eventtype = (MyGridView) findViewById(R.id.gv_eventtype);
        cb_isbyself = (CheckBox) findViewById(R.id.cb_isbyself);
        ll_nextlink_assigneers = findViewById(R.id.ll_nextlink_assigneers);
        et_other = (EditText) findViewById(R.id.et_other);


        upload_btn = (Button) findViewById(R.id.problem_commint);
        btn_save_draft = findViewById(R.id.btn_save_draft);
        int screenWidths = getScreenWidths();
        params = new AutoBreakViewGroup.LayoutParams(screenWidths / 3, AutoBreakViewGroup
                .LayoutParams
                .WRAP_CONTENT);

        mFacilityTypeAdapter = new FacilityTypeAdapter(this);
        mFacilityTypeAdapter.setOnItemClickListener(new OnRecyclerItemClickListener<DictionaryItem>() {
            @Override
            public void onItemClick(View view, int position, DictionaryItem selectedData) {
                facilityCode = selectedData.getCode();
                facilityName = selectedData.getName();
                if ("其他".equals(facilityName)) {
                    ll_eventtype.setVisibility(View.GONE);
                } else {
                    ll_eventtype.setVisibility(View.VISIBLE);
                    problemList = dbService.getChildInVisableDictionaryByPCodeInDB(facilityCode);
                    initEventTypeView(problemList, null);
                }
            }

            @Override
            public void onItemLongClick(View view, int position, DictionaryItem selectedData) {

            }
        });
        gv_facilitytype.setAdapter(mFacilityTypeAdapter);
        mEventTypeAdapter = new EventTypeAdapter(this);
        gv_eventtype.setAdapter(mEventTypeAdapter);

        mEventTypeAdapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object selectedData) {
                if (selectedData != null) {

                    if (((DictionaryItem) selectedData).getName().contains("其他")) {
                        if (!mEventTypeAdapter.getSelectedEventTypeList().contains(selectedData)) {
                            et_other.setVisibility(View.GONE);
                            et_other.setText("");
                        } else {
                            et_other.setVisibility(View.VISIBLE);

                        }
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position, Object selectedData) {

            }
        });


        //  initUrgencyView();
        assigneersAdapter = new NextLinkAssigneersAdapter(this);
        gv_assignee.setAdapter(assigneersAdapter);
        assigneersAdapter.setOnItemClickListener(new OnRecyclerItemClickListener<Assigneers.Assigneer>() {
            @Override
            public void onItemClick(View view, int position, Assigneers.Assigneer selectedData) {
                selAssignee = selectedData;
            }

            @Override
            public void onItemLongClick(View view, int position, Assigneers.Assigneer selectedData) {

            }
        });
        progressDialog = new ProgressDialog(EventEditActivity.this);
        progressDialog.setMessage("正在提交...");
        EventBus.getDefault().register(this);


        cb_is_send_message = (CheckBox) findViewById(R.id.cb_is_send_message);
        ll_self_process = findViewById(R.id.ll_self_process);

        problem_tab_item_self = (TextFieldTableItem) findViewById(R.id.problem_tab_item_self);

        btn_start_date = (Button) findViewById(R.id.btn_start_date);
        initStartDate();
        RxView.clicks(btn_start_date)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        cal.setTimeInMillis(startDate);
                        showDatePickerDialog(btn_start_date, cal, START_DATE);
                    }
                });
    }

    public void showDatePickerDialog(final Button btn, Calendar calendar, final int type) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(this,
                // 绑定监听器(How the parent is notified that the date is set.)
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        display(btn, year, monthOfYear, dayOfMonth);
                        if (type == START_DATE) {
                            startDate = new Date(year - 1900, monthOfYear, dayOfMonth).getTime();
                        }
                    }
                }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void initListener() {
        facility_type_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int checkedRadioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);

                int index = group.indexOfChild(radioButton);
                String code = facility_type_list.get(index).getCode();
                facilityName = radioButton.getText().toString();
                facilityCode = code;
                if ("其他".equals(facilityName)) {
                    ll_eventtype.setVisibility(View.GONE);
                } else {
                    ll_eventtype.setVisibility(View.VISIBLE);
                    problemList = dbService.getChildInVisableDictionaryByPCodeInDB(code);
                    initEventTypeView(problemList, null);
                }

            }
        });
        disease_type_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                int index = group.indexOfChild(radioButton);
                String code = problemList.get(index).getCode();
                diseaseName = radioButton.getText().toString();
                diseaseCode = code;
            }
        });
        urgency_state_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                int index = group.indexOfChild(radioButton);
                urgencyName = radioButton.getText().toString();
                urgencyCode = (index + 1) + "";
            }
        });

        //提交问题到服务器
        RxView.clicks(upload_btn)
                .throttleFirst(2, TimeUnit.SECONDS)   //2秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        User user = new LoginService(getApplicationContext(), AMDatabase.getInstance()).getUser();
                        if ((user.getRoleCode().contains(GzpsConstant.roleCodes[2])
                       /* ||user.getRoleCode().contains(GzpsConstant.roleCodes[3])
                        || user.getRoleCode().contains(GzpsConstant.roleCodes[4]) */)) {
                            //Rg和Rm暂时不能上报
                            ToastUtil.longToast(EventEditActivity.this,
                                    "您是管理层角色，APP暂不支持您的问题上报流转，开放权限的程序正在开发调试中……敬请期待！");
                            return;
                        }

                        progressDialog.show();

                        ProblemUploadBean paramsEntity = getParamsEntity();

                        if (paramsEntity == null) {
                            progressDialog.dismiss();
                            return;
                        }
                        sendToServer();
                    }
                });

        //保存问题到本地草稿
//        RxView.clicks(btn_save_draft)
//                .throttleFirst(2, TimeUnit.SECONDS)   //2秒钟之内只取一个点击事件，防抖操作
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        User user = new LoginService(getApplicationContext(), AMDatabase.getInstance()).getUser();
//                        if((user.getRoleCode().contains(GzpsConstant.roleCodes[2])
//                       /* ||user.getRoleCode().contains(GzpsConstant.roleCodes[3])
//                        || user.getRoleCode().contains(GzpsConstant.roleCodes[4]) */) ){
//                            //Rg和Rm暂时不能上报
//                            ToastUtil.longToast(EventEditActivity.this,
//                                    "您是管理层角色，APP暂不支持您的问题上报流转，开放权限的程序正在开发调试中……敬请期待！");
//                            return;
//                        }
//
//                        ProblemUploadBean paramsEntity = getDraftParamsEntity();
//                        if (paramsEntity == null) {
//                            progressDialog.dismiss();
//                            return;
//                        }
//                        saveDraft();
//                    }
//                });

        /**
         * 选择设施或问题地点
         */
        findViewById(R.id.ll_select_component).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"1".equals(preProblemUploadBean.getSfjb()) && !"2".equals(preProblemUploadBean.getSfjb())) {
                    Intent intent = new Intent(EventEditActivity.this,
                            SelectComponentOrAddressActivity.class);
                    if (isReSelect_component) {//已经再次编辑选择过了
                        if (mSelComponent != null) {
                            intent.putExtra("geometry", mSelComponent.getGraphic().getGeometry());
                        } else if (mSelDetailAddress != null) {
                            Point point = new Point();
                            point.setX(mSelDetailAddress.getX());
                            point.setY(mSelDetailAddress.getY());
                            intent.putExtra("geometry", point);
                        }
                    } else {
                        //否则用之前传递过来的数据
                        if (preProblemUploadBean.getX() != null && preProblemUploadBean != null) {
                            Point point = new Point();
                            point.setX(Double.valueOf(preProblemUploadBean.getX()));
                            point.setY(Double.valueOf(preProblemUploadBean.getY()));
                            intent.putExtra("geometry", point);
                        }
                    }

                    startActivity(intent);
                } else {
                    String layerId = preProblemUploadBean.getLayer_id();
                    String layerUrl = preProblemUploadBean.getLayerurl();
                    String layerName = preProblemUploadBean.getLayer_name();
                    String objectId = preProblemUploadBean.getObject_id();
                    String usid = preProblemUploadBean.getUsid();
                    String x = preProblemUploadBean.getX();
                    String y = preProblemUploadBean.getY();
                    if (StringUtil.isEmpty(x)
                            || StringUtil.isEmpty(y)
                            || (StringUtil.isEmpty(usid) && StringUtil.isEmpty(objectId))) {
                        ToastUtil.shortToast(EventEditActivity.this, "设施位置信息缺失");
                        return;
                    }

                    if ("1".equals(preProblemUploadBean.getSfjb())) {
                        if (!StringUtil.isEmpty(usid)) {
                            Intent intent = new Intent(EventEditActivity.this, EventDetailMapActivity.class);
                            intent.putExtra("layerUrl", layerUrl);
                            intent.putExtra("layerName", layerName);
                            intent.putExtra("x", Double.valueOf(x));
                            intent.putExtra("y", Double.valueOf(y));
                            intent.putExtra("usid", usid);
                            intent.putExtra("objectId", objectId);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(EventEditActivity.this, FeedbackEventMapActivity.class);
                            intent.putExtra("layerUrl", layerUrl);
                            intent.putExtra("layerName", layerName);
                            intent.putExtra("x", Double.valueOf(x));
                            intent.putExtra("y", Double.valueOf(y));
                            intent.putExtra("object_id", objectId);
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(EventEditActivity.this, EventDetailMapActivity.class);
                        intent.putExtra("layerUrl", layerUrl);
                        intent.putExtra("layerName", layerName);
                        intent.putExtra("x", Double.valueOf(x));
                        intent.putExtra("y", Double.valueOf(y));
                        intent.putExtra("usid", usid);
                        intent.putExtra("objectId", objectId);
                        startActivity(intent);
                    }
                }
            }
        });

        //是否自行处理
        cb_isbyself.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isbyself = "true";
                    findViewById(R.id.problem_type).setVisibility(View.GONE);
                    ll_nextlink_assigneers.setVisibility(View.GONE);
                    ll_self_process.setVisibility(View.VISIBLE);
                } else {
                    isbyself = "false";
                    findViewById(R.id.problem_type).setVisibility(View.VISIBLE);
                    ll_nextlink_assigneers.setVisibility(View.VISIBLE);
                    ll_self_process.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.MessageBox(EventEditActivity.this, "提示", "是否确定放弃本次编辑？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        });


        radio_nextlink_org_rg_rm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(checkedRadioButtonId);
                int index = ((RadioGroup) radioButton.getParent()).indexOfChild(radioButton);
                OrgItem orgItem = orgItems.get(index);
                selAssignOrg = orgItem.getName();
                getPersonByOrgCodeAndName(orgItem.getName(), orgItem.getCode());

                //   assigneersAdapter.notifyDatasetChanged(orgItems);
            }
        });
    }


    private void getPersonByOrgCodeAndName(String name, String code) {
        new UploadEventService(getApplicationContext()).getPersonByOrgApiDataObservable(code, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetPersonByOrgApiData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GetPersonByOrgApiData getPersonByOrgApiData) {
                        List<Person> personList = getPersonByOrgApiData.getUserFormList();
                        if (personList != null && !personList.isEmpty()) {
                            List<Assigneers.Assigneer> assigneeList = new ArrayList<>();
                            for (Person person : personList) {
                                assigneeList.add(new Assigneers.Assigneer(person.getCode(), person.getName()));
                            }
                            assigneersAdapter.notifyDatasetChanged(assigneeList);
                        }
                    }
                });
    }

    private void getOrgList() {
        new UploadEventService(getApplicationContext()).getOrgItemList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<OrgItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<OrgItem> list) {
                        if (list != null &&
                                !list.isEmpty()) {

                            orgItems = list;
                            int size = orgItems.size();
                            for (int i = 0; i < size; i++) {
                                RadioButton radioButton = new RadioButton(EventEditActivity.this);
                                radioButton.setText(orgItems.get(i).getName());
                                //    radioButton.setTag(nextLinkOrgs);
                                radioButton.setLayoutParams(params);
                                radio_nextlink_org_rg_rm.addView(radioButton);
                                if(preProblemUploadBean != null && "排水口".equals(preProblemUploadBean.getLayer_name())){
                                    if("河涌中心".equals(orgItems.get(i).getName())){
                                        radioButton.setChecked(true);
                                    }
                                }else {
                                    if (i == 0) {
                                        radioButton.setChecked(true);
                                    }
                                }
                            }


                        }

                    }
                });
    }

    /**
     * 获取下一环节处理人（R1巡查员上报时用）
     */
    private void getNextLinkAssigneers(String objectId) {
        new UploadEventService(getApplicationContext()).getTaskUserByloginName(objectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Assigneers>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(List<Assigneers> assigneersList) {
                        if (assigneersList != null
                                && !ListUtil.isEmpty(assigneersList)) {
                            List<Assigneers.Assigneer> assigneeList = new ArrayList<>();
                            for (Assigneers assigneers : assigneersList) {
                                assigneeList.addAll(assigneers.getAssigneers());
                            }
                            assigneersAdapter.notifyDatasetChanged(assigneeList);

                        }
                    }
                });
    }

    /**
     * 选择位置或设施后的事件回调
     *
     * @param selectComponentFinishEvent
     */
    @Subscribe
    public void onReceivedFinishedSelectEvent2(SelectComponentFinishEvent2 selectComponentFinishEvent) {
        Component component = selectComponentFinishEvent.getFindResult();
        mSelComponent = component;
        mSelDetailAddress = selectComponentFinishEvent.getDetailAddress();

        if (mSelComponent != null) {
            isObjectId = true;
            isReSelect_component = true;//标识已经重新选过设施或者地址了

            //选择的是设施
            layerName = component.getLayerName();
            tv_select_or_check_location.setText(layerName);
//            selFacilityType(layerName);
            mFacilityTypeAdapter.selectItemByName(layerName);
            mFacilityTypeAdapter.setEnable(false);
            RadioGroupUtil.disableRadioGroup(facility_type_rg);  //选择的是设施，则不能再改变设施类型
            String layerId = getLayerId(selectComponentFinishEvent);
            this.layerId = layerId;
            setObjectId(component);
            if (ll_nextlilnk_org_rg_rm.getVisibility() == View.GONE) {
                getNextLinkAssigneers(componentId);
            }
            setXY(selectComponentFinishEvent);
            String subType = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE));
            String type = layerName;
            if(StringUtil.isEmpty(layerName) || "null".equals(layerName) ){
                type = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.LAYER_NAME),"");
            }
            tv_select_or_check_location.setText(type);
//            addrItem.setText(StringUtil.getNotNullString(mSelComponent.getGraphic().getAttributeValue("ADDR"), ""));
//            roadItem.setText("");
            if (selectComponentFinishEvent.getFindResult().getGraphic().getGeometry() instanceof Point) {
                Point point = (Point) selectComponentFinishEvent.getFindResult().getGraphic().getGeometry();
                requestAddress(point.getX(), point.getY(), new Callback1<BaiduGeocodeResult>() {
                    @Override
                    public void onCallback(BaiduGeocodeResult baiduGeocodeResult) {
                        if (baiduGeocodeResult == null) {
                            return;
                        }
                        addrItem.setText(StringUtil.getNotNullString(baiduGeocodeResult.getDetailAddress(), ""));
                        roadItem.setText(baiduGeocodeResult.getResult().getAddressComponent().getStreet());
                    }
                });
            }

        } else if (mSelDetailAddress != null) {
            isReSelect_component = true;
            if("排水口".equals(layerName)){
                layerName = "窨井";
                mFacilityTypeAdapter.selectItemByName(layerName);
            }
            if (isObjectId && ll_nextlilnk_org_rg_rm.getVisibility() == View.GONE) {
                getNextLinkAssigneers("");
                isObjectId = false;
            }
            //选择的是位置
            mFacilityTypeAdapter.setEnable(true);
            RadioGroupUtil.enableRadioGroup(facility_type_rg);   //选择的是位置，可以改变设施类型
            tv_select_or_check_location.setText(mSelDetailAddress.getDetailAddress());
            addrItem.setText(mSelDetailAddress.getDetailAddress());
            roadItem.setText(mSelDetailAddress.getStreet());
        }

    }

    private void setObjectId(Component component) {
        Integer objectid = component.getObjectId();
        componentId = objectid + "";
    }

    private void setXY(SelectComponentFinishEvent2 selectComponentFinishEvent) {
        if (selectComponentFinishEvent.getFindResult().getGraphic().getGeometry() instanceof Point) {
            Point point = (Point) selectComponentFinishEvent.getFindResult().getGraphic().getGeometry();
            //            modifiedIdentification.setX(point.getX());
            //            modifiedIdentification.setY(point.getY());
        }
    }

    /**
     * 从url中截取出图层的id
     *
     * @param selectComponentFinishEvent
     * @return
     */
    @NonNull
    private String getLayerId(SelectComponentFinishEvent2 selectComponentFinishEvent) {
        int i = selectComponentFinishEvent.getFindResult().getLayerUrl().lastIndexOf("/");
        return selectComponentFinishEvent.getFindResult().getLayerUrl().substring(i + 1);
    }


    /**
     * 提交到服务器
     */
    private void sendToServer() {
        UploadEventService eventService = new UploadEventService(EventEditActivity.this);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        String json = gson.toJson(problemUploadBean);
        HashMap<String, RequestBody> photoBody = getPhotoBody(photo_item.getSelectedPhotos());

        //加进自行处理的图片
        if ("true".equals(isbyself)) {
            if (getPhotoBodyForSelfProcess(photo_item_self.getSelectedPhotos()) != null
                    && !getPhotoBodyForSelfProcess(photo_item_self.getSelectedPhotos()).isEmpty()) {
                photoBody.putAll(getPhotoBodyForSelfProcess(photo_item_self.getSelectedPhotos()));
            }
        }


        String assigneeCode = null;
        String assigneeName = null;
        if ("false".equals(isbyself) && selAssignee != null) {
            //如果不是自行处理，则有下一环节处理人
            assigneeCode = selAssignee.getUserCode();
            assigneeName = selAssignee.getUserName();
        }

        if (assigneeCode == null) {
            assigneeCode = "";
        }

        if (assigneeName == null) {
            assigneeName = "";
        }


        if (cb_is_send_message.isChecked()) {
            isSendMessage = "1";
        } else {
            isSendMessage = "0";
        }

        final boolean isUpdateWithAssignee;//是否有重新指派了下一环节处理人
        if ("true".equals(isbyself) || !TextUtils.isEmpty(assigneeName)) {
            isUpdateWithAssignee = true;
        } else {
            isUpdateWithAssignee = false;
        }

        String delImgs = getDeleteImgs(photo_item.getSelectedPhotos());//要删除之前已经上传过的图片
        if (photoBody.isEmpty()) {
            eventService.editEvent(reportId, delImgs, json, assigneeCode, assigneeName, isSendMessage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onNext(Boolean s) {
                            if (!s) {
                                progressDialog.dismiss();
                                ToastUtil.shortToast(EventEditActivity.this, "提交失败，请重试！");
                            } else {
                                progressDialog.dismiss();
                                ToastUtil.shortToast(EventEditActivity.this, "提交成功");
                                if (isUpdateWithAssignee) {
                                    EventBus.getDefault().post(new UpdateAfterEditEvent(UpdateAfterEditEvent.UPDATE_LIST));
                                } else {
                                    EventBus.getDefault().post(new UpdateAfterEditEvent(UpdateAfterEditEvent.UPDATE_DETAIL));
                                }
                                finish();
                            }
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            ToastUtil.shortToast(EventEditActivity.this, "提交失败，请重试！");
                        }
                    });
        } else {
            eventService.editEvent(reportId, delImgs, json, assigneeCode, assigneeName, isSendMessage, photoBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onNext(Boolean s) {
                            if (!s) {
                                progressDialog.dismiss();
                                ToastUtil.shortToast(EventEditActivity.this, "提交失败，请重试！");
                            } else {
                                progressDialog.dismiss();
                                ToastUtil.shortToast(EventEditActivity.this, "提交成功");
                                if (isUpdateWithAssignee) {
                                    EventBus.getDefault().post(new UpdateAfterEditEvent(UpdateAfterEditEvent.UPDATE_LIST));
                                } else {
                                    EventBus.getDefault().post(new UpdateAfterEditEvent(UpdateAfterEditEvent.UPDATE_DETAIL));
                                }
                                finish();
                            }
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable throwable) {
                            progressDialog.dismiss();
                            ToastUtil.shortToast(EventEditActivity.this, "提交失败，请重试！");
                        }
                    });
        }

    }

    /**
     * 保存到本地草稿
     */
//    private void saveDraft() {
//        AMDatabase.getInstance().save(problemUploadBean);
//        List<Photo> photoList = photo_item.getSelectedPhotos();
//        for (Photo photo : photoList) {
//            photo.setProblem_id(problemUploadBean.getDbid() + "");
//            AMDatabase.getInstance().save(photo);
//        }
//        ToastUtil.shortToast(this.getApplicationContext(), "保存成功");
//        finish();
//    }

    /**
     * 定位上报者当前所在位置
     */
    private void startLocate() {
        LocationUtil.register(this, 1000, 0, new LocationUtil.OnLocationChangeListener() {
            @Override
            public void getLastKnownLocation(Location location) {

            }

            @Override
            public void onLocationChanged(Location location) {
//                lat = location.getLongitude() + "";
//                lng = location.getLatitude() + "";
                requestAddress(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        });
    }

    /**
     * 根据经纬度获取详细地址
     *
     * @param location
     */
    private void requestAddress(final Location location) {
        BaiduApiService baiduApiService = new BaiduApiService(this);
        baiduApiService.parseLocation(new LatLng(location.getLatitude(), location.getLongitude()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaiduGeocodeResult>() {
                    @Override
                    public void call(BaiduGeocodeResult baiduGeocodeResult) {
//                        addrItem.setText(baiduGeocodeResult.getDetailAddress());
//                        roadItem.setText(baiduGeocodeResult.getResult().getAddressComponent().getStreet
//                                ());
                        if (baiduGeocodeResult != null) {
                            reportAddr = baiduGeocodeResult.getDetailAddress();
                            reportX = location.getLongitude();
                            reportY = location.getLatitude();
                        }

                    }
                });
    }

    /**
     * 根据经纬度获取详细地址
     */
    private void requestAddress(final double x, final double y, final Callback1<BaiduGeocodeResult> callback) {
        BaiduApiService baiduApiService = new BaiduApiService(this);
        baiduApiService.parseLocation(new LatLng(y, x))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaiduGeocodeResult>() {
                    @Override
                    public void call(BaiduGeocodeResult baiduGeocodeResult) {
//                        addrItem.setText(baiduGeocodeResult.getDetailAddress());
//                        roadItem.setText(baiduGeocodeResult.getResult().getAddressComponent().getStreet
//                                ());
                        if (baiduGeocodeResult != null) {
                            callback.onCallback(baiduGeocodeResult);
                        } else {

                        }

                    }
                });
    }

    private HashMap<String, RequestBody> getPhotoBody(List<Photo> selectedPhotos) {
        String prefix = PhotoUploadType.UPLOAD_FOR_PROBLEAM;
        int i = 0;
        HashMap<String, RequestBody> requestMap = new HashMap<>();
        if (!ListUtil.isEmpty(selectedPhotos)) {
            for (Photo photo : selectedPhotos) {
                if (photo.getLocalPath() != null && photo.getHasBeenUp() != 1) { //此处只上传之前为上传到服务器的图片
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }
            return requestMap;
        }
        return requestMap;
    }

    //自行处理需要的图片
    private HashMap<String, RequestBody> getPhotoBodyForSelfProcess(List<Photo> selectedPhotos) {
        String prefix = PhotoUploadType.UPLOAD_FOR_SELF;
        int i = 0;
        HashMap<String, RequestBody> requestMap = new HashMap<>();
        if (!ListUtil.isEmpty(selectedPhotos)) {
            for (Photo photo : selectedPhotos) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName()
                            , RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }
            return requestMap;
        }
        return null;
    }

    /**
     * 获取原来已经删除掉的图片名字,
     * 多张图片用 ","隔开
     *
     * @return
     */
    private String getDeleteImgs(List<Photo> selectedPhotos) {
        StringBuilder builder = new StringBuilder();
        for (Photo photo : prePhotoList) {
            if (photo.getHasBeenUp() == 1) {
                if (!isContain(selectedPhotos, photo)) {
                    builder.append(photo.getPhotoPath());
                    builder.append(",");
                }
            }
        }
        return builder.toString();
    }

    private boolean isContain(List<Photo> selectedPhotos, Photo photo) {
        boolean isContain = false;
        for (Photo item : selectedPhotos) {
            //   if(item.getHasBeenUp() == 1){ //考虑已经上传到服务器端的
            if (item.getPhotoPath() != null) {
                if (item.getPhotoPath().equals(photo.getPhotoPath())) {
                    isContain = true;
                    break;
                }
            }
            //  }
        }
        return isContain;
    }

    /**
     * 提交服务器前的数据检查及实体类填充
     *
     * @return
     */
    private ProblemUploadBean getParamsEntity() {
        diseaseCode = "";
        if ("1".equals(preProblemUploadBean.getSfjb())) {
            problemUploadBean.setSfjb("1");
        } else if ("2".equals(preProblemUploadBean.getSfjb())) {
            problemUploadBean.setSfjb("2");
        }

        if ("true".equals(isbyself)) {
            if (!TextUtils.isEmpty(problem_tab_item_self.getText())) {
                problemUploadBean.setSelf_process_detail(problem_tab_item_self.getText());
            }
        }

        if (ListUtil.isEmpty(photo_item.getSelectedPhotos())) {
            ToastUtil.shortToast(this, "请上传现场照片");
            return null;
        }

        if (isReSelect_component) {
            if (!TextUtils.isEmpty(addrItem.getText())
                    && (mSelComponent != null || mSelDetailAddress != null)) {
                problemUploadBean.setSZWZ(addrItem.getText());
            } else {
                ToastUtil.shortToast(this, "请选择问题地点");
                return null;
            }
        } else {
            if (!TextUtils.isEmpty(addrItem.getText())) {
                problemUploadBean.setSZWZ(addrItem.getText());
            }
        }

        if (!TextUtils.isEmpty(roadItem.getText())) {
            problemUploadBean.setJDMC(roadItem.getText());
        } else {
            ToastUtil.shortToast(this, "所在道路不能为空");
            return null;
        }
        if (!TextUtils.isEmpty(facilityCode)) {
            problemUploadBean.setSSLX(facilityCode);
            //uploadBean.setSSLX_NAME(facilityName);
        } else {
            ToastUtil.shortToast(this, "请选择设施类型");
            return null;
        }
        /*if (!TextUtils.isEmpty(diseaseCode)) {
            problemUploadBean.setBHLX(diseaseCode);
            //uploadBean.setBHLX_NAME(diseaseName);

        } else {
            ToastUtil.shortToast(this, "请选择问题类型");
            return null;
        }*/

        //获取已选中的问题类型
        List<DictionaryItem> selectedEventTypes = mEventTypeAdapter.getSelectedEventTypeList();
        if ("其他".equals(facilityName)) {
            //设施类型为“其他”，问题类型没得选
            diseaseCode = "";
        } else if (ListUtil.isEmpty(selectedEventTypes)) {
            ToastUtil.shortToast(this, "请选择问题类型");
            return null;
        } else {
            diseaseCode = "";
            for (DictionaryItem dictionaryItem : selectedEventTypes) {
                diseaseCode += "," + dictionaryItem.getCode();
            }

            if (et_other.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(et_other.getText().toString().trim())) {
                diseaseCode += ",其他：" + et_other.getText().toString().trim();
            }


            diseaseCode = diseaseCode.substring(1);
        }
        problemUploadBean.setBHLX(diseaseCode);

        if (!TextUtils.isEmpty(urgencyCode)) {
            problemUploadBean.setJJCD(urgencyCode);
            //uploadBean.setJJCD_NAME(urgencyName);
        } else {
            ToastUtil.shortToast(this, "请选择紧急程度");
            return null;
        }
        if ("1".equals(preProblemUploadBean.getSfjb())) {

            if (checkdate != null && "false".equals(isbyself)) {
                problemUploadBean.setYjwcsj(checkdate);
            } else if (checkdate == null && "false".equals(isbyself)) {
                ToastUtil.shortToast(this, "请选择预计处理完成时间");
                return null;
            } else if ("true".equals(isbyself)) {
                problemUploadBean.setYjwcsj(null);
            }
            if (problemUploadBean.getYjwcsj() != null && checkdate < todayDate) {
                ToastUtil.shortToast(this, "预计处理完成时间不能早于今天，请重新选择");
                return null;
            }
        }

        //如果设施类型为“其他”，问题描述字段必填
        if ("其他".equals(facilityName)
                && TextUtils.isEmpty(problemitem.getText())) {
            ToastUtil.shortToast(this, "请填写问题描述");
            return null;
        }
        problemUploadBean.setWTMS(problemitem.getText());


        if ("false".equals(isbyself)) {
//            if(selAssignee == null){
//                ToastUtil.shortToast(this, "请选择下一环节处理人");
//                return null;
//            }
            if (selAssignee != null) {
                problemUploadBean.setAssignee(selAssignee.getUserCode());
                problemUploadBean.setAssigneeName(selAssignee.getUserName());
            }
        }

        String lng = null;
        String lat = null;
        if (isReSelect_component) {
            if (mSelComponent != null) { //之前的上报一样的逻辑
                lng = StringUtil.valueOf(((Point) mSelComponent.getGraphic().getGeometry()).getX());
                lat = StringUtil.valueOf(((Point) mSelComponent.getGraphic().getGeometry()).getY());
                if (!TextUtils.isEmpty(componentId)) {
                    problemUploadBean.setObject_id(componentId);
                }
                if (!TextUtils.isEmpty(layerId)) {
                    problemUploadBean.setLayer_id(layerId);
                }
                if (!TextUtils.isEmpty(layerName)) {
                    problemUploadBean.setLayer_name(layerName);
                }
                problemUploadBean.setLayerurl(mSelComponent.getLayerUrl());
                problemUploadBean.setUsid(StringUtil.getNotNullString(mSelComponent.getGraphic().getAttributeValue(ComponentFieldKeyConstant.USID), null));
            } else if (mSelDetailAddress != null) {
                lng = StringUtil.valueOf(mSelDetailAddress.getX());
                lat = StringUtil.valueOf(mSelDetailAddress.getY());
                problemUploadBean.setObject_id("-1");
                problemUploadBean.setLayer_id("-1");
                problemUploadBean.setLayer_name(null);
            } else {
                lng = StringUtil.valueOf(reportX);
                lat = StringUtil.valueOf(reportY);
                problemUploadBean.setSZWZ(reportAddr);
                problemUploadBean.setObject_id("-1");
                problemUploadBean.setLayer_id("-1");
                problemUploadBean.setLayer_name(null);
            }

            if (!TextUtils.isEmpty(lng)) {
                problemUploadBean.setX(lng);
            }
            if (!TextUtils.isEmpty(lat)) {
                problemUploadBean.setY(lat);
            }
        } else {
            if (!TextUtils.isEmpty(preProblemUploadBean.getLayerurl()) || "1".equals(preProblemUploadBean.getSfjb()) || "2".equals(preProblemUploadBean.getSfjb())) {
                lng = preProblemUploadBean.getX();
                lat = preProblemUploadBean.getY();
                if (!TextUtils.isEmpty(preProblemUploadBean.getObject_id())) {
                    problemUploadBean.setObject_id(preProblemUploadBean.getObject_id());
                }
                if (!TextUtils.isEmpty(preProblemUploadBean.getLayer_id())) {
                    problemUploadBean.setLayer_id(preProblemUploadBean.getLayer_id());
                }
                if (!TextUtils.isEmpty(preProblemUploadBean.getLayer_name())) {
                    problemUploadBean.setLayer_name(preProblemUploadBean.getLayer_name());

                }
                problemUploadBean.setLayerurl(preProblemUploadBean.getLayerurl());
                problemUploadBean.setUsid(StringUtil.getNotNullString(preProblemUploadBean.getUsid(), null));
            } else {
                lng = preProblemUploadBean.getX();
                lat = preProblemUploadBean.getY();

                problemUploadBean.setObject_id("-1");
                problemUploadBean.setLayer_id("-1");
                problemUploadBean.setLayer_name(null);
            }

            if (!TextUtils.isEmpty(lng)) {
                problemUploadBean.setX(lng);
            }
            if (!TextUtils.isEmpty(lat)) {
                problemUploadBean.setY(lat);
            }
        }

        problemUploadBean.setReportx(StringUtil.valueOf(reportX));
        problemUploadBean.setReporty(StringUtil.valueOf(reportY));
        problemUploadBean.setReportaddr(reportAddr);
        problemUploadBean.setSBR(BaseInfoManager.getUserName(EventEditActivity.this));
        problemUploadBean.setIsbyself(isbyself);
        problemUploadBean.setTemplateCode("GX_XCYH");
        problemUploadBean.setTime(System.currentTimeMillis());
        return problemUploadBean;
    }

    /**
     * 保存到本地草稿前的数据检查及实体类填充
     *
     * @return
     */
    @Deprecated

//    private ProblemUploadBean getDraftParamsEntity() {
//
//        if (ListUtil.isEmpty(photo_item.getSelectedPhotos())) {
//            ToastUtil.shortToast(this, "请上传现场照片");
//            return null;
//        }
//        if (!TextUtils.isEmpty(addrItem.getText())
//                && (mSelComponent != null || mSelDetailAddress != null)) {
//            problemUploadBean.setSZWZ(addrItem.getText());
//        } else {
//            ToastUtil.shortToast(this, "请选择问题地点");
//            return null;
//        }
//        if (!TextUtils.isEmpty(roadItem.getText())) {
//            problemUploadBean.setJDMC(roadItem.getText());
//        } else {
//            ToastUtil.shortToast(this, "所在道路不能为空");
//            return null;
//        }
//        if (!TextUtils.isEmpty(facilityCode)) {
//            problemUploadBean.setSSLX(facilityCode);
//            //uploadBean.setSSLX_NAME(facilityName);
//        } else {
//            ToastUtil.shortToast(this, "请选择设施类型");
//            return null;
//        }
//        /*if (!TextUtils.isEmpty(diseaseCode)) {
//            problemUploadBean.setBHLX(diseaseCode);
//            //uploadBean.setBHLX_NAME(diseaseName);
//
//        } else {
//
//        }*/
//
//        List<DictionaryItem> selectedEventTypes = mEventTypeAdapter.getSelectedEventTypeList();
//        if(ListUtil.isEmpty(selectedEventTypes)){
//            diseaseCode = "";
//        } else {
//            diseaseCode = "";
//            for(DictionaryItem dictionaryItem : selectedEventTypes){
//                diseaseCode = diseaseCode +  "," + dictionaryItem.getCode();
//            }
//            diseaseCode = diseaseCode.substring(1);
//            problemUploadBean.setBHLX(diseaseCode);
//        }
//
//        if (!TextUtils.isEmpty(urgencyCode)) {
//            problemUploadBean.setJJCD(urgencyCode);
//            //uploadBean.setJJCD_NAME(urgencyName);
//        } else {
//
//        }
//        if (!TextUtils.isEmpty(problemitem.getText())) {
//            problemUploadBean.setWTMS(problemitem.getText());
//        } else {
//        }
//
//        if("false".equals(isbyself)
//                && selAssignee != null){
//            //如果不是自行处理，则有下一环节处理人
//            problemUploadBean.setAssignee(selAssignee.getUserCode());
//            problemUploadBean.setAssigneeName(selAssignee.getUserName());
//        }
//
//        if(selAssignOrg != null){  //Rg Rm 流程需要
//            problemUploadBean.setAssigneeOrg(selAssignOrg);
//        }
//
//        String lng = null;
//        String lat = null;
//
//        if (mSelComponent != null) {
//            lng = StringUtil.valueOf(((Point) mSelComponent.getGraphic().getGeometry()).getX());
//            lat = StringUtil.valueOf(((Point) mSelComponent.getGraphic().getGeometry()).getY());
//            if (!TextUtils.isEmpty(componentId)) {
//                problemUploadBean.setObject_id(componentId);
//            }
//            if (!TextUtils.isEmpty(layerId)) {
//                problemUploadBean.setLayer_id(layerId);
//            }
//            if (!TextUtils.isEmpty(layerName)) {
//                problemUploadBean.setLayer_name(layerName);
//            }
//            problemUploadBean.setLayerurl(mSelComponent.getLayerUrl());
//            problemUploadBean.setUsid(StringUtil.getNotNullString(mSelComponent.getGraphic().getAttributeValue("USID"), null));
//        } else if (mSelDetailAddress != null) {
//            lng = StringUtil.valueOf(mSelDetailAddress.getX());
//            lat = StringUtil.valueOf(mSelDetailAddress.getY());
//            problemUploadBean.setObject_id("-1");
//            problemUploadBean.setLayer_id("-1");
//            problemUploadBean.setLayer_name(null);
//        } else {
//            lng = StringUtil.valueOf(reportX);
//            lat = StringUtil.valueOf(reportY);
//            problemUploadBean.setSZWZ(reportAddr);
//            problemUploadBean.setObject_id("-1");
//            problemUploadBean.setLayer_id("-1");
//            problemUploadBean.setLayer_name(null);
//        }
//
//        if (!TextUtils.isEmpty(lng)) {
//            problemUploadBean.setX(lng);
//        }
//        if (!TextUtils.isEmpty(lat)) {
//            problemUploadBean.setY(lat);
//        }
//
//        problemUploadBean.setReportx(StringUtil.valueOf(reportX));
//        problemUploadBean.setReporty(StringUtil.valueOf(reportY));
//        problemUploadBean.setReportaddr(reportAddr);
//        problemUploadBean.setSBR(BaseInfoManager.getUserName(EventEditActivity.this));
//        problemUploadBean.setIsbyself(isbyself);
//        problemUploadBean.setTemplateCode("GX_XCYH");
//        problemUploadBean.setTime(System.currentTimeMillis());
//        return problemUploadBean;
//    }

    //紧急程度
    private void initUrgencyView(String defaultStr) {
        urgency_state_rg.removeAllViews();

        for (int i = 0; i < urgency_type_array.length; i++) {
            RadioButton radioButton = new RadioButton(EventEditActivity.this);
            radioButton.setText(urgency_type_array[i]);
            radioButton.setLayoutParams(params);
            urgency_state_rg.addView(radioButton);

            if (defaultStr != null) {
                if (defaultStr.equals(urgency_type_array[i])) {
                    urgencyName = defaultStr;
                    urgencyCode = fingUrgencyCode(defaultStr);
                    radioButton.setChecked(true);
                }
            }
        }

//        selUrgencyState(urgency_type_array[0]);
    }

    private String fingUrgencyCode(String urgencyName) {
        int code = 0;
        for (int i = 0; i < urgency_type_array.length; i++) {
            if (urgency_type_array[i].equals(urgencyName)) {
                code = i;
                break;
            }
        }
        code = code + 1;
        return String.valueOf(code);
    }

    private void initEventTypeView(List<DictionaryItem> eventTypeList, String defaultStr) {
        Collections.sort(eventTypeList, new Comparator<DictionaryItem>() {
            @Override
            public int compare(DictionaryItem o1, DictionaryItem o2) {
                String code = o1.getValue();
                String target = code;
                if (code.length() > 1) {
                    target = code.replaceAll("[^(0-9)]", "");//去掉所有字母
                }

                String code2 = o2.getValue();
                String target2 = code2;
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
        mEventTypeAdapter.notifyDataSetChanged(eventTypeList);

        List<DictionaryItem> selectedDictionaryItem = new ArrayList<>();

        if (defaultStr != null) {
            String[] strs = defaultStr.split(",");
            for (int j = 0; j < strs.length; j++) {
                for (int i = 0; i < eventTypeList.size(); i++) {
                    if (eventTypeList.get(i).getCode().equals(strs[j])) {

                        selectedDictionaryItem.add(eventTypeList.get(i));
                    }
                }
            }
            if (!selectedDictionaryItem.isEmpty()) {
                mEventTypeAdapter.setmSelectedEventTypeList(selectedDictionaryItem);
            }
        }
    }

    //问题类型
    @Deprecated
    private void initDiseaseView(List<DictionaryItem> problemList) {
        disease_type_rg.removeAllViews();
        if (ListUtil.isEmpty(problemList)) {
            return;
        }
        Collections.sort(problemList, new Comparator<DictionaryItem>() {
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
        SecureRandom ra = new SecureRandom();
        for (int i = 0; i < problemList.size(); i++) {
            RadioButton radioButton = new RadioButton(EventEditActivity.this);
//            radioButton.setId(ra.nextInt());
            radioButton.setText(problemList.get(i).getName());
            radioButton.setLayoutParams(params);
            disease_type_rg.addView(radioButton);
            if (i == 0) {
                //                // 设置默认选中方式1 ，先获取控件，然后设置选中
                //                //根据id 获取radioButton 控件
                //                RadioButton rb_checked = (RadioButton) radioGroup.findViewById(radioButton.getId());
                //                //设置默认选中
                //                rb_checked.setChecked(true);

                // 设置默认选中方式2
//                disease_type_rg.check(radioButton.getId());
            }
        }
        selEventType(problemList.get(0).getName());
    }

    //初始化设施类型列表
    private void initFacilityType(List<DictionaryItem> facility_type_list, String defaultStr) {
        Collections.sort(facility_type_list, new Comparator<DictionaryItem>() {
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
        mFacilityTypeAdapter.notifyDataSetChanged(facility_type_list);
        //  mFacilityTypeAdapter.setSelectedPosition(0);

        int target = 0;
        if (defaultStr != null) {
            for (int i = 0; i < facility_type_list.size(); i++) {
                if (facility_type_list.get(i).getCode().equals(defaultStr)) {
                    target = i;
                    facilityCode = facility_type_list.get(i).getCode();
                    break;
                }
            }
        }
        mFacilityTypeAdapter.setSelectedPosition(target);
    }

    //初始化设施类型列表
    @Deprecated
    private void initFacilityView(List<DictionaryItem> facility_type_list) {
        Collections.sort(facility_type_list, new Comparator<DictionaryItem>() {
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
        SecureRandom ra = new SecureRandom();
        for (int i = 0; i < facility_type_list.size(); i++) {
            RadioButton radioButton = new RadioButton(EventEditActivity.this);
//            radioButton.setId(ra.nextInt());
//            radioButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_radiobutton2, null));
            radioButton.setText(facility_type_list.get(i).getName());
            radioButton.setLayoutParams(params);
            facility_type_rg.addView(radioButton);
            if (i == 0) {
                //                // 设置默认选中方式1 ，先获取控件，然后设置选中
                //                //根据id 获取radioButton 控件
                //                RadioButton rb_checked = (RadioButton) radioGroup.findViewById(radioButton.getId());
                //                //设置默认选中
                //                rb_checked.setChecked(true);

                // 设置默认选中方式2
//                facility_type_rg.check(radioButton.getId());
            }
        }
        selFacilityType(facility_type_list.get(0).getName());
    }

    private void initNextLinkAssignnersRadioButton(Assigneers assigneers) {
        /**
         *  64dp菜单的边距{@link DrawerLayout#MIN_DRAWER_MARGIN}+10dp*2为菜单内部的padding=84dp
         */
        float margin = DensityUtils.dp2px(this, 60);
        float width = DensityUtils.getWidth(this);
        for (Assigneers.Assigneer assigneer : assigneers.getAssigneers()) {
            RadioButton rb = (RadioButton) getLayoutInflater().inflate(R.layout.radiobutton_view, null);
            rb.setText(assigneer.getUserName());
            rb.setTag(assigneer);
            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams((int) (width - margin) / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
            rb.setLayoutParams(lp);
        }

    }

    /**
     * 设置设施类型选中值
     *
     * @param layerName
     */
    private void selFacilityType(String layerName) {
        for (int i = 0; i < facility_type_rg.getChildCount(); i++) {
            View rbv = facility_type_rg.getChildAt(i);
            if (rbv instanceof RadioButton) {
                RadioButton rb = (RadioButton) rbv;
                if (rb.getText().toString().equals(layerName)) {
                    facility_type_rg.check(rb.getId());
                    break;
                }
            }
        }
    }

    /**
     * 设置问题类型选中值
     *
     * @param eventTypes
     */
    private void selEventType(String eventTypes) {
        String[] eventTypeArr = eventTypes.split(",");
        mEventTypeAdapter.setSelectedEventTypes(eventTypeArr);
    }

    /**
     * 设置问题类型选中值
     *
     * @param eventType
     */
    @Deprecated
    private void selEventType2(String eventType) {
        for (int i = 0; i < disease_type_rg.getChildCount(); i++) {
            View rbv = disease_type_rg.getChildAt(i);
            if (rbv instanceof RadioButton) {
                RadioButton rb = (RadioButton) rbv;
                if (rb.getText().toString().equals(eventType)) {
                    disease_type_rg.check(rb.getId());
                    break;
                }
            }
        }
    }

    /**
     * 设置紧急程度选中值
     *
     * @param urgency
     */
    private void selUrgencyState(String urgency) {
        for (int i = 0; i < urgency_state_rg.getChildCount(); i++) {
            View rbv = urgency_state_rg.getChildAt(i);
            if (rbv instanceof RadioButton) {
                RadioButton rb = (RadioButton) rbv;
                if (rb.getText().toString().equals(urgency)) {
                    urgency_state_rg.check(rb.getId());
                    break;
                }
            }
        }
    }

    private int getScreenWidths() {
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        return width;
    }

    /**
     * 设置日期
     */
    public void display(Button dateDisplay, int year,
                        int monthOfYear, int dayOfMonth) {
//        dateDisplay.setText(new StringBuffer().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth));

        dateDisplay.setText(new StringBuffer().append(year).append(monthOfYear < 9 ? "-0" : "-").append(monthOfYear + 1).append(dayOfMonth < 9 ? "-0" : "-").append(dayOfMonth));
        checkdate = new Date(year - 1900, monthOfYear, dayOfMonth).getTime();
    }

    @Override
    public void onBackPressed() {
        DialogUtil.MessageBox(EventEditActivity.this, "提示", "是否确定放弃本次编辑？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (photo_item != null) {
            photo_item.onActivityResult(requestCode, resultCode, data);
        }
        if (photo_item_self != null) {
            photo_item_self.onActivityResult(requestCode, resultCode, data);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationUtil.unregister(this);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
