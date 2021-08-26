package com.augurit.am.cmpt.maintenance.cache.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Build;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.preference.PreferenceManager;

import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.maintenance.cache.model.AutoClearOption;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.log.LogUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述：应用的存储空间管理工具类
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.stats.cache.util
 * @createTime 创建时间 ：2016-09-13
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间：2017-02-08
 * @modifyMemo 修改备注：使用Callback接口返回数据
 */
public final class StorageManageUtil {
    public static int AUTO_CLEAR_SIZE_DEFAULT = 300;   // 默认清理大小阈值
    public static int AUTO_CLEAR_TIME_DEFAULT = 5;     // 默认清理时间阈值
    public static boolean AUTO_CLEAR_SIZE_ON_DEFAULT = false;     // 默认开启按大小清理
    public static boolean AUTO_CLEAR_TIME_ON_DEFAULT = false;     // 默认开启按时间清理
    private StorageManageUtil() {
    }

    /**
     * 获取应用占用空间大小，单位:Byte
     *
     * @param appContext context
     * @param callback   加载回调,返回大小为2的long数组，其中:<br>
     *                   0:总占用大小<br>
     *                   1:可清理大小
     */
    public static void getAppTotalBytes(final Context appContext, final Callback2<long[]> callback) {
        // 获取应用的PackageStats
        getPackageStats(appContext, new Callback2<PackageStats>() {
            @Override
            public void onSuccess(PackageStats stats) {
                // 计算应用的缓存、数据、代码占用空间大小
                long[] results = new long[2];  // 0:总占用大小; 1:可清理大小
                results[0] = stats.cacheSize + stats.dataSize // + stats.codeSize
                        + stats.externalCacheSize + stats.externalDataSize;
                results[1] = stats.cacheSize + stats.externalCacheSize;
                // 计算应用的额外存储位置占用空间大小
                List<File> exDir = getAppExtraDir();
                for (File dir : exDir) {
                    long fileBytes = getFileBytes(dir);
                    results[0] += fileBytes;
                    results[1] += fileBytes;
                }
                callback.onSuccess(results);
            }

            @Override
            public void onFail(Exception error) {
                callback.onFail(error);
            }
        });
    }

    /**
     * 获取当前应用的PackageStats,其中包含了应用数据的空间占用大小
     *
     * @param appContext ApplicationContext
     * @param callback   数据加载回调
     */
    public static void getPackageStats(Context appContext, final Callback2<PackageStats> callback) {
        try {
            String pkgName = appContext.getPackageName();
            //        String pkgName = "com.augurit.am.agmobile";
            Method method = PackageManager.class.getMethod("getPackageSizeInfo",
                    String.class, IPackageStatsObserver.class);
            method.invoke(appContext.getPackageManager(),
                    pkgName,
                    new IPackageStatsObserver.Stub() {
                        @Override
                        public void onGetStatsCompleted(PackageStats stats, boolean succeeded) throws RemoteException {
                            if (callback != null) {
                                callback.onSuccess(stats);
                            }
                        }
                    });
        } catch (Exception e) {
            callback.onFail(e);
        }
    }

    /**
     * 获取内部存储总空间大小，单位:Byte
     *
     * @return 内部存储总空间大小
     */
    public static long getTotalBytes() {
        long total;
        File root = Environment.getExternalStorageDirectory();  // TODO: SD卡
        // StatFs statFs = new StatFs(root.getPath());
        StatFs statFs = new StatFs(root.getAbsolutePath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            total = statFs.getTotalBytes();
        } else {
            int size = statFs.getBlockSize();//得到每一个分区的大小
            int count = statFs.getBlockCount();//得到所有分区的数量
            total = (long) statFs.getBlockSize() * statFs.getBlockCount();
        }
        return total;
    }

    /**
     * 获取内部存储可用空间大小，单位:Byte
     *
     * @return 内部存储可用空间大小
     */
    public static long getAvailableBytes() {
        long available;
        File root = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(root.getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            available = statFs.getAvailableBytes();
        } else {
            int blocks = statFs.getAvailableBlocks();
            int count = statFs.getBlockSize();
            //  available = statFs.getAvailableBlocks() * statFs.getBlockCount();
            available = (long) statFs.getAvailableBlocks() * statFs.getBlockSize();
        }
        return available;
    }

    /**
     * 删除缓存Cache，/data/data/packagename/cache
     *
     * @param appContext ApplicationContext
     * @return 操作结果
     */
    public static boolean deleteCache(Context appContext) {
        File cache = appContext.getCacheDir();
        return deleteDir(cache);
    }

    /**
     * 删除外部缓存Cache，也就是/sdcard/Android/data/packagename/cache
     *
     * @param appContext ApplicationContext
     * @return 操作结果
     */
    public static boolean deleteExternalCache(Context appContext) {
        File externalCacheDir = appContext.getExternalCacheDir();
        return deleteDir(externalCacheDir);
    }

    /**
     * 删除数据Data
     * 同时会删除缓存Cache
     *
     * @param appContext ApplicationContext
     * @return 操作结果
     */
    public static boolean deleteData(Context appContext) {
        File data = appContext.getFilesDir().getParentFile();
        return deleteDir(data);
    }

    /**
     * 删除应用额外文件夹
     *
     * @param appContext ApplicationContext
     */
    public static void deleteExtraDir(Context appContext) {
        List<File> extraDirs = getAppExtraDir();
        for (File extraDir : extraDirs) {
            deleteDir(extraDir);
        }
    }

    /**
     * 删除早于某时间的所有文件
     *
     * @param file 文件夹/文件
     * @param time 时间阈值
     * @return 操作结果
     */
    private static boolean deleteFileOutOfTime(File file, long time) {
        long fileTime = file.lastModified();
        if (fileTime < time) {  // 比指定时间更早
            return deleteDir(file);
        }
        return false;
    }

    /**
     * 删除文件夹及其下文件
     *
     * @param file 文件夹
     * @return 操作结果
     */
    private static boolean deleteDir(File file) {
        if (file != null) {
            if (file.isFile()) {
                return file.delete();
            }
            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    return file.delete();
                }
                for (File childFile : childFiles) {
                    deleteDir(childFile);
                }
                return file.delete();
            }
        }
        return false;
    }

    /**
     * 获取文件夹/文件大小，单位:Byte
     *
     * @param file 文件
     * @return 大小
     */
    private static long getFileBytes(File file) {
        long size = 0;
        if (file != null) {
            if (file.isFile()) {
                return file.length();
            } else if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    return 0;
                }
                for (File childFile : childFiles) {
                    size += getFileBytes(childFile);
                }
            }
        }
        return size;
    }

    /**
     * 保存自动清理选项
     *
     * @param context context
     * @param option  自动清理选项
     */
    public static void saveAutoClearOption(Context context, AutoClearOption option) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("auto_clear_option", JsonUtil.getJson(option));
        editor.apply();
    }

    /**
     * 读取自动清理选项
     *
     * @param context context
     * @return 自动清理选项
     */
    public static AutoClearOption readAutoClearOption(Context context) {
        AutoClearOption option;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String str = sp.getString("auto_clear_option", null);
        if (str == null) {  // 未读取到则使用默认的选项
            option = new AutoClearOption();
            option.setSize(AUTO_CLEAR_SIZE_DEFAULT);
            option.setSizeOn(AUTO_CLEAR_SIZE_ON_DEFAULT);
            option.setTime(AUTO_CLEAR_TIME_DEFAULT);
            option.setTimeOn(AUTO_CLEAR_TIME_ON_DEFAULT);
        } else {
            option = JsonUtil.getObject(str, AutoClearOption.class);
        }
        return option;
    }

    /**
     * 执行自动清理
     * 读取自动清理选项，若选项开启则按选项进行清理
     *
     * @param appContext context
     */
    public static void autoClear(final Context appContext) {
        // 取保存的清理选项
        final AutoClearOption option = readAutoClearOption(appContext);
        if (option.isTimeOn()) {   // 按时间清理选项开启
            Date date = new Date();
            long optionTime = (long) option.getTime() * 7 * 24 * 60 * 60 * 1000;
            long thTime = date.getTime() - optionTime;    // 计算清除时间阈值
            // 清除缓存
            deleteFileOutOfTime(appContext.getCacheDir(), thTime);
            deleteFileOutOfTime(appContext.getExternalCacheDir(), thTime);
            // 清除额外文件夹
            List<File> exDirs = getAppExtraDir();
            for (File dir : exDirs) {
                deleteFileOutOfTime(dir, thTime);
            }
            LogUtil.d("自动清理" ,"已执行按时间清理");
        }
        if (option.isSizeOn()) {   // 按大小清理选项开启
            // 获取当前应用存储占用
            getAppTotalBytes(appContext, new Callback2<long[]>() {
                @Override
                public void onSuccess(long[] results) {
                    if (results[0] >= (long) option.getSize() * 1024 * 1024) { // 超出阈值大小则清理
                        deleteCache(appContext);    // 清除缓存
                        // 清除额外文件夹
                        List<File> exDirs = getAppExtraDir();
                        for (File dir : exDirs) {
                            deleteDir(dir);
                        }
                        LogUtil.d("自动清理" ,"已执行按大小清理");
                    }
                }

                @Override
                public void onFail(Exception error) {
                    error.printStackTrace();
                }
            });
        }
    }

    /**
     * 获取应用所有额外的存储位置
     *
     * @return 存储位置List
     */
    private static List<File> getAppExtraDir() {
        List<File> fileList = new ArrayList<>();
        for (String dir : StorageDirConstant.DIR_PATHS) {
            fileList.add(new File(Environment.getExternalStorageDirectory().getPath().concat(dir)));
        }
        return fileList;
    }

}
