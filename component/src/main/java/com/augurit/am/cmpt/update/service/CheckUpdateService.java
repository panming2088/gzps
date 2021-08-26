package com.augurit.am.cmpt.update.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;

import com.augurit.am.cmpt.update.utils.CheckUpdateUtils;
import com.augurit.am.cmpt.update.utils.UpdateState;
import com.augurit.am.fw.utils.file.SharedPreferencesUtil;

import java.io.File;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.update.service
 * @createTime 创建时间 ：2017/7/6
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/7/6
 * @modifyMemo 修改备注：
 */

public class CheckUpdateService extends Service{

    /**广播接受者*/
    private BroadcastReceiver receiver;
    /**系统下载管理器*/
    private DownloadManager dm;
    /**系统下载器分配的唯一下载任务id，可以通过这个id查询或者处理下载任务*/
    private long enqueue;

    //更新的模式
    private  String state;
    private String url;
    private String appName;

    private AlertDialog.Builder mDialog;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        state = intent.getStringExtra("state");
        url = intent.getStringExtra("url");
        appName = intent.getStringExtra("appname");
        if(appName == null)  appName = "myapp";
        if(url == null) stopSelf();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //默认下次登录提醒安装
                if(state == null){
                    stopSelf();
                }else {
                    //下次登录提醒安装
                    if(state.equals(UpdateState.WIFI_AUTO_UPDATE_NEXT)){
                        stopSelf();

                        //当前提醒进行安装
                    }else if(state.equals(UpdateState.WIFI_AUTO_UPDATE_NOW)){
                        install(context);

                    }
                }
            }
        };

        //注册接收器
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        startDownload(url);
        return Service.START_STICKY;
    }


    /**
     * 通过隐式意图调用系统安装程序安装APK
     */
    public  void install(Context context) {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), appName + ".apk");
            Uri temp = FileProvider.getUriForFile(context, context.getPackageName()+".FileProvider", file);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(temp, "application/vnd.android.package-archive");
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(
                    new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), appName + ".apk")),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        }


    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void startDownload(String downUrl) {
        //获得系统下载器
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        //设置下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downUrl));

        //WIFI下才下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        //设置下载文件的类型
        request.setMimeType("application/vnd.android.package-archive");
        //设置下载存放的文件夹和文件名字
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, appName +".apk");
        //设置下载时或者下载完成时，通知栏是否显示
        //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setTitle("下载新版本");
        //执行异步下载，并返回任务唯一id
        enqueue = dm.enqueue(request);

        //保存下载id
        if(state.equals(UpdateState.WIFI_AUTO_UPDATE_NEXT)) {
            SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(this);
            sharedPreferencesUtil.setLong("KEY_DOWNLOAD_ID", enqueue);
        }
    }
}
