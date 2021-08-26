package com.augurit.agmobile.gzps.test;

import com.augurit.agmobile.mapengine.common.agmobilelayer.AGWMTSLayer;
import com.augurit.agmobile.mapengine.common.agmobilelayer.model.AGWMTSLayerInfo;
import com.augurit.agmobile.mapengine.common.agmobilelayer.model.CapabilitiesBean;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ITileCacheHelper;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ResolutionScalesUtil;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.TdtLayerFactory;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import java.util.List;

/**
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.agmobile.mapengine.common.agmobilelayer
 * @createTime 创建时间 ：2017-08-25
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-08-25 16:07
 */
public class TestTdtLayer extends AGWMTSLayer {

    private int originalScaleLength = 0;  //地图服务中原有的scale个数

    public TestTdtLayer(String url) {
        this("0", url, null, true);
    }

    public TestTdtLayer(String layerId, String url, ITileCacheHelper cachePathHelper, boolean initLayer) {
        super(layerId, url, cachePathHelper, initLayer);
    }

    @Override
    protected void setOrginFullExtent(CapabilitiesBean capabilitiesBean) {
        if (capabilitiesBean == null) {
            return;
        }
        if (String.valueOf(TdtLayerFactory.SRID_2000).equals(capabilitiesBean.getSpatialReference())) {
            this.mOrgin = TdtLayerFactory.ORIGIN_2000;
        } else {
            this.mOrgin = TdtLayerFactory.ORIGIN_MERCATOR;
        }
        if (this.mOrgin != null) {
            capabilitiesBean.setTopLeftCorner(this.mOrgin);
        }
        if (this.mFullExtent != null) {
            capabilitiesBean.setLowerCornerPoint(new Point(this.mFullExtent.getXMin(), this.mFullExtent.getYMin()));
            capabilitiesBean.setUpperCornerPoint(new Point(this.mFullExtent.getXMax(), this.mFullExtent.getYMax()));
        }
    }

    @Override
    protected void initWMTSLayerInfo(CapabilitiesBean capabilitiesBean) {
        if(capabilitiesBean == null){
            return;
        }
        if (mBaseUrl.contains("tianditu.com") && capabilitiesBean != null) {
            mBaseUrl = TdtLayerFactory.NORMAL_URL + capabilitiesBean.getLayer() + TdtLayerFactory.UNDERLINE + capabilitiesBean.getTileMatrixSet() + TdtLayerFactory.NORMAL_URL_ENDING;
        }
        int dpi = TdtLayerFactory.DEFAULT_DPI;
        SpatialReference spatialReference = SpatialReference.create(Integer.valueOf(capabilitiesBean.getSpatialReference()));
        List<Double> scales = capabilitiesBean.getScales();
        int num = scales.size();
        originalScaleLength = num;
        double[] resolution = new double[num+6];
        for (int i = 0; i < num; i++) {
            resolution[i] = ResolutionScalesUtil.getResolutionsFromScales(scales.get(i),
                    dpi,
                    spatialReference);
        }
        resolution[num] = resolution[num-1] / 2;
        resolution[num+1] = resolution[num] / 2;
        resolution[num+2] = resolution[num+1] / 2;
        resolution[num+3] = resolution[num+2] / 2;
        resolution[num+4] = resolution[num+3] / 2;
        resolution[num+5] = resolution[num+4] / 2;
        scales.add(ResolutionScalesUtil.getScalesFromResolutions(resolution[num], dpi, spatialReference));
        scales.add(ResolutionScalesUtil.getScalesFromResolutions(resolution[num+1], dpi, spatialReference));
        scales.add(ResolutionScalesUtil.getScalesFromResolutions(resolution[num+2], dpi, spatialReference));
        scales.add(ResolutionScalesUtil.getScalesFromResolutions(resolution[num+3], dpi, spatialReference));
        scales.add(ResolutionScalesUtil.getScalesFromResolutions(resolution[num+4], dpi, spatialReference));
        scales.add(ResolutionScalesUtil.getScalesFromResolutions(resolution[num+5], dpi, spatialReference));
        capabilitiesBean.setScales(scales);
        mWMTSLayerInfo = new AGWMTSLayerInfo(mBaseUrl, capabilitiesBean);
        mWMTSLayerInfo.setMaxZoomLevel(mWMTSLayerInfo.getMaxZoomLevel() + 6);
        mWMTSLayerInfo.setDomainsLength(TdtLayerFactory.DOMAINS_LENGTH);
    }

    @Override
    protected byte[] getTile(int level, int col, int rol) throws Exception {
        byte[] originalBytes = super.getTile(level, col, rol);
        if(
//                originalBytes != null
//                || level < 0
//                &&
        level < originalScaleLength - 1){
            return originalBytes;
        }
        //获取上一级的瓦片，主要是管点需要大级别，但底图级别不够大，所以大级别要显示上一级的瓦片，
        //这样大级别上要能看瓦片，只是会模糊
        int fallbackZoom = level - 1;
        int fallbackCol = (int) Math.floor(col / 2.0f);
        int fallbackRow = (int) Math.floor(rol / 2.0f);
        return this.getTile(fallbackZoom, fallbackCol, fallbackRow);
    }


}
