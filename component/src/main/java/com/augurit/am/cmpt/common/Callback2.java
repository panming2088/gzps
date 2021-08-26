package com.augurit.am.cmpt.common;

import android.support.annotation.Keep;

/**
 * Created by liangsh on 2017-01-20.
 */
@Keep
public interface Callback2<T> {
    void onSuccess(T t);
    void onFail(Exception error);
}
