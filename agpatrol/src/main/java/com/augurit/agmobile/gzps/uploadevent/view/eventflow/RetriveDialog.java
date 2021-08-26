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
import com.augurit.agmobile.gzps.common.model.Result;
import com.augurit.agmobile.gzps.uploadevent.event.UpdateAfterRetriveEvent;
import com.augurit.agmobile.gzps.uploadevent.service.UploadEventService;
import com.augurit.agmobile.gzps.uploadevent.view.eventlist.EventDetailActivity;
import com.augurit.agmobile.patrolcore.common.util.MaxLengthInputFilter;
import com.augurit.am.cmpt.widget.searchview.util.adapter.TextWatcherAdapter;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * 撤回
 * Created by liangsh on 2017/11/17.
 */

public class RetriveDialog extends DialogFragment {

    private String procInstDbId;
    private EditText et_content;
    private View btn_cancel;
    private View btn_submit;
    private TextView tv_advise;

    private CheckBox cb_is_send_message;//是否发送短信通知对方

    public static RetriveDialog getInstance(String procInstDbId){
        RetriveDialog rollBackDialog = new RetriveDialog();
        Bundle data = new Bundle();
        data.putString("procInstDbId", procInstDbId);
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

        this.procInstDbId = getArguments().getString("procInstDbId");
        final View view = inflater.inflate(R.layout.dialog_rollback, container);
        et_content = (EditText) view.findViewById(R.id.textfield_content);

        tv_advise = (TextView)view.findViewById(R.id.tv_advise);

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

        tv_advise.setText("请填写撤回原因");
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_content.getText().toString();
                if(StringUtil.isEmpty(content)){
                    ToastUtil.shortToast(getContext(),"请填写撤回原因");
                    return;
                }

                final ProgressDialog pd = new ProgressDialog(getContext());
                pd.show();

                new UploadEventService(getContext()).wfRetrive(procInstDbId,content)
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
                                if(stringResult.isSuccess()){
                                    ToastUtil.longToast(getContext(),"撤回成功!");
                                    EventBus.getDefault().post(new UpdateAfterRetriveEvent(procInstDbId));

                                    getActivity().finish();
                                }else {
                                    if(!TextUtils.isEmpty(stringResult.getMessage())){
                                        ToastUtil.longToast(getContext(),stringResult.getMessage());
                                    }else {
                                        ToastUtil.longToast(getContext(),"撤回失败!");
                                    }
                                }
                            }
                        });


            }
        });

        cb_is_send_message =(CheckBox)view.findViewById(R.id.cb_is_send_message);
        cb_is_send_message.setVisibility(View.GONE);
        return view;
    }
}
