package com.augurit.agmobile.gzps.measure.model;

import com.augurit.agmobile.mapengine.measure.model.IMeasureResult;

/**
 * 米
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.meas
 * @createTime 创建时间 ：2017-01-04
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-04
 */

public class MeterResult implements IMeasureResult {
    @Override
    public String getResult(double result) {

        return String.format("%.2f", Double.valueOf(result)) + " 米";
    }
}
