package com.augurit.agmobile.patrolcore.editmap.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.selectlocation.model.Ease;
import com.augurit.agmobile.patrolcore.selectlocation.model.EasingInterpolator;

/**
 * Created by xcl on 2017/10/21.
 */
public class LocationMarker extends LinearLayout {

    private ImageView iv_location;
    private ImageView iv_shadow;

    public LocationMarker(Context context) {
        super(context);
    }

    public LocationMarker(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.widget_location_marker, this, true);
        iv_location = (ImageView) findViewById(R.id.iv_location);
        iv_shadow = (ImageView) findViewById(R.id.iv_shadow);
    }


    /**
     * 弹跳动画
     */
    public  void bounce() {
        if (iv_location.getVisibility() == View.VISIBLE) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(iv_location, "translationY", 0, -80, 0);
            animator.setInterpolator(new EasingInterpolator(Ease.ELASTIC_IN_OUT));
            animator.setDuration(1000);
            animator.start();
        }
    }

    private boolean isUp = false;

    /**
     * 向上移动动画
     */
    public void startUpAnimation(Animation.AnimationListener listener){
        if (isUp){
            return;
        }
        Animation animation = new TranslateAnimation(
                0,
                0,
                0  ,
               - 80);
        animation.setDuration(500);
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        iv_location.startAnimation(animation);//开始动画
        if(listener != null){
            animation.setAnimationListener(listener);
        }

        isUp = true;
    }

    /**
     * 向下移动动画
     */
    public void startDownAnimation(Animation.AnimationListener listener){
        if(isUp){
            Animation animation = new TranslateAnimation(
                    0,
                    0,
                    - 80,
                    0);
            animation.setDuration(500);
            animation.setFillAfter(true);//设置为true，动画转化结束后被应用
            animation.setInterpolator(new AccelerateInterpolator(15));
            if(listener != null){
                animation.setAnimationListener(listener);
            }
            iv_location.startAnimation(animation);//开始动画
            isUp = false;
        }
    }


    public void changeIcon(int resId){
        iv_location.setImageResource(resId);
    }
}
