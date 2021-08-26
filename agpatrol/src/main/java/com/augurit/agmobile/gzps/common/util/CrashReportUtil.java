package com.augurit.agmobile.gzps.common.util;

import android.content.Context;

import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.fw.db.AMDatabase;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzpssb.utils
 * @createTime 创建时间 ：2018-05-16
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-05-16
 * @modifyMemo 修改备注：
 */

public class CrashReportUtil {

    /**
     * @param context
     * @param error
     * @param tag
     */
    public static void reportBugMsg(Context context,Throwable error,String tag) {
        CrashReport.postCatchedException(new Exception(tag+"--用户名：" +
                new LoginRouter(context, AMDatabase.getInstance()).getUser().getLoginName() +"--error--"+ error.getMessage()));
    }

}
