package com.augurit.agmobile.patrolcore.common.opinion.util;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.util
 * @createTime 创建时间 ：2017-07-13
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-13
 * @modifyMemo 修改备注：
 */

public class OpinionConstant {

    /**
     * 意见模板授权常量定义开始
     */
    public static final String PRIVATE = "私有";   //私人
    public static final String PUBLIC = "公开";   //公开

    public static final String REPORT_LINK_NAME = "NONE";  //上报阶段固定环节名，以区分其它环节的意见模板
    /**
     * 意见模板授权常量定义结束
     */

    /**
     * 意见模板规则定义开始
     */
    public static final String USERNAME = "[[username]]";   //替换为用户名
    public static final String DATE = "[[date]]";   //替换为当前日期
    public static final String TIME = "[[time]]";   //替换为当前时间
    public static final String FIELDRULESTART = "[[[";   //字段规则的开始标识
    public static final String FIELDRULEEND = "]]]";   //字段规则的结束标识
    public static final String FIELDRULESEP = "+";     //字段规则的间隔标识
    /**
     * 意见模板规则定义结束
     */
}
