package com.augurit.agmobile.gzps.statistic.model;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author : luob
 * @data : 2017-12-21
 * @des :
 */

public class UploadStatisticBean {

    /**
     * total : 51
     * charts : [{name:"从化区水务局",total:51},{"黄埔区水务局":54},{"南沙区环保水务局":5},{"白云区水务局":1},{"海珠区住房和建设水务局":22},{"增城区水务局":2},{"荔湾区水务和农业局":2},{"越秀区建设和水务局":12},{"广州市净水有限公司":7},{"番禺区水务局":4},{"天河区住房和建设水务局":126},{"花都区水务局":24}]
     * corrCount : 5
     * lackCount : 46
     * "passCorrCount": 10,
     "passLackCount": 6,
     "passTotal": 16,
     */
    private int passLackCount;
    private int passCorrCount;
    private int passTotal;
    private int total;
    private int corrCount;
    private int lackCount;
    private int doubtTotal;
    private int doubtCorrCount;
    private int doubtLackCount;
    private List<ChartsEntity> charts;

    public void setTotal(int total) {
        this.total = total;
    }

    public void setCorrCount(int corrCount) {
        this.corrCount = corrCount;
    }

    public void setLackCount(int lackCount) {
        this.lackCount = lackCount;
    }

    public void setCharts(List<ChartsEntity> charts) {
        this.charts = charts;
    }

    public int getTotal() {
        return total;
    }

    public int getCorrCount() {
        return corrCount;
    }

    public int getLackCount() {
        return lackCount;
    }

    public int getDoubtTotal() {
        return doubtTotal;
    }

    public void setDoubtTotal(int doubtTotal) {
        this.doubtTotal = doubtTotal;
    }

    public int getDoubtCorrCount() {
        return doubtCorrCount;
    }

    public void setDoubtCorrCount(int doubtCorrCount) {
        this.doubtCorrCount = doubtCorrCount;
    }

    public int getDoubtLackCount() {
        return doubtLackCount;
    }

    public void setDoubtLackCount(int doubtLackCount) {
        this.doubtLackCount = doubtLackCount;
    }

    public List<ChartsEntity> getCharts() {
        return charts;
    }

    public int getPassLackCount() {
        return passLackCount;
    }

    public void setPassLackCount(int passLackCount) {
        this.passLackCount = passLackCount;
    }

    public int getPassCorrCount() {
        return passCorrCount;
    }

    public void setPassCorrCount(int passCorrCount) {
        this.passCorrCount = passCorrCount;
    }

    public int getPassTotal() {
        return passTotal;
    }

    public void setPassTotal(int passTotal) {
        this.passTotal = passTotal;
    }

    public String getTotalStr() {
        return getString(total);
    }

    public String getCorrCountStr() {
        return getString(corrCount);
    }

    public String getLackCountStr() {
        return getString(lackCount);
    }

    public String getDoubtTotalStr() {
        return getString(doubtTotal);
    }

    public String getDoubtCorrCountStr() {
        return getString(doubtCorrCount);
    }

    public String getDoubtLackCountStr() {
        return getString(doubtLackCount);
    }

    public String getPassTotalStr(){
        return getString(passTotal);
    }

    public String getPassCorrCountStr(){
        return getString(passCorrCount);
    }
    public String getPassLackCountStr(){
        return getString(passLackCount);
    }

    private String getString(double n) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(n);
    }

    public static class ChartsEntity {
        /**
         * 从化区水务局 : 51
         */
        private String name;
        private int total;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
