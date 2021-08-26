package com.augurit.am.fw.log.save;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;


import com.augurit.am.fw.log.encryption.IEncryption;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 提供通用的保存操作log的日志和设备信息的方法
 * Created by wenmingvs on 2016/7/9.
 */
public abstract class BaseSaver {

    private final static String TAG = "BaseSaver";

    /**
     * 使用线程池对异步的日志写入做管理，提高性能
     */
    public ExecutorService mThreadPool = Executors.newFixedThreadPool(2);

    /**
     * 根据日期创建文件夹,文件夹的名称以日期命名,下面是日期的格式
     */
    public final static SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    /**
     * 在每一条log前面增加一个时间戳
     */
    public final static SimpleDateFormat yyyy_MM_dd_HH_mm_ss_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS", Locale.getDefault());

    public final static SimpleDateFormat yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss", Locale.getDefault());

    /**
     * 日志的保存的类型
     */
    public static final String LOG_FILE_NAME_TYPE = ".txt";

    public static final String LOG_FILE_NAME_MONITORLOG_PRE = "MonitorLog_";

    /**
     * 日志命名的其中一部分：时间戳
     */
    public final static String LOG_CREATE_DATE = yyyy_mm_dd.format(new Date(System.currentTimeMillis()));

    public static String TimeLogFolder;

    /**
     * 操作日志全名拼接
     */
    public final static String LOG_FILE_NAME_MONITOR = LOG_FILE_NAME_MONITORLOG_PRE + LOG_CREATE_DATE + LOG_FILE_NAME_TYPE;

    public Context mContext;

    /**
     * 加密方式
     */
    public static IEncryption mEncryption;

    public BaseSaver(Context context) {
        this.mContext = context;
    }

    /**
     * 用于在每条log前面，增加更多的文本信息，包括时间，线程名字等等
     */
    public static String formatLogMsg(String content) {
        String timeStr = yyyy_MM_dd_HH_mm_ss_SS.format(Calendar.getInstance().getTime());
        StringBuilder sb = new StringBuilder();
        sb.append("\nTime: ")
                .append(timeStr)
                .append("\n")
                .append(content);
        return sb.toString();
    }

    public String deviceInfoStr(Context context){
        StringBuilder sb = new StringBuilder();
        sb.append("Application Information").append('\n');
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai = context.getApplicationInfo();
        sb.append("App Name : ").append(pm.getApplicationLabel(ai)).append('\n');
        try {
            PackageInfo pi = pm.getPackageInfo(ai.packageName, 0);
            sb.append("Version Code: ").append(pi.versionCode).append('\n');
            sb.append("Version Name: ").append(pi.versionName).append('\n');
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        sb.append('\n');
        sb.append("DEVICE INFORMATION").append('\n');
        sb.append("BOOTLOADER: ").append(Build.BOOTLOADER).append('\n');
        sb.append("BRAND: ").append(Build.BRAND).append('\n');
        sb.append("DEVICE: ").append(Build.DEVICE).append('\n');
        sb.append("HARDWARE: ").append(Build.HARDWARE).append('\n').append('\n');
        return sb.toString();
    }




    /**
     * 写入设备的各种参数信息之前，请确保File文件以及他的父路径是存在的
     *
     * @param file 需要创建的文件
     */
    public File createFile(File file, Context context) {
        String sb = deviceInfoStr(context);
        //加密信息
        sb = encodeString(sb);
        try {
            if (!file.exists()) {
                boolean successCreate = file.createNewFile();
                if (!successCreate) {
                    return null;
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sb.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void setEncodeType(IEncryption encodeType) {
        mEncryption = encodeType;
    }

    public String encodeString(String content) {
        if (mEncryption != null) {
            try {
                return mEncryption.encrypt(content);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
                return content;
            }
        }

        return content;

    }

    public String decodeString(String content) {
        if (mEncryption != null) {
            try {
                return mEncryption.decrypt(content);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
                return content;
            }
        }
        return content;
    }



    public void writeText(final File logFile, final String content) {
        FileOutputStream outputStream = null;
        try {
            String encoderesult = encodeString(content);
            outputStream = new FileOutputStream(logFile);
            outputStream.write(encoderesult.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
