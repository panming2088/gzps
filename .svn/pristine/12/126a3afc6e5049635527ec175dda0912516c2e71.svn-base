package com.augurit.agmobile.gzps.uploadfacility.view.reeditfacility;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.model.DoorNOBean;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.publicaffair.service.FacilityAffairService;
import com.augurit.agmobile.gzps.uploadevent.util.PhotoUploadType;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityAddressErrorModel;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityAttributeModel;
import com.augurit.agmobile.gzps.uploadfacility.model.OnMenPaiDeleteEvent;
import com.augurit.agmobile.gzps.uploadfacility.model.RefreshTypeEvent;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.view.SendFacilityOwnerShipUnit;
import com.augurit.agmobile.gzps.uploadfacility.view.UploadFacilitySuccessEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.FacilityAddressErrorView;
import com.augurit.agmobile.gzps.uploadfacility.view.facilityprobrem.FacilityProblemView;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.FacilityAttributeView;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.RefreshMyUploadList;
import com.augurit.agmobile.patrolcore.common.model.LocationInfo;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.locate.BaiduLocationManager;
import com.augurit.am.cmpt.loc.util.LocationUtil;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.spinner.AMSpinner;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.google.android.flexbox.FlexboxLayout;
import com.jakewharton.rxbinding.view.RxView;

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
 * 再次编辑新增设施
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.upload.view
 * @createTime 创建时间 ：17/12/18
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/18
 * @modifyMemo 修改备注：
 */

public class ReEditUploadFacilityActivity extends BaseActivity {
    //1为窨井 ，2为雨水井，3为排放口
    private int facilityType = 1;
    private MultiTakePhotoTableItem take_photo_item;
    private View btn_upload_journal;
    //已挂牌编号控件
    private MultiTakePhotoTableItem take_photo_well;
    //排放口挂牌编号
    private MultiTakePhotoTableItem take_photo_pfk;
    private TextView tv_select_or_check_location;
    private TextItemTableItem tableitem_current_time;
    private TextItemTableItem tableitem_current_user;
    private TextFieldTableItem textitem_description;
    private LinearLayout ll_attributelist_container;

    private LinearLayout ll_container;
    private FacilityAttributeView facilityAttributeView;


    private UploadedFacility uploadedFacility;
    private BaiduLocationManager baiduLocationManager;
    private ProgressDialog progressDialog;
    private CountDownTimer countDownTimer; //用于上报时超过一定时间提示一次
    private FlexboxLayout ll_problems_container;
    private FacilityProblemView facilityProblemView;
    private FacilityAddressErrorView mComponentAddressErrorView;

    private CheckBox cbInnerCity;
    private CheckBox cbDontKnowWhere;

    private String cityVillage;
    private CheckBox cbOthers;
    private EditText etOthers;
    private AMSpinner spinnerOthers;
    private boolean mManStatusIsSpinner = false;   //当前管理状态中的其他是Spinner还是EditText
    private String mLayerName;
    private LinearLayout ll_glzt_containt;
    private LinearLayout ll_problem;
    private boolean isDraft = false;
    private View btn_upload_event_journal;
    private boolean isLoadImage = false;
    private List<Long> mMphDels = new ArrayList<>();
    private List<DoorNOBean> mDoorNOBeans;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reedit_upload_new_facility);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        /**
         * 在Activity跳转之前已经确保detailAddress和location不为空，所以下面的代码省去了这两个变量的非空判断
         */
        uploadedFacility = getIntent().getParcelableExtra("data");
        if (uploadedFacility != null) {
            mDoorNOBeans = uploadedFacility.getMpBeen();
        }
        isDraft = getIntent().getBooleanExtra("isDraft", false);
        isLoadImage = getIntent().getBooleanExtra("isLoad", false);
        if (uploadedFacility != null) {
            mLayerName = uploadedFacility.getLayerName();
            //相片选取
            if (mLayerName.equals("窨井")) {
                facilityType = 1;
            } else if (mLayerName.equals("雨水口")) {
                facilityType = 2;
            } else {
                facilityType = 3;
            }
        }

        initView();
        if (isLoadImage) {
            loadImage();
        }
        //获取用户当前位置
        startLocate();
    }

    private void loadImage() {
        final FacilityAffairService identificationService = new FacilityAffairService(this);
        identificationService.getUploadDetail(uploadedFacility.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadedFacility>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UploadedFacility uploadedFacility1) {
                        if (uploadedFacility1 != null) {
                            uploadedFacility.setMpBeen(uploadedFacility1.getMpBeen());
                            mDoorNOBeans = uploadedFacility1.getMpBeen();
                            take_photo_item.setSelectedPhotos(uploadedFacility1.getPhotos());
                            take_photo_item.setSelThumbPhotos(uploadedFacility1.getThumbnailPhotos());
//                            if(facilityType!=2){
//                                (facilityType==1 ? take_photo_well:take_photo_pfk).setSelectedPhotos(uploadedFacility1.getWellPhotos());
//                                (facilityType==1 ? take_photo_well:take_photo_pfk).setSelThumbPhotos(uploadedFacility1.getWellThumbnailPhotos());
//                            }

                            ll_attributelist_container = (LinearLayout) findViewById(R.id.ll_attributelist_container);
                            facilityAttributeView = new FacilityAttributeView(ReEditUploadFacilityActivity.this, uploadedFacility1, isDraft);
                            ll_attributelist_container.removeAllViews();
                            ll_attributelist_container.setVisibility(View.VISIBLE);
                            facilityAttributeView.addTo(ll_attributelist_container);

                            take_photo_well = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_well);
                            take_photo_pfk = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_pfkou);
                            if (uploadedFacility != null && mLayerName.equals("窨井")) {
                                take_photo_well.setSelectedPhotos(uploadedFacility.getWellPhotos());
                                take_photo_well.setSelThumbPhotos(uploadedFacility.getWellThumbnailPhotos());
                            } else if (uploadedFacility != null && facilityType == 3) {
                                take_photo_pfk.setSelectedPhotos(uploadedFacility.getWellPhotos());
                                take_photo_pfk.setSelThumbPhotos(uploadedFacility.getWellThumbnailPhotos());
                            }
                        }
                    }
                }) ;
    }


    private void initView() {

        take_photo_item = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_item);
        take_photo_item.setTitle("添加照片（方位照等）");
        ll_glzt_containt = (LinearLayout) findViewById(R.id.ll_glzt_containt);
        ll_problem = (LinearLayout) findViewById(R.id.ll_problem);

        /**
         * 设施问题
         */
        ll_problems_container = (FlexboxLayout) findViewById(R.id.ll_problems_container);
        take_photo_item.setPhotoExampleEnable(true);
//        take_photo_item.setRequired(true);
        if (uploadedFacility != null) {
            take_photo_item.setSelectedPhotos(uploadedFacility.getPhotos());
            take_photo_item.setSelThumbPhotos(uploadedFacility.getThumbnailPhotos());
        }

        if (isDraft) {
            if (uploadedFacility != null) {
                uploadedFacility.setId(null);
            }
            ((TextView) findViewById(R.id.tv_title)).setText("数据上报");
        } else {
            ((TextView) findViewById(R.id.tv_title)).setText("再次编辑");
        }

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 首先要判断是否进行过编辑，如果没有动过，那么不需要询问，直接退出
                showAlertDialog();
            }
        });

        ll_container = (LinearLayout) findViewById(R.id.ll_container);


        /**
         * 当前时间
         */
        tableitem_current_time = (TextItemTableItem) findViewById(R.id.tableitem_current_time);
        tableitem_current_time.setVisibility(View.GONE);

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

//        if (uploadedFacility != null && cbInnerCity.getText().toString().equals(uploadedFacility.getCityVillage())) {
//            cbInnerCity.setChecked(true);
//        } else if (uploadedFacility != null && cbDontKnowWhere.getText().toString().equals(uploadedFacility.getCityVillage())) {
//            cbDontKnowWhere.setChecked(true);
//        } else if (uploadedFacility != null && !TextUtils.isEmpty(uploadedFacility.getCityVillage())) {
//            /**
//             * 如果既不是"城中村"/"三不管"，那么选其他
//             */
//            cbOthers.setChecked(true);
//            etOthers.setText(uploadedFacility.getCityVillage());
//        }

        /**
         * 属性容器
         */
        if (!isLoadImage) {
            ll_attributelist_container = (LinearLayout) findViewById(R.id.ll_attributelist_container);
            facilityAttributeView = new FacilityAttributeView(ReEditUploadFacilityActivity.this, uploadedFacility, isDraft);
            ll_attributelist_container.removeAllViews();
            ll_attributelist_container.setVisibility(View.VISIBLE);
            facilityAttributeView.addTo(ll_attributelist_container);
            take_photo_well = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_well);
            take_photo_pfk = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_pfkou);
            if (uploadedFacility != null && mLayerName.equals("窨井")) {
                take_photo_well.setSelectedPhotos(uploadedFacility.getWellPhotos());
                take_photo_well.setSelThumbPhotos(uploadedFacility.getWellThumbnailPhotos());
            } else if (uploadedFacility != null && facilityType == 3) {
                take_photo_pfk.setSelectedPhotos(uploadedFacility.getWellPhotos());
                take_photo_pfk.setSelThumbPhotos(uploadedFacility.getWellThumbnailPhotos());
            }

        }

        /**
         * 填表人
         */
        tableitem_current_user = (TextItemTableItem) findViewById(R.id.tableitem_current_user);
        LoginService loginService = new LoginService(this, AMDatabase.getInstance());
        tableitem_current_user.setText(loginService.getUser().getUserName());
        tableitem_current_user.setReadOnly();
        uploadedFacility.setMarkPersonId(loginService.getUser().getId());
        uploadedFacility.setMarkPerson(loginService.getUser().getUserName());

        /**
         * 描述
         */
        textitem_description = (TextFieldTableItem) findViewById(R.id.textitem_description);
        if (uploadedFacility != null) {
            textitem_description.setText(uploadedFacility.getDescription());
        }

        /**
         * 部件位置
         */
        LinearLayout selectLocationContainer = (LinearLayout) findViewById(R.id.rl_select_location_container);
        mComponentAddressErrorView = new FacilityAddressErrorView(ReEditUploadFacilityActivity.this,
                uploadedFacility);
        selectLocationContainer.removeAllViews();
        mComponentAddressErrorView.addTo(selectLocationContainer);

        if (uploadedFacility != null) {
            facilityProblemView = new FacilityProblemView(this, ll_problems_container, uploadedFacility.getpCode(), uploadedFacility.getChildCode());
        } else {
            facilityProblemView = new FacilityProblemView(this, ll_problems_container);
        }

        if ("排水口".equals(mLayerName)) {
            ll_problem.setVisibility(View.GONE);
            ll_glzt_containt.setVisibility(View.GONE);
        } else {
//            ll_problem.setVisibility(View.VISIBLE);
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
        //相片选取
        if (mLayerName.equals("窨井")) {
            facilityType = 1;
        } else if (mLayerName.equals("雨水口")) {
            facilityType = 2;
        } else {
            facilityType = 3;
        }

//        if(!isLoadImage){
//            take_photo_well = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_well);
//            take_photo_pfk = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_pfkou);
//            if (uploadedFacility != null && mLayerName.equals("窨井")) {
//                take_photo_well.setSelectedPhotos(uploadedFacility.getWellPhotos());
//                take_photo_well.setSelThumbPhotos(uploadedFacility.getWellThumbnailPhotos());
//            }else if(uploadedFacility != null && facilityType == 3){
//                take_photo_pfk.setSelectedPhotos(uploadedFacility.getWellPhotos());
//                take_photo_pfk.setSelThumbPhotos(uploadedFacility.getWellThumbnailPhotos());
//            }
//        }

        /**
         * 提交
         */
        btn_upload_journal = findViewById(R.id.btn_upload_journal);
        btn_upload_event_journal = findViewById(R.id.btn_upload_event_journal);
        if (isDraft) {
            btn_upload_event_journal.setVisibility(View.VISIBLE);
        } else {
            btn_upload_event_journal.setVisibility(View.GONE);
        }
        RxView.clicks(btn_upload_journal)
                .throttleFirst(2, TimeUnit.SECONDS)   //2秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        int mincount = 3;
                        List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();
                        List<Photo> selectedPhotosWell = null;
                        if (facilityType != 2 && (take_photo_well != null || take_photo_pfk != null)) {
                            selectedPhotosWell = (facilityType == 1 ? take_photo_well : take_photo_pfk).getSelectedPhotos();
                        }
                        if (facilityAttributeView != null
                                && facilityAttributeView.getFacilityAttributeModel() != null
                                && "排水口".equals(facilityAttributeView.getFacilityAttributeModel().getLayerName())) {
                            mincount = 4;
                        }
//                        if (ListUtil.isEmpty(selectedPhotos) || selectedPhotos.size() < mincount) {
//                            ToastUtil.iconLongToast(getApplicationContext(), R.mipmap.ic_alert_yellow, "请按“拍照须知”要求至少提供"+mincount+"张照片！");
//                            return;
//                        }

                        boolean ifCompleteAttributeSuccess = true;
                        List<DoorNOBean> mDoorNOBean = new ArrayList<>();
                        if (facilityAttributeView.getFacilityAttributeModel() != null
                                && facilityAttributeView.getFacilityAttributeModel().getLayerName() != null) {
                            FacilityAttributeModel facilityAttributeModel = facilityAttributeView.getFacilityAttributeModel();
                            if (!facilityAttributeModel.isIfAllowUpload()) {
                                ToastUtil.iconShortToast(getApplicationContext(), R.mipmap.ic_alert_yellow,
                                        facilityAttributeModel.getNotAllowUploadReason());
                                ifCompleteAttributeSuccess = false;
                            }
                            uploadedFacility.setLayerName(facilityAttributeModel.getLayerName());
                            uploadedFacility.setComponentType(facilityAttributeModel.getLayerName());
                            uploadedFacility.setAttrOne(facilityAttributeModel.getAttrOne());
                            uploadedFacility.setAttrTwo(facilityAttributeModel.getAttrTwo());
                            uploadedFacility.setAttrThree(facilityAttributeModel.getAttrThree());
                            uploadedFacility.setAttrFour(facilityAttributeModel.getAttrFour());
                            uploadedFacility.setSfCzwscl(facilityAttributeModel.getAttrsfCzwscl());
                            uploadedFacility.setSfpsdyhxn(facilityAttributeModel.getSfpsdyhxn());
                            uploadedFacility.setGpbh(facilityType == 1 ? facilityAttributeModel.getAttrFive() : facilityAttributeModel.getGpbh());
                            uploadedFacility.setAttrFive(facilityAttributeModel.getAttrFive());
                            uploadedFacility.setAttrSix(facilityAttributeModel.getAttrSix());
                            uploadedFacility.setAttrSeven(facilityAttributeModel.getAttrSeven());
                            uploadedFacility.setRiverx(facilityAttributeModel.getX());
                            uploadedFacility.setRivery(facilityAttributeModel.getY());
                            uploadedFacility.setSfgjjd(facilityAttributeModel.getAttrSfgjjd());
                            uploadedFacility.setGjjdBh(facilityAttributeModel.getAttrJDBH());
                            uploadedFacility.setGjjdZrr(facilityAttributeModel.getAttrJDZRR());
                            uploadedFacility.setGjjdLxdh(facilityAttributeModel.getAttrLXDH());
                            uploadedFacility.setYjbh(facilityAttributeModel.getAttrYJBH());
                            mDoorNOBean = facilityAttributeModel.getMpBeen();
                            if (!ListUtil.isEmpty(mDoorNOBean)) {
                                List<DoorNOBean> mDoorNOBeen = new ArrayList<>();
                                for (DoorNOBean doorNOBean : mDoorNOBean) {
                                    if (doorNOBean.getId() == null || doorNOBean.getId() == 0) {
                                        doorNOBean.setId(null);
                                        mDoorNOBeen.add(doorNOBean);
                                    }
                                }
                                uploadedFacility.setMpBeen(mDoorNOBeen);
                            } else {
                                uploadedFacility.setMpBeen(new ArrayList<DoorNOBean>());
                            }
                        }

                        /**
                         * 如果此时完善属性不成功，不继续向下执行
                         */
                        if (!ifCompleteAttributeSuccess) {
                            return;
                        }

                        if (TextUtils.isEmpty(uploadedFacility.getComponentType())) {
                            ToastUtil.shortToast(getApplicationContext(), "请先选择设施类型");
                            return;
                        }
                        if ("接驳井".equals(uploadedFacility.getAttrOne())) {
                            List<Long> mMphDeletes = getMphDels(mDoorNOBean);
                            uploadedFacility.setDeletempBeen(mMphDeletes);
                        } else {
                            uploadedFacility.setDeletempBeen(getAllMphIds());
                        }
                        boolean b = completeAddress();
                        if (!b) {
                            return;
                        }

                        /**
                         * 如果管理状态选了其他，那么一定要填
                         */
                        if (cbOthers.isChecked() && !mManStatusIsSpinner && TextUtils.isEmpty(etOthers.getText().toString())) {
                            ToastUtil.iconShortToast(getApplicationContext(), R.mipmap.ic_alert_yellow, "管理状态不可以为空");
                            return;
                        }

                        if (progressDialog == null) {
                            progressDialog = new ProgressDialog(ReEditUploadFacilityActivity.this);
                            progressDialog.setMessage("正在提交，请等待");
                            progressDialog.setCancelable(false);
                        }

                        progressDialog.show();
                        uploadedFacility.setMarkTime(System.currentTimeMillis());
                        uploadedFacility.setUpdateTime(System.currentTimeMillis());
                        uploadedFacility.setDescription(textitem_description.getText());

                        /**
                         * 管理状态
                         */
                        if (cbDontKnowWhere.isChecked() || cbInnerCity.isChecked()) {
                            uploadedFacility.setCityVillage(cityVillage);
                        } else if (cbOthers.isChecked()) {
                            if (mManStatusIsSpinner) {
                                uploadedFacility.setCityVillage(spinnerOthers.getText());
                            } else {
                                uploadedFacility.setCityVillage(etOthers.getText().toString());
                            }
                        } else {
                            uploadedFacility.setCityVillage("");
                        }

                        /**
                         * 只有新增的图片id是0，从服务端获取到的图片id都是不为0的
                         */
                        List<Photo> newAddedPhotos = new ArrayList<>();
                        List<Photo> newAddedPhotosWell = new ArrayList<>();
                        if (!ListUtil.isEmpty(selectedPhotos)) {
                            for (Photo photo : selectedPhotos) {
                                if (photo.getId() == 0) {
                                    newAddedPhotos.add(photo);
                                }
                            }
                            uploadedFacility.setPhotos(newAddedPhotos);
                        }

                        List<Photo> newAddedPhotosThumbnail = new ArrayList<>();
                        List<Photo> photosThumbnail = take_photo_item.getThumbnailPhotos();
                        if (!ListUtil.isEmpty(photosThumbnail)) {
                            for (Photo photo : photosThumbnail) {
                                if (photo.getId() == 0) {
                                    newAddedPhotosThumbnail.add(photo);
                                }
                            }
                            uploadedFacility.setThumbnailPhotos(newAddedPhotosThumbnail);
                        }


                        if (!ListUtil.isEmpty(selectedPhotosWell)) {
                            for (Photo photo : selectedPhotosWell) {
                                if (photo.getId() == 0) {
                                    newAddedPhotosWell.add(photo);
                                }
                            }
                            uploadedFacility.setWellPhotos(newAddedPhotosWell);
                        }
                        if (facilityType != 2 && !TextUtils.isEmpty(uploadedFacility.getGpbh())) {
//                            uploadedFacility.setThumbnailPhotos((facilityType==1 ? take_photo_well:take_photo_pfk).getThumbnailPhotos());
                            uploadedFacility.setWellThumbnailPhotos((facilityType == 1 ? take_photo_well : take_photo_pfk).getThumbnailPhotos());
                        }

                        List<Photo> deletedPhotos = take_photo_item.getDeletedPhotos();
                        List<Photo> deletedPhotosWell = null;
                        if (facilityType != 2 && (take_photo_well != null || take_photo_pfk != null)) {
                            deletedPhotosWell = (facilityType == 1 ? take_photo_well : take_photo_pfk).getDeletedPhotos();
                        }

                        List<String> deletedPhotoIds = new ArrayList<>();

                        if (!ListUtil.isEmpty(deletedPhotos)) {
                            for (Photo photo : deletedPhotos) {
                                if (photo.getId() != 0) {
                                    deletedPhotoIds.add(String.valueOf(photo.getId()));
                                }
                            }
                        }
                        if (!ListUtil.isEmpty(deletedPhotosWell)) {
                            for (Photo photo : deletedPhotosWell) {
                                if (photo.getId() != 0) {
                                    deletedPhotoIds.add(String.valueOf(photo.getId()));
                                }
                            }
                        }

                        if (TextUtils.isEmpty(uploadedFacility.getGpbh()) || "无".equals(uploadedFacility.getGpbh())) {
                            //以前有图片，现在没有图片，需要删掉
                            if (!ListUtil.isEmpty(selectedPhotosWell)) {
                                for (Photo photo : selectedPhotosWell) {
                                    if (photo.getId() != 0) {
                                        deletedPhotoIds.add(String.valueOf(photo.getId()));
                                    }
                                }
                            }
                            uploadedFacility.setWellPhotos(null);
                        }

                        uploadedFacility.setDeletedPhotoIds(deletedPhotoIds);

                        //设施问题
                        List<String> selectedLargeAndSmallItemList = facilityProblemView.getSelectedParentAndChildICodeList();
                        //父类编码
                        if (selectedLargeAndSmallItemList != null && !TextUtils.isEmpty(selectedLargeAndSmallItemList.get(0))) {
                            uploadedFacility.setpCode(selectedLargeAndSmallItemList.get(0));
                        } else {
                            uploadedFacility.setpCode("");
                        }

                        //子类编码
                        if (selectedLargeAndSmallItemList != null && !TextUtils.isEmpty(selectedLargeAndSmallItemList.get(1))) {
                            uploadedFacility.setChildCode(selectedLargeAndSmallItemList.get(1));
                        } else {
                            uploadedFacility.setChildCode("");
                        }


                        //20秒提示一次
                        countDownTimer = new CountDownTimer(UploadFacilityService.TIMEOUT * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                long time = millisUntilFinished / 1000;
                                if (progressDialog != null) {
                                    progressDialog.setMessage("正在提交，请等待。   " + time + "s");
                                }
                                if (time % 20 == 0) {
                                    ToastUtil.longToast(ReEditUploadFacilityActivity.this, "网络忙，请稍等");
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
                        identificationService.upload(uploadedFacility)
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
                                        ToastUtil.shortToast(ReEditUploadFacilityActivity.this, "提交失败");
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
                                            ToastUtil.shortToast(ReEditUploadFacilityActivity.this, "提交成功");
                                            EventBus.getDefault().post(new RefreshMyUploadList());
                                            //uploadedFacility.setPhotos(take_photo_item.getSelectedPhotos());
                                            if (uploadedFacility.deletedPhotoIds != null) {
                                                uploadedFacility.deletedPhotoIds.clear();
                                            }
                                            EventBus.getDefault().post(new UploadFacilitySuccessEvent(uploadedFacility));
                                            if (isDraft) {
                                                setResult(RESULT_OK);
                                            }
                                            finish();
                                        } else {
                                            ToastUtil.shortToast(ReEditUploadFacilityActivity.this, "保存失败，原因是：" + responseBody.getMessage());
                                        }
                                    }
                                });
                    }
                });

        RxView.clicks(btn_upload_event_journal)
                .throttleFirst(2, TimeUnit.SECONDS)   //2秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UploadedFacility nwUploadedFacility = getUploadFacility();
                        if (nwUploadedFacility != null) {
                            saveUploadFacility(nwUploadedFacility);
                        }
                    }
                });

    }

    private void initData() {

    }

    /**
     * 获取删除的门牌号id
     *
     * @param mpBeen
     * @return
     */
    private List<Long> getMphDels(List<DoorNOBean> mpBeen) {
        List<Long> delIds = new ArrayList<>();
        if (ListUtil.isEmpty(mDoorNOBeans)) {
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
        if (ListUtil.isEmpty(mDoorNOBeans)) {
            return delIds;
        }
        for (DoorNOBean doorNOBean : mDoorNOBeans) {
            if (doorNOBean.getId() != null) {
                delIds.add(doorNOBean.getId());
            }
        }
        return delIds;
    }


    private boolean completeAddress() {
        if (mComponentAddressErrorView != null) {
            if (!mComponentAddressErrorView.ifAllowUpload()) {
                ToastUtil.iconShortToast(this, R.mipmap.ic_alert_yellow, mComponentAddressErrorView.getNotAllowUploadReason());
                return false;
            }
            FacilityAddressErrorModel facilityAddressErrorModel = mComponentAddressErrorView.getFacilityAddressErrorModel();
            uploadedFacility.setAddr(facilityAddressErrorModel.getCorrectAddress());
            uploadedFacility.setX(facilityAddressErrorModel.getCorrectX());
            uploadedFacility.setY(facilityAddressErrorModel.getCorrectY());
            uploadedFacility.setRoad(facilityAddressErrorModel.getRoad());
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
                    uploadedFacility.setUserLocationX(lastLocation.getLocation().getLongitude());
                    uploadedFacility.setUserLocationY(lastLocation.getLocation().getLatitude());
                    uploadedFacility.setUserAddr(lastLocation.getDetailAddress().getDetailAddress());
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
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        if (!LocationUtil.ifUnRegister()) {
            LocationUtil.unregister(ReEditUploadFacilityActivity.this);
        }

        if (baiduLocationManager != null) {
            baiduLocationManager.stopLocate();
        }

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (facilityProblemView != null) {
            facilityProblemView = null;
        }

        if (facilityAttributeView != null) {
            facilityAttributeView.destroy();
            facilityAttributeView = null;
        }
    }


    @Override
    public void onBackPressed() {
        showAlertDialog();
        //super.onBackPressed();
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
//            cbOthers.setChecked(true);
//            etOthers.setText(sendFacilityOwnerShipUnit.originOwnership);

            //清空spinner
            spinnerOthers.removeAll();
            //获取行政区域列表
            TableDBService tableDBService = new TableDBService(this);
            List<DictionaryItem> districts = tableDBService.getDictionaryByTypecodeInDB("A169");
            //跟传递过来的区域进行对比
            String districtCode = null;
            for (DictionaryItem district : districts) {
                boolean contains = sendFacilityOwnerShipUnit.originOwnership.contains(district.getName());
                if (contains) {
                    districtCode = district.getCode();
                    break;
                }
            }
            //根据区域从数据字典读取该区的权属单位列表
            if (districtCode != null) {
                List<DictionaryItem> ownershipDic = tableDBService.getChildDictionaryByPCodeInDB(districtCode);
                if (!ListUtil.isEmpty(ownershipDic)) {
                    mManStatusIsSpinner = true;
                    if (cbOthers.isChecked()) {
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
                    for (DictionaryItem dictionaryItem : ownershipDic) {
                        //填充spinner
                        spinnerOthers.addItems(dictionaryItem.getName(), dictionaryItem);
                    }

                    if (spinnerOthers.containsKey(uploadedFacility.getCityVillage())) {
                        spinnerOthers.selectItem(uploadedFacility.getCityVillage());
                    } else {
                        spinnerOthers.selectItem(0);
                    }
                } else {
                    mManStatusIsSpinner = false;
                    if (cbOthers.isChecked()) {
                        spinnerOthers.setVisibility(View.GONE);
                        etOthers.setVisibility(View.VISIBLE);
                    }
//                    spinnerOthers.setVisibility(View.GONE);
//                    etOthers.setVisibility(View.VISIBLE);
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

    @Subscribe
    public void refreshFactifityType(RefreshTypeEvent event) {
        facilityType = event.getType();
        if (facilityType == 3) {
            take_photo_pfk = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_pfkou);
        }
        if (event.getType() == 1 || event.getType() == 2) {
//            ll_problem.setVisibility(View.VISIBLE);
//            ll_glzt_containt.setVisibility(View.VISIBLE);
        } else {
            ll_problem.setVisibility(View.GONE);
//            ll_glzt_containt.setVisibility(View.GONE);
        }
    }

    private UploadedFacility getUploadFacility() {
        List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();
        List<Photo> selectedPhotosWell = take_photo_item.getSelectedPhotos();
//        int mincount = 3;
//        if(facilityAttributeView.getFacilityAttributeModel()!=null
//                && "排水口".equals(facilityAttributeView.getFacilityAttributeModel().getLayerName())){
//            mincount = 4;
//        }
//        if (ListUtil.isEmpty(selectedPhotos) || selectedPhotos.size() < mincount) {
//            ToastUtil.iconLongToast(getApplicationContext(), R.mipmap.ic_alert_yellow, "请按“拍照须知”要求至少提供"+mincount+"张照片！");
//            return null;
//        }
        boolean ifCompleteAttributeSuccess = true;
        if (facilityAttributeView.getFacilityAttributeModel() != null
                && facilityAttributeView.getFacilityAttributeModel().getLayerName() != null) {
            FacilityAttributeModel facilityAttributeModel = facilityAttributeView.getFacilityAttributeModel();
//            if (!facilityAttributeModel.isIfAllowUpload()) {
//                ToastUtil.iconShortToast(getApplicationContext(), R.mipmap.ic_alert_yellow,
//                        facilityAttributeModel.getNotAllowUploadReason());
//                ifCompleteAttributeSuccess = false;
//            }
            uploadedFacility.setLayerName(facilityAttributeModel.getLayerName());
            uploadedFacility.setComponentType(facilityAttributeModel.getLayerName());
            uploadedFacility.setAttrOne(facilityAttributeModel.getAttrOne());
            uploadedFacility.setAttrTwo(facilityAttributeModel.getAttrTwo());
            uploadedFacility.setAttrThree(facilityAttributeModel.getAttrThree());
            uploadedFacility.setAttrFour(facilityAttributeModel.getAttrFour());
            uploadedFacility.setAttrFour(facilityAttributeModel.getGpbh());
            uploadedFacility.setAttrFive(facilityAttributeModel.getAttrFive());
            uploadedFacility.setAttrSix(facilityAttributeModel.getAttrSix());
            uploadedFacility.setAttrSeven(facilityAttributeModel.getAttrSeven());
            uploadedFacility.setSfpsdyhxn(facilityAttributeModel.getSfpsdyhxn());
            uploadedFacility.setRiverx(facilityAttributeModel.getX());
            uploadedFacility.setRivery(facilityAttributeModel.getY());
        }

        /**
         * 如果此时完善属性不成功，不继续向下执行
         */
//        if (!ifCompleteAttributeSuccess) {
//            return null;
//        }

//        if (TextUtils.isEmpty(uploadedFacility.getComponentType())) {
//            ToastUtil.shortToast(getApplicationContext(), "请先选择设施类型");
//            return null;
//        }

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
//            ToastUtil.iconShortToast(getApplicationContext(), R.mipmap.ic_alert_yellow, "管理状态不可以为空");
//            return null;
//        }


        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ReEditUploadFacilityActivity.this);
            progressDialog.setMessage("正在提交，请等待");
            progressDialog.setCancelable(false);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

        uploadedFacility.setPhotos(selectedPhotos);
        uploadedFacility.setThumbnailPhotos(take_photo_item.getThumbnailPhotos());

        uploadedFacility.setPhotos(selectedPhotos);
        uploadedFacility.setThumbnailPhotos(take_photo_item.getThumbnailPhotos());
        uploadedFacility.setDescription(textitem_description.getText());


        /**
         * 管理状态
         */
//        if (cbDontKnowWhere.isChecked() || cbInnerCity.isChecked()) {
//            uploadedFacility.setCityVillage(cityVillage);
//        } else if (cbOthers.isChecked()) {
//            if(mManStatusIsSpinner){
//                uploadedFacility.setCityVillage(spinnerOthers.getText());
//            } else {
//                uploadedFacility.setCityVillage(etOthers.getText().toString());
//            }
//        } else {
//            uploadedFacility.setCityVillage(null);
//        }
        uploadedFacility.setCityVillage(null);
//        //设施问题
//        List<String> selectedLargeAndSmallItemList = facilityProblemView.getSelectedParentAndChildICodeList();
//        //父类编码
//        if (!TextUtils.isEmpty(selectedLargeAndSmallItemList.get(0))) {
//            uploadedFacility.setpCode(selectedLargeAndSmallItemList.get(0));
//        }
//
//        //子类编码
//        if (!TextUtils.isEmpty(selectedLargeAndSmallItemList.get(1))) {
//            uploadedFacility.setChildCode(selectedLargeAndSmallItemList.get(1));
//        }
        uploadedFacility.setTime(System.currentTimeMillis());
        return uploadedFacility;
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

        if (take_photo_item != null) {
            List<Photo> photos = take_photo_item.getDeletedPhotos();
            List<Photo> thumbPhotos = take_photo_item.getDeletedThumbPhotos();
            if (!ListUtil.isEmpty(photos)) {
                for (Photo photo : photos) {
                    AMDatabase.getInstance().delete(photo);
                }
            }
            if (!ListUtil.isEmpty(thumbPhotos)) {
                for (Photo photo : thumbPhotos) {
                    AMDatabase.getInstance().delete(photo);
                }
            }
        }

        ToastUtil.shortToast(this.getApplicationContext(), "保存成功");
        setResult(123);
        finish();
    }

    @Subscribe
    public void onMphDeleteReve(OnMenPaiDeleteEvent event) {
        if (event == null) {
            return;
        }
        if (!mMphDels.contains(event.getDoorNOBean().getId())) {
            mMphDels.add(event.getDoorNOBean().getId());
        }
    }

}
