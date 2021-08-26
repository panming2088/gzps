package com.augurit.agmobile.gzps.uploadevent.view.eventlist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.constant.GzpsConstant;
import com.augurit.agmobile.gzps.common.model.Result;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.service.GzpsService;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.publicaffair.event.UpdateAdviceEvent;
import com.augurit.agmobile.gzps.uploadevent.event.UpdateAfterDeleteEvent;
import com.augurit.agmobile.gzps.uploadevent.event.UpdateAfterEditEvent;
import com.augurit.agmobile.gzps.uploadevent.event.UpdateJournalEvent;
import com.augurit.agmobile.gzps.uploadevent.model.EventDetail;
import com.augurit.agmobile.gzps.uploadevent.model.EventListItem;
import com.augurit.agmobile.gzps.uploadevent.model.EventProcess;
import com.augurit.agmobile.gzps.uploadevent.model.ProblemUploadBean;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.gzps.uploadevent.util.CodeToStringUtils;
import com.augurit.agmobile.gzps.uploadevent.view.eventedit.EventEditActivity;
import com.augurit.agmobile.gzps.uploadevent.view.eventflow.AddEventAdviceDialog;
import com.augurit.agmobile.gzps.uploadevent.view.eventflow.AddEventJournalActivity;
import com.augurit.agmobile.gzps.uploadevent.view.eventflow.AdviceView;
import com.augurit.agmobile.gzps.uploadevent.view.eventflow.EventProcessView;
import com.augurit.agmobile.gzps.uploadevent.view.eventflow.ReassignDialog;
import com.augurit.agmobile.gzps.uploadevent.view.eventflow.RetriveDialog;
import com.augurit.agmobile.gzps.uploadevent.view.eventflow.RollBackDialog;
import com.augurit.agmobile.gzps.uploadevent.view.eventflow.SendNextLinkActivity;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.selectlocation.util.SelectLocationConstant;
import com.augurit.agmobile.patrolcore.selectlocation.view.SelectLocationActivity;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * 事件详情界面
 * <p>
 * 待受理  处理中  已办结
 * <p>
 * Created by xcl on 2017/11/11.
 */
public class EventDetailActivity extends BaseActivity {

    private String[] urgency_type_array = {"一般", "较紧急", "紧急"};
    private TextItemTableItem textitem_upload_user;
    private TextItemTableItem textitem_parent_org;
    private TextItemTableItem textitem_upload_date;
    private TextItemTableItem textitem_upload_address;
    private TextView tv_check_location;
    private View ll_report_addr;
    private TextView tv_check_component_location;
    private View ll_addr;
    private TextItemTableItem textitem_upload_compttype;
    private TextItemTableItem textitem_upload_eventtype;
    private TextItemTableItem textitem_upload_urgency;
    private TextFieldTableItem textfield_description;
    private MultiTakePhotoTableItem photo_item;
    //    private RecyclerView rv_handle_process;
//    private RecyclerView rv_advice;
//    private HandleProcessAdapter handleProcessAdapter;
//    private EventAdviceAdapter eventAdviceAdapter;
    private ProgressLinearLayout pb_loading;
    private LinearLayout ll_advice;
    private LinearLayout ll_event_process;

    private View ll_signevent;
    private View ll_eventhandle;
    private View ll_add_advice;

    private View btn_signevent;
    private View btn_rollback;
    private View btn_reassign;
    private TextView btn_submit;
    private View btn_add_journal;
    private View btn_addvice;

    private UploadEventService mEventService;
    private ProgressDialog pd;

    private int journalPageNo = 1;

    private EventListItem mEventListItem;
    private int eventPState = GzpsConstant.EVENT_P_STATE_HANDLING;//事件流程类型 待受理  处理中  已办结
    private EventDetail mEventDetail;

    private boolean readOnly = true;

    private String mProcInstDbId;//用于任务转派

    private View ll_retrieve;
    private View btn_retrieve;

    private View ll_delete;
    private View btn_delete;

    private View ll_edit;
    private View btn_edit;
    private TextItemTableItem textitem_upload_deal_time;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdetail);
        mEventListItem = (EventListItem) getIntent().getSerializableExtra("eventList");
        eventPState = getIntent().getIntExtra("eventPState", GzpsConstant.EVENT_P_STATE_HANDLING);
        readOnly = getIntent().getBooleanExtra("readonly", false);
        if (mEventListItem != null && mEventListItem.getProcInstDbId() != null) {
            mProcInstDbId = mEventListItem.getProcInstDbId();
        }
        mEventService = new UploadEventService(this.getApplicationContext());
        initView();
        initListener();
        initData();


        EventBus.getDefault().register(this);
    }

    private void initData() {
//        pb_loading.showLoading();
        /*textitem_upload_user.setText(mEventListItem.getReportUser());
        textitem_upload_date.setText(TimeUtil.getStringTimeYMDS(new Date(mEventListItem.getReportTime())));
        textitem_upload_compttype.setText(mEventListItem.getComponentType());
        textitem_upload_eventtype.setText(mEventListItem.getEventType());
        textitem_upload_address.setText(mEventListItem.getAddr());
        textfield_description.setText(mEventListItem.getDescription());*/
        String taskInstDbid = null;
        String procInstDbid = null;
        if (!GzpsConstant.EVENT_OPEN.equals(mEventListItem.getState())) {
            procInstDbid = mEventListItem.getProcInstDbId();
        } else {
            taskInstDbid = mEventListItem.getTaskInstDbid();
            procInstDbid = mEventListItem.getProcInstDbId();
        }
        UploadEventService uploadEventService = new UploadEventService(getApplicationContext());
        uploadEventService.getDetails(taskInstDbid, procInstDbid, mEventListItem.getMasterEntityKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result2<EventDetail>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtil.MessageBoxCannotCancel(EventDetailActivity.this, null, "请求数据出错！", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        /*pb_loading.showError("加载失败", "", "重试", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                initData();
                            }
                        });*/
                    }

                    @Override
                    public void onNext(Result2<EventDetail> result) {
                        String json = JsonUtil.getJson(result);
                        if (result == null
                                || result.getCode() != 200
                                || result.getData() == null) {
                            DialogUtil.MessageBoxCannotCancel(EventDetailActivity.this, null, "请求数据出错！", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            /*pb_loading.showError("数据为空", "", "刷新", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    initData();
                                }
                            });*/
                            return;
                        }
//                        pb_loading.showContent();
                        EventDetail eventDetail = result.getData();
                        refreshView(eventDetail);

                    }
                });
    }

    private void refreshView(EventDetail eventDetail) {
        mEventDetail = eventDetail;
        /**
         * 填充事件详情
         */
        textitem_upload_user.setText(eventDetail.getEvent().getUserName());
        textitem_parent_org.setText(eventDetail.getEvent().getParentOrgName());
        textitem_upload_date.setText(TimeUtil.getStringTimeYMDS(new Date(eventDetail.getEvent().getTime())));
        textitem_upload_address.setText(eventDetail.getEvent().getAddr());
        tv_check_location.setText(eventDetail.getEvent().getReportaddr());
        tv_check_component_location.setText(eventDetail.getEvent().getAddr());
        textitem_upload_compttype.setText(CodeToStringUtils.getSpinnerValueByCode(
                eventDetail.getEvent().getComponentType()));


        String textOther = "";
        if (eventDetail.getEvent().getEventType().contains("其他")) {
            String[] split = eventDetail.getEvent().getEventType().split("其他");
            if (split.length > 1) {
                textOther = "其他" + split[1];
            }
        }


        String bhlx = getSpinnerValuesByMultiCode(eventDetail.getEvent().getEventType());
        if (!TextUtils.isEmpty(bhlx)) {
            if(!TextUtils.isEmpty(textOther)) {
                textitem_upload_eventtype.setText(bhlx + "，" + textOther);
            }else{
                textitem_upload_eventtype.setText(bhlx);
            }
        } else {
            textitem_upload_eventtype.setText(textOther);
        }
//        textitem_upload_eventtype.setText(CodeToStringUtils.getSpinnerValuesByMultiCode(
//                eventDetail.getEvent().getEventType()) + textOther);


        try {
            textitem_upload_urgency.setText(urgency_type_array[Integer.valueOf(
                    eventDetail.getEvent().getUrgency()) - 1]);
        } catch (Exception e) {

        }
        if ("1".equals(eventDetail.getEvent().getSfjb()) && "false".equals(eventDetail.getEvent().getIsbyself())) {
            textitem_upload_deal_time.setVisibility(View.VISIBLE);
            textitem_upload_deal_time.setText(TimeUtil.getStringTimeYMDChines(new Date(eventDetail.getEvent().getYjwcsj())));
        } else {
            textitem_upload_deal_time.setVisibility(View.GONE);
        }
        textfield_description.setText(eventDetail.getEvent().getDescription());
        if (!ListUtil.isEmpty(eventDetail.getEvent().getFiles())) {
            List<Photo> photoList = new ArrayList<>();
            for (EventDetail.EventBean.FilesBean filesBean : eventDetail.getEvent().getFiles()) {
                Photo photo = new Photo();
                photo.setHasBeenUp(1);
                photo.setPhotoPath(filesBean.getPath());
                photo.setThumbPath(filesBean.getThumbPath());
                photo.setField1("photo");
                photoList.add(photo);
            }
            photo_item.setSelectedPhotos(photoList);
        }

                        /*String currLink = mEventListItem.getActivityName();
                        if (eventPState == GzpsConstant.EVENT_P_STATE_UNSIGN) {
                            ll_signevent.setVisibility(View.VISIBLE);
                        }
                        if (eventPState == GzpsConstant.EVENT_P_STATE_UNFINISH) {
                            ll_add_advice.setVisibility(View.VISIBLE);
                        }
                        if (eventPState == GzpsConstant.EVENT_P_STATE_HANDLING) {
                            ll_eventhandle.setVisibility(View.VISIBLE);
                            if (GzpsConstant.LINK_GET_TASK.equals(currLink)) {
                                btn_add_journal.setVisibility(View.VISIBLE);
                            }
                        }*/

        User user = new LoginService(EventDetailActivity.this.getApplicationContext(), AMDatabase.getInstance()).getUser();
        boolean allow = checkIfCurrentUserAllowAddAdvice();
        if (allow) {
            ll_add_advice.setVisibility(View.VISIBLE);
        }

        //如果当前任务状态是待办，且环节处理人是当前用户，则显示任务处理的按钮
        if (eventPState == GzpsConstant.EVENT_P_STATE_HANDLING
                && user.getUserName().equals(mEventDetail.getCurOpLoginName())) {
            ll_eventhandle.setVisibility(View.VISIBLE);
        }

        if (readOnly) {
            ll_eventhandle.setVisibility(View.GONE);
            ll_add_advice.setVisibility(View.GONE);
            ll_signevent.setVisibility(View.GONE);
        }

//                        photo_item.setSelectedPhotos(eventDetail.get());

                        /*if (!ListUtil.isEmpty(eventDetail.getOpinion())){
                            for (EventProcess eventProcess : eventDetail.getEventProcess()){
                                EventProcessView eventProcessView = new EventProcessView(EventDetailActivity.this);
                                eventProcessView.initView(eventProcess);
                                eventProcessView.addTo(ll_event_process);
                            }
                        }*/

                        /*//显示领导插话（意见）列表
                        if (!ListUtil.isEmpty(eventDetail.getOpinion())) {
                            for (EventDetail.OpinionBean opinion : eventDetail.getOpinion()) {
                                AdviceView adviceView = new AdviceView(EventDetailActivity.this);
                                adviceView.initView(opinion);
                                adviceView.addTo(ll_advice);
                            }
                        }*/

        if ("true".equals(mEventDetail.getEvent().getIsbyself())) {
            //自行处理，不走流程
            findViewById(R.id.tv_isbyself).setVisibility(View.VISIBLE);
        } else {
//                            getEventHandlesAndJournals();
        }
        //获取事件处理过程、施工日志及评论意见
        /*AESEncodeSJID(new Callback2<String>() {
            @Override
            public void onSuccess(String s) {
                getEventHandlesAndJournals(s);
            }

            @Override
            public void onFail(Exception error) {
                ToastUtil.shortToast(EventDetailActivity.this, "无法获取处理情况");
            }
        });*/
        getEventHandlesAndJournals("" + mEventDetail.getEvent().getReportId());

        if (GzpsConstant.LINK_SEND_TASK.equals(mEventDetail.getCurNode())) {
            btn_submit.setText("派发任务");
            //    User user = new LoginService(getApplicationContext(), AMDatabase.getInstance()).getUser();
            //如果当前角色是Rg和Rm,则任务排放按钮不可见
            if (user.getRoleCode().contains(GzpsConstant.roleCodes[3])
                    || user.getRoleCode().contains(GzpsConstant.roleCodes[4])) {
                btn_submit.setVisibility(View.GONE);
            }

        } else if (GzpsConstant.LINK_FH.equals(mEventDetail.getCurNode())) {
//                            btn_submit.setText("填写意见");
        } else if (GzpsConstant.LINK_GET_TASK.equals(mEventDetail.getCurNode())) {
            btn_submit.setText("完成施工");
        }

        //问题上报环节不允许回退
        if (GzpsConstant.LINK_PROBLEM_REPORT.equals(mEventDetail.getCurNode())) {
            btn_rollback.setVisibility(View.GONE);
        }

        //任务派发环节可以转派
        if (GzpsConstant.LINK_SEND_TASK.equals(mEventDetail.getCurNode())) {
            btn_reassign.setVisibility(View.VISIBLE);
        }

//                        handleProcessAdapter.addUpData(eventDetail.getEventProcess());
//                        handleProcessAdapter.notifyDataSetChanged();
//
//                        eventAdviceAdapter.addUpData(eventDetail.getAdvice());
//                        eventAdviceAdapter.notifyDataSetChanged();

        //后端字段控制是否可以撤回
        if (mEventDetail.isRetrieve()) {
            ll_retrieve.setVisibility(View.VISIBLE);
        } else {
            ll_retrieve.setVisibility(View.GONE);
        }

        //后端字段控制是否可以删除
        if (mEventDetail.isDeleteTask()) {
            ll_delete.setVisibility(View.VISIBLE);
        } else {
            ll_delete.setVisibility(View.GONE);
        }

        //问题上报环节而且当前用户是上报人,可进行编辑问题
//        if(GzpsConstant.LINK_PROBLEM_REPORT.equals(mEventDetail.getCurNode()) &&
//                (mEventDetail.getEvent().getLoginName().contains(user.getLoginName()))){
//            ll_edit.setVisibility(View.VISIBLE);
//        }else {
//            ll_edit.setVisibility(View.GONE);
//        }
        if (mEventDetail.isEditAble()) {
            ll_edit.setVisibility(View.VISIBLE);
        } else {
            ll_edit.setVisibility(View.GONE);
        }
    }

    private void initView() {
        pd = new ProgressDialog(this);
        ((TextView) findViewById(R.id.tv_title)).setText("事件详情");

        textitem_upload_user = (TextItemTableItem) findViewById(R.id.textitem_upload_user);
        textitem_parent_org = (TextItemTableItem) findViewById(R.id.textitem_parent_org);
        textitem_upload_date = (TextItemTableItem) findViewById(R.id.textitem_upload_date);
        textitem_upload_address = (TextItemTableItem) findViewById(R.id.textitem_upload_address);
        tv_check_location = (TextView) findViewById(R.id.tv_check_location);
        ll_report_addr = findViewById(R.id.ll_report_addr);
        tv_check_component_location = (TextView) findViewById(R.id.tv_check_component_location);
        ll_addr = findViewById(R.id.ll_addr);
        textitem_upload_compttype = (TextItemTableItem) findViewById(R.id.textitem_upload_compttype);
        textitem_upload_eventtype = (TextItemTableItem) findViewById(R.id.textitem_upload_eventtype);
        textitem_upload_urgency = (TextItemTableItem) findViewById(R.id.textitem_upload_urgency);
        textitem_upload_deal_time = (TextItemTableItem) findViewById(R.id.textitem_upload_deal_time);
        textfield_description = (TextFieldTableItem) findViewById(R.id.textfield_description);
        photo_item = (MultiTakePhotoTableItem) findViewById(R.id.photo_item);
        pb_loading = (ProgressLinearLayout) findViewById(R.id.pb_loading);

        textitem_upload_user.setReadOnly();
        textitem_parent_org.setEditable(false);
        textitem_upload_date.setReadOnly();
        textitem_upload_address.setReadOnly();
        textitem_upload_compttype.setEditable(false);
        textitem_upload_eventtype.setEditable(false);
        textitem_upload_urgency.setEditable(false);
        textitem_upload_deal_time.setEditable(false);
        textfield_description.setReadOnly();
        photo_item.setAddPhotoEnable(false);
        photo_item.setTitle("现场照片");
        photo_item.setReadOnly();

        ll_advice = (LinearLayout) findViewById(R.id.ll_advice);
        ll_event_process = (LinearLayout) findViewById(R.id.ll_event_process);
        ll_signevent = findViewById(R.id.ll_signevent);
        ll_eventhandle = findViewById(R.id.ll_eventhandle);
        ll_add_advice = findViewById(R.id.ll_add_advice);

//        rv_handle_process = (RecyclerView) findViewById(R.id.rv_handle_process);
//        rv_handle_process.setLayoutManager(new LinearLayoutManager(this));
//        handleProcessAdapter = new HandleProcessAdapter(this,new ArrayList<EventProcess>());
//        rv_handle_process.setAdapter(handleProcessAdapter);
//
//        rv_advice = (RecyclerView) findViewById(R.id.rv_advice);
//        rv_advice.setLayoutManager(new LinearLayoutManager(this));
//        eventAdviceAdapter = new EventAdviceAdapter(this,new ArrayList<EventAdvice>());
//        rv_advice.setAdapter(eventAdviceAdapter);

        btn_signevent = findViewById(R.id.btn_signevent);
        btn_rollback = findViewById(R.id.btn_rollback);
        btn_reassign = findViewById(R.id.btn_reassign);
        btn_submit = (TextView) findViewById(R.id.btn_submit);
        btn_add_journal = findViewById(R.id.btn_add_journal);
        btn_addvice = findViewById(R.id.btn_addvice);


        String currLinkName = mEventListItem.getActivityChineseName();
        String currLink = mEventListItem.getActivityName();
        /*User user = new LoginService(this.getApplicationContext(), AMDatabase.getInstance()).getUser();
        if (eventPState == GzpsConstant.EVENT_P_STATE_HANDLING
                && !user.getLoginName().equals(mEventDetail.getCurOpLoginName())) {
            ll_add_advice.setVisibility(View.VISIBLE);
        }*/
        if (eventPState == GzpsConstant.EVENT_P_STATE_HANDLING) {
            ll_eventhandle.setVisibility(View.VISIBLE);
            if (GzpsConstant.LINK_GET_TASK_NAME.equals(currLinkName)) {
                btn_add_journal.setVisibility(View.VISIBLE);
            }
        }

        if (!GzpsConstant.EVENT_OPEN.equals(mEventListItem.getState())) {
            ll_eventhandle.setVisibility(View.GONE);
        }
//        ll_eventhandle.setVisibility(View.VISIBLE);


        //处理中才会显示撤回按钮
        ll_retrieve = (View) findViewById(R.id.ll_retrieve);
        btn_retrieve = (Button) findViewById(R.id.btn_retrieve);

        if (eventPState == GzpsConstant.EVENT_P_STATE_HANDLED) {
            ll_retrieve.setVisibility(View.VISIBLE);
        } else {
            ll_retrieve.setVisibility(View.GONE);
        }


        //待受理中才会显示删除按钮
        ll_delete = (View) findViewById(R.id.ll_delete);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        /*
        if(eventPState == GzpsConstant.EVENT_P_STATE_HANDLING){
            ll_delete.setVisibility(View.VISIBLE);
        }else {
            ll_delete.setVisibility(View.GONE);
        }
        */
        ll_edit = findViewById(R.id.ll_edit);
        btn_edit = findViewById(R.id.btn_edit);


    }

    private void initListener() {
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.share_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AESEncodeSJID(new Callback2<String>() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            String url = BaseInfoManager.getSupportUrl(EventDetailActivity.this.getApplicationContext())
                                    + "event/eventDetail.html?";
                            String param = "sjid=" + URLEncoder.encode(s);
                            url += param;
                            Intent wechatIntent = new Intent(Intent.ACTION_SEND);
                            wechatIntent.setPackage("com.tencent.mm");
                            wechatIntent.setType("text/plain");
                            wechatIntent.putExtra(Intent.EXTRA_TEXT, url);
                            startActivity(wechatIntent);
                        } catch (Exception e) {
                            ToastUtil.shortToast(EventDetailActivity.this, "请确认手机是否已安装微信");
                        }
                    }

                    @Override
                    public void onFail(Exception error) {
                        ToastUtil.shortToast(EventDetailActivity.this, "无法连接网络，请检查网络设置");
                    }
                });
            }
        });

        ll_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEventDetail.getEvent() == null) {
                    return;
                }
                if ((!"1".equals(mEventDetail.getEvent().getSfjb()) && !"2".equals(mEventDetail.getEvent().getSfjb())) && ("-1".equals(mEventDetail.getEvent().getLayerId())
                        || "-1".equals(mEventDetail.getEvent().getObjectId()))) {
                    //选的是地址
                    String x = mEventDetail.getEvent().getX();
                    String y = mEventDetail.getEvent().getY();
                    String addr = mEventDetail.getEvent().getAddr();

                    if (StringUtil.isEmpty(x)
                            || StringUtil.isEmpty(y)) {
                        ToastUtil.shortToast(EventDetailActivity.this, "地址信息缺失");
                        return;
                    }

                    Intent intent = new Intent(EventDetailActivity.this, SelectLocationActivity.class);
                    intent.putExtra(SelectLocationConstant.IF_READ_ONLY, true);
                    intent.putExtra(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_LOCATION, new LatLng(Double.valueOf(y),
                            Double.valueOf(x)));
                    intent.putExtra(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_ADDRESS, addr);
                    intent.putExtra(SelectLocationConstant.INITIAL_SCALE, PatrolLayerPresenter.initScale);
                    startActivity(intent);
                } else {
                    /*if(!LayerUrlConstant.ifLayerUrlInitSuccess()){
                        ToastUtil.shortToast(getApplicationContext(), getResources().getString(R.string.layer_url_init_fail_msg));
                        return;
                    }*/
                    //选的是部件
                    String layerId = mEventDetail.getEvent().getLayerId();
                    String layerUrl = mEventDetail.getEvent().getLayerurl();
                    String layerName = mEventDetail.getEvent().getLayerName();
                    String objectId = mEventDetail.getEvent().getObjectId();
                    String usid = mEventDetail.getEvent().getUsid();
                    String x = mEventDetail.getEvent().getX();
                    String y = mEventDetail.getEvent().getY();

                    Intent intent = new Intent(EventDetailActivity.this, EventDetailMapActivity.class);
                    intent.putExtra("layerUrl", layerUrl);
                    intent.putExtra("layerName", layerName);
                    intent.putExtra("x", Double.valueOf(x));
                    intent.putExtra("y", Double.valueOf(y));
                    intent.putExtra("usid", usid);
                    intent.putExtra("objectId", objectId);
                    startActivity(intent);

                }

            }
        });

        ll_report_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = mEventDetail.getEvent().getReportx();
                String y = mEventDetail.getEvent().getReporty();
                String addr = mEventDetail.getEvent().getReportaddr();

                if (StringUtil.isEmpty(x)
                        || StringUtil.isEmpty(y)) {
                    ToastUtil.shortToast(EventDetailActivity.this, "地址信息缺失");
                    return;
                }

                Intent intent = new Intent(EventDetailActivity.this, SelectLocationActivity.class);
                intent.putExtra(SelectLocationConstant.IF_READ_ONLY, true);
                intent.putExtra(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_LOCATION, new LatLng(Double.valueOf(y),
                        Double.valueOf(x)));
                intent.putExtra(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_ADDRESS, addr);
                intent.putExtra(SelectLocationConstant.INITIAL_SCALE, PatrolLayerPresenter.initScale);
                startActivity(intent);
            }
        });

        btn_signevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signEvent();
            }
        });

        btn_rollback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollback();
            }
        });

        btn_reassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reassign();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        btn_add_journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetailActivity.this, AddEventJournalActivity.class);
                intent.putExtra("taskInstDbid", mEventListItem.getTaskInstDbid());
                intent.putExtra("sjid", mEventDetail.getEvent().getReportId() + "");
                startActivity(intent);
            }
        });

        btn_addvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAdviceDialog();
            }
        });


        //向服务器请求撤回
        btn_retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetriveDialog dialog = RetriveDialog.getInstance(
                        String.valueOf(mProcInstDbId));
                dialog.show(getSupportFragmentManager(), "retriveback");
//                AlertDialog dialog = new AlertDialog.Builder(EventDetailActivity.this)
//                        .setTitle("提示:")
//                        .setMessage("确定撤回当前任务吗?")
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                retrive(null);
//                            }
//                        })
//                        .create();
//                dialog.show();
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.agmobile_red));
//                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(20);
//                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(20);
            }

        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(EventDetailActivity.this)
                        .setTitle("提示:")
                        .setMessage("任务删除后所有数据将无法恢复,是否确定删除当前任务?")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete();
                            }
                        })
                        .create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.agmobile_red));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(20);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(20);
            }
        });

        //重新编辑问题
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventDetailActivity.this, EventEditActivity.class);
                ProblemUploadBean bean = getProblemUploadBean();
                if (bean != null) {
                    intent.putExtra("preProblemUploadBean", bean);
                    intent.putExtra("reportId", mEventDetail.getEvent().getReportId());
                }
                startActivity(intent);

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

    private String getSpinnerValuesByMultiCode(String codes) {
        if (StringUtil.isEmpty(codes)) {
            return "";
        }
        String[] codeArray = codes.split(",");
        String value = "";
        for (String code : codeArray) {
            String bhlx = getSpinnerValueByCode(code);
            if (!TextUtils.isEmpty(bhlx) && !"其他".equals(bhlx)) {
                value = value + "，" + bhlx;
            }
        }
        if (!TextUtils.isEmpty(value)) {
            value = value.substring(1);
        }
        return value;
    }


    private ProblemUploadBean getProblemUploadBean() {
        ProblemUploadBean bean = new ProblemUploadBean();
        if (mEventDetail != null) {
            bean.setSBR(mEventDetail.getEvent().getUserName());
            bean.setReportaddr(mEventDetail.getEvent().getReportaddr());
            bean.setReportx(mEventDetail.getEvent().getReportx());
            bean.setReporty(mEventDetail.getEvent().getReporty());
            bean.setSSLX(mEventDetail.getEvent().getComponentType());
            bean.setBHLX(mEventDetail.getEvent().getEventType());
            bean.setJJCD(urgency_type_array[Integer.valueOf(
                    mEventDetail.getEvent().getUrgency()) - 1]);
            bean.setWTMS(mEventDetail.getEvent().getDescription());

            bean.setBZ(mEventDetail.getEvent().getRemark());
            bean.setJDMC(mEventDetail.getEvent().getRoad());
            bean.setSZWZ(mEventDetail.getEvent().getAddr());
            bean.setX(mEventDetail.getEvent().getX());
            bean.setY(mEventDetail.getEvent().getY());
            bean.setLayer_id(mEventDetail.getEvent().getLayerId());
            bean.setLayer_name(mEventDetail.getEvent().getLayerName());
            bean.setLayerurl(mEventDetail.getEvent().getLayerurl());
            bean.setObject_id(mEventDetail.getEvent().getObjectId());
            bean.setUsid(mEventDetail.getEvent().getUsid());
            bean.setSfjb(mEventDetail.getEvent().getSfjb());
            bean.setYjwcsj(mEventDetail.getEvent().getYjwcsj());

            if (!ListUtil.isEmpty(mEventDetail.getEvent().getFiles())) {
                List<Photo> photoList = new ArrayList<>();
                for (EventDetail.EventBean.FilesBean filesBean : mEventDetail.getEvent().getFiles()) {
                    Photo photo = new Photo();
                    photo.setHasBeenUp(1);
                    photo.setPhotoPath(filesBean.getPath());
                    photo.setPhotoName(filesBean.getName());
                    photo.setField1("photo");
                    photoList.add(photo);
                }
                bean.setPhotos(photoList);
            }
        }
        return bean;

    }

    /**
     * 撤回
     */
//    private void retrive(String retrieveComments){
//        pd.show();
//        mEventService.wfRetrive(mProcInstDbId,retrieveComments)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Result<String>>() {
//                    @Override
//                    public void onCompleted() {
//                        pd.dismiss();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        pd.dismiss();
//                    }
//
//                    @Override
//                    public void onNext(Result<String> stringResult) {
//                        pd.dismiss();
//                        if(stringResult.isSuccess()){
//                            ToastUtil.longToast(EventDetailActivity.this,"撤回成功!");
//                            EventBus.getDefault().post(new UpdateAfterRetriveEvent(mProcInstDbId));
//
//                            finish();
//                        }else {
//                            if(!TextUtils.isEmpty(stringResult.getMessage())){
//                                ToastUtil.longToast(EventDetailActivity.this,stringResult.getMessage());
//                            }else {
//                                ToastUtil.longToast(EventDetailActivity.this,"撤回失败!");
//                            }
//                        }
//                    }
//                });
//
//    }

    /**
     * 删除
     */
    private void delete() {
        pd.show();
        mEventService.wfDelete(String.valueOf(mEventDetail.getEvent().getReportId()), mProcInstDbId, mEventDetail.getTaskInstId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<String>>() {
                    @Override
                    public void onCompleted() {
                        pd.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                    }

                    @Override
                    public void onNext(Result<String> stringResult) {
                        pd.dismiss();
                        if (stringResult != null && stringResult.isSuccess()) {
                            ToastUtil.longToast(EventDetailActivity.this, "删除成功!");
                            EventBus.getDefault().post(new UpdateAfterDeleteEvent(mProcInstDbId));
                            finish();
                        } else {
                            if (stringResult != null && !TextUtils.isEmpty(stringResult.getMessage())) {
                                ToastUtil.longToast(EventDetailActivity.this, stringResult.getMessage());
                            } else {
                                ToastUtil.longToast(EventDetailActivity.this, "无法删除!");
                            }
                        }
                    }
                });

    }

    /**
     * 加密问题ID，然后获取问题处理情况、施工日志、管理层意见
     */
    private void AESEncodeSJID(final Callback2<String> callback) {
        GzpsService gzpsService = new GzpsService(this.getApplicationContext());
        gzpsService.AESEncode("" + mEventDetail.getEvent().getReportId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result2<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.shortToast(EventDetailActivity.this, "无法获取处理情况！");
                    }

                    @Override
                    public void onNext(Result2<String> stringResult2) {
                        if (stringResult2.getCode() == 200
                                && !StringUtil.isEmpty(stringResult2.getData())) {
                            callback.onSuccess(stringResult2.getData());
                        } else {
                            callback.onFail(new Exception(""));
                        }

                    }
                });
    }

    /**
     * 获取处理情况、施工日志及评论意见
     */
    private void getEventHandlesAndJournals(String sjid) {

        mEventService.getEventHandlesAndJournals(sjid + "", 0, 100)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<EventProcess>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                    }

                    @Override
                    public void onNext(List<EventProcess> eventProcessList) {
                        journalPageNo++;
                        if (!ListUtil.isEmpty(eventProcessList)) {
                            ll_advice.removeAllViews();
                            ll_event_process.removeAllViews();


                            List<EventProcess> realEventProcess = new ArrayList<>();
                            for (EventProcess eventProcess : eventProcessList) {
                                if (!StringUtil.isEmpty(eventProcess.getLx())
                                        && eventProcess.getLx().equals("1")) {
                                    EventDetail.OpinionBean opinion = new EventDetail.OpinionBean();
                                    opinion.setOpinion(eventProcess.getContent());
                                    opinion.setUserName(eventProcess.getOpUser());
                                    opinion.setTime(eventProcess.getTime());
                                    //显示领导插话（意见）列表
                                    AdviceView adviceView = new AdviceView(EventDetailActivity.this);
                                    adviceView.initView(opinion);
                                    adviceView.addTo(ll_advice);
                                } else {
                                    realEventProcess.add(eventProcess);
                                }
                            }

                            if ("true".equals(mEventDetail.getEvent().getIsbyself())) {
                                //如果是自行处理，不显示处理过程
                                return;
                            }

                            int index = 0;
                            boolean isFinished = false;
                            /*if(GzpsConstant.LINK_FH.equals(mEventDetail.getCurNode())
                                    && mEventDetail.getEventState().contains("已完成")){
                                isFinished = true;
                            }*/
                            if (StringUtil.isEmpty(mEventDetail.getState())
                                    || GzpsConstant.EVENT_SIMPLE_STATE_ENDED.equals(mEventDetail.getState())) {
                                isFinished = true;
                            }
                            for (EventProcess eventProcess : realEventProcess) {

                                if (!isFinished && (index == realEventProcess.size() - 1)) {
                                    continue;
                                }
                                EventProcessView eventProcessView = new EventProcessView(EventDetailActivity.this);
                                eventProcessView.initView(eventProcess, isFinished, index, realEventProcess.size());
                                eventProcessView.addTo(ll_event_process);

                                index++;
                            }
                        }
                    }
                });
    }

    /**
     * 检查当前用户是否允许对该事件发表意见
     *
     * @return
     */
    private boolean checkIfCurrentUserAllowAddAdvice() {
        boolean allow = false;
        User user = new LoginService(EventDetailActivity.this.getApplicationContext(), AMDatabase.getInstance()).getUser();

        for (String district : GzpsConstant.districtsSimple) {
            if (user.getOrgName().contains(district)) {   //筛选出用户所在机构
                if (mEventDetail.getEvent().getParentOrgName().contains(district)) {   //事件上报所在区域
                    if (user.getRoleCode().contains(GzpsConstant.roleCodes[3])
                            || user.getRoleCode().contains(GzpsConstant.roleCodes[4])) {
                        //Rg和Rm角色才能发表意见
                        allow = true;
                    }
                }
            }

        }
        if (user.getOrgName().contains(GzpsConstant.districtsSimple[0])) {
            if (user.getRoleCode().contains(GzpsConstant.roleCodes[3])
                    || user.getRoleCode().contains(GzpsConstant.roleCodes[4])) {
                //Rg和Rm角色才能发表意见
                allow = true;
            }
        }
        return allow;
    }

    private void signEvent() {
        mEventService.signEvent(mEventListItem.getTaskInstDbid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        pd.show();
                    }
                })
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        pd.dismiss();
                        ToastUtil.shortToast(EventDetailActivity.this, "操作失败，请重试！");
                    }

                    @Override
                    public void onNext(Boolean s) {
                        if (s) {
                            pd.dismiss();
                            ToastUtil.shortToast(EventDetailActivity.this, "操作成功");
                            setResult(123);
                            finish();
                        } else {
                            pd.dismiss();
                            ToastUtil.shortToast(EventDetailActivity.this, "操作失败，请重试！");
                        }
                    }
                });
    }

    /**
     * 回退
     */
    private void rollback() {
        RollBackDialog rollBackDialog = RollBackDialog.getInstance(
                String.valueOf(mEventDetail.getEvent().getReportId()),
                mEventListItem.getTaskInstDbid(),
                mEventDetail.getCurNode());
        rollBackDialog.show(getSupportFragmentManager(), "rollback");
    }

    /**
     * 转派
     */
    private void reassign() {
        String activityName = mEventListItem.getActivityName();
        long sjid = mEventDetail.getEvent().getReportId(); //masterEntityKey

        ReassignDialog reassignDialog = ReassignDialog.getInstance(mEventDetail.getTaskInstId(),
                mEventDetail.getCurNode(), mEventDetail.getCurNodeName(), mProcInstDbId,
                activityName, String.valueOf(sjid));
        reassignDialog.show(getSupportFragmentManager(), "reassign");
    }

    private void submit() {
        ArrayList<EventDetail.NextlinkBean> nextlinkBeans = new ArrayList<>(mEventDetail.getNextlink());
        Intent intent = new Intent(EventDetailActivity.this, SendNextLinkActivity.class);
        intent.putExtra("taskInstId", mEventListItem.getTaskInstDbid());
        intent.putExtra("currLinkCode", mEventDetail.getCurNode());

        intent.putExtra("procInstId", mProcInstDbId);
        intent.putExtra("sjid", String.valueOf(mEventDetail.getEvent().getReportId()));

        intent.putExtra("nextLink", nextlinkBeans);
        startActivityForResult(intent, 456);
        /*SendNextLinkDialog sendNextLinkDialog = SendNextLinkDialog.getInstance(mEventListItem.getTaskInstDbid(),
                mEventListItem.getActivityName(), nextlinkBeans);
        sendNextLinkDialog.show(getSupportFragmentManager(), "addEventAdvice");*/
    }

    private void showAddAdviceDialog() {
        AddEventAdviceDialog addEventAdviceDialog = AddEventAdviceDialog.getInstance(mEventListItem.getTaskInstDbid(), mEventDetail.getEvent().getReportId() + "");
        addEventAdviceDialog.show(getSupportFragmentManager(), "addEventAdvice");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 456
                && resultCode == 456) {
            setResult(123);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateAdviceEvent(UpdateAdviceEvent event) {
        if (event != null && event.getOpinion() != null) {
            AdviceView adviceView = new AdviceView(this);
            adviceView.initView(event.getOpinion());
            adviceView.addTo(ll_advice);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateJounaryEvent(UpdateJournalEvent event) {

        getEventHandlesAndJournals("" + mEventDetail.getEvent().getReportId());
        /*AESEncodeSJID(new Callback2<String>() {
            @Override
            public void onSuccess(String s) {
                getEventHandlesAndJournals(s);
            }

            @Override
            public void onFail(Exception error) {
                ToastUtil.shortToast(EventDetailActivity.this, "无法获取处理情况");
            }
        });*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEditEvent(UpdateAfterEditEvent editEvent) {
        if (editEvent.getUpdateState().equals(UpdateAfterEditEvent.UPDATE_DETAIL)) {
            //编辑成功,重新刷新详情页面
            initData();
        } else if (editEvent.getUpdateState().equals(UpdateAfterEditEvent.UPDATE_LIST)) {
            //编辑成功,并且在编辑中已经指派了下一环节处理人了,则退出当前详情页面,并且刷新列表
            EventBus.getDefault().post(new UpdateAfterDeleteEvent(mProcInstDbId));
            finish();
        }
    }
}
