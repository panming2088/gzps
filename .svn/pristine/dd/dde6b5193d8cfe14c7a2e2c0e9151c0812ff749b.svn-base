package com.augurit.agmobile.gzps.journal.view.pfkjournal;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.journal.service.JournalService;
import com.augurit.agmobile.gzps.journal.view.pfkjournal.video.FileUtil;
import com.augurit.agmobile.gzps.journal.view.pfkjournal.video.VideoInputDialog;
import com.augurit.agmobile.gzps.journal.view.uploadevent.JournalEventUploadActivity;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityAddressErrorModel;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.service.CorrectFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.view.UploadFacilitySuccessEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.FacilityAddressErrorView;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.RefreshMyModificationListEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.facilityprobrem.FacilityProblemView;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.baiduapi.BaiduApiService;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.common.model.LocationInfo;
import com.augurit.agmobile.patrolcore.locate.BaiduLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.model.BaiduGeocodeResult;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.loc.util.LocationUtil;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.ResourceUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.google.android.flexbox.FlexboxLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * com.augurit.agmobile.gzps.journal.view.pfkjournal
 * Created by sdb on 2018/11/6  10:17.
 * Desc：
 */

public class AddNewPfkJournalActivity extends AppCompatActivity {
    @BindView(R.id.ll_back)
    LinearLayout mLlBack;
    @BindView(R.id.take_photo_item)
    MultiTakePhotoTableItem mTakePhotoItem;
    @BindView(R.id.tv_add_video)
    Button mTvAddVideo;
    @BindView(R.id.cb_sunny)
    CheckBox mCbSunny;
    @BindView(R.id.cb_rainy)
    CheckBox mCbRainy;
    @BindView(R.id.cb_keep_flow)
    CheckBox mCbKeepFlow;
    @BindView(R.id.cb_terminal_flow)
    CheckBox mCbTerminalFlow;
    @BindView(R.id.cb_no_flow)
    CheckBox mCbNoFlow;
    @BindView(R.id.cb_hang_clear)
    CheckBox mCbHangClear;
    @BindView(R.id.cb_hang_unclear)
    CheckBox mCbHangUnclear;
    @BindView(R.id.cb_no_hang)
    CheckBox mCbNoHang;
    @BindView(R.id.cb_no_flow_measure)
    CheckBox mCbNoFlowMeasure;
    @BindView(R.id.cb_flow_measure)
    CheckBox mCbFlowMeasure;
    @BindView(R.id.cb_cubage)
    CheckBox mCbCubage;
    @BindView(R.id.cb_flow_speed)
    CheckBox mCbFlowSpeed;
    @BindView(R.id.cb_buoy)
    CheckBox mCbBuoy;
    @BindView(R.id.cb_measure_others)
    CheckBox mCbMeasureOthers;
    @BindView(R.id.ll_measure_method)
    LinearLayout mLlMeasureMethod;
    @BindView(R.id.et_flow_measure_result)
    EditText mEtFlowMeasureResult;
    @BindView(R.id.ll_flow_measure_result)
    LinearLayout mLlFlowMeasureResult;
    @BindView(R.id.cb_no_water_measure)
    CheckBox mCbNoWaterMeasure;
    @BindView(R.id.cb_water_measure)
    CheckBox mCbWaterMeasure;
    @BindView(R.id.et_nitrogen)
    EditText mEtNitrogen;
    @BindView(R.id.et_ph)
    EditText mEtPh;
    @BindView(R.id.ll_water_measure_result)
    LinearLayout mLlWaterMeasureResult;
    @BindView(R.id.text_remarks)
    TextFieldTableItem mTextRemarks;
    @BindView(R.id.tableitem_current_time)
    TextItemTableItem mTableitemCurrentTime;
    @BindView(R.id.tableitem_current_user)
    TextItemTableItem mTableitemCurrentUser;
    @BindView(R.id.btn_upload_journal)
    Button mBtnUploadJournal;
    @BindView(R.id.ll_address_container)
    LinearLayout mLlAddressContainer;
    @BindView(R.id.textitem_component_type)
    TextItemTableItem mTextitemComponentType;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.textitem_facility)
    TextItemTableItem mTextitemFacility;
    @BindView(R.id.tv_video_path)
    TextView mTvVideoPath;
    @BindView(R.id.iv_video)
    ImageView mIvVideo;
    @BindView(R.id.iv_video_close)
    ImageView mIvVideoClose;
    @BindView(R.id.ll_video)
    LinearLayout mLlVideo;
    @BindView(R.id.et_hang_num)
    EditText mEtHangNum;
    @BindView(R.id.ll_hang_num)
    LinearLayout mLlHangNum;
    @BindView(R.id.tv_reset_problems)
    Button mTvResetProblems;
    @BindView(R.id.ll_problems_container)
    FlexboxLayout mLlProblemsContainer;
    @BindView(R.id.btn_upload_event_journal)
    Button btnUploadEventJournal;

    private boolean bPermission = true;
    private final int WRITE_PERMISSION_REQ_CODE = 100;
    private FacilityAddressErrorView componentAddressErrorView;
    private Component mComponent;
    private ModifiedFacility modifiedFacility;
    private BaiduLocationManager baiduLocationManager;
    private ProgressDialog progressDialog;
    private CountDownTimer countDownTimer;
    private boolean mIsModifyFacility;
    private ModifiedFacility mdata;

    private String deleteVideoId;
    private FacilityProblemView facilityProblemView;
    private boolean isEvent = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();


        if (modifiedFacility == null) {
            modifiedFacility = new ModifiedFacility();
        }

        initOther();

        bPermission = checkPermission();


        requestLocation();

        //获取用户当前位置
        startLocate();

    }

    private void initOther() {
        User user = new LoginService(this, AMDatabase.getInstance()).getUser();
        String userName = user.getUserName();
        String userId = user.getId();

        modifiedFacility.setMarkPerson(userName);
        modifiedFacility.setMarkPersonId(userId);
        mTableitemCurrentUser.setText(userName);

        long currentTimeMillis = System.currentTimeMillis();
        modifiedFacility.setMarkTime(currentTimeMillis);
        mTableitemCurrentTime.setText(TimeUtil.getStringTimeYMDMChines(new Date(currentTimeMillis)));

        if (mComponent != null && mComponent.getGraphic() != null && mComponent.getGraphic().getAttributes() != null) {
            Map<String, Object> attributes = mComponent.getGraphic().getAttributes();
            Object o = attributes.get(ComponentFieldKeyConstant.ADDR);
            Object road = attributes.get(ComponentFieldKeyConstant.ROAD);
            if (o != null && road != null) {
                componentAddressErrorView.setAddress(o.toString(), road.toString());
            }
            String layerName = mComponent.getLayerName();
            if (!StringUtil.isEmpty(modifiedFacility.getLayerName())) {
                layerName = modifiedFacility.getLayerName();
            }

            String usid = String.valueOf(attributes.get("US_ID"));
            if (!StringUtil.isEmpty(modifiedFacility.getUsid())) {
                usid = modifiedFacility.getUsid();
            }
            if ((StringUtil.isEmpty(usid) || usid.equals("null")) && !StringUtil.isEmpty(modifiedFacility.getObjectId())) {
                usid = modifiedFacility.getObjectId();
            }
            modifiedFacility.setUsid(usid);

            modifiedFacility.setLayerName(layerName);
            String title = "";
            if (StringUtil.isEmpty(usid) || usid.contains("null")) {
                title = layerName;
            } else {
                title = StringUtil.getNotNullString(layerName, "") + "(" + StringUtil.getNotNullString(usid, "") + ")";
            }
            mTextitemFacility.setText(title);
            mTextitemFacility.setReadOnly();

            Point geometryCenter = GeometryUtil.getGeometryCenter(mComponent.getGraphic().getGeometry());
            modifiedFacility.setOriginX(geometryCenter.getX());
            modifiedFacility.setOriginY(geometryCenter.getY());

            modifiedFacility.setLayerUrl(mComponent.getLayerUrl());
        }

        initUploadListener();

    }


    private void initData() {

        mComponent = (Component) getIntent().getSerializableExtra("component");

        //再次编辑的详情数据
        mdata = getIntent().getParcelableExtra("data");

        if (mComponent != null && mComponent.getGraphic() != null) {
            modifiedFacility = getModifiedFacilityFromGraphic(mComponent.getGraphic().getAttributes(), mComponent.getGraphic().getGeometry());
        }

        if (mdata != null) {
            modifiedFacility = mdata;
        }
    }

    private void initView() {

        setContentView(R.layout.activity_add_new_pfk_journal);

        ButterKnife.bind(this);

        mTvTitle.setText("日常巡检日志上报");
        mTakePhotoItem.setPhotoExampleEnable(true);
        mTakePhotoItem.setPhotoNumShow(true, 6);
        mTakePhotoItem.setRequired(true);

        mTextitemComponentType.setReadOnly();
        mTextitemComponentType.setText("排水口");

        mTableitemCurrentTime.setReadOnly();
        mTableitemCurrentUser.setReadOnly();

        //设施地址
        if (modifiedFacility != null) {
            componentAddressErrorView = new FacilityAddressErrorView(AddNewPfkJournalActivity.this,
                    modifiedFacility);
        } else {
            componentAddressErrorView = new FacilityAddressErrorView(AddNewPfkJournalActivity.this,
                    mComponent);
        }
        mLlAddressContainer.removeAllViews();
        componentAddressErrorView.addTo(mLlAddressContainer);

        // 设施问题
        if(modifiedFacility != null) {
            facilityProblemView = new FacilityProblemView(this, mLlProblemsContainer, modifiedFacility.getChildCode());
        }else{
            facilityProblemView = new FacilityProblemView(this, mLlProblemsContainer, null);
        }


        if (mdata != null) {
            mTvTitle.setText("再次编辑");

            String title = mdata.getLayerName() + "(" + mdata.getUsid() + ")";
            mTextitemFacility.setText(title);
            mTextitemFacility.setReadOnly();

            btnUploadEventJournal.setVisibility(View.GONE);
            mBtnUploadJournal.setText("提交");

            mTakePhotoItem.setSelectedPhotos(mdata.getPhotos());
            mCbSunny.setChecked("晴天".equals(mdata.getTqzq()));
            mCbRainy.setChecked("雨天".equals(mdata.getTqzq()));

            mCbKeepFlow.setChecked("持续流水".equals(mdata.getPskpszt()));
            mCbTerminalFlow.setChecked("间歇排水".equals(mdata.getPskpszt()));
            mCbNoFlow.setChecked("不排水".equals(mdata.getPskpszt()));

//            if (mCbKeepFlow.isChecked()) {
//                mLlVideo.setVisibility(View.VISIBLE);
//                mIvVideo.setVisibility(View.VISIBLE);
//                mIvVideoClose.setVisibility(View.VISIBLE);
//                mTvVideoPath.setText(mdata.getVideoPath());
//            }

            mLlVideo.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mdata.getVideoPath())){
                mTvVideoPath.setText(mdata.getVideoPath());
                mIvVideo.setVisibility(View.VISIBLE);
                mIvVideoClose.setVisibility(View.VISIBLE);
            }else{
                mIvVideo.setVisibility(View.GONE);
                mIvVideoClose.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(mdata.getGpbh())
                    && !"有(编号不清晰)".equals(mdata.getGpbh()) && !"无".equals(mdata.getGpbh())) {
                mCbHangClear.setChecked(true);
                mLlHangNum.setVisibility(View.VISIBLE);
                mEtHangNum.setText(mdata.getGpbh());
            }
//            mCbHangClear.setChecked("有(编号清晰)".equals(mdata.getGpbh()));
            mCbHangUnclear.setChecked("有(编号不清晰)".equals(mdata.getGpbh()));
            mCbNoHang.setChecked("无".equals(mdata.getGpbh()));


            if (!TextUtils.isEmpty(mdata.getCljg()) || !TextUtils.isEmpty(mdata.getClff())) {
                mCbFlowMeasure.setChecked(true);
                mLlMeasureMethod.setVisibility(View.VISIBLE);
                mCbCubage.setChecked("容积法".equals(mdata.getClff()));
                mCbFlowSpeed.setChecked("流速法".equals(mdata.getClff()));
                mCbBuoy.setChecked("浮标法".equals(mdata.getClff()));
                mCbMeasureOthers.setChecked("其他".equals(mdata.getClff()));

                mEtFlowMeasureResult.setText(mdata.getCljg());

            } else {
                mCbNoFlowMeasure.setChecked(true);
            }

            if (!TextUtils.isEmpty(mdata.getAdnd()) || !TextUtils.isEmpty(mdata.getPh())) {
                mCbWaterMeasure.setChecked(true);
                mLlWaterMeasureResult.setVisibility(View.VISIBLE);

                mEtNitrogen.setText(mdata.getAdnd());
                mEtPh.setText(mdata.getPh());

            } else {
                mCbNoWaterMeasure.setChecked(true);
            }


            mTextRemarks.setText(mdata.getDescription());
            if(modifiedFacility != null) {
                modifiedFacility.setTqzq("");
                modifiedFacility.setPskpszt("");
                modifiedFacility.setGpbh("");
                modifiedFacility.setClff("");
                modifiedFacility.setCljg("");
                modifiedFacility.setAdnd("");
                modifiedFacility.setPh("");
            }
//            modifiedFacility.setVideoPath("");

        }

    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }

            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this,
                        permissions.toArray(new String[0]),
                        WRITE_PERMISSION_REQ_CODE);

                return false;
            }

        }

        return true;
    }

    private void showVideoDialog() {

        if (!bPermission) {
            ToastUtil.shortToast(this, "请先允许app所需要的权限");
            bPermission = checkPermission();
            return;
        }

        VideoInputDialog.show(getSupportFragmentManager(), new VideoInputDialog.RecordCallback() {
            @Override
            public void onCallback(String fileName) {
                modifiedFacility.setVideoPath(FileUtil.getCacheFilePath(fileName));
                mTvVideoPath.setText(fileName);
                mLlVideo.setVisibility(View.VISIBLE);
                mIvVideo.setVisibility(View.VISIBLE);
                mIvVideoClose.setVisibility(View.VISIBLE);

                //当点击新增视频时 给删除的视频id赋值  传给后台
                if (mdata != null) {
                    deleteVideoId = mdata.getVideoId();
                }

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_PERMISSION_REQ_CODE:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                bPermission = true;
                break;
        }
    }

    @OnCheckedChanged({R.id.cb_sunny, R.id.cb_rainy, R.id.cb_keep_flow, R.id.cb_terminal_flow,
            R.id.cb_no_flow, R.id.cb_hang_clear, R.id.cb_no_hang, R.id.cb_hang_unclear, R.id.cb_no_flow_measure
            , R.id.cb_flow_measure, R.id.cb_cubage, R.id.cb_flow_speed,
            R.id.cb_buoy, R.id.cb_measure_others, R.id.cb_no_water_measure, R.id.cb_water_measure})
    public void onViewChecked(CompoundButton view, boolean isChecked) {

        mIsModifyFacility = true;

        if (!isChecked) {
            if (view.getId() == R.id.cb_flow_measure) {
                mLlFlowMeasureResult.setVisibility(View.GONE);
                mLlMeasureMethod.setVisibility(View.GONE);
                mEtFlowMeasureResult.setText("");
            } else if (view.getId() == R.id.cb_water_measure) {
                mLlWaterMeasureResult.setVisibility(View.GONE);
                mEtNitrogen.setText("");
                mEtPh.setText("");
            } else if (view.getId() == R.id.cb_keep_flow) {
                mLlVideo.setVisibility(View.GONE);
                modifiedFacility.setVideoPath("");
                mTvVideoPath.setText("");
                mIvVideo.setVisibility(View.GONE);
                mIvVideoClose.setVisibility(View.GONE);
            }
        } else {
            switch (view.getId()) {
                case R.id.cb_sunny:
                    mCbRainy.setChecked(false);
                    break;
                case R.id.cb_rainy:
                    mCbSunny.setChecked(false);
                    break;
                case R.id.cb_keep_flow:
                    mLlVideo.setVisibility(View.VISIBLE);
                    mCbTerminalFlow.setChecked(false);
                    mCbNoFlow.setChecked(false);
                    break;
                case R.id.cb_terminal_flow:
                    mLlVideo.setVisibility(View.GONE);
                    modifiedFacility.setVideoPath("");
                    mTvVideoPath.setText("");
                    mIvVideo.setVisibility(View.GONE);
                    mIvVideoClose.setVisibility(View.GONE);
                    mCbKeepFlow.setChecked(false);
                    mCbNoFlow.setChecked(false);

                    // 给删除的视频id赋值  传给后台
                    if (mdata != null) {
                        deleteVideoId = mdata.getVideoId();
                    }

                    break;
                case R.id.cb_no_flow:
                    mLlVideo.setVisibility(View.GONE);
                    modifiedFacility.setVideoPath("");
                    mTvVideoPath.setText("");
                    mIvVideo.setVisibility(View.GONE);
                    mIvVideoClose.setVisibility(View.GONE);
                    mCbKeepFlow.setChecked(false);
                    mCbTerminalFlow.setChecked(false);


                    // 给删除的视频id赋值  传给后台
                    if (mdata != null) {
                        deleteVideoId = mdata.getVideoId();
                    }


                    break;
                case R.id.cb_hang_clear:
                    mCbNoHang.setChecked(false);
                    mCbHangUnclear.setChecked(false);
                    mLlHangNum.setVisibility(View.VISIBLE);
                    break;
                case R.id.cb_no_hang:
                    mCbHangUnclear.setChecked(false);
                    mCbHangClear.setChecked(false);
                    mLlHangNum.setVisibility(View.GONE);
                    mEtHangNum.setText("");
                    break;
                case R.id.cb_hang_unclear:
                    mCbHangClear.setChecked(false);
                    mCbNoHang.setChecked(false);
                    mLlHangNum.setVisibility(View.GONE);
                    mEtHangNum.setText("");
                    break;
                case R.id.cb_no_flow_measure:
                    mCbFlowMeasure.setChecked(false);
                    mLlFlowMeasureResult.setVisibility(View.GONE);
                    mLlMeasureMethod.setVisibility(View.GONE);
                    mCbCubage.setChecked(false);
                    mCbBuoy.setChecked(false);
                    mCbFlowSpeed.setChecked(false);
                    mCbMeasureOthers.setChecked(false);
                    mEtFlowMeasureResult.setText("");
                    break;
                case R.id.cb_flow_measure:
                    mCbNoFlowMeasure.setChecked(false);
                    mLlFlowMeasureResult.setVisibility(View.VISIBLE);
                    mLlMeasureMethod.setVisibility(View.VISIBLE);
                    break;
                case R.id.cb_cubage:
                    mCbFlowSpeed.setChecked(false);
                    mCbBuoy.setChecked(false);
                    mCbMeasureOthers.setChecked(false);
                    break;
                case R.id.cb_flow_speed:
                    mCbCubage.setChecked(false);
                    mCbBuoy.setChecked(false);
                    mCbMeasureOthers.setChecked(false);
                    break;
                case R.id.cb_buoy:
                    mCbCubage.setChecked(false);
                    mCbFlowSpeed.setChecked(false);
                    mCbMeasureOthers.setChecked(false);
                    break;
                case R.id.cb_measure_others:
                    mCbCubage.setChecked(false);
                    mCbFlowSpeed.setChecked(false);
                    mCbBuoy.setChecked(false);
                    break;
                case R.id.cb_no_water_measure:
                    mCbWaterMeasure.setChecked(false);
                    mLlWaterMeasureResult.setVisibility(View.GONE);
                    mEtNitrogen.setText("");
                    mEtPh.setText("");
                    break;
                case R.id.cb_water_measure:
                    mCbNoWaterMeasure.setChecked(false);
                    mLlWaterMeasureResult.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mTakePhotoItem != null) {
            mTakePhotoItem.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
    public void onBackPressed() {
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

    private ModifiedFacility getModifiedFacilityFromGraphic(Map<String, Object> attribute, Geometry geometry) {
        ModifiedFacility uploadedFacility = new ModifiedFacility();
        uploadedFacility.setObjectId(objectToString(attribute.get("OBJECTID")));

        uploadedFacility.setAddr(objectToString(attribute.get("ADDR")));
        uploadedFacility.setOriginAddr(objectToString(attribute.get("ADDR")));

        uploadedFacility.setRoad(objectToString(attribute.get("ROAD")));
        uploadedFacility.setOriginRoad(objectToString(attribute.get("ROAD")));

        uploadedFacility.setAttrFive(objectToString(attribute.get("ATTR_FIVE")));
        uploadedFacility.setOriginAttrFive(objectToString(attribute.get("ATTR_FIVE")));

        uploadedFacility.setAttrSix(objectToString(attribute.get("ATTR_SIX")));
        uploadedFacility.setOriginAttrSix(objectToString(attribute.get("ATTR_SIX")));

        uploadedFacility.setAttrSeven(objectToString(attribute.get("ATTR_SEVEN")));
        uploadedFacility.setOriginAttrSeven(objectToString(attribute.get("ATTR_SEVEN")));

        uploadedFacility.setAttrFour(objectToString(attribute.get("ATTR_FOUR")));
        uploadedFacility.setOriginAttrFour(objectToString(attribute.get("ATTR_FOUR")));

        uploadedFacility.setAttrThree(objectToString(attribute.get("ATTR_THREE")));
        uploadedFacility.setOriginAttrThree(objectToString(attribute.get("ATTR_THREE")));

        uploadedFacility.setAttrTwo(objectToString(attribute.get("ATTR_TWO")));
        uploadedFacility.setOriginAttrTwo(objectToString(attribute.get("ATTR_TWO")));

        uploadedFacility.setAttrOne(objectToString(attribute.get("ATTR_ONE")));
        uploadedFacility.setOriginAttrOne(objectToString(attribute.get("ATTR_ONE")));

        uploadedFacility.setParentOrgName(objectToString(attribute.get("PARENT_ORG_NAME")));
        uploadedFacility.setDirectOrgName(objectToString(attribute.get("DIRECT_ORG_NAME")));
        uploadedFacility.setTeamOrgName(objectToString(attribute.get("TEAM_ORG_NAME")));
        uploadedFacility.setSuperviseOrgName(objectToString(attribute.get("SUPERVISE_ORG_NAME")));
        uploadedFacility.setLayerName(objectToString(attribute.get("LAYER_NAME")));
        Point geometryCenter = GeometryUtil.getGeometryCenter(geometry);
        uploadedFacility.setX(geometryCenter.getX());
        uploadedFacility.setY(geometryCenter.getY());
        uploadedFacility.setOriginX(geometryCenter.getX());
        uploadedFacility.setOriginY(geometryCenter.getY());
        uploadedFacility.setCheckState(objectToString(attribute.get("CHECK_STATE")));
        uploadedFacility.setUsid(objectToString(attribute.get("US_ID")));
        //管理状态
        uploadedFacility.setCityVillage(objectToString(attribute.get("CITY_VILLAGE")));
        return uploadedFacility;
    }

    private String objectToString(Object object) {
        if (object == null) {
            return "";
        }
        return StringUtil.getNotNullString(object.toString(), "");
    }


    /**
     * 请求百度地址
     */
    private void requestLocation() {
        if (mComponent != null) {
            Point geometryCenter = GeometryUtil.getGeometryCenter(mComponent.getGraphic().getGeometry());
            BaiduApiService selectLocationService = new BaiduApiService(this);
            selectLocationService.parseLocation(new LatLng(geometryCenter.getY(), geometryCenter.getX()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BaiduGeocodeResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(BaiduGeocodeResult baiduGeocodeResult) {
                            if (componentAddressErrorView != null && baiduGeocodeResult != null) {
                                componentAddressErrorView.changeAddress(baiduGeocodeResult.getDetailAddress(), baiduGeocodeResult.getResult().getAddressComponent().getStreet());
                            }
                            if (baiduGeocodeResult != null) {
                                modifiedFacility.setAddr(baiduGeocodeResult.getDetailAddress());
                                modifiedFacility.setRoad(baiduGeocodeResult.getResult().getAddressComponent().getStreet());
                            }
                        }
                    });
        }
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
                        modifiedFacility.setUserX(lastLocation.getLocation().getLongitude());
                        modifiedFacility.setUserY(lastLocation.getLocation().getLatitude());
                    }
                    if (lastLocation.getDetailAddress() != null) {
                        modifiedFacility.setUserAddr(lastLocation.getDetailAddress().getDetailAddress());
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


    private void initUploadListener() {
        RxView.clicks(mBtnUploadJournal)
                .throttleFirst(2, TimeUnit.SECONDS)   //2秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isEvent = false;
                        upload();
                    }
                });
        RxView.clicks(btnUploadEventJournal)
                .throttleFirst(2, TimeUnit.SECONDS)   //2秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isEvent = true;
                        if (ListUtil.isEmpty(facilityProblemView.getSelectedParentAndChildICodeList())
                                || StringUtil.isEmpty(facilityProblemView.getSelectedParentAndChildICodeList().get(0))) {
                            ToastUtil.shortToast(AddNewPfkJournalActivity.this, "选择设施问题才能上报问题！");
                            return;
                        }
                        upload();
                    }
                });
    }

    private void upload() {
//        ListUtil.isEmpty(facilityProblemView.getSelectedParentAndChildICodeList())
//                || StringUtil.isEmpty(facilityProblemView.getSelectedParentAndChildICodeList().get(0))
        List<Photo> selectedPhotos = mTakePhotoItem.getSelectedPhotos();
        modifiedFacility.setPhotos(selectedPhotos);
        modifiedFacility.setThumbnailPhotos(mTakePhotoItem.getThumbnailPhotos());
        if (!componentAddressErrorView.ifAllowUpload()) {
            ToastUtil.iconShortToast(this, R.mipmap.ic_alert_yellow, componentAddressErrorView.getNotAllowUploadReason());
        }
        FacilityAddressErrorModel facilityAddressErrorModel = componentAddressErrorView.getFacilityAddressErrorModel();
        modifiedFacility.setAddr(facilityAddressErrorModel.getCorrectAddress());
        modifiedFacility.setRoad(facilityAddressErrorModel.getRoad());
        modifiedFacility.setX(facilityAddressErrorModel.getCorrectX());
        modifiedFacility.setY(facilityAddressErrorModel.getCorrectY());
        if (mComponent != null && !StringUtil.isEmpty(mComponent.getObjectId())) {
            modifiedFacility.setObjectId(String.valueOf(mComponent.getObjectId()));
        }
        modifiedFacility.setDescription(mTextRemarks.getText().trim());
        modifiedFacility.setMarkPerson(mTableitemCurrentUser.getText());
        long currentTimeMillis = System.currentTimeMillis();
        modifiedFacility.setMarkTime(currentTimeMillis);
        mTableitemCurrentTime.setText(TimeUtil.getStringTimeYMDMChines(new Date(currentTimeMillis)));


        if (mCbSunny.isChecked()) {
            modifiedFacility.setTqzq(mCbSunny.getText().toString());
        }
        if (mCbRainy.isChecked()) {
            modifiedFacility.setTqzq(mCbRainy.getText().toString());
        }
        if (mCbKeepFlow.isChecked()) {
            modifiedFacility.setPskpszt(mCbKeepFlow.getText().toString());
        }
        if (mCbTerminalFlow.isChecked()) {
            modifiedFacility.setPskpszt(mCbTerminalFlow.getText().toString());
        }
        if (mCbNoFlow.isChecked()) {
            modifiedFacility.setPskpszt(mCbNoFlow.getText().toString());
        }
        if (mCbHangClear.isChecked()) {
            modifiedFacility.setGpbh(mEtHangNum.getText().toString().trim());
        }
        if (mCbHangUnclear.isChecked()) {
            modifiedFacility.setGpbh(mCbHangUnclear.getText().toString());
        }
        if (mCbNoHang.isChecked()) {
            modifiedFacility.setGpbh(mCbNoHang.getText().toString());
        }

        if (mCbCubage.isChecked()) {
            modifiedFacility.setClff(mCbCubage.getText().toString());
        }
        if (mCbFlowSpeed.isChecked()) {
            modifiedFacility.setClff(mCbFlowSpeed.getText().toString());
        }
        if (mCbBuoy.isChecked()) {
            modifiedFacility.setClff(mCbBuoy.getText().toString());
        }
        if (mCbMeasureOthers.isChecked()) {
            modifiedFacility.setClff(mCbMeasureOthers.getText().toString());
        }

        modifiedFacility.setCljg(mEtFlowMeasureResult.getText().toString().trim());

        if (mCbNoFlowMeasure.isChecked()) {
            modifiedFacility.setClff("");
            modifiedFacility.setCljg("");
        }

        if (mCbNoWaterMeasure.isChecked()) {
            modifiedFacility.setAdnd("");
            modifiedFacility.setPh("");
        }

        if (mCbWaterMeasure.isChecked()) {
            modifiedFacility.setAdnd(mEtNitrogen.getText().toString());
            modifiedFacility.setPh(mEtPh.getText().toString());
        }


        if (!mIsModifyFacility && componentAddressErrorView.getFacilityAddressErrorModel().isHasModified()) {
            modifiedFacility.setCorrectType("位置错误");
        } else if (mIsModifyFacility && !componentAddressErrorView.getFacilityAddressErrorModel().isHasModified()) {
            modifiedFacility.setCorrectType("信息错误");
        } else if (mIsModifyFacility && componentAddressErrorView.getFacilityAddressErrorModel().isHasModified()) {
            modifiedFacility.setCorrectType("位置与信息错误");
        } else {
            modifiedFacility.setCorrectType("数据确认");
        }


        //数据必填检查
        if (ListUtil.isEmpty(selectedPhotos)) {
            ToastUtil.shortToast(this, "请按“拍照须知”要求至少提供一张照片！");
            return;
        }

        if (TextUtils.isEmpty(modifiedFacility.getTqzq())) {
            ToastUtil.shortToast(this, "请勾天气情况");
            return;
        }
//        if (TextUtils.isEmpty(modifiedFacility.getPskpszt())) {
//            ToastUtil.shortToast(this, "请勾排水口排水状态");
//            return;
//        } else {
//            if (mCbKeepFlow.isChecked() && TextUtils.isEmpty(modifiedFacility.getVideoPath())) {
//                ToastUtil.shortToast(this, "请添加视频");
//                return;
//            }
//        }

//        if (TextUtils.isEmpty(modifiedFacility.getGpbh())) {
//            ToastUtil.shortToast(this, "请勾选或填写挂牌情况");
//            return;
//        }

//        if (mCbFlowMeasure.isChecked()) {
//            if (TextUtils.isEmpty(modifiedFacility.getClff())) {
//                ToastUtil.shortToast(this, "请勾选测流方法");
//                return;
//            }
//            if (TextUtils.isEmpty(modifiedFacility.getCljg())) {
//                ToastUtil.shortToast(this, "请填写测流结果");
//                return;
//            }
//        } else {
//            if (!mCbNoFlowMeasure.isChecked()) {
//                ToastUtil.shortToast(this, "请勾选现场测流");
//                return;
//            }
//        }

//        if (mCbWaterMeasure.isChecked()) {
//            if (TextUtils.isEmpty(modifiedFacility.getAdnd())) {
//                ToastUtil.shortToast(this, "请填写氨氮浓度");
//                return;
//            }
//            if (TextUtils.isEmpty(modifiedFacility.getPh())) {
//                ToastUtil.shortToast(this, "请填写pH值");
//                return;
//            }
//        } else {
//            if (!mCbNoWaterMeasure.isChecked()) {
//                ToastUtil.shortToast(this, "请勾选现场水质监测");
//                return;
//            }
//        }
        //设施问题
        completeFacilityProblem();

        progressDialog = new ProgressDialog(AddNewPfkJournalActivity.this);
        progressDialog.setMessage("正在提交，请等待");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //20秒提示一次
        countDownTimer = new CountDownTimer(CorrectFacilityService.TIMEOUT * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                if (progressDialog != null) {
                    progressDialog.setMessage("正在提交，请等待。   " + time + "s");
                }
                if (time % 20 == 0) {
                    ToastUtil.longToast(AddNewPfkJournalActivity.this, "网络忙，请稍等");
                }
            }

            @Override
            public void onFinish() {
            }
        };

        if (countDownTimer != null) {
            countDownTimer.start();
        }

        if (mdata == null) {
            addNewPfk();
        } else {
            if (ListUtil.isEmpty(facilityProblemView.getSelectedParentAndChildICodeList())
                    || StringUtil.isEmpty(facilityProblemView.getSelectedParentAndChildICodeList().get(0))) {
                reeditPfk();
            } else {
                MessageBox(AddNewPfkJournalActivity.this, "提示", "     存在设施问题，如有对设施问题进行修改，请自行到相应的问题上报界面进行问题类型的修改。", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reeditPfk();
                    }
                }, null);
            }

        }
    }

    private void addNewPfk() {
        //数据纠错
        JournalService identificationService = new JournalService(getApplicationContext());
        identificationService.upload(modifiedFacility)
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

                        mBtnUploadJournal.setEnabled(true);
                        CrashReport.postCatchedException(new Exception("巡检上报失败，上报用户是：" +
                                BaseInfoManager.getUserName(AddNewPfkJournalActivity.this) + "原因是：" + e.getLocalizedMessage()));
                        ToastUtil.shortToast(AddNewPfkJournalActivity.this, "提交失败");
                        //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "提交失败");
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
                        mBtnUploadJournal.setEnabled(true);
                        if (responseBody.getCode() == 200) {
                            ToastUtil.shortToast(AddNewPfkJournalActivity.this, "提交成功");
                            // ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "提交成功");
                            EventBus.getDefault().post(new RefreshMyModificationListEvent());
                            EventBus.getDefault().post(new UploadFacilitySuccessEvent());

                            if (!isEvent) {
                                finish();
                            } else {
                                MessageBox(AddNewPfkJournalActivity.this, "提示", "请转到问题上报继续进行下一步操作！", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(AddNewPfkJournalActivity.this, JournalEventUploadActivity.class);
                                        intent.putExtra("modified", modifiedFacility);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, null);
                            }

                        } else {
                            CrashReport.postCatchedException(new Exception("核准上报失败，上报用户是：" +
                                    BaseInfoManager.getUserName(AddNewPfkJournalActivity.this) + "原因是：" + responseBody.getMessage()));
                            ToastUtil.shortToast(AddNewPfkJournalActivity.this, "保存失败，原因是：" + responseBody.getMessage());
                            //ToastUtil.shortToast(CorrectOrConfirimFacilityActivity.this, "保存失败，原因是：" + responseBody.getMessage());
                        }
                    }
                });
    }

    private void reeditPfk() {
        //再次编辑
        completePhotosAndVideo();

        JournalService identificationService = new JournalService(getApplicationContext());

        identificationService.editDiaryView(modifiedFacility)
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
                        ToastUtil.shortToast(AddNewPfkJournalActivity.this, "提交失败");
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
                            ToastUtil.shortToast(AddNewPfkJournalActivity.this, "提交成功");
                            EventBus.getDefault().post(new RefreshMyModificationListEvent());
                            if (modifiedFacility.deletedPhotoIds != null) {
                                modifiedFacility.deletedPhotoIds.clear();
                            }
                            EventBus.getDefault().post(new UploadFacilitySuccessEvent(modifiedFacility));
                            finish();
                        } else {

                            ToastUtil.shortToast(AddNewPfkJournalActivity.this, "保存失败，原因是：" + responseBody.getMessage());
                        }
                    }
                });
    }

    private void completeFacilityProblem() {
        //设施问题
        List<String> selectedLargeAndSmallItemList = facilityProblemView.getSelectedParentAndChildICodeList();
        //父类编码
        if (selectedLargeAndSmallItemList != null && !TextUtils.isEmpty(selectedLargeAndSmallItemList.get(0))) {
            modifiedFacility.setpCode(selectedLargeAndSmallItemList.get(0));
        } else {
            modifiedFacility.setpCode("");
        }

        //子类编码
        if (selectedLargeAndSmallItemList != null && !TextUtils.isEmpty(selectedLargeAndSmallItemList.get(1))) {
            modifiedFacility.setChildCode(selectedLargeAndSmallItemList.get(1));
        } else {
            modifiedFacility.setChildCode("");
        }
    }

    private void completePhotosAndVideo() {

        List<Photo> selectedPhotos = mTakePhotoItem.getSelectedPhotos();

        /**
         * 只有新增的图片id是0，从服务端获取到的图片id都是不为0的
         */
        if (!ListUtil.isEmpty(selectedPhotos)) {
            List<Photo> newAddedPhotos = new ArrayList<>();
            for (Photo photo : selectedPhotos) {
                if (photo.getId() == 0) {
                    newAddedPhotos.add(photo);
                }
            }
            modifiedFacility.setPhotos(newAddedPhotos);
        }

        List<Photo> deletedPhotos = mTakePhotoItem.getDeletedPhotos();
        List<String> deletedPhotoIds = new ArrayList<>();
        if (!ListUtil.isEmpty(deletedPhotos)) {
            for (Photo photo : deletedPhotos) {
                if (photo.getId() != 0) {
                    deletedPhotoIds.add(String.valueOf(photo.getId()));
                }

            }

        }

        //添加删除的视频id 拼装在图片删除id后面
        if (!TextUtils.isEmpty(deleteVideoId)) {
            deletedPhotoIds.add(deleteVideoId);
        }
        modifiedFacility.setDeletedPhotoIds(deletedPhotoIds);

    }


    @Override
    protected void onDestroy() {

        if (componentAddressErrorView != null) {
            componentAddressErrorView.destroy();
        }

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (LocationUtil.ifUnRegister()) {
            LocationUtil.unregister(this);
        }


        if (facilityProblemView != null) {
            facilityProblemView = null;
        }

        super.onDestroy();

    }

    public void MessageBox(Context context, String title, String msg,
                           DialogInterface.OnClickListener positiveListener,
                           DialogInterface.OnClickListener negetiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        if (positiveListener != null) {
            builder.setPositiveButton(
                    ResourceUtil.getStringId(context, "sure"), positiveListener);
        }
        if (negetiveListener != null) {
            builder.setNegativeButton(
                    ResourceUtil.getStringId(context, "cancel"),
                    negetiveListener);
        }
        builder.setCancelable(false);
        builder.create().setCanceledOnTouchOutside(false);
        builder.show();
    }

    @OnClick({R.id.iv_video, R.id.iv_video_close, R.id.ll_back, R.id.tv_add_video, R.id.tv_reset_problems})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_video:
                if (mdata != null && !TextUtils.isEmpty(mdata.getVideoPath())
                        && mdata.getVideoPath().contains("http") && TextUtils.isEmpty(deleteVideoId)) {
                    String extension = MimeTypeMap.getFileExtensionFromUrl(mdata.getVideoPath());
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                    Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
                    mediaIntent.setDataAndType(Uri.parse(mdata.getVideoPath()), mimeType);
                    startActivity(mediaIntent);
                    return;

                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                File file = new File((modifiedFacility.getVideoPath()));
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(AddNewPfkJournalActivity.this,
                            getApplicationContext().getPackageName() + ".FileProvider", file);
                    intent.setDataAndType(contentUri, "video/*");
                } else {
                    uri = Uri.fromFile(file);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(uri, "video/*");
                }

                startActivity(intent);
                break;
            case R.id.iv_video_close:
                mIvVideo.setVisibility(View.GONE);
                mIvVideoClose.setVisibility(View.GONE);
                mTvVideoPath.setText("");
                modifiedFacility.setVideoPath("");

                //当点击关闭视频时 给删除的视频id赋值  传给后台
                if (mdata != null) {
                    deleteVideoId = mdata.getVideoId();
                }
                break;
            case R.id.ll_back:
                showAlertDialog();
                break;
            case R.id.tv_add_video:
                showVideoDialog();
            case R.id.tv_reset_problems:
                if (facilityProblemView != null) {
                    facilityProblemView.reset();
                }
                break;
        }
    }

}
