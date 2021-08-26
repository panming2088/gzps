package com.augurit.agmobile.gzps.im;

import android.content.Context;
import android.content.Intent;

import com.augurit.agmobile.gzps.LoginActivity;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;


public class ImNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        if(RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)){
            context.startActivity(new Intent(context, LoginActivity.class));
            return true;
        }else{
            return false;
        }

    }

}
