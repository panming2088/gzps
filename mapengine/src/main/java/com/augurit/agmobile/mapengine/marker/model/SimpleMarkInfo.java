package com.augurit.agmobile.mapengine.marker.model;

import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.annotation.Table;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;

/**
 * 保存简单的标注信息，主要用在标注查询的时候。一开始进入的时候，会把标注信息保存到数据库中，当
 * 进行标注查询的时候，根据markName进行查找满足的标注。
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.mark.model
 * @createTime 创建时间 ：2016-12-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-20
 */

@Table("SimpleMarkInfo")
public class SimpleMarkInfo {

    @PrimaryKey(AssignType.BY_MYSELF)
    int id;

    String markName;

    public SimpleMarkInfo(int id,String markName){
        setId(id);
        setMarkName(markName);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }
}
