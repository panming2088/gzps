package com.augurit.agmobile.mapengine.common.agmobilelayer;

import android.text.TextUtils;
import android.util.Log;


import com.augurit.agmobile.mapengine.common.agmobilelayer.model.AGWMTSLayerInfo;
import com.augurit.agmobile.mapengine.common.agmobilelayer.model.CapabilitiesBean;
import com.augurit.agmobile.mapengine.common.agmobilelayer.model.ExceptionMsg;
import com.augurit.agmobile.mapengine.common.agmobilelayer.service.ILayerDownloader;
import com.augurit.agmobile.mapengine.common.agmobilelayer.service.TiandiTuDownloader;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ITileCacheHelper;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.PullParseXmlUtil;
import com.augurit.am.fw.utils.AMFileUtil;
import com.augurit.am.fw.utils.JsonUtil;
import com.esri.android.map.TiledServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.io.EsriSecurityException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.agmobile.mapengine.common.agmobilelayer
 * @createTime 创建时间 ：2017-08-11
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-08-11 15:13
 */
public class AGWMTSLayer extends TiledServiceLayer {

    protected static final String REQUEST_CAPABILITIES_URL = "?SERVICE=WMTS&REQUEST=GetCapabilities&tk=5e882b49b3c81c2a3b5f99a4cbc67e8b";//tk 天地图token
    protected static final String REQUEST_TILE_URL_KVP = "%s?SERVICE=WMTS&REQUEST=GetTile&VERSION=%s&STYLE=%s&LAYER=%s&TILEMATRIXSET=%s&FORMAT=%s&TILEMATRIX=%d&TILEROW=%d&TILECOL=%d&tk=5e882b49b3c81c2a3b5f99a4cbc67e8b";//tk 天地图token
    //	http://<wmts-url>/tile/<wmts-version>/<layer>/<style>/<tilematrixset>/<tilematrix>/<tilerow>/<tilecol>.<format>
    protected static final String REQUEST_TILE_URL_REST = "%s/tile/%s/%s/%s/%s/%d/%d/%d.png";
    protected static final String WMTS_CAPABILITIES = "WMTSCapabilities";
    private static int[][] g = new int[][]{{4001, 4999}, {2044, 2045}, {2081, 2083}, {2085, 2086}, {2093, 2093}, {2096, 2098}, {2105, 2132}, {2169, 2170}, {2176, 2180}, {2193, 2193}, {2200, 2200}, {2206, 2212}, {2319, 2319}, {2320, 2462}, {2523, 2549}, {2551, 2735}, {2738, 2758}, {2935, 2941}, {2953, 2953}, {3006, 3030}, {3034, 3035}, {3058, 3059}, {3068, 3068}, {3114, 3118}, {3126, 3138}, {3300, 3301}, {3328, 3335}, {3346, 3346}, {3350, 3352}, {3366, 3366}, {3416, 3416}, {20004, 20032}, {20064, 20092}, {21413, 21423}, {21473, 21483}, {21896, 21899}, {22171, 22177}, {22181, 22187}, {22191, 22197}, {25884, 25884}, {27205, 27232}, {27391, 27398}, {27492, 27492}, {28402, 28432}, {28462, 28492}, {30161, 30179}, {30800, 30800}, {31251, 31259}, {31275, 31279}, {31281, 31290}, {31466, 31700}, {3038, 3051}};
    protected Point mOrgin;
    protected Envelope mFullExtent;
    /**
     * 处理缓存的类，通过实现buildOffLineCachePath方法可以自定义存储路径或者说存储规则，如果不提供BaseTileCachePathHelper，默认没有缓存功能
     */
    protected ITileCacheHelper mCacheHelper;
    protected String mBaseUrl;
    protected AGWMTSLayerInfo mWMTSLayerInfo;
    protected boolean mInitLayer;
    protected String mLayerId;

    public AGWMTSLayer(String url) {
        this("0", url, null, true);
    }

    public AGWMTSLayer(String layerId, String url, ITileCacheHelper cachePathHelper, boolean initLayer) {
        super(url);
        this.mLayerId = layerId;
        this.mBaseUrl = url;
        this.mCacheHelper = cachePathHelper;
        this.mInitLayer = initLayer;
        getCapabilities();
    }

    /**
     * 获取图层信息
     */
    protected void getCapabilities() {
        if (this.mCacheHelper != null) {//如果支持缓存，sd卡中会保存有对应的CAPABILITIES文件
            try {
                String result = getCapabilitiesFromLocal();
                if (TextUtils.isEmpty(result) || "null".equals(result)) {
                    readFromNet();
                } else {
                    CapabilitiesBean capabilitiesBean = JsonUtil.getObject(result, CapabilitiesBean.class);
                    if (capabilitiesBean == null) {
                        Log.e("AGWMTSLayer", ExceptionMsg.NOT_FOUND);
                    }
                    initTdtLayerInfo(capabilitiesBean);
                }
            } catch (IOException e) {
                readFromNet();
            }
        } else {
            readFromNet();
        }
    }

    /**
     * 从网络里获取
     */
    private void readFromNet() {
        getServiceExecutor().submit(new Runnable() {
            @Override
            public void run() {
                String capabilitiesUrl = mBaseUrl + REQUEST_CAPABILITIES_URL;
                new TiandiTuDownloader().loadMessageFromUrl(capabilitiesUrl, new ILayerDownloader.OnLoadDataListenr() {
                    @Override
                    public void onLoading() {

                    }

                    @Override
                    public void onSuccess(String result) {
                        if (TextUtils.isEmpty(result)) {
                            return;
                        }
                        CapabilitiesBean capabilitiesBean = PullParseXmlUtil.pullParseXML(result);
                        if (capabilitiesBean == null) {
                            Log.e("AGWMTSLayer", ExceptionMsg.NOT_FOUND);
                        }
                        if (capabilitiesBean == null) {
                            return;
                        }
                        dealWithOriginBySP(capabilitiesBean);
                        dealWithExtentBySP(capabilitiesBean);
                        if (mCacheHelper != null) {//如果支持缓存，保存CAPABILITIES
                            String json = JsonUtil.getJson(capabilitiesBean);
                            String path = mCacheHelper.getCacheFolderPath() + "/" + WMTS_CAPABILITIES + ".txt";
                            File file = new File(path);
                            try {
                                AMFileUtil.saveStringToFile(json, Charset.defaultCharset(), file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        initTdtLayerInfo(capabilitiesBean);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

    }

    /**
     * 从本地获取图层信息
     * @return
     * @throws IOException
     */
    public String getCapabilitiesFromLocal() throws IOException {
        String path = this.mCacheHelper.getCacheFolderPath() + "/" + WMTS_CAPABILITIES + ".txt";
        File file = new File(path);
        return AMFileUtil.readStringToFile(file, Charset.defaultCharset());
    }

    /**
     * 初始化
     * @param capabilitiesBean
     */
    private void initTdtLayerInfo(CapabilitiesBean capabilitiesBean) {
        this.setOrginFullExtent(capabilitiesBean);
        initWMTSLayerInfo(capabilitiesBean);
        if (this.mInitLayer) {
            try {
                this.getServiceExecutor().submit(new Runnable() {
                    @Override
                    public void run() {
                        AGWMTSLayer.this.initLayer();
                    }
                });
            } catch (RejectedExecutionException e) {

            }
        }
    }

    protected void initWMTSLayerInfo(CapabilitiesBean capabilitiesBean) {
        mWMTSLayerInfo = new AGWMTSLayerInfo(mBaseUrl, capabilitiesBean);
    }

    protected void setOrginFullExtent(CapabilitiesBean capabilitiesBean) {
        if (this.mOrgin != null) {
            capabilitiesBean.setTopLeftCorner(this.mOrgin);
        }
        if (this.mFullExtent != null) {
            capabilitiesBean.setLowerCornerPoint(new Point(this.mFullExtent.getXMin(), this.mFullExtent.getYMin()));
            capabilitiesBean.setUpperCornerPoint(new Point(this.mFullExtent.getXMax(), this.mFullExtent.getYMax()));
        }
    }

    @Override
    protected void initLayer() {
        if (this.getID() == 0L) {
            this.nativeHandle = this.create();
            this.changeStatus(OnStatusChangedListener.STATUS.INITIALIZATION_FAILED);
        } else {
            try {
                this.setDefaultSpatialReference(mWMTSLayerInfo.getSpatialReference());
                this.setFullExtent(new Envelope(mWMTSLayerInfo.getxMin(), mWMTSLayerInfo.getyMin(),
                        mWMTSLayerInfo.getxMax(), mWMTSLayerInfo.getyMax()));

                this.setTileInfo(new TileInfo(mWMTSLayerInfo.getOrigin(), mWMTSLayerInfo
                        .getScales(), mWMTSLayerInfo.getResolutions(), mWMTSLayerInfo
                        .getScales().length, mWMTSLayerInfo.getDpi(), mWMTSLayerInfo
                        .getTileSize(), mWMTSLayerInfo.getTileSize()));

                this.setRenderNativeResolution(true);
                super.initLayer();
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof EsriSecurityException) {
                    this.changeStatus(OnStatusChangedListener.STATUS.fromInt(((EsriSecurityException) e).getCode()));
                } else {
                    this.changeStatus(OnStatusChangedListener.STATUS.INITIALIZATION_FAILED);
                }
            }
        }
    }

    /**
     * 获取瓦片
     * @param level
     * @param col
     * @param rol
     * @return
     */
    @Override
    protected byte[] getTile(int level, int col, int rol) throws Exception {
        if (getCurrentLevel() != level) {
            return null;
        }
        //生成地图图片的URL
        String url = buildWMTSUrl(level, col, rol);
        if (this.mCacheHelper != null) {
            //读取缓存文件
            byte[] bytes = mCacheHelper.getOfflineCacheFile(level, col, rol);
            if (bytes == null) {
                bytes = getByteFromUrl(url);
                mCacheHelper.addOfflineCacheFile(bytes, level, col, rol);
            }
            return bytes;
        } else {
            return getByteFromUrl(url);
        }
    }

    public byte[] getByteFromUrl(String url) throws Exception {
        return com.esri.core.internal.io.handler.a.a(url, null, null, null);
    }

    public String buildWMTSUrl(int level, int col, int row) {
        String url = null;
        level += mWMTSLayerInfo.getMinZoomLevel();
        if (validLevel(level)) {
            AGWMTSLayerInfo.WMTSInfo wmtsInfo = mWMTSLayerInfo.getWmtsInfo();
            url = wmtsInfo.getUrl();
            int domainsLength = mWMTSLayerInfo.getDomainsLength();
            if (domainsLength > 0) {
                int idx = (int) (Math.random() * (double) domainsLength);
                url = url.replace("{s}", idx + "");
            }
            if (mWMTSLayerInfo.getWmtsInfo().getServiceMode() == AGWMTSLayerInfo.ServiceMode.KVP) {
                //将数据填充到定义好的规则中
                url = String.format(REQUEST_TILE_URL_KVP, url, wmtsInfo.getVersion(), wmtsInfo.getStyle(), wmtsInfo.getLayer(),
                        wmtsInfo.getTileMatrixSet(), wmtsInfo.getFormat(),
                        Integer.valueOf(level), Integer.valueOf(row), Integer.valueOf(col));
            } else if (mWMTSLayerInfo.getWmtsInfo().getServiceMode() == AGWMTSLayerInfo.ServiceMode.RESTful) {
                //	http://<wmts-url>/tile/<wmts-version>/<layer>/<style>/<tilematrixset>/<tilematrix>/<tilerow>/<tilecol>.<format>
                url = String.format(REQUEST_TILE_URL_REST, url, wmtsInfo.getVersion(), wmtsInfo.getLayer(), wmtsInfo.getStyle(), wmtsInfo.getTileMatrixSet(), Integer.valueOf(level), Integer.valueOf(row), Integer.valueOf(col));
            }
        }
        return url;
    }

    private boolean validLevel(int zoom) {
        boolean result = false;
        //如果当前的放大比例大于最小比例小于最大比例
        if (mWMTSLayerInfo.getMinZoomLevel() <= zoom && zoom <= mWMTSLayerInfo.getMaxZoomLevel()) {
            result = true;
        }
        return result;
    }

    /**
     * 获取图层信息
     * @return
     */
    public AGWMTSLayerInfo getWMTSLayerInfo() {
        return mWMTSLayerInfo;
    }

    /**
     * 获取图层id
     * @return 图层id
     */
    public String getLayerId() {
        return mLayerId;
    }

    /**
     * 根据空间坐标系对最大最小缩放范围进行处理
     * @param capabilitiesBean
     */
    private void dealWithExtentBySP(CapabilitiesBean capabilitiesBean) {
        //发现wmts中的4326的最大最小XY顺序逆序
        if (!TextUtils.isEmpty(capabilitiesBean.getSpatialReference())
                && isChangeExtentXYOrder(capabilitiesBean.getSpatialReference())) {
            Point trueLowerCornerPoint = new Point(capabilitiesBean.getLowerCornerPoint().getY(), capabilitiesBean.getLowerCornerPoint().getX());
            Point trueUpperCornerPoint = new Point(capabilitiesBean.getUpperCornerPoint().getY(), capabilitiesBean.getUpperCornerPoint().getX());
            capabilitiesBean.setLowerCornerPoint(trueLowerCornerPoint);
            capabilitiesBean.setUpperCornerPoint(trueUpperCornerPoint);
        }
    }

    /**
     * 根据空间坐标系对原点进行处理
     * @param capabilitiesBean
     */
    private void dealWithOriginBySP(CapabilitiesBean capabilitiesBean) {
        //发现wmts中的原点XY顺序在不同空间投影会发生变化，判断方法参考WMTSTileMatrix的代码
        if (!TextUtils.isEmpty(capabilitiesBean.getSpatialReference())
                && isChangeOrginXYOrder("1.3.0", capabilitiesBean.getSpatialReference())) {
            Point trueTopLeftCornerPoint = new Point(capabilitiesBean.getTopLeftCorner().getY(), capabilitiesBean.getTopLeftCorner().getX());
            capabilitiesBean.setTopLeftCorner(trueTopLeftCornerPoint);
        }
    }

    /**
     * 是否逆序最大最小范围
     * @param wkId
     * @return
     */
    private boolean isChangeExtentXYOrder(String wkId) {
        if (!TextUtils.isEmpty(wkId)) {
            String wkIdTrim = wkId.trim();
            try {
                int wkIdIntValue = Integer.valueOf(wkIdTrim);
                if (wkIdIntValue != 4490 && wkIdIntValue != 3857) {
                    return true;
                }
            } catch (NumberFormatException e) {
            }
        }
        return false;
    }

    /**
     * 是否调转原点坐标
     * @param version
     * @param wkId
     * @return
     */
    private boolean isChangeOrginXYOrder(String version, String wkId) {
        if ("1.3.0".equals(version) && !TextUtils.isEmpty(wkId)) {
            String wkIdTrim = wkId.trim();
            try {
                int wkIdIntValue = Integer.valueOf(wkIdTrim);
                for (int i = 0; i < g.length; ++i) {
                    if (wkIdIntValue >= g[i][0] && wkIdIntValue <= g[i][1]) {
                        return true;
                    }
                }
            } catch (NumberFormatException e) {
            }
        }

        return false;
    }
}


