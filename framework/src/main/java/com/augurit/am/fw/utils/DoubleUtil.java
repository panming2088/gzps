package com.augurit.am.fw.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.fw.utils
 * @createTime 创建时间 ：2016-10-18 10:36
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2016-10-18 10:36
 */
public final class DoubleUtil {
    private DoubleUtil() {
    }

    public static String formatDoubleToString(Double d) {
        DecimalFormat format = new DecimalFormat("###.###");
        return format.format(d);
    }

    /**
     * 格式化数值<br>
     * 可避免出现科学计数法格式，同时确保小数精度
     * @param number 数字
     * @return 格式化的字符串
     */
    public static String formatDecimal(float number) {
        return formatDecimal(String.valueOf(number), new BigDecimal(number));
    }

    /**
     * 格式化数值<br>
     * 可避免出现科学计数法格式，同时确保小数精度
     * @param number 数字
     * @return 格式化的字符串
     */
    public static String formatDecimal(double number) {
        return formatDecimal(String.valueOf(number), new BigDecimal(number));
    }

    private static String formatDecimal(String strNumber, BigDecimal bigDecimal) {
        String decimalPattern = "##";
        Pattern p = Pattern.compile("\\d+\\.(\\d+)$");  // 匹配非科学计数法小数情况
        Matcher m = p.matcher(strNumber);
        if (m.matches()) {
            String g1 = m.group(1);
            g1 = g1.replaceAll(".", "#");   // 所有小数位数字换为"#"
            decimalPattern = decimalPattern.concat(".").concat(g1);
        } else {
            Pattern p2 = Pattern.compile("\\d+\\.(\\d+)[E|e]-(\\d+)$");  // 匹配科学计数法E-n情况
            Matcher m2 = p2.matcher(strNumber);
            if (m2.matches()) {
                String d, e;
                d = m2.group(1);    // 第一组小数位
                e = m2.group(2);    // 第二组E指数位
                decimalPattern = decimalPattern.concat(".");
                for (int i = 0; i < Integer.parseInt(e); i ++) {
                    decimalPattern = decimalPattern.concat("#");
                }
                d = d.replaceAll(".", "#");
                decimalPattern = decimalPattern.concat(d);
            }
            else {
                Pattern p3 = Pattern.compile("\\d+\\.(\\d+)[E|e]\\+?(\\d+)$");  // 匹配科学计数法E+n情况
                Matcher m3 = p3.matcher(strNumber);
                if (m3.matches()) {
                    String d, e;
                    d = m3.group(1);
                    e = m3.group(2);
                    int digits = d.length() - Integer.parseInt(e);
                    if (digits > 0) {   // 小数位多于指数，需指定转换后的小数位
                        decimalPattern = decimalPattern.concat(".");
                        for (int i = 0; i < digits; i++) {
                            decimalPattern = decimalPattern.concat("#");
                        }
                    }
                }
            }
        }   // 整数情况则直接转换
        DecimalFormat format = new DecimalFormat(decimalPattern);
        StringBuffer sb = new StringBuffer();
        format.format(bigDecimal, sb, new FieldPosition(0));
        return sb.toString();
    }
}
