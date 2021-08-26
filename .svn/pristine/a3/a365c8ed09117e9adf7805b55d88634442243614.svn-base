package com.augurit.agmobile.gzps.uploadfacility.view.doorno;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.uploadfacility.model.DoorNoRespone;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadDoorNoBean;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadDoorNoDetailBean;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.util.SetMaxLengthUtils;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.RefreshMyUploadList;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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

public class UploadNewDoorNoActivity extends BaseActivity implements View.OnClickListener {
    private DetailAddress mDetailAddress;
    private TextItemTableItem mItemOne;
    private TextItemTableItem mItemTwo;
    private TextItemTableItem mItemThree;
    private TextItemTableItem mItemFour;
    private TextItemTableItem mItemFive;

    private TextItemTableItem mItemSix;
    private TextItemTableItem mItemSeven;
    private TextFieldTableItem mTextFieldTableItem;
    private Button btnUpload;
    private ProgressDialog progressDialog;
    private double x;
    private double y;
    private Boolean isEditDoorNo;
    private UploadDoorNoDetailBean mCurrentUploadedDoorNo;
    private UploadDoorNoBean mUploadDoorNoBean;
    private boolean isfromQuryAddressMapFragmnet;
    private CountDownTimer countDownTimer;
    private UploadFacilityService mIdentificationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_doorno);
        mIdentificationService = new UploadFacilityService(getApplicationContext());
        mCurrentUploadedDoorNo = (UploadDoorNoDetailBean) getIntent().getSerializableExtra("data");
        isEditDoorNo = mCurrentUploadedDoorNo != null;
        x = getIntent().getDoubleExtra("x", 0);
        y = getIntent().getDoubleExtra("y", 0);
        isfromQuryAddressMapFragmnet = getIntent().getBooleanExtra("isfromQuryAddressMapFragmnet", false);
        mDetailAddress = getIntent().getParcelableExtra("detailAddress");
        initView();
    }

    public void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText(isEditDoorNo ? "编辑门牌" : "新增门牌号");
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });
        mItemOne = (TextItemTableItem) findViewById(R.id.text_item_one);
        mItemTwo = (TextItemTableItem) findViewById(R.id.text_item_two);
        mItemThree = (TextItemTableItem) findViewById(R.id.text_item_three);
        mItemFour = (TextItemTableItem) findViewById(R.id.text_item_four);
        mItemFive = (TextItemTableItem) findViewById(R.id.text_item_five);
        mItemSix = (TextItemTableItem) findViewById(R.id.text_item_six);
        mItemSeven = (TextItemTableItem) findViewById(R.id.text_item_seven);
        btnUpload = (Button) findViewById(R.id.btn_upload_door_no);

        mItemOne.setTextViewName(Html.fromHtml("行政区<font color='#FF0000'> *</font>"));
        mItemTwo.setTextViewName(Html.fromHtml("镇街<font color='#FF0000'> *</font>"));
        mItemThree.setTextViewName(Html.fromHtml("村(居)委会<font color='#FF0000'> *</font>"));
        mItemFour.setTextViewName(Html.fromHtml("街路巷<font color='#FF0000'> *</font>"));
        mItemFive.setTextViewName(Html.fromHtml("门牌号<font color='#FF0000'> *</font>"));
        SetMaxLengthUtils.setMaxLength(mItemTwo.getEt_right(), SetMaxLengthUtils.MAX_LENGTH_15);
        SetMaxLengthUtils.setMaxLength(mItemOne.getEt_right(), SetMaxLengthUtils.MAX_LENGTH_15);
        SetMaxLengthUtils.setMaxLength(mItemThree.getEt_right(), SetMaxLengthUtils.MAX_LENGTH_15);
        SetMaxLengthUtils.setMaxLength(mItemFour.getEt_right(), SetMaxLengthUtils.MAX_LENGTH_15);
        SetMaxLengthUtils.setMaxLength(mItemFive.getEt_right(), SetMaxLengthUtils.MAX_LENGTH_15);


        mItemSix.setVisibility(View.GONE);
        mItemSeven.setVisibility(View.GONE);

        btnUpload.setOnClickListener(this);
        mTextFieldTableItem = (TextFieldTableItem) findViewById(R.id.tf_table_upload_indicate);
        mTextFieldTableItem.setMaxLength(SetMaxLengthUtils.MAX_LENGTH_200);
//        double x = getIntent().getDoubleExtra("x", 0);
//        double y = getIntent().getDoubleExtra("y", 0);
        if (null != mDetailAddress) {
            mItemOne.setText(mDetailAddress.getDistrict());
            mItemFour.setText(mDetailAddress.getStreet());
            mItemFive.setText(mDetailAddress.getStreet_number());
        }
        if (isEditDoorNo) {
            mItemOne.setText(mCurrentUploadedDoorNo.getSsxzqh());
            mItemTwo.setText(mCurrentUploadedDoorNo.getSsxzjd());
            mItemThree.setText(mCurrentUploadedDoorNo.getSssqcjwh());
            mItemFour.setText(mCurrentUploadedDoorNo.getSsjlx());
            mItemFive.setText(mCurrentUploadedDoorNo.getMpdzmc());
            mItemSix.setText(mCurrentUploadedDoorNo.getMarkPerson());
            mTextFieldTableItem.setText(mCurrentUploadedDoorNo.getDescription());

        }
    }


    private void sunmitDoorNo() {
        mUploadDoorNoBean = new UploadDoorNoBean();

        mUploadDoorNoBean.setSsxzqh(mItemOne.getText().toString().replaceAll("\\s*|\t|\r|\n", ""));
        mUploadDoorNoBean.setSsxzjd(mItemTwo.getText().toString().replaceAll("\\s*|\t|\r|\n", ""));
        mUploadDoorNoBean.setSssqcjwh(mItemThree.getText().toString().replaceAll("\\s*|\t|\r|\n", ""));
        mUploadDoorNoBean.setSsjlx(mItemFour.getText().toString().replaceAll("\\s*|\t|\r|\n", ""));
        mUploadDoorNoBean.setMpdzmc(mItemFive.getText().toString().replaceAll("\\s*|\t|\r|\n", ""));
        mUploadDoorNoBean.setDescription(mTextFieldTableItem.getText().toString());

        mUploadDoorNoBean.setZxjd(isEditDoorNo ? mCurrentUploadedDoorNo.getZxjd() : x);
        mUploadDoorNoBean.setZxwd(isEditDoorNo ? mCurrentUploadedDoorNo.getZxwd() : y);
        mUploadDoorNoBean.setsGuid(isEditDoorNo ? mCurrentUploadedDoorNo.getsGuid() : "");
        mUploadDoorNoBean.setObjectId(isEditDoorNo ? mCurrentUploadedDoorNo.getObjectId() : "");

        if (mCurrentUploadedDoorNo != null) {
            mCurrentUploadedDoorNo.setSsxzqh(mItemOne.getText().toString());
            mCurrentUploadedDoorNo.setSsxzjd(mItemTwo.getText().toString());
            mCurrentUploadedDoorNo.setSssqcjwh(mItemThree.getText().toString());
            mCurrentUploadedDoorNo.setSsjlx(mItemFour.getText().toString());
            mCurrentUploadedDoorNo.setMpdzmc(mItemFive.getText().toString());
            mCurrentUploadedDoorNo.setDescription(mTextFieldTableItem.getText().toString());
        }


        //二次编辑后提交传SGID，区分新增门牌
        uploadDoorNO(mUploadDoorNoBean);
    }

    public boolean checkParams() {
        if (TextUtils.isEmpty(mItemOne.getText()) || TextUtils.isEmpty(mItemOne.getText().replaceAll("\\s*|\t|\r|\n", ""))) {
            ToastUtil.shortToast(this, "行政区不能为空");
            return false;
        }

        if (TextUtils.isEmpty(mItemTwo.getText()) || TextUtils.isEmpty(mItemTwo.getText().replaceAll("\\s*|\t|\r|\n", ""))) {
            ToastUtil.shortToast(this, "镇街不能为空");
            return false;
        }

        if (TextUtils.isEmpty(mItemThree.getText()) || TextUtils.isEmpty(mItemThree.getText().replaceAll("\\s*|\t|\r|\n", ""))) {
            ToastUtil.shortToast(this, "村(居)委会不能为空");
            return false;
        }

        if (TextUtils.isEmpty(mItemFour.getText()) || TextUtils.isEmpty(mItemFour.getText().replaceAll("\\s*|\t|\r|\n", ""))) {
            ToastUtil.shortToast(this, "街路巷不能为空");
            return false;
        }

        if (TextUtils.isEmpty(mItemFive.getText()) || TextUtils.isEmpty(mItemFive.getText().replaceAll("\\s*|\t|\r|\n", ""))) {
            ToastUtil.shortToast(this, "门牌号不能为空");
            return false;
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        showAlertDialog();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_upload_door_no:
                sunmitDoorNo();
                break;

            default:
                break;

        }
    }

    /**
     * 提交巡检日志
     *
     * @param bean
     */
    private void uploadDoorNO(final UploadDoorNoBean bean) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(UploadNewDoorNoActivity.this);
            progressDialog.setMessage("正在提交，请等待");
            progressDialog.setCancelable(false);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
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
                    ToastUtil.longToast(UploadNewDoorNoActivity.this, "网络忙，请稍等");
                }
            }

            @Override
            public void onFinish() {

            }
        };

        if (countDownTimer != null) {
            countDownTimer.start();
        }


        mIdentificationService.uploadDoor(bean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DoorNoRespone>() {
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
                        ToastUtil.shortToast(UploadNewDoorNoActivity.this, "提交失败");
                        CrashReport.postCatchedException(new Exception("新增上报失败，上报用户是：" +
                                BaseInfoManager.getUserName(UploadNewDoorNoActivity.this) + "原因是：" + e.getLocalizedMessage()));
                    }

                    @Override
                    public void onNext(DoorNoRespone responseBody) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            countDownTimer = null;
                        }
                        if (responseBody.getCode() == 200) {
                            ToastUtil.shortToast(UploadNewDoorNoActivity.this, "提交成功");
                            EventBus.getDefault().post(new RefreshMyUploadList());
                            if(mCurrentUploadedDoorNo != null) {
                                refreshDoorNo(bean);
                                EventBus.getDefault().post(new UploadDoorNoSuccessEvent(mCurrentUploadedDoorNo));
                            }
                            finish();
                        } else {
                            ToastUtil.shortToast(UploadNewDoorNoActivity.this, "保存失败，原因是：" + responseBody.getMessage());
                            CrashReport.postCatchedException(new Exception("新增上报失败，上报用户是：" +
                                    BaseInfoManager.getUserName(UploadNewDoorNoActivity.this) + "原因是：" + responseBody.getMessage()));
                        }
                    }
                });
    }

    private void refreshDoorNo(UploadDoorNoBean bean) {
        mCurrentUploadedDoorNo.setSsxzqh(bean.getSsxzqh());
        mCurrentUploadedDoorNo.setSsxzjd(bean.getSsxzjd());
        mCurrentUploadedDoorNo.setSssqcjwh(bean.getSssqcjwh());
        mCurrentUploadedDoorNo.setSsjlx(bean.getSsjlx());
        mCurrentUploadedDoorNo.setMpdzmc(bean.getMpdzmc());
        mCurrentUploadedDoorNo.setDescription(bean.getDescription());
    }
}
