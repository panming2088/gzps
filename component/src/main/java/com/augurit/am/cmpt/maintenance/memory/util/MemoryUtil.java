package com.augurit.am.cmpt.maintenance.memory.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.text.format.Formatter;

import java.util.List;

/**
 * Created by liangsh on 2016-09-27.
 */
public final class MemoryUtil {
    private MemoryUtil() {
    }

    /**
     * 获取整体内存信息，如总内存，空闲内存等
     *
     * @param context
     * @return 内存信息
     */
    public static ActivityManager.MemoryInfo getMemoryInfo(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }


    /**
     * 获取应用的内存占用信息
     *
     * @param context
     * @return
     */
    public static ProgressMemoryInfo getRunningAppProcessInfo(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessList = activityManager.getRunningAppProcesses();

        int _pid = -1;
        int _uid = -1;
        String _processName = "";
        long memSize = 0;
        for (int i = 0; i < appProcessList.size(); i++) {
            //获取应用的内存占用信息
            if (appProcessList.get(i).processName.equals(context.getPackageName())) {
                //进程ID
                _pid = appProcessList.get(i).pid;
                // 用户ID 类似于Linux的权限不同，ID也就不同 比如 root等
                _uid = appProcessList.get(i).uid;
                // 进程名，默认是包名或者由属性android：process=""指定
                _processName = appProcessList.get(i).processName;
                // 获得该进程占用的内存
                int[] myMempid = new int[]{_pid};
                // 此MemoryInfo位于android.os.Debug.MemoryInfo包中，用来统计进程的内存信息
                Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(myMempid);
                // 获取进程内存占用信息 kb单位
                int _memSize = memoryInfo[0].dalvikPrivateDirty;
                memSize = _memSize * 1024;
                break;
            }
        }
        ProgressMemoryInfo progressMemoryInfo = new ProgressMemoryInfo();
        progressMemoryInfo.setPid(_pid);
        progressMemoryInfo.setUid(_uid);
        progressMemoryInfo.setProgressName(_processName);
        progressMemoryInfo.setMemSize(memSize);
        return progressMemoryInfo;
    }

    /**
     * 格式化内存占用大小，以KB, MB, GB等为单位
     *
     * @param context
     * @param size
     * @return
     */
    public static String formateFileSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }


    public static class ProgressMemoryInfo {
        int pid;   //进程ID
        int uid;   //用 用户ID 类似于Linux的权限不同，ID也就不同 比如 root等
        String progressName;    //进程名，默认是包名或者由属性android：process=""指定
        long memSize;           //应用占用的内存，单位为Byte

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getProgressName() {
            return progressName;
        }

        public void setProgressName(String progressName) {
            this.progressName = progressName;
        }

        public long getMemSize() {
            return memSize;
        }

        public void setMemSize(long memSize) {
            this.memSize = memSize;
        }

        public String toString() {
            return "pid=" + pid + " uid=" + uid + " progressName=" + progressName + " memSize=" + memSize;
        }
    }
}
