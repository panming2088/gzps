package com.augurit.agmobile.patrolcore.common.table.model;


import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 描述：巡查表单字段定义信息(模板界面表格项)
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model
 * @createTime 创建时间 ：17/3/6
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/6
 * @modifyMemo 修改备注：
 */

public class TableItem extends RealmObject implements Serializable{
    @PrimaryKey
    private String id;
    private String featureCode; //功能类型编码
    private String device_id; //device_id 即为该TableItem所属于的 ProjectId
    private String industry_code; // 行业表
    private String industry_table; //表名
    private String base_table; //字段来源
    private String field1; // 基类模板自定义字段 前端界面控件name值(不用大小写转换)
    private String field2; // 行业表对应字段 行业字段
    private String is_form_field;
    private String html_name; // 控件名称 显示在前端控件前的名称
    private String colum_num; //所占列数
    private String row_num;
    private String is_edit; //是否可编辑 默认可编辑 若为A006001为不可编辑
    private String validate_type; // 验证规则
    private String regex;
    private String control_type;  // 控件类型 对应控件type
    private String dic_code; // 数据字典编码集类型 对应控件下拉框值
                                //DictionaryItem -> typeCode
                                //TableChildItem -> typeCode

    private String if_hidden; // ControlState
    private String value;
    private String if_required; //是否是必填项  RequireState
    private String children_code; //级联下一表格项的 dic_code
    private int display_order; //排序表格项 值越小越在前 不能为空

    private String first_correlation; //模糊匹配的Url
    private String three_correlation;//模糊搜索字
    private String is_search;   // 是否为关键字搜索字段候选

    public String getFirst_correlation() {
        return first_correlation;
    }

    public void setFirst_correlation(String first_correlation) {
        this.first_correlation = first_correlation;
    }

    public String getChildren_code() {
        return children_code;
    }

    public void setChildren_code(String children_code) {
        this.children_code = children_code;
    }

    public int getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(int display_order) {
        this.display_order = display_order;
    }

    public String getIf_required() {
        return if_required;
    }

    public void setIf_required(String if_required) {
        this.if_required = if_required;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getIndustry_code() {
        return industry_code;
    }

    public void setIndustry_code(String industry_code) {
        this.industry_code = industry_code;
    }

    public String getIndustry_table() {
        return industry_table;
    }

    public void setIndustry_table(String industry_table) {
        this.industry_table = industry_table;
    }

    public String getBase_table() {
        return base_table;
    }

    public void setBase_table(String base_table) {
        this.base_table = base_table;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getIs_form_field() {
        return is_form_field;
    }

    public void setIs_form_field(String is_form_field) {
        this.is_form_field = is_form_field;
    }

    public String getHtml_name() {
        return html_name;
    }

    public void setHtml_name(String html_name) {
        this.html_name = html_name;
    }

    public String getColum_num() {
        return colum_num;
    }

    public void setColum_num(String colum_num) {
        this.colum_num = colum_num;
    }

    public String getRow_num() {
        return row_num;
    }

    public void setRow_num(String row_num) {
        this.row_num = row_num;
    }

    public String getIs_edit() {
        return is_edit;
    }

    public void setIs_edit(String id_edit) {
        this.is_edit = id_edit;
    }

    public String getValidate_type() {
        return validate_type;
    }

    public void setValidate_type(String validate_type) {
        this.validate_type = validate_type;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getControl_type() {
        return control_type;
    }

    public void setControl_type(String control_type) {
        this.control_type = control_type;
    }

    public String getDic_code() {
        return dic_code;
    }

    public void setDic_code(String dic_code) {
        this.dic_code = dic_code;
    }

    public String getIf_hidden() {
        return if_hidden;
    }

    public void setIf_hidden(String if_hidden) {
        this.if_hidden = if_hidden;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getThree_correlation() {
        return three_correlation;
    }

    public void setThree_correlation(String three_correlation) {
        this.three_correlation = three_correlation;
    }

    public String getIs_search() {
        return is_search;
    }

    public void setIs_search(String is_search) {
        this.is_search = is_search;
    }
}

