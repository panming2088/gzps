package com.augurit.agmobile.gzps.monitor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.util.NumberUtil;
import com.augurit.agmobile.gzps.jbjpsdy.WellMonitorRefreshEvent;
import com.augurit.agmobile.gzps.monitor.model.WellMonitorInfo;
import com.augurit.agmobile.gzps.monitor.service.WellMonitorService;
import com.augurit.agmobile.gzps.monitor.view.TextItemTableItem1;
import com.augurit.agmobile.gzps.monitor.view.time.ScreenInfo;
import com.augurit.agmobile.gzps.monitor.view.time.WheelMain;
import com.augurit.agmobile.gzps.uploadfacility.service.UploadFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility.RefreshMyUploadList;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.loc.util.LocationUtil;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.RadioGroupUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.crashreport.CrashReport;
import org.greenrobot.eventbus.EventBus;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 接驳井监测信息编辑和提交
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.upload.view
 * @createTime 创建时间 ：17/12/18
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/18
 * @modifyMemo 修改备注：
 */

public class WellMonitorActivity extends BaseActivity {
    private MultiTakePhotoTableItem take_photo_item;
    private TextItemTableItem1 textItemGj;//管径
    private TextItemTableItem1 textItemAd;//氨氮浓度
    private TextItemTableItem1 textItemCod;//cod浓度
    private TextItemTableItem1 textItemUser;//上报人
    private TextItemTableItem1 textItemType;//接驳井类型
    private TextItemTableItem1 textItemWsll;//污水流量

    private RadioGroup rgWater,rgBlock;
    private RadioButton rbWaterYes,rbWaterNo;
    private RadioButton rbBlockYes,rbBlockNo;
    private Button btn_edit,btn_upload,btn_delete;
    private WellMonitorInfo orignMonitorInfo;
    private WellMonitorInfo finalMonitorInfo;
    private View view_water;//污水流量布局
    private TextView tag_block,tag_water;
    private CountDownTimer countDownTimer;
    private ProgressDialog progressDialog;
    private WellMonitorService wellMonitorService;
    private String usid;//接驳井usid
    private String jbjObjectId;//接驳井objectid
    private  TextView tittleTv;
    private Button btn_time;
    private boolean isFromMap;
    private Calendar cal;
    private List<Photo> selectedPhotos;
    boolean isRain;//是否是雨水井
    private WindowManager.LayoutParams params;
    private String type,X,Y,gj;
    WheelMain wheelMain;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_well_monitor);
        orignMonitorInfo = getIntent().getParcelableExtra("WellMonitorInfo");
        initData();
        initView();
    }

    private void initData() {
        wellMonitorService = new WellMonitorService(this);
        orignMonitorInfo = (WellMonitorInfo) getIntent().getSerializableExtra("WellMonitorInfo");
        usid  = getIntent().getStringExtra("usid");
        jbjObjectId  = getIntent().getStringExtra("objectid");
        type  = getIntent().getStringExtra("type");
        X  = getIntent().getStringExtra("X");
        Y  = getIntent().getStringExtra("Y");
        gj  = getIntent().getStringExtra("gj");
        isFromMap  = getIntent().getBooleanExtra("isFromMap",false);
    }

    private void initView() {
        if(finalMonitorInfo == null){
            finalMonitorInfo = new WellMonitorInfo();
        }
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WellMonitorActivity.this.onBackPressed();
            }
        });
        tittleTv =  ((TextView)findViewById(R.id.tv_title));
        tittleTv.setText(orignMonitorInfo == null?"新增监测信息":"监测信息详情");
        take_photo_item = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_item);

        textItemGj = (TextItemTableItem1) findViewById(R.id.tableitem_diameter);
        textItemAd = (TextItemTableItem1) findViewById(R.id.tableitem_ad);
        textItemCod = (TextItemTableItem1) findViewById(R.id.tableitem_cod);
        textItemType = (TextItemTableItem1) findViewById(R.id.tableitem_type);
        textItemUser = (TextItemTableItem1) findViewById(R.id.tableitem_current_user);
        btn_time = (Button) findViewById(R.id.btn_time);
        textItemWsll = (TextItemTableItem1) findViewById(R.id.tableitem_wsll);
        tag_block = (TextView) findViewById(R.id.tag_block);
        tag_water = (TextView) findViewById(R.id.tag_water);

        setEditInputMode();
        rgWater = (RadioGroup) findViewById(R.id.rg_water);
        rgBlock = (RadioGroup) findViewById(R.id.rg_block);
        rbWaterYes = (RadioButton) findViewById(R.id.rb_water_yes);
        rbWaterNo = (RadioButton) findViewById(R.id.rb_water_no);
        rbBlockYes = (RadioButton) findViewById(R.id.rb_block_yes);
        rbBlockNo = (RadioButton) findViewById(R.id.rb_block_no);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_delete= (Button) findViewById(R.id.btn_delete);
        view_water = findViewById(R.id.ll_water);

        isRain = !TextUtils.isEmpty(type) && type.indexOf("雨水")!=-1;

        if(isRain){
           textItemWsll.setVisibility(isRain ? View.GONE :View.VISIBLE);
           view_water.setVisibility(isRain ? View.VISIBLE:View.GONE);
        }else{
            view_water.setVisibility(View.GONE);
            textItemWsll.setVisibility(View.VISIBLE);
        }
        if(orignMonitorInfo !=null){
            fillUI(orignMonitorInfo);
            if(isFromMap){
                setReadOnly(false);
                btn_edit.setVisibility(View.GONE);
                btn_upload.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.VISIBLE);
            }else{
                setReadOnly(true);
            }
        }else{
            if(!TextUtils.isEmpty(gj)){
                textItemGj.setText(gj);
            }
            setReadOnly(false);
            Calendar calendar = Calendar.getInstance();
            btn_time.setText(calendar.get(Calendar.YEAR) + "-"
                    + ((calendar.get(Calendar.MONTH) + 1) >9?(calendar.get(Calendar.MONTH) + 1):"0"+(calendar.get(Calendar.MONTH) + 1))+ "-"
                    + (calendar.get(Calendar.DAY_OF_MONTH)>9?calendar.get(Calendar.DAY_OF_MONTH):"0"+calendar.get(Calendar.DAY_OF_MONTH)) + " "
                    + (calendar.get(Calendar.HOUR_OF_DAY)>9?calendar.get(Calendar.HOUR_OF_DAY):"0"+calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                    + (calendar.get(Calendar.MINUTE)>9?calendar.get(Calendar.MINUTE):"0"+calendar.get(Calendar.MINUTE))
            );
        }
        textItemType.setText(type);
        textItemType.setReadOnly();

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
                        if(!checkParam()) return;
                        if (progressDialog == null) {
                            progressDialog = new ProgressDialog(WellMonitorActivity.this);
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
                                    ToastUtil.longToast(WellMonitorActivity.this, "网络忙，请稍等");
                                }
                            }

                            @Override
                            public void onFinish() {}
                        };

                        if (countDownTimer != null) {
                            countDownTimer.start();
                        }
                        Observable<ResponseBody> observable = null;
                        List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();
                        List<Photo> selectedThumbnail = take_photo_item.getThumbnailPhotos();
                        if(null != orignMonitorInfo){
                            finalMonitorInfo.setDeleteAttachment(getDeletePhotoIds());
                            List<Photo> addPhotos = new ArrayList<>();
                            List<Photo> addThumbnails = new ArrayList<>();

                            for(Photo photo :selectedPhotos){
                                if(photo.getId() == 0){
                                    addPhotos.add(photo);
                                }
                            }
                            for(Photo photo :selectedThumbnail){
                                if(photo.getId() == 0){
                                    addThumbnails.add(photo);
                                }
                            }
                            finalMonitorInfo.setPhotos(addPhotos);
                            finalMonitorInfo.setThumbnailPhotos(addThumbnails);
                            observable = wellMonitorService.addWellMonitor(finalMonitorInfo);
                        }else {
                            finalMonitorInfo.setPhotos(selectedPhotos);
                            finalMonitorInfo.setThumbnailPhotos(selectedThumbnail);
                            observable =   wellMonitorService.addWellMonitor(finalMonitorInfo);
                        }
                        observable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<ResponseBody>() {
                                    @Override
                                    public void onCompleted() {}
                                    @Override
                                    public void onError(Throwable e) {
                                        if (progressDialog != null && progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        if (countDownTimer != null) {
                                            countDownTimer.cancel();
                                            countDownTimer = null;
                                        }
                                        ToastUtil.shortToast(WellMonitorActivity.this, "提交失败");
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
                                            ToastUtil.shortToast(WellMonitorActivity.this, "提交成功");
                                            EventBus.getDefault().post(new RefreshMyUploadList());
                                            EventBus.getDefault().post(new WellMonitorRefreshEvent(2));
                                            finish();
                                        } else {
                                            ToastUtil.shortToast(WellMonitorActivity.this, "保存失败，原因是：" + responseBody.getMessage());
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
        textItemGj.getEt_right().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        textItemAd.getEt_right().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        textItemCod.getEt_right().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        textItemWsll.getEt_right().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //限制输入位数：整数4位，小数点后两位
        textItemGj.getEt_right().addTextChangedListener(new DecimalInputTextWatcher(textItemGj.getEt_right(), 4, 2));
        textItemAd.getEt_right().addTextChangedListener(new DecimalInputTextWatcher(textItemAd.getEt_right(), 4, 2));
        textItemCod.getEt_right().addTextChangedListener(new DecimalInputTextWatcher(textItemCod.getEt_right(), 4, 2));
        textItemWsll.getEt_right().addTextChangedListener(new DecimalInputTextWatcher(textItemWsll.getEt_right(), 4, 2));


    }

    //选择时间
    private void selectTime() {
        LayoutInflater inflater = LayoutInflater
                .from(WellMonitorActivity.this);
        final View timepickerview = inflater.inflate(
                R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(WellMonitorActivity.this);
        wheelMain = new WheelMain(timepickerview, true);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
//        String time = btn_time.getText().toString();
//        if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
//            try {
//                calendar.setTime(dateFormat.parse(time));
//            } catch (ParseException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int h = calendar.getTime().getHours();
        int m = calendar.getTime().getMinutes();
        wheelMain.initDateTimePicker(year, month, day, h, m);
        new AlertDialog.Builder(WellMonitorActivity.this)
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
    public static String dateToStamp(String s) throws ParseException{
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
        wellMonitorService.deleteWellMonitor(orignMonitorInfo.getId())
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
                        ToastUtil.shortToast(WellMonitorActivity.this,"删除失败");
                        CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
                                BaseInfoManager.getUserName(WellMonitorActivity.this) + "原因是：" + e.getLocalizedMessage()));
                    }
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (responseBody.getCode() == 200) {
                            ToastUtil.shortToast(WellMonitorActivity.this,"删除成功");
                            EventBus.getDefault().post(new RefreshMyUploadList());
                            EventBus.getDefault().post(new WellMonitorRefreshEvent(1));
                            finish();
                        } else {
                            ToastUtil.shortToast(WellMonitorActivity.this,responseBody.getMessage());
                            CrashReport.postCatchedException(new Exception("删除失败，上报用户是：" +
                                    BaseInfoManager.getUserName(WellMonitorActivity.this) + "原因是：" + responseBody.getMessage()));
                        }
                    }
                });
    }
    /**
     * 删除图片的id
     * @return
     */
    private String getDeletePhotoIds() {
        StringBuffer sb = new StringBuffer();
        List<Photo> deletedPhotos = take_photo_item.getDeletedPhotos();
        if(!ListUtil.isEmpty(deletedPhotos)) {
            for (Photo photo : deletedPhotos) {
                sb.append(photo.getId()).append(",");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }
    private boolean checkParam() {
        selectedPhotos = take_photo_item.getSelectedPhotos();
        if (ListUtil.isEmpty(selectedPhotos) || selectedPhotos.size() < 1) {
            ToastUtil.shortToast(this,"请按“拍照须知”要求至少提供一张照片！");
            return false;
        }

        if(TextUtils.isEmpty(textItemGj.getText())){
                ToastUtil.shortToast(this,"请填写管径");
                return false;
            }
            if(!NumberUtil.isNumber(textItemGj.getText())){
                ToastUtil.shortToast(this,"管径请填写数字");
                return false;
            }
        if(TextUtils.isEmpty(textItemAd.getText())){
            ToastUtil.shortToast(this,"请填写氨氮浓度");
            return false;
        }
       if(!NumberUtil.isNumber(textItemAd.getText())){
           ToastUtil.shortToast(this,"氨氮浓度请填写数字");
           return false;
       }

        if(!TextUtils.isEmpty(textItemCod.getText()) && !NumberUtil.isNumber(textItemCod.getText())){
            ToastUtil.shortToast(this,"Cod浓度请填写数字");
            return false;
        }
        if(!isRain){
            if(TextUtils.isEmpty(textItemWsll.getText())){
                ToastUtil.shortToast(this,"请填写日污水流量");
                return false;
            }
            if(!NumberUtil.isNumber(textItemWsll.getText())){
                ToastUtil.shortToast(this,"日污水流量请填写数字");
                return false;
            }
        }
        finalMonitorInfo.setAd(textItemAd.getText());
        if(isRain){
            finalMonitorInfo.setQtsfys(rgWater.getCheckedRadioButtonId() == R.id.rb_water_yes?"1":"0");
        }else{
            finalMonitorInfo.setRwsll(textItemWsll.getText().toString());
        }
        finalMonitorInfo.setSfds(rgBlock.getCheckedRadioButtonId() == R.id.rb_block_yes?"1":"0");
        finalMonitorInfo.setGdgj(textItemGj.getText());
        finalMonitorInfo.setAd(textItemAd.getText());
        if(!TextUtils.isEmpty(textItemCod.getText())){
            finalMonitorInfo.setCod(textItemCod.getText().toString());
        }

        if(orignMonitorInfo!=null){
            finalMonitorInfo.setUsid(orignMonitorInfo.getUsid());
            finalMonitorInfo.setJbjObjectId(orignMonitorInfo.getJbjObjectId());
            finalMonitorInfo.setId(orignMonitorInfo.getId());
        }else{
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
        if(!StringUtil.isEmpty(X) && !"null".equals(X)) {
            finalMonitorInfo.setJbjX(Double.valueOf(X));
        }
        if(!StringUtil.isEmpty(Y) && !"null".equals(Y)) {
            finalMonitorInfo.setJbjY(Double.valueOf(Y));
        }
        return true;
    }


    //反显数据
    private void fillUI(WellMonitorInfo wellMonitorInfo) {
        take_photo_item.setSelectedPhotos(wellMonitorInfo.getPhotos());
        ((RadioButton)rgWater.getChildAt("1".equals(wellMonitorInfo.getQtsfys())?0:1)).setChecked(true);
        ((RadioButton)rgBlock.getChildAt("1".equals(wellMonitorInfo.getSfds())?0:1)).setChecked(true);
        textItemGj.setText(wellMonitorInfo.getGdgj()+"");
        textItemAd.setText(wellMonitorInfo.getAd()+"");
        if(!TextUtils.isEmpty(wellMonitorInfo.getCod()) && !TextUtils.isEmpty(wellMonitorInfo.getCod().trim())){
            textItemCod.setText(wellMonitorInfo.getCod());
        }

        textItemWsll.setText(wellMonitorInfo.getRwsll()+"");
        textItemType.setText(wellMonitorInfo.getJbjType());
        if(wellMonitorInfo.getJcsj()!=null && wellMonitorInfo.getJcsj()>0){
                btn_time.setText(stampToDate(wellMonitorInfo.getJcsj()+""));
        }
        if (wellMonitorInfo.getJcsj()!=null && wellMonitorInfo.getJcsj()!= 0) {
            textItemUser.setText(TimeUtil.getStringTimeMdHmChines(new Date(Long.valueOf(wellMonitorInfo.getJcsj()))));
          }
    }

    //编辑与不可编辑模式
    private void setReadOnly(boolean isReadOnly) {
        if(isReadOnly){

            take_photo_item.setReadOnly();
            take_photo_item.setRequired(false);
            take_photo_item.setTitle("接驳井图片");
            take_photo_item.setImageIsShow(false);
            take_photo_item.setPhotoNumShow(false, 9);
            textItemGj.setReadOnly();
            textItemAd.setReadOnly();
            textItemCod.setReadOnly();
            tag_block.setVisibility(View.GONE);
            tag_water.setVisibility(View.GONE);

            textItemUser.setReadOnly();
            btn_time.setEnabled(false);
            textItemWsll.setReadOnly();
            RadioGroupUtil.disableRadioGroup(rgWater);
            RadioGroupUtil.disableRadioGroup(rgBlock);

            btn_edit.setVisibility(View.VISIBLE);
            btn_upload.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
        }else{
            take_photo_item.setEditable();
            take_photo_item.setRequired(true);
            take_photo_item.setImageIsShow(take_photo_item.getSelectedPhotos().size()>=9?false:true);
            take_photo_item.setTitle("添加照片(至少1张)");
            take_photo_item.setPhotoNumShow(true, 9);

            textItemGj.setEditable(true);
            textItemAd.setEditable(true);

            tag_block.setVisibility(View.VISIBLE);
            tag_water.setVisibility(View.VISIBLE);
            textItemGj.setRequireTag();
            textItemAd.setRequireTag();
            textItemWsll.setRequireTag();

            textItemCod.setEditable(true);
            textItemUser.setEditable(true);
            btn_time.setEnabled(true);
            textItemWsll.setEditable(true);
            RadioGroupUtil.enableRadioGroup(rgWater);
            RadioGroupUtil.enableRadioGroup(rgBlock);

            btn_upload.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(null == orignMonitorInfo ? View.GONE : View.VISIBLE);
            btn_edit.setVisibility(View.GONE);
        }
    }

    //时间戳转化为日期
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long lt = new Long(s);
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
            LocationUtil.unregister(WellMonitorActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        if(btn_edit.getVisibility() == View.GONE){
            showAlertDialog();
        }else{
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
}
