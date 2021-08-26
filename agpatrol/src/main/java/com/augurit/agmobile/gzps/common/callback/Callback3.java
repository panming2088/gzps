package com.augurit.agmobile.gzps.common.callback;

/**
 * com.augurit.agmobile.gzpssb.view
 * Created by sdb on 2018/4/17  19:41.
 * Desc：
 */

import android.support.annotation.Keep;

import com.augurit.am.cmpt.common.Callback;


/**
 * Created by liangsh on 2017-01-20.
 */
@Keep
public interface Callback3<T, T1> extends Callback {
    void onCallback(T t, T1 t1);
}
