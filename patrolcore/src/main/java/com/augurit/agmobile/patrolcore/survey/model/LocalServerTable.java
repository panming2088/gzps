package com.augurit.agmobile.patrolcore.survey.model;


import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 本地存储使用实体列
 * 某个项目下单个表格实体（包括表格中的记录）
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.module
 * @createTime 创建时间 ：17/8/25
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/25
 * @modifyMemo 修改备注：
 */
//Todo gkh
public class LocalServerTable extends RealmObject{


    /**
     * projectId : cfddb068-4755-4851-81b0-33f1e58e7ad7
     * tasktableName : 学生信息表
     * records : [{"name":" 岑村 辛鑫龙","recordId":"c3a3f95f-1af9-4f2b-bb41-58443d47b205","items":[{"id":"9efd109f-3163-4b7f-ad72-8553d8e81cc7","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"stu_id","field2":"stu_id","is_form_field":null,"html_name":"序号","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"","order_nm":"1","is_display":null,"is_search":null,"if_required":"A00602","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"1","brief_location":null,"layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"stu_id","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"6a79a465-a649-4406-bcc3-35ebd0747551","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"sch_name","field2":"sch_name","is_form_field":null,"html_name":"学校名称","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"岑村","order_nm":"2","is_display":null,"is_search":null,"if_required":"A00601","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"2","brief_location":"leftUp","layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"8ad314aa-5afe-44e4-a95c-9ac2afba97c6","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"class","field2":"class","is_form_field":null,"html_name":"班级","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"402","order_nm":"3","is_display":null,"is_search":null,"if_required":"A00601","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"3","brief_location":null,"layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"a044499a-6145-46a4-ae34-54dd60d8e9c6","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"name","field2":"name","is_form_field":null,"html_name":"姓名","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"辛鑫龙","order_nm":"4","is_display":null,"is_search":null,"if_required":"A00601","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"4","brief_location":"leftUp","layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"115b0e4d-542d-480f-a1ef-5ecfc2b65d01","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"pes_id","field2":"pes_id","is_form_field":null,"html_name":"身份证件号码","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"440223038384388786","order_nm":"5","is_display":null,"is_search":null,"if_required":"A00601","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"5","brief_location":null,"layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"717b1f1e-b0b2-42f6-a61d-7bdcbc61bb95","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"nation","field2":"nation","is_form_field":null,"html_name":"民族","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"","order_nm":"6","is_display":null,"is_search":null,"if_required":"A00602","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"6","brief_location":null,"layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"nation","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"6c24274f-ac16-40c7-93ac-e1e17795a47c","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"tel_num","field2":"tel_num","is_form_field":null,"html_name":"联系电话","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"","order_nm":"7","is_display":null,"is_search":null,"if_required":"A00602","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"7","brief_location":null,"layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"tel_num","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"d1a51755-9e03-48ce-aff1-ff867ca87eaa","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"time","field2":"time","is_form_field":null,"html_name":"入学时间","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007003","dic_code":null,"if_hidden":"A00601","value":"2017-8-26 10:54:00","order_nm":"8","is_display":null,"is_search":null,"if_required":"A00601","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"8","brief_location":null,"layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null},{"id":"c390d3df-ab73-4b19-8622-fc4e1656f8ed","device_id":"cfddb068-4755-4851-81b0-33f1e58e7ad7","industry_code":null,"base_table":"ag_patrol_fields","industry_table":"DEFAULT","field1":"remark","field2":"remark","is_form_field":null,"html_name":"备注","colum_num":null,"row_num":null,"is_edit":null,"validate_type":null,"regex":null,"control_type":"A007001","dic_code":null,"if_hidden":"A00601","value":"","order_nm":"9","is_display":null,"is_search":null,"if_required":"A00602","if_linkage":null,"linkage_level":null,"subkey_value":null,"pcode":null,"children_code":null,"display_order":"9","brief_location":null,"layer_id":null,"work_order":null,"first_correlation":null,"two_correlation":null,"three_correlation":"","fuzzy_search":null,"search_flag":null,"statistic_group_flag":null,"logical_field":null}]}]
     */

    /**
     * 表单名称
     */
    private String projectName; //本来应该是tableName，但是由于使用realm的时候如果有getTableName方法会报错，故而改成了projectName

    @PrimaryKey
    private String projectId;//tableId 表单id

    //列表项
    private RealmList<LocalServerTableRecord> records;

    private String taskId;

    /**
     * 该记录所在的表格类型，表明它是栋表，套表还是单位表等；类型定义在{@link LocalServerTableRecordConstant}中；
     */
    private String tableType ;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String tableName) {
        this.projectName = tableName;
    }

    public String getTableId() {
        return projectId;
    }

    public void setTableId(String tableId) {
        this.projectId = tableId;
    }


    public RealmList<LocalServerTableRecord> getRecords() {
        return records;
    }

    public void setRecords(RealmList<LocalServerTableRecord> records) {
        this.records = records;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }
}
