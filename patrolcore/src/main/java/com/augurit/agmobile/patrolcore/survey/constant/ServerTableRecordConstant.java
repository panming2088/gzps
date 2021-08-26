package com.augurit.agmobile.patrolcore.survey.constant;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.constant
 * @createTime 创建时间 ：2017/9/2
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/9/2
 * @modifyMemo 修改备注：
 */
public class ServerTableRecordConstant {

    public static final String UNCHECK = "0";//待审核,包括两种情况：1. 完全没审核过 2.已经审核部分，但是未全部完成

    public static final String CHECKED = "1";//已经审核

    public static final String DELETED = "2"; //删除

    public static final String UNUPLOAD = "3";//本地数据，未提交

    public static final String HAS_TAO = "1"; //有套列表

     public static final String JINGWAI = "1"; //境外人员
     public static final String LIUDONG = "2"; //流动人员
     public static final String BENSHI = "3"; //本市人口

}
