package com.augurit.am.fw.log.save.imp;

import android.content.Context;


import com.augurit.am.fw.log.AMLogReport;
import com.augurit.am.fw.log.save.BaseSaver;
import com.augurit.am.fw.log.upload.ILogUpload;
import com.augurit.am.fw.log.util.NetUtil;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.concurrent.Future;

/**
 * 在崩溃之后，马上异步保存崩溃信息，完成后退出线程，并且将崩溃信息都写在一个文件中
 * Created by wenmingvs on 2016/7/7.
 */
public class AMCrashWriter extends BaseSaver {

    private static AMCrashWriter mCrashWriter;

    private final static String TAG = "AMCrashWriter";

    public static final String LOG_FILE_NAME_EXCEPTION_PRE = "CrashLog_";

    private   File crashFile = null;

    private   String saveContent;

    /**
     * 初始化，继承父类
     *
     * @param context 上下文
     */
    public AMCrashWriter(Context context) {
        super(context);
    }

    public static AMCrashWriter getInstance(Context context) {
        if (mCrashWriter == null) {
            synchronized (AMLogReport.class) {
                if (mCrashWriter == null) {
                    mCrashWriter = new AMCrashWriter(context.getApplicationContext());
                }
            }
        }
        return mCrashWriter;
    }


    /**
     * 异步的写操作，使用线程池对异步操作做统一的管理
     *
     * @param ex     崩溃的错误信息
     * @param message   崩溃信息
     */
    public synchronized Future writeCrash(final Throwable ex, final String message) {
        if (AMLogReport.isDebug()) {
            ex.printStackTrace();
            return null;
        }
        return mThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                synchronized (BaseSaver.class) {

                    Writer writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    ex.printStackTrace(printWriter);
                    Throwable cause = ex.getCause();
                    while (cause != null) {
                        cause.printStackTrace(printWriter);
                        cause = cause.getCause();
                    }
                    printWriter.close();
                    final String content = "\n" +
                            writer.toString();
                    saveContent = deviceInfoStr(mContext)
                            + formatLogMsg("\nMessage:" + message + "\n" + content)
                            + "\n";
                    crashFile = createNewCrashFile();

                    if (crashFile != null) {
                        writeText(crashFile, saveContent);
                    }

                    if (NetUtil.isConnected(mContext)) {
                        if (AMLogReport.getInstance().getUploaderList() == null
                                || AMLogReport.getInstance().getUploaderList().size() == 0) {

                        //    saveToLocal();
                            return;
                        }
                        for (ILogUpload uploader : AMLogReport.getInstance().getUploaderList()) {
                            uploader.sendFile(crashFile, saveContent, new ILogUpload.OnUploadFinishedListener(){
                                @Override
                                public void onSuceess() {
                                    if(crashFile.exists()){
                                        crashFile.delete();
                                    }
                                }

                                @Override
                                public void onError(String error) {
                                  //  saveToLocal();
                                }
                            });
                        }
                    }else {
                       // saveToLocal();
                    }
                }
            }
        });

    }

    /**
     * 保存到本地
     */
    private void saveToLocal(){
        if (crashFile != null) {
            writeText(crashFile, saveContent);
        }
    }

    /**
     * 创建异常日志记录文件
     * @return
     */
    private File createNewCrashFile() {
        TimeLogFolder = AMLogReport.getInstance().getROOT() + "/"
                + yyyy_mm_dd.format(new Date(System.currentTimeMillis())) + "/";
        File logsDir = new File(TimeLogFolder);
        File crashFile = new File(logsDir, LOG_FILE_NAME_EXCEPTION_PRE
                + yyyy_MM_dd_HH_mm_ss.format(new Date(System.currentTimeMillis()))
                + LOG_FILE_NAME_TYPE);
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }
        try {
            if (!crashFile.exists()) {
                boolean successCreate = crashFile.createNewFile();
                if (!successCreate) {
                    return null;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return crashFile;
    }

    @Deprecated
    public void writeLog(String tag, String content) {
        throw new RuntimeException();
    }

}
