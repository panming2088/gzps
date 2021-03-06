package com.augurit.agmobile.gzps.journal.view.uploadnewjournal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.OnSpinnerChangedEvent;
import com.augurit.agmobile.gzps.common.model.OUser;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.service.GzpsService;
import com.augurit.agmobile.gzps.common.widget.FlexRadioGroup;
import com.augurit.agmobile.gzps.common.widget.MultiSelectTableItlem;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.journal.model.TeamMember;
import com.augurit.agmobile.gzps.journal.service.JournalService;
import com.augurit.agmobile.gzps.journal.view.uploadevent.JournalEventUploadActivity;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityAddressErrorModel;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityInfoErrorModel;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.service.CorrectFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.util.UploadLayerFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadfacility.view.SendFacilityOwnerShipUnit;
import com.augurit.agmobile.gzps.uploadfacility.view.UploadFacilitySuccessEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.FacilityAddressErrorView;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.FacilityInfoErrorView;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.RefreshMyModificationListEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.drainageentity.DrainageEntityView;
import com.augurit.agmobile.gzps.uploadfacility.view.drainageentity.SelectDrainageEntityActivity;
import com.augurit.agmobile.gzps.uploadfacility.view.facilityprobrem.FacilityProblemView;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.baiduapi.BaiduApiService;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.common.model.LocationInfo;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.locate.BaiduLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.model.BaiduGeocodeResult;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.loc.util.LocationUtil;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.spinner.AMSpinner;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.ResourceUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.google.android.flexbox.FlexboxLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.crashreport.CrashReport;

import org.apache.commons.collections4.MapUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * ???????????????????????????????????????????????????????????????????????????????????????????????????
 * <p>
 * Created by xcl on 2017/12/15.
 */

public class AddNewJournalActivity extends BaseActivity {

    private MultiTakePhotoTableItem takePhotoItem;
    private View btn_upload_journal;

    private ModifiedFacility modifiedFacility;

    private TextItemTableItem tableitem_current_time;
    private TextItemTableItem tableitem_current_user;
    private TextFieldTableItem textitem_description;
    private LinearLayout ll_container;

    //???????????????
    private static final String COMPONENT_NOT_EXIST = "1";
    //????????????
    private static final String OTHERS = "2";

    private String errorType = OTHERS;

    private FacilityAddressErrorView componentAddressErrorView;
    private FacilityInfoErrorView componentInfoErrorView;
    private RadioButton lastSelectButton = null;
    private Component mComponent;
    private TextItemTableItem textitem_facility;

    private FlexRadioGroup rg_1;
    private CheckBox rb_component_not_exist;
    private LinearLayout ll_address_container;
    private LinearLayout ll_address;

    private View ll_textitem_description_container;

    private BaiduLocationManager baiduLocationManager;
    private Toast toast;
    private ProgressDialog progressDialog;
    private CountDownTimer countDownTimer; //?????????????????????????????????????????????

    /**
     * ????????????
     */
    private FlexboxLayout ll_problems_container;
    private FacilityProblemView facilityProblemView;


    /**
     * ?????????
     */
    private View ll_drainage_entity;
    private View btn_add_drainage_entity;
    private FlexboxLayout ll_drainage_entity_container;
    DrainageEntityView mDrainageEntityView;


    /**
     * ?????????
     */
    private CheckBox cbInnerCity;
    /**
     * ?????????
     */
    private CheckBox cbDontKnowWhere;

    private String cityVillage;
    private CheckBox cbOthers;
    private EditText etOthers;
    private AMSpinner spinnerOthers;
    private boolean mManStatusIsSpinner = false;   //?????????????????????????????????Spinner??????EditText
    private MultiSelectTableItlem selectTableItlem;
    private View btn_upload_event_journal;
    private boolean isEvent = false;
    //    private View ll_upload_event;
//    private Button btn_add_event;
//    private XRecyclerView mRecyclerView;
//    private EventDraftAdapter eventDraftAdapter;
//    private List<ProblemUploadBean> uploadBeanList;
//    private ProblemUploadBean problemUploadBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_journal);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mComponent = (Component) getIntent().getSerializableExtra("component");
        if (mComponent != null && mComponent.getGraphic() != null) {
            modifiedFacility = getModifiedFacilityFromGraphic(mComponent.getGraphic().getAttributes(), mComponent.getGraphic().getGeometry());
        }

        initView();
        if (modifiedFacility == null) {
            modifiedFacility = new ModifiedFacility();
        }
        initUser();

        initData();

        requestLocation();

        //????????????????????????
        startLocate();
    }

    private void startLocate() {

        baiduLocationManager = new BaiduLocationManager(this);
        baiduLocationManager.startLocate(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                LocationInfo lastLocation = baiduLocationManager.getLastLocation();
                if (lastLocation != null) {
                    baiduLocationManager.stopLocate();
                    if (lastLocation.getLocation() != null) {
                        modifiedFacility.setUserX(lastLocation.getLocation().getLongitude());
                        modifiedFacility.setUserY(lastLocation.getLocation().getLatitude());
                    }
                    if (lastLocation.getDetailAddress() != null) {
                        modifiedFacility.setUserAddr(lastLocation.getDetailAddress().getDetailAddress());
                    }
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

    /**
     * ??????????????????
     */
    private void requestLocation() {
        if (mComponent != null) {
            Point geometryCenter = GeometryUtil.getGeometryCenter(mComponent.getGraphic().getGeometry());
            BaiduApiService selectLocationService = new BaiduApiService(this);
            selectLocationService.parseLocation(new LatLng(geometryCenter.getY(), geometryCenter.getX()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BaiduGeocodeResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(BaiduGeocodeResult baiduGeocodeResult) {
                            if (componentAddressErrorView != null && baiduGeocodeResult != null) {
                                componentAddressErrorView.changeAddress(baiduGeocodeResult.getDetailAddress(), baiduGeocodeResult.getResult().getAddressComponent().getStreet());
                            }
                            if (baiduGeocodeResult != null) {
                                modifiedFacility.setAddr(baiduGeocodeResult.getDetailAddress());
                                modifiedFacility.setRoad(baiduGeocodeResult.getResult().getAddressComponent().getStreet());
                            }
                        }
                    });
        }
    }


    private void initData() {
//        uploadBeanList = new ArrayList<>();
        if (mComponent != null && mComponent.getGraphic() != null && mComponent.getGraphic().getAttributes() != null) {
            Map<String, Object> attributes = mComponent.getGraphic().getAttributes();
            Object o = attributes.get(ComponentFieldKeyConstant.ADDR);
            Object road = attributes.get(ComponentFieldKeyConstant.ROAD);
            if (o != null && road != null) {
                componentAddressErrorView.setAddress(o.toString(), road.toString());
            }
            String layerName = mComponent.getLayerName();
            if (!StringUtil.isEmpty(modifiedFacility.getLayerName())) {
                layerName = modifiedFacility.getLayerName();
            }

            String usid = String.valueOf(attributes.get("US_ID"));
            if (!StringUtil.isEmpty(modifiedFacility.getUsid())) {
                usid = modifiedFacility.getUsid();
            }
            if ((StringUtil.isEmpty(usid) || usid.equals("null")) && !StringUtil.isEmpty(modifiedFacility.getObjectId())) {
                usid = modifiedFacility.getObjectId();
            }
            modifiedFacility.setUsid(usid);

            modifiedFacility.setLayerName(layerName);
            String title = "";
            if (StringUtil.isEmpty(usid) || usid.contains("null")) {
                title = layerName;
            } else {
                title = StringUtil.getNotNullString(layerName, "") + "(" + StringUtil.getNotNullString(usid, "") + ")";
            }
            textitem_facility.setText(title);
            textitem_facility.setReadOnly();

            Point geometryCenter = GeometryUtil.getGeometryCenter(mComponent.getGraphic().getGeometry());
            modifiedFacility.setOriginX(geometryCenter.getX());
            modifiedFacility.setOriginY(geometryCenter.getY());

            modifiedFacility.setLayerUrl(mComponent.getLayerUrl());
        }
    }


    private void initUser() {
        User user = new LoginService(this, AMDatabase.getInstance()).getUser();
        String userName = user.getUserName();
        String userId = user.getId();

        modifiedFacility.setMarkPerson(userName);
        modifiedFacility.setMarkPersonId(userId);
        tableitem_current_user.setText(userName);
    }


    private void initView() {

        takePhotoItem = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_item);
        takePhotoItem.setPhotoNumShow(true, 6);
        takePhotoItem.setPhotoExampleEnable(true);
        takePhotoItem.setRequired(true);

        ((TextView) findViewById(R.id.tv_title)).setText("????????????????????????");

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        /**
         * ????????????
         */
        tableitem_current_time = (TextItemTableItem) findViewById(R.id.tableitem_current_time);
        /**
         * ?????????
         */
        tableitem_current_user = (TextItemTableItem) findViewById(R.id.tableitem_current_user);
        tableitem_current_user.setReadOnly();
        /**
         * ??????
         */
        textitem_description = (TextFieldTableItem) findViewById(R.id.textitem_description);

        /**
         * ??????
         */
        btn_upload_journal = findViewById(R.id.btn_upload_journal);
        btn_upload_event_journal = findViewById(R.id.btn_upload_event_journal);

        RxView.clicks(btn_upload_journal)
                .throttleFirst(2, TimeUnit.SECONDS)   //2???????????????????????????????????????????????????
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isEvent = false;
                        if (OTHERS.equals(errorType)) {
                            List<Photo> selectedPhotos = takePhotoItem.getSelectedPhotos();
                            if (ListUtil.isEmpty(selectedPhotos)) {
                                showToast("?????????????????????????????????????????????????????????");
                                //ToastUtil.iconLongToast(getApplicationContext(), R.mipmap.ic_alert_yellow, "?????????????????????????????????????????????????????????");;
                                return;
                            }
                        }

                        /**
                         * ???????????????????????????????????????????????????
                         */
                        if (cbOthers.isChecked() && !mManStatusIsSpinner && TextUtils.isEmpty(etOthers.getText().toString())) {
                            showToast("???????????????????????????");
                            return;
                        }


                        progressDialog = new ProgressDialog(AddNewJournalActivity.this);
                        progressDialog.setMessage("????????????????????????");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        upload();
                    }
                });

        RxView.clicks(btn_upload_event_journal)
                .throttleFirst(2, TimeUnit.SECONDS)   //2???????????????????????????????????????????????????
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isEvent = true;
                        if (ListUtil.isEmpty(facilityProblemView.getSelectedParentAndChildICodeList())
                                || StringUtil.isEmpty(facilityProblemView.getSelectedParentAndChildICodeList().get(0))) {
                            showToast("???????????????????????????????????????");
                            return;
                        }
                        if (OTHERS.equals(errorType)) {
                            List<Photo> selectedPhotos = takePhotoItem.getSelectedPhotos();
                            if (ListUtil.isEmpty(selectedPhotos)) {
                                showToast("?????????????????????????????????????????????????????????");
                                //ToastUtil.iconLongToast(getApplicationContext(), R.mipmap.ic_alert_yellow, "?????????????????????????????????????????????????????????");;
                                return;
                            }
                        }

                        /**
                         * ???????????????????????????????????????????????????
                         */
                        if (cbOthers.isChecked() && !mManStatusIsSpinner && TextUtils.isEmpty(etOthers.getText().toString())) {
                            showToast("???????????????????????????");
                            return;
                        }


                        progressDialog = new ProgressDialog(AddNewJournalActivity.this);
                        progressDialog.setMessage("????????????????????????");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        upload();
                    }
                });

        /**
         * ?????????
         */
        ll_drainage_entity = findViewById(R.id.ll_drainage_entity);
        ll_drainage_entity.setVisibility(View.GONE);
        btn_add_drainage_entity = findViewById(R.id.btn_add_drainage_entity);
        ll_drainage_entity_container = (FlexboxLayout) findViewById(R.id.ll_drainage_entity_container);
        btn_add_drainage_entity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewJournalActivity.this, SelectDrainageEntityActivity.class);
                if (mDrainageEntityView != null) {
                    intent.putExtra("data", mDrainageEntityView.getDrainageEntitys());
                }
                startActivityForResult(intent, SelectDrainageEntityActivity.SELECT_DRAINAGE_ENTITY);
            }
        });


        /**
         * ?????????
         */
        cbInnerCity = (CheckBox) findViewById(R.id.cb_inner_city);
        /**
         * ?????????
         */
        cbDontKnowWhere = (CheckBox) findViewById(R.id.cb_dont_know_where);
        /**
         * ??????
         */
        cbOthers = (CheckBox) findViewById(R.id.cb_others);
        etOthers = (EditText) findViewById(R.id.et_others);
        spinnerOthers = (AMSpinner) findViewById(R.id.spinner_others);
        spinnerOthers.setOnItemClickListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                if (item instanceof DictionaryItem) {
//                    etOthers.setText(((DictionaryItem) item).getName());
                }
            }
        });

        cbInnerCity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cityVillage = cbInnerCity.getText().toString();
                    cbDontKnowWhere.setChecked(false);
                    cbOthers.setChecked(false);
                }
            }
        });

        cbDontKnowWhere.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cityVillage = cbDontKnowWhere.getText().toString();
                    cbInnerCity.setChecked(false);
                    cbOthers.setChecked(false);
                }
            }
        });
        cbOthers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    etOthers.setEnabled(true);
                    spinnerOthers.setEditable(true);
                    cbInnerCity.setChecked(false);
                    cbDontKnowWhere.setChecked(false);
                    if (mManStatusIsSpinner) {
                        spinnerOthers.setVisibility(View.VISIBLE);
                    } else {
                        etOthers.setVisibility(View.VISIBLE);
                    }
                } else {
                    etOthers.setEnabled(false);
                    spinnerOthers.setEditable(false);
                    etOthers.setVisibility(View.GONE);
                    spinnerOthers.setVisibility(View.GONE);
                }
            }
        });

        if (modifiedFacility != null && cbInnerCity.getText().toString().equals(modifiedFacility.getCityVillage())) {
            cbInnerCity.setChecked(true);
        } else if (modifiedFacility != null && cbDontKnowWhere.getText().toString().equals(modifiedFacility.getCityVillage())) {
            cbDontKnowWhere.setChecked(true);
        } else if (modifiedFacility != null && !TextUtils.isEmpty(modifiedFacility.getCityVillage())) {
            /**
             * ???????????????"?????????"/"?????????"??????????????????
             */
            cbOthers.setChecked(true);
            etOthers.setText(modifiedFacility.getCityVillage());
        }

        /**
         * ????????????
         */
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        ll_address_container = (LinearLayout) findViewById(R.id.ll_address_container);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        ll_textitem_description_container = findViewById(R.id.ll_textitem_description_container);

        /**
         * ????????????
         */
        if (modifiedFacility != null) {
            componentAddressErrorView = new FacilityAddressErrorView(AddNewJournalActivity.this,
                    modifiedFacility);
        } else {
            componentAddressErrorView = new FacilityAddressErrorView(AddNewJournalActivity.this,
                    mComponent);
        }
        ll_address_container.removeAllViews();
        componentAddressErrorView.addTo(ll_address_container);
        /**
         * ????????????
         */
        if (modifiedFacility != null) {
            componentInfoErrorView = new FacilityInfoErrorView(AddNewJournalActivity.this,
                    modifiedFacility,false);
        } else {
            componentInfoErrorView = new FacilityInfoErrorView(AddNewJournalActivity.this,
                    mComponent);
        }
        ll_container.removeAllViews();
        componentInfoErrorView.addTo(ll_container);
        /**
         * ????????????
         */
        ll_problems_container = (FlexboxLayout) findViewById(R.id.ll_problems_container);
        facilityProblemView = new FacilityProblemView(this, ll_problems_container);

        final View ll_problems_container_parent = findViewById(R.id.ll_problems_container_parent);
        /**
         * ????????????
         */
        View tvResetProblems = findViewById(R.id.tv_reset_problems);
        tvResetProblems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (facilityProblemView != null) {
                    facilityProblemView.reset();
                }
            }
        });

        /**
         * ????????????
         */
        selectTableItlem = (MultiSelectTableItlem) findViewById(R.id.multiselect_table_item);
        initTeamMember();

        /**
         * ????????????
         */
        textitem_facility = (TextItemTableItem) findViewById(R.id.textitem_facility);

        rb_component_not_exist = (CheckBox) findViewById(R.id.rb_component_not_exist);
        rb_component_not_exist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    errorType = COMPONENT_NOT_EXIST;
                    ll_container.setVisibility(View.GONE);
                    ll_address.setVisibility(View.GONE);
                    takePhotoItem.setVisibility(View.GONE);
                    ll_textitem_description_container.setVisibility(View.GONE);
                    ll_problems_container_parent.setVisibility(View.GONE);
                } else {
                    errorType = OTHERS;
                    ll_container.setVisibility(View.VISIBLE);
                    ll_address.setVisibility(View.VISIBLE);
                    takePhotoItem.setVisibility(View.VISIBLE);
                    ll_textitem_description_container.setVisibility(View.VISIBLE);
                    ll_problems_container_parent.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpinnerChangedEvent(OnSpinnerChangedEvent onSpinnerChangedEvent) {
        /*if(YinjingAttributeViewUtil.ATTRIBUTE_ONE.equals(onSpinnerChangedEvent.getSpinnerName())){
            if(ll_drainage_entity != null) {
                if ("?????????".equals(onSpinnerChangedEvent.getKey())) {
                    ll_drainage_entity.setVisibility(View.VISIBLE);
                    if(mDrainageEntityView == null){
                        mDrainageEntityView = new DrainageEntityView(this, ll_drainage_entity_container);
                    }
                } else {
                    ll_drainage_entity.setVisibility(View.GONE);
                }
            }
        }*/
    }

    private void upload() {
//        completeOriginAddress();
//
//        completeOriginAttributes();

        boolean b = completeAttributes();
        /**
         * ?????????????????????????????????????????????????????????
         */
        if (!b) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            return;
        }

        completeCorrectTypeAndReportType();

        boolean b1 = completeAddress();
        /**
         * ????????????????????????????????????????????????????????????
         */
        if (!b1) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            return;
        }
        if (mComponent != null && !StringUtil.isEmpty(mComponent.getObjectId())) {
            modifiedFacility.setObjectId(String.valueOf(mComponent.getObjectId()));
        }
        modifiedFacility.setDescription(textitem_description.getText());
        modifiedFacility.setMarkPerson(tableitem_current_user.getText());


//        /**
//         * ????????????
//         */
//        if (cbDontKnowWhere.isChecked() || cbInnerCity.isChecked()) {
//            modifiedFacility.setCityVillage(cityVillage);
//        } else if (cbOthers.isChecked()) {
//            if (mManStatusIsSpinner) {
//                modifiedFacility.setCityVillage(spinnerOthers.getText());
//            } else {
//                modifiedFacility.setCityVillage(etOthers.getText().toString());
//            }
//        } else {
            modifiedFacility.setCityVillage(null);
//        }


        completeMarkTime();

        //????????????
        completeFacilityProblem();

        Map<String, Object> selectedItems = selectTableItlem.getSelectedItems();
        if (MapUtils.isEmpty(selectedItems)) {
            ToastUtil.shortToast(getApplicationContext(), "?????????????????????");
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            return;
        }

        Set<Map.Entry<String, Object>> entries = selectedItems.entrySet();
        List<TeamMember> teamMembers = new ArrayList<TeamMember>();
        for (Map.Entry<String, Object> entry : entries) {
            OUser oUser = (OUser) entry.getValue();
            TeamMember teamMember = new TeamMember();
            teamMember.setLoginName(oUser.getLoginName());
            teamMember.setUserName(oUser.getUserName());
            teamMembers.add(teamMember);
        }

        String json = JsonUtil.getJson(teamMembers);
        modifiedFacility.setTeamMember(json);

        //20???????????????
        countDownTimer = new CountDownTimer(CorrectFacilityService.TIMEOUT * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                if (progressDialog != null) {
                    progressDialog.setMessage("???????????????????????????   " + time + "s");
                }
                if (time % 20 == 0) {
                    ToastUtil.longToast(AddNewJournalActivity.this, "?????????????????????");
                }
            }

            @Override
            public void onFinish() {

            }
        };

        if (countDownTimer != null) {
            countDownTimer.start();
        }

        //????????????
        JournalService identificationService = new JournalService(getApplicationContext());
        identificationService.upload(modifiedFacility)
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
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            countDownTimer = null;
                        }

                        btn_upload_journal.setEnabled(true);
                        CrashReport.postCatchedException(new Exception("???????????????????????????????????????" +
                                BaseInfoManager.getUserName(AddNewJournalActivity.this) + "????????????" + e.getLocalizedMessage()));
                        showToast("????????????");
                        //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "????????????");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            countDownTimer = null;
                        }
                        btn_upload_journal.setEnabled(true);
                        if (responseBody.getCode() == 200) {
                            showToast("????????????");
                            // ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "????????????");
                            EventBus.getDefault().post(new RefreshMyModificationListEvent());
                            EventBus.getDefault().post(new UploadFacilitySuccessEvent());
                            if (!isEvent) {
                                finish();
                            } else {
                                MessageBox(AddNewJournalActivity.this, "??????", "???????????????????????????????????????????????????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(AddNewJournalActivity.this, JournalEventUploadActivity.class);
                                        intent.putExtra("modified", modifiedFacility);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, null);
                            }

                        } else {
                            CrashReport.postCatchedException(new Exception("???????????????????????????????????????" +
                                    BaseInfoManager.getUserName(AddNewJournalActivity.this) + "????????????" + responseBody.getMessage()));
                            showToast("???????????????????????????" + responseBody.getMessage());
                            //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "???????????????????????????" + responseBody.getMessage());
                        }
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
            builder.setPositiveButton(
                    ResourceUtil.getStringId(context, "sure"), positiveListener);
        }
        if (negetiveListener != null) {
            builder.setNegativeButton(
                    ResourceUtil.getStringId(context, "cancel"),
                    negetiveListener);
        }
        builder.setCancelable(false);
        builder.create().setCanceledOnTouchOutside(false);
        builder.show();
    }


    private void completeFacilityProblem() {
        if (OTHERS.equals(errorType)) {
            //????????????
            List<String> selectedLargeAndSmallItemList = facilityProblemView.getSelectedParentAndChildICodeList();
            //????????????
            if (selectedLargeAndSmallItemList != null && !TextUtils.isEmpty(selectedLargeAndSmallItemList.get(0))) {
                modifiedFacility.setpCode(selectedLargeAndSmallItemList.get(0));
            } else {
                modifiedFacility.setpCode("");
            }

            //????????????
            if (selectedLargeAndSmallItemList != null && !TextUtils.isEmpty(selectedLargeAndSmallItemList.get(1))) {
                modifiedFacility.setChildCode(selectedLargeAndSmallItemList.get(1));
            } else {
                modifiedFacility.setChildCode("");
            }
        } else {
            //???????????????
            modifiedFacility.setChildCode("");
            modifiedFacility.setpCode("");
        }
    }

    /**
     * ?????????????????????
     */
    private void completeOriginAttributes() {
        if (componentInfoErrorView != null) {
            FacilityInfoErrorModel facilityInfoErrorModel = componentInfoErrorView.getOriginalModel();
            modifiedFacility.setOriginAttrOne(facilityInfoErrorModel.getAttrOne());
            modifiedFacility.setOriginAttrTwo(facilityInfoErrorModel.getAttrTwo());
            modifiedFacility.setOriginAttrThree(facilityInfoErrorModel.getAttrThree());
            modifiedFacility.setOriginAttrFour(facilityInfoErrorModel.getAttrFour());
            modifiedFacility.setOriginAttrFive(facilityInfoErrorModel.getAttrFive());
        }
    }

    private void completeMarkTime() {
        long currentTimeMillis = System.currentTimeMillis();
        modifiedFacility.setMarkTime(currentTimeMillis);
        tableitem_current_time.setText(TimeUtil.getStringTimeYMDMChines(new Date(currentTimeMillis)));
    }

    private boolean completeAddress() {
        /**
         * ???????????????????????????
         */
        if (componentAddressErrorView != null && OTHERS.equals(errorType)) {
            if (!componentAddressErrorView.ifAllowUpload()) {
                ToastUtil.iconShortToast(this, R.mipmap.ic_alert_yellow, componentAddressErrorView.getNotAllowUploadReason());
                return false;
            }
            FacilityAddressErrorModel facilityAddressErrorModel = componentAddressErrorView.getFacilityAddressErrorModel();
            // if (componentAddressErrorView.getFacilityAddressErrorModel().isHasModified()) {
            modifiedFacility.setAddr(facilityAddressErrorModel.getCorrectAddress());
            modifiedFacility.setRoad(facilityAddressErrorModel.getRoad());
            //  }

            /**
             * ????????????????????????????????????????????????????????????????????????????????????
             */
            if (modifiedFacility.getCorrectType().equals("????????????")) {
                modifiedFacility.setX(modifiedFacility.getUserX());
                modifiedFacility.setY(modifiedFacility.getUserY());
            } else {
                modifiedFacility.setX(facilityAddressErrorModel.getCorrectX());
                modifiedFacility.setY(facilityAddressErrorModel.getCorrectY());

            }
            return true;
        }

        /**
         * ?????????????????????????????????x,y????????????????????????????????????
         */
        if (COMPONENT_NOT_EXIST.equals(errorType) && mComponent != null && mComponent.getGraphic() != null) {
            Point geometryCenter = GeometryUtil.getGeometryCenter(mComponent.getGraphic().getGeometry());
            modifiedFacility.setX(geometryCenter.getX());
            modifiedFacility.setY(geometryCenter.getY());
        }

        return !errorType.equals(OTHERS);

    }

    private void completeOriginAddress() {
        if (componentAddressErrorView != null) {

            if (mComponent != null && mComponent.getGraphic().getAttributes() != null) {
                Map<String, Object> attributes = mComponent.getGraphic().getAttributes();
                Object o = attributes.get(ComponentFieldKeyConstant.ADDR);
                Object road = attributes.get(ComponentFieldKeyConstant.ROAD);
                if (o != null && road != null) {
                    modifiedFacility.setOriginRoad(road.toString());
                    modifiedFacility.setOriginAddr(o.toString());
                }
            }

            if (mComponent != null && mComponent.getGraphic() != null) {
                Point geometryCenter = GeometryUtil.getGeometryCenter(mComponent.getGraphic().getGeometry());
                modifiedFacility.setOriginX(geometryCenter.getX());
                modifiedFacility.setOriginY(geometryCenter.getY());
            }
        }
    }

    private boolean completeAttributes() {
        if (componentInfoErrorView != null && OTHERS.equals(errorType)) {
            FacilityInfoErrorModel facilityInfoErrorModel = componentInfoErrorView.getFacilityInfoErrorModel();
            if (!facilityInfoErrorModel.isIfAllowUpload() && !"?????????".equals(facilityInfoErrorModel.getAttrOne())) {
                ToastUtil.iconShortToast(getApplicationContext(), R.mipmap.ic_alert_yellow, facilityInfoErrorModel.getNotAllowUploadReason());
                return false;
            }

            modifiedFacility.setAttrOne(facilityInfoErrorModel.getAttrOne());
            modifiedFacility.setAttrTwo(facilityInfoErrorModel.getAttrTwo());
            modifiedFacility.setAttrThree(facilityInfoErrorModel.getAttrThree());
            modifiedFacility.setAttrFour(facilityInfoErrorModel.getAttrFour());
            modifiedFacility.setAttrFive(facilityInfoErrorModel.getAttrFive());
            return true;
        }
        return true;
    }

    private void completeCorrectTypeAndReportType() {

        if (OTHERS.equals(errorType)) {
            if (componentInfoErrorView.getFacilityInfoErrorModel().isHasModified() &&
                    componentAddressErrorView.getFacilityAddressErrorModel().isHasModified()) {

                modifiedFacility.setCorrectType("?????????????????????");
                modifiedFacility.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);

            } else if (componentInfoErrorView.getFacilityInfoErrorModel().isHasModified()
                    && !componentAddressErrorView.getFacilityAddressErrorModel().isHasModified()) {

                modifiedFacility.setCorrectType("????????????");
                modifiedFacility.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);

            } else if (!componentInfoErrorView.getFacilityInfoErrorModel().isHasModified() &&
                    componentAddressErrorView.getFacilityAddressErrorModel().isHasModified()) {
                modifiedFacility.setCorrectType("????????????");
                modifiedFacility.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);
            } else {
                modifiedFacility.setCorrectType("????????????");
                modifiedFacility.setReportType(UploadLayerFieldKeyConstant.CONFIRM);
            }

            List<Photo> selectedPhotos = takePhotoItem.getSelectedPhotos();
            modifiedFacility.setPhotos(selectedPhotos);
            modifiedFacility.setThumbnailPhotos(takePhotoItem.getThumbnailPhotos());

        } else {
            modifiedFacility.setCorrectType("???????????????");
            modifiedFacility.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);

            //?????????????????????????????????
//            if (mComponent != null && mComponent.getGraphic() != null &&
//                    mComponent.getGraphic().getAttributes() != null) {
//
//                Object addr = mComponent.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ADDR);
//                if (addr != null) {
//                    modifiedFacility.setOriginAddr(addr.toString());
//                } else {
//                    modifiedFacility.setOriginAddr("");
//                }
//
//                Object road = mComponent.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ROAD);
//                if (road != null) {
//                    modifiedFacility.setRoad(road.toString());
//                } else {
//                    modifiedFacility.setOriginAddr("");
//                }
//
//            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (takePhotoItem != null) {
            takePhotoItem.onActivityResult(requestCode, resultCode, data);
        }
        if (mDrainageEntityView != null) {
            mDrainageEntityView.onActivityResult(requestCode, resultCode, data);
        }
//        if(requestCode == 456 && resultCode == 456){
//            problemUploadBean = (ProblemUploadBean) data.getSerializableExtra("problemUploadBean");
//            uploadBeanList.add(problemUploadBean);
//            eventDraftAdapter.notifyDataSetChanged(uploadBeanList);
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        if (componentAddressErrorView != null) {
            componentAddressErrorView.destroy();
        }

        if (componentInfoErrorView != null) {
            componentInfoErrorView.destroy();
        }

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (LocationUtil.ifUnRegister()) {
            LocationUtil.unregister(AddNewJournalActivity.this);
        }

        if (facilityProblemView != null) {
            facilityProblemView = null;
        }
    }


    public void showToast(String toastString) {
        if (toast == null || toast.getView().findViewById(R.id.tv_toast_text) == null) {
            initToast(toastString);
        } else {
            ((TextView) toast.getView().findViewById(com.augurit.am.fw.R.id.tv_toast_text)).setText(toastString);
        }
        //????????????
        toast.show();
    }

    public void initToast(String toastString) {
        //??????????????????Toast????????????
        toast = new Toast(getApplication());
        //??????Tosat????????? ??????????????????
        toast.setDuration(Toast.LENGTH_SHORT);
        View view1 = View.inflate(this, com.augurit.am.fw.R.layout.view_toast, null);
        ((TextView) view1.findViewById(com.augurit.am.fw.R.id.tv_toast_text)).setText(toastString);
        ((ImageView) view1.findViewById(com.augurit.am.fw.R.id.iv_icon)).setImageResource(R.mipmap.ic_alert_yellow);
        //???????????????????????????Toast
        toast.setView(view1);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    public void showAlertDialog() {

        DialogUtil.MessageBox(this, "??????", "????????????????????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    /**
     * ??????????????????
     *
     * @param sendFacilityOwnerShipUnit
     */
    @Subscribe
    public void onReceivedFacilityOwnerShipUnit(SendFacilityOwnerShipUnit sendFacilityOwnerShipUnit) {
        if (sendFacilityOwnerShipUnit != null && !TextUtils.isEmpty(sendFacilityOwnerShipUnit.originOwnership)) {
            //cbOthers.setChecked(true);
            //etOthers.setText(sendFacilityOwnerShipUnit.originOwnership);

            //????????????????????????
            TableDBService tableDBService = new TableDBService(this);
            List<DictionaryItem> districts = tableDBService.getDictionaryByTypecodeInDB("A169");
            //????????????????????????????????????
            String districtCode = null;
            for (DictionaryItem district : districts) {
                boolean contains = sendFacilityOwnerShipUnit.originOwnership.contains(district.getName());
                if (contains) {
                    districtCode = district.getCode();
                    break;
                }
            }
            //??????spinner
            spinnerOthers.removeAll();
            //????????????????????????????????????????????????????????????
            if (districtCode != null) {
                List<DictionaryItem> ownershipDic = tableDBService.getChildDictionaryByPCodeInDB(districtCode);
                if (!ListUtil.isEmpty(ownershipDic)) {
                    mManStatusIsSpinner = true;
                    if (cbOthers.isChecked()) {
                        spinnerOthers.setVisibility(View.VISIBLE);
                        etOthers.setVisibility(View.GONE);
                    }
                    //??????
                    Collections.sort(ownershipDic, new Comparator<DictionaryItem>() {
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
                    //??????spinner
                    for (DictionaryItem dictionaryItem : ownershipDic) {
                        //??????spinner
                        spinnerOthers.addItems(dictionaryItem.getName(), dictionaryItem);
                    }
                    //?????????????????????
                    spinnerOthers.selectItem(0);
                } else {
                    mManStatusIsSpinner = false;
                    if (cbOthers.isChecked()) {
                        spinnerOthers.setVisibility(View.GONE);
                        etOthers.setVisibility(View.VISIBLE);
                    }
//                    etOthers.setText("");
                }
            } else {
                mManStatusIsSpinner = false;
                if (cbOthers.isChecked()) {
                    spinnerOthers.setVisibility(View.GONE);
                    etOthers.setVisibility(View.VISIBLE);
                }
//                etOthers.setText("");
            }
        }
    }

    /**
     * ??????????????????
     */
    private void initTeamMember() {
        GzpsService gzpsService = new GzpsService(this.getApplicationContext());
        gzpsService.getTeamMember()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<OUser>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<OUser> oUsers) {
                        if (ListUtil.isEmpty(oUsers)) {
                            //????????????
                            selectTableItlem.setVisibility(View.GONE);
                            //ll_member_total_container.setVisibility(View.GONE);
                            return;
                        }
                        Map<String, Object> items = new LinkedHashMap<String, Object>();
                        for (OUser oUser : oUsers) {
                            items.put(oUser.getUserName(), oUser);
                        }
                        Map<String, Object> selectedItem = new HashMap<String, Object>(2);
                        selectedItem.put(new LoginRouter(getApplicationContext(), AMDatabase.getInstance()).getUser().getUserName(), new LoginRouter(getApplicationContext(), AMDatabase.getInstance()).getUser().getUserName());
                        selectTableItlem.setMultiChoice(items, selectedItem);
                    }
                });
    }

    private ModifiedFacility getModifiedFacilityFromGraphic(Map<String, Object> attribute, Geometry geometry) {
        ModifiedFacility uploadedFacility = new ModifiedFacility();
        uploadedFacility.setObjectId(objectToString(attribute.get("OBJECTID")));

        uploadedFacility.setAddr(objectToString(attribute.get("ADDR")));
        uploadedFacility.setOriginAddr(objectToString(attribute.get("ADDR")));

        uploadedFacility.setRoad(objectToString(attribute.get("ROAD")));
        uploadedFacility.setOriginRoad(objectToString(attribute.get("ROAD")));

        uploadedFacility.setAttrFive(objectToString(attribute.get("ATTR_FIVE")));
        uploadedFacility.setOriginAttrFive(objectToString(attribute.get("ATTR_FIVE")));

        uploadedFacility.setAttrFour(objectToString(attribute.get("ATTR_FOUR")));
        uploadedFacility.setOriginAttrFour(objectToString(attribute.get("ATTR_FOUR")));

        uploadedFacility.setAttrThree(objectToString(attribute.get("ATTR_THREE")));
        uploadedFacility.setOriginAttrThree(objectToString(attribute.get("ATTR_THREE")));

        uploadedFacility.setAttrTwo(objectToString(attribute.get("ATTR_TWO")));
        uploadedFacility.setOriginAttrTwo(objectToString(attribute.get("ATTR_TWO")));

        uploadedFacility.setAttrOne(objectToString(attribute.get("ATTR_ONE")));
        uploadedFacility.setOriginAttrOne(objectToString(attribute.get("ATTR_ONE")));

        uploadedFacility.setParentOrgName(objectToString(attribute.get("PARENT_ORG_NAME")));
        uploadedFacility.setDirectOrgName(objectToString(attribute.get("DIRECT_ORG_NAME")));
        uploadedFacility.setTeamOrgName(objectToString(attribute.get("TEAM_ORG_NAME")));
        uploadedFacility.setSuperviseOrgName(objectToString(attribute.get("SUPERVISE_ORG_NAME")));
        uploadedFacility.setLayerName(objectToString(attribute.get("LAYER_NAME")));
        Point geometryCenter = GeometryUtil.getGeometryCenter(geometry);
        uploadedFacility.setX(geometryCenter.getX());
        uploadedFacility.setY(geometryCenter.getY());
        uploadedFacility.setOriginX(geometryCenter.getX());
        uploadedFacility.setOriginY(geometryCenter.getY());
        uploadedFacility.setCheckState(objectToString(attribute.get("CHECK_STATE")));
        uploadedFacility.setUsid(objectToString(attribute.get("US_ID")));
        //????????????
        uploadedFacility.setCityVillage(objectToString(attribute.get("CITY_VILLAGE")));
        return uploadedFacility;
    }

    private String objectToString(Object object) {
        if (object == null) {
            return "";
        }
        return StringUtil.getNotNullString(object.toString(), "");
    }

}


