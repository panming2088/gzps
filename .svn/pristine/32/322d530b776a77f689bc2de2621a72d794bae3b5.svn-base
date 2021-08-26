package com.augurit.agmobile.gzps.maintain.view.reedit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
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
import com.augurit.agmobile.gzps.maintain.model.Conserve;
import com.augurit.agmobile.gzps.maintain.model.ConserveDetail;
import com.augurit.agmobile.gzps.maintain.model.MaintainBatchInfo;
import com.augurit.agmobile.gzps.maintain.model.MaintainSuccessEvent;
import com.augurit.agmobile.gzps.maintain.service.MaintainFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityAddressErrorModel;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.view.SendFacilityOwnerShipUnit;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.FacilityAddressErrorView;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.RefreshMyUploadList;
import com.augurit.agmobile.patrolcore.common.model.LocationInfo;
import com.augurit.agmobile.patrolcore.locate.BaiduLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.loc.util.LocationUtil;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
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

public class MaintainReEditActivity extends BaseActivity {


    private MultiTakePhotoTableItem take_photo_item;
    private View btn_upload_journal;
    private Conserve mConserve;
    private TextItemTableItem tableitem_current_time;
    private TextItemTableItem tableitem_current_user;
    private TextFieldTableItem textitem_description;

    private LinearLayout ll_container;

    private DetailAddress detailAddress;
    private BaiduLocationManager baiduLocationManager;

    private Toast toast;
    private ProgressDialog progressDialog;
    private CountDownTimer countDownTimer; //用于上报时超过一定时间提示一次
    private FacilityAddressErrorView mComponentAddressErrorView;
    private MaintainBatchInfo.RowsBean rowsbean;
    private TextItemTableItem maintain_name;
    private TextFieldTableItem maintain_mark;
    private ConserveDetail uploadedFacility;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upload_new_maintain);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        /**
         * 在Activity跳转之前已经确保detailAddress和location不为空，所以下面的代码省去了这两个变量的非空判断
         */
        uploadedFacility = getIntent().getParcelableExtra("data");
        if(uploadedFacility != null){
            mConserve = getConserveForDetail(uploadedFacility);
        }
//        mConserve = new Conserve();

        initView();

        initUser();

        initData();

        //获取用户当前位置
        startLocate();
    }

    private Conserve getConserveForDetail(ConserveDetail uploadedFacility) {
        Conserve conserve = new Conserve();
        conserve.setRoad(uploadedFacility.getRoad());
        conserve.setAddr(uploadedFacility.getAddr());
        conserve.setX(Double.valueOf(uploadedFacility.getX()));
        conserve.setY(Double.valueOf(uploadedFacility.getY()));
        conserve.setPlanId(uploadedFacility.getPlanId());
        conserve.setWtms(uploadedFacility.getWtms());
        conserve.setUserName(uploadedFacility.getMarkPerson());
        conserve.setRemark(uploadedFacility.getRemark());
        conserve.setMarkTime(uploadedFacility.getMarkTime());
        conserve.setId(uploadedFacility.getId());
        conserve.setPhotos(uploadedFacility.getPhotos());
        conserve.setParentOrgName(uploadedFacility.getParentOrgName());
        conserve.setState(uploadedFacility.getCheckState());
        return conserve;
    }

    private void initData() {
        if(!ListUtil.isEmpty(uploadedFacility.getPhotos())){
            take_photo_item.setSelectedPhotos(uploadedFacility.getPhotos());
        }
        if(!StringUtil.isEmpty(uploadedFacility.getRemark())){
            maintain_mark.setText(uploadedFacility.getRemark());
        }
        if(!StringUtil.isEmpty(uploadedFacility.getWtms())){
            textitem_description.setText(uploadedFacility.getWtms());
        }
        maintain_name.setText(uploadedFacility.getPlanId());
    }

    private void initUser() {
        User user = new LoginService(this, AMDatabase.getInstance()).getUser();
        String loginName = user.getLoginName();
        tableitem_current_user.setText(mConserve.getUserName());
        mConserve.setLoginName(loginName);
    }


    private void initView() {

        take_photo_item = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_item);
        take_photo_item.setPhotoExampleEnable(false);
        take_photo_item.setRequired(true);
        ((TextView) findViewById(R.id.tv_title)).setText("再次编辑");

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        maintain_name = (TextItemTableItem) findViewById(R.id.maintain_name);
        maintain_name.setTextViewName("养护计划编号");
        maintain_name.setReadOnly();

        /**
         * 当前时间
         */
        tableitem_current_time = (TextItemTableItem) findViewById(R.id.tableitem_current_time);
        tableitem_current_time.setVisibility(View.GONE);

        /**
         * 填表人
         */
        tableitem_current_user = (TextItemTableItem) findViewById(R.id.tableitem_current_user);
        tableitem_current_user.setReadOnly();
        /**
         * 描述
         */
        textitem_description = (TextFieldTableItem) findViewById(R.id.textitem_description);
        maintain_mark = (TextFieldTableItem) findViewById(R.id.maintain_mark);

        /**
         * 部件位置
         */
        if(mConserve != null) {
            detailAddress = new DetailAddress();
            detailAddress.setX(mConserve.getX());
            detailAddress.setY(mConserve.getY());
            detailAddress.setStreet(mConserve.getRoad());
            detailAddress.setDetailAddress(mConserve.getAddr());
            LinearLayout selectLocationContainer = (LinearLayout) findViewById(R.id.rl_select_location_container);
            mComponentAddressErrorView = new FacilityAddressErrorView(MaintainReEditActivity.this,
                    detailAddress);
            selectLocationContainer.removeAllViews();
            mComponentAddressErrorView.addTo(selectLocationContainer);
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

                        List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();
                        if (ListUtil.isEmpty(selectedPhotos) || selectedPhotos.size() < 2) {
                            showToast("请至少提供两张照片！");
                            return;
                        }

                        /**
                         * 填充地址
                         */
                        boolean success = completeAddress();
                        if (!success) {
                            return;
                        }

                        if (progressDialog == null) {
                            progressDialog = new ProgressDialog(MaintainReEditActivity.this);
                            progressDialog.setMessage("正在提交，请等待");
                            progressDialog.setCancelable(false);
                        }

                        if (!progressDialog.isShowing()) {
                            progressDialog.show();
                        }

                        mConserve.setPhotos(selectedPhotos);
                        mConserve.setThumbnailPhotos(take_photo_item.getThumbnailPhotos());
                        List<Photo> deletedPhotos = take_photo_item.getDeletedPhotos();
                        List<String> deletedPhotoIds = new ArrayList<>();
                        if (!ListUtil.isEmpty(deletedPhotos)) {
                            for (Photo photo : deletedPhotos) {
                                if (photo.getId() != 0) {
                                    deletedPhotoIds.add(String.valueOf(photo.getId()));
                                }
                            }
                            mConserve.setDeletedPhotoIds(deletedPhotoIds);
                        }
                        mConserve.setWtms(textitem_description.getText());
                        mConserve.setRemark(maintain_mark.getText());
                        //20秒提示一次
                        countDownTimer = new CountDownTimer(UploadFacilityService.TIMEOUT * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                long time = millisUntilFinished / 1000;
                                if (progressDialog != null) {
                                    progressDialog.setMessage("正在提交，请等待。   " + time + "s");
                                }
                                if (time % 20 == 0) {
                                    ToastUtil.longToast(MaintainReEditActivity.this, "网络忙，请稍等");
                                }
                            }

                            @Override
                            public void onFinish() {

                            }
                        };

                        if (countDownTimer != null) {
                            countDownTimer.start();
                        }

                        MaintainFacilityService identificationService = new MaintainFacilityService(getApplicationContext());
                        identificationService.upload(mConserve)
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
                                                BaseInfoManager.getUserName(MaintainReEditActivity.this) + "原因是：" + e.getLocalizedMessage()));
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
                                            ToastUtil.shortToast(MaintainReEditActivity.this, "提交成功");
                                            EventBus.getDefault().post(new RefreshMyUploadList());
                                            //uploadedFacility.setPhotos(take_photo_item.getSelectedPhotos());
                                            if (mConserve.getDeletedPhotoIds() != null) {
                                                mConserve.getDeletedPhotoIds().clear();
                                            }
                                            EventBus.getDefault().post(new MaintainSuccessEvent(uploadedFacility));
                                            finish();
                                        } else {
                                            ToastUtil.shortToast(MaintainReEditActivity.this, "保存失败，原因是：" + responseBody.getMessage());
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
            mConserve.setAddr(facilityAddressErrorModel.getCorrectAddress());
            mConserve.setX(facilityAddressErrorModel.getCorrectX());
            mConserve.setY(facilityAddressErrorModel.getCorrectY());
            mConserve.setRoad(facilityAddressErrorModel.getRoad());
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
                        mConserve.setX(lastLocation.getLocation().getLongitude());
                        mConserve.setY(lastLocation.getLocation().getLatitude());
                    }
                    if (lastLocation.getDetailAddress() != null) {
                        mConserve.setRoad(lastLocation.getDetailAddress().getStreet());
                        mConserve.setAddr(lastLocation.getDetailAddress().getDetailAddress());
                    }
                    if(detailAddress == null){
                        detailAddress = new DetailAddress();
                        detailAddress.setX(mConserve.getX());
                        detailAddress.setY(mConserve.getY());
                        detailAddress.setStreet(mConserve.getRoad());
                        detailAddress.setDetailAddress(mConserve.getAddr());
                        LinearLayout selectLocationContainer = (LinearLayout) findViewById(R.id.rl_select_location_container);
                        mComponentAddressErrorView = new FacilityAddressErrorView(MaintainReEditActivity.this,
                                detailAddress);
                        selectLocationContainer.removeAllViews();
                        mComponentAddressErrorView.addTo(selectLocationContainer);
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
            LocationUtil.unregister(MaintainReEditActivity.this);
        }

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (baiduLocationManager != null) {
            baiduLocationManager.stopLocate();
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
    }
}
