package com.augurit.agmobile.patrolcore.layer.model;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.map.model
 * @createTime 创建时间 ：2017-03-03
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-03
 * @modifyMemo 修改备注：
 */

public class GetLayerInfoResult {


    /**
     * msg :
     * result : {"addFlag":"2","id":"52f41d46-04dd-47b0-8bee-a3707959f549","isBaseMap":"1","layerTable":"nsXZQ","layerType":"021","name":"南沙行政区(AG)","paramId":"5d8251fe-5e76-434c-9e67-dea3420e5490","shortCut":"","url":"http://192.168.20.114:8081/nsXZQ"}
     * success : true
     */

    private String msg;
    private PatrolLayerInfo result;
    private boolean success;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PatrolLayerInfo getResult() {
        return result;
    }

    public void setResult(PatrolLayerInfo result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
