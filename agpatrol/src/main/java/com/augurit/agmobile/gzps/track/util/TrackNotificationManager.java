package com.augurit.agmobile.gzps.track.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.augurit.agmobile.gzps.R;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.Date;

import static com.augurit.agmobile.gzps.track.service.TrackNewService.LOCATING;
import static com.augurit.agmobile.gzps.track.service.TrackNewService.PAUSE;
import static com.augurit.agmobile.gzps.track.util.TrackConstant.BROCAST_ACTION;
import static com.augurit.agmobile.gzps.track.util.TrackConstant.NOTIFICATION_ID;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.util
 * @createTime 创建时间 ：2017-06-18
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-18
 * @modifyMemo 修改备注：
 */

public class TrackNotificationManager {

    private static TrackNotificationManager instance;

    private Context mContext;

    NotificationManager notificationManager;
    Notification mTrackNotification;
    RemoteViews mRemoteViews;
    private Callback1 startBtnClickListener;
    private BroadcastReceiver mNotificationBroadcast ;

    private TrackNotificationManager(Context context){
        this.mContext = context;
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        initBrocast();
        initNotification();
    }

    public static TrackNotificationManager getInstance(Context context){
        if(instance == null){
            instance = new TrackNotificationManager(context);
        }
        return instance;
    }

    private void initBrocast(){
        mNotificationBroadcast = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if(startBtnClickListener != null){
                    startBtnClickListener.onCallback(null);
                }
            }

        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROCAST_ACTION);
        filter.setPriority(Integer.MAX_VALUE);
        mContext.registerReceiver(mNotificationBroadcast, filter);
    }

    private void initNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        int smallIconId = R.mipmap.ic_action_go;
        Bitmap largeIcon = ((BitmapDrawable) mContext.getResources().getDrawable(R.drawable.ic_open_photo)).getBitmap();
        mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.track_notification_view);
        mRemoteViews.setOnClickPendingIntent(R.id.track_start, getNotificationPendingIntent());
        mRemoteViews.setOnClickPendingIntent(R.id.track_pause, getNotificationPendingIntent());
        builder.setLargeIcon(largeIcon)
                .setSmallIcon(smallIconId)
                .setTicker("开始记录运动轨迹")
                .setOngoing(true)
                .setAutoCancel(true)
                .setContent(mRemoteViews);
        mTrackNotification = builder.build();
    }

    public void showNotification(int currentTrackState){
//        setNotificationStartTime();
        setTrackLength("0");
        setTrackTime("0");
        changeNotificationBtnImg(currentTrackState);
        notificationManager.notify(NOTIFICATION_ID, mTrackNotification);
    }

    public void dismissNotification(){
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public void changeNotificationBtnImg(int currentTrackState){
        Bitmap bitmap = null;
        if(currentTrackState == LOCATING){
            bitmap = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.ic_play_bar_btn_pause)).getBitmap();
        } else if(currentTrackState == PAUSE) {
            bitmap = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.ic_play_bar_btn_play)).getBitmap();
        }
        if(bitmap == null){
            return;
        }
        mRemoteViews.setImageViewBitmap(R.id.track_start, bitmap);
        mTrackNotification.contentView = mRemoteViews;
        notificationManager.notify(NOTIFICATION_ID, mTrackNotification);
    }

    public void setNotificationStartTime(){
        mRemoteViews.setTextViewText(R.id.track_item_starttxt, TimeUtil.getStringTimeDS(new Date()));
        notificationManager.notify(NOTIFICATION_ID, mTrackNotification);
    }

    public PendingIntent getNotificationPendingIntent(){
        Intent intent = new Intent(BROCAST_ACTION);
        return PendingIntent.getBroadcast(mContext, 0x111, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void setTrackLength(String trackLength) {
        mRemoteViews.setTextViewText(R.id.track_length, trackLength);
        mTrackNotification.contentView = mRemoteViews;
        notificationManager.notify(NOTIFICATION_ID, mTrackNotification);
    }

    public void setTrackTime(String trackTime) {
        mRemoteViews.setTextViewText(R.id.track_time, trackTime);
        mTrackNotification.contentView = mRemoteViews;
        notificationManager.notify(NOTIFICATION_ID, mTrackNotification);
    }

    public void setStartBtnClickListener(Callback1 clickListener){
        this.startBtnClickListener = clickListener;
    }
}
