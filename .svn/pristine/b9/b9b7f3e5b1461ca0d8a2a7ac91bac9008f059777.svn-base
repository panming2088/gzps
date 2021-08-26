package com.augurit.agmobile.gzps.uploadfacility.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.augurit.agmobile.gzps.common.model.DoorNOBean;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.liteorm.db.annotation.Ignore;
import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;
import com.augurit.am.fw.utils.ListUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 设施新增实体类
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.identification.model
 * @createTime 创建时间 ：17/11/3
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/3
 * @modifyMemo 修改备注：
 */

public class UploadedFacility implements Serializable, Parcelable {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private long dbid;
    private long time;

    /**
     * addr : 雨水口
     * attrFive :
     * attrFour :
     * attrOne : 水封井
     * attrThree : 侧入
     * attrTwo : 雨污合流
     * componentType : 天河区南山路华南理工大学内,南秀村-37栋附近18米附近
     * description : 测试
     * directOrgId : 1081
     * directOrgName : 黄埔区市政建设有限公司
     * id : 62
     * markPerson : 钟无鑫
     * markPersonId : zhongwx
     * markTime : {"date":13,"day":1,"hours":0,"minutes":13,"month":10,"nanos":0,"seconds":20,"time":1510503200000,"timezoneOffset":-480,"year":117}
     * parentOrgId : 1068
     * parentOrgName : 黄埔区水务局
     * road :
     * superviseOrgId :
     * superviseOrgName :
     * teamOrgId : 1126
     * teamOrgName : 养护1班组
     * updateTime : null
     * x : 113.33803229
     * y : 23.14962483
     */

    private String addr;
    private String attrFive;
    private String attrFour;
    private String attrOne;
    private String attrThree;
    private String attrTwo;
    private String componentType;
    private String description;
    private String directOrgId;
    private String directOrgName;


    public String getSfCzwscl() {
        return sfCzwscl;
    }

    public void setSfCzwscl(String sfCzwscl) {
        this.sfCzwscl = sfCzwscl;
    }

    private String sfCzwscl;
    private String sfpsdyhxn;
    private String gpbh;
    private String objectId;
    /**
     * 唯一标识，由服务端生成并返回
     */
    private Long id;
    private String markPerson;
    private String markPersonId;
    private Long markTime;
    private String parentOrgId;
    private String parentOrgName;
    private String road;
    private String superviseOrgId;
    private String superviseOrgName;
    private String teamOrgId;
    private String teamOrgName;
    private Long updateTime;
    private double x;
    private double y;
    /**
     *审核状态
     */
    private String checkState;
    private String checkPersonId;
    private String checkPerson;
    private String checkDesription;
    /**
     * 由于当请求图片地址后会出现顺序乱的情况，所以加入这个字段进行维护原有顺序
     */
    @Ignore
    private int order;



    private List<Photo> photos;
    private List<Photo> thumbnailPhotos;

    private List<Photo> wellPhotos;
    private List<Photo> wellThumbnailPhotos;
    private String layerUrl;
    private int layerId;
    private String layerName;
    private String usid;
    /**
     * 是否有跟部件绑定，如果有跟部件进行绑定，说明是数据确认，否则说明是数据新增；
     * 有两种数值：1: 表示跟部件进行了绑定，是数据确认，其他则是数据新增。
     */
    private int isBinding;

    /**
     * 用户当前所处的位置
     */
    private String userAddr;

    /**
     * 用户当前位置的经度
     */
    @SerializedName("userX")
    private double userLocationX;

    /**
     * 用户当前位置的纬度
     */
    @SerializedName("userY")
    private double userLocationY;

    /**
     * 再次编辑时删除的图片id
     */
    @Expose
    public List<String> deletedPhotoIds;

    @Expose
    public List<Long> deletempBeen;

    /**
     * 审批意见
     */
    private List<ApprovalOpinion> approvalOpinions;

    /**
     * 大类编码 ， 用","自行分割
     */
    @SerializedName("pcode")
    private String pCode;

    /**
     * 小类编码 ，用","自行分割
     */
    private String childCode;

    /**
     * 管理状态
     */
    private String cityVillage;

    private String QDBH;
    /**
     * 交办状态
     */
    private String QDZT;

    private String OLDOID;
    private double riverx;
    private double rivery;

    private String attrSix;

    private String attrSeven;

    private String sfgjjd;//是否关键节点
    private String gjjdBh;//关键节点编号
    private String gjjdZrr;//责任人
    private String gjjdLxdh;//联系电话

    private String yjbh;//窨井编号

    private List<DoorNOBean> mpBeen;
    public long getDbid() {
        return dbid;
    }

    public void setDbid(long dbid) {
        this.dbid = dbid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    public double getUserLocationX() {
        return userLocationX;
    }

    public void setUserLocationX(double userLocationX) {
        this.userLocationX = userLocationX;
    }

    public double getUserLocationY() {
        return userLocationY;
    }

    public void setUserLocationY(double userLocationY) {
        this.userLocationY = userLocationY;
    }

    public String getLayerUrl() {
        return layerUrl;
    }

    public void setLayerUrl(String layerUrl) {
        this.layerUrl = layerUrl;
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

    public String getUsid() {
        return usid;
    }

    public void setUsid(String usid) {
        this.usid = usid;
    }

    public int getIsBinding() {
        return isBinding;
    }

    public void setIsBinding(int isBinding) {
        this.isBinding = isBinding;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Photo> getThumbnailPhotos() {
        return thumbnailPhotos;
    }

    public void setThumbnailPhotos(List<Photo> thumbnailPhotos) {
        this.thumbnailPhotos = thumbnailPhotos;
    }
    public List<Photo> getWellPhotos() {
        return wellPhotos;
    }

    public void setWellPhotos(List<Photo> wellPhotos) {
        this.wellPhotos = wellPhotos;
    }
    public List<Photo> getWellThumbnailPhotos() {
        return wellThumbnailPhotos;
    }
    public void setWellThumbnailPhotos(List<Photo> wellThumbnailPhotos) {
        this.wellThumbnailPhotos = wellThumbnailPhotos;
    }
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getAttrFive() {
        return attrFive;
    }

    public void setAttrFive(String attrFive) {
        this.attrFive = attrFive;
    }
    public String getGpbh() {
        return gpbh;
    }

    public void setGpbh(String gpbh) {
        this.gpbh = gpbh;
    }
    public String getAttrFour() {
        return attrFour;
    }

    public void setAttrFour(String attrFour) {
        this.attrFour = attrFour;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public String getCheckPersonId() {
        return checkPersonId;
    }

    public void setCheckPersonId(String checkPersonId) {
        this.checkPersonId = checkPersonId;
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

//    public Long getCheckTime() {
//        return checkTime;
//    }
//
//    public void setCheckTime(Long checkTime) {
//        this.checkTime = checkTime;
//    }

    public String getCheckDesription() {
        return checkDesription;
    }

    public void setCheckDesription(String checkDesription) {
        this.checkDesription = checkDesription;
    }

    public String getAttrOne() {
        return attrOne;
    }

    public void setAttrOne(String attrOne) {
        this.attrOne = attrOne;
    }

    public String getAttrThree() {
        return attrThree;
    }

    public void setAttrThree(String attrThree) {
        this.attrThree = attrThree;
    }

    public String getAttrTwo() {
        return attrTwo;
    }

    public void setAttrTwo(String attrTwo) {
        this.attrTwo = attrTwo;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarkPerson() {
        return markPerson;
    }

    public void setMarkPerson(String markPerson) {
        this.markPerson = markPerson;
    }

    public String getMarkPersonId() {
        return markPersonId;
    }

    public void setMarkPersonId(String markPersonId) {
        this.markPersonId = markPersonId;
    }

    public Long getMarkTime() {
        return markTime;
    }

    public void setMarkTime(Long markTime) {
        this.markTime = markTime;
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

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
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

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getDeletedPhotoIdsStr(){

        if(ListUtil.isEmpty(deletedPhotoIds)){
            return null;
        }

        String ids = "";
        for (int i = 0; i < deletedPhotoIds.size() ; i ++){
            if (i == deletedPhotoIds.size() - 1){
                ids  += deletedPhotoIds.get(i);
            }else {
                ids  += deletedPhotoIds.get(i) + ",";
            }
        }
        return ids;

        //return Arrays.toString(deletedPhotoIds.toArray(new String[]{})).replace("[","").replace("]","");
    }

    public List<String> getDeletedPhotoIds() {
        return deletedPhotoIds;
    }

    public void setDeletedPhotoIds(List<String> deletedPhotoIds) {
        this.deletedPhotoIds = deletedPhotoIds;
    }

    public String getDeletempBeenStr(){

        if(ListUtil.isEmpty(deletempBeen)){
            return null;
        }

        String ids = "";
        for (int i = 0; i < deletempBeen.size() ; i ++){
            if (i == deletempBeen.size() - 1){
                ids  += deletempBeen.get(i);
            }else {
                ids  += deletempBeen.get(i) + ",";
            }
        }
        return ids;
    }

    public List<Long> getDeletempBeen() {
        return deletempBeen;
    }

    public void setDeletempBeen(List<Long> deletempBeen) {
        this.deletempBeen = deletempBeen;
    }

    public List<ApprovalOpinion> getApprovalOpinions() {
        return approvalOpinions;
    }

    public void setApprovalOpinions(List<ApprovalOpinion> approvalOpinions) {
        this.approvalOpinions = approvalOpinions;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public String getChildCode() {
        return childCode;
    }

    public void setChildCode(String childCode) {
        this.childCode = childCode;
    }

    public String getCityVillage() {
        return cityVillage;
    }

    public void setCityVillage(String cityVillage) {
        this.cityVillage = cityVillage;
    }

    public String getQDBH() {
        return QDBH;
    }

    public void setQDBH(String QDBH) {
        this.QDBH = QDBH;
    }

    public String getQDZT() {
        return QDZT;
    }

    public void setQDZT(String QDZT) {
        this.QDZT = QDZT;
    }

    public String getOLDOID() {
        return OLDOID;
    }

    public void setOLDOID(String OLDOID) {
        this.OLDOID = OLDOID;
    }

    public double getRiverx() {
        return riverx;
    }

    public void setRiverx(double riverx) {
        this.riverx = riverx;
    }

    public double getRivery() {
        return rivery;
    }

    public void setRivery(double rivery) {
        this.rivery = rivery;
    }

    public String getAttrSix() {
        return attrSix;
    }

    public void setAttrSix(String attrSix) {
        this.attrSix = attrSix;
    }

    public String getAttrSeven() {
        return attrSeven;
    }

    public void setAttrSeven(String attrSeven) {
        this.attrSeven = attrSeven;
    }

    public List<DoorNOBean> getMpBeen() {
        return mpBeen;
    }

    public void setMpBeen(List<DoorNOBean> mpBeen) {
        this.mpBeen = mpBeen;
    }

    public String getSfgjjd() {
        return sfgjjd;
    }

    public void setSfgjjd(String sfgjjd) {
        this.sfgjjd = sfgjjd;
    }

    public String getGjjdBh() {
        return gjjdBh;
    }

    public void setGjjdBh(String gjjdBh) {
        this.gjjdBh = gjjdBh;
    }

    public String getGjjdZrr() {
        return gjjdZrr;
    }

    public void setGjjdZrr(String gjjdZrr) {
        this.gjjdZrr = gjjdZrr;
    }

    public String getGjjdLxdh() {
        return gjjdLxdh;
    }

    public void setGjjdLxdh(String gjjdLxdh) {
        this.gjjdLxdh = gjjdLxdh;
    }

    public String getYjbh() {
        return yjbh;
    }

    public void setYjbh(String yjbh) {
        this.yjbh = yjbh;
    }

    public String getSfpsdyhxn() {
        return sfpsdyhxn;
    }

    public void setSfpsdyhxn(String sfpsdyhxn) {
        this.sfpsdyhxn = sfpsdyhxn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.dbid);
        dest.writeLong(this.time);
        dest.writeString(this.addr);
        dest.writeString(this.attrFive);
        dest.writeString(this.attrFour);
        dest.writeString(this.attrOne);
        dest.writeString(this.attrThree);
        dest.writeString(this.attrTwo);
        dest.writeString(this.componentType);
        dest.writeString(this.description);
        dest.writeString(this.directOrgId);
        dest.writeString(this.directOrgName);
        dest.writeString(this.sfCzwscl);
        dest.writeString(this.sfpsdyhxn);
        dest.writeString(this.gpbh);
        dest.writeString(this.objectId);
        dest.writeValue(this.id);
        dest.writeString(this.markPerson);
        dest.writeString(this.markPersonId);
        dest.writeValue(this.markTime);
        dest.writeString(this.parentOrgId);
        dest.writeString(this.parentOrgName);
        dest.writeString(this.road);
        dest.writeString(this.superviseOrgId);
        dest.writeString(this.superviseOrgName);
        dest.writeString(this.teamOrgId);
        dest.writeString(this.teamOrgName);
        dest.writeValue(this.updateTime);
        dest.writeDouble(this.x);
        dest.writeDouble(this.y);
        dest.writeString(this.checkState);
        dest.writeString(this.checkPersonId);
        dest.writeString(this.checkPerson);
        dest.writeString(this.checkDesription);
        dest.writeInt(this.order);
        dest.writeList(this.photos);
        dest.writeList(this.thumbnailPhotos);
        dest.writeList(this.wellPhotos);
        dest.writeList(this.wellThumbnailPhotos);
        dest.writeString(this.layerUrl);
        dest.writeInt(this.layerId);
        dest.writeString(this.layerName);
        dest.writeString(this.usid);
        dest.writeInt(this.isBinding);
        dest.writeString(this.userAddr);
        dest.writeDouble(this.userLocationX);
        dest.writeDouble(this.userLocationY);
        dest.writeStringList(this.deletedPhotoIds);
        dest.writeList(this.deletempBeen);
        dest.writeTypedList(this.approvalOpinions);
        dest.writeString(this.pCode);
        dest.writeString(this.childCode);
        dest.writeString(this.cityVillage);
        dest.writeString(this.QDBH);
        dest.writeString(this.QDZT);
        dest.writeString(this.OLDOID);
        dest.writeDouble(this.riverx);
        dest.writeDouble(this.rivery);
        dest.writeString(this.attrSix);
        dest.writeString(this.attrSeven);
        dest.writeString(this.sfgjjd);
        dest.writeString(this.gjjdBh);
        dest.writeString(this.gjjdZrr);
        dest.writeString(this.gjjdLxdh);
        dest.writeString(this.yjbh);
        dest.writeTypedList(this.mpBeen);
    }

    public UploadedFacility() {
    }

    protected UploadedFacility(Parcel in) {
        this.dbid = in.readLong();
        this.time = in.readLong();
        this.addr = in.readString();
        this.attrFive = in.readString();
        this.attrFour = in.readString();
        this.attrOne = in.readString();
        this.attrThree = in.readString();
        this.attrTwo = in.readString();
        this.componentType = in.readString();
        this.description = in.readString();
        this.directOrgId = in.readString();
        this.directOrgName = in.readString();
        this.sfCzwscl = in.readString();
        this.sfpsdyhxn = in.readString();
        this.gpbh = in.readString();
        this.objectId = in.readString();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.markPerson = in.readString();
        this.markPersonId = in.readString();
        this.markTime = (Long) in.readValue(Long.class.getClassLoader());
        this.parentOrgId = in.readString();
        this.parentOrgName = in.readString();
        this.road = in.readString();
        this.superviseOrgId = in.readString();
        this.superviseOrgName = in.readString();
        this.teamOrgId = in.readString();
        this.teamOrgName = in.readString();
        this.updateTime = (Long) in.readValue(Long.class.getClassLoader());
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.checkState = in.readString();
        this.checkPersonId = in.readString();
        this.checkPerson = in.readString();
        this.checkDesription = in.readString();
        this.order = in.readInt();
        this.photos = new ArrayList<Photo>();
        in.readList(this.photos, Photo.class.getClassLoader());
        this.thumbnailPhotos = new ArrayList<Photo>();
        in.readList(this.thumbnailPhotos, Photo.class.getClassLoader());
        this.wellPhotos = new ArrayList<Photo>();
        in.readList(this.wellPhotos, Photo.class.getClassLoader());
        this.wellThumbnailPhotos = new ArrayList<Photo>();
        in.readList(this.wellThumbnailPhotos, Photo.class.getClassLoader());
        this.layerUrl = in.readString();
        this.layerId = in.readInt();
        this.layerName = in.readString();
        this.usid = in.readString();
        this.isBinding = in.readInt();
        this.userAddr = in.readString();
        this.userLocationX = in.readDouble();
        this.userLocationY = in.readDouble();
        this.deletedPhotoIds = in.createStringArrayList();
        this.deletempBeen = new ArrayList<Long>();
        in.readList(this.deletempBeen, Long.class.getClassLoader());
        this.approvalOpinions = in.createTypedArrayList(ApprovalOpinion.CREATOR);
        this.pCode = in.readString();
        this.childCode = in.readString();
        this.cityVillage = in.readString();
        this.QDBH = in.readString();
        this.QDZT = in.readString();
        this.OLDOID = in.readString();
        this.riverx = in.readDouble();
        this.rivery = in.readDouble();
        this.attrSix = in.readString();
        this.attrSeven = in.readString();
        this.sfgjjd = in.readString();
        this.gjjdBh = in.readString();
        this.gjjdZrr = in.readString();
        this.gjjdLxdh = in.readString();
        this.yjbh = in.readString();
        this.mpBeen = in.createTypedArrayList(DoorNOBean.CREATOR);
    }

    public static final Parcelable.Creator<UploadedFacility> CREATOR = new Parcelable.Creator<UploadedFacility>() {
        @Override
        public UploadedFacility createFromParcel(Parcel source) {
            return new UploadedFacility(source);
        }

        @Override
        public UploadedFacility[] newArray(int size) {
            return new UploadedFacility[size];
        }
    };
}
