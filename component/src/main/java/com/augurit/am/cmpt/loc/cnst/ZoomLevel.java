package com.augurit.am.cmpt.loc.cnst;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.cmpt.loc.cnst
 * @createTime 创建时间 ：2017-01-07
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-07
 */

public final class ZoomLevel {

    /**
     * 当定位完成后，要缩放的级别
     */
    public static final int ZOOM_TO_MAX = 1;
    public static final int UNCHANGED = 2;
    public static final int ZOOM_TO_MIN = 3;

    private ZoomLevel(){

    }
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ZOOM_TO_MAX,UNCHANGED,ZOOM_TO_MIN})
    public @interface Level{

    }

}
