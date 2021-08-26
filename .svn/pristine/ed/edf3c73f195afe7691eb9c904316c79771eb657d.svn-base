package com.augurit.agmobile.patrolcore.common.table.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
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
import com.augurit.agmobile.patrolcore.common.table.model.TableChildItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 安全隐患h5界面
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.table.util
 * @createTime 创建时间 ：2017/9/14
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/9/14
 * @modifyMemo 修改备注：
 */

public class SaftyHazardView {

    private Context mContext;
    private PopupWindow mPop;
    private WebView mWebView;
    private OnJsCallback mOnJsCallback;
    private boolean mIsPageLoaded = false;
    private String mJson;
    private boolean mIsReadOnly;

    public SaftyHazardView(Context context, String title) {
        mContext = context;
        init(title);
    }

    private void init(String title) {
        View view = View.inflate(mContext, R.layout.safty_hazard_popup, null);
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.addJavascriptInterface(new SaftyJavaScriptInterface(), "Android");
        mWebView.loadUrl("file:///android_asset/localfile2.html");  // 2017-09-20 第二版
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mIsPageLoaded = true;
                if (mJson != null) {
                    setData(mJson, mIsReadOnly);
                }
            }
        });

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(title);
        view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 初始化popup
        mPop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        Drawable bg = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.common_lyrl_dialog, null);
        mPop.setBackgroundDrawable(bg);
    }

    /**
     * 传入数据并显示
     * @param anchor
     * @param json
     * @param isReadOnly
     */
    public void show(View anchor, String json, boolean isReadOnly) {
        mJson = json;
        mIsReadOnly = isReadOnly;
        if (mPop != null) {
            mPop.showAtLocation(anchor, Gravity.CENTER, 0, 0);
            if (mIsPageLoaded) {
                setData(json, isReadOnly);
            }
        }
    }

    private void setData(String json, boolean isReadOnly) {
        String command = "javascript:acceptAndroidData(" +
                json + "," + isReadOnly +
                ");";
        mWebView.loadUrl(command);
    }

    /**
     * 隐藏
     */
    public void dismiss() {
        if (mPop != null) {
            mPop.dismiss();
        }
    }

    /**
     * js数据交互
     */
    public class SaftyJavaScriptInterface {

        @JavascriptInterface
        public void callback(String json) {
            String datasJson = "";
            String show = "";
            // 转换数据
            if (!TextUtils.isEmpty(json)) {
                try {
                    JSONObject dataJo = new JSONObject();
                    JSONArray ja = new JSONArray(json);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        if (jo.getBoolean("isSelected")) {
                            show += (i+1) + ",";
                            // 父项
                            JSONObject item = jo.getJSONObject("item");
                            dataJo.put(item.getString("code"), item.getString("name"));  // 父项code：父项name
                            // 子项   2017-09-20 改为多选
                            JSONArray selectedJa = jo.getJSONArray("selectedCode");
                            for(int j = 0; j < selectedJa.length(); j++) {
                                String selectedCode = selectedJa.getString(j);
                                JSONArray childItems = jo.getJSONArray("childItems");
                                for (int k = 0; k < childItems.length(); k++) {     // 放入选中子项
                                    JSONObject childItem = childItems.getJSONObject(k);
                                    if (childItem.getString("code").equals(selectedCode)) {
                                        dataJo.put(childItem.getString("code"), childItem.get("name")); // 子项code：子项name
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    datasJson = dataJo.toString();
                    if (!TextUtils.isEmpty(show)) {
                        show = show.substring(0, show.length() - 1 );
                    } else {
                        show = "0";
                    }
                    if (datasJson.equals("{}")) {
                        datasJson = "";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Observable.just(new String[]{datasJson, show}).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String[]>() {
                        @Override
                        public void call(String[] strings) {
                            if (mOnJsCallback != null) {
                                mOnJsCallback.onCallback(strings[0], strings[1]);
                            }
                        }
                    });
        }
    }

    public interface OnJsCallback {
        void onCallback(String dataJson, String show);
    }

    public void setOnJsCallback(OnJsCallback onJsCallback) {
        this.mOnJsCallback = onJsCallback;
    }
}

