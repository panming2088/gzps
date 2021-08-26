package com.augurit.agmobile.mapengine.layerquery;

/**
 * 无可查询图层
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerquery
 * @createTime 创建时间 ：2017-04-24
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-24
 * @modifyMemo 修改备注：
 */

public class NoQueryableLayerException extends Exception{

    public NoQueryableLayerException() {
    }

    public NoQueryableLayerException(String message) {
        super(message);
    }

    public NoQueryableLayerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoQueryableLayerException(Throwable cause) {
        super(cause);
    }

    public NoQueryableLayerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
