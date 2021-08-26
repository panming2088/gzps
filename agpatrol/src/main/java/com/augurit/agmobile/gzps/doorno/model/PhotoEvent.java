package com.augurit.agmobile.gzps.doorno.model;

import android.content.Intent;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.event
 * @createTime 创建时间 ：2018-04-11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-04-11
 * @modifyMemo 修改备注：
 */

public class PhotoEvent {
    private int resultCode;
    private Intent data;
    private int requestCode;

    public int getResultCode() {
        return resultCode;
    }

    public Intent getData() {
        return data;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public PhotoEvent(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.data = data;
        this.resultCode = resultCode;
    }
}
