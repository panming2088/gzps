package com.augurit.agmobile.gzps.journal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 班组成员
 * Created by xcl on 2017/11/20.
 */

public class TeamMember implements Parcelable {

    private String loginName;
    private String userName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.loginName);
        dest.writeString(this.userName);
    }

    public TeamMember() {
    }

    protected TeamMember(Parcel in) {
        this.loginName = in.readString();
        this.userName = in.readString();
    }

    public static final Parcelable.Creator<TeamMember> CREATOR = new Parcelable.Creator<TeamMember>() {
        public TeamMember createFromParcel(Parcel source) {
            return new TeamMember(source);
        }

        public TeamMember[] newArray(int size) {
            return new TeamMember[size];
        }
    };
}
