package com.augurit.agmobile.gzps.statistic.model;

import java.io.Serializable;
import java.util.List;

/**
 * 统计结果
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.statistic.model
 * @createTime 创建时间 ：2017/8/15
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/8/15
 * @modifyMemo 修改备注：
 */
public class StatisticResult implements Serializable {

    private String groupField;
    private String groupName;
    private String groupValue;
    private List<Result> result;

    public static class Result {
        private double count;
        private String type;
        private String typeName;

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

    public String getGroupField() {
        return groupField;
    }

    public void setGroupField(String groupField) {
        this.groupField = groupField;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupValue() {
        return groupValue;
    }

    public void setGroupValue(String groupValue) {
        this.groupValue = groupValue;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }
}
