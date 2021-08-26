package com.augurit.agmobile.gzps.publicaffair.model;

import com.augurit.agmobile.gzps.uploadevent.model.EventDetail;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liangsh on 2017/11/17.
 */

public class EventAffair implements Serializable{


    /**
     * handling : 32
     * finished : 8
     * list : [{"bhlx":"","bz":"","code":"","directOrgId":"1081","directOrgName":"黄埔区市政建设有限公司","files":[],"fjzd":"","id":985,"jdmc":"","jjcd":"","layerId":0,"layerName":"","layerurl":"","objectId":"","parentOrgId":"1068","parentOrgName":"黄埔区水务局","reportaddr":"","reportx":"","reporty":"","sbr":"测试1","sbsj":{"date":15,"day":3,"hours":15,"minutes":59,"month":10,"nanos":0,"seconds":6,"time":1510732746000,"timezoneOffset":-480,"year":117},"sslx":"","superviseOrgId":"","superviseOrgName":"","szwz":"","teamOrgId":"1125","teamOrgName":"巡查组","usid":"","wtms":"","x":"","y":"","yjgcl":""},{"bhlx":"","bz":"","code":"","directOrgId":"1081","directOrgName":"黄埔区市政建设有限公司","files":[],"fjzd":"","id":986,"jdmc":"","jjcd":"","layerId":0,"layerName":"","layerurl":"","objectId":"","parentOrgId":"1068","parentOrgName":"黄埔区水务局","reportaddr":"","reportx":"","reporty":"","sbr":"了解","sbsj":{"date":15,"day":3,"hours":16,"minutes":1,"month":10,"nanos":0,"seconds":30,"time":1510732890000,"timezoneOffset":-480,"year":117},"sslx":"","superviseOrgId":"","superviseOrgName":"","szwz":"","teamOrgId":"1125","teamOrgName":"巡查组","usid":"","wtms":"","x":"","y":"","yjgcl":""},{"bhlx":"","bz":"","code":"","directOrgId":"1081","directOrgName":"黄埔区市政建设有限公司","files":[],"fjzd":"","id":987,"jdmc":"","jjcd":"","layerId":0,"layerName":"","layerurl":"","objectId":"","parentOrgId":"1068","parentOrgName":"黄埔区水务局","reportaddr":"","reportx":"","reporty":"","sbr":"444","sbsj":{"date":15,"day":3,"hours":16,"minutes":1,"month":10,"nanos":0,"seconds":36,"time":1510732896000,"timezoneOffset":-480,"year":117},"sslx":"","superviseOrgId":"","superviseOrgName":"","szwz":"","teamOrgId":"1125","teamOrgName":"巡查组","usid":"","wtms":"","x":"","y":"","yjgcl":""},{"bhlx":"","bz":"","code":"","directOrgId":"1081","directOrgName":"黄埔区市政建设有限公司","files":[],"fjzd":"","id":988,"jdmc":"","jjcd":"","layerId":0,"layerName":"","layerurl":"","objectId":"","parentOrgId":"1068","parentOrgName":"黄埔区水务局","reportaddr":"","reportx":"","reporty":"","sbr":"测试","sbsj":{"date":15,"day":3,"hours":16,"minutes":1,"month":10,"nanos":0,"seconds":44,"time":1510732904000,"timezoneOffset":-480,"year":117},"sslx":"","superviseOrgId":"","superviseOrgName":"","szwz":"","teamOrgId":"1125","teamOrgName":"巡查组","usid":"","wtms":"","x":"","y":"","yjgcl":""},{"bhlx":"","bz":"","code":"","directOrgId":"1081","directOrgName":"黄埔区市政建设有限公司","files":[],"fjzd":"","id":989,"jdmc":"","jjcd":"","layerId":0,"layerName":"","layerurl":"","objectId":"","parentOrgId":"1068","parentOrgName":"黄埔区水务局","reportaddr":"","reportx":"","reporty":"","sbr":"测试2017115","sbsj":{"date":15,"day":3,"hours":16,"minutes":20,"month":10,"nanos":0,"seconds":2,"time":1510734002000,"timezoneOffset":-480,"year":117},"sslx":"","superviseOrgId":"","superviseOrgName":"","szwz":"","teamOrgId":"1125","teamOrgName":"巡查组","usid":"","wtms":"","x":"","y":"","yjgcl":""},{"bhlx":"","bz":"1234","code":"111","directOrgId":"1081","directOrgName":"黄埔区市政建设有限公司","files":[],"fjzd":"","id":990,"jdmc":"123","jjcd":"2","layerId":0,"layerName":"","layerurl":"","objectId":"","parentOrgId":"1068","parentOrgName":"黄埔区水务局","reportaddr":"","reportx":"","reporty":"","sbr":"奥格","sbsj":{"date":15,"day":3,"hours":16,"minutes":57,"month":10,"nanos":0,"seconds":1,"time":1510736221000,"timezoneOffset":-480,"year":117},"sslx":"1","superviseOrgId":"","superviseOrgName":"","szwz":"","teamOrgId":"1125","teamOrgName":"巡查组","usid":"","wtms":"12","x":"","y":"","yjgcl":"12"},{"bhlx":"","bz":"","code":"","directOrgId":"1081","directOrgName":"黄埔区市政建设有限公司","files":[],"fjzd":"","id":991,"jdmc":"","jjcd":"","layerId":0,"layerName":"","layerurl":"","objectId":"","parentOrgId":"1068","parentOrgName":"黄埔区水务局","reportaddr":"","reportx":"","reporty":"","sbr":"测试","sbsj":{"date":15,"day":3,"hours":17,"minutes":13,"month":10,"nanos":0,"seconds":41,"time":1510737221000,"timezoneOffset":-480,"year":117},"sslx":"","superviseOrgId":"","superviseOrgName":"","szwz":"","teamOrgId":"1125","teamOrgName":"巡查组","usid":"","wtms":"","x":"","y":"","yjgcl":""},{"bhlx":"","bz":"","code":"","directOrgId":"1081","directOrgName":"黄埔区市政建设有限公司","files":[],"fjzd":"","id":992,"jdmc":"","jjcd":"","layerId":0,"layerName":"","layerurl":"","objectId":"","parentOrgId":"1068","parentOrgName":"黄埔区水务局","reportaddr":"","reportx":"","reporty":"","sbr":"奥格4","sbsj":{"date":15,"day":3,"hours":17,"minutes":32,"month":10,"nanos":0,"seconds":23,"time":1510738343000,"timezoneOffset":-480,"year":117},"sslx":"","superviseOrgId":"","superviseOrgName":"","szwz":"","teamOrgId":"1125","teamOrgName":"巡查组","usid":"","wtms":"","x":"","y":"","yjgcl":""}]
     */

    private int handling;
    private int finished;
    private List<EventAffairBean> list;

    public int getHandling() {
        return handling;
    }

    public void setHandling(int handling) {
        this.handling = handling;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public List<EventAffairBean> getList() {
        return list;
    }

    public void setList(List<EventAffairBean> list) {
        this.list = list;
    }

    public static class EventAffairBean implements Serializable{
        /**
         * bhlx :
         * bz :
         * code :
         * directOrgId : 1081
         * directOrgName : 黄埔区市政建设有限公司
         * files : []
         * fjzd :
         * id : 985
         * jdmc :
         * jjcd :
         * layerId : 0
         * layerName :
         * layerurl :
         * objectId :
         * parentOrgId : 1068
         * parentOrgName : 黄埔区水务局
         * reportaddr :
         * reportx :
         * reporty :
         * sbr : 测试1
         * sbsj : {"date":15,"day":3,"hours":15,"minutes":59,"month":10,"nanos":0,"seconds":6,"time":1510732746000,"timezoneOffset":-480,"year":117}
         * sslx :
         * superviseOrgId :
         * superviseOrgName :
         * szwz :
         * teamOrgId : 1125
         * teamOrgName : 巡查组
         * usid :
         * wtms :
         * x :
         * y :
         * yjgcl :
         */

        private String bhlx;
        private String bz;
        private String code;
        private String directOrgId;
        private String directOrgName;
        private String fjzd;
        private int id;
        private String jdmc;
        private String jjcd;
        private int layerId;
        private String layerName;
        private String layerurl;
        private String objectId;
        private String parentOrgId;
        private String parentOrgName;
        private String reportaddr;
        private String reportx;
        private String reporty;
        private String sbr;
//        private SbsjBean sbsj;
        private long sbsj2;
        private String sslx;
        private String superviseOrgId;
        private String superviseOrgName;
        private String szwz;
        private String teamOrgId;
        private String teamOrgName;
        private String usid;
        private String wtms;
        private String x;
        private String y;
        private String isbyself;
        private String yjgcl;
        private String state;     //事件状态：处理中active，  ended
        private String sfjb;      //是否交办
        private Long yjwcsj2;      //预计处理完成时间
        private List<EventDetail.EventBean.FilesBean> files2;
        private String blqx;
        private String colour;


        public String getBhlx() {
            return bhlx;
        }

        public void setBhlx(String bhlx) {
            this.bhlx = bhlx;
        }

        public String getBz() {
            return bz;
        }

        public void setBz(String bz) {
            this.bz = bz;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDirectOrgId() {
            return directOrgId;
        }

        public void setDirectOrgId(String directOrgId) {
            this.directOrgId = directOrgId;
        }

        public String getDirectOrgName() {
            return directOrgName;
        }

        public void setDirectOrgName(String directOrgName) {
            this.directOrgName = directOrgName;
        }

        public String getFjzd() {
            return fjzd;
        }

        public void setFjzd(String fjzd) {
            this.fjzd = fjzd;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getJdmc() {
            return jdmc;
        }

        public void setJdmc(String jdmc) {
            this.jdmc = jdmc;
        }

        public String getJjcd() {
            return jjcd;
        }

        public void setJjcd(String jjcd) {
            this.jjcd = jjcd;
        }

        public int getLayerId() {
            return layerId;
        }

        public void setLayerId(int layerId) {
            this.layerId = layerId;
        }

        public String getLayerName() {
            return layerName;
        }

        public void setLayerName(String layerName) {
            this.layerName = layerName;
        }

        public String getLayerurl() {
            return layerurl;
        }

        public void setLayerurl(String layerurl) {
            this.layerurl = layerurl;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getParentOrgId() {
            return parentOrgId;
        }

        public void setParentOrgId(String parentOrgId) {
            this.parentOrgId = parentOrgId;
        }

        public String getParentOrgName() {
            return parentOrgName;
        }

        public void setParentOrgName(String parentOrgName) {
            this.parentOrgName = parentOrgName;
        }

        public String getReportaddr() {
            return reportaddr;
        }

        public void setReportaddr(String reportaddr) {
            this.reportaddr = reportaddr;
        }

        public String getReportx() {
            return reportx;
        }

        public void setReportx(String reportx) {
            this.reportx = reportx;
        }

        public String getReporty() {
            return reporty;
        }

        public void setReporty(String reporty) {
            this.reporty = reporty;
        }

        public String getSbr() {
            return sbr;
        }

        public void setSbr(String sbr) {
            this.sbr = sbr;
        }

        public String getSslx() {
            return sslx;
        }

        public void setSslx(String sslx) {
            this.sslx = sslx;
        }

        public String getSuperviseOrgId() {
            return superviseOrgId;
        }

        public void setSuperviseOrgId(String superviseOrgId) {
            this.superviseOrgId = superviseOrgId;
        }

        public String getSuperviseOrgName() {
            return superviseOrgName;
        }

        public void setSuperviseOrgName(String superviseOrgName) {
            this.superviseOrgName = superviseOrgName;
        }

        public String getSzwz() {
            return szwz;
        }

        public void setSzwz(String szwz) {
            this.szwz = szwz;
        }

        public String getTeamOrgId() {
            return teamOrgId;
        }

        public void setTeamOrgId(String teamOrgId) {
            this.teamOrgId = teamOrgId;
        }

        public String getTeamOrgName() {
            return teamOrgName;
        }

        public void setTeamOrgName(String teamOrgName) {
            this.teamOrgName = teamOrgName;
        }

        public String getUsid() {
            return usid;
        }

        public void setUsid(String usid) {
            this.usid = usid;
        }

        public String getWtms() {
            return wtms;
        }

        public void setWtms(String wtms) {
            this.wtms = wtms;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

        public String getIsbyself() {
            return isbyself;
        }

        public void setIsbyself(String isbyself) {
            this.isbyself = isbyself;
        }

        public String getYjgcl() {
            return yjgcl;
        }

        public void setYjgcl(String yjgcl) {
            this.yjgcl = yjgcl;
        }

        public long getSbsj2() {
            return sbsj2;
        }

        public void setSbsj2(long sbsj2) {
            this.sbsj2 = sbsj2;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public List<EventDetail.EventBean.FilesBean> getFiles2() {
            return files2;
        }

        public void setFiles2(List<EventDetail.EventBean.FilesBean> files2) {
            this.files2 = files2;
        }

        public String getSfjb() {
            return sfjb;
        }

        public void setSfjb(String sfjb) {
            this.sfjb = sfjb;
        }

        public Long getYjwcsj2() {
            return yjwcsj2;
        }

        public void setYjwcsj2(Long yjwcsj2) {
            this.yjwcsj2 = yjwcsj2;
        }

        public List<EventDetail.EventBean.FilesBean > getFiles() {
            return files2;
        }

        public void setFiles(List<EventDetail.EventBean.FilesBean > files) {
            this.files2 = files;
        }

        public String getBlqx() {
            return blqx;
        }

        public void setBlqx(String blqx) {
            this.blqx = blqx;
        }

        public String getColour() {
            return colour;
        }

        public void setColour(String colour) {
            this.colour = colour;
        }

        public static class SbsjBean implements Serializable{
            /**
             * date : 15
             * day : 3
             * hours : 15
             * minutes : 59
             * month : 10
             * nanos : 0
             * seconds : 6
             * time : 1510732746000
             * timezoneOffset : -480
             * year : 117
             */

            private int date;
            private int day;
            private int hours;
            private int minutes;
            private int month;
            private int nanos;
            private int seconds;
            private long time;
            private int timezoneOffset;
            private int year;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getNanos() {
                return nanos;
            }

            public void setNanos(int nanos) {
                this.nanos = nanos;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }
        }
    }
}
