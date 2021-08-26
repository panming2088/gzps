package com.augurit.am.cmpt.maintenance.traffic.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.os.IBinder;

import com.augurit.am.cmpt.maintenance.cache.util.StorageManageUtil;
import com.augurit.am.cmpt.maintenance.traffic.dao.MySQLiteOpenHelper;
import com.augurit.am.cmpt.maintenance.traffic.model.TrafficInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * 判断是2g/3g还是wifi，设置个变量W，然后判断，
 * 如果开始就是wifi状态，就获取那个uid流量T，然后赋值给W，直到切换状态为止；
 * 如果切到2g/3g就获取，但是不加入那个变量，就是T，T-W为3g流量，记为G，
 * 如果一直没切换，就是W一直未变；此后如果切换了状态，就用T（最新）-G=W（最新）；
 */

public class TrafficService extends Service {

    private TrafficReceiver tReceiver;  //网络状态广播接收者
    private WifiManager wifiManager;
    private ConnectivityManager cManager;
    private PackageManager pm;
    List<TrafficInfo> trafficInfosOrigin = new ArrayList<TrafficInfo>();
    List<TrafficInfo> trafficInfosWifi;
    List<TrafficInfo> trafficInfosGprs;
    MySQLiteOpenHelper dbhelper = new MySQLiteOpenHelper(this);
    private boolean isWIFI;
    private boolean isGPRS;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // 初始化
        trafficInfosOrigin = getTrafficInfos();
        // WifiManager,ConnectivityManager
        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        cManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 注册TrafficReceiver
        tReceiver = new TrafficReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(tReceiver, filter);
        StorageManageUtil.autoClear(getApplicationContext());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     *
     * @description 网络状态广播接收
     */
    private class TrafficReceiver extends BroadcastReceiver {
        private String action = "";
        private static final String TAG = "TrafficReceiver";

        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();

            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                isWIFI = true;
                if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                    // 启用线程每隔两秒统计WIFI流量
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            while (isWIFI) {
                                try {
                                    Thread.sleep(2000);
                                    trafficInfosWifi = new ArrayList<TrafficInfo>();
                                    trafficInfosWifi = getTrafficInfos();

                                    for (TrafficInfo infoO : trafficInfosOrigin)
                                        for (TrafficInfo infoW : trafficInfosWifi) {
                                            //两个相减，得出2秒钟前到现在所消耗的流量，然后作为一条记录保存到数据库
                                            long traffic = infoW.getTraffic()
                                                    - infoO.getTraffic();
                                            if (infoO.getPackageName().equals(
                                                    infoW.getPackageName())
                                                    && traffic != 0) {
                                                //保存一条记录到数据库
                                                dbhelper.insertTrafficWIFI(traffic, infoW.getPackageName());
                                            }
                                        }
                                    trafficInfosOrigin = trafficInfosWifi;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                } else if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
                    //停止统计WIFI
                    isWIFI = false;
                }
            } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                NetworkInfo networkInfo = cManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                //TODO to 胜洪
                if (networkInfo == null){
                    return;
                }
                State state = networkInfo.getState();
                if (state == State.CONNECTED
                        && !(wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED)) {
                    // 启用线程每隔两秒统计GPRS流量
                    isGPRS = true;
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            while (isGPRS) {

                                try {
                                    Thread.sleep(2000);
                                    trafficInfosGprs = new ArrayList<TrafficInfo>();
                                    trafficInfosGprs = getTrafficInfos();
                                    for (TrafficInfo infoO : trafficInfosOrigin)
                                        for (TrafficInfo infoG : trafficInfosGprs) {
                                            //两个相减，得出2秒钟前到现在所消耗的流量，然后作为一条记录保存到数据库
                                            long traffic = infoG.getTraffic()
                                                    - infoO.getTraffic();
                                            if (infoO.getPackageName().equals(
                                                    infoG.getPackageName())
                                                    && traffic != 0) {
                                                //保存一条记录到数据库
                                                dbhelper.insertTrafficGPRS(traffic, infoG.getPackageName());
                                            }
                                        }
                                    trafficInfosOrigin = trafficInfosGprs;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }).start();
                } else if (state == State.DISCONNECTED) {
                    //停止统计GPRS
                    isGPRS = false;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(tReceiver);
        super.onDestroy();
    }

    /**
     * 获取所有流量信息
     *
     * @return List<TrafficInfo> MyTrafficInfos
     */
    public List<TrafficInfo> getTrafficInfos() {
        List<TrafficInfo> MyTrafficInfos = new ArrayList<TrafficInfo>();

        pm = getPackageManager();
        List<PackageInfo> packinfos = pm
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
                        | PackageManager.GET_PERMISSIONS);

        for (PackageInfo info : packinfos) {
            //只获取本应用的流量信息，如需获取所有应用流量，注释此if语句
            if (!info.packageName.equals(TrafficService.this.getPackageName())) {
                continue;
            }
            String[] premissions = info.requestedPermissions;
            if (premissions != null && premissions.length > 0) {
                for (String premission : premissions) {
                    //只计算申请了网络权限的应用
                    if ("android.permission.INTERNET".equals(premission)) {
                        int uid = info.applicationInfo.uid;
                        //getUidRxBytes(uid)根据UID获取开机以来接收的数据量，
                        //getUidTxBytes(uid)获取开机以来发送的数据量，
                        //两个相加才是开机以来消耗的流量
                        long total = TrafficStats.getUidRxBytes(uid)
                                + TrafficStats.getUidTxBytes(uid);
                        if (total < 0) {
                            TrafficInfo trafficInfo = new TrafficInfo();
                            trafficInfo.setPackageName(info.packageName);
                            trafficInfo.setTraffic(0);
                            MyTrafficInfos.add(trafficInfo);
                        } else {
                            TrafficInfo trafficInfo = new TrafficInfo();
                            trafficInfo.setPackageName(info.packageName);
                            trafficInfo.setTraffic(total);
                            MyTrafficInfos.add(trafficInfo);
                        }
                    }
                }
            }
        }
        return MyTrafficInfos;
    }
}
