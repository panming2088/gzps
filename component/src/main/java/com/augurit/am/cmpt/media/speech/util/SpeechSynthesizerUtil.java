package com.augurit.am.cmpt.media.speech.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

/**
 * @author 创建人: xiejiexin
 * @date 创建时间: 2016-08-06
 * @description 功能描述: 语音合成工具
 */
public class SpeechSynthesizerUtil {

    private static SpeechSynthesizerUtil instance;
    private SpeechSynthesizer mSpeechSynthesizer;
    private Context mContext;

    private static final String TAG = "VOICE";

    private SpeechSynthesizerUtil(Context context) {
        mContext = context;
        init();
    }

    public static SpeechSynthesizerUtil getInstance(Context context) {
        if (instance == null) {
            instance = new SpeechSynthesizerUtil(context);
        }
        return instance;
    }

    /**
     * 初始化语音合成
     */
    private void init() {
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(mContext);
        mSpeechSynthesizer.setApiKey("3TggI5lDAI0sfqUdIc0jv2Gn", "66b2442bf0c921039e8ec1b30dcc1876");
        mSpeechSynthesizer.setAppId("8478710");
        AuthInfo authInfo = mSpeechSynthesizer.auth(TtsMode.MIX);
        if (authInfo.isMixSuccess()) {
            mSpeechSynthesizer.initTts(TtsMode.MIX);
//            mSpeechSynthesizer.speak("百度语音合成示例程序正在运行");
        } else {
            Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
        }
        mSpeechSynthesizer.setSpeechSynthesizerListener(speechSynthesizerListener);
    }

    /**
     * 播放语音
     * @param words 要播放的文字
     */
    public void speak(String words) {
//        mSpeechSynthesizer.stop();
        SpeakThread speakThread = new SpeakThread(words);   // 目前不打断未说完的话
        speakThread.start();
    }

    /**
     * 播放线程
     */
    private class SpeakThread extends Thread {

        private String words;

        SpeakThread(String words) {
            super();
            this.words = words;
        }

        @Override
        public void run() {
            super.run();
            mSpeechSynthesizer.speak(words);
        }
    }

    /**
     * 合成状态监听
     */
    private SpeechSynthesizerListener speechSynthesizerListener = new SpeechSynthesizerListener() {
        @Override
        public void onSynthesizeStart(String s) {
            Log.i(TAG, "-----------onSynthesizeStart,s:" + s);
        }

        @Override
        public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
            Log.i(TAG, "-----------onSynthesizeDataArrived,s:" + s + ",i:" + i);
        }

        @Override
        public void onSynthesizeFinish(String s) {
            Log.i(TAG, "-----------onSynthesizeFinish");
        }

        @Override
        public void onSpeechStart(String s) {
            Log.i(TAG, "-----------onSpeechStart,s:" + s);
        }

        @Override
        public void onSpeechProgressChanged(String s, int i) {
            Log.i(TAG, "-----------onSpeechProgressChanged,s:" + s + ",i:" + i);
        }

        @Override
        public void onSpeechFinish(String s) {
            Log.i(TAG, "-----------onSpeechFinish,s:" + s);
        }

        @Override
        public void onError(String s, SpeechError speechError) {
            Log.i(TAG, "-----------onError");
        }
    };
}
