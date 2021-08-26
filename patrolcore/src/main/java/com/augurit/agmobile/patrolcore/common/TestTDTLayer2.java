package com.augurit.agmobile.patrolcore.common;

import com.augurit.agmobile.mapengine.common.agmobilelayer.TdtLayer;
import com.augurit.agmobile.mapengine.common.agmobilelayer.model.CapabilitiesBean;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ITileCacheHelper;
import com.esri.core.geometry.SpatialReference;

/**
 * 包名：com.augurit.agmobile.gzps.test
 * 类描述：
 * 创建人：luobiao
 * 创建时间：2019/7/9 17:05
 * 修改人：luobiao
 * 修改时间：2019/7/9 17:05
 * 修改备注：
 */
public class TestTDTLayer2 extends TdtLayer{

    public TestTDTLayer2(String url) {
        super(url);
    }

    public TestTDTLayer2(String layerId, String url, ITileCacheHelper cachePathHelper, boolean initLayer) {
        super(layerId, url, cachePathHelper, initLayer);
    }

    @Override
    protected void initWMTSLayerInfo(CapabilitiesBean capabilitiesBean) {
        super.initWMTSLayerInfo(capabilitiesBean);
        mWMTSLayerInfo.setSpatialReference(SpatialReference.create(4326) );
    }
}
