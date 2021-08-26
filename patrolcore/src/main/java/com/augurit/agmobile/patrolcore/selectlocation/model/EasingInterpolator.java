package com.augurit.agmobile.patrolcore.selectlocation.model;

import android.animation.TimeInterpolator;
import android.support.annotation.NonNull;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.map
 * @createTime 创建时间 ：2017-03-07
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-07
 * @modifyMemo 修改备注：
 */

public class EasingInterpolator implements TimeInterpolator {

    private final Ease ease;

    public EasingInterpolator(@NonNull Ease ease) {
        this.ease = ease;
    }

    @Override
    public float getInterpolation(float input) {
        return EasingProvider.get(this.ease, input);
    }

    public Ease getEase() {
        return ease;
    }
}