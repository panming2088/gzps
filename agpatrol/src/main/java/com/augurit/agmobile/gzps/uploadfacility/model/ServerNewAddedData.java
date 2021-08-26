package com.augurit.agmobile.gzps.uploadfacility.model;

import com.augurit.agmobile.gzps.common.model.ServerAttachment;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.upload.model
 * @createTime 创建时间 ：17/12/11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/11
 * @modifyMemo 修改备注：
 */

public class ServerNewAddedData {


    /**
     * code : 200
     * form : {"markPerson":"刘志成","superviseOrgId":"1080","checkPerson":null,"parentOrgName":"黄埔区水务局","layerName":"窨井","addr":"瘦狗岭路","checkTime":null,"road":"瘦狗岭路","attrFour":"天河区住房和建设水务局","id":"301","directOrgName":"广州市黄埔区市政建设有限公司","isBinding":"1","objectId":"7188","description":"hjjhshzhzhzjajshzgxgxbxgxkshzbxmcmz","teamOrgName":"黄埔片区","checkState":"1","checkPersonId":null,"attrOne":"检查井","isAddFeature":"1","markPersonId":"13428893966","attrTwo":"雨污合流","updateTime":null,"teamOrgId":"1320","parentOrgId":"1068","directOrgId":"1317","attrFive":"就说句话说自己傻婚纱照","checkDesription":null,"layerId":5,"componentType":"窨井","attrThree":"铸铁","markTime":1512464195000,"y":23.14822519,"usid":"060209-28420415-000066","x":113.33874166,"superviseOrgName":"黄埔区水务设施管理所"}
     * rows : [{"id":"363","thumPath":"http://139.159.243.185:8080/img/ReportFile/imgSmall/201712/jate4s7ja_20171205165530_img.jpg","markId":"301","attPath":"http://139.159.243.185:8080/img/ReportFile/img/201712/jate4s7ja_20171205165530_img.jpg","attTime":1512464198000,"attName":"20171205165530_img.jpg","mime":"image/*"},{"id":"364","thumPath":"http://139.159.243.185:8080/img/ReportFile/imgSmall/201712/jate4sdob_20171205165525_img.jpg","markId":"301","attPath":"http://139.159.243.185:8080/img/ReportFile/img/201712/jate4sdob_20171205165525_img.jpg","attTime":1512464198000,"attName":"20171205165525_img.jpg","mime":"image/*"},{"id":"361","thumPath":"http://139.159.243.185:8080/img/ReportFile/imgSmall/201712/jate4rw38_20171205165538_img.jpg","markId":"301","attPath":"http://139.159.243.185:8080/img/ReportFile/img/201712/jate4rw38_20171205165538_img.jpg","attTime":1512464197000,"attName":"20171205165538_img.jpg","mime":"image/*"},{"id":"362","thumPath":"http://139.159.243.185:8080/img/ReportFile/imgSmall/201712/jate4s1n9_20171205165542_img.jpg","markId":"301","attPath":"http://139.159.243.185:8080/img/ReportFile/img/201712/jate4s1n9_20171205165542_img.jpg","attTime":1512464197000,"attName":"20171205165542_img.jpg","mime":"image/*"}]
     */

    private int code;
    private UploadedFacility form;
    private List<ServerAttachment.ServerAttachmentDataBean> rows;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UploadedFacility getForm() {
        return form;
    }

    public void setForm(UploadedFacility form) {
        this.form = form;
    }

    public List<ServerAttachment.ServerAttachmentDataBean> getRows() {
        return rows;
    }

    public void setRows(List<ServerAttachment.ServerAttachmentDataBean> rows) {
        this.rows = rows;
    }

}
