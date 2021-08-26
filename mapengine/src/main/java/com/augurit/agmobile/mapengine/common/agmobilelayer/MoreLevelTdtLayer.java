package com.augurit.agmobile.mapengine.common.agmobilelayer;

import com.augurit.agmobile.mapengine.common.agmobilelayer.model.AGWMTSLayerInfo;
import com.augurit.agmobile.mapengine.common.agmobilelayer.model.CapabilitiesBean;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ITileCacheHelper;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ResolutionScalesUtil;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.TdtLayerFactory;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.agmobile.mapengine.common.agmobilelayer
 * @createTime 创建时间 ：2017-08-25
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-08-25 16:07
 */
public class MoreLevelTdtLayer extends AGWMTSLayer {

    private int addLevels = 0;
    private int originalScaleLength = 0;  //地图服务中原有的scale个数

    /**
     * @param url
     * @param addLevels 要增加多少个级别
     */
    public MoreLevelTdtLayer(String url, int addLevels) {
        this("0", url, addLevels, null, true);
    }

    public MoreLevelTdtLayer(String layerId, String url, int addLevels, ITileCacheHelper cachePathHelper, boolean initLayer) {
        super(layerId, url, cachePathHelper, initLayer);
        this.addLevels = addLevels;
        getCapabilities();
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
        if (mBaseUrl.contains("tianditu.com") && capabilitiesBean != null) {
            String tempUrl = TdtLayerFactory.NORMAL_URL;
            int domainsLength = TdtLayerFactory.DOMAINS_LENGTH;
            if (domainsLength > 0) {
                int idx = (int) (Math.random() * (double) domainsLength);
                tempUrl = tempUrl.replace("{s}", idx + "");
            }
            mBaseUrl = tempUrl + capabilitiesBean.getLayer() + TdtLayerFactory.UNDERLINE + capabilitiesBean.getTileMatrixSet() + TdtLayerFactory.NORMAL_URL_ENDING;
        }
        if (capabilitiesBean != null){
            int dpi = TdtLayerFactory.DEFAULT_DPI;
            SpatialReference spatialReference = SpatialReference.create(Integer.valueOf(capabilitiesBean.getSpatialReference()));
            List<Double> scales = capabilitiesBean.getScales();
            if (!ListUtil.isEmpty(scales)){
                int num = scales.size();
                originalScaleLength = num;
                double[] resolution = new double[num + addLevels];
                for (int i = 0; i < num; i++) {
                    resolution[i] = ResolutionScalesUtil.getResolutionsFromScales(scales.get(i),
                            dpi,
                            spatialReference);
                }
                for (int i = 0; i < addLevels; ++i) {
                    resolution[num + i] = resolution[num + (i - 1)] / 2;
                    scales.add(ResolutionScalesUtil.getScalesFromResolutions(resolution[num + i], dpi, spatialReference));
                }
                capabilitiesBean.setScales(scales);
            }
            mWMTSLayerInfo = new AGWMTSLayerInfo(mBaseUrl, capabilitiesBean);
            mWMTSLayerInfo.setMaxZoomLevel(mWMTSLayerInfo.getMaxZoomLevel() + addLevels);
        }
        //        mWMTSLayerInfo.setDomainsLength(TdtLayerFactory.DOMAINS_LENGTH);
    }

    /**
     * * 1.找本地有没本级对应的level, col, rol缓存瓦片；
     * 2.第1步没有，找上一级的1/2坐标有没本地缓存瓦片；
     * 3.第2步没有，找上一级的1/2坐标有没网络瓦片；
     * 4.第3步没有，级别再减一级，进入2、3步的递归。
     *
     * @param trueLevel //arcgis 生成的level从0开始，天地图xml文件一般从1开始，也有从15开始等
     * @param col
     * @param rol
     * @return
     */
    protected byte[] getReplacingTile(int trueLevel, int col, int rol) throws Exception {
        /*//生成地图图片的URL
        String url = buildWMTSUrl(level, col, rol);*/
        //1.找本地有没本级对应的level, col, rol缓存瓦片；
        if (this.mCacheHelper != null) {
            //读取缓存文件
            byte[] bytes = mCacheHelper.getOfflineCacheFile(trueLevel, col, rol);
            if (bytes != null) { //本地已缓存该level, col, rol的瓦片
                mCacheHelper.addOfflineCacheFile(bytes, trueLevel, col, rol);
                return bytes;
            } else {//本地没有缓存该level, col, rol的瓦片
                //2.第1步没有，找上一级的1/2坐标有没本地缓存瓦片；
                int lastLevel = trueLevel - 1;
                if (lastLevel < mWMTSLayerInfo.getMinZoomLevel()) {
                    return null;
                }
                int lastCol = (int) Math.floor(col >> 1);
                int lastRol = (int) Math.floor(rol >> 1);
                LogUtil.d("MoreLevelTdtLayer", "lastLevel=1=" + lastLevel +
                        ",lastCol==" + lastCol
                        + ",col==" + col
                        + ",lastRol==" + lastRol
                        + ",rol==" + rol);
                byte[] lastBytes = mCacheHelper.getOfflineCacheFile(lastLevel, lastCol, lastRol);
                if (lastBytes != null) { //本地已缓存上一级的1/2坐标瓦片
                    mCacheHelper.addOfflineCacheFile(lastBytes, trueLevel, col, rol);
                    return bytes;
                } else {//3.第2步没有，找上一级的1/2坐标有没网络瓦片；
                    String url = buildWMTSUrl(lastLevel - 1, lastCol, lastRol);
                    lastBytes = getByteFromUrl(url);
                    if (lastBytes != null) { //网络上有上一级的1/2坐标瓦片
                        mCacheHelper.addOfflineCacheFile(lastBytes, trueLevel, col, rol);
                        return lastBytes;
                    } else {//4.第3步没有，级别再减一级，进入2、3步的递归。
                        if (lastLevel - 1 < mWMTSLayerInfo.getMinZoomLevel()) {
                            return null;
                        }
                        return getReplacingTile(lastLevel - 1, lastCol, lastRol);
                    }
                }
            }
        } else {
            int lastLevel = trueLevel - 1;
            if (lastLevel < mWMTSLayerInfo.getMinZoomLevel()) {
                return null;
            }
            int lastCol = (int) Math.floor(col >> 1);
            int lastRol = (int) Math.floor(rol >> 1);
            LogUtil.d("MoreLevelTdtLayer", "lastLevel=2=" + lastLevel +
                    ",lastCol==" + lastCol
                    + ",col==" + col
                    + ",lastRol==" + lastRol
                    + ",rol==" + rol);
            String url = buildWMTSUrl(lastLevel - 1, lastCol, lastRol);
            byte[] lastBytes = getByteFromUrl(url);
            if (lastBytes != null) { //网络上有上一级的1/2坐标瓦片
                return lastBytes;
            } else {//4.第3步没有，级别再减一级，进入2、3步的递归。
                if (lastLevel - 1 < mWMTSLayerInfo.getMinZoomLevel()) {
                    return null;
                }
                return getReplacingTile(lastLevel - 1, lastCol, lastRol);
            }
        }
    }

    @Override
    protected byte[] getTile(int level, int col, int rol) throws Exception {
        /*LogUtil.d("MoreLevelTdtLayer", "getCurrentLevel()==" + getCurrentLevel() + ",level==" + level);
        if (getCurrentLevel() != level) {
            return null;
        }
        //生成地图图片的URL
        String url = buildWMTSUrl(level, col, rol);
        int trueLevel = level + mWMTSLayerInfo.getMinZoomLevel();
        if (trueLevel >= originalScaleLength) {//19级及以上
            return getReplacingTile(trueLevel, col, rol);
        } else {//18级及以下
            if (this.mCacheHelper != null) {
                //读取缓存文件
                byte[] bytes = mCacheHelper.getOfflineCacheFile(trueLevel, col, rol);
                if (bytes == null) {
                    bytes = getByteFromUrl(url);
                    mCacheHelper.addOfflineCacheFile(bytes, trueLevel, col, rol);
                }
                return bytes;
            } else {
                return getByteFromUrl(url);
            }
        }*/
        //        return super.getTile(level, col, rol);
        if (getCurrentLevel() != level) {
            return null;
        }
//        LogUtil.d("MoreLevelTdtLayer", "getCurrentLevel()==" + getCurrentLevel() + ",level==" + level);
        byte[] originalBytes = super.getTile(level, col, rol);
        if(originalBytes == null || originalBytes.length == 0){
            return null;
        }
        if (level < originalScaleLength - 1) {
            return originalBytes;
        }
        return null;
        //获取上一级的瓦片，主要是管点需要大级别，但底图级别不够大，所以大级别要显示上一级的瓦片，
        //这样大级别上要能看瓦片，只是会模糊
        /*int fallbackZoom = level - 1;
        int fallbackCol = (int) Math.floor(col / 2);
        int fallbackRow = (int) Math.floor(rol / 2);
        return this.getTile(fallbackZoom, fallbackCol, fallbackRow);*/

       /* if (getCurrentLevel() != level) {
            return null;
        }
        int fallbackZoom = level - 1;
        AGWMTSLayerInfo.WMTSInfo wmtsInfo = mWMTSLayerInfo.getWmtsInfo();
        String baseUrl = wmtsInfo.getUrl();
        int fallbackCol = (int) Math.floor(col / 2);
        int fallbackRow = (int) Math.floor(rol / 2);
        if (level == 19 && (baseUrl.contains("vec_c") || baseUrl.contains("cva_c"))) {
            fallbackZoom = level - 2;
        }
        //生成地图图片的URL
        String url = buildWMTSUrl(level, col, rol);
        if (level >= originalScaleLength - 1) {
            url = buildWMTSUrl(fallbackZoom, fallbackCol, fallbackRow);
        }
        if (this.mCacheHelper != null) {
            //读取缓存文件
            byte[] bytes = mCacheHelper.getOfflineCacheFile(level, col, rol);
            if (bytes == null) {
                if (level >= originalScaleLength - 1) {
                    bytes = mCacheHelper.getOfflineCacheFile(fallbackZoom, fallbackCol, fallbackRow);
                }
                if (bytes == null) {

                    bytes = getByteFromUrl(url);
                }
                mCacheHelper.addOfflineCacheFile(bytes, level, col, rol);
            }
            return bytes;
        } else {
            return getByteFromUrl(url);
        }*/
    }

    private ExecutorService serviceExecutor = null;
    protected ExecutorService getServiceExecutor() {
        if(serviceExecutor == null){
            serviceExecutor = Executors.newFixedThreadPool(2);
        }
        return serviceExecutor;
    }

    private ExecutorService poolExecutor = null;
    protected ExecutorService getPoolExecutor() {
        if(poolExecutor == null){
            poolExecutor = Executors.newFixedThreadPool(3);
        }
        return poolExecutor;
    }
}