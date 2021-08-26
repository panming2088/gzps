package com.augurit.agmobile.gzps.uploadevent.view.eventflow;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.uploadevent.model.EventDetail;
import com.augurit.agmobile.gzps.uploadevent.model.NextLinkOrg;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.gzps.common.widget.AutoBreakViewGroup;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liangsh on 2017/11/12.
 */
@Deprecated
public class SendNextLinkDialog extends DialogFragment {

    private String taskInstId;
    private String currLinkCode;
    private ArrayList<EventDetail.NextlinkBean> nextlinkBeans;

    private AutoBreakViewGroup radio_nextlink;
    private View ll_nextlilnk_org;
    private AutoBreakViewGroup radio_nextlink_org;
    private MultiTakePhotoTableItem photo_item;
    private TextFieldTableItem et_content;
    private View btn_cancel;
    private View btn_submit;
    private RadioGroup.LayoutParams params;

    private EventDetail.NextlinkBean selectedNextlinkBean;
    private List<NextLinkOrg> mNextLinkOrgList;
    private NextLinkOrg selectedNextLinkOrg;

    public static SendNextLinkDialog getInstance(String taskInstId, String currLinkCode, ArrayList<EventDetail.NextlinkBean> nextlinkBeans){
        SendNextLinkDialog sendNextLinkDialog = new SendNextLinkDialog();
        Bundle data = new Bundle();
        data.putString("taskInstId", taskInstId);
        data.putString("currLinkCode", currLinkCode);
        data.putSerializable("nextLink", nextlinkBeans);
        sendNextLinkDialog.setArguments(data);
        return sendNextLinkDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.taskInstId = getArguments().getString("taskInstId");
        this.currLinkCode = getArguments().getString("currLinkCode");
        this.nextlinkBeans = (ArrayList<EventDetail.NextlinkBean>) getArguments().getSerializable("nextLink");
        final View view = inflater.inflate(R.layout.dialog_send_nextlink, container);
        radio_nextlink = (AutoBreakViewGroup) view.findViewById(R.id.radio_nextlink);
        ll_nextlilnk_org = view.findViewById(R.id.ll_nextlilnk_org);
        radio_nextlink_org = (AutoBreakViewGroup) view.findViewById(R.id.radio_nextlink_org);
        photo_item = (MultiTakePhotoTableItem) view.findViewById(R.id.photo_item);
        et_content = (TextFieldTableItem) view.findViewById(R.id.textfield_content);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_submit = view.findViewById(R.id.btn_submit);

        /*if(!GzpsConstant.LINK_SEND_TASK.equals(currLinkCode)){
            ll_nextlilnk_org.setVisibility(View.GONE);
        }*/

        radio_nextlink.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(checkedRadioButtonId);
                int index = ((RadioGroup) radioButton.getParent()).indexOfChild(radioButton);
                selectedNextlinkBean = nextlinkBeans.get(index);
            }
        });
        radio_nextlink_org.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(checkedRadioButtonId);
                int index = ((RadioGroup) radioButton.getParent()).indexOfChild(radioButton);
                selectedNextLinkOrg = mNextLinkOrgList.get(index);
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
                String nextLinkOrg = null;
                if(selectedNextLinkOrg != null){
                    nextLinkOrg = selectedNextLinkOrg.getCode();
                }
                List<Photo> photos = photo_item.getSelectedPhotos();
                String advice = et_content.getText().toString();
                UploadEventService eventService = new UploadEventService(getContext());
                /*eventService.wfSend(selectedNextlinkBean.getLinkname(),
                        selectedNextlinkBean.getLinkcode(),
                        nextLinkOrg,
                        taskInstId,
                        advice,
                        photos)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                            }
                        })
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable throwable) {
                                ToastUtil.shortToast(getContext(), "操作失败，请重试！");
                            }

                            @Override
                            public void onNext(String s) {
                                dismiss();
                                ToastUtil.shortToast(getContext(), "操作成功");
                            }
                        });*/
            }
        });

        int screenWidths = getScreenWidths();
        params = new AutoBreakViewGroup.LayoutParams(screenWidths / 3, AutoBreakViewGroup
                .LayoutParams
                .WRAP_CONTENT);
        for (int i = 0; i < nextlinkBeans.size(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(nextlinkBeans.get(i).getLinkname());
            radioButton.setLayoutParams(params);
            radio_nextlink.addView(radioButton);
        }
        if(!ListUtil.isEmpty(nextlinkBeans)){
//            radio_nextlink.check(0);
        }

        getNextLinkOrgLst();
        return view;
    }

    private int getScreenWidths() {
        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        return width;
    }


    private void getNextLinkOrgLst(){
        UploadEventService eventService = new UploadEventService(getContext());
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
                            radio_nextlink_org.setVisibility(View.GONE);
                            return;
                        }
                        mNextLinkOrgList = nextLinkOrgs;
                        for (int i = 0; i < nextLinkOrgs.size(); i++) {
                            RadioButton radioButton = new RadioButton(getContext());
                            radioButton.setText(nextLinkOrgs.get(i).getName());
                            radioButton.setLayoutParams(params);
                            radio_nextlink_org.addView(radioButton);
                        }
//                        radio_nextlink_org.check(0);
                    }
                });
    }


}
