package com.augurit.agmobile.gzps.track.model;

import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.am.fw.db.liteorm.db.annotation.Ignore;
import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.annotation.Table;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;

import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.model
 * @createTime 创建时间 ：2017-07-31
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-31
 * @modifyMemo 修改备注：
 * <p>
 * "id": 46,
 * "markPerson": "系统管理员",
 * "parentOrgName": "广州市水务局",
 * "state": "1",
 * "recordLength": 457,
 * "markTime": 1531211925000,
 * "startTime": 1531211578000,
 * "endTime": 1531211925000
 */
@Table("Track")
public class Track {

    @PrimaryKey(AssignType.BY_MYSELF)
    private long id;
    private String trackName;   //当前的轨迹名称
    private String loginName;
    private int trackTime;
    private long startTime;     //开始时间戳
    private long endTime;       //结束时间戳
    private double recordLength;        //轨迹长度
    @Ignore
    private List<GPSTrack> gpsTrackList;  //轨迹点

    private double lastLon;
    private double lastLat;

    public double getLastLon() {
        return lastLon;
    }

    public void setLastLon(double lastLon) {
        this.lastLon = lastLon;
    }

    public double getLastLat() {
        return lastLat;
    }

    public void setLastLat(double lastLat) {
        this.lastLat = lastLat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTrackName() {
        return trackName;
    }

    public int getTrackTime() {
        return trackTime;
    }

    public void setTrackTime(int trackTime) {
        this.trackTime = trackTime;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public double getRecordLength() {
        return recordLength;
    }

    public void setRecordLength(double recordLength) {
        this.recordLength = recordLength;
    }

    public List<GPSTrack> getGpsTrackList() {
        return gpsTrackList;
    }

    public void setGpsTrackList(List<GPSTrack> gpsTrackList) {
        this.gpsTrackList = gpsTrackList;
    }
}
