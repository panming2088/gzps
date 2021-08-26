package com.augurit.agmobile.patrolcore.common.model;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 部件
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.common.model
 * @createTime 创建时间 ：17/10/14
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/14
 * @modifyMemo 修改备注：
 */

public class Component implements Serializable {

    private static final long serialVersionUID = 12L;

    private String layerUrl;
    private String layerName;
    Graphic graphic;
//    List<Field> fields;
    Integer objectId;
    String displayFieldName;
//    SpatialReference e;
//    Geometry.Type f;
//    String g;
//    transient Map<String, Object> fieldAlias;
//    boolean i;
    String errorInfo;
    List<Photo> mPhotos;

    public boolean isAllowEditWellType() {
        return isAllowEditWellType;
    }

    public void setAllowEditWellType(boolean allowEditWellType) {
        isAllowEditWellType = allowEditWellType;
    }

    //是否允许用户修改窨井类型
    private boolean isAllowEditWellType;
    /**
     * 下游
     */
    private List<Component> downStream;

    /**
     * 上游
     */
    private List<Component> upStream;

    /**
     * 排放类型  1：污水 2:雨污合流 3： 雨水
     */
    private String emissionType;

    public String getLayerUrl() {
        return layerUrl;
    }

    public void setLayerUrl(String layerUrl) {
        this.layerUrl = layerUrl;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public void setGraphic(Graphic graphic) {
        this.graphic = graphic;
    }

    /*public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }*/

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getDisplayFieldName() {
        return displayFieldName;
    }

    public void setDisplayFieldName(String displayFieldName) {
        this.displayFieldName = displayFieldName;
    }

    /*public SpatialReference getE() {
        return e;
    }

    public void setE(SpatialReference e) {
        this.e = e;
    }

    public Geometry.Type getF() {
        return f;
    }

    public void setF(Geometry.Type f) {
        this.f = f;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public Map<String, Object> getFieldAlias() {
        return fieldAlias;
    }

    public void setFieldAlias(Map<String, Object> fieldAlias) {
        this.fieldAlias = fieldAlias;
    }

    public boolean isI() {
        return i;
    }

    public void setI(boolean i) {
        this.i = i;
    }*/

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<Photo> photos) {
        mPhotos = photos;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public List<Component> getDownStream() {
        return downStream;
    }

    public void setDownStream(List<Component> downStream) {
        this.downStream = downStream;
    }

    public List<Component> getUpStream() {
        return upStream;
    }

    public void setUpStream(List<Component> upStream) {
        this.upStream = upStream;
    }

    public String getEmissionType() {
        return emissionType;
    }

    public void setEmissionType(String emissionType) {
        this.emissionType = emissionType;
    }

    /**
     * 获取排放类型的颜色
     *
     * @return
     */
    public int getEmissionRepresentativeColor(Context context) {

        //1：污水 2:雨污合流 3： 雨水
        switch (this.emissionType) {
            case "1":
                return  ResourcesCompat.getColor(context.getResources(), R.color.agmobile_red, null);

            case "2":
                return  ResourcesCompat.getColor(context.getResources(), R.color.mark_light_purple, null);

            case "3":
                return  ResourcesCompat.getColor(context.getResources(), R.color.progress_line_green, null);

            default:
                return ResourcesCompat.getColor(context.getResources(), R.color.mark_drak_yellow, null);

        }
    }
}
