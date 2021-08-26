package com.augurit.agmobile.gzps.common.util;

import android.content.Context;
import android.os.Build;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SystemUtils {
    private static final String MEM_INFO_PATH = "/proc/meminfo";
    public static final String MEMTOTAL = "MemTotal";
    public static final String MEMFREE = "MemFree";

    public static void checkSystemInfo(final Context context, final CheckCallBack checkCallBack) {
        Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {

                return getTotalMemorySize(context, MEMTOTAL);
            }
        }).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        boolean lowest_memory = false;
                        boolean lowest_version = false;
                        if (Double.valueOf(s) >= 3.0) {
                            lowest_memory = true;
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            lowest_version = true;
                        }
                        if (checkCallBack != null) {
                            checkCallBack.onSuccess(lowest_memory, lowest_version);
                        }

                    }
                });
    }

    /**
     * 获取手机内存总大小
     *
     * @param context
     * @param type
     * @return
     */
    public static String getTotalMemorySize(Context context, String type) {

        String path = "/proc/meminfo";
        String firstLine = null;
        int totalRam = 0;
        FileReader fileReader = null;
        BufferedReader br = null;
        try {
            fileReader = new FileReader(path);
            br = new BufferedReader(fileReader, 8192);
            while ((firstLine = br.readLine()) != null) {
                if (firstLine.contains(type)) {
                    break;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(br != null){
                    br.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (firstLine != null) {
            firstLine = firstLine.split("\\s+")[1];
            totalRam = (int) Math.ceil(Float.valueOf(firstLine) / (1024 * 1024));
        }
        return totalRam + "";//返回1GB/2GB/3GB/4GB
    }


    public interface CheckCallBack {
        void onSuccess(boolean lowest_memory, boolean lowest_version);
    }
}
