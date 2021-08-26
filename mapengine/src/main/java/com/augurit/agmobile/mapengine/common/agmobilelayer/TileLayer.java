package com.augurit.agmobile.mapengine.common.agmobilelayer;

import com.augurit.agmobile.mapengine.common.agmobilelayer.util.TileCacheReader;
import com.esri.android.map.TiledServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;


/**
 * 读取加密的Bundle格式瓦片的Layer
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan
 * @createTime 创建时间 ：2017-04-13
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-04-13
 * @modifyMemo 修改备注：
 */
public class TileLayer extends TiledServiceLayer {

    TileCacheReader tileCacheReader = null;
    Envelope fullExtent;
    TileInfo tileInfo;
    SpatialReference spatialReference;

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

    public TileLayer(String LayersPath, TileCacheReader tileCacheReader) {
        super(LayersPath);
        try {
            this.tileCacheReader = tileCacheReader;
        } catch (Exception e) {
            this.tileCacheReader = null;
        }
        this.initLayer();
    }


    protected void initLayer() {
        if (getID() == 0) {
            this.nativeHandle = create();
        }
        if(tileCacheReader == null){
            super.initLayer();
            return;
        }
        fullExtent = new Envelope(tileCacheReader.getXmin(), tileCacheReader.getYmin(), tileCacheReader.getXmax(), tileCacheReader.getYmax());
        setFullExtent(fullExtent);
        tileInfo = new TileInfo(new Point(tileCacheReader.getTileOriginX(), tileCacheReader.getTileOriginY()),
                tileCacheReader.getScales(), tileCacheReader.getResolution(),
                tileCacheReader.getLevels(), tileCacheReader.getDpi(),
                tileCacheReader.getTileCols(), tileCacheReader.getTileRows());
        setTileInfo(tileInfo);
        if(tileCacheReader.getWkid() != 0){
            spatialReference = SpatialReference.create(tileCacheReader.getWkid());
        } else {
            spatialReference = SpatialReference.create(tileCacheReader.getWkt());
        }

        setDefaultSpatialReference(spatialReference);
        super.initLayer();
    }

    @Override
    public TileInfo getTileInfo() {
        return this.tileInfo;
    }

    @Override
    public Envelope getFullExtent() {
        return this.fullExtent;
    }

    @Override
    public SpatialReference getSpatialReference() {
        return this.spatialReference;
    }

    @Override
    protected byte[] getTile(int level, int col, int row) throws Exception {
        if(tileCacheReader != null){
            return tileCacheReader.getTile(level, col, row);
        }
        return null;
    }

}