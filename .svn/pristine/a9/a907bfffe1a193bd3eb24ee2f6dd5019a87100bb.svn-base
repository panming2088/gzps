package com.augurit.agmobile.gzps.uploadevent.view.eventflow;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.uploadevent.event.UpdateJournalEvent;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.patrolcore.common.util.MaxLengthInputFilter;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.searchview.util.adapter.TextWatcherAdapter;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jakewharton.rxbinding.view.RxView;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.journal
 * @createTime 创建时间 ：17/11/4
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/4
 * @modifyMemo 修改备注：
 */

public class AddEventJournalActivity extends BaseActivity {

    private String taskInstDbid;
    private String sjid;
    private EditText et_content;
    private View btn_upload_journal;
    private MultiTakePhotoTableItem take_photo_item;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_journal);
        taskInstDbid = getIntent().getStringExtra("taskInstDbid");
        sjid = getIntent().getStringExtra("sjid");
        initView();
    }

    protected void initView() {
        take_photo_item = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_item);
        take_photo_item.setPhotoExampleEnable(false);
        ((TextView) findViewById(R.id.tv_title)).setText("填写施工日志");
        et_content = (EditText) findViewById(R.id.textfield_content);
        final TextView tv_size = (TextView) findViewById(R.id.tv_size);
        final int maxTotal = 200;
        et_content.setFilters(new InputFilter[]{new MaxLengthInputFilter(maxTotal,
                null, et_content, "长度不能超过" + maxTotal + "个字").setDismissErrorDelay(1500)});
        et_content.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                try {
                    String inputText = s.toString();
                    if (TextUtils.isEmpty(inputText)) {
                        tv_size.setText("0/" + maxTotal);
                        return;
                    }
                    tv_size.setText(inputText.getBytes("GB2312").length / 2 + "/" + maxTotal);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                        if(ListUtil.isEmpty(take_photo_item.getSelectedPhotos())){
                            ToastUtil.shortToast(AddEventJournalActivity.this, "请至少上传一张照片");
                            return;
                        }
                        if(StringUtil.isEmpty(et_content.getText())){
                            ToastUtil.shortToast(AddEventJournalActivity.this, "请填写日志内容");
                            return;
                        }
                        final ProgressDialog progressDialog = new ProgressDialog(AddEventJournalActivity.this);
                        progressDialog.setMessage("正在提交，请等待");
                        progressDialog.show();

                        List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();
                        UploadEventService eventService = new UploadEventService(AddEventJournalActivity.this.getApplicationContext());
                        eventService.uploadJournal(et_content.getText().toString(), selectedPhotos, sjid)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Boolean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        progressDialog.dismiss();
                                        ToastUtil.shortToast(AddEventJournalActivity.this, "操作失败，请重试！");
                                    }

                                    @Override
                                    public void onNext(Boolean s) {
                                        progressDialog.dismiss();
                                        if(s){
                                            ToastUtil.shortToast(AddEventJournalActivity.this, "操作成功");
                                            EventBus.getDefault().post(new UpdateJournalEvent());
                                            finish();
                                        } else {
                                            ToastUtil.shortToast(AddEventJournalActivity.this, "操作失败，请重试！");
                                        }

                                    }
                                });
                    }
                });
    }


    @Override
    public void onBackPressed(){
        DialogUtil.MessageBox(AddEventJournalActivity.this, "提示", "是否确定放弃本次编辑？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 主要用于拍照、打开照片、地图浏览等返回Activity的刷新操作
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {

        if (take_photo_item != null) {
            take_photo_item.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
