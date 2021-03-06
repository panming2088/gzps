package com.augurit.agmobile.gzps.maintain.view.uploadnewmaintain;

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
import com.augurit.agmobile.gzps.maintain.model.MaintainBatchInfo;
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
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * ??????????????????
 *
 * @author ????????? ???xuciluan
 * @version 1.0
 * @package ?????? ???com.augurit.agmobile.gzps.modifiedIdentification
 * @createTime ???????????? ???17/11/3
 * @modifyBy ????????? ???xuciluan
 * @modifyTime ???????????? ???17/11/3
 * @modifyMemo ???????????????
 */

public class MaintainNewActivity extends BaseActivity {


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
    private CountDownTimer countDownTimer; //?????????????????????????????????????????????
    private FacilityAddressErrorView mComponentAddressErrorView;
    private MaintainBatchInfo.RowsBean rowsbean;
    private TextItemTableItem maintain_name;
    private TextFieldTableItem maintain_mark;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upload_new_maintain);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        /**
         * ???Activity????????????????????????detailAddress???location????????????????????????????????????????????????????????????????????????
         */
        detailAddress = getIntent().getParcelableExtra("detailAddress");
        rowsbean = getIntent().getParcelableExtra("rowsbean");

        mConserve = new Conserve();

        initView();

        initUser();

        initData();

        //????????????????????????
        startLocate();
    }

    private void initData() {
        double x = getIntent().getDoubleExtra("x", 0);
        double y = getIntent().getDoubleExtra("y", 0);
        mConserve.setX(x);
        mConserve.setY(y);
        if(detailAddress != null) {
            mConserve.setAddr(detailAddress.getDetailAddress());
            mConserve.setRoad(detailAddress.getStreet());
        }
        if(rowsbean != null) {
            maintain_name.setText(rowsbean.getName());
            mConserve.setPlanId(rowsbean.getId());
        }
    }

    private void initUser() {
        User user = new LoginService(this, AMDatabase.getInstance()).getUser();
        String userName = user.getUserName();
        String loginName = user.getLoginName();
        mConserve.setLoginName(loginName);
        mConserve.setUserName(userName);
        tableitem_current_user.setText(userName);
    }


    private void initView() {

        take_photo_item = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_item);
        take_photo_item.setPhotoExampleEnable(false);
        take_photo_item.setRequired(true);
        ((TextView) findViewById(R.id.tv_title)).setText("????????????");

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        maintain_name = (TextItemTableItem) findViewById(R.id.maintain_name);
        maintain_name.setReadOnly();

        /**
         * ????????????
         */
        tableitem_current_time = (TextItemTableItem) findViewById(R.id.tableitem_current_time);
        tableitem_current_time.setVisibility(View.GONE);

        /**
         * ?????????
         */
        tableitem_current_user = (TextItemTableItem) findViewById(R.id.tableitem_current_user);
        tableitem_current_user.setReadOnly();
        /**
         * ??????
         */
        textitem_description = (TextFieldTableItem) findViewById(R.id.textitem_description);
        maintain_mark = (TextFieldTableItem) findViewById(R.id.maintain_mark);

        /**
         * ????????????
         */
        double x = getIntent().getDoubleExtra("x", 0);
        double y = getIntent().getDoubleExtra("y", 0);
        if(detailAddress != null) {
            detailAddress.setX(x);
            detailAddress.setY(y);
            LinearLayout selectLocationContainer = (LinearLayout) findViewById(R.id.rl_select_location_container);
            mComponentAddressErrorView = new FacilityAddressErrorView(MaintainNewActivity.this,
                    detailAddress);
            selectLocationContainer.removeAllViews();
            mComponentAddressErrorView.addTo(selectLocationContainer);
        }

        /**
         * ??????
         */
        btn_upload_journal = findViewById(R.id.btn_upload_journal);
        RxView.clicks(btn_upload_journal)
                .throttleFirst(2, TimeUnit.SECONDS)   //2???????????????????????????????????????????????????
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();
                        if (ListUtil.isEmpty(selectedPhotos) || selectedPhotos.size() < 2) {
                            showToast("??????????????????????????????");
                            return;
                        }

                        /**
                         * ????????????
                         */
                        boolean success = completeAddress();
                        if (!success) {
                            return;
                        }

                        if (progressDialog == null) {
                            progressDialog = new ProgressDialog(MaintainNewActivity.this);
                            progressDialog.setMessage("????????????????????????");
                            progressDialog.setCancelable(false);
                        }

                        if (!progressDialog.isShowing()) {
                            progressDialog.show();
                        }

                        mConserve.setPhotos(selectedPhotos);
                        mConserve.setThumbnailPhotos(take_photo_item.getThumbnailPhotos());
                        mConserve.setWtms(textitem_description.getText());
                        mConserve.setRemark(maintain_mark.getText());
                        //20???????????????
                        countDownTimer = new CountDownTimer(UploadFacilityService.TIMEOUT * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                long time = millisUntilFinished / 1000;
                                if (progressDialog != null) {
                                    progressDialog.setMessage("???????????????????????????   " + time + "s");
                                }
                                if (time % 20 == 0) {
                                    ToastUtil.longToast(MaintainNewActivity.this, "?????????????????????");
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
                                        showToast("????????????");
                                        CrashReport.postCatchedException(new Exception("???????????????????????????????????????" +
                                                BaseInfoManager.getUserName(MaintainNewActivity.this) + "????????????" + e.getLocalizedMessage()));
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
                                            showToast("????????????");
                                            EventBus.getDefault().post(new RefreshMyUploadList());
                                            finish();
                                        } else {
                                            showToast("???????????????????????????" + responseBody.getMessage());
                                            CrashReport.postCatchedException(new Exception("???????????????????????????????????????" +
                                                    BaseInfoManager.getUserName(MaintainNewActivity.this) + "????????????" + responseBody.getMessage()));
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
                        mComponentAddressErrorView = new FacilityAddressErrorView(MaintainNewActivity.this,
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
        //????????????
        toast.show();
    }

    public void initToast(String toastString) {
        //??????????????????Toast????????????
        toast = new Toast(getApplication());
        //??????Tosat???????????????????????????
        toast.setDuration(Toast.LENGTH_SHORT);
        View view1 = View.inflate(this, com.augurit.am.fw.R.layout.view_toast, null);
        ((TextView) view1.findViewById(com.augurit.am.fw.R.id.tv_toast_text)).setText(toastString);
        ((ImageView) view1.findViewById(com.augurit.am.fw.R.id.iv_icon)).setImageResource(R.mipmap.ic_alert_yellow);
        //???????????????????????????Toast
        toast.setView(view1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        if (!LocationUtil.ifUnRegister()) {
            LocationUtil.unregister(MaintainNewActivity.this);
        }

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (baiduLocationManager != null) {
            baiduLocationManager.stopLocate();
        }
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
    }
}
