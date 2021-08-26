package com.augurit.agmobile.gzps.statistic.model;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by taoerxiang on 2017/11/20.
 */
public class EchartsDataBean {

    private static Gson gson;

    private static EchartsBarBean barBean;
    private static EchartsPieBean pieBean;
    private static EchartsDataBean echartsDataBean;

    private EchartsDataBean() {
    }

    public synchronized static EchartsDataBean getInstance() {
        if (echartsDataBean == null) {
            echartsDataBean = new EchartsDataBean();
            gson = new Gson();
            barBean = new EchartsBarBean();
            pieBean = new EchartsPieBean();
        }
        return echartsDataBean;
    }

    public String getEchartsBarJson(String[] yAxle, String title, float[] xAxle) {
        String[] reverseY = reverse(yAxle);
        float[] reverseX = reversef(xAxle);
        barBean.title = title;
        barBean.type1 = "安装率";
        barBean.times = reverseY;
        barBean.data = reverseX;
        return gson.toJson(barBean);
    }

    public String getEchartsPieJson(float percent, String title) {
        pieBean.title = title + "排水APP安装率统计";
        pieBean.type = "pie";
        pieBean.values = new ArrayList<>();
        EchartsPieBean.ValueData value;
        value = new EchartsPieBean.ValueData();
        value.value = percent;
        pieBean.values.add(value);
        return gson.toJson(pieBean);
    }
    public String[] reverse(String[] array){
        int length = array.length;
        String[] re = new String[length];
        for(int i=0; i<length; i++){
            re[length-i-1] = array[i];
        }
        return re;
    }
    public float[] reversef(float[] array){
        int length = array.length;
        float[] re = new float[length];
        for(int i=0; i<length; i++){
            re[length-i-1] = array[i];
        }
        return re;
    }
}
