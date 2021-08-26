package com.augurit.agmobile.gzps.setting;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.setting.model.SignBean;
import com.augurit.agmobile.gzps.setting.model.SignResultBean;
import com.augurit.agmobile.gzps.setting.service.SignService;
import com.augurit.agmobile.mapengine.common.baidutransform.pointer.BDPointer;
import com.augurit.agmobile.mapengine.common.baidutransform.pointer.WGSPointer;
import com.augurit.agmobile.patrolcore.common.model.LocationInfo;
import com.augurit.agmobile.patrolcore.locate.BaiduLocationManager;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignActivity extends BaseActivity {

    private WebView mWebView;
    private int year;
    private int month;
    private int day;
    private TextView dateTv;
    private SignService signService;
    private BaiduLocationManager baiduLocationManager;
    private double x = 0;
    private double y = 0;
    private String street;
    private String address;
    private Button btnSign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
//        startLocateAndCheck();
        initView();
        initTime();
        getSignDataFromServer();
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    private void startLocateAndCheck() {
        PermissionsUtil.getInstance().requestPermissions(this, new PermissionsUtil.OnPermissionsCallback() {
                    @Override
                    public void onPermissionsGranted(List<String> perms) {

                    }

                    @Override
                    public void onPermissionsDenied(List<String> perms) {
                        ToastUtil.shortToast(SignActivity.this, "未授予定位权限，无法签到！");
                    }
                },
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION);
        startLocate();
    }

    private void startLocate() {

        baiduLocationManager = new BaiduLocationManager(this);
        baiduLocationManager.startLocate(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                LocationInfo lastLocation = baiduLocationManager.getLastLocation();
                if (lastLocation != null){
                    BDPointer bdPointer = new BDPointer(location.getLatitude(), location.getLongitude());
                    WGSPointer wgsPointer = bdPointer.toWGSPointer();
                    double curX = wgsPointer.getLongtitude();
                    double curY = wgsPointer.getLatitude();
                    if(curX <= 0 || curY <= 0){
                        ToastUtil.shortToast(SignActivity.this, "定位失败，无法签到，请确认是否已授予定位权限");
                        return;
                    }
                    x = curX;
                    y = curY;
                    street = lastLocation.getDetailAddress().getStreet();
                    address = lastLocation.getDetailAddress().getDetailAddress();
                    baiduLocationManager.stopLocate();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

    }

    private void getSignDataFromServer() {

    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateTv.setText(year + "-" + month);
    }

    private void initView() {
        ((TextView)findViewById(R.id.tv_title)).setText("每日签到");
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mWebView = (WebView) findViewById(R.id.wv_sign);
        btnSign = (Button) findViewById(R.id.btn_sign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationPermission();
            }
        });
        dateTv = (TextView) findViewById(R.id.year_month_tv);
        WebSettings settings = mWebView.getSettings();
        signService = new SignService(this);
        settings.setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/statistic/sign.html");
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                mWebView.loadUrl("javascript:setData();");
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                SignBean signBean = new SignBean();
                signBean.setMonthlySignDate(new ArrayList<String>());
                String signList = JsonUtil.getJson(signBean);
                mWebView.loadUrl("javascript:setData("+year+","+month+","+signList+");");
                getSignInfo();
            }
        });
    }

    public void preMonth(View view) {
        if (month == 1) {
            dateTv.setText(year - 1 + "-" + 12);
            month = 12;
            year--;
        } else {
            dateTv.setText(year + "-" + (month - 1));
            month--;
        }
        getSignInfo();
    }

    public void nextMonth(View view) {
        if (month == 12) {
            dateTv.setText(year + 1 + "-" + 1);
            month = 1;
            year++;
        } else {
            dateTv.setText(year + "-" + (month + 1));
            month++;
        }
        getSignInfo();
    }

    /**
     * 获取签到信息
     */
    private void getSignInfo() {
        String monthStr = "" + month;
        if(month < 10){
            monthStr = "0" + monthStr;
        }
        signService.getSignInfo(year+""+monthStr+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.iconLongToast(SignActivity.this, R.mipmap.ic_alert_yellow, "获取签到信息失败");
                    }

                    @Override
                    public void onNext(SignBean signBean) {
                        if(signBean == null || signBean.getMonthlySignDate() == null){
                            signBean =new SignBean();
                            signBean.setMonthlySignDate(new ArrayList<String>());
                        }
                        btnSign.setVisibility(View.VISIBLE);
                        String signList = JsonUtil.getJson(signBean);
                        mWebView.loadUrl("javascript:setData("+year+","+month+","+signList+");");
                        String dayStr = "" + day;
                        if(day < 10){
                            dayStr = "0" + day;
                        }
                        if(signBean.getMonthlySignDate() != null && signBean.getMonthlySignDate().contains(dayStr)){
                            btnSign.setEnabled(false);
                            btnSign.setText("今日已签到");
                            btnSign.setTextColor(SignActivity.this.getResources().getColor(R.color.enable_selecy));
                        } else {
                            startLocateAndCheck();
                        }
                    }
                });
    }

    /**
     * 检查是否有定位权限
     */
    public void checkLocationPermission() {
        PermissionsUtil.getInstance()
                .requestPermissions(
                        (Activity) this,
                        true,
                        new PermissionsUtil.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                if(x <= 0){
                                    ToastUtil.shortToast(SignActivity.this, "定位失败，无法签到，请确认是否已授予定位权限");
                                    return;
                                }
                                /**
                                 * 只有有定位权限，才让进行签到
                                 */
                                sign();
                            }

                            @Override
                            public void onPermissionsDenied(List<String> perms) {
                                ToastUtil.shortToast(SignActivity.this, "未授予定位权限，无法签到！");
                            }
                        },
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    /**
     * 获取签到信息
     */
    private void sign() {
        String appVersion = android.os.Build.BRAND+";"+android.os.Build.MODEL
                +";"+android.os.Build.VERSION.RELEASE+";"+getLocalVersionName(this);
        signService.sign(x,y,street,address,appVersion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignResultBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.iconLongToast(SignActivity.this, R.mipmap.ic_alert_yellow, "签到失败，请重试");
                    }

                    @Override
                    public void onNext(SignResultBean signBean) {
                        if(signBean == null){
                            ToastUtil.iconLongToast(SignActivity.this, R.mipmap.ic_alert_yellow, "签到失败，请重试");
                            return;
                        }
                        String time= BaseInfoManager.getUserId(SignActivity.this)+"_"+signBean.getSignTime();
                        BaseInfoManager.setSign(SignActivity.this,time);
                        btnSign.setEnabled(false);
                        btnSign.setText("今日已签到");
                        getSignInfo();
                    }
                });
    }


}
