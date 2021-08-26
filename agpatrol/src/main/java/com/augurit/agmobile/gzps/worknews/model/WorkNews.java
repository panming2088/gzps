package com.augurit.agmobile.gzps.worknews.model;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * 巡检动态
 * Created by xcl on 2017/11/15.
 */

public class WorkNews {
    /**
     * PARENT_ORG_NAME : 黄埔区水务局
     * DIRECT_ORG_NAME : 黄埔区市政建设有限公司
     * SUPERVISE_ORG_NAME : null
     * TEAM_ORG_NAME : 巡查组
     * ID : 1004
     * time : 1510752103000
     * MARK_PERSON : 哈哈
     * PROBLEM_TYPE : null
     * LAYER_NAME : 8
     * source : problem
     */

    private String PARENT_ORG_NAME;
    private String DIRECT_ORG_NAME;
    private Object SUPERVISE_ORG_NAME;
    private String TEAM_ORG_NAME;
    private int ID;
    private long time;
    private String MARK_PERSON;
    private Object PROBLEM_TYPE;
    private String LAYER_NAME;
    private String CORRECT_TYPE;
    private String COMPONENT_TYPE;
    private String source;



    public String getDateStr() {
        return "[" + TimeUtil.getStringTimeMDS(new Date(this.time)) + "]";
    }

    private String getUnitName(Context context){

        String district = null;
        String userOrg = BaseInfoManager.getUserOrg(context);
        String superviseOrgName = null;
        if(this.SUPERVISE_ORG_NAME !=null){
            if(this.SUPERVISE_ORG_NAME instanceof  String){
                superviseOrgName = (String) this.SUPERVISE_ORG_NAME;
            }
        }

        if (userOrg != null && userOrg.contains("市") && !TextUtils.isEmpty(this.PARENT_ORG_NAME)){
//                district = data.getParentOrgName();
            if (!TextUtils.isEmpty(this.DIRECT_ORG_NAME)){
                district = this.DIRECT_ORG_NAME;
            }else if (!TextUtils.isEmpty(superviseOrgName)){
                district = superviseOrgName;
            }else {
                district = this.PARENT_ORG_NAME;
            }
        }else {
            if (!TextUtils.isEmpty(this.DIRECT_ORG_NAME)){
                district = this.DIRECT_ORG_NAME;
            }else if (!TextUtils.isEmpty(superviseOrgName)){
                district = superviseOrgName;
            }else {
                district = this.PARENT_ORG_NAME;
            }
        }
            return district;
    }


    public String getWorkNewsName(Context context) {
        String unitName = getUnitName(context);
        int type = getType(this.source);
        switch (type) {
                /*
                  *AAA单位XXX已上报一条数据：设施类别      2017-11-18 10:30
                 */
            case UPLOAD_FACILITY:
//                return StringUtil.getNotNullString(this.PARENT_ORG_NAME,"")
                return   unitName + StringUtil.getNotNullString(this.MARK_PERSON,"")
                        + "上报(" + StringUtil.getNotNullString(this.COMPONENT_TYPE,"")
                        + ")数据";
            /**
             * AAA单位XXX已纠错一条数据：设施类别+纠错类别     2017-11-18 09:30
             */
            case MODIFY_FACILITY:
//                return StringUtil.getNotNullString(this.PARENT_ORG_NAME,"")
                return   unitName + StringUtil.getNotNullString(this.MARK_PERSON,"")
                        + "纠错数据(" + StringUtil.getNotNullString(this.LAYER_NAME,"")
                        + StringUtil.getNotNullString(this.CORRECT_TYPE,"")+")";
            case DELETE_FACILITY:
//                return StringUtil.getNotNullString(this.PARENT_ORG_NAME,"")
                return   unitName + StringUtil.getNotNullString(this.MARK_PERSON,"")
                        + "删除(" + StringUtil.getNotNullString(this.COMPONENT_TYPE,"")
                        + ")数据";
            /**
             * AAA单位XXX已上报一个问题：设施类别+问题类别     2017-11-18 08:30
             */
            case UPLOAD_PROBLEM:
                TableDBService dbService = new TableDBService(context);
                List<DictionaryItem> problemTypes = dbService.getDictionaryByCode(StringUtil.getNotNullString(this.PROBLEM_TYPE,""));
                String problemType = "";
                if (!ListUtil.isEmpty(problemTypes)){
                    problemType = problemTypes.get(0).getName();
                }

                List<DictionaryItem> layerNames = dbService.getDictionaryByCode(StringUtil.getNotNullString(this.LAYER_NAME,""));
                String layerName = "";
                if (!ListUtil.isEmpty(layerNames)){
                    layerName = layerNames.get(0).getName();
                }

                if (TextUtils.isEmpty(problemType)){
//                    return StringUtil.getNotNullString(this.PARENT_ORG_NAME,"")
                    return   unitName   + StringUtil.getNotNullString(this.MARK_PERSON,"")
                            + "上报问题";
                }else {
//                    return StringUtil.getNotNullString(this.PARENT_ORG_NAME,"")
                    return   unitName +StringUtil.getNotNullString(this.MARK_PERSON,"")
                            + "上报问题(" +  layerName + problemType +")";
                }
                default:
                    break;
        }
        return "";
    }

    public static final int UPLOAD_FACILITY = 1;
    public static final int MODIFY_FACILITY = 2;
    public static final int UPLOAD_PROBLEM = 3;
    public static final int DELETE_FACILITY = 5;
    public static final int ERROR_TYPE = 4;

    public int getType(String type) {
        if (type.equals("lack")) {
            //设施上报
            return UPLOAD_FACILITY;
        }else if(type.equals("delete")){
            return DELETE_FACILITY;
        }
        else if (type.equals("correct")) {
            //设施纠错
            return MODIFY_FACILITY;
        } else if (type.equals("problem")) {
            //问题上报
            return UPLOAD_PROBLEM;
        }
        return ERROR_TYPE;
    }

    public String getPARENT_ORG_NAME() {
        return PARENT_ORG_NAME;
    }

    public void setPARENT_ORG_NAME(String PARENT_ORG_NAME) {
        this.PARENT_ORG_NAME = PARENT_ORG_NAME;
    }

    public String getDIRECT_ORG_NAME() {
        return DIRECT_ORG_NAME;
    }

    public void setDIRECT_ORG_NAME(String DIRECT_ORG_NAME) {
        this.DIRECT_ORG_NAME = DIRECT_ORG_NAME;
    }

    public Object getSUPERVISE_ORG_NAME() {
        return SUPERVISE_ORG_NAME;
    }

    public void setSUPERVISE_ORG_NAME(Object SUPERVISE_ORG_NAME) {
        this.SUPERVISE_ORG_NAME = SUPERVISE_ORG_NAME;
    }

    public String getTEAM_ORG_NAME() {
        return TEAM_ORG_NAME;
    }

    public void setTEAM_ORG_NAME(String TEAM_ORG_NAME) {
        this.TEAM_ORG_NAME = TEAM_ORG_NAME;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMARK_PERSON() {
        return MARK_PERSON;
    }

    public void setMARK_PERSON(String MARK_PERSON) {
        this.MARK_PERSON = MARK_PERSON;
    }

    public Object getPROBLEM_TYPE() {
        return PROBLEM_TYPE;
    }

    public void setPROBLEM_TYPE(Object PROBLEM_TYPE) {
        this.PROBLEM_TYPE = PROBLEM_TYPE;
    }

    public String getLAYER_NAME() {
        return LAYER_NAME;
    }

    public void setLAYER_NAME(String LAYER_NAME) {
        this.LAYER_NAME = LAYER_NAME;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCORRECT_TYPE() {
        return CORRECT_TYPE;
    }

    public void setCORRECT_TYPE(String CORRECT_TYPE) {
        this.CORRECT_TYPE = CORRECT_TYPE;
    }

    public String getCOMPONENT_TYPE() {
        return COMPONENT_TYPE;
    }

    public void setCOMPONENT_TYPE(String COMPONENT_TYPE) {
        this.COMPONENT_TYPE = COMPONENT_TYPE;
    }
}
