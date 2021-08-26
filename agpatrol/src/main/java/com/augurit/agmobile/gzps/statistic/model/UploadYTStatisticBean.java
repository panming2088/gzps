package com.augurit.agmobile.gzps.statistic.model;

import java.util.List;

/**
 * @author : luob
 * @data : 2017-12-21
 * @des :
 */

public class UploadYTStatisticBean {
    /**
     * toDay : [{"corrCount":3,"name":"黄埔区水务局","lackCount":4}]
     * yestDay : [{"corrCount":3,"name":"黄埔区水务局","lackCount":4}]
     * success : true
     */

    private boolean success;
    private List<ToDayEntity> toDay;
    private List<YestDayEntity> yestDay;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setToDay(List<ToDayEntity> toDay) {
        this.toDay = toDay;
    }

    public void setYestDay(List<YestDayEntity> yestDay) {
        this.yestDay = yestDay;
    }

    public boolean getSuccess() {
        return success;
    }

    public List<ToDayEntity> getToDay() {
        return toDay;
    }

    public List<YestDayEntity> getYestDay() {
        return yestDay;
    }

    public static class ToDayEntity {
        /**
         * corrCount : 3
         * name : 黄埔区水务局
         * lackCount : 4
         */

        private int corrCount;
        private String name;
        private int lackCount;

        public void setCorrCount(int corrCount) {
            this.corrCount = corrCount;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setLackCount(int lackCount) {
            this.lackCount = lackCount;
        }

        public int getCorrCount() {
            return corrCount;
        }

        public String getName() {
            return name;
        }

        public int getLackCount() {
            return lackCount;
        }
    }

    public static class YestDayEntity {
        /**
         * corrCount : 3
         * name : 黄埔区水务局
         * lackCount : 4
         */

        private int corrCount;
        private String name;
        private int lackCount;

        public void setCorrCount(int corrCount) {
            this.corrCount = corrCount;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setLackCount(int lackCount) {
            this.lackCount = lackCount;
        }

        public int getCorrCount() {
            return corrCount;
        }

        public String getName() {
            return name;
        }

        public int getLackCount() {
            return lackCount;
        }
    }
}
