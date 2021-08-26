package com.augurit.agmobile.gzps.doorno.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaoyu on 2018/4/9.
 */

public class SewerageItemBean implements Serializable{


    /**
     * address : 广东省广州市海珠区镜湖西街23号
     * smallAddress : 海珠区镜湖西街23号
     * jlx : 镜湖西街
     * jwh : 保利社区
     * xzj : 南石头街
     * xzq : 海珠区
     * house_list : [{"address":"广东省广州市海珠区镜湖西街23号101铺","buildArea":50,"doorplateId":"1f3c8304-9dae-11e7-9149-38bc019d7a82","extSystemid":"PCS3707201711090000000348264179","gridCode":"440105-011-119-008","houseCategory":"unit_apartment_buliding","houseProperty":"store","houseType":"0","housebuildId":"9f792aed-9daa-11e7-9149-38bc019d7a82","id":"81e2f4dc-c658-11e7-988f-38bc019d7a82","popList":[],"realUseWay":"0","roomNo":"101铺","unitList":[],"useWay":"01"},{"address":"广东省广州市海珠区镜湖西街23号102铺","buildArea":60,"doorplateId":"1f3c8304-9dae-11e7-9149-38bc019d7a82","extSystemid":"PCS3707201711090000000348264181","gridCode":"440105-011-119-008","houseCategory":"unit_apartment_buliding","houseProperty":"store","houseType":"0","housebuildId":"9f792aed-9daa-11e7-9149-38bc019d7a82","id":"81e2f813-c658-11e7-988f-38bc019d7a82","popList":[],"realUseWay":"0","roomNo":"102铺","unitList":[],"useWay":"01"},{"address":"广东省广州市海珠区镜湖西街23号103铺","buildArea":80,"doorplateId":"1f3c8304-9dae-11e7-9149-38bc019d7a82","extSystemid":"PCS3707201711090000000348264203","gridCode":"440105-011-119-008","houseCategory":"unit_apartment_buliding","houseProperty":"store","houseType":"12","housebuildId":"9f792aed-9daa-11e7-9149-38bc019d7a82","id":"81e3152b-c658-11e7-988f-38bc019d7a82","popList":[],"realUseWay":"20","roomNo":"103铺","unitList":[],"useWay":"03"},{"address":"广东省广州市海珠区镜湖西街23号1002房","buildArea":0,"doorplateId":"1f3c8304-9dae-11e7-9149-38bc019d7a82","extSystemid":"GZPCS3707201304220000000011916534","gridCode":"440105-011-119-008","houseCategory":"","houseProperty":"","houseType":"","housebuildId":"9f792aed-9daa-11e7-9149-38bc019d7a82","id":"9b235729-9daf-11e7-9149-38bc019d7a82","popList":[],"realUseWay":"","roomNo":"1002房","unitList":[],"useWay":""},{"address":"广东省广州市海珠区镜湖西街23号101房","buildArea":0,"doorplateId":"1f3c8304-9dae-11e7-9149-38bc019d7a82","extSystemid":"GZPCS3707201304220000000011916535","gridCode":"440105-011-119-008","houseCategory":"","houseProperty":"0","houseType":"","housebuildId":"9f792aed-9daa-11e7-9149-38bc019d7a82","id":"9b2357d5-9daf-11e7-9149-38bc019d7a82","popList":[],"realUseWay":"","roomNo":"101房","unitList":[],"useWay":""},{"address":"广东省广州市海珠区镜湖西街23号102房","buildArea":0,"doorplateId":"1f3c8304-9dae-11e7-9149-38bc019d7a82","extSystemid":"GZPCS3707201304220000000011916536","gridCode":"440105-011-119-008","houseCategory":"","houseProperty":"3","houseType":"","housebuildId":"9f792aed-9daa-11e7-9149-38bc019d7a82","id":"9b2358a1-9daf-11e7-9149-38bc019d7a82","popList":[],"realUseWay":"","roomNo":"102房","unitList":[],"useWay":""},{"address":"广东省广州市海珠区镜湖西街23号701房","buildArea":0,"doorplateId":"1f3c8304-9dae-11e7-9149-38bc019d7a82","extSystemid":"GZPCS3707201304220000000011917017","gridCode":"440105-011-119-008","houseCategory":"","houseProperty":"","houseType":"","housebuildId":"9f792aed-9daa-11e7-9149-38bc019d7a82","id":"9b24cf0b-9daf-11e7-9149-38bc019d7a82","popList":[],"realUseWay":"","roomNo":"701房","unitList":[],"useWay":""},{"address":"广东省广州市海珠区镜湖西街23号702房","buildArea":0,"doorplateId":"1f3c8304-9dae-11e7-9149-38bc019d7a82","extSystemid":"GZPCS3707201304220000000011917018","gridCode":"440105-011-119-008","houseCategory":"","houseProperty":"","houseType":"","housebuildId":"9f792aed-9daa-11e7-9149-38bc019d7a82","id":"9b24d046-9daf-11e7-9149-38bc019d7a82","popList":[],"realUseWay":"","roomNo":"702房","unitList":[],"useWay":""},{"address":"广东省广州市海珠区镜湖西街23号802房","buildArea":0,"doorplateId":"1f3c8304-9dae-11e7-9149-38bc019d7a82","extSystemid":"GZPCS3707201304220000000011917019","gridCode":"440105-011-119-008","houseCategory":"","houseProperty":"","houseType":"","housebuildId":"9f792aed-9daa-11e7-9149-38bc019d7a82","id":"9b24d11a-9daf-11e7-9149-38bc019d7a82","popList":[],"realUseWay":"","roomNo":"802房","unitList":[],"useWay":""},{"address":"广东省广州市海珠区镜湖西街23号803房","buildArea":0,"doorplateId":"1f3c8304-9dae-11e7-9149-38bc019d7a82","extSystemid":"GZPCS3707201304220000000011917020","gridCode":"440105-011-119-008","houseCategory":"","houseProperty":"","houseType":"","housebuildId":"9f792aed-9daa-11e7-9149-38bc019d7a82","id":"9b24d1cf-9daf-11e7-9149-38bc019d7a82","popList":[],"realUseWay":"","roomNo":"803房","unitList":[],"useWay":""}]
     * house_total : 47
     * unit_list : [{"area":"","areaCode":"","businessScope":"","economicType":"100","houseId":"81e2f4dc-c658-11e7-988f-38bc019d7a82","id":14109,"industryType":"R","isexist":0,"isrecorded":1,"legalRepresent":"谭启雄","name":"广州市中通货运代理有限公司速递","registerStatu":"","socialCreditCode":"440105000033124？","status":"1","unitId":"5750ebb9-4b7d-44d2-80dd-39228924bd41","unitType":"enterprise"},{"area":"","areaCode":"","businessScope":"","economicType":"100","houseId":"81e2f813-c658-11e7-988f-38bc019d7a82","id":11290,"industryType":"R","isexist":0,"isrecorded":1,"legalRepresent":"吴雨婷","name":"萌宠汇","registerStatu":"","socialCreditCode":"无牌","status":"1","unitId":"215e389b-40f5-4007-828c-7b76acd6140e","unitType":"enterprise"},{"area":"","areaCode":"","businessScope":"","economicType":"100","houseId":"81e3152b-c658-11e7-988f-38bc019d7a82","id":63997,"industryType":"R","isexist":0,"isrecorded":1,"legalRepresent":"方燕玲","name":"来书鹏艺术中心","registerStatu":"","socialCreditCode":"无牌","status":"1","unitId":"b5cf4435-1f9b-4187-807d-b5303ed5f720","unitType":"enterprise"},{"area":"","areaCode":"","businessScope":"","economicType":"","houseId":"81e3152b-c658-11e7-988f-38bc019d7a82","id":74151,"industryType":"","isexist":0,"isrecorded":1,"legalRepresent":"蔡职雄","name":"广州市海珠区姿整美发美容店","registerStatu":"","socialCreditCode":"无","status":"1","unitId":"5BA203214AA7CEF0E05010AC494A7C24","unitType":"individual_business"}]
     * unit_total : 4
     * unit_survey : 0
     * unit_no_survey : 4
     * pop_list : [{"houseId":"9ed948fe-9daf-11e7-9149-38bc019d7a82","id":1286340,"isexist":0,"isrecorded":1,"name":"郭展宏","populationId":"GUANGZHOU0000001614666401","populationType":"resident_population"},{"houseId":"9b23ff24-9daf-11e7-9149-38bc019d7a82","id":1918881,"isexist":0,"isrecorded":1,"name":"李振韬","populationId":"GUANGZHOU0000001630440122","populationType":"resident_population"},{"houseId":"9b24d276-9daf-11e7-9149-38bc019d7a82","id":2823431,"isexist":0,"isrecorded":1,"name":"林均堂","populationId":"GUANGZHOU0000001682207111","populationType":"resident_population"},{"houseId":"9b24d276-9daf-11e7-9149-38bc019d7a82","id":2819922,"isexist":0,"isrecorded":1,"name":"汤恩惠","populationId":"GUANGZHOU0000001631022943","populationType":"resident_population"},{"houseId":"9b2465e4-9daf-11e7-9149-38bc019d7a82","id":2903626,"isexist":0,"isrecorded":1,"name":"钟惠珍","populationId":"GUANGZHOU0000001655401221","populationType":"resident_population"},{"houseId":"9b243953-9daf-11e7-9149-38bc019d7a82","id":3054659,"isexist":0,"isrecorded":1,"name":"凌润垣","populationId":"GUANGZHOU0000001704112986","populationType":"resident_population"},{"houseId":"9b23ffe7-9daf-11e7-9149-38bc019d7a82","id":3958924,"isexist":0,"isrecorded":1,"name":"林浩","populationId":"GUANGZHOU0000001706649939","populationType":"resident_population"},{"houseId":"9b24d046-9daf-11e7-9149-38bc019d7a82","id":4054011,"isexist":0,"isrecorded":1,"name":"蒲蜀湘","populationId":"GUANGZHOU0000001640521312","populationType":"resident_population"},{"houseId":"9f0ec4d7-9daf-11e7-9149-38bc019d7a82","id":4092138,"isexist":0,"isrecorded":1,"name":"王小惠","populationId":"GUANGZHOU0000001675664161","populationType":"resident_population"},{"houseId":"9e1810d6-9daf-11e7-9149-38bc019d7a82","id":4092142,"isexist":0,"isrecorded":1,"name":"谢辉","populationId":"GUANGZHOU0000001676075957","populationType":"resident_population"}]
     * person_total : 153
     * person_survey : 0
     * person_no_survey : 153
     * result_code : 200
     * result_msg : success
     */

    private String address;
    private String smallAddress;
    private String jlx;
    private String jwh;
    private String xzj;
    private String xzq;

   private String mpwzhm;

   private String mpdzmc;
    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getBuildId() {
        return buildId;
    }

    private String buildId;
    private int house_total;
    private int unit_total;
    private int unit_survey;
    private int unit_no_survey;
    private int person_total;
    private int person_survey;
    private int person_no_survey;
    private int result_code;
    private String result_msg;
    private List<HouseListBean> house_list;
    private List<UnitListBean> unit_list;
    private List<PopListBean> pop_list;

    public String getMpwzhm() {
        return mpwzhm;
    }

    public void setMpwzhm(String mpwzhm) {
        this.mpwzhm = mpwzhm;
    }

    public String getMpdzmc() {
        return mpdzmc;
    }

    public void setMpdzmc(String mpdzmc) {
        this.mpdzmc = mpdzmc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSmallAddress() {
        return smallAddress;
    }

    public void setSmallAddress(String smallAddress) {
        this.smallAddress = smallAddress;
    }

    public String getJlx() {
        return jlx;
    }

    public void setJlx(String jlx) {
        this.jlx = jlx;
    }

    public String getJwh() {
        return jwh;
    }

    public void setJwh(String jwh) {
        this.jwh = jwh;
    }

    public String getXzj() {
        return xzj;
    }

    public void setXzj(String xzj) {
        this.xzj = xzj;
    }

    public String getXzq() {
        return xzq;
    }

    public void setXzq(String xzq) {
        this.xzq = xzq;
    }

    public int getHouse_total() {
        return house_total;
    }

    public void setHouse_total(int house_total) {
        this.house_total = house_total;
    }

    public int getUnit_total() {
        return unit_total;
    }

    public void setUnit_total(int unit_total) {
        this.unit_total = unit_total;
    }

    public int getUnit_survey() {
        return unit_survey;
    }

    public void setUnit_survey(int unit_survey) {
        this.unit_survey = unit_survey;
    }

    public int getUnit_no_survey() {
        return unit_no_survey;
    }

    public void setUnit_no_survey(int unit_no_survey) {
        this.unit_no_survey = unit_no_survey;
    }

    public int getPerson_total() {
        return person_total;
    }

    public void setPerson_total(int person_total) {
        this.person_total = person_total;
    }

    public int getPerson_survey() {
        return person_survey;
    }

    public void setPerson_survey(int person_survey) {
        this.person_survey = person_survey;
    }

    public int getPerson_no_survey() {
        return person_no_survey;
    }

    public void setPerson_no_survey(int person_no_survey) {
        this.person_no_survey = person_no_survey;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public List<HouseListBean> getHouse_list() {
        return house_list;
    }

    public void setHouse_list(List<HouseListBean> house_list) {
        this.house_list = house_list;
    }

    public List<UnitListBean> getUnit_list() {
        return unit_list;
    }

    public void setUnit_list(List<UnitListBean> unit_list) {
        this.unit_list = unit_list;
    }

    public List<PopListBean> getPop_list() {
        return pop_list;
    }

    public void setPop_list(List<PopListBean> pop_list) {
        this.pop_list = pop_list;
    }

    public static class HouseListBean {
        /**
         * address : 广东省广州市海珠区镜湖西街23号101铺
         * buildArea : 50
         * doorplateId : 1f3c8304-9dae-11e7-9149-38bc019d7a82
         * extSystemid : PCS3707201711090000000348264179
         * gridCode : 440105-011-119-008
         * houseCategory : unit_apartment_buliding
         * houseProperty : store
         * houseType : 0
         * housebuildId : 9f792aed-9daa-11e7-9149-38bc019d7a82
         * id : 81e2f4dc-c658-11e7-988f-38bc019d7a82
         * popList : []
         * realUseWay : 0
         * roomNo : 101铺
         * unitList : []
         * useWay : 01
         */

        private String address;
        private int buildArea;
        private String doorplateId;
        private String extSystemid;
        private String gridCode;
        private String houseCategory;
        private String houseProperty;
        private String houseType;
        private String housebuildId;
        private String id;
        private String realUseWay;
        private String roomNo;
        private String useWay;
        private List<?> popList;
        private List<?> unitList;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getBuildArea() {
            return buildArea;
        }

        public void setBuildArea(int buildArea) {
            this.buildArea = buildArea;
        }

        public String getDoorplateId() {
            return doorplateId;
        }

        public void setDoorplateId(String doorplateId) {
            this.doorplateId = doorplateId;
        }

        public String getExtSystemid() {
            return extSystemid;
        }

        public void setExtSystemid(String extSystemid) {
            this.extSystemid = extSystemid;
        }

        public String getGridCode() {
            return gridCode;
        }

        public void setGridCode(String gridCode) {
            this.gridCode = gridCode;
        }

        public String getHouseCategory() {
            return houseCategory;
        }

        public void setHouseCategory(String houseCategory) {
            this.houseCategory = houseCategory;
        }

        public String getHouseProperty() {
            return houseProperty;
        }

        public void setHouseProperty(String houseProperty) {
            this.houseProperty = houseProperty;
        }

        public String getHouseType() {
            return houseType;
        }

        public void setHouseType(String houseType) {
            this.houseType = houseType;
        }

        public String getHousebuildId() {
            return housebuildId;
        }

        public void setHousebuildId(String housebuildId) {
            this.housebuildId = housebuildId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRealUseWay() {
            return realUseWay;
        }

        public void setRealUseWay(String realUseWay) {
            this.realUseWay = realUseWay;
        }

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        public String getUseWay() {
            return useWay;
        }

        public void setUseWay(String useWay) {
            this.useWay = useWay;
        }

        public List<?> getPopList() {
            return popList;
        }

        public void setPopList(List<?> popList) {
            this.popList = popList;
        }

        public List<?> getUnitList() {
            return unitList;
        }

        public void setUnitList(List<?> unitList) {
            this.unitList = unitList;
        }
    }

    public static class UnitListBean implements Serializable{
        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        /**
         * area :
         * areaCode :
         * businessScope :
         * economicType : 100
         * houseId : 81e2f4dc-c658-11e7-988f-38bc019d7a82
         * id : 14109
         * industryType : R
         * isexist : 0
         * isrecorded : 1
         * legalRepresent : 谭启雄
         * name : 广州市中通货运代理有限公司速递
         * registerStatu :
         * socialCreditCode : 440105000033124？
         * status : 1
         * unitId : 5750ebb9-4b7d-44d2-80dd-39228924bd41
         * unitType : enterprise
         */
        //一级排水户的地址
        private String addr;
        private String area;
        private String areaCode;
        private String businessScope;
        private String economicType;
        private String houseId;
        private int id;
        //排水户id
        private int pshId;
        private String industryType;
        private int isexist;
        private int isrecorded;
        private String legalRepresent;
        private String name;
        private String registerStatu;
        private String socialCreditCode;
        private String status;
        private String unitId;
        private String unitType;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getBusinessScope() {
            return businessScope;
        }

        public void setBusinessScope(String businessScope) {
            this.businessScope = businessScope;
        }

        public String getEconomicType() {
            return economicType;
        }

        public void setEconomicType(String economicType) {
            this.economicType = economicType;
        }

        public String getHouseId() {
            return houseId;
        }

        public void setHouseId(String houseId) {
            this.houseId = houseId;
        }

        public int getId() {
            return id;
        }

        public int getPshId() {
            return pshId;
        }

        public void setPshId(int pshId) {
            this.pshId = pshId;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIndustryType() {
            return industryType;
        }

        public void setIndustryType(String industryType) {
            this.industryType = industryType;
        }

        public int getIsexist() {
            return isexist;
        }

        public void setIsexist(int isexist) {
            this.isexist = isexist;
        }

        public int getIsrecorded() {
            return isrecorded;
        }

        public void setIsrecorded(int isrecorded) {
            this.isrecorded = isrecorded;
        }

        public String getLegalRepresent() {
            return legalRepresent;
        }

        public void setLegalRepresent(String legalRepresent) {
            this.legalRepresent = legalRepresent;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRegisterStatu() {
            return registerStatu;
        }

        public void setRegisterStatu(String registerStatu) {
            this.registerStatu = registerStatu;
        }

        public String getSocialCreditCode() {
            return socialCreditCode;
        }

        public void setSocialCreditCode(String socialCreditCode) {
            this.socialCreditCode = socialCreditCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }

        public String getUnitType() {
            return unitType;
        }

        public void setUnitType(String unitType) {
            this.unitType = unitType;
        }
    }

    public static class PopListBean {
        /**
         * houseId : 9ed948fe-9daf-11e7-9149-38bc019d7a82
         * id : 1286340
         * isexist : 0
         * isrecorded : 1
         * name : 郭展宏
         * populationId : GUANGZHOU0000001614666401
         * populationType : resident_population
         */

        private String houseId;
        private int id;
        private int isexist;
        private int isrecorded;
        private String name;
        private String populationId;
        private String populationType;

        public String getHouseId() {
            return houseId;
        }

        public void setHouseId(String houseId) {
            this.houseId = houseId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsexist() {
            return isexist;
        }

        public void setIsexist(int isexist) {
            this.isexist = isexist;
        }

        public int getIsrecorded() {
            return isrecorded;
        }

        public void setIsrecorded(int isrecorded) {
            this.isrecorded = isrecorded;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPopulationId() {
            return populationId;
        }

        public void setPopulationId(String populationId) {
            this.populationId = populationId;
        }

        public String getPopulationType() {
            return populationType;
        }

        public void setPopulationType(String populationType) {
            this.populationType = populationType;
        }
    }
}
