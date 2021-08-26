package com.augurit.agmobile.mapengine.common.agmobilelayer;


import com.augurit.agmobile.mapengine.common.agmobilelayer.model.AGWMTSLayerInfo;
import com.augurit.agmobile.mapengine.common.agmobilelayer.model.CapabilitiesBean;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ITileCacheHelper;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.TdtLayerFactory;

/**
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.agmobile.mapengine.common.agmobilelayer
 * @createTime 创建时间 ：2017-08-25
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-08-25 16:07
 */
public class TdtLayer extends AGWMTSLayer {
    public TdtLayer(String url) {
        this("0", url, null, true);
    }

    public TdtLayer(String layerId, String url, ITileCacheHelper cachePathHelper, boolean initLayer) {
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
        super.setOrginFullExtent(capabilitiesBean);
    }

    @Override
    protected void initWMTSLayerInfo(CapabilitiesBean capabilitiesBean) {
        if (mBaseUrl.contains("tianditu.com") && capabilitiesBean != null) {
            //// TODO: 2017/10/25 【更新到研发版本】 {s}导致日志一直打印url错误
            String tempUrl = TdtLayerFactory.NORMAL_URL;
            int domainsLength = TdtLayerFactory.DOMAINS_LENGTH;
            if (domainsLength > 0) {
                int idx = (int) (Math.random() * (double) domainsLength);
                tempUrl = tempUrl.replace("{s}", idx + "");
            }
            mBaseUrl = tempUrl + capabilitiesBean.getLayer() + TdtLayerFactory.UNDERLINE + capabilitiesBean.getTileMatrixSet() + TdtLayerFactory.NORMAL_URL_ENDING;
        }
        mWMTSLayerInfo = new AGWMTSLayerInfo(mBaseUrl, capabilitiesBean);
        //        mWMTSLayerInfo.setDomainsLength(TdtLayerFactory.DOMAINS_LENGTH);
    }
}
