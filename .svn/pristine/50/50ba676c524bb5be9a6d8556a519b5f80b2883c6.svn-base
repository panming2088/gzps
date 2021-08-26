package com.augurit.agmobile.mapengine.addrsearch.model;

import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.annotation.Table;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;


/**
 * 历史搜索数据实体类
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.addrq.bean
 * @createTime 创建时间 ：2017-01-17
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-01-17
 */
@Table("locationsearch")
public class SearchHistory {

    @PrimaryKey(AssignType.BY_MYSELF)
    String history;

    boolean isDetailedAddress;//是否是详细地址

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public boolean isDetailedAddress() {
        return isDetailedAddress;
    }

    public void setDetailedAddress(boolean detailedAddress) {
        isDetailedAddress = detailedAddress;
    }
}
