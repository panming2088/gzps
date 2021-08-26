package com.augurit.am.cmpt.update.event;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.augurit.am.cmpt.update.utils.UriToPathUtil;
import com.augurit.am.fw.utils.file.SharedPreferencesUtil;

import java.io.File;


/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.common.update.event
 * @createTime 创建时间 ：17/5/10
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/5/10
 * @modifyMemo 修改备注：
 */

public class ApkInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //DownloadManager下载完成后会发出一个广播 android.intent.action.DOWNLOAD_COMPLETE
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            long downloadApkId =intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            installApk(context, downloadApkId);

            //保存下载ID
            /*
            SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
            sharedPreferencesUtil.setLong("KEY_DOWNLOAD_ID",downloadApkId);
            */

        }
    }

    /**
     * 安装apk
     */
    private void installApk(Context context,long downloadApkId) {
        // 获取存储ID
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        long downId =sp.getLong(DownloadManager.EXTRA_DOWNLOAD_ID,-1L);
        if(downloadApkId == downId){
            DownloadManager downManager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadFileUri = downManager.getUriForDownloadedFile(downloadApkId);
            if (downloadFileUri != null) {
                String path = UriToPathUtil.getRealFilePath(context,downloadFileUri);
                if(path != null) {

                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
                        File file = new File(path);
                        Uri temp = FileProvider.getUriForFile(context, context.getPackageName()+".FileProvider", file);
                        Intent install = new Intent(Intent.ACTION_VIEW);
                        //添加这一句表示对目标应用临时授权该Uri所代表的文件
                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        install.setDataAndType(temp, "application/vnd.android.package-archive");
                        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(install);

                    }else {
                        downloadFileUri = Uri.parse("file://" + path);
                        Intent install = new Intent(Intent.ACTION_VIEW);
                        install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(install);
                    }
                }else {
                    Toast.makeText(context, "安装失败,请手动安装", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
