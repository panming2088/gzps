package com.augurit.am.fw.log.crash;

import android.content.Context;
import android.util.Log;


import com.augurit.am.fw.log.AMLogReport;
import com.augurit.am.fw.log.save.imp.AMCrashWriter;
import com.augurit.am.fw.utils.AppManager;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 自定义的崩溃捕获Handler
 */
public class AMCrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "AMCrashHandler";
    private static AMCrashHandler instance = null;
    private Context context;

    private static final int TIMEOUT = 5;

    /**
     * 设置日志的保存方式
     */
    private AMCrashWriter amCrashWriter;

    /**
     * 保证只有一个CrashHandler实例
     */
    private AMCrashHandler(Context context) {
        this.context = context;
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static AMCrashHandler getInstance(Context context) {
        if(instance == null){
            instance = new AMCrashHandler(context);
        }
        return instance;
    }

    /**
     * 初始化,，设置此CrashHandler来响应崩溃事件
     *
     * @param crashWriter 保存的方式
     */
    public void init(AMCrashWriter crashWriter) {
        amCrashWriter = crashWriter;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        String crashCode = "";
        StackTraceElement stackTraceElement = ex.getStackTrace()[0];
        String fileName = stackTraceElement.getFileName();
//        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();
        String exceptionType = ex.getClass().getName();
        Log.i("AMCrashHandler", "fileName="+fileName + " methodName=" +methodName+ " exceptionType=" + exceptionType);
        try {
            context.getAssets().open("crashcode.txt");

        } catch (IOException e) {

        }
        Future future = amCrashWriter.writeCrash(ex, crashCode);
        try {
//            Thread.sleep(3000);
            //等待异步的操作完成，默认最多等待5秒
            if(future!=null){
                future.get(TIMEOUT, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AMLogReport.getInstance().getDefaultHandler().uncaughtException(thread, ex);
//        AppManager.getAppManager().AppExit(context);
    }
}


