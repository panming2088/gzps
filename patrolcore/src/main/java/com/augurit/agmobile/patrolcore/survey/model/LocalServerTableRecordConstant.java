package com.augurit.agmobile.patrolcore.survey.model;

/**
 * 表格类型定义，用于{@link LocalServerTableRecord#tableType}字段
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.model
 * @createTime 创建时间 ：17/9/18
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/9/18
 * @modifyMemo 修改备注：
 */

public class LocalServerTableRecordConstant {

    public static final String DONG = "1" ;//栋表

    public static final String TAO = "2"; //套表

    public static final String REN_KOU_BASIC_INFO = "3" ;//人口基本信息表

    public static final String LIU_DONG_REN_KOU = "4";//流动人口表

    public static final String JING_WAI_REN_KOU = "5" ;//境外人口表

    public static final String SHI_YOU_DAN_WEI= "6" ;//实有单位表

    public static final String XUE_SHENG_INFO = "7"; //学生信息表

    public static final String CONGYE_RENYUAN_INFO = "8"; //从业人员表

    public static final String XIAO_FANG = "9"; //消防表




    /*************任务类型******************/
    public static final String DESIGNATED_BY_LEADER = "1" ;// 领导指派的任务

    public static final String BY_SELF = "2" ;//自己领取的任务
}
