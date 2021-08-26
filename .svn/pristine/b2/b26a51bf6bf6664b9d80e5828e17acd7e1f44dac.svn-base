package com.augurit.agmobile.mapengine.layermanage.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.esri.core.geometry.Envelope;

import java.util.ArrayList;
import java.util.List;

/**
 * AGMobile中的图层信息类
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.layer.model
 * @createTime 创建时间 ：2016-10-14 13:57
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2016-10-14 13:57
 */

@Keep
public class LayerInfo implements Parcelable ,Comparable,Cloneable{

    List<LayerInfo> childLayer;//它的子Layer


    int layerId;
    /**
     * 调用agcom提供的接口时需要用到；
     */
    int projectLayerId;
    /**
     * 图层名称
     */
    String layerName;

    /**
     * Layer类型，是动态还是瓦片还是其他
     */
    LayerType type;

    /**
     * 是否在图层列表中显示该图层
     */
    boolean ifShowInLayerList;

    /**
     * 加载图层的url
     */
    String url;

    int layerOrder;

    //String parentName;

    boolean defaultVisibility; //是否显示

    //boolean exportTileAllow;

    String dirTypeName;//图层所对应的组名

    //String parentId;

    /**
     * 获取图例的Url地址
     */
    String symbolUrl;

    /**
     * 图层透明度
     */
    float opacity;

    /**
     * 图层的作用：1：表示360全景图层:
     * 2：表示这个图层是编辑图层，3表示这个图层是点标注图层，
     * 4：表示这个图层是GPS轨迹图层，另外，0：表示这个功能图层为普通图层,
     */
    int remarkFunc;

    // boolean ifSupported ;//是否支持加载，WFS图层暂时不支持加载
    /**
     * 是否是底图
     */
    boolean isBaseMap;

    /**
     * 是否可查询
     */
    boolean isQueryable;

    /**
     * 初始缩放范围,暂时没用到，初始缩放范围采用中心点+初始缩放级别确定
     */
    Envelope initialExtent;

    /**
     * 最大缩放范围,暂时没用到
     */
    Envelope maxExtent;

    //SpatialReference mSpatialReference ;//空间参考


    //double maxScale ;//最大缩放比例

    //double minScale ;//最小缩放比例

    /**
     * 字段说明：是否属于agcom返回的图层列表中的Tile数组;添加该字段的原因是：agcom返回的图层列表，在tile数组中的图层要放置在图层列表中的最下方，
     * 考虑过采用isBaseMap字段来显示，但是，发现存在放置在tile数组中的图层的isBaseMap为false(不是底图)的情况，最后考虑在这里加入isBelongToTile这个字段
     */
    boolean isBelongToTile;

    /**
     * 目录排序，如果是0，表示不需要排序，否则按照从小到大排序
     */
    private int dirOrder;

    /**
     * 图层对应的表名
     */
    private String layerTable;

    /**
     * 图层中心点
     */
    private LatLng center;

    /**
     * 初始缩放比例
     */
    private double initScale;

    /**
     * 父图层的layerId（当图层类型是{@link LayerType#DynamicLayer_SubLayer}时使用）
     */
    private String parentLayerId;

    // private String downloadId;

    /*public String getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(String downloadId) {
        this.downloadId = downloadId;
    }*/

    //   private String localDirName;//文件夹名称

    /*public boolean isDefaultVisibility() {
        return defaultVisibility;
    }*/

    public String getParentLayerId() {
        return parentLayerId;
    }

    public void setParentLayerId(String parentLayerId) {
        this.parentLayerId = parentLayerId;
    }

    public LatLng getCenter() {
        return center;
    }

    public void setCenter(LatLng center) {
        this.center = center;
    }

    public double getInitScale() {
        return initScale;
    }

    public void setInitScale(double initScale) {
        this.initScale = initScale;
    }

    public int getDirOrder() {
        return dirOrder;
    }

    public void setDirOrder(int dirOrder) {
        this.dirOrder = dirOrder;
    }

    public boolean isBelongToTile() {
        return isBelongToTile;
    }

    public void setBelongToTile(boolean belongToTile) {
        isBelongToTile = belongToTile;
    }

    public boolean isBaseMap() {
        return isBaseMap;
    }

    public void setBaseMap(boolean baseMap) {
        isBaseMap = baseMap;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

/*    public String getParentName() {
        return parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }*/

  /*  public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }*/

    public String getSymbolUrl() {
        return symbolUrl;
    }

    public void setSymbolUrl(String symbolUrl) {
        this.symbolUrl = symbolUrl;
    }

    public String getDirTypeName() {
        return dirTypeName;
    }

    public void setDirTypeName(String dirTypeName) {
        this.dirTypeName = dirTypeName;
    }

    public List<LayerInfo> getChildLayer() {
        return childLayer;
    }

    public void setChildLayer(List<LayerInfo> childLayer) {
        this.childLayer = childLayer;
    }

    public boolean isDefaultVisible() {
        return defaultVisibility;
    }

    public void setDefaultVisibility(boolean defaultVisibility) {
        this.defaultVisibility = defaultVisibility;
    }

    /*public boolean isExportTileAllow() {
        return exportTileAllow;
    }

    public void setExportTileAllow(boolean exportTileAllow) {
        this.exportTileAllow = exportTileAllow;
    }*/

    public int getLayerId() {
        return layerId;
    }

    public void setLayerId(int layerId) {
        this.layerId = layerId;
    }

    public int getProjectLayerId() {
        return projectLayerId;
    }

    public void setProjectLayerId(int projectLayerId) {
        this.projectLayerId = projectLayerId;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public int getLayerOrder() {
        return layerOrder;
    }

    public void setLayerOrder(int layerOrder) {
        this.layerOrder = layerOrder;
    }

    public LayerType getType() {
        return type;
    }

    public void setType(LayerType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRemarkFunc() {
        return remarkFunc;
    }

    public void setRemarkFunc(int remarkFunc) {
        this.remarkFunc = remarkFunc;
    }

    public Envelope getInitialExtent() {
        return initialExtent;
    }

    public void setInitialExtent(Envelope initialExtent) {
        this.initialExtent = initialExtent;
    }

    public Envelope getMaxExtent() {
        return maxExtent;
    }

    public void setMaxExtent(Envelope maxExtent) {
        this.maxExtent = maxExtent;
    }

    /*public SpatialReference getSpatialReference() {
        return mSpatialReference;
    }

    public void setSpatialReference(SpatialReference spatialReference) {
        mSpatialReference = spatialReference;
    }*/


    public boolean isIfShowInLayerList() {
        return ifShowInLayerList;
    }

    public void setIfShowInLayerList(boolean ifShowInLayerList) {
        this.ifShowInLayerList = ifShowInLayerList;
    }

    public boolean isQueryable() {
        return isQueryable;
    }

    public void setQueryable(boolean queryable) {
        isQueryable = queryable;
    }

   /* public double getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(double maxScale) {
        this.maxScale = maxScale;
    }

    public double getMinScale() {
        return minScale;
    }

    public void setMinScale(double minScale) {
        this.minScale = minScale;
    }*/

    public String getLayerTable() {
        return layerTable;
    }

    public void setLayerTable(String layerTable) {
        this.layerTable = layerTable;
    }

    /*public String getLocalDirName() {
        return localDirName;
    }

    public void setLocalDirName(String localDirName) {
        this.localDirName = localDirName;
    }*/

    @Override
    public boolean equals(Object o) {
        if (o instanceof LayerInfo) {
            LayerInfo info = (LayerInfo) o;
            if (info.getLayerId() == this.layerId
                    && info.getUrl() != null
                    && info.getUrl().equals(this.url)) {
                //如果layerId和url都相同就认为是同一个Layer
                return true;
            }
        } else {
            //如果都不是AMLayerInfo，直接返回false
            return false;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.childLayer);
        dest.writeInt(this.layerId);
        dest.writeInt(this.projectLayerId);
        dest.writeString(this.layerName);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeByte(this.ifShowInLayerList ? (byte) 1 : (byte) 0);
        dest.writeString(this.url);
        dest.writeInt(this.layerOrder);
        //  dest.writeString(this.parentName);
        dest.writeByte(this.defaultVisibility ? (byte) 1 : (byte) 0);
       // dest.writeByte(this.exportTileAllow ? (byte) 1 : (byte) 0);
        dest.writeString(this.dirTypeName);
        // dest.writeString(this.parentId);
        dest.writeString(this.symbolUrl);
        dest.writeFloat(this.opacity);
        dest.writeInt(this.remarkFunc);
        dest.writeByte(this.isBaseMap ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isQueryable ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.initialExtent);
        dest.writeSerializable(this.maxExtent);
        //  dest.writeSerializable(this.mSpatialReference);
        //   dest.writeDouble(this.maxScale);
        //   dest.writeDouble(this.minScale);
        dest.writeByte(this.isBelongToTile ? (byte) 1 : (byte) 0);
        dest.writeInt(this.dirOrder);
        dest.writeString(this.layerTable);
        //  dest.writeString(this.downloadId);
        //   dest.writeString(this.localDirName);
    }

    public LayerInfo() {
    }

    protected LayerInfo(Parcel in) {
        this.childLayer = new ArrayList<LayerInfo>();
        in.readList(this.childLayer, LayerInfo.class.getClassLoader());
        this.layerId = in.readInt();
        this.projectLayerId = in.readInt();
        this.layerName = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : LayerType.values()[tmpType];
        this.ifShowInLayerList = in.readByte() != 0;
        this.url = in.readString();
        this.layerOrder = in.readInt();
        //  this.parentName = in.readString();
        this.defaultVisibility = in.readByte() != 0;
        //this.exportTileAllow = in.readByte() != 0;
        this.dirTypeName = in.readString();
        //   this.parentId = in.readString();
        this.symbolUrl = in.readString();
        this.opacity = in.readFloat();
        this.remarkFunc = in.readInt();
        this.isBaseMap = in.readByte() != 0;
        this.isQueryable = in.readByte() != 0;
        this.initialExtent = (Envelope) in.readSerializable();
        this.maxExtent = (Envelope) in.readSerializable();
        //   this.mSpatialReference = (SpatialReference) in.readSerializable();
        //    this.maxScale = in.readDouble();
        //    this.minScale = in.readDouble();
        this.isBelongToTile = in.readByte() != 0;
        this.dirOrder = in.readInt();
        this.layerTable = in.readString();
        // this.downloadId = in.readString();
        //   this.localDirName = in.readString();
    }

    public static final Parcelable.Creator<LayerInfo> CREATOR = new Parcelable.Creator<LayerInfo>() {
        @Override
        public LayerInfo createFromParcel(Parcel source) {
            return new LayerInfo(source);
        }

        @Override
        public LayerInfo[] newArray(int size) {
            return new LayerInfo[size];
        }
    };

    @Override
    public int hashCode() {
        return layerName.hashCode() * layerId;
    }

    @Override
    public int compareTo(Object obj) {
        if(!(obj instanceof LayerInfo))
            throw new RuntimeException();
        LayerInfo p = (LayerInfo)obj;
        return this.layerOrder-p.layerOrder==0?this.layerId-p.layerId:this.layerOrder-p.layerOrder;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}