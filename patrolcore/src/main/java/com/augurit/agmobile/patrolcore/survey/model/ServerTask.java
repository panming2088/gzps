package com.augurit.agmobile.patrolcore.survey.model;

import android.os.Parcel;
import android.os.Parcelable;


import com.augurit.am.fw.db.liteorm.db.annotation.Ignore;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 服务端传递过来的任务实体类
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agsurvey.tasks.model
 * @createTime 创建时间 ：17/8/22
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/22
 * @modifyMemo 修改备注：
 */

public class ServerTask implements Serializable {


    String taskName; //任务名称

    @PrimaryKey
    @SerializedName("id")
    String id; //任务id  taskId

    String stat = "0"; //核查状态

    private String userId; //该任务指派给的对象,用于本地保存时区分不同用户的任务

    // private String taskType;//任务类型

    private String dirId;

    @SerializedName("taskType")
    private String name; //任务类型名字

    private String town;

    private String publishTime;

    private String village;

    private String street;

    private String mph; //栋号

    private String wangge;

    private String offlineSaveTime ; //上次下载的时间，用于下载任务界面显示

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getWangge() {
        return wangge;
    }

    public void setWangge(String wangge) {
        this.wangge = wangge;
    }

    //列表
    private ArrayList<ServerTable> serverTables; //该任务下的所有表单

    public String getDirId() {
        return dirId;
    }

    public void setDirId(String dirId) {
        this.dirId = dirId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<ServerTable> getServerTables() {
        return serverTables;
    }

    public void setServerTables(ArrayList<ServerTable> serverTables) {
        this.serverTables = serverTables;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getMph() {
        return mph;
    }

    public void setMph(String mph) {
        this.mph = mph;
    }

    public String getOfflineSaveTime() {
        return offlineSaveTime;
    }

    public void setOfflineSaveTime(String offlineSaveTime) {
        this.offlineSaveTime = offlineSaveTime;
    }
}
