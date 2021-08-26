package com.augurit.agmobile.gzps.uploadevent.view.eventflow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.augurit.agmobile.gzps.BaseApplication;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.constant.GzpsConstant;
import com.augurit.agmobile.gzps.common.widget.AutoBreakViewGroup;
import com.augurit.agmobile.gzps.common.widget.MyGridView;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.uploadevent.adapter.NextLinkAssigneersAdapter;
import com.augurit.agmobile.gzps.uploadevent.dao.GetPersonByOrgApiData;
import com.augurit.agmobile.gzps.uploadevent.model.Assigneers;
import com.augurit.agmobile.gzps.uploadevent.model.OrgItem;
import com.augurit.agmobile.gzps.uploadevent.model.Person;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.fw.utils.DensityUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * 转派
 * Created by liangsh on 2017/11/23.
 */

public class ReassignDialog extends DialogFragment {

    private String taskInstId;
    private String currLinkCode;
    private String currLinkName;

    private MyGridView gv_assignee;
    private NextLinkAssigneersAdapter assigneersAdapter;
    private Assigneers.Assigneer selAssignee;
    private TextFieldTableItem et_content;
    private View btn_cancel;
    private View btn_submit;
    private String procInstDbId;//流程ID

    private ViewGroup ll_nextlilnk_org_Rg_Rm;
    private AutoBreakViewGroup radio_nextlink_org_Rg_Rm;
    private List<OrgItem> orgItems;
    private RadioGroup.LayoutParams params;


    private String activityName;
    private String sjid;
    private String isSendMessage = "0";//默认不发送,1为发送

    private CheckBox cb_is_send_message;


    public static ReassignDialog getInstance(String taskInstId, String currLinkCode, String currLinkName) {
        ReassignDialog reassignDialog = new ReassignDialog();
        Bundle data = new Bundle();
        data.putString("taskInstId", taskInstId);
        data.putString("currLinkCode", currLinkCode);
        data.putString("currLinkName", currLinkName);

        reassignDialog.setArguments(data);
        reassignDialog.setCancelable(false);
        return reassignDialog;
    }

    public static ReassignDialog getInstance(String taskInstId, String currLinkCode, String currLinkName, String procInstDbId) {
        ReassignDialog reassignDialog = new ReassignDialog();
        Bundle data = new Bundle();
        data.putString("taskInstId", taskInstId);
        data.putString("currLinkCode", currLinkCode);
        data.putString("currLinkName", currLinkName);
        data.putString("procInstDbId", procInstDbId);
        reassignDialog.setArguments(data);
        reassignDialog.setCancelable(false);
        return reassignDialog;
    }

    public static ReassignDialog getInstance(String taskInstId, String currLinkCode,
                                             String currLinkName,
                                             String procInstDbId,
                                             String activityName,
                                             String sjid) {
        ReassignDialog reassignDialog = new ReassignDialog();
        Bundle data = new Bundle();
        data.putString("taskInstId", taskInstId);
        data.putString("currLinkCode", currLinkCode);
        data.putString("currLinkName", currLinkName);
        data.putString("procInstDbId", procInstDbId);

        data.putString("activityName", activityName);
        data.putString("sjid", sjid);
        reassignDialog.setArguments(data);
        reassignDialog.setCancelable(false);
        return reassignDialog;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             final ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout((int) (dm.widthPixels * 1.2), ViewGroup.LayoutParams.WRAP_CONTENT);


        this.taskInstId = getArguments().getString("taskInstId");
        this.currLinkCode = getArguments().getString("currLinkCode");
        this.currLinkName = getArguments().getString("currLinkName");


        if (getArguments().getString("procInstDbId") != null) {
            this.procInstDbId = getArguments().getString("procInstDbId");
        }
        this.sjid = getArguments().getString("sjid");
        this.activityName = getArguments().getString("activityName");

        final View view = inflater.inflate(R.layout.dialog_reassign, container);
        gv_assignee = (MyGridView) view.findViewById(R.id.gv_assignee);
        et_content = (TextFieldTableItem) view.findViewById(R.id.textfield_content);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_submit = view.findViewById(R.id.btn_submit);

        ll_nextlilnk_org_Rg_Rm = (ViewGroup) view.findViewById(R.id.ll_nextlilnk_org_Rg_Rm);
        radio_nextlink_org_Rg_Rm = (AutoBreakViewGroup) view.findViewById(R.id.radio_nextlink_org_Rg_Rm);


        assigneersAdapter = new NextLinkAssigneersAdapter(getContext());
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

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selAssignee == null) {
                    ToastUtil.shortToast(getContext(), "请选择转派对象");
                    return;
                }
                String content = et_content.getText();
                if (StringUtil.isEmpty(content)) {
                    ToastUtil.shortToast(getContext(), "请填写意见");
                    return;
                }

                if (cb_is_send_message.isChecked()) {
                    isSendMessage = "1";
                } else {
                    isSendMessage = "0";
                }

                final ProgressDialog pd = new ProgressDialog(getContext());
                new UploadEventService(getContext()).wfReassign(taskInstId,
                        selAssignee.getUserCode(), selAssignee.getUserName(),
                        currLinkName, content, activityName, procInstDbId, sjid, isSendMessage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                pd.show();
                            }
                        })
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable throwable) {
                                pd.dismiss();
                                ToastUtil.shortToast(getContext(), "操作失败，请重试！");
                            }

                            @Override
                            public void onNext(String s) {
                                pd.dismiss();
                                ToastUtil.shortToast(getContext(), "操作成功");
                                dismiss();
                                getActivity().setResult(123);
                                getActivity().finish();
                            }
                        });

            }
        });

        int screenWidths = getScreenWidths();
        params = new AutoBreakViewGroup.LayoutParams(screenWidths / 3 - 60
                , AutoBreakViewGroup
                .LayoutParams
                .WRAP_CONTENT);

        radio_nextlink_org_Rg_Rm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(checkedRadioButtonId);
                int index = ((RadioGroup) radioButton.getParent()).indexOfChild(radioButton);
                OrgItem orgItem = orgItems.get(index);
                getPersonByOrgCodeAndName(orgItem.getName(), orgItem.getCode());

            }
        });


        if (procInstDbId.contains(GzpsConstant.FLOW_RG_OR_RM_UPLOAD)) {
            getOrgList();
            ll_nextlilnk_org_Rg_Rm.setVisibility(View.VISIBLE);
        } else {
            ll_nextlilnk_org_Rg_Rm.setVisibility(View.GONE);
            getNextLinkAssigneers("");
        }

        cb_is_send_message = (CheckBox) view.findViewById(R.id.cb_is_send_message);
        //  cb_is_send_message.setVisibility(View.GONE);//短信信息发送先屏蔽
        return view;
    }

    private int getScreenWidths() {
        WindowManager manager = ((Activity) getContext()).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        //   float density = outMetrics.density;
        //  width = (int)(width/density);


        return width;
    }


    private void getPersonByOrgCodeAndName(String name, String code) {
        new UploadEventService(BaseApplication.application).getPersonByOrgApiDataObservable(code, name)
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
                        if (personList != null && !personList.isEmpty()) {
                            List<Assigneers.Assigneer> assigneeList = new ArrayList<>();
                            for (Person person : personList) {
                                assigneeList.add(new Assigneers.Assigneer(person.getCode(), person.getName()));
                            }
                            assigneersAdapter.notifyDatasetChanged(assigneeList);
                        }
                    }
                });
    }

    private void getOrgList() {
        new UploadEventService(BaseApplication.application).getOrgItemList()
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
                        if (list != null &&
                                !list.isEmpty()) {

                            orgItems = list;
                            int size = orgItems.size();
                            for (int i = 0; i < size; i++) {
                                RadioButton radioButton = new RadioButton(getContext());
                                radioButton.setScaleX(0.9f);
                                radioButton.setScaleY(0.9f);
                                radioButton.setText(orgItems.get(i).getName());
                                if (i < 5) {
                                    radioButton.setPadding(DensityUtil.dp2px(getActivity(), -5), 0, 0, 0);
                                }
                                //    radioButton.setTag(nextLinkOrgs);
                                radioButton.setLayoutParams(params);
                                radio_nextlink_org_Rg_Rm.addView(radioButton);

                                if (i == 0) {
                                    radioButton.setChecked(true);
                                }
                            }


                        }

                    }
                });
    }


    private void getNextLinkAssigneers(String objectId) {
        new UploadEventService(getContext()).getTaskUserByloginName(objectId)
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
                        if (assigneersList != null
                                && !ListUtil.isEmpty(assigneersList)) {
                            List<Assigneers.Assigneer> assigneeList = new ArrayList<>();
                            for (Assigneers assigneers : assigneersList) {
                                assigneeList.addAll(assigneers.getAssigneers());
                            }
                            assigneersAdapter.notifyDatasetChanged(assigneeList);
                        }
                    }
                });
    }
}
