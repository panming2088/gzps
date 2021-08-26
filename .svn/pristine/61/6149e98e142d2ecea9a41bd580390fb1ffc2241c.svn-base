package com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
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
import com.augurit.agmobile.gzps.common.model.DoorNOBean;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.widget.FlexRadioGroup;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.journal.service.JournalService;
import com.augurit.agmobile.gzps.publicaffair.service.FacilityAffairService;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityAddressErrorModel;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityInfoErrorModel;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.RefreshTypeEvent;
import com.augurit.agmobile.gzps.uploadfacility.service.CorrectFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.util.UploadLayerFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadfacility.view.SendFacilityOwnerShipUnit;
import com.augurit.agmobile.gzps.uploadfacility.view.UploadFacilitySuccessEvent;
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
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.spinner.AMSpinner;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.core.geometry.Point;
import com.google.android.flexbox.FlexboxLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 设施核准界面（程序根据数据是否被修改过判断是数据纠错还是数据确认）
 * <p>
 * Created by xcl on 2017/12/15.
 */

public class CorrectOrConfirimFacilityActivity extends BaseActivity {

    private MultiTakePhotoTableItem takePhotoItem;
    private View btn_upload_journal;

    private ModifiedFacility modifiedFacility;

    private TextItemTableItem tableitem_current_time;
    private TextItemTableItem tableitem_current_user;
    private TextFieldTableItem textitem_description;
    private LinearLayout ll_container;

    //设施不存在
    private static final String COMPONENT_NOT_EXIST = "1";
    //其他情况
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
    private CountDownTimer countDownTimer; //用于上报时超过一定时间提示一次

    /**
     * 设施问题
     */
    private FlexboxLayout ll_problems_container;
    private FacilityProblemView facilityProblemView;
    //已挂牌编号
    private MultiTakePhotoTableItem take_photo_well;

    /**
     * 排水户
     */
    private View ll_drainage_entity;
    private View btn_add_drainage_entity;
    private FlexboxLayout ll_drainage_entity_container;
    DrainageEntityView mDrainageEntityView;


    /**
     * 城中村
     */
    private CheckBox cbInnerCity;
    /**
     * 三不管
     */
    private CheckBox cbDontKnowWhere;

    private String cityVillage;
    private CheckBox cbOthers;
    private EditText etOthers;
    private AMSpinner spinnerOthers;
    private boolean mManStatusIsSpinner = false;   //当前管理状态中的其他是Spinner还是EditText
    private String layerName;
    private LinearLayout ll_glzt_containt;
    private List<DoorNOBean> mDoorNOBean = new ArrayList<>();
    private List<DoorNOBean> mDoorNOBeans;
    //是否允许用户修改窨井类型
    private boolean isAllowEditWellType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_correct_or_confirm_facility);

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        modifiedFacility = new ModifiedFacility();
        mComponent = (Component) getIntent().getSerializableExtra("component");
        isAllowEditWellType = getIntent().getBooleanExtra("isAllowEditWellType",false);
        if(mComponent != null){
            layerName = mComponent.getLayerName();
        }
        initView();

        initUser();

        initData();

        requestLocation();

        //获取用户当前位置
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
     * 请求百度地址
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

        if (mComponent != null && mComponent.getGraphic() != null && mComponent.getGraphic().getAttributes() != null) {
            Map<String, Object> attributes = mComponent.getGraphic().getAttributes();
            Object o = attributes.get(ComponentFieldKeyConstant.ADDR);
            Object road = attributes.get(ComponentFieldKeyConstant.ROAD);
            if (o != null && road != null) {
                componentAddressErrorView.setAddress(o.toString(), road.toString());
            }
            String markId =  StringUtil.getNotNullString(String.valueOf(attributes.get(ComponentFieldKeyConstant.MARK_ID)),"");
            String usid =  StringUtil.getNotNullString(String.valueOf(attributes.get(ComponentFieldKeyConstant.USID)),"");
            String objectId = String.valueOf(attributes.get(ComponentFieldKeyConstant.OBJECTID));
            if(!StringUtil.isEmpty(markId)){
                modifiedFacility.setId(Long.valueOf(markId));
            }
            modifiedFacility.setUsid(usid);
            modifiedFacility.setObjectId(objectId);
            modifiedFacility.setLayerName(mComponent.getLayerName());
            String title = "";
            if(!StringUtil.isEmpty(usid)){
                title = StringUtil.getNotNullString(mComponent.getLayerName(), "") + "(" + StringUtil.getNotNullString(usid, "") + ")";
            }else{
                title = StringUtil.getNotNullString(mComponent.getLayerName(), "") + "(" + StringUtil.getNotNullString(objectId, "") + ")";
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
        //相片选取

        takePhotoItem = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_item);
        takePhotoItem.setTitle("添加照片（方位照等）");
        takePhotoItem.setPhotoExampleEnable(true);
//        takePhotoItem.setRequired(true);
        ((TextView) findViewById(R.id.tv_title)).setText("核准上报");

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        /**
         * 当前时间
         */
        tableitem_current_time = (TextItemTableItem) findViewById(R.id.tableitem_current_time);
        /**
         * 填表人
         */
        tableitem_current_user = (TextItemTableItem) findViewById(R.id.tableitem_current_user);
        tableitem_current_user.setReadOnly();
        /**
         * 描述
         */
        textitem_description = (TextFieldTableItem) findViewById(R.id.textitem_description);
        if (mComponent != null) {
            textitem_description.setText(StringUtil.getNotNullString(mComponent.getGraphic().getAttributeValue("DESRIPTION"),""));
        }

        /**
         * 提交
         */
        btn_upload_journal = findViewById(R.id.btn_upload_journal);

        RxView.clicks(btn_upload_journal)
                .throttleFirst(2, TimeUnit.SECONDS)   //2秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        if (OTHERS.equals(errorType)) {
//                            List<Photo> selectedPhotos = takePhotoItem.getSelectedPhotos();
//                            if (ListUtil.isEmpty(selectedPhotos) || selectedPhotos.size() < 3) {
//                                showToast("请按“拍照须知”要求至少提供三张照片！");
//                                //ToastUtil.iconLongToast(getApplicationContext(), R.mipmap.ic_alert_yellow, "请按“拍照须知”要求至少提供三张照片！");;
//                                return;
//                            }
                        }

                        /**
                         * 如果管理状态选了其他，那么一定要填
                         */
                        if (cbOthers.isChecked() && !mManStatusIsSpinner && TextUtils.isEmpty(etOthers.getText().toString())){
                            showToast("管理状态不可以为空");
                            return;
                        }


                        progressDialog = new ProgressDialog(CorrectOrConfirimFacilityActivity.this);
                        progressDialog.setMessage("正在提交，请等待");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        upload();
                    }
                });

        /**
         * 排水户
         */
        ll_drainage_entity = findViewById(R.id.ll_drainage_entity);
        ll_drainage_entity.setVisibility(View.GONE);
        btn_add_drainage_entity = findViewById(R.id.btn_add_drainage_entity);
        ll_drainage_entity_container = (FlexboxLayout) findViewById(R.id.ll_drainage_entity_container);
        btn_add_drainage_entity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CorrectOrConfirimFacilityActivity.this, SelectDrainageEntityActivity.class);
                if (mDrainageEntityView != null) {
                    intent.putExtra("data", mDrainageEntityView.getDrainageEntitys());
                }
                startActivityForResult(intent, SelectDrainageEntityActivity.SELECT_DRAINAGE_ENTITY);
            }
        });


        /**
         * 城中村
         */
        cbInnerCity = (CheckBox) findViewById(R.id.cb_inner_city);
        /**
         * 三不管
         */
        cbDontKnowWhere = (CheckBox) findViewById(R.id.cb_dont_know_where);
        /**
         * 其他
         */
        cbOthers = (CheckBox) findViewById(R.id.cb_others);
        etOthers = (EditText) findViewById(R.id.et_others);
        spinnerOthers = (AMSpinner) findViewById(R.id.spinner_others);
        spinnerOthers.setOnItemClickListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                if (item instanceof DictionaryItem){
//                    etOthers.setText(((DictionaryItem) item).getName());
                }
            }
        });

        cbInnerCity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    cityVillage = cbInnerCity.getText().toString();
                    cbDontKnowWhere.setChecked(false);
                    cbOthers.setChecked(false);
                }
            }
        });

        cbDontKnowWhere.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    cityVillage = cbDontKnowWhere.getText().toString();
                    cbInnerCity.setChecked(false);
                    cbOthers.setChecked(false);
                }
            }
        });
        cbOthers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    etOthers.setEnabled(true);
                    spinnerOthers.setEditable(true);
                    cbInnerCity.setChecked(false);
                    cbDontKnowWhere.setChecked(false);
                    if(mManStatusIsSpinner){
                        spinnerOthers.setVisibility(View.VISIBLE);
                    } else {
                        etOthers.setVisibility(View.VISIBLE);
                    }
                }else {
                    etOthers.setEnabled(false);
                    spinnerOthers.setEditable(false);
                    etOthers.setVisibility(View.GONE);
                    spinnerOthers.setVisibility(View.GONE);
                }
            }
        });
        String city_village = StringUtil.getNotNullString(mComponent.getGraphic().getAttributeValue("CITY_VILLAGE"),"");
        if (mComponent != null && cbInnerCity.getText().toString().equals(city_village)){
            cbInnerCity.setChecked(true);
        }else if (mComponent != null && cbDontKnowWhere.getText().toString().equals(city_village)){
            cbDontKnowWhere.setChecked(true);
        }else if (mComponent != null && !TextUtils.isEmpty(city_village)){
            /**
             * 如果既不是"城中村"/"三不管"，那么选其他
             */
            cbOthers.setChecked(true);
            etOthers.setText(city_village);
        }

        /**
         * 属性容器
         */
        ll_glzt_containt = (LinearLayout) findViewById(R.id.ll_glzt_containt);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        ll_address_container = (LinearLayout) findViewById(R.id.ll_address_container);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        ll_textitem_description_container = findViewById(R.id.ll_textitem_description_container);

        /**
         * 设施地址
         */
        componentAddressErrorView = new FacilityAddressErrorView(CorrectOrConfirimFacilityActivity.this,
                mComponent);
        ll_address_container.removeAllViews();
        componentAddressErrorView.addTo(ll_address_container);
        /**
         * 设施属性
         */
        mComponent.setAllowEditWellType(isAllowEditWellType);
        componentInfoErrorView = new FacilityInfoErrorView(CorrectOrConfirimFacilityActivity.this,
                mComponent);
        ll_container.removeAllViews();
        componentInfoErrorView.addTo(ll_container);

        if(mComponent != null){
            loadDataImage();
        }
        /**
         * 设施问题
         */
        ll_problems_container = (FlexboxLayout) findViewById(R.id.ll_problems_container);

        String pCode = StringUtil.getNotNullString(mComponent.getGraphic().getAttributeValue("PCODE"),"");
        if (mComponent != null && !StringUtil.isEmpty(pCode)) {
            String childCode = StringUtil.getNotNullString(mComponent.getGraphic().getAttributeValue("CHILD_CODE"),"");
            facilityProblemView = new FacilityProblemView(this, ll_problems_container, pCode, childCode);
        } else {
            facilityProblemView = new FacilityProblemView(this, ll_problems_container);
        }
        final View ll_problems_container_parent = findViewById(R.id.ll_problems_container_parent);
        if("排水口".equals(layerName)){
            ll_problems_container_parent.setVisibility(View.GONE);
            ll_glzt_containt.setVisibility(View.GONE);
        }else{
//            ll_problems_container_parent.setVisibility(View.VISIBLE);
//            ll_glzt_containt.setVisibility(View.VISIBLE);
        }
        /**
         * 重置设施
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
         * 设施类型
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

        take_photo_well = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_well);
    }

    private void loadDataImage() {
        final FacilityAffairService identificationService = new FacilityAffairService(this);
        String id = StringUtil.getNotNullString(mComponent.getGraphic().getAttributeValue("MARK_ID"),"0");
        identificationService.getModifiedDetail(Long.valueOf(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModifiedFacility>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ModifiedFacility modifiedIdentifications) {
                        if (modifiedIdentifications != null) {
                            takePhotoItem.setSelectedPhotos(modifiedIdentifications.getPhotos());
                            takePhotoItem.setSelThumbPhotos(modifiedIdentifications.getThumbnailPhotos());

                            take_photo_well.setSelectedPhotos(modifiedIdentifications.getWellPhotos());
                            take_photo_well.setSelThumbPhotos(modifiedIdentifications.getWellThumbnailPhotos());
                            mDoorNOBeans = modifiedIdentifications.getMpBeen();
                            modifiedIdentifications.setAllowEditWellType(isAllowEditWellType);
                            componentInfoErrorView = new FacilityInfoErrorView(CorrectOrConfirimFacilityActivity.this,
                                    modifiedIdentifications);
                            ll_container.removeAllViews();
                            componentInfoErrorView.addTo(ll_container);
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpinnerChangedEvent(OnSpinnerChangedEvent onSpinnerChangedEvent) {
        /*if(YinjingAttributeViewUtil.ATTRIBUTE_ONE.equals(onSpinnerChangedEvent.getSpinnerName())){
            if(ll_drainage_entity != null) {
                if ("接驳井".equals(onSpinnerChangedEvent.getKey())) {
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

        completeOriginAddress();

        completeOriginAttributes();

        boolean b = completeAttributes();
        /**
         * 如果此时完善属性不成功，不继续向下执行
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
         * 如果此时完善性地址不成功，不继续向下执行
         */
        if (!b1) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            return;
        }

        modifiedFacility.setDescription(textitem_description.getText());
        modifiedFacility.setMarkPerson(tableitem_current_user.getText());


        /**
         * 管理状态
         */
        if (cbDontKnowWhere.isChecked() || cbInnerCity.isChecked()){
            modifiedFacility.setCityVillage(cityVillage);
        }else if (cbOthers.isChecked()){
            if(mManStatusIsSpinner){
                modifiedFacility.setCityVillage(spinnerOthers.getText());
            } else {
                modifiedFacility.setCityVillage(etOthers.getText().toString());
            }
        } else{
            modifiedFacility.setCityVillage(null);
        }


        completeMarkTime();

        //设施问题
        completeFacilityProblem();
        if ("接驳井".equals(modifiedFacility.getAttrOne())) {
            List<Long> mMphDeletes = getMphDels(mDoorNOBean);
            modifiedFacility.setDeletempBeen(mMphDeletes);
        } else {
            modifiedFacility.setDeletempBeen(getAllMphIds());
        }
        //20秒提示一次
        countDownTimer = new CountDownTimer(CorrectFacilityService.TIMEOUT * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                if (progressDialog != null) {
                    progressDialog.setMessage("正在提交，请等待。   " + time + "s");
                }
                if (time % 20 == 0) {
                    ToastUtil.longToast(CorrectOrConfirimFacilityActivity.this, "网络忙，请稍等");
                }
            }

            @Override
            public void onFinish() {

            }
        };

        if (countDownTimer != null) {
            countDownTimer.start();
        }

        //数据纠错
        CorrectFacilityService identificationService = new CorrectFacilityService(getApplicationContext());
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
                        CrashReport.postCatchedException(new Exception("核准上报失败，上报用户是：" +
                                BaseInfoManager.getUserName(CorrectOrConfirimFacilityActivity.this) + "原因是：" + e.getLocalizedMessage()));
                        showToast("提交失败");
                        //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "提交失败");
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
//                            showToast("提交成功");
                            // ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "提交成功");
                            EventBus.getDefault().post(new RefreshMyModificationListEvent());
                            EventBus.getDefault().post(new UploadFacilitySuccessEvent());
//                            finish();
                            uploadJournal(modifiedFacility);
                        } else {
                            CrashReport.postCatchedException(new Exception("核准上报失败，上报用户是：" +
                                    BaseInfoManager.getUserName(CorrectOrConfirimFacilityActivity.this) + "原因是：" + responseBody.getMessage()));
                            showToast("保存失败，原因是：" + responseBody.getMessage());
                            //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "保存失败，原因是：" + responseBody.getMessage());
                        }
                    }
                });
    }

    /**
     * 获取删除的门牌号id
     *
     * @param mpBeen
     * @return
     */
    private List<Long> getMphDels(List<DoorNOBean> mpBeen) {
        List<Long> delIds = new ArrayList<>();
        if(ListUtil.isEmpty(mDoorNOBeans)){
            return delIds;
        }
        for (DoorNOBean doorNOBean : mDoorNOBeans) {
            boolean isDeles = true;
            for (DoorNOBean doorNOBean1 : mpBeen) {
                if (doorNOBean.getMpObjectId().equals(doorNOBean1.getMpObjectId())) {
                    isDeles = false;
                    break;
                }
            }
            if (isDeles) {
                delIds.add(doorNOBean.getId());
            }
        }
        return delIds;
    }

    /**
     * 获取删除的门牌号id
     *
     * @return
     */
    private List<Long> getAllMphIds() {
        List<Long> delIds = new ArrayList<>();
        if(ListUtil.isEmpty(mDoorNOBeans)){
            return delIds;
        }
        for (DoorNOBean doorNOBean : mDoorNOBeans) {
            if (doorNOBean.getId() != null) {
                delIds.add(doorNOBean.getId());
            }
        }
        return delIds;
    }

    private void completeFacilityProblem() {
        if (OTHERS.equals(errorType)) {
            //设施问题
            List<String> selectedLargeAndSmallItemList = facilityProblemView.getSelectedParentAndChildICodeList();
            //父类编码
            if (selectedLargeAndSmallItemList != null && !TextUtils.isEmpty(selectedLargeAndSmallItemList.get(0))) {
                modifiedFacility.setpCode(selectedLargeAndSmallItemList.get(0));
            } else {
                modifiedFacility.setpCode("");
            }

            //子类编码
            if (selectedLargeAndSmallItemList != null && !TextUtils.isEmpty(selectedLargeAndSmallItemList.get(1))) {
                modifiedFacility.setChildCode(selectedLargeAndSmallItemList.get(1));
            } else {
                modifiedFacility.setChildCode("");
            }
        } else {
            //设施不存在
            modifiedFacility.setChildCode("");
            modifiedFacility.setpCode("");
        }
    }

    /**
     * 补全原设施属性
     */
    private void completeOriginAttributes() {
        if (componentInfoErrorView != null) {
            FacilityInfoErrorModel facilityInfoErrorModel = componentInfoErrorView.getOriginalModel();
            modifiedFacility.setOriginAttrOne(facilityInfoErrorModel.getAttrOne());
            modifiedFacility.setOriginAttrTwo(facilityInfoErrorModel.getAttrTwo());
            modifiedFacility.setOriginAttrThree(facilityInfoErrorModel.getAttrThree());
            modifiedFacility.setOriginAttrFour(facilityInfoErrorModel.getAttrFour());
            modifiedFacility.setOriginAttrFive(facilityInfoErrorModel.getAttrFive());
            modifiedFacility.setMpBeen(facilityInfoErrorModel.getMpBeen());
            modifiedFacility.setSfgjjd(facilityInfoErrorModel.getAttrSfgjjd());
            modifiedFacility.setSfpsdyhxn(facilityInfoErrorModel.getSfpsdyhxn());
            modifiedFacility.setGjjdBh(facilityInfoErrorModel.getAttrJDBH());
            modifiedFacility.setGjjdZrr(facilityInfoErrorModel.getAttrJDZRR());
            modifiedFacility.setGjjdLxdh(facilityInfoErrorModel.getAttrLXDH());
            modifiedFacility.setYjbh(facilityInfoErrorModel.getAttrYJBH());
            mDoorNOBean = facilityInfoErrorModel.getMpBeen();
            if (!ListUtil.isEmpty(mDoorNOBean)) {
                List<DoorNOBean> mDoorNOBeen = new ArrayList<>();
                for (DoorNOBean doorNOBean : mDoorNOBean) {
                    if (doorNOBean.getId() == null || doorNOBean.getId() == 0) {
                        doorNOBean.setId(null);
                        mDoorNOBeen.add(doorNOBean);
                    }
                }
                modifiedFacility.setMpBeen(mDoorNOBeen);
            } else {
                modifiedFacility.setMpBeen(new ArrayList<DoorNOBean>());
            }
        }
    }

    private void completeMarkTime() {
        long currentTimeMillis = System.currentTimeMillis();
        modifiedFacility.setMarkTime(currentTimeMillis);
        tableitem_current_time.setText(TimeUtil.getStringTimeYMDMChines(new Date(currentTimeMillis)));
    }

    private boolean completeAddress() {
        /**
         * 如果不是设施不存在
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
             * 如果是数据确认，那么将设施的位置设定成当前用户所在的位置
             */
//            if (modifiedFacility.getCorrectType().equals("数据确认")) {
//                modifiedFacility.setX(modifiedFacility.getUserX());
//                modifiedFacility.setY(modifiedFacility.getUserY());
//            } else {
                modifiedFacility.setX(facilityAddressErrorModel.getCorrectX());
                modifiedFacility.setY(facilityAddressErrorModel.getCorrectY());

//            }
            return true;
        }

        /**
         * 如果是设施不存在，那么x,y坐标设成跟原设施位置一样
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
            if (!facilityInfoErrorModel.isIfAllowUpload()) {
                ToastUtil.iconShortToast(getApplicationContext(), R.mipmap.ic_alert_yellow, facilityInfoErrorModel.getNotAllowUploadReason());
                return false;
            }

            modifiedFacility.setAttrOne(facilityInfoErrorModel.getAttrOne());
            modifiedFacility.setAttrTwo(facilityInfoErrorModel.getAttrTwo());
            modifiedFacility.setAttrThree(facilityInfoErrorModel.getAttrThree());
            modifiedFacility.setAttrFour(facilityInfoErrorModel.getAttrFour());
            modifiedFacility.setAttrFive(facilityInfoErrorModel.getAttrFive());
            modifiedFacility.setAttrSix(facilityInfoErrorModel.getAttrSix());
            modifiedFacility.setAttrSeven(facilityInfoErrorModel.getAttrSeven());
            modifiedFacility.setRiverx(facilityInfoErrorModel.getX());
            modifiedFacility.setRivery(facilityInfoErrorModel.getY());
            modifiedFacility.setSfgjjd(facilityInfoErrorModel.getAttrSfgjjd());
            modifiedFacility.setSfpsdyhxn(facilityInfoErrorModel.getSfpsdyhxn());
            modifiedFacility.setGjjdBh(facilityInfoErrorModel.getAttrJDBH());
            modifiedFacility.setGjjdZrr(facilityInfoErrorModel.getAttrJDZRR());
            modifiedFacility.setGjjdLxdh(facilityInfoErrorModel.getAttrLXDH());
            modifiedFacility.setYjbh(facilityInfoErrorModel.getAttrYJBH());
            return true;
        }
        return true;
    }

    private void completeCorrectTypeAndReportType() {

        if (OTHERS.equals(errorType)) {
            if (componentInfoErrorView.getFacilityInfoErrorModel().isHasModified() &&
                    componentAddressErrorView.getFacilityAddressErrorModel().isHasModified()) {

                modifiedFacility.setCorrectType("位置与信息错误");
                modifiedFacility.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);

            } else if (componentInfoErrorView.getFacilityInfoErrorModel().isHasModified()
                    && !componentAddressErrorView.getFacilityAddressErrorModel().isHasModified()) {

                modifiedFacility.setCorrectType("信息错误");
                modifiedFacility.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);

            } else if (!componentInfoErrorView.getFacilityInfoErrorModel().isHasModified() &&
                    componentAddressErrorView.getFacilityAddressErrorModel().isHasModified()) {
                modifiedFacility.setCorrectType("位置错误");
                modifiedFacility.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);
            } else {
                modifiedFacility.setCorrectType("数据确认");
                modifiedFacility.setReportType(UploadLayerFieldKeyConstant.CONFIRM);
            }

            List<Photo> selectedPhotos = takePhotoItem.getSelectedPhotos();
            modifiedFacility.setPhotos(selectedPhotos);
            modifiedFacility.setThumbnailPhotos(takePhotoItem.getThumbnailPhotos());

            if(take_photo_well != null) {
                List<Photo> selectedPhotosWell = take_photo_well.getSelectedPhotos();
                modifiedFacility.setWellPhotos(selectedPhotosWell);
                modifiedFacility.setWellThumbnailPhotos(take_photo_well.getThumbnailPhotos());
            }

        } else {
            modifiedFacility.setCorrectType("设施不存在");
            modifiedFacility.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);

            //不使用从百度获取到地址
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
        if(findViewById(R.id.take_photo_well)!=null){
            take_photo_well = (MultiTakePhotoTableItem)findViewById(R.id.take_photo_well);
            take_photo_well.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Subscribe
    public void refreshFactifityType(RefreshTypeEvent event) {
        int type = event.getType();
        mComponent.setLayerName(type == 1 ? "窨井" : (type == 2 ? "雨水口" : "排水口"));
        if (mComponent != null && mComponent.getGraphic() != null && mComponent.getGraphic().getAttributes() != null) {
            Map<String, Object> attributes = mComponent.getGraphic().getAttributes();
            Object o = attributes.get(ComponentFieldKeyConstant.ADDR);
            Object road = attributes.get(ComponentFieldKeyConstant.ROAD);
            if (o != null && road != null) {
                componentAddressErrorView.setAddress(o.toString(), road.toString());
            }
            String markId = StringUtil.getNotNullString(String.valueOf(attributes.get(ComponentFieldKeyConstant.MARK_ID)), "");
            String usid = StringUtil.getNotNullString(String.valueOf(attributes.get(ComponentFieldKeyConstant.USID)), "");
            String objectId = String.valueOf(attributes.get(ComponentFieldKeyConstant.OBJECTID));
            if (!StringUtil.isEmpty(markId)) {
                modifiedFacility.setId(Long.valueOf(markId));
            }
            modifiedFacility.setUsid(usid);
            modifiedFacility.setObjectId(objectId);
            modifiedFacility.setLayerName(mComponent.getLayerName());
            String title = "";
            if (!StringUtil.isEmpty(usid)) {
                title = StringUtil.getNotNullString(mComponent.getLayerName(), "") + "(" + StringUtil.getNotNullString(usid, "") + ")";
            } else {
                title = StringUtil.getNotNullString(mComponent.getLayerName(), "") + "(" + StringUtil.getNotNullString(objectId, "") + ")";
            }
            textitem_facility.setText(title);
            textitem_facility.setReadOnly();
        }
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
            LocationUtil.unregister(CorrectOrConfirimFacilityActivity.this);
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
        //显示提示
        toast.show();
    }

    public void initToast(String toastString) {
        //使用带图标的Toast提示显示
        toast = new Toast(getApplication());
        //设置Tosat的属性，如显示时间
        toast.setDuration(Toast.LENGTH_SHORT);
        View view1 = View.inflate(this, com.augurit.am.fw.R.layout.view_toast, null);
        ((TextView) view1.findViewById(com.augurit.am.fw.R.id.tv_toast_text)).setText(toastString);
        ((ImageView) view1.findViewById(com.augurit.am.fw.R.id.iv_icon)).setImageResource(R.mipmap.ic_alert_yellow);
        //将布局管理器添加进Toast
        toast.setView(view1);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    public void showAlertDialog() {

        DialogUtil.MessageBox(this, "提示", "是否放弃本次编辑", new DialogInterface.OnClickListener() {
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
     * 接收权属单位
     * @param sendFacilityOwnerShipUnit
     */
    @Subscribe
    public void onReceivedFacilityOwnerShipUnit(SendFacilityOwnerShipUnit sendFacilityOwnerShipUnit){
        if (sendFacilityOwnerShipUnit != null && !TextUtils.isEmpty(sendFacilityOwnerShipUnit.originOwnership)) {
            //cbOthers.setChecked(true);
            //etOthers.setText(sendFacilityOwnerShipUnit.originOwnership);

            //获取行政区域列表
            TableDBService tableDBService = new TableDBService(this);
            List<DictionaryItem> districts = tableDBService.getDictionaryByTypecodeInDB("A169");
            //跟传递过来的区域进行对比
            String districtCode = null;
            for (DictionaryItem district : districts){
                boolean contains = sendFacilityOwnerShipUnit.originOwnership.contains(district.getName());
                if (contains){
                    districtCode = district.getCode();
                    break;
                }
            }
            //清空spinner
            spinnerOthers.removeAll();
            //根据区域从数据字典读取该区的权属单位列表
            if (districtCode != null){
                List<DictionaryItem> ownershipDic = tableDBService.getChildDictionaryByPCodeInDB(districtCode);
                if (!ListUtil.isEmpty(ownershipDic)){
                    mManStatusIsSpinner = true;
                    if(cbOthers.isChecked()) {
                        spinnerOthers.setVisibility(View.VISIBLE);
                        etOthers.setVisibility(View.GONE);
                    }
                    //排序
                    Collections.sort(ownershipDic, new Comparator<DictionaryItem>() {
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
                    //填充spinner
                    for (DictionaryItem dictionaryItem : ownershipDic){
                        //填充spinner
                        spinnerOthers.addItems(dictionaryItem.getName(),dictionaryItem);
                    }
                    String city_village = StringUtil.getNotNullString(mComponent.getGraphic().getAttributeValue("CITY_VILLAGE"),"");
                    if (spinnerOthers.containsKey(city_village)){
                        spinnerOthers.selectItem(city_village);
                    }else {
                        spinnerOthers.selectItem(0);
                    }
                } else {
                    mManStatusIsSpinner = false;
                    if(cbOthers.isChecked()) {
                        spinnerOthers.setVisibility(View.GONE);
                        etOthers.setVisibility(View.VISIBLE);
                    }
//                    etOthers.setText("");
                }
            } else {
                mManStatusIsSpinner = false;
                if(cbOthers.isChecked()) {
                    spinnerOthers.setVisibility(View.GONE);
                    etOthers.setVisibility(View.VISIBLE);
                }
//                etOthers.setText("");
            }
        }
    }

    /**
     * 提交巡检日志
     * @param modifiedFacility
     */
    private void uploadJournal(ModifiedFacility modifiedFacility) {
        //数据纠错
        modifiedFacility.setId(null);
        JournalService identificationService = new JournalService(getApplicationContext());
        identificationService.upload(modifiedFacility)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        CrashReport.postCatchedException(new Exception("巡检上报失败，上报用户是：" +
                                BaseInfoManager.getUserName(CorrectOrConfirimFacilityActivity.this) + "原因是：" + e.getLocalizedMessage()));
                        ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this,"巡检提交失败");
                        //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "提交失败");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (responseBody.getCode() == 200) {
                            ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this,"提交成功");
                        } else {
                            CrashReport.postCatchedException(new Exception("核准上报失败，上报用户是：" +
                                    BaseInfoManager.getUserName(CorrectOrConfirimFacilityActivity.this) + "原因是：" + responseBody.getMessage()));
                            ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this,"巡检日志保存失败，原因是：" + responseBody.getMessage());
                            //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "保存失败，原因是：" + responseBody.getMessage());
                        }
                    }
                });
    }

}


