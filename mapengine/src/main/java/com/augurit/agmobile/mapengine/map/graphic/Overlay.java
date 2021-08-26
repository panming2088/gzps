package com.augurit.agmobile.mapengine.map.graphic;

/**
 * 地图覆盖物基类
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.map.graphic
 * @createTime 创建时间 ：2017-02-16
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-16
 * @modifyMemo 修改备注：
 */

public class Overlay {

    protected String mWkt;

    public String getWKT() {
        return mWkt;
    }

    public void setWKT(String wkt) {
        mWkt = wkt;
    }
}
