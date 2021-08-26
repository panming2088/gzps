package com.augurit.agmobile.gzps.common.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.augurit.am.fw.utils.ResourceUtil;


/**
 * Created by ac on 2016-07-26.
 */
public final class DialogUtil {
    private DialogUtil() {
    }

    public static void MessageBox(Context context, String tile, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(tile)) {
            builder.setTitle(tile);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        builder.setPositiveButton(ResourceUtil.getStringId(context, "sure"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    public static void MessageBoxCannotCancel(Context context, String title, String msg,
                                  DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        if (positiveListener != null) {
            builder.setPositiveButton(
                    ResourceUtil.getStringId(context, "sure"), positiveListener);
        }
        builder.setCancelable(false);
        builder.show();
    }

    public static void MessageBox(Context context, String title, String msg,
                                  DialogInterface.OnClickListener positiveListener,
                                  DialogInterface.OnClickListener negetiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        if (positiveListener != null) {
            builder.setPositiveButton(
                    ResourceUtil.getStringId(context, "sure"), positiveListener);
        }
        if (negetiveListener != null) {
            builder.setNegativeButton(
                    ResourceUtil.getStringId(context, "cancel"),
                    negetiveListener);
        }
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

//        dialog.getWindow().setDimAmount(0);
        dialog.show();
//        builder.show();
    }

    public static void MessageBox(Context context, String title, View infoView,
                                  DialogInterface.OnClickListener positiveListener,
                                  DialogInterface.OnClickListener negetiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (infoView != null) {
            builder.setView(infoView);
        }
        if (positiveListener != null) {
            builder.setPositiveButton(
                    ResourceUtil.getStringId(context, "sure"), positiveListener);
        }
        if (negetiveListener != null) {
            builder.setNegativeButton(
                    ResourceUtil.getStringId(context, "cancel"),
                    negetiveListener);
        }
        builder.show();
    }

    public static ProgressDialog showProgressDialog(Context context,
                                                    String title, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        if (!TextUtils.isEmpty(title)) {
            progressDialog.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            progressDialog.setMessage(msg);
        }
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        return progressDialog;
    }
}
