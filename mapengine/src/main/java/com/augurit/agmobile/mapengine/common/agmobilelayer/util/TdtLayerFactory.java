package com.augurit.agmobile.mapengine.common.agmobilelayer.util;

import com.esri.core.geometry.Point;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by QiuYue on 2016-03-22.
 */
public final class TdtLayerFactory {
    private static final int EARTH_RADIUS = 6378137;
    public static int DEFAULT_MINZOOMLEVEL = 0;
    public static int DEFAULT_MAXZOOMLEVEL = 20;
    public static int DEFAULT_DPI = 96;
    public static int DEFAULT_TILESIZE = 256;
    public static String CUSTOM_URL = "";
    public static Point ORIGIN_2000 = new Point(-180, 90);
    public static int SRID_2000 = 4490;
    public static Point ORIGIN_MERCATOR = new Point(-20037508.3427892, 20037508.3427892);
    public static int SRID_MERCATOR = 102100;
    //  private static String DEFAULT_URL = "%s?SERVICE=WMTS&REQUEST=GetTile&VERSION=%s&LAYER=%s&FORMAT=%s&TILEMATRIXSET=%s&STYLE=%s&TILEMATRIX=%s:%d&TILEROW=%d&TILECOL=%d";
    public static String NORMAL_URL = "http://t{s}.tianditu.com/";
    public static String NORMAL_URL_ENDING = "/wmts";
    public static int DOMAINS_LENGTH = 8;
    public static String UNDERLINE = "_";
    private static double X_MIN_2000 = -180;
    private static double Y_MIN_2000 = -90;
    private static double X_MAX_2000 = 180;
    private static double Y_MAX_2000 = 90;
    private static double X_MIN_MERCATOR = -20037508.3427892;
    private static double Y_MIN_MERCATOR = -20037508.3427892;
    private static double X_MAX_MERCATOR = 20037508.3427892;
    private static double Y_MAX_MERCATOR = 20037508.3427892;
    private static String DEFAULT_VERSION = "1.0.0";
    private static String DEFAULT_STYLE = "default";
    private static String DEFAULT_FORMAT = "image/png";
    private static String DEFAULT_URL = "%s?SERVICE=WMTS&REQUEST=GetTile&VERSION=%s&LAYER=%s&FORMAT=%s&TILEMATRIXSET=%s&STYLE=%s&TILEMATRIX=%d&TILEROW=%d&TILECOL=%d";
    /*http://t0.tianditu.com/vec_c/wmts?SERVICE=WMTS&REQUEST=GetTile&
    // VERSION=1.0.0&LAYER=vec&STYLE=null&TILEMATRIXSET=c&
    // TILEMATRIX=[z]&TILEROW=[y]&TILECOL=[x]&FORMAT=tiles*/
   /* private static String BEIJING_URL =" %s?SERVICE=WMTS&REQUEST=GetTile&" +
            "VERSION=1.0.0&LAYER=vec&STYLE=null&TILEMATRIXSET=c&TILEMATRIX=%s&TILEROW=%s&TILECOL=%s&FORMAT=tiles";*/
    private static String DEFAULT_REST_URL = "%s/tile/%s/%s/%s/%s/%s/%s/%s.png";
    private static String TIANDITU_URL = "%s?T=%s_%s&x=%d&y=%d&l=%d";
    private static String LAYER_NAME_VECTOR = "vec";
    private static String LAYER_NAME_VECTOR_ANNOTATION_CHINESE = "cva";
    private static String LAYER_NAME_VECTOR_ANNOTATION_ENGLISH = "eva";
    private static String LAYER_NAME_IMAGE = "img";
    private static String LAYER_NAME_IMAGE_ANNOTATION_CHINESE = "cia";
    private static String LAYER_NAME_IMAGE_ANNOTATION_ENGLISH = "eia";
    private static String LAYER_NAME_TERRAIN = "ter";
    private static String LAYER_NAME_TERRAIN_ANNOTATION_CHINESE = "cta";
    private static String LAYER_NAME_TERRAIN_ANNOTATION_ENGLISH = "eta";
    private static String TILE_MATRIX_SET_MERCATOR = "w";
    private static String TILE_MATRIX_SET_2000 = "c";
    private static String NORMAL_FORMAT = "tiles";
    private static String PATH_ROOT;

    private TdtLayerFactory() {
    }

    //设置一些常用数据
   /* public static void setCommonData(TdtLayerInfo tdtLayerInfo) {
        if (null == tdtLayerInfo || tdtLayerInfo.getWmtsInfo() == null)
            throw new IllegalArgumentException("tdtLayerInfo can not be null");
        TdtLayerInfo.WMTSInfo wmtsInfo = tdtLayerInfo.getWmtsInfo();
        if (null == wmtsInfo)
            throw new IllegalArgumentException("wmtsInfo can not be null");
        //如果是国家2000坐标系
        if (tdtLayerInfo.getCoordinateType() == CoordinateType.COUNTRY_2000) {
            tdtLayerInfo.setOrigin(ORIGIN_2000);
            tdtLayerInfo.setxMin(X_MIN_2000);
            tdtLayerInfo.setyMin(Y_MIN_2000);
            tdtLayerInfo.setxMax(X_MAX_2000);
            tdtLayerInfo.setyMax(Y_MAX_2000);
            wmtsInfo.srid(SRID_2000);
        } else {
            tdtLayerInfo.setOrigin(ORIGIN_MERCATOR);
            tdtLayerInfo.setxMin(X_MIN_MERCATOR);
            tdtLayerInfo.setyMin(Y_MIN_MERCATOR);
            tdtLayerInfo.setxMax(X_MAX_MERCATOR);
            tdtLayerInfo.setyMax(Y_MAX_MERCATOR);
            wmtsInfo.srid(SRID_MERCATOR);
        }
        //如果版本号为空，那么设置为默认的版本号
        if (TextUtils.isEmpty(wmtsInfo.getVersion()))
            wmtsInfo.version(DEFAULT_VERSION);
        if (TextUtils.isEmpty(wmtsInfo.getStyle()))
            wmtsInfo.style(DEFAULT_STYLE);
        if (TextUtils.isEmpty(wmtsInfo.getFormat()))
            wmtsInfo.format(DEFAULT_FORMAT);

        //根据TdtLayer设置resolution
        TdtLayerFactory.setUpResolution(tdtLayerInfo);
    }*/

    /*private static void setUpResolution(TdtLayerInfo tdtLayerInfo) {
        if (null == tdtLayerInfo)
            throw new IllegalArgumentException("tdtLayerInfo can not be null");
        int minZoomLevel = tdtLayerInfo.getMinZoomLevel();
        int maxZoomLevel = tdtLayerInfo.getMaxZoomLevel();
        int zooms = maxZoomLevel - minZoomLevel + 1;
        double[] fixedResolutions = new double[zooms];
        double[] fixedScales = new double[zooms];

        for (int i = minZoomLevel; i < maxZoomLevel + 1; ++i) {
            //国家2000分辨率和比例尺的计算
            double res = 360.0D / (Math.pow(2.0D, (double) i) * (double) tdtLayerInfo.getTileSize());
            double scale = 111319.49079327358D * res * (double) tdtLayerInfo.getDpi() / 0.0254D;
            if (tdtLayerInfo.getCoordinateType() == CoordinateType.MERCATOR) { //墨卡托分辨率和比例尺的计算
                res = 2 * Math.PI * EARTH_RADIUS / (Math.pow(2.0D, (double) i) * (double) tdtLayerInfo.getTileSize());
                scale = res * (double) tdtLayerInfo.getDpi() / 0.0254D;
            }
            fixedResolutions[i - minZoomLevel] = res;
            fixedScales[i - minZoomLevel] = scale;
        }
        tdtLayerInfo.setResolutions(fixedResolutions);
        tdtLayerInfo.setScales(fixedScales);
    }*/

    /*public static String buildWMTSUrl(TdtLayerInfo tdtLayerInfo, int level, int col, int rol) {
        String url = null;
        level += tdtLayerInfo.getMinZoomLevel();
        if (validLevel(tdtLayerInfo, level)) {
            TdtLayerInfo.WMTSInfo wmtsInfo = tdtLayerInfo.getWmtsInfo();
            url = wmtsInfo.getUrl();
            int domainsLength = tdtLayerInfo.getDomainsLength();
            if (domainsLength > 0) {
                int idx = (int) (Math.random() * (double) domainsLength);
                url = url.replace("{s}", idx + "");
            }
            if (tdtLayerInfo.getWmtsInfo().getServiceMode() == TdtLayerInfo.ServiceMode.KVP) {
                //将数据填充到定义好的规则中

                //  url = String.format(BEIJING_URL,new Object[]{url,Integer.valueOf(level),Integer.valueOf(rol),Integer.valueOf(col)});
                url = String.format(DEFAULT_URL, url, wmtsInfo.getVersion(), wmtsInfo.getLayer(),
                        wmtsInfo.getFormat(), wmtsInfo.getTileMatrixSet(), wmtsInfo.getStyle(),
                        Integer.valueOf(level), Integer.valueOf(rol), Integer.valueOf(col));
                //   url = String.format(DEFAULT_URL, new Object[]{url, wmtsInfo.getVersion(), wmtsInfo.getLayer(), wmtsInfo.getFormat(), wmtsInfo.getTileMatrixSet(), wmtsInfo.getStyle(), wmtsInfo.getTileMatrixSet(), Integer.valueOf(level), Integer.valueOf(rol), Integer.valueOf(col)});
                // Log.d("Test",url);
            } else if (tdtLayerInfo.getWmtsInfo().getServiceMode() == TdtLayerInfo.ServiceMode.REST) {
                //	http://<wmts-url>/tile/<wmts-version>/<layer>/<style>/<tilematrixset>/<tilematrix>/<tilerow>/<tilecol>.<format>
                url = String.format(DEFAULT_REST_URL, url, wmtsInfo.getVersion(), wmtsInfo.getLayer(), wmtsInfo.getStyle(), wmtsInfo.getTileMatrixSet(), Integer.valueOf(level), Integer.valueOf(rol), Integer.valueOf(col));
                //   Log.d("Test",url);
            }
        }
        return url;
    }*/

    /*private static boolean validLevel(TdtLayerInfo tdtLayerInfo, int zoom) {
        boolean result = false;
        //如果当前的放大比例大于最小比例小于最大比例
        if (tdtLayerInfo.getMinZoomLevel() <= zoom && zoom <= tdtLayerInfo.getMaxZoomLevel()) {
            result = true;
        }
        return result;
    }*/

    /*//这里是一些测试用的天地图地址
    public static TdtLayerInfo getTdtLayerInfo(CoordinateType coordinateType, int layerType, int minZoomLevel, int maxZoomLevel) {
        TdtLayerInfo tdtLayerInfo = null;
        TdtLayerInfo.WMTSInfo wmtsInfo = null;
        if (coordinateType == CoordinateType.COUNTRY_2000) {
            switch (layerType) {
                case TdtLayerType.VECTOR://"http://t{s}.tianditu.com/vec_c/wmts";
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_VECTOR + UNDERLINE + TILE_MATRIX_SET_2000 + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_VECTOR);
                    break;
                case TdtLayerType.VECTOR_ANNOTATION_CHINESE://"http://t{s}.tianditu.com/cva_c/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_VECTOR_ANNOTATION_CHINESE + UNDERLINE + TILE_MATRIX_SET_2000 + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_VECTOR_ANNOTATION_CHINESE);
                    break;
                case TdtLayerType.VECTOR_ANNOTATION_ENGLISH://"http://t{s}.tianditu.com/eva_c/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_VECTOR_ANNOTATION_ENGLISH + UNDERLINE + TILE_MATRIX_SET_2000 + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_VECTOR_ANNOTATION_ENGLISH);
                    break;
                case TdtLayerType.IMAGE://"http://t{s}.tianditu.com/img_c/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_IMAGE + UNDERLINE + TILE_MATRIX_SET_2000 + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_IMAGE);
                    break;
                case TdtLayerType.IMAGE_ANNOTATION_CHINESE://"http://t{s}.tianditu.com/cia_c/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_IMAGE_ANNOTATION_CHINESE + UNDERLINE + TILE_MATRIX_SET_2000 + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_IMAGE_ANNOTATION_CHINESE);
                    break;
                case TdtLayerType.IMAGE_ANNOTATION_ENGLISH://"http://t{s}.tianditu.com/eia_c/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_IMAGE_ANNOTATION_ENGLISH + UNDERLINE + TILE_MATRIX_SET_2000 + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_IMAGE_ANNOTATION_ENGLISH);
                    break;
                case TdtLayerType.TERRAIN://"http://t{s}.tianditu.com/ter_c/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_TERRAIN + UNDERLINE + TILE_MATRIX_SET_2000 + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_TERRAIN);
                    break;
                case TdtLayerType.TERRAIN_ANNOTATION_CHINESE://"http://t{s}.tianditu.com/cta_c/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_TERRAIN_ANNOTATION_CHINESE + UNDERLINE + TILE_MATRIX_SET_2000 + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_TERRAIN_ANNOTATION_CHINESE);
                    break;
                case TdtLayerType.TERRAIN_ANNOTATION_ENGLISH://"http://t{s}.tianditu.com/eta_c/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_TERRAIN_ANNOTATION_ENGLISH + UNDERLINE + TILE_MATRIX_SET_2000 + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_TERRAIN_ANNOTATION_ENGLISH);
                    break;
            }
            if (wmtsInfo != null)
                wmtsInfo.tileMatrixSet(TILE_MATRIX_SET_2000);
        } else {
            switch (layerType) {
                case TdtLayerType.VECTOR://"http://t{s}.tianditu.com/vec_w/wmts";
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_VECTOR + UNDERLINE + TILE_MATRIX_SET_MERCATOR + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_VECTOR);
                    break;
                case TdtLayerType.VECTOR_ANNOTATION_CHINESE://"http://t{s}.tianditu.com/cva_w/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_VECTOR_ANNOTATION_CHINESE + UNDERLINE + TILE_MATRIX_SET_MERCATOR + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_VECTOR_ANNOTATION_CHINESE);
                    break;
                case TdtLayerType.VECTOR_ANNOTATION_ENGLISH://"http://t{s}.tianditu.com/eva_w/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_VECTOR_ANNOTATION_ENGLISH + UNDERLINE + TILE_MATRIX_SET_MERCATOR + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_VECTOR_ANNOTATION_ENGLISH);
                    break;
                case TdtLayerType.IMAGE://"http://t{s}.tianditu.com/img_w/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_IMAGE + UNDERLINE + TILE_MATRIX_SET_MERCATOR + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_IMAGE);
                    break;
                case TdtLayerType.IMAGE_ANNOTATION_CHINESE://"http://t{s}.tianditu.com/cia_w/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_IMAGE_ANNOTATION_CHINESE + UNDERLINE + TILE_MATRIX_SET_MERCATOR + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_IMAGE_ANNOTATION_CHINESE);
                    break;
                case TdtLayerType.IMAGE_ANNOTATION_ENGLISH://"http://t{s}.tianditu.com/eia_w/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_IMAGE_ANNOTATION_ENGLISH + UNDERLINE + TILE_MATRIX_SET_MERCATOR + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_IMAGE_ANNOTATION_ENGLISH);
                    break;
                case TdtLayerType.TERRAIN://"http://t{s}.tianditu.com/ter_w/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_TERRAIN + UNDERLINE + TILE_MATRIX_SET_MERCATOR + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_TERRAIN);
                    break;
                case TdtLayerType.TERRAIN_ANNOTATION_CHINESE://"http://t{s}.tianditu.com/cta_w/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_TERRAIN_ANNOTATION_CHINESE + UNDERLINE + TILE_MATRIX_SET_MERCATOR + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_TERRAIN_ANNOTATION_CHINESE);
                    break;
                case TdtLayerType.TERRAIN_ANNOTATION_ENGLISH://"http://t{s}.tianditu.com/eta_w/wmts"
                    wmtsInfo = new TdtLayerInfo.WMTSInfo(NORMAL_URL + LAYER_NAME_TERRAIN_ANNOTATION_ENGLISH + UNDERLINE + TILE_MATRIX_SET_MERCATOR + NORMAL_URL_ENDING)
                            .layer(LAYER_NAME_TERRAIN_ANNOTATION_ENGLISH);
                    break;
            }
            if (wmtsInfo != null)
                wmtsInfo.tileMatrixSet(TILE_MATRIX_SET_MERCATOR);
        }
        if (wmtsInfo != null)
            wmtsInfo.format(NORMAL_FORMAT);
        tdtLayerInfo = new TdtLayerInfo(coordinateType, minZoomLevel, maxZoomLevel, wmtsInfo);
        tdtLayerInfo.setDomainsLength(DOMAINS_LENGTH);
        return tdtLayerInfo;
    }*/

   /* *//**
     * 从本地获取图片
     *
     * @param cachepath
     * @param level
     * @param col
     * @param row
     * @return
     *//*
    public static byte[] getOfflineCacheFile(String cachepath, int level, int col, int row) {
        byte[] bytes = null;
        File rowfile = new File(getFullPath(cachepath, level, col, row));
        if (rowfile.exists()) {
            try {
                //读取文件夹中的数据
                bytes = CopySdcardbytes(rowfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bytes = null;
        }
        return bytes;
    }*/

    /**
     * 读取本地图片流
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static byte[] CopySdcardbytes(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);

        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

        byte[] temp = new byte[1024];

        int size = 0;

        while ((size = in.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        in.close();
        byte[] bytes = out.toByteArray();
        return bytes;
    }

   /* *//**
     * 判断sd卡是否存在
     *
     * @return
     *//*
    public static boolean isSdCardExit() {

        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (!sdCardExist) {
            throw new IllegalArgumentException(
                    "Root path is empty.Please check whether the insert SD card！");
        }
        return sdCardExist;
    }*/


    /**
     * 将图片保存到本地，目录结构可以随便定义
     *
     * @param level
     * @param col
     * @param row
     * @param bytes
     * @return
     *//*
    public static byte[] AddOfflineCacheFile(String cachepath, int level, int col, int row, byte[] bytes) {
        String basePath = buildURL(cachepath, PATH_ROOT);
        File file = new File(basePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File levelfile = new File(basePath + "/" + level);
        if (!levelfile.exists()) {
            levelfile.mkdirs();
        }
        File colfile = new File(basePath + "/" + level + "/" + col);
        if (!colfile.exists()) {
            colfile.mkdirs();
        }
        File rowfile = new File(getFullPath(cachepath, level, col, row));
        if (!rowfile.exists()) {
            try {
                FileOutputStream out = new FileOutputStream(rowfile);
                out.write(bytes);
            } catch (Exception e) {
                LogUtil.d("TdtLayer", "当前没有天地图的瓦片缓存");
            }
        }
        return bytes;

    }*/

    /*private static String getFullPath(String cachepath, int level, int col, int row) {
    *//*    if (cachepath == null || cachepath.equals("")){
            File sdDir =  Environment.getExternalStorageDirectory();
            PATH_ROOT = sdDir.toString();
        }else {
            PATH_ROOT = cachepath;
        }*//*
        if (PATH_ROOT == null) {
            //判断sd卡是否存在，如果存在
            if (isSdCardExit()) {
                File sdDir = Environment.getExternalStorageDirectory();
                PATH_ROOT = sdDir.toString();
            }
        }
        return buildURL(cachepath + "/" + level + "/" + col + "/" + row + ".dat", PATH_ROOT);
    }*/

    /*public static String buildURL(String url, String urlBase) {
        //去掉url中的空格
        url = url.trim();
        //如果不存在"://"这个符号
        if (url.indexOf("://") == -1) {
            //如果urlBase以"/"结尾或者以"/"开头，那么url = urlBase+url
            //否则，url = urlBase+"/"+url；
            url = urlBase
                    + (urlBase.endsWith("/") || url.startsWith("/") ? url : "/"
                    + url);
        } else {
            //抛出异常
            throw new IllegalArgumentException("Url '" + url
                    + "' should not contain '://'");
        }

        return url;
    }*/

}
