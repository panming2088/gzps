package com.augurit.agmobile.gzps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.service.GzpsService;
import com.augurit.agmobile.gzps.common.widget.PatrolAlphaTabsIndicator;
import com.augurit.agmobile.gzps.componentmaintenance.IChangeTabListener;
import com.augurit.agmobile.gzps.im.CommunicationFragment;
import com.augurit.agmobile.gzps.login.PatrolLoginService;
import com.augurit.agmobile.gzps.setting.SettingFragment;
import com.augurit.agmobile.gzps.setting.model.SignBean;
import com.augurit.agmobile.gzps.setting.model.SignResultBean;
import com.augurit.agmobile.gzps.setting.service.SignService;
import com.augurit.agmobile.gzps.setting.view.LegalNoticeDialog;
import com.augurit.agmobile.gzps.statistic.view.StatisticsFragment2;
import com.augurit.agmobile.gzps.workcation.WorkcationFragment;
import com.augurit.agmobile.mapengine.common.AGMobileSDK;
import com.augurit.agmobile.mapengine.common.baidutransform.pointer.BDPointer;
import com.augurit.agmobile.mapengine.common.baidutransform.pointer.WGSPointer;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.agmobile.patrolcore.common.action.dao.local.ActionDBLogic;
import com.augurit.agmobile.patrolcore.common.action.util.ActionNameConstant;
import com.augurit.agmobile.patrolcore.common.action.util.ActionNoConstant;
import com.augurit.agmobile.patrolcore.common.device.DeviceRegisterManager;
import com.augurit.agmobile.patrolcore.common.device.event.CheckDeviceRegisterEvent;
import com.augurit.agmobile.patrolcore.common.device.model.RegisterModel;
import com.augurit.agmobile.patrolcore.common.model.LocationInfo;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.widget.LocationButton;
import com.augurit.agmobile.patrolcore.locate.BaiduLocationManager;
import com.augurit.agmobile.patrolcore.search.view.SearchActivity;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.log.AMLogReport;
import com.augurit.am.fw.log.save.imp.AMCrashWriter;
import com.augurit.am.fw.log.upload.email.AMEmailReporter;
import com.augurit.am.fw.log.upload.http.AMHttpReporter;
import com.augurit.am.fw.net.util.NetBroadcastReceiver;
import com.augurit.am.fw.net.util.NetworkUtil;
import com.augurit.am.fw.utils.DeviceIdUtils;
import com.augurit.am.fw.utils.DoubleClickExitHelper;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;
import me.leolin.shortcutbadger.ShortcutBadger;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol
 * @createTime 创建时间 ：2017-03-30
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-30
 * @modifyMemo 修改备注：
 */

public class PatrolMainActivity extends BaseActivity implements IChangeTabListener,
        IDrawerController, IUnReadMessageObserver {
    private PatrolAlphaTabsIndicator alphaTabsIndicator;
    private DoubleClickExitHelper mHelper;
    private ViewPager mViewPger;
    private Fragment currentFragment;
    private ActionDBLogic mLocalMenuStorageDao;
    protected ViewGroup progress_linearlayout;
    protected DrawerLayout drawer_layout;
    private LocationButton locationButton;
    private BaiduLocationManager baiduLocationManager;
    //签到信息
    private int year;
    private int month;
    private int day;
    private double x;
    private double y;
    private String street;
    private String address;
    private SignService signService;
    private String nowTime;
    private int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    private String lastUserId = "";
    private String signTime = "";

    private NetBroadcastReceiver netBroadcastReceiver; //网络状态变化广播接收器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        EventBus.getDefault().register(this);
        mHelper = new DoubleClickExitHelper(this);

        mViewPger = (ViewPager) findViewById(R.id.mViewPager);
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());
        mViewPger.setAdapter(mainAdapter);
        mViewPger.addOnPageChangeListener(mainAdapter);
        mViewPger.setOffscreenPageLimit(5);
        mViewPger.setCurrentItem(2);
        mainAdapter.onPageSelected(2);

        alphaTabsIndicator = (PatrolAlphaTabsIndicator) findViewById(R.id.alphaIndicator);
        alphaTabsIndicator.setViewPager(mViewPger);
        alphaTabsIndicator.getTabView(2).performClick();

        //初始化DrawerLayer
        progress_linearlayout = (ViewGroup) findViewById(R.id.progress_linearlayout);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置右面的侧滑菜单只能通过编程来打开
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END);
        //alphaTabsIndicator.getTabView(0).showNumber(6);
        //alphaTabsIndicator.getTabView(1).showNumber(1);
        //alphaTabsIndicator.getTabView(2).showNumber(88);
        //alphaTabsIndicator.getTabView(3).showPoint();

        initComponentLayers();
        registerNetworkStateReceiver();
        initLogReport();
        initRongIMMessageCountListener();
        initTime();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(300);
                    checkLegalNotice();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
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

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String saveTime = BaseInfoManager.getSign(this);
        String[] arr = saveTime.split("_");
        if (arr != null && arr.length == 2) {
            lastUserId = arr[0];
            signTime = arr[1];
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        if (!BaseInfoManager.getUserId(this).equals(lastUserId) || !str.equals(getDateToString(Long.valueOf(signTime)))) {
            getSignInfo();
        }
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
                                /**
                                 * 只有有定位权限，才让进行签到
                                 */
                                startLocate();
                            }

                            @Override
                            public void onPermissionsDenied(List<String> perms) {
                                ToastUtil.shortToast(PatrolMainActivity.this, "未授予定位权限，无法签收！");
                            }
                        },
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
//        }
    }

    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(d);
    }

    /**
     * 获取签到信息
     */
    private void getSignInfo() {
        String monthStr = "" + month;
        if (month < 10) {
            monthStr = "0" + monthStr;
        }
        if (signService == null) {
            signService = new SignService(this);
        }
        signService.getSignInfo(year + "" + monthStr + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(SignBean signBean) {
                        if (signBean == null || signBean.getMonthlySignDate() == null) {
                            signBean = new SignBean();
                            signBean.setMonthlySignDate(new ArrayList<String>());
                        }
                        String signList = JsonUtil.getJson(signBean);
                        String dayStr = "" + day;
                        if (day < 10) {
                            dayStr = "0" + day;
                        }
                        if (signBean.getMonthlySignDate() != null && signBean.getMonthlySignDate().contains(dayStr)) {
                        } else {
                            checkLocationPermission();
                        }
                    }
                });
    }

    private void startLocate() {
        baiduLocationManager = new BaiduLocationManager(this);
        baiduLocationManager.startLocate(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                LocationInfo lastLocation = baiduLocationManager.getLastLocation();
                if (lastLocation != null) {
                    baiduLocationManager.stopLocate();
                    BDPointer bdPointer = new BDPointer(location.getLatitude(), location.getLongitude());
                    WGSPointer wgsPointer = bdPointer.toWGSPointer();
                    double curX = wgsPointer.getLongtitude();
                    double curY = wgsPointer.getLatitude();
                    if (curX <= 0 || curY <= 0) {
                        ToastUtil.shortToast(PatrolMainActivity.this, "定位失败，无法签到，请确认是否已授予定位权限");
                        return;
                    }
                    x = curX;
                    y = curY;
                    street = lastLocation.getDetailAddress().getStreet();
                    address = lastLocation.getDetailAddress().getDetailAddress();
                    sign();
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

    /**
     * 获取签到信息
     */
    private void sign() {
        if (signService == null) {
            signService = new SignService(this);
        }
        String appVersion = android.os.Build.BRAND + ";" + android.os.Build.MODEL
                + ";" + android.os.Build.VERSION.RELEASE + ";" + getLocalVersionName(this);
        signService.sign(x, y, street, address, appVersion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignResultBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.iconLongToast(PatrolMainActivity.this, R.mipmap.ic_alert_yellow, "签到失败！");
                    }

                    @Override
                    public void onNext(SignResultBean signBean) {
                        if (signBean == null) {
                            ToastUtil.iconLongToast(PatrolMainActivity.this, R.mipmap.ic_alert_yellow, "签到失败！");
                            return;
                        }
                        String time = BaseInfoManager.getUserId(PatrolMainActivity.this) + "_" + String.valueOf(signBean.getSignTime());
                        BaseInfoManager.setSign(PatrolMainActivity.this, time);
                        if (!signBean.getFirstSign()) {
                            return;
                        }
                        ToastUtil.iconLongToast(PatrolMainActivity.this, R.mipmap.ic_alert_yellow, "签到成功！");
                    }
                });
    }

    private void initRongIMMessageCountListener() {

        final Conversation.ConversationType[] conversationTypes = {Conversation.ConversationType.PRIVATE, Conversation.ConversationType.DISCUSSION,
                Conversation.ConversationType.GROUP};
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!LayerUrlConstant.ifLayerUrlInitSuccess()) {
            LayerUrlConstant.initComponentLayers(getApplicationContext(), null);
        }

//        PermissionsUtil2.getInstance().requestPermissions(this, "需要定位权限才能进行签到，请点击确定允许", 120, new PermissionsUtil2.OnPermissionsCallback() {
//            @Override
//            public void onPermissionsGranted(List<String> perms) {
//                ToastUtil.shortToast(getApplicationContext(), "授权了");
//            }
//        });

    }

    public void finishSelf() {
        if (!ListUtil.isEmpty(fragments) && fragments.get(1) instanceof WorkcationFragment) {
            ((WorkcationFragment) fragments.get(1)).finishTrackService();
        }
        super.finish();
    }

    private void initComponentLayers() {
        LayerUrlConstant.initComponentLayers(getApplicationContext(), new Callback2<String[]>() {
            @Override
            public void onSuccess(String[] result) {
                if (!Boolean.valueOf(result[0])) {
                    if (result[1] != null && !result[1].contains("新增")) {
                        AMCrashWriter.getInstance(getApplicationContext())
                                .writeCrash(new Exception("设施图层初始化失败--" + result[1]), "设施图层初始化失败--" + result[1]);
                        CrashReport.postCatchedException(new Exception("设施图层初始化失败--" + result[1]));
                    }
                }
            }

            @Override
            public void onFail(Exception error) {
                AMCrashWriter.getInstance(getApplicationContext()).writeCrash(error, "设施图层初始化失败");
                CrashReport.postCatchedException(new Exception("设施图层初始化失败--" + error.getMessage()));
            }
        });

    }

    private void registerNetworkStateReceiver() {
        NetBroadcastReceiver.evevt = new NetBroadcastReceiver.NetEvent() {
            @Override
            public void onNetChange(int netMobile) {
                if (netMobile != NetworkUtil.NETWORK_NONE
                        && !LayerUrlConstant.ifLayerUrlInitSuccess()) {
                    initComponentLayers();
                }
            }
        };
        netBroadcastReceiver = new NetBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netBroadcastReceiver, filter);
    }


    /**
     * 跳转到历史界面
     */
    public void jumpToHistoryPage() {
        // mViewPger.setCurrentItem(2);
        startActivity(new Intent(this, SearchActivity.class));
    }

    @Override
    public void changeToTab(int tabNo) {

    }

    @Override
    public void onCountChanged(int i) {
        alphaTabsIndicator.getTabView(0).showNumber(i);
        //ToastUtil.shortToast(PatrolMainActivity.this,i+"条未读");
        ShortcutBadger.applyCount(PatrolMainActivity.this, i);
    }

    private List<Fragment> fragments = new ArrayList<>();

    private class MainAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {

        //即时通讯
        private String[] titles = {"通讯", "工作台", "统计", "通讯", "我的"};

        public MainAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(new CommunicationFragment());
            fragments.add(new WorkcationFragment());
            fragments.add(new PatrolMainFragment2());
            fragments.add(new StatisticsFragment2());
            fragments.add(SettingFragment.newInstance(titles[3]));
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override

        public void onPageSelected(int position) {
            if (position != 1) {
                locationButton = ((WorkcationFragment) fragments.get(1)).getLocationButton();
                if (locationButton != null && locationButton.ifLocating()) {
                    locationButton.setStateNormal();
                }
            }
//            if ( position == 1) {
//                EventBus.getDefault().post(new OnRefreshEvent());
//            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //askStopTrack();
            // return true;
            return mHelper.onKeyDown(keyCode, new DoubleClickExitHelper.AppExitListener() {
                @Override
                public void beforeExit() {
                    AGMobileSDK.destroy();
                }
            });
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    private void askStopTrack() {
        if (TrackService.getInstance(this).getCurrentTrackId() == -1) {
            mHelper.onKeyDown(KeyEvent.KEYCODE_BACK);
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("对话框")
                .setMessage("当前正在记录运动轨迹，确定退出应用吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TrackService trackService = TrackService.getInstance(PatrolMainActivity.this);
                        LocalTrackServiceImpl localTrackService = new LocalTrackServiceImpl();
                        List<GPSTrack> gpsTracks = trackService.getmGPSTracks();
                        if (ListUtil.isEmpty(gpsTracks)
                                || gpsTracks.size() < TrackConstant.minPointAmount
                                //                        || getTrackLength() == 0
                                || trackService.getmTrackLength() < TrackConstant.minLength
//                        || getTrackTime() == 0
                                || trackService.getmTrackTimeSecond()/60 < TrackConstant.minTime){
                            localTrackService.deleteGPSTrackSByTrackId("" + trackService.getCurrentTrackId());
                        } else {
                            GPSTrack lastestGPSTrack = gpsTracks.get(gpsTracks.size() - 1);
                            lastestGPSTrack.setPointState(GPSTraceConstant.POINT_STAT_END);
                            localTrackService.saveTrackPoint(lastestGPSTrack);
                        }
                        trackService.stopLocate();
                        trackService.getmGPSTracks().clear();
                        //                        mHelper.onKeyDown(KeyEvent.KEYCODE_BACK);
                        TrackNotificationManager.getInstance(PatrolMainActivity.this).dismissNotification();
                        AppManager.getAppManager().AppExit(PatrolMainActivity.this);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
    }
    */

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        AMDatabase.getInstance().closeDatabase();  //加上这句会出现数据库实例被释放但却又再次读取数据库的闪退问题
        AGMobileSDK.destroy();
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(this);
        RongIM.getInstance().disconnect();
        NetBroadcastReceiver.evevt = null;
        if (netBroadcastReceiver != null) {
            unregisterReceiver(netBroadcastReceiver);
        }
        locationButton.unregisterSensor();
//        android.os.Process.killProcess(android.os.Process.myPid());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void initLogReport() {
        //初始化异常日志记录
        initCrashReport();

        //异常日志是否上传到服务器
        boolean isUpload = false;
        String url = null;
        TableDBService tableDBService = new TableDBService();
        Map<String, String> configureItemsFromDB = tableDBService.getConfigureItemsFromDB(ActionNameConstant.LOG_UPLOAD,
                ActionNoConstant.currentActionNo);
        if (configureItemsFromDB != null && configureItemsFromDB.size() > 0) {
            //异常日志上报服务器地址
            url = configureItemsFromDB.get("upload_url");
            if (url != null && !url.isEmpty()) {
                isUpload = true;
            }
        }

        if (isUpload) {
//            initHttpReporter(url);
        }
    }


    private void initCrashReport() {
        AMLogReport.getInstance()
                .setDebug(false)
                .setCacheSize(50 * 1024 * 1024)//支持设置缓存大小，超出后清空，这里设置为50M，默认为30M
                .setLogDir(getApplicationContext(),
                        new FilePathUtil(this).getSavePath() + "/")//定义路径为：sdcard/{应用名}/AMLog/
                // .setEncryption(new AESEncode()) //支持日志到AES加密或者DES加密，默认不开启
                .init(getApplicationContext());
        //        initEmailReporter();
        //        initHttpReporter();

    }


    /**
     * 使用EMAIL发送日志
     */
    private void initEmailReporter() {
        AMEmailReporter email = new AMEmailReporter(this.getApplicationContext());
        email.setReceiver("419078705@qq.com");//收件人邮箱
        email.setSender("13265125802@163.com");//发送人邮箱
        email.setSendPassword("gzps*#5802");//邮箱密码
        email.setSMTPHost("smtp.163.com");//SMTP地址
        email.setPort("465");//SMTP 端口
        AMLogReport.getInstance().addUploader(email);
    }

    /**
     * 设置上传日志到服务器
     */
    public void initHttpReporter(String url) {
        AMHttpReporter http = new AMHttpReporter(this);
        http.setUrl(url);//发送请求的地址
        http.setFileParam("fileName");//文件的参数名
        http.setToParam("to");//收件人参数名
        http.setTo("你的接收邮箱");//收件人
        http.setTitleParam("subject");//标题
        http.setBodyParam("message");//内容
        AMLogReport.getInstance().addUploader(http);

        AMLogReport.getInstance().uploadAll(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckIfDeviceRegister(CheckDeviceRegisterEvent event) {
        if (!event.isRegister()) {
            String deviceId = DeviceIdUtils.getDeviceId(this);
            new AlertDialog.Builder(PatrolMainActivity.this).setTitle("应用提示")
                    .setMessage("当前设备号为:" + deviceId + "," + "请联系维护人员进行注册!")
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).show();
        }
    }


    /**
     * 校验设备是否注册
     */
    public void checkIfDeviceRegister() {
        DeviceRegisterManager manager = new DeviceRegisterManager(this);
        if (manager.checkIfActionOn()) {
            manager.checkIfRegisterDevice().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<RegisterModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(RegisterModel registerModel) {
                            if (registerModel.getDevice() != null && registerModel.getUserId() != null &&
                                    !registerModel.getDevice().isEmpty() && !registerModel.getUserId().isEmpty()) {
                                //已经注册
                                EventBus.getDefault().post(new CheckDeviceRegisterEvent(true));

                            } else {
                                //注册失败
                                EventBus.getDefault().post(new CheckDeviceRegisterEvent(false));
                            }
                        }
                    });
        }
    }


    /**
     * 检查用户是否已经阅读法律声明
     */
    private void checkLegalNotice() {
        new GzpsService(getApplicationContext())
                .columnsReadInfo(new PatrolLoginService(getApplicationContext(), AMDatabase.getInstance()).getUser().getId(), "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<okhttp3.ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(okhttp3.ResponseBody responseBody) {
                        boolean readed = false;
                        if (responseBody != null) {
                            try {
                                String result = responseBody.string();
                                JSONObject jsonObject = new JSONObject(result);
                                int flsmUnread = jsonObject.getInt("flsmUnread");//法律声明
                                if (flsmUnread == 0) {
                                    readed = true;
                                }
                            } catch (Exception e) {
                                readed = false;
                                e.printStackTrace();
                            }
                        }
                        if (!readed) {
                            showLegalNoticeDialog();
                        }
                    }
                });
    }

    private void showLegalNoticeDialog() {
        LegalNoticeDialog legalNoticeDialog = new LegalNoticeDialog();
        legalNoticeDialog.setCancelable(false);
        legalNoticeDialog.setCallback(new Callback1() {
            @Override
            public void onCallback(Object o) {

            }
        });
        legalNoticeDialog.show(getSupportFragmentManager(), "legalNoticeDialog");

    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/

    @Override
    public void openDrawer(final OnDrawerOpenListener listener) {
        drawer_layout.openDrawer(Gravity.RIGHT);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                GravityCompat.END);    //解除锁定
        drawer_layout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (listener != null) {
                    listener.onOpened(drawerView);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //设置右面的侧滑菜单只能通过编程来打开
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                        GravityCompat.END);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void closeDrawer() {

    }

    @Override
    public ViewGroup getDrawerContainer() {
        return progress_linearlayout;
    }

}
