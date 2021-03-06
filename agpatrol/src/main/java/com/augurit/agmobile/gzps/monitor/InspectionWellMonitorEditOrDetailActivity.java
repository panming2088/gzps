package com.augurit.agmobile.gzps.monitor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.util.NumberUtil;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.jbjpsdy.WellMonitorRefreshEvent;
import com.augurit.agmobile.gzps.monitor.model.InspectionWellMonitorInfo;
import com.augurit.agmobile.gzps.monitor.service.InspectionWellMonitorService;
import com.augurit.agmobile.gzps.monitor.view.TextItemTableItem1;
import com.augurit.agmobile.gzps.monitor.view.time.ScreenInfo;
import com.augurit.agmobile.gzps.monitor.view.time.WheelMain;
import com.augurit.agmobile.gzps.uploadfacility.model.CheckState;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.util.InspectionSetCheckStateUtil;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.RefreshMyUploadList;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.loc.util.LocationUtil;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * ??????????????????????????????????????????/?????????
 *
 * @author ????????? ???xuciluan
 * @version 1.0
 * @package ?????? ???com.augurit.agmobile.gzps.upload.view
 * @createTime ???????????? ???17/12/18
 * @modifyBy ????????? ???xuciluan
 * @modifyTime ???????????? ???17/12/18
 * @modifyMemo ???????????????
 */

public class InspectionWellMonitorEditOrDetailActivity extends BaseActivity {
    private MultiTakePhotoTableItem take_photo_item;
    private TextItemTableItem1 textItemAd;//????????????
    private TextItemTableItem1 textItemCod;//cod??????
    private TextItemTableItem1 textItemUser;//?????????
    private TextFieldTableItem textItemRemarks; // ??????

    private Button btn_edit, btn_upload, btn_delete;
    private InspectionWellMonitorInfo orignMonitorInfo;
    private InspectionWellMonitorInfo finalMonitorInfo;
    private CountDownTimer countDownTimer;
    private ProgressDialog progressDialog;
    private InspectionWellMonitorService inspectionWellMonitorService;
    private String usid;//usid
    private String jbjObjectId;//objectid
    private TextView tittleTv;
    private Button btn_time;
    private boolean isFromMap = false;
    private boolean onlyRead = false;
    private Calendar cal;
    private List<Photo> selectedPhotos;
    private WindowManager.LayoutParams params;
    private String type, X, Y;
    WheelMain wheelMain;
    private View ll_check_status;
    private TextItemTableItem1 tableitem_checkstate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_well_monitor_edit_or_detail);
        initData();
        initView();
    }

    private void initData() {
        inspectionWellMonitorService = new InspectionWellMonitorService(this);

        Object obj = getSerializable("InspectionWellMonitorInfo", null);
        if (obj != null) {
            orignMonitorInfo = (InspectionWellMonitorInfo) obj;
            String usid = orignMonitorInfo.getUsid();
            if (TextUtils.isEmpty(usid) || "null".equals(usid)) {
                orignMonitorInfo.setUsid("");
            }
        }
        usid = getString("usid", "");
        jbjObjectId = getString("objectid", "");
        type = getString("type", "");
        X = getString("X", "");
        Y = getString("Y", "");
        isFromMap = getBoolean("isFromMap", false);
        onlyRead = orignMonitorInfo != null && !isFromMap;
    }

    private void initView() {
        if (finalMonitorInfo == null) {
            finalMonitorInfo = new InspectionWellMonitorInfo();
        }
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InspectionWellMonitorEditOrDetailActivity.this.onBackPressed();
            }
        });
        tittleTv = ((TextView) findViewById(R.id.tv_title));
        tittleTv.setText(orignMonitorInfo == null ? "??????????????????" : "??????????????????");
        take_photo_item = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_item);

        ll_check_status = findViewById(R.id.ll_check_status);
        tableitem_checkstate = (TextItemTableItem1) findViewById(R.id.tableitem_checkstate);
        tableitem_checkstate.setReadOnly();
        textItemAd = (TextItemTableItem1) findViewById(R.id.tableitem_ad);
        textItemCod = (TextItemTableItem1) findViewById(R.id.tableitem_cod);
        textItemUser = (TextItemTableItem1) findViewById(R.id.tableitem_current_user);
        btn_time = (Button) findViewById(R.id.btn_time);

        textItemRemarks = (TextFieldTableItem) findViewById(R.id.textitem_remark);

        setEditInputMode();
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_delete = (Button) findViewById(R.id.btn_delete);

        setReadOnly(onlyRead);
        if (orignMonitorInfo != null) {
            fillUI(orignMonitorInfo);
            if (!onlyRead) {
                btn_edit.setVisibility(View.GONE);
                btn_upload.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.VISIBLE);
            }
        } else {
            Calendar calendar = Calendar.getInstance();
            /*btn_time.setText(calendar.get(Calendar.YEAR) + "-"
                    + ((calendar.get(Calendar.MONTH) + 1) > 9 ? (calendar.get(Calendar.MONTH) + 1) : "0" + (calendar.get(Calendar.MONTH) + 1)) + "-"
                    + (calendar.get(Calendar.DAY_OF_MONTH) > 9 ? calendar.get(Calendar.DAY_OF_MONTH) : "0" + calendar.get(Calendar.DAY_OF_MONTH)) + " "
                    + (calendar.get(Calendar.HOUR_OF_DAY) > 9 ? calendar.get(Calendar.HOUR_OF_DAY) : "0" + calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                    + (calendar.get(Calendar.MINUTE) > 9 ? calendar.get(Calendar.MINUTE) : "0" + calendar.get(Calendar.MINUTE))
            );*/
            btn_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(calendar.getTime()));
        }

        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime();
            }
        });

        RxView.clicks(btn_upload)
                .throttleFirst(2, TimeUnit.SECONDS)   //2???????????????????????????????????????????????????
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (!checkParam()) return;
                        if (progressDialog == null) {
                            progressDialog = new ProgressDialog(InspectionWellMonitorEditOrDetailActivity.this);
                            progressDialog.setMessage("????????????????????????");
                            progressDialog.setCancelable(false);
                        }
                        if (!progressDialog.isShowing()) {
                            progressDialog.show();
                        }
                        //20???????????????
                        countDownTimer = new CountDownTimer(UploadFacilityService.TIMEOUT * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                long time = millisUntilFinished / 1000;
                                if (progressDialog != null) {
                                    progressDialog.setMessage("???????????????????????????   " + time + "s");
                                }
                                if (time % 20 == 0) {
                                    ToastUtil.longToast(InspectionWellMonitorEditOrDetailActivity.this, "?????????????????????");
                                }
                            }

                            @Override
                            public void onFinish() {
                            }
                        };

                        if (countDownTimer != null) {
                            countDownTimer.start();
                        }
                        Observable<ResponseBody> observable = null;
                        List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();
                        List<Photo> selectedThumbnail = take_photo_item.getThumbnailPhotos();
                        if (null != orignMonitorInfo) {
                            finalMonitorInfo.setDeleteAttachment(getDeletePhotoIds());
                            List<Photo> addPhotos = new ArrayList<>();
                            List<Photo> addThumbnails = new ArrayList<>();

                            for (Photo photo : selectedPhotos) {
                                if (photo.getId() == 0) {
                                    addPhotos.add(photo);
                                }
                            }
                            for (Photo photo : selectedThumbnail) {
                                if (photo.getId() == 0) {
                                    addThumbnails.add(photo);
                                }
                            }
                            finalMonitorInfo.setPhotos(addPhotos);
                            finalMonitorInfo.setThumbnailPhotos(addThumbnails);
                        } else {
                            finalMonitorInfo.setPhotos(selectedPhotos);
                            finalMonitorInfo.setThumbnailPhotos(selectedThumbnail);
                        }
                        observable = inspectionWellMonitorService.addInspectionWellMonitor(finalMonitorInfo);
                        observable.subscribeOn(Schedulers.io())
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
                                        ToastUtil.shortToast(InspectionWellMonitorEditOrDetailActivity.this, "????????????");
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
                                            ToastUtil.shortToast(InspectionWellMonitorEditOrDetailActivity.this, "????????????");
                                            EventBus.getDefault().post(new RefreshMyUploadList());
                                            EventBus.getDefault().post(new WellMonitorRefreshEvent(2));
                                            finish();
                                        } else {
                                            ToastUtil.shortToast(InspectionWellMonitorEditOrDetailActivity.this, "???????????????????????????" + responseBody.getMessage());
                                        }
                                    }
                                });
                    }
                });

        RxView.clicks(btn_edit)
                .throttleFirst(2, TimeUnit.SECONDS)   //2???????????????????????????????????????????????????
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        tittleTv.setText("??????????????????");
                        setReadOnly(false);

                    }
                });
        RxView.clicks(btn_delete)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDeleteDialog();
                    }
                });
    }


    private void setEditInputMode() {
        //??????????????????????????????????????????
        textItemAd.getEt_right().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        textItemCod.getEt_right().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //???????????????????????????4????????????????????????
        textItemAd.getEt_right().addTextChangedListener(new DecimalInputTextWatcher(textItemAd.getEt_right(), 4, 2));
        textItemCod.getEt_right().addTextChangedListener(new DecimalInputTextWatcher(textItemCod.getEt_right(), 4, 2));


    }

    //????????????
    private void selectTime() {
        LayoutInflater inflater = LayoutInflater
                .from(InspectionWellMonitorEditOrDetailActivity.this);
        final View timepickerview = inflater.inflate(
                R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(InspectionWellMonitorEditOrDetailActivity.this);
        wheelMain = new WheelMain(timepickerview, true);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int h = calendar.getTime().getHours();
        int m = calendar.getTime().getMinutes();
        wheelMain.initDateTimePicker(year, month, day, h, m);
        new AlertDialog.Builder(InspectionWellMonitorEditOrDetailActivity.this)
                .setTitle("????????????")
                .setView(timepickerview)
                .setPositiveButton("??????",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                btn_time.setText(wheelMain.getTime());
                            }
                        })
                .setNegativeButton("??????",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
    }

    //????????????????????????
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    private void showDeleteDialog() {
        DialogUtil.MessageBox(this, "??????", "????????????????????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }

    private void delete() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("????????????????????????");
            progressDialog.setCancelable(false);
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        inspectionWellMonitorService.deleteInspectionWellMonitor(orignMonitorInfo.getId())
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
                        ToastUtil.shortToast(InspectionWellMonitorEditOrDetailActivity.this, "????????????");
                        CrashReport.postCatchedException(new Exception("?????????????????????????????????" +
                                BaseInfoManager.getUserName(InspectionWellMonitorEditOrDetailActivity.this) + "????????????" + e.getLocalizedMessage()));
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (responseBody.getCode() == 200) {
                            ToastUtil.shortToast(InspectionWellMonitorEditOrDetailActivity.this, "????????????");
                            EventBus.getDefault().post(new RefreshMyUploadList());
                            EventBus.getDefault().post(new WellMonitorRefreshEvent(1));
                            finish();
                        } else {
                            ToastUtil.shortToast(InspectionWellMonitorEditOrDetailActivity.this, responseBody.getMessage());
                            CrashReport.postCatchedException(new Exception("?????????????????????????????????" +
                                    BaseInfoManager.getUserName(InspectionWellMonitorEditOrDetailActivity.this) + "????????????" + responseBody.getMessage()));
                        }
                    }
                });
    }

    /**
     * ???????????????id
     *
     * @return
     */
    private String getDeletePhotoIds() {
        StringBuffer sb = new StringBuffer();
        List<Photo> deletedPhotos = take_photo_item.getDeletedPhotos();
        if (!ListUtil.isEmpty(deletedPhotos)) {
            for (Photo photo : deletedPhotos) {
                sb.append(photo.getId()).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private boolean checkParam() {
//        selectedPhotos = take_photo_item.getSelectedPhotos();
//        if (ListUtil.isEmpty(selectedPhotos) || selectedPhotos.size() < 1) {
//            ToastUtil.shortToast(this, "?????????????????????????????????????????????????????????");
//            return false;
//        }

        if (TextUtils.isEmpty(textItemAd.getText())) {
            ToastUtil.shortToast(this, "?????????????????????");
            return false;
        }
        if (!NumberUtil.isNumber(textItemAd.getText())) {
            ToastUtil.shortToast(this, "???????????????????????????");
            return false;
        }

        if (!TextUtils.isEmpty(textItemCod.getText()) && !NumberUtil.isNumber(textItemCod.getText())) {
            ToastUtil.shortToast(this, "Cod?????????????????????");
            return false;
        }
        finalMonitorInfo.setAd(textItemAd.getText());
        finalMonitorInfo.setAd(textItemAd.getText());
        if (!TextUtils.isEmpty(textItemCod.getText())) {
            finalMonitorInfo.setCod(textItemCod.getText().toString());
        }

        if (orignMonitorInfo != null) {
            finalMonitorInfo.setUsid(orignMonitorInfo.getUsid());
            finalMonitorInfo.setJbjObjectId(orignMonitorInfo.getJbjObjectId());
            finalMonitorInfo.setId(orignMonitorInfo.getId());
        } else {
            finalMonitorInfo.setUsid(usid);
            finalMonitorInfo.setJbjObjectId(jbjObjectId);
        }
        try {
            finalMonitorInfo.setJcsj(Long.valueOf(dateToStamp(btn_time.getText().toString())));
        } catch (ParseException e) {
            e.printStackTrace();
            finalMonitorInfo.setJcsj(System.currentTimeMillis());
        }
        finalMonitorInfo.setJbjType(type);
        finalMonitorInfo.setJbjX(Double.parseDouble(X));
        finalMonitorInfo.setJbjY(Double.parseDouble(Y));
        finalMonitorInfo.setDescription(textItemRemarks.getText());

        return true;
    }


    //????????????
    private void fillUI(InspectionWellMonitorInfo inspectionWellMonitorInfo) {
        take_photo_item.setSelectedPhotos(inspectionWellMonitorInfo.getPhotos());
        textItemAd.setText(inspectionWellMonitorInfo.getAd() + "");
        InspectionSetCheckStateUtil.setCheckState(inspectionWellMonitorInfo.getCheckState(), tableitem_checkstate);
        if (!TextUtils.isEmpty(inspectionWellMonitorInfo.getCod()) && !TextUtils.isEmpty(inspectionWellMonitorInfo.getCod().trim())) {
            textItemCod.setText(inspectionWellMonitorInfo.getCod());
        }

        if (inspectionWellMonitorInfo.getJcsj() != null && inspectionWellMonitorInfo.getJcsj() > 0) {
            btn_time.setText(stampToDate(inspectionWellMonitorInfo.getJcsj() + ""));
        }
        if (inspectionWellMonitorInfo.getJcsj() != null && inspectionWellMonitorInfo.getJcsj() != 0) {
            textItemUser.setText(TimeUtil.getStringTimeMdHmChines(new Date(inspectionWellMonitorInfo.getJcsj())));
        }
        if (!TextUtils.isEmpty(inspectionWellMonitorInfo.getDescription())) {
            // ??????\n???????????????????????????
            textItemRemarks.setText(inspectionWellMonitorInfo.getDescription().replaceAll("(\\n)|(???n)", "\n"));
        }
    }

    //???????????????????????????
    private void setReadOnly(boolean isReadOnly) {
        if (isReadOnly) {

            take_photo_item.setReadOnly();
            take_photo_item.setRequired(false);
            take_photo_item.setTitle("??????????????????");
            take_photo_item.setImageIsShow(false);
            take_photo_item.setPhotoNumShow(false, 9);
            textItemAd.setReadOnly();
            textItemCod.setReadOnly();
            ll_check_status.setVisibility(View.VISIBLE);
            textItemUser.setReadOnly();
            btn_time.setEnabled(false);

            textItemRemarks.setReadOnly();

            btn_edit.setVisibility(View.VISIBLE);
            if(CheckState.UNCHECK2.equals(orignMonitorInfo.getCheckState()) && orignMonitorInfo.getId() != -1) {
                btn_edit.setVisibility(View.GONE);
            }
            btn_upload.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);

            if (orignMonitorInfo != null && ListUtil.isEmpty(orignMonitorInfo.getPhotos())) {
                take_photo_item.setVisibility(View.GONE);
            }
        } else {
            take_photo_item.setEditable();
            ll_check_status.setVisibility(View.GONE);
            take_photo_item.setRequired(false);
            take_photo_item.setImageIsShow(take_photo_item.getSelectedPhotos().size() < 9);
            take_photo_item.setTitle("????????????");
            take_photo_item.setPhotoNumShow(true, 9);

            if (take_photo_item.getVisibility() != View.VISIBLE) {
                take_photo_item.setVisibility(View.VISIBLE);
            }

            textItemAd.setEditable(true);

            textItemAd.setRequireTag();

            textItemCod.setEditable(true);
            textItemUser.setEditable(true);
            btn_time.setEnabled(true);
            textItemRemarks.setEnableEdit(true);

            btn_upload.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(null == orignMonitorInfo ? View.GONE : View.VISIBLE);
            btn_edit.setVisibility(View.GONE);
        }
    }

    //????????????????????????
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        long lt = Long.parseLong(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (take_photo_item != null) {
            take_photo_item.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (!LocationUtil.ifUnRegister()) {
            LocationUtil.unregister(InspectionWellMonitorEditOrDetailActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        if (btn_upload.getVisibility() == View.VISIBLE) {
            showAlertDialog();
        } else {
            this.finish();
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
     * ?????????????????????
     */
    public static void jump(Context context, InspectionWellMonitorInfo info) {
        context.startActivity(getIntent(context, info, null, null, null, null, null, false));
    }

    /**
     * ???????????????Activity
     */
    public static void jump(Context context, String usid, String objectid, String type, String x, String y) {
        context.startActivity(getIntent(context, null, usid, objectid, type, x, y, false));
    }


    public static Intent getIntent(Context context, InspectionWellMonitorInfo info, String usid, String objectid, String type, String x, String y, boolean isFromMap) {
        Intent intent = new Intent(context, InspectionWellMonitorEditOrDetailActivity.class);
        Bundle extras = new Bundle();
        if (info != null) {
            extras.putSerializable("InspectionWellMonitorInfo", info);
            extras.putBoolean("ONLYREAD", true);
        }
        if (usid != null) {
            extras.putString("usid", usid);
        }
        if (objectid != null) {
            extras.putString("objectid", objectid);
        }
        if (type != null) {
            extras.putString("type", type);
        }
        if (x != null) {
            extras.putString("X", x);
        }
        if (y != null) {
            extras.putString("Y", y);
        }
        extras.putBoolean("isFromMap", isFromMap);
        intent.putExtras(extras);
        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    /**
     * ???Intent?????????Key??????Value?????????
     */
    private String getString(String key, String defaultValue) {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(key)) {
                return extras.getString(key);
            }
        }
        return defaultValue;
    }

    /**
     * ???Intent?????????Key??????getSerializable??????
     */
    private Serializable getSerializable(String key, Serializable defaultValue) {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(key)) {
                return extras.getSerializable(key);
            }
        }
        return defaultValue;
    }

    /**
     * ???Intent?????????Key??????getSerializable??????
     */
    private boolean getBoolean(String key, Boolean defaultValue) {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(key)) {
                return extras.getBoolean(key);
            }
            extras.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }
}
