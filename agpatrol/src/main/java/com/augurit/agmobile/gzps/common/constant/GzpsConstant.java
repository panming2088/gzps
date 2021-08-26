package com.augurit.agmobile.gzps.common.constant;

/**
 * Created by liangsh on 2017/11/7.
 */

public class GzpsConstant {
    public final static String[] districts = new String[]{
            "天河区",
            "越秀区",
            "海珠区",
            "黄埔区",
            "荔湾区",
            "白云区",
            "番禺区",
            "增城区",
            "花都区",
            "从化区",
            "南沙区",
            "净水公司"
    };

    public final static String[] districtsSimple1 = new String[]{
            "天河",
            "越秀",
            "海珠",
            "黄埔",
            "荔湾",
            "白云",
            "番禺",
            "增城",
            "花都",
            "从化",
            "南沙",
            "净水公司"
    };

    public final static String[] districtsSimple2 = new String[]{
            "市排水公司",
            "科学城排水公司",
            "番禺污水公司",
            "南沙排水公司",
            "花都排水公司",
            "增城排水公司",
            "从化排水公司",
            "其他"
    };

    public final static String[] districtsSimple = new String[]{
            "市水务局",
            "天河",
            "越秀",
            "海珠",
            "黄埔",
            "荔湾",
            "白云",
            "番禺",
            "增城",
            "花都",
            "从化",
            "南沙",
            "净水"
    };


    public static final String[] roleCodes = new String[]{
            "ps_xc",
            "ps_yh",
            "ps_yz_chief",
            "ps_gc",
            "ps_manager",
            "ps_zyxbc"
    };

    public static final String[] roleNames = new String[]{
            "巡查员R1",
            "养护员R2",
            "区所管养监管R0",
            "旁观者Rg",
            "排水中心Rm",
            "专业修补测"
    };

    /**
     * 下面的事件状态相对于流程参与人员而言，区别于一般意义上的流程状态（待办，已办，办结）
     */
    public static final String EVENT_P_STATE_KEY = "EVENT_P_STATE";

//    public static final int EVENT_P_STATE_UNSIGN = 0;         //事件为待领取

    public static final int EVENT_P_STATE_HANDLING = 1;         //事件为处理中，1.本人已领取但未提交；2.本人曾经领取过，不处于待办结或已办结的任意其他状态；

    public static final int EVENT_P_STATE_UNFINISH = 2;         //事件为待办结,处于复核节点

    public static final int EVENT_P_STATE_FINISHED = 3;         //事件为已办结

    public static final int EVENT_P_STATE_UPLOADED = 4;         //事件为已提交，针对上报人员

    public static final int EVENT_P_STATE_ROLLBACKED = 5;         //事件为被回退，针对上报人员

    public static final int EVENT_P_STATE_HANDLED = 6;         //事件为被回退，针对上报人员

 //   public static final int EVENT_P_MY_COMMIT = 7;             //我已上报的事件
    /**
     * 事件参与人状态结束
     */


    /**
     * 事件是否可处理
     */
    public static final String EVENT_OPEN = "open";   //可处理
    public static final String EVENT_COMPLETED = "completed";  //处理完成
    public static final String EVENT_R_OBSOLETE = "r_obsolete";   //已退回
    /**
     * 事件是否可处理结束
     */

    /**
     * 流程是否已结束
     */
    public static final String EVENT_SIMPLE_STATE_ACTIVE = "active";
    public static final String EVENT_SIMPLE_STATE_ENDED = "ended";
    /**
     *
     */

    /**
     * 各环节名称
     */
    public static final String LINK_PROBLEM_REPORT_NAME = "问题上报";
    public static final String LINK_PROBLEM_REPORT = "problemReport";

    public static final String LINK_SEND_TASK_NAME = "任务派发";
    public static final String LINK_SEND_TASK = "sendTask";

    public static final String LINK_GET_TASK_NAME = "任务处置";
    public static final String LINK_GET_TASK = "getTask";

    public static final String LINK_TASK_FH_NAME = "任务复核";
    public static final String LINK_TASK_FH = "rwfh";

    public static final String LINK_REPORT_FH_NAME = "巡查员复核";
    public static final String LINK_REPORT_TASK_FH = "xcyfh";

    public static final String LINK_FH_NAME = "复核";
    public static final String LINK_FH = "fh";

    public static final String LINK_END_NAME = "正常结束";
    public static final String LINK_END = "end";
    /**
     * 环节名称结束
     */



    public static final String FLOW_R1_UPLOAD= "gxxcyh";//R1上班的流程
    public static final String FLOW_RG_OR_RM_UPLOAD = "GX_XCYH_RGRM";//Rg或者Rm上报的流程
}
