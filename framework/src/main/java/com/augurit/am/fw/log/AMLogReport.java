package com.augurit.am.fw.log;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;



import com.augurit.am.fw.log.crash.AMCrashHandler;
import com.augurit.am.fw.log.encryption.IEncryption;
import com.augurit.am.fw.log.save.imp.AMCrashWriter;
import com.augurit.am.fw.log.upload.ILogUpload;
import com.augurit.am.fw.log.upload.UploadService;
import com.augurit.am.fw.log.util.CheckCacheUtil;
import com.augurit.am.fw.log.util.FileUtil;
import com.augurit.am.fw.log.util.NetUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * 日志崩溃管理框架
 * Created by wenmingvs on 2016/7/7.
 */
public class AMLogReport {

    private static AMLogReport mAMLogReport;

    private boolean inited = false;
    /**
     * 设置上传的方式
     */
    public ArrayList<ILogUpload> mUploaderList;
    /**
     * 设置缓存文件夹的大小,默认是30MB
     */
    private long mCacheSize = 30 * 1024 * 1024;

    /**
     * 设置日志保存的路径
     */
    private String mROOT;

    private String mLogDir = "/AMLog/";

    /**
     * 设置加密方式
     */
    private IEncryption mEncryption;


    /**
     * 设置在哪种网络状态下上传，true为只在wifi模式下上传，false是wifi和移动网络都上传
     */
    private boolean mWifiOnly = true;


    /**
     * 设置开发模式
     */
    private static boolean debug = true;

    /**
     * 。系统默认异常处理
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;


    private AMLogReport() {
        mUploaderList = new ArrayList<>();
    }


    public static AMLogReport getInstance() {
        if (mAMLogReport == null) {
            synchronized (AMLogReport.class) {
                if (mAMLogReport == null) {
                    mAMLogReport = new AMLogReport();

                }
            }
        }
        return mAMLogReport;
    }

    public AMLogReport setCacheSize(long cacheSize) {
        this.mCacheSize = cacheSize;
        return this;
    }

    public AMLogReport setEncryption(IEncryption encryption) {
        this.mEncryption = encryption;
        return this;
    }

    public AMLogReport addUploader(ILogUpload logUpload) {
        mUploaderList.add(logUpload);
        return this;
    }

    public AMLogReport setWifiOnly(boolean wifiOnly) {
        mWifiOnly = wifiOnly;
        return this;
    }

    public static boolean isDebug() {
        return debug;
    }

    public AMLogReport setDebug(boolean d) {
        debug = d;
        return this;
    }


    public AMLogReport setLogDir(Context context, String logDir) {
        if (TextUtils.isEmpty(logDir)) {
            //如果SD不可用，则存储在沙盒中
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                mROOT = context.getExternalCacheDir().getAbsolutePath();
            } else {
                mROOT = context.getCacheDir().getAbsolutePath();
            }
        } else {
            mROOT = logDir;
        }
        mROOT = mROOT + mLogDir;
        return this;
    }


    public String getROOT() {
        return mROOT;
    }

    public void init(Context context) {
        if (TextUtils.isEmpty(mROOT)) {
            //如果SD不可用，则存储在沙盒中
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                mROOT = context.getExternalCacheDir().getAbsolutePath();
            } else {
                mROOT = context.getCacheDir().getAbsolutePath();
            }
            mROOT = mROOT + mLogDir;
        }
        AMCrashWriter amCrashWriter = AMCrashWriter.getInstance(context);
        if (mEncryption != null) {
            amCrashWriter.setEncodeType(mEncryption);
        }
        if(!inited){
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            AMCrashHandler.getInstance(context).init(amCrashWriter);
        }
        inited = true;
        checkCacheSize();
    }

    public ArrayList<ILogUpload> getUploaderList() {
        return mUploaderList;
    }

    public long getCacheSize() {
        return mCacheSize;
    }


    private synchronized void checkCacheSize() {
        new Thread() {
            @Override
            public void run() {
                File logDir = new File(getROOT());
                CheckCacheUtil.checkCacheSize(logDir);
            }
        }.start();
    }

    public synchronized boolean deleteLogDir() {
        return FileUtil.deleteDir(new File(getROOT()));

    }

    /**
     * 调用此方法，上传全部日志信息
     *
     * @param applicationContext 全局的application context，避免内存泄露
     */
    public void uploadAll(Context applicationContext) {
        //如果没有设置上传，则不执行
        if (mUploaderList == null) {
            return;
        }
        //如果网络可用，而且是移动网络，但是用户设置了只在wifi下上传，返回
        if (NetUtil.isConnected(applicationContext) && !NetUtil.isWifi(applicationContext) && mWifiOnly) {
            return;
        }
        Intent intent = new Intent(applicationContext, UploadService.class);
        applicationContext.startService(intent);
    }


    public Thread.UncaughtExceptionHandler getDefaultHandler() {
        return mDefaultHandler;
    }

    public void setDefaultHandler(Thread.UncaughtExceptionHandler defaultHandler) {
        this.mDefaultHandler = defaultHandler;
    }

}
