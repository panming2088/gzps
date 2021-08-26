package com.augurit.am.cmpt.update.view;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.cmpt.update.dao.AppInnerDownLoder;
import com.augurit.am.cmpt.update.dao.FileDownloadManager;
import com.augurit.am.cmpt.update.dao.StatusBarDownLoader;
import com.augurit.am.cmpt.update.event.ApkInstallReceiver;
import com.augurit.am.cmpt.update.model.UpdateAppInfo;
import com.augurit.am.cmpt.update.service.CheckUpdateService;
import com.augurit.am.cmpt.update.utils.CheckUpdateUtils;
import com.augurit.am.cmpt.update.utils.UpdateState;
import com.augurit.am.cmpt.update.utils.UriToPathUtil;
import com.augurit.am.fw.utils.file.SharedPreferencesUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.io.File;
import java.util.AbstractCollection;
import java.util.List;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.common.update.view
 * @createTime 创建时间 ：17/5/10
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/5/10
 * @modifyMemo 修改备注：
 */

public class ApkUpdateManager {
    private Context mContext;
    private AlertDialog.Builder mDialog;
    private String mUpdateState;
    private NoneUpdateCallback mNoneUpdateCallback;

    /**
     * INNER_UPDATE 应用内更新 同步更新下使用该构造函数,该回调函数会再检查后进行调用
     *
     * @param context
     * @param updateState
     * @param noneUpdateCallback
     */
    public ApkUpdateManager(Context context, String updateState, NoneUpdateCallback noneUpdateCallback) {
        this.mContext = context;
        this.mUpdateState = updateState;
        this.mNoneUpdateCallback = noneUpdateCallback;
    }

    /**
     * NOTIFICATION_UPDATE  WIFI_AUTO_UPDATE_NEXT  WIFI_AUTO_UPDATE_NOW
     * 其他异步情况下使用该构造函数,不需要回调函数
     *
     * @param context
     * @param updateState
     */
    public ApkUpdateManager(Context context, String updateState) {
        this.mContext = context;
        this.mUpdateState = updateState;
    }

    public void checkUpdate() {
        final int curVersion = getLocalVersion();
        //网络检查版本是否需要更新
        CheckUpdateUtils.checkUpdate("apk", String.valueOf(curVersion), new CheckUpdateUtils.CheckCallBack() {
            @Override
            public void onSuccess(UpdateAppInfo updateInfo) {
                final String isForce = updateInfo.data.getLastForce();//是否需要强制更新
                final String downUrl = updateInfo.data.getUpdateurl();//apk下载地址
                //String downUrl = "http://192.168.175.2:8080/agpatrol-2.0.apk";//apk下载地址
                final String updateinfo = updateInfo.data.getUpgradeinfo();//apk更新详情
                final String appName = updateInfo.data.getAppname();
                final String version = updateInfo.data.getVersionCode();
                final int ver = Integer.valueOf(version);
                if (checkIfApkExist(ver)) {
                    return;
                }
                if (curVersion < ver) {
                    PermissionsUtil.getInstance().requestPermissions((Activity) mContext, new PermissionsUtil.OnPermissionsCallback() {
                        @Override
                        public void onPermissionsGranted(List<String> perms) {
                            //WIFI下不提示,先下载为强
                            if (mUpdateState.equals(UpdateState.WIFI_AUTO_UPDATE_NOW) || mUpdateState.equals(UpdateState.WIFI_AUTO_UPDATE_NEXT)) {
                                //WiFi环境下
                                if (isWifiEnabled(mContext)) {
                                    //启动Service
                                    Intent intent = new Intent(mContext, CheckUpdateService.class);
                                    intent.putExtra("state", mUpdateState);
                                    intent.putExtra("url", downUrl);
                                    intent.putExtra("appname", appName);
                                    mContext.startService(intent);
                                    return;
                                }
                            }
                            if (isForce.equals("1") && !TextUtils.isEmpty(updateinfo)) {//强制更新
                                forceUpdate(mContext, appName, downUrl, updateinfo);
                            } else {//非强制更新
                                //正常升级
                                normalUpdate(mContext, appName, downUrl, updateinfo);
                            }
                        }

                        @Override
                        public void onPermissionsDenied(List<String> perms) {
                            ToastUtil.shortToast(mContext, "未授予存储权限，无法下载更新！");
                        }
                    }
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                } else {
                    //noneUpdate(mContext);
                }
            }

            @Override
            public void onError() {
                //noneUpdate(mContext);
            }
        });
    }

    private boolean checkIfApkExist(int ver) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
        long downloadId = sharedPreferencesUtil.getLong("KEY_DOWNLOAD_ID", -1L);
        if (downloadId != -1L) {
            FileDownloadManager fdm = FileDownloadManager.getInstance(mContext);
            int status = fdm.getDownloadStatus(downloadId);
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                Uri uri = fdm.getDownloadUri(downloadId);
                if (uri != null) {
                    updateWithoutNet(mContext, uri);
                    DownloadManager downloadManager = fdm.getDownloadManager();
                    downloadManager.remove(downloadId);
                    sharedPreferencesUtil.setLong("KEY_DOWNLOAD_ID", -1L);
                    return true;
                    //当前已下载apk的版本是否是最新的版本
                    /*
                    if(!equal(getApkInfo(mContext,uri.getPath()),ver)){
                        DownloadManager downloadManager = fdm.getDownloadManager();
                        downloadManager.remove(downloadId);
                        return false;
                    }

                    if (compare(getApkInfo(mContext, uri.getPath()), mContext)) {
                        updateWithoutNet(mContext,downloadId);
                        return true;
                    }else {
                        DownloadManager downloadManager = fdm.getDownloadManager();
                        downloadManager.remove(downloadId);
                    }*/

                }
            }

        }

        return false;
    }

    /**
     * 当前已经下载好了,提示更新
     *
     * @param context
     */
    private void updateWithoutNet(final Context context, final Uri uri) {

        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("版本更新！");
        mDialog.setMessage("新版本已经在WIFI环境下下载好了,是否进行安装更新?");
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent install = new Intent(Intent.ACTION_VIEW);
                //  DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                //  Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadId);
                if (uri != null) {
                    //Android 7.0采用了StrictMode API政策,私有目录被限制访问
                    //判断Android版本是否是Android7.0以上
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                        String path = UriToPathUtil.getRealFilePath(context, uri);
                        File file = new File(path);
                        Uri temp = FileProvider.getUriForFile(mContext, mContext.getPackageName()+".FileProvider", file);
                        //添加这一句表示对目标应用临时授权该Uri所代表的文件
                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        install.setDataAndType(temp, "application/vnd.android.package-archive");
                        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(install);
                    } else {
                        install.setDataAndType(uri, "application/vnd.android.package-archive");
                        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(install);
                    }
                } else {

                }
            }
        }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false).create().show();

        /*
        mDialog = new AlertDialog.Builder(context);
        if(context instanceof Activity){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            View view = inflater.inflate(R.layout.custom_alert_dialog,null);
            mDialog.setView(view);
            mDialog.create().show();
        }*/


    }

    /**
     * 强制更新
     *
     * @param context
     * @param appName
     * @param downUrl
     * @param updateinfo
     */
    private void forceUpdate(final Context context, final String appName, final String downUrl, final String updateinfo) {
      /*
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle(appName + "又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState()) {
                    showDownloadSetting();
                    return;
                }
                if (mUpdateState.equals(UpdateState.INNER_UPDATE)) {
                    AppInnerDownLoder.downLoadApk(mContext, downUrl, appName);
                } else {

                    //注册下载监听
                    ApkInstallReceiver receiver = new ApkInstallReceiver();
                    mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    StatusBarDownLoader.download(mContext, downUrl, updateinfo, appName);
                }
            }
        }).setCancelable(false).create().show();
        */
        mDialog = new AlertDialog.Builder(context);
        if(context instanceof Activity){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            View view = inflater.inflate(R.layout.custom_alert_dialog,null);
            TextView title = (TextView)view.findViewById(R.id.title);
            title.setText(appName + "又更新咯！");
            TextView message = (TextView)view.findViewById(R.id.message);
            message.setText(updateinfo);
            TextView sure =(TextView)view.findViewById(R.id.sure);
            sure.setText("立即更新");
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!canDownloadState()) {
                        showDownloadSetting();
                        return;
                    }
                    if (mUpdateState.equals(UpdateState.INNER_UPDATE)) {
                        AppInnerDownLoder.downLoadApk(mContext, downUrl, appName);
                    } else {

                        //注册下载监听
                        ApkInstallReceiver receiver = new ApkInstallReceiver();
                        mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        StatusBarDownLoader.download(mContext, downUrl, updateinfo, appName);
                    }
                }
            });
            mDialog.setView(view);
            mDialog.setCancelable(false);
            mDialog.create().show();
        }
    }

    /**
     * 正常更新
     *
     * @param context
     * @param appName
     * @param downUrl
     * @param updateinfo
     */
    private void normalUpdate(Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle(appName + "又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState()) {
                    showDownloadSetting();
                    return;
                }
                if (mUpdateState.equals(UpdateState.INNER_UPDATE)) {
                    AppInnerDownLoder.downLoadApk(mContext, downUrl, appName);
                } else {

                    //注册下载监听
                    ApkInstallReceiver receiver = new ApkInstallReceiver();
                    mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    StatusBarDownLoader.download(mContext, downUrl, updateinfo, appName);
                }
            }
        }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNoneUpdateCallback.onFinish();
                dialog.dismiss();
            }
        }).setCancelable(false).create().show();
    }

    /**
     * 无需更新
     *
     * @param context
     */
    private void noneUpdate(Context context) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("版本更新")
                .setMessage("当前已是最新版本无需更新")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        mNoneUpdateCallback.onFinish();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false).create().show();
    }

    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            mContext.startActivity(intent);
        }
    }

    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = mContext.getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private boolean canDownloadState() {
        try {
            int state = mContext.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private int getLocalVersion() {
        PackageManager manager = mContext.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // Log.e(TAG, "获取应用程序版本失败，原因：" + e.getMessage());
            return 0;
        }

        return Integer.valueOf(info.versionCode);
    }

    /**
     * 获取apk程序信息[packageName,versionName...]
     *
     * @param context Context
     * @param path    apk path
     */
    private static PackageInfo getApkInfo(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            //String packageName = info.packageName;
            //String version = info.versionName;
            //Log.d(TAG, "packageName:" + packageName + ";version:" + version);
            //String appName = pm.getApplicationLabel(appInfo).toString();
            //Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息
            return info;
        }
        return null;
    }

    /**
     * 当前已经下载版本是服务器更新信息里面的版本
     *
     * @param apkinfo
     * @param version
     * @return
     */
    private static boolean equal(PackageInfo apkinfo, int version) {
        if (apkinfo == null) {
            return false;
        }

        return apkinfo.versionCode == version;

    }

    /**
     * 下载的apk和当前程序版本比较
     *
     * @param apkInfo apk file's packageInfo
     * @param context Context
     * @return 如果当前应用版本小于apk的版本则返回true
     */
    private static boolean compare(PackageInfo apkInfo, Context context) {
        if (apkInfo == null) {
            return false;
        }
        String localPackage = context.getPackageName();
        if (apkInfo.packageName.equals(localPackage)) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(localPackage, 0);
                if (apkInfo.versionCode > packageInfo.versionCode) {
                    return true;
                } else {

                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 判断WIFI是否打开
     *
     * @param context
     * @return
     */
    public boolean isWifiEnabled(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 检查版本后无后续更新操作
     * 注意:异步操作不会执行此回调
     */
    public interface NoneUpdateCallback {
        void onFinish();
    }
}
