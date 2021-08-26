package com.augurit.agmobile.gzps.track.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.model
 * @createTime 创建时间 ：2017-08-14
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-14
 * @modifyMemo 修改备注：
 */

public class TrackConfig {

//    private String message;
//    private boolean success;
    @SerializedName("data")
    private TrackConfigResult result;

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    //    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public boolean isSuccess() {
//        return success;
//    }
//
//    public void setSuccess(boolean success) {
//        this.success = success;
//    }

    public TrackConfigResult getResult() {
        return result;
    }

    public void setResult(TrackConfigResult result) {
        this.result = result;
    }

    /**
     * "trackMinLength": "10",
     "trackMinPointAmount": "5",
     "trackMinTime": "1",
     "locateIntervalDis": "20",
     "locateIntervalTime": "5",
     "uploadIntervalTime": "10"
     */
    public class TrackConfigResult{

        private int trackMinLength;   //允许保存的轨迹最短路程，单位米
        private int trackMinPointAmount;   //允许保存的最少轨迹点数
        private int trackMinTime;   //允许保存的运动最短时间，单位分钟
        private int locateIntervalDis;   //定位距离间隔，单位米
        private int locateIntervalTime;  ///定位时间间隔，单位秒
        private int uploadIntervalTime;   //上传轨迹记录的间隔，单位分钟


        public int getLocateIntervalTime() {
            return locateIntervalTime;
        }

        public void setLocateIntervalTime(int locateIntervalTime) {
            this.locateIntervalTime = locateIntervalTime;
        }

        public int getLocateIntervalDis() {
            return locateIntervalDis;
        }

        public void setLocateIntervalDis(int locateIntervalDis) {
            this.locateIntervalDis = locateIntervalDis;
        }

        public int getUploadIntervalTime() {
            return uploadIntervalTime;
        }

        public void setUploadIntervalTime(int uploadIntervalTime) {
            this.uploadIntervalTime = uploadIntervalTime;
        }

        public int getTrackMinTime() {
            return trackMinTime;
        }

        public void setTrackMinTime(int trackMinTime) {
            this.trackMinTime = trackMinTime;
        }

        public int getTrackMinLength() {
            return trackMinLength;
        }

        public void setTrackMinLength(int trackMinLength) {
            this.trackMinLength = trackMinLength;
        }

        public int getTrackMinPointAmount() {
            return trackMinPointAmount;
        }

        public void setTrackMinPointAmount(int trackMinPointAmount) {
            this.trackMinPointAmount = trackMinPointAmount;
        }
    }
}
