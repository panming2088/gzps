package com.augurit.agmobile.gzps.professionreparation.reparationnewfacility;

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
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.professionreparation.correctfacility.ReparationFacilityAddressErrorView;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityAddressErrorModel;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityAttributeModel;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.view.SendFacilityOwnerShipUnit;
import com.augurit.agmobile.gzps.uploadfacility.view.facilityprobrem.FacilityProblemView;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.RefreshMyUploadList;
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
import com.google.android.flexbox.FlexboxLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

public class ReparationNewFacilityActivity extends BaseActivity {


    private MultiTakePhotoTableItem take_photo_item;
    private View btn_upload_journal;
    private UploadedFacility uploadFacility;
    private TextItemTableItem tableitem_current_time;
    private TextItemTableItem tableitem_current_user;
    private TextFieldTableItem textitem_description;
    private LinearLayout ll_attributelist_container;

    private LinearLayout ll_container;
    private RpFacilityAttributeView facilityAttributeView;

    private DetailAddress detailAddress;
    private BaiduLocationManager baiduLocationManager;

    private Toast toast;
    private ProgressDialog progressDialog;
    private CountDownTimer countDownTimer; //用于上报时超过一定时间提示一次
    private FlexboxLayout ll_problems_container;
    private FacilityProblemView facilityProblemView;
    private ReparationFacilityAddressErrorView mComponentAddressErrorView;

    /**
     * 城中村
     */
    protected CheckBox cbInnerCity;
    /**
     * 三不管
     */
    protected CheckBox cbDontKnowWhere;

    protected String cityVillage = null;
    protected CheckBox cbOthers;
    protected EditText etOthers;
    protected AMSpinner spinnerOthers;
    private boolean mManStatusIsSpinner = false;   //当前管理状态中的其他是Spinner还是EditText


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
        detailAddress = getIntent().getParcelableExtra("detailAddress");

        uploadFacility = new UploadedFacility();

        initView();

        initUser();

        initData();

        //获取用户当前位置
        startLocate();
    }

    private void initData() {
        double x = getIntent().getDoubleExtra("x", 0);
        double y = getIntent().getDoubleExtra("y", 0);
        uploadFacility.setX(x);
        uploadFacility.setY(y);
        uploadFacility.setAddr(detailAddress.getDetailAddress());
        uploadFacility.setRoad(detailAddress.getStreet());
        uploadFacility.setIsBinding(0);
    }

    private void initUser() {
        User user = new LoginService(this, AMDatabase.getInstance()).getUser();
        String userName = user.getUserName();
        String userId = user.getId();

        uploadFacility.setMarkPerson(userName);
        uploadFacility.setMarkPersonId(userId);
        tableitem_current_user.setText(userName);
    }


    private void initView() {

        take_photo_item = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_item);
        take_photo_item.setPhotoExampleEnable(true);
        take_photo_item.setRequired(true);
        ((TextView) findViewById(R.id.tv_title)).setText("数据新增");

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
        facilityAttributeView = new RpFacilityAttributeView(ReparationNewFacilityActivity.this);
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
        mComponentAddressErrorView = new ReparationFacilityAddressErrorView(ReparationNewFacilityActivity.this,
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
                        if (ListUtil.isEmpty(selectedPhotos) || selectedPhotos.size() < 3) {
                            showToast("请按“拍照须知”要求至少提供三张照片！");
                            return;
                        }


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
                            uploadFacility.setComponentType(facilityAttributeModel.getLayerName());
                            uploadFacility.setAttrOne(facilityAttributeModel.getAttrOne());
                            uploadFacility.setAttrTwo(facilityAttributeModel.getAttrTwo());
                            uploadFacility.setAttrThree(facilityAttributeModel.getAttrThree());
                            uploadFacility.setAttrFour(facilityAttributeModel.getAttrFour());
                            uploadFacility.setAttrFive(facilityAttributeModel.getAttrFive());
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
                        if (cbOthers.isChecked() && !mManStatusIsSpinner && TextUtils.isEmpty(etOthers.getText().toString())) {
                            showToast("管理状态不可以为空");
                            return;
                        }


                        if (progressDialog == null) {
                            progressDialog = new ProgressDialog(ReparationNewFacilityActivity.this);
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

                        //20秒提示一次
                        countDownTimer = new CountDownTimer(UploadFacilityService.TIMEOUT * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                long time = millisUntilFinished / 1000;
                                if (progressDialog != null) {
                                    progressDialog.setMessage("正在提交，请等待。   " + time + "s");
                                }
                                if (time % 20 == 0) {
                                    ToastUtil.longToast(ReparationNewFacilityActivity.this, "网络忙，请稍等");
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
                                                BaseInfoManager.getUserName(ReparationNewFacilityActivity.this) + "原因是：" + e.getLocalizedMessage()));
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
                                            showToast("提交成功");
                                            EventBus.getDefault().post(new RefreshMyUploadList());
                                            finish();
                                        } else {
                                            showToast("保存失败，原因是：" + responseBody.getMessage());
                                            CrashReport.postCatchedException(new Exception("新增上报失败，上报用户是：" +
                                                    BaseInfoManager.getUserName(ReparationNewFacilityActivity.this) + "原因是：" + responseBody.getMessage()));
                                        }
                                    }
                                });
                    }
                });
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
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (take_photo_item != null) {
            take_photo_item.onActivityResult(requestCode, resultCode, data);
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
                    }
                    if (lastLocation.getDetailAddress() != null) {
                        uploadFacility.setUserAddr(lastLocation.getDetailAddress().getDetailAddress());
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
            LocationUtil.unregister(ReparationNewFacilityActivity.this);
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

}
