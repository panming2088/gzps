package com.augurit.agmobile.mapengine.common.agmobilelayer;

import android.util.Log;

import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ITileCacheHelper;
import com.esri.android.map.TiledServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import java.util.concurrent.RejectedExecutionException;

/**
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.agmobile.mapengine.common.agmobilelayer
 * @createTime 创建时间 ：2017-08-28
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-08-28 11:34
 */
public class AGBaiduMapLayer extends TiledServiceLayer {
    public static final double BAIDU_MAPS_YMAX = 20037508.342789244;
    private static final int BADIDU_DPI = 96;
    private static final int BADIDU_TILESIZE = 256;
    private static int BAIDU_WKID = 102100;
    private static String BAIDU_REQUEST_TILE_URL = "%s?qt=tile&x=%d&y=%d&z=%d&styles=pl&udt=20170818&scaler=1&p=1";
    private static String BAIDU_NUMAL_URL = "http://online{s}.map.bdimg.com/onlinelabel/";
    private static int BAIDU_MIN_ZOOMLEVEL = 0;
    private static int BAIDU_MAX_ZOOMLEVEL = 19;
    protected String mBaseUrl;
    private SpatialReference mBaiduSpatialReference;
    private double[] resolutions;
    private double[] scales;

    public AGBaiduMapLayer(String url) {
        this(url, null, true);
    }

    public AGBaiduMapLayer(String url, ITileCacheHelper cachePathHelper, boolean initLayer) {
        super(initLayer);
        mBaseUrl = url;
        mBaiduSpatialReference = SpatialReference.create(BAIDU_WKID);
        initResAndScales();
        if (initLayer) {
            try {
                this.getServiceExecutor().submit(new Runnable() {
                    public void run() {
                        AGBaiduMapLayer.this.initLayer();
                    }
                });
            } catch (RejectedExecutionException e) {
                Log.e("AGBaiduMapLayer", "initialization of the layer failed.", e);
            }
        }
    }

    private void initResAndScales() {
        /*resolutions = new double[BAIDU_MAX_ZOOMLEVEL];
        scales = new double[BAIDU_MAX_ZOOMLEVEL];
        for (int i = 0; i < BAIDU_MAX_ZOOMLEVEL; i++) {
            resolutions[i] = Math.pow(2, (18 - i));
            scales[i] = ResolutionScalesUtil.getScalesFromResolutions(resolutions[i], BADIDU_DPI, mBaiduSpatialReference);
        }*/
        resolutions = new double[BAIDU_MAX_ZOOMLEVEL];
        scales = new double[BAIDU_MAX_ZOOMLEVEL];
        double resolution = BAIDU_MAPS_YMAX * 2 / 256;
        double scale = resolution * 96 * 39.37;
        for (int i = 0; i < BAIDU_MAX_ZOOMLEVEL; i++) {
            resolutions[i] = resolution;
            scales[i] = scale;
            resolution /= 2;
            scale /= 2;
        }
    }

    @Override
    protected void initLayer() {
        if (this.getID() == 0L) {
            this.nativeHandle = this.create();
        }

        if (this.getID() == 0L) {
            this.changeStatus(OnStatusChangedListener.STATUS.INITIALIZATION_FAILED);
            Log.e("AGBaiduMapLayer", "url =" + this.getUrl());
        } else {
            try {
                this.setDefaultSpatialReference(mBaiduSpatialReference);
                this.setFullExtent(new Envelope(-BAIDU_MAPS_YMAX, -BAIDU_MAPS_YMAX, BAIDU_MAPS_YMAX, BAIDU_MAPS_YMAX));
                this.setTileInfo(new TileInfo(new Point(-BAIDU_MAPS_YMAX, BAIDU_MAPS_YMAX), scales, resolutions, scales.length, BADIDU_DPI,
                        BADIDU_TILESIZE, BADIDU_TILESIZE));
                super.initLayer();
            } catch (Exception var2) {
                Log.e("AGBaiduMapLayer", "Baidu map url =" + this.getUrl(), var2);
            }
        }
    }

    @Override
    protected byte[] getTile(int level, int col, int row) throws Exception {
        String url = buildBaiduMapUrl(level, col, row);
        return com.esri.core.internal.io.handler.a.a(url, null, null, null);
    }

    private String buildBaiduMapUrl(int level, int col, int row) {
        String url = null;
        if (validLevel(level)) {
            url = mBaseUrl;
            if (url.contains("online")) {
                url = BAIDU_NUMAL_URL;
            }
            int zoom = level-1;
            int offsetX = (int) Math.pow(2, zoom);
            int offsetY = offsetX - 1;
            int numX = col - offsetX;
            int numY = (-row) + offsetY;
            int num = (col + row) % 8 + 1;
            zoom = level + 1;
            url = url.replace("{s}", num + "");
            url = String.format(BAIDU_REQUEST_TILE_URL, url,
                    numX, numY, zoom);
            Log.d("AGBaiduMapLayer","level="+level+",col=="+col+",row=="+row);
            Log.d("AGBaiduMapLayer","zoom="+zoom+",numX=="+numX+",numY=="+numY);
            Log.d("AGBaiduMapLayer","url="+url);
        }

        /*String url = null;
//        level += BAIDU_MIN_ZOOMLEVEL;
        if (validLevel(level)) {
            int domainsLength = 10;
            url = mBaseUrl;
            if (url.contains("online")) {
                url = BAIDU_NUMAL_URL;
            }
            if (domainsLength > 0) {
                int idx = (int) (Math.random() * (double) domainsLength);
                url = url.replace("{s}", idx + "");
            }
            url = String.format(BAIDU_REQUEST_TILE_URL, url,
                    col, row, Integer.valueOf(level));
        }*/
        return url;
    }

    private boolean validLevel(int zoom) {
        boolean result = false;
        //如果当前的放大比例大于最小比例小于最大比例
        if (BAIDU_MIN_ZOOMLEVEL <= zoom && zoom <= BAIDU_MAX_ZOOMLEVEL) {
            result = true;
        }
        return result;
    }
}
