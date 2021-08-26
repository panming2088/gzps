package com.augurit.agmobile.gzps.common.facilityownership.dao;

import com.augurit.agmobile.gzps.common.facilityownership.model.FacilityOwnerShipUnit;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by xcl on 2017/11/27.
 */

public interface IFacilityOwnerShipUnitApi {

    @POST("rest/parts/toParentName")
    Observable<FacilityOwnerShipUnit> getFacilityOwnerShipUnit(@Query("loginName") String loginName);
}
