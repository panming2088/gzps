package com.augurit.agmobile.patrolcore.search.view.filterview;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.search.view.filterview
 * @createTime 创建时间 ：2017/7/11
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/7/11
 * @modifyMemo 修改备注：
 */

public class FilterItem {

    private String id;
    private String field1;
    private String name;
    private String type;
    private String value;
    private LinkedHashMap<String, Object> spinnerDatas;
    private Map<String, Object> attributes;
    private List<FilterItem> children;

    public FilterItem() {
    }

    public FilterItem(String id, String field1, String name, String type, String value) {
        this.id = id;
        this.field1 = field1;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LinkedHashMap<String, Object> getSpinnerDatas() {
        return spinnerDatas;
    }

    public void setSpinnerDatas(LinkedHashMap<String, Object> spinnerDatas) {
        this.spinnerDatas = spinnerDatas;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public List<FilterItem> getChildren() {
        return children;
    }

    public void setChildren(List<FilterItem> children) {
        this.children = children;
    }
}
