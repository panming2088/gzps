package com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist;

/**
 * 上报设施总数（纠错+ 新增）
 * Created by xucil on 2018/1/11.
 */

public class UploadFacilitySumEvent {
    public UploadFacilitySumEvent() {
    }

    public UploadFacilitySumEvent(int sum) {
        this.sum = sum;
    }

    private int fAdd;
    private int fChecked;
    private int pipeAdd;
    private int pipeChecked;
    private int pipeDelete;
    private int keynode;
    /**
     * 1、管井新增
     * 2、管井校核
     * 3、管线新增
     * 4、管线校核
     * 5、管线删除？
     * 6、关键点监测
     */
    private int status;
    private int sum;

    public int getfAdd() {
        return fAdd;
    }

    public void setfAdd(int fAdd) {
        this.fAdd = fAdd;
    }

    public int getfChecked() {
        return fChecked;
    }

    public void setfChecked(int fChecked) {
        this.fChecked = fChecked;
    }

    public int getPipeAdd() {
        return pipeAdd;
    }

    public void setPipeAdd(int pipeAdd) {
        this.pipeAdd = pipeAdd;
    }

    public int getPipeChecked() {
        return pipeChecked;
    }

    public void setPipeChecked(int pipeChecked) {
        this.pipeChecked = pipeChecked;
    }

    public int getPipeDelete() {
        return pipeDelete;
    }

    public void setPipeDelete(int pipeDelete) {
        this.pipeDelete = pipeDelete;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getKeynode() {
        return keynode;
    }

    public void setKeynode(int keynode) {
        this.keynode = keynode;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
