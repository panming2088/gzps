package com.augurit.agmobile.patrolcore.survey.model;

import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.annotations.PrimaryKey;

/**
 * 单个记录
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.module
 * @createTime 创建时间 ：17/8/23
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/23
 * @modifyMemo 修改备注：
 */

public class ServerTableRecord implements Serializable {



    /**
     * name :  岑村 辛鑫龙
     * recordId : c3a3f95f-1af9-4f2b-bb41-58443d47b205
     * items : [{"id":"9efd109f-3163-4b7f-ad72-8553d8e81cc7","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"stu_id","field2":"stu_id","is_form_field":null,"html_name":"序号","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"","order_nm":"1","is_display":null,"is_search":null,"if_required":"A00602","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"1","brief_location":null,"layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"stu_id","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"6a79a465-a649-4406-bcc3-35ebd0747551","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"sch_name","field2":"sch_name","is_form_field":null,"html_name":"学校名称","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"岑村","order_nm":"2","is_display":null,"is_search":null,"if_required":"A00601","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"2","brief_location":"leftUp","layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"8ad314aa-5afe-44e4-a95c-9ac2afba97c6","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"class","field2":"class","is_form_field":null,"html_name":"班级","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"402","order_nm":"3","is_display":null,"is_search":null,"if_required":"A00601","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"3","brief_location":null,"layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"a044499a-6145-46a4-ae34-54dd60d8e9c6","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"name","field2":"name","is_form_field":null,"html_name":"姓名","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"辛鑫龙","order_nm":"4","is_display":null,"is_search":null,"if_required":"A00601","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"4","brief_location":"leftUp","layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"115b0e4d-542d-480f-a1ef-5ecfc2b65d01","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"pes_id","field2":"pes_id","is_form_field":null,"html_name":"身份证件号码","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"440223038384388786","order_nm":"5","is_display":null,"is_search":null,"if_required":"A00601","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"5","brief_location":null,"layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"717b1f1e-b0b2-42f6-a61d-7bdcbc61bb95","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"nation","field2":"nation","is_form_field":null,"html_name":"民族","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"","order_nm":"6","is_display":null,"is_search":null,"if_required":"A00602","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"6","brief_location":null,"layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"nation","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"6c24274f-ac16-40c7-93ac-e1e17795a47c","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"tel_num","field2":"tel_num","is_form_field":null,"html_name":"联系电话","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"","order_nm":"7","is_display":null,"is_search":null,"if_required":"A00602","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"7","brief_location":null,"layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"tel_num","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"d1a51755-9e03-48ce-aff1-ff867ca87eaa","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"time","field2":"time","is_form_field":null,"html_name":"入学时间","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007003","dic_code":null,"if_hidden":"A00601","value":"2017-8-26 10:54:00","order_nm":"8","is_display":null,"is_search":null,"if_required":"A00601","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"8","brief_location":null,"layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"c390d3df-ab73-4b19-8622-fc4e1656f8ed","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"remark","field2":"remark","is_form_field":null,"html_name":"备注","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"","order_nm":"9","is_display":null,"is_search":null,"if_required":"A00602","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"9","brief_location":null,"layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null}]
     */

    private String dongId; //栋的id

    private String dongName ; //栋的名称

    private String taoId; //套的id

    private String taoName ; //套的名称

    @SerializedName("u_id")
    private String danweiId; //单位id

    private String taskId; //任务id

    private String taskName;//记录所属的任务名

    private String tableId;

    private String projectName;//表名

    private String tableKey;//LocalTable 中的key 本地编辑而且保存过则由值

    private String name;

    @PrimaryKey
    private String recordId;

    private String dirId; //目录id


    @Deprecated
    private String ifHasTao ; //该字段已经废弃使用，服务端已经不返回该字段；  是否有套列表  1 表示有 ； 其他任何数值表示没有

    private String renkouType ; //人口类型 1表示"本市户籍";  2 表示"境外人员" ; 3表示"流动人员"

    private ServerTableRecord jingwaiOrLiuDongRecord ;//如果是境外人员或者流动人员，这里表示境外或者流动的记录

    private String jingWaiOrLiuDongTableid; //境外人口或者流动人口表的id

    private String jingWaiOrLiuDongTableName ; //境外人口或者流动人口表名

    //表格内容
    private ArrayList<TableItem> items;

    private String standardaddress;

    @SerializedName("data_stata")
    private String state = "0"; //待审核状态

    private String lastModifyTime ; //上次修改的时间

    public String getJingWaiOrLiuDongTableName() {
        return jingWaiOrLiuDongTableName;
    }

    public void setJingWaiOrLiuDongTableName(String jingWaiOrLiuDongTableName) {
        this.jingWaiOrLiuDongTableName = jingWaiOrLiuDongTableName;
    }

    public String getJingWaiOrLiuDongTableid() {
        return jingWaiOrLiuDongTableid;
    }

    public void setJingWaiOrLiuDongTableid(String jingWaiOrLiuDongTableid) {
        this.jingWaiOrLiuDongTableid = jingWaiOrLiuDongTableid;
    }

    public ServerTableRecord getJingwaiOrLiuDongRecord() {
        return jingwaiOrLiuDongRecord;
    }

    public void setJingwaiOrLiuDongRecord(ServerTableRecord jingwaiOrLiuDongRecord) {
        this.jingwaiOrLiuDongRecord = jingwaiOrLiuDongRecord;
    }

    public String getRenkouType() {
        return renkouType;
    }

    public void setRenkouType(String renkouType) {
        this.renkouType = renkouType;
    }

    public String isIfHasTao() {
        return ifHasTao;
    }

    public void setIfHasTao(String ifHasTao) {
        this.ifHasTao = ifHasTao;
    }

    public String getDirId() {
        return dirId;
    }

    public void setDirId(String dirId) {
        this.dirId = dirId;
    }



    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTableKey() {
        return tableKey;
    }

    public void setTableKey(String tableKey) {
        this.tableKey = tableKey;
    }

    public ArrayList<TableItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<TableItem> items) {
        this.items = items;
    }


    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDongId() {
        return dongId;
    }

    public void setDongId(String dongId) {
        this.dongId = dongId;
    }

    public String getDongName() {
        return dongName;
    }

    public void setDongName(String dongName) {
        this.dongName = dongName;
    }

    public String getStandardaddress() {
        return standardaddress;
    }

    public void setStandardaddress(String standardaddress) {
        this.standardaddress = standardaddress;
    }

    public String getTaoId() {
        return taoId;
    }

    public void setTaoId(String taoId) {
        this.taoId = taoId;
    }

    public String getTaoName() {
        return taoName;
    }

    public void setTaoName(String taoName) {
        this.taoName = taoName;
    }

    public String getDanweiId() {
        return danweiId;
    }

    public void setDanweiId(String danweiId) {
        this.danweiId = danweiId;
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
