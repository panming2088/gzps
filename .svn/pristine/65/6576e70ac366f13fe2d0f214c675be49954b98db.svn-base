package com.augurit.agmobile.mapengine.gpsstrace.model;

import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.annotation.Table;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * GPS轨迹实体类
 *
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.gpsstrace.model
 * @createTime 创建时间 ：2017-02-10
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-10
 * @modifyMemo 修改备注：
 * @version 1.0
 */
/**
 * 服务器返回字段：
 * "id": 6645,
 "markPerson": "系统管理员",
 "parentOrgName": "广州市水务局",
 "state": "1",
 "recordLength": 0,
 "x": 113.350347,
 "y": 113.350347,
 "markTime": 1531211656000
 */

@Table("GPSTrack")
public class GPSTrack implements Serializable {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private long id;
    private long trackId;
    private String trackName;   //当前的轨迹名称
    private String userName;

    @SerializedName("x")
    private double longitude;   //该点的经度
    @SerializedName("y")
    private double latitude;    //该点的纬度
    private long recordDate;       //记录该点时的时间
    private int recordLength;     //从开始记录到当前点所经过的路径长度
    private int pointState;    //点的状态，是起始点，中间点还是结束点
    private int uploadState = 0;   //提交状态，1已提交，0未提交

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(long recordDate) {
        this.recordDate = recordDate;
    }

    public int getRecordLength() {
        return recordLength;
    }

    public void setRecordLength(int recordLength) {
        this.recordLength = recordLength;
    }

    public int getPointState() {
        return pointState;
    }

    public void setPointState(int pointState) {
        this.pointState = pointState;
    }

    public int getUploadState() {
        return uploadState;
    }

    public void setUploadState(int uploadState) {
        this.uploadState = uploadState;
    }
}
