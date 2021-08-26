package com.augurit.agmobile.patrolcore.survey.model;

/**
 * 网格实体类
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.model
 * @createTime 创建时间 ：2017/9/21
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017/9/21
 * @modifyMemo 修改备注：
 */

public class GridItem {

    private String id;

    private String name;

    private String town;

    private String village;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
