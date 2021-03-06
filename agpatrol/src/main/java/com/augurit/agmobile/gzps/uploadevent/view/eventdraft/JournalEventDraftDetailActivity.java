package com.augurit.agmobile.gzps.uploadevent.view.eventdraft;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.callback.Callback3;
import com.augurit.agmobile.gzps.common.constant.GzpsConstant;
import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.selectcomponent.SelectComponentFinishEvent2;
import com.augurit.agmobile.gzps.common.widget.AutoBreakViewGroup;
import com.augurit.agmobile.gzps.common.widget.MyGridView;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadevent.adapter.EventTypeAdapter;
import com.augurit.agmobile.gzps.uploadevent.adapter.FacilityTypeAdapter;
import com.augurit.agmobile.gzps.uploadevent.adapter.NextLinkAssigneersAdapter;
import com.augurit.agmobile.gzps.uploadevent.dao.GetPersonByOrgApiData;
import com.augurit.agmobile.gzps.uploadevent.model.Assigneers;
import com.augurit.agmobile.gzps.uploadevent.model.OrgItem;
import com.augurit.agmobile.gzps.uploadevent.model.Person;
import com.augurit.agmobile.gzps.uploadevent.model.ProblemUploadBean;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.gzps.uploadevent.util.PhotoUploadType;
import com.augurit.agmobile.gzps.uploadevent.view.eventlist.EventDetailMapActivity;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.baiduapi.BaiduApiService;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.selectlocation.model.BaiduGeocodeResult;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.loc.util.LocationUtil;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.AMFileUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.RadioGroupUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.core.geometry.Point;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxbinding.view.RxView;

import org.apache.commons.io.FileUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * ????????????????????????????????????
 */
public class JournalEventDraftDetailActivity extends BaseActivity {

    private MultiTakePhotoTableItem photo_item;
    private AutoBreakViewGroup disease_type_rg, urgency_state_rg, facility_type_rg;
    private MyGridView gv_facilitytype;
    private FacilityTypeAdapter mFacilityTypeAdapter;
    private View ll_eventtype;
    private MyGridView gv_eventtype;
    private EventTypeAdapter mEventTypeAdapter;
    private RadioGroup.LayoutParams params;
    private String[] urgency_type_array = {"??????", "?????????", "??????"};
    private Button upload_btn;
    private View btn_save_draft;
    private TextItemTableItem roadItem, addrItem;
    private String facilityCode, diseaseCode, urgencyCode, facilityName, diseaseName, urgencyName;
    private TextFieldTableItem problemitem;
    private TextFieldTableItem remarkItem;
    private CheckBox cb_isbyself;
    private View ll_nextlink_assigneers;
    private MyGridView gv_assignee;
    private NextLinkAssigneersAdapter assigneersAdapter;
    private Assigneers.Assigneer selAssignee;
    private TableDBService dbService;
    private List<DictionaryItem> facility_type_list;
    private List<DictionaryItem> problemList;
    private ProgressDialog progressDialog;

    private String componentId;
    private String layerId;
    private String layerName;
    private TextView tv_select_or_check_location;

    private Component mSelComponent;
    private DetailAddress mSelDetailAddress;

    private double reportX;   //?????????????????????X??????
    private double reportY;   //?????????????????????Y??????
    private String reportAddr;      //???????????????????????????
    private String isbyself = "false";  //??????????????????

    private ProblemUploadBean problemUploadBean = null;


    private View ll_nextlilnk_org_rg_rm;     //Rg???Rm???????????????????????????UI
    private AutoBreakViewGroup radio_nextlink_org_rg_rm;   //Rg???Rm???????????????UI
    private List<OrgItem> orgItems;

    private View ll_self_process;

    private CheckBox cb_is_send_message;

    private TextFieldTableItem problem_tab_item_self;
    private MultiTakePhotoTableItem photo_item_self;

    /**
     * ????????????????????????
     */
//    private Button btn_start_date;
    private Calendar cal;
    private Long startDate = null;
    private Long todayDate = null;
    private static final int START_DATE = 1;
    private Long checkdate = null;
    private List<File> fileTemp = new ArrayList<>();
    private boolean isObjectId = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_problem_upload);
        initView();
        initListener();
        initData();
        startLocate();
    }

    private void initData() {
        Serializable data = getIntent().getSerializableExtra("problemUploadBean");
        Serializable photos = getIntent().getSerializableExtra("photos");
        if(data != null){
            problemUploadBean = (ProblemUploadBean) data;
        }
        if(photos != null){
            ArrayList<Photo> photoArrayList = (ArrayList<Photo>) photos;
            ArrayList<Photo> photosProbleam = new ArrayList<>();
            ArrayList<Photo> photoSelf =  new ArrayList<>();

            for(Photo photo : photoArrayList){
                if(!TextUtils.isEmpty(photo.getPhotoName())) {
                    if (photo.getPhotoName().contains(PhotoUploadType.UPLOAD_FOR_PROBLEAM)) {
                        photosProbleam.add(photo);
                    } else if (photo.getPhotoName().contains(PhotoUploadType.UPLOAD_FOR_SELF)) {
                        photoSelf.add(photo);
                    } else {//???????????????
                        photosProbleam.add(photo);
                    }
                }else {
                    photosProbleam.add(photo);//???????????????
                }
            }
            photo_item.setSelectedPhotos(photosProbleam);
            photo_item_self.setSelectedPhotos(photoSelf);
        }
        if(problemUploadBean == null){
            problemUploadBean = new ProblemUploadBean();
        }
        dbService = new TableDBService(JournalEventDraftDetailActivity.this.getApplicationContext());
        facility_type_list = dbService.getDictionaryByTypecodeInDB("A174");

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ((TextView) findViewById(R.id.tv_title)).setText("????????????");

        initFacilityType(facility_type_list);
//        initFacilityView(facility_type_list);

        initDraftData();

        User user = new LoginService(getApplicationContext(), AMDatabase.getInstance()).getUser();
        if(user.getRoleCode().contains(GzpsConstant.roleCodes[3])
                || user.getRoleCode().contains(GzpsConstant.roleCodes[4])){
            ll_nextlilnk_org_rg_rm.setVisibility(View.VISIBLE);
            getOrgList();

        }else {
            ll_nextlilnk_org_rg_rm.setVisibility(View.GONE);
            if (problemUploadBean != null && !StringUtil.isEmpty(problemUploadBean.getObject_id())) {
                getNextLinkAssigneers(problemUploadBean.getObject_id());
            } else {
                getNextLinkAssigneers("");
            }
        }
    }

    private void  getPersonByOrgCodeAndName(String name,String code){
        new UploadEventService(getApplicationContext()).getPersonByOrgApiDataObservable(code,name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetPersonByOrgApiData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GetPersonByOrgApiData getPersonByOrgApiData) {
                        List<Person> personList = getPersonByOrgApiData.getUserFormList();
                        if(personList != null && !personList.isEmpty()) {
                            List<Assigneers.Assigneer> assigneeList = new ArrayList<>();
                            for (Person person : personList) {
                                assigneeList.add(new Assigneers.Assigneer(person.getCode(),person.getName()));
                            }
                            // assigneersAdapter.notifyDatasetChanged(assigneeList);
                            int selPosition = -1;
                            if(selAssignee != null){
                                for(int i=0; i<assigneeList.size(); i++){
                                    Assigneers.Assigneer assigneer = assigneeList.get(i);
                                    if(assigneer.getUserCode().equals(selAssignee.getUserCode())){
                                        selPosition = i;
                                    }
                                }
                            }
                            assigneersAdapter.notifyDatasetChanged(assigneeList, selPosition);
                        }
                    }
                });
    }

    private void getOrgList(){
        new UploadEventService(getApplicationContext()).getOrgItemList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<OrgItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<OrgItem> list) {
                        if(list != null &&
                                !list.isEmpty()){

                            orgItems= list;
                            int size = orgItems.size();
                            for (int i = 0; i < size; i++) {
                                RadioButton radioButton = new RadioButton(JournalEventDraftDetailActivity.this);
                                radioButton.setText(orgItems.get(i).getName());
                                //    radioButton.setTag(nextLinkOrgs);
                                radioButton.setLayoutParams(params);
                                radio_nextlink_org_rg_rm.addView(radioButton);

                                //????????????????????????
                                if(i == 0){
                                    radioButton.setChecked(true);
                                }

                                //????????????,?????????????????????
                                if(!TextUtils.isEmpty(problemUploadBean.getAssigneeOrg())){
                                    if(orgItems.get(i).getName().equals(problemUploadBean.getAssigneeOrg())){
                                        radioButton.setChecked(true);
                                    }
                                }
                            }


                        }

                    }
                });
    }

    /**
     * ???????????????
     */
    private void initStartDate() {
        cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);       //????????????????????????
        int month = cal.get(Calendar.MONTH) + 1;   //????????????????????????0????????????
        int day = cal.get(Calendar.DAY_OF_MONTH);
        startDate = new Date(year - 1900, month - 1, day).getTime();
        todayDate = new Date(year - 1900, month - 1, day).getTime();
    }

    private void initDraftData(){
        addrItem.setText(problemUploadBean.getSZWZ());
        roadItem.setText(problemUploadBean.getJDMC());
        problemitem.setText(problemUploadBean.getWTMS());
        if(!TextUtils.isEmpty(problemUploadBean.getSelf_process_detail())) {
            problem_tab_item_self.setText(problemUploadBean.getSelf_process_detail());
        }

        if("false".equals(problemUploadBean.getIsbyself())){
            cb_isbyself.setChecked(false);
            ll_self_process.setVisibility(View.GONE);
            ll_nextlink_assigneers.setVisibility(View.VISIBLE);
        } else {
            cb_isbyself.setChecked(true);
            ll_self_process.setVisibility(View.VISIBLE);
            ll_nextlink_assigneers.setVisibility(View.GONE);
        }
        if(!StringUtil.isEmpty(problemUploadBean.getSSLX())){
            String facilityTypeName = getSpinnerValueByCode(problemUploadBean.getSSLX());
            if("??????".equals(facilityTypeName)){
                   ll_eventtype.setVisibility(View.GONE);
            }
//            selFacilityType(facilityTypeName);
            mFacilityTypeAdapter.selectItemByName(facilityTypeName);
            mFacilityTypeAdapter.setEnable(false);
//            RadioGroupUtil.disableRadioGroup(facility_type_rg);
        }
        if(!StringUtil.isEmpty(problemUploadBean.getBHLX())) {
//            selEventType(getSpinnerValueByCode(problemUploadBean.getBHLX()));
            selEventType(problemUploadBean.getBHLX());
        }
        if(!StringUtil.isEmpty(problemUploadBean.getJJCD())){
            selUrgencyState(urgency_type_array[Integer.valueOf(problemUploadBean.getJJCD())-1]);
        }

        if(problemUploadBean.getAssignee() != null
                && problemUploadBean.getAssigneeName() != null){
            selAssignee = new Assigneers.Assigneer();
            selAssignee.setUserCode(problemUploadBean.getAssignee());
            selAssignee.setUserName(problemUploadBean.getAssigneeName());
        }

        if("-1".equals(problemUploadBean.getLayer_id())
                && "-1".equals(problemUploadBean.getObject_id())){
            //???????????????
            tv_select_or_check_location.setText(problemUploadBean.getSZWZ());
        } else {
            //???????????????
            tv_select_or_check_location.setText(problemUploadBean.getLayer_name());
        }
    }

    private void initView() {
        ll_nextlilnk_org_rg_rm = (View)findViewById(R.id.ll_nextlilnk_org);
        radio_nextlink_org_rg_rm =(AutoBreakViewGroup) findViewById(R.id.radio_nextlink_org);

        roadItem = (TextItemTableItem) findViewById(R.id.road_tab_item);
        addrItem = (TextItemTableItem) findViewById(R.id.addr_tab_item);
        problemitem = (TextFieldTableItem) findViewById(R.id.problem_tab_item);
        tv_select_or_check_location = (TextView) findViewById(R.id.tv_select_or_check_location);
        //personItem = (TextItemTableItem) findViewById(R.id.patrol_person);
        //remarkItem = (TextFieldTableItem) findViewById(R.id.remark);
        photo_item = (MultiTakePhotoTableItem) findViewById(R.id.photo_item);
        photo_item.setReadOnly();
        photo_item.setAddPhotoEnable(false);

        photo_item_self =(MultiTakePhotoTableItem)findViewById(R.id.photo_item_self);

        facility_type_rg = (AutoBreakViewGroup) findViewById(R.id.facility_type_rg);
        disease_type_rg = (AutoBreakViewGroup) findViewById(R.id.disease_type_rg);
        urgency_state_rg = (AutoBreakViewGroup) findViewById(R.id.urgency_state_rg);
        gv_facilitytype = (MyGridView) findViewById(R.id.gv_facilitytype);
        ll_eventtype = findViewById(R.id.ll_eventtype);
        gv_eventtype = (MyGridView) findViewById(R.id.gv_eventtype);
        cb_isbyself = (CheckBox) findViewById(R.id.cb_isbyself);
        ll_nextlink_assigneers = findViewById(R.id.ll_nextlink_assigneers);
        gv_assignee = (MyGridView) findViewById(R.id.gv_assignee);
        upload_btn = (Button) findViewById(R.id.problem_commint);
        btn_save_draft = findViewById(R.id.btn_save_draft);
        int screenWidths = getScreenWidths();
        params = new AutoBreakViewGroup.LayoutParams(screenWidths / 3, AutoBreakViewGroup
                .LayoutParams
                .WRAP_CONTENT);

        mFacilityTypeAdapter = new FacilityTypeAdapter(this);
        mFacilityTypeAdapter.setOnItemClickListener(new OnRecyclerItemClickListener<DictionaryItem>() {
            @Override
            public void onItemClick(View view, int position, DictionaryItem selectedData) {
                facilityCode = selectedData.getCode();
                facilityName = selectedData.getName();
                if("??????".equals(facilityName)){
                    ll_eventtype.setVisibility(View.GONE);
                } else {
                    ll_eventtype.setVisibility(View.VISIBLE);
                    problemList = dbService.getChildInVisableDictionaryByPCodeInDB(facilityCode);
                    initEventTypeView(problemList);
                }
            }

            @Override
            public void onItemLongClick(View view, int position, DictionaryItem selectedData) {

            }
        });
        gv_facilitytype.setAdapter(mFacilityTypeAdapter);
        mEventTypeAdapter = new EventTypeAdapter(this);
        gv_eventtype.setAdapter(mEventTypeAdapter);

        initUrgencyView();
        assigneersAdapter = new NextLinkAssigneersAdapter(this);
        gv_assignee.setAdapter(assigneersAdapter);
        assigneersAdapter.setOnItemClickListener(new OnRecyclerItemClickListener<Assigneers.Assigneer>() {
            @Override
            public void onItemClick(View view, int position, Assigneers.Assigneer selectedData) {
                selAssignee = selectedData;
            }

            @Override
            public void onItemLongClick(View view, int position, Assigneers.Assigneer selectedData) {

            }
        });
        progressDialog = new ProgressDialog(JournalEventDraftDetailActivity.this);
        progressDialog.setMessage("????????????...");
        EventBus.getDefault().register(this);

        ll_self_process =findViewById(R.id.ll_self_process);
        cb_is_send_message =(CheckBox)findViewById(R.id.cb_is_send_message);
        problem_tab_item_self =(TextFieldTableItem)findViewById(R.id.problem_tab_item_self);
//        btn_start_date = (Button) findViewById(R.id.btn_start_date);
//        initStartDate();
//        RxView.clicks(btn_start_date)
//                .throttleFirst(200,TimeUnit.MILLISECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        cal.setTimeInMillis(startDate);
//                        showDatePickerDialog(btn_start_date, cal, START_DATE);
//                    }
//                });
    }

    private void initListener() {
        facility_type_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int checkedRadioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);

                int index = group.indexOfChild(radioButton);
                String code = facility_type_list.get(index).getCode();
                facilityName = radioButton.getText().toString();
                facilityCode = code;
                if("??????".equals(facilityName)){
                    ll_eventtype.setVisibility(View.GONE);
                } else {
                    ll_eventtype.setVisibility(View.VISIBLE);
                    problemList = dbService.getChildInVisableDictionaryByPCodeInDB(code);
                    initEventTypeView(problemList);
                }

            }
        });
        disease_type_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                int index = group.indexOfChild(radioButton);
                String code = problemList.get(index).getCode();
                diseaseName = radioButton.getText().toString();
                diseaseCode = code;
            }
        });
        urgency_state_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                int index = group.indexOfChild(radioButton);
                urgencyName = radioButton.getText().toString();
                urgencyCode = (index + 1) + "";
            }
        });

        //????????????????????????
        RxView.clicks(upload_btn)
                .throttleFirst(2, TimeUnit.SECONDS)   //2???????????????????????????????????????????????????
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        progressDialog.show();
                        ProblemUploadBean paramsEntity = getParamsEntity();
                        if (paramsEntity == null) {
                            progressDialog.dismiss();
                            return;
                        }
                        sendToServerPre();
                    }
                });

        //???????????????????????????
        RxView.clicks(btn_save_draft)
                .throttleFirst(2, TimeUnit.SECONDS)   //2???????????????????????????????????????????????????
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        ProblemUploadBean paramsEntity = getDraftParamsEntity();
                        if (paramsEntity == null) {
                            progressDialog.dismiss();
                            return;
                        }
                        saveDraft();
                    }
                });

        /**
         * ????????????
         */
        findViewById(R.id.ll_select_component).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String layerId = problemUploadBean.getLayer_id();
                String layerUrl = problemUploadBean.getLayerurl();
                String layerName = problemUploadBean.getLayer_name();
                String objectId = problemUploadBean.getObject_id();
                String usid = problemUploadBean.getUsid();
                String x = problemUploadBean.getX();
                String y = problemUploadBean.getY();
                if(StringUtil.isEmpty(x)
                        || StringUtil.isEmpty(y)
                        || (StringUtil.isEmpty(usid) && StringUtil.isEmpty(objectId))){
                    ToastUtil.shortToast(JournalEventDraftDetailActivity.this, "????????????????????????");
                    return;
                }
                Intent intent = new Intent(JournalEventDraftDetailActivity.this, EventDetailMapActivity.class);
                intent.putExtra("layerUrl", layerUrl);
                intent.putExtra("layerName", layerName);
                intent.putExtra("x", Double.valueOf(x));
                intent.putExtra("y", Double.valueOf(y));
                intent.putExtra("usid", usid);
                intent.putExtra("object_id", objectId);
                startActivity(intent);

            }
        });

        cb_isbyself.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isbyself = "true";
                    ll_self_process.setVisibility(View.VISIBLE);
                    ll_nextlink_assigneers.setVisibility(View.GONE);
                } else {
                    isbyself = "false";
                    ll_self_process.setVisibility(View.GONE);
                    ll_nextlink_assigneers.setVisibility(View.VISIBLE);
                }
            }
        });
        photo_item.setOnDeletePhotoListener(new Callback3<Photo, Photo>() {
            @Override
            public void onCallback(Photo photo, Photo photo2) {
                AMDatabase.getInstance().deleteWhere(Photo.class, "id", photo.getId() + "");
            }
        });
//        photo_item.setOnDeletePhotoListener(new Callback1<Photo>() {
//            @Override
//            public void onCallback(Photo photo) {
//                AMDatabase.getInstance().deleteWhere(Photo.class, "id", photo.getId() + "");
//            }
//        });

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.MessageBox(JournalEventDraftDetailActivity.this, "??????", "?????????????????????????????????", new DialogInterface.OnClickListener() {
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
        });


        radio_nextlink_org_rg_rm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(checkedRadioButtonId);
                int index = ((RadioGroup) radioButton.getParent()).indexOfChild(radioButton);
                OrgItem orgItem = orgItems.get(index);
                getPersonByOrgCodeAndName(orgItem.getName(),orgItem.getCode());

                //   assigneersAdapter.notifyDatasetChanged(orgItems);
            }
        });
    }

    public String getSpinnerValueByCode(String code) {
        List<DictionaryItem> dictionaryItems = new TableDBService(this.getApplicationContext()).getDictionaryByCode(code);
        if (ListUtil.isEmpty(dictionaryItems)) {
            return "";
        }
        DictionaryItem dictionaryItem = dictionaryItems.get(0);
        return dictionaryItem.getName();
    }

    /**
     * ??????????????????????????????R1????????????????????????
     */
    private void getNextLinkAssigneers(String objectId) {
        new UploadEventService(getApplicationContext()).getTaskUserByloginName(objectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Assigneers>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(List<Assigneers> assigneersList) {
                        if(assigneersList != null
                                && !ListUtil.isEmpty(assigneersList)){
                            List<Assigneers.Assigneer> assigneeList = new ArrayList<>();
                            for(Assigneers assigneers : assigneersList){
                                assigneeList.addAll(assigneers.getAssigneers());
                            }
                            int selPosition = -1;
                            if(selAssignee != null){
                                for(int i=0; i<assigneeList.size(); i++){
                                    Assigneers.Assigneer assigneer = assigneeList.get(i);
                                    if(assigneer.getUserCode().equals(selAssignee.getUserCode())){
                                        selPosition = i;
                                    }
                                }
                            }
                            assigneersAdapter.notifyDatasetChanged(assigneeList, selPosition);
                        }
                    }
                });
    }

    /**
     * ???????????????????????????????????????
     * @param selectComponentFinishEvent
     */
    @Subscribe
    public void onReceivedFinishedSelectEvent2(SelectComponentFinishEvent2 selectComponentFinishEvent) {
        Component component = selectComponentFinishEvent.getFindResult();
        mSelComponent = component;
        mSelDetailAddress = selectComponentFinishEvent.getDetailAddress();


        if (mSelComponent != null) {
            isObjectId = true;
            //??????????????????
            layerName = component.getLayerName();
//            selFacilityType(layerName);
            mFacilityTypeAdapter.selectItemByName(layerName);
            mFacilityTypeAdapter.setEnable(false);
            RadioGroupUtil.disableRadioGroup(facility_type_rg);  //???????????????????????????????????????????????????
            String layerId = getLayerId(selectComponentFinishEvent);
            this.layerId = layerId;
            setObjectId(component);
            if (ll_nextlilnk_org_rg_rm.getVisibility() == View.GONE) {
                getNextLinkAssigneers(componentId);
            }
            setXY(selectComponentFinishEvent);
            String subType = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SUBTYPE));
            String type = layerName;
            if(StringUtil.isEmpty(layerName) || "null".equals(layerName) ){
                type = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.LAYER_NAME),"");
            }
            tv_select_or_check_location.setText(type);
            addrItem.setText(StringUtil.getNotNullString(mSelComponent.getGraphic().getAttributeValue(ComponentFieldKeyConstant.ADDR), ""));
            roadItem.setText("");
            if (selectComponentFinishEvent.getFindResult().getGraphic().getGeometry() instanceof Point) {
                Point point = (Point) selectComponentFinishEvent.getFindResult().getGraphic().getGeometry();
                requestAddress(point.getX(), point.getY(), new Callback1<BaiduGeocodeResult>() {
                    @Override
                    public void onCallback(BaiduGeocodeResult baiduGeocodeResult) {
                        addrItem.setText(StringUtil.getNotNullString(baiduGeocodeResult.getDetailAddress(), ""));
                        roadItem.setText(baiduGeocodeResult.getResult().getAddressComponent().getStreet());
                    }
                });
            }

        } else if (mSelDetailAddress != null) {
            //??????????????????
            if (isObjectId && ll_nextlilnk_org_rg_rm.getVisibility() == View.GONE) {
                getNextLinkAssigneers("");
                isObjectId = false;
            }
            mFacilityTypeAdapter.setEnable(true);
            RadioGroupUtil.enableRadioGroup(facility_type_rg);   //?????????????????????????????????????????????
            tv_select_or_check_location.setText(mSelDetailAddress.getDetailAddress());
            addrItem.setText(mSelDetailAddress.getDetailAddress());
            roadItem.setText(mSelDetailAddress.getStreet());
        }

    }

    private void setObjectId(Component component) {
        Integer objectid = component.getObjectId();
        componentId = objectid + "";
    }

    private void setXY(SelectComponentFinishEvent2 selectComponentFinishEvent) {
        if (selectComponentFinishEvent.getFindResult().getGraphic().getGeometry() instanceof Point) {
            Point point = (Point) selectComponentFinishEvent.getFindResult().getGraphic().getGeometry();
            //            modifiedIdentification.setX(point.getX());
            //            modifiedIdentification.setY(point.getY());
        }
    }

    /**
     * ???url?????????????????????id
     *
     * @param selectComponentFinishEvent
     * @return
     */
    @NonNull
    private String getLayerId(SelectComponentFinishEvent2 selectComponentFinishEvent) {
        int i = selectComponentFinishEvent.getFindResult().getLayerUrl().lastIndexOf("/");
        return selectComponentFinishEvent.getFindResult().getLayerUrl().substring(i + 1);
    }

    private void sendToServerPre(){

        Observable.create(new Observable.OnSubscribe<HashMap<String, RequestBody>>(){

            @Override
            public void call(Subscriber<? super HashMap<String, RequestBody>> subscriber) {
                HashMap<String, RequestBody> photoBody = getPhotoBody(photo_item.getSelectedPhotos());
                if(photoBody == null || photoBody.size() == 0){
                    subscriber.onError(new Exception("???????????????????????????"));
                    return;
                }else{
                    subscriber.onNext(photoBody);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HashMap<String, RequestBody>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ToastUtil.shortToast(JournalEventDraftDetailActivity.this, "???????????????????????????");
                    }

                    @Override
                    public void onNext(HashMap<String, RequestBody> queryFeatureSetList) {
                        sendToServer(queryFeatureSetList);
                    }
                });
    }

    public  File getFile(String url) {
        URL myFileUrl = null;
        String tempFile = new FilePathUtil(this).getSavePath() + "/images/" + System.currentTimeMillis() + ".jpg";
        File targetFile = new File(tempFile);
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            AMFileUtil.saveInputStreamToFile(is,targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetFile;
    }

    /**
     * ??????????????????
     */
    private void sendToServer(HashMap<String, RequestBody> photoBody) {
        UploadEventService eventService = new UploadEventService(JournalEventDraftDetailActivity.this);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        String json = gson.toJson(problemUploadBean);

        //???????????????????????????
        if("true".equals(isbyself)) {
            if (!getPhotoBodyForSelfProcess(photo_item_self.getSelectedPhotos()).isEmpty()) {
                photoBody.putAll(getPhotoBodyForSelfProcess(photo_item_self.getSelectedPhotos()));
            }
        }



        String assigneeCode = null;
        String assigneeName = null;
        if("false".equals(isbyself) && selAssignee != null){
            //??????????????????????????????????????????????????????
            assigneeCode = selAssignee.getUserCode();
            assigneeName = selAssignee.getUserName();
        }
        String isSendMessage="0";
        if(cb_is_send_message.isChecked()){
            isSendMessage ="1";
        }else {
            isSendMessage="0";
        }
        eventService.uploadEvent(isSendMessage,json, assigneeCode, assigneeName, photoBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean s) {
                        if (!s) {
                            progressDialog.dismiss();
                            ToastUtil.shortToast(JournalEventDraftDetailActivity.this, "???????????????????????????");
                        } else {
                            progressDialog.dismiss();
                            ToastUtil.shortToast(JournalEventDraftDetailActivity.this, "????????????");
                            AMDatabase.getInstance().deleteWhere(Photo.class, "problem_id", problemUploadBean.getDbid() + "");
                            AMDatabase.getInstance().delete(problemUploadBean);
                            setResult(123);
                            finish();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        for(File file:fileTemp){
                            try {
                                FileUtils.forceDelete(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        fileTemp.clear();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ToastUtil.shortToast(JournalEventDraftDetailActivity.this, "???????????????????????????");
                    }
                });

    }

    /**
     * ?????????????????????
     */
    private void saveDraft(){
        AMDatabase.getInstance().save(problemUploadBean);
        List<Photo> photoList = photo_item.getSelectedPhotos();
        for(Photo photo : photoList){
            photo.setProblem_id(problemUploadBean.getDbid() + "");
            AMDatabase.getInstance().save(photo);
        }

        //?????????????????????
        if("true".equals(isbyself)) {
            List<Photo> photoList1 = photo_item_self.getSelectedPhotos();
            for (Photo photo : photoList1) {
                photo.setProblem_id(problemUploadBean.getDbid() + "");
                photo.setPhotoName(PhotoUploadType.UPLOAD_FOR_SELF + photo.getPhotoName());
                AMDatabase.getInstance().save(photo);
            }
        }
        ToastUtil.shortToast(this.getApplicationContext(), "????????????");
        setResult(123);
        finish();
    }

    /**
     * ?????????????????????????????????
     */
    private void startLocate() {
        LocationUtil.register(this, 1000, 0, new LocationUtil.OnLocationChangeListener() {
            @Override
            public void getLastKnownLocation(Location location) {

            }

            @Override
            public void onLocationChanged(Location location) {
//                lat = location.getLongitude() + "";
//                lng = location.getLatitude() + "";
                requestAddress(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        });
    }

    /**
     * ?????????????????????????????????
     * @param location
     */
    private void requestAddress(final Location location) {
        BaiduApiService baiduApiService = new BaiduApiService(this);
        baiduApiService.parseLocation(new LatLng(location.getLatitude(), location.getLongitude()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaiduGeocodeResult>() {
                    @Override
                    public void call(BaiduGeocodeResult baiduGeocodeResult) {
//                        addrItem.setText(baiduGeocodeResult.getDetailAddress());
//                        roadItem.setText(baiduGeocodeResult.getResult().getAddressComponent().getStreet
//                                ());
                        if (baiduGeocodeResult != null) {
                            reportAddr = baiduGeocodeResult.getDetailAddress();
                            reportX = location.getLongitude();
                            reportY = location.getLatitude();
                        }

                    }
                });
    }

    /**
     * ?????????????????????????????????
     */
    private void requestAddress(final double x, final double y, final Callback1<BaiduGeocodeResult> callback) {
        BaiduApiService baiduApiService = new BaiduApiService(this);
        baiduApiService.parseLocation(new LatLng(y, x))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaiduGeocodeResult>() {
                    @Override
                    public void call(BaiduGeocodeResult baiduGeocodeResult) {
//                        addrItem.setText(baiduGeocodeResult.getDetailAddress());
//                        roadItem.setText(baiduGeocodeResult.getResult().getAddressComponent().getStreet
//                                ());
                        if (baiduGeocodeResult != null) {
                            callback.onCallback(baiduGeocodeResult);
                        } else {

                        }

                    }
                });
    }


    //???????????????????????????
    private HashMap<String, RequestBody> getPhotoBody(List<Photo> selectedPhotos) {
        fileTemp.clear();
        String prefix = PhotoUploadType.UPLOAD_FOR_PROBLEAM;
        int i = 0;
        HashMap<String, RequestBody> requestMap = new HashMap<>();
        if (!ListUtil.isEmpty(selectedPhotos)) {
            for (Photo photo : selectedPhotos) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName()
                            , RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }else{
                    File file = getFile(photo.getPhotoPath());
                    fileTemp.add(file);
                    if(file != null) {
                        requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName()
                                , RequestBody.create(MediaType.parse("image/*"), file));
                        i++;
                    }
                }
            }
            return requestMap;
        }
        return null;
    }

    //???????????????????????????
    private HashMap<String, RequestBody> getPhotoBodyForSelfProcess(List<Photo> selectedPhotos) {
        String prefix =  PhotoUploadType.UPLOAD_FOR_SELF;
        int i = 0;
        HashMap<String, RequestBody> requestMap = new HashMap<>();
        if (!ListUtil.isEmpty(selectedPhotos)) {
            for (Photo photo : selectedPhotos) {
                if (photo.getLocalPath() != null) {
                    File file = new File(photo.getLocalPath());
                    requestMap.put("file" + i + "\"; filename=\"" + prefix + file.getName()
                            , RequestBody.create(MediaType.parse("image/*"), file));
                    i++;
                }
            }
            return requestMap;
        }
        return null;
    }

    /**
     * ???????????????????????????????????????????????????
     * @return
     */
    private ProblemUploadBean getParamsEntity() {
        if("true".equals(isbyself)) {
            if (!TextUtils.isEmpty(problem_tab_item_self.getText())) {
                problemUploadBean.setSelf_process_detail(problem_tab_item_self.getText());
            }
        }

        if (ListUtil.isEmpty(photo_item.getSelectedPhotos())) {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }
        if (!TextUtils.isEmpty(addrItem.getText())
//                && (mSelComponent != null || mSelDetailAddress != null)
                ) {
            problemUploadBean.setSZWZ(addrItem.getText());
        } else {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }
        if (!TextUtils.isEmpty(roadItem.getText())) {
            problemUploadBean.setJDMC(roadItem.getText());
        } else {
            ToastUtil.shortToast(this, "????????????????????????");
            return null;
        }
        if (!TextUtils.isEmpty(facilityCode)) {
            problemUploadBean.setSSLX(facilityCode);
            //uploadBean.setSSLX_NAME(facilityName);
        } else {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }
        /*if (!TextUtils.isEmpty(diseaseCode)) {
            problemUploadBean.setBHLX(diseaseCode);
            //uploadBean.setBHLX_NAME(diseaseName);

        } else {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }*/

        //??????????????????????????????
        List<DictionaryItem> selectedEventTypes = mEventTypeAdapter.getSelectedEventTypeList();
        if("??????".equals(facilityName)){
            //???????????????????????????????????????????????????
            diseaseCode = "";
        } else if(ListUtil.isEmpty(selectedEventTypes)){
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        } else {
            diseaseCode = "";
            for(DictionaryItem dictionaryItem : selectedEventTypes){
                diseaseCode +=  "," + dictionaryItem.getCode();
            }
            diseaseCode = diseaseCode.substring(1);
        }
        problemUploadBean.setBHLX(diseaseCode);

        if (!TextUtils.isEmpty(urgencyCode)) {
            problemUploadBean.setJJCD(urgencyCode);
            //uploadBean.setJJCD_NAME(urgencyName);
        } else {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }

//        if(checkdate != null && "false".equals(isbyself)){
//            problemUploadBean.setYjwcsj(checkdate);
//        }else if(checkdate == null && "false".equals(isbyself)){
//            ToastUtil.shortToast(this, "?????????????????????????????????");
//            return null;
//        }else if("true".equals(isbyself)){
//            problemUploadBean.setYjwcsj(null);
//        }
//
//        if(problemUploadBean.getYjwcsj() != null && checkdate < todayDate){
//            ToastUtil.shortToast(this, "????????????????????????????????????????????????????????????");
//            return null;
//        }

        //????????????????????????????????????????????????????????????
        if ("??????".equals(facilityName)
                && TextUtils.isEmpty(problemitem.getText())) {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }
        problemUploadBean.setWTMS(problemitem.getText());

        if("false".equals(isbyself)){
            if(selAssignee == null){
                ToastUtil.shortToast(this, "??????????????????????????????");
                return null;
            }
            problemUploadBean.setAssignee(selAssignee.getUserCode());
            problemUploadBean.setAssigneeName(selAssignee.getUserName());
        }else{
            List<Photo> selectedPhotos = photo_item_self.getSelectedPhotos();
            if(ListUtil.isEmpty(selectedPhotos)){
                ToastUtil.shortToast(this, "?????????????????????????????????");
                return null;
            }
        }


        String lng = null;
        String lat = null;

        if (mSelComponent != null) {
            //??????????????????
            lng = StringUtil.valueOf(((Point) mSelComponent.getGraphic().getGeometry()).getX());
            lat = StringUtil.valueOf(((Point) mSelComponent.getGraphic().getGeometry()).getY());
            if (!TextUtils.isEmpty(componentId)) {
                problemUploadBean.setObject_id(componentId);
            }
            if (!TextUtils.isEmpty(layerId)) {
                problemUploadBean.setLayer_id(layerId);
            }
            if (!TextUtils.isEmpty(layerName)) {
                problemUploadBean.setLayer_name(layerName);
            }
            problemUploadBean.setLayerurl(mSelComponent.getLayerUrl());
            problemUploadBean.setUsid(StringUtil.getNotNullString(mSelComponent.getGraphic().getAttributeValue(ComponentFieldKeyConstant.USID), null));
        } else if (mSelDetailAddress != null) {
            //??????????????????
            lng = StringUtil.valueOf(mSelDetailAddress.getX());
            lat = StringUtil.valueOf(mSelDetailAddress.getY());
            problemUploadBean.setObject_id("-1");
            problemUploadBean.setLayer_id("-1");
            problemUploadBean.setLayer_name(null);
        }

        if (!TextUtils.isEmpty(lng)) {
            problemUploadBean.setX(lng);
        }
        if (!TextUtils.isEmpty(lat)) {
            problemUploadBean.setY(lat);
        }
        problemUploadBean.setYjwcsj(null);
        problemUploadBean.setLayer_id("-1");
        problemUploadBean.setReportx(StringUtil.valueOf(reportX));
        problemUploadBean.setReporty(StringUtil.valueOf(reportY));
        problemUploadBean.setReportaddr(reportAddr);
        problemUploadBean.setSBR(BaseInfoManager.getUserName(JournalEventDraftDetailActivity.this));
        problemUploadBean.setIsbyself(isbyself);
        problemUploadBean.setTemplateCode("GX_XCYH");
        return problemUploadBean;
    }

    /**
     * ?????????????????????????????????????????????????????????
     * @return
     */
    private ProblemUploadBean getDraftParamsEntity() {

        if (ListUtil.isEmpty(photo_item.getSelectedPhotos())) {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }
        if (!TextUtils.isEmpty(addrItem.getText())
//                && (mSelComponent != null || mSelDetailAddress != null)
                ) {
            problemUploadBean.setSZWZ(addrItem.getText());
        } else {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }
        if (!TextUtils.isEmpty(roadItem.getText())) {
            problemUploadBean.setJDMC(roadItem.getText());
        } else {
            ToastUtil.shortToast(this, "????????????????????????");
            return null;
        }
        if (!TextUtils.isEmpty(facilityCode)) {
            problemUploadBean.setSSLX(facilityCode);
            //uploadBean.setSSLX_NAME(facilityName);
        } else {
            ToastUtil.shortToast(this, "?????????????????????");
            return null;
        }
        /*if (!TextUtils.isEmpty(diseaseCode)) {
            problemUploadBean.setBHLX(diseaseCode);
            //uploadBean.setBHLX_NAME(diseaseName);

        } else {

        }*/
        List<DictionaryItem> selectedEventTypes = mEventTypeAdapter.getSelectedEventTypeList();
        if("??????".equals(facilityName)){
            //???????????????????????????????????????????????????
            diseaseCode = "";
        } else if(ListUtil.isEmpty(selectedEventTypes)){
            diseaseCode = "";
        } else {
            diseaseCode = "";
            for(DictionaryItem dictionaryItem : selectedEventTypes){
                diseaseCode = diseaseCode +  "," + dictionaryItem.getCode();
            }
            diseaseCode = diseaseCode.substring(1);
            problemUploadBean.setBHLX(diseaseCode);
        }

        if (!TextUtils.isEmpty(urgencyCode)) {
            problemUploadBean.setJJCD(urgencyCode);
            //uploadBean.setJJCD_NAME(urgencyName);
        } else {

        }
//        if(checkdate != null && "false".equals(isbyself)){
//            problemUploadBean.setYjwcsj(checkdate);
//        }else{
//            problemUploadBean.setYjwcsj(null);
//        }

        if (!TextUtils.isEmpty(problemitem.getText())) {
            problemUploadBean.setWTMS(problemitem.getText());
        } else {
        }

        if("false".equals(isbyself)
                && selAssignee != null){
            //??????????????????????????????????????????????????????
            problemUploadBean.setAssignee(selAssignee.getUserCode());
            problemUploadBean.setAssigneeName(selAssignee.getUserName());
        } else {
            problemUploadBean.setAssignee(null);
            problemUploadBean.setAssigneeName(null);
        }

        String lng = null;
        String lat = null;

        if (mSelComponent != null) {
            lng = StringUtil.valueOf(((Point) mSelComponent.getGraphic().getGeometry()).getX());
            lat = StringUtil.valueOf(((Point) mSelComponent.getGraphic().getGeometry()).getY());
            if (!TextUtils.isEmpty(componentId)) {
                problemUploadBean.setObject_id(componentId);
            }
            if (!TextUtils.isEmpty(layerId)) {
                problemUploadBean.setLayer_id(layerId);
            }else{
                problemUploadBean.setLayer_id("-1");
            }
            if (!TextUtils.isEmpty(layerName)) {
                problemUploadBean.setLayer_name(layerName);
            }
            problemUploadBean.setLayerurl(mSelComponent.getLayerUrl());
            problemUploadBean.setUsid(StringUtil.getNotNullString(mSelComponent.getGraphic().getAttributeValue(ComponentFieldKeyConstant.USID), null));
        } else if (mSelDetailAddress != null) {
            lng = StringUtil.valueOf(mSelDetailAddress.getX());
            lat = StringUtil.valueOf(mSelDetailAddress.getY());
            problemUploadBean.setObject_id("-1");
            problemUploadBean.setLayer_id("-1");
            problemUploadBean.setLayer_name(null);
        } else {
            /*lng = StringUtil.valueOf(reportX);
            lat = StringUtil.valueOf(reportY);
            problemUploadBean.setSZWZ(reportAddr);
            problemUploadBean.setObject_id("-1");
            problemUploadBean.setLayer_id("-1");
            problemUploadBean.setLayer_name(null);*/
            problemUploadBean.setLayer_id("-1");
        }

        if (!TextUtils.isEmpty(lng)) {
            problemUploadBean.setX(lng);
        }
        if (!TextUtils.isEmpty(lat)) {
            problemUploadBean.setY(lat);
        }

        problemUploadBean.setReportx(StringUtil.valueOf(reportX));
        problemUploadBean.setReporty(StringUtil.valueOf(reportY));
        problemUploadBean.setReportaddr(reportAddr);
        problemUploadBean.setSBR(BaseInfoManager.getUserName(JournalEventDraftDetailActivity.this));
        problemUploadBean.setIsbyself(isbyself);
        problemUploadBean.setTemplateCode("GX_XCYH");
        problemUploadBean.setTime(System.currentTimeMillis());
        return problemUploadBean;
    }

    /**
     * ??????????????????????????????
     */
    private void initUrgencyView() {
        for (int i = 0; i < urgency_type_array.length; i++) {
            RadioButton radioButton = new RadioButton(JournalEventDraftDetailActivity.this);
            radioButton.setText(urgency_type_array[i]);
            radioButton.setLayoutParams(params);
            urgency_state_rg.addView(radioButton);
        }
    }

    /**
     * ??????????????????????????????
     * @param eventTypeList  ??????????????????
     */
    private void initEventTypeView(List<DictionaryItem> eventTypeList){
        Collections.sort(eventTypeList, new Comparator<DictionaryItem>() {
            @Override
            public int compare(DictionaryItem o1, DictionaryItem o2) {
                String code = o1.getValue();
                String target = code;
                if (code.length() > 1) {
                    target = code.replaceAll("[^(0-9)]", "");//??????????????????
                }

                String code2 = o2.getValue();
                String target2 = code2;
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
        mEventTypeAdapter.notifyDataSetChanged(eventTypeList);
    }

    /**
     * ??????????????????????????????
     * @param problemList  ??????????????????
     */
    @Deprecated
    private void initDiseaseView(List<DictionaryItem> problemList) {
        disease_type_rg.removeAllViews();
        Collections.sort(problemList, new Comparator<DictionaryItem>() {
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
        SecureRandom ra = new SecureRandom();
        for (int i = 0; i < problemList.size(); i++) {
            RadioButton radioButton = new RadioButton(JournalEventDraftDetailActivity.this);
//            radioButton.setId(ra.nextInt());
            radioButton.setText(problemList.get(i).getName());
            radioButton.setLayoutParams(params);
            disease_type_rg.addView(radioButton);
            if (i == 0) {
                //                // ????????????????????????1 ???????????????????????????????????????
                //                //??????id ??????radioButton ??????
                //                RadioButton rb_checked = (RadioButton) radioGroup.findViewById(radioButton.getId());
                //                //??????????????????
                //                rb_checked.setChecked(true);

                // ????????????????????????2
//                disease_type_rg.check(radioButton.getId());
            }
        }
    }

    //???????????????????????????
    private void initFacilityType(List<DictionaryItem> facility_type_list){
        Collections.sort(facility_type_list, new Comparator<DictionaryItem>() {
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
        mFacilityTypeAdapter.notifyDataSetChanged(facility_type_list);
    }

    /**
     * ???????????????????????????
     * @param facility_type_list  ??????????????????
     */
    @Deprecated
    private void initFacilityView(List<DictionaryItem> facility_type_list) {
        Collections.sort(facility_type_list, new Comparator<DictionaryItem>() {
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
        SecureRandom ra = new SecureRandom();
        for (int i = 0; i < facility_type_list.size(); i++) {
            RadioButton radioButton = new RadioButton(JournalEventDraftDetailActivity.this);
//            radioButton.setId(ra.nextInt());
            radioButton.setText(facility_type_list.get(i).getName());
            radioButton.setLayoutParams(params);
            facility_type_rg.addView(radioButton);
            if (i == 0) {
                //                // ????????????????????????1 ???????????????????????????????????????
                //                //??????id ??????radioButton ??????
                //                RadioButton rb_checked = (RadioButton) radioGroup.findViewById(radioButton.getId());
                //                //??????????????????
                //                rb_checked.setChecked(true);

                // ????????????????????????2
//                facility_type_rg.check(radioButton.getId());
            }
        }
    }

    /**
     * ???????????????????????????
     * @param layerName
     */
    private void selFacilityType(String layerName) {
        for (int i = 0; i < facility_type_rg.getChildCount(); i++) {
            View rbv = facility_type_rg.getChildAt(i);
            if (rbv instanceof RadioButton) {
                RadioButton rb = (RadioButton) rbv;
                if (rb.getText().toString().equals(layerName)) {
                    facility_type_rg.check(rb.getId());
                    break;
                }
            }
        }
    }

    /**
     * ???????????????????????????
     * @param eventTypes
     */
    private void selEventType(String eventTypes) {
        if(StringUtil.isEmpty(eventTypes)){
            return;
        }
        String[] eventTypeArr = eventTypes.split(",");
        mEventTypeAdapter.setSelectedEventTypes(eventTypeArr);
    }

    /**
     * ???????????????????????????
     * @param eventType
     */
    @Deprecated
    private void selEventType2(String eventType) {
        for (int i = 0; i < disease_type_rg.getChildCount(); i++) {
            View rbv = disease_type_rg.getChildAt(i);
            if (rbv instanceof RadioButton) {
                RadioButton rb = (RadioButton) rbv;
                if (rb.getText().toString().equals(eventType)) {
                    disease_type_rg.check(rb.getId());
                    break;
                }
            }
        }
    }

    /**
     * ???????????????????????????
     * @param urgency
     */
    private void selUrgencyState(String urgency){
        for (int i = 0; i < urgency_state_rg.getChildCount(); i++) {
            View rbv = urgency_state_rg.getChildAt(i);
            if (rbv instanceof RadioButton) {
                RadioButton rb = (RadioButton) rbv;
                if (rb.getText().toString().equals(urgency)) {
                    urgency_state_rg.check(rb.getId());
                    break;
                }
            }
        }
    }

    private int getScreenWidths() {
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        return width;
    }

    public void showDatePickerDialog(final Button btn, Calendar calendar, final int type) {
        // ??????????????????DatePickerDialog???????????????????????????????????????
        new DatePickerDialog(this,
                // ???????????????(How the parent is notified that the date is set.)
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        display(btn, year, monthOfYear, dayOfMonth);
                        if (type == START_DATE) {
                            startDate = new Date(year - 1900, monthOfYear, dayOfMonth).getTime();
                        }
                    }
                }
                // ??????????????????
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * ????????????
     */
    public void display(Button dateDisplay, int year,
                        int monthOfYear, int dayOfMonth) {
//        dateDisplay.setText(new StringBuffer().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth));

        dateDisplay.setText(new StringBuffer().append(year).append(monthOfYear < 9 ? "-0" : "-").append(monthOfYear + 1).append(dayOfMonth < 9 ? "-0" : "-").append(dayOfMonth));
//        checkdate = (new StringBuffer().append(year).append(monthOfYear < 9 ? "-0" : "-").append(monthOfYear + 1).append(dayOfMonth < 9 ? "-0" : "-").append(dayOfMonth)).toString();
        checkdate = new Date(year - 1900, monthOfYear, dayOfMonth).getTime();
    }

    @Override
    public void onBackPressed(){
        DialogUtil.MessageBox(JournalEventDraftDetailActivity.this, "??????", "?????????????????????????????????", new DialogInterface.OnClickListener() {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (photo_item != null) {
            photo_item.onActivityResult(requestCode, resultCode, data);
        }

        if(photo_item_self != null){
            photo_item_self.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        LocationUtil.unregister(this);
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
