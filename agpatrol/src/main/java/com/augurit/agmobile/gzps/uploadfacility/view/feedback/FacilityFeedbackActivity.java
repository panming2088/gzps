package com.augurit.agmobile.gzps.uploadfacility.view.feedback;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.adapter.FacilityFeedBackAdapter;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.widget.MyGridView;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.uploadfacility.model.FeedbackInfo;
import com.augurit.agmobile.gzps.uploadfacility.service.FeedbackFacilityService;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lsh on 2018/3/6.
 */

public class FacilityFeedbackActivity extends BaseActivity {

    private View btn_edit;
    private View btn_delete;
    private MultiTakePhotoTableItem take_photo_item;
    //  private SpinnerTableItem spinner_situation;
    private TextFieldTableItem textitem_describe;
    private TextItemTableItem tableitem_current_user;
    private Button btn_upload;
    private MyGridView gv_facilitytype;
    private List<String> facility_type_list;
    private LinearLayout ll_feedback_time;
    FacilityFeedBackAdapter mFacilityFeedBackAdapter;
    private TextItemTableItem tv_upload_time;

    private long id = -1;
    private FeedbackInfo mFeedbackInfo;
    private FeedbackFacilityService mFeedbackFacilityService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_feedback);
        mFeedbackInfo = new FeedbackInfo();
        mFeedbackInfo.setAid(getIntent().getLongExtra("id", -1));
        mFeedbackInfo.setTableType(getIntent().getStringExtra("tableType"));
        mFeedbackInfo.setObjectId(getIntent().getStringExtra("objectid"));

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("数据反馈");

        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);

        gv_facilitytype = (MyGridView) findViewById(R.id.gv_facilitytype);
        tv_upload_time = (TextItemTableItem) findViewById(R.id.tv_upload_time);

        take_photo_item = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_item);
        take_photo_item.setRequired(true);
        textitem_describe = (TextFieldTableItem) findViewById(R.id.textitem_describe);
        tableitem_current_user = (TextItemTableItem) findViewById(R.id.tableitem_current_user);
        ll_feedback_time = (LinearLayout) findViewById(R.id.ll_feedback_time);
        btn_upload = (Button) findViewById(R.id.btn_upload);

        tableitem_current_user.setEditable(false);

        facility_type_list = new ArrayList<>();
        facility_type_list.add("正在整改");
        facility_type_list.add("已完成整改");
        facility_type_list.add("无需整改");


        mFacilityFeedBackAdapter = new FacilityFeedBackAdapter(this, facility_type_list);
        gv_facilitytype.setAdapter(mFacilityFeedBackAdapter);


        String userName = new LoginRouter(FacilityFeedbackActivity.this, AMDatabase.getInstance()).getUser().getUserName();
        tableitem_current_user.setText(userName);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                take_photo_item.setAddPhotoEnable(true);
                take_photo_item.setImageIsShow(true);
                mFacilityFeedBackAdapter.setCheck(true);
                textitem_describe.setEnableEdit(true);
                btn_upload.setVisibility(View.VISIBLE);
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.MessageBox(FacilityFeedbackActivity.this, "提示", "确定要删除该记录吗？",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mFeedbackFacilityService == null) {
                                    mFeedbackFacilityService = new FeedbackFacilityService(FacilityFeedbackActivity.this.getApplicationContext());
                                }
                                mFeedbackFacilityService.delete(mFeedbackInfo.getId() + "")
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<ResponseBody>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                ToastUtil.shortToast(FacilityFeedbackActivity.this, "删除失败，请重试！");
                                            }

                                            @Override
                                            public void onNext(ResponseBody responseBody) {
                                                ToastUtil.shortToast(FacilityFeedbackActivity.this, "删除成功！");
                                                finish();
                                            }
                                        });
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Photo> photoList = take_photo_item.getSelectedPhotos();
                if (ListUtil.isEmpty(photoList)
                        || photoList.size() < 1) {
                    ToastUtil.shortToast(FacilityFeedbackActivity.this, "请至少增加1张现场照片！");
                    return;
                }

                if (StringUtil.isEmpty(mFacilityFeedBackAdapter.getSelectString())) {
                    ToastUtil.shortToast(FacilityFeedbackActivity.this, "请选择整改完成情况！");
                    return;
                }

                /*if (StringUtil.isEmpty(textitem_describe.getText())) {
                    ToastUtil.shortToast(FacilityFeedbackActivity.this, "反馈说明不能为空！");
                    return;
                }*/



                mFeedbackInfo.setFiles(take_photo_item.getSelectedPhotos());
                mFeedbackInfo.setThumbPhoto(take_photo_item.getThumbnailPhotos());
                mFeedbackInfo.setDescribe(textitem_describe.getText());

                mFeedbackInfo.setSituation(mFacilityFeedBackAdapter.getSelectString());


                if (mFeedbackFacilityService == null) {
                    mFeedbackFacilityService = new FeedbackFacilityService(FacilityFeedbackActivity.this.getApplicationContext());
                }
                final ProgressDialog pd = new ProgressDialog(FacilityFeedbackActivity.this);
                pd.setMessage("提交中...");
                pd.show();
                if(id == -1) {
                    mFeedbackFacilityService.save(mFeedbackInfo)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<ResponseBody>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    pd.dismiss();
                                    ToastUtil.shortToast(FacilityFeedbackActivity.this, "提交失败，请重试！");
                                }

                                @Override
                                public void onNext(ResponseBody responseBody) {
                                    pd.dismiss();
                                    try {
                                        String result = responseBody.string();
                                        if (result == null) {
                                            ToastUtil.shortToast(FacilityFeedbackActivity.this, "提交失败，请重试！");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        ToastUtil.shortToast(FacilityFeedbackActivity.this, "提交失败，请重试！");
                                    }
                                    ToastUtil.shortToast(FacilityFeedbackActivity.this, "提交成功");
                                    finish();
                                }
                            });
                } else {
                    List<Photo> deletedPhotos = take_photo_item.getDeletedPhotos();
                    String deletePicId = "";
                    if(!ListUtil.isEmpty(deletedPhotos)){
                        for(Photo photo : deletedPhotos){
                            deletePicId = deletePicId + "," + photo.getId();
                        }
                        deletePicId = deletePicId.substring(1);
                    }
                    mFeedbackFacilityService.update(mFeedbackInfo, deletePicId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<ResponseBody>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    pd.dismiss();
                                    ToastUtil.shortToast(FacilityFeedbackActivity.this, "提交失败，请重试！");
                                }

                                @Override
                                public void onNext(ResponseBody responseBody) {
                                    pd.dismiss();
                                    try {
                                        String result = responseBody.string();
                                        if (result == null) {
                                            ToastUtil.shortToast(FacilityFeedbackActivity.this, "提交失败，请重试！");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        ToastUtil.shortToast(FacilityFeedbackActivity.this, "提交失败，请重试！");
                                    }
                                    ToastUtil.shortToast(FacilityFeedbackActivity.this, "提交成功");
                                    finish();
                                }
                            });
                }
            }
        });

        initIntentData();
    }

    private void initIntentData() {
        Intent intent = getIntent();
        ArrayList<Photo> bitmaplist = (ArrayList<Photo>) intent.getSerializableExtra("BITMAPLIST");
        FeedbackInfo feedbackInfo = intent.getParcelableExtra("feedbackInfo");
        if(feedbackInfo != null){
            id = feedbackInfo.getId();
            mFeedbackInfo = feedbackInfo;
            btn_edit.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(View.VISIBLE);
        } else {
            btn_edit.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
        }
        if (bitmaplist != null && bitmaplist.size() != 0) {
            take_photo_item.setSelectedPhotos(bitmaplist);
            take_photo_item.setOnClickListener(null);
            btn_upload.setVisibility(View.GONE);//隐藏提交按钮
            take_photo_item.setReadOnly();
            take_photo_item.setTitle("现场照片");
            take_photo_item.setImageIsShow(false);
        }
        if (feedbackInfo != null) {
            if (!TextUtils.isEmpty(feedbackInfo.getSituation())) {
                mFacilityFeedBackAdapter.setCheck(false);
                mFacilityFeedBackAdapter.setSelectItem(feedbackInfo.getSituation());
            }
            if (!TextUtils.isEmpty(feedbackInfo.getDescribe())) {
                textitem_describe.setText(feedbackInfo.getDescribe());
                textitem_describe.setEnableEdit(false);
                textitem_describe.setTvSizeVisible(View.GONE);
            }
            if (!TextUtils.isEmpty(TimeUtil.getStringTimeYMDMChines(new Date(feedbackInfo.getTime())))) {
                ll_feedback_time.setVisibility(View.VISIBLE);
                tv_upload_time.setEditable(false);
                tv_upload_time.setText(TimeUtil.getStringTimeYMDMChines(new Date(feedbackInfo.getTime())));
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        take_photo_item.onActivityResult(requestCode, resultCode, data);
    }
}
