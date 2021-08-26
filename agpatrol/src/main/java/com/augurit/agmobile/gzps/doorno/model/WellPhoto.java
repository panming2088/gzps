package com.augurit.agmobile.gzps.doorno.model;

import java.util.List;

/**
 * com.augurit.agmobile.gzpssb.seweragewell.model
 * Created by sdb on 2018/4/21  11:01.
 * Desc：
 */

public class WellPhoto {

    /**
     * rows : [{"id":"4741","thumPath":"http://139.159.243.185:8080/img/ReportFile/imgSmall/201801/jbwyyt5rr7_20180102094159_img.jpg","markId":"1307","attPath":"http://139.159.243.185:8080/img/ReportFile/img/201801/jbwyyt5rr7_20180102094159_img.jpg","attTime":1514857372000,"attName":"20180102094159_img.jpg","mime":"image/*"},{"id":"4742","thumPath":"http://139.159.243.185:8080/img/ReportFile/imgSmall/201801/jbwyytjwr8_20180102094115_img.jpg","markId":"1307","attPath":"http://139.159.243.185:8080/img/ReportFile/img/201801/jbwyytjwr8_20180102094115_img.jpg","attTime":1514857372000,"attName":"20180102094115_img.jpg","mime":"image/*"},{"id":"4743","thumPath":"http://139.159.243.185:8080/img/ReportFile/imgSmall/201801/jbwyyu0or9_20180102094144_img.jpg","markId":"1307","attPath":"http://139.159.243.185:8080/img/ReportFile/img/201801/jbwyyu0or9_20180102094144_img.jpg","attTime":1514857373000,"attName":"20180102094144_img.jpg","mime":"image/*"},{"id":"4744","thumPath":"http://139.159.243.185:8080/img/ReportFile/imgSmall/201801/jbwyyuhwra_20180102094122_img.jpg","markId":"1307","attPath":"http://139.159.243.185:8080/img/ReportFile/img/201801/jbwyyuhwra_20180102094122_img.jpg","attTime":1514857373000,"attName":"20180102094122_img.jpg","mime":"image/*"}]
     * code : 200
     */

    private int code;
    private List<RowsBean> rows;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * id : 4741
         * thumPath : http://139.159.243.185:8080/img/ReportFile/imgSmall/201801/jbwyyt5rr7_20180102094159_img.jpg
         * markId : 1307
         * attPath : http://139.159.243.185:8080/img/ReportFile/img/201801/jbwyyt5rr7_20180102094159_img.jpg
         * attTime : 1514857372000
         * attName : 20180102094159_img.jpg
         * mime : image/*
         */

        private String id;
        private String thumPath;
        private String markId;
        private String attPath;
        private long attTime;
        private String attName;
        private String mime;
        private String attType;
        public String getAttType() {
            return attType;
        }

        public void setAttType(String attType) {
            this.attType = attType;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getThumPath() {
            return thumPath;
        }

        public void setThumPath(String thumPath) {
            this.thumPath = thumPath;
        }

        public String getMarkId() {
            return markId;
        }

        public void setMarkId(String markId) {
            this.markId = markId;
        }

        public String getAttPath() {
            return attPath;
        }

        public void setAttPath(String attPath) {
            this.attPath = attPath;
        }

        public long getAttTime() {
            return attTime;
        }

        public void setAttTime(long attTime) {
            this.attTime = attTime;
        }

        public String getAttName() {
            return attName;
        }

        public void setAttName(String attName) {
            this.attName = attName;
        }

        public String getMime() {
            return mime;
        }

        public void setMime(String mime) {
            this.mime = mime;
        }
    }
}
