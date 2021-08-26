package com.augurit.agmobile.gzps.common.constant;

/**
 * 描述：登录常量
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common
 * @createTime 创建时间 ：2017-03-03
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-03-03
 * @modifyMemo 修改备注：
 */
public final class LoginConstant {

    //*******  各项目URL begin  **********/

    public static final String DEFAULT_SERVER_URL = "192.168.31.21:8080/agweb_main";
    public static final String DEFAULT_SUPPORT_URL = "192.168.31.21:8080/agweb_main";

    //public static String DEFAULT_SERVER_URL =  "112.74.13.45:8088/agweb_yd";
    //public static String DEFAULT_SUP
    // PORT_URL = "112.74.13.45:8088/agweb_yd";

    public static final String PRESENTATION_SERVER_URL = "210.21.98.71:8088/agweb14";
    public static final String PRESENTATION_SUPPORT_URL = "210.21.98.71:8088/agweb14";


    /**
     * 设施上报端口号
     */
    public static final String[] UPLOAD_PORT = {":8086",":8082",":8083",":8084",":8085"};
    /**
     * 设施上报接口后缀
     */
    public static final String UPLOAD_POSTFIX = "/agsupport_swj";
//
//    正式环境
//    public static final String GZPS_AGWEB = "139.159.243.230:8081/agweb_ha";
//    public static final String GZPS_AGSUPPORT = "139.159.243.230:8080/agsupport_swj";//设施上报的URL
//    public static final String UPLOAD_AGSUPPORT = "http://139.159.243.230";//设施上报的URL
//    public static  final String COLUMN_URL = "http://139.159.243.230:8099";       //专栏根URL

    //测试环境
    public static final String GZPS_AGWEB = "139.159.243.185:8081/agweb_ha";
    public static final String GZPS_AGSUPPORT = "139.159.243.185:8081/agsupport_swj";
    public static final String UPLOAD_AGSUPPORT = "http://139.159.243.185";//设施上报的URL
    public static final String COLUMN_URL = "http://139.159.243.230:8099";     //专栏根URL

    // public static String GZPS_AGSUPPORT ="192.168.43.130:8080/agsupport_swj";//许志勇的测试URL

    // public static String GZPS_AGSUPPORT ="192.168.43.139:8080/agsupport_swj";//李阳的测试URL

    //*******  各项目URL end  **********/
    //APP版本更新
    public static final String APP_UPDATE_ONE = "http://139.159.227.100:8080/appFile/apk_version.json";
    public static final String APP_UPDATE_TWO = "http://139.159.243.217:8080/appFile/apk_version.json";

    //版本更新测试URL
    //public static String APP_UPDATE_ONE = "http://139.159.243.185:8080/agsupport_swj/uploadFile/apk_version.json";
    //public static String APP_UPDATE_TWO = "http://139.159.243.185:8080/agsupport_swj/uploadFile/apk_version.json";

    public static final String DEFAULT_USERNAME = "zhongwx";
    public static final String DEFAULT_PASSWORD = "123";
    public static final int MAX_LENGTH_USERNAME = 16;    // 用户名可输入的最大长度(超过该长度给予提示)
    public static final int MAX_LENGTH_PASSWORD = 16;    // 密码可输入的最大长度(超过该长度给予提示)
    public static final boolean SAVE_USERNAME = true;    // 是否允许记住账号(关闭此选项仍会存到本地用以离线登录，只是不再显示登录过的账户)
    public static final boolean SAVE_PASSWORD = true;    // 记住密码选项是否开启(关闭此选项仍会存到本地用以离线登录)
    public static String SERVER_URL = GZPS_AGWEB;
    public static String SUPPORT_URL = GZPS_AGSUPPORT;
    public static final String LOGIN_URL = "/rest/system/getUser/";
    public static final String USER_BY_ID_URL = "/rest/system/getUserByUserId/";
    public static final String USER_BY_GROUPID = "/rest/system/getUsersByGroupId/";
    public static final String GROUP_URL = "/rest/system/getGroupsByUserId/";
    public static final String FRIEND_URL = "/rest/system/getContactsByUserId/";
    public static final String COLUMNSUNREAD_URL = "/rest/columnsUnread/getUnreadInfo/";
    public static final String CHECK_USERNAME_URL = "/rest/system/checkUserName/";
    public static final String MODIFY_PASSWORD_URL = "/rest/system/updatePasswordByLoginName/";


    public static String pshUploadPort = ":8081";
    public static String pshAGWEBPort = ":8081";
    public static final String UPLOAD_AGWEB = "/agweb_ha";
    public static Boolean isCheckOut=true;//排水户-广州排水用户基本信息表是否校验，false:不检验。true:校验
    private LoginConstant() {
    }
}
