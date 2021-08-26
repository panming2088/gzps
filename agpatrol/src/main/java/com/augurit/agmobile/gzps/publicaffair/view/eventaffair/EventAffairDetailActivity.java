package com.augurit.agmobile.gzps.publicaffair.view.eventaffair;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.constant.GzpsConstant;
import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.service.GzpsService;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.publicaffair.event.UpdateAdviceEvent;
import com.augurit.agmobile.gzps.publicaffair.model.EventAffair;
import com.augurit.agmobile.gzps.uploadevent.model.EventDetail;
import com.augurit.agmobile.gzps.uploadevent.model.EventProcess;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.gzps.uploadevent.view.eventflow.AddEventAdviceDialog;
import com.augurit.agmobile.gzps.uploadevent.view.eventflow.AdviceView;
import com.augurit.agmobile.gzps.uploadevent.view.eventflow.EventProcessView;
import com.augurit.agmobile.gzps.uploadevent.view.eventlist.EventDetailMapActivity;
import com.augurit.agmobile.gzps.uploadfacility.view.feedback.FeedbackEventMapActivity;
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
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
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
import rx.schedulers.Schedulers;

/**
 * Created by liangsh on 2017/11/17.
 */

public class EventAffairDetailActivity extends BaseActivity {

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

    private LinearLayout ll_advice;
    private LinearLayout ll_event_process;
    private View ll_add_advice;
    private View btn_addvice;
    private boolean fromJournal;
    private EventAffair.EventAffairBean mEventAffairBean;
    private UploadEventService mEventService;
    private TextItemTableItem textitem_upload_deal_time;

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_event_affair_detail);
        mEventAffairBean = (EventAffair.EventAffairBean) getIntent().getSerializableExtra("eventAffairBean");
        mEventService = new UploadEventService(this.getApplicationContext());

        initView();
        initData();

        EventBus.getDefault().register(this);
    }


    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("事件详情");
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        ll_event_process = (LinearLayout) findViewById(R.id.ll_event_process);
        ll_advice = (LinearLayout) findViewById(R.id.ll_advice);
        ll_add_advice = findViewById(R.id.ll_add_advice);
        btn_addvice = findViewById(R.id.btn_addvice);


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
        photo_item.setTitle("现场图片");
        photo_item.setReadOnly();

        ll_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!"1".equals(mEventAffairBean.getSfjb()) && !"2".equals(mEventAffairBean.getSfjb())) && ("-1".equals(mEventAffairBean.getLayerId())
                        || "-1".equals(mEventAffairBean.getObjectId()))) {
                    //选的是地址
                    String x = mEventAffairBean.getX();
                    String y = mEventAffairBean.getY();
                    String addr = mEventAffairBean.getSzwz();

                    if (StringUtil.isEmpty(x)
                            || StringUtil.isEmpty(y)) {
                        ToastUtil.shortToast(EventAffairDetailActivity.this, "地址信息缺失");
                        return;
                    }

                    Intent intent = new Intent(EventAffairDetailActivity.this, SelectLocationActivity.class);
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
                    String layerId = mEventAffairBean.getLayerId() + "";
                    String layerUrl = mEventAffairBean.getLayerurl();
                    String layerName = mEventAffairBean.getLayerName();
                    String objectId = mEventAffairBean.getObjectId();
                    String usid = mEventAffairBean.getUsid();
                    String x = mEventAffairBean.getX();
                    String y = mEventAffairBean.getY();

                    if (StringUtil.isEmpty(x)
                            || StringUtil.isEmpty(y)
                            || (StringUtil.isEmpty(usid) && StringUtil.isEmpty(objectId))) {
                        ToastUtil.shortToast(EventAffairDetailActivity.this, "设施位置信息缺失");
                        return;
                    }
                    if ("1".equals(mEventAffairBean.getSfjb())) {
                        if (!StringUtil.isEmpty(usid)) {
                            Intent intent = new Intent(EventAffairDetailActivity.this, EventDetailMapActivity.class);
                            intent.putExtra("layerUrl", layerUrl);
                            intent.putExtra("layerName", layerName);
                            intent.putExtra("x", Double.valueOf(x));
                            intent.putExtra("y", Double.valueOf(y));
                            intent.putExtra("usid", usid);
                            intent.putExtra("objectId", mEventAffairBean.getObjectId());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(EventAffairDetailActivity.this, FeedbackEventMapActivity.class);
                            intent.putExtra("layerUrl", layerUrl);
                            intent.putExtra("layerName", layerName);
                            intent.putExtra("x", Double.valueOf(x));
                            intent.putExtra("y", Double.valueOf(y));
                            intent.putExtra("object_id", objectId);
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(EventAffairDetailActivity.this, EventDetailMapActivity.class);
                        intent.putExtra("layerUrl", layerUrl);
                        intent.putExtra("layerName", layerName);
                        intent.putExtra("x", Double.valueOf(x));
                        intent.putExtra("y", Double.valueOf(y));
                        intent.putExtra("usid", usid);
                        intent.putExtra("objectId", mEventAffairBean.getObjectId());
                        startActivity(intent);
                    }
                }

            }
        });

        ll_report_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = mEventAffairBean.getReportx();
                String y = mEventAffairBean.getReporty();
                String addr = mEventAffairBean.getReportaddr();

                if (StringUtil.isEmpty(x)
                        || StringUtil.isEmpty(y)) {
                    ToastUtil.shortToast(EventAffairDetailActivity.this, "地址信息缺失");
                    return;
                }

                Intent intent = new Intent(EventAffairDetailActivity.this, SelectLocationActivity.class);
                intent.putExtra(SelectLocationConstant.IF_READ_ONLY, true);
                intent.putExtra(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_LOCATION, new LatLng(Double.valueOf(y),
                        Double.valueOf(x)));
                intent.putExtra(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_ADDRESS, addr);
                intent.putExtra(SelectLocationConstant.INITIAL_SCALE, PatrolLayerPresenter.initScale);
                startActivity(intent);
            }
        });
        findViewById(R.id.share_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new LoginService(EventAffairDetailActivity.this.getApplicationContext(), AMDatabase.getInstance()).getUser();
//                    String url = "来自排水app分享";
                AESEncodeSJID(new Callback2<String>() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            String url = BaseInfoManager.getSupportUrl(EventAffairDetailActivity.this.getApplicationContext())
                                    + "event/eventDetail.html?";
                            String param = "sjid=" + URLEncoder.encode(s);
                            url += param;
                            Intent wechatIntent = new Intent(Intent.ACTION_SEND);
                            wechatIntent.setPackage("com.tencent.mm");
                            wechatIntent.setType("text/plain");
                            wechatIntent.putExtra(Intent.EXTRA_TEXT, url);
                            startActivity(wechatIntent);
                        } catch (Exception e) {
                            ToastUtil.shortToast(EventAffairDetailActivity.this, "请确认手机是否已安装微信");
                        }
                    }

                    @Override
                    public void onFail(Exception error) {
                        ToastUtil.shortToast(EventAffairDetailActivity.this, "无法连接网络，请检查网络设置");
                    }
                });
            }
        });

        btn_addvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAdviceDialog();
            }
        });

    }


    private void initData() {
        fromJournal = getIntent().getBooleanExtra("from_journal", false);
        textitem_upload_user.setText(mEventAffairBean.getSbr());
        textitem_parent_org.setText(mEventAffairBean.getParentOrgName());
        textitem_upload_date.setText(TimeUtil.getStringTimeYMDS(new Date(mEventAffairBean.getSbsj2())));
        textitem_upload_address.setText(mEventAffairBean.getSzwz());
        textitem_upload_compttype.setText(getSpinnerValueByCode(mEventAffairBean.getSslx()));

        String textOther = "";
        if (mEventAffairBean.getBhlx().contains("其他")) {
            String[] split = mEventAffairBean.getBhlx().split("其他");
            if (split.length > 1) {
                textOther = "其他" + split[1];
            }
        }

        String bhlx = getSpinnerValuesByMultiCode(mEventAffairBean.getBhlx());
        if (!TextUtils.isEmpty(bhlx)) {
            if(!TextUtils.isEmpty(textOther)) {
                textitem_upload_eventtype.setText(bhlx + "，" + textOther);
            }else{
                textitem_upload_eventtype.setText(bhlx);
            }
        } else {
            textitem_upload_eventtype.setText(textOther);
        }

        try {
            textitem_upload_urgency.setText(urgency_type_array[Integer.valueOf(mEventAffairBean.getJjcd()) - 1]);
        } catch (Exception e) {

        }
        tv_check_location.setText(mEventAffairBean.getReportaddr());
        if ("1".equals(mEventAffairBean.getSfjb()) && "false".equals(mEventAffairBean.getIsbyself())) {
            textitem_upload_deal_time.setVisibility(View.VISIBLE);
            textitem_upload_deal_time.setText(TimeUtil.getStringTimeYMDChines(new Date(mEventAffairBean.getYjwcsj2())));
        } else {
            textitem_upload_deal_time.setVisibility(View.GONE);
        }
        tv_check_component_location.setText(mEventAffairBean.getSzwz());
        textfield_description.setText(mEventAffairBean.getWtms());
        if (!ListUtil.isEmpty(mEventAffairBean.getFiles())) {
            List<Photo> photoList = new ArrayList<>();
            for (EventDetail.EventBean.FilesBean filesBean : mEventAffairBean.getFiles()) {
                Photo photo = new Photo();
                photo.setHasBeenUp(1);
                photo.setPhotoPath(filesBean.getPath());
                photo.setThumbPath(filesBean.getThumbPath());
                photo.setField1("photo");
                photoList.add(photo);
            }
            photo_item.setSelectedPhotos(photoList);
        }
        boolean allow = checkIfCurrentUserAllowAddAdvice();
        if (allow) {
            ll_add_advice.setVisibility(View.VISIBLE);
        }

        /*
        if ("true".equals(mEventAffairBean.getIsbyself())) {
            //自行处理，不走流程
            findViewById(R.id.tv_isbyself).setVisibility(View.VISIBLE);
        } else {

        }
        */

        //获取事件处理过程、施工日志及评论意见
        /*AESEncodeSJID(new Callback2<String>() {
            @Override
            public void onSuccess(String s) {
                getEventHandlesAndJournals(s);
            }

            @Override
            public void onFail(Exception error) {
                ToastUtil.shortToast(EventAffairDetailActivity.this, "无法获取处理情况");
            }
        });*/
        getEventHandlesAndJournals("" + mEventAffairBean.getId());

    }

    /**
     * 加密问题ID
     */
    private void AESEncodeSJID(final Callback2<String> callback) {
        GzpsService gzpsService = new GzpsService(this.getApplicationContext());
        gzpsService.AESEncode("" + mEventAffairBean.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result2<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.shortToast(EventAffairDetailActivity.this, "无法获取处理情况！");
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
     * 获取处理情况和施工日志
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
                        if (!ListUtil.isEmpty(eventProcessList)) {
                            List<EventProcess> realEventProcess = new ArrayList<>();
                            for (EventProcess eventProcess : eventProcessList) {
                                if (!StringUtil.isEmpty(eventProcess.getLx())
                                        && eventProcess.getLx().equals("1")) {
                                    EventDetail.OpinionBean opinion = new EventDetail.OpinionBean();
                                    opinion.setOpinion(eventProcess.getContent());
                                    opinion.setUserName(eventProcess.getOpUser());
                                    opinion.setTime(eventProcess.getTime());
                                    //显示领导插话（意见）列表
                                    AdviceView adviceView = new AdviceView(EventAffairDetailActivity.this);
                                    adviceView.initView(opinion);
                                    adviceView.addTo(ll_advice);
                                } else {
                                    realEventProcess.add(eventProcess);
                                }
                            }

                            //                    if ("true".equals(mEventAffairBean.getIsbyself())) {
                            //如果是自行处理
//                                int index = 0;
//                                for (EventProcess eventProcess : realEventProcess) {
//                                    eventProcess.setLinkName("自行处理(已结束)");
//                                    EventProcessView eventProcessView = new EventProcessView(EventAffairDetailActivity.this);
//                                    eventProcessView.initView(eventProcess, true, index, realEventProcess.size());
//                                    eventProcessView.addTo(ll_event_process);
//                                    index++;
//                                }
                            //  return;
                            //                        }

                            int index = 0;
                            boolean isFinished = false;
                            if (StringUtil.isEmpty(mEventAffairBean.getState())
                                    || GzpsConstant.EVENT_SIMPLE_STATE_ENDED.equals(mEventAffairBean.getState())) {
                                isFinished = true;
                            }
                            for (EventProcess eventProcess : realEventProcess) {
                                if (!isFinished && (index == realEventProcess.size() - 1)) {
                                    continue;
                                }
                                EventProcessView eventProcessView = new EventProcessView(EventAffairDetailActivity.this);
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
        User user = new LoginService(EventAffairDetailActivity.this.getApplicationContext(), AMDatabase.getInstance()).getUser();

        for (String district : GzpsConstant.districtsSimple) {
            if (user.getOrgName().contains(district)) {   //筛选出用户所在机构
                if (mEventAffairBean.getParentOrgName().contains(district)) {   //事件上报所在区域
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

    private void showAddAdviceDialog() {
        AddEventAdviceDialog addEventAdviceDialog = AddEventAdviceDialog.getInstance("", mEventAffairBean.getId() + "");
        addEventAdviceDialog.show(getSupportFragmentManager(), "addEventAdvice");
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
            AdviceView adviceView = new AdviceView(EventAffairDetailActivity.this);
            adviceView.initView(event.getOpinion());
            adviceView.addTo(ll_advice);
        }
    }
}
