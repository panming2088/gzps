package com.augurit.agmobile.gzps.doorno.dao;


import com.augurit.agmobile.gzps.doorno.model.WellPhoto;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * com.augurit.agmobile.gzpssb.seweragewell.dao
 * Created by sdb on 2018/4/21  10:56.
 * Desc：
 */

public interface SewerageWellApi {

    @POST("rest/discharge/getImgsByObjId")
    Observable<WellPhoto> getWellPhotos(@Query("objectId") String objectId);

}
