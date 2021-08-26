package com.augurit.agmobile.mapengine.marker.model;

import com.augurit.am.fw.db.liteorm.db.annotation.Ignore;
import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.annotation.Table;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;
import com.esri.core.geometry.Geometry;
import com.esri.core.symbol.Symbol;

/**
 * 离线保存的标注信息
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.marker.model
 * @createTime 创建时间 ：2017-04-14
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-14
 * @modifyMemo 修改备注：
 */
@Table("localmark")
public class LocalMark {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private Integer  id;
    private String markName;
    private String markMemo;
    private Geometry geometry;
    private int lineWidth;
    private String inColor;
    private String lineColor;
    private String createPerson;
    private Long createDate;
    private String pointDrawableName; //暂时这么用于解决点标注的问题


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

    public String getMarkMemo() {
        return markMemo;
    }

    public void setMarkMemo(String markMemo) {
        this.markMemo = markMemo;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public String getInColor() {
        return inColor;
    }

    public void setInColor(String inColor) {
        this.inColor = inColor;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public String getPointDrawableName() {
        return pointDrawableName;
    }

    public void setPointDrawableName(String pointDrawableName) {
        this.pointDrawableName = pointDrawableName;
    }
}
