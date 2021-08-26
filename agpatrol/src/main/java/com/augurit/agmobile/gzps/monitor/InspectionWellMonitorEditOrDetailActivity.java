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
 * 窨井（关键节点）监测信息编辑/详情页
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.upload.view
 * @createTime 创建时间 ：17/12/18
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/18
 * @modifyMemo 修改备注：
 */

public class InspectionWellMonitorEditOrDetailActivity extends BaseActivity {
    private MultiTakePhotoTableItem take_photo_item;
    private TextItemTableItem1 textItemAd;//氨氮浓度
    private TextItemTableItem1 textItemCod;//cod浓度
    private TextItemTableItem1 textItemUser;//上报人
    private TextFieldTableItem textItemRemarks; // 备注

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
        tittleTv.setText(orignMonitorInfo == null ? "新增监测信息" : "监测信息详情");
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
                .throttleFirst(2, TimeUnit.SECONDS)   //2秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (!checkParam()) return;
                        if (progressDialog == null) {
                            progressDialog = new ProgressDialog(InspectionWellMonitorEditOrDetailActivity.this);
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
                                    ToastUtil.longToast(InspectionWellMonitorEditOrDetailActivity.this, "网络忙，请稍等");
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
                                        ToastUtil.shortToast(InspectionWellMonitorEditOrDetailActivity.this, "提交失败");
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
                                            ToastUtil.shortToast(InspectionWellMonitorEditOrDetailActivity.this, "提交成功");
                                            EventBus.getDefault().post(new RefreshMyUploadList());
                                            EventBus.getDefault().post(new WellMonitorRefreshEvent(2));
                                            finish();
                                        } else {
                                            ToastUtil.shortToast(InspectionWellMonitorEditOrDetailActivity.this, "保存失败，原因是：" + responseBody.getMessage());
                                        }
                                    }
                                });
                    }
                });

        RxView.clicks(btn_edit)
                .throttleFirst(2, TimeUnit.SECONDS)   //2秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        tittleTv.setText("编辑监测信息");
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
        //设置输入内容只能是数字，小数
        textItemAd.getEt_right().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        textItemCod.getEt_right().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //限制输入位数：整数4位，小数点后两位
        textItemAd.getEt_right().addTextChangedListener(new DecimalInputTextWatcher(textItemAd.getEt_right(), 4, 2));
        textItemCod.getEt_right().addTextChangedListener(new DecimalInputTextWatcher(textItemCod.getEt_right(), 4, 2));


    }

    //选择时间
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
                .setTitle("设置时间")
                .setView(timepickerview)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                btn_time.setText(wheelMain.getTime());
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
    }

    //日期转化为时间戳
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    private void showDeleteDialog() {
        DialogUtil.MessageBox(this, "提醒", "是否确定要删除？", new DialogInterface.OnClickListener() {
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
            progressDialog.setMessage("正在删除，请等待");
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
                        ToastUtil.shortToast(InspectionWellMonitorEditOrDetailActivity.this, "删除失败");
                        CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
                                BaseInfoManager.getUserName(InspectionWellMonitorEditOrDetailActivity.this) + "原因是：" + e.getLocalizedMessage()));
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (responseBody.getCode() == 200) {
                            ToastUtil.shortToast(InspectionWellMonitorEditOrDetailActivity.this, "删除成功");
                            EventBus.getDefault().post(new RefreshMyUploadList());
                            EventBus.getDefault().post(new WellMonitorRefreshEvent(1));
                            finish();
                        } else {
                            ToastUtil.shortToast(InspectionWellMonitorEditOrDetailActivity.this, responseBody.getMessage());
                            CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
                                    BaseInfoManager.getUserName(InspectionWellMonitorEditOrDetailActivity.this) + "原因是：" + responseBody.getMessage()));
                        }
                    }
                });
    }

    /**
     * 删除图片的id
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
//            ToastUtil.shortToast(this, "请按“拍照须知”要求至少提供一张照片！");
//            return false;
//        }

        if (TextUtils.isEmpty(textItemAd.getText())) {
            ToastUtil.shortToast(this, "请填写氨氮浓度");
            return false;
        }
        if (!NumberUtil.isNumber(textItemAd.getText())) {
            ToastUtil.shortToast(this, "氨氮浓度请填写数字");
            return false;
        }

        if (!TextUtils.isEmpty(textItemCod.getText()) && !NumberUtil.isNumber(textItemCod.getText())) {
            ToastUtil.shortToast(this, "Cod浓度请填写数字");
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


    //反显数据
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
            // 解决\n无法正常换行的问题
            textItemRemarks.setText(inspectionWellMonitorInfo.getDescription().replaceAll("(\\n)|(＼n)", "\n"));
        }
    }

    //编辑与不可编辑模式
    private void setReadOnly(boolean isReadOnly) {
        if (isReadOnly) {

            take_photo_item.setReadOnly();
            take_photo_item.setRequired(false);
            take_photo_item.setTitle("关键节点图片");
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
            take_photo_item.setTitle("添加照片");
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

    //时间戳转化为日期
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
     * 跳转到当前页面
     */
    public static void jump(Context context, InspectionWellMonitorInfo info) {
        context.startActivity(getIntent(context, info, null, null, null, null, null, false));
    }

    /**
     * 跳转到这个Activity
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
     * 从Intent中获取Key对应Value字符串
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
     * 从Intent中获取Key对应getSerializable对象
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
     * 从Intent中获取Key对应getSerializable对象
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
