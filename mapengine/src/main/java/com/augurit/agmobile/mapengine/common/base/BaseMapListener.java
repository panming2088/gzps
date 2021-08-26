package com.augurit.agmobile.mapengine.common.base;

import android.app.Activity;
import android.content.Context;

import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.agmobile.action.zoom
 * @createTime 创建时间 ：2016-11-18
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-18
 */

public abstract class BaseMapListener extends MapOnTouchListener {
    public static final String TAG = BaseMapListener.class.getName();
    protected MapView mMapView;
    protected Activity mContext;
    private long mlastTime = 0L;//上传显示的时间
    private boolean isFirstMove = true;

    public BaseMapListener(Activity context, MapView view) {
        super(context, view);
        this.mMapView = view;
        this.mContext = context;
    }

   /* @Override
    public boolean onGraphicSelected(MotionEvent point) {
        //注销掉是为了可以点查时点击空白地方隐藏气泡
       *//* if (mMapView.getCallout().isShowing()) {
            mMapView.getCallout().hide();
        }*//*
        return super.onGraphicSelected(point);
    }

    @Override
    public boolean onFling(MotionEvent from, MotionEvent to, float velocityX, float velocityY) {
*//*
        Point center = mMapView.getCenter();
        Envelope extent = mMapView.getMaxExtent();
        Point upperLeft = extent.getUpperLeft();
        Point lowerRight = extent.getLowerRight();
        if (Math.abs(center.getX()- upperLeft.getX()) < 0.01||
                Math.abs(center.getY()- upperLeft.getY())<0.01 ||
                Math.abs(center.getX() - lowerRight.getX())<0.01 ||
                Math.abs(center.getY() - lowerRight.getY())<0.01){
          //  ToastUtil.shortToast(mContext,"到达地图边界");
            LogUtil.e(TAG,"到达地图边界");
        }*//*
    *//*    LogUtil.e(TAG,"当前的center:"+center.getX()+","+center.getY()
                +"/当前的upperleft是："+upperLeft.getX()+","+upperLeft.getY()
                +"/当前的lowerRight是："+lowerRight.getX()+","+lowerRight.getY());*//*
        return super.onFling(from, to, velocityX, velocityY);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouch(v, event);
      *//*  if (event != null){
            int action = event.getAction();
            switch (action){
                case MotionEvent.ACTION_MOVE:
                    Point center = mMapView.getCenter();
                    Envelope extent = mMapView.getMaxExtent();
                    Point upperLeft = extent.getUpperLeft();
                    Point lowerRight = extent.getLowerRight();
               *//**//* if (Math.abs(center.getX()- upperLeft.getX()) < 0.001||
                        Math.abs(center.getY()- upperLeft.getY())<0.001 ||
                        Math.abs(center.getX() - lowerRight.getX())<0.001 ||
                        Math.abs(center.getY() - lowerRight.getY())<0.001){
                    ToastUtil.shortToast(mContext,"到达地图边界");
                }*//**//*
                  *//**//*  if (center.getX()== upperLeft.getX()||
                            center.getY()== upperLeft.getY() ||
                            center.getX() == lowerRight.getX() ||
                            center.getY() == lowerRight.getY()){

                        long currentTime = System.currentTimeMillis();
                        if (currentTime - mlastTime > 2000){
                            //如果大于两秒才显示
                           // ToastUtil.shortToast(mContext,"到达地图边界");
                            LogUtil.w(TAG,"到达地图边界");
                        }
                        mlastTime = currentTime;
                    }*//**//*
                    break;
            }

        }
        return true;*//*
    }*/

    public abstract void enableRealMapTouch();

    public abstract void disableRealMapTouch();

    public abstract void clearMap();

    public abstract void onDestroy();
}
