package com.augurit.agmobile.mapengine.addrsearch.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.addrq.util
 * @createTime 创建时间 ：2017-01-19
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-19
 */

public class AddressSearchUtil {

    public static String filterSpecialCharacter(String string){
        // 清除掉所有特殊字符
        String regEx="[·`~!@#$%^&*+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(string);
        String trim = m.replaceAll("").trim();
        return trim.replace(" ","");
    }
}
