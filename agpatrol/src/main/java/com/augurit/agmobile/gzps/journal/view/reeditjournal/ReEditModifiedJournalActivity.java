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
 * ??????????????????
 *
 * @author ????????? ???xuciluan
 * @version 1.0
 * @package ?????? ???com.augurit.agmobile.gzps.upload.view
 * @createTime ???????????? ???17/12/18
 * @modifyBy ????????? ???xuciluan
 * @modifyTime ???????????? ???17/12/18
 * @modifyMemo ???????????????
 */

//todo xcl bug???1. ??????????????????????????????????????????????????????????????????????????????????????????????????????x,y???????????????????????????????????????????????????????????????
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

    //???????????????
    private static final String COMPONENT_NOT_EXIST = "1";
    //????????????
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

    private CountDownTimer countDownTimer; //?????????????????????????????????????????????
    private FlexboxLayout ll_problems_container;
    private FacilityProblemView facilityProblemView;

    /**
     * ?????????
     */
    private CheckBox cbInnerCity;
    /**
     * ?????????
     */
    private CheckBox cbDontKnowWhere;

    private String cityVillage;
    private CheckBox cbOthers;
    private EditText etOthers;
    private AMSpinner spinnerOthers;
    private boolean mManStatusIsSpinner = false;   //?????????????????????????????????Spinner??????EditText
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


        ((TextView) findViewById(R.id.tv_title)).setText("????????????");

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo ????????????????????????????????????????????????????????????????????????????????????????????????
                showAlertDialog();
            }
        });

        /**
         * ????????????
         */
        tableitem_current_time = (TextItemTableItem) findViewById(R.id.tableitem_current_time);
        /**
         * ?????????
         */
        tableitem_current_user = (TextItemTableItem) findViewById(R.id.tableitem_current_user);
        tableitem_current_user.setReadOnly();


        /**
         * ??????
         */
        textitem_description = (TextFieldTableItem) findViewById(R.id.textitem_description);
        if (originalModifiedFacility != null) {
            textitem_description.setText(originalModifiedFacility.getDescription());
        }


        /**
         * ????????????
         */
        ll_problems_container = (FlexboxLayout) findViewById(R.id.ll_problems_container);
        final View ll_problems_container_parent = findViewById(R.id.ll_problems_container_parent);
        if (originalModifiedFacility != null) {
            facilityProblemView = new FacilityProblemView(this, ll_problems_container, originalModifiedFacility.getpCode(), originalModifiedFacility.getChildCode());
        } else {
            facilityProblemView = new FacilityProblemView(this, ll_problems_container);
        }
        /**
         * ????????????
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
         * ??????
         */
        btn_upload_journal = findViewById(R.id.btn_upload_journal);
        RxView.clicks(btn_upload_journal)
                .throttleFirst(2, TimeUnit.SECONDS)   //2???????????????????????????????????????????????????
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (OTHERS.equals(errorType)) {
                            List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();
                            if (ListUtil.isEmpty(selectedPhotos) || selectedPhotos.size() < 1) {
                                ToastUtil.iconLongToast(getApplicationContext(), R.mipmap.ic_alert_yellow, "?????????????????????????????????????????????????????????");
                                return;
                            }
                        }

                        if ("???????????????".equals(originalCorrectType) && COMPONENT_NOT_EXIST.equals(errorType)) {
                            ToastUtil.iconLongToast(getApplicationContext(), R.mipmap.ic_alert_yellow, "?????????????????????????????????????????????????????????");
                            return;
                        }

                        /**
                         * ???????????????????????????????????????????????????
                         */
                        if (cbOthers.isChecked() && !mManStatusIsSpinner && TextUtils.isEmpty(etOthers.getText().toString())){
                            ToastUtil.iconShortToast(getApplicationContext(),R.mipmap.ic_alert_yellow,"???????????????????????????");
                            return;
                        }

                        if (progressDialog == null) {
                            progressDialog = new ProgressDialog(ReEditModifiedJournalActivity.this);
                            progressDialog.setMessage("????????????????????????");
                            progressDialog.setCancelable(false);
                        }
                        upload();
                    }
                });

        /**
         * ????????????
         */
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        ll_address_container = (LinearLayout) findViewById(R.id.ll_address_container);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        ll_textitem_description_container = findViewById(R.id.ll_textitem_description_container);

        /**
         * ?????????
         */
        cbInnerCity = (CheckBox) findViewById(R.id.cb_inner_city);
        /**
         * ?????????
         */
        cbDontKnowWhere = (CheckBox) findViewById(R.id.cb_dont_know_where);
        /**
         * ??????
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
             * ???????????????"?????????"/"?????????"??????????????????
             */
            cbOthers.setChecked(true);
            etOthers.setText(originalModifiedFacility.getCityVillage());
        }

        /**
         * ????????????
         */
        componentAddressErrorView = new FacilityAddressErrorView(ReEditModifiedJournalActivity.this,
                originalModifiedFacility);
        ll_address_container.removeAllViews();
        componentAddressErrorView.addTo(ll_address_container);
        /**
         * ????????????
         */
        componentInfoErrorView = new FacilityInfoErrorView(ReEditModifiedJournalActivity.this,
                originalModifiedFacility,false);
        ll_container.removeAllViews();
        componentInfoErrorView.addTo(ll_container);
        /**
         * ????????????
         */
        textitem_facility = (TextItemTableItem) findViewById(R.id.textitem_facility);
        textitem_facility.setReadOnly();
        if (originalModifiedFacility != null) {
            textitem_facility.setText(originalModifiedFacility.getLayerName());
        }
        /**
         * ???????????????
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

        if ("???????????????".equals(originalModifiedFacility.getCorrectType())) {
            rb_component_not_exist.setChecked(true);
        }

        /**
         * ????????????
         */
        selectTableItlem = (MultiSelectTableItlem) findViewById(R.id.multiselect_table_item);
        initTeamMember();
    }

    /**
     * ??????????????????
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
                            //????????????
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
         * ?????????????????????????????????????????????????????????
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
         * ????????????????????????????????????????????????????????????
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
         * ????????????
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
         * ??????????????????
         */
        completeFacilityProblem();

        Map<String, Object> selectedItems = selectTableItlem.getSelectedItems();
        if (MapUtils.isEmpty(selectedItems)) {
            ToastUtil.shortToast(getApplicationContext(), "?????????????????????");
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

        //20???????????????
        countDownTimer = new CountDownTimer(CorrectFacilityService.TIMEOUT * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                if (progressDialog != null) {
                    progressDialog.setMessage("???????????????????????????   " + time + "s");
                }
                if (time % 20 == 0) {
                    ToastUtil.longToast(ReEditModifiedJournalActivity.this, "?????????????????????");
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
            MessageBox(ReEditModifiedJournalActivity.this, "??????", "     ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    uploadData();
                }
            },null);
        }
    }

    /**
     * ????????????
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
                        ToastUtil.shortToast(ReEditModifiedJournalActivity.this, "????????????");
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
                            ToastUtil.shortToast(ReEditModifiedJournalActivity.this, "????????????");
                            EventBus.getDefault().post(new RefreshMyModificationListEvent());
                            if (originalModifiedFacility.deletedPhotoIds != null) {
                                originalModifiedFacility.deletedPhotoIds.clear();
                            }
                            EventBus.getDefault().post(new UploadFacilitySuccessEvent(originalModifiedFacility));
                            finish();
                        } else {

                            ToastUtil.shortToast(ReEditModifiedJournalActivity.this, "???????????????????????????" + responseBody.getMessage());
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
            //????????????
            List<String> selectedLargeAndSmallItemList = facilityProblemView.getSelectedParentAndChildICodeList();
            //????????????
            if (selectedLargeAndSmallItemList != null && !TextUtils.isEmpty(selectedLargeAndSmallItemList.get(0))) {
                originalModifiedFacility.setpCode(selectedLargeAndSmallItemList.get(0));
            } else {
                originalModifiedFacility.setpCode("");
            }

            //????????????
            if (selectedLargeAndSmallItemList != null && !TextUtils.isEmpty(selectedLargeAndSmallItemList.get(1))) {
                originalModifiedFacility.setChildCode(selectedLargeAndSmallItemList.get(1));
            } else {
                originalModifiedFacility.setChildCode("");
            }
        } else {
            //???????????????
            originalModifiedFacility.setChildCode("");
            originalModifiedFacility.setpCode("");
        }
    }

    private void completePhotos() {

        /**
         * ?????????????????????????????????????????????????????????????????????????????????????????????
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
             * ?????????????????????id???0?????????????????????????????????id????????????0???
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
            if (!facilityInfoErrorModel.isIfAllowUpload() && !"?????????".equals(facilityInfoErrorModel.getAttrOne())) {
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

                originalModifiedFacility.setCorrectType("?????????????????????");
                originalModifiedFacility.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);

            } else if (componentInfoErrorView.getFacilityInfoErrorModel().isHasModified()
                    && !componentAddressErrorView.getFacilityAddressErrorModel().isHasModified()) {

                originalModifiedFacility.setCorrectType("????????????");
                originalModifiedFacility.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);

            } else if (!componentInfoErrorView.getFacilityInfoErrorModel().isHasModified() &&
                    componentAddressErrorView.getFacilityAddressErrorModel().isHasModified()) {
                originalModifiedFacility.setCorrectType("????????????");
                originalModifiedFacility.setReportType(UploadLayerFieldKeyConstant.CORRECT_ERROR);
            } else {
                originalModifiedFacility.setCorrectType("????????????");
                originalModifiedFacility.setReportType(UploadLayerFieldKeyConstant.CONFIRM);
            }

            List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();
            originalModifiedFacility.setPhotos(selectedPhotos);
            originalModifiedFacility.setThumbnailPhotos(take_photo_item.getThumbnailPhotos());
        } else {
            originalModifiedFacility.setCorrectType("???????????????");
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
     * @param sendFacilityOwnerShipUnit
     */
    @Subscribe
    public void onReceivedFacilityOwnerShipUnit(SendFacilityOwnerShipUnit sendFacilityOwnerShipUnit){
        if (sendFacilityOwnerShipUnit != null && !TextUtils.isEmpty(sendFacilityOwnerShipUnit.originOwnership)) {
//            cbOthers.setChecked(true);
//            etOthers.setText(sendFacilityOwnerShipUnit.originOwnership);

            //??????spinner
            spinnerOthers.removeAll();
            //????????????????????????
            TableDBService tableDBService = new TableDBService(this);
            List<DictionaryItem> districts = tableDBService.getDictionaryByTypecodeInDB("A169");
            //????????????????????????????????????
            String districtCode = null;
            for (DictionaryItem district : districts) {
                boolean contains = sendFacilityOwnerShipUnit.originOwnership.contains(district.getName());
                if (contains) {
                    districtCode = district.getCode();
                    break;
                }
            }
            //????????????????????????????????????????????????????????????
            if (districtCode != null) {
                List<DictionaryItem> ownershipDic = tableDBService.getChildDictionaryByPCodeInDB(districtCode);
                if (!ListUtil.isEmpty(ownershipDic)) {
                    mManStatusIsSpinner = true;
                    if(cbOthers.isChecked()) {
                        spinnerOthers.setVisibility(View.VISIBLE);
                        etOthers.setVisibility(View.GONE);
                    }
                    //??????
                    Collections.sort(ownershipDic, new Comparator<DictionaryItem>() {
                        @Override
                        public int compare(DictionaryItem o1, DictionaryItem o2) {
                            String code = o1.getCode();
                            String target = code;
                            if (code.length() > 1) {
                                target = code.replaceAll("[^(0-9)]", "");//??????????????????
                            }

                            String code2 = o2.getCode();
                            String target2 = code;
                            if (code2.length() > 1) {
                                target2 = code2.replaceAll("[^(0-9)]", "");//??????????????????
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
                    //??????spinner
                    for (DictionaryItem dictionaryItem : ownershipDic) {
                        //??????spinner
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
