package com.augurit.agmobile.gzps.statistic.model;

import java.util.Arrays;

/**
 * Created by taoerxiang on 2017/11/20.
 */
public class EchartsBarBean {

    public String type1;
    public String title;
    public String imageUrl;
    public String[] times;
    public float[] data;


    @Override
    public String toString() {
        return "EchartsBarBean{" +
                "type1='" + type1 + '\'' +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", times=" + Arrays.toString(times) +
                ", data1=" + Arrays.toString(data) + '}';
    }
}
