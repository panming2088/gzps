package com.augurit.agmobile.mapengine.common.agmobilelayer.model;

import android.text.TextUtils;

import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ResolutionScalesUtil;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.TdtLayerFactory;
import com.augurit.am.fw.utils.ListUtil;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import java.util.List;

/**
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.agmobile.mapengine.common.agmobilelayer.model
 * @createTime 创建时间 ：2017-08-11
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-08-11 16:03
 */
public class AGWMTSLayerInfo {
    protected int domainsLength;//有多个服务器的时候重写这个方法，使得随机从不同服务器获取瓦片
    private int minZoomLevel;//最小缩放级别
    private int maxZoomLevel;//最大缩放级别
    private Point origin;//切片原点
    //地图显示范围
    private double xMin;
    private double yMin;
    private double xMax;
    private double yMax;
    //每张瓦片大小
    private int tileSize;
    //比例尺
    private double[] scales;
    //分辨率
    private double[] resolutions;
    //每英寸多少像素
    private int dpi;
    private WMTSInfo wmtsInfo;
    private SpatialReference spatialReference;

    public AGWMTSLayerInfo(String baseUrl, CapabilitiesBean capabilitiesBean) {
        if (TextUtils.isEmpty(capabilitiesBean.getSpatialReference())) {
            capabilitiesBean.setSpatialReference("3857");
        }
        this.spatialReference = SpatialReference.create(Integer.valueOf(capabilitiesBean.getSpatialReference()));
        this.origin = capabilitiesBean.getTopLeftCorner();
        this.xMin = capabilitiesBean.getLowerCornerPoint().getX();
        this.yMin = capabilitiesBean.getLowerCornerPoint().getY();
        this.xMax = capabilitiesBean.getUpperCornerPoint().getX();
        this.yMax = capabilitiesBean.getUpperCornerPoint().getY();
        this.minZoomLevel = capabilitiesBean.getMinZoomLevel();
        this.maxZoomLevel = capabilitiesBean.getMaxZoomLevel();
        this.dpi = TdtLayerFactory.DEFAULT_DPI;
        this.tileSize = TdtLayerFactory.DEFAULT_TILESIZE;
        initScaleResolution(capabilitiesBean.getScales());
        this.wmtsInfo = new AGWMTSLayerInfo.WMTSInfo(baseUrl)
                .layer(capabilitiesBean.getLayer())
                .style(capabilitiesBean.getStyle())
                .tileMatrixSet(capabilitiesBean.getTileMatrixSet())
                .format(capabilitiesBean.getFormat())
                .version(capabilitiesBean.getVersion())
                .serviceMode(getServiceMode(capabilitiesBean));
    }

    private AGWMTSLayerInfo() {
    }



    private void initScaleResolution(List<Double> scaleList) {
        if (!ListUtil.isEmpty(scaleList)) {
            scales = new double[scaleList.size()];
            for (int i = 0; i < scaleList.size(); i++) {
                scales[i] = scaleList.get(i);
            }
            int num = scales.length;
            double[] resolution = new double[num];
            for (int i = 0; i < num; i++) {
                resolution[i] = ResolutionScalesUtil.getResolutionsFromScales(scales[i], dpi, spatialReference);
            }
            this.setResolutions(resolution);
        }
    }

    public SpatialReference getSpatialReference() {
        return spatialReference;
    }

    public void setSpatialReference(SpatialReference spatialReference) {
        this.spatialReference = spatialReference;
    }

    private AGWMTSLayerInfo.ServiceMode getServiceMode(CapabilitiesBean capabilitiesBean) {
        if (capabilitiesBean == null) {
            return ServiceMode.KVP;
        } else if (TextUtils.isEmpty(capabilitiesBean.getServiceMode())) {
            return ServiceMode.KVP;
        } else if (capabilitiesBean.getServiceMode().equalsIgnoreCase("KVP")) {
            return ServiceMode.KVP;
        } else if (capabilitiesBean.getServiceMode().equalsIgnoreCase("RESTful")) {
            return ServiceMode.RESTful;
        } else if (capabilitiesBean.getServiceMode().equals("TIANDITU")) {
            return ServiceMode.TIANDITU;
        }
        return AGWMTSLayerInfo.ServiceMode.KVP;
    }

    public int getMinZoomLevel() {
        return minZoomLevel;
    }

    public void setMinZoomLevel(int minZoomLevel) {
        this.minZoomLevel = minZoomLevel;
    }

    public int getMaxZoomLevel() {
        return maxZoomLevel;
    }

    public void setMaxZoomLevel(int maxZoomLevel) {
        this.maxZoomLevel = maxZoomLevel;
    }

    public double getxMin() {
        return xMin;
    }

    public void setxMin(double xMin) {
        this.xMin = xMin;
    }

    public double getyMin() {
        return yMin;
    }

    public void setyMin(double yMin) {
        this.yMin = yMin;
    }

    public double getxMax() {
        return xMax;
    }

    public void setxMax(double xMax) {
        this.xMax = xMax;
    }

    public double getyMax() {
        return yMax;
    }

    public void setyMax(double yMax) {
        this.yMax = yMax;
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

    public double[] getScales() {
        return scales;
    }

    public void setScales(double[] scales) {
        this.scales = scales;
    }

    public double[] getResolutions() {
        return resolutions;
    }

    public void setResolutions(double[] resolutions) {
        this.resolutions = resolutions;
    }

    public int getDpi() {
        return dpi;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    public Point getOrigin() {
        return origin;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public int getDomainsLength() {
        return domainsLength;
    }

    public void setDomainsLength(int domainsLength) {
        this.domainsLength = domainsLength;
    }

    public WMTSInfo getWmtsInfo() {
        return wmtsInfo;
    }

    public void setWmtsInfo(WMTSInfo wmtsInfo) {
        this.wmtsInfo = wmtsInfo;
    }

    public enum ServiceMode {
        KVP,
        RESTful,
        TIANDITU,
        CUSTOM
    }

    public static final class WMTSInfo {
        //WMTS服务基本url
        protected String url;
        //WMTS图层名
        protected String layer;
        protected String tileMatrixSet;
        //返回格式
        protected String format;

        //WMTS样式，默认default
        protected String style;
        //WMTS版本号默认1.0.0
        protected String version;
        protected int srid;
        protected ServiceMode serviceMode;
        protected SpatialReference spatialReference;

        public WMTSInfo(String aurl) {
            this.url = aurl;
        }

        public AGWMTSLayerInfo.WMTSInfo layer(String layer) {
            this.layer = layer;
            return this;
        }

        public AGWMTSLayerInfo.WMTSInfo srid(int srid) {
            this.srid = srid;
            return this;
        }

        public AGWMTSLayerInfo.WMTSInfo tileMatrixSet(String tileMatrixSet) {
            this.tileMatrixSet = tileMatrixSet;
            return this;
        }

        public AGWMTSLayerInfo.WMTSInfo format(String format) {
            this.format = format;
            return this;
        }

        public AGWMTSLayerInfo.WMTSInfo style(String style) {
            this.style = style;
            return this;
        }

        public AGWMTSLayerInfo.WMTSInfo version(String version) {
            this.version = version;
            return this;
        }

        public SpatialReference getSpatialReference() {
            return spatialReference;
        }

        public void setSpatialReference(SpatialReference spatialReference) {
            this.spatialReference = spatialReference;
        }

        public AGWMTSLayerInfo.WMTSInfo spatialReference(SpatialReference spatialReference) {
            this.spatialReference = spatialReference;
            return this;
        }

        public ServiceMode getServiceMode() {
            return serviceMode;
        }

        public AGWMTSLayerInfo.WMTSInfo serviceMode(ServiceMode serviceMode) {
            this.serviceMode = serviceMode;
            return this;
        }

        public String getFormat() {
            return format;
        }

        public String getLayer() {
            return layer;
        }

        public int getSrid() {
            return srid;
        }

        public String getStyle() {
            return style;
        }

        public String getTileMatrixSet() {
            return tileMatrixSet;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVersion() {
            return version;
        }
    }
}
