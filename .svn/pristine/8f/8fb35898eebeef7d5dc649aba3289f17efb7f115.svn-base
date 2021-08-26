package com.augurit.agmobile.patrolcore.common.table.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.augurit.agmobile.patrolcore.R;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 消防信息h5界面
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.table.util
 * @createTime 创建时间 ：2017/9/23
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/9/23
 * @modifyMemo 修改备注：
 */

public class FireFightingView {

    private Context mContext;
    private PopupWindow mPop;
    private View mView;
    private WebView mWebView;
    private boolean mIsPageLoaded = false;
    private String mTypes;
    private String mInfos;
    private boolean mIsReadOnly;
    private OnJsCallback mOnJsCallback;

    public FireFightingView(Context context, String title, ViewGroup container) {
        mContext = context;
        init(title, container);
    }

    private void init(String title, ViewGroup container) {
        mView = View.inflate(mContext, R.layout.firefighting_view, null);
        mWebView = (WebView) mView.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.addJavascriptInterface(new FireFightingJavaScriptInterface(), "Android");
        mWebView.loadUrl("file:///android_asset/firefighting.html");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mIsPageLoaded = true;
                if (mTypes != null) {
                    setData(mTypes, mInfos, mIsReadOnly);
                }
            }
        });

        TextView tv_title = (TextView) mView.findViewById(R.id.tv_title);
        tv_title.setText(title);
        mView.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 初始化popup
//        mPop = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
//        Drawable bg = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.common_lyrl_dialog, null);
//        mPop.setBackgroundDrawable(bg);

        container.removeAllViews();
        container.addView(mView);
    }

    /**
     * 传入数据并显示
     */
    @Deprecated
    public void show(View anchor, String types, String infos, boolean isReadOnly) {
        mTypes = types;
        mInfos = infos;
        mIsReadOnly = isReadOnly;
        if (mPop != null) {
            mPop.showAtLocation(anchor, Gravity.CENTER, 0, 0);
            if (mIsPageLoaded) {
                setData(types, infos, isReadOnly);
            }
        }
    }

    /**
     * 传入数据并显示
     */
    public void show(String types, String infos, boolean isReadOnly) {
        mTypes = types;
        mInfos = infos;
        mIsReadOnly = isReadOnly;
        if (mIsPageLoaded) {
            setData(types, infos, isReadOnly);
        }
    }

    private void setData(String types, String infos, boolean isReadOnly) {
        String command = "javascript:setData("
                + types + ","
                + infos + ","
                + isReadOnly +
                ");";
        mWebView.loadUrl(command);
    }

    /**
     * 隐藏
     */
    @Deprecated
    public void dismiss() {
        if (mPop != null) {
            mPop.dismiss();
        }
    }

    /**
     * js数据交互
     */
    public class FireFightingJavaScriptInterface {

        @JavascriptInterface
        public void callback(String types, String infos) {
            Observable.just(new String[]{types, infos, ""}).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String[]>() {
                        @Override
                        public void call(String[] strings) {
                            if (mOnJsCallback != null) {
                                mOnJsCallback.onCallback(strings[0], strings[1], "");
                            }
                        }
                    });
        }
    }

    public interface OnJsCallback {
        void onCallback(String typesJson, String infosJson, String show);
    }

    public void setOnJsCallback(OnJsCallback onJsCallback) {
        this.mOnJsCallback = onJsCallback;
    }
}
