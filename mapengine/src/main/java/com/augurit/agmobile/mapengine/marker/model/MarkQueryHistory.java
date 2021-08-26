package com.augurit.agmobile.mapengine.marker.model;

import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.annotation.Table;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;

/**
 * 搜索历史
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.mark.model
 * @createTime 创建时间 ：2016-12-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-20
 */
@Table("MarkQueryHistory")
public class MarkQueryHistory {
    @PrimaryKey(AssignType.BY_MYSELF)
    String history;

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
