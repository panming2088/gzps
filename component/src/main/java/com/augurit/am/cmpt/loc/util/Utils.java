package com.augurit.am.cmpt.loc.util;

import android.content.Context;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.location
 * @createTime 创建时间 ：2017-03-09
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-09
 * @modifyMemo 修改备注：
 */

public class Utils {

    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

}