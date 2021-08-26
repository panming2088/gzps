package com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.model.DoorNOBean;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.widget.MyGridView;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.journal.service.JournalService;
import com.augurit.agmobile.gzps.uploadevent.adapter.AreasAdapter;
import com.augurit.agmobile.gzps.uploadevent.util.PhotoUploadType;
import com.augurit.agmobile.gzps.uploadfacility.model.Area;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityAddressErrorModel;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityAttributeModel;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.RefreshTypeEvent;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.view.SendFacilityOwnerShipUnit;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.FacilityAddressErrorView;
import com.augurit.agmobile.gzps.uploadfacility.view.facilityprobrem.FacilityProblemView;
import com.augurit.agmobile.patrolcore.common.model.LocationInfo;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.locate.BaiduLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.loc.util.LocationUtil;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.spinner.AMSpinner;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.core.geometry.Point;
import com.google.android.flexbox.FlexboxLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 数据新增界面
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.modifiedIdentification
 * @createTime 创建时间 ：17/11/3
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/3
 * @modifyMemo 修改备注：
 */

public class UploadNewFacilityActivity extends BaseActivity {
    //窨井挂牌编号
    private MultiTakePhotoTableItem take_photo_well;
    private MultiTakePhotoTableItem take_photo_item;
    //排放口挂牌编号
    private MultiTakePhotoTableItem take_photo_pfk;
    private View btn_upload_journal;
    private UploadedFacility uploadFacility;
    private ModifiedFacility modifiedFacility;
    private TextItemTableItem tableitem_current_time;
    private TextItemTableItem tableitem_current_user;
    private TextFieldTableItem textitem_description;
    private LinearLayout ll_attributelist_container;

    private LinearLayout ll_container;
    private FacilityAttributeView facilityAttributeView;

    private DetailAddress detailAddress;
    private BaiduLocationManager baiduLocationManager;

    private Toast toast;
    private ProgressDialog progressDialog;
    private CountDownTimer countDownTimer; //用于上报时超过一定时间提示一次
    private FlexboxLayout ll_problems_container;
    private FacilityProblemView facilityProblemView;
    private FacilityAddressErrorView mComponentAddressErrorView;
    private MyGridView gv_area;
    AreasAdapter mAreasAdapter;
    private View groups;
    private View ll_mange_state,ll_company ,ll_problem,ll_glzt_containt;
    /**
     * 城中村
     */
    protected CheckBox cbInnerCity;
    /**
     * 三不管
     */
    protected CheckBox cbDontKnowWhere;
    //1为窨井 ，2为雨水井，3为排放口
    private int facilityType = 1;


    private TextView type_title;
    protected String company = null;
    protected String cityVillage = null;
    protected CheckBox cbOthers;
    protected EditText etOthers;
    protected AMSpinner spinnerOthers;
    private boolean mManStatusIsSpinner = false;   //当前管理状态中的其他是Spinner还是EditText
    private String[] lead_yAxle = {"天河", "番禺", "黄埔", "白云", "南沙", "海珠", "荔湾", "花都", "越秀", "增城", "从化"};

    /**
     * 水投集团
     */
    protected CheckBox cbwaterGroup;
    /**
     * 水投集团
     */
    protected CheckBox cbProtectionGroup;
    /**
     * 区
     */
    protected CheckBox cbAreaGroup;
    private Button btn_upload_event_journal;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reedit_upload_new_facility);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        gv_area = (MyGridView) findViewById(R.id.gv_area);
        groups = findViewById(R.id.groups);
        /**
         * 在Activity跳转之前已经确保detailAddress和location不为空，所以下面的代码省去了这两个变量的非空判断
         */
        detailAddress = getIntent().getParcelableExtra("detailAddress");

        uploadFacility = new UploadedFacility();
        modifiedFacility = new ModifiedFacility();

        initView();

        initUser();

        initData();

        //获取用户当前位置
        startLocate();
    }
    @Subscribe
    public void refreshFactifityType(RefreshTypeEvent event) {
        facilityType = event.getType();
        if(event.getType()==1 || event.getType()== 2){
            ll_company.setVisibility(View.GONE);
            ll_mange_state.setVisibility(View.VISIBLE);
//            ll_problem.setVisibility(View.VISIBLE);
//            ll_glzt_containt.setVisibility(View.VISIBLE);
        }else{
            take_photo_pfk = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_pfkou);
            ll_company.setVisibility(View.GONE);
            ll_mange_state.setVisibility(View.GONE);
            ll_problem.setVisibility(View.GONE);
            ll_glzt_containt.setVisibility(View.GONE);
        }



    }
    private void initData() {
        double x = getIntent().getDoubleExtra("x", 0);
        double y = getIntent().getDoubleExtra("y", 0);
        uploadFacility.setX(x);
        uploadFacility.setY(y);
        uploadFacility.setAddr(detailAddress.getDetailAddress());
        uploadFacility.setRoad(detailAddress.getStreet());
        uploadFacility.setIsBinding(0);

        modifiedFacility.setX(x);
        modifiedFacility.setY(y);
        modifiedFacility.setAddr(detailAddress.getDetailAddress());
        modifiedFacility.setRoad(detailAddress.getStreet());
    }

    private void initUser() {
        User user = new LoginService(this, AMDatabase.getInstance()).getUser();
        String userName = user.getUserName();
        String userId = user.getId();
        long currentTimeMillis = System.currentTimeMillis();

        modifiedFacility.setMarkTime(currentTimeMillis);
        uploadFacility.setMarkPerson(userName);
        uploadFacility.setMarkPersonId(userId);
        modifiedFacility.setMarkPerson(userName);
        modifiedFacility.setMarkPersonId(userId);
        tableitem_current_user.setText(userName);
    }


    private void initView() {
        mAreasAdapter  = new AreasAdapter(this);
        gv_area.setAdapter(mAreasAdapter);
        ll_mange_state = findViewById(R.id.ll_mange_state);
        ll_company = findViewById(R.id.ll_company);
        ll_glzt_containt = findViewById(R.id.ll_glzt_containt);
        ll_problem = findViewById(R.id.ll_problem);
        take_photo_item = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_item);
        take_photo_item.setTitle("添加照片（方位照等）");
        take_photo_item.setPhotoExampleEnable(true);
//        take_photo_item.setRequired(true);
        ((TextView) findViewById(R.id.tv_title)).setText("数据新增");
        btn_upload_event_journal = (Button)findViewById(R.id.btn_upload_event_journal);
        RxView.clicks(btn_upload_event_journal)
                .throttleFirst(2, TimeUnit.SECONDS)   //2秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UploadedFacility nwUploadedFacility = getUploadFacility();
                        if(nwUploadedFacility != null) {
                            saveUploadFacility(nwUploadedFacility);
                        }
                    }
                });


        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        ll_container = (LinearLayout) findViewById(R.id.ll_container);


        /**
         * 当前时间
         */
        tableitem_current_time = (TextItemTableItem) findViewById(R.id.tableitem_current_time);
        tableitem_current_time.setVisibility(View.GONE);
        uploadFacility.setIsBinding(0);

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


        cbAreaGroup = (CheckBox) findViewById(R.id.cb_area);
        cbwaterGroup = (CheckBox) findViewById(R.id.cb_water_group);
        cbProtectionGroup = (CheckBox) findViewById(R.id.cb_protection);
        cbAreaGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    company = cbInnerCity.getText().toString();
                    cbwaterGroup.setChecked(false);
                    cbProtectionGroup.setChecked(false);
                    groups.setVisibility(View.VISIBLE);
                    gv_area.setVisibility(View.VISIBLE);
                    ArrayList<Area>arrayList = new ArrayList<>();
                    Area area = null;

                    for(int i=0 ; i<lead_yAxle.length;i++){
                        area = new Area();
                        area.setCode("dd");
                        area.setName(lead_yAxle[i]);
                        arrayList.add(area);
                    }
                    mAreasAdapter.notifyDatasetChanged(arrayList);

                }else{
                    groups.setVisibility(View.GONE);
                }
            }
        });

        cbwaterGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    company = cbwaterGroup.getText().toString();
                    cbAreaGroup.setChecked(false);
                    cbProtectionGroup.setChecked(false);
                    groups.setVisibility(View.GONE);
                }
            }
        });


        cbProtectionGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    company = cbProtectionGroup.getText().toString();
                    cbwaterGroup.setChecked(false);
                    cbAreaGroup.setChecked(false);
                    groups.setVisibility(View.GONE);
                }
            }
        });


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
//                    spinnerOthers.setVisibility(View.VISIBLE);
                    spinnerOthers.setEditable(true);
                    cbInnerCity.setChecked(false);
                    cbDontKnowWhere.setChecked(false);
                    if(mManStatusIsSpinner){
                        spinnerOthers.setVisibility(View.VISIBLE);
                    } else {
                        etOthers.setVisibility(View.VISIBLE);
                    }
                } else {
                    etOthers.setEnabled(false);
//                    spinnerOthers.setVisibility(View.INVISIBLE);
                    spinnerOthers.setEditable(false);
                    etOthers.setVisibility(View.GONE);
                    spinnerOthers.setVisibility(View.GONE);
                }
            }
        });

        /**
         * 属性容器
         */
        ll_attributelist_container = (LinearLayout) findViewById(R.id.ll_attributelist_container);
        double mx = getIntent().getDoubleExtra("x", 0);
        double my = getIntent().getDoubleExtra("y", 0);
        Point point = new Point(mx,my);
        facilityAttributeView = new FacilityAttributeView(UploadNewFacilityActivity.this,point);
        ll_attributelist_container.removeAllViews();
        ll_attributelist_container.setVisibility(View.VISIBLE);
        facilityAttributeView.addTo(ll_attributelist_container);

        /**
         * 填表人
         */
        tableitem_current_user = (TextItemTableItem) findViewById(R.id.tableitem_current_user);
        tableitem_current_user.setReadOnly();
        /**
         * 描述
         */
        textitem_description = (TextFieldTableItem) findViewById(R.id.textitem_description);

        /**
         * 部件位置
         */
        double x = getIntent().getDoubleExtra("x", 0);
        double y = getIntent().getDoubleExtra("y", 0);
        UploadedFacility temp = new UploadedFacility();
        if (detailAddress != null) {
            temp.setAddr(detailAddress.getDetailAddress());
            temp.setRoad(detailAddress.getStreet());
        }
        temp.setX(x);
        temp.setY(y);
        LinearLayout selectLocationContainer = (LinearLayout) findViewById(R.id.rl_select_location_container);
        mComponentAddressErrorView = new FacilityAddressErrorView(UploadNewFacilityActivity.this,
                temp);
        selectLocationContainer.removeAllViews();
        mComponentAddressErrorView.addTo(selectLocationContainer);


        /**
         * 设施问题
         */
        ll_problems_container = (FlexboxLayout) findViewById(R.id.ll_problems_container);
        facilityProblemView = new FacilityProblemView(this, ll_problems_container);
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
        //相片选取
        take_photo_well = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_well);
        /**
         * 提交
         */
        btn_upload_journal = findViewById(R.id.btn_upload_journal);
        RxView.clicks(btn_upload_journal)
                .throttleFirst(2, TimeUnit.SECONDS)   //2秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();
                        int mincount = 3;
                        if(facilityAttributeView.getFacilityAttributeModel()!=null
                                && "排水口".equals(facilityAttributeView.getFacilityAttributeModel().getLayerName())){
                            mincount = 4;
                        }
//                        if (ListUtil.isEmpty(selectedPhotos) || selectedPhotos.size() < mincount) {
//                            showToast("请按“拍照须知”要求至少提供"+mincount+"张照片！");
//                            return;
//                        }
                        boolean ifCompleteAttributeSuccess = true;
                        if (facilityAttributeView.getFacilityAttributeModel() != null
                                && facilityAttributeView.getFacilityAttributeModel().getLayerName() != null) {
                            FacilityAttributeModel facilityAttributeModel = facilityAttributeView.getFacilityAttributeModel();
                            if (!facilityAttributeModel.isIfAllowUpload()) {
                                ToastUtil.iconShortToast(getApplicationContext(), R.mipmap.ic_alert_yellow,
                                        facilityAttributeModel.getNotAllowUploadReason());
                                ifCompleteAttributeSuccess = false;
                            }
                            uploadFacility.setLayerName(facilityAttributeModel.getLayerName());
                            modifiedFacility.setLayerName(facilityAttributeModel.getLayerName());
                            uploadFacility.setComponentType(facilityAttributeModel.getLayerName());
                            uploadFacility.setAttrOne(facilityAttributeModel.getAttrOne());
                            uploadFacility.setAttrTwo(facilityAttributeModel.getAttrTwo());
                            uploadFacility.setAttrThree(facilityAttributeModel.getAttrThree());
                            uploadFacility.setAttrFour(facilityAttributeModel.getAttrFour());
                            uploadFacility.setGpbh(facilityType == 1? facilityAttributeModel.getAttrFive():facilityAttributeModel.getGpbh());
                            uploadFacility.setAttrFive(facilityAttributeModel.getAttrFive());
                            uploadFacility.setAttrSix(facilityAttributeModel.getAttrSix());
                            uploadFacility.setAttrSeven(facilityAttributeModel.getAttrSeven());
                            uploadFacility.setMpBeen(facilityAttributeModel.getMpBeen());
                            uploadFacility.setRiverx(facilityAttributeModel.getX());
                            uploadFacility.setRivery(facilityAttributeModel.getY());
                            uploadFacility.setSfCzwscl(facilityAttributeModel.getAttrsfCzwscl());
                            uploadFacility.setSfpsdyhxn(facilityAttributeModel.getSfpsdyhxn());
                            uploadFacility.setSfgjjd(facilityAttributeModel.getAttrSfgjjd());
                            uploadFacility.setGjjdBh(facilityAttributeModel.getAttrJDBH());
                            uploadFacility.setGjjdZrr(facilityAttributeModel.getAttrJDZRR());
                            uploadFacility.setGjjdLxdh(facilityAttributeModel.getAttrLXDH());
                            uploadFacility.setYjbh(facilityAttributeModel.getAttrYJBH());
                        }

                        /**
                         * 如果此时完善属性不成功，不继续向下执行
                         */
                        if (!ifCompleteAttributeSuccess) {
                            return;
                        }

                        if (TextUtils.isEmpty(uploadFacility.getComponentType())) {
                            showToast("请先选择设施类型");
                            return;
                        }

                        /**
                         * 填充地址
                         */
                        boolean success = completeAddress();
                        if (!success) {
                            return;
                        }

                        /**
                         * 如果管理状态选了其他，那么一定要填
                         */
//                        if (cbOthers.isChecked() && !mManStatusIsSpinner && TextUtils.isEmpty(etOthers.getText().toString())) {
//                            showToast("管理状态不可以为空");
//                            return;
//                        }


                        if (progressDialog == null) {
                            progressDialog = new ProgressDialog(UploadNewFacilityActivity.this);
                            progressDialog.setMessage("正在提交，请等待");
                            progressDialog.setCancelable(false);
                        }

                        if (!progressDialog.isShowing()) {
                            progressDialog.show();
                        }

                        uploadFacility.setPhotos(selectedPhotos);
                        uploadFacility.setThumbnailPhotos(take_photo_item.getThumbnailPhotos());
                        uploadFacility.setDescription(textitem_description.getText());

                        if(facilityType!=2 && !TextUtils.isEmpty(uploadFacility.getGpbh()) && !"无".equals(uploadFacility.getGpbh())){
                            take_photo_pfk = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_pfkou);
                            take_photo_well = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_well);
                            List<Photo> selectedWellPhotos = (facilityType==1 ? take_photo_well:take_photo_pfk).getSelectedPhotos();
                            uploadFacility.setWellPhotos(selectedWellPhotos);
                            uploadFacility.setWellThumbnailPhotos((facilityType==1 ? take_photo_well:take_photo_pfk).getThumbnailPhotos());
                        }

                        /**
                         * 管理状态
                         */
                        if (cbDontKnowWhere.isChecked() || cbInnerCity.isChecked()) {
                            uploadFacility.setCityVillage(cityVillage);
                        } else if (cbOthers.isChecked()) {
                            if(mManStatusIsSpinner){
                                uploadFacility.setCityVillage(spinnerOthers.getText());
                            } else {
                                uploadFacility.setCityVillage(etOthers.getText().toString());
                            }
                        } else {
                            uploadFacility.setCityVillage(null);
                        }

//                        //设施问题
//                        List<String> selectedLargeAndSmallItemList = facilityProblemView.getSelectedParentAndChildICodeList();
//                        //父类编码
//                        if (!TextUtils.isEmpty(selectedLargeAndSmallItemList.get(0))) {
//                            uploadFacility.setpCode(selectedLargeAndSmallItemList.get(0));
//                        }
//
//                        //子类编码
//                        if (!TextUtils.isEmpty(selectedLargeAndSmallItemList.get(1))) {
//                            uploadFacility.setChildCode(selectedLargeAndSmallItemList.get(1));
//                        }

                        //20秒提示一次
                        countDownTimer = new CountDownTimer(UploadFacilityService.TIMEOUT * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                long time = millisUntilFinished / 1000;
                                if (progressDialog != null) {
                                    progressDialog.setMessage("正在提交，请等待。   " + time + "s");
                                }
                                if (time % 20 == 0) {
                                    ToastUtil.longToast(UploadNewFacilityActivity.this, "网络忙，请稍等");
                                }
                            }

                            @Override
                            public void onFinish() {

                            }
                        };

                        if (countDownTimer != null) {
                            countDownTimer.start();
                        }

                        UploadFacilityService identificationService = new UploadFacilityService(getApplicationContext());
                        identificationService.upload(uploadFacility)
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
                                        showToast("提交失败");
                                        CrashReport.postCatchedException(new Exception("新增上报失败，上报用户是：" +
                                                BaseInfoManager.getUserName(UploadNewFacilityActivity.this) + "原因是：" + e.getLocalizedMessage()));
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
                                        if (responseBody.getCode() == 200) {
//                                            showToast("提交成功");
                                            EventBus.getDefault().post(new RefreshMyUploadList());
                                            modifiedFacility.setObjectId(responseBody.getObjectid());
                                            uploadJournal(modifiedFacility);
                                        } else {
                                            showToast("保存失败，原因是：" + responseBody.getMessage());
                                            CrashReport.postCatchedException(new Exception("新增上报失败，上报用户是：" +
                                                    BaseInfoManager.getUserName(UploadNewFacilityActivity.this) + "原因是：" + responseBody.getMessage()));
                                        }
                                    }
                                });
                    }
                });
    }

    private UploadedFacility getUploadFacility() {
        List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();
        boolean ifCompleteAttributeSuccess = true;
        if (facilityAttributeView.getFacilityAttributeModel() != null
                && facilityAttributeView.getFacilityAttributeModel().getLayerName() != null) {
            FacilityAttributeModel facilityAttributeModel = facilityAttributeView.getFacilityAttributeModel();
//            if (!facilityAttributeModel.isIfAllowUpload()) {
//                ToastUtil.iconShortToast(getApplicationContext(), R.mipmap.ic_alert_yellow,
//                        facilityAttributeModel.getNotAllowUploadReason());
//                ifCompleteAttributeSuccess = false;
//            }
            uploadFacility.setLayerName(facilityAttributeModel.getLayerName());
            modifiedFacility.setLayerName(facilityAttributeModel.getLayerName());
            uploadFacility.setComponentType(facilityAttributeModel.getLayerName());
            uploadFacility.setAttrOne(facilityAttributeModel.getAttrOne());
            uploadFacility.setAttrTwo(facilityAttributeModel.getAttrTwo());
            uploadFacility.setAttrThree(facilityAttributeModel.getAttrThree());
            uploadFacility.setAttrFour(facilityAttributeModel.getAttrFour());
            uploadFacility.setAttrFour(facilityAttributeModel.getGpbh());
            uploadFacility.setAttrFive(facilityAttributeModel.getAttrFive());
            uploadFacility.setAttrSix(facilityAttributeModel.getAttrSix());
            uploadFacility.setAttrSeven(facilityAttributeModel.getAttrSeven());
            uploadFacility.setSfpsdyhxn(facilityAttributeModel.getSfpsdyhxn());
            uploadFacility.setRiverx(facilityAttributeModel.getX());
            uploadFacility.setRivery(facilityAttributeModel.getY());
            uploadFacility.setMpBeen(facilityAttributeModel.getMpBeen());
        }

        /**
         * 如果此时完善属性不成功，不继续向下执行
         */
//        if (!ifCompleteAttributeSuccess) {
//            return null;
//        }

        if (TextUtils.isEmpty(uploadFacility.getComponentType())) {
            showToast("请先选择设施类型");
            return null;
        }

        /**
         * 填充地址
         */
        boolean success = completeAddress();
        if (!success) {
            return null;
        }

        /**
         * 如果管理状态选了其他，那么一定要填
         */
//        if (cbOthers.isChecked() && !mManStatusIsSpinner && TextUtils.isEmpty(etOthers.getText().toString())) {
//            showToast("管理状态不可以为空");
//            return null;
//        }


        if (progressDialog == null) {
            progressDialog = new ProgressDialog(UploadNewFacilityActivity.this);
            progressDialog.setMessage("正在提交，请等待");
            progressDialog.setCancelable(false);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

        uploadFacility.setPhotos(selectedPhotos);
        uploadFacility.setThumbnailPhotos(take_photo_item.getThumbnailPhotos());
        uploadFacility.setDescription(textitem_description.getText());



        /**
         * 管理状态
         */
        if (cbDontKnowWhere.isChecked() || cbInnerCity.isChecked()) {
            uploadFacility.setCityVillage(cityVillage);
        } else if (cbOthers.isChecked()) {
            if(mManStatusIsSpinner){
                uploadFacility.setCityVillage(spinnerOthers.getText());
            } else {
                uploadFacility.setCityVillage(etOthers.getText().toString());
            }
        } else {
            uploadFacility.setCityVillage(null);
        }

        //设施问题
        List<String> selectedLargeAndSmallItemList = facilityProblemView.getSelectedParentAndChildICodeList();
        //父类编码
        if (!TextUtils.isEmpty(selectedLargeAndSmallItemList.get(0))) {
            uploadFacility.setpCode(selectedLargeAndSmallItemList.get(0));
        }

        //子类编码
        if (!TextUtils.isEmpty(selectedLargeAndSmallItemList.get(1))) {
            uploadFacility.setChildCode(selectedLargeAndSmallItemList.get(1));
        }
        uploadFacility.setTime(System.currentTimeMillis());
        return uploadFacility;
    }


    private boolean completeAddress() {
        if (mComponentAddressErrorView != null) {
            if (!mComponentAddressErrorView.ifAllowUpload()) {
                ToastUtil.iconShortToast(this, R.mipmap.ic_alert_yellow, mComponentAddressErrorView.getNotAllowUploadReason());
                return false;
            }
            FacilityAddressErrorModel facilityAddressErrorModel = mComponentAddressErrorView.getFacilityAddressErrorModel();
            uploadFacility.setAddr(facilityAddressErrorModel.getCorrectAddress());
            uploadFacility.setX(facilityAddressErrorModel.getCorrectX());
            uploadFacility.setY(facilityAddressErrorModel.getCorrectY());
            uploadFacility.setRoad(facilityAddressErrorModel.getRoad());

            modifiedFacility.setAddr(facilityAddressErrorModel.getCorrectAddress());
            modifiedFacility.setX(facilityAddressErrorModel.getCorrectX());
            modifiedFacility.setY(facilityAddressErrorModel.getCorrectY());
            modifiedFacility.setRoad(facilityAddressErrorModel.getRoad());
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (take_photo_item != null) {
            take_photo_item.onActivityResult(requestCode, resultCode, data);
        }

        if(findViewById(R.id.take_photo_well)!=null){
            take_photo_well = (MultiTakePhotoTableItem)findViewById(R.id.take_photo_well);
            take_photo_well.onActivityResult(requestCode, resultCode, data);
        }

        if(findViewById(R.id.take_photo_pfkou)!=null){
            take_photo_pfk = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_pfkou);
            take_photo_pfk.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                        uploadFacility.setUserLocationX(lastLocation.getLocation().getLongitude());
                        uploadFacility.setUserLocationY(lastLocation.getLocation().getLatitude());
                        modifiedFacility.setUserX(lastLocation.getLocation().getLongitude());
                        modifiedFacility.setUserY(lastLocation.getLocation().getLatitude());
                    }
                    if (lastLocation.getDetailAddress() != null) {
                        uploadFacility.setUserAddr(lastLocation.getDetailAddress().getDetailAddress());
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

    @Override
    public void onBackPressed() {
        showAlertDialog();
        //super.onBackPressed();
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
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        if (!LocationUtil.ifUnRegister()) {
            LocationUtil.unregister(UploadNewFacilityActivity.this);
        }

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (baiduLocationManager != null) {
            baiduLocationManager.stopLocate();
        }

        if (facilityProblemView != null) {
            facilityProblemView = null;
        }

        if (facilityAttributeView != null) {
            facilityAttributeView.destroy();
            facilityAttributeView = null;
        }
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
     *
     * @param sendFacilityOwnerShipUnit
     */
    @Subscribe
    public void onReceivedFacilityOwnerShipUnit(SendFacilityOwnerShipUnit sendFacilityOwnerShipUnit) {
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
                    //默认选中第一个
                    spinnerOthers.selectItem(0);
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
                if (cbOthers.isChecked()) {
                    spinnerOthers.setVisibility(View.GONE);
                    etOthers.setVisibility(View.VISIBLE);
                }
//                etOthers.setText("");
            }
        }
    }

    /**
     * 保存本地数据
     */
    private void saveUploadFacility(UploadedFacility nwUploadedFacility) {
        //保存数据上报
        AMDatabase.getInstance().save(nwUploadedFacility);
        //原图
        List<Photo> photoList = nwUploadedFacility.getPhotos();
        //保存上报图片
        for (Photo photo : photoList) {
            photo.setProblem_id(PhotoUploadType.UPLOAD_FOR_FACILITY + nwUploadedFacility.getDbid() + "");
            photo.setPhotoName(PhotoUploadType.UPLOAD_FOR_FACILITY + photo.getPhotoName());
            photo.setType(1);//原图
            AMDatabase.getInstance().save(photo);
        }
        //缩略图
        List<Photo> thumphotoList = nwUploadedFacility.getThumbnailPhotos();
        //保存上报图片
        for (Photo photo : thumphotoList) {
            photo.setProblem_id(PhotoUploadType.UPLOAD_FOR_FACILITY + nwUploadedFacility.getDbid() + "");
            photo.setPhotoName(PhotoUploadType.UPLOAD_FOR_FACILITY + photo.getPhotoName());
            photo.setType(2);//缩略图
            AMDatabase.getInstance().save(photo);
        }
        List<DoorNOBean> mpBeen = nwUploadedFacility.getMpBeen();
        if("接驳井".equals(nwUploadedFacility.getAttrOne()) && !ListUtil.isEmpty(mpBeen)){
            for (DoorNOBean doorNOBean : mpBeen) {
                doorNOBean.setMph_id(PhotoUploadType.MPH_FOR_FACILITY + nwUploadedFacility.getDbid() + "");
                AMDatabase.getInstance().save(doorNOBean);
            }
        }
        ToastUtil.shortToast(this.getApplicationContext(), "保存成功");
        finish();
    }

    /**
     * 提交巡检日志
     * @param modifiedFacility
     */
    private void uploadJournal(ModifiedFacility modifiedFacility) {
        //数据纠错
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
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        CrashReport.postCatchedException(new Exception("巡检上报失败，上报用户是：" +
                                BaseInfoManager.getUserName(UploadNewFacilityActivity.this) + "原因是：" + e.getLocalizedMessage()));
                        ToastUtil.shortToast(UploadNewFacilityActivity.this,"巡检提交失败");
                        //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "提交失败");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (responseBody.getCode() == 200) {
                            ToastUtil.shortToast(UploadNewFacilityActivity.this,"提交成功");
                        } else {
                            CrashReport.postCatchedException(new Exception("核准上报失败，上报用户是：" +
                                    BaseInfoManager.getUserName(UploadNewFacilityActivity.this) + "原因是：" + responseBody.getMessage()));
                            ToastUtil.shortToast(UploadNewFacilityActivity.this,"巡检日志保存失败，原因是：" + responseBody.getMessage());
                            //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "保存失败，原因是：" + responseBody.getMessage());
                        }
                    }
                });
    }

}
