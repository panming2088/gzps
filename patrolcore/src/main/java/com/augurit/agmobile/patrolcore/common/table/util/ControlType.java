package com.augurit.agmobile.patrolcore.common.table.util;

/**
 * 描述：控件类型  跟后端协商好的
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model
 * @createTime 创建时间 ：17/3/7
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/7
 * @modifyMemo 修改备注：
 */

public class ControlType {
    public final static String TEXT_FIELD = "A007001"; //文本
    public final static String  SPINNER = "A007002"; //下拉框
    public final static String DATE = "A007003";   //日期
    public final static String CHEXK_BOX = "A007004";//复选框
    public final static String TEXT_AREA = "A007005";//文本域
    public final static String IMAGE_PICKER = "A007006";//图片选择
    public final static String MAP_PICKER = "A007007";//地图选点
    public final static String WEB_MAP_PICKER = "A007013";//webview地图控件
    public final static String AUTO_COMPLETE_NET = "A007008";   // 在线模糊查询
    public final static String PATROL_PROGRESS = "A007009";//工单进度
    public final static String CUSSTOM = "A007010";//自定义类型,可通过Field1进行扩展无数自定义表格项View
    public final static String AUTO_COMPLETE_LOCAL ="A007011";//本地模糊查询
    public final static String OPINION_TEMPLATE = "A007012";  //应用意见模板的表单项
    public final static String COMPLEX_OPINION_TEMPLATE = "A007013";  //应用复杂意见模板的表单项
    public final static String PHOTO_IDENTIFY_TEXT_FIELD="A007014";//用于身份证图像识别的文本框控件
    public final static String H5_HTML = "A007016";     // h5详情展示控件
    public final static String EDIT_MAP_FEATURE = "A007017";     // 编辑地图
    public final static String RECEIVE_MAP_GEOMETRY = "A007018";     // 接收从地图传过来的几何图形
    public final static String ABSOLUTE_IMAGE_PICKER = "A007019";     // 绝对路径图片控件

    public final static String TEXT_INPUT_SEARCH_LOCAL = "482";//文本框 支持本地模糊搜索
    public final static String TEXT_INPUT_SEARCH_NET = "483";//文本框 支持服务器模糊搜索

    public final static String QR_SCAN_BTN = "A007015";//二维码扫描


}
