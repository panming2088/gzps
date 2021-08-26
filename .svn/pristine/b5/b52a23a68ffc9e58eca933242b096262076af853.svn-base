package com.augurit.agmobile.gzps.track.util;

import java.text.DecimalFormat;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.util
 * @createTime 创建时间 ：2017-06-20
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-20
 * @modifyMemo 修改备注：
 */

public class LengthUtil {

    public static String formatLength(double length){
        if(length < 1000){
            return (int)length + "米";
        } else {
            DecimalFormat df = new DecimalFormat("#.00");
            return df.format(length/1000) + "公里";
        }
    }
}
