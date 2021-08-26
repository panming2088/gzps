package com.augurit.agmobile.gzps.statistic.view.uploadview;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;


/**
 * 解决与父控件的左右滑动冲突
 */

public class CustomWebView extends WebView {

    private boolean isScrollX = false;
    private float x1;
    private float y1;

    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEventCompat.getPointerCount(event) == 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x1 = event.getX();
                    y1 = event.getY();
                    isScrollX = false;
                    //事件由webview处理
                    getParent().getParent()
                            .requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    //嵌套Viewpager时
                    if(event.getAction() == MotionEvent.ACTION_MOVE){
                        if (Math.abs(event.getX() - x1) > Math.abs(event.getY() - y1)) {
                            getParent().getParent()
                                    .requestDisallowInterceptTouchEvent(!isScrollX);
                        } else if (Math.abs(event.getY() - y1) > 50) {
                            getParent().getParent()
                                    .requestDisallowInterceptTouchEvent(false);
                        }
                    }

                    break;
                default:
                    getParent().getParent()
                            .requestDisallowInterceptTouchEvent(false);
            }
        } else {
            //使webview可以双指缩放（前提是webview必须开启缩放功能，并且加载的网页也支持缩放）
            getParent().getParent().
                    requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(event);
    }

    //当webview滚动到边界时执行
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        isScrollX = clampedX;
    }
}