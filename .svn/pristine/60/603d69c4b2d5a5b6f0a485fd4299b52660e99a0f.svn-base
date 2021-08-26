package com.augurit.agmobile.mapengine.addrsearch.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 模糊查询结果
 * @author 创建人 ：guokunhu
 * @package 包名 ：com.augurit.am.map.arcgis.addrq.bean
 * @createTime 创建时间 ：2017-01-17
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-01-20
 */
public class LocationSearchSuggestion implements Parcelable {

    private String mSugguestString;

    public LocationSearchSuggestion(String suggestionResults) {
        mSugguestString = suggestionResults;
    }

    public String getBody() {
        return mSugguestString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mSugguestString);
    }

    protected LocationSearchSuggestion(Parcel in) {
        this.mSugguestString = in.readString();
    }

    public static final Parcelable.Creator<LocationSearchSuggestion> CREATOR = new Parcelable.Creator<LocationSearchSuggestion>() {
        @Override
        public LocationSearchSuggestion createFromParcel(Parcel source) {
            return new LocationSearchSuggestion(source);
        }

        @Override
        public LocationSearchSuggestion[] newArray(int size) {
            return new LocationSearchSuggestion[size];
        }
    };
}


