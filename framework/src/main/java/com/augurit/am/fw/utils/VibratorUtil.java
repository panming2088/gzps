package com.augurit.am.fw.utils;

import android.content.Context;
import android.os.Vibrator;


/**
 * 使设备震动
 */
public class VibratorUtil {
    private static VibratorUtil instance;
    private Vibrator vibrator;

    private VibratorUtil(){

    }

    public static VibratorUtil getInstance(Context context) {
        if (instance == null) {
            instance = new VibratorUtil();
        }
        instance.setVibrator(
                (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE));
        return instance;
    }


    /**
     * @param milliseconds 震动时间，单位毫秒
     */
    public void vibrate(long milliseconds) {
        vibrator.vibrate(milliseconds);
    }

    public void vibrate(long[] pattern, int repeat) {
        vibrator.vibrate(pattern, repeat);
    }

    public void cancle() {
        vibrator.cancel();
    }


    public Vibrator getVibrator() {
        return vibrator;
    }


    public void setVibrator(Vibrator vibrator) {
        this.vibrator = vibrator;
    }


}
