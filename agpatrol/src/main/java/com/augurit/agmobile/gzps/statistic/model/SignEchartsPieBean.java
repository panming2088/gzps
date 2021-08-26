package com.augurit.agmobile.gzps.statistic.model;

import java.util.List;

/**
 * Created by luob on 2017/12/26.
 */
public class SignEchartsPieBean {

    public String type;
    public String title;
    public List<ValueData> values;
    public List<String> legends;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ValueData> getValues() {
        return values;
    }

    public void setValues(List<ValueData> values) {
        this.values = values;
    }

    public List<String> getLegends() {
        return legends;
    }

    public void setLegends(List<String> legends) {
        this.legends = legends;
    }

    public static class ValueData {
        public float value;
        public String name;

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
