package com.augurit.agmobile.gzps.uploadevent.view.uploadevent;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
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
import com.augurit.agmobile.gzps.track.service.TrackNewService;
import com.augurit.agmobile.gzps.uploadevent.adapter.EventTypeAdapter;
import com.augurit.agmobile.gzps.uploadevent.adapter.FacilityTypeAdapter;
import com.augurit.agmobile.gzps.uploadevent.adapter.NextLinkAssigneersAdapter;
import com.augurit.agmobile.gzps.uploadevent.dao.GetPersonByOrgApiData;
import com.augurit.agmobile.gzps.uploadevent.model.Assigneers;
import com.augurit.agmobile.gzps.uploadevent.model.OrgItem;
import com.augurit.agmobile.gzps.uploadevent.model.Person;
import com.augurit.agmobile.gzps.uploadevent.model.ProblemUploadBean;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.gzps.uploadevent.util.PhotoUploadType;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
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
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.RadioGroupUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxbinding.view.RxView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
 * 问题上报界面
 * R1 巡检员上报
 * Rg或Rm上报
 */
public class EventUploadActivity extends BaseActivity {
    private MultiTakePhotoTableItem photo_item;
    private MultiTakePhotoTableItem photo_item_self;

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

    //如果是自行处理,则将直接出现在"已上报"列表,并且不可撤回,无法撤回也就无法再次编辑
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


    private String activityName;
    private String sjid;
    private String isSendMessage = "0";//默认不发送,1为发送

    private CheckBox cb_is_send_message;//是否发送短信通知对方


    private View ll_self_process;//自行处理过程UI
    private TextFieldTableItem problem_tab_item_self;
    private EditText et_other;
    private int Position = -1;
    private boolean isObjectId;
    private List<String> attribute = Arrays.asList("窨井(中心城区)","窨井","雨水口","排水口","排水管道","排水管道(中心城区)","排水沟渠","排水沟渠(中心城区)");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_upload);
        initView();
        initListener();
        initData();

        // 定位当前位置，reportx、reporty、reportaddr这三个字段要传给接口
        startLocate();
        bindService();
    }

    public void bindService() {
        if (mTrackService == null) {
            Intent intent = new Intent(this, TrackNewService.class);
            this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        }
    }


    private TrackNewService mTrackService;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mTrackService = ((TrackNewService.MyBinder) service).getService();
            if(mTrackService != null && mTrackService.getCurrentTrackId() != -1){
                problemUploadBean.setTrackId(mTrackService.getCurrentTrackId()+"");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            mTrackService = null;
        }
    };

    private void initData() {
        problemUploadBean = new ProblemUploadBean();
        dbService = new TableDBService(EventUploadActivity.this);
        facility_type_list = dbService.getDictionaryByTypecodeInDB("A174");

        /*
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        */

        ((TextView) findViewById(R.id.tv_title)).setText("问题上报");
        //初始化设施类型列表
//        initFacilityView(facility_type_list);
        initFacilityType(facility_type_list);
        initUrgencyView();

        User user = new LoginService(getApplicationContext(), AMDatabase.getInstance()).getUser();
        //如果是Rg和Rm 则获取机构列表
        if (user.getRoleCode().contains(GzpsConstant.roleCodes[3])
                || user.getRoleCode().contains(GzpsConstant.roleCodes[4])) {
            ll_nextlilnk_org_rg_rm.setVisibility(View.VISIBLE);
            getOrgList();

        } else {
            ll_nextlilnk_org_rg_rm.setVisibility(View.GONE);
            getNextLinkAssigneers("");
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
        et_other = (EditText) findViewById(R.id.et_other);
        ll_eventtype = findViewById(R.id.ll_eventtype);
        gv_eventtype = (MyGridView) findViewById(R.id.gv_eventtype);
        cb_isbyself = (CheckBox) findViewById(R.id.cb_isbyself);
        ll_nextlink_assigneers = findViewById(R.id.ll_nextlink_assigneers);

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
                    initEventTypeView(problemList);
                }

                et_other.setText("");
                et_other.setVisibility(View.GONE);
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
        progressDialog = new ProgressDialog(EventUploadActivity.this);
        progressDialog.setMessage("正在提交...");
        EventBus.getDefault().register(this);

        cb_is_send_message = (CheckBox) findViewById(R.id.cb_is_send_message);

        ll_self_process = findViewById(R.id.ll_self_process);

        problem_tab_item_self = (TextFieldTableItem) findViewById(R.id.problem_tab_item_self);
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
                    initEventTypeView(problemList);
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
                            ToastUtil.longToast(EventUploadActivity.this,
                                    "您是管理层角色，APP暂不支持您的问题上报流转，开放权限的程序正在开发调试中……敬请期待！");
                            return;
                        }

                        if (user.getRoleCode().contains(GzpsConstant.roleCodes[1])) {
                            ToastUtil.longToast(EventUploadActivity.this,
                                    "您是养护人员，APP暂不支持您的问题上报流转，开放权限的程序正在开发调试中……敬请期待！");
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
        RxView.clicks(btn_save_draft)
                .throttleFirst(2, TimeUnit.SECONDS)   //2秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        User user = new LoginService(getApplicationContext(), AMDatabase.getInstance()).getUser();
                        if ((user.getRoleCode().contains(GzpsConstant.roleCodes[2])
                       /* ||user.getRoleCode().contains(GzpsConstant.roleCodes[3])
                        || user.getRoleCode().contains(GzpsConstant.roleCodes[4]) */)) {
                            //Rg和Rm暂时不能上报
                            ToastUtil.longToast(EventUploadActivity.this,
                                    "您是管理层角色，APP暂不支持您的问题上报流转，开放权限的程序正在开发调试中……敬请期待！");
                            return;
                        }

                        if (user.getRoleCode().contains(GzpsConstant.roleCodes[1])) {
                            ToastUtil.longToast(EventUploadActivity.this,
                                    "您是养护人员，APP暂不支持您的问题上报流转，开放权限的程序正在开发调试中……敬请期待！");
                            return;
                        }

                        ProblemUploadBean paramsEntity = getDraftParamsEntity();
                        if (paramsEntity == null) {
                            progressDialog.dismiss();
                            return;
                        }
                        saveDraft();
                    }
                });

        /**
         * 选择设施或问题地点
         */
        findViewById(R.id.ll_select_component).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventUploadActivity.this,
                        SelectComponentOrAddressActivity.class);
                if (mSelComponent != null) {
                    intent.putExtra("geometry", mSelComponent.getGraphic().getGeometry());
                } else if (mSelDetailAddress != null) {
                    Point point = new Point();
                    point.setX(mSelDetailAddress.getX());
                    point.setY(mSelDetailAddress.getY());
                    intent.putExtra("geometry", point);
                }
                startActivity(intent);
            }
        });

        //是否自行处理
        cb_isbyself.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isbyself = "true";
                    ll_nextlink_assigneers.setVisibility(View.GONE);
                    ll_self_process.setVisibility(View.VISIBLE);
                } else {
                    isbyself = "false";
                    ll_nextlink_assigneers.setVisibility(View.VISIBLE);
                    ll_self_process.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.MessageBox(EventUploadActivity.this, "提示", "是否确定放弃本次编辑？", new DialogInterface.OnClickListener() {
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
                                RadioButton radioButton = new RadioButton(EventUploadActivity.this);
                                radioButton.setText(orgItems.get(i).getName());
                                //    radioButton.setTag(nextLinkOrgs);
                                if ("河涌中心".equals(orgItems.get(i).getName())) {
                                    Position = i;
                                }
                                radioButton.setLayoutParams(params);
                                radio_nextlink_org_rg_rm.addView(radioButton);

                                if (i == 0) {
                                    radioButton.setChecked(true);
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
            //选择的是设施
            layerName = getLayerName(component.getLayerName());
//            selFacilityType(layerName);
            if ("排水口".equals(layerName) && Position != -1) {
                ((RadioButton) radio_nextlink_org_rg_rm.getChildAt(Position)).setChecked(true);
            } else if (Position != -1) {
                ((RadioButton) radio_nextlink_org_rg_rm.getChildAt(0)).setChecked(true);
            }
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
            String type = component.getLayerName();
            String title = StringUtil.getNotNullString(type, "");
            if (!ListUtil.isEmpty(subType) && !"null".equals(subType)) {
                title = title + "(" + StringUtil.getNotNullString(subType, "") + ")";
            }
            tv_select_or_check_location.setText(title);
//            addrItem.setText(StringUtil.getNotNullString(mSelComponent.getGraphic().getAttributeValue("ADDR"), ""));
//            roadItem.setText("");
            if (selectComponentFinishEvent.getFindResult().getGraphic().getGeometry() instanceof com.esri.core.geometry.Point) {
                com.esri.core.geometry.Point point = (com.esri.core.geometry.Point) selectComponentFinishEvent.getFindResult().getGraphic().getGeometry();
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
            } else if (selectComponentFinishEvent.getFindResult().getGraphic().getGeometry() instanceof com.esri.core.geometry.Polyline) {
                addrItem.setText(StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ADDR), ""));
                roadItem.setText(StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ROAD), ""));

            }

        } else if (mSelDetailAddress != null) {
            if ("排水口".equals(layerName)) {
                layerName = "窨井";
                mFacilityTypeAdapter.selectItemByName(layerName);
                ((RadioButton) radio_nextlink_org_rg_rm.getChildAt(0)).setChecked(true);
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
        if (selectComponentFinishEvent.getFindResult().getGraphic().getGeometry() instanceof com.esri.core.geometry.Point) {
            com.esri.core.geometry.Point point = (com.esri.core.geometry.Point) selectComponentFinishEvent.getFindResult().getGraphic().getGeometry();
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
        UploadEventService eventService = new UploadEventService(EventUploadActivity.this);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        String json = gson.toJson(problemUploadBean);
        HashMap<String, RequestBody> photoBody = getPhotoBody(photo_item.getSelectedPhotos());
        List<Photo> selectedPhotos = photo_item_self.getSelectedPhotos();
        if ("true".equals(isbyself) && ListUtil.isEmpty(selectedPhotos)) {
            ToastUtil.shortToast(this, "请上传处理照片");
            progressDialog.dismiss();
            return;
        }
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
        String isSendMessage = "0";
        if (cb_is_send_message.isChecked()) {
            isSendMessage = "1";
        } else {
            isSendMessage = "0";
        }
        eventService.uploadEvent(isSendMessage, json, assigneeCode, assigneeName, photoBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean s) {
                        if (!s) {
                            progressDialog.dismiss();
                            ToastUtil.shortToast(EventUploadActivity.this, "提交失败，请重试！");
                        } else {
                            progressDialog.dismiss();
                            ToastUtil.shortToast(EventUploadActivity.this, "提交成功");
                            finish();
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ToastUtil.shortToast(EventUploadActivity.this, "提交失败，请重试！");
                    }
                });

    }

    /**
     * 保存到本地草稿
     */
    private void saveDraft() {
        AMDatabase.getInstance().save(problemUploadBean);
        List<Photo> photoList = photo_item.getSelectedPhotos();

        for (Photo photo : photoList) {
            photo.setProblem_id(problemUploadBean.getDbid() + "");
            photo.setPhotoName(PhotoUploadType.UPLOAD_FOR_PROBLEAM + photo.getPhotoName());
            AMDatabase.getInstance().save(photo);
        }

        //自行处理的图片
        if ("true".equals(isbyself)) {
            List<Photo> photoList1 = photo_item_self.getSelectedPhotos();
            for (Photo photo : photoList1) {
                photo.setProblem_id(problemUploadBean.getDbid() + "");
                photo.setPhotoName(PhotoUploadType.UPLOAD_FOR_SELF + photo.getPhotoName());
                AMDatabase.getInstance().save(photo);
            }
        }
        ToastUtil.shortToast(this.getApplicationContext(), "保存成功");
        finish();
    }

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

    //问题上报需要的图片
    private HashMap<String, RequestBody> getPhotoBody(List<Photo> selectedPhotos) {
        String prefix = PhotoUploadType.UPLOAD_FOR_PROBLEAM;
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
     * 提交服务器前的数据检查及实体类填充
     *
     * @return
     */
    private ProblemUploadBean getParamsEntity() {
        diseaseCode = "";
        if ("true".equals(isbyself)) {
            if (!TextUtils.isEmpty(problem_tab_item_self.getText())) {
                problemUploadBean.setSelf_process_detail(problem_tab_item_self.getText());
            }
        }

        if (ListUtil.isEmpty(photo_item.getSelectedPhotos())) {
            ToastUtil.shortToast(this, "请上传现场照片");
            return null;
        }
        if (!TextUtils.isEmpty(addrItem.getText())
                && (mSelComponent != null || mSelDetailAddress != null)) {
            problemUploadBean.setSZWZ(addrItem.getText());
        } else {
            ToastUtil.shortToast(this, "请选择问题地点");
            return null;
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
        //如果设施类型为“其他”，问题描述字段必填
        if ("其他".equals(facilityName)
                && TextUtils.isEmpty(problemitem.getText())) {
            ToastUtil.shortToast(this, "请填写问题描述");
            return null;
        }
        problemUploadBean.setWTMS(problemitem.getText());

        if ("false".equals(isbyself)) {
            if (selAssignee == null) {
                ToastUtil.shortToast(this, "请选择下一环节处理人");
                return null;
            }
            problemUploadBean.setAssignee(selAssignee.getUserCode());
            problemUploadBean.setAssigneeName(selAssignee.getUserName());
        }

        String lng = null;
        String lat = null;

        if (mSelComponent != null) {
            if( mSelComponent.getGraphic().getGeometry() instanceof Polyline){
                Point point = GeometryUtil.getLineCenterPoint((Polyline) mSelComponent.getGraphic().getGeometry());
                lng = StringUtil.valueOf(point.getX());
                lat = StringUtil.valueOf(point.getY());
            }else if(mSelComponent.getGraphic().getGeometry() instanceof Point) {
                lng = StringUtil.valueOf(((Point) mSelComponent.getGraphic().getGeometry()).getX());
                lat = StringUtil.valueOf(((Point) mSelComponent.getGraphic().getGeometry()).getY());
            }
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


        problemUploadBean.setReportx(StringUtil.valueOf(reportX));
        problemUploadBean.setReporty(StringUtil.valueOf(reportY));
        problemUploadBean.setYjwcsj(null);
        problemUploadBean.setReportaddr(reportAddr);
        problemUploadBean.setSBR(BaseInfoManager.getUserName(EventUploadActivity.this));
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
    private ProblemUploadBean getDraftParamsEntity() {
        diseaseCode = "";
        if ("true".equals(isbyself)) {
            if (!TextUtils.isEmpty(problem_tab_item_self.getText())) {
                problemUploadBean.setSelf_process_detail(problem_tab_item_self.getText());
            }
        }

        if (ListUtil.isEmpty(photo_item.getSelectedPhotos())) {
            ToastUtil.shortToast(this, "请上传现场照片");
            return null;
        }
        if (!TextUtils.isEmpty(addrItem.getText())
                && (mSelComponent != null || mSelDetailAddress != null)) {
            problemUploadBean.setSZWZ(addrItem.getText());
        } else {
            ToastUtil.shortToast(this, "请选择问题地点");
            return null;
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

        }*/

        List<DictionaryItem> selectedEventTypes = mEventTypeAdapter.getSelectedEventTypeList();
        if (ListUtil.isEmpty(selectedEventTypes)) {
            diseaseCode = "";
        } else {
            diseaseCode = "";
            for (DictionaryItem dictionaryItem : selectedEventTypes) {
                diseaseCode = diseaseCode + "," + dictionaryItem.getCode();
            }

            if (et_other.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(et_other.getText().toString().trim())) {
                diseaseCode += ",其他：" + et_other.getText().toString().trim();
            }

            diseaseCode = diseaseCode.substring(1);
            problemUploadBean.setBHLX(diseaseCode);
        }

        if (!TextUtils.isEmpty(urgencyCode)) {
            problemUploadBean.setJJCD(urgencyCode);
            //uploadBean.setJJCD_NAME(urgencyName);
        } else {

        }
        if (!TextUtils.isEmpty(problemitem.getText())) {
            problemUploadBean.setWTMS(problemitem.getText());
        } else {
        }

        if ("false".equals(isbyself)
                && selAssignee != null) {
            //如果不是自行处理，则有下一环节处理人
            problemUploadBean.setAssignee(selAssignee.getUserCode());
            problemUploadBean.setAssigneeName(selAssignee.getUserName());
        }

        if (selAssignOrg != null) {  //Rg Rm 流程需要
            problemUploadBean.setAssigneeOrg(selAssignOrg);
        }

        String lng = null;
        String lat = null;

        if (mSelComponent != null) {
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

        problemUploadBean.setReportx(StringUtil.valueOf(reportX));
        problemUploadBean.setReporty(StringUtil.valueOf(reportY));
        problemUploadBean.setReportaddr(reportAddr);
        problemUploadBean.setSBR(BaseInfoManager.getUserName(EventUploadActivity.this));
        problemUploadBean.setIsbyself(isbyself);
        problemUploadBean.setTemplateCode("GX_XCYH");
        problemUploadBean.setTime(System.currentTimeMillis());
        return problemUploadBean;
    }

    //紧急程度
    private void initUrgencyView() {
        for (int i = 0; i < urgency_type_array.length; i++) {
            RadioButton radioButton = new RadioButton(EventUploadActivity.this);
            radioButton.setText(urgency_type_array[i]);
            radioButton.setLayoutParams(params);
            urgency_state_rg.addView(radioButton);
        }
//        selUrgencyState(urgency_type_array[0]);
    }

    private void initEventTypeView(List<DictionaryItem> eventTypeList) {
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
            RadioButton radioButton = new RadioButton(EventUploadActivity.this);
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
    private void initFacilityType(List<DictionaryItem> facility_type_list) {
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
        mFacilityTypeAdapter.setSelectedPosition(0);
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
            RadioButton radioButton = new RadioButton(EventUploadActivity.this);
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

    @Override
    public void onBackPressed() {
        DialogUtil.MessageBox(EventUploadActivity.this, "提示", "是否确定放弃本次编辑？", new DialogInterface.OnClickListener() {
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
        if (mTrackService != null) {
            this.unbindService(conn);
//            mTrackService = null;
        }
    }

    /**
     * 去掉中心城区的设施名称
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
        } else {
            return "";
        }
    }
}
