package com.augurit.agmobile.mapengine.gpsstrace.service;

import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.am.cmpt.common.Callback2;

import java.util.List;

/**
 * 本地GPS轨迹操作
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.gpstrace1.service
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public interface ILocalGpsTrackService {

    void saveTrackPoint(GPSTrack track);

    void saveTrackPoint(List<GPSTrack> tracks);

    List<GPSTrack> getGPSTracksByTrackId(String trackId);

    void getTodaysTrack(Callback2<GetTodayTrackResponse> callback);

    void getTrack(int pageNo, int pageSize, Callback2<GetTodayTrackResponse> callback);

    void clearGpsTrackBeyondToday();

     final class GetTodayTrackResponse{

         private List<List<GPSTrack>> mGPSTracks;
         private String mTotalTime ;//总时长
         private String mTotalLength; //总长度

         public GetTodayTrackResponse(List<List<GPSTrack>> tracks,String totalTime,String totalLength){
             this.mGPSTracks = tracks;
             this.mTotalLength = totalLength;
             this.mTotalTime = totalTime;
         }

         public String getTotalTime() {
             return mTotalTime;
         }

         public String getTotalLength() {
             return mTotalLength;
         }

         public List<List<GPSTrack>> getGPSTracks() {
             return mGPSTracks;
         }
     }
}
