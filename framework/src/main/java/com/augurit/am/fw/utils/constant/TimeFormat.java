package com.augurit.am.fw.utils.constant;

/**
 * Created by QiuYue on 2015-12-23.
 */
public interface TimeFormat {
    String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";//用横杠划分，直到秒
    String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";//用横杠划分，直到分
    String YYYY_MM_DD_HH = "yyyy-MM-dd HH";//用横杠划分，直到时
    String YYYY_MM_DD = "yyyy-MM-dd";//用横杠划分，直到日
    String YYYY_MM = "yyyy-MM";//用横杠划分，直到月
    String MM_DD_HH_MM = "MM-dd HH:mm";//无年份，用横杠划分，直到分
    String HH_MM = "HH:mm";//用冒号划分，只有时分
    String HH_MM_SS = "HH:mm:ss";//用冒号划分，只有时分秒

    String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";//无划分符号，直到秒
    String YYYYMMDDHHMM = "yyyyMMddHHmm";//无划分符号，直到分
    String YYYYMMDD = "yyyyMMdd";//无划分符号，直到日
    String YYYYMM = "yyyyMM";//无划分符号，直到月
    String MMDDHHMM = "MMddHHmm";//无年份，无划分符号，直到分
    String HHMM = "HHmm";//无划分符号，只有时分
    String HHMMSS = "HHmmss";//无划分符号，只有时分秒

    String YYYY年MM月DD日HH时MM分SS秒 = "yyyy年MM月dd日HH时mm分ss秒";//用中文划分，直到秒
    String YYYY年MM月DD日HH时MM分 = "yyyy年MM月dd日HH时mm分";//用中文划分，直到分
    String YYYY年MM月DD日 = "yyyy年MM月dd日";//用中文划分，直到日
    String YYYY年MM月 = "yyyy年MM月";//用中文符号，直到月
    String MM月DD日HH时MM分 = "MM月dd日HH时mm分";//无年份，用中文划分，直到分
    String HH时MM分 = "HH时mm分";//用中文划分，只有时分
    String HH时MM分SS秒 = "HH时mm分ss秒";//用中文划分，只有时分秒
}
