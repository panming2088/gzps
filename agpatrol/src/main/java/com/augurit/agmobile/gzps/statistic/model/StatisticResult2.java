package com.augurit.agmobile.gzps.statistic.model;


import com.esri.core.geometry.Geometry;
import com.esri.core.symbol.Symbol;

import java.util.Map;

/**
 *
 * 统计结果
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.statistic.model
 * @createTime 创建时间 ：17/11/2
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/2
 * @modifyMemo 修改备注：
 */

public class StatisticResult2 {

    private Map<String,Object> attrs;

    private long id;

    private String layerUrl;

    private Symbol symbol;

    private Geometry mGeometry;

    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLayerUrl() {
        return layerUrl;
    }

    public void setLayerUrl(String layerUrl) {
        this.layerUrl = layerUrl;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public Geometry getGeometry() {
        return mGeometry;
    }

    public void setGeometry(Geometry mGeometry) {
        this.mGeometry = mGeometry;
    }
}
