package com.augurit.agmobile.gzps.setting.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.service.GzpsService;
import com.augurit.agmobile.gzps.login.PatrolLoginService;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.AMFileUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONObject;

import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liangsh on 2018/1/3.
 */

public class LegalNoticeDialog extends DialogFragment {

    private Callback1 callback;
    private boolean checked = false;

    private View btn_close;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_legal_notice, container);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        final View btn_sure = view.findViewById(R.id.btn_sure);
        View btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_close = view.findViewById(R.id.btn_close);
        CheckBox cb = (CheckBox) view.findViewById(R.id.cb_laws);
        WebView webView = (WebView) view.findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setBackgroundColor(Color.TRANSPARENT);  //  WebView 背景透明效果
        webView.loadUrl("file:///android_asset/flsm.html");

        /*cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;
                if(checked){
                    btn_sure.setEnabled(true);
                    btn_sure.setBackgroundResource(R.drawable.radio_bg);
                } else {
                    btn_sure.setEnabled(false);
                    btn_sure.setBackgroundResource(R.color.grey);
                }
            }
        });*/

        try {
            String htmlStr = AMFileUtil.readStringFromAsset(getContext(), "flsm.html", Charset.defaultCharset());
            tv.setText(Html.fromHtml(htmlStr));
//            tv.setMovementMethod(ScrollingMovementMethod.getInstance());//滚动
        } catch (Exception e) {
            e.printStackTrace();
        }

//        btn_sure.setEnabled(false);
//        btn_sure.setBackgroundResource(R.color.grey);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLegalNotice();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                System.exit(0);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        return view;
    }

    /**
     * 设置用户已经阅读过法律声明
     */
    private void checkLegalNotice(){
        new GzpsService(getContext())
                .columnsReadInfo(new PatrolLoginService(getContext(), AMDatabase.getInstance()).getUser().getId(), "flsm")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtil.shortToast(getContext(), "网络信号弱");
                        btn_close.setVisibility(View.VISIBLE);
                        CrashReport.postCatchedException(
                                new Exception("法律声明操作失败，用户登录名"
                                        + BaseInfoManager.getLoginName(getContext())
                                        + "，用户名" + BaseInfoManager.getUserName(getContext())));
                        CrashReport.postCatchedException(throwable);
                    }

                    @Override
                    public void onNext(okhttp3.ResponseBody responseBody) {
                        boolean readed = false;
                        if(responseBody != null){
                            try {
                                String result = responseBody.string();
                                JSONObject jsonObject = new JSONObject(result);
                                int flsmUnread = jsonObject.getInt("flsmUnread");//法律声明
                                if(flsmUnread == 0){
                                    readed = true;
                                }
                            } catch (Exception e) {
                                readed = false;
                                e.printStackTrace();
                            }
                        }
                        if(readed){
                            dismiss();
                            if(callback != null){
                                callback.onCallback(null);
                            }
                        } else {
                            ToastUtil.shortToast(getContext(), "网络信号弱");
                            btn_close.setVisibility(View.VISIBLE);
                            CrashReport.postCatchedException(
                                    new Exception("法律声明操作失败，用户登录名"
                                            + BaseInfoManager.getLoginName(getContext())
                                    + "，用户名" + BaseInfoManager.getUserName(getContext())));
                        }
                    }
                });
    }

    public void setCallback(Callback1 callback){
        this.callback = callback;
    }
}
