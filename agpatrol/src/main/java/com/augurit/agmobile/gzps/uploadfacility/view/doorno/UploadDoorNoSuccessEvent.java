package com.augurit.agmobile.gzps.uploadfacility.view.doorno;

import com.augurit.agmobile.gzps.uploadfacility.model.UploadDoorNoDetailBean;

/**
 * Created by xcl on 2017/12/2.
 */

public class UploadDoorNoSuccessEvent {

    private UploadDoorNoDetailBean uploadedFacility;

    public UploadDoorNoSuccessEvent(){

    }

    public UploadDoorNoSuccessEvent(UploadDoorNoDetailBean uploadedFacility) {
        this.uploadedFacility = uploadedFacility;
    }

    public UploadDoorNoDetailBean getUploadedFacility() {
        return uploadedFacility;
    }
}
