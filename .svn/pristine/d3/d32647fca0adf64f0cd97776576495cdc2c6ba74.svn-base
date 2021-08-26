package com.augurit.am.cmpt.media.speech;

import android.content.Context;
import android.util.Log;

import com.augurit.am.cmpt.media.speech.intfc.WakeUpListener;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 *
 * 包名：com.augurit.am.cmpt.media.speech
 * 文件描述：
 * 创建人：xiejiexin
 * 创建时间：2016-09-06 12:01
 * 修改人：xiejiexin
 * 修改时间：2016-09-06 12:01
 * 修改备注：语音唤醒功能封装类
 * @version
 *
 */
public class VoiceWakeUp {

    private static final String TAG = "VoiceWakeUp";
    private EventManager mWpEventManager;   // 唤醒事件管理
    private WakeUpListener mWakeUpListener; // 唤醒监听

    public VoiceWakeUp(Context context) {
        init(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void init(Context context) {
        // 创建管理器
        mWpEventManager = EventManagerFactory.create(context, "wp");

        // 注册监听
        mWpEventManager.registerListener(new EventListener() {
            @Override
            public void onEvent(String name, String params, byte[] bytes, int offset, int length) {
                Log.d(TAG, String.format("event: name=%s, params=%s", name, params));
                try {
                    JSONObject jo = new JSONObject(params);
                    if ("wp.data".equals(name)) { // 唤醒成功回调name=wp.data
                        String word = jo.getString("word"); // 唤醒词
                        if (mWakeUpListener != null) {
                            mWakeUpListener.onWakeUp(word);
                        }
                    } else if ("wp.exit".equals(name)) {    // 唤醒停止
                        if (mWakeUpListener != null) {
                            mWakeUpListener.onExit();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 启动语音唤醒
     */
    public void start() {
        // 启动唤醒功能
        HashMap params = new HashMap();
//        params.put("kws-file", "assets:///wake_up.bin");
        // 设置唤醒资源
        // wake_up.bin唤醒关键字：百度一下、小度你好
        // wake_up_ag.bin唤醒关键字：奥格你好、你好奥格、嗨奥格
        params.put("kws-file", "res:///res/raw/wake_up_ag.bin");
        mWpEventManager.send("wp.start", new JSONObject(params).toString(), null, 0, 0);
    }

    /**
     * 停止语音唤醒
     */
    public void stop() {
        mWpEventManager.send("wp.stop", null, null, 0, 0);
    }

    /**
     * 设置唤醒状态监听
     * @param wakeUpListener 监听
     */
    public void setWakeUpListener(WakeUpListener wakeUpListener) {
        mWakeUpListener = wakeUpListener;
    }

    public WakeUpListener getWakeUpListener() {
        return mWakeUpListener;
    }
}
