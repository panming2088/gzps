package com.augurit.am.cmpt.common.base;

import android.content.Context;
import android.support.annotation.Keep;

import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.file.SharedPreferencesUtil;


/**
 * 基本URL和UserId的管理类
 *
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.common
 * @createTime 创建时间 ：2017-01-20
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-01-20
 */
@Keep
public class BaseInfoManager {

    public static String getRestSystemUrl(Context context) {
        String baseServerUrl = getBaseServerUrl(context);
        return (baseServerUrl + RequestConstant.URL_REST + RequestConstant.URL_DIVIDER + RequestConstant.URL_SYSTEM +
                RequestConstant.URL_DIVIDER).trim();
    }


    public static String getBaseServerUrl(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        //默认服务器版本是4.4以上
        // boolean ifAbove44 = sharedPreferencesUtil.getBoolean(RequestConstant.IF_SERVER_VERSION_ABOVE_44, true);
        String server_url = null;
        if (false) {
            //如果服务器地址大于4.4，那么大部分的rest接口来自于agsupport
            server_url = sharedPreferencesUtil.getString(RequestConstant.SUPPORT_URL, "");
        } else {
            //如果服务器地址小于4.4，那么大部分的rest接口来自于agcom
            server_url = sharedPreferencesUtil.getString(RequestConstant.SERVER_URL, "");
        }
//        LogUtil.d("项目中的大部分接口来自于："+server_url);
        String base_url = null;
        if (server_url.contains(RequestConstant.HTTP_REQUEST)) {
            base_url = server_url + RequestConstant.URL_DIVIDER;
        } else {
            base_url = RequestConstant.HTTP_REQUEST + server_url + RequestConstant.URL_DIVIDER;
        }
        return base_url;
    }

    /**
     * 返回设置界面填写的ip，不包含“rest/system”
     *
     * @param context
     * @return
     */
    public static String getBaseServerUrlWithoutRestSystem(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        //默认服务器版本是4.4以上
        // boolean ifAbove44 = sharedPreferencesUtil.getBoolean(RequestConstant.IF_SERVER_VERSION_ABOVE_44, true);
        String server_url = null;
        if (false) {
            //如果服务器地址大于4.4，那么大部分的rest接口来自于agsupport
            server_url = sharedPreferencesUtil.getString(RequestConstant.SUPPORT_URL, "");
        } else {
            //如果服务器地址小于4.4，那么大部分的rest接口来自于agcom
            server_url = sharedPreferencesUtil.getString(RequestConstant.SERVER_URL, "");
        }

        if (!server_url.contains(RequestConstant.HTTP_REQUEST)) {
            server_url = RequestConstant.HTTP_REQUEST + server_url;
        }
        return server_url;
    }

    public static String getSupportUrl(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String server_url = sharedPreferencesUtil.getString(RequestConstant.SUPPORT_URL, "");
        String base_url = null;
        if (server_url.contains(RequestConstant.HTTP_REQUEST)) {
            base_url = server_url + RequestConstant.URL_DIVIDER;
        } else {
            base_url = RequestConstant.HTTP_REQUEST + server_url + RequestConstant.URL_DIVIDER;
        }
        return base_url;
    }

    /**
     * don't call
     *
     * @param context
     * @return
     */
    @Deprecated
    public static String getAgcomUrl(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String server_url = sharedPreferencesUtil.getString(RequestConstant.SERVER_URL, "");
        String base_url = null;
        if (server_url.contains(RequestConstant.HTTP_REQUEST)) {
            base_url = server_url + RequestConstant.URL_DIVIDER;
        } else {
            base_url = RequestConstant.HTTP_REQUEST + server_url + RequestConstant.URL_DIVIDER;
        }
        return base_url;
    }


    public static String getRestSupportUrl(Context context) {

        return getSupportUrl(context) + "rest/";
    }


    public static String getUserId(Context context) {
//        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
//        String userId = sharedPreferencesUtil.getString(RequestConstant.USER_ID, "");
        LoginRouter loginRouter = new LoginRouter(context, AMDatabase.getInstance());
        return loginRouter.getUser().getId();
    }


    public static void setServerUrl(Context context, String serverUrl) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(RequestConstant.SERVER_URL, serverUrl);
    }


    public static void setSupportUrl(Context context, String supportUrl) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(RequestConstant.SUPPORT_URL, supportUrl);
    }


    public static void setUserId(Context context, String userId) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(RequestConstant.USER_ID, userId);
    }

    public static void setImToken(Context context, String token) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(RequestConstant.IM_TOKEN, token);
    }

    public static String getImToken(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String string = sharedPreferencesUtil.getString(RequestConstant.IM_TOKEN, "");
        return string;
    }

    public static void setUserName(Context context, String userName) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(RequestConstant.USER_NAME, userName);
    }

    public static String getUserName(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String userName = sharedPreferencesUtil.getString(RequestConstant.USER_NAME, "");
        return userName;
    }
    public static void setUserPhone(Context context, String phone) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(RequestConstant.USER_PHONE, phone);
    }

    public static String getUserPhone(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String phone = sharedPreferencesUtil.getString(RequestConstant.USER_PHONE, "");
        return phone;
    }
    public static void setUserDuty(Context context, String duty) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(RequestConstant.USER_DUTY, duty);
    }

    public static String getUserDuty(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String duty = sharedPreferencesUtil.getString(RequestConstant.USER_DUTY, "");
        return duty;
    }

    public static void setRoleName(Context context, String roleName) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(RequestConstant.ROLE_NAME, roleName);
    }

    public static String getRoleName(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String roleName = sharedPreferencesUtil.getString(RequestConstant.ROLE_NAME, "");
        return roleName;
    }

    public static void setUserOrg(Context context, String userOrg) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(RequestConstant.USER_ORG, userOrg);
    }

    public static String getUserOrg(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String userOrg = sharedPreferencesUtil.getString(RequestConstant.USER_ORG, "");
        return userOrg;
    }

    public static void setOrgCode(Context context, String orgCode) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(RequestConstant.ORG_CODE, orgCode);
    }

    public static String getOrgCode(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String userOrg = sharedPreferencesUtil.getString(RequestConstant.ORG_CODE, "");
        return userOrg;
    }


    public static void setLoginName(Context context, String loginName) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(RequestConstant.LOGIN_NAME, loginName);
    }

    public static String getLoginName(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String loginName = sharedPreferencesUtil.getString(RequestConstant.LOGIN_NAME, "");
        return loginName;
    }

    public static void setSign(Context context, String time) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setString(RequestConstant.SIGN_INFO, time);
    }

    public static String getSign(Context context) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String loginName = sharedPreferencesUtil.getString(RequestConstant.SIGN_INFO, "");
        return loginName;
    }
   /* public static void setServerVersionAbove44(Context context,boolean ifServerVersionAbove44) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        sharedPreferencesUtil.setBoolean(RequestConstant.IF_SERVER_VERSION_ABOVE_44,ifServerVersionAbove44);
    }*/


    public static String getBaseLayerServerUrl(Context context) {
        //SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String server_url = getBaseServerUrl(context);
        // String server_url = sharedPreferencesUtil.getString(RequestConstant.SERVER_URL,"");
       /* String base_url = null;
        if (server_url.contains(RequestConstant.HTTP_REQUEST)){
            base_url = server_url + RequestConstant.URL_DIVIDER+ RequestConstant.URL_REST+ RequestConstant.URL_DIVIDER+ RequestConstant.URL_AGMOIBLELAYER+
                    RequestConstant.URL_DIVIDER;
        }else {
            base_url = RequestConstant.HTTP_REQUEST + server_url+ RequestConstant.URL_DIVIDER+ RequestConstant.URL_REST+ RequestConstant.URL_DIVIDER+
                    RequestConstant.URL_AGMOIBLELAYER+
                    RequestConstant.URL_DIVIDER;
        }*/
        return server_url + RequestConstant.URL_REST + RequestConstant.URL_DIVIDER + RequestConstant.URL_AGMOIBLELAYER +
                RequestConstant.URL_DIVIDER;
    }

    public static String getBaseLayerServerUrlWithSystem(Context context) {
        //SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context);
        String server_url = getBaseServerUrl(context);
        // String server_url = sharedPreferencesUtil.getString(RequestConstant.SERVER_URL,"");
       /* String base_url = null;
        if (server_url.contains(RequestConstant.HTTP_REQUEST)){
            base_url = server_url + RequestConstant.URL_DIVIDER+ RequestConstant.URL_REST+ RequestConstant.URL_DIVIDER+ RequestConstant.URL_AGMOIBLELAYER+
                    RequestConstant.URL_DIVIDER;
        }else {
            base_url = RequestConstant.HTTP_REQUEST + server_url+ RequestConstant.URL_DIVIDER+ RequestConstant.URL_REST+ RequestConstant.URL_DIVIDER+
                    RequestConstant.URL_AGMOIBLELAYER+
                    RequestConstant.URL_DIVIDER;
        }*/
        return server_url + RequestConstant.URL_REST + RequestConstant.URL_DIVIDER + RequestConstant.URL_SYSTEM +
                RequestConstant.URL_DIVIDER;
    }
}
