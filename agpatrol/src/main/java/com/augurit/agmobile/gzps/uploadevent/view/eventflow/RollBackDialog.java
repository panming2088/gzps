package com.augurit.agmobile.gzps.uploadevent.view.eventflow;

import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.patrolcore.common.util.MaxLengthInputFilter;
import com.augurit.am.cmpt.widget.searchview.util.adapter.TextWatcherAdapter;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.io.UnsupportedEncodingException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * 回退
 * Created by liangsh on 2017/11/17.
 */

public class RollBackDialog extends DialogFragment {

    private String taskInstId;
    private String currLinkCode;
    private String repordId;

    private EditText et_content;
    private View btn_cancel;
    private View btn_submit;


    private String activityName;
    private String sjid;
    private String isSendMessage = "0";//默认不发送,1为发送

    private CheckBox cb_is_send_message;//是否发送短信通知对方

    public static RollBackDialog getInstance(String repordId,String taskInstId, String currLinkCode){
        RollBackDialog rollBackDialog = new RollBackDialog();
        Bundle data = new Bundle();
        data.putString("taskInstId", taskInstId);
        data.putString("repordId",repordId);
        rollBackDialog.setArguments(data);
        rollBackDialog.setCancelable(false);
        return rollBackDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             final ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.taskInstId = getArguments().getString("taskInstId");
        this.currLinkCode = getArguments().getString("currLinkCode");
        this.repordId = getArguments().getString("repordId");
        final View view = inflater.inflate(R.layout.dialog_rollback, container);
        et_content = (EditText) view.findViewById(R.id.textfield_content);

        final TextView tv_size = (TextView) view.findViewById(R.id.tv_size);
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
                String content = et_content.getText().toString();
                if(StringUtil.isEmpty(content)){
                    ToastUtil.shortToast(getContext(), "请填写意见");
                    return;
                }

                String isSendMessage ="0";
                if(cb_is_send_message.isChecked()){
                    isSendMessage ="1";
                }else {
                    isSendMessage = "0";
                }
                final ProgressDialog pd = new ProgressDialog(getContext());
                new UploadEventService(getContext()).wfReturn(isSendMessage,repordId,taskInstId, content)
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

        cb_is_send_message =(CheckBox)view.findViewById(R.id.cb_is_send_message);

        return view;
    }
}
