package io.rong.callkit;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import io.rong.calllib.IRongCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.calllib.message.CallSTerminateMessage;
import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.utils.NotificationUtil;
import io.rong.imlib.model.Conversation;
import io.rong.message.InformationNotificationMessage;

/**
 * Created by weiqinxiao on 16/3/17.
 */
public class CallFloatBoxView {
    private static Context mContext;
    private static Timer timer;
    private static long mTime;
    private static View mView;
    private static Boolean isShown = false;
    private static WindowManager wm;
    private static Bundle mBundle;
    private static final String TAG = "CallFloatBoxView";

    public static void showFloatBox(Context context, Bundle bundle) {
        if (isShown) {
            return;
        }

        mContext = context;
        isShown = true;
        RongCallSession session = RongCallClient.getInstance().getCallSession();
        long activeTime = session != null ? session.getActiveTime() : 0;
        mTime = activeTime == 0 ? 0 : (System.currentTimeMillis() - activeTime) / 1000;

        mBundle = bundle;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        int type;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < 24) {
            type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params.type = type;
        params.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        params.format = PixelFormat.TRANSLUCENT;
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        params.x = context.getResources().getDisplayMetrics().widthPixels;
        params.y = 0;

        mView = LayoutInflater.from(context).inflate(R.layout.rc_voip_float_box, null);
        mView.setOnTouchListener(new View.OnTouchListener() {
            float lastX, lastY;
            int oldOffsetX, oldOffsetY;
            int tag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                float x = event.getX();
                float y = event.getY();
                if (tag == 0) {
                    oldOffsetX = params.x;
                    oldOffsetY = params.y;
                }
                if (action == MotionEvent.ACTION_DOWN) {
                    lastX = x;
                    lastY = y;
                } else if (action == MotionEvent.ACTION_MOVE) {
                    // ???????????????,??????????????????
                    params.x += (int) (x - lastX) / 3;
                    params.y += (int) (y - lastY) / 3;
                    tag = 1;
                    if (mView != null)
                        wm.updateViewLayout(mView, params);
                } else if (action == MotionEvent.ACTION_UP) {
                    int newOffsetX = params.x;
                    int newOffsetY = params.y;
                    if (Math.abs(oldOffsetX - newOffsetX) <= 20 && Math.abs(oldOffsetY - newOffsetY) <= 20) {
                        onClickToResume();
                    } else {
                        tag = 0;
                    }
                }
                return true;
            }
        });
        wm.addView(mView, params);
        TextView timeV = (TextView) mView.findViewById(R.id.rc_time);
        setupTime(timeV);
        ImageView mediaIconV = (ImageView) mView.findViewById(R.id.rc_voip_media_type);
        RongCallCommon.CallMediaType mediaType = RongCallCommon.CallMediaType.valueOf(bundle.getInt("mediaType"));
        if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
            mediaIconV.setImageResource(R.drawable.rc_voip_float_audio);
        } else {
            mediaIconV.setImageResource(R.drawable.rc_voip_float_video);
        }
        RongCallClient.getInstance().setVoIPCallListener(new IRongCallListener() {
            @Override
            public void onCallOutgoing(RongCallSession callInfo, SurfaceView localVideo) {

            }

            @Override
            public void onRemoteUserRinging(String userId) {

            }

            @Override
            public void onCallDisconnected(RongCallSession callProfile, RongCallCommon.CallDisconnectedReason reason) {
                String senderId;
                String extra = "";
                senderId = callProfile.getInviterUserId();
                switch (reason) {
                    case HANGUP:
                    case REMOTE_HANGUP:
                        if (mTime >= 3600) {
                            extra = String.format("%d:%02d:%02d", mTime / 3600, (mTime % 3600) / 60, (mTime % 60));
                        } else {
                            extra = String.format("%02d:%02d", (mTime % 3600) / 60, (mTime % 60));
                        }
                        break;
                }

                if (!TextUtils.isEmpty(senderId)) {
                    switch (callProfile.getConversationType()) {
                        case PRIVATE:
                            CallSTerminateMessage callSTerminateMessage = new CallSTerminateMessage();
                            callSTerminateMessage.setReason(reason);
                            callSTerminateMessage.setMediaType(callProfile.getMediaType());
                            callSTerminateMessage.setExtra(extra);
                            if (senderId.equals(callProfile.getSelfUserId())) {
                                callSTerminateMessage.setDirection("MO");
                                RongIM.getInstance().insertOutgoingMessage(Conversation.ConversationType.PRIVATE, callProfile.getTargetId(),
                                        io.rong.imlib.model.Message.SentStatus.SENT, callSTerminateMessage, null);
                            } else {
                                callSTerminateMessage.setDirection("MT");
                                io.rong.imlib.model.Message.ReceivedStatus receivedStatus = new io.rong.imlib.model.Message.ReceivedStatus(0);
                                RongIM.getInstance().insertIncomingMessage(Conversation.ConversationType.PRIVATE, callProfile.getTargetId(),
                                        senderId, receivedStatus, callSTerminateMessage, null);
                            }
                            break;
                        case GROUP:
                            InformationNotificationMessage informationNotificationMessage;
                            if (reason.equals(RongCallCommon.CallDisconnectedReason.NO_RESPONSE)) {
                                informationNotificationMessage = InformationNotificationMessage.obtain(RongContext.getInstance().getString(R.string.rc_voip_audio_no_response));
                            } else {
                                informationNotificationMessage = InformationNotificationMessage.obtain(RongContext.getInstance().getString(R.string.rc_voip_audio_ended));
                            }

                            if (senderId.equals(callProfile.getSelfUserId())) {
                                RongIM.getInstance().insertOutgoingMessage(Conversation.ConversationType.GROUP, callProfile.getTargetId(),
                                        io.rong.imlib.model.Message.SentStatus.SENT, informationNotificationMessage, null);
                            } else {
                                io.rong.imlib.model.Message.ReceivedStatus receivedStatus = new io.rong.imlib.model.Message.ReceivedStatus(0);
                                RongIM.getInstance().insertIncomingMessage(Conversation.ConversationType.GROUP, callProfile.getTargetId(),
                                        senderId, receivedStatus, informationNotificationMessage, null);
                            }
                            break;
                        default:
                            break;
                    }
                }
                Toast.makeText(mContext, mContext.getString(R.string.rc_voip_call_terminalted), Toast.LENGTH_SHORT).show();

                if (wm != null && mView != null) {
                    wm.removeView(mView);
                    timer.cancel();
                    timer = null;
                    isShown = false;
                    mView = null;
                    mTime = 0;
                }
                NotificationUtil.clearNotification(mContext, BaseCallActivity.CALL_NOTIFICATION_ID);
                RongCallClient.getInstance().setVoIPCallListener(RongCallProxy.getInstance());
            }

            @Override
            public void onRemoteUserInvited(String userId, RongCallCommon.CallMediaType mediaType) {

            }

            @Override
            public void onRemoteUserJoined(String userId, RongCallCommon.CallMediaType mediaType, SurfaceView remoteVideo) {

            }

            @Override
            public void onRemoteUserLeft(String userId, RongCallCommon.CallDisconnectedReason reason) {

            }

            @Override
            public void onMediaTypeChanged(String userId, RongCallCommon.CallMediaType mediaType, SurfaceView video) {

            }

            @Override
            public void onError(RongCallCommon.CallErrorCode errorCode) {

            }

            @Override
            public void onCallConnected(RongCallSession callInfo, SurfaceView localVideo) {

            }

            @Override
            public void onRemoteCameraDisabled(String userId, boolean muted) {

            }
        });
    }

    public static void hideFloatBox() {
        RongCallClient.getInstance().setVoIPCallListener(RongCallProxy.getInstance());
        if (isShown && null != mView) {
            wm.removeView(mView);
            timer.cancel();
            timer = null;
            isShown = false;
            mView = null;
            mTime = 0;
            mBundle = null;
        }
    }

    public static Intent getResumeIntent() {
        if (mBundle == null) {
            return null;
        }
        RongCallClient.getInstance().setVoIPCallListener(RongCallProxy.getInstance());
        Intent intent = new Intent(mBundle.getString("action"));
        intent.putExtra("floatbox", mBundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("callAction", RongCallAction.ACTION_RESUME_CALL.getName());

        return intent;
    }

    public static void onClickToResume() {
        //?????????????????????????????????????????????????????????mBundle??????????????????????????????????????????NPE
        if (mBundle == null) {
            RLog.d(TAG, "onClickToResume mBundle is null");
            return;
        }
        RongCallClient.getInstance().setVoIPCallListener(RongCallProxy.getInstance());
        Intent intent = new Intent(mBundle.getString("action"));
        intent.putExtra("floatbox", mBundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("callAction", RongCallAction.ACTION_RESUME_CALL.getName());
        mContext.startActivity(intent);
        mBundle = null;
    }

    private static void setupTime(final TextView timeView) {
        final Handler handler = new Handler(Looper.getMainLooper());
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mTime++;
                        if (mTime >= 3600) {
                            timeView.setText(String.format("%d:%02d:%02d", mTime / 3600, (mTime % 3600) / 60, (mTime % 60)));
                        } else {
                            timeView.setText(String.format("%02d:%02d", (mTime % 3600) / 60, (mTime % 60)));
                        }
                    }
                });
            }
        };

        timer = new Timer();
        timer.schedule(task, 0, 1000);
    }
}
