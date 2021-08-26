package com.augurit.agmobile.patrolcore.layerdownload;

/**
 * Created by luobiao on 17/7/28.
 */

public interface OnDownloadListener {
    void onUpdateTask(int taskId, double total, double downed, boolean done, Throwable e);
}
