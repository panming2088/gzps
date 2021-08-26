package com.augurit.agmobile.patrolcore.common.table.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.util
 * @createTime 创建时间 ：17/4/19
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/4/19
 * @modifyMemo 修改备注：
 */

public class ValidateUtils {
    public static boolean isNumeric(String str){
        for(int i = 0; i< str.length(); i++){
            if(!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为正整数
     * +1, +2, +3……算作正整数
     * 0,1.0,2.0,3.0……不算做正整数
     *
     * @param str
     * @return true=>是;false=>不是
     */
    public static boolean isPositiveInteger(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        Pattern p = Pattern.compile("[1-9]\\d*");
        Pattern p1 = Pattern.compile("\\+[1-9]\\d*");
        Matcher m = p.matcher(str);
        Matcher m1 = p1.matcher(str);
        return m.matches() || m1.matches();
    }

    /**
     * 判断字符串是否为负整数
     * @param str
     */
    public static boolean isNegativeInteger(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        Pattern p = Pattern.compile("-[1-9]\\d*");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断字符串是否是整数
     * @param str
     */
    public static boolean isInteger(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        str = str.trim();
        Pattern p = Pattern.compile("((-|\\+)?[1-9]\\d*)|((-|\\+)?0)");
        Matcher m = p.matcher(str);
        boolean match = m.matches();
        return match;
    }

    /**
     * 判断字符串是否为正浮点型（Float or Double）数据，
     * 已采用  以下字符串做过Junit 测试
         // String str = "10.99";
         //  String str = "8P99";
         //   String str = "8P99.9P99";
         //   String str = "0.99";
         //   String str = "123.0";
         //   String str = "xxx.0";
         //   String str= "0";
         // String str= "8999P";
     * @param str
     */
    public static boolean isPositiveFloat(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        if (isPositiveInteger(str) || isNegativeInteger(str))
            return false;
//        Pattern p = Pattern.compile("[1-9]\\d*.\\d*|0.\\d*[0-9]\\d*");
//        Pattern p1 = Pattern.compile("\\+([1-9]\\d*.\\d*|0.\\d*[0-9]\\d*)");
//        Matcher m = p.matcher(str);
//        Matcher m1 = p1.matcher(str);
//        boolean match = m.matches() || m1.matches();
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        boolean match = pattern.matcher(str).matches();
        return match;
    }

    /**
     * 判断字符串是否为负浮点型（Float or Double）数据
     * @param str
     */
    public static boolean isNegativeFloat(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        if (isPositiveInteger(str) || isNegativeInteger(str))
            return false;
        Pattern p = Pattern.compile("-([1-9]\\d*.\\d*|0.\\d*[0-9]\\d*)");
        Matcher m = p.matcher(str);
        boolean match = m.matches();
        return match;
    }

    /**
     * 校验手机格式
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles){
        String regex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if(TextUtils.isEmpty(mobiles)){
            return false;
        }else {
            return mobiles.matches(regex);
        }
    }

    /**
     * 校验格式是否符合 固定电话号码
     * @param number
     * @return
     */
    public static boolean isTelPhoneNO(String number){
        String regex ="^(^0\\d{2}-?\\d{8}$)|(^0\\d{3}-?\\d{7}$)|(^0\\d2-?\\d{8}$)|(^0\\d3-?\\d{7}$)$";
        if(TextUtils.isEmpty(number)){
            return false;
        }else {
            return number.matches(regex);
        }
    }

    public static boolean isString(String str){
        return !(str == null || str.length() <= 0);
    }
}
