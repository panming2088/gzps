package com.augurit.agmobile.mapengine.common.agmobilelayer.util;

import android.os.MemoryFile;

import com.augurit.agmobile.mapengine.common.agmobilelayer.model.TileCacheInfo;

import org.geowebcache.arcgis.compact.ArcGISCompactCache;
import org.geowebcache.arcgis.compact.ArcGISCompactCacheV1;
import org.geowebcache.arcgis.compact.ArcGISCompactCacheV2;
import org.geowebcache.arcgis.config.CacheStorageInfo;
import org.geowebcache.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.agmobilelayer.util
 * @createTime 创建时间 ：2017-04-14
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-04-14
 * @modifyMemo 修改备注：
 */
public class TileCacheReader {


    //web墨卡托默认值
//    private double xmin = 8176078.237600003;
//    private double ymin = 2056264.7502700;
//    private double xmax = 15037808.29357646;
//    private double ymax = 7087593.892070787;
//    private Point origin = new Point(-20037508.342787001, 20037508.342787001);
//    private double[] scale = new double[]{591657527.591555, 295828763.79577702, 147914381.89788899, 73957190.948944002, 36978595.474472001, 18489297.737236001, 9244648.8686180003,
//            4622324.4343090001,2311162.2171550002,1155581.108577,577790.55428899999,288895.27714399999,144447.638572,72223.819285999998,36111.909642999999,18055.954822,9027.9774109999998,
//            4513.9887049999998,2256.994353,1128.4971760000001};
//    private double[] res = new double[]{156543.03392799999,78271.516963999893, 39135.758482000099, 19567.879240999901, 9783.9396204999593,4891.9698102499797, 2445.9849051249898, 1222.9924525624899, 611.49622628138002,
//            305.74811314055802,152.874056570411,76.437028285073197,38.218514142536598,19.109257071268299,9.5546285356341496,4.7773142679493699,2.38865713397468,
//            1.1943285668550501,0.59716428355981699,0.29858214164761698};
//    private int levels = 20;
//    private int dpi = 96;
//    private int tileWidth = 256;
//    private int tileHeight = 256;

    private String mLayersPath;
    private String mAlllayersPath;

    private ArcGISCompactCache mArcGISCompactCache;

    private TileCacheInfo mTileCacheInfo;


    public TileCacheReader(String LayersPath) throws Exception{
        this.mLayersPath = LayersPath;
        this.mAlllayersPath = mLayersPath + "/_alllayers";
        initTileInfo();
    }

    private void initTileInfo() throws Exception{

        mTileCacheInfo = new TileCacheInfo();

        //以下为需要从配置文件获取到的信息
        int wkid = 2362, dpi = 96, levels = 20, tileCols = 256, tileRows = 256;
        String wkt = null;
        double xmin = 038392883.663000003, ymin = 0, xmax = 0, ymax = 0;
        double tileOriginX = 0, tileOriginY = 0;
        List<TileCacheInfo.LODInfo> lodInfoList = new ArrayList<>();
        String storageFormat = null;

        //初始化budle相关信息
        File confXmlFile = new File(mLayersPath + "/conf.xml");
        if(!confXmlFile.exists()){
            confXmlFile = new File(mLayersPath + "/Conf.xml");
        }
        File confCdiFile = new File(mLayersPath + "/conf.cdi");

        InputStream confXmlInputStream = null;
        InputStream confCdiInputStream = null;

        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        Document document = null;

            confXmlInputStream = getInputStream(new FileInputStream(confXmlFile));
            confCdiInputStream = getInputStream(new FileInputStream(confCdiFile));

        //首先找到xml文件
        factory = DocumentBuilderFactory.newInstance();
        try {
            //找到conf.xml，并加载文档
            builder = factory.newDocumentBuilder();

            document = builder.parse(confXmlInputStream);
            //找到根Element
            Element root = document.getDocumentElement();

            try {
                wkid = Integer.parseInt(root.getElementsByTagName("WKID").item(0).getChildNodes().item(0).getNodeValue());
            } catch (Exception e) {
                wkid = 0;
            }
            if(wkid == 0){
                wkt = root.getElementsByTagName("WKT").item(0).getChildNodes().item(0).getNodeValue();
            }
            dpi = Integer.parseInt(root.getElementsByTagName("DPI").item(0).getChildNodes().item(0).getNodeValue());
            tileCols = Integer.parseInt(root.getElementsByTagName("TileCols").item(0).getChildNodes().item(0).getNodeValue());
            tileRows = Integer.parseInt(root.getElementsByTagName("TileRows").item(0).getChildNodes().item(0).getNodeValue());
            tileOriginX = Double.valueOf(root.getElementsByTagName("TileOrigin").item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
            tileOriginY = Double.valueOf(root.getElementsByTagName("TileOrigin").item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeValue());

            storageFormat = root.getElementsByTagName("StorageFormat").item(0).getChildNodes().item(0).getNodeValue();

            //LODInfos
            NodeList nodes = root.getElementsByTagName("LODInfos").item(0).getChildNodes();
            levels = nodes.getLength();
            //遍历根节点所有子节点,rivers 下所有river
            for (int i = 0; i < nodes.getLength(); i++) {
                int levelId;
                double scale, resolution;
                TileCacheInfo.LODInfo lodInfo = new TileCacheInfo.LODInfo();
                //获取river元素节点
                Element riverElement = (Element) (nodes.item(i));
                levelId = Integer.parseInt(riverElement.getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
                scale = (Double.valueOf(riverElement.getChildNodes().item(1).getChildNodes().item(0).getNodeValue()));
                resolution = (Double.valueOf(riverElement.getChildNodes().item(2).getChildNodes().item(0).getNodeValue()));
                lodInfo.setLevelID(levelId);
                lodInfo.setScale(scale);
                lodInfo.setResolution(resolution);
                lodInfoList.add(lodInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (confXmlInputStream != null) {
                    confXmlInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            //找到conf.cdi，并加载文档
            builder = factory.newDocumentBuilder();

            document = builder.parse(confCdiInputStream);
            //找到根Element
            Element root = document.getDocumentElement();

            xmin = Double.valueOf(root.getElementsByTagName("XMin").item(0).getChildNodes().item(0).getNodeValue());
            ymin = Double.valueOf(root.getElementsByTagName("YMin").item(0).getChildNodes().item(0).getNodeValue());
            xmax = Double.valueOf(root.getElementsByTagName("XMax").item(0).getChildNodes().item(0).getNodeValue());
            ymax = Double.valueOf(root.getElementsByTagName("YMax").item(0).getChildNodes().item(0).getNodeValue());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (confCdiInputStream != null) {
                    confCdiInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mTileCacheInfo.setWkid(wkid);
        mTileCacheInfo.setWkt(wkt);
        mTileCacheInfo.setDpi(dpi);
        mTileCacheInfo.setLevels(levels);
        mTileCacheInfo.setTileCols(tileCols);
        mTileCacheInfo.setTileRows(tileRows);
        mTileCacheInfo.setXmin(xmin);
        mTileCacheInfo.setYmin(ymin);
        mTileCacheInfo.setXmax(xmax);
        mTileCacheInfo.setYmax(ymax);
        mTileCacheInfo.setTileOriginX(tileOriginX);
        mTileCacheInfo.setTileOriginY(tileOriginY);
        mTileCacheInfo.setLodInfoList(lodInfoList);
        mTileCacheInfo.setStorageFormat(storageFormat);

        if (storageFormat.equals(CacheStorageInfo.COMPACT_FORMAT_CODE)) {
            //uses compact format (ArcGIS 10.0 - 10.2)
            mArcGISCompactCache = new ArcGISCompactCacheV1(mAlllayersPath);
        } else if (storageFormat.equals(CacheStorageInfo.COMPACT_FORMAT_CODE_V2)) {
            //uses compact format (ArcGIS 10.3)"
            mArcGISCompactCache = new ArcGISCompactCacheV2(mAlllayersPath);
        }

    }

    public byte[] getTile(int level, int col, int row){
        if(true){
            return getTile2(level, col, row);
        }
        try {
            Resource resource = mArcGISCompactCache.getBundleFileResource(level, row, col);
            InputStream is = resource.getInputStream();
            byte[] bytes = new byte[Integer.valueOf(String.valueOf(resource.getSize()))];
            int readed = is.read(bytes);
            if(readed > 0){
                return bytes;
            }
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }


    public byte[] getTile2(int level, int col, int row) {
        FileInputStream isBundlx = null;
        FileInputStream isBundle = null;
        byte[] result = null;
        try {
            String l = "0" + level;
            int lLength = l.length();
            if (lLength > 2) {
                l = l.substring(lLength - 2);
            }
            l = "L" + l;

            int rGroup = 128 * (row / 128);
            String r = "000" + Integer.toHexString(rGroup);
            int rLength = r.length();
            if (rLength > 4) {
                r = r.substring(rLength - 4);
            }
            r = "R" + r;

            int cGroup = 128 * (col / 128);
            String c = "000" + Integer.toHexString(cGroup);
            int cLength = c.length();
            if (cLength > 4) {
                c = c.substring(cLength - 4);
            }
            c = "C" + c;

            String bundleBase = String
                    .format("%s/%s/%s%s", mAlllayersPath, l, r, c);
            String bundlxFileName = bundleBase + ".bundlx";
            String bundleFileName = bundleBase + ".bundle";

            int index = 128 * (col - cGroup) + (row - rGroup);
            isBundlx = new FileInputStream(bundlxFileName);
            isBundlx.skip(16 + 5 * index);
            byte[] buffer = new byte[5];
            isBundlx.read(buffer);
            buffer = getByte(buffer);
            long offset = (long) (buffer[0] & 0xff) + (long) (buffer[1] & 0xff)
                    * 256 + (long) (buffer[2] & 0xff) * 65536
                    + (long) (buffer[3] & 0xff) * 16777216
                    + (long) (buffer[4] & 0xff) * 4294967296L;

            isBundle = new FileInputStream(bundleFileName);
            isBundle.skip(offset);
            byte[] lengthBytes = new byte[4];
            isBundle.read(lengthBytes);
            lengthBytes = getByte(lengthBytes);
            int length = (lengthBytes[0] & 0xff)
                    + (lengthBytes[1] & 0xff) * 256
                    + (lengthBytes[2] & 0xff) * 65536
                    + (lengthBytes[3] & 0xff) * 16777216;
            result = new byte[length];
            isBundle.read(result);
            result = getByte(result);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (isBundle != null) {
                    isBundle.close();
                }
                if(isBundlx != null){
                    isBundlx.close();
                }
            } catch (Exception e) {

            }
        }
        return result;
    }


    /*
    public byte[] getTile2(int level, int col, int row) {
        //  FileInputStream isBundlx = null;
        //   FileInputStream isBundle = null;
        MemoryFile bundlxFile = null;
        MemoryFile bundleFile = null;
        InputStream isBundlx = null;
        InputStream isBundle = null;
        byte[] result = null;
        try {
            String l = "0" + level;
            int lLength = l.length();
            if (lLength > 2) {
                l = l.substring(lLength - 2);
            }
            l = "L" + l;

            int rGroup = 128 * (row / 128);
            String r = "000" + Integer.toHexString(rGroup);
            int rLength = r.length();
            if (rLength > 4) {
                r = r.substring(rLength - 4);
            }
            r = "R" + r;

            int cGroup = 128 * (col / 128);
            String c = "000" + Integer.toHexString(cGroup);
            int cLength = c.length();
            if (cLength > 4) {
                c = c.substring(cLength - 4);
            }
            c = "C" + c;

            String bundleBase = String
                    .format("%s/%s/%s%s", mAlllayersPath, l, r, c);
            String bundlxFileName = bundleBase + ".bundlx";
            String bundleFileName = bundleBase + ".bundle";

            int index = 128 * (col - cGroup) + (row - rGroup);
            //  isBundlx = new FileInputStream(bundlxFileName);
            bundlxFile = new MemoryFile(bundlxFileName,10000000);
            isBundlx = bundlxFile.getInputStream();
            isBundlx.skip(16 + 5 * index);
            byte[] buffer = new byte[5];
            isBundlx.read(buffer);
            buffer = getByte(buffer);
            long offset = (long) (buffer[0] & 0xff) + (long) (buffer[1] & 0xff)
                    * 256 + (long) (buffer[2] & 0xff) * 65536
                    + (long) (buffer[3] & 0xff) * 16777216
                    + (long) (buffer[4] & 0xff) * 4294967296L;

            // isBundle = new FileInputStream(bundleFileName);
            bundleFile = new MemoryFile(bundleFileName,10000000);
            isBundle = bundleFile.getInputStream();
            isBundle.skip(offset);
            byte[] lengthBytes = new byte[4];
            isBundle.read(lengthBytes);
            lengthBytes = getByte(lengthBytes);
            int length = (int) (lengthBytes[0] & 0xff)
                    + (int) (lengthBytes[1] & 0xff) * 256
                    + (int) (lengthBytes[2] & 0xff) * 65536
                    + (int) (lengthBytes[3] & 0xff) * 16777216;
            result = new byte[length];
            isBundle.read(result);
            result = getByte(result);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                /*
                if (isBundle != null) {
                    isBundle.close();
                }
                if(isBundlx != null){
                    isBundlx.close();
                }
                */
    /*
                if(bundlxFile != null){
                    bundlxFile.close();
                }

                if(bundleFile != null){
                    bundleFile.close();
                }
            } catch (Exception e) {

            }
        }
        return result;
    }
        */

    InputStream getInputStream(InputStream inputStream){
        return inputStream;
    }

    byte[] getByte(byte[] bytes){
        return bytes;
    }


    public String getLayersPath() {
        return mLayersPath;
    }

    public void setLayersPath(String mLayersPath) {
        this.mLayersPath = mLayersPath;
    }

    public String getAlllayersPath() {
        return mAlllayersPath;
    }

    public void setAlllayersPath(String mAlllayersPath) {
        this.mAlllayersPath = mAlllayersPath;
    }

    public int getWkid() {
        return mTileCacheInfo.getWkid();
    }

    public String getWkt(){
        return mTileCacheInfo.getWkt();
    }

    public int getDpi() {
        return mTileCacheInfo.getDpi();
    }

    public int getLevels() {
        return mTileCacheInfo.getLevels();
    }

    public int getTileCols() {
        return mTileCacheInfo.getTileCols();
    }

    public int getTileRows() {
        return mTileCacheInfo.getTileRows();
    }

    public double getXmin() {
        return mTileCacheInfo.getXmin();
    }

    public double getYmin() {
        return mTileCacheInfo.getYmin();
    }

    public double getXmax() {
        return mTileCacheInfo.getXmax();
    }

    public double getYmax() {
        return mTileCacheInfo.getYmax();
    }

    public double getTileOriginX() {
        return mTileCacheInfo.getTileOriginX();
    }

    public double getTileOriginY() {
        return mTileCacheInfo.getTileOriginY();
    }

    public List<TileCacheInfo.LODInfo> getLodInfoList() {
        return mTileCacheInfo.getLodInfoList();
    }

    public String getStorageFormat() {
        return mTileCacheInfo.getStorageFormat();
    }

    public double[] getScales() {
        List<TileCacheInfo.LODInfo> lodInfoList = mTileCacheInfo.getLodInfoList();
        double[] scal = new double[lodInfoList.size()];
        double[] resls = new double[lodInfoList.size()];
        for (int i = 0; i < lodInfoList.size(); i++) {
            scal[i] = lodInfoList.get(i).getScale();
            resls[i] = lodInfoList.get(i).getResolution();
        }
        return scal;
    }

    public double[] getResolution() {
        List<TileCacheInfo.LODInfo> lodInfoList = mTileCacheInfo.getLodInfoList();
        double[] scal = new double[lodInfoList.size()];
        double[] resls = new double[lodInfoList.size()];
        for (int i = 0; i < lodInfoList.size(); i++) {
            scal[i] = lodInfoList.get(i).getScale();
            resls[i] = lodInfoList.get(i).getResolution();
        }
        return resls;
    }
}
