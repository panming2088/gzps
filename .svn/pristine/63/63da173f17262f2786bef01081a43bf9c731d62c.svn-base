package com.augurit.agmobile.mapengine.common.agmobilelayer.util;

import com.augurit.agmobile.mapengine.common.agmobilelayer.model.TileCacheInfo;

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
 * @createTime 创建时间 ：2017-04-17
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-04-17
 * @modifyMemo 修改备注：
 */
public class MapServiceInfoReader {


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

    private String mMapServerUrl;

    private TileCacheInfo mTileCacheInfo;


    public MapServiceInfoReader(String mapServerUrl) throws Exception{
        this.mMapServerUrl = mapServerUrl;
    }

    /**
     * 从网络获取图层基本信息，须在子线程中运行
     * @throws Exception
     */
    public void initTileInfo() throws Exception{

        String jsonUrl = mMapServerUrl + "?f=json";

        mTileCacheInfo = new TileCacheInfo();

        //以下为需要从配置文件获取到的信息
        int wkid = 2362, dpi = 96, levels = 20, tileCols = 256, tileRows = 256;
        double xmin = 038392883.663000003, ymin = 0, xmax = 0, ymax = 0;
        double tileOriginX = 0, tileOriginY = 0;
        List<TileCacheInfo.LODInfo> lodInfoList = new ArrayList<>();



        mTileCacheInfo.setWkid(wkid);
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
        mTileCacheInfo.setStorageFormat(null);

    }

    public byte[] getTile(int level, int col, int row){

        return null;
    }



    public int getWkid() {
        return mTileCacheInfo.getWkid();
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
