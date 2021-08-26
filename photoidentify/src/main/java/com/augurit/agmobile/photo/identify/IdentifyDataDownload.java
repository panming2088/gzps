package com.augurit.agmobile.photo.identify;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.photo.identify
 * @createTime 创建时间 ：2017/9/20
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/9/20
 * @modifyMemo 修改备注：
 */

public class IdentifyDataDownload {
    public static void download(final Context context, final String url, final FileUtils.FileOperateCallback callback){
        AlertDialog.Builder mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("网络数据初始化");
        mDialog.setMessage("首次使用需要下载初始化数据,请耐心等待下载,否则应用无法正常使用!");
        mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    DataInnerDownLoder.downLoadData(context,url,callback);
            }
        }).setCancelable(false).create().show();
    }
}
