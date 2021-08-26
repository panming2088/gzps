package com.augurit.agmobile.mapengine.common.utils.wktutil;

import com.augurit.agmobile.mapengine.common.utils.wktutil.model.Polygon;

import java.util.List;

/**
 * 描述：Geometry面积计算工具类(bs端的面积计算方法)
 *
 * @author 创建人 ：luobiao
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.utils.wktutil
 * @createTime 创建时间 ：2017-05-10
 * @modifyBy 修改人 ：luobiao
 * @modifyTime 修改时间 ：2017-05-10
 * @modifyMemo 修改备注：
 */

public class GeometryAreaUtil {

    /**
     * @param polygon 面对象
     * @return
     * @throws Exception
     */
    public static String calculatedGeoArea(Polygon polygon) throws Exception {
        List<List<Double[]>> rings1 = polygon.getRings();
        Double tempArea = 0d;
        for(int k = 0;k<rings1.size();k++){
            List<Double[]> doubles = rings1.get(k);
            Double x1 = 0d;
            Double x2 = 0d;
            Double y1 = 0d;
            Double y2 = 0d;
            for(int i = 0;i<doubles.size()-1;i++){
                x1 =doubles.get(i)[0];
                y1 =doubles.get(i)[1];
                x2 =doubles.get(i+1)[0];
                y2 =doubles.get(i+1)[1];
                tempArea += (x1 + x2) * (y1 - y2);
            }
        }
        return String.valueOf(Math.abs(tempArea)/2);
    }
}
