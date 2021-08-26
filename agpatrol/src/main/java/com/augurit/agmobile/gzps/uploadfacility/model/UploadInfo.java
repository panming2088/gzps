package com.augurit.agmobile.gzps.uploadfacility.model;

/**
 * Created by xcl on 2017/12/7.
 */

public class UploadInfo {

    private String objectId;
    private String reportType;
    private String markId;
    private ModifiedFacility modifiedFacilities;
    private UploadedFacility uploadedFacilities;
    private CompleteTableInfo completeTableInfo;

    public CompleteTableInfo getCompleteTableInfo() {
        return completeTableInfo;
    }

    public void setCompleteTableInfo(CompleteTableInfo completeTableInfo) {
        this.completeTableInfo = completeTableInfo;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public UploadedFacility getUploadedFacilities() {
        return uploadedFacilities;
    }

    public void setUploadedFacilities(UploadedFacility uploadedFacilities) {
        this.uploadedFacilities = uploadedFacilities;
    }

    public ModifiedFacility getModifiedFacilities() {
        return modifiedFacilities;
    }

    public void setModifiedFacilities(ModifiedFacility modifiedFacilities) {
        this.modifiedFacilities = modifiedFacilities;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getMarkId() {
        return markId;
    }

    public void setMarkId(String markId) {
        this.markId = markId;
    }
}
