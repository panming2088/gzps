package com.augurit.agmobile.gzps.uploadfacility.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.augurit.am.fw.utils.TimeUtil;
import com.google.gson.annotations.SerializedName;

import java.util.Date;


/**
 *
 * 审批意见
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.model
 * @createTime 创建时间 ：17/12/26
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/26
 * @modifyMemo 修改备注：
 */

public class ApprovalOpinion implements Parcelable {


    /**
     * checkDesription : 测试数据，待复核！
     * checkPerson : 系统管理员
     * checkPersonId : 236
     * checkState : 3
     * checkTime : 1514301150000
     * id : 1
     * reportId : 605
     * reportType : confirm
     * usId : 060209-28420415-000053
     */

    private String checkDesription;
    private String checkPerson;
    private String checkState;
    @SerializedName("checkPersonId")
    private String phone;
    private long checkTime;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCheckDesription() {
        return checkDesription;
    }

    public void setCheckDesription(String checkDesription) {
        this.checkDesription = checkDesription;
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(long checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckTimeStr(){
        return TimeUtil.getStringTimeYMDSFromDate(new Date(this.checkTime));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.checkDesription);
        dest.writeString(this.checkPerson);
        dest.writeString(this.checkState);
        dest.writeLong(this.checkTime);
    }

    public ApprovalOpinion() {
    }

    protected ApprovalOpinion(Parcel in) {
        this.checkDesription = in.readString();
        this.checkPerson = in.readString();
        this.checkState = in.readString();
        this.checkTime = in.readLong();
    }

    public static final Parcelable.Creator<ApprovalOpinion> CREATOR = new Parcelable.Creator<ApprovalOpinion>() {
        @Override
        public ApprovalOpinion createFromParcel(Parcel source) {
            return new ApprovalOpinion(source);
        }

        @Override
        public ApprovalOpinion[] newArray(int size) {
            return new ApprovalOpinion[size];
        }
    };
}
