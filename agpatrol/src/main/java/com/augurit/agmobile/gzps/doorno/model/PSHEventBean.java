package com.augurit.agmobile.gzps.doorno.model;

import java.util.List;

/**
 * com.augurit.agmobile.gzpssb.pshpublicaffair.model
 * Created by sdb on 2018/4/11  20:19.
 * Desc：
 */

public class PSHEventBean {

    /**
     * code : 200
     * addTotal : 7
     * checkTotal : 0
     * total : 7
     * data : [{"id":23,"markPerson":"psh12","parentOrgName":"番禺区水务局","name":null,"dischargerType1":null,"dischargerType2":null,"area":"天河区","town":"搜狗岭","addr":"天河区***jiewojqel","ownerTele":null,"state":"1","markTime":{"date":11,"day":3,"hours":16,"minutes":27,"month":3,"nanos":0,"seconds":35,"time":1523435255000,"timezoneOffset":-480,"year":118}},{"id":21,"markPerson":"排水户测试","parentOrgName":"天河区住房和建设水务局","name":null,"dischargerType1":null,"dischargerType2":null,"area":"天河区","town":"搜狗岭1","addr":"天河区***天河北","ownerTele":null,"state":"1","markTime":{"date":10,"day":2,"hours":10,"minutes":4,"month":3,"nanos":0,"seconds":6,"time":1523325846000,"timezoneOffset":-480,"year":118},"imgPath":"http://139.159.243.185:8080/img/pshFile/imgSmall/201804/20180410/jft0vk4i0_20171115200806_sewerageUser_img.jpg"},{"id":20,"markPerson":"排水户测试","parentOrgName":"天河区住房和建设水务局","name":null,"dischargerType1":null,"dischargerType2":null,"area":"天河区","town":"搜狗岭1","addr":"天河区***天河北","ownerTele":null,"state":"1","markTime":{"date":10,"day":2,"hours":9,"minutes":4,"month":3,"nanos":0,"seconds":6,"time":1523322246000,"timezoneOffset":-480,"year":118},"imgPath":"http://139.159.243.185:8080/img/pshFile/imgSmall/201804/20180409/jfs63zn30_20171115200806_sewerageUser_img.jpg"},{"id":19,"markPerson":"排水户测试","parentOrgName":"天河区住房和建设水务局","name":null,"dischargerType1":null,"dischargerType2":null,"area":"天河区","town":"搜狗岭","addr":"天河区***jiewojqel","ownerTele":null,"state":"1","markTime":{"date":9,"day":1,"hours":19,"minutes":30,"month":3,"nanos":0,"seconds":21,"time":1523273421000,"timezoneOffset":-480,"year":118},"imgPath":"http://139.159.243.185:8080/img/DischargeFile/imgSmall/201804/20180409/jfs5nwofp_20171115200806_sewerageUser_img.jpg"},{"id":18,"markPerson":"排水户测试","parentOrgName":"天河区住房和建设水务局","name":null,"dischargerType1":null,"dischargerType2":null,"area":"天河区","town":"搜狗岭","addr":"天河区***jiewojqel","ownerTele":null,"state":"1","markTime":{"date":9,"day":1,"hours":18,"minutes":2,"month":3,"nanos":0,"seconds":4,"time":1523268124000,"timezoneOffset":-480,"year":118},"imgPath":"http://139.159.243.185:8080/img/DischargeFile/imgSmall/201804/20180409/jfs2id14k_20171115200806_sewerageUser_img.jpg"},{"id":17,"markPerson":"龙杰","parentOrgName":"天河区住房和建设水务局","name":null,"dischargerType1":null,"dischargerType2":null,"area":"天河区","town":"搜狗岭","addr":"天河区***jiewojqel","ownerTele":null,"state":"1","markTime":{"date":9,"day":1,"hours":17,"minutes":42,"month":3,"nanos":0,"seconds":13,"time":1523266933000,"timezoneOffset":-480,"year":118},"imgPath":"http://139.159.243.185:8080/img/DischargeFile/imgSmall/201804/20180409/jfs1svn8f_20171115200806_sewerageUser_img.jpg"},{"id":16,"markPerson":"龙杰","parentOrgName":"天河区住房和建设水务局","name":null,"dischargerType1":null,"dischargerType2":null,"area":"天河区","town":"搜狗岭","addr":"天河区***jiewojqel","ownerTele":null,"state":"1","markTime":{"date":9,"day":1,"hours":17,"minutes":41,"month":3,"nanos":0,"seconds":4,"time":1523266864000,"timezoneOffset":-480,"year":118},"imgPath":"http://139.159.243.185:8080/img/DischargeFile/imgSmall/201804/20180409/jfs1qknua_20171115200806_sewerageUser_img.jpg"}]
     */

    private int code;
    private int addTotal;
    private int checkTotal;
    private int total;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getAddTotal() {
        return addTotal;
    }

    public void setAddTotal(int addTotal) {
        this.addTotal = addTotal;
    }

    public int getCheckTotal() {
        return checkTotal;
    }

    public void setCheckTotal(int checkTotal) {
        this.checkTotal = checkTotal;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 23
         * markPerson : psh12
         * parentOrgName : 番禺区水务局
         * name : null
         * dischargerType1 : null
         * dischargerType2 : null
         * area : 天河区
         * town : 搜狗岭
         * addr : 天河区***jiewojqel
         * ownerTele : null
         * state : 1
         * markTime : {"date":11,"day":3,"hours":16,"minutes":27,"month":3,"nanos":0,"seconds":35,"time":1523435255000,"timezoneOffset":-480,"year":118}
         * imgPath : http://139.159.243.185:8080/img/pshFile/imgSmall/201804/20180410/jft0vk4i0_20171115200806_sewerageUser_img.jpg
         */

        private int id;
        private String markPerson;
        private String parentOrgName;
        private String name;
        private String dischargerType1;
        private String dischargerType2;
        private String area;
        private String town;
        private String addr;
        private String ownerTele;
        private String state;
        private Long markTime;
        private String imgPath;

        public Long getMarkTime() {
            return markTime;
        }

        public void setMarkTime(Long markTime) {
            this.markTime = markTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMarkPerson() {
            return markPerson;
        }

        public void setMarkPerson(String markPerson) {
            this.markPerson = markPerson;
        }

        public String getParentOrgName() {
            return parentOrgName;
        }

        public void setParentOrgName(String parentOrgName) {
            this.parentOrgName = parentOrgName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDischargerType1() {
            return dischargerType1;
        }

        public void setDischargerType1(String dischargerType1) {
            this.dischargerType1 = dischargerType1;
        }

        public String getDischargerType2() {
            return dischargerType2;
        }

        public void setDischargerType2(String dischargerType2) {
            this.dischargerType2 = dischargerType2;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getOwnerTele() {
            return ownerTele;
        }

        public void setOwnerTele(String ownerTele) {
            this.ownerTele = ownerTele;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }


        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

    }
}
