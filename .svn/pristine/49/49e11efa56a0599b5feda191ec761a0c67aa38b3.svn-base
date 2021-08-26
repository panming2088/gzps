package com.augurit.agmobile.gzps.uploadfacility.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.esri.core.geometry.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * 包名：com.augurit.agmobile.gzps.review.dao
 * 类描述：
 * 创建人：luobiao
 * 创建时间：2019/6/10 14:59
 * 修改人：luobiao
 * 修改时间：2019/6/10 14:59
 * 修改备注：
 */
public class PipeBean implements Parcelable {
    private String id;
    /**
     * 坐标x,y
     */
    private double x;
    private double y;
    private double endX;
    private double endY;
    private transient Geometry geometry;
    private String objectId;
    private String direction;//方向
    private String pipeType;//管线类型
    private String oldDirection;//原方向
    private String oldPipeType;//原管线类型
    private transient String componentType;//类型
    private Long markTime;
    private Long updateTime;
    private String directOrgName;
    private String parentOrgName;
    private String markPerson;
    private String Description;
    private String checkState;
    private String lackType;
    private List<PipeBean> children;
    private double lineLength;
    private double startXcoord;
    private double startYcoord;
    private double endXcoord;
    private double endYcoord;
    private String pipeGj;
    private String sfpsdyhxn = "0"; // 是否属于排水单元红线内设施

    public String getSuperviseOrgName() {
        return superviseOrgName;
    }

    public void setSuperviseOrgName(String superviseOrgName) {
        this.superviseOrgName = superviseOrgName;
    }

    private String superviseOrgName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPipeType() {
        return pipeType;
    }

    public void setPipeType(String pipeType) {
        this.pipeType = pipeType;
    }

    public String getOldDirection() {
        return oldDirection;
    }

    public void setOldDirection(String oldDirection) {
        this.oldDirection = oldDirection;
    }

    public String getOldPipeType() {
        return oldPipeType;
    }

    public void setOldPipeType(String oldPipeType) {
        this.oldPipeType = oldPipeType;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public Long getMarkTime() {
        return markTime;
    }

    public void setMarkTime(Long markTime) {
        this.markTime = markTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getDirectOrgName() {
        return directOrgName;
    }

    public void setDirectOrgName(String directOrgName) {
        this.directOrgName = directOrgName;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }

    public String getMarkPerson() {
        return markPerson;
    }

    public void setMarkPerson(String markPerson) {
        this.markPerson = markPerson;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public List<PipeBean> getChildren() {
        return children;
    }

    public void setChildren(List<PipeBean> children) {
        this.children = children;
    }

    public String getLackType() {
        return lackType;
    }

    public void setLackType(String lackType) {
        this.lackType = lackType;
    }

    public double getLineLength() {
        return lineLength;
    }

    public void setLineLength(double lineLength) {
        this.lineLength = lineLength;
    }

    public double getStartXcoord() {
        return startXcoord;
    }

    public void setStartXcoord(double startXcoord) {
        this.startXcoord = startXcoord;
    }

    public double getStartYcoord() {
        return startYcoord;
    }

    public void setStartYcoord(double startYcoord) {
        this.startYcoord = startYcoord;
    }

    public double getEndXcoord() {
        return endXcoord;
    }

    public void setEndXcoord(double endXcoord) {
        this.endXcoord = endXcoord;
    }

    public double getEndYcoord() {
        return endYcoord;
    }

    public void setEndYcoord(double endYcoord) {
        this.endYcoord = endYcoord;
    }

    public String getPipeGj() {
        return pipeGj;
    }

    public void setPipeGj(String pipeGj) {
        this.pipeGj = pipeGj;
    }

    public String isSfpsdyhxn() {
        return sfpsdyhxn;
    }

    public void setSfpsdyhxn(String sfpsdyhxn) {
        this.sfpsdyhxn = sfpsdyhxn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeDouble(this.x);
        dest.writeDouble(this.y);
        dest.writeDouble(this.endX);
        dest.writeDouble(this.endY);
        dest.writeString(this.objectId);
        dest.writeString(this.direction);
        dest.writeString(this.pipeType);
        dest.writeString(this.oldDirection);
        dest.writeString(this.oldPipeType);
        dest.writeValue(this.markTime);
        dest.writeValue(this.updateTime);
        dest.writeString(this.directOrgName);
        dest.writeString(this.parentOrgName);
        dest.writeString(this.markPerson);
        dest.writeString(this.Description);
        dest.writeString(this.checkState);
        dest.writeString(this.lackType);
        dest.writeList(this.children);
        dest.writeDouble(this.lineLength);
        dest.writeDouble(this.startXcoord);
        dest.writeDouble(this.startYcoord);
        dest.writeDouble(this.endXcoord);
        dest.writeDouble(this.endYcoord);
        dest.writeString(this.pipeGj);
        dest.writeString(this.superviseOrgName);
        dest.writeString(this.sfpsdyhxn);
    }

    public PipeBean() {
    }

    protected PipeBean(Parcel in) {
        this.id = in.readString();
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.endX = in.readDouble();
        this.endY = in.readDouble();
        this.objectId = in.readString();
        this.direction = in.readString();
        this.pipeType = in.readString();
        this.oldDirection = in.readString();
        this.oldPipeType = in.readString();
        this.markTime = (Long) in.readValue(Long.class.getClassLoader());
        this.updateTime = (Long) in.readValue(Long.class.getClassLoader());
        this.directOrgName = in.readString();
        this.parentOrgName = in.readString();
        this.markPerson = in.readString();
        this.Description = in.readString();
        this.checkState = in.readString();
        this.lackType = in.readString();
        this.children = new ArrayList<PipeBean>();
        in.readList(this.children, PipeBean.class.getClassLoader());
        this.lineLength = in.readDouble();
        this.startXcoord = in.readDouble();
        this.startYcoord = in.readDouble();
        this.endXcoord = in.readDouble();
        this.endYcoord = in.readDouble();
        this.pipeGj = in.readString();
        this.superviseOrgName = in.readString();
        this.sfpsdyhxn = in.readString();
    }

    public static final Parcelable.Creator<PipeBean> CREATOR = new Parcelable.Creator<PipeBean>() {
        @Override
        public PipeBean createFromParcel(Parcel source) {
            return new PipeBean(source);
        }

        @Override
        public PipeBean[] newArray(int size) {
            return new PipeBean[size];
        }
    };
}
