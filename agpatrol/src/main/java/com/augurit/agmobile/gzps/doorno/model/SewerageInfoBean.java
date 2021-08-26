package com.augurit.agmobile.gzps.doorno.model;

import com.augurit.agmobile.gzps.common.util.FileBean;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SewerageInfoBean implements Serializable{
    private long saveTime;
    public long getDbid() {
        return dbid;
    }

    public void setDbid(long dbid) {
        this.dbid = dbid;
    }

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private long dbid;

    // 属性
    @Expose
    private String id;//
    @Expose
    private int saveType;//保存类型
    @Expose
    private String doorplateAddressCode;//门牌号id
    @Expose
    private String houseIdFlag;//房间栋或房间套id标识
    @Expose
    private String houseId;//房屋栋或套id
    @Expose
    private String markPersonId;//上报人ID
    @Expose
    private String markPerson;//上报人名称
    @Expose
    private Date markTime;//上报时间
    @Expose
    private String addr;//地址
    @Expose
    private String area;//区
    @Expose
    private String town;//乡镇街道
    @Expose
    private String village;//社区居委
    @Expose
    private String street;//街路巷名称
    @Expose
    private String mph;//门牌号
    @Expose
    private String name;//排水户名称
    @Expose
    private String psdy;//排水单元分区
    @Expose
    private String fqname;//分区名称
    @Expose
    private String volume;//月均用水量
    @Expose
    private String owner;//产权人
    @Expose
    private String ownerTele;//产权人联系电话
    @Expose
    private String operator;//经营人
    @Expose
    private String operatorTele;//经营人联系电话
    @Expose
    private String hasCert1;//是否有工商营业执照
    @Expose
    private String cert1Code;//工商执照代码
    @Expose
    private String hasCert2;//是否有接驳核准意见书
    @Expose
    private String hasCert3;//是否有排水许可证
    @Expose
    private String cert3Code;//排水许可证代码
    @Expose
    private String hasCert4;//是否有排污许可证
    @Expose
    private String hasCert5;//是否有管网图
    @Expose
    private String hasCert6;//是否有管网图pdf文件

    private String hasCert7;//登记表
    @Expose
    private String cert4Code;//排污许可证代码
    @Expose
    private String dischargerType1;//行业类别大类：
    @Expose
    private String dischargerType2;//行业类别小类
    @Expose
    private String fac1;//是否有隔油池
    @Expose
    private String fac1Cont;///隔油池运行情况
    @Expose
    private String fac1Main;//隔油池是否进行保养
    @Expose
    private String fac1Record;//隔油池是否有养护记录
    @Expose
    private String fac2;//是否有格栅
    @Expose
    private String fac2Cont;//格栅运行情况
    @Expose
    private String fac2Main;//格栅是否进行保养
    @Expose
    private String fac2Record;//格栅是否有养护记录
    @Expose
    private String fac3;//是否有沉淀池
    @Expose
    private String fac3Cont;///沉淀池运行情况
    @Expose
    private String fac3Main;//沉淀池是否进行保养
    @Expose
    private String fac3Record;//沉淀池是否有养护记录
    @Expose
    private String fac4;//预处理设施名称
    @Expose
    private String fac4Cont;//沉淀池运行情况
    @Expose
    private String fac4Main;//沉淀池是否进行保养
    @Expose
    private String fac4Record;//沉淀池是否有养护记录
    @Expose
    private String state;//
    @Expose
    private String checkPersonId;
    private String checkPerson;
    private Date checkTime;
    @Expose
    private String checkDesription;
    private String updatetime;
    @Expose
    private String description;
    @Expose
    private String psxkFzrq;//排水许可发证日期
    @Expose
    private String psxkLx;//排水户类型
    @Expose
    private String psxkJzrq;//排水许可有效期截止至
    @Expose
    private String isRight;//排水许可证发证日期必须小于截止日期
    @Expose
    private boolean sfgypsh;//属于工业类排水户
    private String ysUnitid ;//原始的unitid，用于修改工业排水户
    private String sfdrpsh;//关联导入排水户
    @Expose
    private String waterNo;//水表号
    @Expose
    private String add_type; //add_type=0  两个列表接口加上这个参数  0是 自增的门牌   不传就默认是sbss的门牌


    private boolean isDraft;//是否是草稿
    public boolean isIndustry() {
        return isIndustry;
    }

    public void setIndustry(boolean industry) {
        isIndustry = industry;
    }

    private boolean isIndustry;//工业排水户
    public String getPsxkFzrq() {
        return psxkFzrq;
    }

    public void setPsxkFzrq(String psxkFzrq) {
        this.psxkFzrq = psxkFzrq;
    }

    public String getPsxkJzrq() {
        return psxkJzrq;
    }

    public void setPsxkJzrq(String psxkJzrq) {
        this.psxkJzrq = psxkJzrq;
    }

    public String getIsRight() {
        return isRight;
    }

    public void setIsRight(String isRight) {
        this.isRight = isRight;
    }
    public String getHasCert5() {
        return hasCert5;
    }

    public String getHasCert6() {
        return hasCert6;
    }

    public void setHasCert6(String hasCert6) {
        this.hasCert6 = hasCert6;
    }
    public void setHasCert5(String hasCert5) {
        this.hasCert5 = hasCert5;
    }
    public String getAdd_type() {
        return add_type;
    }

    public void setAdd_type(String add_type) {
        this.add_type = add_type;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setX(double x) {
        X = x;
    }

    public void setY(double y) {
        Y = y;
    }

    public double getX() {
        return X;
    }

    public String getYsUnitid() {
        return ysUnitid;
    }

    public void setYsUnitid(String ysUnitid) {
        this.ysUnitid = ysUnitid;
    }
    public double getY() {
        return Y;
    }
    @Expose
    private double X;
    @Expose
    private double Y;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
    @Expose
    private String unitId;
    private String deletewellBeen;//删除的接驳井id，id
    private String deleteSewerageUserAttachment;//删除的图片id，id

    public String getSfdrpsh() {
        return sfdrpsh;
    }

    public void setSfdrpsh(String sfdrpsh) {
        this.sfdrpsh = sfdrpsh;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeletewellBeen() {
        return deletewellBeen;
    }

    public void setDeletewellBeen(String deletewellBeen) {
        this.deletewellBeen = deletewellBeen;
    }

    public String getDeleteSewerageUserAttachment() {
        return deleteSewerageUserAttachment;
    }

    public void setDeleteSewerageUserAttachment(String deleteSewerageUserAttachment) {
        this.deleteSewerageUserAttachment = deleteSewerageUserAttachment;
    }

    public String getJzwcode() {
        return jzwcode;
    }

    public void setJzwcode(String jzwcode) {
        this.jzwcode = jzwcode;
    }

    public boolean isDraft() {
        return isDraft;
    }

    public void setDraft(boolean draft) {
        isDraft = draft;
    }
    @Expose
    private String jzwcode;
    //现场照片
    @Expose
    private List<Photo> photos0;

    public void setPhotos0(List<Photo> photos0) {
        this.photos0 = photos0;
    }

    public void setThumbnailPhotos0(List<Photo> thumbnailPhotos0) {
        this.thumbnailPhotos0 = thumbnailPhotos0;
    }
    @Expose
    private List<Photo> thumbnailPhotos0;
    //工商营业执照
    //
    @Expose
    private List<Photo> photos1;
    @Expose
    private List<Photo> thumbnailPhotos1;
    //接驳意见
    @Expose
    private List<Photo> photos2;
    @Expose
    private List<Photo> thumbnailPhotos2;

    //排水
    @Expose
    private List<Photo> photos3;
    @Expose
    private List<Photo> thumbnailPhotos3;

    //排污
    private List<Photo> photos4;
    private List<Photo> thumbnailPhotos4;

    //排管网图
    @Expose
    private List<Photo> photos5;
    @Expose
    private List<Photo> thumbnailPhotos5;
    //登记表图片
    @Expose
    private List<Photo> photos7;
    @Expose
    private List<Photo> thumbnailPhotos7;

    public String getHasCert7() {
        return hasCert7;
    }

    public void setHasCert7(String hasCert7) {
        this.hasCert7 = hasCert7;
    }

    public List<Photo> getPhotos7() {
        return photos7;
    }

    public void setPhotos7(List<Photo> photos7) {
        this.photos7 = photos7;
    }

    public List<Photo> getThumbnailPhotos7() {
        return thumbnailPhotos7;
    }

    public void setThumbnailPhotos7(List<Photo> thumbnailPhotos7) {
        this.thumbnailPhotos7 = thumbnailPhotos7;
    }

    public List<FileBean> getFiles() {
        return files;
    }

    public void setFiles(List<FileBean> files) {
        this.files = files;
    }

    @Expose
    private List<FileBean> files;
    public List<Photo> getPhotos0() {
        return photos0;
    }

    public List<Photo> getThumbnailPhotos0() {
        return thumbnailPhotos0;
    }

    public void setPhotos1(List<Photo> photos1) {
        this.photos1 = photos1;
    }

    public void setThumbnailPhotos1(List<Photo> thumbnailPhotos1) {
        this.thumbnailPhotos1 = thumbnailPhotos1;
    }

    public void setPhotos2(List<Photo> photos2) {
        this.photos2 = photos2;
    }

    public void setThumbnailPhotos2(List<Photo> thumbnailPhotos2) {
        this.thumbnailPhotos2 = thumbnailPhotos2;
    }

    public void setPhotos3(List<Photo> photos3) {
        this.photos3 = photos3;
    }

    public void setThumbnailPhotos3(List<Photo> thumbnailPhotos3) {
        this.thumbnailPhotos3 = thumbnailPhotos3;
    }

    public void setPhotos4(List<Photo> photos4) {
        this.photos4 = photos4;
    }

    public void setThumbnailPhotos4(List<Photo> thumbnailPhotos4) {
        this.thumbnailPhotos4 = thumbnailPhotos4;
    }

    public List<Photo> getPhotos5() {
        return photos5;
    }

    public void setPhotos5(List<Photo> photos5) {
        this.photos5 = photos5;
    }

    public List<Photo> getThumbnailPhotos5() {
        return thumbnailPhotos5;
    }

    public void setThumbnailPhotos5(List<Photo> thumbnailPhotos5) {
        this.thumbnailPhotos5 = thumbnailPhotos5;
    }
    public List<Photo> getPhotos1() {
        return photos1;
    }

    public List<Photo> getThumbnailPhotos1() {
        return thumbnailPhotos1;
    }

    public List<Photo> getPhotos2() {
        return photos2;
    }

    public List<Photo> getThumbnailPhotos2() {
        return thumbnailPhotos2;
    }

    public List<Photo> getPhotos3() {
        return photos3;
    }

    public List<Photo> getThumbnailPhotos3() {
        return thumbnailPhotos3;
    }

    public List<Photo> getPhotos4() {
        return photos4;
    }

    public List<Photo> getThumbnailPhotos4() {
        return thumbnailPhotos4;
    }
    @Expose
    String att_name;
    @Expose
    String att_time;
    @Expose
    String mime;
    @Expose
    String att_path;
    @Expose
    String thum_path;


    public void setAtt_name(String att_name) {
        this.att_name = att_name;
    }

    public void setAtt_time(String att_time) {
        this.att_time = att_time;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public void setAtt_path(String att_path) {
        this.att_path = att_path;
    }

    public void setThum_path(String thum_path) {
        this.thum_path = thum_path;
    }

    public String getAtt_name() {
        return att_name;
    }

    public String getAtt_time() {
        return att_time;
    }

    public String getMime() {
        return mime;
    }

    public String getAtt_path() {
        return att_path;
    }

    public String getThum_path() {
        return thum_path;
    }

    @Expose
    private String loginName;
    @Expose
    private String directOrgId;
    @Expose
    private String directOrgName;
    @Expose
    private String teamOrgId;
    @Expose
    private String teamOrgName;
    @Expose
    private String parentOrgId;
    @Expose
    private String parentOrgName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public String getTeamOrgId() {
        return teamOrgId;
    }

    public void setTeamOrgId(String teamOrgId) {
        this.teamOrgId = teamOrgId;
    }

    public String getTeamOrgName() {
        return teamOrgName;
    }
    public boolean isSfgypsh() {
        return sfgypsh;
    }

    public void setSfgypsh(boolean sfgypsh) {
        this.sfgypsh = sfgypsh;
    }

    public String getWaterNo() {
        return waterNo;
    }

    public void setWaterNo(String waterNo) {
        this.waterNo = waterNo;
    }
    public void setTeamOrgName(String teamOrgName) {
        this.teamOrgName = teamOrgName;
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


    public String getDoorplateAddressCode() {
        return this.doorplateAddressCode;
    }

    public void setDoorplateAddressCode(String doorplateAddressCode) {
        this.doorplateAddressCode = doorplateAddressCode;
    }

    public String getHouseIdFlag() {
        return this.houseIdFlag;
    }

    public void setHouseIdFlag(String houseIdFlag) {
        this.houseIdFlag = houseIdFlag;
    }

    public String getHouseId() {
        return this.houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getMarkPersonId() {
        return this.markPersonId;
    }

    public void setMarkPersonId(String markPersonId) {
        this.markPersonId = markPersonId;
    }

    public String getMarkPerson() {
        return this.markPerson;
    }

    public void setMarkPerson(String markPerson) {
        this.markPerson = markPerson;
    }

    public Date getMarkTime() {
        return this.markTime;
    }

    public void setMarkTime(Date markTime) {
        this.markTime = markTime;
    }

    public String getAddr() {
        return this.addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTown() {
        return this.town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage() {
        return this.village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getMph() {
        return this.mph;
    }

    public void setMph(String mph) {
        this.mph = mph;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPsdy() {
        return this.psdy;
    }

    public void setPsdy(String psdy) {
        this.psdy = psdy;
    }

    public String getFqname() {
        return this.fqname;
    }

    public void setFqname(String fqname) {
        this.fqname = fqname;
    }


    public String getVolume() {
        return this.volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerTele() {
        return this.ownerTele;
    }

    public void setOwnerTele(String ownerTele) {
        this.ownerTele = ownerTele;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorTele() {
        return this.operatorTele;
    }

    public void setOperatorTele(String operatorTele) {
        this.operatorTele = operatorTele;
    }

    public String getHasCert1() {
        return this.hasCert1;
    }

    public void setHasCert1(String hasCert1) {
        this.hasCert1 = hasCert1;
    }

    public String getCert1Code() {
        return this.cert1Code;
    }

    public void setCert1Code(String cert1Code) {
        this.cert1Code = cert1Code;
    }

    public String getHasCert2() {
        return this.hasCert2;
    }

    public void setHasCert2(String hasCert2) {
        this.hasCert2 = hasCert2;
    }

    public String getHasCert3() {
        return this.hasCert3;
    }

    public void setHasCert3(String hasCert3) {
        this.hasCert3 = hasCert3;
    }

    public String getCert3Code() {
        return this.cert3Code;
    }

    public void setCert3Code(String cert3Code) {
        this.cert3Code = cert3Code;
    }

    public String getHasCert4() {
        return this.hasCert4;
    }

    public void setHasCert4(String hasCert4) {
        this.hasCert4 = hasCert4;
    }

    public String getCert4Code() {
        return this.cert4Code;
    }

    public void setCert4Code(String cert4Code) {
        this.cert4Code = cert4Code;
    }

    public String getDischargerType1() {
        return this.dischargerType1;
    }

    public void setDischargerType1(String dischargerType1) {
        this.dischargerType1 = dischargerType1;
    }

    public String getDischargerType2() {
        return this.dischargerType2;
    }

    public void setDischargerType2(String dischargerType2) {
        this.dischargerType2 = dischargerType2;
    }

    public String getFac1() {
        return this.fac1;
    }

    public void setFac1(String fac1) {
        this.fac1 = fac1;
    }

    public String getFac1Cont() {
        return this.fac1Cont;
    }

    public void setFac1Cont(String fac1Cont) {
        this.fac1Cont = fac1Cont;
    }

    public String getFac1Main() {
        return this.fac1Main;
    }

    public void setFac1Main(String fac1Main) {
        this.fac1Main = fac1Main;
    }

    public String getFac1Record() {
        return this.fac1Record;
    }

    public void setFac1Record(String fac1Record) {
        this.fac1Record = fac1Record;
    }

    public String getFac2() {
        return this.fac2;
    }

    public void setFac2(String fac2) {
        this.fac2 = fac2;
    }

    public String getFac2Cont() {
        return this.fac2Cont;
    }

    public void setFac2Cont(String fac2Cont) {
        this.fac2Cont = fac2Cont;
    }

    public String getFac2Main() {
        return this.fac2Main;
    }

    public void setFac2Main(String fac2Main) {
        this.fac2Main = fac2Main;
    }

    public String getFac2Record() {
        return this.fac2Record;
    }

    public void setFac2Record(String fac2Record) {
        this.fac2Record = fac2Record;
    }

    public String getFac3() {
        return this.fac3;
    }

    public void setFac3(String fac3) {
        this.fac3 = fac3;
    }

    public String getFac3Cont() {
        return this.fac3Cont;
    }

    public void setFac3Cont(String fac3Cont) {
        this.fac3Cont = fac3Cont;
    }

    public String getFac3Main() {
        return this.fac3Main;
    }

    public void setFac3Main(String fac3Main) {
        this.fac3Main = fac3Main;
    }

    public String getFac3Record() {
        return this.fac3Record;
    }

    public void setFac3Record(String fac3Record) {
        this.fac3Record = fac3Record;
    }

    public String getFac4() {
        return this.fac4;
    }


    public void setFac4(String fac4) {
        this.fac4 = fac4;
    }


    public String getFac4Cont() {
        return this.fac4Cont;
    }

    public void setFac4Cont(String fac4Cont) {
        this.fac4Cont = fac4Cont;
    }

    public String getFac4Main() {
        return this.fac4Main;
    }

    public void setFac4Main(String fac4Main) {
        this.fac4Main = fac4Main;
    }

    public String getFac4Record() {
        return this.fac4Record;
    }

    public void setFac4Record(String fac4Record) {
        this.fac4Record = fac4Record;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCheckPersonId() {
        return this.checkPersonId;
    }

    public void setCheckPersonId(String checkPersonId) {
        this.checkPersonId = checkPersonId;
    }

    public int getSaveType() {
        return saveType;
    }

    public void setSaveType(int saveType) {
        this.saveType = saveType;
    }
    public String getCheckPerson() {
        return this.checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    public Date getCheckTime() {
        return this.checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckDesription() {
        return this.checkDesription;
    }

    public void setCheckDesription(String checkDesription) {
        this.checkDesription = checkDesription;
    }

    public List<WellBeen> getWellBeen() {
        return wellBeen;
    }

    public void setWellBeen(List<WellBeen> wellBeen) {
        this.wellBeen = wellBeen;
    }

    public String getPsxkLx() {
        return psxkLx;
    }

    public void setPsxkLx(String psxkLx) {
        this.psxkLx = psxkLx;
    }
    @Expose
    List<WellBeen> wellBeen;

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public long getSaveTime() {
        return saveTime;
    }

    //接驳井信息
    public static class WellBeen implements Serializable {
        public Long getWiId() {
            return wiId;
        }

        public void setWiId(Long wiId) {
            this.wiId = wiId;
        }

        @PrimaryKey(AssignType.AUTO_INCREMENT)
        Long wiId;
        String dischargeId;
        Long id;
        String wellId;
        String onlyid;
        String newDrainPro;

        public String getProblem_id() {
            return problem_id;
        }

        public void setProblem_id(String problem_id) {
            this.problem_id = problem_id;
        }

        private String problem_id; //上报内容ID

        public String getLxId() {
            return lxId;
        }

        public void setLxId(String lxId) {
            this.lxId = lxId;
        }
        String lxId;
        String x;
        String y;
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

        public String getNewDrainPro() {
            return newDrainPro;
        }

        public void setNewDrainPro(String newDrainPro) {
            this.newDrainPro = newDrainPro;
        }

        public String getOnlyid() {
            return onlyid;
        }

        public void setOnlyid(String onlyid) {
            this.onlyid = onlyid;
        }

        public String getDischargeId() {
            return dischargeId;
        }

        public void setDischargeId(String dischargeId) {
            this.dischargeId = dischargeId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getWellId() {
            return wellId;
        }

        public String getWellType() {
            return wellType;
        }

        public String getWellPro() {
            return wellPro;
        }

        public String getWellDir() {
            return wellDir;
        }

        public String getDrainPro() {
            return drainPro;
        }

        public String getPipeType() {
            return pipeType;
        }
        String wellType;
        String wellPro;

        public void setWellId(String wellId) {
            this.wellId = wellId;
        }

        public void setWellType(String wellType) {
            this.wellType = wellType;
        }

        public void setWellPro(String wellPro) {
            this.wellPro = wellPro;
        }

        public void setWellDir(String wellDir) {
            this.wellDir = wellDir;
        }

        public void setDrainPro(String drainPro) {
            this.drainPro = drainPro;
        }

        public void setPipeType(String pipeType) {
            this.pipeType = pipeType;
        }

        String wellDir;
        String drainPro;
        String pipeType;

    }
}