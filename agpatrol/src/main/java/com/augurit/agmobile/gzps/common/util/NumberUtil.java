package com.augurit.agmobile.gzps.common.util;

import java.util.regex.Pattern;

public class NumberUtil {
    public static boolean isNumber(String str) {

        boolean isInt = Pattern.compile("^-?[1-9]\\d*$").matcher(str).find();

        boolean isDouble = Pattern.compile("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$").matcher(str).find();

        return isInt || isDouble;

    }
}
