package com.augurit.agmobile.gzps.common.model;

import java.util.List;

/**
 *
 * 服务端返回的附件实体类
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.identification.model
 * @createTime 创建时间 ：17/11/3
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/3
 * @modifyMemo 修改备注：
 */

public class ServerAttachment {


    /**
     * code : 200
     * data : [{"attName":"20171109153955_img.jpg","attPath":"http://139.159.247.149:8080/agsupport_swj/uploadFile/uploadFile/20171109/20171109153955_img.jpg","attTime":{"date":9,"day":4,"hours":15,"minutes":39,"month":10,"nanos":0,"seconds":35,"time":1510213175000,"timezoneOffset":-480,"year":117},"id":22,"markId":"74","mime":"image/*"}]
     */

    private int code;
    private List<ServerAttachmentDataBean> data;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ServerAttachmentDataBean> getData() {
        return data;
    }

    public void setData(List<ServerAttachmentDataBean> data) {
        this.data = data;
    }

    public static class ServerAttachmentDataBean {

        /**
         * id : 128
         * thumPath :
         * markId : 204
         * attPath : http://139.159.243.185:8080/agsupport_swj/uploadFile/uploadFile/20171120/20171120102231_img.jpg
         * attTime : 1511144555000
         * attName : 20171120102231_img.jpg
         * mime : image/*
         */

        private String id;
        private String thumPath;
        private String markId;
        private String attPath;
//        private long attTime;
        private String attName;
        private String mime;

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

//        public long getAttTime() {
//            return attTime;
//        }
//
//        public void setAttTime(long attTime) {
//            this.attTime = attTime;
//        }

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
