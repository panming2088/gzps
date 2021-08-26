package com.augurit.am.fw.utils;

import java.text.DecimalFormat;

/**
 * Created by ac on 2016-07-26.
 */
public final class StringUtil {
    private StringUtil() {
    }

    /**
     * To judge a string whether empty or not.
     *
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        return string == null || "".equals(string.trim());
    }

    public static boolean isEmpty(Object string) {
        return string == null || "".equals(string.toString().trim());
    }

    // public static String getNotNullString(String string, String
    // defaultString) {
    // return string == null ? defaultString : string;
    // }

    /**
     * To get a string format of an object,if it's null ,return defaultString.
     *
     * @param stringObject
     * @param defaultString
     * @return
     */
    public static String getNotNullString(Object stringObject,
                                          String defaultString) {
        return stringObject == null||"null".equals(stringObject.toString().trim()) ? defaultString : stringObject.toString();
    }

    // public static String replaceNullString(String string) {
    //
    // return string.replace("null", "");
    // }

    /**
     * To compare two strings whether they are equal or not.
     *
     * @param firstString
     * @param secondString
     * @return
     */
    public static boolean isTwoStringEqual(String firstString,
                                           String secondString) {
        if (firstString == null) {
            return secondString == null;
        } else {
            return firstString.equals(secondString);
        }
    }

    /**
     * To covert String to Long.
     *
     * @param string
     * @return
     */
    public static Long getLong(String string) {
        return getLong(string, null);
    }

    public static Long getLong(String string, Long defaultValue) {
        if (string == null || "".equals(string.trim()))
            return defaultValue;
        try {
            return Long.parseLong(string.trim());
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * To covert String to Float.
     *
     * @param string
     * @return
     */
    public static Float getFloat(String string) {
        return getFloat(string, null);
    }

    public static Float getFloat(String string, Float defaultValue) {
        if (string == null || "".equals(string.trim()))
            return defaultValue;
        try {
            return Float.parseFloat(string.trim());
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * To covert String to Double.
     *
     * @param string
     * @return
     */
    public static Double getDouble(String string) {
        return getDouble(string, null);
    }

    public static Double getDouble(String string, Double defaultValue) {
        if (string == null || "".equals(string.trim()))
            return defaultValue;
        try {
            return Double.parseDouble(string.trim());
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }


    /**
     * double 转 String
     *
     * @param value
     * @return
     */
    public static String valueOf(double value) {
        DecimalFormat df = new DecimalFormat("###############0.0###############");
        String str = df.format(value);
        return str;
    }

    /**
     * double 转 String
     *
     * @param value
     * @param w   要保留的小数位数
     * @return
     */
    public static String valueOf(double value, int w) {
        String pattern = "###############0.0";
        for(int i = 0; i<w; i++){
            pattern += "#";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        String str = df.format(value);
        return str;
    }

    /**
     * To get strings from an array.
     *
     * @param strings
     * @return
     */
    public static String getString(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    /**
     * To get stringbuilder from an array.
     *
     * @param strings
     * @return
     */
    public static StringBuilder getStringBuilder(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string);
        }
        return stringBuilder;
    }

    /**
     * Using Java regular expressions to get rid of redundant "." and "0" at the
     * end,like "1001.0"
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");// Get rid of redundant"0"
            s = s.replaceAll("[.]$", "");// If the last one is".", remove it.
        }
        return s;
    }


    /**
     * bytes字符串转换为Byte值
     * @param  src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src)
    {
        return src.getBytes();
    }

    /**
     * bytes转换成十六进制字符串
     * @param  b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b)
    {
        return new String(b);
    }

}
