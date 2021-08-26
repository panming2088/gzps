package com.augurit.agmobile.patrolcore.selectlocation.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.selectlocation.model.OnSelectLocationFinishEvent;
import com.augurit.agmobile.patrolcore.selectlocation.util.SelectLocationConstant;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.file.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 选择位置界面
 *
 * @author 创建人 ：luobiao
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.map
 * @createTime 创建时间 ：2017-07-14
 * @modifyBy 修改人 ：luobiao
 * @modifyTime 修改时间 ：2017-07-14
 * @modifyMemo 修改备注：
 */

public class WebViewSelectLocationActivity extends AppCompatActivity {
    public WebView mWebView;
    private String mUrl;
    private String lng;
    private String lat;
    private String addr;
    private String zoom;
    private boolean mIfReadOnly;
    private LatLng mDestinationOrLastTimeSelectLocation;
    private String mLastSelectedAddress;
    private double mInitialScale = -1;
    protected ILayersService mILayersService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_map_patrol);
//        mILayersService = PatrolLayerServiceProvider.provideLayerService(WebViewSelectLocationActivity.this);
        mIfReadOnly = getIntent().getBooleanExtra(SelectLocationConstant.IF_READ_ONLY, false);

        mDestinationOrLastTimeSelectLocation = getIntent().getParcelableExtra(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_LOCATION);

        mLastSelectedAddress = getIntent().getStringExtra(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_ADDRESS);

        mInitialScale = getIntent().getDoubleExtra(SelectLocationConstant.INITIAL_SCALE, -1);

        //1、userId需登录后传人  2、http://192.168.32.13:8080/agweb也可根据需要sp动态配置
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitActivity();
            }
        });

        mWebView = (WebView) findViewById(R.id.edit_wv);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.pb);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSaveFormData(true);
        webSettings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        // 设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setGeolocationEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        mWebView.addJavascriptInterface(new JavaScriptinterface(WebViewSelectLocationActivity.this), "android");//第二个参数用于在js中调用第一个参数对象的方法
//        mWebView.loadUrl(mUrl);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pb.setVisibility(View.VISIBLE);
                if (newProgress == 100) {
                    pb.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            //当点击链接时,希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);  //加载新的url

                return false;    //返回true,代表事件已处理,事件流到此终止
            }

        });
        loadWebview();

    }

    private void loadWebview() {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(WebViewSelectLocationActivity.this);
        mUrl = sharedPreferencesUtil.getString("twoUrl", "");
        if (TextUtils.isEmpty(mUrl)) {
            return;
        }
        mUrl = mUrl + "?userId=" + BaseInfoManager.getUserId(WebViewSelectLocationActivity.this) + "&serverUrl=" + BaseInfoManager.getBaseServerUrl(WebViewSelectLocationActivity.this);
        if (mDestinationOrLastTimeSelectLocation != null) {
            mUrl += "&longitude=" + mDestinationOrLastTimeSelectLocation.getLongitude() + "&latitude=" + mDestinationOrLastTimeSelectLocation.getLatitude();
        }
        if (mInitialScale != -1) {
            mUrl += "&zoom=" + mInitialScale;
        }
        mWebView.loadUrl(mUrl);

    }

    private void exitActivity() {
        this.finish();
        overridePendingTransition(R.anim.slide_from_lefthide_to_rightshow, R.anim.slide_from_leftshow_to_righthide);
    }

    public class JavaScriptinterface {
        Context context;

        public JavaScriptinterface(Context context) {
            this.context = context;
        }

        /**
         * 与js交互时用到的方法，在js里调用
         */
        @JavascriptInterface
        public void fromJsData(String json) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);

                JSONObject coordinate = jsonObject.getJSONObject("coordinate");
                lng = coordinate.getString("longitude");
                lat = coordinate.getString("latitude");
                zoom = jsonObject.getString("zoom");
                addr = jsonObject.getString("info") + jsonObject.getString("detail");

                EventBus.getDefault().post(new OnSelectLocationFinishEvent(new LatLng(Double.valueOf(lat), Double.valueOf(lng)), Double.valueOf(zoom), addr));
                exitActivity();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitActivity();
    }
}
