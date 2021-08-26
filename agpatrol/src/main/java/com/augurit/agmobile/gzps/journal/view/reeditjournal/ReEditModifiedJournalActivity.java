package com.augurit.agmobile.gzps.journal.view.reeditjournal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.model.OUser;
import com.augurit.agmobile.gzps.common.model.ResponseBody;
import com.augurit.agmobile.gzps.common.service.GzpsService;
import com.augurit.agmobile.gzps.common.widget.FlexRadioGroup;
import com.augurit.agmobile.gzps.common.widget.MultiSelectTableItlem;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.journal.model.TeamMember;
import com.augurit.agmobile.gzps.journal.service.JournalService;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityAddressErrorModel;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityInfoErrorModel;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.service.CorrectFacilityService;
import com.augurit.agmobile.gzps.uploadfacility.util.UploadLayerFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadfacility.view.SendFacilityOwnerShipUnit;
import com.augurit.agmobile.gzps.uploadfacility.view.UploadFacilitySuccessEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.FacilityAddressErrorView;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.FacilityInfoErrorView;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.RefreshMyModificationListEvent;
import com.augurit.agmobile.gzps.uploadfacility.view.facilityprobrem.FacilityProblemView;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.spinner.AMSpinner;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.ResourceUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.view.RxView;

import org.apache.commons.collections4.MapUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * 再次编辑设施
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.upload.view
 * @createTime 创建时间 ：17/12/18
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/18
 * @modifyMemo 修改备注：
 */

//todo xcl bug：1. 当第一次编辑的时候改变了位置，再次编辑改成『设施不存在』的时候上传的x,y还是改变后的位置，应该传递的是原设施的位置
public class ReEditModifiedJournalActivity extends BaseActivity {


    private ModifiedFacility originalModifiedFacility;

    private MultiTakePhotoTableItem take_photo_item;
    private View btn_upload_journal;
    private ProgressDialog progressDialog;
    private TextView tv_select_or_check_location;
    private TextItemTableItem textitem_component_type;
    private TextItemTableItem tableitem_current_time;
    private TextItemTableItem tableitem_current_user;
    private TextFieldTableItem textitem_description;
    private LinearLayout ll_container;

    //设施不存在
    private static final String COMPONENT_NOT_EXIST = "1";
    //其他情况
    private static final String OTHERS = "2";

    private String errorType = OTHERS;

    private FacilityAddressErrorView componentAddressErrorView;
    private FacilityInfoErrorView componentInfoErrorView;
    private RadioButton lastSelectButton = null;
    private TextItemTableItem textitem_facility;
    private FlexRadioGroup rg_1;
    private CheckBox rb_component_not_exist;
    private LinearLayout ll_address_container;
    private LinearLayout ll_address;
    private View ll_textitem_description_container;
    private String originalCorrectType = null;

    private CountDownTimer countDownTimer; //用于上报时超过一定时间提示一次
    private FlexboxLayout ll_problems_container;
    private FacilityProblemView facilityProblemView;

    /**
     * 城中村
     */
    private CheckBox cbInnerCity;
    /**
     * 三不管
     */
    private CheckBox cbDontKnowWhere;

    private String cityVillage;
    private CheckBox cbOthers;
    private EditText etOthers;
    private AMSpinner spinnerOthers;
    private boolean mManStatusIsSpinner = false;   //当前管理状态中的其他是Spinner还是EditText
    private MultiSelectTableItlem selectTableItlem;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reedit_journal);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        originalModifiedFacility = getIntent().getParcelableExtra("data");
        if (originalModifiedFacility != null) {
            originalCorrectType = originalModifiedFacility.getCorrectType();
        }

        initView();

        initUser();

    }

    private void initUser() {
        User user = new LoginService(this, AMDatabase.getInstance()).getUser();
        String userName = user.getUserName();
        String userId = user.getId();

        originalModifiedFacility.setMarkPerson(userName);
        originalModifiedFacility.setMarkPersonId(userId);
        tableitem_current_user.setText(userName);
    }

    private void initView() {

        take_photo_item = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_item);
        take_photo_item.setPhotoNumShow(true, 6);
        take_photo_item.setPhotoExampleEnable(true);
        take_photo_item.setRequired(true);
        if (originalModifiedFacility != null) {
            take_photo_item.setSelectedPhotos(originalModifiedFacility.getPhotos());
        }


        ((TextView) findViewById(R.id.tv_title)).setText("再次编辑");

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 首先要判断是否进行过编辑，如果没有动过，那么不需要询问，直接退出
                showAlertDialog();
            }
        });

        /**
         * 当前时间
         */
        tableitem_current_time = (TextItemTableItem) findViewById(R.id.tableitem_current_time);
        /**
         * 填表人
         */
        tableitem_current_user = (TextItemTableItem) findViewById(R.id.tableitem_current_user);
        tableitem_current_user.setReadOnly();


        /**
         * 描述
         */
        textitem_description = (TextFieldTableItem) findViewById(R.id.textitem_description);
        if (originalModifiedFacility != null) {
            textitem_description.setText(originalModifiedFacility.getDescription());
        }


        /**
         * 设施问题
         */
        ll_problems_container = (FlexboxLayout) findViewById(R.id.ll_problems_container);
        final View ll_problems_container_parent = findViewById(R.id.ll_problems_container_parent);
        if (originalModifiedFacility != null) {
            facilityProblemView = new FacilityProblemView(this, ll_problems_container, originalModifiedFacility.getpCode(), originalModifiedFacility.getChildCode());
        } else {
            facilityProblemView = new FacilityProblemView(this, ll_problems_container);
        }
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
                        if (OTHERS.equals(errorType)) {
                            List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();
                            if (ListUtil.isEmpty(selectedPhotos) || selectedPhotos.size() < 1) {
                                ToastUtil.iconLongToast(getApplicationContext(), R.mipmap.ic_alert_yellow, "请按“拍照须知”要求至少提供三张照片！");
                                return;
                            }
                        }

                        if ("设施不存在".equals(originalCorrectType) && COMPONENT_NOT_EXIST.equals(errorType)) {
                            ToastUtil.iconLongToast(getApplicationContext(), R.mipmap.ic_alert_yellow, "当前无修改，不需要进行提交，请直接退出");
                            return;
                        }

                        /**
                         * 如果管理状态选了其他，那么一定要填
                         */
                        if (cbOthers.isChecked() && !mManStatusIsSpinner && TextUtils.isEmpty(etOthers.getText().toString())){
                            ToastUtil.iconShortToast(getApplicationContext(),R.mipmap.ic_alert_yellow,"管理状态不可以为空");
                            return;
                        }

                        if (progressDialog == null) {
                            progressDialog = new ProgressDialog(ReEditModifiedJournalActivity.this);
                            progressDialog.setMessage("正在提交，请等待");
                            progressDialog.setCancelable(false);
                        }
                        upload();
                    }
                });

        /**
         * 属性容器
         */
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        ll_address_container = (LinearLayout) findViewById(R.id.ll_address_container);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        ll_textitem_description_container = findViewById(R.id.ll_textitem_description_container);

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
                if (item instanceof DictionaryItem) {
//                    etOthers.setText(((DictionaryItem) item).getName());
                }
            }
        });

        cbInnerCity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    cityVillage = cbInnerCity.getText().toString();
                    cbDontKnowWhere.setChecked(false);
                    cbOthers.setChecked(false);
                }
            }
        });

        cbDontKnowWhere.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    cityVillage = cbDontKnowWhere.getText().toString();
                    cbInnerCity.setChecked(false);
                    cbOthers.setChecked(false);
                }
            }
        });

        cbOthers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    etOthers.setEnabled(true);
                    spinnerOthers.setEditable(true);
                    cbInnerCity.setChecked(false);
                    cbDontKnowWhere.setChecked(false);
                    if(mManStatusIsSpinner){
                        spinnerOthers.setVisibility(View.VISIBLE);
                    } else {
                        etOthers.setVisibility(View.VISIBLE);
                    }
                }else {
                    etOthers.setEnabled(false);
                    spinnerOthers.setEditable(false);
                    etOthers.setVisibility(View.GONE);
                    spinnerOthers.setVisibility(View.GONE);
                }
            }
        });

        if (originalModifiedFacility != null && cbInnerCity.getText().toString().equals(originalModifiedFacility.getCityVillage())){
            cbInnerCity.setChecked(true);
        }else if (originalModifiedFacility != null && cbDontKnowWhere.getText().toString().equals(originalModifiedFacility.getCityVillage())){
            cbDontKnowWhere.setChecked(true);
        }else if (originalModifiedFacility != null && !TextUtils.isEmpty(originalModifiedFacility.getCityVillage())){
            /**
             * 如果既不是"城中村"/"三不管"，那么选其他
             */
            cbOthers.setChecked(true);
            etOthers.setText(originalModifiedFacility.getCityVillage());
        }

        /**
         * 设施地址
         */
        componentAddressErrorView = new FacilityAddressErrorView(ReEditModifiedJournalActivity.this,
                originalModifiedFacility);
        ll_address_container.removeAllViews();
        componentAddressErrorView.addTo(ll_address_container);
        /**
         * 设施属性
         */
        componentInfoErrorView = new FacilityInfoErrorView(ReEditModifiedJournalActivity.this,
                originalModifiedFacility,false);
        ll_container.removeAllViews();
        componentInfoErrorView.addTo(ll_container);
        /**
         * 设施类型
         */
        textitem_facility = (TextItemTableItem) findViewById(R.id.textitem_facility);
        textitem_facility.setReadOnly();
        if (originalModifiedFacility != null) {
            textitem_facility.setText(originalModifiedFacility.getLayerName());
        }
        /**
         * 设施不存在
         */
        rb_component_not_exist = (CheckBox) findViewById(R.id.rb_component_not_exist);
        rb_component_not_exist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    errorType = COMPONENT_NOT_EXIST;
                    ll_container.setVisibility(View.GONE);
                    ll_address.setVisibility(View.GONE);
                    take_photo_item.setVisibility(View.GONE);
                    ll_textitem_description_container.setVisibility(View.GONE);
                    ll_problems_container_parent.setVisibility(View.GONE);
                } else {
                    errorType = OTHERS;
                    ll_container.setVisibility(View.VISIBLE);
                    ll_address.setVisibility(View.VISIBLE);
                    take_photo_item.setVisibility(View.VISIBLE);
                    ll_textitem_description_container.setVisibility(View.VISIBLE);
                    ll_problems_container_parent.setVisibility(View.VISIBLE);
                }
            }
        });

        if ("设施不存在".equals(originalModifiedFacility.getCorrectType())) {
            rb_component_not_exist.setChecked(true);
        }

        /**
         * 班组成员
         */
        selectTableItlem = (MultiSelectTableItlem) findViewById(R.id.multiselect_table_item);
        initTeamMember();
    }

    /**
     * 获取班组成员
     */
    private void initTeamMember() {
        GzpsService gzpsService = new GzpsService(this.getApplicationContext());
        gzpsService.getTeamMember()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<OUser>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<OUser> oUsers) {
                        if (ListUtil.isEmpty(oUsers)) {
                            //隐藏该项
                            selectTableItlem.setVisibility(View.GONE);
                            //ll_member_total_container.setVisibility(View.GONE);
                            return;
                        }
                        Map<String, Object> items = new LinkedHashMap<String, Object>();
                        for (OUser oUser : oUsers) {
                            items.put(oUser.getUserName(), oUser);
                        }
                        Map<String, Object> selectedItem = new HashMap<String, Object>(2);
                        if(originalModifiedFacility == null){
                            selectedItem.put(new LoginRouter(getApplicationContext(), AMDatabase.getInstance()).getUser().getUserName(), new LoginRouter(getApplicationContext(), AMDatabase.getInstance()).getUser().getUserName());
                        }else {
                            String teamMember = originalModifiedFacility.getTeamMember();
                            String strTeam = "";
                            if (!StringUtil.isEmpty(teamMember)) {
                                List<TeamMember> teamMembers = JsonUtil.getObject(teamMember, new TypeToken<List<TeamMember>>() {
                                }.getType());
                                if (!ListUtil.isEmpty(teamMembers)) {
                                    for (TeamMember teamMember1 : teamMembers) {
                                        selectedItem.put(teamMember1.getUserName(), teamMember1.getUserName());
                                    }
                                }
                            }
                        }
                        selectTableItlem.setMultiChoice(items, selectedItem);
                    }
                });
    }

    private void upload() {

        boolean b = completeAttributes();
        /**
         * 如果此时完善属性不成功，不继续向下执行
         */
        if (!b) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            return;
        }

        completeCorrectTypeAndReportType();

        boolean b1 = completeAddress();
        /**
         * 如果此时完善性地址不成功，不继续向下执行
         */
        if (!b1) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            return;
        }

        completeCorrectTypeAndReportType();

        originalModifiedFacility.setDescription(textitem_description.getText());
        originalModifiedFacility.setMarkPerson(tableitem_current_user.getText());

        completeTime();
        completePhotos();


        /**
         * 管理状态
         */
        if (cbDontKnowWhere.isChecked() || cbInnerCity.isChecked()){
            originalModifiedFacility.setCityVillage(cityVillage);
        }else if (cbOthers.isChecked()){
            if(mManStatusIsSpinner){
                originalModifiedFacility.setCityVillage(spinnerOthers.getText());
            } else {
                originalModifiedFacility.setCityVillage(etOthers.getText().toString());
            }
        } else {
            originalModifiedFacility.setCityVillage("");
        }

        /**
         * 填充设施问题
         */
        completeFacilityProblem();

        Map<String, Object> selectedItems = selectTableItlem.getSelectedItems();
        if (MapUtils.isEmpty(selectedItems)) {
            ToastUtil.shortToast(getApplicationContext(), "请选择班组成员");
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            return;
        }

        Set<Map.Entry<String, Object>> entries = selectedItems.entrySet();
        List<TeamMember> teamMembers = new ArrayList<TeamMember>();
        for (Map.Entry<String, Object> entry : entries) {
            OUser oUser = (OUser) entry.getValue();
            TeamMember teamMember = new TeamMember();
            teamMember.setLoginName(oUser.getLoginName());
            teamMember.setUserName(oUser.getUserName());
            teamMembers.add(teamMember);
        }

        String json = JsonUtil.getJson(teamMembers);
        originalModifiedFacility.setTeamMember(json);

        //20秒提示一次
        countDownTimer = new CountDownTimer(CorrectFacilityService.TIMEOUT * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                if (progressDialog != null) {
                    progressDialog.setMessage("正在提交，请等待。   " + time + "s");
                }
                if (time % 20 == 0) {
                    ToastUtil.longToast(ReEditModifiedJournalActivity.this, "网络忙，请稍等");
                }
            }

            @Override
            public void onFinish() {

            }
        };
        if(ListUtil.isEmpty(facilityProblemView.getSelectedParentAndChildICodeList())
                || StringUtil.isEmpty(facilityProblemView.getSelectedParentAndChildICodeList().get(0))) {
            uploadData();
        }else{
            MessageBox(ReEditModifiedJournalActivity.this, "提示", "     存在设施问题，如有对设施问题进行修改，请自行到相应的问题上报界面进行问题类型的修改。", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    uploadData();
                }
            },null);
        }
    }

    /**
     * 数据上报
     */
    private void uploadData() {
        if(progressDialog != null){
            progressDialog.show();
        }
        if (countDownTimer != null) {
            countDownTimer.start();
        }

        JournalService identificationService = new JournalService(getApplicationContext());

        identificationService.editDiaryView(originalModifiedFacility)
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
                        ToastUtil.shortToast(ReEditModifiedJournalActivity.this, "提交失败");
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
                            ToastUtil.shortToast(ReEditModifiedJournalActivity.this, "提交成功");
                            EventBus.getDefault().post(new RefreshMyModificationListEvent());
                            if (originalModifiedFacility.deletedPhotoIds != null) {
                                originalModifiedFacility.deletedPhotoIds.clear();
                            }
                            EventBus.getDefault().post(new UploadFacilitySuccessEvent(originalModifiedFacility));
                            finish();
                        } else {

                            ToastUtil.shortToast(ReEditModifiedJournalActivity.this, "保存失败，原因是：" + responseBody.getMessage());
                        }
                    }
                });
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

    private void completeFacilityProblem() {
        if (OTHERS.equals(errorType)) {
            //设施问题
            List<String> selectedLargeAndSmallItemList = facilityProblemView.getSelectedParentAndChildICodeList();
            //父类编码
            if (selectedLargeAndSmallItemList != null && !TextUtils.isEmpty(selectedLargeAndSmallItemList.get(0))) {
                originalModifiedFacility.setpCode(selectedLargeAndSmallItemList.get(0));
            } else {
                originalModifiedFacility.setpCode("");
            }

            //子类编码
            if (selectedLargeAndSmallItemList != null && !TextUtils.isEmpty(selectedLargeAndSmallItemList.get(1))) {
                originalModifiedFacility.setChildCode(selectedLargeAndSmallItemList.get(1));
            } else {
                originalModifiedFacility.setChildCode("");
            }
        } else {
            //设施不存在
            originalModifiedFacility.setChildCode("");
            originalModifiedFacility.setpCode("");
        }
    }

    private void completePhotos() {

        /**
         * 如果是从其他类型修改成设施不存在，那么需要把之前上传的图片删除
         */
        if (COMPONENT_NOT_EXIST.equals(errorType)) {

            List<Photo> deletedPhotos = originalModifiedFacility.getPhotos();
            List<String> deletedPhotoIds = new ArrayList<>();
            if (!ListUtil.isEmpty(deletedPhotos)) {
                for (Photo photo : deletedPhotos) {
                    if (photo.getId() != 0) {
                        deletedPhotoIds.add(String.valueOf(photo.getId()));
                    }
                }
                originalModifiedFacility.setDeletedPhotoIds(deletedPhotoIds);
            }

            originalModifiedFacility.setPhotos(new ArrayList<Photo>());

        } else {
            List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();

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
                originalModifiedFacility.setPhotos(newAddedPhotos);
            }

            List<Photo> deletedPhotos = take_photo_item.getDeletedPhotos();
            List<String> deletedPhotoIds = new ArrayList<>();
            if (!ListUtil.isEmpty(deletedPhotos)) {
                for (Photo photo : deletedPhotos) {
                    if (photo.getId() != 0) {
                        deletedPhotoIds.add(String.valueOf(photo.getId()));
                    }

                }
                originalModifiedFacility.setDeletedPhotoIds(deletedPhotoIds);
            }
        }

    }


    private void completeTime() {
        long currentTimeMillis = System.currentTimeMillis();
        originalModifiedFacility.setUpdateTime(currentTimeMillis);
        originalModifiedFacility.setMarkTime(currentTimeMillis);
    }

    private boolean completeAddress() {
        if (componentAddressErrorView != null
                && OTHERS.equals(errorType)) {
            if (!componentAddressErrorView.ifAllowUpload()) {
                ToastUtil.iconShortToast(this, R.mipmap.ic_alert_yellow, componentAddressErrorView.getNotAllowUploadReason());
                return false;
            }
            FacilityAddressErrorModel facilityAddressErrorModel = componentAddressErrorView.getFacilityAddressErrorModel();
            originalModifiedFacility.setAddr(facilityAddressErrorModel.getCorrectAddress());
            originalModifiedFacility.setX(facilityAddressErrorModel.getCorrectX());
            originalModifiedFacility.setY(facilityAddressErrorModel.getCorrectY());
            originalModifiedFacility.setRoad(facilityAddressErrorModel.getRoad());
            return true;
        }
        return !errorType.equals(OTHERS);
    }


    private boolean completeAttributes() {
        if (componentInfoErrorView != null && OTHERS.equals(errorType)) {
            FacilityInfoErrorModel facilityInfoErrorModel = componentInfoErrorView.getFacilityInfoErrorModel();
            if (!facilityInfoErrorModel.isIfAllowUpload() && !"接驳井".equals(facilityInfoErrorModel.getAttrOne())) {
                ToastUtil.iconShortToast(getApplicationContext(), R.mipmap.ic_alert_yellow, facilityInfoErrorModel.getNotAllowUploadReason());
                return false;
            }
            originalModifiedFacility.setAttrOne(facilityInfoErrorModel.getAttrOne());
            originalModifiedFacility.setAttrTwo(facilityInfoErrorModel.getAttrTwo());
            originalModifiedFacility.setAttrThree(facilityInfoErrorModel.getAttrThree());
            originalModifiedFacility.setAttrFour(facilityInfoErrorModel.getAttrFour());
            originalModifiedFacility.setAttrFive(facilityInfoErrorModel.getAttrFive());
            return true;
        }
        return !errorType.equals(OTHERS);
    }

    private void completeCorrectTypeAndReportType() {


        if (OTHERS.equals(errorType)) {
            if (componentInfoErrorView.getFacilityInfoErrorModel().isHasModified() &&
                    componentAddressErrorView.getFacilityAddressErrorModel().isHasModified()) {

                originalModifiedFacility.setCorrectType("位置与信息错误");
                originalModifiedFacility.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);

            } else if (componentInfoErrorView.getFacilityInfoErrorModel().isHasModified()
                    && !componentAddressErrorView.getFacilityAddressErrorModel().isHasModified()) {

                originalModifiedFacility.setCorrectType("信息错误");
                originalModifiedFacility.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);

            } else if (!componentInfoErrorView.getFacilityInfoErrorModel().isHasModified() &&
                    componentAddressErrorView.getFacilityAddressErrorModel().isHasModified()) {
                originalModifiedFacility.setCorrectType("位置错误");
                originalModifiedFacility.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);
            } else {
                originalModifiedFacility.setCorrectType("数据确认");
                originalModifiedFacility.setReportType(UploadLayerFieldKeyConstant.CONFIRM);
            }

            List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();
            originalModifiedFacility.setPhotos(selectedPhotos);
            originalModifiedFacility.setThumbnailPhotos(take_photo_item.getThumbnailPhotos());
        } else {
            originalModifiedFacility.setCorrectType("设施不存在");
            originalModifiedFacility.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);
        }
    }


    @Override
    public void onBackPressed() {
        showAlertDialog();
        //super.onBackPressed();
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
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (facilityProblemView != null){
            facilityProblemView = null;
        }

        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
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
     * @param sendFacilityOwnerShipUnit
     */
    @Subscribe
    public void onReceivedFacilityOwnerShipUnit(SendFacilityOwnerShipUnit sendFacilityOwnerShipUnit){
        if (sendFacilityOwnerShipUnit != null && !TextUtils.isEmpty(sendFacilityOwnerShipUnit.originOwnership)) {
//            cbOthers.setChecked(true);
//            etOthers.setText(sendFacilityOwnerShipUnit.originOwnership);

            //清空spinner
            spinnerOthers.removeAll();
            //获取行政区域列表
            TableDBService tableDBService = new TableDBService(this);
            List<DictionaryItem> districts = tableDBService.getDictionaryByTypecodeInDB("A169");
            //跟传递过来的区域进行对比
            String districtCode = null;
            for (DictionaryItem district : districts) {
                boolean contains = sendFacilityOwnerShipUnit.originOwnership.contains(district.getName());
                if (contains) {
                    districtCode = district.getCode();
                    break;
                }
            }
            //根据区域从数据字典读取该区的权属单位列表
            if (districtCode != null) {
                List<DictionaryItem> ownershipDic = tableDBService.getChildDictionaryByPCodeInDB(districtCode);
                if (!ListUtil.isEmpty(ownershipDic)) {
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
                    for (DictionaryItem dictionaryItem : ownershipDic) {
                        //填充spinner
                        spinnerOthers.addItems(dictionaryItem.getName(), dictionaryItem);
                    }

                    if (spinnerOthers.containsKey(originalModifiedFacility.getCityVillage())){
                        spinnerOthers.selectItem(originalModifiedFacility.getCityVillage());
                    }else {
                        spinnerOthers.selectItem(0);
                    }
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
                if(cbOthers.isChecked()) {
                    spinnerOthers.setVisibility(View.GONE);
                    etOthers.setVisibility(View.VISIBLE);
                }
//                etOthers.setText("");
            }
        }
    }

}
