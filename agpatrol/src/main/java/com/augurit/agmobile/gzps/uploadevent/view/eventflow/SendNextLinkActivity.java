package com.augurit.agmobile.gzps.uploadevent.view.eventflow;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.constant.GzpsConstant;
import com.augurit.agmobile.gzps.common.model.Result;
import com.augurit.agmobile.gzps.common.widget.AutoBreakViewGroup;
import com.augurit.agmobile.gzps.common.widget.MyGridView;
import com.augurit.agmobile.gzps.uploadevent.adapter.NextLinkAssigneersAdapter;
import com.augurit.agmobile.gzps.uploadevent.dao.GetPersonByOrgApiData;
import com.augurit.agmobile.gzps.uploadevent.model.Assigneers;
import com.augurit.agmobile.gzps.uploadevent.model.EventDetail;
import com.augurit.agmobile.gzps.uploadevent.model.NextLinkOrg;
import com.augurit.agmobile.gzps.uploadevent.model.OrgItem;
import com.augurit.agmobile.gzps.uploadevent.model.Person;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.common.util.MaxLengthInputFilter;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.searchview.util.adapter.TextWatcherAdapter;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jakewharton.rxbinding.view.RxView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 推送到下一环节
 *
 * Created by liangsh on 2017/11/15.
 */

public class SendNextLinkActivity extends BaseActivity{

    private String taskInstId;
    private String currLinkCode;
    private ArrayList<EventDetail.NextlinkBean> nextlinkBeans;

    private AutoBreakViewGroup radio_nextlink;   //下一环节列表单选框
    private View ll_nextlilnk_org;     //下一环节处理人布局容器

    private AutoBreakViewGroup radio_nextlink_org;   //养护班组列表单选框
    private MyGridView gv_assignee;

    private NextLinkAssigneersAdapter assigneersAdapter;
    private List<Assigneers> mAssigneersList;      //下一环节处理人
    private Assigneers.Assigneer selAssignee;     //当前选中的下一环节处理人
    private MultiTakePhotoTableItem photo_item;
    private EditText et_content;
    private View btn_cancel;
    private View btn_submit;
    private RadioGroup.LayoutParams params;
    private ProgressDialog pd;

    private EventDetail.NextlinkBean selectedNextlinkBean;    //当前选中的下一环节
    private List<NextLinkOrg> mNextLinkOrgList;               //养护班组列表(R2)
    private NextLinkOrg selectedNextLinkOrg;                  //当前选中的养护班组

    private String procInstId;
    private String sjid;
    private String isSendMessage = "0";//默认不发送,1为发送

    private ViewGroup ll_nextlilnk_org_Rg_Rm;
    private AutoBreakViewGroup radio_nextlink_org_Rg_Rm;
    private    List<OrgItem> orgItems;

    private CheckBox cb_is_send_message;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_nextlink);
        this.taskInstId = getIntent().getStringExtra("taskInstId");
        this.currLinkCode = getIntent().getStringExtra("currLinkCode");

        this.procInstId =  getIntent().getStringExtra("procInstId");
        this.sjid = getIntent().getStringExtra("sjid");

        this.nextlinkBeans = (ArrayList<EventDetail.NextlinkBean>) getIntent().getSerializableExtra("nextLink");
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        initListener();
    }

    private void initView(){
        ((TextView)findViewById(R.id.tv_title)).setText("推送到下一环节");
        radio_nextlink = (AutoBreakViewGroup) findViewById(R.id.radio_nextlink);
        ll_nextlilnk_org = findViewById(R.id.ll_nextlilnk_org);

        //班组列表选择
        radio_nextlink_org = (AutoBreakViewGroup) findViewById(R.id.radio_nextlink_org);
        //下一环节处理人
        gv_assignee = (MyGridView) findViewById(R.id.gv_assignee);


        //Rg或者Rm需要额外加载机构列表
        ll_nextlilnk_org_Rg_Rm = (ViewGroup)findViewById(R.id.ll_nextlilnk_org_Rg_Rm);
        radio_nextlink_org_Rg_Rm =(AutoBreakViewGroup)findViewById(R.id.radio_nextlink_org_Rg_Rm);

        photo_item = (MultiTakePhotoTableItem) findViewById(R.id.photo_item);
        photo_item.setPhotoExampleEnable(false);
        et_content = (EditText) findViewById(R.id.textfield_content);


        btn_cancel = findViewById(R.id.btn_cancel);
        btn_submit = findViewById(R.id.btn_submit);


        /*if(!GzpsConstant.LINK_SEND_TASK.equals(currLinkCode)){
            ll_nextlilnk_org.setVisibility(View.GONE);
        }*/
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


        int screenWidths = getScreenWidths();
        params = new AutoBreakViewGroup.LayoutParams(screenWidths / 3, AutoBreakViewGroup
                .LayoutParams
                .WRAP_CONTENT);
        for (int i = 0; i < nextlinkBeans.size(); i++) {
            RadioButton radioButton = new RadioButton(SendNextLinkActivity.this);
            radioButton.setText(nextlinkBeans.get(i).getLinkname());
            radioButton.setLayoutParams(params);
            radio_nextlink.addView(radioButton);
        }
        if(!ListUtil.isEmpty(nextlinkBeans)){
//            radio_nextlink.check(0);
        }

        //下发给R2的情况(任务处置)
        if(GzpsConstant.LINK_SEND_TASK.equals(currLinkCode)){
            ll_nextlilnk_org.setVisibility(View.VISIBLE);
            //获取任务处置环节处理人列表

            getNextLinkOrgLst();
        }

        //下发给各区R0和排水中心
        if(GzpsConstant.LINK_PROBLEM_REPORT.equals(currLinkCode)
                || GzpsConstant.LINK_GET_TASK.equals(currLinkCode)
                || GzpsConstant.LINK_TASK_FH.equals(currLinkCode)
                || GzpsConstant.LINK_REPORT_TASK_FH.equals(currLinkCode)){

            User user = new LoginService(getApplicationContext(), AMDatabase.getInstance()).getUser();
            if(GzpsConstant.LINK_PROBLEM_REPORT.equals(currLinkCode) && (user.getRoleCode().contains(GzpsConstant.roleCodes[3])
                    || user.getRoleCode().contains(GzpsConstant.roleCodes[4]) )){
                //Rg或者Rm
                ll_nextlilnk_org_Rg_Rm.setVisibility(View.VISIBLE);
                ll_nextlilnk_org.setVisibility(View.VISIBLE);
                getOrgList();

                //加载机构列表

            }else {
                //获取下一环节及处理人列表
                getNextLinkAssigneers();
                ll_nextlilnk_org_Rg_Rm.setVisibility(View.GONE);

            }

        }

        cb_is_send_message =(CheckBox)findViewById(R.id.cb_is_send_message);
        //cb_is_send_message.setVisibility(View.GONE);//短信信息发送先屏蔽
    }

    private void initListener(){
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

        radio_nextlink.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(checkedRadioButtonId);
                int index = ((RadioGroup) radioButton.getParent()).indexOfChild(radioButton);
                selectedNextlinkBean = nextlinkBeans.get(index);
                if(GzpsConstant.LINK_END.equals(selectedNextlinkBean.getLinkcode())
                        && GzpsConstant.LINK_GET_TASK.equals(currLinkCode)){
                    photo_item.setRequired(true);
                }else{
                    photo_item.setRequired(false);
                }
                //Rg或者Rm selAssignee不置空
                User user = new LoginService(getApplicationContext(), AMDatabase.getInstance()).getUser();
                if(GzpsConstant.LINK_PROBLEM_REPORT.equals(currLinkCode) && (user.getRoleCode().contains(GzpsConstant.roleCodes[3])
                        || user.getRoleCode().contains(GzpsConstant.roleCodes[4]) )) {

                }else {
                    selAssignee = null;
                }


                //如果选的下一环节是“正常结束”，则不需要选下一环节处理人
                if(GzpsConstant.LINK_END.equals(selectedNextlinkBean.getLinkcode())){
                    ll_nextlilnk_org.setVisibility(View.GONE);
                    return;
                }
                if(!ListUtil.isEmpty(mAssigneersList)){
                    List<Assigneers.Assigneer> assigneers = null;
                    //根据选中的下一环节名，获取下一环节处理人
                    for(Assigneers a : mAssigneersList){
                        if(a.getActivityName().equals(selectedNextlinkBean.getLinkcode())){
                            assigneers = a.getAssigneers();
                            break;
                        }
                    }
                    if(ListUtil.isEmpty(assigneers)){
                        ll_nextlilnk_org.setVisibility(View.GONE);
                    } else if(assigneers != null){
                        ll_nextlilnk_org.setVisibility(View.VISIBLE);
                        int selPosition = -1;
                        for(int i=0; i<assigneers.size(); i++){
                            if(assigneers.get(i).isDefault()){
                                selPosition = i;
                                selAssignee = assigneers.get(i);
                                break;
                            }
                        }
                        assigneersAdapter.notifyDatasetChanged(assigneers, selPosition);
                    }
                } else if(!ListUtil.isEmpty(mNextLinkOrgList)){
                    ll_nextlilnk_org.setVisibility(View.VISIBLE);
                    assigneersAdapter.setSelectedPosition(-1);
                }
            }
        });
        radio_nextlink_org.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selAssignee = null;
                int checkedRadioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(checkedRadioButtonId);
                int index = ((RadioGroup) radioButton.getParent()).indexOfChild(radioButton);
                selectedNextLinkOrg = mNextLinkOrgList.get(index);
                assigneersAdapter.notifyDatasetChanged(selectedNextLinkOrg.getUserFormList());
                assigneersAdapter.setSelectedPosition(-1);
            }
        });


        radio_nextlink_org_Rg_Rm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(checkedRadioButtonId);
                int index = ((RadioGroup) radioButton.getParent()).indexOfChild(radioButton);
                OrgItem orgItem = orgItems.get(index);
                getPersonByOrgCodeAndName(orgItem.getName(), orgItem.getCode());

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RxView.clicks(btn_submit)
                .throttleFirst(2, TimeUnit.SECONDS)   //2秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(selectedNextlinkBean == null){
                            ToastUtil.shortToast(SendNextLinkActivity.this, "请选择下一环节");
                            return;
                        }

                        if(GzpsConstant.LINK_SEND_TASK.equals(currLinkCode)
                                && !GzpsConstant.LINK_END.equals(selectedNextlinkBean.getLinkcode())){
                            //任务派发环节必须选班组和下一环节处理人
                            if(selectedNextLinkOrg == null
                                    || selAssignee == null){
                                ToastUtil.shortToast(SendNextLinkActivity.this, "请选择下一环节处理人");
                                return;
                            }
                        }
                        if((GzpsConstant.LINK_END.equals(selectedNextlinkBean.getLinkcode())
                                && GzpsConstant.LINK_GET_TASK.equals(currLinkCode))
                                && ListUtil.isEmpty(photo_item.getSelectedPhotos())){
                            ToastUtil.shortToast(SendNextLinkActivity.this, "请添加照片");
                            return;
                        }
                        if(GzpsConstant.LINK_PROBLEM_REPORT.equals(currLinkCode)
                                || GzpsConstant.LINK_GET_TASK.equals(currLinkCode)
                                || GzpsConstant.LINK_TASK_FH.equals(currLinkCode)
                                || GzpsConstant.LINK_REPORT_TASK_FH.equals(currLinkCode)){
                            if(!GzpsConstant.LINK_END.equals(selectedNextlinkBean.getLinkcode())) {
                                User user = new LoginService(getApplicationContext(), AMDatabase.getInstance()).getUser();
                                if (GzpsConstant.LINK_PROBLEM_REPORT.equals(currLinkCode) && (user.getRoleCode().contains(GzpsConstant.roleCodes[3])
                                        || user.getRoleCode().contains(GzpsConstant.roleCodes[4]))) {
                                    //Rg或者Rm
                                    if (selAssignee == null) {
                                        ToastUtil.shortToast(SendNextLinkActivity.this, "请选择下一环节处理人");
                                        return;
                                    }
                                } else {
                                    List<Assigneers.Assigneer> assigneers = null;
                                    if (mAssigneersList == null) {
                                        ToastUtil.shortToast(SendNextLinkActivity.this, "请选择下一环节处理人");
                                        return;
                                    }
                                    for (Assigneers assigneers1 : mAssigneersList) {
                                        if (assigneers1.getActivityName().equals(selectedNextlinkBean.getLinkcode())) {
                                            assigneers = assigneers1.getAssigneers();
                                        }
                                    }
                                    if (!ListUtil.isEmpty(assigneers) && selAssignee == null) {
                                        ToastUtil.shortToast(SendNextLinkActivity.this, "请选择下一环节处理人");
                                        return;
                                    }
                                }
                            }
                        }
                        if(StringUtil.isEmpty(et_content.getText().toString())){
                            ToastUtil.shortToast(SendNextLinkActivity.this, "请填写环节意见");
                            return;
                        }

                        //是否发送短信
                        if(cb_is_send_message.isChecked()){
                            isSendMessage = "1";
                        }else {
                            isSendMessage = "0";
                        }

                        String nextLinkOrg = null;
                        if(selectedNextLinkOrg != null){
                            nextLinkOrg = selectedNextLinkOrg.getCode();
                        }
                        String assigneeCode = null;
                        String assigneeName = null;
                        if(selAssignee != null){
                            assigneeCode = selAssignee.getUserCode();
                            assigneeName = selAssignee.getUserName();
                        }
                        List<Photo> photos = photo_item.getSelectedPhotos();
                        String advice = et_content.getText().toString();
                        final UploadEventService eventService = new UploadEventService(SendNextLinkActivity.this);
                        eventService.wfSend(selectedNextlinkBean.getLinkname(),
                                selectedNextlinkBean.getLinkcode(),
                                nextLinkOrg,
                                assigneeCode,
                                assigneeName,
                                taskInstId,
                                advice,
                                procInstId,
                                sjid,
                                isSendMessage,
                                photos)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe(new Action0() {
                                    @Override
                                    public void call() {
                                        pd = new ProgressDialog(SendNextLinkActivity.this);
                                        pd.setMessage("正在提交...");
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
                                        ToastUtil.shortToast(SendNextLinkActivity.this, "操作失败，请重试！");
                                    }

                                    @Override
                                    public void onNext(Boolean s) {
                                        if(!s){
                                            ToastUtil.shortToast(getApplicationContext(), "操作失败，请重试");
                                            return;
                                        }
                                        if(ListUtil.isEmpty(photo_item.getSelectedPhotos())){
                                            pd.dismiss();
                                            setResult(456);
                                            finish();
                                            ToastUtil.shortToast(SendNextLinkActivity.this, "操作成功");
                                        } else {
                                            saveJdFile();
                                        }
                                    }
                                });
                    }
                });
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
                            assigneersAdapter.notifyDatasetChanged(assigneeList);
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
                                RadioButton radioButton = new RadioButton(SendNextLinkActivity.this);
                                radioButton.setText(orgItems.get(i).getName());
                                //    radioButton.setTag(nextLinkOrgs);
                                radioButton.setLayoutParams(params);
                                radio_nextlink_org_Rg_Rm.addView(radioButton);

                                if(i == 0){
                                    radioButton.setChecked(true);
                                }
                            }


                        }

                    }
                });
    }

    private int getScreenWidths() {
        WindowManager manager = SendNextLinkActivity.this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        return width;
    }


    /**
     * 任务分发环节获取当前用户所在的机构下的养护班组
     * 获取班组中的处理人列表(获取班组列表????)
     */
    private void getNextLinkOrgLst(){
        UploadEventService eventService = new UploadEventService(SendNextLinkActivity.this);
        eventService.getNextLinkOrg()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<NextLinkOrg>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(List<NextLinkOrg> nextLinkOrgs) {
                        if(ListUtil.isEmpty(nextLinkOrgs)){
                            ll_nextlilnk_org.setVisibility(View.GONE);
                            return;
                        }
                        mNextLinkOrgList = nextLinkOrgs;
                        for (int i = 0; i < nextLinkOrgs.size(); i++) {
                            RadioButton radioButton = new RadioButton(SendNextLinkActivity.this);
                            radioButton.setText(nextLinkOrgs.get(i).getName());
                            radioButton.setTag(nextLinkOrgs);
                            radioButton.setLayoutParams(params);
                            radio_nextlink_org.addView(radioButton);
                        }
//                        radio_nextlink_org.check(0);
                    }
                });
    }

    /**
     *
     * 获取下一环节及处理人列表
     */
    private void getNextLinkAssigneers() {
        new UploadEventService(getApplicationContext()).getNextActivityInfo(taskInstId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        pd = new ProgressDialog(SendNextLinkActivity.this);
                        pd.setMessage("正在获取下一环节处理人列表...");
                        pd.show();
                        radio_nextlink.setEnabled(false);
                    }
                })
                .subscribe(new Subscriber<Result<List<Assigneers>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        pd.dismiss();
                        ToastUtil.shortToast(SendNextLinkActivity.this, "获取下一环节处理人失败");
                    }

                    @Override
                    public void onNext(Result<List<Assigneers>> assigneersList) {
                        pd.dismiss();
                        if(!assigneersList.isSuccess()){
                            ToastUtil.shortToast(SendNextLinkActivity.this, "获取下一环节处理人失败");
                            return;
                        }
                        mAssigneersList = assigneersList.getResult();
                        ll_nextlilnk_org.setVisibility(View.VISIBLE);
                        radio_nextlink_org.setVisibility(View.GONE);
                        radio_nextlink.setEnabled(true);
                    }
                });
    }


    private void saveJdFile(){
        UploadEventService uploadEventService = new UploadEventService(this.getApplicationContext());
        uploadEventService.saveJdFile(taskInstId, photo_item.getSelectedPhotos())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        pd.dismiss();
                        ToastUtil.longToast(SendNextLinkActivity.this, "提交下一环节成功，但上传附件失败！");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        pd.dismiss();
                        if(aBoolean){
                            setResult(456);
                            finish();
                            ToastUtil.shortToast(SendNextLinkActivity.this, "操作成功");
                        } else {
                            ToastUtil.shortToast(SendNextLinkActivity.this, "操作失败！");
                        }

                    }
                });
    }


    @Override
    public void onBackPressed(){
        DialogUtil.MessageBox(SendNextLinkActivity.this, "提示", "是否确定放弃本次编辑？", new DialogInterface.OnClickListener() {
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

        if (photo_item != null) {
            photo_item.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
