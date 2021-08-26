package com.augurit.agmobile.gzps.common.util;

import android.text.TextUtils;

import com.augurit.agmobile.gzps.doorno.model.SewerageInfoBean;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;

import java.util.Date;
import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzpssb.utils
 * @createTime 创建时间 ：2018-04-11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-04-11
 * @modifyMemo 修改备注：
 */

public class SewerageBeanManger {
    private static SewerageBeanManger sManger;
    private static SewerageInfoBean sInfoBean;

    private SewerageBeanManger() {
    }

    public static synchronized SewerageBeanManger getInstance() {
        if (sManger == null) {
            sManger = new SewerageBeanManger();
        }
        if (sInfoBean == null) {
            sInfoBean = new SewerageInfoBean();
        }
        return sManger;
    }

    public void clear() {
        sInfoBean = null;
    }

    public SewerageInfoBean getInfoBean() {
        return sInfoBean;
    }

    public void setDelImg(String id) {
        if (TextUtils.isEmpty(sInfoBean.getDeleteSewerageUserAttachment())) {
            sInfoBean.setDeleteSewerageUserAttachment(id);
        } else {
            StringBuffer buffer = new StringBuffer(sInfoBean.getDeleteSewerageUserAttachment());
            buffer.append("," + id);
            sInfoBean.setDeleteSewerageUserAttachment(buffer.toString());
        }

    }

    public void setX(double X) {
        sInfoBean.setX(X);
    }

    public void setY(double Y) {
        sInfoBean.setY(Y);
    }

    public void setUnitId(String unitId) {
        sInfoBean.setUnitId(unitId);
    }

    public void setDelWell(String id) {
        if (TextUtils.isEmpty(sInfoBean.getDeletewellBeen())) {
            sInfoBean.setDeletewellBeen(id);
        } else {
            StringBuffer buffer = new StringBuffer(sInfoBean.getDeletewellBeen());
            buffer.append("," + id);
            sInfoBean.setDeletewellBeen(buffer.toString());
        }
    }

    public void clearDelImg() {
        sInfoBean.setDeleteSewerageUserAttachment("");

    }

    public void clearDelWell() {
        sInfoBean.setDeletewellBeen("");

    }


    public void setDescription(String description) {
        sInfoBean.setDescription(description);
    }

    public void setJzwcode(String jzwcode) {
        sInfoBean.setJzwcode(jzwcode);
    }

    public void setPhotos0(List<Photo> photos0) {
        sInfoBean.setPhotos0(photos0);
    }

    public void setPhotos1(List<Photo> photos1) {
        sInfoBean.setPhotos1(photos1);
    }

    public void setSaveType(int saveType) {
        sInfoBean.setSaveType(saveType);
    }

    public void setThumbnailPhotos0(List<Photo> thumbnailPhotos0) {
        sInfoBean.setThumbnailPhotos0(thumbnailPhotos0);
    }

    public void setThumbnailPhotos1(List<Photo> thumbnailPhotos1) {
        sInfoBean.setThumbnailPhotos1(thumbnailPhotos1);
    }
    public List<FileBean> getFiles() {
        return sInfoBean.getFiles();
    }

    public void setFiles(List<FileBean> files) {
        this.sInfoBean.setFiles(files);
    }
    public void setPhotos5(List<Photo> photos5) {
        sInfoBean.setPhotos5(photos5);
    }

    public void setThumbnailPhotos5(List<Photo> thumbnailPhotos5) {
        sInfoBean.setThumbnailPhotos5(thumbnailPhotos5);
    }
    public void setPhotos7(List<Photo> photos7) {
        sInfoBean.setPhotos7(photos7);
    }

    public void setThumbnailPhotos7(List<Photo> thumbnailPhotos7) {
        sInfoBean.setThumbnailPhotos7(thumbnailPhotos7);
    }

    public void setPhotos2(List<Photo> photos2) {
        sInfoBean.setPhotos2(photos2);
    }

    public void setThumbnailPhotos2(List<Photo> thumbnailPhotos2) {
        sInfoBean.setThumbnailPhotos2(thumbnailPhotos2);
    }
    public void setPhotos3(List<Photo> photos3) {

        sInfoBean.setPhotos3(photos3);
    }

    public void setThumbnailPhotos3(List<Photo> thumbnailPhotos3) {
        sInfoBean.setThumbnailPhotos3(thumbnailPhotos3);
    }

    public void setPhotos4(List<Photo> photos4) {
        sInfoBean.setPhotos4(photos4);
    }

    public void setThumbnailPhotos4(List<Photo> thumbnailPhotos4) {
        sInfoBean.setThumbnailPhotos4(thumbnailPhotos4);
    }

    public void setPsxkFzrq(String thumbnailPhotos4) {
        sInfoBean.setPsxkFzrq(thumbnailPhotos4);
    }

    public void setPsxkJzrq(String thumbnailPhotos4) {
        sInfoBean.setPsxkJzrq(thumbnailPhotos4);
    }
    public void setPsxkLx(String psxkLx) {
        sInfoBean.setPsxkLx(psxkLx);
    }

    public void setIsRight(String isRight) {
        sInfoBean.setIsRight(isRight);
    }


    public void setLoginName(String loginName) {
        sInfoBean.setLoginName(loginName);
    }

    public void setDirectOrgId(String directOrgId) {
        sInfoBean.setDirectOrgId(directOrgId);
    }

    public void setDirectOrgName(String directOrgName) {
        sInfoBean.setDirectOrgName(directOrgName);
    }

    public void setTeamOrgId(String teamOrgId) {
        sInfoBean.setTeamOrgId(teamOrgId);
    }


    public void setTeamOrgName(String teamOrgName) {
        sInfoBean.setTeamOrgName(teamOrgName);
    }

    public void setParentOrgId(String parentOrgId) {
        sInfoBean.setParentOrgId(parentOrgId);
    }

    public void setParentOrgName(String parentOrgName) {
        sInfoBean.setParentOrgName(parentOrgName);
    }

    public void setId(String id) {
        sInfoBean.setId(id);
    }

    public void setDoorplateAddressCode(String doorplateAddressCode) {
        sInfoBean.setDoorplateAddressCode(doorplateAddressCode);
    }

    public void setHouseIdFlag(String houseIdFlag) {
        sInfoBean.setHouseIdFlag(houseIdFlag);
    }

    public void setHouseId(String houseId) {
        sInfoBean.setHouseId(houseId);
    }

    public void setMarkPersonId(String markPersonId) {
        sInfoBean.setMarkPersonId(markPersonId);
    }

    public void setMarkPerson(String markPerson) {
        sInfoBean.setMarkPerson(markPerson);
    }

    public void setMarkTime(Date markTime) {
        sInfoBean.setMarkTime(markTime);
    }

    public void setAddr(String addr) {
        sInfoBean.setAddr(addr);
    }

    public void setArea(String area) {
        sInfoBean.setArea(area);
    }

    public void setTown(String town) {
        sInfoBean.setTown(town);
    }


    public void setVillage(String village) {
        sInfoBean.setVillage(village);
    }

    public void setStreet(String street) {
        sInfoBean.setStreet(street);
    }

    public void setMph(String mph) {
        sInfoBean.setMph(mph);
    }

    public void setName(String name) {
        sInfoBean.setName(name);
    }

    public void setPsdy(String psdy) {
        sInfoBean.setPsdy(psdy);
    }

    public void setFqname(String fqname) {
        sInfoBean.setFqname(fqname);
    }

    public void setVolume(String volume) {
        sInfoBean.setVolume(volume);
    }

    public void setOwner(String owner) {
        sInfoBean.setOwner(owner);
    }
    public void setWaterNo(String waterNo) {
        sInfoBean.setWaterNo(waterNo);;
    }

    public void setSfgypsh(boolean sfgypsh) {
        sInfoBean.setSfgypsh(sfgypsh);;
    }

    public void setOwnerTele(String ownerTele) {
        sInfoBean.setOwnerTele(ownerTele);
    }

    public void setOperator(String operator) {
        sInfoBean.setOperator(operator);
    }

    public void setOperatorTele(String operatorTele) {
        sInfoBean.setOperatorTele(operatorTele);
    }

    public void setHasCert1(String hasCert1) {
        sInfoBean.setHasCert1(hasCert1);
    }

    public void setCert1Code(String cert1Code) {
        sInfoBean.setCert1Code(cert1Code);
    }

    public void setHasCert2(String hasCert2) {
        sInfoBean.setHasCert2(hasCert2);
    }

    public void setHasCert3(String hasCert3) {
        sInfoBean.setHasCert3(hasCert3);
    }

    public void setCert3Code(String cert3Code) {
        sInfoBean.setCert3Code(cert3Code);
    }

    public void setHasCert4(String hasCert4) {
        sInfoBean.setHasCert4(hasCert4);
    }
    public void setHasCert5(String hasCert5) {
        sInfoBean.setHasCert5(hasCert5);
    }

    public void setHasCert7(String hasCert7) {
        sInfoBean.setHasCert7(hasCert7);
    }

    public void setCert4Code(String cert4Code) {
        sInfoBean.setCert4Code(cert4Code);
    }


    public void setDischargerType1(String dischargerType1) {
        sInfoBean.setDischargerType1(dischargerType1);
    }

    public void setDischargerType2(String dischargerType2) {
        sInfoBean.setDischargerType2(dischargerType2);
    }


    public void setFac1(String fac1) {
        sInfoBean.setFac1(fac1);
    }


    public void setFac1Cont(String fac1Cont) {
        sInfoBean.setFac1Cont(fac1Cont);
    }


    public void setFac1Main(String fac1Main) {
        sInfoBean.setFac1Main(fac1Main);
    }


    public void setFac1Record(String fac1Record) {
        sInfoBean.setFac1Record(fac1Record);
    }


    public void setFac2(String fac2) {
        sInfoBean.setFac2(fac2);
    }

    public void setFac2Cont(String fac2Cont) {
        sInfoBean.setFac2Cont(fac2Cont);
    }


    public void setFac2Main(String fac2Main) {
        sInfoBean.setFac2Main(fac2Main);
    }

    public void setFac2Record(String fac2Record) {
        sInfoBean.setFac2Record(fac2Record);
    }

    public void setFac3(String fac3) {
        sInfoBean.setFac3(fac3);
    }

    public void setFac3Cont(String fac3Cont) {
        sInfoBean.setFac3Cont(fac3Cont);
    }

    public void setFac3Main(String fac3Main) {
        sInfoBean.setFac3Main(fac3Main);
    }

    public void setFac3Record(String fac3Record) {
        sInfoBean.setFac3Record(fac3Record);
    }

    public void setFac4(String fac4) {
        sInfoBean.setFac4(fac4);
    }


    public void setFac4Cont(String fac4Cont) {
        sInfoBean.setFac4Cont(fac4Cont);
    }

    public void setFac4Main(String fac4Main) {
        sInfoBean.setFac4Main(fac4Main);
    }

    public void setFac4Record(String fac4Record) {
        sInfoBean.setFac4Record(fac4Record);
    }

    public void setState(String state) {
        sInfoBean.setState(state);
    }

    public void setCheckPersonId(String checkPersonId) {
        sInfoBean.setCheckPersonId(checkPersonId);
    }

    public void setCheckPerson(String checkPerson) {
        sInfoBean.setCheckPerson(checkPerson);
    }

    public void setCheckTime(Date checkTime) {
        sInfoBean.setCheckTime(checkTime);
    }

    public void setCheckDesription(String checkDesription) {
        sInfoBean.setCheckDesription(checkDesription);
    }

    public void setWellBeen(List<SewerageInfoBean.WellBeen> wellBeen) {
        sInfoBean.setWellBeen(wellBeen);
    }
}
