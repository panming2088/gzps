package com.augurit.agmobile.gzps.uploadevent.view.eventflow;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.publicaffair.event.UpdateAdviceEvent;
import com.augurit.agmobile.gzps.uploadevent.model.EventDetail;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.patrolcore.common.util.MaxLengthInputFilter;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.widget.searchview.util.adapter.TextWatcherAdapter;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.view.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liangsh on 2017/11/12.
 */

public class AddEventAdviceDialog extends DialogFragment {

    private String taskInstId;
    private String sjid;
    private EditText et_content;
    private View btn_cancel;
    private View btn_submit;

    public static AddEventAdviceDialog getInstance(String taskInstId, String sjid){
        AddEventAdviceDialog addEventAdviceDialog = new AddEventAdviceDialog();
        Bundle data = new Bundle();
        data.putString("taskInstId", taskInstId);
        data.putString("sjid", sjid);
        addEventAdviceDialog.setArguments(data);
        addEventAdviceDialog.setCancelable(false);
        return addEventAdviceDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.taskInstId = getArguments().getString("taskInstId");
        this.sjid = getArguments().getString("sjid");

        View view = inflater.inflate(R.layout.dialog_add_event_advice, container);
        final TextView tv_size = (TextView) view.findViewById(R.id.tv_size);
        et_content = (EditText) view.findViewById(R.id.textfield_content);
        final int maxTotal = 100;
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
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_submit = view.findViewById(R.id.btn_submit);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String advice = et_content.getText().toString();
                if(advice.isEmpty()){
                    ToastUtil.longToast(getContext(),"请填写提交内容!");
                    return;
                }
                UploadEventService eventService = new UploadEventService(getContext());
                eventService.uploadAdvice(advice, sjid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Boolean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable throwable) {
                                ToastUtil.shortToast(getContext(), "提交失败，请重试！");
                            }

                            @Override
                            public void onNext(Boolean s) {
                                if(s){
                                    dismiss();
                                    User user = new LoginRouter(getContext(), AMDatabase.getInstance()).getUser();
                                    EventDetail.OpinionBean opinionBean = new EventDetail.OpinionBean();
                                    opinionBean.setTime(System.currentTimeMillis());
                                    opinionBean.setUserName(user.getUserName());
                                    opinionBean.setOpinion(advice);
                                    EventBus.getDefault().post(new UpdateAdviceEvent(opinionBean));
                                    ToastUtil.shortToast(getContext(), "提交成功");
                                } else {
                                    ToastUtil.shortToast(getContext(), "提交失败，请重试！");
                                }

                            }
                        });
            }
        });


        return view;
    }


}
