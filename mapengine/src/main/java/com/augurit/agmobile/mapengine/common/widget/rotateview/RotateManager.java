package com.augurit.agmobile.mapengine.common.widget.rotateview;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.view.animation.AccelerateInterpolator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.widget
 * @createTime 创建时间 ：2018-03-02
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2018-03-02
 * @modifyMemo 修改备注：
 */

public class RotateManager {

    private static RotateManager instance;

    private Context mContext;



    private Map<Object, OnRotateChangedListener> mListeners;

    /**
     * 以下变量用于确定方向
     */
    private final float MAX_ROATE_DEGREE = 1.0f;
    private SensorManager mSensorManager;
    private Sensor mOrientationSensor;
    private float mDirection = -10000;
    private float mTargetDirection;
    private AccelerateInterpolator mInterpolator;
    protected final Handler mHandler = new Handler();

    protected Runnable mCompassViewUpdater = new Runnable() {
        @Override
        public void run() {
                if (mDirection != mTargetDirection) {

                    // calculate the short routine
                    float to = mTargetDirection;
                    if (to - mDirection > 180) {
                        to -= 360;
                    } else if (to - mDirection < -180) {
                        to += 360;
                    }

                    // limit the max speed to MAX_ROTATE_DEGREE
                    float distance = to - mDirection;
                    if (Math.abs(distance) > MAX_ROATE_DEGREE) {
                        distance = distance > 0 ? MAX_ROATE_DEGREE : (-1.0f * MAX_ROATE_DEGREE);
                    }

                    // need to slow down if the distance is short
                    mDirection = normalizeDegree(mDirection
                            + ((to - mDirection) * mInterpolator.getInterpolation(Math
                            .abs(distance) > MAX_ROATE_DEGREE ? 0.4f : 0.3f)));
                    updateArrowRotate();
                }
                mHandler.postDelayed(mCompassViewUpdater, 20);
        }
    };
    /**
     * 确定方向变量结束
     */

    public static RotateManager getInstance(Context context){
        if(instance == null){
            instance = new RotateManager(context);
        }
        return instance;
    }

    private RotateManager(Context context){
        this.mContext = context;
        mListeners = new HashMap<>();
        initResources();
        initServices();
        if (mOrientationSensor != null) {
            mSensorManager.registerListener(mOrientationSensorEventListener, mOrientationSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
        mHandler.postDelayed(mCompassViewUpdater, 20);
    }



    private void initResources() {
        mDirection = 0.0f;
        mTargetDirection = 0.0f;
        mInterpolator = new AccelerateInterpolator();
    }

    private void initServices() {
        // sensor manager
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

    }

    private SensorEventListener mOrientationSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            float direction = event.values[0] * -1.0f;
            mTargetDirection = normalizeDegree(direction);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private float normalizeDegree(float degree) {
        return (degree + 720) % 360;
    }

    private void updateArrowRotate(){
        if(//mGraphicId == -1 ||
                mDirection == -10000){
            return;
        }
        if(mListeners.size() > 0){
            for(OnRotateChangedListener onRotateChangeListener : mListeners.values()){
                if(onRotateChangeListener != null){
                    onRotateChangeListener.onRotateChanged(-mDirection);
                }
            }
        }
        /*Bitmap bm = BitmapUtil.rotateBitmapByDegree2(mBitmap, -mDirection);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mContext, BitmapUtil.bimapToDrawable(bm));
        mGraphicsLayer.updateGraphic(mGraphicId, pictureMarkerSymbol);*/
    }

    public void onDestroy(){
        mContext = null;
        mSensorManager.unregisterListener(mOrientationSensorEventListener, mOrientationSensor);
        mSensorManager = null;
        mOrientationSensorEventListener = null;
        mOrientationSensor = null;
        mInterpolator = null;
        instance = null;
    }

    public void addOnRotateChangedListener(Object object, OnRotateChangedListener onRotateChangedListener){
        if(mListeners.size() == 0){
            mSensorManager.registerListener(mOrientationSensorEventListener, mOrientationSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
        if(mListeners.containsKey(object)){
            return;
        }
        mListeners.put(object, onRotateChangedListener);
    }

    public void removeListener(Object object){
        mListeners.remove(object);
        if(mListeners.size() == 0){
            mSensorManager.unregisterListener(mOrientationSensorEventListener, mOrientationSensor);
        }
    }

    public interface OnRotateChangedListener{
        void onRotateChanged(float direction);
    }

}
