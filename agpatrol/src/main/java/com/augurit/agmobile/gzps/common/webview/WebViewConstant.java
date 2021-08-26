package com.augurit.agmobile.gzps.common.webview;

import com.augurit.agmobile.gzps.common.constant.LoginConstant;

/**
 * Created by xcl on 2017/10/23.
 */

public class WebViewConstant {

    /**
     * {@link WebViewActivity}的标题
     */
    public static final String WEBVIEW_TITLE = "WEBVIEW_TITLE";
    public static final String WEBVIEW_URL_PATH = "WEBVIEW_URL_PATH";


    public static class H5_URLS {

        private static final String HTML_PATH = "/DrainageFacilities/html/";
        /**
         * 安装率、签到率统计URL
         */
        public static final String MY_SIGN_IN_STATISTIC_URL = HTML_PATH + "Statistics.html";
        /**
         * 每日签到的Url
         */
        public static final String MY_SIGN_IN_URL = HTML_PATH + "EverydaySign.html";
        /**
         * 经验交流的Url
         */
        public static final String EXPERIMENT_SHARE_URL = LoginConstant.COLUMN_URL + "/jyjl/index.jhtml";
        /**
         * 修改密码的Url
         */
        public static final String CHANGE_PASSWORD_URL = HTML_PATH + "ModifyPassword.html";

        /**
         * 政策法规的url
         */
        public static final String POLICY_REGULATIOS = LoginConstant.COLUMN_URL + "/zcfg/index.jhtml";
        /**
         * 标准规范
         */
        public static final String STANDARD_SPECIFICATION = LoginConstant.COLUMN_URL + "/bzgf/index.jhtml";

        /**
         * 新闻动态URL
         */
        public static final String RECENT_NEWS = LoginConstant.COLUMN_URL + "/xwdt/index.jhtml";

        /**
         * 操作须知URL
         */
        public static final String OPERATE_KNOW = LoginConstant.COLUMN_URL + "/czxz/index.jhtml";

        /**
         * 通知公告
         */
        public static final String NOTIFICATIONS = LoginConstant.COLUMN_URL + "/tzgg/index.jhtml";
        /**
         * 红榜单
         */
        public static final String RED_BOARD = LoginConstant.COLUMN_URL + "/redb/index.jhtml";
        /**
         * 黑榜单
         */
        public static final String BLACK_BOARD = LoginConstant.COLUMN_URL + "/blackb/index.jhtml";
    }
}
