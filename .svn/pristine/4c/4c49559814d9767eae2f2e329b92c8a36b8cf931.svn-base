package com.augurit.am.fw.net.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * 网络状态变化监听
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.am.fw.net.util
 * @createTime 创建时间 ：2017-12-25
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-12-25
 * @modifyMemo 修改备注：
 */

public class NetBroadcastReceiver  extends BroadcastReceiver {

    public static NetEvent evevt = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话就说明网络状态发生了变化
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            int netWorkState = NetworkUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型
            if(evevt != null){
                evevt.onNetChange(netWorkState);
            }
        }
    }

    // 自定义接口
    public interface NetEvent {
        void onNetChange(int netMobile);
    }
}
