package com.augurit.agmobile.mapengine.map;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.am.intro.map
 * @createTime 创建时间 ：2017-01-11
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-11 16:52
 */
public class MapOptions implements Parcelable {
    public static final Creator<MapOptions> CREATOR = new Creator<MapOptions>() {
        public MapOptions createFromParcel(Parcel in) {
            return new MapOptions(in);
        }

        public MapOptions[] newArray(int size) {
            return new MapOptions[size];
        }
    };

    public MapOptions() {
    }

    private MapOptions(Parcel in) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
