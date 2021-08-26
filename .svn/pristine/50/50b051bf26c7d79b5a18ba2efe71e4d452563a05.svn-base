package com.augurit.am.cmpt.media.speech.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.media.speech.intfc.VoiceRecognizeListener;
import com.baidu.speech.VoiceRecognitionService;

import java.util.ArrayList;

/**
 *
 * 包名：com.augurit.am.cmpt.media.speech
 * 文件描述：语音识别窗口
 * 创建人：xiejiexin
 * 创建时间：2016-09-06 11:35
 * 修改人：xiejiexin
 * 修改时间：2016-09-06 11:35
 * 修改备注：
 * @version
 *
 */
public class VoiceRecognizeDialog {

    private SpeechRecognizer mSpeechRecognizer;
    private Context mContext;
    private PopupWindow mPopup;
    private ImageButton mSpeechBtn;
    private TextView mInfoText;     // 上方提示文字(大)
    private TextView mHintText;     // 按钮下方提示文字(小)
    private ImageView mSoundWave;   // 声波用以显示动画
    private SoundPool mSoundPool;   // 声音池,用以播放提示音
    private VoiceRecognizeListener mListener; // 语音识别监听,用于返回结果

    public static final int STATUS_None = 0;            // 无状态
    public static final int STATUS_WaitingReady = 2;    // 启动中
    public static final int STATUS_Ready = 3;           // 准备开始识别
    public static final int STATUS_Listening = 4;       // 聆听中
    public static final int STATUS_Recognizing = 5;     // 识别中
    public static final int STATUS_Normal = 6;          // 正常状态
    private int mStatus = STATUS_None;

    private static final String TAG = "VoiceRecognize";

    public VoiceRecognizeDialog(Context context) {
        mContext = context;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        // 识别器
        mSpeechRecognizer  = SpeechRecognizer.createSpeechRecognizer(
                mContext, new ComponentName(mContext, VoiceRecognitionService.class));
        mSpeechRecognizer.setRecognitionListener(recognitionListener);   // 识别器监听

        // 初始化视图
        View content = View.inflate(mContext, R.layout.speech_dialog_voice_recognize, null);
        mSpeechBtn = (ImageButton) content.findViewById(R.id.btn_speech);
        mSpeechBtn.setOnClickListener(onClickListener);
        enableSpeechBtn(false);
        mInfoText = (TextView) content.findViewById(R.id.tv_info);
        mHintText = (TextView) content.findViewById(R.id.tv_hint);
        mHintText.setVisibility(View.INVISIBLE);
        mSoundWave = (ImageView) content.findViewById(R.id.sound_wave_circle);
        // 初始化窗口
        mPopup = new PopupWindow(content,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true);
        Drawable dialogBG = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.speech_lylr_speech_dialog, null);
        mPopup.setBackgroundDrawable(dialogBG);
        mPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {  // 消失监听
            @Override
            public void onDismiss() {
                cancel();   // 取消识别
            }
        });

        // 初始化声音池
        mSoundPool = new SoundPool(5, AudioManager.STREAM_NOTIFICATION, 0);
        mSoundPool.load(mContext, R.raw.speech_recognition_cancel, 1);    // 1,取消
        mSoundPool.load(mContext, R.raw.speech_recognition_error, 1);     // 2,错误
        mSoundPool.load(mContext, R.raw.speech_recognition_start, 1);     // 3,开始
        mSoundPool.load(mContext, R.raw.speech_recognition_success, 1);   // 4,成功
        mSoundPool.load(mContext, R.raw.speech_speech_end, 1);            // 5,结束

        // 启动一次语音识别
        mStatus = STATUS_WaitingReady;
        start();    // 语音识别第一次启动需要一定时间，初始化时提前启动可节省用户等待时间
    }

    /**
     * 语音识别器监听
     */
    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            // 准备就绪，可以开始说话
            mStatus = STATUS_Ready;
            mInfoText.setText("请说话");
            mHintText.setText("正在聆听");
            mHintText.setVisibility(View.VISIBLE);
            enableSpeechBtn(true);
            if (!mPopup.isShowing()) {
                // 启动完毕而用户并未进入此功能，则取消此次识别（针对初始化时的启动）
                cancel();
                return;
            }
            mSoundPool.play(3, 1, 1, 1, 0, 1);  // 播放提示音
        }

        @Override
        public void onBeginningOfSpeech() {
            // 开始说话
            mStatus = STATUS_Listening;
            mHintText.setText("点击停止");
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            // 音量改变
            Log.v(TAG, "音量：" + rmsdB);
            AnimateSoundWave(rmsdB);
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            // 当前帧语音数据
        }

        @Override
        public void onEndOfSpeech() {
            // 说完了
            mSoundPool.play(5, 1, 1, 1, 0, 1);  // 播放提示音
            mSoundWave.clearAnimation();
            mStatus = STATUS_Recognizing;
            mHintText.setText("识别中");
            enableSpeechBtn(false);
        }

        @Override
        public void onError(int error) {
            // 错误处理
            mSoundPool.play(2, 1, 1, 1, 0, 1);  // 播放提示音
            StringBuilder sb = new StringBuilder();
            String originText = mInfoText.getText().toString();
            mInfoText.setText("出了点问题，请稍后再试");
            enableSpeechBtn(true);
            mHintText.setVisibility(View.VISIBLE);
            mHintText.setText("点击说话");
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    sb.append("音频问题");
                    mInfoText.setText("对不起，我没有听清，请再说一遍");
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    mInfoText.setText("你好像没有说话");
                    sb.append("没有语音输入");
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    sb.append("其它客户端错误");
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    sb.append("权限不足");
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    sb.append("网络问题");
                    mInfoText.setText("无法连接到网络，请检查设置");
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    sb.append("没有匹配的识别结果");
                    mInfoText.setText("没有匹配的识别结果");
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    sb.append("引擎忙");
                    if (mStatus == STATUS_WaitingReady || mStatus == STATUS_Ready) {
                        mInfoText.setText("请说话");
                        mHintText.setText("正在聆听");
                    }
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    sb.append("服务端错误");
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    sb.append("连接超时");
                    mInfoText.setText("连接超时，请稍后再试");
                    break;
            }
            sb.append(":").append(error);
            Log.e(TAG, sb.toString());
            mStatus = STATUS_Normal;
        }

        @Override
        public void onResults(Bundle results) {
            // 识别结果
            ArrayList<String> resultList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (resultList != null && resultList.size() > 0) {
                mSoundPool.play(4, 1, 1, 1, 0, 1);  // 播放提示音
                mInfoText.setText("结果:" + resultList.get(0));
                if (mListener != null) {    // 返回结果
                    mListener.onResult(resultList.get(0));
                }
            } else {
                mSoundPool.play(2, 1, 1, 1, 0, 1);  // 播放提示音
                mInfoText.setText("没有结果");
            }
            mStatus = STATUS_Normal;
            enableSpeechBtn(true);
            mHintText.setText("点击说话");
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            // 临时的识别结果
            ArrayList<String> resultList = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (resultList != null && resultList.size() > 0) {
                mInfoText.setText(resultList.get(0));
            }
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    /**
     * 按钮点击监听
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (mStatus) {
                case STATUS_None:
                    break;
                case STATUS_WaitingReady:
                    break;
                case STATUS_Ready:
                    break;
                case STATUS_Listening:
                    stop();
                case STATUS_Recognizing:
                    break;
                case STATUS_Normal:
                    start();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 声音动画
     * @param rmsdB 声音数值
     */
    private void AnimateSoundWave(float rmsdB) {
        float toX;
        toX = (rmsdB / 3000) > 2 ? 2 : (rmsdB / 3000);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, toX, 1, toX,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setRepeatCount(1);
        mSoundWave.setAnimation(scaleAnimation);
    }

    /**
     * 开始聆听
     */
    private void start() {
        mSpeechRecognizer.startListening(new Intent());
        mStatus = STATUS_WaitingReady;
    }

    /**
     * 停止聆听
     */
    private void stop() {
        mSpeechRecognizer.stopListening();
        mStatus = STATUS_Recognizing;
    }

    /**
     * 取消
     */
    private void cancel() {
        mSpeechRecognizer.cancel();
        if (mStatus != STATUS_None && mStatus != STATUS_WaitingReady) {
            // 不处于启动状态则切换至正常状态
            mInfoText.setText("请说话");
            mStatus = STATUS_Normal;
        }
    }

    /**
     * 设置是否开启说话按钮
     * @param enabled 是否可用
     */
    private void enableSpeechBtn(boolean enabled) {
        if (enabled) {
            mSpeechBtn.setBackgroundResource(R.drawable.speech_sel_bg_btn_speech);
            mSpeechBtn.setEnabled(true);
        } else {
            mSpeechBtn.setBackgroundResource(R.drawable.speech_lylr_speech_disabled);
            mSpeechBtn.setEnabled(false);
        }
    }

    /**
     * 显示对话框
     * @param location 显示位置
     */
    public void show(View location) {
        if (mPopup != null) {
            if (!mPopup.isShowing()) {
                mPopup.showAtLocation(location, Gravity.CENTER, 0, 0);
            }
            start();
        }
    }

    /**
     * 显示对话框
     * @param anchor 锚点View
     * @param xoff x方向偏移量
     * @param yoff y方向偏移量
     */
    public void show(View anchor, int xoff, int yoff) {
        if (mPopup != null && !mPopup.isShowing()) {
            mPopup.showAsDropDown(anchor, xoff, yoff);
            mSpeechRecognizer.startListening(new Intent());
        }
    }

    /**
     * 隐藏对话框
     */
    public void dismiss() {
        if (mPopup != null) {
            mPopup.dismiss();
            cancel();
        }
    }

    /**
     * 销毁
     */
    public void destroy() {
        mPopup.dismiss();
        mSpeechRecognizer.destroy();
    }

    /**
     * 设置语音识别状态监听
     * @param listener 监听
     */
    public void setVoiceRecognizeListener(VoiceRecognizeListener listener) {
        mListener = listener;
    }
}
