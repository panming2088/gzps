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
 * ??????????????????
 * R1 ???????????????
 * Rg???Rm??????
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
    private View ll_nextlilnk_org_rg_rm;     //Rg???Rm???????????????????????????UI
    private AutoBreakViewGroup radio_nextlink_org_rg_rm;   //Rg???Rm???????????????UI
    private ProgressDialog progressDialog;

    private TableDBService dbService;

    private String componentId;
    private String layerId;
    private String layerName;
    private String[] urgency_type_array = {"??????", "?????????", "??????"};
    private double reportX;       //?????????????????????X??????
    private double reportY;   //?????????????????????Y??????
    private String reportAddr;      //???????????????????????????

    //?????????????????????,?????????????????????"?????????"??????,??????????????????,????????????????????????????????????
    private String isbyself = "false"; //??????????????????

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
    private String isSendMessage = "0";//???????????????,1?????????

    private CheckBox cb_is_send_message;//??????????????????????????????


    private View ll_self_process;//??????????????????UI
    private TextFieldTableItem problem_tab_item_self;
    private EditText et_other;
    private int Position = -1;
    private boolean isObjectId;
    private List<String> attribute = Arrays.asList("??????(????????????)","??????","?????????","?????????","????????????","????????????(????????????)","????????????","????????????(????????????)");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_upload);
        initView();
        initListener();
        initData();

        // ?????????????????????reportx???reporty???reportaddr??????????????????????????????
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

        ((TextView) findViewById(R.id.tv_title)).setText("????????????");
        //???????????????????????????
//        initFacilityView(facility_type_list);
        initFacilityType(facility_type_list);
        initUrgencyView();

        User user = new LoginService(getApplicationContext(), AMDatabase.getInstance()).getUser();
        //?????????Rg???Rm ?????????????????????
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
                if ("??????".equals(facilityName)) {
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

                    if (((DictionaryItem) selectedData).getName().contains("??????")) {
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
        progressDialog.setMessage("????????????...");
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
                if ("??????".equals(facilityName)) {
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

        //????????????????????????
        RxView.clicks(upload_btn)
                .throttleFirst(2, TimeUnit.SECONDS)   //2???????????????????????????????????????????????????
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        User user = new LoginService(getApplicationContext(), AMDatabase.getInstance()).getUser();
                        if ((user.getRoleCode().contains(GzpsConstant.roleCodes[2])
                       /* ||user.getRoleCode().contains(GzpsConstant.roleCodes[3])
                        || user.getRoleCode().contains(GzpsConstant.roleCodes[4]) */)) {
                            //Rg???Rm??????????????????
                            ToastUtil.longToast(EventUploadActivity.this,
                                    "????????????????????????APP??????????????????????????????????????????????????????????????????????????????????????????????????????");
                            return;
                        }

                        if (user.getRoleCode().contains(GzpsConstant.roleCodes[1])) {
                            ToastUtil.longToast(EventUploadActivity.this,
                                    "?????????????????????APP??????????????????????????????????????????????????????????????????????????????????????????????????????");
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

        //???????????????????????????
        RxView.clicks(btn_save_draft)
                .throttleFirst(2, TimeUnit.SECONDS)   //2???????????????????????????????????????????????????
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        User user = new LoginService(getApplicationContext(), AMDatabase.getInstance()).getUser();
                        if ((user.getRoleCode().contains(GzpsConstant.roleCodes[2])
                       /* ||user.getRoleCode().contains(GzpsConstant.roleCodes[3])
                        || user.getRoleCode().contains(GzpsConstant.roleCodes[4]) */)) {
                            //Rg???Rm??????????????????
                            ToastUtil.longToast(EventUploadActivity.this,
                                    "????????????????????????APP??????????????????????????????????????????????????????????????????????????????????????????????????????");
                            return;
                        }

                        if (user.getRoleCode().contains(GzpsConstant.roleCodes[1])) {
                            ToastUtil.longToast(EventUploadActivity.this,
                                    "?????????????????????APP??????????????????????????????????????????????????????????????????????????????????????????????????????");
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
         * ???????????????????????????
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

        //??????????????????
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
                DialogUtil.MessageBox(EventUploadActivity.this, "??????", "?????????????????????????????????", new DialogInterface.OnClickListener() {
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
                                if ("????????????".equals(orgItems.get(i).getName())) {
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
     * ??????????????????????????????R1????????????????????????
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
     * ???????????????????????????????????????
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
            //??????????????????
            layerName = getLayerName(component.getLayerName());
//            selFacilityType(layerName);
            if ("?????????".equals(layerName) && Position != -1) {
                ((RadioButton) radio_nextlink_org_rg_rm.getChildAt(Position)).setChecked(true);
            } else if (Position != -1) {
                ((RadioButton) radio_nextlink_org_rg_rm.getChildAt(0)).setChecked(true);
            }
            mFacilityTypeAdapter.selectItemByName(layerName);
            mFacilityTypeAdapter.setEnable(false);
            RadioGroupUtil.disableRadioGroup(facility_type_rg);  //???????????????????????????????????????????????????
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
            if ("?????????".equals(layerName)) {
                layerName = "??????";
                mFacilityTypeAdapter.selectItemByName(layerName);
                ((RadioButton) radio_nextlink_org_rg_rm.getChildAt(0)).setChecked(true);
            }
            if (isObjectId && ll_nextlilnk_org_rg_rm.getVisibility() == View.GONE) {
                getNextLinkAssigneers("");
                isObjectId = false;
            }
            //??????????????????
            mFacilityTypeAdapter.setEnable(true);
            RadioGroupUtil.enableRadioGroup(facility_type_rg);   //?????????????????????????????????????????????
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
     * ???url?????????????????????id
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
     * ??????????????????
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
            ToastUtil.shortToast(this, "?????????????????????");
            progressDialog.dismiss();
            return;
        }
        //???????????????????????????
        if ("true".equals(isbyself)) {
            if (getPhotoBodyForSelfProcess(photo_item_self.getSelectedPhotos()) != null
                    && !getPhotoBodyForSelfProcess(photo_item_self.getSelectedPhotos()).isEmpty()) {
                photoBody.putAll(getPhotoBodyForSelfProcess(photo_item_self.getSelectedPhotos()));
            }
        }

        String assigneeCode = null;
        String assigneeName = null;
        if ("false".equals(isbyself) && selAssignee != null) {
            //??????????????????????????????????????????????????????
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
                            ToastUtil.shortToast(EventUploadActivity.this, "???????????????????????????");
                        } else {
                            progressDialog.dismiss();
                            ToastUtil.shortToast(EventUploadActivity.this, "????????????");
                            finish();
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ToastUtil.shortToast(EventUploadActivity.this, "???????????????????????????");
                    }
                });

    }

    /**
     * ?????????????????????
     */
    private void saveDraft() {
        AMDatabase.getInstance().save(problemUploadBean);
        List<Photo> photoList = photo_item.getSelectedPhotos();

        for (Photo photo : photoList) {
            photo.setProblem_id(problemUploadBean.getDbid() + "");
            photo.setPhotoName(PhotoUploadType.UPLOAD_FOR_PROBLEAM + photo.getPhotoName());
            AMDatabase.getInstance().save(photo);
        }

        //?????????????????????
        if ("true".equals(isbyself)) {
            List<Photo> photoList1 = photo_item_self.getSelectedPhotos();
            for (Photo photo : photoList1) {
                photo.setProblem_id(problemUploadBean.getDbid() + "");
                photo.setPhotoName(PhotoUploadType.UPLOAD_FOR_SELF + photo.getPhotoName());
                AMDatabase.getInstance().save(photo);
            }
        }
        ToastUtil.shortToast(this.getApplicationContext(), "????????????");
        finish();
    }

    /**
     * ?????????????????????????????????
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
     * ?????????????????????????????????
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
     * ?????????????????????????????????
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

    //???????????????????????????
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

    //???????????????????????????
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
     * ???????????????????????????????????????????????????
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
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }
        if (!TextUtils.isEmpty(addrItem.getText())
                && (mSelComponent != null || mSelDetailAddress != null)) {
            problemUploadBean.setSZWZ(addrItem.getText());
        } else {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }
        if (!TextUtils.isEmpty(roadItem.getText())) {
            problemUploadBean.setJDMC(roadItem.getText());
        } else {
            ToastUtil.shortToast(this, "????????????????????????");
            return null;
        }
        if (!TextUtils.isEmpty(facilityCode)) {
            problemUploadBean.setSSLX(facilityCode);
            //uploadBean.setSSLX_NAME(facilityName);
        } else {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }
        /*if (!TextUtils.isEmpty(diseaseCode)) {
            problemUploadBean.setBHLX(diseaseCode);
            //uploadBean.setBHLX_NAME(diseaseName);

        } else {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }*/

        //??????????????????????????????
        List<DictionaryItem> selectedEventTypes = mEventTypeAdapter.getSelectedEventTypeList();
        if ("??????".equals(facilityName)) {
            //???????????????????????????????????????????????????
            diseaseCode = "";
        } else if (ListUtil.isEmpty(selectedEventTypes)) {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        } else {
            diseaseCode = "";
            for (DictionaryItem dictionaryItem : selectedEventTypes) {
                diseaseCode += "," + dictionaryItem.getCode();
            }

            if (et_other.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(et_other.getText().toString().trim())) {
                diseaseCode += ",?????????" + et_other.getText().toString().trim();
            }

            diseaseCode = diseaseCode.substring(1);
        }
        problemUploadBean.setBHLX(diseaseCode);

        if (!TextUtils.isEmpty(urgencyCode)) {
            problemUploadBean.setJJCD(urgencyCode);
            //uploadBean.setJJCD_NAME(urgencyName);
        } else {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }
        //????????????????????????????????????????????????????????????
        if ("??????".equals(facilityName)
                && TextUtils.isEmpty(problemitem.getText())) {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }
        problemUploadBean.setWTMS(problemitem.getText());

        if ("false".equals(isbyself)) {
            if (selAssignee == null) {
                ToastUtil.shortToast(this, "??????????????????????????????");
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
     * ?????????????????????????????????????????????????????????
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
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }
        if (!TextUtils.isEmpty(addrItem.getText())
                && (mSelComponent != null || mSelDetailAddress != null)) {
            problemUploadBean.setSZWZ(addrItem.getText());
        } else {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }
        if (!TextUtils.isEmpty(roadItem.getText())) {
            problemUploadBean.setJDMC(roadItem.getText());
        } else {
            ToastUtil.shortToast(this, "????????????????????????");
            return null;
        }
        if (!TextUtils.isEmpty(facilityCode)) {
            problemUploadBean.setSSLX(facilityCode);
            //uploadBean.setSSLX_NAME(facilityName);
        } else {
            ToastUtil.shortToast(this, "?????????????????????");
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
                diseaseCode += ",?????????" + et_other.getText().toString().trim();
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
            //??????????????????????????????????????????????????????
            problemUploadBean.setAssignee(selAssignee.getUserCode());
            problemUploadBean.setAssigneeName(selAssignee.getUserName());
        }

        if (selAssignOrg != null) {  //Rg Rm ????????????
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

    //????????????
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
                    target = code.replaceAll("[^(0-9)]", "");//??????????????????
                }

                String code2 = o2.getValue();
                String target2 = code2;
                if (code2.length() > 1) {
                    target2 = code2.replaceAll("[^(0-9)]", "");//??????????????????
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

    //????????????
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
                    target = code.replaceAll("[^(0-9)]", "");//??????????????????
                }

                String code2 = o2.getCode();
                String target2 = code;
                if (code2.length() > 1) {
                    target2 = code2.replaceAll("[^(0-9)]", "");//??????????????????
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
                //                // ????????????????????????1 ???????????????????????????????????????
                //                //??????id ??????radioButton ??????
                //                RadioButton rb_checked = (RadioButton) radioGroup.findViewById(radioButton.getId());
                //                //??????????????????
                //                rb_checked.setChecked(true);

                // ????????????????????????2
//                disease_type_rg.check(radioButton.getId());
            }
        }
        selEventType(problemList.get(0).getName());
    }

    //???????????????????????????
    private void initFacilityType(List<DictionaryItem> facility_type_list) {
        Collections.sort(facility_type_list, new Comparator<DictionaryItem>() {
            @Override
            public int compare(DictionaryItem o1, DictionaryItem o2) {
                String code = o1.getCode();
                String target = code;
                if (code.length() > 1) {
                    target = code.replaceAll("[^(0-9)]", "");//??????????????????
                }

                String code2 = o2.getCode();
                String target2 = code;
                if (code2.length() > 1) {
                    target2 = code2.replaceAll("[^(0-9)]", "");//??????????????????
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

    //???????????????????????????
    @Deprecated
    private void initFacilityView(List<DictionaryItem> facility_type_list) {
        Collections.sort(facility_type_list, new Comparator<DictionaryItem>() {
            @Override
            public int compare(DictionaryItem o1, DictionaryItem o2) {
                String code = o1.getCode();
                String target = code;
                if (code.length() > 1) {
                    target = code.replaceAll("[^(0-9)]", "");//??????????????????
                }

                String code2 = o2.getCode();
                String target2 = code;
                if (code2.length() > 1) {
                    target2 = code2.replaceAll("[^(0-9)]", "");//??????????????????
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
                //                // ????????????????????????1 ???????????????????????????????????????
                //                //??????id ??????radioButton ??????
                //                RadioButton rb_checked = (RadioButton) radioGroup.findViewById(radioButton.getId());
                //                //??????????????????
                //                rb_checked.setChecked(true);

                // ????????????????????????2
//                facility_type_rg.check(radioButton.getId());
            }
        }
        selFacilityType(facility_type_list.get(0).getName());
    }

    private void initNextLinkAssignnersRadioButton(Assigneers assigneers) {
        /**
         *  64dp???????????????{@link DrawerLayout#MIN_DRAWER_MARGIN}+10dp*2??????????????????padding=84dp
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
     * ???????????????????????????
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
     * ???????????????????????????
     *
     * @param eventTypes
     */
    private void selEventType(String eventTypes) {
        String[] eventTypeArr = eventTypes.split(",");
        mEventTypeAdapter.setSelectedEventTypes(eventTypeArr);
    }

    /**
     * ???????????????????????????
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
     * ???????????????????????????
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
        DialogUtil.MessageBox(EventUploadActivity.this, "??????", "?????????????????????????????????", new DialogInterface.OnClickListener() {
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
     * ?????????????????????????????????
     * @param oldLayerName
     * @return
     */
    private String getLayerName(String oldLayerName) {
        if (!attribute.contains(oldLayerName))
            return "";
        if (oldLayerName.contains("??????")) {
            return "??????";
        } else if (oldLayerName.contains("????????????")) {
            return "????????????";
        } else if (oldLayerName.contains("?????????")) {
            return "?????????";
        } else if (oldLayerName.contains("?????????")) {
            return "?????????";
        } else if (oldLayerName.contains("????????????")) {
            return "????????????";
        } else {
            return "";
        }
    }
}
