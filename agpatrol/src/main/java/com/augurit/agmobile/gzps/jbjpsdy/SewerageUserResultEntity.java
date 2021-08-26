package com.augurit.agmobile.gzps.jbjpsdy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * 排水户实体类
 *
 * @PROJECT GZPS
 * @USER Augurit
 * @CREATE 2020/11/9 10:53
 */
public class SewerageUserResultEntity {
    private int lifeCount = 0;
    private int dangerCount = 0;
    private int precipitateCount = 0;
    private int cateringTradeCount = 0;
    private int totalCount = 0;
    private List<SewerageUserEntity> sewerageUserEntities;

    public int getLifeCount() {
        return lifeCount;
    }

    public void setLifeCount(int lifeCount) {
        this.lifeCount = lifeCount;
    }

    public int getDangerCount() {
        return dangerCount;
    }

    public void setDangerCount(int dangerCount) {
        this.dangerCount = dangerCount;
    }

    public int getPrecipitateCount() {
        return precipitateCount;
    }

    public void setPrecipitateCount(int precipitateCount) {
        this.precipitateCount = precipitateCount;
    }

    public int getCateringTradeCount() {
        return cateringTradeCount;
    }

    public void setCateringTradeCount(int cateringTradeCount) {
        this.cateringTradeCount = cateringTradeCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<SewerageUserEntity> getSewerageUserEntities() {
        return sewerageUserEntities;
    }

    public void setSewerageUserEntities(List<SewerageUserEntity> sewerageUserEntities) {
        this.sewerageUserEntities = sewerageUserEntities;
    }
}
