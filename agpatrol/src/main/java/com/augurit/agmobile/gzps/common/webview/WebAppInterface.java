package com.augurit.agmobile.gzps.common.webview;

import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * Created by xcl on 2017/10/27.
 */

public class WebAppInterface{
    Context mContext;

    /**
     * Instantiate the interface and set the context
     */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /**
     * Show a toast from the web page
     */
    // 如果target 大于等于API 17，则需要加上如下注解
    @JavascriptInterface
    public void hybridProtocol(String flag) {
//接受js返回的结果
    }
}
