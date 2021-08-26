package com.augurit.agmobile.gzps.publicaffair.model;

import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;

import java.util.List;

/**
 * 设施上报详情
 * Created by xcl on 2017/11/17.
 */

public class UploadFacilityDetail {


    /**
     * code : 200
     * rows : [{"attName":"20171116125808_img.jpg","attPath":"http://139.159.243.185:8080/agsupport_swj/uploadFile/uploadFile/20171116/20171116125808_img.jpg","attTime":{"date":16,"day":4,"hours":12,"minutes":59,"month":10,"nanos":0,"seconds":19,"time":1510808359000,"timezoneOffset":-480,"year":117},"id":110,"markId":"169","mime":"image/*"}]
     * data : {"addr":"天河区瘦狗岭路595广州市水务局东北53米附近","addrLatest":"","attrFive":"","attrFour":"","attrOne":"","attrThree":"","attrTwo":"","coox":0,"cooy":0,"correctType":"设施不存在","description":"窨井缺失。","directOrgId":"","directOrgName":"","id":169,"layerId":6,"layerName":"窨井","layerUrl":"http://139.159.243.230:6080/arcgis/rest/services/GZPS/GZSWGX5000/MapServer/6","markPerson":"黄如冲","markPersonId":"huangruchong","markTime":{"date":16,"day":4,"hours":12,"minutes":59,"month":10,"nanos":0,"seconds":20,"time":1510808360000,"timezoneOffset":-480,"year":117},"objectId":"165567","parentOrgId":"1068","parentOrgName":"黄埔区水务局","road":"","superviseOrgId":"","superviseOrgName":"","teamOrgId":"","teamOrgName":"","updateTime":null,"x":113.33899791,"y":23.14861821}
     */

    private int code;
    private UploadedFacility data;
    private List<ServerAttachment.ServerAttachmentDataBean> rows;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UploadedFacility  getData() {
        return data;
    }

    public void setData(UploadedFacility  data) {
        this.data = data;
    }

    public List<ServerAttachment.ServerAttachmentDataBean> getRows() {
        return rows;
    }

    public void setRows(List<ServerAttachment.ServerAttachmentDataBean> rows) {
        this.rows = rows;
    }
}
